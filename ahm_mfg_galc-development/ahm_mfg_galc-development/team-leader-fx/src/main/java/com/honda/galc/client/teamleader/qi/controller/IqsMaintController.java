package com.honda.galc.client.teamleader.qi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.honda.galc.client.teamleader.qi.model.IqsMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.IqsCategoryDialog;
import com.honda.galc.client.teamleader.qi.view.IqsMaintPanel;
import com.honda.galc.client.teamleader.qi.view.IqsQuestionDialog;
import com.honda.galc.client.teamleader.qi.view.IqsValidationResultDialog;
import com.honda.galc.client.teamleader.qi.view.IqsVersionDialog;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.utils.ExcelFileUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiIqsValidationResultDto;
import com.honda.galc.entity.qi.QiIqs;
import com.honda.galc.entity.qi.QiIqsCategory;
import com.honda.galc.entity.qi.QiIqsQuestion;
import com.honda.galc.entity.qi.QiIqsQuestionId;
import com.honda.galc.entity.qi.QiIqsVersion;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.util.AuditLoggerUtil;

public class IqsMaintController extends AbstractQiController<IqsMaintenanceModel, IqsMaintPanel> implements EventHandler<ActionEvent> {
	
	private enum IqsImportColumn {
		
		IQS_VERSION(0),
		IQS_CATEGORY(1),
		IQS_QUES_NUM(2),
		IQS_QUESTION(3);
		
		final int colNum;
	    
	    private IqsImportColumn(int colNum) {
			this.colNum = colNum;
		}
	    public int getColNum() {
	        return colNum;
	    }
	}
	
	private static final String IQS_VERSION = "Version";
	private static final String IQS_CATEGORY = "Category";
	private static final String IQS_QUESTION = "Question";
	private static final String IQS_ASSOCIATION = "IqsAssociation";

	public IqsMaintController(IqsMaintenanceModel model, IqsMaintPanel view) {
		super(model, view);
	}

	/**
	 * This method is added because initEventHandlers() is called before the selection listener is triggered.
	 */
	public void addContextMenuItems(String selectedTable)
	{
		QiIqsVersion selectedIqsVersion =getView().getIqsVersionTablePane().getSelectedItem();
		QiIqsCategory selectedIqsCategory = getView().getIqsCategoryTablePane().getSelectedItem();
		QiIqsQuestion selectedIqsQuestion = getView().getIqsQuestionTablePane().getSelectedItem();
		List<QiIqs> selectedIqs = getView().getIqsAssociationTablePane().getSelectedItems();
		String[] menuItems;
		if(selectedTable!=IQS_ASSOCIATION){
			if(selectedIqsVersion!=null && selectedIqsCategory!=null && selectedIqsQuestion !=null ){
				menuItems = new String[] {
						QiConstant.CREATE, QiConstant.UPDATE, QiConstant.DELETE, QiConstant.ASSOCIATE
				};
			}
			else if(selectedIqsVersion!=null || selectedIqsCategory!=null || selectedIqsQuestion !=null){
				menuItems = new String[] {
						QiConstant.CREATE, QiConstant.UPDATE, QiConstant.DELETE
				};
			}
			else {
				menuItems = new String[] {
						QiConstant.CREATE
				};
			}
		}
		else if(!selectedIqs.isEmpty()) {
			if(getView().getActiveRadioBtn().isSelected()){
				menuItems = new String[] {
						QiConstant.INACTIVATE
				};
				getView().getIqsAssociationTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
			else if(getView().getInactiveRadioBtn().isSelected()){
				menuItems = new String[] {
						QiConstant.REACTIVATE
				};
				getView().getIqsAssociationTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
			else{
				getView().getIqsAssociationTablePane().getTable().getContextMenu().getItems().clear();
				menuItems = new String[] {
				};
			}
		}else{
			getView().getIqsAssociationTablePane().getTable().getContextMenu().getItems().clear();
			menuItems = new String[] {
			};
		}

		if(selectedTable.equalsIgnoreCase(IQS_VERSION))
			getView().getIqsVersionTablePane().createContextMenu(menuItems, this);
		else if(selectedTable.equalsIgnoreCase(IQS_CATEGORY))
			getView().getIqsCategoryTablePane().createContextMenu(menuItems, this);
		else if(selectedTable.equalsIgnoreCase(IQS_QUESTION))
			getView().getIqsQuestionTablePane().createContextMenu(menuItems, this);
		else if(selectedTable.equalsIgnoreCase(IQS_ASSOCIATION))
			getView().getIqsAssociationTablePane().createContextMenu(menuItems, this);

	}

	/**
	 * This is an implemented method of EventHandler interface. Called whenever an ActionEvent is triggered.
	 * Selecting context menu is an ActionEvent. So respective method is called based on which action event is triggered.
	 */
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof MenuItem) {
			QiIqsVersion selectedIqsVersion =getView().getIqsVersionTablePane().getSelectedItem();
			QiIqsCategory selectedIqsCategory = getView().getIqsCategoryTablePane().getSelectedItem();
			QiIqsQuestion selectedIqsQuestion = getView().getIqsQuestionTablePane().getSelectedItem();
			MenuItem menuItem = (MenuItem) actionEvent.getSource();

			if(QiConstant.CREATE.equals(menuItem.getText())){
				if(getView().getIqsVersionTablePane().getTable().isFocused())
					createIqsVersion(actionEvent);
				else if(getView().getIqsCategoryTablePane().getTable().isFocused())
					createIqsCategory(actionEvent);
				else 
					createIqsQuestion(actionEvent);
			}
			else if(QiConstant.UPDATE.equals(menuItem.getText())){
				if(getView().getIqsVersionTablePane().getTable().isFocused())
					updateIqsVersion(actionEvent,selectedIqsVersion);
				else if(getView().getIqsCategoryTablePane().getTable().isFocused())
					updateIqsCategory(actionEvent, selectedIqsCategory);
				else
					updateIqsQuestion(actionEvent, selectedIqsQuestion);
			}
			else if(QiConstant.DELETE.equals(menuItem.getText())){
				if(getView().getIqsVersionTablePane().getTable().isFocused())
					deleteIqsVersion(actionEvent,selectedIqsVersion);
				else if(getView().getIqsCategoryTablePane().getTable().isFocused())
					deleteIqsCategory(actionEvent, selectedIqsCategory);
				else
					deleteIqsQuestion(actionEvent, selectedIqsQuestion);
			}else if(QiConstant.REACTIVATE.equals(menuItem.getText())){
				if(getView().getIqsAssociationTablePane().getTable().isFocused())
					updateIqsAssociationStatus(actionEvent, true);
			}else if(QiConstant.INACTIVATE.equals(menuItem.getText())){
				updateIqsAssociationStatus(actionEvent, false);
			}
			else{
				AssociateIqsData(selectedIqsVersion.getIqsVersion(),selectedIqsCategory.getIqsCategory(),selectedIqsQuestion.getId().getIqsQuestionNo(),selectedIqsQuestion.getId().getIqsQuestion());
			}
		} 
		if(actionEvent.getSource() instanceof LoggedRadioButton){
			getView().reload();
		}
		if(actionEvent.getSource() instanceof LoggedButton) {
			onClickImportBtnAction(actionEvent);
		} 
	}

	@Override
	public void initEventHandlers() {
			addIqsVersionTablePaneListener();
			addIqsCategoryTablePaneListener();
			addIqsQuestionTablePaneListener();
			addIqsTablePaneListener();
	}
	/**
	 * IQS Version Table Pane Listener
	 */
	private void addIqsVersionTablePaneListener() {
		getView().getIqsVersionTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiIqsVersion>() {
			public void changed(ObservableValue<? extends QiIqsVersion> arg0,
					QiIqsVersion arg1, QiIqsVersion arg2) {
				if(!getView().isRefresh()){
				fetchAssociatedData();
				addContextMenu();
				}
			}
		});
		getView().getIqsVersionTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				if(!getView().isRefresh()){
					addContextMenu();
				}
			}
		});

	}
	
	/**
	 * 
	 */
	private void addContextMenu() {
		clearDisplayMessage();
		if(isFullAccess())
		addContextMenuItems(IQS_VERSION);
	}
	
	/**
	 * IQS Category Table Pane Listener
	 */
	private void addIqsCategoryTablePaneListener(){
		getView().getIqsCategoryTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiIqsCategory>() {
			public void changed(ObservableValue<? extends QiIqsCategory> arg0,
					QiIqsCategory arg1, QiIqsCategory arg2) {
				if(!getView().isRefresh()){
				fetchAssociatedData();
				clearDisplayMessage();
				if(isFullAccess())
				addContextMenuItems(IQS_CATEGORY);
				}
			}
		});
		getView().getIqsCategoryTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				if(!getView().isRefresh()){
				clearDisplayMessage();
				if(isFullAccess())
				addContextMenuItems(IQS_CATEGORY);
				}
			}
		});
	}
	/**
	 * IQS Question Table Pane Listener
	 */
	private void addIqsQuestionTablePaneListener(){
		getView().getIqsQuestionTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiIqsQuestion>() {
			public void changed(ObservableValue<? extends QiIqsQuestion> arg0,
					QiIqsQuestion arg1, QiIqsQuestion arg2) {
				if(!getView().isRefresh()){
				fetchAssociatedData();
				clearDisplayMessage();
				if(isFullAccess())
				addContextMenuItems(IQS_QUESTION);
				}
			}
		});
		getView().getIqsQuestionTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				if(!getView().isRefresh()){
				clearDisplayMessage();
				if(isFullAccess())
				addContextMenuItems(IQS_QUESTION);
				}
			}
		});
	}
	/**
	 * IQS IQS Table Pane Listener
	 */
	private void addIqsTablePaneListener(){
		getView().getIqsAssociationTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiIqs>() {
			public void changed(ObservableValue<? extends QiIqs> arg0,
					QiIqs arg1, QiIqs arg2) {
				if(!getView().isRefresh()){
				if(isFullAccess())
				addContextMenuItems(IQS_ASSOCIATION);
				}
			}
		});
		getView().getIqsAssociationTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				if(!getView().isRefresh()){
				clearDisplayMessage();
				if(isFullAccess())
				addContextMenuItems(IQS_ASSOCIATION);
				}
			}
		});
	}
	/**
	 * This method is used to fetch Associated Data
	 */
	private void fetchAssociatedData() {
		QiIqsVersion selectedIqsVersion = getView().getIqsVersionTablePane().getSelectedItem();
		QiIqsCategory selectedIqsCategory = getView().getIqsCategoryTablePane().getSelectedItem();
		QiIqsQuestion selectedQuestion = getView().getIqsQuestionTablePane().getSelectedItem();
		QiIqs qiIqs = new QiIqs();

		if(selectedIqsVersion!=null){
			qiIqs.setIqsVersion(selectedIqsVersion.getIqsVersion());
		}else{
			qiIqs.setIqsVersion(StringUtils.EMPTY);
		}
		if(getView().getIqsCategoryTablePane().getSelectedItem()!=null){
			qiIqs.setIqsCategory(selectedIqsCategory.getIqsCategory());
		}else{
			qiIqs.setIqsCategory(StringUtils.EMPTY);
		}
		if(getView().getIqsQuestionTablePane().getSelectedItem()!=null){
			qiIqs.setIqsQuestionNo(selectedQuestion.getId().getIqsQuestionNo());
			qiIqs.setIqsQuestion(selectedQuestion.getId().getIqsQuestion());
		}else{
			qiIqs.setIqsQuestionNo(0);
		}

		if(qiIqs.getIqsVersion().isEmpty() && qiIqs.getIqsCategory().isEmpty() && qiIqs.getIqsQuestionNo()==0)
			getView().reload();
		else
			getView().reload(qiIqs);
	}

	/**
	 * This method is used to Create IQS Version
	 */
	private void createIqsVersion(ActionEvent actionEvent){
		try{
			IqsVersionDialog dialog = new IqsVersionDialog(QiConstant.CREATE, new QiIqsVersion(), getModel(),getApplicationId());
			dialog.showDialog();
			getView().reload();
		}catch(Exception e){
			handleException("An error occured in createIqsVersion method ", "Failed to open Create IQS Version Pop up screen", e);
		}
	}
	/**
	 * This method is used to Update IQS Version
	 */
	private void updateIqsVersion(ActionEvent actionEvent, QiIqsVersion selectedVersion){
		try{
			IqsVersionDialog dialog = new IqsVersionDialog(QiConstant.UPDATE,selectedVersion, getModel(),getApplicationId());
			dialog.setScreenName(getView().getScreenName());
			dialog.showDialog();
			getView().reload();
		}catch(Exception e){
			handleException("An error occured in updateIqsVersion method ", "Failed to open Update IQS Version Pop up screen", e);
		}
	}
	/**
	 * This method is used to Delete IQS Version
	 */
	private void deleteIqsVersion(ActionEvent actionEvent, QiIqsVersion selectedVersion){
		try{
			List<QiIqs> qiIqsAssociation = getModel().getIqsAssociationForSelectedVersion(selectedVersion.getIqsVersion());
			List<QiPartDefectCombination> totalList = new ArrayList<QiPartDefectCombination>();
			boolean isDeleted=false ;
			if(!qiIqsAssociation.isEmpty()){
				findAllPdcByIqsId(qiIqsAssociation, totalList);
				if(totalList.size()>0){
					 MessageDialog.showError(getView().getStage(),"Deleting IQS Version is not allowed as it is assigned to "+totalList.size()+" Part Defect Combination(s) in Regional Attribute." );
					 return;
				}else{
					isDeleted =  MessageDialog.confirm(getView().getStage(),"Deleting IQS Version will delete its Associations. Do you still want to continue?");
				}
				if(!isDeleted)
					return;
			}
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			if(dialog.showReasonForChangeDialog(null))
			{
				getModel().deleteIqsVersion(selectedVersion);
				//call to prepare and insert audit data
				AuditLoggerUtil.logAuditInfo(selectedVersion, null, dialog.getReasonForChangeText(),getView().getScreenName() ,getUserId());
				for(QiIqs qiIqs : qiIqsAssociation){
					getModel().deleteIqsAssociation(qiIqs.getIqsId());
				}
				updatePartDefectCombination(totalList,dialog.getReasonForChangeText());
				getView().reload();
			}else{
				return;
			}
		}catch(Exception e){
			handleException("An error occured in deleteIqsVersion method ", "Failed to Delete IQS Version", e);
		}
	}

	private void findAllPdcByIqsId(List<QiIqs> qiIqsAssociation,
			List<QiPartDefectCombination> totalList) {
		List<QiPartDefectCombination> pdcCombinationList;
		for(QiIqs qiIqs : qiIqsAssociation){
			pdcCombinationList = getModel().findAllByIqsId(qiIqs.getIqsId());
			totalList.addAll(pdcCombinationList);
		}
	}
	/**
	 * This method is used to Create IQS Category
	 */
	private void createIqsCategory(ActionEvent actionEvent){
		IqsCategoryDialog dialog = new IqsCategoryDialog(QiConstant.CREATE, new QiIqsCategory(), getModel(),getApplicationId());
		dialog.showDialog();
		getView().reload();
	}
	/**
	 * This method is used to Update IQS Category
	 */
	private void updateIqsCategory(ActionEvent actionEvent, QiIqsCategory selectCategory){
		IqsCategoryDialog dialog = new IqsCategoryDialog(QiConstant.UPDATE,selectCategory, getModel(),getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		getView().reload();
	}
	/**
	 * This method is used to Delete IQS Category
	 */
	private void deleteIqsCategory(ActionEvent actionEvent, QiIqsCategory selectCategory){
		List<QiIqs> iqsAssociation = getModel().getIqsAssociationForSelectedCategory(selectCategory.getIqsCategory());
		List<QiPartDefectCombination> totalList = new ArrayList<QiPartDefectCombination>();
		boolean isDeleted =false;
		try{
			if(!iqsAssociation.isEmpty()){
				findAllPdcByIqsId(iqsAssociation, totalList);
				if(totalList.size()>0){
					MessageDialog.showError(getView().getStage(),"Deleting IQS Category is not allowed as it is assigned to "+totalList.size()+" Part Defect Combination(s) in Regional Attribute." );
					return;
				}else{
					isDeleted =  MessageDialog.confirm(getView().getStage(),"Deleting IQS Category will delete its Associations. Do you still want to continue?");
				}
				if(!isDeleted)
					return;
			}
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			if(dialog.showReasonForChangeDialog(null))
			{
				getModel().deleteIqsCategory(selectCategory);
				//call to prepare and insert audit data
				AuditLoggerUtil.logAuditInfo(selectCategory, null, dialog.getReasonForChangeText(),getView().getScreenName(),getUserId() );
				for(QiIqs qiIqs : iqsAssociation){
					getModel().deleteIqsAssociation(qiIqs.getIqsId());
				}
				updatePartDefectCombination(totalList,dialog.getReasonForChangeText());
				getView().reload();
			}else{
				return;
			}
		}catch(Exception e){
			handleException("An error occured in deleteIqsCategory method ", "Failed to Delete IQS Category", e);
		}
	}
	/**
	 * This method is used to Create IQS Question
	 */
	private void createIqsQuestion(ActionEvent actionEvent){
		IqsQuestionDialog dialog = new IqsQuestionDialog(QiConstant.CREATE, new QiIqsQuestion(), getModel(),getApplicationId());
		dialog.showDialog();
		getView().reload();
	}
	/**
	 * This method is used to Update IQS Question
	 */
	private void updateIqsQuestion(ActionEvent actionEvent, QiIqsQuestion selectedQuestion){
		IqsQuestionDialog dialog = new IqsQuestionDialog(QiConstant.UPDATE,selectedQuestion, getModel(),getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		getView().reload();
	}
	/**
	 * This method is used to Delete IQS Question
	 */
	private void deleteIqsQuestion(ActionEvent actionEvent, QiIqsQuestion selectedQuestion){
		List<QiIqs> iqsAssociation = getModel().getIqsAssociationForSelectedQuestionNo(selectedQuestion.getId().getIqsQuestionNo(),selectedQuestion.getId().getIqsQuestion());
		List<QiPartDefectCombination> totalList = new ArrayList<QiPartDefectCombination>();
		boolean isDeleted=false ;
		try{
			if(!iqsAssociation.isEmpty()){
				findAllPdcByIqsId(iqsAssociation, totalList);
				if(totalList.size()>0){
					
					MessageDialog.showError(getView().getStage(),"Deleting IQS Question is not allowed as it is assigned to "+totalList.size()+" Part Defect Combination(s) in regional attribute." );
					return;
				}else{
					isDeleted =  MessageDialog.confirm(getView().getStage(),"Deleting IQS Question will delete its Associations. Do you still want to continue?");
				}
				if(!isDeleted)
					return;
			}
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			if(dialog.showReasonForChangeDialog(null))
			{
				getModel().deleteIqsQuestion(selectedQuestion);
				//call to prepare and insert audit data
				AuditLoggerUtil.logAuditInfo(selectedQuestion, null, dialog.getReasonForChangeText(),getView().getScreenName() ,getUserId());
				for(QiIqs qiIqs : iqsAssociation){
					getModel().deleteIqsAssociation(qiIqs.getIqsId());
				}
				updatePartDefectCombination(totalList,dialog.getReasonForChangeText());
				getView().reload();
			}else{
				return;
			}
		}catch(Exception e){
			handleException("An error occured in deleteIqsQuestion method ", "Failed to Delete IQS Question", e);
		}
	}

	@Override
	public void addContextMenuItems() {

	}

	/**
	 * This method is used to Associate IQS Data
	 */
	private void AssociateIqsData(String iqsVersion, String iqsCategory, Integer iqsQuestionNo, String iqsQuestion){
		QiIqs qiIqs = new QiIqs();
		qiIqs.setIqsVersion(iqsVersion);
		qiIqs.setIqsCategory(iqsCategory);
		qiIqs.setIqsQuestionNo(iqsQuestionNo);
		qiIqs.setIqsQuestion(iqsQuestion);
		qiIqs.setActive(true);
		try{
			if(getModel().isIqsAssociationExists(qiIqs)){
				displayErrorMessage("Association for selected combination already Exists. Please select another one.");
				return;
			}
			getModel().AssociateIqsData(qiIqs);
			getView().reload();
		}catch (Exception e) {
			handleException("An error occured in AssociateIqsData method " , "Failed to Associate Iqs Data ", e);
		}
	}
	/**
	 * This method is used to update IQS Association Status
	 */
	private void updateIqsAssociationStatus(ActionEvent actionEvent, boolean isActive){
		clearDisplayMessage();
		try{
			List<QiIqs> qiIqsList = getView().getIqsAssociationTablePane().getSelectedItems();
			List<QiPartDefectCombination> pdcList = new ArrayList<QiPartDefectCombination>();
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			List<QiPartDefectCombination> pdcCombinationList = new ArrayList<QiPartDefectCombination>();
			for(QiIqs qiIqs : qiIqsList){
				pdcCombinationList = getModel().findAllByIqsId(qiIqs.getIqsId());
				pdcList.addAll(pdcCombinationList);
			}
			boolean isDeleted=false ;
			if(pdcList.size()>0 && !isActive){
				MessageDialog.showError("Inactivating IQS Association is not allowed as it is assigned to "+pdcList.size()+" Part Defect Combination in regional attribute.");
				if(!isDeleted)
					return;
			}
			if(dialog.showReasonForChangeDialog(null))
			{
				try{
					for(QiIqs iqs : qiIqsList){
						getModel().updateIqsAssociationStatus(iqs.getIqsId(),isActive? (short) 1:(short) 0);
						QiIqs qiIqsCloned = (QiIqs) iqs.deepCopy();
						iqs.setActive(isActive);
						iqs.setActiveValue(isActive? (short) 1:(short) 0);
						
						//call to prepare and insert audit data
						AuditLoggerUtil.logAuditInfo(qiIqsCloned,iqs, dialog.getReasonForChangeTextArea().getText(),getView().getScreenName() ,getUserId());
					}
					if(!isActive)
						updatePartDefectCombination(pdcList,dialog.getReasonForChangeText());
						getView().reload();
				}
				catch (Exception e) {
					handleException("An error occured in updateIqsAssociationStatus method " , "Failed to update Iqs Association Status ", e);
				}
			}
			else
				return;

		}catch (Exception e) {
			handleException("An error occurred in updateIqsAssociationStatus method ", "Failed to open inactivate Iqs popup ", e);
		}
	}

	/**
	 * This method is called when user clicks on import button
	 */
	private void onClickImportBtnAction(ActionEvent actionEvent){
		clearDisplayMessage();
		File file = ExcelFileUtil.loadExcelFile();
		if(file!=null){
			String fileName = file.getName();
			List<Object[]> inputDataList = readInputFile(file);
			List<QiIqsValidationResultDto> dtoList = new ArrayList<QiIqsValidationResultDto>();
			List<String> iqsVersionList = new ArrayList<String>();
			List<String> iqsCategoryList = new ArrayList<String>();
			List<QiIqsQuestion> iqsQList = new ArrayList<QiIqsQuestion>();
			Integer validCount = 0;
			Integer duplicateCount = 0;
			if(!inputDataList.isEmpty()){
				try{
					for(Object[] object :inputDataList){
						QiIqsValidationResultDto iqsDto = new QiIqsValidationResultDto();
						QiIqs iqsAssociation = new QiIqs();
						setValueInIqsDto(object, iqsDto);
						Pattern regex = Pattern.compile(QiConstant.MATCH_PATTERN_REGEX);
						Matcher matcherVersion = regex.matcher(StringUtils.trim(object[IqsImportColumn.IQS_VERSION.getColNum()].toString()));
						Matcher matcherQuestionNo = regex.matcher(StringUtils.trim(object[IqsImportColumn.IQS_QUES_NUM.getColNum()].toString()));
						/** Check whether any of the column is empty */
						if(StringUtils.trim(object[IqsImportColumn.IQS_VERSION.getColNum()].toString()).isEmpty() || 
								StringUtils.trim(object[IqsImportColumn.IQS_CATEGORY.getColNum()].toString()).isEmpty() || 
								StringUtils.trim(object[IqsImportColumn.IQS_QUES_NUM.getColNum()].toString()).isEmpty() || 
								StringUtils.trim(object[IqsImportColumn.IQS_QUESTION.getColNum()].toString()).isEmpty()){
							iqsDto.setComments(QiConstant.INVALID+"-EMPTY FIELD");
						}
						/** Check if entries in the excel has any special character */
						else if(!matcherVersion.find() ||  !matcherQuestionNo.find()){
							iqsDto.setComments(QiConstant.INVALID+"-SPECIAL CHARACTER");
						}
						/** Check if each column does not violate length restriction */
						else if(object[IqsImportColumn.IQS_VERSION.getColNum()].toString().length()>16 || 
								object[IqsImportColumn.IQS_CATEGORY.getColNum()].toString().length()>32 || 
								object[IqsImportColumn.IQS_QUES_NUM.getColNum()].toString().length()>10 || 
								object[IqsImportColumn.IQS_QUESTION.getColNum()].toString().length()>256){
							iqsDto.setComments(QiConstant.INVALID+"-TEXT LENGTH");
						}
						/** Check if Question number is integer value */
						else if(!object[IqsImportColumn.IQS_QUES_NUM.getColNum()].toString().matches("\\d+") || 
								Long.valueOf(object[IqsImportColumn.IQS_QUES_NUM.getColNum()].toString()) > Integer.MAX_VALUE || 
								Long.valueOf(object[IqsImportColumn.IQS_QUES_NUM.getColNum()].toString())==0){
							iqsDto.setComments(QiConstant.INVALID+"-ONLY POSITIVE INTEGER DATA TYPE ALLOWED FOR QUESTION NUMBER");
						}
						/** Check if given association is present in DB */
						else if(isIqsAssociationsExist(object,iqsAssociation)){
							iqsDto.setComments(QiConstant.INVALID+"-EXISTS IN DB");
						}
						else{
							boolean isDuplicate= false;
							if(iqsQList.size()>0)
							for(QiIqsQuestion ques : iqsQList){
								/** Check if entries in the excel are duplicate */
								if(ques.getId().getIqsQuestion().equalsIgnoreCase(object[IqsImportColumn.IQS_QUESTION.getColNum()].toString()) 
										&& (ques.getId().getIqsQuestionNo()==(Integer.parseInt(object[IqsImportColumn.IQS_QUES_NUM.getColNum()].toString())))
										&& iqsVersionList.contains(object[IqsImportColumn.IQS_VERSION.getColNum()].toString()) 
										&& iqsCategoryList.contains(object[IqsImportColumn.IQS_CATEGORY.getColNum()].toString())){
									iqsDto.setComments(QiConstant.DUPLICATE);
									duplicateCount++;
									isDuplicate= true;
								}
							}
							addIqsList(iqsVersionList, iqsCategoryList, iqsQList,object);
							if(!isDuplicate){
								iqsDto.setComments(QiConstant.VALID);
								validCount++;
							}
						}
						dtoList.add(iqsDto);
					}
					IqsValidationResultDialog dialog = new IqsValidationResultDialog("Validation Result", dtoList, validCount, duplicateCount,fileName, getModel(),getApplicationId());
					dialog.showDialog();
					getView().reload();
				}catch(Exception e){
					handleException("An error occurred in onClickImportBtnAction method ", "Failed to open validation result popup screen ", e);
				}
			}
		}
	}

	/**
	 * This method is used to add Iqs Version/Category/Question to check the validations.
	 * @param iqsVersionList
	 * @param iqsCategoryList
	 * @param iqsQList
	 * @param object
	 */
	private void addIqsList(List<String> iqsVersionList,
			List<String> iqsCategoryList, List<QiIqsQuestion> iqsQList,
			Object[] object) {
		iqsVersionList.add(object[IqsImportColumn.IQS_VERSION.getColNum()].toString());
		iqsCategoryList.add(object[IqsImportColumn.IQS_CATEGORY.getColNum()].toString());
		QiIqsQuestion iqsQuestion = new QiIqsQuestion();
		QiIqsQuestionId iqsQuestionId = new QiIqsQuestionId();
		iqsQuestion.setId(iqsQuestionId);
		iqsQuestion.getId().setIqsQuestionNo(Integer.parseInt(object[IqsImportColumn.IQS_QUES_NUM.getColNum()].toString()));
		iqsQuestion.getId().setIqsQuestion(object[IqsImportColumn.IQS_QUESTION.getColNum()].toString());
		iqsQList.add(iqsQuestion);
	}
	/**
	 * This method is used to set value in iqs dto
	 */
	private void setValueInIqsDto(Object[] object, QiIqsValidationResultDto iqsDto) {
		iqsDto.setIqsVersion(object[IqsImportColumn.IQS_VERSION.getColNum()].toString());
		iqsDto.setIqsCategory(object[IqsImportColumn.IQS_CATEGORY.getColNum()].toString());
		iqsDto.setIqsQuestionNo(object[IqsImportColumn.IQS_QUES_NUM.getColNum()].toString());
		iqsDto.setIqsQuestion(object[IqsImportColumn.IQS_QUESTION.getColNum()].toString());
	}

	/**
	 * This method is used to read data from excel
	 * @param file
	 */
	private List<Object[]> readInputFile(File file) {
		FileInputStream fstream = null;
		List<Object[]> sheetData = new ArrayList<Object[]>();
		Workbook workbook ;
		try {
			fstream = new FileInputStream(file);
			if(file.getName().contains(".xlsx")){
				workbook = new XSSFWorkbook(fstream);
			}else{
				workbook = new HSSFWorkbook(fstream);
			}
			ExcelFileUtil.readDataFromExcelFile(workbook, sheetData);

		} catch (Exception e) {
			handleException("An error occurred in readInputFile method ", "Failed to read data ", e);
		}
		return sheetData;
	}
	/**
	 * Update QiPartDefectCombination due to deletion of IQS Association
	 * @param totalList
	 * @return
	 */
	private void updatePartDefectCombination(List<QiPartDefectCombination> totalList,String reasonForChange) {
		List<QiPartDefectCombination> pdcList = new ArrayList<QiPartDefectCombination>();
		for(QiPartDefectCombination qiPartDefectCombination : totalList){
			qiPartDefectCombination.setIqsId(null);
			pdcList.add(qiPartDefectCombination);
			
		}
		getModel().updatePartDefectCombination(pdcList);
	}
	/**
	 * check if Iqs Association exists
	 * @param object
	 * @param iqs
	 * @return
	 */
	private boolean isIqsAssociationsExist(Object[] object, QiIqs iqs){
		iqs.setIqsVersion(object[IqsImportColumn.IQS_VERSION.getColNum()].toString());
		iqs.setIqsCategory(object[IqsImportColumn.IQS_CATEGORY.getColNum()].toString());
		iqs.setIqsQuestionNo(Integer.parseInt(object[IqsImportColumn.IQS_QUES_NUM.getColNum()].toString()));
		iqs.setActive(true);
		return getModel().isIqsAssociationExists(iqs);
	}

}
