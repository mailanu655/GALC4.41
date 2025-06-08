package com.honda.galc.entity.oif;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * Entity to save the Park Changes sending by YMS
 *
 */
@Entity 
@Table ( name = "GAL163TBX" )
public class ParkChange extends AuditEntry{

	private static final long serialVersionUID = 1L;
	@Id
	@Column ( name = "VIN" )
	private String vin;
	@Column ( name = "DATE_STRING" )
	private String date;
	@Column ( name = "TIME" )
	private String time;
	@Column ( name = "SEND_LOCATION" )
	private String sendLocation;
	@Column ( name = "TRAN_TYPE" )
	private String transactiontype;
	@Column ( name = "PARKING_LOCATION" )
	private String parkingLocation;
	@Column ( name = "PARK_CONTROL_NO" )
	private String parkControlNumber;
	
	/**
	 * @return the vin
	 */
	public String getVin() {
		return StringUtils.trim( vin );
	}
	/**
	 * @param vin the vin to set
	 */
	public void setVin(String vin) {
		this.vin = vin;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return StringUtils.trim( date );
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the time
	 */
	public String getTime() {
		return StringUtils.trim( time );
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}
	/**
	 * @return the sendLocation
	 */
	public String getSendLocation() {
		return StringUtils.trim( sendLocation );
	}
	/**
	 * @param sendLocation the sendLocation to set
	 */
	public void setSendLocation(String sendLocation) {
		this.sendLocation = sendLocation;
	}
	/**
	 * @return the transactiontype
	 */
	public String getTransactiontype() {
		return StringUtils.trim( transactiontype );
	}
	/**
	 * @param transactiontype the transactiontype to set
	 */
	public void setTransactiontype(String transactiontype) {
		this.transactiontype = transactiontype;
	}
	/**
	 * @return the parkingLocation
	 */
	public String getParkingLocation() {
		return StringUtils.trim( parkingLocation );
	}
	/**
	 * @param parkingLocation the parkingLocation to set
	 */
	public void setParkingLocation(String parkingLocation) {
		this.parkingLocation = parkingLocation;
	}
	/**
	 * @return the parkControlNumber
	 */
	public String getParkControlNumber() {
		return StringUtils.trim( parkControlNumber );
	}
	/**
	 * @param parkControlNumber the parkControlNumber to set
	 */
	public void setParkControlNumber(String parkControlNumber) {
		this.parkControlNumber = parkControlNumber;
	}
	
	public String getId() {
		return getVin();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime
				* result
				+ ((parkControlNumber == null) ? 0 : parkControlNumber
						.hashCode());
		result = prime * result
				+ ((parkingLocation == null) ? 0 : parkingLocation.hashCode());
		result = prime * result
				+ ((sendLocation == null) ? 0 : sendLocation.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result
				+ ((transactiontype == null) ? 0 : transactiontype.hashCode());
		result = prime * result + ((vin == null) ? 0 : vin.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParkChange other = (ParkChange) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (parkControlNumber == null) {
			if (other.parkControlNumber != null)
				return false;
		} else if (!parkControlNumber.equals(other.parkControlNumber))
			return false;
		if (parkingLocation == null) {
			if (other.parkingLocation != null)
				return false;
		} else if (!parkingLocation.equals(other.parkingLocation))
			return false;
		if (sendLocation == null) {
			if (other.sendLocation != null)
				return false;
		} else if (!sendLocation.equals(other.sendLocation))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (transactiontype == null) {
			if (other.transactiontype != null)
				return false;
		} else if (!transactiontype.equals(other.transactiontype))
			return false;
		if (vin == null) {
			if (other.vin != null)
				return false;
		} else if (!vin.equals(other.vin))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.honda.galc.entity.AbstractEntity#toString(java.lang.Object[])
	 */
	@Override
	protected String toString(Object... objects) {
		return super.toString(vin,date,time,transactiontype,parkingLocation,parkControlNumber);
	}
	
}
