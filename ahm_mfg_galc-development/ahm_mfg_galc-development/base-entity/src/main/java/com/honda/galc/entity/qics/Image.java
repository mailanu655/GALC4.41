package com.honda.galc.entity.qics;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>Image Class description</h3>
 * <p> Image description </p>
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
@Table(name = "GAL173TBX")
public class Image extends AuditEntry {
    @Id
    @Column(name = "BITMAP_FILE_NAME")
    private String bitmapFileName;

    @Column(name = "IMAGE_NAME")
    private String imageName;

    @Column(name = "IMAGE_DESCRIPTION_SHORT")
    private String imageDescriptionShort;

    @Column(name = "IMAGE_DESCRIPTION_LONG")
    private String imageDescriptionLong;


    @Column(name = "IMAGE_DATA")
    @Lob
    private byte[] imageData;

    private static final long serialVersionUID = 1L;
    
    @OneToMany(targetEntity = ImageSection.class,fetch = FetchType.EAGER,cascade={})
    @ElementJoinColumn(name="IMAGE_NAME", referencedColumnName="IMAGE_NAME",updatable = false,insertable=false)
    @OrderBy(" imageSectionId ASC")
    private List<ImageSection> sections = new ArrayList<ImageSection>();

    public Image() {
        super();
    }

    public String getBitmapFileName() {
        return StringUtils.trim(this.bitmapFileName);
    }
    
    public String getId() {
    	return getBitmapFileName();
    }

    public void setBitmapFileName(String bitmapFileName) {
        this.bitmapFileName = bitmapFileName;
    }

    public String getImageName() {
        return StringUtils.trim(this.imageName);
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageDescriptionShort() {
        return StringUtils.trim(this.imageDescriptionShort);
    }

    public void setImageDescriptionShort(String imageDescriptionShort) {
        this.imageDescriptionShort = imageDescriptionShort;
    }

    public String getImageDescriptionLong() {
        return StringUtils.trim(this.imageDescriptionLong);
    }

    public void setImageDescriptionLong(String imageDescriptionLong) {
        this.imageDescriptionLong = imageDescriptionLong;
    }

    public byte[] getImageData() {
        return this.imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

	public List<ImageSection> getSections() {
		return sections;
	}

	public void setSections(List<ImageSection> sections) {
		this.sections = sections;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Image)) return false;
		Image other = (Image) o;
		return StringUtils.equals(getImageName(), other.getImageName());
	}

	@Override
	public String toString() {
		return toString(getBitmapFileName(), getImageName());	}
}
