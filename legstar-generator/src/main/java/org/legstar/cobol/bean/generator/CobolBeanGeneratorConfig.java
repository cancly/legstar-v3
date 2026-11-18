package org.legstar.cobol.bean.generator;

import org.legstar.cobol.copybook.parser.CopybookParserConfig;
import org.legstar.cobol.generator.CobolGeneratorConfigBase;

/**
 * Java bean generator parameters.
 */
public class CobolBeanGeneratorConfig extends CobolGeneratorConfigBase<CobolBeanGeneratorConfig> {

    private boolean validateSetters = true;

    /** Whether generated setters validate scalar values and OCCURS arrays. */
    public boolean validateSetters() { return validateSetters; }

    public CobolBeanGeneratorConfig setValidateSetters(boolean value) {
        validateSetters = value;
        return this;
    }

    private String hostCharsetName = "IBM037";
    private boolean rightPadCobolAlphanumWithSpaces = true;
    private boolean truncateHostStringsTrailingSpaces;

    public String hostCharsetName() { return hostCharsetName; }
    public boolean rightPadCobolAlphanumWithSpaces() { return rightPadCobolAlphanumWithSpaces; }
    public boolean truncateHostStringsTrailingSpaces() { return truncateHostStringsTrailingSpaces; }

    public CobolBeanGeneratorConfig setHostCharsetName(String name) {
        hostCharsetName = java.nio.charset.Charset.forName(name).name();
        return this;
    }
    public CobolBeanGeneratorConfig setRightPadCobolAlphanumWithSpaces(boolean value) {
        rightPadCobolAlphanumWithSpaces = value;
        return this;
    }
    public CobolBeanGeneratorConfig setTruncateHostStringsTrailingSpaces(boolean value) {
        truncateHostStringsTrailingSpaces = value;
        return this;
    }

	/**
	 * Create a mutable set of parameters.
	 */
	public CobolBeanGeneratorConfig() {
		this(new CopybookParserConfig());
	}

	/**
	 * Create a mutable set of parameters.
	 * 
	 * @param parserConfig configuration parameters for the copybook parser
	 */
	public CobolBeanGeneratorConfig(CopybookParserConfig parserConfig) {
		super(parserConfig);
	}

	@Override
	public CobolBeanGeneratorConfig self() {
		return this;
	}

}
