package com.honda.galc.client.teamleader.qi.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.List;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.model.PdcLocalAttributeMaintModel;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dao.audit.AuditLogDao;
import com.honda.galc.entity.audit.AuditLog;
import com.honda.galc.entity.audit.AuditLogId;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>PdcLocalAttrDispQrCodeDialog Class description</h3>
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
 * @author LnTInfotech<br>
 * 
 */

public class QiPdcHistoryDialog extends QiFxDialog<PdcLocalAttributeMaintModel> {


	private QiLocalDefectCombination localDefectCombination = null;
	private ObjectTablePane<AuditLog> qiPdcHistoryTablePane;
	private String entryScreen;
	private String fullPartName;

	public QiPdcHistoryDialog(String title, PdcLocalAttributeMaintModel model, Stage parentStage, String entryScreen, QiLocalDefectCombination ldc, String partName) {
		super(title, parentStage, model);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.entryScreen=entryScreen;
		setLocalDefectCombination(ldc);
		setFullPartName(partName);
		initComponents();
	}
	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	private ObjectTablePane<AuditLog> createQiPdcHistoryTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Actual Timestamp","id.actualTimeStamp")
				.put("Update User","updateUser")
				.put("Maintenance Screen","maintenanceScreen")
				.put("Table Name","id.tableName")
				.put("Column Name","id.columnName")
				.put("Previous Value","previousValue")
				.put("Current Value","currentValue")
				.put("Update Reason","updateReason")
				.put("Change Type","changeType")
			
				;
		
		Double[] columnWidth = new Double[] {
				0.092,
				0.057,
				0.115,
				0.115,
				0.115,
				0.115,
				0.115,
				0.115,
				0.115,
		};
		ObjectTablePane<AuditLog> panel = new ObjectTablePane<AuditLog>(columnMappingList,columnWidth);
		return panel;
	
	}
	
	/**
	 * This method is used to initialize the components of Dialog screen.
	 */
	private void initComponents() {
		setQiPdcHistoryTablePane(createQiPdcHistoryTablePane());
		double screenResolutionWidth=Screen.getPrimary().getVisualBounds().getWidth();
		double screenResolutionHeight=Screen.getPrimary().getVisualBounds().getHeight();
		Button okButton = new Button(QiConstant.OK); 

			okButton.setOnAction(new EventHandler<ActionEvent>() {
				   @Override public void handle(ActionEvent e) {
					   Stage stage = (Stage) okButton.getScene().getWindow();
						stage.close();
				   }
			   }
		);
						
		MigPane migPane = new MigPane("","[center]", "[][]");
		migPane.setPrefWidth(screenResolutionWidth/1.17);
		migPane.setPrefHeight(screenResolutionHeight/2.4);
		migPane.add(getQiPdcHistoryTablePane(),"wrap");
		migPane.add(okButton, "width 150");
		((BorderPane) this.getScene().getRoot()).setCenter(migPane);
		reload();
	}
		
	public String getEntryScreen() {
		return entryScreen;
	}

	public ObjectTablePane<AuditLog> getQiPdcHistoryTablePane() {
		return qiPdcHistoryTablePane;
	}

	public void setQiPdcHistoryTablePane(ObjectTablePane<AuditLog> qiPdcHistoryTablePane) {
		this.qiPdcHistoryTablePane = qiPdcHistoryTablePane;
	}

	public QiLocalDefectCombination getLocalDefectCombination() {
		return localDefectCombination;
	}
	public void setLocalDefectCombination(QiLocalDefectCombination localDefectCombination) {
		this.localDefectCombination = localDefectCombination;
	}
	public String getFullPartName() {
		return fullPartName;
	}
	public void setFullPartName(String fullPartName) {
		this.fullPartName = fullPartName;
	}
	public void reload()  {
		 List<AuditLog> history = ServiceFactory.getDao(AuditLogDao.class).findPdcHistory(localDefectCombination.getLocalDefectCombinationId());
		 AuditLog currentRec = new AuditLog();
		 currentRec.setUpdateUser(localDefectCombination.getCreateUser());
		 currentRec.setCreateTimestamp(localDefectCombination.getUpdateTimestamp());
		 currentRec.setUpdateTimestamp(localDefectCombination.getUpdateTimestamp());
		 currentRec.setPrimaryKey(String.valueOf(localDefectCombination.getLocalDefectCombinationId()));
		 currentRec.setUpdateReason("Current");
		 currentRec.setChangeType("Qics Maintenance");
		 currentRec.setMaintenanceScreen("Local Attribute");
		 currentRec.setCurrentValue("Current");
		 currentRec.setSystem("QICS");
		 currentRec.setPreviousValue("All");
		 currentRec.setId(new AuditLogId());
		 currentRec.getId().setActualTimeStamp(new Timestamp(localDefectCombination.getCreateTimestamp().getTime()));
		 currentRec.getId().setColumnName("All");
		 currentRec.getId().setPrimaryKeyValue(getFullPartName());
		 currentRec.getId().setTableName(CommonUtil.getTableName(QiLocalDefectCombination.class));
		 
		 history.add(0, currentRec);		 
		 qiPdcHistoryTablePane.setData(history);
	}
		
	public void reload(List<AuditLog>  qiPdcHistoryList) {
		qiPdcHistoryTablePane.setData(qiPdcHistoryList);
	}
	
}	

