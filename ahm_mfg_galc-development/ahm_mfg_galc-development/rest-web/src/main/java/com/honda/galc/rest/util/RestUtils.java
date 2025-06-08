package com.honda.galc.rest.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.springframework.aop.framework.Advised;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.Primitive;
import com.honda.galc.util.ReflectionUtils;

/**
 * @author Subu Kathiresan
 * @date Feb 23, 2015
 */
public class RestUtils {

	public static final String LOGGER_ID = "RestService";
	private static final String SPRING_AOP_PACKAGE = "org.springframework.aop";
	
	/**
	 * Checks if method requested is available in the Dao/Service interface hierarchy.
	 * Only these methods will be available for Rest Service consumption.  All others
	 * will not be exposed. 
	 * 
	 * In contrast, ReflectionUtils.getMethod() is designed to fetch any method that
	 * is accessible. Hence the existence of this method.
	 * @param bean
	 * @param methodName
	 * @param parameters
	 * @return
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("unchecked")
	public static Method getMethod(Object bean, String methodName, Object[] parameters) {
		Class<?> cls = getTargetClass(bean);
		List<Class<?>> interfaces = ClassUtils.getAllInterfaces(cls);
		for (Class<?> iFace: interfaces) {
			if (isSpringProxyInterface(iFace)) {
				continue;
			}
			try {
				Method method = MethodUtils.getMatchingAccessibleMethod(iFace, methodName, ReflectionUtils.getParameterTypes(parameters));
				if (method != null) {
					return method;
				}
			} catch(Exception ex) {}
		}
		return null;
	}
	
	/**
	 * creates a formatted methods list with generic notations
	 * @param methods
	 * @return
	 */
	public static ArrayList<String> getFormattedMethodsList(ArrayList<Method> methods) {
		ArrayList<String> daoMethods = new ArrayList<String>();
		try {
			for (Method m: methods) {
				daoMethods.add(m.toGenericString().replace("public abstract ", ""));
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Could not create methods list");
		}
		return daoMethods;
	}
	
	/**
	 * adds parameter to list
	 * wraps primitive parameters
	 * @param param
	 * @param parameters
	 * @return
	 */
	public static void addParam(Object param, ArrayList<Object> parameters) {
		if (param instanceof PrimitiveInt) {		
			parameters.add(new Primitive(((PrimitiveInt) param).getVal()));	
		} else if (param instanceof PrimitiveFloat) {
			parameters.add(new Primitive(((PrimitiveFloat) param).getVal()));	
		} else if (param instanceof PrimitiveDouble) {
			parameters.add(new Primitive(((PrimitiveDouble) param).getVal()));
		} else if (param instanceof PrimitiveShort) {
			parameters.add(new Primitive(((PrimitiveShort) param).getVal()));
		} else if (param instanceof PrimitiveBoolean) {
			parameters.add(new Primitive(((PrimitiveBoolean) param).getVal()));
		} else if (param instanceof PrimitiveLong) {
			parameters.add(new Primitive(((PrimitiveLong) param).getVal()));
		} else if (param instanceof PrimitiveChar) {
			parameters.add(new Primitive(((PrimitiveChar) param).getVal()));
		} else{
			parameters.add(param);
		}
	}
		
	public static Class<?> getParameterClass(String clazz) {
		Class<?> parameterClass = null;
		try {
			parameterClass = Class.forName(clazz);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Parameter class " + clazz + " is not valid");
		}
		return parameterClass;
	}
	
	public static Class<?> getTargetClass(Object obj) {
		if (obj instanceof Advised)
			return ((Advised) obj).getTargetSource().getTargetClass();
		else
			return obj.getClass();
	}

	public static boolean isSpringProxyInterface(Class<?> iFace) {
		return iFace.getName().startsWith(SPRING_AOP_PACKAGE);
	}
	
	public static String getMethodSignature(String beanName, String methodName, Object[] parameters) {
		String mSignature = beanName + "." + methodName + "(";
		for (Class<?> clazz: ReflectionUtils.getParameterTypes(parameters)) {
			mSignature+= clazz.getName() + ", ";
		}
		if (parameters.length > 0) {
			mSignature = mSignature.substring(0, mSignature.length() - 2);
		}
		mSignature+= ")";
		return mSignature;
	}
	
	public static Logger getLogger(){
		return Logger.getLogger(LOGGER_ID);
	}
}
