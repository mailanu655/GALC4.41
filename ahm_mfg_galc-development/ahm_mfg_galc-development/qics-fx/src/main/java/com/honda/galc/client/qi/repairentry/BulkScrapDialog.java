/**
 * 
 */
package com.honda.galc.client.qi.repairentry;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiRepairResultDto;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableView;

/**
 * @author vf031824
 *
 */
public class BulkScrapDialog extends AbstractScrapDialog {
	
	final TreeTableView<QiRepairResultDto> parentTableView;
	
	/**
	 * @param model
	 * @param mainDefectList
	 * @param treeTablePane
	 * @param applicationId
	 */
	public BulkScrapDialog(RepairEntryModel model, QiRepairResultDto defectToScrap,
			TreeTableView<QiRepairResultDto> treeTablePane, String applicationId, String associateId) {
		super("SELECTED NOT FIXED DEFECT FOR SCRAP", applicationId, model);

		this.defectToScrap = defectToScrap;
		this.parentTableView = treeTablePane;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.setDialogController(new BulkScrapDialogController(model, this, associateId));
		EventBusUtil.register(this);
		initComponents();
		reload();
		addScrapReasonChangeListener();	}
	
	
	public void reload() {			
				getDefectToScrapLabel().setText(defectToScrap.getDefectDesc().toString());
			}
	
	
	private void addScrapReasonChangeListener() {
		scrapReasonTextArea.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (StringUtils.isNotBlank(newValue))
					getDialogController().clearDisplayMessage();
			}
		});
	}

	public TreeTableView<QiRepairResultDto> getParentTableView() {
		return parentTableView;
	}


}
