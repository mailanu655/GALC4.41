package com.honda.galc.oif.dto;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Map;

import com.honda.galc.util.OutputData;

//Additional fields not in DailyDepartmentSchedule
public class SecondaryVinDTO   implements IOutputFormat { 
	/*
	PRODUCT_ID|F|C|0|17|L
	ENGINE_SERIAL_NO|F|C|17|12|L
	SRS DRIVER MODULE|F|C|29|20|L
	SRS ASSIST MODULE|F|C|49|20|L
	SRS LEFT SENSOR|F|C|69|20|L
	SRS RIGHT SENSOR|F|C|89|20|L
	SRS CABLE REEL|F|C|109|20|L
	ECU|F|C|129|20|L
	MISSION_SERIAL_NO|F|C|149|20|L
	INT. MANIF. ASSY.|F|C|169|20|L
	BLOCK MC|F|C|189|20|L
	BLOCK DC|F|C|209|20|L
	REAR HEAD MC|F|C|229|20|L
	REAR HEAD DC|F|C|249|20|L
	FILLER|F|C|269|31|L
	*/
	@OutputData("PRODUCT_ID")
	private String productId;
	@OutputData("FRAME_NO_PREFIX")
	private String frameNoPrefix;
	@OutputData("FRAME_NO")
	private String frameNo;
	@OutputData("ENGINE_SERIAL_NO")
	private String engineSerialNo;
	@OutputData("ENGINE_NO_PREFIX")
	private String engineNoPrefix;
	@OutputData("ENGINE_NO")
	private String engineNo;
	@OutputData("MISSION_SERIAL_NO")
	private String missionSerialNo;
	@OutputData("SRS DRIVER MODULE")
	private String srsDriverModule;
	@OutputData("SRS ASSIST MODULE")
	private String srsAssistModule;
	@OutputData("SRS LEFT SENSOR")
	private String srsLeftSensor;
	@OutputData("SRS RIGHT SENSOR")
	private String srsRightSensor;
	@OutputData("SRS CABLE REEL")
	private String srsCableReel;
	@OutputData("ECU")
	private String ecu;
	@OutputData("INTAKE_MANIFOLD")
	private String intakeManifold;
	@OutputData("BLOCK MC")
	private String blockMc;
	@OutputData("BLOCK DC")
	private String blockDc;
	@OutputData("REAR HEAD MC")
	private String rearHeadMc;
	@OutputData("REAR HEAD DC")
	private String rearHeadDc;
	@OutputData("FILLER")
	private String filler;

	public SecondaryVinDTO() {
	}

	public SecondaryVinDTO(SecondaryVinDTO sVin) {
	super();
	this.productId = sVin.productId;
	this.engineSerialNo = sVin.engineSerialNo;
	this.missionSerialNo = sVin.missionSerialNo;
	this.srsDriverModule = sVin.srsDriverModule;
	this.srsAssistModule = sVin.srsAssistModule;
	this.srsLeftSensor = sVin.srsLeftSensor;
	this.srsRightSensor = sVin.srsRightSensor;
	this.srsCableReel = sVin.srsCableReel;
	this.ecu = sVin.ecu;
	this.blockMc = sVin.blockMc;
	this.blockDc = sVin.blockDc;
	this.rearHeadMc = sVin.rearHeadMc;
	this.rearHeadDc = sVin.rearHeadDc;
	this.filler = sVin.filler;
}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getEngineSerialNo() {
		return engineSerialNo;
	}

	public void setEngineSerialNo(String engineSerialNo) {
		this.engineSerialNo = engineSerialNo;
	}

	public String getMissionSerialNo() {
		return missionSerialNo;
	}

	public void setMissionSerialNo(String missionSerialNo) {
		this.missionSerialNo = missionSerialNo;
	}

	public String getSrsDriverModule() {
		return srsDriverModule;
	}

	public void setSrsDriverModule(String srsDriverModule) {
		this.srsDriverModule = srsDriverModule;
	}

	public String getSrsAssistModule() {
		return srsAssistModule;
	}

	public void setSrsAssistModule(String srsAssistModule) {
		this.srsAssistModule = srsAssistModule;
	}

	public String getSrsLeftSensor() {
		return srsLeftSensor;
	}

	public void setSrsLeftSensor(String srsLeftSensor) {
		this.srsLeftSensor = srsLeftSensor;
	}

	public String getSrsRightSensor() {
		return srsRightSensor;
	}

	public void setSrsRightSensor(String srsRightSensor) {
		this.srsRightSensor = srsRightSensor;
	}

	public String getSrsCableReel() {
		return srsCableReel;
	}

	public void setSrsCableReel(String srsCableReel) {
		this.srsCableReel = srsCableReel;
	}

	public String getEcu() {
		return ecu;
	}

	public void setEcu(String ecu) {
		this.ecu = ecu;
	}

	public String getBlockMc() {
		return blockMc;
	}

	public void setBlockMc(String blockMc) {
		this.blockMc = blockMc;
	}

	public String getBlockDc() {
		return blockDc;
	}

	public void setBlockDc(String blockDc) {
		this.blockDc = blockDc;
	}

	public String getRearHeadMc() {
		return rearHeadMc;
	}

	public void setRearHeadMc(String rearHeadMc) {
		this.rearHeadMc = rearHeadMc;
	}

	public String getRearHeadDc() {
		return rearHeadDc;
	}

	public void setRearHeadDc(String rearHeadDc) {
		this.rearHeadDc = rearHeadDc;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

	public String getFrameNoPrefix() {
		return frameNoPrefix;
	}

	public void setFrameNoPrefix(String frameNoPrefix) {
		this.frameNoPrefix = frameNoPrefix;
	}

	public String getFrameNo() {
		return frameNo;
	}

	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}

	public String getEngineNoPrefix() {
		return engineNoPrefix;
	}

	public void setEngineNoPrefix(String engineNoPrefix) {
		this.engineNoPrefix = engineNoPrefix;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getIntakeManifold() {
		return intakeManifold;
	}

	public void setIntakeManifold(String intakeManifold) {
		this.intakeManifold = intakeManifold;
	}

	public void initialize(Map<String,String>  inputValues)  {
		if(inputValues == null || inputValues.isEmpty())  return;
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field f : fields)  {
			OutputData a1 = f.getAnnotation(OutputData.class);
			if(!inputValues.containsKey(a1.value()))  continue;
			if(f.getType().isAssignableFrom(String.class))  {
				try {
					f.set(this, inputValues.get(a1.value()));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
