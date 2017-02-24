package com.kncept.junit.dataprovider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code ParameterisedTest} is used to mark a method to be picked
 * up for Parameterisation.
 * 
 * <p> a {@code source} name must be specified, and the method parameters
 * will be filled in from that.
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ParameterisedTest {
	String source(); 
}
