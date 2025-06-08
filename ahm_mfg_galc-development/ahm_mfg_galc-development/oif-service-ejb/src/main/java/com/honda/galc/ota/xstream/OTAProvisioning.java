package com.honda.galc.ota.xstream;

import java.io.Serializable;
import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias(value="OTAProvisioning")
public class OTAProvisioning implements Serializable{

	private static final long serialVersionUID = 1L;

    @XStreamAsAttribute 
    @XStreamAlias("xsi:noNamespaceSchemaLocation")
    String noNamespaceSchemaLocation= null;
    
	@XStreamAsAttribute
    @XStreamAlias("xmlns:xsi")
    final String xsi = "http://www.w3.org/2001/XMLSchema-instance";

	@XStreamImplicit
    @XStreamAlias("ProvisionVehicle")
    private ArrayList<ProvisionVehicle> provisionVehicle;
    
    public OTAProvisioning(String noNamespaceSchemaLocation) {
		this.setNoNamespaceSchemaLocation(noNamespaceSchemaLocation);
	}
    
	public ArrayList<ProvisionVehicle> getProvisionVehicle() {
		return provisionVehicle;
	}

	public void setProvisionVehicle(ArrayList<ProvisionVehicle> provisionVehicle) {
		this.provisionVehicle = provisionVehicle;
	}

	public void setNoNamespaceSchemaLocation(String noNamespaceSchemaLocation){
		this.noNamespaceSchemaLocation = noNamespaceSchemaLocation;
	}
	
	public String getNoNamespaceSchemaLocation() {
		return noNamespaceSchemaLocation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((noNamespaceSchemaLocation == null) ? 0
						: noNamespaceSchemaLocation.hashCode());
		result = prime
				* result
				+ ((provisionVehicle == null) ? 0 : provisionVehicle.hashCode());
		result = prime * result + ((xsi == null) ? 0 : xsi.hashCode());
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
		OTAProvisioning other = (OTAProvisioning) obj;
		if (noNamespaceSchemaLocation == null) {
			if (other.noNamespaceSchemaLocation != null)
				return false;
		} else if (!noNamespaceSchemaLocation
				.equals(other.noNamespaceSchemaLocation))
			return false;
		if (provisionVehicle == null) {
			if (other.provisionVehicle != null)
				return false;
		} else if (!provisionVehicle.equals(other.provisionVehicle))
			return false;
		if (xsi == null) {
			if (other.xsi != null)
				return false;
		} else if (!xsi.equals(other.xsi))
			return false;
		return true;
	}
	
}
