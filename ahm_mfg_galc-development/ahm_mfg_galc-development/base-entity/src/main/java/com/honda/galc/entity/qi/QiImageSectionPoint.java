package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;

/** * *
 * @version 0.2
* @author LnT Infotech
* @since May 18, 2016
*/

@Entity
@Table(name="QI_IMAGE_SECTION_POINT_TBX")
public class QiImageSectionPoint extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private QiImageSectionPointId id;

	@Column(name="POINT_X")
	@Auditable
	private int pointX;

	@Column(name="POINT_Y")
	@Auditable
	private int pointY;

	public QiImageSectionPoint() {
		super();
	}

	public QiImageSectionPointId getId() {
		return this.id;
	}

	public void setId(QiImageSectionPointId id) {
		this.id = id;
	}

	public int getPointX() {
		return this.pointX;
	}

	public void setPointX(int pointX) {
		this.pointX = pointX;
	}

	public int getPointY() {
		return this.pointY;
	}

	public void setPointY(int pointY) {
		this.pointY = pointY;
	}
	
	public void setImageSectionId(int imageSectionId) {
		if(id == null) id = new QiImageSectionPointId();
		id.setImageSectionId(imageSectionId);
	}
	
	public int getImageSectionId() {
		return id.getImageSectionId();
	}
	
	public void setPointSequenceNo(int pointSequenceNo) {
		if(id == null) id = new QiImageSectionPointId();
		id.setPointSequenceNo(pointSequenceNo);
	}
	
	public int getPointSequenceNo() {
		return id.getPointSequenceNo();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + pointX;
		result = prime * result + pointY;
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
		QiImageSectionPoint other = (QiImageSectionPoint) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (pointX != other.pointX)
			return false;
		if (pointY != other.pointY)
			return false;
		return true;
	}

}
