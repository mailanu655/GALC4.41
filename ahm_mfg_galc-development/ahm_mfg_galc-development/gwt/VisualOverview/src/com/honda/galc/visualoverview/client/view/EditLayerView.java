package com.honda.galc.visualoverview.client.view;


import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.honda.galc.visualoverview.client.presenter.EditLayerPresenter;
import com.honda.galc.visualoverview.client.widgets.HasValueListBox;
import com.honda.galc.visualoverview.client.widgets.InputPanel;
import com.honda.galc.visualoverview.client.widgets.StyledButton;
import com.honda.galc.visualoverview.shared.Layer;


public class EditLayerView extends DialogBox implements EditLayerPresenter.Display {

	private final VerticalPanel vPanel = new VerticalPanel();
	private InputPanel layerIdPanel;
	private InputPanel layerTypePanel;
	private InputPanel layerDaoPanel;
	private InputPanel defaultZoomPanel;
	private InputPanel boundsLeftPanel;
	private InputPanel boundsRightPanel;
	private InputPanel boundsUpperPanel;
	private InputPanel boundsLowerPanel;
	private HorizontalPanel buttonPanel;
	private StyledButton saveButton;
	private StyledButton editButton;
	private StyledButton cancelButton;
	private final TextBox layerIdTextBox = new TextBox();
	private final TextBox layerTypeTextBox = new TextBox();
	private final TextBox layerDaoTextBox = new TextBox();
	private final TextBox defaultZoomTextBox = new TextBox();
	private final TextBox boundsLeftTextBox = new TextBox();
	private final TextBox boundsRightTextBox = new TextBox();
	private final TextBox boundsUpperTextBox = new TextBox();
	private final TextBox boundsLowerTextBox = new TextBox();
	private final HasValueListBox layerIdListBox = new HasValueListBox();
	private List<Layer> layerList = new ArrayList<Layer>();
	
	
	public EditLayerView()
	{
		setGlassEnabled(true);
		
		buildPanel();
		
		add(vPanel);
		center();
		show();

	}
	
	private void buildPanel()
	{
		layerIdListBox.setVisible(true);
		layerIdTextBox.setVisible(false);
		layerIdListBox.setWidth("100%");
		layerIdTextBox.getElement().getStyle().setFontSize(200, Unit.PCT);
		layerIdPanel = new InputPanel("Layer Id: ", layerIdListBox);
		layerIdPanel.add(layerIdTextBox);
		
		layerTypePanel = new InputPanel("Layer Type: ", layerTypeTextBox);
		layerDaoPanel = new InputPanel("Layer DAO: ", layerDaoTextBox);
		defaultZoomPanel = new InputPanel("Default Zoom: ", defaultZoomTextBox);
		boundsLeftPanel = new InputPanel("Left Boundary: ", boundsLeftTextBox);
		boundsRightPanel = new InputPanel("Right Boundary: ", boundsRightTextBox);
		boundsUpperPanel = new InputPanel("Upper Boundary: ", boundsUpperTextBox);
		boundsLowerPanel = new InputPanel("Lower Boundary: ", boundsLowerTextBox);
		
		saveButton = new StyledButton("Save");
		editButton = new StyledButton("Edit");
		cancelButton = new StyledButton("Cancel");
		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();
				
			}
			
		});
		editButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();
				
			}
			
		});
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();
				
			}
			
		});
		
		layerIdListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				setData(layerIdListBox.getSelectedIndex());
				if(layerList.get(layerIdListBox.getSelectedIndex()).getLayerId().equals("NEW"))
				{
					layerIdListBox.setVisible(false);
					layerIdTextBox.setVisible(true);
					layerIdTextBox.setFocus(true);
				}
			}
			
		});
		buttonPanel = new HorizontalPanel();
		buttonPanel.setWidth("100%");
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		buttonPanel.add(saveButton);
		buttonPanel.add(editButton);
		buttonPanel.add(cancelButton);
		
		vPanel.add(layerIdPanel);
		vPanel.add(layerTypePanel);
		vPanel.add(layerDaoPanel);
		vPanel.add(defaultZoomPanel);
		vPanel.add(boundsLeftPanel);
		vPanel.add(boundsRightPanel);
		vPanel.add(boundsUpperPanel);
		vPanel.add(boundsLowerPanel);
		vPanel.add(buttonPanel);
	}
	
	@Override
	public HasClickHandlers getSaveButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getCancelButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasValue<String> getLayerId() {
		return layerIdListBox;
	}

	@Override
	public HasValue<String> getLayerType() {
		return layerTypeTextBox;
	}

	@Override
	public HasValue<String> getLayerDao() {
		return layerDaoTextBox;
	}

	@Override
	public HasValue<String> getDefaultZoom() {
		return defaultZoomTextBox;
	}

	@Override
	public HasValue<String> getBoundsLeft() {
		return boundsLeftTextBox;
	}

	@Override
	public HasValue<String> getBoundsRight() {
		return boundsRightTextBox;
	}

	@Override
	public HasValue<String> getBoundsUpper() {
		return boundsUpperTextBox;
	}

	@Override
	public HasValue<String> getBoundsLower() {
		return boundsLowerTextBox;
	}

	@Override
	public void setLayers(List<Layer> layers) {
		clearData();
		layerList.addAll(layers);
		int index = 0;
		for(Layer layer : layers)
		{
			layerIdListBox.insertItem(layer.getLayerId(), index);
			index++;
		}
		
		setData(0);
		if(layerList.size() > 0)
		{
			layerIdListBox.setSelectedIndex(1);
			setData(1);
		}
		
	}
	
	public void setData(int index)
	{
		layerTypeTextBox.setText(layerList.get(index).getLayerType());
		layerDaoTextBox.setText(layerList.get(index).getLayerDao());
		defaultZoomTextBox.setText(Integer.toString(layerList.get(index).getDefaultZoom()));
		boundsLeftTextBox.setText(Double.toString(layerList.get(index).getBoundaryLeft()));
		boundsRightTextBox.setText(Double.toString(layerList.get(index).getBoundaryRight()));
		boundsUpperTextBox.setText(Double.toString(layerList.get(index).getBoundaryUpper()));
		boundsLowerTextBox.setText(Double.toString(layerList.get(index).getBoundaryLower()));
	}
	
	public void clearData()
	{
		layerList.clear();
		layerIdListBox.clear();
		layerIdTextBox.setText("");
		layerTypeTextBox.setText("");
		layerDaoTextBox.setText("");
		defaultZoomTextBox.setText("");
		boundsLeftTextBox.setText("");
		boundsRightTextBox.setText("");
		boundsUpperTextBox.setText("");
		boundsLowerTextBox.setText("");
	}

	@Override
	public HasClickHandlers getSaveLayerButton() {
		return saveButton;
	}

	@Override
	public HasValue<String> getNewLayerId() {
		return layerIdTextBox;
	}

	@Override
	public HasClickHandlers getEditLayerButton() {
		return editButton;
	}


}
