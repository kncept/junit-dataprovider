package com.kncept.junit.dataprovider.testfactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;

import com.kncept.junit.dataprovider.ParameterSource;
import com.kncept.junit.dataprovider.ParameterisedTest;
import com.kncept.junit.dataprovider.util.ReflectionUtil;

public class ParameterisedMethodTestFactory {
	final Object testInstance;
	final List<Method> parameterisedTestMethods;
	final List<Method> parameterSourceMethods;
	
	public ParameterisedMethodTestFactory(Object testInstance) {
		this.testInstance = testInstance;
		parameterisedTestMethods = Collections.unmodifiableList(ReflectionUtil.findMethodsWithAnnonation(
				testInstance.getClass(),
				ParameterisedTest.class));
		
		parameterSourceMethods = Collections.unmodifiableList(ReflectionUtil.findMethodsWithAnnonation(
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
		throw new IllegalArgumentException(String.format("No ParameterSource called %s", name));
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
			Iterator iterator = ReflectionUtil.toIterator(parameterMethod.invoke(testInstance));
			List<DynamicTest> tests = new ArrayList<>();
			while(iterator.hasNext()) {
				Object next = iterator.next();
				tests.add(createTest(testMethod, ReflectionUtil.toArray(next)));
			}
			return tests;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		}
	}
	
	private DynamicTest createTest(Method testMethod, Object[] args) {
		return DynamicTest.dynamicTest(
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
				});
	}
	
	public String generateTestName(Method method, Object[] args) {
		DisplayName displayName = method.getAnnotation(DisplayName.class);
		if (displayName != null)
			return String.format(displayName.value(), args);
		StringBuilder testName = new StringBuilder(method.getName());
		for(Object arg: args)
			testName.append(" ").append(arg == null ? "null" : arg.toString());
		return testName.toString();
	}
	
}
