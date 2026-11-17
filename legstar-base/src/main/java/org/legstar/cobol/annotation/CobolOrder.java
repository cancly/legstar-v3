package org.legstar.cobol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * COBOL group item.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@CobolItemType
public @interface CobolOrder {
	String value();
}
