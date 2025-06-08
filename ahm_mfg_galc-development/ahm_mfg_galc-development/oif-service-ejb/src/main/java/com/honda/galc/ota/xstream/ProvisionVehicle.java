package com.honda.galc.ota.xstream;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias(value="ProvisionVehicle")
public class ProvisionVehicle implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias(value="VIN")
	private String vin = null;
	
	@XStreamAlias(value="InitialProvisioningInformation")
	private InitialProvisioningInformation initialProvisioningInformation;
	
	@XStreamAlias(value="InitialSoftwareInformation")
	private String initialSoftwareInformation = null;

	
	public ProvisionVehicle(String vin, String chasisNumber, String ctrlBoxSerial, String plant, String prodWeek, String domainName, String vehModelCode, String vehicleModleYear){
		this.setVin(vin);
		this.setInitialProvisioningInformation(new InitialProvisioningInformation(chasisNumber, ctrlBoxSerial, plant, prodWeek, domainName, vehModelCode, vehicleModleYear));
	}
	
	public String getVin() {
		return StringUtils.trim(this.vin);
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public InitialProvisioningInformation getInitialProvisioningInformation() {
		return initialProvisioningInformation;
	}

	public void setInitialProvisioningInformation(
			InitialProvisioningInformation initialProvisioningInformation) {
		this.initialProvisioningInformation = initialProvisioningInformation;
	}

	public String getInitialSoftwareInformation() {
		return StringUtils.trim(initialSoftwareInformation);
	}

	public void setInitialSoftwareInformation(String initialSoftwareInformation) {
		this.initialSoftwareInformation = initialSoftwareInformation;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((initialProvisioningInformation == null) ? 0
						: initialProvisioningInformation.hashCode());
		result = prime
				* result
				+ ((initialSoftwareInformation == null) ? 0
						: initialSoftwareInformation.hashCode());
		result = prime * result + ((vin == null) ? 0 : vin.hashCode());
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
		ProvisionVehicle other = (ProvisionVehicle) obj;
		if (initialProvisioningInformation == null) {
			if (other.initialProvisioningInformation != null)
				return false;
		} else if (!initialProvisioningInformation
				.equals(other.initialProvisioningInformation))
			return false;
		if (initialSoftwareInformation == null) {
			if (other.initialSoftwareInformation != null)
				return false;
		} else if (!initialSoftwareInformation
				.equals(other.initialSoftwareInformation))
			return false;
		if (vin == null) {
			if (other.vin != null)
				return false;
		} else if (!vin.equals(other.vin))
			return false;
		return true;
	}

	
	
}
