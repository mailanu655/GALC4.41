package com.honda.galc.entity.gts;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.GtsLineStyle;
import com.honda.galc.entity.enumtype.GtsOrientation;
import com.honda.galc.entity.enumtype.GtsShapeType;

/**
 * 
 * 
 * <h3>GtsShape Class description</h3>
 * <p> GtsShape description </p>
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
@Table(name="GTS_SHAPE_TBX")
public class GtsShape extends AuditEntry {
	@EmbeddedId
	private GtsShapeId id;

	@Column(name="SHAPE_TYPE")
	private int shapeType = GtsShapeType.ELLIPSE.getId();

	@Column(name="SHAPE_STYLE")
	private int shapeStyle = GtsOrientation.NORTH.getId();

	@Column(name="LINE_WIDTH")
	private double lineWidth =1.0;

	@Column(name="LINE_STYLE")
	private int lineStyle = GtsLineStyle.SOLID.getId();

	@Column(name="LINE_COLOR")
	private String lineColor =  Integer.toHexString(Color.black.getRGB() & 0xffffff);

	@Column(name="FILL_COLOR")
	private String fillColor =  Integer.toHexString(Color.white.getRGB() & 0xffffff);

	private int x;

	private int y;

	@Column(name="X_EXTENT")
	private int xExtent = 50;

	@Column(name="Y_EXTENT")
	private int yExtent = 50;

	private static final long serialVersionUID = 1L;

	public GtsShape(GtsShape shape) {
		this.id = new GtsShapeId(shape.getId());
		this.shapeType = shape.shapeType;
		this.shapeStyle = shape.shapeStyle;
		this.lineWidth = shape.lineWidth;
		this.lineStyle = shape.lineStyle;
		this.lineColor = shape.lineColor;
		this.fillColor = shape.fillColor;
		this.x = shape.x;
		this.y = shape.y;
		this.xExtent = shape.xExtent;
		this.yExtent = shape.yExtent;
		this.setCreateTimestamp(shape.getCreateTimestamp());
		this.setUpdateTimestamp(shape.getUpdateTimestamp());
	}
	
	public GtsShape() {
		super();
	}

	public GtsShapeId getId() {
		return this.id;
	}

	public void setId(GtsShapeId id) {
		this.id = id;
	}

	public int getShapeTypeValue() {
		return this.shapeType;
	}
	
	public GtsShapeType getShapeType() {
		return GtsShapeType.getType(shapeType);
	}

	public void setShapeTypeValue(int shapeType) {
		this.shapeType = shapeType;
	}

	public int getShapeStyle() {
		return this.shapeStyle;
	}

	public void setShapeStyle(int shapeStyle) {
		this.shapeStyle = shapeStyle;
	}
	
	public GtsOrientation getOrientation() {
		return GtsOrientation.getType(getShapeStyle());
	}

	public double getLineWidth() {
		return this.lineWidth;
	}

	public void setLineWidth(double lineWidth) {
		this.lineWidth = lineWidth;
	}

	public int getLineStyleVlaue() {
		return this.lineStyle;
	}
	
	public GtsLineStyle getLineStyle() {
		return GtsLineStyle.getType(lineStyle);
	}

	public void setLineStyleValue(int lineStyle) {
		this.lineStyle = lineStyle;
	}

	public String getLineColorValue() {
		return StringUtils.trim(this.lineColor);
	}

	public Color getLineColor() {
		if(StringUtils.isEmpty(getLineColorValue())) return null;
		return new Color(Integer.parseInt(getLineColorValue(), 16) | 0xff000000);
	}

	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}
	
	public void setLineColor(Color color) {
		this.lineColor = color == null ? null :Integer.toHexString(color.getRGB() & 0xffffff);
	}

	public String getFillColorValue() {
		return StringUtils.trim(this.fillColor);
	}
	
	public Color getFillColor() {
		if(StringUtils.isEmpty(getFillColorValue())) return null;
		return new Color(Integer.parseInt(getFillColorValue(), 16) | 0xff000000);
	}


	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}
	
	public void setFillColor(Color color) {
		this.fillColor = color == null ? "" :Integer.toHexString(color.getRGB() & 0xffffff);
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getXExtent() {
		return this.xExtent;
	}
	
	public int getWidth() {
		return this.xExtent;
	}

	public void setXExtent(int xExtent) {
		this.xExtent = xExtent;
	}

	public int getYExtent() {
		return this.yExtent;
	}
	
	public int getHeight() {
		return this.yExtent;
	}

	public void setYExtent(int yExtent) {
		this.yExtent = yExtent;
	}
	
	public Rectangle2D.Double getBounds(){
	    return new Rectangle2D.Double(x, y, xExtent, yExtent);
	}
	   
	public void setBounds(Rectangle2D.Double bound){
        this.x = (int)bound.x;
        this.y = (int)bound.y;
        this.xExtent = (int)bound.width;
        this.yExtent = (int)bound.height;
    }
	
	public String toString() {
		return toString(getId().getTrackingArea(),getId().getShapeId(),getShapeType(),getShapeStyle(),
				getLineStyle(),getLineColor(),getX(),getY(),getXExtent(),getYExtent());
	}
	
	public GtsShape copy() {
		return new GtsShape(this);
	}
}
