package com.honda.galc.client.teamlead.vios.platform;

import com.honda.galc.client.teamlead.vios.AbstractViosDialog;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.ui.component.LoggedButton;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * <h3>ViosPlatformMaintDialog Class description</h3>
 * <p>
 * Dialog for Vios Platform Maintenance
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
public class ViosPlatformMaintDialog extends AbstractViosDialog {

	public ViosPlatformMaintDialog(Stage stage) {
		super("VIOS Platform Maintenance", stage);
		((BorderPane) this.getScene().getRoot()).setCenter(getCenterPanel());
		this.setWidth(getScreenWidth() * 0.90);
	}

	@Override
	public Node getMainContainer() {
		return null;
	}

	@Override
	public void initHandler() {

	}

	@Override
	public void loadData() {

	}
	
	public Node getCenterPanel() {
		VBox mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		
		ViosPlatformMaintPanel panel = new ViosPlatformMaintPanel(this);
		
		final LoggedButton closeButton = createBtn(ViosConstants.CLOSE);
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage stage = (Stage) closeButton.getScene().getWindow();
				stage.close();
			}
		});
		HBox buttonContainer = new HBox(closeButton);
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(5));
		
		mainBox.getChildren().addAll(panel, buttonContainer);
		return mainBox;
	}

}
