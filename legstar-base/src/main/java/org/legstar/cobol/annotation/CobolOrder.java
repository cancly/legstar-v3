package org.legstar.cobol.annotation;

import java.lang.annotation.*;

/** Position of a field within its containing COBOL group. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CobolOrder {
    int value();
}
