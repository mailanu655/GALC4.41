package com.honda.galc.visualoverview.client.view;


import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.honda.galc.visualoverview.client.presenter.EditFeaturePresenter;
import com.honda.galc.visualoverview.client.widgets.HasValueListBox;
import com.honda.galc.visualoverview.client.widgets.InputPanel;
import com.honda.galc.visualoverview.client.widgets.StyledButton;
import com.honda.galc.visualoverview.shared.Feature;
import com.honda.galc.visualoverview.shared.Layer;


public class EditFeatureView extends DialogBox implements EditFeaturePresenter.Display {

	private final VerticalPanel vPanel = new VerticalPanel();
	private InputPanel layerIdPanel;
	private InputPanel featureIdPanel;
	private InputPanel featureTypePanel;
	private InputPanel referenceIdPanel;
	private InputPanel referenceTypePanel;
	private HorizontalPanel buttonPanel;
	private StyledButton saveButton;
	private StyledButton cancelButton;
	private Label layerIdLabel;
	private final TextBox featureIdTextBox = new TextBox();
	private final TextBox referenceIdTextBox = new TextBox();
	private final TextBox referenceTypeTextBox = new TextBox();
	private final HasValueListBox layerIdListBox = new HasValueListBox();
	private final HasValueListBox featureIdListBox = new HasValueListBox();
	private final HasValueListBox featureTypeListBox = new HasValueListBox();
	private List<Layer> layerList = new ArrayList<Layer>();
	private List<Feature> featureList = new ArrayList<Feature>();
	
	
	public EditFeatureView()
	{
		setGlassEnabled(true);
		buildPanel();
		setFeatureTypes();
		
		add(vPanel);
		center();
		show();
	}
	
	private void buildPanel()
	{
		featureIdListBox.setVisible(true);
		featureIdTextBox.setVisible(false);
		featureIdListBox.setWidth("100%");
		featureIdTextBox.getElement().getStyle().setFontSize(200, Unit.PCT);
		featureIdPanel = new InputPanel("Feature Id: ", featureIdListBox);
		featureIdPanel.add(featureIdTextBox);
		
		layerIdLabel = new Label();
		layerIdLabel.getElement().getStyle().setFontSize(200, Unit.PCT);
		layerIdPanel = new InputPanel("Layer Id: ", layerIdLabel);		
		featureTypePanel = new InputPanel("Feature Type: ", featureTypeListBox);
		referenceIdPanel = new InputPanel("Reference Id: ", referenceIdTextBox);
		referenceTypePanel = new InputPanel("Reference Type: ", referenceTypeTextBox);
		//enableHistoryPanel = new InputPanel("Enable History: ", enableHistorySwitch);
		
		saveButton = new StyledButton("Add/Save");
		cancelButton = new StyledButton("Cancel");
		saveButton.addClickHandler(new ClickHandler() {

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
		
		featureIdListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				setData(featureIdListBox.getSelectedIndex());
				if(featureList.get(featureIdListBox.getSelectedIndex()).getFeatureId().equals("NEW"))
				{
					featureIdListBox.setVisible(false);
					featureIdTextBox.setVisible(true);
					featureIdTextBox.setFocus(true);
					saveButton.setTitle("Add");
				}
			}
			
		});
		buttonPanel = new HorizontalPanel();
		buttonPanel.setWidth("100%");
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);
		
		vPanel.add(layerIdPanel);
		vPanel.add(featureIdPanel);
		vPanel.add(featureTypePanel);
		vPanel.add(referenceIdPanel);
		vPanel.add(referenceTypePanel);
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

	/*@Override
	public void setLayers(List<Layer> layers) {
		clearData();
		layerList.addAll(layers);
		int index = 0;
		for(Layer layer : layers)
		{
			layerIdListBox.insertItem(layer.getLayerId(), index);
			index++;
		}
				
	}*/
	
	@Override
	public void setFeatures(List<Feature> features) {
		featureList.clear();
		featureIdListBox.clear();
		featureList.addAll(features);
		int index = 0;
		for(Feature feature : features)
		{
			featureIdListBox.insertItem(feature.getFeatureId(), index);
			index++;
		}
		
		setData(0);
		if(featureIdListBox.getItemCount() > 1)
		{
			featureIdListBox.setSelectedIndex(1);
			setData(1);
		}
		else
		{
			featureIdListBox.setVisible(false);
			featureIdTextBox.setVisible(true);
			featureIdTextBox.setFocus(true);
			saveButton.setTitle("Add");
		}

	}
	
	public void setFeatureTypes()
	{
		featureTypeListBox.clear();
		featureTypeListBox.addItem("Point");
		featureTypeListBox.addItem("Line");
		featureTypeListBox.addItem("Area");
		featureTypeListBox.addItem("Label");
		
	}
	
	public void setData(int index)
	{
		for(int i = 0; i < featureTypeListBox.getItemCount(); i++)
		{
			if(featureTypeListBox.getItemText(i).toUpperCase() == featureList.get(index).getFeatureType())
				featureTypeListBox.setSelectedIndex(i);
		}
		referenceIdTextBox.setText(featureList.get(index).getReferenceId());
		referenceTypeTextBox.setText(featureList.get(index).getReferenceType());
	}
	
	public void clearData()
	{
		layerList.clear();
		featureList.clear();
		layerIdListBox.clear();
		featureIdListBox.clear();
		featureIdTextBox.setText("");
		referenceIdTextBox.setText("");
		referenceTypeTextBox.setText("");
	}

	@Override
	public HasClickHandlers getSaveLayerButton() {
		return saveButton;
	}

	@Override
	public Label getLayerId() {
		return layerIdLabel;
	}

	@Override
	public HasChangeHandlers getLayerIdListBox() {
		return layerIdListBox;
	}

	@Override
	public HasValue<String> getFeatureId() {
		return featureIdListBox;
	}

	@Override
	public HasValue<String> getNewFeatureId() {
		return featureIdTextBox;
	}

	@Override
	public HasValue<String> getFeatureType() {
		return featureTypeListBox;
	}

	@Override
	public HasValue<String> getReferenceId() {
		return referenceIdTextBox;
	}

	@Override
	public HasValue<String> getReferenceType() {
		return referenceTypeTextBox;
	}

	@Override
	public ListBox getLayerListBox() {
		return layerIdListBox;
	}






}
