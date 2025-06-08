package com.honda.galc.entity.conf;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.honda.galc.util.StringUtil;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Embeddable
public class ParkingAddressId implements Serializable {
    @Column(name = "PARKING_NAME")
    private String parkingName;

    @Column(name = "PARKING_ADDRESS_SPACE")
    private Integer parkingAddressSpace;
    
    @Column(name = "PARKING_ADDRESS_ROW")
    private Integer parkingAddressRow;
 
    private static final long serialVersionUID = 1L;

    
	public ParkingAddressId(String parkingName, Integer parkingAddressSpace,
			Integer parkingAddressRow) {
		super();
		this.parkingName = parkingName;
		this.parkingAddressSpace = parkingAddressSpace;
		this.parkingAddressRow = parkingAddressRow;
	}

	public ParkingAddressId() {
		super();
	}

	public String getParkingName() {
		return parkingName;
	}

	public void setParkingName(String parkingName) {
		this.parkingName = parkingName;
	}

	public Integer getParkingAddressSpace() {
		return parkingAddressSpace;
	}

	public void setParkingAddressSpace(Integer parkingAddressSpace) {
		this.parkingAddressSpace = parkingAddressSpace;
	}

	public Integer getParkingAddressRow() {
		return parkingAddressRow;
	}

	public void setParkingAddressRow(Integer parkingAddressRow) {
		this.parkingAddressRow = parkingAddressRow;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((parkingAddressRow == null) ? 0 : parkingAddressRow
						.hashCode());
		result = prime
				* result
				+ ((parkingAddressSpace == null) ? 0 : parkingAddressSpace
						.hashCode());
		result = prime * result
				+ ((parkingName == null) ? 0 : parkingName.hashCode());
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
		ParkingAddressId other = (ParkingAddressId) obj;
		if (parkingAddressRow == null) {
			if (other.parkingAddressRow != null)
				return false;
		} else if (!parkingAddressRow.equals(other.parkingAddressRow))
			return false;
		if (parkingAddressSpace == null) {
			if (other.parkingAddressSpace != null)
				return false;
		} else if (!parkingAddressSpace.equals(other.parkingAddressSpace))
			return false;
		if (parkingName == null) {
			if (other.parkingName != null)
				return false;
		} else if (!parkingName.equals(other.parkingName))
			return false;
		return true;
	}
	    
	@Override
	public String toString() {		
	  return StringUtil.toString(getClass().getSimpleName(),getParkingName(),getParkingAddressSpace(), getParkingAddressRow());
	}
 
}
