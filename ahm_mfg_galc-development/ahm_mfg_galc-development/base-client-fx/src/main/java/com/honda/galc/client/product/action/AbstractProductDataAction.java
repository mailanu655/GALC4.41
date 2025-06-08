package com.honda.galc.client.product.action;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;

import com.honda.galc.checkers.CheckResult;
import com.honda.galc.checkers.ICheckPoint;
import com.honda.galc.checkers.ReactionType;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.product.mvc.AbstractProductClientPane;
import com.honda.galc.client.ui.ApplicationMainPane;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.BaseCheckerData;

/**
 * 
 * 
 * <h3>AbstractProductAction Class description</h3>
 * <p>
 * AbstractProductAction description
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
 * @author Jeffray Huang<br>
 *         Mar 10, 2014
 *
 *
 */
public abstract class AbstractProductDataAction extends AbstractAction implements ICheckPoint<BaseCheckerData> {
	private static final String OUTSTANDING_OPERATIONS_CHECK="MC OPERATIONS CHECK";
	private ApplicationMainPane applicationMainPane;

	public AbstractProductDataAction(ApplicationMainPane applicationMainPane) {
		this.applicationMainPane = applicationMainPane;
	}

	protected ApplicationMainPane getApplicationMainPane() {
		return applicationMainPane;
	}

	protected void setScheduleClientController(ApplicationMainPane applicationMainPane) {
		this.applicationMainPane = applicationMainPane;
	}

	@Override
	public boolean dispatchReactions(List<CheckResult> checkResults, BaseCheckerData inputData) {
		if (checkResults != null && !checkResults.isEmpty() && checkResults.get(0).getReactionType() != null) {
			ReactionType reactionType = checkResults.get(0).getReactionType();
			String msg = checkResults.get(0).getCheckMessage();

			switch (reactionType) {
			case DISPLAY_ERR_MSG:
				Logger.getLogger().error(msg);
				getApplicationMainPane().getMainWindow().setErrorMessage(msg);
				return false;
			case DISPLAY_WARNING_MSG:
				Logger.getLogger().error(msg);
				getApplicationMainPane().getMainWindow().setErrorMessage(msg);
				return true;
			case DISPLAY_MSG_WITH_CONFIRMATION:
				Logger.getLogger().error(msg);
				displayPopUp(false, msg,checkResults.get(0).getCheckName());
				return true;
			case DISPLAY_ERR_WITH_CONFIRMATION:
				playNgSound();
				Logger.getLogger().error(msg);
				displayPopUp(true, msg,checkResults.get(0).getCheckName());
				return false;
			
			default:
				break;
			}
		}
		return false;
	}

	protected boolean playNgSound() {
		ApplicationMainPane applicationMainPane = getApplicationMainPane();
		if (applicationMainPane instanceof com.honda.galc.client.product.mvc.ProductClientPane) {
			((AbstractProductClientPane) applicationMainPane).getController().getAudioManager().playNGSound();
			return true;
		}
		return false;
	}

	private void displayPopUp(boolean isError, String msg, String checkerName) {
		final FxDialog dialogStage = new FxDialog("", ClientMainFx.getInstance().getStage());
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		GridPane pane = new GridPane();
		Label header = new Label();
		header.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		GridPane.setHalignment(header, HPos.CENTER);
		header.setPadding(new Insets(0, 0, 10, 0));
		if (isError) {
			header.setText("ERROR");
			pane.setStyle("-fx-background-color: RED;");
		} else {
			header.setText("WARNING");
			pane.setStyle("-fx-background-color: YELLOW;");
		}

		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(10);// padding
		Scene scene = null;
		if(OUTSTANDING_OPERATIONS_CHECK.equals(checkerName)){
			scene= new Scene(pane,1500,400);
		}else{
			scene= new Scene(pane, 800, 300);
		}
		dialogStage.setScene(scene);
		pane.add(header, 0, 1);
		pane.autosize();
		String[] splitMsg = msg.split("\n", 2);
		if (splitMsg.length == 1) {
			// Add generic message to alert
			Label label = new Label(splitMsg[0]);
			label.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
			pane.add(label, 0, 2);
		} else if (splitMsg.length > 1) {
			// Add generic message to alert
			Label label = new Label(splitMsg[0]);
			label.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
			pane.add(label, 0, 2);
			pane.setHalignment(label, HPos.CENTER);

			// Add all the missing parts to the alert
			if("MC OPERATIONS CHECK".equals(checkerName)){
				TextArea text = new TextArea(splitMsg[1]);
				text.setFont(Font.font("Verdana", FontWeight.NORMAL, 15));
				text.setPrefSize(1300, 300);
				text.setEditable(false);
				pane.add(text, 0, 3);
			}else{
				TextArea text = new TextArea(splitMsg[1]);
				text.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
				text.setEditable(false);
				pane.add(text, 0, 3);
			}
		}

		Button button = new Button("OK");
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dialogStage.close();

			}
		});
		
		pane.add(button, 0, 4);
		pane.setHalignment(button, HPos.CENTER);
		dialogStage.sizeToScene();
		dialogStage.showDialog();
		
	}
}
