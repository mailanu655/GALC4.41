package com.honda.galc.client.vinstamp;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.honda.galc.client.device.plc.IPlcSocketDevice;
import com.honda.galc.client.device.plc.RegisterLoc;
import com.honda.galc.client.device.plc.omron.FinsSocketPlcDevice;

public class EGATests {	
	private static final boolean WORKING_WITH_EGA_PLC = false;

	private static final String EGA_VIN_STAMPING_PLC_IP = "10.49.80.200";	
//	private static final String EGA_VIN_STAMPING_PLC_IP = "xxx.xxx.xxx.xxx";	
	private static final int EGA_VIN_STAMPING_PLC_PORT = 9600;
	
//	private static final String HMIN_TEST_PLC_IP = "10.204.17.160";
	private static final String HMIN_TEST_PLC_IP = "xx.xx.xx.xx";
	private static final int HMIN_TEST_PLC_PORT = 9700;

	private static final RegisterLoc EGA_VIN_STAMPING_PLC_MEMORY_BANK = RegisterLoc.E3;
	private static final RegisterLoc HMIN_TEST_PLC_MEMORY_BANK = RegisterLoc.DM;
	
	private static final int CONFIRM_READ_STARTING_MEM_LOC = 500;
	private static final int CONFIRM_READ_MEM_BLOCK_LENGTH = 107;
	private static final int REQUEST_READ_STARTING_MEM_LOC = 100;
	private static final int REQUEST_READ_MEM_BLOCK_LENGTH = 107;
	private static final int REQUEST_READ_NEXT_VIN_STARTING_MEM_LOC = 111;
	private static final int REQUEST_READ_NEXT_VIN_MEM_BLOCK_LENGTH = 10;
	private static final int REQUEST_READ_INFO_STARTING_MEM_LOC = 186;
	private static final int REQUEST_READ_INFO_MEM_BLOCK_LENGTH = 42;
	private static final int RESULT_READ_STARTING_MEM_LOC = 300;
	private static final int RESULT_READ_MEM_BLOCK_LENGTH = 105;
	
	private static final int CONFIRM_WRITE_STARTING_MEM_LOC = 509;
	private static final int CONFIRM_WRITE_MEM_BLOCK_LENGTH = 98;
	private static final int REQUEST_WRITE_STARTING_MEM_LOC = 109;
	private static final int REQUEST_WRITE_MEM_BLOCK_LENGTH = 98;
	private static final int RESULT_WRITE_STARTING_MEM_LOC = 382;
	private static final int RESULT_WRITE_MEM_BLOCK_LENGTH = 23;
	
	private static final String VIN_INVALID = "w9c8h";
	
	private IPlcSocketDevice plcDevice;
		
	private Field[] fields;
	
	private String nextVin = "19XFA1F5XAE044970"; // 17
	
	private static final String MTOC = "ASVKAB500 R525P     L "; // 22
	private static final String PROD_LOT_KD = "HMI 01AF201003040020"; // 20
	
	public EGATests() {
		try {
			if(WORKING_WITH_EGA_PLC)
				plcDevice = new FinsSocketPlcDevice(EGA_VIN_STAMPING_PLC_IP, EGA_VIN_STAMPING_PLC_PORT);
			else
				plcDevice = new FinsSocketPlcDevice(HMIN_TEST_PLC_IP, HMIN_TEST_PLC_PORT);
	        plcDevice.activate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
//	public static void main(String[] args) {
//		boolean confirmRun = false, requestRun = true, resultRun = false;
//		
//		if(confirmRun) {
//			new Thread(
//				new Runnable() {
//					public void run() {						
//						EGATests egaTests = new EGATests();
//						egaTests.plcRead(egaTests.vinStampRequest, CONFIRM_READ_STARTING_MEM_LOC, CONFIRM_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampConfirm.getResource());
//						StringBuilder oldEqDataReady = new StringBuilder(" 0");
//						StringBuilder newEqDataReady = new StringBuilder(" 1");
//						
//						while(true) {
//							try {
//								Thread.sleep(100);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//							newEqDataReady = egaTests.plcDevice.readMemory(new PlcMemory("E3.509", 1));
//							if(newEqDataReady == null || newEqDataReady.length() != 2)
//								continue;
//							if(oldEqDataReady.charAt(1) == '0' && newEqDataReady.charAt(1) == '1') {
//								egaTests.plcRead(egaTests.vinStampRequest, CONFIRM_READ_STARTING_MEM_LOC, CONFIRM_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampConfirm.getResource());
//								System.out.println("Confirm EQ Data Ready value changed from 0 to 1. Writing information to PLC...");
//								egaTests.confirmPlcWriteValidVinTest();
//							}
//							oldEqDataReady = newEqDataReady;
//						}
//					}
//				}
//			).start();
//		}
//		
//		if(requestRun) {
//			new Thread(
//				new Runnable() {
//					public void run() {
//						EGATests egaTests = new EGATests();
////						egaTests.plcRead(egaTests.vinStampRequest, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampRequest.getResource());
////						while(true) {
////							try {
////								Thread.sleep(100);
////							} catch (InterruptedException e) {
////								e.printStackTrace();
////							}
////							newEqDataReady = egaTests.plcDevice.readMemory(new PlcMemory("E3.109", 1));
////							if(newEqDataReady == null || newEqDataReady.length() != 2)
////								continue;
////							if(oldEqDataReady.charAt(1) == '0' && newEqDataReady.charAt(1) == '1') {
//								for(int i = 0; i < 100; ++i) {
//									egaTests.plcRead(egaTests.vinStampRequest, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampRequest.getResource());
////									System.out.println("Reuqest EQ Data Ready value changed from 0 to 1. Writing information to PLC...");
//									egaTests.requestPlcWriteValidVinTest();
//								}
////							}
////							oldEqDataReady = newEqDataReady;
////						}
//					}
//				}
//			).start();
//		}
//		
//		if(resultRun) {
//			new Thread(
//				new Runnable() {
//					public void run() {
//						EGATests egaTests = new EGATests();
//						egaTests.plcRead(egaTests.vinStampResult, RESULT_READ_STARTING_MEM_LOC, RESULT_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampResult.getResource());
//						StringBuilder newEqDataReady = new StringBuilder(" 1");
//						
//						while(true) {
//							try {
//								Thread.sleep(100);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//							newEqDataReady = egaTests.plcDevice.readMemory(new PlcMemory("E3.382", 1));
//							if(newEqDataReady == null || newEqDataReady.length() != 2)
//								continue;
//							if(newEqDataReady.charAt(1) == '1') {
//								egaTests.plcRead(egaTests.vinStampResult, RESULT_READ_STARTING_MEM_LOC, RESULT_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampResult.getResource());
//								System.out.println("Result EQ Data Ready value changed from 0 to 1. Writing information to PLC...");
//								egaTests.resultPlcWriteValidVinTest();
//							}
//						}
//					}
//				}
//			).start();
//		}
//	}
//	
//	@Before
//	public void setUp() {
//		vinStampRequest = (VinStampRequest) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampConfirm.getResource());
//		vinStampResult = (VinStampResult) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampResult.getResource());
//	}
//	
//	
//	@Test
//	public void confirmPlcReadTest() {
//		System.out.println("\nEGATests - Confirm PLC Read...");
//		System.out.println("Reading from PLC..");
//		vinStampRequest = (VinStampRequest) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampConfirm.getResource());
//		plcRead(vinStampRequest, CONFIRM_READ_STARTING_MEM_LOC, CONFIRM_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampConfirm.getResource());
//		printBean(vinStampRequest);
//	}
//	
//	
//	@Test
//	public void requestPlcReadTest() {
//		System.out.println("\nEGATests - Request PLC Read...");
//		System.out.println("Reading from PLC..");
//		vinStampRequest = (VinStampRequest) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampRequest.getResource());
//		plcRead(vinStampRequest, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampRequest.getResource());
//		printBean(vinStampRequest);
//	}
//	
//	
//	@Test
//	public void resultPlcReadTest() {
//		System.out.println("\nEGATests - Result PLC Read...");
//		System.out.println("Reading from PLC..");
//		vinStampResult = (VinStampResult) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampResult.getResource());
//		plcRead(vinStampResult, RESULT_READ_STARTING_MEM_LOC, RESULT_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampResult.getResource());
//		printBean(vinStampResult);
//	}
//	
//
//	@Test
//	public void confirmPlcWriteValidVinTest() {
//		System.out.println("\nEGATests - Confirm PLC Write (Valid VIN)...");
//		
//		vinStampRequest = (VinStampRequest) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampConfirm.getResource());
//		plcRead(vinStampRequest, CONFIRM_READ_STARTING_MEM_LOC, CONFIRM_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampConfirm.getResource());
//		
//		updateWriteBean(vinStampRequest);
//		
//		String nextVin = findNextVinInList(vinStampRequest.getLastVin().trim());
//		if(nextVin != null) {
//			vinStampRequest.setNextVin(nextVin);
//			vinStampRequest.setRfidVin(nextVin + "   ");
//		}
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampRequest, PlcXmlMemoryMap.VinStampConfirm.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == CONFIRM_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				CONFIRM_WRITE_STARTING_MEM_LOC, CONFIRM_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//		
//		plcRead(vinStampRequest, CONFIRM_READ_STARTING_MEM_LOC, CONFIRM_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampConfirm.getResource());
//		
//		printBean(vinStampRequest);
//	}
//	
//	
//	@Test
//	public void requestPlcWriteValidVinTest() {
//		System.out.println("\nEGATests - Request PLC Write (Valid VIN)...");
//	
//		vinStampRequest = (VinStampRequest) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampRequest.getResource());
//		plcRead(vinStampRequest, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampRequest.getResource());
//
//		updateWriteBean(vinStampRequest);
//		
//		String nextVin = findNextVinInList(vinStampRequest.getLastVin().trim());
//		if(nextVin != null) {
//			vinStampRequest.setNextVin(nextVin);
//			vinStampRequest.setRfidVin(nextVin + "   ");
//		}
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append((PlcMemoryMapHelper.createMessage(vinStampRequest, PlcXmlMemoryMap.VinStampRequest.getResource(), PlcMemoryOperations.WRITE)));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");		
//		System.out.println(asciiToHex(plcWrite.toString()));
////		assertTrue(plcWrite.length() == REQUEST_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				REQUEST_WRITE_STARTING_MEM_LOC, REQUEST_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//		
//		plcRead(vinStampRequest, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampRequest.getResource());
//		printBean(vinStampRequest);
//	}
//	
//	@Test
//	public void resultPlcWriteValidVinTest() {
//		System.out.println("\nEGATests - Result PLC Write (Valid VIN)...");
//		
//		vinStampResult = (VinStampResult) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampResult.getResource());
//		plcRead(vinStampResult, RESULT_READ_STARTING_MEM_LOC, RESULT_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampResult.getResource());
//
//		updateWriteBean(vinStampResult);
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampResult, PlcXmlMemoryMap.VinStampResult.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");		
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == RESULT_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				RESULT_WRITE_STARTING_MEM_LOC, RESULT_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//		
//		plcRead(vinStampResult, RESULT_READ_STARTING_MEM_LOC, RESULT_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampResult.getResource());
//		printBean(vinStampResult);
//	}
//	
//	// Error Code - 31
//	@Test
//	public void confirmPlcWriteVinAlreadyRequestedTest() {
//		System.out.println("\nEGATests - Confirm PLC Write (VIN Already Processed)...");
//	
//		vinStampRequest = (VinStampRequest) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampConfirm.getResource());
//		plcRead(vinStampRequest, CONFIRM_READ_STARTING_MEM_LOC, CONFIRM_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampConfirm.getResource());
//
//		printBean(vinStampRequest);
//		
//		updateWriteBean(vinStampRequest);
//		
//		String nextVin = findNextVinInList(vinStampRequest.getLastVin().trim());
//		if(nextVin != null) {
//			vinStampRequest.setNextVin(nextVin);
//			vinStampRequest.setRfidVin(nextVin + "   ");
//		}
//		
////		vinStampRequest.setLastVin(VIN_ALREADY_REQUESTED);
//		vinStampRequest.setInfoCode("" + VinStampInfo.REQUEST_VIN_ALREADY_PROCESSED.getInfoCode());
//		vinStampRequest.setInfoMessage(VinStampInfo.REQUEST_VIN_ALREADY_PROCESSED.getInfoMessage(vinStampRequest.getLastVin()));
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampRequest, PlcXmlMemoryMap.VinStampConfirm.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == CONFIRM_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				CONFIRM_WRITE_STARTING_MEM_LOC, CONFIRM_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//		
//		plcRead(vinStampRequest, CONFIRM_READ_STARTING_MEM_LOC, CONFIRM_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampConfirm.getResource());
//		printBean(vinStampRequest);
//	}
//	
//	// Error Code - 31
//	@Test
//	public void requestPlcWriteVinAlreadyRequestedTest() {
//		System.out.println("\nEGATests - Request PLC Write (VIN Already Processed)...");
//	
//		vinStampRequest = (VinStampRequest) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampRequest.getResource());
//		plcRead(vinStampRequest, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampRequest.getResource());
//
//		printBean(vinStampRequest);
//		
//		updateWriteBean(vinStampRequest);
//		
//		String nextVin = findNextVinInList(vinStampRequest.getLastVin().trim());
//		if(nextVin != null) {
//			vinStampRequest.setNextVin(nextVin);
//			vinStampRequest.setRfidVin(nextVin + "   ");
//		}
//		
////		vinStampRequest.setLastVin(VIN_ALREADY_REQUESTED);
//		vinStampRequest.setInfoCode("" + VinStampInfo.REQUEST_VIN_ALREADY_PROCESSED.getInfoCode());
//		vinStampRequest.setInfoMessage(VinStampInfo.REQUEST_VIN_ALREADY_PROCESSED.getInfoMessage(vinStampRequest.getLastVin()));
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampRequest, PlcXmlMemoryMap.VinStampRequest.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == REQUEST_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				REQUEST_WRITE_STARTING_MEM_LOC, REQUEST_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//		
//		plcRead(vinStampRequest, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampRequest.getResource());
//		printBean(vinStampRequest);
//	}
//	
//	// Error Code - 52
//	@Test
//	public void resultPlcWriteVinAlreadyProcessedTest() {
//		System.out.println("\nEGATests - Result PLC Write (VIN Already Processed)...");
//	
//		vinStampResult = (VinStampResult) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampResult.getResource());
//		plcRead(vinStampResult, RESULT_READ_STARTING_MEM_LOC, RESULT_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampResult.getResource());
//
//		printBean(vinStampResult);
//		
//		updateWriteBean(vinStampResult);
//		
//		vinStampResult.setInfoCode("" + VinStampInfo.RESULT_VIN_ALREADY_PROCESSED.getInfoCode());
//		vinStampResult.setInfoMessage(VinStampInfo.RESULT_VIN_ALREADY_PROCESSED.getInfoMessage(vinStampResult.getStampedVin()));
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampResult, PlcXmlMemoryMap.VinStampResult.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == RESULT_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				RESULT_WRITE_STARTING_MEM_LOC, RESULT_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//		
//		plcRead(vinStampResult, RESULT_READ_STARTING_MEM_LOC, RESULT_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampResult.getResource());
//		printBean(vinStampResult);
//	}
//	
//	// Error Code = 03
//	@Test
//	public void confirmPlcWriteVinInvalidTest() {
//		System.out.println("\nEGATests - Confirm PLC Write (VIN Invalid)...");
//	
//		vinStampRequest = (VinStampRequest) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampConfirm.getResource());
//		plcRead(vinStampRequest, CONFIRM_READ_STARTING_MEM_LOC, CONFIRM_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampConfirm.getResource());
//		
//		updateWriteBean(vinStampRequest);
//		vinStampRequest.setNextVin(vinStampRequest.getLastVin().trim());
//		
//		vinStampRequest.setInfoCode("" + VinStampInfo.REQUEST_VIN_INVALID.getInfoCode());
//		vinStampRequest.setInfoMessage(VinStampInfo.REQUEST_VIN_INVALID.getInfoMessage(VIN_INVALID));
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampRequest, PlcXmlMemoryMap.VinStampRequest.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == CONFIRM_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				CONFIRM_WRITE_STARTING_MEM_LOC, CONFIRM_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//		
//		plcRead(vinStampRequest, CONFIRM_READ_STARTING_MEM_LOC, CONFIRM_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampConfirm.getResource());
//		printBean(vinStampRequest);
//	}	
//	
//	// Error Code = 03
//	@Test
//	public void requestPlcWriteVinInvalidTest() {
//		System.out.println("\nEGATests - Request PLC Write (VIN Invalid)...");
//	
//		vinStampRequest = (VinStampRequest) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampRequest.getResource());
//		plcRead(vinStampRequest, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampRequest.getResource());
//		StringBuilder plcRead = plcDevice.readMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH));
//		PlcMemoryMapHelper.updateBean(vinStampRequest, plcRead, PlcXmlMemoryMap.VinStampRequest.getResource());
//		
//		vinStampRequest.setEqDataReady(" 0");
//		vinStampRequest.setLastVin(VIN_INVALID);
//		vinStampRequest.setInfoCode("" + VinStampInfo.REQUEST_VIN_INVALID.getInfoCode());
//		vinStampRequest.setInfoMessage(VinStampInfo.REQUEST_VIN_INVALID.getInfoMessage(vinStampRequest.getLastVin()));
//		vinStampRequest.setAlcDataReady(" 1");
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampRequest, PlcXmlMemoryMap.VinStampRequest.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == REQUEST_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				REQUEST_WRITE_STARTING_MEM_LOC, REQUEST_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//		
//		plcRead(vinStampRequest, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampRequest.getResource());
//		printBean(vinStampRequest);
//	}
//	
//	// Error Code - 53
//	@Test
//	public void resultPlcWriteVinInvalidTest() {
//		System.out.println("\nEGATests - Result PLC Write (VIN Already Processed)...");
//	
//		vinStampResult = (VinStampResult) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampResult.getResource());
//		plcRead(vinStampResult, RESULT_READ_STARTING_MEM_LOC, RESULT_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampResult.getResource());
//
//		printBean(vinStampResult);
//		
//		updateWriteBean(vinStampResult);
//		
//		vinStampResult.setInfoCode("" + VinStampInfo.RESULT_VIN_INVALID.getInfoCode());
//		vinStampResult.setInfoMessage(VinStampInfo.RESULT_VIN_INVALID.getInfoMessage(vinStampResult.getStampedVin()));
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampResult, PlcXmlMemoryMap.VinStampResult.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == RESULT_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				RESULT_WRITE_STARTING_MEM_LOC, RESULT_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//		
//		plcRead(vinStampResult, RESULT_READ_STARTING_MEM_LOC, RESULT_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampResult.getResource());
//		printBean(vinStampResult);
//	}
//	
//	// Error Code - 30 (N/A per Bruce Corbett)
//	@Test
//	public void confirmPlcWriteVinNoNextTest() {
//		System.out.println("\nEGATests - Confirm PLC Write (No Next VIN)...");
//	
//		vinStampRequest = (VinStampRequest) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampConfirm.getResource());
//		plcRead(vinStampRequest, CONFIRM_READ_STARTING_MEM_LOC, CONFIRM_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampConfirm.getResource());
//		
//		updateWriteBean(vinStampRequest);
//		
//		vinStampRequest.setNextVin(vinStampRequest.getLastVin().trim());
//		vinStampRequest.setRfidVin(vinStampRequest.getLastVin().trim() + "   ");
//		
////		vinStampRequest.setLastVin(VIN_NO_NEXT);
//		vinStampRequest.setInfoCode("" + VinStampInfo.REQUEST_VIN_NO_NEXT_VIN.getInfoCode());
//		vinStampRequest.setInfoMessage(VinStampInfo.REQUEST_VIN_NO_NEXT_VIN.getInfoMessage(vinStampRequest.getLastVin()));
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampRequest, PlcXmlMemoryMap.VinStampRequest.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == CONFIRM_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				CONFIRM_WRITE_STARTING_MEM_LOC, CONFIRM_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//		
//		plcRead(vinStampRequest, CONFIRM_READ_STARTING_MEM_LOC, CONFIRM_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampConfirm.getResource());
//		printBean(vinStampRequest);
//	}
//	
//	// Error Code - 30
//	@Test
//	public void requestPlcWriteVinNoNextTest() {
//		System.out.println("\nEGATests - Request PLC Write (No Next VIN)...");
//	
//		vinStampRequest = (VinStampRequest) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampRequest.getResource());
//		plcRead(vinStampRequest, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampRequest.getResource());
//		
//		updateWriteBean(vinStampRequest);
//		
//		vinStampRequest.setNextVin(vinStampRequest.getLastVin().trim());
//		vinStampRequest.setRfidVin(vinStampRequest.getLastVin().trim() + "   ");
//		
////		vinStampRequest.setLastVin(VIN_NO_NEXT);
//		vinStampRequest.setInfoCode("" + VinStampInfo.REQUEST_VIN_NO_NEXT_VIN.getInfoCode());
//		vinStampRequest.setInfoMessage(VinStampInfo.REQUEST_VIN_NO_NEXT_VIN.getInfoMessage(vinStampRequest.getLastVin()));
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampRequest, PlcXmlMemoryMap.VinStampRequest.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == REQUEST_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				REQUEST_WRITE_STARTING_MEM_LOC, REQUEST_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//		
//		plcRead(vinStampRequest, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampRequest.getResource());
//		printBean(vinStampRequest);
//	}
//	
//	// Error Code - 20
//	@Test
//	public void confirmPlcWriteRfidNg() {
//		System.out.println("\nEGATests - Request PLC Write (RFID Data NG)...");
//	
//		vinStampRequest = (VinStampRequest) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampConfirm.getResource());
//		plcRead(vinStampRequest, CONFIRM_READ_STARTING_MEM_LOC, CONFIRM_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampConfirm.getResource());
//
//		updateWriteBean(vinStampRequest);
//		
//		String nextVin = findNextVinInList(vinStampRequest.getLastVin().trim());
//		if(nextVin != null) {
//			vinStampRequest.setNextVin(nextVin);
//			vinStampRequest.setRfidVin(nextVin + "   ");
//		}
//		
//		vinStampRequest.setInfoCode("" + VinStampInfo.REQUEST_VIN_RFID_NG.getInfoCode());
//		vinStampRequest.setInfoMessage(VinStampInfo.REQUEST_VIN_RFID_NG.getInfoMessage(nextVin));
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampRequest, PlcXmlMemoryMap.VinStampConfirm.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == CONFIRM_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				CONFIRM_WRITE_STARTING_MEM_LOC, CONFIRM_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//		
//		plcRead(vinStampRequest, CONFIRM_READ_STARTING_MEM_LOC, CONFIRM_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampConfirm.getResource());
//		printBean(vinStampRequest);
//	}
//	
//	// Error Code - 20
//	@Test
//	public void requestPlcWriteRfidNg() {
//		System.out.println("\nEGATests - Request PLC Write (RFID Data NG)...");
//	
//		vinStampRequest = (VinStampRequest) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampRequest.getResource());
//		plcRead(vinStampRequest, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampRequest.getResource());
//
//		updateWriteBean(vinStampRequest);
//		
//		String nextVin = findNextVinInList(vinStampRequest.getLastVin().trim());
//		if(nextVin != null) {
//			vinStampRequest.setNextVin(nextVin);
//			vinStampRequest.setRfidVin(nextVin + "   ");
//		}
//		
//		vinStampRequest.setInfoCode("" + VinStampInfo.REQUEST_VIN_RFID_NG.getInfoCode());
//		vinStampRequest.setInfoMessage(VinStampInfo.REQUEST_VIN_RFID_NG.getInfoMessage(nextVin));
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampRequest, PlcXmlMemoryMap.VinStampRequest.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == REQUEST_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				REQUEST_WRITE_STARTING_MEM_LOC, REQUEST_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//		
//		plcRead(vinStampRequest, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampRequest.getResource());
//		printBean(vinStampRequest);
//	}
//	
//	// Error Code - 70
//	@Test
//	public void resultPlcWriteRfidNg() {
//		System.out.println("\nEGATests - Result PLC Write (RFID Data NG)...");
//		
//		vinStampResult = (VinStampResult) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampResult.getResource());
//		plcRead(vinStampResult, RESULT_READ_STARTING_MEM_LOC, RESULT_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampResult.getResource());
//
//		updateWriteBean(vinStampResult);
//		
//		vinStampResult.setInfoCode("" + VinStampInfo.RESULT_VIN_RFID_NG.getInfoCode());
//		vinStampResult.setInfoMessage(VinStampInfo.RESULT_VIN_RFID_NG.getInfoMessage(vinStampResult.getStampedVin()));
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampResult, PlcXmlMemoryMap.VinStampResult.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == RESULT_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				RESULT_WRITE_STARTING_MEM_LOC, RESULT_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//		
//		plcRead(vinStampResult, RESULT_READ_STARTING_MEM_LOC, RESULT_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampResult.getResource());
//		printBean(vinStampResult);
//	}
//		
//	// Error Code - 04
//	@Test
//	public void confirmPlcWriteVinSkippedMultiple() {
//		System.out.println("\nEGATests - Confirm PLC Write (Multiple VINs Skipped)...");
//	
//		vinStampRequest = (VinStampRequest) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampConfirm.getResource());
//		plcRead(vinStampRequest, CONFIRM_READ_STARTING_MEM_LOC, CONFIRM_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampConfirm.getResource());
//
//		updateWriteBean(vinStampRequest);
//		
//		String firstSkippedVin = findNextVinInList(vinStampRequest.getLastVin().trim());
//		String actualNextVin = findNextVinInList(findNextVinInList(findNextVinInList(findNextVinInList(vinStampRequest.getLastVin().trim())).trim()).trim()).trim();
//		String lastSkippedVin = findPreviousVinInList(actualNextVin).trim();
//		
//		if(actualNextVin != null) {
//			vinStampRequest.setNextVin(actualNextVin);
//			vinStampRequest.setRfidVin(actualNextVin + "   ");
//		}
//		
//		vinStampRequest.setInfoCode("" + VinStampInfo.REQUEST_VIN_SKIPPED.getInfoCode());
//		vinStampRequest.setInfoMessage(VinStampInfo.REQUEST_VIN_SKIPPED.getInfoMessage(firstSkippedVin + " - " +
//				lastSkippedVin.substring(lastSkippedVin.length() - 4, lastSkippedVin.length())));
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampRequest, PlcXmlMemoryMap.VinStampConfirm.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == CONFIRM_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				CONFIRM_WRITE_STARTING_MEM_LOC, CONFIRM_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//
//		plcRead(vinStampRequest, CONFIRM_READ_STARTING_MEM_LOC, CONFIRM_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampConfirm.getResource());
//		printBean(vinStampRequest);
//	}
//	
//	// Error Code - 04
//	@Test
//	public void requestPlcWriteVinSkippedMultiple() {
//		System.out.println("\nEGATests - Request PLC Write (Multiple VINs Skipped)...");
//	
//		vinStampRequest = (VinStampRequest) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampRequest.getResource());
//		plcRead(vinStampRequest, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampRequest.getResource());
//
//		updateWriteBean(vinStampRequest);
//		
//		String firstSkippedVin = findNextVinInList(vinStampRequest.getLastVin().trim());
//		String actualNextVin = findNextVinInList(findNextVinInList(findNextVinInList(findNextVinInList(vinStampRequest.getLastVin().trim())).trim()).trim()).trim();
//		String lastSkippedVin = findPreviousVinInList(actualNextVin).trim();
//		
//		if(actualNextVin != null) {
//			vinStampRequest.setNextVin(actualNextVin);
//			vinStampRequest.setRfidVin(actualNextVin + "   ");
//		}
//		
//		vinStampRequest.setInfoCode("" + VinStampInfo.REQUEST_VIN_SKIPPED.getInfoCode());
//		vinStampRequest.setInfoMessage(VinStampInfo.REQUEST_VIN_SKIPPED.getInfoMessage(firstSkippedVin + " - " +
//				lastSkippedVin.substring(lastSkippedVin.length() - 4, lastSkippedVin.length())));
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampRequest, PlcXmlMemoryMap.VinStampRequest.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == REQUEST_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				REQUEST_WRITE_STARTING_MEM_LOC, REQUEST_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//
//		plcRead(vinStampRequest, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampRequest.getResource());
//		printBean(vinStampRequest);
//	}
//	
//	// Error Code - 55
//	@Test
//	public void resultPlcWriteVinSkippedMultiple() {
//		System.out.println("\nEGATests - Result PLC Write (Multiple VINs Skipped)...");
//		
//		vinStampResult = (VinStampResult) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampResult.getResource());
//		plcRead(vinStampResult, RESULT_READ_STARTING_MEM_LOC, RESULT_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampResult.getResource());
//
//		updateWriteBean(vinStampResult);
//		
//		String firstSkippedVin = findNextVinInList(vinStampResult.getStampedVin().trim());
//		String actualNextVin = findNextVinInList(findNextVinInList(findNextVinInList(findNextVinInList(firstSkippedVin)).trim()).trim()).trim();
//		String lastSkippedVin = findPreviousVinInList(actualNextVin).trim();
//		
//		vinStampRequest.setInfoCode("" + VinStampInfo.RESULT_VIN_SKIPPED.getInfoCode());
//		vinStampRequest.setInfoMessage(VinStampInfo.RESULT_VIN_SKIPPED.getInfoMessage(firstSkippedVin + " - " + lastSkippedVin.substring(lastSkippedVin.length() - 4, lastSkippedVin.length())));
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampResult, PlcXmlMemoryMap.VinStampResult.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == RESULT_WRITE_MEM_BLOCK_LENGTH * 2);
//		
////		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
////				RESULT_WRITE_STARTING_MEM_LOC, RESULT_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//		
//		plcRead(vinStampResult, RESULT_READ_STARTING_MEM_LOC, RESULT_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampResult.getResource());
//		printBean(vinStampResult);
//	}	
//	
//	// Error Code - 04
//	@Test
//	public void confirmPlcWriteVinSkippedSingle() {
//		System.out.println("\nEGATests - Confirm PLC Write (Single VIN Skipped)...");
//	
//		vinStampRequest = (VinStampRequest) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampConfirm.getResource());
//		plcRead(vinStampRequest, CONFIRM_READ_STARTING_MEM_LOC, CONFIRM_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampConfirm.getResource());
//
//		updateWriteBean(vinStampRequest);
//		
//		String nextVin = findNextVinInList(vinStampRequest.getLastVin().trim());
//		if(nextVin != null) {
//			vinStampRequest.setNextVin(findNextVinInList(nextVin.trim()));
//			vinStampRequest.setRfidVin(findNextVinInList(nextVin.trim()) + "   ");
//		}
//		
//		vinStampRequest.setInfoCode("" + VinStampInfo.REQUEST_VIN_SKIPPED.getInfoCode());
//		vinStampRequest.setInfoMessage(VinStampInfo.REQUEST_VIN_SKIPPED.getInfoMessage(nextVin));
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampRequest, PlcXmlMemoryMap.VinStampConfirm.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == CONFIRM_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				CONFIRM_WRITE_STARTING_MEM_LOC, CONFIRM_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//
//		plcRead(vinStampRequest, CONFIRM_READ_STARTING_MEM_LOC, CONFIRM_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampConfirm.getResource());
//		printBean(vinStampRequest);
//	}	
//	
//	// Error Code - 04
//	@Test
//	public void requestPlcWriteVinSkippedSingle() {
//		System.out.println("\nEGATests - Request PLC Write (Single VIN Skipped)...");
//	
//		vinStampRequest = (VinStampRequest) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampRequest.getResource());
//		plcRead(vinStampRequest, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampRequest.getResource());
//
//		updateWriteBean(vinStampRequest);
//		
//		String nextVin = findNextVinInList(vinStampRequest.getLastVin().trim());
//		if(nextVin != null) {
////			nextVin = 
//			vinStampRequest.setNextVin(findNextVinInList(nextVin.trim()));
//			vinStampRequest.setRfidVin(findNextVinInList(nextVin.trim()) + "   ");
//		}
//		
//		vinStampRequest.setInfoCode("" + VinStampInfo.REQUEST_VIN_SKIPPED.getInfoCode());
//		vinStampRequest.setInfoMessage(VinStampInfo.REQUEST_VIN_SKIPPED.getInfoMessage(nextVin));
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampRequest, PlcXmlMemoryMap.VinStampRequest.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == REQUEST_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				REQUEST_WRITE_STARTING_MEM_LOC, REQUEST_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//
//		plcRead(vinStampRequest, REQUEST_READ_STARTING_MEM_LOC, REQUEST_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampRequest.getResource());
//		printBean(vinStampRequest);
//	}	
//	
//	// Error Code - 55
//	@Test
//	public void resultPlcWriteVinSkippedSingle() {
//		System.out.println("\nEGATests - Result PLC Write (Single VIN Skipped)...");
//		
//		vinStampResult = (VinStampResult) PlcMemoryMapHelper.createBean(PlcXmlMemoryMap.VinStampResult.getResource());
//		plcRead(vinStampResult, RESULT_READ_STARTING_MEM_LOC, RESULT_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampResult.getResource());
//
//		updateWriteBean(vinStampResult);
//		
//		vinStampResult.setInfoCode("" + VinStampInfo.RESULT_VIN_SKIPPED.getInfoCode());
//		vinStampResult.setInfoMessage(VinStampInfo.RESULT_VIN_SKIPPED.getInfoMessage(findNextVinInList(vinStampResult.getStampedVin().trim())));
//		
//		StringBuilder plcWrite = new StringBuilder();
//		plcWrite.append(PlcMemoryMapHelper.createMessage(vinStampResult, PlcXmlMemoryMap.VinStampResult.getResource(), PlcMemoryOperations.WRITE));
//		
//		System.out.println("PLC Write(" + new Date() + ")...");
//		System.out.println(plcWrite + "\n");
//		System.out.println(asciiToHex(plcWrite.toString()));
//		assertTrue(plcWrite.length() == RESULT_WRITE_MEM_BLOCK_LENGTH * 2);
//		
//		plcDevice.writeMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK,
//				RESULT_WRITE_STARTING_MEM_LOC, RESULT_WRITE_MEM_BLOCK_LENGTH), plcWrite);
//		
//		plcRead(vinStampResult, RESULT_READ_STARTING_MEM_LOC, RESULT_READ_MEM_BLOCK_LENGTH, PlcXmlMemoryMap.VinStampResult.getResource());
//		printBean(vinStampResult);
//	}	
//	
//	private void plcRead(AbstractPlcMemoryMap memoryMap, int memLoc, int memLength, String resource) {
//		System.out.println("PLC Read (" + new Date() + ")...");
//		StringBuilder plcRead = plcDevice.readMemory(new PlcMemory(WORKING_WITH_EGA_PLC ? EGA_VIN_STAMPING_PLC_MEMORY_BANK : HMIN_TEST_PLC_MEMORY_BANK, memLoc, memLength));
//		System.out.println(plcRead);
//		System.out.println(asciiToHex(plcRead.toString()) + "\n");
//		PlcMemoryMapHelper.updateBean(memoryMap, plcRead, resource);
//	}
//	
//	private void updateWriteBean(AbstractPlcMemoryMap memoryMap) {
//		if(memoryMap instanceof VinStampRequest) {
//			vinStampRequest.setEqDataReady(" 0"); // 2
//			
//			vinStampRequest.setBoundaryMarkLeft(" "); // 1		
//			vinStampRequest.setNextVin(nextVin); // 17
//			vinStampRequest.setBoundaryMarkRight("  "); // 2
//			
//			// RFID data
//			vinStampRequest.setTagNumber("  "); // 2
//			vinStampRequest.setTagWriteCount("  ");	// 2	
//			vinStampRequest.setBodyCount("  "); // 2
//			vinStampRequest.setControlFlag("V"); // 1
//			vinStampRequest.setFrameCode("   "); // 2
//			vinStampRequest.setRfidVin(nextVin + "   "); // 20
//			vinStampRequest.setProductionLot(PROD_LOT_KD + "  "); // 22
//			vinStampRequest.setProdLotKdQty("30 "); // 3
//			vinStampRequest.setHoodInsulator("0"); // 1
//			vinStampRequest.setTrunkLiner("0"); // 1
//			vinStampRequest.setProductSpecCode(MTOC + " "); // 23
//			vinStampRequest.setSunRoof("0"); // 1
//			vinStampRequest.setRepairLevel("0"); // 1
//			vinStampRequest.setSealerUbc(StringUtil.binaryToChar("01000110")); // 8b
//			vinStampRequest.setLasdSurfacerExt(StringUtil.binaryToChar("00010010")); //8b
//			vinStampRequest.setSurfacerIntTopcoatExt(StringUtil.binaryToChar("00111000")); // 8b
//			vinStampRequest.setTopcoatIntWax(StringUtil.binaryToChar("11001001")); // 8b
//			
//			vinStampRequest.setInfoCode("" + VinStampInfo.REQUEST_VIN_OK.getInfoCode());
//			vinStampRequest.setInfoMessage(VinStampInfo.REQUEST_VIN_OK.getInfoMessage(vinStampRequest.getLastVin().trim()));
//			
//			vinStampRequest.setAlcDataReady(" 1");
//		}
//		else {
//			vinStampResult.setEqDataReady(" 0");
//			
//			vinStampResult.setInfoCode("" + VinStampInfo.RESULT_VIN_OK.getInfoCode());
//			vinStampResult.setInfoMessage(VinStampInfo.RESULT_VIN_OK.getInfoMessage(vinStampResult.getStampedVin()));
//			
//			vinStampResult.setAlcDataReady(" 1");
//		}
//	}
//	
//	private void printBean(AbstractPlcMemoryMap memoryMap) {
//		fields = memoryMap instanceof VinStampRequest ? VinStampRequest.class.getDeclaredFields() : VinStampResult.class.getDeclaredFields();
//		for(Field field : fields) {
//			XStreamAlias annotation = field.getAnnotation(XStreamAlias.class);
//			if (annotation == null)
//				continue;
//			
//			field.setAccessible(true);
//			try {
//				System.out.println(field.getName() + " = " + field.get(memoryMap));
//			} catch (Exception exception) {
//				System.out.println(exception.getMessage());
//			}
//		}
//	}
	
	private String findNextVinInList(String vin) {
		String nextVin = null;
		
		for(String currentVin : VINS) {
			if(currentVin.equals(vin)) {
				nextVin = VINS.get(VINS.indexOf(currentVin) + 1);
				break;
			}
		}		
		return nextVin;
	}
	
	private String findPreviousVinInList(String vin) {
		String previousVin = null;
		
		for(String currentVin : VINS) {
			if(currentVin.equals(vin)) {
				previousVin = VINS.get(VINS.indexOf(currentVin) - 1);
				break;
			}
		}		
		return previousVin;
	}
	
	private String asciiToHex(String ascii) {
		char[] charArray = ascii.toCharArray();
		String result = "";
		
		for(int i = 0; i < charArray.length; ++i) {
			result += Integer.toHexString((int)charArray[i]) + "(" + charArray[i] + ")" + " ";
		}		
		return result;
	}
	
	public static final List<String> VINS = Arrays.asList(new String[]{
			"19XFA1F5XAE044970",
			"19XFA1F51AE044971",
			"19XFA1F53AE044972",
			"19XFA1F55AE044973",
			"19XFA1F57AE044974",
			"19XFA1F59AE044975",
			"19XFA1F50AE044976",
			"19XFA1F52AE044977",
			"19XFA1F54AE044978",
			"19XFA1F56AE044979",
			"19XFA1F52AE044980",
			"19XFA1F54AE044981",
			"19XFA1F56AE044982",
			"19XFA1F58AE044983",
			"19XFA1F5XAE044984",
			"19XFA1F51AE044985",
			"19XFA1F53AE044986",
			"19XFA1F55AE044987",
			"19XFA1F57AE044988",
			"19XFA1F59AE044989",
			"19XFA1F55AE044990",
			"19XFA1F57AE044991",
			"19XFA1F59AE044992",
			"19XFA1F50AE044993",
			"19XFA1F52AE044994",
			"19XFA1F54AE044995",
			"19XFA1F56AE044996",
			"19XFA1F58AE044997",
			"19XFA1F5XAE044998",
			"19XFA1F51AE044999",
			"19XFA1F52AE045000",
			"19XFA1F54AE045001",
			"19XFA1F56AE045002",
			"19XFA1F58AE045003",
			"19XFA1F5XAE045004",
			"19XFA1F51AE045005",
			"19XFA1F53AE045006",
			"19XFA1F55AE045007",
			"19XFA1F57AE045008",
			"19XFA1F59AE045009",
			"19XFA1F55AE045010",
			"19XFA1F57AE045011",
			"19XFA1F59AE045012",
			"19XFA1F50AE045013",
			"19XFA1F52AE045014",
			"19XFA1F54AE045015",
			"19XFA1F56AE045016",
			"19XFA1F58AE045017",
			"19XFA1F5XAE045018",
			"19XFA1F51AE045019",
			"19XFA1F58AE045020",
			"19XFA1F5XAE045021",
			"19XFA1F51AE045022",
			"19XFA1F53AE045023",
			"19XFA1F55AE045024",
			"19XFA1F57AE045025",
			"19XFA1F59AE045026",
			"19XFA1F50AE045027",
			"19XFA1F52AE045028",
			"19XFA1F54AE045029",
			"19XFA1F50AE045030",
			"19XFA1F52AE045031",
			"19XFA1F54AE045032",
			"19XFA1F56AE045033",
			"19XFA1F58AE045034",
			"19XFA1F5XAE045035",
			"19XFA1F51AE045036",
			"19XFA1F53AE045037",
			"19XFA1F55AE045038",
			"19XFA1F57AE045039",
			"19XFA1F53AE045040",
			"19XFA1F55AE045041",
			"19XFA1F57AE045042",
			"19XFA1F59AE045043",
			"19XFA1F50AE045044",
			"19XFA1F52AE045045",
			"19XFA1F54AE045046",
			"19XFA1F56AE045047",
			"19XFA1F58AE045048",
			"19XFA1F5XAE045049",
			"19XFA1F56AE045050",
			"19XFA1F58AE045051",
			"19XFA1F5XAE045052",
			"19XFA1F51AE045053",
			"19XFA1F53AE045054",
			"19XFA1F55AE045055",
			"19XFA1F57AE045056",
			"19XFA1F59AE045057",
			"19XFA1F50AE045058",
			"19XFA1F52AE045059",
			"19XFA1F59AE045060",
			"19XFA1F50AE045061",
			"19XFA1F52AE045062",
			"19XFA1F54AE045063",
			"19XFA1F56AE045064",
			"19XFA1F58AE045065",
			"19XFA1F5XAE045066",
			"19XFA1F51AE045067",
			"19XFA1F53AE045068",
			"19XFA1F55AE045069",
			"19XFA1F51AE045070",
			"19XFA1F53AE045071",
			"19XFA1F55AE045072",
			"19XFA1F57AE045073",
			"19XFA1F59AE045074",
			"19XFA1F50AE045075",
			"19XFA1F52AE045076",
			"19XFA1F54AE045077",
			"19XFA1F56AE045078",
			"19XFA1F58AE045079",
			"19XFA1F54AE045080",
			"19XFA1F56AE045081",
			"19XFA1F58AE045082",
			"19XFA1F5XAE045083",
			"19XFA1F51AE045084",
			"19XFA1F53AE045085",
			"19XFA1F55AE045086",
			"19XFA1F57AE045087",
			"19XFA1F59AE045088",
			"19XFA1F50AE045089",
			"19XFA1F57AE045090",
			"19XFA1F59AE045091",
			"19XFA1F50AE045092",
			"19XFA1F52AE045093",
			"19XFA1F54AE045094",
			"19XFA1F56AE045095",
			"19XFA1F58AE045096",
			"19XFA1F5XAE045097",
			"19XFA1F51AE045098",
			"19XFA1F53AE045099",
			"19XFA1F56AE045100",
			"19XFA1F58AE045101",
			"19XFA1F5XAE045102",
			"19XFA1F51AE045103",
			"19XFA1F53AE045104",
			"19XFA1F55AE045105",
			"19XFA1F57AE045106",
			"19XFA1F59AE045107",
			"19XFA1F50AE045108",
			"19XFA1F52AE045109",
			"19XFA1F59AE045110",
			"19XFA1F50AE045111",
			"19XFA1F52AE045112",
			"19XFA1F54AE045113",
			"19XFA1F56AE045114",
			"19XFA1F58AE045115",
			"19XFA1F5XAE045116",
			"19XFA1F51AE045117",
			"19XFA1F53AE045118",
			"19XFA1F55AE045119",
			"19XFA1F51AE045120",
			"19XFA1F53AE045121",
			"19XFA1F55AE045122",
			"19XFA1F57AE045123",
			"19XFA1F59AE045124",
			"19XFA1F50AE045125",
			"19XFA1F52AE045126",
			"19XFA1F54AE045127",
			"19XFA1F56AE045128",
			"19XFA1F58AE045129",
			"19XFA1F54AE045130",
			"19XFA1F56AE045131",
			"19XFA1F58AE045132",
			"19XFA1F5XAE045133",
			"19XFA1F51AE045134",
			"19XFA1F53AE045135",
			"19XFA1F55AE045136",
			"19XFA1F57AE045137",
			"19XFA1F59AE045138",
			"19XFA1F50AE045139",
			"19XFA1F57AE045140",
			"19XFA1F59AE045141",
			"19XFA1F50AE045142",
			"19XFA1F52AE045143",
			"19XFA1F54AE045144",
			"19XFA1F56AE045145",
			"19XFA1F58AE045146",
			"19XFA1F5XAE045147",
			"19XFA1F51AE045148",
			"19XFA1F53AE045149",
			"19XFA1F5XAE045150",
			"19XFA1F51AE045151",
			"19XFA1F53AE045152",
			"19XFA1F55AE045153",
			"19XFA1F57AE045154",
			"19XFA1F59AE045155",
			"19XFA1F50AE045156",
			"19XFA1F52AE045157",
			"19XFA1F54AE045158",
			"19XFA1F56AE045159",
			"19XFA1F52AE045160",
			"19XFA1F54AE045161",
			"19XFA1F56AE045162",
			"19XFA1F58AE045163",
			"19XFA1F5XAE045164",
			"19XFA1F51AE045165",
			"19XFA1F53AE045166",
			"19XFA1F55AE045167",
			"19XFA1F57AE045168",
			"19XFA1F59AE045169",
			"19XFA1F55AE045170",
			"19XFA1F57AE045171",
			"19XFA1F59AE045172",
			"19XFA1F50AE045173",
			"19XFA1F52AE045174",
			"19XFA1F54AE045175",
			"19XFA1F56AE045176",
			"19XFA1F58AE045177",
			"19XFA1F5XAE045178",
			"19XFA1F51AE045179",
			"19XFA1F58AE045180",
			"19XFA1F5XAE045181",
			"19XFA1F51AE045182",
			"19XFA1F53AE045183",
			"19XFA1F55AE045184",
			"19XFA1F57AE045185",
			"19XFA1F59AE045186",
			"19XFA1F50AE045187",
			"19XFA1F52AE045188",
			"19XFA1F54AE045189",
			"19XFA1F50AE045190",
			"19XFA1F52AE045191",
			"19XFA1F54AE045192",
			"19XFA1F56AE045193",
			"19XFA1F58AE045194",
			"19XFA1F5XAE045195",
			"19XFA1F51AE045196",
			"19XFA1F53AE045197",
			"19XFA1F55AE045198",
			"19XFA1F57AE045199",
			"19XFA1F5XAE045200",
			"19XFA1F51AE045201",
			"19XFA1F53AE045202",
			"19XFA1F55AE045203",
			"19XFA1F57AE045204",
			"19XFA1F59AE045205",
			"19XFA1F50AE045206",
			"19XFA1F52AE045207",
			"19XFA1F54AE045208",
			"19XFA1F56AE045209",
			"19XFA1F52AE045210",
			"19XFA1F54AE045211",
			"19XFA1F56AE045212",
			"19XFA1F58AE045213",
			"19XFA1F5XAE045214",
			"19XFA1F51AE045215",
			"19XFA1F53AE045216",
			"19XFA1F55AE045217",
			"19XFA1F57AE045218",
			"19XFA1F59AE045219",
			"19XFA1F55AE045220",
			"19XFA1F57AE045221",
			"19XFA1F59AE045222",
			"19XFA1F50AE045223",
			"19XFA1F52AE045224",
			"19XFA1F54AE045225",
			"19XFA1F56AE045226",
			"19XFA1F58AE045227",
			"19XFA1F5XAE045228",
			"19XFA1F51AE045229",
			"19XFA1F58AE045230",
			"19XFA1F5XAE045231",
			"19XFA1F51AE045232",
			"19XFA1F53AE045233",
			"19XFA1F55AE045234",
			"19XFA1F57AE045235",
			"19XFA1F59AE045236",
			"19XFA1F50AE045237",
			"19XFA1F52AE045238",
			"19XFA1F54AE045239",
			"19XFA1F50AE045240",
			"19XFA1F52AE045241",
			"19XFA1F54AE045242",
			"19XFA1F56AE045243",
			"19XFA1F58AE045244",
			"19XFA1F5XAE045245",
			"19XFA1F51AE045246",
			"19XFA1F53AE045247",
			"19XFA1F55AE045248",
			"19XFA1F57AE045249",
			"19XFA1F53AE045250",
			"19XFA1F55AE045251",
			"19XFA1F57AE045252",
			"19XFA1F59AE045253",
			"19XFA1F50AE045254",
			"19XFA1F52AE045255",
			"19XFA1F54AE045256",
			"19XFA1F56AE045257",
			"19XFA1F58AE045258",
			"19XFA1F5XAE045259",
			"19XFA1F56AE045260",
			"19XFA1F58AE045261",
			"19XFA1F5XAE045262",
			"19XFA1F51AE045263",
			"19XFA1F53AE045264",
			"19XFA1F55AE045265",
			"19XFA1F57AE045266",
			"19XFA1F59AE045267",
			"19XFA1F50AE045268",
			"19XFA1F52AE045269",
			"19XFA1F59AE045270",
			"19XFA1F50AE045271",
			"19XFA1F52AE045272",
			"19XFA1F54AE045273",
			"19XFA1F56AE045274",
			"19XFA1F58AE045275",
			"19XFA1F5XAE045276",
			"19XFA1F51AE045277",
			"19XFA1F53AE045278",
			"19XFA1F55AE045279",
			"19XFA1F51AE045280",
			"19XFA1F53AE045281",
			"19XFA1F55AE045282",
			"19XFA1F57AE045283",
			"19XFA1F59AE045284",
			"19XFA1F50AE045285",
			"19XFA1F52AE045286",
			"19XFA1F54AE045287",
			"19XFA1F56AE045288",
			"19XFA1F58AE045289",
			"19XFA1F54AE045290",
			"19XFA1F56AE045291",
			"19XFA1F58AE045292",
			"19XFA1F5XAE045293",
			"19XFA1F51AE045294",
			"19XFA1F53AE045295",
			"19XFA1F55AE045296",
			"19XFA1F57AE045297",
			"19XFA1F59AE045298",
			"19XFA1F50AE045299",
			"19XFA1F53AE045300",
			"19XFA1F55AE045301",
			"19XFA1F57AE045302",
			"19XFA1F59AE045303",
			"19XFA1F50AE045304",
			"19XFA1F52AE045305",
			"19XFA1F54AE045306",
			"19XFA1F56AE045307",
			"19XFA1F58AE045308",
			"19XFA1F5XAE045309",
			"19XFA1F56AE045310",
			"19XFA1F58AE045311",
			"19XFA1F5XAE045312",
			"19XFA1F51AE045313",
			"19XFA1F53AE045314",
			"19XFA1F55AE045315",
			"19XFA1F57AE045316",
			"19XFA1F59AE045317",
			"19XFA1F50AE045318",
			"19XFA1F52AE045319",
			"19XFA1F59AE045320",
			"19XFA1F50AE045321",
			"19XFA1F52AE045322",
			"19XFA1F54AE045323",
			"19XFA1F56AE045324",
			"19XFA1F58AE045325",
			"19XFA1F5XAE045326",
			"19XFA1F51AE045327",
			"19XFA1F53AE045328",
			"19XFA1F55AE045329",
			"19XFA1F55AE084566",
			"19XFA1F57AE084567",
			"19XFA1F59AE084568",
			"19XFA1F50AE084569",
			"19XFA1F57AE084570",
			"19XFA1F59AE084571",
			"19XFA1F50AE084572",
			"19XFA1F52AE084573",
			"19XFA1F54AE084574",
			"19XFA1F56AE084575",
			"19XFA1F58AE084576",
			"19XFA1F5XAE084577",
			"19XFA1F51AE084578",
			"19XFA1F53AE084579",
			"19XFA1F5XAE084580",
			"19XFA1F51AE084581",
			"19XFA1F53AE084582",
			"19XFA1F55AE084583",
			"19XFA1F57AE084584",
			"19XFA1F59AE084585",
			"19XFA1F50AE084586",
			"19XFA1F52AE084587",
			"19XFA1F54AE084588",
			"19XFA1F56AE084589",
			"19XFA1F52AE084590",
			"19XFA1F54AE084591",
			"19XFA1F56AE084592",
			"19XFA1F58AE084593",
			"19XFA1F5XAE084594",
			"19XFA1F51AE084595",
			"19XFA1F58AE084836",
			"19XFA1F5XAE084837",
			"19XFA1F51AE084838",
			"19XFA1F53AE084839",
			"19XFA1F5XAE084840",
			"19XFA1F51AE084841",
			"19XFA1F53AE084842",
			"19XFA1F55AE084843",
			"19XFA1F57AE084844",
			"19XFA1F59AE084845",
			"19XFA1F50AE084846",
			"19XFA1F52AE084847",
			"19XFA1F54AE084848",
			"19XFA1F56AE084849",
			"19XFA1F52AE084850",
			"19XFA1F54AE084851",
			"19XFA1F56AE084852",
			"19XFA1F58AE084853",
			"19XFA1F5XAE084854",
			"19XFA1F51AE084855",
			"19XFA1F53AE084856",
			"19XFA1F55AE084857",
			"19XFA1F57AE084858",
			"19XFA1F59AE084859",
			"19XFA1F55AE084860",
			"19XFA1F57AE084861",
			"19XFA1F59AE084862",
			"19XFA1F50AE084863",
			"19XFA1F52AE084864",
			"19XFA1F54AE084865",
			"19XFA1F56AE084866",
			"19XFA1F58AE084867",
			"19XFA1F5XAE084868",
			"19XFA1F51AE084869",
			"19XFA1F58AE084870",
			"19XFA1F5XAE084871",
			"19XFA1F51AE084872",
			"19XFA1F53AE084873",
			"19XFA1F55AE084874",
			"19XFA1F57AE084875",
			"19XFA1F59AE084876",
			"19XFA1F50AE084877",
			"19XFA1F52AE084878",
			"19XFA1F54AE084879",
			"19XFA1F50AE084880",
			"19XFA1F52AE084881",
			"19XFA1F54AE084882",
			"19XFA1F56AE084883",
			"19XFA1F58AE084884",
			"19XFA1F5XAE084885",
			"19XFA1F51AE084886",
			"19XFA1F53AE084887",
			"19XFA1F55AE084888",
			"19XFA1F57AE084889",
			"19XFA1F53AE084890",
			"19XFA1F55AE084891",
			"19XFA1F57AE084892",
			"19XFA1F59AE084893",
			"19XFA1F50AE084894",
			"19XFA1F52AE084895",
			"19XFA1F54AE084896",
			"19XFA1F56AE084897",
			"19XFA1F58AE084898",
			"19XFA1F5XAE084899",
			"19XFA1F52AE084900",
			"19XFA1F54AE084901",
			"19XFA1F56AE084902",
			"19XFA1F58AE084903",
			"19XFA1F5XAE084904",
			"19XFA1F51AE084905",
			"19XFA1F53AE084906",
			"19XFA1F55AE084907",
			"19XFA1F57AE084908",
			"19XFA1F59AE084909",
			"19XFA1F55AE084910",
			"19XFA1F57AE084911",
			"19XFA1F59AE084912",
			"19XFA1F50AE084913",
			"19XFA1F52AE084914",
			"19XFA1F54AE084915",
			"19XFA1F56AE084916",
			"19XFA1F58AE084917",
			"19XFA1F5XAE084918",
			"19XFA1F51AE084919",
			"19XFA1F58AE084920",
			"19XFA1F5XAE084921",
			"19XFA1F51AE084922",
			"19XFA1F53AE084923",
			"19XFA1F55AE084924",
			"19XFA1F57AE084925",
			"19XFA1F58AE085226",
			"19XFA1F5XAE085227",
			"19XFA1F51AE085228",
			"19XFA1F53AE085229",
			"19XFA1F5XAE085230",
			"19XFA1F51AE085231",
			"19XFA1F53AE085232",
			"19XFA1F55AE085233",
			"19XFA1F57AE085234",
			"19XFA1F59AE085235",
			"19XFA1F50AE085236",
			"19XFA1F52AE085237",
			"19XFA1F54AE085238",
			"19XFA1F56AE085239",
			"19XFA1F52AE085240",
			"19XFA1F54AE085241",
			"19XFA1F56AE085242",
			"19XFA1F58AE085243",
			"19XFA1F5XAE085244",
			"19XFA1F51AE085245",
			"19XFA1F53AE085246",
			"19XFA1F55AE085247",
			"19XFA1F57AE085248",
			"19XFA1F59AE085249",
			"19XFA1F55AE085250",
			"19XFA1F57AE085251",
			"19XFA1F59AE085252",
			"19XFA1F50AE085253",
			"19XFA1F52AE085254",
			"19XFA1F54AE085255",
			"19XFA1F56AE085256",
			"19XFA1F58AE085257",
			"19XFA1F5XAE085258",
			"19XFA1F51AE085259",
			"19XFA1F58AE085260",
			"19XFA1F5XAE085261",
			"19XFA1F51AE085262",
			"19XFA1F53AE085263",
			"19XFA1F55AE085264",
			"19XFA1F57AE085265",
			"19XFA1F59AE085266",
			"19XFA1F50AE085267",
			"19XFA1F52AE085268",
			"19XFA1F54AE085269",
			"19XFA1F50AE085270",
			"19XFA1F52AE085271",
			"19XFA1F54AE085272",
			"19XFA1F56AE085273",
			"19XFA1F58AE085274",
			"19XFA1F5XAE085275",
			"19XFA1F51AE085276",
			"19XFA1F53AE085277",
			"19XFA1F55AE085278",
			"19XFA1F57AE085279",
			"19XFA1F53AE085280",
			"19XFA1F55AE085281",
			"19XFA1F57AE085282",
			"19XFA1F59AE085283",
			"19XFA1F50AE085284",
			"19XFA1F52AE085285",
			"19XFA1F54AE085286",
			"19XFA1F56AE085287",
			"19XFA1F58AE085288",
			"19XFA1F5XAE085289",
			"19XFA1F56AE085290",
			"19XFA1F58AE085291",
			"19XFA1F5XAE085292",
			"19XFA1F51AE085293",
			"19XFA1F53AE085294",
			"19XFA1F55AE085295",
			"19XFA1F57AE085296",
			"19XFA1F59AE085297",
			"19XFA1F50AE085298",
			"19XFA1F52AE085299",
			"19XFA1F55AE085300",
			"19XFA1F57AE085301",
			"19XFA1F59AE085302",
			"19XFA1F50AE085303",
			"19XFA1F52AE085304",
			"19XFA1F54AE085305",
			"19XFA1F56AE085306",
			"19XFA1F58AE085307",
			"19XFA1F5XAE085308",
			"19XFA1F51AE085309",
			"19XFA1F58AE085310",
			"19XFA1F5XAE085311",
			"19XFA1F51AE085312",
			"19XFA1F53AE085313",
			"19XFA1F55AE085314",
			"19XFA1F57AE085315",
			"19XFA1F59AE085316",
			"19XFA1F50AE085317",
			"19XFA1F52AE085318",
			"19XFA1F54AE085319",
			"19XFA1F50AE085320",
			"19XFA1F52AE085321",
			"19XFA1F54AE085322",
			"19XFA1F56AE085323",
			"19XFA1F58AE085324",
			"19XFA1F5XAE085325",
			"19XFA1F51AE085326",
			"19XFA1F53AE085327",
			"19XFA1F55AE085328",
			"19XFA1F57AE085329",
			"19XFA1F53AE085330",
			"19XFA1F55AE085331",
			"19XFA1F57AE085332",
			"19XFA1F59AE085333",
			"19XFA1F50AE085334",
			"19XFA1F52AE085335",
			"19XFA1F54AE085336",
			"19XFA1F56AE085337",
			"19XFA1F58AE085338",
			"19XFA1F5XAE085339",
			"19XFA1F56AE085340",
			"19XFA1F58AE085341",
			"19XFA1F5XAE085342",
			"19XFA1F51AE085343",
			"19XFA1F53AE085344",
			"19XFA1F55AE085345",
			"19XFA1F57AE085346",
			"19XFA1F59AE085347",
			"19XFA1F50AE085348",
			"19XFA1F52AE085349",
			"19XFA1F59AE085350",
			"19XFA1F50AE085351",
			"19XFA1F52AE085352",
			"19XFA1F54AE085353",
			"19XFA1F56AE085354",
			"19XFA1F58AE085355",
			"19XFA1F5XAE085356",
			"19XFA1F51AE085357",
			"19XFA1F53AE085358",
			"19XFA1F55AE085359",
			"19XFA1F51AE085360",
			"19XFA1F53AE085361",
			"19XFA1F55AE085362",
			"19XFA1F57AE085363",
			"19XFA1F59AE085364",
			"19XFA1F50AE085365",
			"19XFA1F52AE085366",
			"19XFA1F54AE085367",
			"19XFA1F56AE085368",
			"19XFA1F58AE085369",
			"19XFA1F54AE085370",
			"19XFA1F56AE085371",
			"19XFA1F58AE085372",
			"19XFA1F5XAE085373",
			"19XFA1F51AE085374",
			"19XFA1F53AE085375",
			"19XFA1F55AE085376",
			"19XFA1F57AE085377",
			"19XFA1F59AE085378",
			"19XFA1F50AE085379",
			"19XFA1F57AE085380",
			"19XFA1F59AE085381",
			"19XFA1F50AE085382",
			"19XFA1F52AE085383",
			"19XFA1F54AE085384",
			"19XFA1F56AE085385",
			"19XFA1F58AE085386",
			"19XFA1F5XAE085387",
			"19XFA1F51AE085388",
			"19XFA1F53AE085389",
			"19XFA1F5XAE085390",
			"19XFA1F51AE085391",
			"19XFA1F53AE085392",
			"19XFA1F55AE085393",
			"19XFA1F57AE085394",
			"19XFA1F59AE085395",
			"19XFA1F50AE085396",
			"19XFA1F52AE085397",
			"19XFA1F54AE085398",
			"19XFA1F56AE085399",
			"19XFA1F59AE085400",
			"19XFA1F50AE085401",
			"19XFA1F52AE085402",
			"19XFA1F54AE085403",
			"19XFA1F56AE085404",
			"19XFA1F58AE085405",
			"19XFA1F5XAE085406",
			"19XFA1F51AE085407",
			"19XFA1F53AE085408",
			"19XFA1F55AE085409",
			"19XFA1F51AE085410",
			"19XFA1F53AE085411",
			"19XFA1F55AE085412",
			"19XFA1F57AE085413",
			"19XFA1F59AE085414",
			"19XFA1F50AE085415",
			"19XFA1F52AE085416",
			"19XFA1F54AE085417",
			"19XFA1F56AE085418",
			"19XFA1F58AE085419",
			"19XFA1F54AE085420",
			"19XFA1F56AE085421",
			"19XFA1F58AE085422",
			"19XFA1F5XAE085423",
			"19XFA1F51AE085424",
			"19XFA1F53AE085425",
			"19XFA1F55AE085426",
			"19XFA1F57AE085427",
			"19XFA1F59AE085428",
			"19XFA1F50AE085429",
			"19XFA1F57AE085430",
			"19XFA1F59AE085431",
			"19XFA1F50AE085432",
			"19XFA1F52AE085433",
			"19XFA1F54AE085434",
			"19XFA1F56AE085435",
			"19XFA1F58AE085436",
			"19XFA1F5XAE085437",
			"19XFA1F51AE085438",
			"19XFA1F53AE085439",
			"19XFA1F5XAE085440",
			"19XFA1F51AE085441",
			"19XFA1F53AE085442",
			"19XFA1F55AE085443",
			"19XFA1F57AE085444",
			"19XFA1F59AE085445",
			"19XFA1F50AE085446",
			"19XFA1F52AE085447",
			"19XFA1F54AE085448",
			"19XFA1F56AE085449",
			"19XFA1F52AE085450",
			"19XFA1F54AE085451",
			"19XFA1F56AE085452",
			"19XFA1F58AE085453",
			"19XFA1F5XAE085454",
			"19XFA1F51AE085455",
			"19XFA1F53AE085456",
			"19XFA1F55AE085457",
			"19XFA1F57AE085458",
			"19XFA1F59AE085459",
			"19XFA1F55AE085460",
			"19XFA1F57AE085461",
			"19XFA1F59AE085462",
			"19XFA1F50AE085463",
			"19XFA1F52AE085464",
			"19XFA1F54AE085465",
			"19XFA1F56AE085466",
			"19XFA1F58AE085467",
			"19XFA1F5XAE085468",
			"19XFA1F51AE085469",
			"19XFA1F58AE085470",
			"19XFA1F5XAE085471",
			"19XFA1F51AE085472",
			"19XFA1F53AE085473",
			"19XFA1F55AE085474",
			"19XFA1F57AE085475",
			"19XFA1F59AE085476",
			"19XFA1F50AE085477",
			"19XFA1F52AE085478",
			"19XFA1F54AE085479",
			"19XFA1F50AE085480",
			"19XFA1F52AE085481",
			"19XFA1F54AE085482",
			"19XFA1F56AE085483",
			"19XFA1F58AE085484",
			"19XFA1F5XAE085485",
			"19XFA1F51AE085486",
			"19XFA1F53AE085487",
			"19XFA1F55AE085488",
			"19XFA1F57AE085489",
			"19XFA1F53AE085490",
			"19XFA1F55AE085491",
			"19XFA1F57AE085492",
			"19XFA1F59AE085493",
			"19XFA1F50AE085494",
			"19XFA1F52AE085495",
			"19XFA1F54AE085496",
			"19XFA1F56AE085497",
			"19XFA1F58AE085498",
			"19XFA1F5XAE085499",
			"19XFA1F52AE085500",
			"19XFA1F54AE085501",
			"19XFA1F56AE085502",
			"19XFA1F58AE085503",
			"19XFA1F5XAE085504",
			"19XFA1F51AE085505",
			"19XFA1F53AE085506",
			"19XFA1F55AE085507",
			"19XFA1F57AE085508",
			"19XFA1F59AE085509",
			"19XFA1F55AE085510",
			"19XFA1F57AE085511",
			"19XFA1F59AE085512",
			"19XFA1F50AE085513",
			"19XFA1F52AE085514",
			"19XFA1F54AE085515",
			"19XFA1F56AE085516",
			"19XFA1F58AE085517",
			"19XFA1F5XAE085518",
			"19XFA1F51AE085519",
			"19XFA1F58AE085520",
			"19XFA1F5XAE085521",
			"19XFA1F51AE085522",
			"19XFA1F53AE085523",
			"19XFA1F55AE085524",
			"19XFA1F57AE085525",
			"19XFA1F50AE085706",
			"19XFA1F52AE085707",
			"19XFA1F54AE085708",
			"19XFA1F56AE085709",
			"19XFA1F52AE085710",
			"19XFA1F54AE085711",
			"19XFA1F56AE085712",
			"19XFA1F58AE085713",
			"19XFA1F5XAE085714",
			"19XFA1F51AE085715",
			"19XFA1F53AE085716",
			"19XFA1F55AE085717",
			"19XFA1F57AE085718",
			"19XFA1F59AE085719",
			"19XFA1F55AE085720",
			"19XFA1F57AE085721",
			"19XFA1F59AE085722",
			"19XFA1F50AE085723",
			"19XFA1F52AE085724",
			"19XFA1F54AE085725",
			"19XFA1F56AE085726",
			"19XFA1F58AE085727",
			"19XFA1F5XAE085728",
			"19XFA1F51AE085729",
			"19XFA1F58AE085730",
			"19XFA1F5XAE085731",
			"19XFA1F51AE085732",
			"19XFA1F53AE085733",
			"19XFA1F55AE085734",
			"19XFA1F57AE085735",
			"19XFA1F59AE085736",
			"19XFA1F50AE085737",
			"19XFA1F52AE085738",
			"19XFA1F54AE085739",
			"19XFA1F50AE085740",
			"19XFA1F52AE085741",
			"19XFA1F54AE085742",
			"19XFA1F56AE085743",
			"19XFA1F58AE085744",
			"19XFA1F5XAE085745",
			"19XFA1F51AE085746",
			"19XFA1F53AE085747",
			"19XFA1F55AE085748",
			"19XFA1F57AE085749",
			"19XFA1F53AE085750",
			"19XFA1F55AE085751",
			"19XFA1F57AE085752",
			"19XFA1F59AE085753",
			"19XFA1F50AE085754",
			"19XFA1F52AE085755",
			"19XFA1F54AE085756",
			"19XFA1F56AE085757",
			"19XFA1F58AE085758",
			"19XFA1F5XAE085759",
			"19XFA1F56AE085760",
			"19XFA1F58AE085761",
			"19XFA1F5XAE085762",
			"19XFA1F51AE085763",
			"19XFA1F53AE085764",
			"19XFA1F55AE085765",
			"19XFA1F57AE085766",
			"19XFA1F59AE085767",
			"19XFA1F50AE085768",
			"19XFA1F52AE085769",
			"19XFA1F59AE085770",
			"19XFA1F50AE085771",
			"19XFA1F52AE085772",
			"19XFA1F54AE085773",
			"19XFA1F56AE085774",
			"19XFA1F58AE085775",
			"19XFA1F5XAE085776",
			"19XFA1F51AE085777",
			"19XFA1F53AE085778",
			"19XFA1F55AE085779",
			"19XFA1F51AE085780",
			"19XFA1F53AE085781",
			"19XFA1F55AE085782",
			"19XFA1F57AE085783",
			"19XFA1F59AE085784",
			"19XFA1F50AE085785",
			"19XFA1F52AE085786",
			"19XFA1F54AE085787",
			"19XFA1F56AE085788",
			"19XFA1F58AE085789",
			"19XFA1F54AE085790",
			"19XFA1F56AE085791",
			"19XFA1F58AE085792",
			"19XFA1F5XAE085793",
			"19XFA1F51AE085794",
			"19XFA1F53AE085795",
			"19XFA1F55AE085796",
			"19XFA1F57AE085797",
			"19XFA1F59AE085798",
			"19XFA1F50AE085799",
			"19XFA1F53AE085800",
			"19XFA1F55AE085801",
			"19XFA1F57AE085802",
			"19XFA1F59AE085803",
			"19XFA1F50AE085804",
			"19XFA1F52AE085805",
			"19XFA1F54AE085806",
			"19XFA1F56AE085807",
			"19XFA1F58AE085808",
			"19XFA1F5XAE085809",
			"19XFA1F56AE085810",
			"19XFA1F58AE085811",
			"19XFA1F5XAE085812",
			"19XFA1F51AE085813",
			"19XFA1F53AE085814",
			"19XFA1F55AE085815",
			"19XFA1F57AE085816",
			"19XFA1F59AE085817",
			"19XFA1F50AE085818",
			"19XFA1F52AE085819",
			"19XFA1F59AE085820",
			"19XFA1F50AE085821",
			"19XFA1F52AE085822",
			"19XFA1F54AE085823",
			"19XFA1F56AE085824",
			"19XFA1F58AE085825",
			"19XFA1F5XAE085826",
			"19XFA1F51AE085827",
			"19XFA1F53AE085828",
			"19XFA1F55AE085829",
			"19XFA1F51AE085830",
			"19XFA1F53AE085831",
			"19XFA1F55AE085832",
			"19XFA1F57AE085833",
			"19XFA1F59AE085834",
			"19XFA1F50AE085835",
			"19XFA1F52AE085836",
			"19XFA1F54AE085837",
			"19XFA1F56AE085838",
			"19XFA1F58AE085839",
			"19XFA1F54AE085840",
			"19XFA1F56AE085841",
			"19XFA1F58AE085842",
			"19XFA1F5XAE085843",
			"19XFA1F51AE085844",
			"19XFA1F53AE085845",
			"19XFA1F55AE085846",
			"19XFA1F57AE085847",
			"19XFA1F59AE085848",
			"19XFA1F50AE085849",
			"19XFA1F57AE085850",
			"19XFA1F59AE085851",
			"19XFA1F50AE085852",
			"19XFA1F52AE085853",
			"19XFA1F54AE085854",
			"19XFA1F56AE085855",
			"19XFA1F58AE085856",
			"19XFA1F5XAE085857",
			"19XFA1F51AE085858",
			"19XFA1F53AE085859",
			"19XFA1F5XAE085860",
			"19XFA1F51AE085861",
			"19XFA1F53AE085862",
			"19XFA1F55AE085863",
			"19XFA1F57AE085864",
			"19XFA1F59AE085865",
			"19XFA1F50AE085866",
			"19XFA1F52AE085867",
			"19XFA1F54AE085868",
			"19XFA1F56AE085869",
			"19XFA1F52AE085870",
			"19XFA1F54AE085871",
			"19XFA1F56AE085872",
			"19XFA1F58AE085873",
			"19XFA1F5XAE085874",
			"19XFA1F51AE085875",
			"19XFA1F53AE085876",
			"19XFA1F55AE085877",
			"19XFA1F57AE085878",
			"19XFA1F59AE085879",
			"19XFA1F55AE085880",
			"19XFA1F57AE085881",
			"19XFA1F59AE085882",
			"19XFA1F50AE085883",
			"19XFA1F52AE085884",
			"19XFA1F54AE085885",
			"19XFA1F56AE085886",
			"19XFA1F58AE085887",
			"19XFA1F5XAE085888",
			"19XFA1F51AE085889",
			"19XFA1F58AE085890",
			"19XFA1F5XAE085891",
			"19XFA1F51AE085892",
			"19XFA1F53AE085893",
			"19XFA1F55AE085894",
			"19XFA1F57AE085895",
			"19XFA1F59AE085896",
			"19XFA1F50AE085897",
			"19XFA1F52AE085898",
			"19XFA1F54AE085899",
			"19XFA1F57AE085900",
			"19XFA1F59AE085901",
			"19XFA1F50AE085902",
			"19XFA1F52AE085903",
			"19XFA1F54AE085904",
			"19XFA1F56AE085905",
			"19XFA1F58AE085906",
			"19XFA1F5XAE085907",
			"19XFA1F51AE085908",
			"19XFA1F53AE085909",
			"19XFA1F5XAE085910",
			"19XFA1F51AE085911",
			"19XFA1F53AE085912",
			"19XFA1F55AE085913",
			"19XFA1F57AE085914",
			"19XFA1F59AE085915",
			"19XFA1F50AE085916",
			"19XFA1F52AE085917",
			"19XFA1F54AE085918",
			"19XFA1F56AE085919",
			"19XFA1F52AE085920",
			"19XFA1F54AE085921",
			"19XFA1F56AE085922",
			"19XFA1F58AE085923",
			"19XFA1F5XAE085924",
			"19XFA1F51AE085925",
			"19XFA1F53AE085926",
			"19XFA1F55AE085927",
			"19XFA1F57AE085928",
			"19XFA1F59AE085929",
			"19XFA1F55AE085930",
			"19XFA1F57AE085931",
			"19XFA1F59AE085932",
			"19XFA1F50AE085933",
			"19XFA1F52AE085934",
			"19XFA1F54AE085935",
			"19XFA1F56AE085936",
			"19XFA1F58AE085937",
			"19XFA1F5XAE085938",
			"19XFA1F51AE085939",
			"19XFA1F58AE085940",
			"19XFA1F5XAE085941",
			"19XFA1F51AE085942",
			"19XFA1F53AE085943",
			"19XFA1F55AE085944",
			"19XFA1F57AE085945",
			"19XFA1F59AE085946",
			"19XFA1F50AE085947",
			"19XFA1F52AE085948",
			"19XFA1F54AE085949",
			"19XFA1F50AE085950",
			"19XFA1F52AE085951",
			"19XFA1F54AE085952",
			"19XFA1F56AE085953",
			"19XFA1F58AE085954",
			"19XFA1F5XAE085955",
			"19XFA1F51AE085956",
			"19XFA1F53AE085957",
			"19XFA1F55AE085958",
			"19XFA1F57AE085959",
			"19XFA1F53AE085960",
			"19XFA1F55AE085961",
			"19XFA1F57AE085962",
			"19XFA1F59AE085963",
			"19XFA1F50AE085964",
			"19XFA1F52AE085965",
			"19XFA1F54AE085966",
			"19XFA1F56AE085967",
			"19XFA1F58AE085968",
			"19XFA1F5XAE085969",
			"19XFA1F56AE085970",
			"19XFA1F58AE085971",
			"19XFA1F5XAE085972",
			"19XFA1F51AE085973",
			"19XFA1F53AE085974",
			"19XFA1F55AE085975",
			"19XFA1F57AE085976",
			"19XFA1F59AE085977",
			"19XFA1F50AE085978",
			"19XFA1F52AE085979",
			"19XFA1F59AE085980",
			"19XFA1F50AE085981",
			"19XFA1F52AE085982",
			"19XFA1F54AE085983",
			"19XFA1F56AE085984",
			"19XFA1F58AE085985",
			"19XFA1F5XAE085986",
			"19XFA1F51AE085987",
			"19XFA1F53AE085988",
			"19XFA1F55AE085989",
			"19XFA1F51AE085990",
			"19XFA1F53AE085991",
			"19XFA1F55AE085992",
			"19XFA1F57AE085993",
			"19XFA1F59AE085994",
			"19XFA1F50AE085995",
			"19XFA1F52AE085996",
			"19XFA1F54AE085997",
			"19XFA1F56AE085998",
			"19XFA1F58AE085999",
			"19XFA1F59AE086000",
			"19XFA1F50AE086001",
			"19XFA1F52AE086002",
			"19XFA1F54AE086003",
			"19XFA1F56AE086004",
			"19XFA1F58AE086005",
			"19XFA1F5XAE086006",
			"19XFA1F51AE086007",
			"19XFA1F53AE086008",
			"19XFA1F55AE086009",
			"19XFA1F51AE086010",
			"19XFA1F53AE086011",
			"19XFA1F55AE086012",
			"19XFA1F57AE086013",
			"19XFA1F59AE086014",
			"19XFA1F50AE086015",
			"19XFA1F52AE086016",
			"19XFA1F54AE086017",
			"19XFA1F56AE086018",
			"19XFA1F58AE086019",
			"19XFA1F54AE086020",
			"19XFA1F56AE086021",
			"19XFA1F58AE086022",
			"19XFA1F5XAE086023",
			"19XFA1F51AE086024",
			"19XFA1F53AE086025",
			"19XFA1F55AE086026",
			"19XFA1F57AE086027",
			"19XFA1F59AE086028",
			"19XFA1F50AE086029",
			"19XFA1F57AE086030",
			"19XFA1F59AE086031",
			"19XFA1F50AE086032",
			"19XFA1F52AE086033",
			"19XFA1F54AE086034",
			"19XFA1F56AE086035",
			"19XFA1F58AE086036",
			"19XFA1F5XAE086037",
			"19XFA1F51AE086038",
			"19XFA1F53AE086039",
			"19XFA1F5XAE086040",
			"19XFA1F51AE086041",
			"19XFA1F53AE086042",
			"19XFA1F55AE086043",
			"19XFA1F57AE086044",
			"19XFA1F59AE086045",
			"19XFA1F50AE086046",
			"19XFA1F52AE086047",
			"19XFA1F54AE086048",
			"19XFA1F56AE086049",
			"19XFA1F52AE086050",
			"19XFA1F54AE086051",
			"19XFA1F56AE086052",
			"19XFA1F58AE086053",
			"19XFA1F5XAE086054",
			"19XFA1F51AE086055",
			"19XFA1F53AE086056",
			"19XFA1F55AE086057",
			"19XFA1F57AE086058",
			"19XFA1F59AE086059",
			"19XFA1F55AE086060",
			"19XFA1F57AE086061",
			"19XFA1F59AE086062",
			"19XFA1F50AE086063",
			"19XFA1F52AE086064",
			"19XFA1F54AE086065",
			"19XFA1F56AE086066",
			"19XFA1F58AE086067",
			"19XFA1F5XAE086068",
			"19XFA1F51AE086069",
			"19XFA1F58AE086070",
			"19XFA1F5XAE086071",
			"19XFA1F51AE086072",
			"19XFA1F53AE086073",
			"19XFA1F55AE086074",
			"19XFA1F57AE086075",
			"19XFA1F59AE086076",
			"19XFA1F50AE086077",
			"19XFA1F52AE086078",
			"19XFA1F54AE086079",
			"19XFA1F50AE086080",
			"19XFA1F52AE086081",
			"19XFA1F54AE086082",
			"19XFA1F56AE086083",
			"19XFA1F58AE086084",
			"19XFA1F5XAE086085",
			"19XFA1F51AE086086",
			"19XFA1F53AE086087",
			"19XFA1F55AE086088",
			"19XFA1F57AE086089",
			"19XFA1F53AE086090",
			"19XFA1F55AE086091",
			"19XFA1F57AE086092",
			"19XFA1F59AE086093",
			"19XFA1F50AE086094",
			"19XFA1F52AE086095",
			"19XFA1F54AE086096",
			"19XFA1F56AE086097",
			"19XFA1F58AE086098",
			"19XFA1F5XAE086099",
			"19XFA1F52AE086100",
			"19XFA1F54AE086101",
			"19XFA1F56AE086102",
			"19XFA1F58AE086103",
			"19XFA1F5XAE086104",
			"19XFA1F51AE086105",
			"19XFA1F53AE086106",
			"19XFA1F55AE086107",
			"19XFA1F57AE086108",
			"19XFA1F59AE086109",
			"19XFA1F55AE086110",
			"19XFA1F57AE086111",
			"19XFA1F59AE086112",
			"19XFA1F50AE086113",
			"19XFA1F52AE086114",
			"19XFA1F54AE086115",
			"19XFA1F56AE086116",
			"19XFA1F58AE086117",
			"19XFA1F5XAE086118",
			"19XFA1F51AE086119",
			"19XFA1F58AE086120",
			"19XFA1F5XAE086121",
			"19XFA1F51AE086122",
			"19XFA1F53AE086123",
			"19XFA1F55AE086124",
			"19XFA1F57AE086125",
			"19XFA1F59AE086126",
			"19XFA1F50AE086127",
			"19XFA1F52AE086128",
			"19XFA1F54AE086129",
			"19XFA1F50AE086130",
			"19XFA1F52AE086131",
			"19XFA1F54AE086132",
			"19XFA1F56AE086133",
			"19XFA1F58AE086134",
			"19XFA1F5XAE086135",
			"19XFA1F51AE086136",
			"19XFA1F53AE086137",
			"19XFA1F55AE086138",
			"19XFA1F57AE086139",
			"19XFA1F53AE086140",
			"19XFA1F55AE086141",
			"19XFA1F57AE086142",
			"19XFA1F59AE086143",
			"19XFA1F50AE086144",
			"19XFA1F52AE086145",
			"19XFA1F54AE086146",
			"19XFA1F56AE086147",
			"19XFA1F58AE086148",
			"19XFA1F5XAE086149",
			"19XFA1F56AE086150",
			"19XFA1F58AE086151",
			"19XFA1F5XAE086152",
			"19XFA1F51AE086153",
			"19XFA1F53AE086154",
			"19XFA1F55AE086155",
			"19XFA1F57AE086156",
			"19XFA1F59AE086157",
			"19XFA1F50AE086158",
			"19XFA1F52AE086159",
			"19XFA1F59AE086160",
			"19XFA1F50AE086161",
			"19XFA1F52AE086162",
			"19XFA1F54AE086163",
			"19XFA1F56AE086164",
			"19XFA1F58AE086165",
			"19XFA1F5XAE086166",
			"19XFA1F51AE086167",
			"19XFA1F53AE086168",
			"19XFA1F55AE086169",
			"19XFA1F51AE086170",
			"19XFA1F53AE086171",
			"19XFA1F55AE086172",
			"19XFA1F57AE086173",
			"19XFA1F59AE086174",
			"19XFA1F50AE086175",
			"19XFA1F52AE086176",
			"19XFA1F54AE086177",
			"19XFA1F56AE086178",
			"19XFA1F58AE086179",
			"19XFA1F54AE086180",
			"19XFA1F56AE086181",
			"19XFA1F58AE086182",
			"19XFA1F5XAE086183",
			"19XFA1F51AE086184",
			"19XFA1F53AE086185",
			"19XFA1F55AE086186",
			"19XFA1F57AE086187",
			"19XFA1F59AE086188",
			"19XFA1F50AE086189",
			"19XFA1F57AE086190",
			"19XFA1F59AE086191",
			"19XFA1F50AE086192",
			"19XFA1F52AE086193",
			"19XFA1F54AE086194",
			"19XFA1F56AE086195",
			"19XFA1F58AE086196",
			"19XFA1F5XAE086197",
			"19XFA1F51AE086198",
			"19XFA1F53AE086199",
			"19XFA1F56AE086200",
			"19XFA1F58AE086201",
			"19XFA1F5XAE086202",
			"19XFA1F51AE086203",
			"19XFA1F53AE086204",
			"19XFA1F55AE086205",
			"19XFA1F57AE086206",
			"19XFA1F59AE086207",
			"19XFA1F50AE086208",
			"19XFA1F52AE086209",
			"19XFA1F59AE086210",
			"19XFA1F50AE086211",
			"19XFA1F52AE086212",
			"19XFA1F54AE086213",
			"19XFA1F56AE086214",
			"19XFA1F58AE086215",
			"19XFA1F5XAE086216",
			"19XFA1F51AE086217",
			"19XFA1F53AE086218",
			"19XFA1F55AE086219",
			"19XFA1F51AE086220",
			"19XFA1F53AE086221",
			"19XFA1F55AE086222",
			"19XFA1F57AE086223",
			"19XFA1F59AE086224",
			"19XFA1F50AE086225",
			"19XFA1F52AE086226",
			"19XFA1F54AE086227",
			"19XFA1F56AE086228",
			"19XFA1F58AE086229",
			"19XFA1F54AE086230",
			"19XFA1F56AE086231",
			"19XFA1F58AE086232",
			"19XFA1F5XAE086233",
			"19XFA1F51AE086234",
			"19XFA1F53AE086235",
			"19XFA1F55AE086236",
			"19XFA1F57AE086237",
			"19XFA1F59AE086238",
			"19XFA1F50AE086239",
			"19XFA1F57AE086240",
			"19XFA1F59AE086241",
			"19XFA1F50AE086242",
			"19XFA1F52AE086243",
			"19XFA1F54AE086244",
			"19XFA1F56AE086245",
			"19XFA1F58AE086246",
			"19XFA1F5XAE086247",
			"19XFA1F51AE086248",
			"19XFA1F53AE086249",
			"19XFA1F5XAE086250",
			"19XFA1F51AE086251",
			"19XFA1F53AE086252",
			"19XFA1F55AE086253",
			"19XFA1F57AE086254",
			"19XFA1F59AE086255",
			"19XFA1F50AE086256",
			"19XFA1F52AE086257",
			"19XFA1F54AE086258",
			"19XFA1F56AE086259",
			"19XFA1F52AE086260",
			"19XFA1F54AE086261",
			"19XFA1F56AE086262",
			"19XFA1F58AE086263",
			"19XFA1F5XAE086264",
			"19XFA1F51AE086265",
			"19XFA1F53AE086266",
			"19XFA1F55AE086267",
			"19XFA1F57AE086268",
			"19XFA1F59AE086269",
			"19XFA1F55AE086270",
			"19XFA1F57AE086271",
			"19XFA1F59AE086272",
			"19XFA1F50AE086273",
			"19XFA1F52AE086274",
			"19XFA1F54AE086275",
			"19XFA1F56AE086276",
			"19XFA1F58AE086277",
			"19XFA1F5XAE086278",
			"19XFA1F51AE086279",
			"19XFA1F58AE086280",
			"19XFA1F5XAE086281",
			"19XFA1F51AE086282",
			"19XFA1F53AE086283",
			"19XFA1F55AE086284",
			"19XFA1F57AE086285",
			"19XFA1F59AE086286",
			"19XFA1F50AE086287",
			"19XFA1F52AE086288",
			"19XFA1F54AE086289",
			"19XFA1F50AE086290",
			"19XFA1F52AE086291",
			"19XFA1F54AE086292",
			"19XFA1F56AE086293",
			"19XFA1F58AE086294",
			"19XFA1F5XAE086295",
			"19XFA1F51AE086296",
			"19XFA1F53AE086297",
			"19XFA1F55AE086298",
			"19XFA1F57AE086299",
			"19XFA1F5XAE086300",
			"19XFA1F51AE086301",
			"19XFA1F53AE086302",
			"19XFA1F55AE086303",
			"19XFA1F57AE086304",
			"19XFA1F59AE086305",
			"19XFA1F50AE086306",
			"19XFA1F52AE086307",
			"19XFA1F54AE086308",
			"19XFA1F56AE086309",
			"19XFA1F52AE086310",
			"19XFA1F54AE086311",
			"19XFA1F56AE086312",
			"19XFA1F58AE086313",
			"19XFA1F5XAE086314",
			"19XFA1F51AE086315",
			"19XFA1F53AE086316",
			"19XFA1F55AE086317",
			"19XFA1F57AE086318",
			"19XFA1F59AE086319",
			"19XFA1F55AE086320",
			"19XFA1F57AE086321",
			"19XFA1F59AE086322",
			"19XFA1F50AE086323",
			"19XFA1F52AE086324",
			"19XFA1F54AE086325",
			"19XFA1F56AE086326",
			"19XFA1F58AE086327",
			"19XFA1F5XAE086328",
			"19XFA1F51AE086329",
			"19XFA1F58AE086330",
			"19XFA1F5XAE086331",
			"19XFA1F51AE086332",
			"19XFA1F53AE086333",
			"19XFA1F55AE086334",
			"19XFA1F57AE086335",
			"19XFA1F59AE086336",
			"19XFA1F50AE086337",
			"19XFA1F52AE086338",
			"19XFA1F54AE086339",
			"19XFA1F50AE086340",
			"19XFA1F52AE086341",
			"19XFA1F54AE086342",
			"19XFA1F56AE086343",
			"19XFA1F58AE086344",
			"19XFA1F5XAE086345",
			"19XFA1F51AE086346",
			"19XFA1F53AE086347",
			"19XFA1F55AE086348",
			"19XFA1F57AE086349",
			"19XFA1F53AE086350",
			"19XFA1F55AE086351",
			"19XFA1F57AE086352",
			"19XFA1F59AE086353",
			"19XFA1F50AE086354",
			"19XFA1F52AE086355",
			"19XFA1F54AE086356",
			"19XFA1F56AE086357",
			"19XFA1F58AE086358",
			"19XFA1F5XAE086359",
			"19XFA1F56AE086360",
			"19XFA1F58AE086361",
			"19XFA1F5XAE086362",
			"19XFA1F51AE086363",
			"19XFA1F53AE086364",
			"19XFA1F55AE086365",
			"19XFA1F57AE086366",
			"19XFA1F59AE086367",
			"19XFA1F50AE086368",
			"19XFA1F52AE086369",
			"19XFA1F59AE086370",
			"19XFA1F50AE086371",
			"19XFA1F52AE086372",
			"19XFA1F54AE086373",
			"19XFA1F56AE086374",
			"19XFA1F58AE086375",
			"19XFA1F5XAE086376",
			"19XFA1F51AE086377",
			"19XFA1F53AE086378",
			"19XFA1F55AE086379",
			"19XFA1F51AE086380",
			"19XFA1F53AE086381",
			"19XFA1F55AE086382",
			"19XFA1F57AE086383",
			"19XFA1F59AE086384",
			"19XFA1F50AE086385",
			"19XFA1F52AE086386",
			"19XFA1F54AE086387",
			"19XFA1F56AE086388",
			"19XFA1F58AE086389",
			"19XFA1F54AE086390",
			"19XFA1F56AE086391",
			"19XFA1F58AE086392",
			"19XFA1F5XAE086393",
			"19XFA1F51AE086394",
			"19XFA1F53AE086395",
			"19XFA1F55AE086396",
			"19XFA1F57AE086397",
			"19XFA1F59AE086398",
			"19XFA1F50AE086399",
			"19XFA1F53AE086400",
			"19XFA1F55AE086401",
			"19XFA1F57AE086402",
			"19XFA1F59AE086403",
			"19XFA1F50AE086404",
			"19XFA1F52AE086405",
			"19XFA1F54AE086406",
			"19XFA1F56AE086407",
			"19XFA1F58AE086408",
			"19XFA1F5XAE086409",
			"19XFA1F56AE086410",
			"19XFA1F58AE086411",
			"19XFA1F5XAE086412",
			"19XFA1F51AE086413",
			"19XFA1F53AE086414",
			"19XFA1F55AE086415",
			"19XFA1F57AE086416",
			"19XFA1F59AE086417",
			"19XFA1F50AE086418",
			"19XFA1F52AE086419",
			"19XFA1F59AE086420",
			"19XFA1F50AE086421",
			"19XFA1F52AE086422",
			"19XFA1F54AE086423",
			"19XFA1F56AE086424",
			"19XFA1F58AE086425",
			"19XFA1F5XAE086426",
			"19XFA1F51AE086427",
			"19XFA1F53AE086428",
			"19XFA1F55AE086429",
			"19XFA1F51AE086430",
			"19XFA1F53AE086431",
			"19XFA1F55AE086432",
			"19XFA1F57AE086433",
			"19XFA1F59AE086434",
			"19XFA1F50AE086435",
			"19XFA1F52AE086436",
			"19XFA1F54AE086437",
			"19XFA1F56AE086438",
			"19XFA1F58AE086439",
			"19XFA1F54AE086440",
			"19XFA1F56AE086441",
			"19XFA1F58AE086442",
			"19XFA1F5XAE086443",
			"19XFA1F51AE086444",
			"19XFA1F53AE086445",
			"19XFA1F55AE086446",
			"19XFA1F57AE086447",
			"19XFA1F59AE086448",
			"19XFA1F50AE086449",
			"19XFA1F57AE086450",
			"19XFA1F59AE086451",
			"19XFA1F50AE086452",
			"19XFA1F52AE086453",
			"19XFA1F54AE086454",
			"19XFA1F56AE086455",
			"19XFA1F58AE086456",
			"19XFA1F5XAE086457",
			"19XFA1F51AE086458",
			"19XFA1F53AE086459",
			"19XFA1F5XAE086460",
			"19XFA1F51AE086461",
			"19XFA1F53AE086462",
			"19XFA1F55AE086463",
			"19XFA1F57AE086464",
			"19XFA1F59AE086465",
			"19XFA1F50AE086466",
			"19XFA1F52AE086467",
			"19XFA1F54AE086468",
			"19XFA1F56AE086469",
			"19XFA1F52AE086470",
			"19XFA1F54AE086471",
			"19XFA1F56AE086472",
			"19XFA1F58AE086473",
			"19XFA1F5XAE086474",
			"19XFA1F51AE086475",
			"19XFA1F53AE086476",
			"19XFA1F55AE086477",
			"19XFA1F57AE086478",
			"19XFA1F59AE086479",
			"19XFA1F55AE086480",
			"19XFA1F57AE086481",
			"19XFA1F59AE086482",
			"19XFA1F50AE086483",
			"19XFA1F52AE086484",
			"19XFA1F54AE086485",
			"19XFA1F56AE086486",
			"19XFA1F58AE086487",
			"19XFA1F5XAE086488",
			"19XFA1F51AE086489",
			"19XFA1F58AE086490",
			"19XFA1F5XAE086491",
			"19XFA1F51AE086492",
			"19XFA1F53AE086493",
			"19XFA1F55AE086494",
			"19XFA1F57AE086495",
			"19XFA1F59AE086496",
			"19XFA1F50AE086497",
			"19XFA1F52AE086498",
			"19XFA1F54AE086499",
			"19XFA1F57AE086500",
			"19XFA1F59AE086501",
			"19XFA1F50AE086502",
			"19XFA1F52AE086503",
			"19XFA1F54AE086504",
			"19XFA1F56AE086505",
			"19XFA1F58AE086506",
			"19XFA1F5XAE086507",
			"19XFA1F51AE086508",
			"19XFA1F53AE086509",
			"19XFA1F5XAE086510",
			"19XFA1F51AE086511",
			"19XFA1F53AE086512",
			"19XFA1F55AE086513",
			"19XFA1F57AE086514",
			"19XFA1F59AE086515",
			"19XFA1F50AE086516",
			"19XFA1F52AE086517",
			"19XFA1F54AE086518",
			"19XFA1F56AE086519",
			"19XFA1F52AE086520",
			"19XFA1F54AE086521",
			"19XFA1F56AE086522",
			"19XFA1F58AE086523",
			"19XFA1F5XAE086524",
			"19XFA1F51AE086525",
			"19XFA1F53AE086526",
			"19XFA1F55AE086527",
			"19XFA1F57AE086528",
			"19XFA1F59AE086529",
			"19XFA1F55AE086530",
			"19XFA1F57AE086531",
			"19XFA1F59AE086532",
			"19XFA1F50AE086533",
			"19XFA1F52AE086534",
			"19XFA1F54AE086535",
			"19XFA1F56AE086536",
			"19XFA1F58AE086537",
			"19XFA1F5XAE086538",
			"19XFA1F51AE086539",
			"19XFA1F58AE086540",
			"19XFA1F5XAE086541",
			"19XFA1F51AE086542",
			"19XFA1F53AE086543",
			"19XFA1F55AE086544",
			"19XFA1F57AE086545",
			"19XFA1F59AE086546",
			"19XFA1F50AE086547",
			"19XFA1F52AE086548",
			"19XFA1F54AE086549",
			"19XFA1F50AE086550",
			"19XFA1F52AE086551",
			"19XFA1F54AE086552",
			"19XFA1F56AE086553",
			"19XFA1F58AE086554",
			"19XFA1F5XAE086555",
			"19XFA1F51AE086556",
			"19XFA1F53AE086557",
			"19XFA1F55AE086558",
			"19XFA1F57AE086559",
			"19XFA1F53AE086560",
			"19XFA1F55AE086561",
			"19XFA1F57AE086562",
			"19XFA1F59AE086563",
			"19XFA1F50AE086564",
			"19XFA1F52AE086565",
			"19XFA1F54AE086566",
			"19XFA1F56AE086567",
			"19XFA1F58AE086568",
			"19XFA1F5XAE086569",
			"19XFA1F56AE086570",
			"19XFA1F58AE086571",
			"19XFA1F5XAE086572",
			"19XFA1F51AE086573",
			"19XFA1F53AE086574",
			"19XFA1F55AE086575",
			"19XFA1F57AE086576",
			"19XFA1F59AE086577",
			"19XFA1F50AE086578",
			"19XFA1F52AE086579",
			"19XFA1F59AE086580",
			"19XFA1F50AE086581",
			"19XFA1F52AE086582",
			"19XFA1F54AE086583",
			"19XFA1F56AE086584",
			"19XFA1F58AE086585",
			"19XFA1F5XAE086586",
			"19XFA1F51AE086587",
			"19XFA1F53AE086588",
			"19XFA1F55AE086589",
			"19XFA1F51AE086590",
			"19XFA1F53AE086591",
			"19XFA1F55AE086592",
			"19XFA1F57AE086593",
			"19XFA1F59AE086594",
			"19XFA1F50AE086595",
			"19XFA1F52AE086596",
			"19XFA1F54AE086597",
			"19XFA1F56AE086598",
			"19XFA1F58AE086599",
			"19XFA1F50AE086600",
			"19XFA1F52AE086601",
			"19XFA1F54AE086602",
			"19XFA1F56AE086603",
			"19XFA1F58AE086604",
			"19XFA1F5XAE086605",
			"19XFA1F51AE086606",
			"19XFA1F53AE086607",
			"19XFA1F55AE086608",
			"19XFA1F57AE086609",
			"19XFA1F53AE086610",
			"19XFA1F55AE086611",
			"19XFA1F57AE086612",
			"19XFA1F59AE086613",
			"19XFA1F50AE086614",
			"19XFA1F52AE086615",
			"19XFA1F54AE086616",
			"19XFA1F56AE086617",
			"19XFA1F58AE086618",
			"19XFA1F5XAE086619",
			"19XFA1F56AE086620",
			"19XFA1F58AE086621",
			"19XFA1F5XAE086622",
			"19XFA1F51AE086623",
			"19XFA1F53AE086624",
			"19XFA1F55AE086625",
			"19XFA1F57AE086626",
			"19XFA1F59AE086627",
			"19XFA1F50AE086628",
			"19XFA1F52AE086629",
			"19XFA1F59AE086630",
			"19XFA1F50AE086631",
			"19XFA1F52AE086632",
			"19XFA1F54AE086633",
			"19XFA1F56AE086634",
			"19XFA1F58AE086635",
			"19XFA1F5XAE086636",
			"19XFA1F51AE086637",
			"19XFA1F53AE086638",
			"19XFA1F55AE086639",
			"19XFA1F51AE086640",
			"19XFA1F53AE086641",
			"19XFA1F55AE086642",
			"19XFA1F57AE086643",
			"19XFA1F59AE086644",
			"19XFA1F50AE086645",
			"19XFA1F52AE086646",
			"19XFA1F54AE086647",
			"19XFA1F56AE086648",
			"19XFA1F58AE086649",
			"19XFA1F54AE086650",
			"19XFA1F56AE086651",
			"19XFA1F58AE086652",
			"19XFA1F5XAE086653",
			"19XFA1F51AE086654",
			"19XFA1F53AE086655",
			"19XFA1F55AE086656",
			"19XFA1F57AE086657",
			"19XFA1F59AE086658",
			"19XFA1F50AE086659",
			"19XFA1F57AE086660",
			"19XFA1F59AE086661",
			"19XFA1F50AE086662",
			"19XFA1F52AE086663",
			"19XFA1F54AE086664",
			"19XFA1F56AE086665",
			"19XFA1F58AE086666",
			"19XFA1F5XAE086667",
			"19XFA1F51AE086668",
			"19XFA1F53AE086669",
			"19XFA1F5XAE086670",
			"19XFA1F51AE086671",
			"19XFA1F53AE086672",
			"19XFA1F55AE086673",
			"19XFA1F57AE086674",
			"19XFA1F59AE086675",
			"19XFA1F50AE086676",
			"19XFA1F52AE086677",
			"19XFA1F54AE086678",
			"19XFA1F56AE086679",
			"19XFA1F52AE086680",
			"19XFA1F54AE086681",
			"19XFA1F56AE086682",
			"19XFA1F58AE086683",
			"19XFA1F5XAE086684",
			"19XFA1F51AE086685",
			"19XFA1F53AE086686",
			"19XFA1F55AE086687",
			"19XFA1F57AE086688",
			"19XFA1F59AE086689",
			"19XFA1F55AE086690",
			"19XFA1F57AE086691",
			"19XFA1F59AE086692",
			"19XFA1F50AE086693",
			"19XFA1F52AE086694",
			"19XFA1F54AE086695",
			"19XFA1F56AE086696",
			"19XFA1F58AE086697",
			"19XFA1F5XAE086698",
			"19XFA1F51AE086699",
			"19XFA1F54AE086700",
			"19XFA1F56AE086701",
			"19XFA1F58AE086702",
			"19XFA1F5XAE086703",
			"19XFA1F51AE086704",
			"19XFA1F53AE086705",
			"19XFA1F55AE086706",
			"19XFA1F57AE086707",
			"19XFA1F59AE086708",
			"19XFA1F50AE086709",
			"19XFA1F57AE086710",
			"19XFA1F59AE086711",
			"19XFA1F50AE086712",
			"19XFA1F52AE086713",
			"19XFA1F54AE086714",
			"19XFA1F56AE086715",
			"19XFA1F58AE086716",
			"19XFA1F5XAE086717",
			"19XFA1F51AE086718",
			"19XFA1F53AE086719",
			"19XFA1F5XAE086720",
			"19XFA1F51AE086721",
			"19XFA1F53AE086722",
			"19XFA1F55AE086723",
			"19XFA1F57AE086724",
			"19XFA1F59AE086725",
			"19XFA1F50AE086726",
			"19XFA1F52AE086727",
			"19XFA1F54AE086728",
			"19XFA1F56AE086729",
			"19XFA1F52AE086730",
			"19XFA1F54AE086731",
			"19XFA1F56AE086732",
			"19XFA1F58AE086733",
			"19XFA1F5XAE086734",
			"19XFA1F51AE086735",
			"19XFA1F53AE086736",
			"19XFA1F55AE086737",
			"19XFA1F57AE086738",
			"19XFA1F59AE086739",
			"19XFA1F55AE086740",
			"19XFA1F57AE086741",
			"19XFA1F59AE086742",
			"19XFA1F50AE086743",
			"19XFA1F52AE086744",
			"19XFA1F54AE086745",
			"19XFA1F56AE086746",
			"19XFA1F58AE086747",
			"19XFA1F5XAE086748",
			"19XFA1F51AE086749",
			"19XFA1F58AE086750",
			"19XFA1F5XAE086751",
			"19XFA1F51AE086752",
			"19XFA1F53AE086753",
			"19XFA1F55AE086754",
			"19XFA1F57AE086755",
			"19XFA1F52AE086906",
			"19XFA1F54AE086907",
			"19XFA1F56AE086908",
			"19XFA1F58AE086909",
			"19XFA1F54AE086910",
			"19XFA1F56AE086911",
			"19XFA1F58AE086912",
			"19XFA1F5XAE086913",
			"19XFA1F51AE086914",
			"19XFA1F53AE086915",
			"19XFA1F55AE086916",
			"19XFA1F57AE086917",
			"19XFA1F59AE086918",
			"19XFA1F50AE086919",
			"19XFA1F57AE086920",
			"19XFA1F59AE086921",
			"19XFA1F50AE086922",
			"19XFA1F52AE086923",
			"19XFA1F54AE086924",
			"19XFA1F56AE086925",
			"19XFA1F58AE086926",
			"19XFA1F5XAE086927",
			"19XFA1F51AE086928",
			"19XFA1F53AE086929",
			"19XFA1F5XAE086930",
			"19XFA1F51AE086931",
			"19XFA1F53AE086932",
			"19XFA1F55AE086933",
			"19XFA1F57AE086934",
			"19XFA1F59AE086935",
			"19XFA1F50AE086936",
			"19XFA1F52AE086937",
			"19XFA1F54AE086938",
			"19XFA1F56AE086939",
			"19XFA1F52AE086940",
			"19XFA1F54AE086941",
			"19XFA1F56AE086942",
			"19XFA1F58AE086943",
			"19XFA1F5XAE086944",
			"19XFA1F51AE086945",
			"19XFA1F53AE086946",
			"19XFA1F55AE086947",
			"19XFA1F57AE086948",
			"19XFA1F59AE086949",
			"19XFA1F55AE086950",
			"19XFA1F57AE086951",
			"19XFA1F59AE086952",
			"19XFA1F50AE086953",
			"19XFA1F52AE086954",
			"19XFA1F54AE086955",
			"19XFA1F56AE086956",
			"19XFA1F58AE086957",
			"19XFA1F5XAE086958",
			"19XFA1F51AE086959",
			"19XFA1F58AE086960",
			"19XFA1F5XAE086961",
			"19XFA1F51AE086962",
			"19XFA1F53AE086963",
			"19XFA1F55AE086964",
			"19XFA1F57AE086965",
			"19XFA1F59AE086966",
			"19XFA1F50AE086967",
			"19XFA1F52AE086968",
			"19XFA1F54AE086969",
			"19XFA1F50AE086970",
			"19XFA1F52AE086971",
			"19XFA1F54AE086972",
			"19XFA1F56AE086973",
			"19XFA1F58AE086974",
			"19XFA1F5XAE086975",
			"19XFA1F51AE086976",
			"19XFA1F53AE086977",
			"19XFA1F55AE086978",
			"19XFA1F57AE086979",
			"19XFA1F53AE086980",
			"19XFA1F55AE086981",
			"19XFA1F57AE086982",
			"19XFA1F59AE086983",
			"19XFA1F50AE086984",
			"19XFA1F52AE086985",
			"19XFA1F54AE086986",
			"19XFA1F56AE086987",
			"19XFA1F58AE086988",
			"19XFA1F5XAE086989",
			"19XFA1F56AE086990",
			"19XFA1F58AE086991",
			"19XFA1F5XAE086992",
			"19XFA1F51AE086993",
			"19XFA1F53AE086994",
			"19XFA1F55AE086995",
			"19XFA1F56AE087086",
			"19XFA1F58AE087087",
			"19XFA1F5XAE087088",
			"19XFA1F51AE087089",
			"19XFA1F58AE087090",
			"19XFA1F5XAE087091",
			"19XFA1F51AE087092",
			"19XFA1F53AE087093",
			"19XFA1F55AE087094",
			"19XFA1F57AE087095",
			"19XFA1F59AE087096",
			"19XFA1F50AE087097",
			"19XFA1F52AE087098",
			"19XFA1F54AE087099",
			"19XFA1F57AE087100",
			"19XFA1F59AE087101",
			"19XFA1F50AE087102",
			"19XFA1F52AE087103",
			"19XFA1F54AE087104",
			"19XFA1F56AE087105",
			"19XFA1F58AE087106",
			"19XFA1F5XAE087107",
			"19XFA1F51AE087108",
			"19XFA1F53AE087109",
			"19XFA1F5XAE087110",
			"19XFA1F51AE087111",
			"19XFA1F53AE087112",
			"19XFA1F55AE087113",
			"19XFA1F57AE087114",
			"19XFA1F59AE087115",
			"19XFA1F50AE087116",
			"19XFA1F52AE087117",
			"19XFA1F54AE087118",
			"19XFA1F56AE087119",
			"19XFA1F52AE087120",
			"19XFA1F54AE087121",
			"19XFA1F56AE087122",
			"19XFA1F58AE087123",
			"19XFA1F5XAE087124",
			"19XFA1F51AE087125",
			"19XFA1F53AE087126",
			"19XFA1F55AE087127",
			"19XFA1F57AE087128",
			"19XFA1F59AE087129",
			"19XFA1F55AE087130",
			"19XFA1F57AE087131",
			"19XFA1F59AE087132",
			"19XFA1F50AE087133",
			"19XFA1F52AE087134",
			"19XFA1F54AE087135",
			"19XFA1F56AE087136",
			"19XFA1F58AE087137",
			"19XFA1F5XAE087138",
			"19XFA1F51AE087139",
			"19XFA1F58AE087140",
			"19XFA1F5XAE087141",
			"19XFA1F51AE087142",
			"19XFA1F53AE087143",
			"19XFA1F55AE087144",
			"19XFA1F57AE087145",
			"19XFA1F59AE087146",
			"19XFA1F50AE087147",
			"19XFA1F52AE087148",
			"19XFA1F54AE087149",
			"19XFA1F50AE087150",
			"19XFA1F52AE087151",
			"19XFA1F54AE087152",
			"19XFA1F56AE087153",
			"19XFA1F58AE087154",
			"19XFA1F5XAE087155",
			"19XFA1F51AE087156",
			"19XFA1F53AE087157",
			"19XFA1F55AE087158",
			"19XFA1F57AE087159",
			"19XFA1F53AE087160",
			"19XFA1F55AE087161",
			"19XFA1F57AE087162",
			"19XFA1F59AE087163",
			"19XFA1F50AE087164",
			"19XFA1F52AE087165",
			"19XFA1F54AE087166",
			"19XFA1F56AE087167",
			"19XFA1F58AE087168",
			"19XFA1F5XAE087169",
			"19XFA1F56AE087170",
			"19XFA1F58AE087171",
			"19XFA1F5XAE087172",
			"19XFA1F51AE087173",
			"19XFA1F53AE087174",
			"19XFA1F55AE087175",
			"19XFA1F59AE087356",
			"19XFA1F50AE087357",
			"19XFA1F52AE087358",
			"19XFA1F54AE087359",
			"19XFA1F50AE087360",
			"19XFA1F52AE087361",
			"19XFA1F54AE087362",
			"19XFA1F56AE087363",
			"19XFA1F58AE087364",
			"19XFA1F5XAE087365",
			"19XFA1F51AE087366",
			"19XFA1F53AE087367",
			"19XFA1F55AE087368",
			"19XFA1F57AE087369",
			"19XFA1F53AE087370",
			"19XFA1F55AE087371",
			"19XFA1F57AE087372",
			"19XFA1F59AE087373",
			"19XFA1F50AE087374",
			"19XFA1F52AE087375",
			"19XFA1F54AE087376",
			"19XFA1F56AE087377",
			"19XFA1F58AE087378",
			"19XFA1F5XAE087379",
			"19XFA1F56AE087380",
			"19XFA1F58AE087381",
			"19XFA1F5XAE087382",
			"19XFA1F51AE087383",
			"19XFA1F53AE087384",
			"19XFA1F55AE087385",
			"19XFA1F57AE087386",
			"19XFA1F59AE087387",
			"19XFA1F50AE087388",
			"19XFA1F52AE087389",
			"19XFA1F59AE087390",
			"19XFA1F50AE087391",
			"19XFA1F52AE087392",
			"19XFA1F54AE087393",
			"19XFA1F56AE087394",
			"19XFA1F58AE087395",
			"19XFA1F5XAE087396",
			"19XFA1F51AE087397",
			"19XFA1F53AE087398",
			"19XFA1F55AE087399",
			"19XFA1F58AE087400",
			"19XFA1F5XAE087401",
			"19XFA1F51AE087402",
			"19XFA1F53AE087403",
			"19XFA1F55AE087404",
			"19XFA1F57AE087405",
			"19XFA1F59AE087406",
			"19XFA1F50AE087407",
			"19XFA1F52AE087408",
			"19XFA1F54AE087409",
			"19XFA1F50AE087410",
			"19XFA1F52AE087411",
			"19XFA1F54AE087412",
			"19XFA1F56AE087413",
			"19XFA1F58AE087414",
			"19XFA1F5XAE087415",
			"19XFA1F51AE087416",
			"19XFA1F53AE087417",
			"19XFA1F55AE087418",
			"19XFA1F57AE087419",
			"19XFA1F53AE087420",
			"19XFA1F55AE087421",
			"19XFA1F57AE087422",
			"19XFA1F59AE087423",
			"19XFA1F50AE087424",
			"19XFA1F52AE087425",
			"19XFA1F54AE087426",
			"19XFA1F56AE087427",
			"19XFA1F58AE087428",
			"19XFA1F5XAE087429",
			"19XFA1F56AE087430",
			"19XFA1F58AE087431",
			"19XFA1F5XAE087432",
			"19XFA1F51AE087433",
			"19XFA1F53AE087434",
			"19XFA1F55AE087435",
			"19XFA1F57AE087436",
			"19XFA1F59AE087437",
			"19XFA1F50AE087438",
			"19XFA1F52AE087439",
			"19XFA1F59AE087440",
			"19XFA1F50AE087441",
			"19XFA1F52AE087442",
			"19XFA1F54AE087443",
			"19XFA1F56AE087444",
			"19XFA1F58AE087445",
			"19XFA1F5XAE087446",
			"19XFA1F51AE087447"
	}
	);
}
