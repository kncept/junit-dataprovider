package com.kncept.junit.dataprovider.testfactory;

import static com.kncept.junit.dataprovider.util.ReflectionUtil.findMethodsWithAnnonation;
import static com.kncept.junit.dataprovider.util.ReflectionUtil.toArray;
import static com.kncept.junit.dataprovider.util.ReflectionUtil.toIterator;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;

import com.kncept.junit.dataprovider.ParameterSource;
import com.kncept.junit.dataprovider.ParameterisedTest;
import com.kncept.junit.dataprovider.util.ReflectionUtil;

/**
 * This class processes an object instance for parameterised tests
 * 
 * <p>There should be no need for API Consumers to use this class directly, 
 * instead use {@code ParameterisedMethodTestFactory}
 *
 */
public class ParameterisedMethodTestFactory {
	final Object testInstance;
	final List<Method> parameterisedTestMethods;
	final List<Method> parameterSourceMethods;
	
	public ParameterisedMethodTestFactory(Object testInstance) {
		this.testInstance = testInstance;
		parameterisedTestMethods = unmodifiableList(findMethodsWithAnnonation(
				testInstance.getClass(),
				ParameterisedTest.class));
		
		parameterSourceMethods = unmodifiableList(findMethodsWithAnnonation(
				testInstance.getClass(),
				ParameterSource.class));
	}
	
	public List<Method> getParameterisedTestMethods() {
		return  parameterisedTestMethods;
	}
	
	public List<Method> getParameterSourceMethods() {
		return parameterSourceMethods;
	}

	public Method parameterSourceMethod(String name) {
		for(Method parameterMethod: parameterSourceMethods) {
			if (parameterMethod.getAnnotation(ParameterSource.class).name().equals(name))
				return parameterMethod;
		}
		throw new IllegalArgumentException(format("No ParameterSource called %s", name));
	}
	
	public Collection<DynamicTest> generateDynamicTests() {
		List<DynamicTest> tests = new ArrayList<>();
		for(Method testMethod: parameterisedTestMethods) {
			ParameterisedTest parameterisedTestAnnotation = testMethod.getAnnotation(ParameterisedTest.class);
			String sourceName = parameterisedTestAnnotation.source();
			tests.addAll(createTests(testMethod, parameterSourceMethod(sourceName)));
		}
		return tests;
	}
	
	public List<DynamicTest> createTests(Method testMethod, Method parameterMethod) {
		if (!ReflectionUtil.canGetIterator(parameterMethod.getReturnType()))
			throw new IllegalArgumentException("Method " + parameterMethod.getName() + " must return an Iterator, Iterable or Stream");
		try {
			Iterator iterator = toIterator(parameterMethod.invoke(testInstance));
			List<DynamicTest> tests = new ArrayList<>();
			while(iterator.hasNext()) {
				Object next = iterator.next();
				tests.addAll(createTest(testMethod, toArray(next)));
			}
			return tests;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		}
	}
	
	private List<DynamicTest> createTest(Method testMethod, Object[] args) {
		if (testMethod.getDeclaredAnnotation(Disabled.class) != null)
			return emptyList();
		return asList(dynamicTest(
				generateTestName(testMethod, args),
				() -> {
					try {
						testMethod.invoke(testInstance, args);
					} catch (InvocationTargetException e) {
						Throwable cause = e.getCause();
						if (cause != null)
							throw cause;
						throw e;
					} catch (Throwable t) {
						throw t;
					}
				}));
	}
	
	public String generateTestName(Method method, Object[] args) {
		DisplayName displayName = method.getAnnotation(DisplayName.class);
		if (displayName != null)
			return format(displayName.value(), args);
		StringBuilder testName = new StringBuilder(method.getName());
		for(Object arg: args)
			testName.append(" ").append(arg);
		return testName.toString();
	}
	
}
