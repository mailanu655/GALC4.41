package com.honda.galc.entity.product;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * <h3>EngineFiringResultId Class description</h3>
 * <p> EngineFiringResultId description </p>
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
 * @author Jeffray Huang<br>
 * Jul 27, 2011
 *
 *
 */
@Embeddable
public class EngineFiringResultId implements Serializable {
	@Column(name="ENGINE_SERIAL_NO")
	private String engineSerialNo;

	@Column(name="RESULT_ID")
	private int resultId;

	private static final long serialVersionUID = 1L;

	public EngineFiringResultId() {
		super();
	}

	public String getEngineSerialNo() {
		return this.engineSerialNo;
	}

	public void setEngineSerialNo(String engineSerialNo) {
		this.engineSerialNo = engineSerialNo;
	}

	public int getResultId() {
		return this.resultId;
	}

	public void setResultId(int resultId) {
		this.resultId = resultId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof EngineFiringResultId)) {
			return false;
		}
		EngineFiringResultId other = (EngineFiringResultId) o;
		return this.engineSerialNo.equals(other.engineSerialNo)
			&& (this.resultId == other.resultId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.engineSerialNo.hashCode();
		hash = hash * prime + this.resultId;
		return hash;
	}

}
