/**
 * 
 */
package com.honda.galc.client.device.plc.omron;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.honda.galc.client.device.plc.omron.PlcMemory;
import com.honda.galc.client.device.plc.RegisterLoc;

/**
 * @author Subu Kathiresan
 * @date Dec 16, 2011
 */
public class FinsMemoryTest {

	PlcMemory _plcMemory = null;
	
	@Before
	public void setup() {
		
	}
	
	@After
	public void cleanup() {
		
	}
	
	@Test
	public void createPlcMemory1() {
		_plcMemory = new PlcMemory("E3.100");
		assertEquals(_plcMemory.getMemoryLoc().name(), "E3");
		assertEquals(_plcMemory.getRegister(), 100);
	}
	
	@Test
	public void createPlcMemory2() {
		_plcMemory = new PlcMemory("E3.100.5");
		assertEquals(_plcMemory.getMemoryLoc().name(), "E3");
		assertEquals(_plcMemory.getRegister(), 100);
		assertEquals(_plcMemory.getBitAddress(), 5);
	}
	
	@Test
	public void createPlcMemory3() {
		_plcMemory = new PlcMemory("E3.100", 10);
		assertEquals(_plcMemory.getMemoryLoc().name(), "E3");
		assertEquals(_plcMemory.getRegister(), 100);
		assertEquals(_plcMemory.getNumberOfRegisters(), 10);
	}
	
	@Test
	public void createPlcMemory4() {
		_plcMemory = new PlcMemory(RegisterLoc.E3, 100, 10);
		assertEquals(_plcMemory.getMemoryLoc().name(), "E3");
		assertEquals(_plcMemory.getRegister(), 100);
		assertEquals(_plcMemory.getNumberOfRegisters(), 10);
	}
}
