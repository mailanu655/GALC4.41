package com.honda.galc.client.qi.defectentry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.pane.ProcessPointSelectionPane;
import com.honda.galc.client.qi.base.AbstractQiProcessView;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.AutoCompleteTextField;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.TileListView;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.ResetEvent;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.IqsScoreUtils;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.QiDefectEntryDto;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.IqsAuditAction;
import com.honda.galc.entity.enumtype.IqsScore;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.enumtype.QiEntryStationDefaultStatus;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.property.KickoutPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.util.Callback;
/**
 * 
 * <h3>DefectEntryView Class description</h3>
 * <p> DefectEntryView description </p>
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
 * Nov 26, 2016
 *
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class DefectEntryView extends AbstractQiProcessView<DefectEntryModel, DefectEntryController>{

	private ObjectProperty<Font> textFont;
	private ObjectProperty<Font> labelFont;
	private ObjectProperty<Font> buttonFont;
	private StringProperty radioButtonStyle;
	private StringProperty comboBoxStyle;
	private StringProperty textStyle;

	private TileListView<String> menuTileListView;
	private ListView<String> part1ListView;
	private ListView<String> locListView;
	private ListView<String> part2ListView;
	private ListView<String> defectListView;
	private ListView<QiDefectResultDto> partDefectListView;
	private LabeledComboBox<String> siteComboBox;
	private LabeledComboBox<String> plantComboBox;
	private LabeledComboBox<String> departmentComboBox;
	private LabeledComboBox<KeyValue<String,Integer>> level1ComboBox;
	private LoggedComboBox<String> writeUpDeptComboBox;
	private LoggedComboBox<IqsScore> iqsScoreComboBox;
	private LoggedComboBox<IqsAuditAction> iqsAuditActionComboBox;
	private UpperCaseFieldBean menuSearchTextField;
	private AutoCompleteTextField partSearchTextField;
	private TextArea lastDefectEnteredTextAreaForText;
	private ToggleGroup toggleGroup;

	private AnchorPane imagePane;
	private ListView<String> defect1ListView;
	private StackPane defectStackPane;
	private VBox defectPanelByText;
	private VBox defectPanelByImage;
	private ImageView defectImageView;
	private List<Polygon> imageSections;
	private TextArea lastDefectEnteredTextAreaForImage;
	private LoggedRadioButton repairedRadioBtn ;
	private LoggedRadioButton notRepairedRadioBtn ;
	private LoggedRadioButton nonRepairableRadioBtn;
	
	private LoggedLabel entryModelLabel;
	private LoggedLabel colorCodeLabel;
	private VBox mainPanel;
	
	private TitledPane part1TitledPane;
	private TitledPane locTitledPane;
	private TitledPane part2TitledPane;
	private TitledPane defectTitledPane;
	private TitledPane defect1TitledPane;
	private ProcessPointSelectionPane kickoutLocationPane;
	private EventHandler<KeyEvent> keyEventHandler = null;
	

	private LoggedButton acceptBtn;
	private LoggedButton resetBtn;
	private LoggedButton existingDefectBtn;
	private TileListView<QiDefectEntryDto> entryScreenListView;
	private UpperCaseFieldBean scannedTextField;
	private LoggedButton clearPartSearchTxtBtn;
	private boolean viewStarted = false;
	private List<String> mostFreqList;
	public boolean isMostFreqUsed = false;
	public boolean isScrapedproduct = false;
	public String scrapMessage;
	ResponsibleLevelController respController = null;
	ResponsibleLevelPanel rPanel = null;
	
	private LoggedButton recentDefectBtn;

	public DefectEntryView(MainWindow window) {
		super(ViewId.DEFECT_ENTRY, window);
	}
	
	public DefectEntryView(ViewId history, MainWindow mainWindow) {
		super(history, mainWindow);
	}

	@Override
	public void reload() {
		clearErrorMessage();
		if (isScrapedproduct)  {
			EventBusUtil.publish(new StatusMessageEvent(QiConstant.PRODUCT_ALREADY_SCRAPED + ": "+scrapMessage, StatusMessageEventType.WARNING));
		}
		clearComponentsOnTabSelect();
		getModel().refreshDefect();
		/** Used to keep focus on Defect Scan Text Field when ever Defect Entry Screen gets loaded */
		Platform.runLater( new Runnable() {
			@Override
			public void run() {
				getScanTextField().requestFocus();
			}
		});
		if(!getModel().getCachedDefectResultList().isEmpty()) {
			EventBusUtil.publish(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_DEFECT_ACCEPT));
		}
		if (!viewStarted) {
			getController().startLoadData();
			setViewStarted(true);
		}
		if(defect1ListView!=null) {
			defect1ListView.scrollTo(0);
		}
		QiStationConfiguration openExistingDefect = 
				this.getModel().findPropertyKeyValueByProcessPoint(QiEntryStationConfigurationSettings.OPEN_EXISTING_DEFECTS.getSettingsName());
		if (openExistingDefect != null &&
			openExistingDefect.getPropertyValue().equalsIgnoreCase(QiConstant.YES) &&
			existingDefectBtn.isDisabled() == false) {
			Platform.runLater( new Runnable() {
				@Override
				public void run() {
					getController().openVoidUpdatePopup();
				}
			});
		}
	}
	/**
	 * This method is used to reload view when navigating from recent defect poupup screen
	 * @param result
	 */
	@SuppressWarnings("unchecked")
	public void reload(QiDefectResult result) {
		try {
			clearErrorMessage();
			entryScreenListView.clearSelection();
			getModel().refreshDefect();
			if(getController().loadRecentDefectData(result)) {
				writeUpDeptComboBox.getSelectionModel().select(result.getWriteUpDept());
				siteComboBox.getControl().getSelectionModel().select(result.getResponsibleSite());
				plantComboBox.getControl().getSelectionModel().select(result.getResponsiblePlant());
				departmentComboBox.getControl().getSelectionModel().select(result.getResponsibleDept());
				KeyValue<String,Integer> kv = ResponsibleLevelController.getKeyValue(result.getResponsibleLevel1()); 
				level1ComboBox.getControl().getSelectionModel().select(kv);
				if(result.getOriginalDefectStatus()== DefectStatus.REPAIRED.getId()){
					repairedRadioBtn.setSelected(true);
				}else if(result.getOriginalDefectStatus()==DefectStatus.NOT_REPAIRED.getId())
					notRepairedRadioBtn.setSelected(true);
				else if(result.getOriginalDefectStatus()==DefectStatus.NON_REPAIRABLE.getId())
					nonRepairableRadioBtn.setSelected(true);
			
				if (!getAcceptBtn().isDisabled() && getController().isResponsibilityAccessible()) {
					//if Accept button is enabled and allow to overwrite responsibility, enable responsibility
					getController().setResponsibilityComboboxDisable(false);
				}
			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	@Override
	public void start() {
		getModel().refreshDefect();
		getController().startLoadData();
	}

	@Override
	public void reset() {
		getEntryScreenListView().clearSelection();
		getEntryScreenListView().clear();
	}

	@Override
	public void initView() {
		if(Platform.isSupported(ConditionalFeature.INPUT_MULTITOUCH))
			getMainWindow().getStylesheets().add(QiConstant.CSS_PATH_TOUCH_SCREEN_DEVICE);
		else
			getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		defectStackPane = createDefectPanel();
		this.setPrefHeight(getScreenHeight() * 0.70);
		this.setMinHeight(getScreenHeight() * 0.30);
		this.setMaxHeight(getScreenHeight());
		this.setMinWidth(getScreenWidth() * 0.99);
		this.setMaxWidth(getScreenWidth() * 0.99);
		this.setLeft(createEntryScreenPanel());
		this.setCenter(defectStackPane);
		this.setRight(createResponsibilityPanel());
		defectPanelByImage.setVisible(false);
		defectPanelByText.setVisible(false);
		imageSections = new ArrayList<Polygon>();
		Logger.getLogger().info("scannedTextField gained Focus");
	}
	
	/**
	 * This method is used to create Entry Screen Panel to be displayed on screen.
	 * @return Node
	 */
	private Node createEntryScreenPanel() {
		
		double padding = getScreenWidth() * 0.0025;
		double mainPanelWidth = getScreenWidth() * 0.16;
		double entryScreenPanelHeight = getScreenHeight() * 0.58;
		
		mainPanel = new VBox();
		mainPanel.prefWidthProperty().bind(widthProperty().multiply(0.18));
		mainPanel.setMinWidth(mainPanelWidth);
		mainPanel.setMaxWidth(mainPanelWidth);
		
		mainPanel.setPadding(new Insets(padding));
		mainPanel.setSpacing(padding);
		partSearchTextField= createAutoCompleteTextField("partSearchTextField", 500, "Search Part");
		HBox.setHgrow(partSearchTextField, Priority.ALWAYS);
		clearPartSearchTxtBtn =UiFactory.createButton(QiConstant.CLEAR_TEXT_SYMBOL, "clearPartSearchTxtBtn");
		clearPartSearchTxtBtn.setOnAction(getController());
		HBox entryScreenPanel = new HBox();
		HBox partSearchBox = new HBox();
		partSearchBox.getChildren().addAll(partSearchTextField,clearPartSearchTxtBtn);
		entryScreenListView = new TileListView<QiDefectEntryDto>();
		entryScreenListView.setHgap(padding);
		entryScreenListView.setVgap(padding);
		entryScreenListView.setAlignment(Pos.TOP_CENTER);
		entryScreenListView.setPadding(new Insets(0, 10, 0, 0));
		ScrollPane scrollPane = new ScrollPane(entryScreenListView);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		entryScreenListView.minWidthProperty().bind(scrollPane.widthProperty());
		entryScreenListView.maxWidthProperty().bind(scrollPane.widthProperty());
		scrollPane.setStyle("-fx-background-color:transparent;");
		entryScreenPanel.getChildren().addAll(scrollPane);
		entryScreenPanel.setPrefHeight(entryScreenPanelHeight);
		entryScreenPanel.setMinHeight(entryScreenPanelHeight);
		entryScreenPanel.setMaxHeight(entryScreenPanelHeight);
		
		TitledPane titledPane = new TitledPane("Thumbnail Selection", entryScreenPanel);
		
		mainPanel.getChildren().addAll(partSearchBox, titledPane);
		mainPanel.setAlignment(Pos.CENTER);
		return mainPanel;
	}
	
	/**
	 * This method is used to create Responsibility Panel to be displayed on screen.
	 * @return Node
	 */
	private Node createResponsibilityPanel() {
		VBox mainPanel = new VBox();
		mainPanel.setPrefWidth(getScreenWidth() * 0.18);
		mainPanel.setSpacing(getScreenHeight() * 0.005);
		mainPanel.setAlignment(Pos.CENTER);
		
		HBox entryModelBox = new HBox();
		LoggedLabel entryModelTitle = createLabel("entryModelTitle", "Entry Model:");
		entryModelLabel = createLabel("entryModelLabel", StringUtils.EMPTY);
		entryModelTitle.fontProperty().bind(labelFont);
		entryModelLabel.fontProperty().bind(labelFont);
		entryModelBox.maxHeightProperty().bind(mainPanel.heightProperty().multiply(0.055));
		entryModelBox.prefHeightProperty().bind(mainPanel.heightProperty().multiply(0.48));
		entryModelBox.minHeightProperty().bind(mainPanel.heightProperty().multiply(0.04));
		entryModelBox.getChildren().addAll(entryModelTitle,entryModelLabel);
		entryModelBox.setAlignment(Pos.CENTER_LEFT);
		entryModelBox.spacingProperty().bind(mainPanel.heightProperty().multiply(0.0132));

		
		HBox taskActionPane = createTaskButtonPane();
		TitledPane statusPane = createDefectStatusPane();
		VBox wuIqsBox = createWuIqsBox();
		TitledPane responsibilityPane = null;
		if(!getController().isShowL2L3())  {
			responsibilityPane = createResponsibilityPane();
		}
		else  {
			rPanel = ResponsibleLevelPanel.getInstance(this, getCurrentScreenBounds().getWidth());
			respController = getController().getRespController();
			rPanel.setController(respController);
			respController.setResponsiblePanel(rPanel);
			responsibilityPane = rPanel.createEntryResponsibilityPanel();
			this.setRight(responsibilityPane);
			siteComboBox = rPanel.getSiteComboBox();
			plantComboBox = rPanel.getPlantComboBox();
			departmentComboBox = rPanel.getDepartmentComboBox();
			level1ComboBox = rPanel.getResponsibleLevel1ComboBox();
			if (getScreenHeight() < 870) {
				int height = 22;
				siteComboBox.setHeight(height);
				plantComboBox.setHeight(height);
				departmentComboBox.setHeight(height);
				level1ComboBox.setHeight(height);
				rPanel.getResponsibleLevel2ComboBox().setHeight(height);
				rPanel.getResponsibleLevel3ComboBox().setHeight(height);
			}
		}
		HBox buttonBox = createProcessButtonPane();
		if(getController().isShowKickoutPane()) {
			TitledPane kickoutPane = createKickoutLocationPane();
			if(getScreenHeight() <= 728) {
				kickoutPane.setMinHeight(90);
			} else {
				kickoutPane.setMaxHeight(115);
			}
			mainPanel.getChildren().addAll(entryModelBox,taskActionPane, statusPane, wuIqsBox, responsibilityPane,  kickoutPane, buttonBox);
		} else {
			mainPanel.getChildren().addAll(entryModelBox,taskActionPane, statusPane, wuIqsBox, responsibilityPane, buttonBox);
		}
		return mainPanel;
	}

	private TitledPane createKickoutLocationPane() {
		boolean isFilterKickout = PropertyService
				.getPropertyBean(KickoutPropertyBean.class, getMainWindow().getApplicationContext().getApplicationId())
				.isFilterKickoutProcess();
		kickoutLocationPane = new ProcessPointSelectionPane(this, false, false, true, isFilterKickout);
		kickoutLocationPane.getDepartmentComboBox().getStyleClass().add("station-respons-combo");
		kickoutLocationPane.getLineComboBox().getStyleClass().add("station-respons-combo");
		kickoutLocationPane.getProcessPointComboBox().getStyleClass().add("station-respons-combo");

		
		return new TitledPane("Kickout Location", kickoutLocationPane);
	}
	
	/**
	 * This method is used to create Defect Panel to be displayed on screen.
	 * @return Node
	 */
	private StackPane createDefectPanel() {
        
		StackPane mainPane = new StackPane();
		defectPanelByText = (VBox) createDefectPanelByText();
		defectPanelByImage = (VBox) createDefectPanelByImage();
		mainPane.getChildren().addAll(defectPanelByText,defectPanelByImage);
		mainPane.setMinWidth(getScreenWidth() * 0.60);
		mainPane.setMaxWidth(getScreenWidth() * 0.60);
		defectPanelByText.minWidthProperty().bind(mainPane.widthProperty());
		defectPanelByImage.minWidthProperty().bind(mainPane.widthProperty());
		return mainPane;
	}
	/**
	 * This method is used to create Defect Panel to be displayed on screen.
	 * @return Node
	 */
	private Node createDefectPanelByText() {
		VBox mainPanel = new VBox();
		mainPanel.setAlignment(Pos.CENTER);
		mainPanel.setSpacing(20);
		
		Double screenWidth = getScreenWidth();
		Double width = getScreenWidth() * 0.30;
		scannedTextField = createFilterTextField("scannedTextField",width.intValue(),"Scan");
		
		VBox menuBox = createMenuPane();
		menuBox.minHeightProperty().bind(mainPanel.heightProperty().multiply(0.18));
		menuBox.maxHeightProperty().bind(mainPanel.heightProperty().multiply(0.18));
		
		HBox partfilterBox = createPartFilterPane();
		partfilterBox.minHeightProperty().bind(mainPanel.heightProperty().multiply(0.25));
		partfilterBox.maxHeightProperty().bind(mainPanel.heightProperty().multiply(0.25));
		
		double partFilterTotalWidth = screenWidth * 0.60;
		
		part1ListView.setMinWidth(partFilterTotalWidth * 0.30);
		part1ListView.setMaxWidth(partFilterTotalWidth * 0.30);
		
		locListView.setMinWidth(partFilterTotalWidth * 0.13);
		locListView.setMaxWidth(partFilterTotalWidth * 0.13);
		
		part2ListView.setMinWidth(partFilterTotalWidth * 0.28);
		part2ListView.setMaxWidth(partFilterTotalWidth * 0.28);
		
		defectListView.setMinWidth(partFilterTotalWidth * 0.28);
		defectListView.setMaxWidth(partFilterTotalWidth * 0.28);
		
		HBox defectBox = new HBox();
		partDefectListView = createPartDefectListView();
		partDefectListView.setMinWidth(partFilterTotalWidth * 0.75);
		partDefectListView.setMaxWidth(partFilterTotalWidth * 0.75);
		
		lastDefectEnteredTextAreaForText = createDisplayTextArea();
		
		setLastDefectEnteredText(true);
		defectBox.setSpacing(20);
		defectBox.getChildren().addAll(partDefectListView, lastDefectEnteredTextAreaForText);
		defectBox.minHeightProperty().bind(mainPanel.heightProperty().multiply(0.41));
		defectBox.maxHeightProperty().bind(mainPanel.heightProperty().multiply(0.41));
		
		mainPanel.getChildren().addAll(scannedTextField, menuBox, partfilterBox, defectBox);
		mainPanel.setMinHeight(getScreenHeight() * 0.30);
		mainPanel.setMaxHeight(getScreenHeight() * 1.30);
		return mainPanel;
	}
	
	/**
	 * This method is used to create Defect Panel for Image
	 * @return
	 */
	private Node createDefectPanelByImage() {
		VBox mainPanel = new VBox();
		mainPanel.setPadding(new Insets(10));
		mainPanel.setAlignment(Pos.CENTER);
		mainPanel.setSpacing(10);
		
		HBox imageBox = createImagePane();
		
		mainPanel.getChildren().addAll(imageBox);
		mainPanel.setMinHeight(getScreenHeight() * 0.30);
		mainPanel.setMaxHeight(getScreenHeight() * 1.30);
		return mainPanel;
	}
	
	/**
	 * This method is used to set Last Defect Accepted on respective TextField.
	 */
	public void setLastDefectEnteredText(boolean resetCombobox) {
		if(defectPanelByText != null && defectPanelByImage != null) {
			if(getModel().getCachedDefectResultList().isEmpty()) {
				if(defectPanelByText.isVisible()) {
					lastDefectEnteredTextAreaForText.setText("No Defect Entered");
					lastDefectEnteredTextAreaForText.setTooltip(null);
					if(resetCombobox)
						resetResponsibilityCombobox();
				}
				else if(defectPanelByImage.isVisible()) {
					lastDefectEnteredTextAreaForImage.setText("No Defect Entered");
					if(resetCombobox)
						resetResponsibilityCombobox();
				}
			}
			else {
				QiDefectResult lastDefect = getModel().getCachedDefectResultList().get(getModel().getCachedDefectResultList().size() - 1);
				if(defectPanelByText.isVisible()) {
					String defect = lastDefect.getPartDefectDesc().toUpperCase();
					lastDefectEnteredTextAreaForText.setText("Last Defect Entered: \n\""+defect+"\"");
					lastDefectEnteredTextAreaForText.setTooltip(new Tooltip(defect));
				}
				else if(defectPanelByImage.isVisible())
					lastDefectEnteredTextAreaForImage.setText("Last Defect Entered: \n\""+lastDefect.getPartDefectDesc().toUpperCase()+"\"");
			}
		}
	}
	
	/**
	 * This method is used to create Part Filter Pane
	 * @return HBox
	 */
	private HBox createPartFilterPane() {
		HBox partfilterBox = new HBox();
		part1ListView = createListViewWithString();
		part1TitledPane = new TitledPane("Part 1", part1ListView);
		part1TitledPane.getStyleClass().add("station-titled-pane");
		
		locListView = createListViewWithString();
		locTitledPane = new TitledPane("Loc", locListView);
		locTitledPane.getStyleClass().add("station-titled-pane");
		
		part2ListView = createListViewWithString();
		part2TitledPane = new TitledPane("Part 2", part2ListView);
		part2TitledPane.getStyleClass().add("station-titled-pane");
		
		defectListView = createListViewWithString();
		defectTitledPane = new TitledPane("Defect", defectListView);
		defectTitledPane.getStyleClass().add("station-titled-pane");
		
		partfilterBox.getChildren().addAll(part1TitledPane, locTitledPane, part2TitledPane, defectTitledPane);
		return partfilterBox;
	}
	
	/**
	 * This method is used to create Menu Pane
	 * @return VBox
	 */
	private VBox createMenuPane() {
		VBox menuBox = new VBox();
		Double width = getScreenWidth() * 0.30;
		menuSearchTextField = createFilterTextField("menuSearchTextField",width.intValue(),"Search Menu");
		menuTileListView = new TileListView<String>();
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setContent(menuTileListView);
		scrollPane.minHeightProperty().bind(menuBox.heightProperty().multiply(0.64));
		scrollPane.maxHeightProperty().bind(menuBox.heightProperty().multiply(0.64));
		
		menuTileListView.minWidthProperty().bind(scrollPane.widthProperty());
		menuTileListView.maxWidthProperty().bind(scrollPane.widthProperty());
		
		menuBox.setSpacing(10);
		menuBox.getChildren().addAll(menuSearchTextField, scrollPane);
		return menuBox;
	}
	
	/**
	 * This method is used to create Filter TextField
	 * @param id: Id of TextField
	 * @param width: Width of TextField
	 * @param promptText: Prompt text to be displayed on the TextField.
	 * @return UpperCaseFieldBean
	 */
	private UpperCaseFieldBean createFilterTextField(String id, int width, String promptText) {
		UpperCaseFieldBean menuSearchTextField= (UpperCaseFieldBean) UiFactory.createTextField(id, width, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, true);
		menuSearchTextField.setMaxWidth(width);
		menuSearchTextField.setPromptText(promptText);
		menuSearchTextField.setStyle("-fx-prompt-text-fill: black;");
		menuSearchTextField.setOnAction(getController());
		return menuSearchTextField;
	}
	
	/**
	 * This method is used to create Filter TextField
	 * @param id: Id of TextField
	 * @param width: Width of TextField
	 * @param promptText: Prompt text to be displayed on the TextField.
	 * @return UpperCaseFieldBean
	 */
	private AutoCompleteTextField createAutoCompleteTextField(String id, int width, String promptText) {
		AutoCompleteTextField menuSearchTextField= new AutoCompleteTextField(id,
				new AutoCompleteTextField.ISuggestionList()  {
			private volatile boolean flag = false;
			public boolean isFlag() {
				return flag;
			}
			public LinkedList<String> getSuggestionList(List<String> sourceList, String newText)  {
				String searchText = StringUtils.trimToEmpty(newText).toLowerCase();
				SortedSet<String> newSet = new TreeSet<String>();
				
				String modifiedFilter = StringUtils.trimToEmpty(newText).toLowerCase().replaceAll("%", ".*");
				Pattern pat = Pattern.compile(modifiedFilter);
				for (String e : sourceList) {
					Matcher mat = pat.matcher(e.toLowerCase());
					if (mat.find()) {
						String[] partDefect = e.split("@");
						newSet.add(partDefect[0].trim());
					}
				}

				return new LinkedList<String>(newSet);
			}
		});
		menuSearchTextField.setMaxSuggestions(15);
		menuSearchTextField.setMaxWidth(width);
		menuSearchTextField.setPromptText(promptText);
		menuSearchTextField.setStyle("-fx-prompt-text-fill: black; -fx-font-size: 12px; -fx-background-color: #F7DFB5;");
		menuSearchTextField.setOnAction(getController());
		menuSearchTextField.getSuggestionPopup().setOnAction(getController());
		return menuSearchTextField;
	}
	
	/**
	 * This method is used to create Part Defect ListView.
	 * @return ListView
	 */
	private ListView<QiDefectResultDto> createPartDefectListView(){
		final ListView<QiDefectResultDto> panel = new ListView<QiDefectResultDto>();
		panel.setCellFactory(new Callback<ListView<QiDefectResultDto>, ListCell<QiDefectResultDto>>() {
			@Override
			public ListCell<QiDefectResultDto> call(ListView<QiDefectResultDto> param) {
				ListCell<QiDefectResultDto> cell = new ListCell<QiDefectResultDto>(){
                    @Override
                    protected void updateItem(QiDefectResultDto item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty)
                            setText(null);
                        else
							setText(item.getPartDefectDesc());
                        setGraphic(null);
                    }
                };
                cell.addEventFilter(MouseEvent.MOUSE_PRESSED, getDeselectEventHandler(cell));
                return cell;
			}
		});
		panel.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.01 * getScreenWidth())));
		return panel;
	}
	
	/**
	 * This method is used to create Task(Existing Defects & Recent Defects) Button Panel
	 * @return HBox
	 */
	private HBox createTaskButtonPane(){
		HBox buttonContainer = new HBox();
		existingDefectBtn = createBtn("Existing Defects", getController());
		existingDefectBtn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		recentDefectBtn = createBtn("Recent Defects", getController());
		recentDefectBtn.setDisable(true);
		recentDefectBtn.fontProperty().bind(buttonFont);
		existingDefectBtn.setMaxHeight(30);
		recentDefectBtn.setMaxHeight(30);
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setSpacing(5);
		buttonContainer.getChildren().addAll(existingDefectBtn, recentDefectBtn);
		return buttonContainer;
	}
	
	/**
	 * This method is used to create Process(Accept & Reset) button Panel
	 * @return HBox
	 */
	private HBox createProcessButtonPane() {
		HBox buttonBox = new HBox();
		buttonBox.setSpacing(5);
		buttonBox.setAlignment(Pos.CENTER);
		acceptBtn = createBtn("Accept", getController());
		resetBtn = createBtn("Reset", getController());
		acceptBtn.fontProperty().bind(buttonFont);
		resetBtn.fontProperty().bind(buttonFont);
		acceptBtn.maxHeight(30);
		resetBtn.maxHeight(30);
		if(getController().isAcceptButton())  {
			buttonBox.getChildren().addAll(acceptBtn, resetBtn);
		}
		else  {
			buttonBox.getChildren().addAll(resetBtn);
			buttonBox.setAlignment(Pos.CENTER_RIGHT);
		}
		return buttonBox;
	}
	
	/**
	 * This method is used to create Responsibility Pane.
	 * @return TitledPane
	 */
	private TitledPane createResponsibilityPane() {
		VBox responsibilityBox = new VBox();
		siteComboBox = createRespLabeledComboBox("siteComboBox", "Site", true, true, true);
		plantComboBox = createRespLabeledComboBox("plantComboBox", "Plant", true, true, true);
		plantComboBox.getLabel().fontProperty().bind(textFont);
		departmentComboBox = createRespLabeledComboBox("departmentComboBox", "Dept", true, true, true);
		departmentComboBox.getLabel().fontProperty().bind(textFont);
		level1ComboBox = ResponsibleLevelPanel.createRespLevelComboBox("levelComboBox", "Level 1", true, true, true, getScreenWidth());
		level1ComboBox.getLabel().fontProperty().bind(textFont);
		responsibilityBox.getChildren().addAll(siteComboBox, plantComboBox, departmentComboBox, level1ComboBox);
		responsibilityBox.setAlignment(Pos.CENTER_RIGHT);
		TitledPane responsibilityPane = new TitledPane("Responsibility", responsibilityBox);
		return responsibilityPane;
	}
	
	/**
	 * This method is used to create Labeled Combobox for Responsibility Panel
	 */
	public LabeledComboBox<String> createRespLabeledComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory, boolean isDisabled) {
		LabeledComboBox<String> comboBox = createLabeledComboBox(id, labelName, isHorizontal, isMandatory, isDisabled);
		comboBox.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.009 * getScreenWidth())));
		return comboBox;
	}
	
	/**
	 * This method is used to create WriteUpDept Pane.
	 * @return TitledPane
	 */
	private TitledPane createWriteUpDeptPane() {
		HBox deptBox = new HBox();
		writeUpDeptComboBox = createComboBox("writeUpDeptComboBox", "WRITE UP DEPT");
		writeUpDeptComboBox.styleProperty().bind(comboBoxStyle);
		colorCodeLabel = createLabel("colorCodeLabel",StringUtils.EMPTY);
		colorCodeLabel.styleProperty().bind(textStyle);
		deptBox.getChildren().addAll(writeUpDeptComboBox,colorCodeLabel);
		deptBox.setPadding(new Insets(5));
		deptBox.setAlignment(Pos.CENTER);
		TitledPane writeUpDeptPane = new TitledPane("Write Up Dept", deptBox);
		return writeUpDeptPane;
	}

	private VBox createWuIqsBox() {
		VBox vBox = new VBox(5);
		vBox.setAlignment(Pos.TOP_CENTER);
		TitledPane wuPane = createWriteUpDeptPane();
		VBox.setVgrow(wuPane, Priority.ALWAYS);
		if(getController().isIqsInput()) {
			HBox iqsBox = createIqsBox();
			VBox.setVgrow(iqsBox, Priority.ALWAYS);
			vBox.getChildren().addAll(wuPane, iqsBox);
		} else {
			vBox.getChildren().addAll(wuPane);
		}
		return vBox;
	}
	
	private TitledPane createIqsScorePane() {
		VBox vb = new VBox();
		vb.setAlignment(Pos.CENTER);
		vb.setPadding(new Insets(5, 5, 5 + getScreenWidth() / 68, 5));
		iqsScoreComboBox = new LoggedComboBox<IqsScore>("iqsScoreComboBox");
		iqsScoreComboBox.setPromptText("IQS SCORE");
		iqsScoreComboBox.getItems().addAll(IqsScoreUtils.getActiveScores());

		vb.getChildren().addAll(iqsScoreComboBox);
		return new TitledPane("IQS Score", vb);
	}
	
	private TitledPane createIqsAuditActionPane() {
		VBox vb = new VBox();
		vb.setAlignment(Pos.CENTER);
		vb.setPadding(new Insets(5, 5, 5 + getScreenWidth() / 68, 5));
		iqsAuditActionComboBox = new LoggedComboBox<IqsAuditAction>("iqsAuditActionComboBox");
		iqsAuditActionComboBox.setPromptText("IQS AUDIT ACTION");
		List<IqsAuditAction> auditActions = new ArrayList<IqsAuditAction>();
		for(IqsAuditAction auditAction : IqsAuditAction.values()) {
			if (auditAction.getId() > IqsAuditAction.NONE.getId())
				auditActions.add(auditAction);
		}
		iqsAuditActionComboBox.getItems().addAll(auditActions);

		vb.getChildren().addAll(iqsAuditActionComboBox);
		return new TitledPane("IQS Audit Action", vb);
	}
	
	private HBox createIqsBox() {
		HBox hBox = new HBox(5);
		hBox.setAlignment(Pos.TOP_CENTER);
		TitledPane iqsScorePane = createIqsScorePane();
		HBox.setHgrow(iqsScorePane, Priority.ALWAYS);
		if (getController().isIqsAuditActionInput()) {
			TitledPane iqsAuditActionPane = createIqsAuditActionPane();
			HBox.setHgrow(iqsAuditActionPane, Priority.ALWAYS);
			resetIqsScoreSelection();
			hBox.getChildren().addAll(iqsScorePane, iqsAuditActionPane);
		} else {
			resetIqsScoreSelection();
			hBox.getChildren().addAll(iqsScorePane);
		}
		return hBox;
	}
	
	public void resetIqsScoreSelection() {
		if (getController().isIqsInput()) {
			iqsScoreComboBox.getSelectionModel().select(IqsScore.IQS70);
			if (getController().isIqsAuditActionInput()) {
				iqsAuditActionComboBox.getSelectionModel().select(IqsAuditAction.REPAIRED);
			}
		}
	}
	
	/**
	 * This method is used to create DefectStatus Pane
	 * @return TitledPane
	 */
	private TitledPane createDefectStatusPane() {
		String id;
		if(getScreenHeight() >= 1040) {
			id ="radio-btn-medium";
		}
		else if(getScreenHeight() >= 860) {
			id = "radio-btn";
		} else {
			id = "radio-btn-small";
		}
		VBox statusBox = new VBox();
		toggleGroup = new ToggleGroup();
		repairedRadioBtn = createRadioButton(DefectStatus.REPAIRED.getName(), toggleGroup, false, getController());
		notRepairedRadioBtn = createRadioButton(DefectStatus.NOT_REPAIRED.getName(), toggleGroup, false, getController());
		nonRepairableRadioBtn = createRadioButton(DefectStatus.NON_REPAIRABLE.getName(), toggleGroup, false, getController());
		repairedRadioBtn.setId(id);
		notRepairedRadioBtn.setId(id);
		nonRepairableRadioBtn.setId(id);
		statusBox.getChildren().addAll(repairedRadioBtn, notRepairedRadioBtn, nonRepairableRadioBtn);
		repairedRadioBtn.setVisible(false);
		notRepairedRadioBtn.setVisible(false);
		nonRepairableRadioBtn.setVisible(false);
		TitledPane statusPane = new TitledPane("Defect Status", statusBox);
		
		return statusPane;
	}
	
	@Override
	public LoggedRadioButton createRadioButton(String title, ToggleGroup group, boolean isSelected, EventHandler<ActionEvent> handler) {
		LoggedRadioButton radioButton = UiFactory.createRadioButton(title);
		radioButton.setToggleGroup(group);
		radioButton.setSelected(isSelected);
		radioButton.setOnAction(handler);
		return radioButton;
	}
	
	/**
	 * This method is used to clear all selected components.
	 */
	public void clearComponents() {
		defectPanelByImage.setVisible(false);
		defectPanelByText.setVisible(false);
		entryScreenListView.clear();
		part1ListView.getItems().clear();
		locListView.getItems().clear();
		part2ListView.getItems().clear();
		defectListView.getItems().clear();
		writeUpDeptComboBox.getItems().clear();
		siteComboBox.getControl().getItems().clear();
		plantComboBox.getControl().getItems().clear();
		departmentComboBox.getControl().getItems().clear();
		level1ComboBox.getControl().getItems().clear();
		defect1ListView.getItems().clear();
	}
	
	public void clearListSelections()  {
		part1ListView.getSelectionModel().clearSelection();
		locListView.getSelectionModel().clearSelection();
		part2ListView.getSelectionModel().clearSelection();
		defectListView.getSelectionModel().clearSelection();
		defect1ListView.getSelectionModel().clearSelection();		
	}
		
	/**
	 * This method is used to clear components on Tab Select
	 */
	private void clearComponentsOnTabSelect() {
		resetResponsibilityCombobox();
		resetKickoutComboBox();
		getController().setExistingDefectCount();
	}
	
	/**
	 * This method is used to reset responsibility combobox
	 */
	public void resetResponsibilityCombobox() {
		siteComboBox.getControl().getSelectionModel().select(null);
		plantComboBox.getControl().getSelectionModel().select(null);
		departmentComboBox.getControl().getSelectionModel().select(null);
		level1ComboBox.getControl().getSelectionModel().select(null);
		resetKickoutComboBox();
		if(getController().isShowL2L3())  {
			getController().getRespController().clearAll();
		}
	}
	
	public void resetKickoutComboBox() {
		if(getController().isShowKickoutPane()) {
			try {
			EventBusUtil.publish(new ResetEvent(ViewId.DEFECT_ENTRY));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method is used to create image pane
	 * @return
	 */
	private HBox createImagePane() {
		HBox imageBox = new HBox();
		imagePane = new AnchorPane();
		double size = getScreenHeight() * 0.67;
		defectImageView = new ImageView();
		defectImageView.setFitHeight(size);
		defectImageView.setFitWidth(size);
		imagePane.setPrefSize(size, size);
		imagePane.setMaxSize(size, size);
		imagePane.setMinSize(size, size);
		imagePane.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
		imagePane.getChildren().add(defectImageView);
		
		VBox defectView = new VBox();
		defect1ListView = createListViewWithString();
		defect1ListView.setId("defect1ListView");

		defect1TitledPane = new TitledPane("Primary Defect", defect1ListView);
		defect1TitledPane.getStyleClass().add("station-titled-pane");
		defect1TitledPane.setMinHeight(0.875 * size);
		defect1TitledPane.setMaxHeight(0.875 * size);
		
		lastDefectEnteredTextAreaForImage = createDisplayTextArea();
		lastDefectEnteredTextAreaForImage.setMinHeight(0.1 * size);
		lastDefectEnteredTextAreaForImage.setMaxHeight(0.1 * size);
		
		defectView.setSpacing(10);
		defectView.setPadding(new Insets(5));
		defectView.getChildren().addAll(defect1TitledPane,lastDefectEnteredTextAreaForImage);
		
		double factor = 0;
		int screenWidth = (int) getScreenWidth();
		switch(screenWidth) {
		case 1366:
			factor = 0.25;
			break;
		case 1024:
			factor = 0.12;
			break;
		case 1280:
			factor = 0.09;
			break;
		}
		if(factor != 0) {
			double width = getScreenWidth() * factor;
			defect1TitledPane.setMinWidth(width);
			defect1TitledPane.setMaxWidth(width);
			lastDefectEnteredTextAreaForImage.setMinWidth(width);
			lastDefectEnteredTextAreaForImage.setMaxWidth(width);
			defectView.setMinWidth(width);
			defectView.setMaxWidth(width);
		} else {
			defectView.prefWidthProperty().bind(imageBox.widthProperty());
		}
		
		imageBox.getChildren().addAll(imagePane,defectView);
		imageBox.setSpacing(10);
		return imageBox;
	}

	private TextArea createDisplayTextArea() {
		TextArea textArea = new TextArea();
		textArea.setWrapText(true);
		textArea.setEditable(false);
		textArea.setStyle(String.format("-fx-font-size: %dpx; -fx-font-weight: bold;", (int)(0.009 * getScreenWidth())));
		textArea.getStyleClass().add("text-area-station");
		return textArea;
	}
	
	private EventHandler<MouseEvent> getDeselectEventHandler(final ListCell<? extends Object> cell) {
		EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ListView<? extends Object> panel = cell.getListView();
				panel.requestFocus();
                if (!cell.isEmpty()) {
                	if(event.isPrimaryButtonDown())  {
	                    int index = cell.getIndex(); 
	                    if (panel.getSelectionModel().getSelectedIndices().contains(index)) {
	                    	panel.getSelectionModel().clearSelection(index);
	                    } else {
	                    	try {
	                    		if (index < 0 || index >= panel.getItems().size()) {
	                    			panel.getSelectionModel().clearSelection();
	                    		} else {
	                    			panel.getSelectionModel().select(index);
	                    		}
	                    	} catch (IndexOutOfBoundsException e) {
	                    		panel.getSelectionModel().clearSelection();
	                    	}
	                    }
                	}
                    event.consume();
                }
			}
		};
		return handler;
	}
	/**
	 * 
	 * This method is used to create ListView with String
	 * @return
	 */
	private ListView<String> createListViewWithString() {
		final ListView<String> panel = new ListView<String>();
		panel.setCellFactory
		(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> arg0) {
				class MyCell<T> extends ListCell<String> {
				     public MyCell() {    }
				     @Override protected void updateItem(String item, boolean empty)  {
				    	 super.updateItem(item, empty);
				    	 List<String> mostFreqUsed = getMostFreqList();
				    	 if(!StringUtils.isBlank(item)  && isMostFreqUsed() && mostFreqUsed.contains(item))  {
				    		 setStyle(getCellStyleBold());
				    	 }
				    	 else  {
				    		 setStyle(getCellStyleNormal());
				    	 }
				     }
				}
				ListCell<String> cell = new MyCell<String>();
				      
				ContextMenu ctxMnu = new ContextMenu();
				String[] defectStatusList = getController().getAvailableDefectStatusList();
				if(defectStatusList != null && defectStatusList.length > 0)   {
					for(String defectStatus : defectStatusList)  {
						if(defectStatus.equalsIgnoreCase(QiEntryStationDefaultStatus.REPAIRED.getName())){
							MenuItem repaired = new MenuItem(QiEntryStationDefaultStatus.REPAIRED.getName());
							repaired.setOnAction(
									event -> getRepairedRadioBtn().setSelected(true)
							);
							ctxMnu.getItems().add(repaired);
						}
						else if(defectStatus.equalsIgnoreCase(QiEntryStationDefaultStatus.NOT_REPAIRED.getName())){
							MenuItem notRepaired = new MenuItem(QiEntryStationDefaultStatus.NOT_REPAIRED.getName());
							notRepaired.setOnAction(
									event -> getNotRepairedRadioBtn().setSelected(true)
							);
							ctxMnu.getItems().add(notRepaired);
						}
						else if(defectStatus.equalsIgnoreCase(QiConstant.NON_REPAIRABLE)){
							MenuItem nonRepairable = new MenuItem(QiConstant.NON_REPAIRABLE);
							nonRepairable.setOnAction(
									event -> getNonRepairableRadioBtn().setSelected(true)
							);
							ctxMnu.getItems().add(nonRepairable);
						}
					}
				}
				
	            cell.textProperty().bind(cell.itemProperty());
	            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
	                if (isNowEmpty) {
	                    cell.setContextMenu(null);
	                } else {
	                    cell.setContextMenu(ctxMnu);
	                }
	            });
				cell.addEventFilter(MouseEvent.MOUSE_PRESSED, getDeselectEventHandler(cell));
				return cell;
			}
		});
		panel.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.01 * getScreenWidth())));
		return panel;
	}
	
	public  String  getCellStyleBold() {
		return String.format("-fx-font-weight: bold; -fx-font-size: %dpx;", (int)(0.01 * getScreenWidth()));
	}
	public  String  getCellStyleNormal() {
		return String.format("-fx-font-size: %dpx;", (int)(0.01 * getScreenWidth()));
	}
	public String getPrimarySelectedDefect()  {
		String defect = StringUtils.trimToEmpty(getDefect1ListView().getSelectionModel().getSelectedItem());
		return parseDefect(defect, false);
	}
	
	public String getSecondarySelectedDefect()  {
		String defect = StringUtils.trimToEmpty(getDefect1ListView().getSelectionModel().getSelectedItem());
		return parseDefect(defect, true);
	}
	
	public String parseDefect(String defect, boolean isSec)  {
		
		String defect1 = "", defect2 = "";
		
		if(StringUtils.isBlank(defect))  {
			return "";
		}

		String[] defectParts = defect.split("\\s*\\-\\s*");
		if(defectParts == null || defectParts.length < 1)  {
			return "";
		}

		defect1 = StringUtils.trimToEmpty(defectParts[0]);
		if(defectParts.length > 1 )  {
			defect2 = StringUtils.trimToEmpty(defectParts[1]);
		}
		if(isSec)  return defect2;
		else  return defect1;
	}
	
	public double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}
	public double getScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}
	
	public StringProperty getComboBoxStyle() {
		return comboBoxStyle;
	}

	public void setComboBoxStyle(StringProperty comboBoxStyle) {
		this.comboBoxStyle = comboBoxStyle;
	}

	public ObjectProperty<Font> getTextFont() {
		return textFont;
	}

	public void setTextFont(ObjectProperty<Font> textFont) {
		this.textFont = textFont;
	}

	public ObjectProperty<Font> getLabelFont() {
		return labelFont;
	}

	public void setLabelFont(ObjectProperty<Font> labelFont) {
		this.labelFont = labelFont;
	}

	public ObjectProperty<Font> getButtonFont() {
		return buttonFont;
	}

	public void setButtonFont(ObjectProperty<Font> buttonFont) {
		this.buttonFont = buttonFont;
	}

	public StringProperty getRadioButtonStyle() {
		return radioButtonStyle;
	}

	public void setRadioButtonStyle(StringProperty radioButtonStyle) {
		this.radioButtonStyle = radioButtonStyle;
	}

	public StringProperty getTextStyle() {
		return textStyle;
	}

	public void setTextStyle(StringProperty textStyle) {
		this.textStyle = textStyle;
	}
	
	public TileListView<String> getMenuTileListView() {
		return menuTileListView;
	}
	public ListView<String> getPart1ListView() {
		return part1ListView;
	}
	public ListView<String> getLocListView() {
		return locListView;
	}
	public ListView<String> getPart2ListView() {
		return part2ListView;
	}
	public ListView<String> getDefectListView() {
		return defectListView;
	}
	public ListView<QiDefectResultDto> getPartDefectListView() {
		return partDefectListView;
	}
	public ComboBox<String> getSiteComboBox() {
		return siteComboBox.getControl();
	}
	public ComboBox<String> getPlantComboBox() {
		return plantComboBox.getControl();
	}
	public ComboBox<String> getDepartmentComboBox() {
		return departmentComboBox.getControl();
	}
	public ComboBox<KeyValue<String,Integer>> getLevel1ComboBox() {
		return level1ComboBox.getControl();
	}
	public LoggedComboBox<String> getWriteUpDeptComboBox() {
		return writeUpDeptComboBox;
	}
	public LoggedComboBox<IqsScore> getIqsScoreComboBox() {
		return iqsScoreComboBox;
	}
	public LoggedComboBox<IqsAuditAction> getIqsAuditActionComboBox() {
		return iqsAuditActionComboBox;
	}
	public UpperCaseFieldBean getMenuSearchTextField() {
		return menuSearchTextField;
	}
	public AutoCompleteTextField getPartSearchTextField() {
		return partSearchTextField;
	}
	public ToggleGroup getToggleGroup() {
		return toggleGroup;
	}
	public AnchorPane getImagePane() {
		return imagePane;
	}
	public StackPane getDefectStackPane() {
		return defectStackPane;
	}
	public VBox getDefectPanelByText() {
		return defectPanelByText;
	}
	public VBox getdefectPanelByImage() {
		return defectPanelByImage;
	}
	public ImageView getDefectImageView() {
		return defectImageView;
	}
	public List<Polygon> getImageSections() {
		return imageSections;
	}
	public ListView<String> getDefect1ListView() {
		return defect1ListView;
	}
	public TextArea getLastDefectEnteredTextAreaForImage() {
		return lastDefectEnteredTextAreaForImage;
	}
	public TextArea getLastDefectEnteredTextAreaForText() {
		return lastDefectEnteredTextAreaForText;
	}
	public LoggedRadioButton getRepairedRadioBtn() {
		return repairedRadioBtn;
	}
	public LoggedRadioButton getNotRepairedRadioBtn() {
		return notRepairedRadioBtn;
	}
	public LoggedRadioButton getNonRepairableRadioBtn() {
		return nonRepairableRadioBtn;
	}
	public LoggedLabel getEntryModelLabel() {
		return entryModelLabel;
	}
	public TitledPane getPart1TitledPane() {
		return part1TitledPane;
	}
	public TitledPane getLocTitledPane() {
		return locTitledPane;
	}
	public TitledPane getPart2TitledPane() {
		return part2TitledPane;
	}
	public TitledPane getDefectTitledPane() {
		return defectTitledPane;
	}
	public TitledPane getDefect1TitledPane() {
		return defect1TitledPane;
	}
	public LoggedLabel getColorCodeLabel() {
		return colorCodeLabel;
	}
	public LoggedButton getAcceptBtn() {
		return acceptBtn;
	}
	public LoggedButton getExistingDefectBtn() {
		return existingDefectBtn;
	}
	public TileListView<QiDefectEntryDto> getEntryScreenListView() {
		return entryScreenListView;
	}
	public UpperCaseFieldBean getScanTextField() {
		return scannedTextField;
	}
	public LoggedButton getClearPartSearchTxtBtn() {
		return clearPartSearchTxtBtn;
	}
	public boolean getViewStarted() {
		return viewStarted;
	}
	public void setViewStarted(boolean start) {
		viewStarted = start;
	}

	public List<String> getMostFreqList() {
		return mostFreqList;
	}
	
	public ProcessPointSelectionPane getKickoutLocationPane() {
		return this.kickoutLocationPane;
	}

	public void setKickoutLocationPane(ProcessPointSelectionPane kickoutLocationPane) {
		this.kickoutLocationPane = kickoutLocationPane;
	}

	public void setMostFreqList(List<String> newFreqList) {
		if(mostFreqList != null)  {
			mostFreqList.clear();
		}
		else  {
			mostFreqList = new ArrayList<String>();
		}
		if(newFreqList == null || newFreqList.isEmpty())  return;
		for(String defect : newFreqList)  {
			mostFreqList.add(defect);
		}
	}
	
	public void clearFreqList()  {
		if(mostFreqList != null)  {
			mostFreqList.clear();
		}		
	}

	public boolean isMostFreqUsed() {
		return isMostFreqUsed && mostFreqList != null && !mostFreqList.isEmpty();
	}

	public void setMostFreqUsed(boolean isMostFreqUsed) {
		this.isMostFreqUsed = isMostFreqUsed;
	}

	@Override
	public void resetFocus() {
		if (!getScanTextField().isDisabled()) {
			Platform.runLater( new Runnable() {
				@Override
				public void run() {
					getScanTextField().requestFocus();
				}
			});
		}
		
		if(getController().isPlayNgSoundForBadDFScan()) {
			addKeyEventHandler();
		}
	}
	
	public void addKeyEventHandler() {
		if(keyEventHandler != null) {
			removeEventHandler(KeyEvent.KEY_RELEASED, keyEventHandler);
		}
		keyEventHandler = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event != null && KeyCode.ENTER.equals(event.getCode())) {
					if(!isBypass()) {
						getController().getAudioManager().playNGSound();
					}
				}
			}
		};
		addEventHandler(KeyEvent.KEY_RELEASED, keyEventHandler);
	}

	private boolean isBypass() {
		List<String> ids = Arrays.asList(ProductActionId.DIRECTPASS.getActionName(), getScanTextField().getId());
		return getScene().getFocusOwner() == null ? false : ids.stream().anyMatch(item -> item.equals(getScene().getFocusOwner().getId()));
	}
	
	
	public LoggedButton getRecentDefectBtn() {
		return recentDefectBtn;
	}

	public void setRecentDefectBtn(LoggedButton recentDefectBtn) {
		this.recentDefectBtn = recentDefectBtn;
	}
	
	public boolean isScrapedproduct() {
		return isScrapedproduct;
	}

	public void setScrapedproduct(boolean isScrapedproduct) {
		this.isScrapedproduct = isScrapedproduct;
	}

	public String getScrapMessage() {
		return scrapMessage;
	}

	public void setScrapMessage(String scrapMessage) {
		this.scrapMessage = scrapMessage;
	}
	
}
