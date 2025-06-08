package com.honda.galc.entity.qi;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.entity.enumtype.QiActiveStatus;
/**
 * 
 * <h3>QiImage Class description</h3>
 * <p> QiImage description </p>
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
 * @author L&T Infotech<br>
 * May 06, 2016
 *
 *
 */

@Entity
@Table(name = "QI_IMAGE_TBX")
public class QiImage extends CreateUserAuditEntry {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "BITMAP_FILE_NAME")
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private String bitmapFileName;
	
	@Column(name = "IMAGE_NAME")
	@Auditable(isPartOfPrimaryKey = true, sequence = 2)
	private String imageName;
	
	@Column(name = "IMAGE_DESCRIPTION")
	@Auditable(isPartOfPrimaryKey = true, sequence = 3)
	private String imageDescription;
	
	@Column(name="IMAGE_DATA")
	@Lob
	@Auditable
	private byte[] imageData;
	
	@Column(name = "PRODUCT_KIND")
	private String productKind;
	
	@Column(name = "ACTIVE")
	@Auditable
	private short active;
	
	public QiImage()
	{
		super();
	}
	
	public String getImageName() {
		return StringUtils.trimToEmpty(this.imageName);
	}


	public void setImageName(String imageName) {
		this.imageName = imageName;
	}


	public String getImageDescription() {
		return StringUtils.trimToEmpty(this.imageDescription);
	}


	public void setImageDescription(String imageDescription) {
		this.imageDescription = imageDescription;
	}


	public String getBitmapFileName() {
		return StringUtils.trimToEmpty(this.bitmapFileName);
	}


	public void setBitmapFileName(String bitmapFileName) {
		this.bitmapFileName = bitmapFileName;
	}


	public byte[] getImageData() {
		return imageData;
	}


	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}


	public String getProductKind() {
		return StringUtils.trimToEmpty(this.productKind);
	}


	public void setProductKind(String productKind) {
		this.productKind = productKind;
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
	
	public Object getId() {
		return getBitmapFileName();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result
				+ ((bitmapFileName == null) ? 0 : bitmapFileName.hashCode());
		result = prime * result + Arrays.hashCode(imageData);
		result = prime
				* result
				+ ((imageDescription == null) ? 0 : imageDescription.hashCode());
		result = prime * result
				+ ((imageName == null) ? 0 : imageName.hashCode());
		result = prime * result
				+ ((productKind == null) ? 0 : productKind.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiImage other = (QiImage) obj;
		if (active != other.active)
			return false;
		if (bitmapFileName == null) {
			if (other.bitmapFileName != null)
				return false;
		} else if (!bitmapFileName.equals(other.bitmapFileName))
			return false;
		if (!Arrays.equals(imageData, other.imageData))
			return false;
		if (imageDescription == null) {
			if (other.imageDescription != null)
				return false;
		} else if (!imageDescription.equals(other.imageDescription))
			return false;
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		if (productKind == null) {
			if (other.productKind != null)
				return false;
		} else if (!productKind.equals(other.productKind))
			return false;
		return true;
	}

}
