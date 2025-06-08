package com.honda.galc.service.msip.dto.outbound;

import java.lang.reflect.Field;
import java.util.Map;

import com.honda.galc.service.msip.util.OutputData;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
import com.honda.galc.util.ToStringUtil;

//Additional fields not in DailyDepartmentSchedule
public class SrsDataDto  extends BaseOutboundDto  implements IMsipOutboundDto { 
	private static final long serialVersionUID = 1L;
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
	RIGHT SIDE CURTAIN AIRBAG|F|C|229|20|L
	LEFT SIDE CURTAIN AIRBAG|F|C|249|20|L
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
	@OutputData("RIGHT SIDE CURTAIN AIRBAG")
	private String rightSideCurtainAirbag;
	@OutputData("LEFT SIDE CURTAIN AIRBAG")
	private String leftSideCurtainAirbag;
	@OutputData("FILLER")
	private String filler;
	private String errorMsg;
	private Boolean isError;
	
	public String getVersion() {
		return version;
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

	public String getRightSideCurtainAirbag() {
		return rightSideCurtainAirbag;
	}

	public void setRightSideCurtainAirbag(String rightSideCurtainAirbag) {
		this.rightSideCurtainAirbag = rightSideCurtainAirbag;
	}

	public String getLeftSideCurtainAirbag() {
		return leftSideCurtainAirbag;
	}

	public void setLeftSideCurtainAirbag(String leftSideCurtainAirbag) {
		this.leftSideCurtainAirbag = leftSideCurtainAirbag;
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

	public String getSiteName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPlantName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getProcessPointId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Boolean getIsError() {
		return isError;
	}

	public void setIsError(Boolean isError) {
		this.isError = isError;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((frameNoPrefix == null) ? 0 : frameNoPrefix.hashCode());
		result = prime * result + ((frameNo == null) ? 0 : frameNo.hashCode());
		result = prime * result + ((engineSerialNo == null) ? 0 : engineSerialNo.hashCode());
		result = prime * result + ((engineNoPrefix == null) ? 0 : engineNoPrefix.hashCode());
		result = prime * result + ((engineNo == null) ? 0 : engineNo.hashCode());
		result = prime * result + ((missionSerialNo == null) ? 0 : missionSerialNo.hashCode());
		result = prime * result + ((srsDriverModule == null) ? 0 : srsDriverModule.hashCode());
		result = prime * result + ((srsAssistModule == null) ? 0 : srsAssistModule.hashCode());
		result = prime * result + ((srsLeftSensor == null) ? 0 : srsLeftSensor.hashCode());
		result = prime * result + ((srsRightSensor == null) ? 0 : srsRightSensor.hashCode());
		result = prime * result + ((srsCableReel == null) ? 0 : srsCableReel.hashCode());
		result = prime * result + ((ecu == null) ? 0 : ecu.hashCode());
		result = prime * result + ((intakeManifold == null) ? 0 : intakeManifold.hashCode());
		result = prime * result + ((blockMc == null) ? 0 : blockMc.hashCode());
		result = prime * result + ((blockDc == null) ? 0 : blockDc.hashCode());
		result = prime * result + ((rearHeadMc == null) ? 0 : rearHeadMc.hashCode());
		result = prime * result + ((rearHeadDc == null) ? 0 : rearHeadDc.hashCode());
		result = prime * result + ((rightSideCurtainAirbag == null) ? 0 : rightSideCurtainAirbag.hashCode());
		result = prime * result + ((leftSideCurtainAirbag == null) ? 0 : leftSideCurtainAirbag.hashCode());
		result = prime * result + ((filler == null) ? 0 : filler.hashCode());
		result = prime * result + ((errorMsg == null) ? 0 : errorMsg.hashCode());
		result = prime * result + ((isError == null) ? 0 : isError.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SrsDataDto other = (SrsDataDto) obj;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (frameNoPrefix == null) {
			if (other.frameNoPrefix != null)
				return false;
		} else if (!frameNoPrefix.equals(other.frameNoPrefix))
			return false;
		if (frameNo == null) {
			if (other.frameNo != null)
				return false;
		} else if (!frameNo.equals(other.frameNo))
			return false;
		if (engineSerialNo == null) {
			if (other.engineSerialNo != null)
				return false;
		} else if (!engineSerialNo.equals(other.engineSerialNo))
			return false;
		if (engineNoPrefix == null) {
			if (other.engineNoPrefix != null)
				return false;
		} else if (!engineNoPrefix.equals(other.engineNoPrefix))
			return false;
		if (engineNo == null) {
			if (other.engineNo != null)
				return false;
		} else if (!engineNo.equals(other.engineNo))
			return false;
		if (missionSerialNo == null) {
			if (other.missionSerialNo != null)
				return false;
		} else if (!missionSerialNo.equals(other.missionSerialNo))
			return false;
		if (srsDriverModule == null) {
			if (other.srsDriverModule != null)
				return false;
		} else if (!srsDriverModule.equals(other.srsDriverModule))
			return false;
		if (srsAssistModule == null) {
			if (other.srsAssistModule != null)
				return false;
		} else if (!srsAssistModule.equals(other.srsAssistModule))
			return false;
		if (srsLeftSensor == null) {
			if (other.srsLeftSensor != null)
				return false;
		} else if (!srsLeftSensor.equals(other.srsLeftSensor))
			return false;
		if (srsRightSensor == null) {
			if (other.srsRightSensor != null)
				return false;
		} else if (!srsRightSensor.equals(other.srsRightSensor))
			return false;
		if (srsCableReel == null) {
			if (other.srsCableReel != null)
				return false;
		} else if (!srsCableReel.equals(other.srsCableReel))
			return false;
		if (ecu == null) {
			if (other.ecu != null)
				return false;
		} else if (!ecu.equals(other.ecu))
			return false;
		if (intakeManifold == null) {
			if (other.intakeManifold != null)
				return false;
		} else if (!intakeManifold.equals(other.intakeManifold))
			return false;
		if (blockMc == null) {
			if (other.blockMc != null)
				return false;
		} else if (!blockMc.equals(other.blockMc))
			return false;
		if (blockDc == null) {
			if (other.blockDc != null)
				return false;
		} else if (!blockDc.equals(other.blockDc))
			return false;
		if (rearHeadMc == null) {
			if (other.rearHeadMc != null)
				return false;
		} else if (!rearHeadMc.equals(other.rearHeadMc))
			return false;
		if (rearHeadDc == null) {
			if (other.rearHeadDc != null)
				return false;
		} else if (!rearHeadDc.equals(other.rearHeadDc))
			return false;
		if (rightSideCurtainAirbag == null) {
			if (other.rightSideCurtainAirbag != null)
				return false;
		} else if (!rightSideCurtainAirbag.equals(other.rightSideCurtainAirbag))
			return false;
		if (leftSideCurtainAirbag == null) {
			if (other.leftSideCurtainAirbag != null)
				return false;
		} else if (!leftSideCurtainAirbag.equals(other.leftSideCurtainAirbag))
			return false;
		if (filler == null) {
			if (other.filler != null)
				return false;
		} else if (!filler.equals(other.filler))
			return false;
		if (errorMsg == null) {
			if (other.errorMsg != null)
				return false;
		} else if (!errorMsg.equals(other.errorMsg))
			return false;
		if (isError == null) {
			if (other.isError != null)
				return false;
		} else if (!isError.equals(other.isError))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
