package com.honda.galc.client.teamlead;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.dto.ChangeFormDTO;
import com.honda.galc.client.dto.MCRevisionDTO;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.constant.RevisionType;
import com.honda.galc.dao.pdda.ChangeFormDao;
import com.honda.galc.entity.conf.MCRevision;
import com.honda.galc.entity.pdda.ChangeForm;
import com.honda.galc.property.MfgControlMaintenancePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.pdda.GenericPddaDaoService;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RevisionAssignmentWizard {

	private Stage dialog = null;
	private Stage owner = null;
	private String userId;
	private List<Integer> changeFormList;
	private List<ChangeFormDTO> selectedList;
	private TableView<MCRevisionDTO> revisionsTblView;
	private TableView<ChangeFormDTO> changeFormTblView;
	private Label errorMsg = UiFactory.createLabel("errorMsg");
	private Button okButton = UiFactory.createButton("OK");
	private String revType = RevisionType.INVALID.getRevType();
	private MfgControlMaintenancePropertyBean propertyBean;

	private TextField revDescription;

	public RevisionAssignmentWizard(List<Integer> changeFormlist, List<ChangeFormDTO> list, String userId,
			TableView<MCRevisionDTO> revisionsTblView,
			TableView<ChangeFormDTO> changeFormTblView,
			MfgControlMaintenancePropertyBean propertyBean) {
		this.changeFormList = changeFormlist;
		this.selectedList = list;
		this.userId = userId;
		this.revisionsTblView = revisionsTblView;
		this.changeFormTblView = changeFormTblView;
		this.propertyBean = propertyBean;
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
		dialog.sizeToScene();

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

		revDescription = UiFactory.createTextField("revDescription");

		Label label = UiFactory.createLabel("revisionDesc", "Enter a Description for this Revision");
		
		okButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				if (StringUtils.isNotBlank(revDescription.getText())) {
					MCRevision entity = ServiceFactory.getService(
							GenericPddaDaoService.class)
							.createRevisionForChangeForms(changeFormList, userId,
									revDescription.getText(), revType);

					MCRevisionDTO revDto = new MCRevisionDTO(entity);
					revisionsTblView.getItems().add(revDto);

					// Removing selected items from new change form table
					changeFormTblView.getItems().removeAll(selectedList);
					
					close();
				} else {
					errorMsg.setText("You must enter a Description");
					errorMsg.setTextFill(Color.RED);
				}
			
			}

		});

		Button cancelButton = UiFactory.createButton("Cancel");
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				close();
			}
		});
		
		validateChangeForms();

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(30, 30, 30, 30));

		grid.add(label, 0, 0);
		grid.add(revDescription, 1, 0);

		grid.add(okButton, 1, 1);
		grid.add(cancelButton, 0, 1);
		grid.add(errorMsg, 0, 2, 2, 1);

		Scene scene = new Scene(grid);
		scene.getStylesheets().add(cssPath);
		return scene;
	}

	private void close() {
		dialog.close();
	}
	
	private void validateChangeForms() {
		if (selectedList != null) {
			Set<String> revSet = new HashSet<String>();
			Set<String> platformSet = new HashSet<String>();
			List<Integer> selectedChangeFormIds = new ArrayList<Integer>();
			for (ChangeFormDTO cf : selectedList) {
				selectedChangeFormIds.add(cf.getChangeFormId());
				RevisionType rType = ServiceFactory.getService(
						GenericPddaDaoService.class)
						.getRevisionType(cf.getChangeFormId());
				revSet.add(rType.getRevType());
				
				ChangeForm chgFrm = ServiceFactory
				.getDao(ChangeFormDao.class)
				.findByKey(cf.getChangeFormId());
				
				String platform = chgFrm.getPlantLocCode().trim() + chgFrm.getDeptCode().trim()
						+ chgFrm.getProdSchQty().toString() + "_" 
						+ chgFrm.getProdAsmLineNo().trim() + chgFrm.getVehicleModelCode().trim() 
						+ chgFrm.getModelYearDate().toString();
				platformSet.add(platform);

			}
			if(revSet.size() > 1) {
				errorMsg.setText("PDDA mass and standard approved change forms can not be selected in one revision");
				errorMsg.setTextFill(Color.RED);
				okButton.setDisable(true);
			}
			else if(revSet.size() == 1) {
				revType = revSet.iterator().next();
				if(revType.equalsIgnoreCase(RevisionType.INVALID.getRevType())) {
					errorMsg.setText("PDDA Approval is neither mass nor standard");
					errorMsg.setTextFill(Color.RED);
					okButton.setDisable(true);
				}
			}
			
			if(platformSet.size() > 1) {
				errorMsg.setText("Multiple platfroms can not be selected in one revision");
				errorMsg.setTextFill(Color.RED);
				okButton.setDisable(true);
			}
			else if(platformSet.size() == 1) {
				//Validation for chronological changes 
				if(propertyBean.isCheckChronologicalOrder()) {
					List<String> unprocessedChangeForms = ServiceFactory.getDao(ChangeFormDao.class).getUnprocessedChangeForms(selectedChangeFormIds);
					if(unprocessedChangeForms!=null && !unprocessedChangeForms.isEmpty()) {
						errorMsg.setText("Please process following change forms before processing "+StringUtils.join(selectedChangeFormIds, Delimiter.COMMA)+": "+StringUtils.join(unprocessedChangeForms, Delimiter.COMMA));
						errorMsg.setTextFill(Color.RED);
						okButton.setDisable(true);
						return;
					}
				}
			}
		}
	}

}
