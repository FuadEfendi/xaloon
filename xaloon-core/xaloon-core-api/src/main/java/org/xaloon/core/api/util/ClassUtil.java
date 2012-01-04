/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xaloon.core.api.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

import org.xaloon.core.api.exception.CreateClassInstanceException;

/**
 * Simple utility for class related operations
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.5
 */
public class ClassUtil {

	/**
	 * Returns annotation for specified class. if current class does not have such annotation then super class is checked.
	 * 
	 * @param <A>
	 * 
	 * @param classForAnnotation
	 *            to check for specified annotation
	 * @param annotationClass
	 *            what kind of annotation to lookup
	 * @return annotation if found. otherwise - null
	 */
	public static <A extends Annotation> A getAnnotation(Class<?> classForAnnotation, Class<A> annotationClass) {
		if (classForAnnotation == null) {
			return null;
		}
		A result = classForAnnotation.getAnnotation(annotationClass);
		if (result != null) {
			return result;
		}
		return getAnnotation(classForAnnotation.getSuperclass(), annotationClass);
	}

	/**
	 * Returns class which has specified annotation. If current class does not have one then check is processed on super class.
	 * 
	 * @param <A>
	 * 
	 * @param classForAnnotation
	 *            to check for specified annotation
	 * @param annotationClass
	 *            what kind of annotation to lookup
	 * @return class, which has such annotation. null if not found.
	 */
	public static <A extends Annotation> Class<?> getClassByAnnottation(Class<?> classForAnnotation, Class<A> annotationClass) {
		if (classForAnnotation == null) {
			return null;
		}
		A result = classForAnnotation.getAnnotation(annotationClass);
		if (result != null) {
			return classForAnnotation;
		}
		return getClassByAnnottation(classForAnnotation.getSuperclass(), annotationClass);
	}

	/**
	 * Determine whether the given class has a public constructor with the given signature, and return it if available (else return <code>null</code>
	 * ).
	 * <p>
	 * Essentially translates <code>NoSuchMethodException</code> to <code>null</code>.
	 * 
	 * @param <T>
	 * 
	 * @param clazz
	 *            the clazz to analyze
	 * @param paramTypes
	 *            the parameter types of the method
	 * @return the constructor, or <code>null</code> if not found
	 * @see java.lang.Class#getConstructor
	 */
	public static <T> Constructor<T> getConstructorIfAvailable(Class<T> clazz, Class<?>... paramTypes) {
		if (clazz == null) {
			return null;
		}
		try {
			return clazz.getConstructor(paramTypes);
		} catch (NoSuchMethodException ex) {
			return null;
		}
	}

	/**
	 * Determine whether the given class has a public constructor with the given signature.
	 * <p>
	 * Essentially translates <code>NoSuchMethodException</code> to "false".
	 * 
	 * @param clazz
	 *            the clazz to analyze
	 * @param paramTypes
	 *            the parameter types of the method
	 * @return whether the class has a corresponding constructor
	 * @see java.lang.Class#getMethod
	 */
	public static boolean hasConstructor(Class<?> clazz, Class<?>... paramTypes) {
		return (getConstructorIfAvailable(clazz, paramTypes) != null);
	}

	/**
	 * Creates new instance of class object provided.
	 * 
	 * @param classToCreate
	 * @return instance of provided class
	 * @throws CreateClassInstanceException
	 *             custom exception is thrown if class instance could not be created
	 */
	public static <T> T newInstance(Class<T> classToCreate) throws CreateClassInstanceException {
		try {
			return classToCreate.newInstance();
		} catch (Exception e) {
			throw new CreateClassInstanceException("Could not create instance for class '" + classToCreate);
		}
	}

	/**
	 * Create instance of specified class with concrete parameters
	 * 
	 * @param classToCreate
	 * @param parameterTypes
	 * @param parameterValues
	 * @return instance of provided class
	 * @throws CreateClassInstanceException
	 */
	public static Object newInstance(Class<?> classToCreate, Class<?>[] parameterTypes, Object[] parameterValues) throws CreateClassInstanceException {
		try {
			if (ClassUtil.hasConstructor(classToCreate, parameterTypes)) {
				return classToCreate.getConstructor(parameterTypes).newInstance(parameterValues);
			}
		} catch (NoSuchMethodException e) {
			throw new CreateClassInstanceException("Could not create instance for class '" + classToCreate + "' with parameters '" +
				Arrays.toString(parameterValues) + "'");
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	/**
	 * Returns first interface of provided class
	 * 
	 * @param clz
	 *            class to lookup for an interface
	 * @return return first interface of provided class if found. null is returned if there is no any interface found.
	 */
	public static Class<?> getFirstInterface(Class<?> clz) {
		if (clz == null) {
			return null;
		}
		if (clz.getInterfaces().length > 0) {
			return clz.getInterfaces()[0];
		}
		return getFirstInterface(clz.getSuperclass());
	}

	/**
	 * Returns class of generic T type
	 * 
	 * @param classToLookup
	 *            class to search for generics type
	 * @param index
	 *            if there are more than one generic type of class - which should be returned
	 * @param assignableClass
	 * @return generic type class
	 */
	public static Class<?> getClassGenericType(Class<?> classToLookup, int index, Class<?> assignableClass) {
		if (classToLookup.getGenericSuperclass() instanceof Class<?>) {
			return getClassGenericType((Class<?>)classToLookup.getGenericSuperclass(), index, assignableClass);
		}
		ParameterizedType parameterizedType = (ParameterizedType)classToLookup.getGenericSuperclass();
		if (parameterizedType != null && parameterizedType.getActualTypeArguments().length >= index &&
			parameterizedType.getActualTypeArguments()[index] instanceof Class<?>) {
			Class<?> clz = (Class<?>)parameterizedType.getActualTypeArguments()[index];
			if (!assignableClass.isAssignableFrom(clz)) {
				return getClassGenericType(classToLookup.getSuperclass(), index, assignableClass);
			} else {
				return clz;
			}
		}
		return null;
	}

	/**
	 * @param clazz
	 * @param methodName
	 * @param paramTypes
	 * @return
	 */
	public static Method getMethodIfAvailable(Class<?> clazz, String methodName, Class<?>... paramTypes) {
		if (clazz == null) {
			return null;
		}
		try {
			return clazz.getDeclaredMethod(methodName, paramTypes);
		} catch (NoSuchMethodException e) {
			// try superclass
			return getMethodIfAvailable(clazz.getSuperclass(), methodName, paramTypes);
		}
	}
}
