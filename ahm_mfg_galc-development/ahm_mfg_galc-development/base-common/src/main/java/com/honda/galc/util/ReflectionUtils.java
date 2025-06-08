package com.honda.galc.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.reflect.ConstructorUtils;
import org.apache.commons.lang.reflect.MethodUtils;

import com.honda.galc.common.exception.BaseException;
import com.honda.galc.common.exception.ReflectionException;
import com.honda.galc.common.logging.Logger;
/**
 * 
 * <h3>ReflectionUtils Class description</h3>
 * <p> ReflectionUtils description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * May 2, 2011
 */
public class ReflectionUtils {
	
	/**
	 * @param clazz		constructor type
	 * @return			returns default constructor for the type clazz
	 */
	public static <T> T createInstance(Class<T> clazz) {
		try {
			return (T) clazz.getConstructor().newInstance(new Object[]{});
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Only looks for the constructor matching the exact signature (based on the supplied parameter types)
	 * 
	 * @param clazz				constructor type
	 * @param parameterTypes 	explicitly state the parameter types if the parameter Object being passed in 
	 * 							is an inherited type of the actual type required to locate the constructor
	 * @param parameters		can be a combination of Objects and wrapped Primitives
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T createInstance(Class<T> clazz, Class<?>[] parameterTypes, Object... parameters) {
		try {
			Constructor<T> constructor = ConstructorUtils.getMatchingAccessibleConstructor(clazz, parameterTypes);
			return (T) constructor.newInstance(unwrapPrimitives(parameters));
		} catch (Exception e) {
			Logger.getLogger().warn(e, "Failed to instantiate " + clazz.getCanonicalName() + " using explicit parameter types " + parameterTypes);
			return createInstance(clazz);
		}
	}
	
	/**
	 * Looks for the constructor matching the exact signature first. If no such constructor is found, all 
	 * the constructors of the class are tested if their signatures are assignment compatible with the parameter types. 
	 * The first matching constructor is returned.
	 * 
	 * @param clazz			constructor type
	 * @param parameters	can be a combination of Objects and wrapped Primitives
	 * @return
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T createInstance(Class<T> clazz, Object... parameters) {
		try {
			Constructor<T> constructor = ConstructorUtils.getMatchingAccessibleConstructor(clazz, getParameterTypes(parameters));
			return (T) constructor.newInstance(unwrapPrimitives(parameters));
		}catch(InvocationTargetException ex){
			if(ex.getTargetException()instanceof BaseException) {
				throw (BaseException)ex.getTargetException();
			}else {
				try {
					throw ex.getTargetException();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
		catch (Exception e) {
			Logger.getLogger().warn("Failed to instantiate " + clazz.getCanonicalName() + " with inferred actual parameter types " + getParameterTypes(parameters));
			return createInstance(clazz);
		}
		return null;
	}
	
	/**
	 * @param target		target object for method invocation
	 * @param methodName	method name
	 * @param parameters	can be a combination of actual parameter objects and Primitives
	 * @return
	 */
	public static Object invoke(Object target, String methodName, Object... parameters) {
		if (target == null) return null;
		try {
			return invoke(target, getMethod(target, methodName, parameters), parameters);
		} catch (Exception e) {
			throw new ReflectionException("failed to invoke method " + toString(target, methodName, parameters), e);
		}
	}

	public static Object invoke(Object target, Method method, Object... parameters) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (target == null || method == null) {
			return null;
		}
		return method.invoke(target, unwrapPrimitives(parameters));
	}
	
	public static Method getMethod(Object target, String methodName, Object... parameters) throws NoSuchMethodException {
		if (target == null) return null;
		Class<?>[] types = null;
		types = getParameterTypes(parameters);
		Method retMethod = MethodUtils.getMatchingAccessibleMethod(target.getClass(), methodName, types);
		if (retMethod != null) {
			return retMethod;
		} else {
			try {
				Method[] methods = target.getClass().getMethods();
				return matchMethod(methods,methodName,types);
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			throw new NoSuchMethodException("failed to get method " + toString(target, methodName, parameters));
		}
	}
	
	public static Method getInterfaceMethod(Method[] methods, String methodName, Object... parameters) throws NoSuchMethodException {
		Class<?>[] types = null;
		types = getParameterTypes(parameters);
		try {
			return matchMethod(methods,methodName,types);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		throw new NoSuchMethodException("failed to get method " +  methodName + "(" + parameters + ")");

	}
	
	public static Method matchMethod(Method[] methods, String methodName, Class<?>[] types) throws NoSuchMethodException {

		for(Method method:methods){
			if(!method.getName().equals(methodName)) continue;
			Class<?>[] parameterTypes = method.getParameterTypes();
			if(parameterTypes.length != types.length)continue;
			boolean usable = true;
			for (int i=0; i < parameterTypes.length; i++){
				Class<?> parameterType = parameterTypes[i];
				if(!isAssignable(types[i],parameterType)){
					usable = false;
					break;
				}
			}
			if(usable) return method;
		}
		throw new NoSuchMethodException();
	}
	
	public static String getMethodSignature(String beanName, String methodName, Object... parameters) {
		String mSignature = beanName + "." + methodName + "(";
		for (Class<?> clazz: getParameterTypes(parameters)) {
			mSignature+= clazz.getName() + ", ";
		}
		if (parameters.length > 0) {
			mSignature = mSignature.substring(0, mSignature.length() - 2);
		}
		mSignature+= ")";
		return mSignature;
	}
    
    private static boolean isAssignable(Class<?> objectType, Class<?> paramType) {
    	if(objectType == null || paramType.isAssignableFrom(objectType)) return true; 
    	return paramType.isPrimitive() ? autobox(paramType).isAssignableFrom(objectType) : false;
    }
		
	public static Class<?>[] getParameterTypes(Object... parameters) {
		Class<?>[] paramTypes = new Class[parameters.length];
		for(int i = 0; i < paramTypes.length; i++)
			paramTypes[i] = getParameterType(parameters[i]);
		return paramTypes;
	}

	public static Object[] unwrapPrimitives(Object... parameters) {
		Object[] params = new Object[parameters.length];
		for(int i = 0; i < parameters.length; i++)
			params[i] = unwrapPrimitive(parameters[i]);
		return params;
	}

	public static Object unwrapPrimitive(Object o) {
	    if(o == null) return null;
		if (o instanceof Primitive){
			return ((Primitive) o).getValue();
		} else {
			return o;
		}
	}
	
	public static Class<?> getParameterType(Object o){
        if(o == null) return null;
        if(o instanceof Primitive) {
        	return ((Primitive)o).getType();
        } else { 
        	return o.getClass();
        }
    }
	
	public static String toString(Object target, String methodName, Object... parameters) {
		return target.getClass().getSimpleName() + "->" + methodName + "(" + parameters + ")";
	}
	
    /**
     * This method emulates server J5SE argument autoboxing.
     * @param arg The the argument class to test for boxing. If the argument
     * <i>type</i> is primitive, it will be substituted with the corresponding
     * primitive <i>class</i> representation.
     * @return The corresponding class matching the primitive type, or the
     * original argument class, if it is not primitive.
     */
    public static Class<?> autobox(Class<?> arg) {
       return arg.isPrimitive() ?
          arg == Boolean.TYPE   ? Boolean.class   :
          arg == Byte.TYPE      ? Byte.class      :
          arg == Character.TYPE ? Character.class :
          arg == Short.TYPE     ? Short.class     :
          arg == Integer.TYPE   ? Integer.class   :
          arg == Long.TYPE      ? Long.class      :
          arg == Float.TYPE     ? Float.class     : Double.class : arg;
    }
    
    /**
     * Retrieves the name of the field that is annotated with provided annotation.
     * Annotation must define target:FIELD and retention RUNTIME.
     * @param objectClass
     * @param annotationClass
     * @return fieldName or empty String if there is no field annotated with provided annotation
     */
	public static String getAnnotatedFieldName(Class<?> objectClass, Class<? extends Annotation> annotationClass) {
		if (objectClass == null || annotationClass == null) {
			return "";
		}
		for (Field field : objectClass.getDeclaredFields()) {
			if (field.isAnnotationPresent(annotationClass)) {
				return field.getName();
			}
		}
		return getAnnotatedFieldName(objectClass.getSuperclass(), annotationClass);
	}
}
