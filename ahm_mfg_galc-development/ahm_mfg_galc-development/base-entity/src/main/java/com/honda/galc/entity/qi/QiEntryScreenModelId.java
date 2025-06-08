package com.honda.galc.entity.qi;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>QiEntryScreenModelId Class description</h3>
 * <p>
 * QiEntryScreenModelId contains the getter and setter of the Entry Screen Model composite key and maps this class with database and these columns .
 * </p>
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
 * @author LnTInfotech<br>
 *        Jul 26, 2016
 * 
 */
@Embeddable
public class QiEntryScreenModelId  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "ENTRY_SCREEN", nullable=false)
	private String entryScreen;
	
	@Column(name = "ENTRY_MODEL")
	private String entryModel;
	
    public QiEntryScreenModelId() {
		super();
	}


    public QiEntryScreenModelId(String entryScreen,String entryModel) {
    	this.setEntryModel(entryModel);
    	this.setEntryScreen(entryScreen);
    }


	public String getEntryScreen() {
		return StringUtils.trimToEmpty(entryScreen);
	}

	public void setEntryScreen(String entryScreen) {
		this.entryScreen = entryScreen;
	}

	public String getEntryModel() {
		return StringUtils.trimToEmpty(entryModel);
	}

	public void setEntryModel(String entryModel) {
		this.entryModel = entryModel;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((entryModel == null) ? 0 : entryModel.hashCode());
		result = prime * result
				+ ((entryScreen == null) ? 0 : entryScreen.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiEntryScreenModelId other = (QiEntryScreenModelId) obj;
		if (entryModel == null) {
			if (other.entryModel != null)
				return false;
		} else if (!entryModel.equals(other.entryModel))
			return false;
		if (entryScreen == null) {
			if (other.entryScreen != null)
				return false;
		} else if (!entryScreen.equals(other.entryScreen))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}

}
