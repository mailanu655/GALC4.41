package com.honda.galc.entity.gts;

import java.awt.Color;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * 
 * <h3>GtsColorMap Class description</h3>
 * <p> GtsColorMap description </p>
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
 * Nov 14, 2014
 *
 *
 */
@Entity
@Table(name="GTS_COLOR_MAP_TBX")
public class GtsColorMap extends AuditEntry {
	@EmbeddedId
	private GtsColorMapId id;

	@Column(name="COLOR_CODE")
	private String colorCode;

	private String color;

	private static final long serialVersionUID = 1L;

	public GtsColorMap() {
		super();
	}

	public GtsColorMap(String trackingArea) {
		id = new GtsColorMapId();
		id.setTrackingArea(trackingArea);
	}
	
	public GtsColorMapId getId() {
		return this.id;
	}

	public void setId(GtsColorMapId id) {
		this.id = id;
	}

	public String getColorCode() {
		return StringUtils.trim(this.colorCode);
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getColorString() {
		return StringUtils.trim(this.color);
	}
	
	public Color getColor() {
		return StringUtils.isEmpty(getColorString()) ? 
				null : new Color(Integer.parseInt(getColorString(), 16) | 0xff000000);
	}

	public void setColorString(String color) {
		this.color = color;
	}
	
	public void setColor(Color color) {
		this.color = Integer.toHexString(color.getRGB() & 0xffffff);
	}
	
	public String toString() {
		return toString(getId().getTrackingArea(),getColorString(),getColorCode());
	}
	
}
