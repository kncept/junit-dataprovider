package com.kncept.junit.dataprovider.util;

import static com.kncept.junit.dataprovider.util.ReflectionUtil.findMethodsWithAnnonation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ReflectionUtilTest {

	
	@Test
	public void noAnnotationsToFind() {
		List<Method> methods = findMethodsWithAnnonation(TypeZero.class, TestAnnotation.class);
		assertTrue(methods.isEmpty());
	}
	
	@Test
	public void canFindAnnotations() {
		List<Method> methods = findMethodsWithAnnonation(TypeOne.class, TestAnnotation.class);
		assertEquals(1,  methods.size());
	}
	
	@Test
	public void canFindMultipleAnnotations() {
		List<Method> methods = findMethodsWithAnnonation(TypeTwo.class, TestAnnotation.class);
		assertEquals(2,  methods.size());
	}
	
	@Test
	public void canFindAnnotationsOnOverriddenMethods() {
		List<Method> methods = findMethodsWithAnnonation(TypeThree.class, TestAnnotation.class);
		assertEquals(2,  methods.size());
	}
	
	

	@Retention(RetentionPolicy.RUNTIME)
	public static @interface TestAnnotation {}
	
	public static class TypeZero {
		public void miss(){}
	}
	
	public static class TypeOne {
		@TestAnnotation
		public void methodOne() {};
		public void missOne() {};
	}
	
	public static class TypeTwo {
		@TestAnnotation
		public void methodTwo() {};
		@TestAnnotation
		public void anotherMethodTwo() {};
	}
	
	public static class TypeThree extends TypeOne {
		@Override
		@TestAnnotation
		public void methodOne() {
		}
		public void missThree() {};
	}
	
}
