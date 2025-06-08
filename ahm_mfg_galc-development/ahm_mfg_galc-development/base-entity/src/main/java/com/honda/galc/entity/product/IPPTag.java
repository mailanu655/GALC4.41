package com.honda.galc.entity.product;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>IPPTag Class description</h3>
 * <p> IPPTag description </p>
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
 * Aug 22, 2011
 *
 *
 */
@Entity
@Table(name="GAL191TBX")
public class IPPTag extends AuditEntry {
	@EmbeddedId
	private IPPTagId id;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;	
	
	
	@Column(name="PROCESS_POINT_ID")
	private String processPointId;	
	

	private static final long serialVersionUID = 1L;

	public IPPTag() {
		super();
	}

	public IPPTag(IPPTagId id, Timestamp actualTimestamp) {
		super();
		this.id = id;
		this.actualTimestamp = actualTimestamp;
	}

	public IPPTagId getId() {
		return this.id;
	}

	public void setId(IPPTagId id) {
		this.id = id;
	}

	public Timestamp getActualTimestamp() {
		return this.actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}
	
	public String getProductId() {
		return id == null ? null : getId().getProductId();		
	}

	public String getIppTagNo() {
		return id == null ? null : getId().getIppTagNo();		
	}

	public String getDivisionId() {
		return id == null ? null : getId().getDivisionId();		
	}

	public String toString() {
		return toString(id.getProductId(),id.getIppTagNo(),id.getDivisionId());
	}
	
	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}



}
