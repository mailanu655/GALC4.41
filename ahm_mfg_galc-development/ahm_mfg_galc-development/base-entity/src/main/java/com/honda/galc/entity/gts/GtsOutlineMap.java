package com.honda.galc.entity.gts;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * 
 * <h3>GtsOutlineMap Class description</h3>
 * <p> GtsOutlineMap description </p>
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
@Table(name="GTS_OUTLINE_MAP_TBX")
public class GtsOutlineMap extends AuditEntry {
	@EmbeddedId
	private GtsOutlineMapId id;

	@Column(name="MODEL_CODE")
	private String modelCode;

	@Lob
	private byte[] image;

	private static final long serialVersionUID = 1L;

	public GtsOutlineMap() {
		super();
	}
	
	public GtsOutlineMap(String trackingArea, int outlineImageId) {
		id = new GtsOutlineMapId(trackingArea,outlineImageId);
	}

	public GtsOutlineMapId getId() {
		return this.id;
	}

	public void setId(GtsOutlineMapId id) {
		this.id = id;
	}

	public String getModelCode() {
		return StringUtils.trim(this.modelCode);
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public byte[] getImageBytes() {
		return this.image;
	}
	
	public void setImageBytes(byte[] image) {
		this.image = image;
	}
	
	public BufferedImage getImage(){
        try{
            return ImageIO.read(new ByteArrayInputStream(image)); 
        }catch(Exception e){
        }
        return null;
    }
	
	public ImageIcon getImageIcon(){
        BufferedImage image = getImage();
        if(image == null) return new ImageIcon();
        else return new ImageIcon(image);
    }
	
	public String toString() {
		return toString(getId().getTrackingArea(),getId().getOutlineMapId(),getModelCode());
	}
}
