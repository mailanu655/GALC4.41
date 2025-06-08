package com.honda.galc.client.linesidemonitor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.product.BaseProduct;

/**
 * 
 * <h3>LineSideMonitorData Class description</h3>
 * <p> LineSideMonitorData description </p>
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
public class LineSideMonitorData {

	List<PrintAttributeFormat> attributeFormats = new ArrayList<PrintAttributeFormat>();
	List<Object> values = new ArrayList<Object>();

	private BaseProduct product;

	boolean isChecked = false;

	private Color backgroundColor;
	private Color foregroundColor;
	private Color specialBackgroundColor;
	private Color specialForegroundColor;
	private Color highlightRowBackgroundColor;
	private Color highlightRowForegroundColor;

	public LineSideMonitorData() {

	}

	public LineSideMonitorData(List<PrintAttributeFormat> attributeFormats) {
		this.attributeFormats = attributeFormats;
	}

	public List<PrintAttributeFormat> getAttributeFormats() {
		return attributeFormats;
	}

	public void setAttributeFormats(List<PrintAttributeFormat> attributeFormats) {
		if(attributeFormats == null) return;
		this.attributeFormats = attributeFormats;
	}

	public Object getValue(int position) {

		if(values == null || values.size() <= position) return null;

		return values.get(position);

	}

	public Object getValue(String attribute) {
		if (values == null) return null;
		int index = indexOf(attribute);
		if (index > -1 && index < values.size()) return values.get(index);
		return null;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public Color getSpecialBackgroundColor() {
		return specialBackgroundColor;
	}

	public void setSpecialBackgroundColor(Color specialBackgroundColor) {
		this.specialBackgroundColor = specialBackgroundColor;
	}

	public Color getSpecialForegroundColor() {
		return specialForegroundColor;
	}

	public void setSpecialForegroundColor(Color specialForegroundColor) {
		this.specialForegroundColor = specialForegroundColor;
	}

	public Color getHighlightRowBackgroundColor() {
		return highlightRowBackgroundColor;
	}

	public void setHighlightRowBackgroundColor(Color highlightBackgroundColor) {
		this.highlightRowBackgroundColor = highlightBackgroundColor;
	}

	public Color getHighlightRowForegroundColor() {
		return highlightRowForegroundColor;
	}

	public void setHighlightRowForegroundColor(Color highlightForegroundColor) {
		this.highlightRowForegroundColor = highlightForegroundColor;
	}

	public Color getDominantBackgroundColor() {
		if (this.highlightRowBackgroundColor != null) {
			return this.highlightRowBackgroundColor;
		}
		if (this.specialBackgroundColor != null) {
			return this.specialBackgroundColor;
		}
		return this.backgroundColor;
	}

	public Color getDominantForegroundColor() {
		if (this.highlightRowForegroundColor != null) {
			return this.highlightRowForegroundColor;
		}
		if (this.specialForegroundColor != null) {
			return this.specialForegroundColor;
		}
		return this.foregroundColor;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = new ArrayList<Object>(values);
	}

	public BaseProduct getProduct() {
		return product;
	}

	public void setProduct(BaseProduct product) {
		this.product = product;
	}

	private int indexOf(String attribute) {
		if (attribute == null || attributeFormats == null) return -1;
		for (int i = 0; i < attributeFormats.size(); i++) {
			if (attribute.equals(attributeFormats.get(i).getAttribute())) {
				return i;
			}
		}
		return -1;
	}



}
