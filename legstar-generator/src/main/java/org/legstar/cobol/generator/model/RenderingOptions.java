package org.legstar.cobol.generator.model;

import java.util.function.Function;

/**
 * Allows derived generators to produce more content for a generated bean.
 */
public interface RenderingOptions {

    /** Whether to emit validation before assigning a generated field. */
    default boolean validateSetters() { return true; }


	/**
	 * Whether the generated java bean should include more imports.
	 * 
	 * @return true if the generated java bean should include more imports
	 */
	boolean hasMoreImports();

	/**
	 * An additional imports provider.
	 * 
	 * @return a method that renders additional imports to include in the generated
	 *         java bean.
	 */
	Function<RenderingModel, String> moreImports();

	/**
	 * Whether the generated java bean should include additional annotations for
	 * group items.
	 * 
	 * @return true if the generated java bean should more annotations for group
	 *         items
	 */
	boolean hasMoreGroupAnnotations();

	/**
	 * An additional group annotations provider.
	 * 
	 * @return a method that renders additional group annotations to include in the
	 *         generated java bean.
	 */
	Function<RenderingGroup, String> moreGroupAnnotations();

	/**
	 * Whether the generated java bean should include additional annotations for
	 * choice items.
	 * 
	 * @return true if the generated java bean should more annotations for choice
	 *         items
	 */
	boolean hasMoreChoiceAnnotations();

	/**
	 * An additional choice annotations provider.
	 * 
	 * @return a method that renders additional choice annotations to include in the
	 *         generated java bean.
	 */
	Function<RenderingChoice, String> moreChoiceAnnotations();

	/**
	 * Whether the generated java bean should include additional annotations for
	 * root item.
	 * 
	 * @return true if the generated java bean should more annotations for the root
	 *         item
	 */
	boolean hasMoreRootGroupAnnotations();

	/**
	 * An additional root annotations provider.
	 * 
	 * @return a method that renders additional root annotations to include in the
	 *         generated java bean.
	 */
	Function<RenderingGroup, String> moreRootGroupAnnotations();

	/**
	 * A default implementation.
	 * 
	 * @return the default set of rendering options
	 */
	static RenderingOptions defaults() {
		return mainframeDefaults(null, true, false);
	}

    static RenderingOptions mainframeDefaults(String charset, boolean spaces, boolean trim) {
        return mainframeDefaults(charset, spaces, trim, true);
    }

    static RenderingOptions mainframeDefaults(String charset, boolean spaces, boolean trim, boolean validate) {
		return new RenderingOptions() {
            @Override
            public boolean validateSetters() { return validate; }


			@Override
			public boolean hasMoreImports() {
				return false;
			}

			@Override
			public Function<RenderingModel, String> moreImports() {
				return null;
			}

			@Override
			public boolean hasMoreGroupAnnotations() {
				return false;
			}

			@Override
			public Function<RenderingGroup, String> moreGroupAnnotations() {
				return null;
			}

			@Override
			public boolean hasMoreChoiceAnnotations() {
				return false;
			}

			@Override
			public Function<RenderingChoice, String> moreChoiceAnnotations() {
				return null;
			}

			@Override
			public boolean hasMoreRootGroupAnnotations() {
				return charset != null;
			}

			@Override
			public Function<RenderingGroup, String> moreRootGroupAnnotations() {
                return group -> "@CobolEncoding(hostCharsetName = \"" + charset
                    + "\", rightPadCobolAlphanumWithSpaces = " + spaces
                    + ", truncateHostStringsTrailingSpaces = " + trim + ")";
			}

		};
	}

}
