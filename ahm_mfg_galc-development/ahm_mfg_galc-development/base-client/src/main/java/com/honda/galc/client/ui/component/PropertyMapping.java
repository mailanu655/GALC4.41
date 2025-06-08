package com.honda.galc.client.ui.component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Format;
import java.util.Map;


/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PropertyMapping</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
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
public class PropertyMapping extends ColumnMapping {

	private String propertyPath;
	private Class<?> type = String.class;
	private Format format;

	public PropertyMapping(String name, String propertyPath) {
		super(name);
		this.propertyPath = propertyPath;
	}

	public PropertyMapping(String name, String propertyPath, Class<?> type) {
		super(name, type);
		this.propertyPath = propertyPath;
	}

	public PropertyMapping(String name, String propertyPath, Format format) {
		super(name);
		this.propertyPath = propertyPath;
		this.format = format;
	}

	public PropertyMapping(String name, String propertyPath, Class<?> type, Format format) {
		super(name, type);
		this.propertyPath = propertyPath;
		this.format = format;
	}

	@SuppressWarnings("unchecked")
	public Object invoke(Object obj) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (getPropertyPath() == null || getPropertyPath().trim().length() == 0) {
			return obj;
		}
		String[] propertyNames = getPropertyPath().split("[.]");
		Object value = obj;
		for (String str : propertyNames) {
			if (value instanceof Map) {
				Map<String, Object> map = (Map<String, Object>) value;
				value = map.get(str);
			} else {
				String methodName = createGetterName(str);
				Method method = value.getClass().getMethod(methodName, (Class[]) null);
				value = method.invoke(value, (Object[]) null);
			}
		}
		if (getFormat() != null) {
			value = getFormat().format(value);
		}
		return value;
	}

	protected String createGetterName(String propertyName) {
		String prefix = boolean.class.equals(getType()) ? "is" : "get";
		String capitalizedToken = propertyName.substring(0, 1).toUpperCase();
		String reminder = propertyName.substring(1);
		String getterName = String.format("%s%s%s", prefix, capitalizedToken, reminder);
		return getterName;
	}

	// === get/set === //
	public Class<?> getType() {
		return type;
	}

	public Format getFormat() {
		return format;
	}

	public String getPropertyPath() {
		return propertyPath;
	}
}
