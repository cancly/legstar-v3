@import org.legstar.cobol.generator.model.RenderingArray
@import org.legstar.cobol.generator.model.RenderingItem
@param RenderingItem field
@param RenderingArray renderingArray
    @CobolArray(minOccurs=${renderingArray.minOccurs()}, maxOccurs=${renderingArray.maxOccurs()}, order = ${field.ordinal()}@if(renderingArray.isVariableSize()), dependingOn = "${renderingArray.dependingOn()}"@endif)