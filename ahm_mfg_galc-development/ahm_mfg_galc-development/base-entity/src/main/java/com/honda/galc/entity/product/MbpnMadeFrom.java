package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


/**
 * 
 * <h3>MbpnMadeFrom</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MbpnMadeFrom description </p>
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
 * <TD>May 24, 2017</TD>
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
 * @since May 24, 2017
 */
@Entity
@Table(name="MBPN_MADE_FROM_TBX")
public class MbpnMadeFrom extends AuditEntry {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MbpnMadeFromId id;

	@Column(name="FROM_MAIN_NO")
	private String fromMainNo;

	@Column(name="FROM_PRODUCT_ID")
	private String fromProductId;

	public MbpnMadeFrom() {
	}

	public MbpnMadeFromId getId() {
		return this.id;
	}

	public void setId(MbpnMadeFromId id) {
		this.id = id;
	}

	public String getFromMainNo() {
		return StringUtils.trim(this.fromMainNo);
	}

	public void setFromMainNo(String fromMainNo) {
		this.fromMainNo = fromMainNo;
	}

	public String getFromProductId() {
		return StringUtils.trim(this.fromProductId);
	}

	public void setFromProductId(String fromProductId) {
		this.fromProductId = fromProductId;
	}
}