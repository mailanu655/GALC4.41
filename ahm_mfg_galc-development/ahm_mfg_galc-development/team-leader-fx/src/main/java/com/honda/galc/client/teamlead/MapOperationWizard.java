package com.honda.galc.client.teamlead;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.dto.ChangeFormUnitDTO;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.conf.MCOperationDao;
import com.honda.galc.dao.conf.MCPddaUnitDao;
import com.honda.galc.entity.conf.MCOperation;
import com.honda.galc.entity.conf.MCPddaUnit;
import com.honda.galc.entity.conf.MCPddaUnitId;
import com.honda.galc.service.ServiceFactory;

public class MapOperationWizard {

	private Stage dialog = null;
	private Stage owner = null;
	private ChangeFormUnitDTO record;
	private TextField mappedOperationTextField = UiFactory.createTextField("mappedOperationTextField");
	private TextField newOperationTextField = UiFactory.createTextField("newOperationTextField");
	private ListView<String> mappedOpList;
	private String userId;
	TableView<ChangeFormUnitDTO> tableView;

	public MapOperationWizard(ChangeFormUnitDTO rec, String userId, TableView<ChangeFormUnitDTO> tableView) {
		this.record = rec;
		this.userId = userId;
		this.tableView = tableView;
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

		dialog.setHeight(600);
		dialog.setWidth(800);

		dialog.setScene(scene);
		dialog.centerOnScreen();
		dialog.sizeToScene();

		dialog.toFront();
		dialog.showAndWait();

	}

	private Scene constructScene() {
		String cssPath = "/resource/css/mfgmaintscreen.css";

		if (ClientMainFx.class.getResource(cssPath) == null) {
			getLogger().warn(
					String.format(
							"Unable to load stylesheet [%s]. Using default",
							cssPath));

		}

		ListView<String> mappedPPList = getMappedOperationList();
		Label mappedListLabel = UiFactory.createLabel("mappedListLabel", "mappedListLabel", "Already Mapped Operations");

		VBox vb2 = new VBox();
		vb2.getChildren().addAll(mappedListLabel, mappedPPList);
		Button mapButton = UiFactory.createButton("Map");
		Button createButton = UiFactory.createButton("Create");

		mapButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				if(StringUtils.isNotBlank(mappedOperationTextField.getText())) {
					addMCPddaUnitRecord();
					tableView.getItems().remove(record);
				}
                closeMessage();
			}

		});

		createButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				if(StringUtils.isNotBlank(newOperationTextField.getText())) {
					addMCOpRecord();
					addMCPddaUnitRecord(newOperationTextField.getText());
					tableView.getItems().remove(record);
				}
				closeMessage();
			}

		});

		mapButton.getStyleClass().add("oprevtextfield");
		
		Button cancelButton = UiFactory.createButton("Cancel");
		
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {dialog.close();}
		});

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(30, 30, 30, 30));

		VBox vb = new VBox();
		vb.setSpacing(15);
		Label mappedToLabel = UiFactory.createLabel("mappedToLabel", "Map To:");
		Label newOpToLabel = UiFactory.createLabel("newOpToLabel", "Create New Operation:");
		vb.getChildren().addAll(mappedToLabel, mappedOperationTextField,
				mapButton, newOpToLabel, newOperationTextField, createButton, cancelButton);

		grid.add(vb, 1, 0);

		grid.add(vb2, 2, 0);

		// grid.add(mapButton, 1, 4);

		Scene scene = new Scene(grid);
		scene.getStylesheets().add(cssPath);
		return scene;
	}

	protected void addMCPddaUnitRecord() {
		MCPddaUnit unit = new MCPddaUnit();

		unit.setApproved(new Timestamp(System.currentTimeMillis()));
		unit.setApproverNo(userId);
		MCPddaUnitId unitId = new MCPddaUnitId();
		unitId.setOperationName(mappedOperationTextField.getText());
		unitId.setUnitNo(record.getUnitId());
		unit.setId(unitId);

		ServiceFactory.getDao(MCPddaUnitDao.class).save(unit);

	}
	
	protected void addMCPddaUnitRecord(String opName) {
		MCPddaUnit unit = new MCPddaUnit();

		unit.setApproved(new Timestamp(System.currentTimeMillis()));
		unit.setApproverNo(userId);
		MCPddaUnitId unitId = new MCPddaUnitId();
		unitId.setOperationName(opName);
		unitId.setUnitNo(record.getUnitId());
		unit.setId(unitId);

		ServiceFactory.getDao(MCPddaUnitDao.class).save(unit);

	}

	private void addMCOpRecord() {
		MCOperation op = new MCOperation();
		op.setName(newOperationTextField.getText());
		op.setRevision(1);
		op.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));

		ServiceFactory.getDao(MCOperationDao.class).save(op);
	}

	private ListView<String> getMappedOperationList() {

		List<MCOperation> opList = ServiceFactory.getDao(MCOperationDao.class)
				.findAll();
		List<String> opListStr = new ArrayList<String>();

		for (MCOperation op : opList) {
			opListStr.add(op.getName());
		}

		ObservableList<String> observableList = FXCollections
				.observableList(opListStr);
		opListStr.add(0, "UNLINKED");

		mappedOpList = new ListView<String>();

		mappedOpList.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {

					public void changed(ObservableValue<? extends String> arg0,
							String arg1, String arg2) {

						mappedOperationTextField.setText(arg2);

					}

				});

		mappedOpList.setItems(observableList);

		return mappedOpList;
	}

}
