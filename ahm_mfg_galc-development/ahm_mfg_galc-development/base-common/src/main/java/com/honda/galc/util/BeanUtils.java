package com.honda.galc.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>BeanUtils</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Karol Wozniak</TD>
 * <TD>Sep 2, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class BeanUtils {

	public static boolean safeEquals(Object o1, Object o2) {
		if (o1 == null && o2 == null) {
			return true;
		}
		if (o1 == null) {
			return false;
		}
		if (o2 == null) {
			return false;
		}
		return o1.equals(o2);
	}

	public static boolean safeEquals(String str1, String str2) {
		if (str1 == null && str2 == null) {
			return true;
		}
		if (str1 == null) {
			return false;
		}
		if (str2 == null) {
			return false;
		}
		return str1.trim().equals(str2.trim());
	}

	@SuppressWarnings("unchecked")
	public static int safeCompare(Object value1, Object value2) {
		if (value1 == null && value2 == null) {
			return 0;
		}
		if (value1 == null) {
			return -1;
		}
		if (value2 == null) {
			return 1;
		}

		if (value1 instanceof Comparable && value2 instanceof Comparable) {
			Comparable c1 = (Comparable) value1;
			Comparable c2 = (Comparable) value2;
			return c1.compareTo(c2);
		}
		return 0;
	}

	public static <T> Object getPropertyValue(T bean, String propertyName) {
		Object propertyValue = null;
		if (bean == null) {
			return propertyValue;
		}
		Method propertyAccessor = getPropertyAccessor(bean.getClass(), propertyName);
		return getPropertyValue(bean, propertyAccessor);
	}

	public static Object getNestedPropertyValue(Object bean, String propertyName) {
		Object value = null;
		if (bean == null || propertyName == null || propertyName.trim().length() == 0) {
			return value;
		}

		String[] tokens = propertyName.trim().split("\\.");
		value = bean;
		for (String token : tokens) {
			if (value == null) {
				break;
			}
			if (value instanceof Map) {
				value = ((Map<?, ?>) value).get(token);
			} else {
				Class<?> clazz = value.getClass();
				Method propertyAccessor = getPropertyAccessor(clazz, token);
				value = getPropertyValue(value, propertyAccessor);
			}
		}
		return value;
	}

	public static <T> Object getPropertyValue(T bean, Method propertyAccessor) {
		Object propertyValue = null;
		if (bean == null) {
			return propertyValue;
		}
		if (propertyAccessor == null) {
			return propertyValue;
		}
		try {
			propertyValue = propertyAccessor.invoke(bean, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return propertyValue;
	}

	public static <T> void setPropertyValue(T bean, String propertyName, Object propertyValue, Class<?>... parameterTypes) {
		if (bean == null) {
			return;
		}
		Method propertyModifier = getPropertyModifier(bean.getClass(), propertyName, parameterTypes);
		setPropertyValue(bean, propertyModifier, propertyValue);
	}

	public static <T> void setPropertyValue(T bean, Method propertyModifier, Object propertyValue) {
		if (bean == null) {
			return;
		}
		if (propertyModifier == null) {
			return;
		}
		try {
			propertyModifier.invoke(bean, new Object[] { propertyValue });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// === compare === //
	public static <T> int compare(Class<T> clazz, String propertyName, T o1, T o2) {
		int result = 0;
		try {
			Method propertyAccessor = getPropertyAccessor(clazz, propertyName);
			return compare(propertyAccessor, o1, o2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> int compare(Method propertyAccessor, T o1, T o2) {
		int result = 0;
		if (propertyAccessor == null) {
			return result;
		}
		try {
			Comparable property1 = (Comparable) propertyAccessor.invoke(o1, new Object[] {});
			Comparable property2 = (Comparable) propertyAccessor.invoke(o2, new Object[] {});
			result = safeCompare(property1, property2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static <T> int compare(List<Method> propertyAccessors, T o1, T o2) {
		int result = 0;
		if (propertyAccessors == null || propertyAccessors.size() == 0) {
			return result;
		}
		for (Method propertyAccessor : propertyAccessors) {
			result = compare(propertyAccessor, o1, o2);
			if (result != 0) {
				return result;
			}
		}
		return result;
	}

	// === get property accessor methods === //
	public static <T> Method getPropertyAccessor(Class<T> clazz, String propertyName) {
		Method method = null;
		if (clazz == null) {
			return method;
		}
		try {
			method = clazz.getMethod(getPropertyAccessorName(propertyName), new Class[] {});
		} catch (Exception e) {
			try {
				method = clazz.getMethod(getBooleanPropertyAccessorName(propertyName), new Class[] {});
			} catch (Exception e2) {
				System.err.println("Missing property, class:" + clazz + ", propertyName:" + propertyName + ".");
			}
		}
		return method;
	}

	public static <T> List<Method> getPropertyAccessors(Class<T> clazz, String[] propertyNames) {
		List<Method> propertyAccessors = new ArrayList<Method>();
		if (clazz == null || propertyNames == null || propertyNames.length == 0) {
			return propertyAccessors;
		}
		for (String propertyName : propertyNames) {
			Method propertyAccessor = getPropertyAccessor(clazz, propertyName);
			propertyAccessors.add(propertyAccessor);
		}
		return propertyAccessors;
	}

	// === get property accessor methods === //
	public static <T> Method getPropertyModifier(Class<T> clazz, String propertyName, Class<?>... parameterTypes) {
		Method method = null;
		if (clazz == null) {
			return method;
		}
		try {
			method = clazz.getMethod(getPropertyModifierName(propertyName), parameterTypes);
		} catch (Exception e) {
			System.err.println("Missing property, class:" + clazz + ", propertyName:" + propertyName + ".");
		}
		return method;
	}

	// === generate accessor names === //
	public static String getPropertyAccessorName(String propertyName) {
		return "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	}

	public static String getBooleanPropertyAccessorName(String propertyName) {
		return "is" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	}

	public static String getPropertyModifierName(String propertyName) {
		return "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	}
}
