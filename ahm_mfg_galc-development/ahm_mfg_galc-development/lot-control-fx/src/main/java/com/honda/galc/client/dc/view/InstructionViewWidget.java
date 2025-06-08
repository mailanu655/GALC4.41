package com.honda.galc.client.dc.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.KeypadEvent;
import com.honda.galc.client.utils.StyleUtil;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.OperationType;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;

/**
 * @author Subu Kathiresan
 * @date Jun 16, 2015
 */
public class InstructionViewWidget extends LotControlOperationView {

	private static String COMPLETE = "COMPLETE";
	private static String REJECT   = "REJECT";
	
	private volatile Button completeRejectBtn;
	private Timeline timer = new Timeline();

	public InstructionViewWidget(OperationProcessor opProcessor) {
		super(opProcessor);
	}

	@Override
	public void init() {
		borderPane = new BorderPane();
		VBox vBox = new VBox(10);

		addCompleteButton(vBox);
		borderPane.setBottom(vBox);
		this.setRight(borderPane);
	}

	public Button getCompleteRejectBtn() {
		if (completeRejectBtn == null) {
			completeRejectBtn = UiFactory.createButton(""
					, StyleUtil.getCompleteBtnStyle(getViosViewProperty().getCompleteRejectButtonFont())
					, true);
			completeRejectBtn.setPrefWidth(getViosViewProperty().getCompleteRejectButtonPrefWidth());
			setBtnImageAndText(COMPLETE);
		}
		return completeRejectBtn;
	}
	
	public void addCompleteButton(VBox vBox) {
		getCompleteRejectBtn().setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				
				if (getCompleteRejectBtn().getText().equals(COMPLETE)) {
					getCompleteRejectBtn().setDisable(true);
					EventBusUtil.publish(createDataCollectionEvent(DataCollectionEventType.PDDA_CONFIRM));
					Logger.getLogger().check("Complete button is clicked");
				} else {
					setBtnImageAndText(COMPLETE);
					EventBusUtil.publish(createDataCollectionEvent(DataCollectionEventType.PDDA_REJECT));
					checkAutoComplete();
					Logger.getLogger().check("Reject button is clicked");
				}
			}
		});

		vBox.getChildren().add(getCompleteRejectBtn());
	}

	private void setBtnImageAndText(String btnText) {
		Image imageComplete = new Image("resource/images/common/confirm.png");
		Image imageReject = new Image("resource/images/common/reject.png");
		getCompleteRejectBtn().setText(btnText);
		if (btnText.equals(COMPLETE)) {
			getCompleteRejectBtn().setGraphic(StyleUtil.normalizeImage(new ImageView(imageComplete)
					, getViosViewProperty().getCompleteRejectNormalizedImageSize()));
		} else {
			getCompleteRejectBtn().setGraphic(StyleUtil.normalizeImage(new ImageView(imageReject)
					, getViosViewProperty().getCompleteRejectNormalizedImageSize()));
		}
	}
	
	@Subscribe
	public void handle(KeypadEvent event) {
	
		switch (event.getEventType()) {
		    // Dont let the user reject an instruction via the clicker
			case KEY_COMPLETE:
				if (getCompleteRejectBtn() != null &&
					getCompleteRejectBtn().getText().equals(COMPLETE) &&
					getCompleteRejectBtn().isDisabled() == false) {
					getCompleteRejectBtn().fire();
				}
				break;
			case KEY_REJECT:
				if (getCompleteRejectBtn() != null &&
					getCompleteRejectBtn().getText().equals(REJECT) &&
					getCompleteRejectBtn().isDisabled() == false) {
					getCompleteRejectBtn().fire();
				}
				break;
			default:
				break;
		}
	}
	
	public void checkAutoComplete() {
		timer.stop();
		if (getOperation().getType().equals(OperationType.GALC_AUTO_COMPLETE) && getCompleteRejectBtn().getText().equals(COMPLETE)) {

			KeyFrame keyFrame = new KeyFrame(Duration.seconds(getModel().getOpsTimeMap().get(getOperation().getId().getOperationName())),
					new EventHandler<ActionEvent>() {
				public void handle(javafx.event.ActionEvent event) {
					getCompleteRejectBtn().fire();
				}
			});
			timer.getKeyFrames().setAll(keyFrame);
			timer.play();
		}
	}

	public Timeline getTimer() {
		return timer;
	}

	@Override
	public void populateCollectedData(InstalledPart installedPart) {
		if ((getOperation() == null
				|| getOperation().getType().equals(OperationType.INSTRUCTION) 
				|| getOperation().getType().equals(OperationType.GALC_INSTRUCTION)
				|| getOperation().getType().equals(OperationType.GALC_AUTO_COMPLETE)
				|| getOperation().getType().equals(OperationType.GALC_MADE_FROM))
				&& installedPart.getInstalledPartStatus().equals(InstalledPartStatus.OK)) {
			setBtnImageAndText(REJECT);
		} else {
			setBtnImageAndText(COMPLETE);
		}
	}
}
