package com.honda.galc.client.teamlead.vios.platform;

import java.util.List;

import com.honda.galc.client.teamlead.vios.AbstractViosDialog;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.MCViosMasterPlatformDao;
import com.honda.galc.dao.pdda.ChangeFormDao;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.vios.dto.PddaPlatformDto;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.honda.galc.service.ServiceFactory.getDao;
/**
 * <h3>ImportViosPlatformDialog Class description</h3>
 * <p>
 * Dialog to import Vios Platform
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
 * @author Hemant Kumar<br>
 *        Aug 28, 2018
 */
public class ImportViosPlatformDialog extends AbstractViosDialog {
	
	private ObjectTablePane<PddaPlatformDto> platformTablePane;
	private LoggedButton importButton;
	private LoggedButton cancelButton;
	
	private String userId;

	public ImportViosPlatformDialog(Stage stage, String userId) {
		super("Import VIOS Platform", stage);
		this.userId = userId;
	}

	
	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	@Override
	public Node getMainContainer() {
		VBox mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setPadding(new Insets(20));
		mainBox.setSpacing(10);
		
		platformTablePane = createPlatformTablePane();
		
		HBox buttonContainer = new HBox();
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(5));
		buttonContainer.setSpacing(10);
		
		importButton = createBtn(ViosConstants.IMPORT);
		cancelButton = createBtn(ViosConstants.CANCEL);
		
		buttonContainer.getChildren().addAll(importButton, cancelButton);
		
		mainBox.getChildren().addAll(platformTablePane, buttonContainer);
		
		return mainBox;
	}

	@Override
	public void initHandler() {
		importButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					List<PddaPlatformDto> platformList = platformTablePane.getSelectedItems();
					if(platformList.size() == 0) {
						setErrorMessage("Please select atleast one platform to import");
						return;
					}
					for(PddaPlatformDto dto : platformList) {
						MCViosMasterPlatform platform = new MCViosMasterPlatform(dto);
						platform.setUserId(getUserId());
						getDao(MCViosMasterPlatformDao.class).insert(platform);
					}
					Stage stage = (Stage) importButton.getScene().getWindow();
					stage.close();
				} catch (Exception e) {
					Logger.getLogger().error(e, new LogRecord("An exception occured while importing MCViosMasterPlatform"));
					setErrorMessage("Something went wrong while importing Platforms");
				}
			}
		});
		
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage stage = (Stage) cancelButton.getScene().getWindow();
				stage.close();
			}
		});
	}

	@Override
	public void loadData() {
		List<PddaPlatformDto> platformList = getDao(ChangeFormDao.class).getAllNewPlatforms();
		platformTablePane.setData(platformList);
	}
	
	private ObjectTablePane<PddaPlatformDto> createPlatformTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with(ViosConstants.PLANT, "plantLocCode")
				.put(ViosConstants.DEPARTMENT, "deptCode")
				.put(ViosConstants.MODEL_YEAR, "modelYearDate")
				.put(ViosConstants.PRODUCTION_RATE, "prodSchQty")
				.put(ViosConstants.LINE_NUMBER, "prodAsmLineNo")
				.put(ViosConstants.VEHICLE_MODEL_CODE,"vehicleModelCode");

		Double[] columnWidth = new Double[] {0.10,0.10,0.10,0.10,0.10,0.10};
		final ObjectTablePane<PddaPlatformDto> panel = new ObjectTablePane<PddaPlatformDto>(columnMappingList,columnWidth);
		panel.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		return panel;
	}

}
