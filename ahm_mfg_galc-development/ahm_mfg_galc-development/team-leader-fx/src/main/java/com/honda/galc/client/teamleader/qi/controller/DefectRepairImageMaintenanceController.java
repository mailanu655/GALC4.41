package com.honda.galc.client.teamleader.qi.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.DefectRepairImageMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.DefectRepairImageMaintenancePanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.MediaUtils;
import com.honda.galc.client.utils.MicroserviceUtils;
import com.honda.galc.dto.qi.QiDefectResultImageDto;
import com.honda.galc.entity.qi.QiDefectResultImage;
import com.honda.galc.entity.qi.QiRepairResultImage;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

/**
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * </TABLE>
 * @version 1.0
 * @author Dylan Yang
 * @see
 *
 */
public class DefectRepairImageMaintenanceController extends AbstractQiController<DefectRepairImageMaintenanceModel, DefectRepairImageMaintenancePanel> 
													implements EventHandler<ActionEvent> {

	public DefectRepairImageMaintenanceController(DefectRepairImageMaintenanceModel model, DefectRepairImageMaintenancePanel view) {
		super(model, view);
	}

	public void handle(ActionEvent actionEvent) {
		clearDisplayMessage();
		if (actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();

			if (QiConstant.UPDATE.equals(menuItem.getText())) {
				updateImage();
			} else if (QiConstant.DELETE.equals(menuItem.getText())) {
				deleteImage();
			}
		}
		if (actionEvent.getSource() instanceof LoggedButton || actionEvent.getSource() instanceof LoggedTextField) {
			loadData();
		}
	}
	
	private void loadData() {
		if(getView().getSearchProductIdRadioButton().isSelected()) {
			String productId = getView().getProductIdTextField().getText();
			if(StringUtils.isEmpty(productId)) {
				getView().reload();
			} else {
				getView().reload(getModel().findAllByProductId(productId));
			}
		} else if (getView().getSearchPartDefectCombinationRadioButton().isSelected()) {
			String partDefectCombination = getView().getPartDefectCombinationTextField().getText();
			if(StringUtils.isEmpty(partDefectCombination)) {
				getView().reload();
			} else {
				getView().reload(getModel().findAllByPartDefectCombination(partDefectCombination.toUpperCase()));
			}
		} else if(getView().getSearchApplicationIdRadioButton().isSelected()) {
			String applicationId = getView().getApplicationIdTextField().getText();
			if(StringUtils.isEmpty(applicationId)) {
				getView().reload();
			} else {
				getView().reload(getModel().findAllByApplicationId(applicationId));
			}
		}
	}

	private void updateImage() {
		QiDefectResultImageDto imageDto = getView().getImageTablePane().getSelectedItem();
		if(imageDto != null) {
			File mediaFile = MediaUtils.browseMediaFile();
			if(mediaFile != null) {
				ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
				if(dialog.showReasonForChangeDialog("Are you sure to update the media file?")) {
					String url = MicroserviceUtils.getInstance().postFile(mediaFile);
					if(url != null) {
						if(imageDto.getRepairId() > 0) {
							QiRepairResultImage repairImage = getModel().findRepairResultImage(imageDto);
							QiRepairResultImage newRepairImage = getModel().createRepairResultImage(imageDto, url);
							AuditLoggerUtil.logAuditInfo(repairImage, newRepairImage, dialog.getReasonForChangeText(), getView().getScreenName(),getUserId());
							getModel().deleteRepairResultImage(repairImage);;
						} else {
							QiDefectResultImage defectImage = getModel().findDefectResultImage(imageDto);
							QiDefectResultImage newDefectImage = getModel().createDefectResultImage(imageDto, url);
							AuditLoggerUtil.logAuditInfo(defectImage, newDefectImage, dialog.getReasonForChangeText(), getView().getScreenName(),getUserId());
							getModel().deleteDefectResultImage(defectImage);
						}
					} else {
						displayErrorMessage("An error occured when updating the media file.");
					}
				}
			}
			loadData();
		}
	}

	private void deleteImage() {
		QiDefectResultImageDto imageDto = getView().getImageTablePane().getSelectedItem();
		if(imageDto != null) {
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			if (dialog.showReasonForChangeDialog("Are you sure to delete the media file?")) {
				if(imageDto.getRepairId() > 0) {
					QiRepairResultImage repairImage = getModel().findRepairResultImage(imageDto);
					AuditLoggerUtil.logAuditInfo(repairImage, null, dialog.getReasonForChangeText(), getView().getScreenName(),getUserId());
					getModel().deleteRepairResultImage(repairImage);;
				} else {
					QiDefectResultImage defectImage = getModel().findDefectResultImage(imageDto);
					AuditLoggerUtil.logAuditInfo(defectImage, null, dialog.getReasonForChangeText(), getView().getScreenName(),getUserId());
					getModel().deleteDefectResultImage(defectImage);
				}
			}
			loadData();
		}
	}

	public void addContextMenuItems() {
		clearDisplayMessage();
		if(MicroserviceUtils.getInstance().isServiceAvailable()) {
			List<String> menuItemsList = new ArrayList<String>();
			menuItemsList.add(QiConstant.DELETE);
			menuItemsList.add(QiConstant.UPDATE);
			getView().getImageTablePane().createContextMenu((String[]) menuItemsList.toArray(new String[menuItemsList.size()]), this);
		}
	}

	@Override
	public void initEventHandlers() {
		addContextMenuItems();
	}
}
