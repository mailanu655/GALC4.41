package com.honda.galc.entity.product;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3>
 * Entity for data collection image.
 * 
 * <h4>Description</h4>
 * <p>
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Sep. 12, 2017</TD>
 * <TD>1.0</TD>
 * <TD>20170912</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Dylan Yang
 */
@Entity
@Table(name = "DATA_COLLECTION_IMAGE_TBX")
public class DataCollectionImage extends AuditEntry implements IDto {
	private static final long serialVersionUID = -7732540055361465073L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IMAGE_ID", nullable = false)
	@DtoTag(outputName = "IMAGE_ID")
	private int imageId;

    @Column(name = "IMAGE_NAME")
	@DtoTag(outputName = "IMAGE_NAME")
    private String imageName;

    @Column(name = "IMAGE_DESCRIPTION")
	@DtoTag(outputName = "IMAGE_DESCRIPTION")
    private String imageDescription;

    @Column(name = "INFORMATION")
	@DtoTag(outputName = "INFORMATION")
    private String information;

    @Column(name = "IMAGE_DATA")
    @Lob
    private byte[] imageData;

    @Column(name = "ACTIVE")
	@DtoTag(outputName = "ACTIVE")
    private short active;
    
    public DataCollectionImage() {
        super();
    }

	public Object getId() {
		return getImageId();
	}

    public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
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

    public String getInformation() {
        return StringUtils.trimToEmpty(this.information);
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public byte[] getImageData() {
        return this.imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

	public short getActiveValue() {
		return active;
	}
	
	public void setActiveValue(short active) {
        this.active = active;
    }
	
	public boolean isActive() {
        return this.active == (short) 1;
    }

	public void setActive(boolean active) {
		this.active = (short) (active ? 1 : 0);
	}

	@Override
	public String toString() {
		return String.format("%5d - %s", imageId, imageName);	
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result	+ (imageId);
		result = prime * result + Arrays.hashCode(imageData);
		result = prime * result + ((imageDescription == null) ? 0 : imageDescription.hashCode());
		result = prime * result + ((imageName == null) ? 0 : imageName.hashCode());
		result = prime * result + ((information == null) ? 0 : information.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DataCollectionImage)) {
			return false;
		}

		DataCollectionImage other = (DataCollectionImage) obj;

		return (active == other.getActiveValue()) 
				&& getImageId() == other.getImageId()
				&& StringUtils.equals(getImageName(), other.getImageName())
				&& Arrays.equals(getImageData(), other.getImageData()) 
				&& StringUtils.equals(getImageDescription(), other.getImageDescription())
				&& StringUtils.equals(getInformation(), other.getInformation());
	}

}
