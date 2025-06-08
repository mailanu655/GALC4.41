package com.honda.galc.entity.product;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>EngineFiringResult Class description</h3>
 * <p> EngineFiringResult description </p>
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
@Entity
@Table(name="GAL141TBX")
public class EngineFiringResult extends AuditEntry {
	@EmbeddedId
	private EngineFiringResultId id;

	@Column(name="FIRING_TEST_TYPE")
	private String firingTestType;

	@Column(name="ASSOCIATE_NO")
	private String associateNo;

	@Column(name="FIRING_NOTES")
	private String firingNotes;

	@Column(name="FIRING_STATUS")
	private short firingStatus;

	@Column(name="FIRING_BENCH_NO")
	private int firingBenchNo;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;


	private static final long serialVersionUID = 1L;

	public EngineFiringResult() {
		super();
	}

	public EngineFiringResultId getId() {
		return this.id;
	}

	public void setId(EngineFiringResultId id) {
		this.id = id;
	}
	
	public String getProductId() {
		return id == null ? null : id.getEngineSerialNo();
	}
	
	public int getResultId() {
		return id == null ? 0: id.getResultId();
	}

	public String getFiringTestType() {
		return StringUtils.trim(this.firingTestType);
	}

	public void setFiringTestType(String firingTestType) {
		this.firingTestType = firingTestType;
	}

	public String getAssociateNo() {
		return StringUtils.trim(this.associateNo);
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}

	public String getFiringNotes() {
		return StringUtils.trim(this.firingNotes);
	}

	public void setFiringNotes(String firingNotes) {
		this.firingNotes = firingNotes;
	}

	public short getFiringStatus() {
		return this.firingStatus;
	}

	public void setFiringStatus(short firingStatus) {
		this.firingStatus = firingStatus;
	}

	public int getFiringBenchNo() {
		return this.firingBenchNo;
	}

	public void setFiringBenchNo(int firingBenchNo) {
		this.firingBenchNo = firingBenchNo;
	}

	public Timestamp getActualTimestamp() {
		return this.actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}


}
