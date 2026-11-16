package org.legstar.cobol.converter;

import java.io.ByteArrayOutputStream;
import org.legstar.cobol.model.Copybook;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import org.legstar.cobol.annotation.*;
import org.legstar.cobol.model.Copybook;
import org.legstar.cobol.io.CobolOutputStream;

/** Maps a COBOL DTO to the raw buffer supplied to ECI (without a record header).
 * Configure the instance before use; do not mutate its configuration concurrently.
 */
public final class MainframeMapper {
    private final CobolConverterConfig config;
    private final CobolClassInfo classInfo = new CobolClassInfoReflect();

    public MainframeMapper() {
        this.config = null;
    }

    public MainframeMapper(CobolConverterConfig config) {
        this.config = Objects.requireNonNull(config, "config");
    }

    private CobolConverterConfig configFor(Class<?> type) {
        if (config != null) return config;
        var encoding = type.getAnnotation(CobolEncoding.class);
        if (encoding == null) return CobolBeanConverterConfig.ebcdic();
        return new CobolBeanConverterConfig(encoding.hostCharsetName())
                .setRightPadCobolAlphanumWithSpaces(encoding.rightPadCobolAlphanumWithSpaces())
                .setTruncateHostStringsTrailingSpaces(encoding.truncateHostStringsTrailingSpaces());
    }

    public byte[] encode(Copybook dto) throws IOException {
        Objects.requireNonNull(dto, "dto");
        validate(dto, dto.getClass().getSimpleName(), new HashMap<>(),
                Collections.newSetFromMap(new IdentityHashMap<>()));
        var buffer = new ByteArrayOutputStream();
        try (var output = new CobolOutputStream(buffer)) {
            new CobolBeanSerializer(new CobolPrimitiveConverter(configFor(dto.getClass())), classInfo).serialize(output, dto);
        }
        return buffer.toByteArray();
    }

    /** Updates the supplied DTO in place. On any exception discard the DTO:
     * earlier fields may already have been updated. Setter failures retain their cause.
     * @throws IllegalArgumentException for invalid data, layout or setter failures
     */
    public void decodeInto(byte[] output, Copybook dto) {
        Objects.requireNonNull(output, "output");
        Objects.requireNonNull(dto, "dto");
        var input = new java.io.ByteArrayInputStream(output);
        readGroup(input, dto, dto.getClass().getSimpleName(), new HashMap<>(),
                new CobolPrimitiveConverter(configFor(dto.getClass())));
        if (input.available() != 0) {
            throw invalid(dto.getClass().getSimpleName(), input.available() + " unexpected trailing bytes");
        }
    }

    private void readGroup(java.io.ByteArrayInputStream input, Object bean, String path,
            Map<String, BigDecimal> counters, CobolPrimitiveConverter converter) {
        if (!(classInfo.getCobolItemType(bean.getClass()) instanceof CobolGroup)) {
            throw invalid(path, "expected COBOL group; REDEFINES is deferred");
        }
        for (var field : classInfo.fieldInfos(bean.getClass())) {
            String fieldPath = path + "." + field.name();
            try {
                Object value;
                var array = field.cobolArray();
                if (array != null) {
                    int length = array.maxOccurs();
                    if (!array.dependingOn().isBlank()) {
                        var count = counters.get(array.dependingOn());
                        if (count == null) throw invalid(fieldPath, "missing counter " + array.dependingOn());
                        length = count.intValueExact();
                    }
                    if (length < array.minOccurs() || length > array.maxOccurs()) {
                        throw invalid(fieldPath, "OCCURS count outside allowed range: " + length);
                    }
                    Class<?> type = field.javaType().getComponentType();
                    value = Array.newInstance(type, length);
                    // Assign through the setter after all array elements are decoded.
                    for (int i = 0; i < length; i++) {
                        Array.set(value, i, readItem(input, field.cobolItemType(), type, null,
                                fieldPath + "[" + i + "]", counters, converter));
                    }
                } else {
                    Object existing = field.cobolItemType() instanceof CobolGroup
                            ? field.getter().invoke(bean) : null;
                    value = readItem(input, field.cobolItemType(), field.javaType(), existing,
                            fieldPath, counters, converter);
                }
                field.setter().invoke(bean, value);
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw new IllegalArgumentException(fieldPath + ": accessor rejected value", e.getCause());
            } catch (ReflectiveOperationException | RuntimeException e) {
                throw new IllegalArgumentException(fieldPath + ": " + e.getMessage(), e);
            }
        }
    }

    private Object readItem(java.io.ByteArrayInputStream input, java.lang.annotation.Annotation item,
            Class<?> type, Object existing, String path, Map<String, BigDecimal> counters,
            CobolPrimitiveConverter converter) {
        try {
            if (item instanceof CobolGroup) {
                Object group = existing == null ? classInfo.newInstance(type) : existing;
                readGroup(input, group, path, counters, converter);
                return group;
            }
            int size;
            if (item instanceof CobolString n) size = n.charNum();
            else if (item instanceof CobolBinaryNumber n) size = org.legstar.cobol.utils.BytesLenUtils.binaryNumberByteLen(n.totalDigits());
            else if (item instanceof CobolPackedDecimal n) size = org.legstar.cobol.utils.BytesLenUtils.packedDecimalByteLen(n.totalDigits());
            else if (item instanceof CobolZonedDecimal n) size = org.legstar.cobol.utils.BytesLenUtils.zonedDecimalByteLen(n.totalDigits(), n.signSeparate());
            else if (item instanceof CobolFloat) size = 4;
            else if (item instanceof CobolDouble) size = 8;
            else throw invalid(path, "unsupported COBOL type; REDEFINES is deferred");
            if (input.available() < size) throw invalid(path, "truncated buffer: requires " + size + " bytes, remaining " + input.available());
            Object value;
            String counter = null;
            if (item instanceof CobolString n) value = converter.toJava(input, n, type);
            else if (item instanceof CobolBinaryNumber n) {
                value = converter.toJava(input, n, type);
                if (n.odoObject()) counter = n.cobolName();
            } else if (item instanceof CobolPackedDecimal n) {
                value = converter.toJava(input, n, type);
                if (n.odoObject()) counter = n.cobolName();
            } else if (item instanceof CobolZonedDecimal n) {
                value = converter.toJava(input, n, type);
                if (n.odoObject()) counter = n.cobolName();
            } else if (item instanceof CobolFloat n) value = converter.toJava(input, n, type);
            else value = converter.toJava(input, (CobolDouble) item, type);
            if (counter != null) counters.put(counter, new BigDecimal(value.toString()));
            return value;
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(path + ": " + e.getMessage(), e);
        }
    }

    private void validate(Object bean, String path, Map<String, BigDecimal> inherited, Set<Object> active) {
        if (bean == null) throw invalid(path, "initialize the group before encoding");
        if (!(classInfo.getCobolItemType(bean.getClass()) instanceof CobolGroup)) {
            throw invalid(path, "only COBOL groups are supported; REDEFINES is deferred");
        }
        if (!active.add(bean)) throw invalid(path, "cyclic group reference");
        var counters = inherited;
        try {
            for (var field : classInfo.fieldInfos(bean.getClass())) {
                String fieldPath = path + "." + field.name();
                Object value;
                try {
                    value = field.getter().invoke(bean);
                } catch (ReflectiveOperationException e) {
                    throw new IllegalArgumentException(fieldPath + ": getter failed", e);
                }
                var item = field.cobolItemType();
                if (item instanceof CobolChoice) throw invalid(fieldPath, "REDEFINES is deferred");
                var array = field.cobolArray();
                if (array != null) {
                    // Require explicit arrays so the actual ODO length is unambiguous.
                    if (value == null) throw invalid(fieldPath, "initialize the OCCURS array before encoding");
                    int length = Array.getLength(value);
                    boolean variable = !array.dependingOn().isBlank();
                    int min = variable ? array.minOccurs() : array.maxOccurs();
                    if (length < min || length > array.maxOccurs()) {
                        throw invalid(fieldPath, "array length " + length + " outside " + min + ".." + array.maxOccurs());
                    }
                    if (variable) {
                        BigDecimal count = counters.get(array.dependingOn());
                        if (count == null || count.compareTo(BigDecimal.valueOf(length)) != 0) {
                            throw invalid(fieldPath, "length " + length + " differs from " + array.dependingOn() + "=" + count);
                        }
                    }
                    for (int i = 0; i < length; i++) {
                        Object element = Array.get(value, i);
                        String elementPath = fieldPath + "[" + i + "]";
                        if (element == null) throw invalid(elementPath, "null OCCURS element");
                        if (item instanceof CobolGroup) validate(element, elementPath, counters, active);
                    }
                } else if (item instanceof CobolGroup) {
                    validate(value, fieldPath, counters, active);
                } else {
                    String name = null;
                    if (item instanceof CobolBinaryNumber n && n.odoObject()) name = n.cobolName();
                    if (item instanceof CobolPackedDecimal n && n.odoObject()) name = n.cobolName();
                    if (item instanceof CobolZonedDecimal n && n.odoObject()) name = n.cobolName();
                    if (name != null) counters.put(name, value == null ? BigDecimal.ZERO : new BigDecimal(value.toString()));
                }
            }
        } finally {
            active.remove(bean);
        }
    }

    private IllegalArgumentException invalid(String path, String message) {
        return new IllegalArgumentException(path + ": " + message);
    }
}
