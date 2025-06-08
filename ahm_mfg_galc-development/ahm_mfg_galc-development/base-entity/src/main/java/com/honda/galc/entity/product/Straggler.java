package com.honda.galc.entity.product;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Jan 05, 2015
 */




@Entity
@Table(name="STRAGGLER_TBX")
public class Straggler extends AuditEntry {

	@EmbeddedId
	private StragglerId id;
	
	@Column(name="LAST_PASSING_PROCESS_POINT")
	private String lastPassingProcessPoint;

	@Column(name="IDENTIFIED_TIMESTAMP")
	private Timestamp identifiedTimestamp;
	
	@Column(name="ACTUAL_PROCESS_TIMESTAMP")
	private Timestamp actualProcessTimestamp;
	
	@Column(name="UNITS_BEHIND")
	private Integer unitsBehind;
	
	@Column(name="ACTUAL_UNITS_BEHIND")
	private Integer actualUnitsBehind;
	
	@Column(name="CODE")
	private String code;
	
	@Column(name="COMMENTS")
	private String comments;

	private static final long serialVersionUID = 1L;

	public Straggler() {
		super();
	}

	public Straggler(StragglerId id, String lastPassingProcessPoint,
			Timestamp identifiedTimestamp, Timestamp actualProcessTimestamp,
			Integer unitsBehind, Integer actualUnitsBehind) {
		super();
		this.id = id;
		this.lastPassingProcessPoint = lastPassingProcessPoint;
		this.identifiedTimestamp = identifiedTimestamp;
		this.actualProcessTimestamp = actualProcessTimestamp;
		this.unitsBehind = unitsBehind;
		this.actualUnitsBehind = actualUnitsBehind;
	}



	public String getProductId() {
		return this.id == null ? null : this.id.getProductId();
	}

	public String getPpDelayedAt() {
		return this.id == null ? null : this.id.getPpDelayedAt();
	}

	public String getLastPassingProcessPoint() {
		return StringUtils.trimToEmpty(lastPassingProcessPoint);
	}

	public void setLastPassingProcessPoint(String lastPassingProcessPoint) {
		this.lastPassingProcessPoint = lastPassingProcessPoint;
	}


	public Timestamp getIdentifiedTimestamp() {
		return identifiedTimestamp;
	}

	public void setIdentifiedTimestamp(Timestamp identifiedTimestamp) {
		this.identifiedTimestamp = identifiedTimestamp;
	}

	public Integer getUnitsBehind() {
		return unitsBehind;
	}

	public void setUnitsBehind(Integer unitsBehind) {
		this.unitsBehind = unitsBehind;
	}

	public Integer getActualUnitsBehind() {
		return actualUnitsBehind;
	}

	public void setActualUnitsBehind(Integer actualUnitsBehind) {
		this.actualUnitsBehind = actualUnitsBehind;
	}

	public String getCode() {
		return StringUtils.trim(code);
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getComments() {
		return StringUtils.trim(comments);
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	
	public Timestamp getActualProcessTimestamp() {
		return actualProcessTimestamp;
	}

	public void setActualProcessTimestamp(Timestamp actualProcessTimestamp) {
		this.actualProcessTimestamp = actualProcessTimestamp;
	}

	public StragglerId getId() {
		return this.id;
	}
	
	public void setId(StragglerId id) {
		 this.id=id;
	}
	
	@Override
	public String toString() {
		return toString(getId().getProductId(),getId().getPpDelayedAt(),getActualProcessTimestamp(),getActualUnitsBehind(),getUnitsBehind(),getIdentifiedTimestamp());
	}
	
}