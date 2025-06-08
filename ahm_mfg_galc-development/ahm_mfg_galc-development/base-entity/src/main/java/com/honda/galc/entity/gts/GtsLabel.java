package com.honda.galc.entity.gts;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.StringTokenizer;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.GtsBorderType;
import com.honda.galc.entity.enumtype.GtsFontStyle;

/**
 * 
 * 
 * <h3>GtsLabel Class description</h3>
 * <p> GtsLabel description </p>
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
@Table(name="GTS_LABEL_TBX")
public class GtsLabel extends AuditEntry {
	@EmbeddedId
	private GtsLabelId id;

	@Column(name="LABEL_TEXT")
	private String labelText = "AAA";

	@Column(name="LABEL_FONT")
	private String labelFont;

	@Column(name="BORDER_TYPE")
	private int borderType = GtsBorderType.RECTANGLE.getId();

	@Column(name="BORDER_COLOR")
	private String borderColor = Integer.toHexString(Color.black.getRGB() & 0xffffff);

	@Column(name="TEXT_COLOR")
	private String textColor = Integer.toHexString(Color.black.getRGB() & 0xffffff);

	@Column(name="BACK_COLOR")
	private String backColor = Integer.toHexString(Color.green.getRGB() & 0xffffff);

	private int x;

	private int y;

	@Column(name="X_EXTENT")
	private int xExtent = 30;

	@Column(name="Y_EXTENT")
	private int yExtent = 20;

	private static final long serialVersionUID = 1L;

	public GtsLabel(GtsLabel label) {
		this.id = new GtsLabelId(label.getId());
		this.labelText = label.labelText;
		this.labelFont = label.labelFont;
		this.borderType = label.borderType;
		this.borderColor = label.borderColor;
		this.textColor = label.textColor;
		this.backColor = label.backColor;
		this.x = label.x;
		this.y = label.y;
		this.xExtent = label.xExtent;
		this.yExtent = label.yExtent;
		this.setCreateTimestamp(label.getCreateTimestamp());
		this.setUpdateTimestamp(label.getUpdateTimestamp());
	}
	
	public GtsLabel() {
		super();
	}
	
	public GtsLabel(String text) {
		this.labelText = text;
	}

	public GtsLabelId getId() {
		return this.id;
	}

	public void setId(GtsLabelId id) {
		this.id = id;
	}

	public String getLabelText() {
		return StringUtils.trim(this.labelText);
	}

	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	public String getLabelFont() {
		return this.labelFont;
	}
	
	public Font getFont(){
        if(labelFont == null) return null;
        StringTokenizer token = new StringTokenizer(labelFont,"-");
        String fontName = "Arial";
        int fontStyle = 0;
        int fontSize = 12;
        if(token.hasMoreTokens()) fontName = token.nextToken();
        if(token.hasMoreTokens()){
            GtsFontStyle fs = GtsFontStyle.valueOf(token.nextToken().toUpperCase());
            if(fs != null) fontStyle = fs.ordinal();
        }
        if(token.hasMoreTokens()){
            try{    
                fontSize = Integer.parseInt(token.nextToken());
            }catch(Exception e){
            }
        }
        return new Font(fontName,fontStyle,fontSize);
    }

	public void setLabelFont(String labelFont) {
		this.labelFont = labelFont;
	}
	
	public void setFont(Font font){
        if (font == null) return;
        this.labelFont = font.getName()+"-"+GtsFontStyle.getType(font.getStyle())+"-"+Integer.toString(font.getSize());
    }

	public int getBorderTypeValue() {
		return this.borderType;
	}
	
	public GtsBorderType getBorderType() {
		return GtsBorderType.getType(borderType);
	}
	
	public java.awt.Shape getBorder(double grow){
        switch(getBorderType()){
            case RECTANGLE:
                Rectangle2D.Double rect = new Rectangle2D.Double(x,y,xExtent,yExtent);
                rect.x -= grow;
                rect.y -= grow;
                rect.width += grow * 2d;
                rect.height += grow * 2d;
                return rect;
            case ROUND_RECTANGLE:
                RoundRectangle2D.Double roundRect = new RoundRectangle2D.Double(x,y,xExtent,yExtent,15.0,15.0);
                roundRect.x -= grow;
                roundRect.y -= grow;
                roundRect.width += grow * 2d;
                roundRect.height += grow * 2d;
                return roundRect;
            case ELLIPSE:
                Ellipse2D.Double ellipse =  new Ellipse2D.Double(x, y, xExtent,yExtent);
                ellipse.x -= grow;
                ellipse.y -= grow;
                ellipse.width += grow * 2d;
                ellipse.height += grow * 2d;
                return ellipse;
        }
        return null;
        
    }

	public void setBorderType(int borderType) {
		this.borderType = borderType;
	}
	
	public void setBorderType(GtsBorderType borderType) {
		this.borderType = borderType.getId();
	}

	public String getBorderColorValue() {
		return StringUtils.trim(this.borderColor);
	}
	
	public Color getBorderColor() {
		if(StringUtils.isEmpty(getBorderColorValue())) return null;
		return new Color(Integer.parseInt(getBorderColorValue(), 16) | 0xff000000);
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public void setBorderColor(Color color) {
		this.borderColor = color == null ? null :Integer.toHexString(color.getRGB() & 0xffffff);
	}
	
	public String getTextColorValue() {
		return StringUtils.trim(this.textColor);
	}
	
	public Color getTextColor() {
		if(StringUtils.isEmpty(getTextColorValue())) return null;
		return new Color(Integer.parseInt(getTextColorValue(), 16) | 0xff000000);
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}
	
	public void setTextColor(Color color) {
		this.textColor = Integer.toHexString(color.getRGB() & 0xffffff);
	}

	public String getBackColorValue() {
		return StringUtils.trim(this.backColor);
	}
	
	public Color getBackColor() {
		if(StringUtils.isEmpty(backColor)) return null;
		return new Color(Integer.parseInt(getBackColorValue(), 16) | 0xff000000);
	}

	public void setBackColor(String backColor) {
		this.backColor = backColor;
	}
	
	public void setBackColor(Color color) {
		this.backColor = color == null ? null :Integer.toHexString(color.getRGB() & 0xffffff);
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

	public void setWidth(int xExtent) {
		this.xExtent = xExtent;
	}

	public int getWidth() {
		return this.xExtent;
	}
	
	public int getYExtent() {
		return this.yExtent = 50;
	}

	public void setHeight(int yExtent) {
		this.yExtent = yExtent;
	}
	
	public int getHeight() {
		return this.yExtent;
	}

	public String toString() {
		return toString(getId().getTrackingArea(),getId().getLabelId(),
				getLabelText(),getBackColor(),getBorderColor(),getBorderType());
	}
	
	public GtsLabel copy() {
		return new GtsLabel(this);
	}

}
