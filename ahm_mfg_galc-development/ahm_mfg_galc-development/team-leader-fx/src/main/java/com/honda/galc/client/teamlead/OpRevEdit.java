package com.honda.galc.client.teamlead;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.dto.MCOperationRevisionDTO;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCOperationRevisionId;
import com.honda.galc.service.ServiceFactory;

public class OpRevEdit {

	private Stage dialog = null;
	private Stage owner = null;
	private TableView<MCOperationRevisionDTO> tblView;
	private Button okButton;
	private Button cancelButton;
	private TextField opProcessor, opView;
	private MCOperationRevisionDTO rec;
	
	public OpRevEdit(TableView<MCOperationRevisionDTO> tblView, TableRow<MCOperationRevisionDTO> row) {
		this.tblView = tblView;
		this.rec = row.getItem();
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

	private Scene constructScene() {
		String cssPath = "/resource/css/mfgmaintscreen.css";

		if (ClientMainFx.class.getResource(cssPath) == null) {
			getLogger().warn(
					String.format("Unable to load stylesheet [%s]. Using default",
							cssPath));
		}
		
		
		opView = UiFactory.createTextField("opView");
		Label opViewLabel = UiFactory.createLabel("opViewLabel", "Enter Operation View");
		opView.setText(rec.getView());

		opProcessor = UiFactory.createTextField("opProcessor");
		Label opProcessorLabel = UiFactory.createLabel("opProcessorLabel", "Enter Operation Processor");
		opProcessor.setText(rec.getProcessor());

		okButton = UiFactory.createButton("Save");
		cancelButton = UiFactory.createButton("Cancel");

		okButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				MCOperationRevisionDao opRevDao = ServiceFactory.getDao(MCOperationRevisionDao.class);
				MCOperationRevisionId opRevId = new MCOperationRevisionId();
				opRevId.setOperationName(rec.getOperationName());
				opRevId.setOperationRevision(Integer.valueOf(rec.getOpRevision()));
				MCOperationRevision entity = opRevDao.findByKey(opRevId);
				entity.setView(opView.getText());
				entity.setProcessor(opProcessor.getText());
				opRevDao.save(entity);
				
				rec.setView(entity.getView());
				rec.setProcessor(entity.getProcessor());
				List<MCOperationRevisionDTO> items = new ArrayList<MCOperationRevisionDTO>(tblView.getItems());
				tblView.getItems().setAll(items);
				
				closeMessage();
			}
		});

		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				closeMessage();
			}
		});

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(30, 30, 30, 30));

		grid.add(opViewLabel, 0, 2);
		grid.add(opView, 1, 2);

		grid.add(opProcessorLabel, 0, 3);
		grid.add(opProcessor, 1, 3);

		grid.add(okButton, 1, 4);
		grid.add(cancelButton, 0, 4);

		Scene scene = new Scene(grid);
		scene.getStylesheets().add(cssPath);
		return scene;
	}
}
