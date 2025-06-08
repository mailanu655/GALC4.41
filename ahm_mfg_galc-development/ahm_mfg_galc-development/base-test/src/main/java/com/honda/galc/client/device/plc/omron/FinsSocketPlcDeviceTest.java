package com.honda.galc.client.device.plc.omron;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.honda.galc.test.dao.AbstractBaseTest;
import com.honda.galc.test.data.setup.Environment;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * Dec 5, 2011
 */
public class FinsSocketPlcDeviceTest extends AbstractBaseTest {
	
	private int iter;
	private int maxIterations;
	private long startTime;
	private long endTime;
	private long avgWriteTime;
	private long avgReadTime;
	private long readWriteThreshold;
	private boolean error;
	private PlcMemory testMemory;
	
	private static ArrayList<FinsSocketPlcDevice> finsSocketDevices = new ArrayList<FinsSocketPlcDevice>();
	
	public FinsSocketPlcDeviceTest() {}
	
	@BeforeClass
	public static void beforeClass() throws InterruptedException {
		activateDevice(0);
	}
	
	@AfterClass
	public static void afterClass() {
		int i = 0;
		for (FinsSocketPlcDevice device: finsSocketDevices){
			device.deActivate();
			System.out.println("\nfinsSocketDevice " + i++ + " de-activated");
		}
	}

	@Before
	public void before() {
		iter = 0;
		maxIterations = 100;
		readWriteThreshold = 300;
		startTime = 0;
		endTime = 0;
		avgWriteTime = 0;
		avgReadTime = 0;
		testMemory = new PlcMemory("E3.700");
	}
	
	@After
	public void after() {}

	private static void activateDevice(int index) throws InterruptedException {
		System.out.println("activating finsSocketDevice " + index);
		finsSocketDevices.add(index, new FinsSocketPlcDevice(getDeviceIp(), getDevicePort()));
		finsSocketDevices.get(index).activate();
		Thread.sleep(4000);
	}
	
	@Test 
	public void writeReadSingleWordTest() {
		testMemory = new PlcMemory("DM.26630");
		StringBuilder stringToWrite = new StringBuilder(" 1");
		System.out.println("\nWriting string.." + stringToWrite);
		startTime = System.currentTimeMillis();
		if (!finsSocketDevices.get(0).writeMemory(testMemory, stringToWrite))
			System.out.println("Write operation failed");

		endTime = System.currentTimeMillis();
		System.out.println(StringUtil.now("HH:mm:ss:SSS") + " Time taken to write: " + (endTime - startTime));
		StringBuilder readVal = finsSocketDevices.get(0).readMemory(testMemory);
		System.out.println("Read string.." + readVal);
		assertTrue(stringToWrite.toString().equals(readVal.toString()));
	}
	
	@Test
	public void writeReadWordsTest() {
		while (!error && iter < maxIterations) {
			try {
				System.out.println("\nWriting string.." + ++iter);
				startTime = System.currentTimeMillis();
				if (!finsSocketDevices.get(0).writeMemory(testMemory, new StringBuilder("" + iter)))
					System.out.println("Write operation failed");

				endTime = System.currentTimeMillis();
				System.out.println(StringUtil.now("HH:mm:ss:SSS") + " Time taken to write: " + (endTime - startTime));
				avgWriteTime = (avgWriteTime + endTime - startTime)/2;
				System.out.println("Average write time " + avgWriteTime);
				assertTrue(endTime - startTime < readWriteThreshold);						// write time should be less than 300 ms

				startTime = System.currentTimeMillis();
				StringBuilder readVal = finsSocketDevices.get(0).readMemory(testMemory);
				endTime = System.currentTimeMillis();
				System.out.println(StringUtil.now("HH:mm:ss:SSS") +  " Time taken to read: " + (endTime - startTime));
				avgReadTime = (avgReadTime + endTime - startTime)/2;
				System.out.println("Average read time " + avgReadTime);
				System.out.println("Read value        : " + readVal);
				assertTrue(endTime - startTime < readWriteThreshold);						// read time should be less than 300 ms

				if (iter != Integer.parseInt(readVal.toString().trim())) {
					System.out.println("Value does not match " + iter + " <> "  + readVal);
					fail();
				}
				assertEquals(iter, Integer.parseInt(readVal.toString().trim()));
			}catch(Exception ex) {
				ex.printStackTrace();
				fail();
			}
		}
	}
	
	@Test
	public void writeReadBitsTest() {
		writeReadBit(new PlcMemory("HR.100"));
		writeReadBit(new PlcMemory("E0.100"));
		writeReadBit(new PlcMemory("E1.200"));
		writeReadBit(new PlcMemory("E2.300"));
		writeReadBit(new PlcMemory("E3.400"));
		writeReadBit(new PlcMemory("E4.500"));
		writeReadBit(new PlcMemory("E5.600"));
		writeReadBit(new PlcMemory("E6.700"));
		writeReadBit(new PlcMemory("DM.100"));
		writeReadBit(new PlcMemory("DM.25480"));
	}
	
	@Test
	public void multipleConnectionsTest() throws InterruptedException {
		activateDevice(1);
		assertTrue(finsSocketDevices.get(1).isInitialized());
		System.out.println("finsSocketDevice 1 successfully created");

		activateDevice(2);
		assertTrue(finsSocketDevices.get(2).isInitialized());
		System.out.println("finsSocketDevice 2 successfully created");
		
		activateDevice(3);
		assertTrue(finsSocketDevices.get(3).isInitialized());
		System.out.println("finsSocketDevice 3 successfully created");
		
		activateDevice(4);
		assertTrue(finsSocketDevices.get(4).isInitialized());
		System.out.println("finsSocketDevice 4 successfully created");
		
		activateDevice(5);
		assertTrue(finsSocketDevices.get(5).isInitialized());
		System.out.println("finsSocketDevice 5successfully created");

		activateDevice(6);
		assertTrue(finsSocketDevices.get(6).isInitialized());
		System.out.println("finsSocketDevice 6 successfully created");
		
		activateDevice(7);
		assertTrue(finsSocketDevices.get(7).isInitialized());
		System.out.println("finsSocketDevice 7 successfully created");
		
		activateDevice(8);
		assertTrue(finsSocketDevices.get(8).isInitialized());
		System.out.println("finsSocketDevice 8 successfully created");
	}

	private void writeReadBit(PlcMemory memory) {
		finsSocketDevices.get(0).writeMemory(memory, new StringBuilder((char) 0 + (char) 0));
		System.out.println("Initial  value     " + memory.toString() + ": <" + StringUtil.stringToBitArray(finsSocketDevices.get(0).readMemory(memory)) + ">\n");
		
		setAllBitsInRegister(1, memory);
		assertEquals(finsSocketDevices.get(0).readMemory(memory).charAt(0), (char) 255);
		assertEquals(finsSocketDevices.get(0).readMemory(memory).charAt(1), (char) 255);

		setAllBitsInRegister(0, memory);
		assertEquals(finsSocketDevices.get(0).readMemory(memory).charAt(0), ((char) 0));
		assertEquals(finsSocketDevices.get(0).readMemory(memory).charAt(1), ((char) 0));
	}

	private void setAllBitsInRegister(int bitVal, PlcMemory testRegister) {
		int bit = 0;
		error = false;
		while (!error && bit < 16) {
			testRegister.setBitAddress(bit);
			try {
				System.out.println("Bit value before setting bit        " + bit + ": <" + StringUtil.stringToBitArray(finsSocketDevices.get(0).readMemory(testRegister)) + ">");
				finsSocketDevices.get(0).writeMemory(testRegister, new StringBuilder("" + bitVal)) ;
				System.out.println("Bit value after setting bit         " + bit + ": <" + StringUtil.stringToBitArray(finsSocketDevices.get(0).readMemory(testRegister)) + ">");
				testRegister.setBitAddress(-1);		
				System.out.println("Register Contents after setting bit " + bit++ + ": <" + StringUtil.stringToBitArray(finsSocketDevices.get(0).readMemory(testRegister)) + ">\n");
			}catch(Exception ex) {
				error = true;
			}
		}
	}

	private static String getDeviceIp() {
		Environment env = Enum.valueOf(Environment.class, System.getProperty("environment"));
		switch(env) {
		case LOCAL:
		case HMIN_SBX:
		case HMIN_DEV:
			return 	"10.204.42.142";		// test lab PLC
		default:
			return "Device Ip not specified";
		}
	}
	
	private static int getDevicePort() {
		Environment env = Enum.valueOf(Environment.class, System.getProperty("environment"));
		switch(env) {
		case LOCAL:
		case HMIN_SBX:
		case HMIN_DEV:
			return 	9700;		// test lab PLC
		default:
			return -1;
		}
	}
	
	protected String getTestClassName() {
		return this.getClass().getSimpleName();
	}
}
