package org.legstar.cobol.bean.generator;

import java.io.StringWriter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BeanIndentWriterOutputTest {
    @Test void removesEmptyLinesInsideAnnotationBlocksAndAtBlockEdges() {
        var result = new StringWriter();
        var output = new BeanIndentWriterOutput(result);
        String nl = System.lineSeparator();
        // Split writes, as performed by JTE for interpolated annotations.
        output.writeContent(nl + "@CobolGroup(cobolName = ");
        output.writeContent("\"ROOT\")" + nl + nl + "@CobolEncoding" + nl + nl);
        output.writeContent("public class Root {" + nl + nl + "    @CobolOrder(0)" + nl + nl);
        output.writeContent("    @CobolString" + nl + nl + "    private String value;" + nl + nl + nl);
        output.writeContent("    public String getValue() {" + nl + nl + "        return value;" + nl + nl);
        output.writeContent("    }" + nl + nl + "}" + nl);
        assertEquals(String.join(nl,
                "@CobolGroup(cobolName = \"ROOT\")", "@CobolEncoding", "public class Root {",
                "    @CobolOrder(0)", "    @CobolString", "    private String value;", "",
                "    public String getValue() {", "        return value;", "    }", "}", ""),
                result.toString());
    }
}
