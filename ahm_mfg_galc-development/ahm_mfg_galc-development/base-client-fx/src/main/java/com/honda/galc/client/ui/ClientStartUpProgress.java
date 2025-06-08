package com.honda.galc.client.ui;

import java.awt.HeadlessException;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.event.ProgressEvent;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;


/**
 * 
 * <h3>ClientProgress</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ClientProgress description </p>
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
 * @author Paul Chou
 * Dec 28, 2010
 * 
 * 
 * @author Suriya Sena
 * Jan 26, 2014 JavaFx migration
 *
 */

public class ClientStartUpProgress{
	private static final long serialVersionUID = 1L;
	
	private Stage stage;
	private BorderPane progressPanel;
	private ProgressBar progressBar;
	private Label descriptionLabel;
	
	public ClientStartUpProgress(String title) throws HeadlessException {
		intialize(title);
	}

	private void intialize(String title) {
		EventBusUtil.register(this);
		Group root = new Group(getProgressPanel());
		Scene scene = new Scene(root);
		stage = new Stage();
		stage.setTitle(title);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.initOwner(ClientMainFx.getInstance().getStage());
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.centerOnScreen();
		stage.toFront();
	}

	public Pane getProgressPanel() {
		if(progressPanel == null){
			progressPanel = new BorderPane();
			progressPanel.setPadding(new Insets(40));
			progressPanel.setTop(getDescriptionLabel());
			progressPanel.setCenter(getProgressBar());
		}
		return progressPanel;
	}
		

	private ProgressBar getProgressBar() {
		if(progressBar == null){
			progressBar = new ProgressBar();
			progressBar.setMinSize(500, 35);
		}

		return progressBar;
	}
	
	public Label getDescriptionLabel() {
		if(descriptionLabel == null) {
			descriptionLabel = UiFactory.createLabel("descriptionLabel", "");
		}
		return descriptionLabel;
	}

	public void setDescriptionLabel(Label descriptionLabel) {
		this.descriptionLabel = descriptionLabel;
	}

	@Subscribe
	public void processProgressEvent(ProgressEvent event){
		// allow progress bar to hide
		if(event.getProgress() == 0) {
			stage.hide();
			return;
		}
		
		stage.toFront();
		
		getProgressBar().setProgress(event.getPercentComplete());
		getDescriptionLabel().setText(event.getDescription());

		Logger.getLogger().info("Received Progress Event :", event.getDescription() + event.getProgress());

		if(event.getProgress() >= 100) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
				   try {
					// Add a delay to allow enough time for the control to be displayed.
					Thread.sleep(500);
				   } catch (InterruptedException e) {
				   }
				   EventBusUtil.unregister(ClientStartUpProgress.this);
				   stage.close();
				}
			});
		}
	}

	
	public void start(){
		try {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					stage.show();
				}
			});
		} catch (Exception e) {
			Logger.getLogger().warn(e, "Failed to start client start up progress monitor.");
		}		
	}

}
