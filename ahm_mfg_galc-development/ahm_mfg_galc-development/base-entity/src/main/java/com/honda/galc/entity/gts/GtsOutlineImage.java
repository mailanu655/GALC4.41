package com.honda.galc.entity.gts;

import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * 
 * <h3>GtsOutlineImage Class description</h3>
 * <p> GtsOutlineImage description </p>
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
 * Nov 16, 2014
 *
 *
 */
@Entity
@Table(name="GTS_OUTLINE_IMAGE_TBX")
public class GtsOutlineImage extends AuditEntry {
	@Id
	@Column(name="IMAGE_ID")
	private int imageId;

	@Column(name="OUTLINE_IMAGE_DESCRIPTION")
	private String outlineImageDescription;

	@Lob
	private byte[] image;

	private static final long serialVersionUID = 1L;

	public GtsOutlineImage() {
		super();
	}

	public Integer getId() {
		return this.imageId;
	}
	
	public int getImageId() {
		return this.imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getOutlineImageDescription() {
		return StringUtils.trim(this.outlineImageDescription);
	}

	public void setOutlineImageDescription(String outlineImageDescription) {
		this.outlineImageDescription = outlineImageDescription;
	}

	public byte[] getImage() {
		return this.image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	
	public ImageIcon getImageIcon(){
        try{
            return new ImageIcon(ImageIO.read(new ByteArrayInputStream(image)));
        }catch(Exception e){
        }
        return new ImageIcon();
    }

	
	public String toString() {
		return toString(getId(),getOutlineImageDescription());
	}

}
