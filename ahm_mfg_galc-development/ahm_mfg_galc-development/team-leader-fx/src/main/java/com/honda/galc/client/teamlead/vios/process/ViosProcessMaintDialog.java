package com.honda.galc.client.teamlead.vios.process;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamlead.vios.AbstractViosDialog;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.MCViosMasterPlatform;

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
 * <h3>ViosProcessMaintDialog Class description</h3>
 * <p>
 * Dialog for Vios Process Maintenance
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
public class ViosProcessMaintDialog extends AbstractViosDialog {

	public ViosProcessMaintDialog(Stage stage, long revId, MCViosMasterPlatform platform) {
		super("VIOS Process Maintenance", stage);
		((BorderPane) this.getScene().getRoot()).setCenter(getCenterPanel(revId, platform));
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
	
	public Node getCenterPanel(long revId, MCViosMasterPlatform platform) {
		VBox mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		
		LoggedLabel revIdLabel = UiFactory.createLabel("revIdLabel", StringUtils.EMPTY, "-fx-font-size: 14px;-fx-font-weight: bold;");
		revIdLabel.setText("Rev ID: "+revId);
		HBox labelContainer = new HBox(revIdLabel);
		labelContainer.setAlignment(Pos.CENTER);
		labelContainer.setPadding(new Insets(5));
		
		
		ViosProcessMaintPanel panel = new ViosProcessMaintPanel(this, revId, platform);
		
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
		
		mainBox.getChildren().addAll(labelContainer, panel, buttonContainer);
		return mainBox;
	}

}
