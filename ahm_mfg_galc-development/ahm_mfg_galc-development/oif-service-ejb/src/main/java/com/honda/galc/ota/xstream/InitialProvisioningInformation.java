package com.honda.galc.ota.xstream;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias(value="InitialProvisioningInformation")
public class InitialProvisioningInformation implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ChassisNumber")
	private String chassisNumber = null;
	
	@XStreamAlias("ControlBoxSerial")
	private String controlBoxSerial = null;

	@XStreamAlias("Plant")
	private String plant = null;
	
	@XStreamAlias("ProductionWeek")
	private String productionWeek = null;
	
	@XStreamAlias("DomainName")
	private String domainName = null;
	
	@XStreamAlias("VehicleModelCode")
	private String vehicleModelCode = null;
	
	@XStreamAlias("VehicleModelYear")
	private String vehicleModelYear = null;
	
	public InitialProvisioningInformation(String chasisNumber, String ctrlBoxSerial, String plant, String prodWeek, String domainName, String vehicleModelCode, String vehicleModelYear){
		this.setChassisNumber(chasisNumber);
		this.setControlBoxSerial(ctrlBoxSerial);
		this.setPlant(plant);
		this.setProductionWeek(prodWeek);
		this.setDomainName(domainName);
		this.setVehicleModelCode(vehicleModelCode);
		this.setVehicleModelYear(vehicleModelYear);
	}

	public String getChassisNumber() {
		return StringUtils.trim(chassisNumber);
	}

	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber;
	}

	public String getControlBoxSerial() {
		return StringUtils.trim(controlBoxSerial);
	}

	public void setControlBoxSerial(String controlBoxSerial) {
		this.controlBoxSerial = controlBoxSerial;
	}

	public String getPlant() {
		return StringUtils.trim(plant);
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getProductionWeek() {
		return StringUtils.trim(productionWeek);
	}

	public void setProductionWeek(String productionWeek) {
		this.productionWeek = productionWeek;
	}

	public String getDomainName() {
		return StringUtils.trim(domainName);
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getVehicleModelCode() {
		return StringUtils.trim(vehicleModelCode);
	}

	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}

	public String getVehicleModelYear() {
		return vehicleModelYear;
	}

	public void setVehicleModelYear(String vehicleModelYear) {
		this.vehicleModelYear = vehicleModelYear;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((chassisNumber == null) ? 0 : chassisNumber.hashCode());
		result = prime
				* result
				+ ((controlBoxSerial == null) ? 0 : controlBoxSerial.hashCode());
		result = prime * result
				+ ((domainName == null) ? 0 : domainName.hashCode());
		result = prime * result + ((plant == null) ? 0 : plant.hashCode());
		result = prime * result
				+ ((productionWeek == null) ? 0 : productionWeek.hashCode());
		result = prime
				* result
				+ ((vehicleModelCode == null) ? 0 : vehicleModelCode.hashCode());
		result = prime
				* result
				+ ((vehicleModelYear == null) ? 0 : vehicleModelYear.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof InitialProvisioningInformation))
			return false;
		InitialProvisioningInformation other = (InitialProvisioningInformation) obj;
		if (chassisNumber == null) {
			if (other.chassisNumber != null)
				return false;
		} else if (!chassisNumber.equals(other.chassisNumber))
			return false;
		if (controlBoxSerial == null) {
			if (other.controlBoxSerial != null)
				return false;
		} else if (!controlBoxSerial.equals(other.controlBoxSerial))
			return false;
		if (domainName == null) {
			if (other.domainName != null)
				return false;
		} else if (!domainName.equals(other.domainName))
			return false;
		if (plant == null) {
			if (other.plant != null)
				return false;
		} else if (!plant.equals(other.plant))
			return false;
		if (productionWeek == null) {
			if (other.productionWeek != null)
				return false;
		} else if (!productionWeek.equals(other.productionWeek))
			return false;
		if (vehicleModelCode == null) {
			if (other.vehicleModelCode != null)
				return false;
		} else if (!vehicleModelCode.equals(other.vehicleModelCode))
			return false;
		if (vehicleModelYear == null) {
			if (other.vehicleModelYear != null)
				return false;
		} else if (!vehicleModelYear.equals(other.vehicleModelYear))
			return false;
		return true;
	}


	
}
