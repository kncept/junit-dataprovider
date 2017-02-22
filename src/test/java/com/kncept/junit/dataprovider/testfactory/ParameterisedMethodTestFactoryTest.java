package com.kncept.junit.dataprovider.testfactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;

import com.kncept.junit.dataprovider.ParameterSource;
import com.kncept.junit.dataprovider.ParameterisedTest;

public class ParameterisedMethodTestFactoryTest {
	private ParameterisedMethodTestFactory testFactory;
	
	@BeforeEach
	public void init() {
		testFactory = new ParameterisedMethodTestFactory(this);
	}
	
	@Test
	public void canFindTestMethods() {
		Assertions.assertEquals(3, testFactory.getParameterisedTestMethods().size());
	}
	
	@Test
	public void canFindSourceMethods() {
		Assertions.assertEquals(1, testFactory.getParameterSourceMethods().size());
	}
	
	@Test
	public void canLookupSourceMethods() {
		Method sourceMethod = testFactory.parameterSourceMethod("source");
		Assertions.assertNotNull(sourceMethod);
	}
	
	@Test
	public void throwsExceptionOnInvalidSourceMethodLookup() {
		IllegalArgumentException exception = Assertions.assertThrows(
				IllegalArgumentException.class,
				() -> testFactory.parameterSourceMethod("xxInvalidName"));
		Assertions.assertEquals("No ParameterSource called xxInvalidName", exception.getMessage());
	}
	
	@Test
	public void testNamesCanBeSpecified() throws Exception {
		String testName = testFactory.generateTestName(
				getClass().getMethod("parameterisedTest", int.class),
				new Object[]{1});
		Assertions.assertEquals("parameterisedTest with display name 1", testName);
	}
	
	@Test
	public void testNamesCanBeDefaulted() throws Exception {
		String testName = testFactory.generateTestName(
				getClass().getMethod("noDisplayName", int.class),
				new Object[]{1});
		Assertions.assertEquals("noDisplayName 1", testName);
	}
	
	@Test
	public void canCreateTests() throws Exception {
		List<DynamicTest> tests = testFactory.createTests(
				getClass().getMethod("parameterisedTest", int.class),
				getClass().getMethod("paramSource"));
		Assertions.assertEquals(2, tests.size()); 
	}
	
	@Test
	public void canExecuteTests() throws Throwable {
		List<DynamicTest> tests = testFactory.createTests(
				getClass().getMethod("parameterisedTest", int.class),
				getClass().getMethod("paramSource"));
		for(DynamicTest test: tests)
			test.getExecutable().execute();
	}
	
	@Test
	public void exceptionTypesAreRetained() throws Throwable {
		List<DynamicTest> tests = testFactory.createTests(
				getClass().getMethod("throwsException", int.class),
				getClass().getMethod("paramSource"));
		Assertions.assertThrows(
				TestException.class,
				() ->tests.get(0).getExecutable().execute());
	}
	
	@ParameterSource(name = "source")
	public List<Object[]> paramSource() {
		return Arrays.asList(
				new Object[]{1},
				new Object[]{2}
				);
	}
	
	@ParameterisedTest(source = "source")
	@DisplayName("parameterisedTest with display name %d")
	public void parameterisedTest(int val) {
	}
	
	@ParameterisedTest(source = "source")
	public void noDisplayName(int val) {
	}
	
	@ParameterisedTest(source = "source")
	public void throwsException(int val) {
		throw new TestException();
	}
	
	public static class TestException extends RuntimeException {
		
	}
	
}
