package com.honda.galc.client.teamlead;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.dto.MCPddaPlatformDTO;
import com.honda.galc.client.dto.MCRevisionDTO;
import com.honda.galc.client.teamlead.vios.process.ViosProcessMaintDialog;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.constant.RevisionStatus;
import com.honda.galc.constant.RevisionType;
import com.honda.galc.dao.conf.MCViosMasterMBPNMatrixDataDao;
import com.honda.galc.dao.conf.MCPddaChangeDao;
import com.honda.galc.dao.conf.MCPddaPlatformDao;
import com.honda.galc.dao.conf.MCRevisionDao;
import com.honda.galc.dao.pdda.ChangeFormUnitDao;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.entity.conf.MCPddaPlatform;
import com.honda.galc.entity.conf.MCRevision;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.property.MfgControlMaintenancePropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.pdda.GenericPddaDaoService;
import com.honda.galc.service.property.PropertyService;

public class RevisionApprovalWizard {

	private Stage dialog = null;
	private Stage owner = null;
	private String userId;
	private MCRevisionDTO rev;
	private Label userMessage = UiFactory.createLabel("userMessage");
	final Button okButton = UiFactory.createButton("OK");
	private TableView<MCPddaPlatformDTO> platformMMTblView;
	private MfgControlMaintenancePropertyBean propertyBean;

	TextField revDescription;
	
	private TextFlow linkFlow;
	private ToggleGroup group;
	
	private MCViosMasterPlatform platform;

	public RevisionApprovalWizard(MCRevisionDTO rev,TableView<MCPddaPlatformDTO> platformMMTblView, String userId,
			MfgControlMaintenancePropertyBean propertyBean, MCViosMasterPlatform platform) {
		this.rev = rev;
		this.userId = userId;
		this.propertyBean = propertyBean;
		this.platformMMTblView=platformMMTblView;
		this.platform = platform;
		constructStage();
	}

	public void closeMessage() {
		dialog.close();
	}

	public void constructStage() {

		dialog = new Stage();
		dialog.initStyle(StageStyle.DECORATED);
		if (owner != null)
			dialog.initOwner(owner);
		dialog.initModality(Modality.APPLICATION_MODAL);
		Scene scene = constructScene();

		dialog.setHeight(400);
		dialog.setWidth(600);

		dialog.setScene(scene);
		dialog.centerOnScreen();
		// dialog.sizeToScene();

		dialog.toFront();
		dialog.showAndWait();

	}

	public String getDescription() {
		return "";
	}

	private Scene constructScene() {
		String cssPath = "/resource/css/mfgmaintscreen.css";

		if (ClientMainFx.class.getResource(cssPath) == null) {
			getLogger().warn(
					String.format(
							"Unable to load stylesheet [%s]. Using default",
							cssPath));

		}

		final Button cancelButton = UiFactory.createButton("Cancel");
		group = new ToggleGroup();

		RadioButton rb1 = UiFactory.createRadioButton(RevisionStatus.DEVELOPING.getRevStatus());
		rb1.setToggleGroup(group);
		rb1.setUserData(RevisionStatus.DEVELOPING.getRevStatus());

		RadioButton rb3 = UiFactory.createRadioButton(RevisionStatus.APPROVED.getRevStatus());
		rb3.setToggleGroup(group);
		rb3.setUserData(RevisionStatus.APPROVED.getRevStatus());
		
		okButton.setDisable(true);
		
		//Perform validation for chronological order
		List<Long> pendingRevs = getUnapprovedOldRevisions();
		if(pendingRevs!=null && !pendingRevs.isEmpty()) {
			rb1.setDisable(true);
			rb3.setDisable(true);
			userMessage
			.setText("Please approve following revisions first: "+StringUtils.join(pendingRevs, Delimiter.COMMA));
			userMessage.setTextFill(Color.RED);
		}
		else {
			// Activate status flow
			String status = rev.getStatus()!=null ? rev.getStatus().trim().toUpperCase() : "";
			if(status.equals(RevisionStatus.MAPPING.getRevStatus())) {
				rb3.setDisable(true);
			}
			else if(status.equals(RevisionStatus.DEVELOPING.getRevStatus())) {
				rb1.setDisable(true);
			}
			else if(status.equals(RevisionStatus.APPROVED.getRevStatus())) {
				rb1.setDisable(true);
				rb3.setDisable(true);
			}

			group.selectedToggleProperty().addListener(
					new ChangeListener<Toggle>() {
						public void changed(ObservableValue<? extends Toggle> ov,
								Toggle old_toggle, Toggle new_toggle) {
							if (group.getSelectedToggle() != null) {
								linkFlow.setVisible(false);
								userMessage.setText("");
								if (group.getSelectedToggle().getUserData()
										.equals(RevisionStatus.DEVELOPING.getRevStatus())) {
									if (checkForUnMappedProcesses() > 0) {
										okButton.setDisable(true);
										userMessage
												.setText("There are Unmapped Processes for this Revision");
										userMessage.setTextFill(Color.RED);
										linkFlow.setVisible(true);
									} else {
										okButton.setDisable(false);
									}
								} else if (group.getSelectedToggle().getUserData()
										.equals(RevisionStatus.APPROVED.getRevStatus())) {
									okButton.setDisable(false);
								} else {
									okButton.setDisable(false);
								}

							}
						}

					});

			okButton.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent arg0) {
					if (group.getSelectedToggle().getUserData()
							.equals(RevisionStatus.DEVELOPING.getRevStatus())) {
						addChildRecords();
					} else if (group.getSelectedToggle().getUserData()
							.equals(RevisionStatus.APPROVED.getRevStatus())) {
						Logger.getLogger().info("Perform approval");
						performApproval();
						
					}
				}

				
			});
		}	
		
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {close();}
		});

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(30, 30, 30, 30));

		grid.add(rb1, 0, 0);
		grid.add(rb3, 0, 1);
		grid.add(userMessage, 0, 6);
		
		Hyperlink hyperLink = new Hyperlink("here");
		hyperLink.setStyle("-fx-font-size: 10pt;-fx-font-weight: bold ;");
		linkFlow = new TextFlow(new Text("Please click "), hyperLink , new Text(" to map all unmapped processes"));
		linkFlow.setStyle("-fx-font-size: 10pt; -fx-font-family: arial;");
		linkFlow.setVisible(false);
		hyperLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	ViosProcessMaintDialog viosProcessMaintDialog = new ViosProcessMaintDialog(dialog, rev.getId(), platform);
            	viosProcessMaintDialog.showDialog();
            	Toggle toggle = group.getSelectedToggle();
            	toggle.setSelected(false);
            	toggle.setSelected(true);
            }
        });
		grid.add(linkFlow, 0, 7);
		
		HBox hButtonBox = new HBox(20);
		hButtonBox.setPadding(new Insets(0, 0, 0, 30));
		hButtonBox.getChildren().addAll(okButton, cancelButton);

		VBox vbox = new VBox();
		vbox.getChildren().addAll(grid, hButtonBox);

		// grid.add(okButton, 1, 1);

		Scene scene = new Scene(vbox);
		scene.getStylesheets().add(cssPath);
		return scene;
	}

	protected void addChildRecords() {
		//MC Revision
		String revisionType = getRevisionType();
		revisionType = revisionType!=null?revisionType:"";
		//Get Model Year Code Map for populating matrix
		String productType = PropertyService.getPropertyBean(SystemPropertyBean.class).getProductType();
		Map<String, String> yearCodeMap = ServiceFactory.getService(GenericPddaDaoService.class).getYearDescriptionCodeMap(productType);
		
		//For PDDA Platform Change revision, checking for deprecated id
		if(revisionType.equalsIgnoreCase(RevisionType.PLATFORM_CHG.getRevType())) {
			//For Revision type 'Platform change', there will always be only one platform
			MCPddaPlatform platform = getFirstPlatformForRev();
			if(platform!=null) {
				if(checkForDeprecatedParent(platform)) {
					return;
				}
				ServiceFactory.getService(GenericPddaDaoService.class).addPlatformChg(rev.getId(), this.userId);
			}
			ServiceFactory.getDao(MCRevisionDao.class).setRevisionStatus(rev.getId(), RevisionStatus.DEVELOPING);
			rev.setStatus(RevisionStatus.DEVELOPING.getRevStatus());
			close();
		}
		else if(revisionType.equalsIgnoreCase(RevisionType.PDDA_MASS.getRevType()) 
				|| revisionType.equalsIgnoreCase(RevisionType.PDDA_STD.getRevType())) {
			// Add all unmapped processes in MC_PDDA_PLATFORM_TBX
			List<String> processList = ServiceFactory.getDao(ChangeFormUnitDao.class).findAllUnmappedPddaProcessBy(rev.getId());
			for(String process : processList) {
				ServiceFactory.getService(GenericPddaDaoService.class).addMCPddaPlatformRecord(process, rev.getId(), this.userId);
			}
			ServiceFactory.getService(GenericPddaDaoService.class).createMfgCtrlRecords(rev.getId(), yearCodeMap, this.userId);
			rev.setStatus(RevisionStatus.DEVELOPING.getRevStatus());
			close();
		}
		else {
			okButton.setDisable(true);
			userMessage.setText("Unknown revision type: "+rev.getId());
			userMessage.setTextFill(Color.RED);
		}
	}

	private void close() {
		dialog.close();
	}

	private int checkForUnMappedProcesses() {
		return ServiceFactory
				.getDao(ChangeFormUnitDao.class)
				.findAllProcessesByRevId(rev.getId(), false).size();
	}
	
	private List<Long> getUnapprovedOldRevisions() {
		if(propertyBean.isCheckChronologicalOrder()) {
			String revisionType = getRevisionType();
			if(revisionType.equalsIgnoreCase(RevisionType.PLATFORM_CHG.getRevType())) {
				return ServiceFactory
						.getDao(MCPddaPlatformDao.class)
						.getUnapprovedOldRevisions(rev.getId());
			}
			else if(revisionType.equalsIgnoreCase(RevisionType.PDDA_MASS.getRevType()) 
					|| revisionType.equalsIgnoreCase(RevisionType.PDDA_STD.getRevType())) {
				return ServiceFactory
						.getDao(MCPddaChangeDao.class)
						.getUnapprovedOldRevisions(rev.getId());
			}
		}
		return null;
	}
	
	private MCPddaPlatform getParentPlatform(MCPddaPlatform platform) {
		long deprRevId = platform.getDeprecatedRevId();
		if(deprRevId > 0) {
			MCPddaPlatform parent = ServiceFactory
					.getDao(MCPddaPlatformDao.class).getPDDAPlatformForOperation(
							platform.getPlantLocCode(), platform.getDeptCode(),
							platform.getModelYearDate(), platform.getProductScheduleQty(),
							platform.getProductAsmLineNo(), platform.getVehicleModelCode(),
							platform.getAsmProcessNo(), deprRevId);
			return parent;
		}
		return null;
	}
	
	private void performApproval() {
		if(getRevisionType().equalsIgnoreCase(RevisionType.PLATFORM_CHG.getRevType())) {
			//For PDDA Platform Change revision, checking for deprecated id
			if(checkForDeprecatedParent(getFirstPlatformForRev())) {
				return;
			}
		}

		ServiceFactory.getService(GenericPddaDaoService.class).performApproval(rev.getId(),this.userId);
		updateMBPNMatrix();
		rev.setStatus(RevisionStatus.APPROVED.getRevStatus());
		updateMatrixMaintenence();
		close();
	}
	
	private void updateMBPNMatrix() {
		List<MCViosMasterMBPNMatrixData> mbpnMasterdataList =
				ServiceFactory.getService(MCViosMasterMBPNMatrixDataDao.class).findAllData(platform.getGeneratedId(),StringUtils.EMPTY);
		
		for (MCViosMasterMBPNMatrixData mcmbpnMasterData : mbpnMasterdataList) {
			ServiceFactory.getDao(MCViosMasterMBPNMatrixDataDao.class).updateMBPNMasterRevision(mcmbpnMasterData, platform.getGeneratedId());
		}
	}

	private void updateMatrixMaintenence() {
		List<MCPddaPlatform> approvedRevision=ServiceFactory.getDao(MCPddaPlatformDao.class).getAprvdPDDAPlatformsforMatrix(rev.getId());
		for(int i=0;i<approvedRevision.size();i++){
			platformMMTblView.getItems().add(DTOConverter.convertPlatform(approvedRevision.get(i)));	
		}
	}

	private String getRevisionType() {
		//MC Revision Type
		MCRevision revision = ServiceFactory.getDao(MCRevisionDao.class).findByKey(rev.getId());
		String revisionType = revision.getType();
		revisionType = revisionType!=null?revisionType:"";
		return revisionType;
	}
	
	private MCPddaPlatform getFirstPlatformForRev() {
		List<MCPddaPlatform> platforms = ServiceFactory.getDao(MCPddaPlatformDao.class).getPlatformsForRevision(rev.getId());
		if(platforms!=null && platforms.size() > 0) {
			return platforms.iterator().next();
		}
		return null;
	}
	
	private boolean checkForDeprecatedParent(MCPddaPlatform platform) {
		if(platform!=null) {
			MCPddaPlatform parent = getParentPlatform(platform);
			//Checking if parent platform is deprecated or not
			if(parent!=null && parent.getDeprecated()!=null) {
				//The parent platform is been deprecated. 
				//Need to deprecate all the data for this revision
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				platform.setDeprecated(timestamp);
				platform.setDeprecaterNo(this.userId);
				platform.setUpdateTimestamp(timestamp);
				platform = ServiceFactory.getDao(MCPddaPlatformDao.class).update(platform);
				
				//Deprecating Operations
				ServiceFactory.getService(GenericPddaDaoService.class).deprecateOperations(platform.getPddaPlatformId(), this.userId, platform.getDeprecatedRevId());
				//Approving just as a process
				ServiceFactory.getService(GenericPddaDaoService.class).approveRevision(rev.getId(), this.userId);
				
				rev.getEntity().setStatus(RevisionStatus.APPROVED.getRevStatus());
				ServiceFactory.getDao(MCRevisionDao.class).update(rev.getEntity());
				rev.setStatus(RevisionStatus.APPROVED.getRevStatus());
				
				okButton.setDisable(true);
				String eol = System.getProperty("line.separator");
				userMessage.setText("The parent revision [Revision id: "+platform.getDeprecatedRevId()+"] is already been deprecated."+eol+"This Revision is marked as Approved.");
				userMessage.setTextFill(Color.RED);
				return true;
			}
		}
		return false;
	}
}
