package com.honda.galc.client.qi.repairentry;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiRepairResultDto;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableView;

/**
 * <h3>ScrapDialog Class description</h3>
 * <p>
 * ScrapDialog description
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
 *         Dec 16, 2016
 *
 */
public class ScrapDialog extends AbstractScrapDialog {
	final TreeTableView<QiRepairResultDto> parentTableView;

	public ScrapDialog(RepairEntryModel model, QiRepairResultDto defectToScrap,
			TreeTableView<QiRepairResultDto> treeTablePane, String applicationId, String associateId) {
		super("SELECTED NOT FIXED DEFECT FOR SCRAP", applicationId, model);

		this.parentTableView = treeTablePane;
		this.defectToScrap = defectToScrap;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.setDialogController(new ScrapDialogController(model, this, associateId));
		EventBusUtil.register(this);
		initComponents();
		reload();
		addScrapReasonChangeListener();
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
