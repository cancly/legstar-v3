@import gg.jte.support.ForSupport
@import org.legstar.cobol.generator.model.*
@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingPackedDecimal
@import org.legstar.cobol.generator.model.RenderingZonedDecimal
@param RenderingModel model
@param java.util.List<RenderingItem> fields


public String toPositionalString() {
    StringBuilder sb = new StringBuilder();
@for(var field : fields)
    !{var fieldName = field.fieldName();}
    @if(field.isArray())
    Arrays.stream(${fieldName}).forEachOrdered(
        s -> sb.append(String.valueOf(
@if(field instanceof RenderingZonedDecimal renderingZoneDecimal)
    "0"
@elseif(field instanceof RenderingPackedDecimal renderingPackedDecimal)
    "0"
@elseif(field instanceof RenderingBinaryNumber renderingBinaryNumber)
    "0"
@elseif(field instanceof RenderingString renderingString)
    " "
@elseif(field instanceof RenderingFloat renderingFloat)
    "0"
@elseif(field instanceof RenderingDouble renderingDouble)
    "0"
@elseif(field instanceof RenderingChoice renderingChoice)
    "0"
@endif
        ).repeat(Math.max(0, ${field.maxBytesLen()} - s.length())) + s););
@elseif(field instanceof RenderingGroup renderingSubGroup)
        sb.append(${fieldName};
@else

        sb.append(String.valueOf(
        @if(field instanceof RenderingZonedDecimal renderingZoneDecimal)
            "0"
        @elseif(field instanceof RenderingPackedDecimal renderingPackedDecimal)
            "0"
        @elseif(field instanceof RenderingBinaryNumber renderingBinaryNumber)
            "0"
        @elseif(field instanceof RenderingString renderingString)
            " "
        @elseif(field instanceof RenderingFloat renderingFloat)
            "0"
        @elseif(field instanceof RenderingDouble renderingDouble)
            "0"
        @elseif(field instanceof RenderingChoice renderingChoice)
            "0"
        @endif
        ).repeat(Math.max(0, ${field.maxBytesLen()} - s.length())) + s););

@endif
@endfor
    return sb.toString();
}
