package org.legstar.cobol.annotation;

import java.lang.annotation.*;

/** Default mainframe conversion settings carried by a generated root DTO. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CobolEncoding {
    String hostCharsetName() default "IBM037";
    boolean rightPadCobolAlphanumWithSpaces() default true;
    boolean truncateHostStringsTrailingSpaces() default false;
}
