@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingPackedDecimal
@param RenderingModel model
@param RenderingPackedDecimal field

!{var fieldName = field.fieldName();}

    public BigDecimal@if(field.isArray())[]@endif ${field.getterName()}() {
        return ${fieldName};
    }

    public void ${field.setterName()}(BigDecimal@if(field.isArray())[]@endif ${fieldName}) {
@if(model.options().validateSetters())@if(field.isArray())        if (${fieldName} != null) {
            if (${fieldName}.length < ${field.array().isVariableSize() ? field.array().minOccurs() : field.array().maxOccurs()}
                    || ${fieldName}.length > ${field.array().maxOccurs()}) {
                throw new IllegalArgumentException("${fieldName}: expected array length ${field.array().isVariableSize() ? field.array().minOccurs() : field.array().maxOccurs()}..${field.array().maxOccurs()}");
            }
            for (int occurrenceIndex = 0; occurrenceIndex < ${fieldName}.length; occurrenceIndex++) {
        if (${fieldName}[occurrenceIndex] != null) {
            java.math.BigDecimal maximum = java.math.BigDecimal.TEN.pow(${field.totalDigits()})
                .subtract(java.math.BigDecimal.ONE).movePointLeft(${field.fractionDigits()});
            if (${fieldName}[occurrenceIndex].abs().compareTo(maximum) > 0) {
                throw new IllegalArgumentException("${fieldName}[" + occurrenceIndex + "]: absolute value exceeds " + maximum);
            }
@if(!field.signed())            if (${fieldName}[occurrenceIndex].signum() < 0) {
                throw new IllegalArgumentException("${fieldName}[" + occurrenceIndex + "]: negative values are not allowed");
            }
@endif        }
            }
        }
@else        if (${fieldName} != null) {
            java.math.BigDecimal maximum = java.math.BigDecimal.TEN.pow(${field.totalDigits()})
                .subtract(java.math.BigDecimal.ONE).movePointLeft(${field.fractionDigits()});
            if (${fieldName}.abs().compareTo(maximum) > 0) {
                throw new IllegalArgumentException("${fieldName}: absolute value exceeds " + maximum);
            }
@if(!field.signed())            if (${fieldName}.signum() < 0) {
                throw new IllegalArgumentException("${fieldName}: negative values are not allowed");
            }
@endif        }
@endif@endif        this.${fieldName} = ${fieldName};
    }