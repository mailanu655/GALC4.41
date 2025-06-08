package com.honda.galc.vios.dto.mfg;

import java.util.Date;
import java.util.Set;

import com.honda.galc.dto.IDto;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCPddaPlatform;
import com.honda.galc.entity.conf.MCRevision;
import com.honda.galc.entity.pdda.ChangeFormUnit;
import com.honda.galc.entity.pdda.Unit;
import com.honda.galc.property.MfgControlMaintenancePropertyBean;

public class MfgCtrlDto implements IDto {
	private static final long serialVersionUID = 7474525743803909640L;
	private String specCodeType;
	private long currentTime;
	private Date currentDate;
	private MCRevision mfgRevision;
	private MCPddaPlatform platform;
	private ChangeFormUnit changeFormUnit;
	private Unit unit;
	private String operationName;
	private Integer modelYear;
	private boolean isDeprecated;
	private String userId;
	private MCOperationRevision newOperation;
	private int newPartRev;
	private Set<String> newOperationSpecCodeMasks;
	private MfgControlMaintenancePropertyBean propertyBean;
	private MfgCtrlViosDto mfgCtrlViosDto;
	
	public MfgCtrlDto() {
		currentTime = System.currentTimeMillis();
		currentDate = new Date(currentTime);
	}

	public String getSpecCodeType() {
		return specCodeType;
	}

	public void setSpecCodeType(String specCodeType) {
		this.specCodeType = specCodeType;
	}

	public MCRevision getMfgRevision() {
		return mfgRevision;
	}

	public void setMfgRevision(MCRevision mfgRevision) {
		this.mfgRevision = mfgRevision;
	}

	public MCPddaPlatform getPlatform() {
		return platform;
	}

	public void setPlatform(MCPddaPlatform platform) {
		this.platform = platform;
	}

	public ChangeFormUnit getChangeFormUnit() {
		return changeFormUnit;
	}

	public void setChangeFormUnit(ChangeFormUnit changeFormUnit) {
		this.changeFormUnit = changeFormUnit;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	public Integer getModelYear() {
		return modelYear;
	}

	public void setModelYear(Integer modelYear) {
		this.modelYear = modelYear;
	}

	public boolean isDeprecated() {
		return isDeprecated;
	}

	public void setDeprecated(boolean isDeprecated) {
		this.isDeprecated = isDeprecated;
	}

	

	public long getCurrentTime() {
		return currentTime;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public MCOperationRevision getNewOperation() {
		return newOperation;
	}

	public void setNewOperation(MCOperationRevision newOperation) {
		this.newOperation = newOperation;
	}

	public Set<String> getNewOperationSpecCodeMasks() {
		return newOperationSpecCodeMasks;
	}

	public void setNewOperationSpecCodeMasks(Set<String> newOperationSpecCodeMasks) {
		this.newOperationSpecCodeMasks = newOperationSpecCodeMasks;
	}
	
	public int getNewPartRev() {
		return newPartRev;
	}

	public void setNewPartRev(int newPartRev) {
		this.newPartRev = newPartRev;
	}

	public MfgControlMaintenancePropertyBean getPropertyBean() {
		return propertyBean;
	}

	public void setPropertyBean(MfgControlMaintenancePropertyBean propertyBean) {
		this.propertyBean = propertyBean;
	}

	public MfgCtrlViosDto getMfgCtrlViosDto() {
		return mfgCtrlViosDto;
	}

	public void setMfgCtrlViosDto(MfgCtrlViosDto mfgCtrlViosDto) {
		this.mfgCtrlViosDto = mfgCtrlViosDto;
	}

	public void clear() {
		this.specCodeType=null;
		this.currentDate = null;
		this.mfgRevision = null;
		this.platform = null;
		this.changeFormUnit = null;
		this.operationName=null;
		this.unit = null;
		this.newOperation =null;
		if(this.newOperationSpecCodeMasks!=null)
			this.newOperationSpecCodeMasks.clear();
		this.newOperationSpecCodeMasks = null;
		if(mfgCtrlViosDto != null)
			mfgCtrlViosDto.clear();
		mfgCtrlViosDto = null;
	}
}
