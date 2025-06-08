package com.honda.galc.entity.product;
import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class HostPriorityPlanId implements Serializable {
	@Column(name="AFAE_FLAG")
	private String afaeFlag;

	@Column(name="PLAN_CODE")
	private String planCode;

	@Column(name="LINE_NUMBER")
	private String lineNumber;

	@Column(name="PLAN_PROC_LOC")
	private String planProcLoc;

	@Column(name="WE_LINE_NUMBER")
	private String weLineNumber;

	@Column(name="WE_PLAN_PROC_LOC")
	private String wePlanProcLoc;

	@Column(name="PA_LINE_NUMBER")
	private String paLineNumber;

	@Column(name="PA_PLAN_PROC_LOC")
	private String paPlanProcLoc;

	@Column(name="AFAE_OFF_DATE")
	private Date afaeOffDate;

	@Column(name="PROD_SEQ_NUMBER")
	private String prodSeqNumber;

	@Column(name="CREATE_DATE")
	private Date createDate;

	private static final long serialVersionUID = 1L;
	
	private static String KNUCKLE_ID = "KN";

	public HostPriorityPlanId() {
		super();
	}

	public String getAfaeFlag() {
		return StringUtils.trim(this.afaeFlag);
	}

	public void setAfaeFlag(String afaeFlag) {
		this.afaeFlag = afaeFlag;
	}

	public String getPlanCode() {
		return StringUtils.trim(this.planCode);
	}
	
	public String getPlantCode() {
		return getPlanCode().substring(0, 3);
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getLineNumber() {
		return StringUtils.trim(this.lineNumber);
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getPlanProcLoc() {
		return StringUtils.trim(this.planProcLoc);
	}

	public void setPlanProcLoc(String planProcLoc) {
		this.planProcLoc = planProcLoc;
	}

	public String getWeLineNumber() {
		return StringUtils.trim(this.weLineNumber);
	}

	public void setWeLineNumber(String weLineNumber) {
		this.weLineNumber = weLineNumber;
	}

	public String getWePlanProcLoc() {
		return StringUtils.trim(this.wePlanProcLoc);
	}

	public void setWePlanProcLoc(String wePlanProcLoc) {
		this.wePlanProcLoc = wePlanProcLoc;
	}

	public String getPaLineNumber() {
		return StringUtils.trim(this.paLineNumber);
	}

	public void setPaLineNumber(String paLineNumber) {
		this.paLineNumber = paLineNumber;
	}

	public String getPaPlanProcLoc() {
		return StringUtils.trim(this.paPlanProcLoc);
	}
	
	public String getKnPlanProcLoc() {
		return KNUCKLE_ID;
	}

	public void setPaPlanProcLoc(String paPlanProcLoc) {
		this.paPlanProcLoc = paPlanProcLoc;
	}

	public Date getAfaeOffDate() {
		return this.afaeOffDate;
	}

	public void setAfaeOffDate(Date afaeOffDate) {
		this.afaeOffDate = afaeOffDate;
	}

	public String getProdSeqNumber() {
		return StringUtils.trim(this.prodSeqNumber);
	}

	public void setProdSeqNumber(String prodSeqNumber) {
		this.prodSeqNumber = prodSeqNumber;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	public String deriveProductionLot() {
		
		return getPlantCode() + " " + getLineNumber() + getPlanProcLoc() + getProdSeqNumber(); 
		
	}
	
	public String deriveProductionLot(String processLocation) {
		return getPlantCode() + " " + getLineNumber() + processLocation + getProdSeqNumber(); 
	}
		
	
	public String deriveKnuckleProductionLot() {

		return getPlantCode() + " " + getLineNumber() + KNUCKLE_ID + getProdSeqNumber(); 

	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof HostPriorityPlanId)) {
			return false;
		}
		HostPriorityPlanId other = (HostPriorityPlanId) o;
		return this.afaeFlag.equals(other.afaeFlag)
			&& this.planCode.equals(other.planCode)
			&& this.lineNumber.equals(other.lineNumber)
			&& this.planProcLoc.equals(other.planProcLoc)
			&& this.weLineNumber.equals(other.weLineNumber)
			&& this.wePlanProcLoc.equals(other.wePlanProcLoc)
			&& this.paLineNumber.equals(other.paLineNumber)
			&& this.paPlanProcLoc.equals(other.paPlanProcLoc)
			&& this.afaeOffDate.equals(other.afaeOffDate)
			&& this.prodSeqNumber.equals(other.prodSeqNumber)
			&& this.createDate.equals(other.createDate);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.afaeFlag.hashCode();
		hash = hash * prime + this.planCode.hashCode();
		hash = hash * prime + this.lineNumber.hashCode();
		hash = hash * prime + this.planProcLoc.hashCode();
		hash = hash * prime + this.weLineNumber.hashCode();
		hash = hash * prime + this.wePlanProcLoc.hashCode();
		hash = hash * prime + this.paLineNumber.hashCode();
		hash = hash * prime + this.paPlanProcLoc.hashCode();
		hash = hash * prime + this.afaeOffDate.hashCode();
		hash = hash * prime + this.prodSeqNumber.hashCode();
		hash = hash * prime + this.createDate.hashCode();
		return hash;
	}

}
