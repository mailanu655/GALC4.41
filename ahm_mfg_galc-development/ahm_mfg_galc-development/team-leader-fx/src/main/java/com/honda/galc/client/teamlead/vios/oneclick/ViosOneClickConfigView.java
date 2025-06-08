package com.honda.galc.client.teamlead.vios.oneclick;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.dto.ChangeFormDTO;
import com.honda.galc.client.dto.MCRevisionDTO;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamlead.DTOConverter;
import com.honda.galc.client.teamlead.vios.AbstractViosTabbedView;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.teamlead.vios.ViosPlatform;
import com.honda.galc.client.teamlead.vios.ViosPlatformPane;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.conf.MCRevisionDao;
import com.honda.galc.dao.pdda.ChangeFormDao;
import com.honda.galc.dto.MCRevisionDto;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.ComponentStatusId;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.pdda.ChangeForm;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.PddaPlatformUtil;
import com.honda.galc.vios.dto.PddaPlatformDto;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ViosOneClickConfigView extends AbstractViosTabbedView<ViosOneClickConfigModel, ViosOneClickConfigController> {

	private VBox mainBox;
	private ViosPlatformPane platformTitledPane;

	private MCViosMasterPlatform platform;
	
	private ObjectTablePane<MCRevisionDTO> revisionTable;
	private ObjectTablePane<ChangeFormDTO> newChangeForms;
	
	private LoggedButton makeActive;
	private LoggedLabel filterTextField;

	
	public ViosOneClickConfigView(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
		
	}

	@Override
	public String getScreenName() {
		return "One Click Approval";
	}

	@Override
	public void reload() {
		reload(StringUtils.EMPTY);
	}
	
	public void reload(String filter) {
		if(platform != null) {
			ComponentStatusId cpIdKd = new ComponentStatusId("OIF_MFG_CTRL_APPROVAL", "RUNNING_STATUS{"+platform.getGeneratedId()+"}");
			ComponentStatus cpKdLot = getDao(ComponentStatusDao.class).findByKey(cpIdKd);
			boolean isTimer = false;
			if(cpKdLot!=null && cpKdLot.getStatusValue().equalsIgnoreCase("RUNNING")) {
				isTimer = true;
				getMakeActiveButton().setDisable(true);
				filterTextField.setVisible(true);
			} else {
				isTimer = false;
				filterTextField.setVisible(false);
				if(getController().hasStarted()) {
					if(getController().getTimer() !=null)
						getController().getTimer().cancel();
					
					if(getController().getMainTask() != null)
						getController().getMainTask().cancel();
					
					getController().setHasStarted(false);
					
				}
				
			}
			refreshData(isTimer);
			
		}
	}

	public void refreshData(Boolean isTimer) {
		//load new change forms 
		List<ChangeForm> newchangeFormsList = ServiceFactory.getDao(ChangeFormDao.class).findAllByPddaPlatform(platform.getPlantLocCode(), platform.getDeptCode(), platform.getModelYearDate(), 
				platform.getProdSchQty(), platform.getProdAsmLineNo(), platform.getVehicleModelCode());
		newChangeForms.setData(DTOConverter.convertChangeForm(newchangeFormsList).getChangeForms());
		if(!isTimer) {
			if(newchangeFormsList.size() <= 0){
				getMakeActiveButton().setDisable(true);
			} else {
				getMakeActiveButton().setDisable(false);
			}
		}
		//Load Revisions Table
		List<MCRevisionDto> revisionList = ServiceFactory.getDao(
				MCRevisionDao.class).findAllByPddaPlatformWithControlNumber(platform.getPlantLocCode(), platform.getDeptCode(), platform.getModelYearDate(), 
						platform.getProdSchQty(), platform.getProdAsmLineNo(), platform.getVehicleModelCode());
		revisionTable.setData(DTOConverter.convertMCRevisionDto(revisionList));
	}
	

	@Override
	public void start() {

	}
	
	@Override
	public Node getTopBody() {
		platformTitledPane = new ViosPlatformPane(this) {
			@Override
			public void vmcComboboxListener(String value) {
				addVMCComboBoxListener(value);
			}
		};
		BorderPane.setMargin(platformTitledPane, new Insets(0, 0, 10, 0));
		return platformTitledPane;
	}
	
	private void addVMCComboBoxListener(String value) {
		clearErrorMessage();
		if(value != null) {
			mainBox.setDisable(false);
			platform = new MCViosMasterPlatform(platformTitledPane.getPlantCombobox().getSelectionModel().getSelectedItem(), 
					platformTitledPane.getDeptCombobox().getSelectionModel().getSelectedItem(), 
					new BigDecimal(platformTitledPane.getModelYearCombobox().getSelectionModel().getSelectedItem()), 
					new BigDecimal(platformTitledPane.getProdRateCombobox().getSelectionModel().getSelectedItem()), 
					platformTitledPane.getLineNoCombobox().getSelectionModel().getSelectedItem(), 
					platformTitledPane.getVmcCombobox().getSelectionModel().getSelectedItem());
			platform.setUserId(getUserId());
			reload();
			getController().startTimer();
		} else {
			mainBox.setDisable(true);
			filterTextField.setVisible(false);
			revisionTable.getTable().getItems().clear();
			newChangeForms.getTable().getItems().clear();
			platform = null;
		}
	}
	
	@Override
	public Node getCenterBody() {
		VBox newChangeFormDetailsBox = new VBox();
		newChangeFormDetailsBox.setAlignment(Pos.CENTER);
		newChangeFormDetailsBox.setSpacing(10);
		newChangeFormDetailsBox.setPadding(new Insets(10));

		HBox filterBox = new HBox();
		filterBox.setAlignment(Pos.CENTER);
		filterBox.setSpacing(20);

		filterTextField = new LoggedLabel(getId());
		
		filterTextField.setText("Approval Job is in Running state.");
		filterTextField.setStyle("-fx-font-weight: bold;-fx-font-size: 15;-fx-text-fill:blue;");
		filterTextField.setMaxWidth(500);
		filterTextField.setMinHeight(25);
		filterTextField.setVisible(false);

		filterBox.getChildren().addAll(filterTextField);

		newChangeForms = createNewChangeFormDetailsTable();

		newChangeFormDetailsBox.getChildren().addAll(filterBox, newChangeForms);

		revisionTable = createRevisionDetailsTable();

		VBox revisionTableBox = new VBox();
		revisionTableBox.setAlignment(Pos.CENTER);
		revisionTableBox.setSpacing(10);
		revisionTableBox.setPadding(new Insets(10));

		revisionTableBox.getChildren().add(revisionTable);

		HBox buttonContainer = new HBox();
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setSpacing(10);

		makeActive = createBtn(ViosConstants.MAKE_ACTIVE, getController());
		

		buttonContainer.getChildren().addAll(makeActive);

		TitledPane partTitledPane = createTitiledPane("New ChangeForm Details", newChangeFormDetailsBox);
		TitledPane checkerDetailsTitledPane = createTitiledPane("Revision Details", revisionTableBox);

		VBox detailsBox = new VBox();
		detailsBox.setAlignment(Pos.CENTER);
		detailsBox.setSpacing(10);
		detailsBox.getChildren().addAll(partTitledPane, checkerDetailsTitledPane);

		mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setSpacing(10);
		mainBox.setDisable(true);

		mainBox.getChildren().addAll(detailsBox, buttonContainer);
		return mainBox;
	}
	
	public LoggedButton createBtn(String text,EventHandler<ActionEvent> handler) {
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.setOnAction(handler);
		btn.getStyleClass().add("action-btn");
		btn.setPrefWidth(150);
		btn.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.45 * btn.getWidth())));
		return btn;
	}

	private TitledPane createTitiledPane(String title, Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setFont(Font.font(StringUtils.EMPTY, FontWeight.BOLD, 12));
		titledPane.setContent(content);
		titledPane.setCollapsible(false);
		titledPane.setContentDisplay(ContentDisplay.CENTER);
		return titledPane;
	}
	
	private ObjectTablePane<MCRevisionDTO> createRevisionDetailsTable() {
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Control No", "controlNo")
				.put("Revision ID", "id")
				.put("Description", "description")
				.put("Associate", "associateNo")
				.put("Type", "type")
				.put("Status", "status");
		Double[] columnWidth = new Double[] {
				0.10, 0.10,  0.20, 0.08, 0.20, 0.12
		}; 
		ObjectTablePane<MCRevisionDTO> subTable = new ObjectTablePane<MCRevisionDTO>(columnMappingList, columnWidth, true);
		subTable.setConstrainedResize(false);
		return subTable;
	}

	private ObjectTablePane<ChangeFormDTO> createNewChangeFormDetailsTable() {
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Change Form Id", "changeFormId")
				.put("Type", "changeFormType")
				.put("Control No", "controlNo")
				.put("ASM Proc Numbers", "asmProcNumbers")
				.put("ASM Proc Names", "asmProcNames");
		
		Double[] columnWidth = new Double[] { 0.10,  0.05, 0.08, 0.30, 0.20 };
		
		ObjectTablePane<ChangeFormDTO> subTable = new ObjectTablePane<ChangeFormDTO>(columnMappingList,columnWidth,true);
		subTable.setConstrainedResize(false);
		return subTable;
	}

	@Override
	public void onScreenSelected() {
		PddaPlatformDto platformDto = ViosPlatform.getInstance().getPddaPlatformDto();
		if(!PddaPlatformUtil.isBlankPddaPlatform(platformDto)) {
			MCViosMasterPlatform selectedPlatform = new MCViosMasterPlatform(platformDto);
			platformTitledPane.selectPlatform(selectedPlatform);
			platformTitledPane.setDisable(true);
		}
		
	}

	public LoggedLabel getFilterTextField() {
		return filterTextField;
	}

	public LoggedButton getMakeActiveButton() {
		return makeActive;
	}
	
	public MCViosMasterPlatform getPlatform() {
		return platform;
	}

	public String getUserId() {
		ApplicationContext applicationContext = ClientMainFx.getInstance().getApplicationContext();
		return applicationContext.getUserId();
	}
}

