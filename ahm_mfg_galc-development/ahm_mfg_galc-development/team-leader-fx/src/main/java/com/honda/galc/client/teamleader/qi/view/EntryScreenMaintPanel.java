package com.honda.galc.client.teamleader.qi.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.EntryScreenMaintController;
import com.honda.galc.client.teamleader.qi.model.EntryScreenMaintenanceModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.QiEntryScreenDto;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class EntryScreenMaintPanel extends QiAbstractTabbedView<EntryScreenMaintenanceModel, EntryScreenMaintController> {

	private UpperCaseFieldBean entryScreenFilterTextField;
	private ObjectTablePane<QiEntryScreenDto> entryScreenTablePane;

	public EntryScreenMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}

	public void initView() {	
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double width = primaryScreenBounds.getWidth()/2;
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		LoggedLabel entryScreenFilterLabel = UiFactory.createLabel("entryScreenFilterLabel", "Entry Screen Filter");
		entryScreenFilterLabel.getStyleClass().add("display-label");
		HBox filterContainer = new HBox();		
		HBox mainFilterContainer = new HBox();
		entryScreenFilterTextField= createFilterTextField("filterTextField", 25, getController());
		HBox radioButtonContainer =createFilterRadioButtons(getController(), width);
		setFilterContainer(width, entryScreenFilterLabel, filterContainer);

		mainFilterContainer.getChildren().addAll(radioButtonContainer,filterContainer);
		mainFilterContainer.setAlignment(Pos.CENTER_LEFT);
		entryScreenTablePane = createEntryScreenTablePane();	
		entryScreenTablePane.setId("entryScreenTablePane");
		this.setTop(mainFilterContainer);
		this.setCenter(entryScreenTablePane);
	}

	private void setFilterContainer(double width, LoggedLabel entryScreenFilterLabel, HBox filterContainer) {
		filterContainer.getChildren().addAll(entryScreenFilterLabel, entryScreenFilterTextField);
		filterContainer.setSpacing(10);
		filterContainer.setPadding(new Insets(10));
		filterContainer.setAlignment(Pos.CENTER_RIGHT);
		filterContainer.setPrefWidth(width);
	}

	private ObjectTablePane<QiEntryScreenDto> createEntryScreenTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Product Type", "productType")
				.put("Entry Model", "entryModel")
				.put("Entry Plant", "plantName")
				.put("Entry Department", "divisionId")
				.put("Entry Screen", "entryScreen")
				.put("Is Used","IsUsedVersionData")
				.put("Entry Screen Description", "entryScreenDescription")
				.put("Entry Screen Type"+"\n"+"Image=1"+"\n"+"Text=0"+"\n", "screenType")
				.put("Status"+"\n"+"ACTIVE=1"+"\n"+"INACTIVE=0"+"\n", "status");
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] { 0.10, 0.10, 0.10, 0.10, 0.10,0.05, 0.20, 0.09, 0.092};
		ObjectTablePane<QiEntryScreenDto> panel = new ObjectTablePane<QiEntryScreenDto>(columnMappingList, columnWidth);
		panel.setConstrainedResize(false);
		return panel;
	}

	public void onTabSelected() {
		reload(StringUtils.trim(entryScreenFilterTextField.getText()));
	}

	@Override
	public void reload() {
		List<QiEntryScreenDto> dataList = getModel().findAllEntryScreensByFilter("", getSelectedRadioButtonValue());
		Collections.sort(dataList, new Comparator<QiEntryScreenDto>(){
			public int compare(QiEntryScreenDto entryScreenObjectOne, QiEntryScreenDto entryScreenObjectTwo) {
				return entryScreenObjectOne.getProductType().compareTo(entryScreenObjectTwo.getProductType());
			}
		});	
		entryScreenTablePane.setData(dataList);
	}

	public void reload(String filter) {
		List<QiEntryScreenDto> dataList = getModel().findAllEntryScreensByFilter(filter, getSelectedRadioButtonValue());
		Collections.sort(dataList, new Comparator<QiEntryScreenDto>(){
			public int compare(QiEntryScreenDto entryScreenObjectOne, QiEntryScreenDto entryScreenObjectTwo) {
				return entryScreenObjectOne.getProductType().compareTo(entryScreenObjectTwo.getProductType());
			}
		});
		entryScreenTablePane.setData(dataList);
		Logger.getLogger().check("Entry screen tablepane refreshed");
	}	

	public List<Short> getSelectedRadioButtonValue() {
		List<Short> statusList = new ArrayList<Short>();
		if(getAllRadioBtn().isSelected()) {
			statusList.add((short)1);
			statusList.add((short)0);
		} else {
			if(getActiveRadioBtn().isSelected())
				statusList.add((short)1);
			else
				statusList.add((short)0);
			statusList.add((short)2);
		}
		return statusList;
	}

	@Override
	public ViewId getViewId() {
		return null;
	}

	@Override
	public void start() {
	}

	public String getScreenName() {
		return "Entry Screen";
	}

	public ObjectTablePane<QiEntryScreenDto> getEntryScreenTablePane() {
		return entryScreenTablePane;
	}

	public String getFilterTextData() {
		return StringUtils.trimToEmpty(entryScreenFilterTextField.getText());
	}

	public UpperCaseFieldBean getEntryScreenFilterTextField() {
		return entryScreenFilterTextField;
	}

	public void setEntryScreenFilterTextField(UpperCaseFieldBean entryScreenFilterTextField) {
		this.entryScreenFilterTextField = entryScreenFilterTextField;
	}

	public void setEntryScreenTablePane(ObjectTablePane<QiEntryScreenDto> entryScreenTablePane) {
		this.entryScreenTablePane = entryScreenTablePane;
	}

}
