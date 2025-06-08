package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>EngineManifestHisId</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EngineManifestHisId description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Mar 17, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Mar 17, 2017
 */
@Embeddable
public class EngineManifestHisId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="ENGINE_NO")
	private String engineNo;

	private String company="";

	private String plant="";

	private String department="";

	private String line="";

	private String process="";

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ACTUAL_TIMESTAMP")
	private java.util.Date actTimestamp;

	public EngineManifestHisId() {
	}
	public String getEngineNo() {
		return StringUtils.trim(this.engineNo);
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getCompany() {
		return StringUtils.trim(this.company);
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPlant() {
		return StringUtils.trim(this.plant);
	}
	public void setPlant(String plant) {
		this.plant = plant;
	}
	public String getDepartment() {
		return StringUtils.trim(this.department);
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getLine() {
		return StringUtils.trim(this.line);
	}
	public void setLine(String line) {
		this.line = line;
	}
	public String getProcess() {
		return StringUtils.trim(this.process);
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public java.util.Date getActTimestamp() {
		return this.actTimestamp;
	}
	public void setActTimestamp(java.util.Date actTimestamp) {
		this.actTimestamp = actTimestamp;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof EngineManifestHisId)) {
			return false;
		}
		EngineManifestHisId castOther = (EngineManifestHisId)other;
		return 
			this.getEngineNo().equals(castOther.getEngineNo())
			&& this.getCompany().equals(castOther.getCompany())
			&& this.getPlant().equals(castOther.getPlant())
			&& this.getDepartment().equals(castOther.getDepartment())
			&& this.getLine().equals(castOther.getLine())
			&& this.getProcess().equals(castOther.getProcess())
			&& this.actTimestamp.equals(castOther.actTimestamp);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.getEngineNo().hashCode();
		hash = hash * prime + this.getCompany().hashCode();
		hash = hash * prime + this.getPlant().hashCode();
		hash = hash * prime + this.getDepartment().hashCode();
		hash = hash * prime + this.getLine().hashCode();
		hash = hash * prime + this.getProcess().hashCode();
		hash = hash * prime + this.actTimestamp.hashCode();
		
		return hash;
	}
}