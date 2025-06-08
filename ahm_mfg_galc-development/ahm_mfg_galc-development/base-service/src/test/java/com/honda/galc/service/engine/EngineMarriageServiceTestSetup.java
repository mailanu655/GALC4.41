package com.honda.galc.service.engine;

import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.PartName;

public class EngineMarriageServiceTestSetup {

	/**
	 * This is used to initialize a Engine object with the minimum 
	 * amount of data for testing.
	 */
	protected Engine initEngine(Engine engine) {
		engine.setProductId("J35Y71056715");
		engine.setVin(null);
		engine.setMissionSerialNo(null);
		engine.setEngineFiringFlag((short) 0);
		engine.setTrackingStatus("LINE27");
		engine.setLastPassingProcessPointId("PP10355");
		engine.setMissionStatus(0);
		engine.setActualMissionType(null);	
		return engine;
	}

	/**
	 * This is used to initialize a Frame object with the minimum 
	 * amount of data for testing.
	 */
	protected Frame initFrame(Frame frame) {
		frame.setProductId("5FNRL6H20JB057874");
		frame.setEngineSerialNo(null);
		frame.setMissionSerialNo(null);
		frame.setShortVin(null);
		frame.setProductSpecCode("JTHRAA500 NH830M    D ");
		frame.setTrackingStatus("LINE11");
		frame.setLastPassingProcessPointId("PP10477");
		frame.setEngineStatus(false);
		frame.setAfOnSequenceNumber(7570);
		frame.setActualMissionType(null);
		return frame;
	}

	/**
	 * This is used to initialize a InstalledPart object with the minimum 
	 * amount of data for testing.
	 */
	protected InstalledPart initInstalledPart(InstalledPart installedPart) {
		installedPart.setProcessPointId("PP10072");
		installedPart.setProductType("ENGINE");
		installedPart.setProductId("J35Y71056715");
		installedPart.setPartName(null);
		installedPart.setPartSerialNumber(null);
		installedPart.setInstalledPartStatusId(0);
		return installedPart;
	}

	/**
	 * This is used, along with installed part init, to preset InstalledPart
	 * attributes to setup assigning the mission with a NG status.
	 * @param installedPart
	 */
	protected InstalledPart setInstalledPartWithMissionTypeNG(InstalledPart installedPart) {
		initInstalledPart(installedPart);
		installedPart.setPartName("MISSION_TYPE");
		installedPart.setPartSerialNumber("Q5NZMISSION");
		installedPart.setInstalledPartStatusId(0);
		return installedPart;
	}

	/**
	 * This is used, along with installed part init, to preset InstalledPart
	 * attributes to setup assigning the mission type with a good status.
	 * @param installedPart
	 */
	protected InstalledPart setInstalledPartWithMissionTypeGood(InstalledPart installedPart) {
		initInstalledPart(installedPart);
		installedPart.setPartName("MISSION_TYPE");
		installedPart.setPartSerialNumber("Q5NZMISSION");
		installedPart.setInstalledPartStatusId(1);
		return installedPart;
	}

	/**
	 * This is used, along with installed part init, to preset InstalledPart
	 * attributes to setup assigning the mission with a NG status.
	 * @param installedPart
	 */
	protected InstalledPart setInstalledPartWithMissionNG(InstalledPart installedPart) {
		initInstalledPart(installedPart);
		installedPart.setPartName("MISSION");
		installedPart.setPartSerialNumber("Q5NZ0263739");
		installedPart.setInstalledPartStatusId(0);
		return installedPart;
	}

	/**
	 * This is used, along with installed part init, to preset InstalledPart
	 * attributes to setup assigning the mission with a good status.
	 * @param installedPart
	 */
	protected InstalledPart setInstalledPartWithMissionGood(InstalledPart installedPart) {
		initInstalledPart(installedPart);
		installedPart.setPartName("MISSION");
		installedPart.setPartSerialNumber("Q5NZ0263739");
		installedPart.setInstalledPartStatusId(1);
		return installedPart;
	}

	/**
	 * This is used, along with installed part init, to preset InstalledPart
	 * attributes to setup assigning the Engine with a good status.
	 * @param installedPart
	 */
	protected InstalledPart setInstalledPartWithEngineGood(InstalledPart installedPart) {
		initInstalledPart(installedPart);
		installedPart.setPartName("ENGINE");
		installedPart.setPartSerialNumber("J35Y71056715");
		installedPart.setInstalledPartStatusId(1);
		return installedPart;
	}

	/**
	 * This is used, along with installed part init, to preset InstalledPart
	 * attributes to setup assigning the engine with a NG status.
	 * @param installedPart
	 */
	protected InstalledPart setInstalledPartWithEngineNG(InstalledPart installedPart) {
		initInstalledPart(installedPart);
		installedPart.setPartName("ENGINE");
		installedPart.setPartSerialNumber("J35Y71056715");
		installedPart.setInstalledPartStatusId(0);
		return installedPart;
	}

	/**
	 * This is used, along with installed part init, to preset InstalledPart
	 * attributes to setup deassigning the mission.
	 * @param installedPart
	 */
	protected InstalledPart removeInstalledPartWithMission(InstalledPart installedPart) {
		initInstalledPart(installedPart);
		installedPart.setPartName("MISSION");
		installedPart.setPartSerialNumber("Q5NZ0263739");
		installedPart.setInstalledPartStatusId(-9);
		return installedPart;
	}

	/**
	 * This is used, along with installed part init, to preset InstalledPart
	 * attributes to setup deassigning the mission type.
	 * @param installedPart.
	 */
	protected InstalledPart removeInstalledPartWithMissionType(InstalledPart installedPart) {
		initInstalledPart(installedPart);
		installedPart.setPartName("MISSION_TYPE");
		installedPart.setPartSerialNumber("Q5NZMISSION");
		installedPart.setInstalledPartStatusId(1);
		return installedPart;
	}

	/**
	 * This is used, along with installed part init, to preset InstalledPart
	 * attributes to setup installing a engine to a frame with a
	 * NG part staus.
	 * @param installedPart
	 */
	protected InstalledPart setInstalledPartWithFrameNG(InstalledPart installedPart) {
		initInstalledPart(installedPart);
		installedPart.setProductId("5FNRL6H20JB057874");
		installedPart.setPartName("ENGINE");
		installedPart.setPartSerialNumber("J35Y71056715");
		installedPart.setProductType("FRAME");
		installedPart.setInstalledPartStatusId(0);
		return installedPart;
	}

	/**
	 * This is used, along with installed part init, to preset InstalledPart
	 * attributes to setup installing a engine to a frame with a
	 * good part staus.
	 * @param installedPart
	 */
	protected InstalledPart setInstalledPartWithFrameGood(InstalledPart installedPart) {
		initInstalledPart(installedPart);
		installedPart.setProductId("5FNRL6H20JB057874");
		installedPart.setPartName("ENGINE");
		installedPart.setPartSerialNumber("J35Y71056715");
		installedPart.setProductType("FRAME");
		installedPart.setInstalledPartStatusId(1);
		return installedPart;
	}

	/**
	 * This is used, along with installed part init, to preset InstalledPart
	 * attributes to test deassigning a Engine from Frame
	 * @param installedPart
	 */
	protected InstalledPart removeEngineFromFrame(InstalledPart installedPart) {
		initInstalledPart(installedPart);
		installedPart.setProductId("5FNRL6H20JB057874");
		installedPart.setPartName("ENGINE");
		installedPart.setPartSerialNumber("J35Y71056715");
		installedPart.setProductType("FRAME");
		installedPart.setInstalledPartStatusId(-9);
		return installedPart;
	}

	/**
	 * This is used, along with frame init,
	 * to preset frame attributes for testing a frame with only an engine marriage 
	 * @param frame
	 */
	protected Frame setFrameWithEngine(Frame frame) {
		initFrame(frame);
		frame.setEngineSerialNo("J35Y71056715");
		return frame;
	}

	/**
	 * This is used, along with frame init,
	 * to preset frame attributes for testing a frame with engine
	 * and mission marriage only 
	 * @param frame
	 */
	protected Frame setFrameWithMission(Frame frame) {
		initFrame(frame);
		frame.setEngineSerialNo("J35Y71056715");
		frame.setMissionSerialNo("Q5NZ0263739");
		return frame;
	}

	/**
	 * This is used, along with frame init,
	 * to preset frame attributes for testing a frame with engine
	 * and mission type marriage only 
	 * @param frame
	 */
	protected Frame setFrameWithMissionType(Frame frame) {
		initFrame(frame);
		frame.setEngineSerialNo("J35Y71056715");
		frame.setActualMissionType("Q5NZMISSION");
		return frame;
	}

	/**
	 * This is used, along with frame init,
	 * to preset frame attributes for testing a frame with engine,
	 * mission, and mission type marriage where the engine
	 * status is good.  
	 * @param frame
	 */
	protected Frame setFrameGoodEngine(Frame frame) {
		initFrame(frame);
		frame.setEngineSerialNo("J35Y71056715");
		frame.setMissionSerialNo("Q5NZ0263739");
		frame.setActualMissionType("Q5NZMISSION");
		frame.setEngineStatus(InstalledPartStatus.OK.getId());
		return frame;
	}

	/**
	 * This is used, along with frame init,
	 * to preset frame attributes for testing a frame with engine,
	 * mission, and mission type marriage where the engine
	 * status is NG.  
	 * @param frame
	 */
	protected Frame setFrameNGEngine(Frame frame) {
		initFrame(frame);
		frame.setEngineSerialNo("J35Y71056715");
		frame.setMissionSerialNo("Q5NZ0263739");
		frame.setActualMissionType("Q5NZMISSION");
		frame.setEngineStatus(InstalledPartStatus.NG.getId());
		return frame;
	}

	/**
	 * This is used, along with engine init,
	 * to preset frame attributes for testing a engine with only a frame marriage 
	 * @param engine
	 */
	protected Engine setEngineWithFrame(Engine engine) {
		initEngine(engine);
		engine.setVin("5FNRL6H20JB057874");
		return engine;
	}

	/**
	 * This is used, along with engine init,
	 * to preset frame attributes for testing a engine with only a
	 * mission marriage 
	 * @param engine
	 */
	protected Engine setEngineWithMission(Engine engine) {
		initEngine(engine);
		engine.setMissionSerialNo("Q5NZ0263739");
		return engine;
	}

	/**
	 * This is used, along with engine init,
	 * to preset frame attributes for testing a engine with only 
	 * a mission type
	 * @param engine
	 */
	protected Engine setEngineWithMissionType(Engine engine) {
		initEngine(engine);
		engine.setActualMissionType("Q5NZMISSION");
		return engine;
	}

	/**
	 * This is used, along with engine init,
	 * to preset frame attributes for testing a engine with a 
	 * frame and mission marriage
	 * @param engine
	 */
	protected Engine setEngineWithFrameMission(Engine engine) {
		initEngine(engine);
		engine.setVin("5FNRL6H20JB057874");
		engine.setMissionSerialNo("Q5NZ0263739");
		return engine;
	}
	
	/**
	 * This is used, along with engine init,
	 * to preset frame attributes for testing a engine with a 
	 * frame and mission type marriage
	 * @param engine
	 */
	protected Engine setEngineWithFrameMissionType(Engine engine) {
		initEngine(engine);
		engine.setVin("5FNRL6H20JB057874");
		engine.setActualMissionType("Q5NZMISSION");
		return engine;
	}

	/**
	 * This is used, along with engine init,
	 * to preset frame attributes for testing a engine with a 
	 * frame, mission, and mission type marriage with a mission status
	 * of good
	 * @param engine
	 */
	protected Engine setEngineWithFrameMissionTypeGood(Engine engine) {
		initEngine(engine);
		engine.setVin("5FNRL6H20JB057874");
		engine.setMissionSerialNo("Q5NZ0263739");
		engine.setActualMissionType("Q5NZMISSION");
		engine.setMissionStatus(InstalledPartStatus.OK.getId());
		return engine;
	}

	/**
	 * This is used, along with engine init,
	 * to preset frame attributes for testing a engine with a 
	 * engine and mission type marriage with a mission status
	 * of good
	 * @param engine
	 */
	protected Engine setEngineWithMissionTypeGood(Engine engine) {
		initEngine(engine);
		engine.setMissionSerialNo("Q5NZ0263739");
		engine.setActualMissionType("Q5NZMISSION");
		engine.setMissionStatus(InstalledPartStatus.OK.getId());
		return engine;
	}

	/**
	 * This is used, along with engine init,
	 * to preset frame attributes for testing a engine with a 
	 * engine and mission type marriage with a mission status
	 * of NG
	 * @param engine
	 */
	protected Engine setEngineWithMissionTypeNG(Engine engine) {
		initEngine(engine);
		engine.setMissionSerialNo("Q5NZ0263739");
		engine.setActualMissionType("Q5NZMISSION");
		engine.setMissionStatus(InstalledPartStatus.NG.getId());
		return engine;
	}

	/**
	 * This is used, along with engine init,
	 * to preset frame attributes for testing a engine with a 
	 * frame and mission type marriage with a mission status
	 * of NG
	 * @param engine
	 */
	protected Engine setEngineWithFrameMissionTypeNG(Engine engine) {
		initEngine(engine);
		engine.setVin("5FNRL6H20JB057874");
		engine.setMissionSerialNo("Q5NZ0263739");
		engine.setActualMissionType("Q5NZMISSION");
		engine.setMissionStatus(InstalledPartStatus.NG.getId());
		return engine;
	}

	public PartName initMissionPart() {
		PartName part = new PartName("MISSION", "ENGINE");
		return part;
	}
	
	public PartName initEnginePart() {
		PartName part = new PartName("ENGINE", "FRAME");
		return part;
	}
}