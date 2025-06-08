package com.honda.galc.web;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.Transient;

import org.apache.openjpa.util.Proxy;

import com.honda.galc.common.exception.BaseException;

public class JpaOutputStream extends ObjectOutputStream {

	public JpaOutputStream() throws IOException, SecurityException {
		super();
	}
	
	 public JpaOutputStream(OutputStream out) throws IOException {
		 super(out);
		 this.enableReplaceObject(true);
	 }		
	
	public Object replaceObject(Object obj) throws IOException {
        
        return obj instanceof Proxy ? replaceProxy((Proxy)obj) : obj;
    	
    }
    
    private Object replaceProxy(Proxy obj) {
        return obj.copy(obj);
    }
    
    public void outputObject(Object object) throws IOException {
    	writeObject(detachObject(object));
    }
    
    public Object detachObject(Object object) {
    	if(object == null || object instanceof BaseException) return object;
    	Method method = getMethod(object, "writeReplace");
		if(method == null) return object;
		
		try{
			Object newObject = method.invoke(object, new Object[]{});
			for(Field field : newObject.getClass().getDeclaredFields()) {
				if(field.getAnnotation(Transient.class) == null) continue;
				Method getterMethod = getMethod(object,deriveGetMethodName(field));
				if(getterMethod == null) {
					// todo throw exception
					return object;
				}
				field.setAccessible(true);
				field.set(newObject, getterMethod.invoke(object,new Object[]{}));
			}
			return newObject;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return object;
		
    }
    private String deriveGetMethodName(Field field) {
    	String fieldName = field.getName();
    	return "get"+fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
    }
    
    private Method getMethod(Object object, String methodName) {
    	try {
			return object.getClass().getMethod(methodName, new Class[] {});
		} catch (SecurityException e) {
			
		} catch (NoSuchMethodException e) {
		}
		
		return null;
		
    }

}
