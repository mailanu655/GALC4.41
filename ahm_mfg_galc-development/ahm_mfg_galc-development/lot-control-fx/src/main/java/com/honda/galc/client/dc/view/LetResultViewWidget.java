package com.honda.galc.client.dc.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.processor.LetResultProcessor;
import com.honda.galc.client.ui.EventBusUtil;
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
public class LetResultViewWidget extends LotControlOperationView {

	private static String LETCheck   = "LET Check";
	private static String REJECT   = "REJECT";
	
	private volatile Button letCheckBtn;

	public LetResultViewWidget(LetResultProcessor operationProcessor) {
		super(operationProcessor);
	}

	@Override
	public void init() {
		borderPane = new BorderPane();
		VBox vBox = new VBox(10);

		addLetCheckButton(vBox);
		borderPane.setBottom(vBox);
		this.setRight(borderPane);
	}

	public Button getCompleteRejectBtn() {
		if (letCheckBtn == null) {
			letCheckBtn = UiFactory.createButton("", StyleUtil.getCompleteBtnStyle(20), true);
			letCheckBtn.setPrefWidth(200);
			getCompleteRejectBtn().setText(LETCheck);
		}
		return letCheckBtn;
	}
	
	public void addLetCheckButton(VBox vBox) {
		getCompleteRejectBtn().setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				 if (getCompleteRejectBtn().getText().equals(REJECT)){
					 getCompleteRejectBtn().setText(LETCheck);
					EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.PDDA_REJECT, null));
					Logger.getLogger().check("Reject button is clicked");
				} else 	((LetResultProcessor) getProcessor()).broadcastDataContainer();
			}
		});

		vBox.getChildren().add(getCompleteRejectBtn());
	}

	
	@Override
	public void populateCollectedData(InstalledPart installedPart) {
		if (getOperation().getType().equals(OperationType.INSTRUCTION)
				&& installedPart.getInstalledPartStatus().equals(InstalledPartStatus.OK)) {
			getCompleteRejectBtn().setText(REJECT);
		} else {
			getCompleteRejectBtn().setText(LETCheck);
		}
	}
}
