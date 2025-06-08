package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>LotTraceabilityId Class description</h3>
 * <p> LotTraceabilityId description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Vivek Bettada<br>
 * Mar 02, 2015
 *
 *
 */
@Embeddable
public class LotTraceabilityId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "LSN")
	private String lsn;

	@Column(name = "KD_LOT")
	private String kdLotNumber;


	public LotTraceabilityId() {
		super();
	}

	public LotTraceabilityId(String lsn, String kdLotNumber) {
		super();
		this.lsn = lsn;
		this.kdLotNumber = kdLotNumber;
	}

	public String getLsn()  {
		return StringUtils.trim(lsn);
	}

	public void setLsn(String newLsn)  {
		lsn=newLsn;
	}

	public String getKdLotNumber()  {
		return StringUtils.trim(kdLotNumber);
	}

	public void setKdLotNumber(String newKdLotNumber)  {
		kdLotNumber=newKdLotNumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((lsn == null) ? 0 : lsn.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LotTraceabilityId other = (LotTraceabilityId) obj;
		if (kdLotNumber == null) {
			if (other.kdLotNumber != null)
				return false;
		} else if (!kdLotNumber.equals(other.kdLotNumber))
			return false;
		if (lsn == null) {
			if (other.lsn != null)
				return false;
		} else if (!lsn.equals(other.lsn))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LotTraceabilityId [lsn=");
		builder.append(lsn);
		builder.append(", kdLotNumber=");
		builder.append(kdLotNumber);
		builder.append("]");
		return builder.toString();
	}
	
}
