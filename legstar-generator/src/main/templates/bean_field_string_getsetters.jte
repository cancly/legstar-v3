@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingString
@param RenderingModel model
@param RenderingString field

!{var fieldName = field.fieldName();}

    public String@if(field.isArray())[]@endif ${field.getterName()}() {
        return ${fieldName};
    }

    public void ${field.setterName()}(String@if(field.isArray())[]@endif ${fieldName}) {
@if(model.options().validateSetters())@if(field.isArray())        if (${fieldName} != null) {
            if (${fieldName}.length < ${field.array().isVariableSize() ? field.array().minOccurs() : field.array().maxOccurs()}
                    || ${fieldName}.length > ${field.array().maxOccurs()}) {
                throw new IllegalArgumentException("${fieldName}: expected array length ${field.array().isVariableSize() ? field.array().minOccurs() : field.array().maxOccurs()}..${field.array().maxOccurs()}");
            }
            for (int occurrenceIndex = 0; occurrenceIndex < ${fieldName}.length; occurrenceIndex++) {
        if (${fieldName}[occurrenceIndex] != null && ${fieldName}[occurrenceIndex].length() > ${field.charNum()}) {
            throw new IllegalArgumentException("${fieldName}[" + occurrenceIndex + "]: maximum length is ${field.charNum()}");
        }
            }
        }
@else        if (${fieldName} != null && ${fieldName}.length() > ${field.charNum()}) {
            throw new IllegalArgumentException("${fieldName}: maximum length is ${field.charNum()}");
        }
@endif@endif        this.${fieldName} = ${fieldName};
    }