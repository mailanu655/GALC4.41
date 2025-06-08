package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;


import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import com.honda.galc.entity.enumtype.QiEntryScreenType;
/**
 * 
 * <h3>QIEntryScreen Class description</h3>
 * <p>
 * QIEntryScreen contains the getter and setter of the entry screen properties and maps this class with database table and properties with the database its columns .
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
 *         July 5, 2016
 * 
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */
@Entity
@Table(name = "QI_ENTRY_SCREEN_TBX")
public class QiEntryScreen extends CreateUserAuditEntry {
	
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true)
	private QiEntryScreenId id;
	@Column(name = "ENTRY_SCREEN_DESCRIPTION")
	@Auditable(isPartOfPrimaryKey = true, sequence = 2)
	private String entryScreenDescription;
	@Column(name = "IS_IMAGE")
	@Auditable(isPartOfPrimaryKey = true, sequence = 3)
	private short isImage;
	@Column(name = "PRODUCT_TYPE")
	@Auditable(isPartOfPrimaryKey = true, sequence = 4)
	private String productType;
	@Column(name = "IMAGE_NAME")
	@Auditable(isPartOfPrimaryKey = true, sequence = 5)
	private String imageName;
	@Column(name = "ACTIVE")
	@Auditable
	private short active;
	@Column(name = "SCREEN_IS_USED")
	@Auditable
	private short screenIsUsed;

	public QiEntryScreen() {
		
	}
	
	public QiEntryScreen(String entryScreen,String entryModel, Short isUsed) {
		this.id = new QiEntryScreenId(entryScreen,entryModel,isUsed);
	}

	public String getEntryScreenDescription() {
		return StringUtils.trimToEmpty(this.entryScreenDescription);
	}


	public void setEntryScreenDescription(String entryScreenDescription) {
		this.entryScreenDescription = entryScreenDescription;
	}

	public short getIsImage() {
		return isImage;
	}

	public void setIsImage(short isImage) {
		this.isImage = isImage;
	}
	
	public boolean isImage() {
		return this.isImage>=1;
	}

	public void setImage(boolean image) {
		this.isImage =(short)( image ? 1 : 0);
	}
	
	public String getScreenType() {
		return QiEntryScreenType.getType(isImage).getName();
	}

	public String getProductType() {
		return StringUtils.trimToEmpty(this.productType);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}	

	public String getImageName() {
		return StringUtils.trimToEmpty(this.imageName);
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public short getActiveValue() {
		return active;
	}
	
	public void setActiveValue(short active) {
        this.active = active;
    }
	
	public boolean isActive() {
        return this.active ==(short) 1;
    }

	public void setActive(boolean active) {
		this.active =(short)( active ? 1 : 0);
	}
	
	public String getStatus() {
		return QiActiveStatus.getType(active).getName();
	}
	
	public short getScreenIsUsed() {
		return screenIsUsed;
	}

	public void setScreenIsUsed(short screenIsUsed) {
		this.screenIsUsed = screenIsUsed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime
				* result
				+ ((entryScreenDescription == null) ? 0
						: entryScreenDescription.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((imageName == null) ? 0 : imageName.hashCode());
		result = prime * result + isImage;
		result = prime * result + ((productType == null) ? 0 : productType.hashCode());
		result = prime * result + screenIsUsed;
		return result;
	}

	public void setId(QiEntryScreenId id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiEntryScreen other = (QiEntryScreen) obj;
		if (active != other.active)
			return false;
		if (entryScreenDescription == null) {
			if (other.entryScreenDescription != null)
				return false;
		} else if (!entryScreenDescription.equals(other.entryScreenDescription))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		if (isImage != other.isImage)
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		if (screenIsUsed != other.screenIsUsed)
			return false;
		return true;
	}

	public QiEntryScreenId getId() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return toString(getId().getEntryScreen(), getId().getEntryModel(), getId().getIsUsed(),getEntryScreenDescription(),isImage(),getProductType(), 
				getImageName(),getActiveValue(),getScreenIsUsed(),getScreenType());
	} 
}
