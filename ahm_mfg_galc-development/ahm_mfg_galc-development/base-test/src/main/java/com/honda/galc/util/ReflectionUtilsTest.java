package com.honda.galc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Stack;
import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Subu Kathiresan
 * @date Mar 29, 2013 
 * 
 */
public class ReflectionUtilsTest {
	
	public ReflectionUtilsTest() {
		
	}
		
	@Test
	public void createInstanceTest() {
		int i = 0;
		
		ReflectionDummy testObj = ReflectionUtils.createInstance(ReflectionDummy.class);
		assertNotNull(testObj);
		
		Stack<String> stack = new Stack<String>();
		stack.add("TestString");
		testObj = ReflectionUtils.createInstance(ReflectionDummy.class, new Class<?>[] {Vector.class}, stack);
		assertEquals("TestString", testObj.vector.elementAt(0));
		
		testObj = ReflectionUtils.createInstance(ReflectionDummy.class, new Primitive(i));
		assertEquals(0, testObj.intVal);
		
		testObj = ReflectionUtils.createInstance(ReflectionDummy.class, new Primitive(i), new String("stringParamA"));
		assertEquals(5, testObj.intVal);
		assertEquals("stringParamA", testObj.strVal);
		
		testObj = ReflectionUtils.createInstance(ReflectionDummy.class, new Integer(i), new String("stringParamA"));
		assertEquals(10, testObj.intVal);
		assertEquals("stringParamA", testObj.strVal);
	}
	
	@Test
	public void invokeTest(){

		ReflectionDummy testObj = new ReflectionDummy();
		int i = 1;
		assertEquals(int.class, ReflectionUtils.invoke(testObj, "testMethod", new Primitive(i)));
				
		assertEquals(Integer.class, ReflectionUtils.invoke(testObj, "testMethod", new Integer(10)));
		
		long j = 1L;
		assertEquals(long.class, ReflectionUtils.invoke(testObj, "testMethod", new Primitive(j)));
				
		assertEquals(Long.class, ReflectionUtils.invoke(testObj, "testMethod", new Long(10L)));
		
		double d = 1.1;
		assertEquals(double.class, ReflectionUtils.invoke(testObj, "testMethod", new Primitive(d)));
				
		assertEquals(Double.class, ReflectionUtils.invoke(testObj, "testMethod", new Double(9.5)));
				
		assertEquals(boolean.class, ReflectionUtils.invoke(testObj, "testMethod", new Primitive(true)));
		
		assertEquals(Boolean.class, ReflectionUtils.invoke(testObj, "testMethod", new Boolean(true)));
	}
	
	@Test
	public void getAnnotatedFieldName() {

		String superField = ReflectionUtils.getAnnotatedFieldName(SuperClaz.class, Marker.class);
		String midField = ReflectionUtils.getAnnotatedFieldName(MidClaz.class, Marker.class);
		String subField = ReflectionUtils.getAnnotatedFieldName(SubClaz.class, Marker.class);

		Assert.assertEquals(SuperClaz.class.getSimpleName(), "", superField);
		Assert.assertEquals(MidClaz.class.getSimpleName(), "firstName", midField);
		Assert.assertEquals(SubClaz.class.getSimpleName(), "firstName", subField);
		
	}

	class SuperClaz {

	}

	class MidClaz extends SuperClaz {
		@Marker
		private String firstName;
	}

	class SubClaz extends MidClaz {

	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@interface Marker {

	}	
}
