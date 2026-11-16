package org.legstar.cobol.converter;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.model.Copybook;
import org.legstar.cobol.annotation.*;
import static org.junit.jupiter.api.Assertions.*;

class MainframeMapperTest {
    @CobolGroup(cobolName="REQUEST")
    public static class Request implements Copybook {
        @CobolOrder(2) @CobolArray(minOccurs=0, maxOccurs=3, dependingOn="COUNT")
        @CobolString(cobolName="ITEM", charNum=2)
        private String[] items;
        @CobolOrder(1) @CobolBinaryNumber(cobolName="COUNT", totalDigits=4, odoObject=true)
        private Short count;
        public String[] getItems() { return items; }
        public void setItems(String[] value) { items = value; }
        public Short getCount() { return count; }
        public void setCount(Short value) { count = value; }
    }

    @Test void encodesOrderedVariableArrayWithoutRecordHeader() throws Exception {
        var request = new Request();
        request.setCount((short) 2);
        request.setItems(new String[]{"A", "BC"});
        assertArrayEquals(new byte[]{0, 2, 65, 32, 66, 67},
                new MainframeMapper(CobolBeanConverterConfig.ascii()).encode(request));
    }

    @Test void rejectsInconsistentCountAndReportsField() {
        var request = new Request();
        request.setCount((short) 2);
        request.setItems(new String[]{"A"});
        var error = assertThrows(IllegalArgumentException.class,
                () -> new MainframeMapper().encode(request));
        assertTrue(error.getMessage().contains("Request.items"));
    }

    @Test void rejectsNullElementWithIndex() {
        var request = new Request();
        request.setCount((short) 1);
        request.setItems(new String[]{null});
        var error = assertThrows(IllegalArgumentException.class,
                () -> new MainframeMapper().encode(request));
        assertTrue(error.getMessage().contains("items[0]"));
    }
    @Test void decodesOutputCountAndReplacesArray() {
        var request = new Request();
        request.setCount((short) 3);
        request.setItems(new String[]{"X", "Y", "Z"});
        new MainframeMapper(CobolBeanConverterConfig.ascii()).decodeInto(
                new byte[]{0, 2, 65, 32, 66, 67}, request);
        assertEquals((short) 2, request.getCount());
        assertArrayEquals(new String[]{"A ", "BC"}, request.getItems());
    }

    @Test void rejectsTruncatedStringAndKeepsEarlierUpdates() {
        var request = new Request();
        var error = assertThrows(IllegalArgumentException.class,
                () -> new MainframeMapper().decodeInto(new byte[]{0, 1, 65}, request));
        assertTrue(error.getMessage().contains("items[0]"));
        assertEquals((short) 1, request.getCount());
        assertNull(request.getItems());
    }

    @Test void rejectsTrailingBytesAndInvalidCount() {
        var mapper = new MainframeMapper();
        assertThrows(IllegalArgumentException.class,
                () -> mapper.decodeInto(new byte[]{0, 0, 99}, new Request()));
        assertThrows(IllegalArgumentException.class,
                () -> mapper.decodeInto(new byte[]{0, 4}, new Request()));
    }

    @CobolGroup(cobolName="VALIDATED")
    public static class Validated implements Copybook {
        @CobolString(cobolName="VALUE", charNum=1)
        private String value;
        public String getValue() { return value; }
        public void setValue(String value) { throw new IllegalStateException("setter validation"); }
    }

    @Test void preservesSetterExceptionAsCause() {
        var error = assertThrows(IllegalArgumentException.class,
                () -> new MainframeMapper(CobolBeanConverterConfig.ascii())
                        .decodeInto(new byte[]{65}, new Validated()));
        assertTrue(error.getMessage().contains("Validated.value"));
        assertInstanceOf(IllegalStateException.class, error.getCause());
    }

    @CobolGroup(cobolName="NESTED")
    public static class Nested implements Copybook {
        private Request child;
        public Request getChild() { return child; }
        public void setChild(Request value) { child = value; }
    }

    @Test void createsMissingGroupAndPreservesExistingGroup() {
        var mapper = new MainframeMapper(CobolBeanConverterConfig.ascii());
        var dto = new Nested();
        mapper.decodeInto(new byte[]{0, 0}, dto);
        var child = dto.getChild();
        assertNotNull(child);
        mapper.decodeInto(new byte[]{0, 1, 65, 32}, dto);
        assertSame(child, dto.getChild());
        assertArrayEquals(new String[]{"A "}, child.getItems());
    }

    @CobolGroup(cobolName="ENCODED")
    @CobolEncoding(hostCharsetName="ISO-8859-1", rightPadCobolAlphanumWithSpaces=false,
            truncateHostStringsTrailingSpaces=true)
    public static class Encoded implements Copybook {
        @CobolString(cobolName="TEXT", charNum=2)
        private String text;
        public String getText() { return text; }
        public void setText(String value) { text = value; }
    }

    @Test void usesDtoEncodingAndExplicitConstructorOverridesIt() throws Exception {
        var dto = new Encoded();
        dto.setText("A");
        var mapper = new MainframeMapper();
        assertArrayEquals(new byte[]{65, 0}, mapper.encode(dto));
        mapper.decodeInto(new byte[]{65, 32}, dto);
        assertEquals("A", dto.getText());
        assertArrayEquals(new byte[]{65, 32},
                new MainframeMapper(CobolBeanConverterConfig.ascii()).encode(dto));
    }

    @CobolGroup(cobolName="AMOUNTS")
    public static class Amounts implements Copybook {
        @CobolArray(minOccurs=1, maxOccurs=1)
        @CobolPackedDecimal(cobolName="VALUE", totalDigits=2)
        private String[] values;
        public String[] getValues() { return values; }
        public void setValues(String[] value) { values = value; }
    }

    @Test void encodeConversionErrorIncludesArrayIndexAndCause() {
        var dto = new Amounts();
        dto.setValues(new String[]{"invalid"});
        var error = assertThrows(IllegalArgumentException.class, () -> new MainframeMapper().encode(dto));
        assertTrue(error.getMessage().contains("Amounts.values[0]"));
        assertNotNull(error.getCause());
    }
    @Test void publicMapperContractAcceptsCopybook() throws Exception {
        assertNotNull(MainframeMapper.class.getMethod("encode", Copybook.class));
        assertNotNull(MainframeMapper.class.getMethod("decodeInto", byte[].class, Copybook.class));
        assertThrows(NoSuchMethodException.class,
                () -> MainframeMapper.class.getMethod("encode", Object.class));
        assertThrows(NoSuchMethodException.class,
                () -> MainframeMapper.class.getMethod("decodeInto", byte[].class, Object.class));
    }
}
