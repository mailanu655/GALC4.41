package com.honda.galc.entity.qi;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
/**
 * 
 * <h3>QiImageSection Class description</h3>
 * <p>
 * QiImageSection contains the getter and setter of the ImageSection properties and maps this class with database table and properties with the database its columns .
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
 *         May 10, 2016
 * 
 */
@Entity
@Table(name = "QI_IMAGE_SECTION_TBX")
public class QiImageSection extends CreateUserAuditEntry {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private QiImageSectionId id;
	
	@Column(name = "IMAGE_NAME")
	@Auditable(isPartOfPrimaryKey = true, sequence = 2)
	private String imageName;
	
	@OneToMany(targetEntity = QiImageSectionPoint.class,fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @ElementJoinColumn(name="IMAGE_SECTION_ID", referencedColumnName="IMAGE_SECTION_ID")
    @OrderBy
    private List<QiImageSectionPoint> qiImageSectionPoints = new ArrayList<QiImageSectionPoint>();
	
	public QiImageSection() {
		super();
	}
	
	public QiImageSectionId getId() {
		return this.id;
	}

	public void setId(QiImageSectionId id) {
		this.id = id;
	}
	
	public String getImageName() {
		return StringUtils.trimToEmpty(this.imageName);
	}
	
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public List<QiImageSectionPoint> getQiImageSectionPoints() {
		return qiImageSectionPoints;
	}
	
	public void setQiImageSectionPoints(List<QiImageSectionPoint> qiImageSectionPoints) {
		this.qiImageSectionPoints = qiImageSectionPoints;
	}
	
	public int getImageSectionId() {
		return id.getImageSectionId();
	}
	
	public void setImageSectionId(int imageSectionId) {
		if(id == null) id = new QiImageSectionId();
		id.setImageSectionId(imageSectionId);
	}
	
	public int getPartLocationId() {
		return id.getPartLocationId();
	}
	
	public void setPartLocationId(int partLocationId) {
		if(id == null) id = new QiImageSectionId();
		id.setPartLocationId(partLocationId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((imageName == null) ? 0 : imageName.hashCode());
		result = prime
				* result
				+ ((qiImageSectionPoints == null) ? 0 : qiImageSectionPoints
						.hashCode());
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
		QiImageSection other = (QiImageSection) obj;
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
		if (qiImageSectionPoints == null) {
			if (other.qiImageSectionPoints != null)
				return false;
		} else if (!qiImageSectionPoints.equals(other.qiImageSectionPoints))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiImageSection [id=" + id + ", imageName=" + imageName
				+ ", qiImageSectionPoints=" + qiImageSectionPoints + "]";
	}
	
}
