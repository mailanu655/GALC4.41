package com.honda.galc.client.qi.repairentry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.qi.base.AbstractQiDefectProcessModel;
import com.honda.galc.client.qi.base.AbstractQiProcessView;
import com.honda.galc.client.qi.base.QiFxDialog;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopup;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopupBuilder;
import com.honda.galc.client.ui.keypad.robot.RobotFactory;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiAppliedRepairMethodDto;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.client.utils.QiConstant;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * <h3>AddRepairMethodDialog Class description</h3>
 * <p>
 * AddRepairMethodDialog description
 * </p>
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
 *         Dec 20, 2016
 *
 *
 */
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class AddRepairMethodDialog extends QiFxDialog<RepairEntryModel> {

	private static final double DIALOG_HEIGHT = 0.90;
	private static final double DIALOG_WIDTH = 0.65;
	private ObjectTablePane<QiAppliedRepairMethodDto> historyRepairMethodDataPane;
	private ObjectTablePane<QiAppliedRepairMethodDto> currentRepairMethodDataPane;
	private AddRepairMethodDialogController addRepairMethodController;
	private UpperCaseFieldBean methodFilterTextField;
	
	private Spinner<Integer> spinner;
	private LoggedButton deleteButton;
	private LoggedButton addButton;
	private LoggedRadioButton yesButton;
	private LoggedRadioButton noButton;
	private LoggedButton saveButton;
	
	private LoggedButton returnToRepairScreenBtn;
	private LoggedButton returnToHomeScreenBtn;
	private LoggedTextArea commentArea;
	private QiRepairResultDto qiRepairResultDto;
	private ToggleGroup toggleGroup;
	private HBox commentBox;
	private boolean isCompletelyFixed;
	private KeyBoardPopup popup;
	private List<QiAppliedRepairMethodDto> repairMethodList;
	private TitledPane currentMethodTitledPane;
	private boolean isParentDefectFixed;
	private String defaultRepairMethod;
	private Integer defaultRepairTime;
	private LoggedButton clearMethodFilterTxtBtn;
	private volatile boolean noProblemFound=false;
	private List<QiRepairResultDto> allSelectedDefects; //For bulk Multi-Select Reapirs
	private VBox completelyFixBox;
	private VBox spinnerBox;
	private List<Long> repairIds = new ArrayList<Long>();

	public AddRepairMethodDialog(String title, RepairEntryModel model, QiRepairResultDto qiRepairResultDto,
			String applicationId, boolean isFixedDefect,boolean noProblemFound, List<QiRepairResultDto> allSelectedDefects, Date sessionTimestamp,
			List<Long> newRepairIds, AbstractRepairEntryController repairEntryController) {
		super("Add Repair Method", applicationId, model);
		this.repairIds = newRepairIds;
		if(repairIds == null)  {
			repairIds = new ArrayList<>();
		}
		initInstance(title, model, qiRepairResultDto, isFixedDefect, noProblemFound, allSelectedDefects, sessionTimestamp, repairEntryController);
	}
	
	public AddRepairMethodDialog(String title, RepairEntryModel model, QiRepairResultDto qiRepairResultDto,
			Stage myStage, boolean isFixedDefect,boolean noProblemFound, List<QiRepairResultDto> allSelectedDefects, Date sessionTimestamp,
			AbstractRepairEntryController repairEntryController) {
		super("Add Repair Method", myStage, model);
		initInstance(title, model, qiRepairResultDto, isFixedDefect, noProblemFound, allSelectedDefects, sessionTimestamp, repairEntryController);
	}
	
	public AddRepairMethodDialog(String title, RepairEntryModel model, QiRepairResultDto qiRepairResultDto,
			Stage myStage, boolean isFixedDefect,boolean noProblemFound, List<QiRepairResultDto> allSelectedDefects, Date sessionTimestamp,
			List<Long> newRepairIds, AbstractRepairEntryController repairEntryController) {
		super("Add Repair Method", myStage, model);
		setRepairIds(newRepairIds);
		if(repairIds == null)  {
			repairIds = new ArrayList<>();
		}
		initInstance(title, model, qiRepairResultDto, isFixedDefect, noProblemFound, allSelectedDefects, sessionTimestamp, repairEntryController);
	}
	
	private void initInstance(String title, RepairEntryModel model, QiRepairResultDto qiRepairResultDto,
			boolean isFixedDefect,boolean noProblemFound, List<QiRepairResultDto> allSelectedDefects, Date sessionTimestamp,
			AbstractRepairEntryController<AbstractQiDefectProcessModel, AbstractQiProcessView<?,?>> repairEntryController)  {
		this.noProblemFound  = noProblemFound;
		this.qiRepairResultDto = qiRepairResultDto;
		this.allSelectedDefects = allSelectedDefects;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		initController(model, sessionTimestamp, repairEntryController);
		initComponents();
		loadInitialData();
		
		
		if (isFixedDefect && noProblemFound==false) {
			deleteButton.setVisible(false);
			disableSelectedItem();
			
		} else {
			currentMethodTitledPane.setDisable(false);
		}
	}
	
	private void disableSelectedItem() {
		currentRepairMethodDataPane.getTable().setDisable(true);
		methodFilterTextField.setDisable(true);
		completelyFixBox.setDisable(true);
		spinnerBox.setDisable(true);
		saveButton.setDisable(true);
		commentArea.setDisable(true);
	}
	
	protected void initController(RepairEntryModel model, Date sessionTimestamp,
			AbstractRepairEntryController<AbstractQiDefectProcessModel, AbstractQiProcessView<?,?>> repairEntryController) {
		setAddRepairMethodController(new AddRepairMethodDialogController(model, this, qiRepairResultDto, getAllSelectedDefects(), sessionTimestamp, repairEntryController));		
	}

	private void initComponents() {		
		MigPane tableContainer = createAddRepairMethodDialogPane();
		getRootBorderPane().setCenter(tableContainer);
	}
	
	@Override
	public void close() {
		super.close();
		getAddRepairMethodController().close();
	}	
	
	public void loadInitialData(){
		repairMethodList = getModel().findCurrentMethods();
		currentRepairMethodDataPane.getTable().getItems().clear();
		currentRepairMethodDataPane.setData(repairMethodList);
		initHistoryRepairMethodTable();
		getAddRepairMethodController().initListeners();
		if (!isShowRepairComment()) {
			saveButton.setVisible(false);
			commentBox.setVisible(false);
		} else {
			saveButton.setVisible(true);
            saveButton.setDisable(true);
            commentBox.setVisible(true);
            commentArea.setDisable(true);
		}
		completelyFixBox.setDisable(true);
		spinnerBox.setDisable(true);
		if (null == AbstractRepairEntryView.getParentCachedDefectList() || AbstractRepairEntryView.getParentCachedDefectList().isEmpty())  {
			setDefaultRepairDetails();
		}
	}
	
	public void initHistoryRepairMethodTable() {
		historyRepairMethodDataPane.getTable().getItems().clear();
		if(getRepairIds() != null && !getRepairIds().isEmpty())  {
			getHistoryRepairMethodDataPane().setData(getModel().getAppliedRepairMethodHistoryData(getRepairIds(), getQiRepairResultDto()));
		}
	}
	
	/**
	 * This method will be used to set default attribute repair details.
	 * 
	 */
	private void setDefaultRepairDetails() {
		QiRepairResult qiRepairResult = getModel().findRepairResultById(qiRepairResultDto.getRepairId());
		if(qiRepairResult != null) {
			defaultRepairMethod = qiRepairResult.getRepairMethodNamePlan();
			defaultRepairTime = qiRepairResult.getRepairTimePlan();
			
			if (defaultRepairMethod != null && defaultRepairMethod.trim().length() > 0) {
				ObservableList<QiAppliedRepairMethodDto> itemList = currentRepairMethodDataPane.getTable().getItems();

				for (QiAppliedRepairMethodDto appliedRepairMethodDto : itemList) {
					if (appliedRepairMethodDto.getRepairMethod().equalsIgnoreCase(defaultRepairMethod)) {
						currentRepairMethodDataPane.getTable().getSelectionModel().select(appliedRepairMethodDto);
						int selectedIndex = currentRepairMethodDataPane.getTable().getSelectionModel().getSelectedIndex();
						if (selectedIndex > 5 && selectedIndex < itemList.size() - 1) {
							currentRepairMethodDataPane.getTable().scrollTo(selectedIndex + 1);
						} else {
							currentRepairMethodDataPane.getTable().scrollTo(selectedIndex);
						}
						getSpinner().getValueFactory().setValue(defaultRepairTime);
						break;
					}
				}
			}
		}		
	}
	
	/**
	 * this method returns the value based on which repair comment is either shown or hidden
	 * 
	 * @return
	 */
	public boolean isShowRepairComment() {
		
		boolean isRepairCommentNeedToBeVisible;
		QiStationConfiguration entryStation = getModel().findEntryStationConfigById("Repair Comment");
		if (entryStation != null) {
			isRepairCommentNeedToBeVisible = "Yes".equalsIgnoreCase(entryStation.getPropertyValue()) ? true : false;
		} else {
			isRepairCommentNeedToBeVisible = QiEntryStationConfigurationSettings.REPAIR_COMMMENT.getDefaultPropertyValue()
					.equalsIgnoreCase("Yes") ? true : false;
		}
		
		return isRepairCommentNeedToBeVisible;
	}

	/**
	 * this method creates the Add Repair method entry screen
	 * 
	 * @return
	 */
	private MigPane createAddRepairMethodDialogPane() {		
		historyRepairMethodDataPane = createHistoryRepairMethodDataPane();
		currentRepairMethodDataPane = createCurrentRepairMethodDataPane();
		currentRepairMethodDataPane.setId("currentRepairMethodDataPane");
		
		VBox completelyFixAddBox = new VBox();
		completelyFixBox = createCompletelyFixBox();
		if (isNoProblemFound()) {
			completelyFixBox.setDisable(true);
		}
		
		addButton = createBtn(QiConstant.ADD_REPAIR_METHOD, getAddRepairMethodController());

		addButton.setPrefSize(175, 38);
		addButton.setTranslateX(getDialogWidth() * 0.08);
		addButton.setTranslateY(getDialogHeight() * 0.11);
		
		addButton.setDisable(true);
		
		HBox currentMethodMainBox = new HBox();
		saveButton = createBtn(QiConstant.SAVE_REPAIR_COMMENT, getAddRepairMethodController());
		saveButton.setPrefSize(175, 38);
		saveButton.setTranslateX(getDialogWidth() * 0.08);
		saveButton.setTranslateY(getDialogHeight() * 0.15);
		
		completelyFixAddBox.getChildren().addAll(completelyFixBox, addButton, saveButton);
		currentMethodMainBox.getChildren().addAll(createFilterCurrentRepairMethodBox(), createKeyboardSpinnerCommentBox(), completelyFixAddBox);
		currentMethodTitledPane = new TitledPane("Current Repair Method Entry", currentMethodMainBox);
		
		VBox historyMethodDeleteBox = new VBox();
		historyMethodDeleteBox.setSpacing(5);
		deleteButton = createBtn("Delete Selected Method", getAddRepairMethodController());
		deleteButton.setDisable(true);
		historyMethodDeleteBox.getChildren().addAll(historyRepairMethodDataPane, deleteButton);
		
		TitledPane historyMethodTitledPane = new TitledPane("Problem Repair Method History", historyMethodDeleteBox);
		historyMethodTitledPane.setMinHeight(getDialogHeight() * 0.40);
		historyMethodTitledPane.setMaxHeight(getDialogHeight() * 0.40);
		
		LoggedLabel problemLabel = UiFactory.createLabel("prlId","Working on Problem : " + qiRepairResultDto.getDefectDesc());	
		returnToRepairScreenBtn = createBtn(QiConstant.RETURN_TO_REPAIR_SCREEN, getAddRepairMethodController());
		returnToHomeScreenBtn = createBtn(QiConstant.RETURN_TO_HOME_SCREEN, getAddRepairMethodController());
		HBox returnButtonsBox = new HBox();
		returnButtonsBox.setSpacing(15);
		returnButtonsBox.getChildren().addAll(returnToRepairScreenBtn, returnToHomeScreenBtn);
		returnButtonsBox.setAlignment(Pos.BOTTOM_CENTER);
		
		MigPane repairMethodContainer = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		repairMethodContainer.add(problemLabel,"span");
		repairMethodContainer.add(historyMethodTitledPane,"span");
		repairMethodContainer.add(currentMethodTitledPane,"span");
		repairMethodContainer.add(returnButtonsBox,"span");
		repairMethodContainer.setPrefWidth(getScreenWidth() * DIALOG_WIDTH);
		repairMethodContainer.setPrefHeight(getScreenHeight() * DIALOG_HEIGHT);
		
		return repairMethodContainer;
	}

	/**
	 * @return
	 */
	private VBox createCompletelyFixBox() {
		VBox optionBox = new VBox();
		optionBox.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
		optionBox.setSpacing(getDialogWidth() * 0.05);
		optionBox.setTranslateY(getDialogHeight() * 0.06);
		optionBox.setTranslateX(getDialogWidth() * 0.05);
		optionBox.setMaxWidth(getDialogWidth() * 0.22);
		optionBox.getChildren().addAll(createCompletelyFixLabel(), createCompletelyFixButtonsBox());
		
		return optionBox;
	}

	/**
	 * @return
	 */
	private VBox createFilterCurrentRepairMethodBox() {
		VBox filterCurrentRepairMethodBox = new VBox();
		filterCurrentRepairMethodBox.setSpacing(10);		
		filterCurrentRepairMethodBox.getChildren().addAll(createFilter(), currentRepairMethodDataPane);
		return filterCurrentRepairMethodBox;
	}

	/**
	 * this method creates the label for confirmation
	 * 
	 * @return
	 */
	private LoggedLabel createCompletelyFixLabel() {
		LoggedLabel confLabel = UiFactory.createLabel("confId","Did this repair method completely fix the real problem?");
		confLabel.wrapTextProperty().set(true);
		return confLabel;
	}

	/**
	 * this method creates the radio buttons for confirmation
	 * 
	 * @return
	 */
	private HBox createCompletelyFixButtonsBox() {
		QiStationConfiguration qiEntryStationConfigManagement=getModel().findPropertyKeyValueByProcessPoint(QiEntryStationConfigurationSettings.DEFAULT_REPAIR_METHOD_FIX.getSettingsName());
		boolean selectionValueYes = QiEntryStationConfigurationSettings.DEFAULT_REPAIR_METHOD_FIX.getDefaultPropertyValue().equalsIgnoreCase(QiConstant.YES);
		if(qiEntryStationConfigManagement!=null)
			selectionValueYes = qiEntryStationConfigManagement.getPropertyValue().equalsIgnoreCase(QiConstant.YES);
		toggleGroup = new ToggleGroup();
		yesButton = createRadioButton("Yes", toggleGroup, selectionValueYes, getAddRepairMethodController());
		noButton = createRadioButton("No", toggleGroup, !selectionValueYes, getAddRepairMethodController());
		HBox rButtonBox = new HBox();
		rButtonBox.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
		rButtonBox.getChildren().addAll(yesButton, noButton);
		return rButtonBox;
	}

	/**
	 * @param timeLabel
	 * @return
	 */
	private VBox createSpinnerBox() {
		spinner = new Spinner<Integer>(0, 100000, 0);
		spinner.getStyleClass().add(new String(Spinner.STYLE_CLASS_ARROWS_ON_LEFT_VERTICAL));
		LoggedLabel timeLabel = UiFactory.createLabel("timeLabel", "Time it took to apply method (in minutes)");
		timeLabel.wrapTextProperty().set(true);
		timeLabel.setTranslateX(getDialogWidth() * 0.01);
		
		spinnerBox = new VBox();
		spinnerBox.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
		spinnerBox.setSpacing(getDialogWidth() * 0.05);
		spinnerBox.setTranslateX(getDialogWidth() * 0.10);
		spinnerBox.setMinHeight(getScreenHeight()*0.14);
		spinnerBox.setMaxWidth(getScreenWidth()*0.11);
		spinnerBox.getChildren().addAll(timeLabel, spinner);
		return spinnerBox;
	}

	/**
	 * @return
	 */
	private HBox createCommentBox() {		
		commentArea = UiFactory.createTextArea();
		LoggedLabel commentLabel = UiFactory.createLabel("comment", "Repair Comment");
		commentLabel.setTranslateY(getDialogHeight() * 0.09);
		commentLabel.setTranslateX(getDialogWidth() * 0.01);
		
		commentArea.setMaxSize(getScreenWidth()*0.11, getScreenHeight()*0.12);
		commentArea.setWrapText(true);
		commentArea.setTranslateY(getDialogHeight() * 0.03);
		commentArea.setTranslateX(getDialogWidth() * 0.03);
		commentBox=new HBox();
		commentBox.getChildren().addAll(commentLabel, commentArea);
		return commentBox;
	}

	/**
	 * @return
	 */
	private VBox createKeyboardSpinnerCommentBox() {
		VBox keyboardSpinnerCommentBox = new VBox();
		keyboardSpinnerCommentBox.getChildren().addAll(createKeyboardBtn(), createSpinnerBox(), createCommentBox());			
		return keyboardSpinnerCommentBox;
	}

	/**
	 * this method creates the filter for the methods available
	 * 
	 * @return
	 */
	private HBox createFilter() {
		Label labelFilter = new Label("Repair Method Filter");
		labelFilter.getStyleClass().add("display-label");
		methodFilterTextField = UiFactory.createUpperCaseFieldBean("filter-textfield", (int) (getDialogWidth() * 0.015), Fonts.SS_DIALOG_PLAIN(12),TextFieldState.EDIT, Pos.BASELINE_LEFT);
		methodFilterTextField.setOnAction(getAddRepairMethodController());
		methodFilterTextField.setPromptText("Search Method");
		methodFilterTextField.setFocusTraversable(true);
		clearMethodFilterTxtBtn =UiFactory.createButton(QiConstant.CLEAR_TEXT_SYMBOL, "clearMethodFilterTxtBtn");
		clearMethodFilterTxtBtn.setOnAction(getAddRepairMethodController());
		HBox labelFilterBox = new HBox();
		HBox.setHgrow(methodFilterTextField, Priority.ALWAYS);
		labelFilterBox.setSpacing(10);
		labelFilterBox.getChildren().addAll(labelFilter, methodFilterTextField,clearMethodFilterTxtBtn);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				getMethodFilterTextField().requestFocus();
			}
		});
		return labelFilterBox;
	}

	private BorderPane getRootBorderPane() {
		return (BorderPane) getScene().getRoot();
	}

	/**
	 * this method creates the problem repair method history panel
	 * 
	 * @return
	 */
	private ObjectTablePane<QiAppliedRepairMethodDto> createHistoryRepairMethodDataPane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("#","$rowid")
				.put("Entry Timestamp", "repairTimestamp", Date.class, StringUtils.EMPTY, false, true)
				.put("Associate", "createUser", String.class, StringUtils.EMPTY, false, true)
				.put("Update User", "updateUser", String.class, StringUtils.EMPTY, false, true)
				.put("Repair Method", "repairMethod", String.class, StringUtils.EMPTY, false, true)
				.put("Repair Stn", "applicationId", String.class, StringUtils.EMPTY, false, true)
				.put("Repair Time", "repairTime", Integer.class, StringUtils.EMPTY, false, true)
				.put("Fixed", "fixedStatus", String.class, StringUtils.EMPTY, false, true);
		Double[] columnWidth = new Double[] { 0.03, 0.13, 0.06, 0.05, 0.2, 0.08, 0.1, 0.06 };
		if(isShowRepairComment()){
			columnMappingList.put("Repair Comment", "comment", String.class, StringUtils.EMPTY, false, true);
			columnWidth = new Double[] { 0.03, 0.1, 0.05, 0.05, 0.1, 0.05, 0.1, 0.04, 0.2  };
		}
		ObjectTablePane<QiAppliedRepairMethodDto> panel = new ObjectTablePane<QiAppliedRepairMethodDto>(columnMappingList, columnWidth);
		panel.setMinHeight(getScreenHeight()*0.25);
		
		return panel;
	}

	/**
	 * this method creates the current repair method entry panel
	 * 
	 * @return
	 */
	private ObjectTablePane<QiAppliedRepairMethodDto> createCurrentRepairMethodDataPane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("#","$rowid").put("Repair Method", "repairMethod", String.class, StringUtils.EMPTY, false, true);
		Double[] columnWidth = new Double[] { 0.03,0.2 };
		ObjectTablePane<QiAppliedRepairMethodDto> panel = new ObjectTablePane<QiAppliedRepairMethodDto>(columnMappingList, columnWidth);
		panel.setMinHeight(getScreenHeight() * 0.25);
		panel.setMaxWidth(getDialogWidth() *0.40);
		return panel;
	}

	/**
	 * this method gets the repair methods based on the filter provided
	 * 
	 * @param string
	 */
	public void findFilterData(String string) {
		currentRepairMethodDataPane.setData(getModel().findRepairMethodByFilter(string));
	}
	
	/**
	 * This method creates keyboard button
	 * 
	 * @return LoggedButton
	 */
	private LoggedButton createKeyboardBtn() {
		LoggedButton keyboardBtn = UiFactory.createButton("KEYBOARD", "KEYBOARD");
		keyboardBtn.getStyleClass().add("popup-btn");
		keyboardBtn.setStyle("-fx-font-size: 10pt; -fx-font-weight: bold;");
		keyboardBtn.setTranslateY(-8);
		createKeyBoardPopup();
		keyboardBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(!popup.isVisible())
					setPopupVisible(true);
				else
					setPopupVisible(false);	
			}
		});
		return keyboardBtn;
	}
	
	/**
	 * This method creates pop up to be displayed on click of keyboard button
	 * 
	 */
	private void createKeyBoardPopup() {
		popup = KeyBoardPopupBuilder.create().initLayout("numblock").initScale(1.4).initLocale(Locale.ENGLISH).addIRobot(RobotFactory.createFXRobot()).build();
		popup.getKeyBoard().setOnKeyboardCloseButton(new EventHandler<Event>() {
			public void handle(Event event) {
				setPopupVisible(false);
			}
		});
		popup.getKeyBoard().setPrefSize(620, 140);
		popup.setOwner(this.getScene());
		popup.setHideOnEscape(true);
	}
	
	/**
	 * This method shows/hides keyboard pop up on click of button.
	 * 
	 * @param string
	 */
	private void setPopupVisible(final boolean isVisible) {
		Platform.runLater(new Runnable() {
			private Animation fadeAnimation;
			@Override
			public void run() {
				if (fadeAnimation != null) {
					fadeAnimation.stop();
				}
				if (!isVisible){
					popup.hide();
					return;
				}
				if (popup.isShowing()){
					return;
				}
				popup.getKeyBoard().setOpacity(0.0);

				FadeTransition fade = new FadeTransition(Duration.seconds(.5), popup.getKeyBoard());
				fade.setToValue(isVisible ? 1.0 : 0.0);
				fade.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						fadeAnimation = null;
					}
				});

				ScaleTransition scale = new ScaleTransition(Duration.seconds(.5), popup.getKeyBoard());
				scale.setToX(isVisible ? 1 : 0.8);
				scale.setToY(isVisible ? 1 : 0.8);

				ParallelTransition tx = new ParallelTransition(fade, scale);
				fadeAnimation = tx;
				tx.play();
				if (isVisible) {
					if (!popup.isShowing()) {
						popup.show(popup.getOwner().getWindow());
						popup.getScene().getStylesheets().add(this.getClass().getResource(QiConstant.KEYBOARD_CSS_PATH).toExternalForm());
					}
				}
			}
		});
	}

	public AddRepairMethodDialogController getAddRepairMethodController() {
		return addRepairMethodController;
	}

	public void setAddRepairMethodController(AddRepairMethodDialogController addRepairMethodController) {
		this.addRepairMethodController = addRepairMethodController;
	}

	public ObjectTablePane<QiAppliedRepairMethodDto> getHistoryRepairMethodDataPane() {
		return historyRepairMethodDataPane;
	}

	public void setHistoryRepairMethodDataPane(ObjectTablePane<QiAppliedRepairMethodDto> historyRepairMethodDataPane) {
		this.historyRepairMethodDataPane = historyRepairMethodDataPane;
	}

	public ObjectTablePane<QiAppliedRepairMethodDto> getCurrentRepairMethodDataPane() {
		return currentRepairMethodDataPane;
	}

	public void setCurrentRepairMethodDataPane(ObjectTablePane<QiAppliedRepairMethodDto> currentRepairMethodDataPane) {
		this.currentRepairMethodDataPane = currentRepairMethodDataPane;
	}

	public UpperCaseFieldBean getMethodFilterTextField() {
		return methodFilterTextField;
	}

	public void setMethodFilterTextField(UpperCaseFieldBean methodFilterTextField) {
		this.methodFilterTextField = methodFilterTextField;
	}

	public Spinner<Integer> getSpinner() {
		spinner.setEditable(true);
		return spinner;
	}

	public void setSpinner(Spinner<Integer> spinner) {
		this.spinner = spinner;
	}

	public LoggedButton getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(LoggedButton deleteButton) {
		this.deleteButton = deleteButton;
	}

	public LoggedButton getAddButton() {
		return addButton;
	}

	public void setAddButton(LoggedButton addButton) {
		this.addButton = addButton;
	}

	public LoggedButton getReturnToRepairScreenBtn() {
		return returnToRepairScreenBtn;
	}

	public void setReturnToRepairScreenBtn(LoggedButton returnToRepairScreenBtn) {
		this.returnToRepairScreenBtn = returnToRepairScreenBtn;
	}

	public LoggedTextArea getCommentArea() {
		return commentArea;
	}

	public void setCommentArea(LoggedTextArea commentArea) {
		this.commentArea = commentArea;
	}

	public ToggleGroup getToggleGroup() {
		return toggleGroup;
	}

	public void setToggleGroup(ToggleGroup toggleGroup) {
		this.toggleGroup = toggleGroup;
	}
	
	public double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}
	public double getScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}
	
	public HBox getCommentBox() {
		return commentBox;
	}

	public void setCommentBox(HBox commentBox) {
		this.commentBox = commentBox;
	}

	public boolean isCompletelyFixed() {
		return isCompletelyFixed;
	}

	public void setCompletelyFixed(boolean isCompletelyFixed) {
		this.isCompletelyFixed = isCompletelyFixed;
	}

	public LoggedButton getReturnToHomeScreenBtn() {
		return returnToHomeScreenBtn;
	}

	public TitledPane getCurrentMethodTitledPane() {
		return currentMethodTitledPane;
	}

	public boolean isParentDefectFixed() {
		return isParentDefectFixed;
	}

	public void setParentDefectFixed(boolean isParentDefectFixed) {
		this.isParentDefectFixed = isParentDefectFixed;
	}
	
	public double getDialogWidth() {
		return getScreenWidth() * DIALOG_WIDTH;
	}

	public double getDialogHeight() {
		return getScreenHeight() * DIALOG_HEIGHT;
	}

	public String getDefaultRepairMethod() {
		return defaultRepairMethod;
	}

	public Integer getDefaultRepairTime() {
		return defaultRepairTime;
	}
	
	public LoggedButton getClearMethodFilterTxtBtn() {
		return clearMethodFilterTxtBtn;
	}

	public QiRepairResultDto getQiRepairResultDto() {
		return this.qiRepairResultDto;
	}

	public void setQiRepairResultDto(QiRepairResultDto qiRepairResultDto) {
		this.qiRepairResultDto = qiRepairResultDto;
	}

	public boolean isNoProblemFound() {
		return noProblemFound;
	}

	public void setNoProblemFound(boolean noProblemFound) {
		this.noProblemFound = noProblemFound;
	}

	public List<QiRepairResultDto> getAllSelectedDefects() {
		return allSelectedDefects;
	}

	public void setAllSelectedDefects(List<QiRepairResultDto> allSelectedDefects) {
		this.allSelectedDefects = allSelectedDefects;
	}
	
	public LoggedButton getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(LoggedButton saveButton) {
		this.saveButton = saveButton;
	}
	
	public VBox getCompletelyFixBox() {
		return completelyFixBox;
	}

	public void setCompletelyFixBox(VBox completelyFixBox) {
		this.completelyFixBox = completelyFixBox;
	}

	public VBox getSpinnerBox() {
		return spinnerBox;
	}

	public void setSpinnerBox(VBox spinnerBox) {
		this.spinnerBox = spinnerBox;
	}

	public List<Long> getRepairIds() {
		return repairIds;
	}

	public void setRepairIds(List<Long> repairIds) {
		this.repairIds = repairIds;
	}
	
	public boolean isDirty()  {
		return getAddRepairMethodController().isDirty();
	}
}