package com.kncept.junit.dataprovider.testfactory;

import java.util.Collection;

import org.junit.jupiter.api.DynamicTest;

public class TestFactoryCallback {

	public static Collection<DynamicTest> instanceProvider(Object testInstance) {
		return new ParameterisedMethodTestFactory(testInstance).generateDynamicTests();
	}
	
}
