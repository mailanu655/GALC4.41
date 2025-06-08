package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.ReasonForChangeMaintModel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeMaintDialog;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeMaintPanel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.qi.QiReasonForChangeCategory;
import com.honda.galc.entity.qi.QiReasonForChangeDetail;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

/**
 * 
 * <h3>ReasonForChangeMaintController Class description</h3>
 * <p>
 * ReasonForChangeMaintController description
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
 * @author Justin Jiang<br>
 *         November 3, 2020
 *
 */

public class ReasonForChangeMaintController extends AbstractQiController<ReasonForChangeMaintModel, ReasonForChangeMaintPanel> implements EventHandler<ActionEvent>{
	
	private String selectedPlantName;
	private Division selectedDept;
	private QiReasonForChangeCategory selectedCategory;
	private QiReasonForChangeDetail selectedDetail;
	
	public ReasonForChangeMaintController(ReasonForChangeMaintModel model, ReasonForChangeMaintPanel view) {
		super(model, view);
	}

	public void handle(ActionEvent actionEvent) {
		clearDisplayMessage();
		if (actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			String menuItemText = menuItem.getText();
			if (QiConstant.CREATE_CATEGORY.equals(menuItemText)) 
				createCategory(actionEvent);
			else if (QiConstant.UPDATE_CATEGORY.equals(menuItemText))
				updateCategory(actionEvent);
			else if (QiConstant.DELETE_CATEGORY.equals(menuItemText))
				deleteCategory(actionEvent);
			else if (QiConstant.CREATE_DETAIL.equals(menuItemText))
				createDetail(actionEvent);
			else if (QiConstant.UPDATE_DETAIL.equals(menuItemText))
				updateDetail(actionEvent);
			else if (QiConstant.DELETE_DETAIL.equals(menuItemText))
				deleteDetail(actionEvent);
		}
	}

	@Override
	public void initEventHandlers() {
		addPlantComboboxListener();
		addDeptTableListener();
		addCategoryTableListener();
		addDetailTableListener();
		
		if(getView().getPlantCombobox().getItems().size() == 1)	{
			getView().getPlantCombobox().getSelectionModel().select(0);
		}
		
		if(getView().getDeptTablePane().getTable().getItems().size() == 1)	{
			getView().getDeptTablePane().getTable().getSelectionModel().select(0);
		}
		
		if(getView().getCategoryTablePane().getTable().getItems().size() == 1)	{
			getView().getCategoryTablePane().getTable().getSelectionModel().select(0);
		}
		
		if(getView().getDetailTablePane().getTable().getItems().size() == 1)	{
			getView().getDetailTablePane().getTable().getSelectionModel().select(0);
		}
	}

	@SuppressWarnings("unchecked")
	private void addPlantComboboxListener() {
		getView().getPlantCombobox().valueProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, 
					String oldValue, String newValue) {
				
				clearDisplayMessage();

				getView().getDeptTablePane().getTable().getItems().clear();
				getView().getCategoryTablePane().getTable().getItems().clear();
				getView().getDetailTablePane().getTable().getItems().clear();
				
				if (getView().getCategoryTablePane().getTable().getContextMenu() != null) {
					getView().getCategoryTablePane().getTable().getContextMenu().getItems().clear();
				}
				
				if (getView().getDetailTablePane().getTable().getContextMenu() != null) {
					getView().getDetailTablePane().getTable().getContextMenu().getItems().clear();
				}
				
				selectedPlantName = newValue;
				
				if (!StringUtils.isBlank(selectedPlantName)) {
					refreshDept(selectedPlantName);
				}
			}
		});		
	}

	private void addDeptTableListener() {	
		getView().getDeptTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Division>() {
			public void changed(
					ObservableValue<? extends Division> arg0,
					Division arg1,
					Division arg2) {
				
				clearDisplayMessage();
					
				if (getView().getCategoryTablePane().getTable().getContextMenu() != null) {
					getView().getCategoryTablePane().getTable().getContextMenu().getItems().clear();
				}
					
				if (getView().getDetailTablePane().getTable().getContextMenu() != null) {
					getView().getDetailTablePane().getTable().getContextMenu().getItems().clear();
				}

				getView().getCategoryTablePane().getTable().getItems().clear();
				getView().getDetailTablePane().getTable().getItems().clear();
				
				selectedDept = getView().getDeptTablePane().getSelectedItem();
				if (selectedDept != null) {
					refreshCategory(selectedDept, null);
					updateContextMenuItemsForCategoryTable();
				}
			}
		});
	}

	private void addCategoryTableListener() {
		getView().getCategoryTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiReasonForChangeCategory>() {
			public void changed(
					ObservableValue<? extends QiReasonForChangeCategory> arg0,
					QiReasonForChangeCategory arg1, QiReasonForChangeCategory arg2) {
				
				clearDisplayMessage();
				
				if (getView().getCategoryTablePane().getTable().getContextMenu() != null) {
					getView().getCategoryTablePane().getTable().getContextMenu().getItems().clear();
				}
				
				if (getView().getDetailTablePane().getTable().getContextMenu() != null) {
					getView().getDetailTablePane().getTable().getContextMenu().getItems().clear();
				}
				
				getView().getDetailTablePane().getTable().getItems().clear();
				
				updateContextMenuItemsForCategoryTable();
				
				selectedCategory = getView().getCategoryTablePane().getSelectedItem();
				
				if (selectedCategory != null) {
					refreshDetail(selectedCategory.getCategoryId(), null);
					updateContextMenuItemsForDetailTable();
				}
			}
		});
	}



	private void addDetailTableListener() {
		getView().getDetailTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiReasonForChangeDetail>() {
			public void changed (
					ObservableValue<? extends QiReasonForChangeDetail> arg0,
					QiReasonForChangeDetail arg1, QiReasonForChangeDetail arg2) {
				
				clearDisplayMessage();
				
				getView().getDetailTablePane().getTable().getContextMenu().getItems().clear();

				updateContextMenuItemsForDetailTable();
			}
		});
	}

	@Override
	public void addContextMenuItems() {
	}
	
	public void updateContextMenuItemsForCategoryTable() {
		List<String> menuItemsList = new ArrayList<String>();
		selectedCategory = getView().getCategoryTablePane().getSelectedItem();
		menuItemsList.add(QiConstant.CREATE_CATEGORY);
		
		if(selectedDept !=null && selectedCategory != null){
			menuItemsList.add(QiConstant.UPDATE_CATEGORY);
			menuItemsList.add(QiConstant.DELETE_CATEGORY);
		}
		getView().getCategoryTablePane().createContextMenu((String[]) menuItemsList.toArray(new String[menuItemsList.size()]), this);
	}

	private void updateContextMenuItemsForDetailTable() {
		List<String> menuItemsList = new ArrayList<String>();
		selectedDetail = getView().getDetailTablePane().getSelectedItem();
		menuItemsList.add(QiConstant.CREATE_DETAIL);
		
		if (selectedDept != null && selectedCategory != null && selectedDetail != null) {
			menuItemsList.add(QiConstant.UPDATE_DETAIL);
			menuItemsList.add(QiConstant.DELETE_DETAIL);	
		} 
		getView().getDetailTablePane().createContextMenu((String[]) menuItemsList.toArray(new String[menuItemsList.size()]), this);
	}

	private void createCategory(ActionEvent event){
		ReasonForChangeMaintDialog dialog = new ReasonForChangeMaintDialog(getApplicationId());
		if (!dialog.showReasonForChangeMaintDialog("Create Category", "Category", false)) {
			//clicked Cancel button
			return;
		}
		String site = getModel().getSiteName();
		String plant = getView().getPlantCombobox().getSelectionModel().getSelectedItem().toString();
		String dept = getView().getDeptTablePane().getSelectedItem().getDivisionId();
		String category = dialog.getText();
		if (!getModel().isCategoryExisted(site, plant, dept, category)) {
			selectedCategory = getModel().createCategory(site, plant, dept, category, getUserId());
			int index = refreshCategory(selectedDept, selectedCategory);
			getView().getCategoryTablePane().getTable().getSelectionModel().select(index);
		} else {
			MessageDialog.showError("New Category can't be the same as an existing one!");
		}
	}

	private void updateCategory(ActionEvent event){
		ReasonForChangeMaintDialog dialog = new ReasonForChangeMaintDialog(getApplicationId());
		if (!dialog.showReasonForChangeMaintDialog("Update Category", "Category", true)) {
			return;
		}
		if (!getModel().isCategoryExisted(selectedCategory.getSite(), selectedCategory.getPlant(), selectedCategory.getDepartment(), dialog.getText())) {
			getModel().updateCategory(selectedCategory.getCategoryId(), dialog.getText(), getUserId());
			
			QiReasonForChangeCategory updatedCategory = (QiReasonForChangeCategory)selectedCategory.deepCopy();
			updatedCategory.setCategory(dialog.getText());
			AuditLoggerUtil.logAuditInfo(selectedCategory, updatedCategory, "Updated by user", getView().getScreenName(), getUserId());
			
			int index = refreshCategory(selectedDept, selectedCategory);
			getView().getCategoryTablePane().getTable().getSelectionModel().select(index);
		} else {
			MessageDialog.showError("Updated Category can't be the same as an existing one!");
		}
	}
	
	private void deleteCategory(ActionEvent event){
		boolean deleteCategory = MessageDialog.confirm(getView().getStage(), 
				"Are you sure you want to delete the category with all its details?");

		if (!deleteCategory) {
			return;
		} else {
			getModel().deleteCategory(selectedCategory);
			AuditLoggerUtil.logAuditInfo(selectedCategory, null, "Deleted by user", getView().getScreenName(), getUserId());
			selectedCategory = null;
			refreshCategory(selectedDept, null);
		}
	}

	private void createDetail(ActionEvent event){
		ReasonForChangeMaintDialog dialog = new ReasonForChangeMaintDialog(getApplicationId());
		if (!dialog.showReasonForChangeMaintDialog("Create Detail", "Detail", false)) {
			return;
		}
		
		if (!getModel().isDetailExisted(selectedCategory.getCategoryId(), dialog.getText())) {
			selectedDetail = getModel().createDetail(selectedCategory.getCategoryId(), dialog.getText(), getUserId());
			int index = refreshDetail(selectedCategory.getCategoryId(), selectedDetail);
			getView().getDetailTablePane().getTable().getSelectionModel().select(index);
		} else {
			MessageDialog.showError("New Detail can't be the same as an existing one!");
		}
	}

	private void updateDetail(ActionEvent event){
		ReasonForChangeMaintDialog dialog = new ReasonForChangeMaintDialog(getApplicationId());
		if (!dialog.showReasonForChangeMaintDialog("Update Detail", "Detail", true)) {
			return;
		}
		
		if (!getModel().isDetailExisted(selectedCategory.getCategoryId(), dialog.getText())) {
			getModel().updateDetail(selectedDetail.getDetailId(), dialog.getText(), getUserId());
			
			QiReasonForChangeDetail updatedDetail = (QiReasonForChangeDetail)selectedDetail.deepCopy();
			updatedDetail.setDetail(dialog.getText());
			AuditLoggerUtil.logAuditInfo(selectedDetail, updatedDetail, "Updated by user", getView().getScreenName(), getUserId());
			
			int index = refreshDetail(selectedCategory.getCategoryId(), selectedDetail);
			getView().getDetailTablePane().getTable().getSelectionModel().select(index);
		} else {
			MessageDialog.showError("Updated Detail can't be the same as an existing one!");
		}
	}
	
	private void deleteDetail(ActionEvent event){
		boolean deleteDetail = MessageDialog.confirm(getView().getStage(), 
				"Are you sure you want to delete the selected detail(s)?");

		if (!deleteDetail) {
			return;
		} else {
			getModel().deleteDetail(selectedDetail);
			AuditLoggerUtil.logAuditInfo(selectedDetail, null, "Deleted by user", getView().getScreenName(), getUserId());
			selectedDetail = null;
			refreshDetail(selectedCategory.getCategoryId(), null);
		}
	}

	private void refreshDept(String plantName) {
		List<Division> list = getModel().findAllDeptBySiteAndPlant(getModel().getSiteName(), plantName);
		getView().getDeptTablePane().setData(list);
	}
	
	private int refreshCategory(Division division, QiReasonForChangeCategory selectedCategory){
		List<QiReasonForChangeCategory> list= getModel().findAllCategoryByDept(division.getSiteName(), division.getPlantName(), division.getDivisionId());
		int index = 0;
		if (selectedCategory != null) {
			for (int i = 0; i < list.size(); i++) {
				if (((QiReasonForChangeCategory)list.get(i)).getId() == selectedCategory.getId()) {
					index = i;
					break;
				}
			}
		}
		getView().getCategoryTablePane().setData(list);
		return index;
	}

	private int refreshDetail(int categoryId, QiReasonForChangeDetail selectedDetail){
		List<QiReasonForChangeDetail> list= getModel().findAllDetailByCategory(categoryId);
		int index = 0;
		if (selectedDetail != null) {
			for (int i = 0; i < list.size(); i++) {
				if (((QiReasonForChangeDetail)list.get(i)).getId() == selectedDetail.getId()) {
					index = i;
					break;
				}
			}
		}
		getView().getDetailTablePane().setData(list);
		return index;
	}
}
