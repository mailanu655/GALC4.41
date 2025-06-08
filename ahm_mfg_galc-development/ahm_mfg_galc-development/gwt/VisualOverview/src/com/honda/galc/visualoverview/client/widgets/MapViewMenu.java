package com.honda.galc.visualoverview.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class MapViewMenu extends HorizontalPanel {

	private Image configImage = null;
	private Image saveImage = null;
	private Image addImage = null;
	private Image moveImage = null;
	private Image pointsImage = null;
	private Image resizeImage = null;
	private Image rotateImage = null;
	private Image deleteImage = null;
	private static final String IMAGE_SIZE = "3em";
	
	public MapViewMenu()
	{
		super();
		try
		{
			setWidth("100%");
			HorizontalPanel leftPanel = new HorizontalPanel();
			leftPanel.setStylePrimaryName("paddedHorizontalPanel");
			setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			
			saveImage = new Image(GWT.getModuleBaseURL() + "save.png");
			saveImage.setSize(IMAGE_SIZE, IMAGE_SIZE);
			saveImage.setVisible(false);
			
			addImage = new Image(GWT.getModuleBaseURL() + "add.png");
			addImage.setSize(IMAGE_SIZE, IMAGE_SIZE);
			addImage.setVisible(false);
			
			moveImage = new Image(GWT.getModuleBaseURL() + "move.png");
			moveImage.setSize(IMAGE_SIZE, IMAGE_SIZE);
			moveImage.setVisible(false);
			
			pointsImage = new Image(GWT.getModuleBaseURL() + "points.png");
			pointsImage.setSize(IMAGE_SIZE, IMAGE_SIZE);
			pointsImage.setVisible(false);
			
			resizeImage = new Image(GWT.getModuleBaseURL() + "resize.png");
			resizeImage.setSize(IMAGE_SIZE, IMAGE_SIZE);
			resizeImage.setVisible(false);
			
			rotateImage = new Image(GWT.getModuleBaseURL() + "rotate.png");
			rotateImage.setSize(IMAGE_SIZE, IMAGE_SIZE);
			rotateImage.setVisible(false);
			
			deleteImage = new Image(GWT.getModuleBaseURL() + "delete.png");
			deleteImage.setSize(IMAGE_SIZE, IMAGE_SIZE);
			deleteImage.setVisible(false);

			leftPanel.add(addImage);
			leftPanel.add(moveImage);
			leftPanel.add(pointsImage);
			leftPanel.add(resizeImage);
			leftPanel.add(rotateImage);
			leftPanel.add(deleteImage);
			leftPanel.add(saveImage);

			add(leftPanel);
			
			setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			configImage = new Image(GWT.getModuleBaseURL() + "Gear.png");
			configImage.setSize(IMAGE_SIZE, IMAGE_SIZE);
			add(configImage);
		}
		catch(Exception ex)
		{
			Window.alert(ex.getMessage() + "Menu");
		}

	}
	
	public Image getConfigButton()
	{
		return this.configImage;
	}
	
	public Image getSaveButton()
	{
		return this.saveImage;
	}
	
	public Image getAddButton()
	{
		return this.addImage;
	}
	
	public Image getMoveButton()
	{
		return this.moveImage;
	}
	
	public Image getPointsButton()
	{
		return this.pointsImage;
	}
	
	public Image getResizeButton()
	{
		return this.resizeImage;
	}
	
	public Image getRotateButton()
	{
		return this.rotateImage;
	}
	
	public Image getDeleteImage()
	{
		return this.deleteImage;
	}
}
