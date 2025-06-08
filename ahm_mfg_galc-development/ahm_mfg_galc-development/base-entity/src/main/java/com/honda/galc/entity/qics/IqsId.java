package com.honda.galc.entity.qics;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>IqsId Class description</h3>
 * <p> IqsId description </p>
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
 * Mar 30, 2011
 *
 *
 */
   /**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 30, 2014
 IQS and Regression Code Maintenance Screens
 */
@Embeddable
public class IqsId implements Serializable {
	@Column(name="IQS_CATEGORY_NAME")
	private String iqsCategoryName;

	@Column(name="IQS_ITEM_NAME")
	private String iqsItemName;

	private static final long serialVersionUID = 1L;

	public IqsId() {
		super();
	}
	
	

	public IqsId(String iqsCategoryName, String iqsItemName) {
		super();
		this.iqsCategoryName = iqsCategoryName;
		this.iqsItemName = iqsItemName;
	}



	public String getIqsCategoryName() {
		return StringUtils.trim(this.iqsCategoryName);
	}

	public void setIqsCategoryName(String iqsCategoryName) {
		this.iqsCategoryName = iqsCategoryName;
	}

	public String getIqsItemName() {
		return StringUtils.trim(this.iqsItemName);
	}

	public void setIqsItemName(String iqsItemName) {
		this.iqsItemName = iqsItemName;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof IqsId)) {
			return false;
		}
		IqsId other = (IqsId) o;
		return this.iqsCategoryName.equals(other.iqsCategoryName)
			&& this.iqsItemName.equals(other.iqsItemName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.iqsCategoryName.hashCode();
		hash = hash * prime + this.iqsItemName.hashCode();
		return hash;
	}

}
