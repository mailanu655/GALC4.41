package com.honda.galc.visualoverview.client.view;


import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.honda.galc.visualoverview.client.presenter.EditViewPresenter;
import com.honda.galc.visualoverview.client.widgets.DisplayedLayersListGrid;
import com.honda.galc.visualoverview.client.widgets.HasValueListBox;
import com.honda.galc.visualoverview.client.widgets.InputPanel;
import com.honda.galc.visualoverview.client.widgets.StyledButton;
import com.honda.galc.visualoverview.client.widgets.ViewListGrid;
import com.honda.galc.visualoverview.shared.Layer;
import com.honda.galc.visualoverview.shared.View;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.ListGridRecord;


public class EditViewView extends DialogBox implements EditViewPresenter.Display {


	private final VerticalPanel vPanel = new VerticalPanel();
	private InputPanel viewIdPanel;
	private HorizontalPanel listBoxPanel;
	private HorizontalPanel buttonPanel;
	private VerticalPanel displayPanel;
	private VerticalPanel layerPanel;
	private TextBox viewIdTextBox = new TextBox();
	private Label displayedLabel = new Label("Displayed");
	private Label layerLabel = new Label("Layers   ");
	private DisplayedLayersListGrid displayedListBox;
	private ViewListGrid layerListBox;
	private StyledButton showButton;
	private StyledButton cancelButton;
	private final HasValueListBox viewIdListBox = new HasValueListBox();
	private List<View> viewList = new ArrayList<View>();
	private List<Layer> layerList = new ArrayList<Layer>();
	private List<View> displayList = new ArrayList<View>();
	
	
	public EditViewView()
	{
		setGlassEnabled(true);
		buildPanel();		
		add(vPanel);
		setPopupPosition((int)(Window.getClientWidth() * .25), (int)(Window.getClientHeight() * .1));
		show();
	}
	
	private void buildPanel()
	{

		viewIdTextBox.setVisible(false);
		viewIdTextBox.getElement().getStyle().setFontSize(200, Unit.PCT);
		vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		viewIdPanel = new InputPanel("View: ", viewIdListBox);
		viewIdPanel.add(viewIdTextBox);
		displayedListBox = new DisplayedLayersListGrid();
		layerListBox = new ViewListGrid();
		listBoxPanel = new HorizontalPanel();
		listBoxPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		displayPanel = new VerticalPanel();
		layerPanel = new VerticalPanel();
		displayPanel.setWidth("100%");
		layerPanel.setWidth("100%");
		displayedLabel.setWidth("10em");
		layerLabel.setWidth("10em");
		displayPanel.add(displayedLabel);
		displayPanel.add(displayedListBox);
		layerPanel.add(layerLabel);
		layerPanel.add(layerListBox);
		listBoxPanel.add(displayPanel);
		listBoxPanel.add(layerPanel);
		displayedLabel.getElement().getStyle().setFontSize(200, Unit.PCT);
		layerLabel.getElement().getStyle().setFontSize(200, Unit.PCT);
		displayedListBox.getElement().getStyle().setFontSize(200, Unit.PCT);
		layerListBox.getElement().getStyle().setFontSize(200, Unit.PCT);
		listBoxPanel.setWidth("100%");
		layerListBox.setWidth("25%");
		displayedListBox.setWidth("25%");
		layerListBox.setHeight((int)(Window.getClientHeight() * .35));
		displayedListBox.setHeight((int)(Window.getClientHeight() * .35));
		
		buttonPanel = new HorizontalPanel();
		buttonPanel.setWidth("100%");
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		showButton = new StyledButton("Show");
		cancelButton = new StyledButton("Cancel");
		showButton.addClickHandler(new ClickHandler() {

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
		
		viewIdListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				setData(viewIdListBox.getSelectedIndex());
				if(viewIdListBox.getItemText(viewIdListBox.getSelectedIndex()).equals("NEW"))
				{
					viewIdListBox.setVisible(false);
					viewIdTextBox.setVisible(true);
					viewIdTextBox.setFocus(true);
				}
			}
			
		});
		
		buttonPanel.add(showButton);
		buttonPanel.add(cancelButton);
		
		vPanel.add(viewIdPanel);
		vPanel.add(listBoxPanel);
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
	public ListBox getViewIdListBox() {
		return viewIdListBox;
	}

	@Override
	public HasValue<String> getViewId() {
		return viewIdListBox;
	}

	@Override
	public void setViews(List<View> views, String currentView) {
		viewList.clear();
		viewIdListBox.clear();
		viewIdTextBox.setVisible(false);
		viewIdListBox.setVisible(true);
		viewList.addAll(views);
		int index = 0;
		int selectedIndex = 0;
		String previousView = "";
		for(View view : views)
		{
			if(view.getViewId().equals(previousView))
				continue;
			previousView = view.getViewId();
			viewIdListBox.insertItem(view.getViewId(), index);
			if(view.getViewId().equals(currentView))
				selectedIndex = index;
			index++;
		}
		
		if(selectedIndex > 0)
			viewIdListBox.setSelectedIndex(selectedIndex);
		else if(views.size() > 1)
			viewIdListBox.setSelectedIndex(1);
		
	}
	
	private void setData(int index)
	{
		
	}

	@Override
	public void setLayers(List<Layer> layers) {
		layerList.clear();
		layerList.addAll(layers);
		int index = 0;
		ListGridRecord[] records = new ListGridRecord[layers.size()];
		for(Layer layer : layers)
		{
			ListGridRecord record = new ListGridRecord();
			record.setAttribute("layerId", layer.getLayerId().trim());
			records[index] = record;
			index++;
		}
		layerListBox.setData(records);
	}

	@Override
	public void setDisplayed(List<View> views) {
		displayList.clear();
		displayList.addAll(views);
		int index = 0;
		ListGridRecord[] records = new ListGridRecord[views.size()];
		for(View view : views)
		{
			ListGridRecord record = new ListGridRecord();
			record.setAttribute("layerId", view.getLayerId().trim());
			records[index] = record;
			index++;
		}
		displayedListBox.setData(records);
	}

	@Override
	public HasClickHandlers getShowButton() {
		return showButton;
	}

	@Override
	public List<String> getDisplayedLayers() {
		List<String> displayedLayers = new ArrayList<String>();
		for(Record record : displayedListBox.getDataAsRecordList().toArray())
		{
			displayedLayers.add(record.getAttribute("layerId"));
		}
		
		return displayedLayers;
	}

	@Override
	public DisplayedLayersListGrid getDisplayedLayersListGrid() {
		return displayedListBox;
	}

	@Override
	public HasValue<String> getNewViewId() {
		return viewIdTextBox;
	}


	

	





}
