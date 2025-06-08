package com.honda.galc.client.teamlead;

import static com.honda.galc.common.logging.Logger.getLogger;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.utils.UiFactory;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OpRevWizard {

	private Stage dialog = null;
	private Stage owner = null;
	private String message = null;

	public OpRevWizard() {
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
					String.format(
							"Unable to load stylesheet [%s]. Using default",
							cssPath));
			
		}
		
		TextField opName = UiFactory.createTextField("opName");		
	
		Label opNameLabel = UiFactory.createLabel("opNameLabel", "Enter Operation Name");	
		opNameLabel.getStyleClass().add("oprevtextfield"); 
		
		TextField opType = UiFactory.createTextField("opType");
		Label opTypeLabel = UiFactory.createLabel("opTypeLabel", "Enter Operation Type");
		
		TextField opView = UiFactory.createTextField("opView");
		Label opViewLabel = UiFactory.createLabel("opViewLabel", "Enter Operation View");
		
		TextField opProcessor = UiFactory.createTextField("opProcessor");
		Label opProcessorLabel = UiFactory.createLabel("opProcessorLabel", "Enter Operation Processor");
		
		Button okButton  = UiFactory.createButton("OK");
		
		okButton.getStyleClass().add("oprevtextfield"); 		
		
		GridPane grid = new GridPane();
	    grid.setHgap(10);
	    grid.setVgap(10);
	    grid.setPadding(new Insets(30, 30, 30, 30));
	    
	    grid.add(opNameLabel, 0, 0); 
	    grid.add(opName, 1, 0); 
	    
	    grid.add(opTypeLabel, 0, 1); 
	    grid.add(opType, 1, 1); 
	    
	    grid.add(opViewLabel, 0, 2); 
	    grid.add(opView, 1, 2); 
	    
	    grid.add(opProcessorLabel, 0, 3); 
	    grid.add(opProcessor, 1, 3); 
	    
	    grid.add(okButton, 1, 4); 
	    	    
		Scene scene = new Scene(grid);
		scene.getStylesheets().add(cssPath);
		return scene;
	}
}
