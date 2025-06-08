package com.honda.galc.client.qi.defectentry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.enumtype.ObservableListChangeEventType;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.AbstractTileListViewBehaviour;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.ObservableListChangeEvent;
import com.honda.galc.client.ui.event.ProgressEvent;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiDefectEntryDto;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.entity.enumtype.QiDefectEntryScanType;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationEntryScreen;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
/**
 * <h3>DefectEntryByTextController Class description</h3>
 * <p> DefectEntryByTextController description </p>
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
 * @author L&T Infotech<br>
 * July 14, 2017
 *
 */
public class DefectEntryByTextController {
	
	private DefectEntryController parentController;
	
	public static final String DEFECT = "Defect";
	public static final String PART_2 = "Part 2";
	public static final String PART_1 = "Part 1";
	public static final String LOC    = "Loc";
	private static final String HYPHEN_SYMBOL = "-";
	
	public DefectEntryByTextController(DefectEntryController parentController) {
		super();
		this.parentController = parentController;
	}
	
	/**
	 * This method is used to Initialize Listeners
	 */
	public void initializeListeners() {
		setMenuTileListViewBehavior();
		addPart1ListViewListener();
		addLocListViewListener();
		addPart2ListViewListener();
		addDefectListViewListener();
		addPartDefectListViewListener();
	} 
	
	/**
	 * This method is used to set Menu ListView Behavior
	 */
	private void setMenuTileListViewBehavior() {
		getView().getMenuTileListView().setBehaviour(new AbstractTileListViewBehaviour<String>() {
			@Override
			public void setBehaviour(String item) {
				UpperCaseFieldBean field = new UpperCaseFieldBean("item");
				field.setText(item);
				field.setEditable(false);
				field.getStyleClass().add("tile-list-view-textfield");
				field.minWidthProperty().bind(getView().getMenuTileListView().widthProperty().multiply(0.33));
				field.maxWidthProperty().bind(getView().getMenuTileListView().widthProperty().multiply(0.33));
				getView().getMenuTileListView().setNode(field);
			}

			@Override
			public void addListener(String item) {
				addMenuListViewListener();
			}
		});
	}
	
	/**
	 * This method is used to add Menu ListView Listener
	 */
	private void addMenuListViewListener() {
		clearStatusAndResponsibleLevelMessage(DefectEntryController.RESPONSIBLE_LVL_MSG_ID);				
		if(null!=getView().getPartDefectListView().getSelectionModel())
			getView().getPartDefectListView().getSelectionModel().clearSelection();
		QiDefectEntryDto entryScreen = getView().getEntryScreenListView().getSelectedItem();
		if(entryScreen!=null && !entryScreen.isImage()) {
			String textEntryMenuName = StringUtils.trimToEmpty(getView().getMenuTileListView().getSelectedItem());
			clearListViewsElements();			
			if(StringUtils.isNotEmpty(textEntryMenuName)) {					
				initTextFilterComponents();
			}
		}
	}
	
	/**
	 * This method is used to add Part1 Table Pane Listener
	 */
	private void addPart1ListViewListener() {
		getView().getPart1ListView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
				if(!parentController.isListInitialized())  return;
				String part1Name  =  StringUtils.trimToEmpty(newValue);
				String loc        =  StringUtils.trimToEmpty(getView().getLocListView().getSelectionModel().getSelectedItem());
				String part2Name  =  StringUtils.trimToEmpty(getView().getPart2ListView().getSelectionModel().getSelectedItem());
				String defectName =  StringUtils.trimToEmpty(getView().getDefectListView().getSelectionModel().getSelectedItem());
				String defect1 = getView().parseDefect(defectName, false);
				String defect2 = getView().parseDefect(defectName, true);

				clearStatusAndResponsibleLevelMessage(DefectEntryController.RESPONSIBLE_LVL_MSG_ID);				
				String textEntryMenuName = StringUtils.trimToEmpty(getView().getMenuTileListView().getSelectedItem());
				if(!parentController.getSelectValue() && StringUtils.isNotEmpty(textEntryMenuName)){ 
					refreshTextFilterComponents(part1Name, loc, part2Name, defect1, defect2);
				}								
			}
		});
	}
	
	/**
	 * This method is used to add Loc1 Table Pane Listener
	 */
	private void addLocListViewListener() {
		getView().getLocListView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String oldValue, String newValue) {
				if(!parentController.isListInitialized())  return;
				String part1Name  = StringUtils.trimToEmpty(getView().getPart1ListView().getSelectionModel().getSelectedItem());
				String loc        = newValue;
				String part2Name  = StringUtils.trimToEmpty(getView().getPart2ListView().getSelectionModel().getSelectedItem());
				String defectName = StringUtils.trimToEmpty(getView().getDefectListView().getSelectionModel().getSelectedItem());
				String defect1 = getView().parseDefect(defectName, false);
				String defect2 = getView().parseDefect(defectName, true);

				clearStatusAndResponsibleLevelMessage(DefectEntryController.RESPONSIBLE_LVL_MSG_ID);				
				String textEntryMenuName = StringUtils.trimToEmpty(getView().getMenuTileListView().getSelectedItem());
				if(!parentController.getSelectValue() && StringUtils.isNotEmpty(textEntryMenuName)){ 
					refreshTextFilterComponents(part1Name, loc, part2Name, defect1, defect2);
				}				
			}
		});
	}

	/**
	 * This method is used to add Part2 Table Pane Listener
	 */
	private void addPart2ListViewListener() {
		getView().getPart2ListView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String oldValue, String newValue) {
				if(!parentController.isListInitialized())  return;
				String part1Name   = StringUtils.trimToEmpty(getView().getPart1ListView().getSelectionModel().getSelectedItem());
				String loc         = StringUtils.trimToEmpty(getView().getLocListView().getSelectionModel().getSelectedItem());
				String part2Name   = StringUtils.trimToEmpty(newValue);
				String defectName  = StringUtils.trimToEmpty(getView().getDefectListView().getSelectionModel().getSelectedItem());
				String defect1 = getView().parseDefect(defectName, false);
				String defect2 = getView().parseDefect(defectName, true);
				
				clearStatusAndResponsibleLevelMessage(DefectEntryController.RESPONSIBLE_LVL_MSG_ID);				
				String textEntryMenuName = StringUtils.trimToEmpty(getView().getMenuTileListView().getSelectedItem());
				if(!parentController.getSelectValue() && StringUtils.isNotEmpty(textEntryMenuName)){ 
					refreshTextFilterComponents(part1Name, loc, part2Name, defect1, defect2);
				}				
			}
		});
	}

	/**
	 * This method is used to add Defect Table Pane Listener
	 */
	private void addDefectListViewListener() {
		getView().getDefectListView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String oldValue, String newValue) {
				if(!parentController.isListInitialized())  return;
				String part1Name  = StringUtils.trimToEmpty(getView().getPart1ListView().getSelectionModel().getSelectedItem());
				String loc        = StringUtils.trimToEmpty(getView().getLocListView().getSelectionModel().getSelectedItem());
				String part2Name  = StringUtils.trimToEmpty(getView().getPart2ListView().getSelectionModel().getSelectedItem());
				String defectName = StringUtils.trimToEmpty(newValue);
				String defect1 = getView().parseDefect(defectName, false);
				String defect2 = getView().parseDefect(defectName, true);
				
				clearStatusAndResponsibleLevelMessage(DefectEntryController.RESPONSIBLE_LVL_MSG_ID);				
				String textEntryMenuName = StringUtils.trimToEmpty(getView().getMenuTileListView().getSelectedItem());
				if(!parentController.getSelectValue() && StringUtils.isNotEmpty(textEntryMenuName)){ 
					refreshTextFilterComponents(part1Name, loc, part2Name, defect1, defect2);
				}				
			}
		});
	}
	
	/**
	 * This method is used to refresh Defect Entry by Text filter components.
	 */
	private void initTextFilterComponents() {
		refreshTextFilterComponents(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
	}
	
	/**
	 * This method is used to refresh Defect Entry by Text filter components.
	 */
	public void refreshTextFilterComponents(String part1Name, String loc, String part2Name, String defectName, String defectName2) {
		String entryScreenName = getSelectedEntryScreen();
		String textEntryMenuName = StringUtils.trimToEmpty(getView().getMenuTileListView().getSelectedItem());
		
		//Part1 load
		String part1TitleSearchText = getPart1TitleSearchText();
		String p1 = part1Name, p2 = part2Name, p1l1 = loc;
		if(parentController.isInSearchMode()){
			//Check if searched part is on part1 list
			String partFilter = parentController.getSearchedPartName();
			List<QiDefectResultDto> filteredDtoList = getPart1ListViewByPartFilter(entryScreenName, textEntryMenuName, partFilter);
			List<String> part1List = getModel().getDefectEntryCacheUtil().getPart1ListFromDtoList(filteredDtoList);			
			List<String> part2List = getModel().getDefectEntryCacheUtil().getPart2ListFromDtoList(filteredDtoList);
			List<String> locList = getModel().getDefectEntryCacheUtil().getLocListFromDtoList(filteredDtoList);
			setPart1ListView(part1List);
			setPart2ListView(part2List);
			setLocListView(locList);
			if(part1List != null && !part1List.isEmpty()) {
				p1 = part1List.get(0);
			}
			if(part2List != null && !part2List.isEmpty()) {
				p2 = part2List.get(0);
			}
			if(locList != null && !locList.isEmpty()) {
				p1l1 = locList.get(0);
			}
					
		}else{
			//If no part is searched do normal load
			loadPart1ListViewElements(entryScreenName, textEntryMenuName, loc, part2Name, defectName, defectName2, part1TitleSearchText, StringUtils.EMPTY);
			loadPart2ListViewElements(entryScreenName, textEntryMenuName, part1Name, loc, defectName, defectName2, getPart2TitleSearchText(), part2Name);
			if(getView().getPart2ListView().getItems().contains(part2Name)){
				EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.PART2, part2Name, ObservableListChangeEventType.CHANGE_SELECTION));
			}
			//Loc load
			loadLocListViewElements(entryScreenName, textEntryMenuName, part1Name, part2Name, defectName, defectName2, part1TitleSearchText, part1Name);		
			if(getView().getLocListView().getItems().contains(loc)){
				EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.LOC, loc, ObservableListChangeEventType.CHANGE_SELECTION));
			}
			
		}
		//Defect load
		loadDefectListViewElements(entryScreenName, textEntryMenuName, p1, p1l1, p2, getDefectTitleSearchText(), part1Name);
		
		//Part Defect Comb load
		loadPartDefectCombListViewElements(entryScreenName, textEntryMenuName, p1, p1l1, p2, defectName, defectName2, part1Name);
	}
	
	/**
	 * This method is used to Clear Part Defect, Loc, Part1, Part and Defect List Views
	 */
	protected void clearListViewsElements(){
		getView().getPart1ListView().getItems().clear();
		getView().getLocListView().getItems().clear();
		getView().getPart2ListView().getItems().clear();
		getView().getDefectListView().getItems().clear();
		getView().getPartDefectListView().getItems().clear();
	}	
	
	/**
	 * This method is used to load Part1 List View elements based on given parameters
	 */
	protected void loadPart1ListViewElements(String entryScreen, String textEntryMenu, String part1Filter){		
		loadPart1ListViewElements(entryScreen, textEntryMenu, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, part1Filter);
	}	
	
	/**
	 * This method is used to load Part1 List View elements based on given parameters
	 */
	protected void loadPart1ListViewElements(String entryScreen, String textEntryMenu, String loc, String part2, String defect, String defect2, String filter, String part1Filter){		
		List<String> part1List = FXCollections.observableArrayList(
			getModel().findAllPart1ByEntryScreen(entryScreen, textEntryMenu,loc, part2,defect, defect2, filter, part1Filter)
		);
		EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.PART1_SET_ITEMS, part1List, ObservableListChangeEventType.CHANGE_SELECTION));		
	}
	
	/**
	 * This method is used to load Part1 List View elements based on given parameters
	 */
	protected void loadPart1ListViewByPartFilter(String entryScreen, String textEntryMenu, String part1Filter){		
		List<String> part1List = FXCollections.observableArrayList(
			getModel().findAllPart1ByEntryScreenAndPartFilter(entryScreen, textEntryMenu, part1Filter)
		);
		EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.PART1_SET_ITEMS, part1List, ObservableListChangeEventType.CHANGE_SELECTION));		
	}
	
	/**
	 * set part 1 list
	 */
	protected void setPart1ListView(List<String> part1List)  {
		List<String> fxList = FXCollections.observableArrayList(part1List);
		EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.PART1_SET_ITEMS, fxList, ObservableListChangeEventType.CHANGE_SELECTION));				
	}
	
	/**
	 * This method is used to get list of matching PDC
	 */
	protected List<QiDefectResultDto> getPart1ListViewByPartFilter(String entryScreen, String textEntryMenu, String partFilter){		
		List<QiDefectResultDto> filteredDtoList = getModel().getDefectEntryCacheUtil().getTextPdcListByCombinedFilter(entryScreen, textEntryMenu, partFilter);
		return filteredDtoList;
	}
	
	/**
	 * This method is used to load Loc List View elements based on given parameters
	 */
	protected void loadLocListViewElements(String entryScreenName, String menuName, String part1TextFilter){
		loadLocListViewElements(entryScreenName, menuName, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, part1TextFilter);
	}
	
	/**
	 * This method is used to load Loc List View elements based on given parameters
	 */
	protected void loadLocListViewElements(String entryScreen, String textEntryMenu, String part1, String part2, String defect, String defect2, String filter, String part1Filter){
		List<String> locList = FXCollections.observableArrayList(
			getModel().findAllLocByEntryScreen(entryScreen, textEntryMenu, part1, part2, defect, defect2, filter, part1Filter)
		);
		EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.LOC_SET_ITEMS, locList, ObservableListChangeEventType.CHANGE_SELECTION));
	}
	
	/**
	 * set location list
	 */
	protected void setLocListView(List<String> locList)  {
		List<String> fxList = FXCollections.observableArrayList(locList);
		EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.LOC_SET_ITEMS, fxList, ObservableListChangeEventType.CHANGE_SELECTION));				
	}
	
	/**
	 * This method is used to load Part 2 List View elements based on given parameters
	 */
	protected void loadPart2ListViewElements(String entryScreenName, String menuName, String part1TextFilter){
		loadPart2ListViewElements(entryScreenName, menuName, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, part1TextFilter);
	}
	
	/**
	 * This method is used to load Part 2 List View elements based on given parameters
	 */
	protected void loadPart2ListViewElements(String entryScreen, String textEntryMenu, String part1, String loc, String defect, String defect2, String filter, String part2Name){		
		List<String> part2List = FXCollections.observableArrayList(
			getModel().findAllPart2ByEntryScreen(entryScreen, textEntryMenu, part1, loc, defect, defect2, filter, part2Name)
		);
		EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.PART2_SET_ITEMS, part2List, ObservableListChangeEventType.CHANGE_SELECTION));				
	}
	
	/**
	 * set part 2 list
	 */
	protected void setPart2ListView(List<String> part2List)  {
		List<String> fxList = FXCollections.observableArrayList(part2List);
		EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.PART2_SET_ITEMS, fxList, ObservableListChangeEventType.CHANGE_SELECTION));				
	}
	
	/**
	 * This method is used to load Defect List View elements based on given parameters
	 */
	protected void loadDefectListViewElements(String entryScreenName, String menuName, String part1TextFilter){
		loadDefectListViewElements(entryScreenName, menuName,StringUtils.EMPTY, StringUtils.EMPTY,StringUtils.EMPTY, StringUtils.EMPTY, part1TextFilter);
	}
	
	/**
	 * This method is used to load Defect List View elements based on given parameters
	 */
	protected void loadDefectListViewElements(String entryScreen, String textEntryMenu, String part1, String loc, String part2, String filter, String part1Filter){
		List<String> defectList = FXCollections.observableArrayList(
			getModel().findAllDefectByEntryScreen(entryScreen, textEntryMenu, part1, loc, part2, filter, part1Filter)
		);
		EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.DEFECT_SET_ITEMS, defectList, ObservableListChangeEventType.CHANGE_SELECTION));			
	}
	
	/**
	 * This method is used to load Part1 List View elements based on given parameters
	 */
	protected void loadPartDefectCombListViewElements(String entryScreen, String textEntryMenu, String part1Filter){
		loadPartDefectCombListViewElements(entryScreen, textEntryMenu, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, part1Filter);
	}
	
	/**
	 * This method is used to load Part1 List View elements based on given parameters
	 */
	protected void loadPartDefectCombListViewElements(String entryScreen, String textEntryMenu, String part1, String loc, String part2, String defect, String defect2, String part1Filter){
		List<QiDefectResultDto> uniqueDtoList = new ArrayList<QiDefectResultDto>(
			new HashSet<QiDefectResultDto>(
					getModel().findAllPartDefectCombByFilter(entryScreen, textEntryMenu, part1, loc, part2, defect, defect2, part1Filter)
			)
		);
		if(!uniqueDtoList.isEmpty()){
			Collections.sort(uniqueDtoList, new QiDefectResultDto());
		}
		EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.PART_DEFECT_SET_ITEMS, uniqueDtoList, ObservableListChangeEventType.CHANGE_SELECTION));
	}
	
	/**
	 * This method is used to add Part Defect ListView Listener
	 */
	private void addPartDefectListViewListener() {
		getView().getPartDefectListView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiDefectResultDto>() {
			@Override
			public void changed(
					ObservableValue<? extends QiDefectResultDto> arg0,
					QiDefectResultDto oldValue, QiDefectResultDto newValue) {
				clearStatusAndResponsibleLevelMessage(DefectEntryController.RESPONSIBLE_LVL_MSG_ID);				
				getView().resetResponsibilityCombobox();
				if(newValue!=null) {
					parentController.setResponsibilityOnDefectSelect(newValue);
					getView().getLastDefectEnteredTextAreaForText().setText("Current Defect Selected: \n\""+newValue.getPartDefectDesc()+"\"");
					parentController.setResponsibilityComboboxDisable(false);
					if(getView().getAcceptBtn().isDisabled() && getView().getPartDefectListView().isFocused()){
						EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.SHOW_CONFIRM_POPUP, null, ObservableListChangeEventType.WARNING));
					}
				} 
				else{
					getView().setLastDefectEnteredText(true);
					parentController.setResponsibilityComboboxDisable(true);
				}
			}
		});
	}	
	
	/**
	 * This method is used to create Text Entry Screen
	 */
	public Node createTextEntryScreen(QiDefectEntryDto item, double size) {
		String tempArray[] = item.getEntryScreen().split("\\(");
		String entryScreenName = tempArray[0];
		HBox box = new HBox();
		LoggedLabel textLabel = UiFactory.createLabel("textLabel");
		textLabel.setText(entryScreenName);
		Tooltip toolTip = new Tooltip(entryScreenName);
		toolTip.setStyle("-fx-font-weight: bold;-fx-font-size: "+getModel().getThumbnailHoverFontSize()+";");
		textLabel.setTooltip(toolTip);
		textLabel.getStyleClass().add("display-label");
		textLabel.setWrapText(true);
		textLabel.setTextAlignment(TextAlignment.CENTER);
		textLabel.setContentDisplay(ContentDisplay.CENTER);
		textLabel.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.15 * size)));
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(textLabel);
		box.setMinSize(size, size);
		box.setMaxSize(size, size);
		box.getStyleClass().add("tile-list-view-textfield");
		box.setPadding(new Insets(5));
		return box;
	}
	
	/**
	 * This method is used to add Text Entry Screen ListView Listener
	 */
	public void textEntryScreenListener() {
		final QiDefectEntryDto newValue = getView().getEntryScreenListView().getSelectedItem();
		EventBusUtil.publish(new ProgressEvent(100,"Loading Text defect screens."));
		clearMessage();
		getView().resetResponsibilityCombobox();
		getView().getDefectPanelByText().toFront();
		getView().getDefectPanelByText().setVisible(true);
		getView().getdefectPanelByImage().setVisible(false);
		getView().setLastDefectEnteredText(true);
		getView().getPart1TitledPane().setText(PART_1);
		getView().getLocTitledPane().setText(LOC);
		getView().getPart2TitledPane().setText(PART_2);
		getView().getDefectTitledPane().setText(DEFECT);
		getView().resetIqsScoreSelection();
		if(newValue!=null) {
			String entryModel = getView().getEntryModelLabel().getText();
			getModel().generatePartDefectCombCache(newValue, newValue.isImage(), parentController.assignRealProblem, isMbpnDefectEntryLimitToProductIdPart(), entryModel);
			List<String> entryMenuList = getModel().findAllTextEntryMenuByEntryScreen(newValue.getEntryScreen(), getPart1TextFilter());			
			Collections.sort(entryMenuList);
			
			getView().getPart1ListView().getItems().clear();
			getView().getLocListView().getItems().clear();
			getView().getPart2ListView().getItems().clear();
			getView().getDefectListView().getItems().clear();
			getView().getPartDefectListView().getItems().clear();			
			
			String entryScreenName = newValue.getEntryScreen();
			if(parentController.isInSearchMode()){
				List<String> filteredMenuList = new ArrayList<String>();
				for(String menuName : entryMenuList){
					List<String> partList = getModel().findAllPart1ByEntryScreenAndPartFilter(entryScreenName, menuName, parentController.getSearchedPartName());
					if(!partList.isEmpty()){
						filteredMenuList.add(menuName);
						
					}		
				}	
				entryMenuList = filteredMenuList;
			}			
			getView().getMenuTileListView().setItems(entryMenuList);
			
			if(!StringUtils.isEmpty(getPart1TextFilter()) || parentController.isInSearchMode()) {
				getView().getMenuTileListView().selectFirst();
			}
			parentController.checkMandatoryFields();

		} else {
			getModel().setDefectResult(null);
			parentController.setSelectedDefect(null);
			getView().getdefectPanelByImage().setVisible(false);
			getView().getDefectPanelByText().setVisible(false);
			
			getView().getMenuTileListView().clear();
			getView().getPart1ListView().getItems().clear();
			getView().getLocListView().getItems().clear();
			getView().getPart2ListView().getItems().clear();
			getView().getDefectListView().getItems().clear();
			getView().getPartDefectListView().getItems().clear();
		}
		/** Used to keep focus on Defect Scan Text Field when ever any Entry Screen is being selected */
		Platform.runLater( new Runnable() {
		    @Override
		    public void run() {
		    	// check if scan is set true for Entry Screen used to hide/display scan textfield.
		    	if( newValue != null && enableScanTextField(newValue.getEntryScreen())){
					getView().getScanTextField().setDisable(false);
					getView().getScanTextField().requestFocus();
				}
				else
				{
					getView().getScanTextField().setDisable(true);
				}
				
		    }
		});
	}

	/**
	 * This method is used to set all the parameters and select required menu, pdc before accept/save the defect
	 * @param scanned
	 */
	public void preAcceptDefect(String scanned){
		clearMessage();
		List<QiDefectResult> defectResults = getModel().getCachedDefectResultList();
		QiDefectEntryScanType uniqueScanType = QiDefectEntryScanType.getType(getView().getScanTextField().getText());
		switch(uniqueScanType){
			case REPAIRED: getView().getRepairedRadioBtn().setSelected(true);
				break;

			case NOT_REPAIRED: getView().getNotRepairedRadioBtn().setSelected(true);
				break;

			case NON_REPAIRABLE: getView().getNonRepairableRadioBtn().setSelected(true);
				break;

			case DIRECT_PASS: 
				if(defectResults.isEmpty()){
				EventBusUtil.publishAndWait(new ProductEvent(getView().getViewLabel(), ProductEventType.PRODUCT_DIRECT_PASSED));
				}else{
					parentController.displayErrorMessage("Direct Pass scan is not allowed as some data is already accepted");	
				}
				break;

			case DONE: 
				if(!defectResults.isEmpty()){
				EventBusUtil.publishAndWait(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_DEFECT_DONE));
				}else{
					parentController.displayErrorMessage("Done scan is not allowed as no data is accepted");	
				}
				break;

			case VOID_LAST: 
				if(!defectResults.isEmpty()){
					voidLastAction(defectResults);
				} else {
					parentController.displayErrorMessage("Invalid action. No defect entered yet.");	
				}
				break;

			case PART_DEFECT_COMBINATION:
				if(scanned.contains("-")){
					if(!selectPartDefectCombination(scanned)){
						parentController.displayErrorMessage("Allow Scan is not configured for the entry screen.");	
						return;
					}
					else if(getView().getPartDefectListView().getSelectionModel().getSelectedItem()==null){
						parentController.displayErrorMessage("Scanned PDC does not exist for selected Entry Screen");	
						return;
					}
					parentController.acceptDefect();
				}
				else{ 
					parentController.displayErrorMessage("Not a valid Scan");	
				}
				break;

			default : break;
		}
		getView().getScanTextField().clear();

	}

	/**
	 * @param scanned
	 */
	private boolean selectPartDefectCombination(String scanned) {
		String sArray[] = scanned.split(HYPHEN_SYMBOL);
		Integer localAttributeId = Integer.parseInt(sArray[0].toString());
		String entryScreen = sArray[1].toString();
		String textMenu = sArray[2];
		List<QiDefectEntryDto> listView = getView().getEntryScreenListView().getItems();
		boolean isScanAllowed=enableScanTextField(entryScreen);

		/** Selection of Entry Screen */
		for(QiDefectEntryDto dto : listView){
			if(dto.getEntryScreen().equalsIgnoreCase(entryScreen)){
				if(isScanAllowed){
					getView().getEntryScreenListView().select(dto);
				}
				else
				{
					return enableScanTextField(entryScreen);
				}
				break;
			}
		}

		/** Selection of menu */
		getView().getMenuTileListView().select(textMenu);

		/** Selection of part defect combination */
		List<QiDefectResultDto> pdcList = getView().getPartDefectListView().getItems();
		for(QiDefectResultDto dto : pdcList){
			if(dto.getLocalDefectCombinationId()==localAttributeId){
				getView().getPartDefectListView().getSelectionModel().select(dto);
				break;
			}
		}
		getView().getScanTextField().clear();
		return isScanAllowed;
	}
	

	public void voidLastAction(List<QiDefectResult> defectResults) {
		String qualitity = getModel().getQuantity();
		if(getModel().getProperty().isUpcStation() && qualitity != null){
			for (int i = 0; i < Integer.parseInt(qualitity); i++) {
				defectResults.remove(defectResults.size() - 1);
			}
		} else {
			defectResults.remove(defectResults.size() - 1);
		}
		
		getView().setLastDefectEnteredText(false);
		
		if(defectResults.isEmpty()) {
			EventBusUtil.publish(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_DEFECT_VOID_ALL));
		}

		parentController.setExistingDefectCount();
	}

	private DefectEntryView getView() {
		return parentController.getView();
	}
	
	private DefectEntryModel getModel() {
		return parentController.getModel();
	}
	
	private void clearMessage() {
		parentController.clearMessage();
	}
	
	private void clearStatusAndResponsibleLevelMessage(String newId) {
		parentController.clearStatusOnly();
		parentController.clearById(newId);
	}
	
	private String getPart1TextFilter() {
		return parentController.getPart1TextFilter();
	}
	
	private boolean enableScanTextField(String entryScreen) {
		List<QiStationEntryScreen> stationEntryList=getModel().findAllByEntryScreenModelAndDept(entryScreen,getView().getEntryModelLabel().getText(),parentController.getApplicationContext().getEntryDept(),getModel().getProcessPointId());
		if( stationEntryList!=null && stationEntryList.size()>0){
			return true;
		}
		return false;
	}
	
	private boolean isMbpnDefectEntryLimitToProductIdPart() {
		boolean isMbpnDefectEntryLimitToProductIdPart;
		QiStationConfiguration qiStationConfigEntry = getModel().findPropertyKeyValueByProcessPoint(QiEntryStationConfigurationSettings.MBPN_DEFECT_ENTRY_LIMIT_TO_PRODUCT_ID_PART.getSettingsName());
		if (qiStationConfigEntry != null) {
			isMbpnDefectEntryLimitToProductIdPart = "Yes".equalsIgnoreCase(qiStationConfigEntry.getPropertyValue()) ? true : false;
		} else {
			isMbpnDefectEntryLimitToProductIdPart = QiEntryStationConfigurationSettings.MBPN_DEFECT_ENTRY_LIMIT_TO_PRODUCT_ID_PART.getDefaultPropertyValue()
					.equalsIgnoreCase("Yes") ? true : false;
		}
		return isMbpnDefectEntryLimitToProductIdPart;
	}
	
	protected String getSelectedEntryScreen(){
		QiDefectEntryDto entryScreen = getView().getEntryScreenListView().getSelectedItem();
		return entryScreen != null ? entryScreen.getEntryScreen() : StringUtils.EMPTY;
		
	}
	
	protected String getPart1TitleSearchText(){
		return StringUtils.trimToEmpty(getView().getPart1TitledPane().getText().equalsIgnoreCase(PART_1) ? null : getView().getPart1TitledPane().getText());
	}
	
	protected String getPart2TitleSearchText(){
		return StringUtils.trimToEmpty(getView().getPart2TitledPane().getText().equalsIgnoreCase(PART_2) ? null : getView().getPart2TitledPane().getText());
	}
	
	protected String getDefectTitleSearchText(){
		return StringUtils.trimToEmpty(getView().getDefectTitledPane().getText().equalsIgnoreCase(DEFECT) ? null : getView().getDefectTitledPane().getText());
	}

}
