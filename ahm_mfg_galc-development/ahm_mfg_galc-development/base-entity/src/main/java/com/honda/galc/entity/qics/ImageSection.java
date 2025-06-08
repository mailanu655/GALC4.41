package com.honda.galc.entity.qics;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>ImageSection Class description</h3>
 * <p> ImageSection description </p>
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
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL174TBX")
public class ImageSection extends AuditEntry {
    @Id
    @Column(name = "IMAGE_SECTION_ID")
    private int imageSectionId;

    @Column(name = "IMAGE_NAME")
    private String imageName;

    @Column(name = "PART_KIND_FLAG")
    private short partKindFlag;

    @Column(name = "DESCRIPTION_ID")
    private int descriptionId;

    @Column(name = "OVERLAY_NO")
    private int overlayNo;

    @OneToMany(targetEntity = ImageSectionPoint.class,fetch = FetchType.EAGER,cascade={})
    @ElementJoinColumn(name="IMAGE_SECTION_ID", referencedColumnName="IMAGE_SECTION_ID",updatable = false,insertable=false)
    @OrderBy
    private List<ImageSectionPoint> imageSectionPoints = new ArrayList<ImageSectionPoint>();

    
    private static final long serialVersionUID = 1L;

    @Transient
    private DefectDescription defectDescription;
    
    public ImageSection() {
        super();
    }

    public int getImageSectionId() {
        return this.imageSectionId;
    }
    
    public Integer getId() {
    	return getImageSectionId();
    }

    public void setImageSectionId(int imageSectionId) {
        this.imageSectionId = imageSectionId;
    }

    public String getImageName() {
        return StringUtils.trim(this.imageName);
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public short getPartKindFlagValue() {
        return this.partKindFlag;
    }

    public void setPartKindFlagValue(short partKindFlag) {
        this.partKindFlag = partKindFlag;
    }
    
    public boolean getPartKindFlag() {
        return this.partKindFlag == 1 ? true : false;
    }

    public void setPartKindFlag(boolean partKindFlag) {
        this.partKindFlag =(short)( partKindFlag ? 1:0);
    }

    public int getDescriptionId() {
        return this.descriptionId;
    }

    public void setDescriptionId(int descriptionId) {
        this.descriptionId = descriptionId;
    }

    public int getOverlayNo() {
        return this.overlayNo;
    }

    public void setOverlayNo(int overlayNo) {
        this.overlayNo = overlayNo;
    }
    
	public List<ImageSectionPoint> getImageSectionPoints() {
		return imageSectionPoints;
	}
	
	public void setImageSectionPoints(List<ImageSectionPoint> imageSectionPoints) {
		this.imageSectionPoints = imageSectionPoints;
	}
	
	public List<Point> getPoints() {
		List<Point> points = new ArrayList<Point>();
		if (getImageSectionPoints() == null || getImageSectionPoints().isEmpty()) {
			return points;
		}

		for (ImageSectionPoint imageSectionPoint : getImageSectionPoints()) {
			if (imageSectionPoint == null) {
				continue;
			}
			Point point = new Point(imageSectionPoint.getPointX(), imageSectionPoint.getPointY());
			points.add(point);
		}
		return points;
	}

	public DefectDescription getDefectDescription() {
		return defectDescription;
	}

	public void setDefectDescription(DefectDescription defectDescription) {
		this.defectDescription = defectDescription;
	}

	@Override
	public String toString() {
		return toString(getId(),getImageName());
	}


}
