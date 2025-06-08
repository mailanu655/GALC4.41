package com.honda.galc.vios.shared;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.BaseWidget;

abstract public class WidgetBase implements EntryPoint {
	
	private int widgetHeight= 120;
	private  int fontSize = 100;
	private int widgetWidth = 400;
	private String backgroundColor;
	private int numberofRecords = 4;
	private String productId;
	
	
	public int getWidgetHeight() {
		return widgetHeight;
	}
	public void setWidgetHeight(int widgetHeight) {
		this.widgetHeight = widgetHeight;
	}
	public int getWidgetWidth() {
		return widgetWidth;
	}
	public void setWidgetWidth(int widgetWidth) {
		this.widgetWidth = widgetWidth;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	
	
    public WidgetBase() {
    	setProperties();
    }
	@Override
	public void onModuleLoad() {

	}

	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	private void setProperties() {
		
		String strHeight = Window.Location.getParameter("height");
		String strWidth = Window.Location.getParameter("width");
		String strFontSize = Window.Location.getParameter("font-size");
		String strRecords = Window.Location.getParameter("number-of-records");
		backgroundColor = Window.Location.getParameter("background-color");
		productId =  Window.Location.getParameter("product-id");
		
		if (strHeight != null)
			widgetHeight = Integer.parseInt(strHeight);

		if (strWidth != null)
			widgetWidth =  Integer.parseInt(strWidth);
		
		if (strFontSize != null)
			fontSize =  Integer.parseInt(strFontSize);
		
		if (strRecords != null)
			numberofRecords =  Integer.parseInt(strRecords);


	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public int getNumberofRecords() {
		return numberofRecords;
	}
	public void setNumberofRecords(int numberofRecords) {
		this.numberofRecords = numberofRecords;
	}

}
