package com.kncept.junit.dataprovider.util;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class is considered internal.
 * 
 * <p> There should be no need for API consumers to use this class
 *
 */
public final class ReflectionUtil {
	private ReflectionUtil(){}

	/**
	 * Finds all methods with the supplied annotation.<br>
	 * N.B. multiple methods of the same signature can be returned.<br>
	 * Overriding methods always have a smaller index than their superclass.<br>
	 * @param type the type we are scanning
	 * @param annotationClass the annotation type to look for
	 * @return matching methods
	 */
	public static List<Method> findMethodsWithAnnonation(Class<?> type, Class<? extends Annotation> annotationClass) {
		List<Method> methods = new ArrayList<>();
		if (type != Object.class) {
			for(Method method: type.getDeclaredMethods()) {
				if (method.getAnnotation(annotationClass) != null)
					methods.add(method);
			}
			methods.addAll(findMethodsWithAnnonation(type.getSuperclass(), annotationClass));
		}
		return methods;
	}
	
	/**
	 * Static type checking for the ability to get/create an iterator
	 * @param type the type to check
	 * @return is this type is known to be able to be iterated over
	 */
	public static boolean canGetIterator(Class<?> type) {
		return
				Iterable.class.isAssignableFrom(type) ||
				Stream.class.isAssignableFrom(type) ||
				Iterator.class.isAssignableFrom(type) ||
				type.isArray();
	}
	
	/**
	 * Converts {@code value} to an iterator
	 * @param value an iterator, iterable, stream or array
	 * @return an iterator
	 * @throws IllegalArgumentException throws in the conversion is unknown
	 */
	public static Iterator toIterator(Object value) throws IllegalArgumentException {
		if (value instanceof Iterator)
			return (Iterator)value;
		if (value instanceof Iterable)
			return ((Iterable)value).iterator();
		if (value instanceof Stream)
			return ((Stream)value).iterator();
		if (value.getClass().isArray()) {
			return asList((Object[])value).iterator();
		}
		throw new IllegalArgumentException("input value must be of type Iterator, Iterable, Stream or Object[]");
	}
	
	/**
	 * Converts {@code value} to an object array
	 * @param value an iterator, iterable, stream or array
	 * @return an Object[]
	 * @throws IllegalArgumentException throws in the conversion is unknown
	 */
	
	public static Object[] toArray(Object value) throws IllegalArgumentException{
		if (value.getClass().isArray())
			return (Object[]) value;
		if (canGetIterator(value.getClass())) {
			List<Object> list = new ArrayList<>();
			Iterator iterator = toIterator(value);
			while (iterator.hasNext())
				list.add(iterator.next());
			return list.toArray(new Object[list.size()]);
		}
		throw new IllegalArgumentException(format("Unable to coerce to an array: %s", value));
	}
}
