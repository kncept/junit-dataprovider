package com.kncept.junit.dataprovider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code ParameterSource} is used to mark a method as a source
 * for parameters.
 * 
 * <p> The method must return a Stream, Iterator or Iterable.
 * 
 * <p> Each item must be an Object[], and the contents of the array must
 * match the method parameter types that uses this parameter source.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ParameterSource {
	String name();
}
