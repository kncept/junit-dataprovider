package com.kncept.junit.dataprovider.testfactory;

import java.util.Collection;

import org.junit.jupiter.api.DynamicTest;

/**
 * The TestFactoryCallback class gives a single static lookup point for Dynamic Tests.
 *
 */
public class TestFactoryCallback {

	/**
	 * Bridging method to simplify the creation of Dynamic Test instances.
	 * @param testInstance the instance of the test to test agains
	 * @return parameterised tests
	 */
	public static Collection<DynamicTest> instanceProvider(Object testInstance) {
		return new ParameterisedMethodTestFactory(testInstance).generateDynamicTests();
	}
	
}
