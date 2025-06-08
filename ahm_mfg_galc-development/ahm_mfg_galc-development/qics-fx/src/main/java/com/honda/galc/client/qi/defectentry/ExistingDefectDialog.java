package com.honda.galc.client.qi.defectentry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.data.ProductSearchResult;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.qi.base.QiFxDialog;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiProgressBar;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.ExistingDefectDto;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiStationPreviousDefect;
import com.honda.galc.util.KeyValue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
/**
 * <h3>ExistingDefectDialog Class description</h3>
 * <p> ExistingDefectDialog description </p>
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
 * Nov 18, 2016
 *
 *
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class ExistingDefectDialog extends QiFxDialog<DefectEntryModel>{

	private ProductModel productModel;
	private ObjectTablePane<ExistingDefectDto> defectResultTablePane;
	private LoggedButton voidAllButton;
	private LoggedButton closeBtn;
	private LoggedButton applyBtn;
	private LabeledComboBox<String> respSite;
	private LabeledComboBox<String> respPlant;
	private LabeledComboBox<String> respDept;
	private LabeledComboBox<KeyValue<String,Integer>> respLevel;
	private ExistingDefectDialogController existingDialogController;
	private TitledPane responsibilityPane ;
	private LoggedButton voidLastButton;
	private LoggedButton changeToFixedButton;
	private LoggedButton uploadImageVideoButton;
	private LoggedButton noProblemFoundButton;
	private ToggleGroup toggleGroup;
	private LoggedRadioButton repairedRadioBtn ;
	private LoggedRadioButton notRepairedRadioBtn ;
	private LoggedRadioButton nonRepairableRadioBtn;

	private TitledPane statusPane ;
	private LoggedButton voidButton;
	private boolean isTrainingModeOn;
	private LoggedButton changeToNotFixedButton;
	private List<String> defectEntryStatusList=null;

	String processPointId = "";
	ResponsibleLevelPanel rPanel = null;
	ResponsibleLevelController respController = null;

	public ExistingDefectDialog(String string, DefectEntryModel model, ProductModel productModel, String applicationId, String ppId,
			String quantity, boolean isTrainingModeOn, boolean isRepairRelated) {
		super("Existing Defects",applicationId, model);
		this.productModel = productModel;
		setProcessPointId(ppId);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.setExistingDialogController(new ExistingDefectDialogController(model,productModel,this,quantity));
		this.isTrainingModeOn = isTrainingModeOn;
		respController = ResponsibleLevelController.getInstance(getModel(), this, getProcessPointId());
		getExistingDialogController().setRespController(respController);
		initComponents();
		reload();
		existingDialogController.initListeners();
		repairedRadioBtn.setDisable(isRepairRelated);
	}

	@Override
	public void close() {
		super.close();
		getExistingDialogController().close();
	}

	private void initComponents() {
		VBox outerPane = new VBox();
		outerPane.setPrefHeight(500);
		HBox buttonContainer = createButtonPane();
		HBox tableContainer = createValidationResultPane();
		createResponsibilityPanel();
		respSite.setDisable(true);
		respPlant.setDisable(true);
		respDept.setDisable(true);
		outerPane.getChildren().addAll(buttonContainer,tableContainer,createResponsibilityPanel());
		getRootBorderPane().setCenter(outerPane);
	}

	/**
	 * This method is used to create DefectStatus Pane
	 * @return TitledPane
	 */
	private TitledPane createDefectStatusPane() {
		VBox statusBox = new VBox();
		toggleGroup = new ToggleGroup();
		repairedRadioBtn = createRadioButton(DefectStatus.REPAIRED.getName(), toggleGroup, false, getExistingDialogController());
		notRepairedRadioBtn = createRadioButton(DefectStatus.NOT_REPAIRED.getName(), toggleGroup, false, getExistingDialogController());
		nonRepairableRadioBtn = createRadioButton(DefectStatus.NON_REPAIRABLE.getName(),toggleGroup, false, getExistingDialogController());
		statusBox.getChildren().addAll(repairedRadioBtn, notRepairedRadioBtn, nonRepairableRadioBtn);
		statusBox.setSpacing(5);
		repairedRadioBtn.setVisible(false);
		notRepairedRadioBtn.setVisible(false);
		nonRepairableRadioBtn.setVisible(false);
		statusPane = new TitledPane("Defect Status", statusBox);
		statusPane.setDisable(true);
		return statusPane;
	}

	/**
	 * This method creates Responsibility Panel
	 */
	private HBox createResponsibilityPanel() {
		HBox changeResponsibleContainer = new HBox();
		HBox outerBox = new HBox();
		if(!respController.isShowL2L3())  {		
			respSite = ResponsibleLevelPanel.createLabeledComboBox("respSite","Resp Site", false, true, true);
			respPlant = ResponsibleLevelPanel.createLabeledComboBox("respPlant","Resp Plant", false, true, true);
			respDept = ResponsibleLevelPanel.createLabeledComboBox("respDept","Resp Dept", false, true, true);
			respSite.getControl().getStyleClass().add("station-respons-combo");
			respPlant.getControl().getStyleClass().add("station-respons-combo");
			respDept.getControl().getStyleClass().add("station-respons-combo");
			respLevel = ResponsibleLevelPanel.createLabeledKVComboBox("respLevel","Resp Level 1", false, true, true);
			respLevel.getControl().getStyleClass().add("station-respons-combo");
			changeResponsibleContainer.getChildren().addAll(respSite, respPlant, respDept,respLevel);
		}
		else  {
			rPanel = ResponsibleLevelPanel.getInstance(this, 0.0d);
			rPanel.setController(respController);
			respController.setResponsiblePanel(rPanel);
			HBox responsibilityHBox = rPanel.createExistingResponsibilityPanel();
			respLevel = rPanel.getResponsibleLevel1ComboBox();
			respSite = rPanel.getSiteComboBox();
			respPlant = rPanel.getPlantComboBox();
			respDept = rPanel.getDepartmentComboBox();
			changeResponsibleContainer.getChildren().addAll(respSite, respPlant, respDept,responsibilityHBox);
		}

		applyBtn = createBtn(QiConstant.APPLY, getExistingDialogController());
		applyBtn.setDisable(true);
		responsibilityPane = new TitledPane("Responsibility", changeResponsibleContainer);
		outerBox.getChildren().addAll(createDefectStatusPane(),responsibilityPane,applyBtn);
		outerBox.setPadding(new Insets(10));
		outerBox.setSpacing(10);
		outerBox.setAlignment(Pos.CENTER);
		return outerBox;
	}

	private BorderPane getRootBorderPane() {
		return (BorderPane) getScene().getRoot();
	}

	/**
	 * This method is used to create a Button HBox and all components inside it.
	 * @return
	 */
	private HBox createButtonPane() {
		HBox buttonContainer = new HBox();
		uploadImageVideoButton = createBtn(QiConstant.UPLOAD_IMAGE_VIDEO, getExistingDialogController());
		uploadImageVideoButton.setDisable(true);
		
		
		voidLastButton = createBtn(QiConstant.VOID_LAST,getExistingDialogController());
		voidAllButton = createBtn(QiConstant.VOID_ALL, getExistingDialogController());

		if (getModel().getCachedDefectResultList().isEmpty()) {
			voidLastButton.setDisable(true);
			voidAllButton.setDisable(true);
		}

		voidButton = createBtn(QiConstant.VOID, getExistingDialogController());
		voidButton.setDisable(true);
		noProblemFoundButton = createBtn(QiConstant.NO_PROBLEM_FOUND, getExistingDialogController());
		changeToFixedButton = createBtn(QiConstant.CHANGE_TO_FIXED,getExistingDialogController());
		changeToNotFixedButton = createBtn(QiConstant.CHANGE_TO_NOT_FIXED,getExistingDialogController());
		closeBtn = createBtn(QiConstant.CLOSE, getExistingDialogController());
		buttonContainer.setAlignment(Pos.CENTER_RIGHT);
		buttonContainer.setPadding(new Insets(5,5,5,5));
		buttonContainer.setSpacing(20);
		buttonContainer.getChildren().addAll(noProblemFoundButton,uploadImageVideoButton, voidLastButton, voidButton, voidAllButton, changeToFixedButton, changeToNotFixedButton, closeBtn);
		return buttonContainer;
	}

	/**
	 * This method is used to create a validationResult HBox and all components inside it.
	 * @return
	 */
	private HBox createValidationResultPane() {
		HBox validationResultTableContainer = new HBox();
		HBox TableBox = new HBox();
		defectResultTablePane = createValidationResultTablePane();
		createSerialNumberColumn();
		TableBox.getChildren().addAll(defectResultTablePane);
		TitledPane responsibilityPane = new TitledPane("Defect Result", TableBox);
		validationResultTableContainer.getChildren().addAll(responsibilityPane);
		return validationResultTableContainer;
	}

	/**
	 * This method is used to create serial number column
	 */
	private void createSerialNumberColumn() {
		LoggedTableColumn<ExistingDefectDto, Integer> column = new LoggedTableColumn<ExistingDefectDto, Integer>();

		createSerialNumber(column);
		defectResultTablePane.getTable().getColumns().add(0, column);
		defectResultTablePane.getTable().getColumns().get(0).setText("#");
		defectResultTablePane.getTable().getColumns().get(0).setResizable(true);
		defectResultTablePane.getTable().getColumns().get(0).setMaxWidth(50);
		defectResultTablePane.getTable().getColumns().get(0).setMinWidth(1);
	}

	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	private ObjectTablePane<ExistingDefectDto> createValidationResultTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Original Defect Status", "qiDefectResult.originalStatus",String.class,StringUtils.EMPTY,false,false)
				.put("Current Defect Status", "qiDefectResult.currentStatus",String.class,StringUtils.EMPTY,false,false)
				.put("Part Defect Comb Desc", "qiDefectResult.PartDefectDesc",String.class,StringUtils.EMPTY,false,false)
				.put("Resp\nSite", "qiDefectResult.responsibleSite",String.class,StringUtils.EMPTY,false,false)
				.put("Resp\nPlant", "qiDefectResult.responsiblePlant",String.class,StringUtils.EMPTY,false,false)
				.put("Resp\nDept","qiDefectResult.responsibleDept",String.class,StringUtils.EMPTY,false,false)
				.put("Resp\nLevel 1","qiDefectResult.responsibleLevel1",String.class,StringUtils.EMPTY,false,false)
				.put("Entry\nDept", "qiDefectResult.entryDept",String.class,StringUtils.EMPTY,false,false)
				.put("Process Point Id", "processPointId", String.class, StringUtils.EMPTY, false, false)
				.put("Actual Timestamp","qiDefectResult.actualTimestampForDisplay",String.class,StringUtils.EMPTY,false,false);

		Double[] columnWidth = new Double[] {0.1,0.1,0.20,0.05,0.05,0.05,0.05,0.05, 0.07, 0.13};
		final ObjectTablePane<ExistingDefectDto> panel = new ObjectTablePane<ExistingDefectDto>(columnMappingList,columnWidth);
		panel.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		return panel;
	}

	/**
	 * This method is used to load Existing Defects Screen.
	 */
	public void reload(){
		List<ExistingDefectDto> finalDefectList = new ArrayList<ExistingDefectDto>();
		List<ExistingDefectDto> dbResult = new ArrayList<ExistingDefectDto>();
		List<QiStationPreviousDefect> previousDefectList = getModel().findAllByProcessPoint();
		List<ExistingDefectDto> configuredDefectList = new ArrayList<ExistingDefectDto>();
		StringBuilder entryDept = new StringBuilder(StringUtils.EMPTY);

		QiProgressBar qiProgressBar = null;
		try{
			qiProgressBar = QiProgressBar.getInstance("Loading Existing Defects Dialog.",
					"Loading Existing Defects Dialog.", getModel().getProductId(),getStage(),true);
			qiProgressBar.showMe();

			Map<String, String> configuredPreviousDefect = new HashMap<String, String>();
			for(QiStationPreviousDefect qiStationPreviousDefect : previousDefectList ){
				configuredPreviousDefect.put(qiStationPreviousDefect.getId().getEntryDivisionId(), qiStationPreviousDefect.getOriginalDefectStatus() + "," + qiStationPreviousDefect.getCurrentDefectStatus());
				if(!StringUtils.isEmpty(entryDept.toString()))
					entryDept.append(",").append("'").append(qiStationPreviousDefect.getId().getEntryDivisionId()).append("'");
				else
					entryDept.append("'").append(qiStationPreviousDefect.getId().getEntryDivisionId()).append("'");
			}
			List<QiDefectResult> defectResults = new  ArrayList<QiDefectResult>();
			if(!StringUtils.isEmpty(entryDept.toString())) {
				if (productModel.isBulkProcess()) {
					List<QiRepairResultDto> repairResults = ProductSearchResult.getDefectsProcessing(); // get common defects
					for (QiRepairResultDto repairResult : repairResults) {
						if (repairResult.getProductId().equals(getModel().getProductId())) { // common defects include duplicates for each product, so only use the defect copies for the current product id
							QiDefectResult defectResult = getModel().findDefectResultById(repairResult.getDefectResultId());
							defectResults.add(defectResult);
						}
					}
				} else {
					defectResults = getModel().findAllByProductIdAndEntryDept(entryDept.toString());
				}
			}
			for(QiDefectResult qiDefectResult : defectResults ){
				for(Map.Entry<String, String>  map:configuredPreviousDefect.entrySet()){
					if(map.getKey().toString().equalsIgnoreCase(qiDefectResult.getEntryDept())){
						String statusString = map.getValue();
						int index = statusString.indexOf(",");
						short originalStatus = new Short(statusString.substring(0, index)).shortValue();
						short currentStatus = new Short(statusString.substring(index + 1)).shortValue();
						if (originalStatus == (short)DefectStatus.ALL.getId() && currentStatus == (short)DefectStatus.ALL.getId()
								|| (originalStatus == (short)DefectStatus.ALL.getId() && currentStatus == qiDefectResult.getCurrentDefectStatus()) 
								|| (currentStatus == (short)DefectStatus.ALL.getId() && originalStatus == qiDefectResult.getOriginalDefectStatus())
								|| (originalStatus == qiDefectResult.getOriginalDefectStatus() && currentStatus == qiDefectResult.getCurrentDefectStatus())) {
							configuredDefectList.add(getModel().createExistingDefectDto(qiDefectResult));
						}
					}
				}
			}
			List<ExistingDefectDto> cachedDefectResultList = new ArrayList<ExistingDefectDto>(getModel().getCacheDefects());

			if(getModel().getProperty().isUpcStation()){
				List<QiDefectResult> qiDefectResults = cachedDefectResultList.stream().map(d -> d.getQiDefectResult()).collect(Collectors.toList());
				List<QiDefectResult> uniqueDefectList = getUniqueDefectListForUpc(qiDefectResults);
				uniqueDefectList.stream().forEach(d -> dbResult.add(getModel().createExistingDefectDto(d)));
				Collections.reverse(dbResult);
			}else{
				if(isTrainingModeOn && !getExistingDialogController().getFinalResultList().isEmpty()){
					List<ExistingDefectDto> defectList = new ArrayList<ExistingDefectDto>();
					for(ExistingDefectDto  defectResult : configuredDefectList){
						if(getExistingDialogController().getFinalResultList().contains(defectResult.getQiDefectResult())){
							defectResult.getQiDefectResult().setCurrentDefectStatus((short)DefectStatus.FIXED.getId());
							defectResult.getQiDefectResult().setUpdateUser(getModel().getUserId());
						}
						defectList.add(defectResult);
					}
					dbResult.addAll(defectList);
				}else{
					dbResult.addAll(configuredDefectList);
				}
				Collections.reverse(cachedDefectResultList);
				finalDefectList.addAll(cachedDefectResultList);
			}

			finalDefectList.addAll(dbResult);
			defectResultTablePane.setData(finalDefectList);
			Logger.getLogger().check("Total no. of defects in Existing Defects screen : "+ finalDefectList.size());
		}
		finally {
			if(qiProgressBar != null)  {
				qiProgressBar.closeMe();
			}
		}
	}


	private List<QiDefectResult> getUniqueDefectListForUpc(List<QiDefectResult> cachedDefectResultList) {
		List<QiDefectResult> dbResult = new ArrayList<QiDefectResult>();

		QiDefectResult tempDefectResult = null ;
		for (int i = 0; i < cachedDefectResultList.size(); i++) {
			QiDefectResult defectResult = cachedDefectResultList.get(i);
			if(!validateDefectresultForDuplicate(defectResult,tempDefectResult))
				dbResult.add(defectResult);
			tempDefectResult = defectResult;
		}

		return dbResult;
	}

	private boolean validateDefectresultForDuplicate(QiDefectResult defectResult,QiDefectResult tempDefectResult) {
		if (tempDefectResult == null)
			return false;
		if (defectResult == null)
			return false;
		QiDefectResult other = (QiDefectResult) defectResult;
		if (!tempDefectResult.getDefectTypeName().equals(other.getDefectTypeName()))
			return false;
		if (!tempDefectResult.getDefectTypeName2().equals(other.getDefectTypeName2()))
			return false;
		if (!tempDefectResult.getImageName().equals(other.getImageName()))
			return false;
		if (!tempDefectResult.getInspectionPart2Location2Name()
				.equals(other.getInspectionPart2Location2Name()))
			return false;
		if (!tempDefectResult.getInspectionPart2LocationName()
				.equals(other.getInspectionPart2LocationName()))
			return false;
		if (!tempDefectResult.getInspectionPart2Name().equals(other.getInspectionPart2Name()))
			return false;
		if (!tempDefectResult.getInspectionPart3Name().equals(other.getInspectionPart3Name()))
			return false;
		if (!tempDefectResult.getInspectionPartLocation2Name()
				.equals(other.getInspectionPartLocation2Name()))
			return false;
		if (!tempDefectResult.getInspectionPartLocationName()
				.equals(other.getInspectionPartLocationName()))
			return false;
		if (!tempDefectResult.getInspectionPartName().equals(other.getInspectionPartName()))
			return false;
		if (tempDefectResult.getPointX() != other.getPointX())
			return false;
		if (tempDefectResult.getPointY() != other.getPointY())
			return false;

		return true;
	}


	public LoggedButton getVoidAllButton() {
		return voidAllButton;
	}

	public void setVoidAllButton(LoggedButton voidAllButton) {
		this.voidAllButton = voidAllButton;
	}

	public LoggedButton getDoneBtn() {
		return closeBtn;
	}

	public void setDoneBtn(LoggedButton doneBtn) {
		this.closeBtn = doneBtn;
	}

	public ComboBox<String> getRespSite() {
		return respSite.getControl();
	}

	public void setRespSite(LabeledComboBox<String> respSite) {
		this.respSite = respSite;
	}

	public ComboBox<String> getRespPlant() {
		return respPlant.getControl();
	}

	public void setRespPlant(LabeledComboBox<String> respPlant) {
		this.respPlant = respPlant;
	}

	public ComboBox<String> getRespDept() {
		return respDept.getControl();
	}

	public void setRespDept(LabeledComboBox<String> respDept) {
		this.respDept = respDept;
	}

	public List<String> getDefectEntryStatusList() {
		return defectEntryStatusList;
	}

	public void setDefectEntryStatusList(List<String> defectEntryStatusList) {
		this.defectEntryStatusList = defectEntryStatusList;
	}

	public ComboBox<KeyValue<String,Integer>> getRespLevel() {
		return respLevel.getControl();
	}

	public void setRespLevel(LabeledComboBox<KeyValue<String,Integer>> respLevel) {
		this.respLevel = respLevel;
	}

	public ExistingDefectDialogController getExistingDialogController() {
		return existingDialogController;
	}

	public void setExistingDialogController(ExistingDefectDialogController voidController) {
		this.existingDialogController = voidController;
	}

	public ObjectTablePane<ExistingDefectDto> getDefectResultTablePane() {
		return defectResultTablePane;
	}

	public void setDefectResultTablePane(
			ObjectTablePane<ExistingDefectDto> defectResultTablePane) {
		this.defectResultTablePane = defectResultTablePane;
	}

	public TitledPane getResponsibilityPane() {
		return responsibilityPane;
	}

	public void setResponsibilityPane(TitledPane responsibilityPane) {
		this.responsibilityPane = responsibilityPane;
	}

	public LoggedButton getApplyBtn() {
		return applyBtn;
	}

	public void setApplyBtn(LoggedButton applyBtn) {
		this.applyBtn = applyBtn;
	}

	public LoggedButton getVoidLastButton() {
		return voidLastButton;
	}

	public void setVoidLastButton(LoggedButton voidLastButton) {
		this.voidLastButton = voidLastButton;
	}

	public LoggedButton getChangeToFixedButton() {
		return changeToFixedButton;
	}

	public void setChangeToFixedButton(LoggedButton changeToFixedButton) {
		this.changeToFixedButton = changeToFixedButton;
	}

	public LoggedRadioButton getRepairedRadioBtn() {
		return repairedRadioBtn;
	}

	public void setRepairedRadioBtn(LoggedRadioButton repairedRadioBtn) {
		this.repairedRadioBtn = repairedRadioBtn;
	}

	public LoggedRadioButton getNotRepairedRadioBtn() {
		return notRepairedRadioBtn;
	}

	public void setNotRepairedRadioBtn(LoggedRadioButton notRepairedRadioBtn) {
		this.notRepairedRadioBtn = notRepairedRadioBtn;
	}

	public LoggedRadioButton getNonRepairableRadioBtn() {
		return nonRepairableRadioBtn;
	}
	public void setNonRepairableRadioBtn(LoggedRadioButton nonRepairableRadioBtn) {
		this.nonRepairableRadioBtn = nonRepairableRadioBtn;
	}

	public LoggedButton getUploadImageVideoButton() {
		return uploadImageVideoButton;
	}

	public LoggedButton getNoProblemFoundButton() {
		return noProblemFoundButton;
	}

	public void setNoProblemFoundButton(LoggedButton noProblemFoundButton) {
		this.noProblemFoundButton = noProblemFoundButton;
	}

	public TitledPane getStatusPane() {
		return statusPane;
	}

	public void setStatusPane(TitledPane statusPane) {
		this.statusPane = statusPane;
	}

	public LoggedButton getVoidButton() {
		return voidButton;
	}

	public void setVoidButton(LoggedButton voidButton) {
		this.voidButton = voidButton;
	}

	public boolean isTrainingModeOn() {
		return isTrainingModeOn;
	}

	public void setTrainingModeOn(boolean isTrainingModeOn) {
		this.isTrainingModeOn = isTrainingModeOn;
	}

	public LoggedButton getChangeToNotFixedButton() {
		return changeToNotFixedButton;
	}

	/**
	 * @return the processPointId
	 */
	public String getProcessPointId() {
		return processPointId;
	}

	/**
	 * @param processPointId the processPointId to set
	 */
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
}
