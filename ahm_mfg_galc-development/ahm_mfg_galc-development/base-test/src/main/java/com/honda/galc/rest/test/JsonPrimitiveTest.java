package com.honda.galc.rest.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.honda.galc.rest.util.PrimitiveBoolean;
import com.honda.galc.rest.util.PrimitiveChar;
import com.honda.galc.rest.util.PrimitiveDouble;
import com.honda.galc.rest.util.PrimitiveFloat;
import com.honda.galc.rest.util.PrimitiveInt;
import com.honda.galc.rest.util.PrimitiveLong;
import com.honda.galc.rest.util.PrimitiveShort;
import com.honda.galc.util.Primitive;

/**
 * @author Subu Kathiresan
 * @date May 6, 2013
 */
public class JsonPrimitiveTest {

	@Test
	public void intTest() {
		PrimitiveInt PrimitiveInt = new PrimitiveInt();
		PrimitiveInt.setVal(5);
		assertEquals(int.class, new Primitive(PrimitiveInt.getVal()).getType());
	}
	
	@Test
	public void booleanTest() {
		PrimitiveBoolean jsonBool = new PrimitiveBoolean();
		jsonBool.setVal(true);
		assertEquals(boolean.class, new Primitive(jsonBool.getVal()).getType());
	}
	
	@Test
	public void shortTest() {
		PrimitiveShort PrimitiveShort = new PrimitiveShort();
		PrimitiveShort.setVal((short) 6);
		assertEquals(short.class, new Primitive(PrimitiveShort.getVal()).getType());		
	}
	
	@Test
	public void charTest() {
		PrimitiveChar PrimitiveChar = new PrimitiveChar();
		PrimitiveChar.setVal('a');
		assertEquals(char.class, new Primitive(PrimitiveChar.getVal()).getType());
	}
	
	@Test
	public void longTest() {
		PrimitiveLong PrimitiveLong = new PrimitiveLong();
		PrimitiveLong.setVal(5230820348242343434L);
		assertEquals(long.class, new Primitive(PrimitiveLong.getVal()).getType());
	}
	
	@Test
	public void floatTest() {
		PrimitiveFloat PrimitiveFloat = new PrimitiveFloat();
		PrimitiveFloat.setVal(Float.MAX_VALUE);
		assertEquals(float.class, new Primitive(PrimitiveFloat.getVal()).getType());
	}
	
	@Test
	public void doubleTest() {
		PrimitiveDouble PrimitiveDouble = new PrimitiveDouble();
		PrimitiveDouble.setVal(Double.MAX_VALUE);
		assertEquals(double.class, new Primitive(PrimitiveDouble.getVal()).getType());
	}
}
