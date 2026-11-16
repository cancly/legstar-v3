@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingBinaryNumber
@param RenderingModel model
@param RenderingBinaryNumber field

!{var fieldName = field.fieldName();}
!{var typeName = field.typeName();}

    public ${typeName}@if(field.isArray())[]@endif ${field.getterName()}() {
        return ${fieldName};
    }

    public void ${field.setterName()}(${typeName}@if(field.isArray())[]@endif ${fieldName}) {
@if(model.options().validateSetters())@if(field.isArray())        if (${fieldName} != null) {
            if (${fieldName}.length < ${field.array().isVariableSize() ? field.array().minOccurs() : field.array().maxOccurs()}
                    || ${fieldName}.length > ${field.array().maxOccurs()}) {
                throw new IllegalArgumentException("${fieldName}: expected array length ${field.array().isVariableSize() ? field.array().minOccurs() : field.array().maxOccurs()}..${field.array().maxOccurs()}");
            }
            for (int occurrenceIndex = 0; occurrenceIndex < ${fieldName}.length; occurrenceIndex++) {
        if (${fieldName}[occurrenceIndex] != null) {
            java.math.BigDecimal maximum = java.math.BigDecimal.TEN.pow(${field.totalDigits()})
                .subtract(java.math.BigDecimal.ONE).movePointLeft(0);
            if (new java.math.BigDecimal(${fieldName}[occurrenceIndex].toString()).abs().compareTo(maximum) > 0) {
                throw new IllegalArgumentException("${fieldName}[" + occurrenceIndex + "]: absolute value exceeds " + maximum);
            }
@if(!field.signed())            if (new java.math.BigDecimal(${fieldName}[occurrenceIndex].toString()).signum() < 0) {
                throw new IllegalArgumentException("${fieldName}[" + occurrenceIndex + "]: negative values are not allowed");
            }
@endif        }
            }
        }
@else        if (${fieldName} != null) {
            java.math.BigDecimal maximum = java.math.BigDecimal.TEN.pow(${field.totalDigits()})
                .subtract(java.math.BigDecimal.ONE).movePointLeft(0);
            if (new java.math.BigDecimal(${fieldName}.toString()).abs().compareTo(maximum) > 0) {
                throw new IllegalArgumentException("${fieldName}: absolute value exceeds " + maximum);
            }
@if(!field.signed())            if (new java.math.BigDecimal(${fieldName}.toString()).signum() < 0) {
                throw new IllegalArgumentException("${fieldName}: negative values are not allowed");
            }
@endif        }
@endif@endif        this.${fieldName} = ${fieldName};
    }