package org.legstar.cobol.generator.model;

/**
 * The model to be used with the templating engine.
 * 
 * @param package_name the generated java bean package name
 * @param root_item    the main cobol item
 * @param withToString should the generated java bean include a tostring method
 * @param options      any additional rendering options
 * @param withToPositionalString should the generated java bean include a toPositionalString method
 */
public record RenderingModel(String package_name, RenderingItem root_item, boolean withToString,
		RenderingOptions options, boolean withToPositionalString) {
}
