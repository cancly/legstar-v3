//package org.legstar.cobol.annotation;
//
//import java.lang.reflect.Array;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@CobolGroup(cobolName = "ALLTYPES")
//public class Alltypes {
//
//    @CobolString(cobolName = "S-STRING", charNum = 4, order = 1)
//    private String s_String;
//
//    @CobolString(cobolName = "S-BINARY", charNum = 4, order =2)
//    private String s_Binary;
//
//    @CobolBinaryNumber(cobolName = "S-SHORT", signed = true, totalDigits = 4, order =3)
//    private Short s_Short;
//
//    @CobolBinaryNumber(cobolName = "S-USHORT", totalDigits = 4, order =4)
//    private Short s_Ushort;
//
//    @CobolBinaryNumber(cobolName = "S-INT", signed = true, totalDigits = 9, order =5)
//    private Integer s_Int;
//
//    @CobolBinaryNumber(cobolName = "S-UINT", totalDigits = 9, order =6)
//    private Integer s_Uint;
//
//    @CobolBinaryNumber(cobolName = "S-LONG", signed = true, totalDigits = 18, order =7)
//    private Long s_Long;
//
//    @CobolBinaryNumber(cobolName = "S-ULONG", totalDigits = 18, order =8)
//    private Long s_Ulong;
//
//    @CobolPackedDecimal(cobolName = "S-PDECIMAL", totalDigits = 9, fractionDigits = 2, order =9)
//    private BigDecimal s_Pdecimal;
//
//    @CobolFloat(cobolName = "S-FLOAT", order =10)
//    private Float s_Float;
//
//    @CobolDouble(cobolName = "S-DOUBLE", order =11)
//    private Double s_Double;
//
//    @CobolZonedDecimal(cobolName = "S-ZONED", totalDigits = 5, fractionDigits = 2, order =12)
//    private BigDecimal s_Zoned;
//
//    @CobolZonedDecimal(cobolName = "S-UZONED", totalDigits = 5, fractionDigits = 2, order =13)
//    private BigDecimal s_Uzoned;
//
//    @CobolZonedDecimal(cobolName = "S-ZONED-SL", totalDigits = 3, signLeading = true, order =13)
//    private BigDecimal s_ZonedSl;
//
//    @CobolZonedDecimal(cobolName = "S-ZONED-ST", totalDigits = 2, order =14)
//    private BigDecimal s_ZonedSt;
//
//    @CobolZonedDecimal(cobolName = "S-ZONED-SLS", totalDigits = 1, signLeading = true, signSeparate = true, order =15)
//    private BigDecimal s_ZonedSls;
//
//    @CobolZonedDecimal(cobolName = "S-ZONED-STS", totalDigits = 2, signSeparate = true, order =16)
//    private BigDecimal s_ZonedSts;
//
//    @CobolZonedDecimal(cobolName = "S-UZONED-BWZ", totalDigits = 5, fractionDigits = 2, blankWhenZero = true, order =17)
//    private BigDecimal s_UzonedBwz;
//
//    public String getS_String() {
//        return s_String;
//    }
//
//    public void setS_String(String s_String) {
//        validateStringLength("s_String", s_String, 4);
//        this.s_String = s_String;
//    }
//
//    public String getS_Binary() {
//        return s_Binary;
//    }
//
//    public void setS_Binary(String s_Binary) {
//        validateStringLength("s_Binary", s_Binary, 4);
//        this.s_Binary = s_Binary;
//    }
//
//    public Short getS_Short() {
//        return s_Short;
//    }
//
//    public void setS_Short(Short s_Short) {
//        this.s_Short = s_Short;
//    }
//
//    public Short getS_Ushort() {
//        return s_Ushort;
//    }
//
//    public void setS_Ushort(Short s_Ushort) {
//        this.s_Ushort = s_Ushort;
//    }
//
//    public Integer getS_Int() {
//        return s_Int;
//    }
//
//    public void setS_Int(Integer s_Int) {
//        this.s_Int = s_Int;
//    }
//
//    public Integer getS_Uint() {
//        return s_Uint;
//    }
//
//    public void setS_Uint(Integer s_Uint) {
//        this.s_Uint = s_Uint;
//    }
//
//    public Long getS_Long() {
//        return s_Long;
//    }
//
//    public void setS_Long(Long s_Long) {
//        this.s_Long = s_Long;
//    }
//
//    public Long getS_Ulong() {
//        return s_Ulong;
//    }
//
//    public void setS_Ulong(Long s_Ulong) {
//        this.s_Ulong = s_Ulong;
//    }
//
//    public BigDecimal getS_Pdecimal() {
//        return s_Pdecimal;
//    }
//
//    public void setS_Pdecimal(BigDecimal s_Pdecimal) {
//        this.s_Pdecimal = s_Pdecimal;
//    }
//
//    public Float getS_Float() {
//        return s_Float;
//    }
//
//    public void setS_Float(Float s_Float) {
//        this.s_Float = s_Float;
//    }
//
//    public Double getS_Double() {
//        return s_Double;
//    }
//
//    public void setS_Double(Double s_Double) {
//        this.s_Double = s_Double;
//    }
//
//    public BigDecimal getS_Zoned() {
//        return s_Zoned;
//    }
//
//    public void setS_Zoned(BigDecimal s_Zoned) {
//        this.s_Zoned = s_Zoned;
//    }
//
//    public BigDecimal getS_Uzoned() {
//        return s_Uzoned;
//    }
//
//    public void setS_Uzoned(BigDecimal s_Uzoned) {
//        this.s_Uzoned = s_Uzoned;
//    }
//
//    public BigDecimal getS_ZonedSl() {
//        return s_ZonedSl;
//    }
//
//    public void setS_ZonedSl(BigDecimal s_ZonedSl) {
//        this.s_ZonedSl = s_ZonedSl;
//    }
//
//    public BigDecimal getS_ZonedSt() {
//        return s_ZonedSt;
//    }
//
//    public void setS_ZonedSt(BigDecimal s_ZonedSt) {
//        this.s_ZonedSt = s_ZonedSt;
//    }
//
//    public BigDecimal getS_ZonedSls() {
//        return s_ZonedSls;
//    }
//
//    public void setS_ZonedSls(BigDecimal s_ZonedSls) {
//        this.s_ZonedSls = s_ZonedSls;
//    }
//
//    public BigDecimal getS_ZonedSts() {
//        return s_ZonedSts;
//    }
//
//    public void setS_ZonedSts(BigDecimal s_ZonedSts) {
//        this.s_ZonedSts = s_ZonedSts;
//    }
//
//    public BigDecimal getS_UzonedBwz() {
//        return s_UzonedBwz;
//    }
//
//    public void setS_UzonedBwz(BigDecimal s_UzonedBwz) {
//        this.s_UzonedBwz = s_UzonedBwz;
//    }
//
//    @Override
//    public String toString() {
//        return toPositionalString();
//    }
//
//    private void validateStringLength(String fieldName, String value, int maxLength) {
//        if (value != null && value.length() > maxLength) {
//            throw new IllegalArgumentException(
//                    "String field '" + fieldName + "' is too long: " + value
//            );
//        }
//    }
//
//    private String toPositionalString() {
//        StringBuilder sb = new StringBuilder();
//
//        getOrderedFields(getClass()).forEach(field -> {
//            try {
//                Object value = readFieldValueByGetter(this, field);
//                sb.append(renderField(field, value));
//            } catch (Exception e) {
//                throw new RuntimeException(
//                        "Errore nella costruzione della stringa posizionale per il campo: " + field.getName(),
//                        e
//                );
//            }
//        });
//
//        return sb.toString();
//    }
//
//    private String renderField(Field field, Object value) {
//        if (field.isAnnotationPresent(CobolArray.class)) {
//            return renderArray(field, value);
//        }
//        if (isGroupField(field)) {
//            return renderGroup(field.getType(), value);
//        }
//        return renderSimple(field, value);
//    }
//
//    private boolean isGroupField(Field field) {
//        return field.getType().isAnnotationPresent(CobolGroup.class)
//                || field.getType().isAnnotationPresent(CobolChoice.class);
//    }
//
//    private Object readFieldValueByGetter(Object target, Field field) throws Exception {
//        String fieldName = field.getName();
//        String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
//        Method getter = target.getClass().getMethod(getterName);
//        return getter.invoke(target);
//    }
//
//    private String renderSimple(Field field, Object value) {
//        int fieldLength = resolveFieldLength(field);
//        char padChar = resolvePadChar(field);
//        String renderedValue = value == null ? "" : String.valueOf(value);
//
//        if (fieldLength <= 0) {
//            return renderedValue;
//        }
//
//        if (renderedValue.length() >= fieldLength) {
//            return renderedValue;
//        }
//
//        return String.valueOf(padChar).repeat(fieldLength - renderedValue.length()) + renderedValue;
//    }
//
//    private int resolveFieldLength(Field field) {
//        if (field.isAnnotationPresent(CobolString.class)) {
//            return field.getAnnotation(CobolString.class).charNum();
//        }
//        if (field.isAnnotationPresent(CobolBinaryNumber.class)) {
//            return field.getAnnotation(CobolBinaryNumber.class).totalDigits();
//        }
//        if (field.isAnnotationPresent(CobolPackedDecimal.class)) {
//            return field.getAnnotation(CobolPackedDecimal.class).totalDigits();
//        }
//        if (field.isAnnotationPresent(CobolZonedDecimal.class)) {
//            return field.getAnnotation(CobolZonedDecimal.class).totalDigits();
//        }
//        return -1;
//    }
//
//    private char resolvePadChar(Field field) {
//        if (field.isAnnotationPresent(CobolString.class)) {
//            return ' ';
//        }
//        return '0';
//    }
//
//    private String renderGroup(Class<?> groupType, Object value) {
//        Object source = value;
//        if (source == null) {
//            source = instantiateGroup(groupType);
//            if (source == null) {
//                return "";
//            }
//        }
//
//        return getOrderedFields(groupType).stream()
//                .map(field -> {
//                    try {
//                        Object nestedValue = readFieldValueByGetter(source, field);
//                        return renderField(field, nestedValue);
//                    } catch (Exception e) {
//                        throw new RuntimeException(
//                                "Errore nella costruzione della stringa posizionale per il campo annidato: "
//                                        + field.getName(),
//                                e
//                        );
//                    }
//                })
//                .collect(Collectors.joining());
//    }
//
//    private String renderArray(Field field, Object value) {
//        CobolArray cobolArray = field.getAnnotation(CobolArray.class);
//        int maxOccurs = cobolArray.maxOccurs();
//        List<Object> elements = toElements(value);
//        StringBuilder sb = new StringBuilder();
//
//        for (int i = 0; i < maxOccurs; i++) {
//            if (i < elements.size()) {
//                sb.append(renderArrayElement(field, elements.get(i)));
//            } else {
//                sb.append(renderEmptyArrayElement(field));
//            }
//        }
//
//        return sb.toString();
//    }
//
//    private List<Object> toElements(Object value) {
//        List<Object> elements = new ArrayList<>();
//
//        if (value == null) {
//            return elements;
//        }
//
//        if (value.getClass().isArray()) {
//            int length = Array.getLength(value);
//            for (int i = 0; i < length; i++) {
//                elements.add(Array.get(value, i));
//            }
//            return elements;
//        }
//
//        if (value instanceof Collection<?> collection) {
//            elements.addAll(collection);
//            return elements;
//        }
//
//        elements.add(value);
//        return elements;
//    }
//
//    private String renderEmptyArrayElement(Field field) {
//        Class<?> elementType = resolveArrayElementType(field);
//
//        if (elementType == null) {
//            return "";
//        }
//
//        if (isSimpleType(elementType)) {
//            return renderSimple(field, null);
//        }
//
//        return renderEmptyGroup(elementType);
//    }
//
//    private Class<?> resolveArrayElementType(Field field) {
//        Class<?> fieldType = field.getType();
//
//        if (fieldType.isArray()) {
//            return fieldType.getComponentType();
//        }
//
//        return null;
//    }
//
//    private String renderArrayElement(Field field, Object element) {
//        if (element == null) {
//            return renderEmptyArrayElement(field);
//        }
//
//        if (isSimpleValue(element)) {
//            return renderSimple(field, element);
//        }
//
//        return renderGroup(element.getClass(), element);
//    }
//
//    private String renderEmptyGroup(Class<?> groupType) {
//        Object emptyGroup = instantiateGroup(groupType);
//        if (emptyGroup == null) {
//            return "";
//        }
//        return renderGroup(groupType, emptyGroup);
//    }
//
//    private Object instantiateGroup(Class<?> groupType) {
//        try {
//            return groupType.getDeclaredConstructor().newInstance();
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    private boolean isSimpleType(Class<?> type) {
//        return type.isPrimitive()
//                || String.class.equals(type)
//                || Number.class.isAssignableFrom(type)
//                || Boolean.class.equals(type)
//                || Character.class.equals(type)
//                || BigDecimal.class.equals(type);
//    }
//
//    private boolean isSimpleValue(Object value) {
//        return isSimpleType(value.getClass());
//    }
//
//    private List<Field> getOrderedFields(Class<?> type) {
//        return Arrays.stream(type.getDeclaredFields())
//                .filter(field ->
//                        !(field.isAnnotationPresent(CobolFloat.class) ||  field.isAnnotationPresent(CobolDouble.class)) // TODO: al momento non gestisco i campi comp
//                )
//                .sorted((o1, o2) -> {
//                    return resolveFieldOrder(o1) < resolveFieldOrder(o2) ? 1 : -1;
//                })
//                .toList();
//    }
//
//
//    private int resolveFieldOrder(Field field) {
//        if (field.isAnnotationPresent(CobolString.class)) {
//            return field.getAnnotation(CobolString.class).order();
//        }
//        if (field.isAnnotationPresent(CobolBinaryNumber.class)) {
//            return field.getAnnotation(CobolBinaryNumber.class).order();
//        }
//        if (field.isAnnotationPresent(CobolPackedDecimal.class)) {
//            return field.getAnnotation(CobolPackedDecimal.class).order();
//        }
//        if (field.isAnnotationPresent(CobolZonedDecimal.class)) {
//            return field.getAnnotation(CobolZonedDecimal.class).order();
//        }
//        if (field.isAnnotationPresent(CobolArray.class)) {
//            return field.getAnnotation(CobolArray.class).order();
//        }
//        return Integer.MAX_VALUE;
//    }
//}