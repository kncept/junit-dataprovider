package com.kncept.junit.dataprovider.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

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
	
	public static boolean canGetIterator(Class<?> type) {
		return
				Iterable.class.isAssignableFrom(type) ||
				Stream.class.isAssignableFrom(type) ||
				Iterator.class.isAssignableFrom(type);
	}
	
	public static Iterator toIterator(Object value) {
		if (value instanceof Iterator)
			return (Iterator)value;
		if (value instanceof Iterable)
			return ((Iterable)value).iterator();
		if (value instanceof Stream)
			return ((Stream)value).iterator();
		throw new IllegalArgumentException("input value must be of type Iterator, Iterable or Stream");
	}
	
	public static Object[] toArray(Object arrayObject) {
		if (arrayObject.getClass().isArray())
			return (Object[]) arrayObject;
		if (canGetIterator(arrayObject.getClass())) {
			List<Object> list = new ArrayList<>();
			Iterator iterator = toIterator(arrayObject);
			while (iterator.hasNext())
				list.add(iterator.next());
			return list.toArray(new Object[list.size()]);
		}
		throw new IllegalArgumentException(String.format("Unable to coerce to an array: %s", arrayObject));
	}
}
