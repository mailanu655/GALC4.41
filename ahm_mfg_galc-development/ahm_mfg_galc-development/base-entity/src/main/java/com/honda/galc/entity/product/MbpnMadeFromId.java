package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>MbpnMadeFromId</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MbpnMadeFromId description </p>
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
@Embeddable
public class MbpnMadeFromId implements Serializable{
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="MAIN_NO")
	private String mainNo;

	public MbpnMadeFromId() {
	}
	public String getProductId() {
		return StringUtils.trim(this.productId);
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getMainNo() {
		return StringUtils.trim(this.mainNo);
	}
	public void setMainNo(String mainNo) {
		this.mainNo = mainNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MbpnMadeFromId)) {
			return false;
		}
		MbpnMadeFromId castOther = (MbpnMadeFromId)other;
		return 
			this.productId.equals(castOther.productId)
			&& this.mainNo.equals(castOther.mainNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productId.hashCode();
		hash = hash * prime + this.mainNo.hashCode();
		
		return hash;
	}
	
}