package com.honda.galc.entity.conf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.honda.galc.entity.AuditEntry;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL205TBX")
public class ParkingAddress extends AuditEntry {
    @EmbeddedId
    private ParkingAddressId id;
    
    @Column(name = "VIN")
    private String vin;
    
    @Column(name = "PARKING_ADDRESS_ROW_NAME")
    private String parkingAddressRowName;


	@Column(name = "PARKING_ADDRESS_PRIORITY")
    private String parkingAddressPriority;
    

    
    private static final long serialVersionUID = 1L;


	public ParkingAddress() {
		super();
	}



	public ParkingAddress(ParkingAddressId id) {
		super();
		this.id = id;
	}



	public String getVin() {
		return vin;
	}



	public void setVin(String vin) {
		this.vin = vin;
	}
	



	public String getParkingAddressPriority() {
		return parkingAddressPriority;
	}



	public void setParkingAddressPriority(String parkingAddressPriority) {
		this.parkingAddressPriority = parkingAddressPriority;
	}



	


	public ParkingAddressId getId() {
		 return this.id;
	}
	

	public String getParkingAddressRowName() {
		return parkingAddressRowName;
	}



	public void setParkingAddressRowName(String parkingAddressRowName) {
		this.parkingAddressRowName = parkingAddressRowName;
	}
	
	@Override
	public String toString() {
		return toString(getId().getParkingName(), 
				getId().getParkingAddressRow(), getId().getParkingAddressSpace(),
				getVin(), getParkingAddressRowName(),getParkingAddressPriority());
	}

    
	
}
