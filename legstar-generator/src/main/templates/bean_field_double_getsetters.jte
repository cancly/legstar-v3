@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingDouble
@param RenderingModel model
@param RenderingDouble field

!{var fieldName = field.fieldName();}

    public Double@if(field.isArray())[]@endif ${field.getterName()}() {
        return ${fieldName};
    }

    public void ${field.setterName()}(Double@if(field.isArray())[]@endif ${fieldName}) {
@if(model.options().validateSetters())@if(field.isArray())        if (${fieldName} != null) {
            if (${fieldName}.length < ${field.array().isVariableSize() ? field.array().minOccurs() : field.array().maxOccurs()}
                    || ${fieldName}.length > ${field.array().maxOccurs()}) {
                throw new IllegalArgumentException("${fieldName}: expected array length ${field.array().isVariableSize() ? field.array().minOccurs() : field.array().maxOccurs()}..${field.array().maxOccurs()}");
            }
        }
@endif@endif        this.${fieldName} = ${fieldName};
    }