package com.honda.doclet;

import com.honda.galc.service.property.PropertyBeanInvocationHandler;

public class PropertyBeanAttributeStruct {
	String  propertyKey;
	String  methodName;
	String  defaultValue;
	String  commentText;
	
	PropertyBeanAttributeStruct(String methodName) {
		this.propertyKey =  PropertyBeanInvocationHandler.getPropertyByMethodName(methodName);
		this.methodName = methodName;
		this.defaultValue = "";
		this.commentText = "";
	}
	
	public String toString() {
		return String.format("%s %s %s " , propertyKey, defaultValue, commentText);
	}
	
	public String toHtml() {
		return String.format("<td>%s</td><td>%s</td><td>%s</td>" , propertyKey, defaultValue, commentText);
	}
}
