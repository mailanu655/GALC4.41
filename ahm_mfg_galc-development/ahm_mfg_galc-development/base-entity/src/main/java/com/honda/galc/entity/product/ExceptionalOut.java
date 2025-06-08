package com.honda.galc.entity.product;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name="GAL136TBX")
public class ExceptionalOut extends AuditEntry{
	@EmbeddedId
	private ExceptionalOutId id;

	@Column(name="ASSOCIATE_NO")
	private String associateNo;

	@Column(name="EXCEPTIONAL_OUT_COMMENT")
	private String exceptionalOutComment;

	@Column(name="EXCEPTIONAL_OUT_REASON_STRING")
	private String exceptionalOutReasonString;

	@Column(name="PRODUCTION_DATE")
	private Date productionDate;

	@Column(name="PROCESS_POINT_ID")
	private String processPointId;

	@Column(name="LOCATION")
	private String location;

	private static final long serialVersionUID = 1L;

	public ExceptionalOut() {
		super();
	}

	public ExceptionalOut(ExceptionalOutId id, String associateNo,
			String exceptionalOutComment, String exceptionalOutReasonString,
			Date productionDate, String processPointId, String location) {
		super();
		this.id = id;
		this.associateNo = associateNo;
		this.exceptionalOutComment = exceptionalOutComment;
		this.exceptionalOutReasonString = exceptionalOutReasonString;
		this.productionDate = productionDate;
		this.processPointId = processPointId;
		this.location = location;
	}

	public ExceptionalOutId getId() {
		return this.id;
	}

	public void setId(ExceptionalOutId id) {
		this.id = id;
	}

	public String getAssociateNo() {
		return StringUtils.trim(this.associateNo);
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}

	public String getExceptionalOutComment() {
		return StringUtils.trim(this.exceptionalOutComment);
	}

	public void setExceptionalOutComment(String exceptionalOutComment) {
		this.exceptionalOutComment = exceptionalOutComment;
	}

	public String getExceptionalOutReasonString() {
		return StringUtils.trim(this.exceptionalOutReasonString);
	}

	public void setExceptionalOutReasonString(String exceptionalOutReasonString) {
		this.exceptionalOutReasonString = exceptionalOutReasonString;
	}

	public Date getProductionDate() {
		return this.productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}


	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getProductId() {
		return getId().getProductId();
	}
	
	public void setProductId(String productId) {
		
		if(id == null) id = new ExceptionalOutId();
		id.setProductId(productId);
		
	}
	
	public static ExceptionalOut create(BaseProduct product,String processPointId,Date productionDate) {
		
		ExceptionalOutId id = new ExceptionalOutId();
		id.setProductId(product.getProductId());
		id.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		ExceptionalOut exceptionalOut = new ExceptionalOut();
		exceptionalOut.setId(id);
		exceptionalOut.setProcessPointId(processPointId);
		exceptionalOut.setExceptionalOutReasonString(getReason(product));
		exceptionalOut.setProductionDate(productionDate);
		return exceptionalOut;
		
	}
	
	private static String getReason(BaseProduct product) {
		if(product.isScrapStatus()) return "Scrap";
		else return "Preheat";
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(":");
		sb.append("[productId:").append(getProductId()).append(", ");
		sb.append("actualTimestamp:").append(getId().getActualTimestamp()).append(", ");
		sb.append("processPointId:").append(getProcessPointId()).append(", ");
		sb.append("productionDate:").append(getProductionDate()).append(", ");
		sb.append("exceptionalOutComment:").append(getExceptionalOutComment()).append(",");;
		sb.append("exceptionalOutReason:").append(getExceptionalOutReasonString()).append(", ");
		sb.append("associateNo:").append(getAssociateNo()).append("]");;
		return sb.toString();
	}

}
