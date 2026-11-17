@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingString
@param RenderingModel model
@param RenderingString field

!{var fieldName = field.fieldName();}

    public String@if(field.isArray())[]@endif ${field.getterName()}() {
        return ${fieldName};
    }

    public void ${field.setterName()}(String@if(field.isArray())[]@endif ${fieldName}) {
        @if(field.isArray())if( ${fieldName} .lenght > ${field.array().maxOccurs()}) {
            throw new IllegalArgumentException("Array lenght bigger then maxOccurs");
        }
        if(Arrays.stream( ${fieldName} ).matchAny(s -> s.length() > ${field.maxBytesLen()} ) {
            throw new IllegalArgumentException("a field of the array '" + ${fieldName} + "' is too long");@else if(${fieldName}.length() > ${field.maxBytesLen()}) {
            throw new IllegalArgumentException("String field '" + ${fieldName} + "' is too long");
        }@endif
        this.${fieldName} = ${fieldName};
    }