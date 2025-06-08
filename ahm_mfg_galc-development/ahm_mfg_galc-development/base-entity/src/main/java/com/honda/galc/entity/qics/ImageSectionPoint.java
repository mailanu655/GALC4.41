package com.honda.galc.entity.qics;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;
/**
 * 
 * <h3>ImageSectionPoint Class description</h3>
 * <p> ImageSectionPoint description </p>
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
@Entity
@Table(name="GAL175TBX")
public class ImageSectionPoint extends AuditEntry {
	@EmbeddedId
	private ImageSectionPointId id;

	@Column(name="POINT_X")
	private int pointX;

	@Column(name="POINT_Y")
	private int pointY;

	private static final long serialVersionUID = 1L;

	public ImageSectionPoint() {
		super();
	}

	public ImageSectionPointId getId() {
		return this.id;
	}

	public void setId(ImageSectionPointId id) {
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
		if(id == null) id = new ImageSectionPointId();
		id.setImageSectionId(imageSectionId);
	}
	
	public int getImageSectionId() {
		return id.getImageSectionId();
	}
	
	public void setPointSequenceNo(int pointSequenceNo) {
		if(id == null) id = new ImageSectionPointId();
		id.setPointSequenceNo(pointSequenceNo);
	}
	
	public int getPointSequenceNo() {
		return id.getPointSequenceNo();
	}

	@Override
	public String toString() {
		return toString(getImageSectionId(),getPointX(),getPointY());
	} 
	



}
