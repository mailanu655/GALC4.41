package com.honda.galc.client.dc.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.processor.OperationProcessor;
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
public class InstructionWithoutCompleteBtnViewWidget extends LotControlOperationView {

	private static String REJECT   = "REJECT";
	private volatile Button RejectBtn;

	public InstructionWithoutCompleteBtnViewWidget(OperationProcessor operationProcessor) {
		super(operationProcessor);
	}

	@Override
	public void init() {
	}

	public Button getRejectBtn() {
		if (RejectBtn == null) {
			RejectBtn = UiFactory.createButton("", StyleUtil.getCompleteBtnStyle(20), true);
			RejectBtn.setPrefWidth(200);
			getRejectBtn().setText(REJECT);
		}
		return RejectBtn;
	}
	
	public void addRejectButton(VBox vBox) {
		getRejectBtn().setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				 if (getRejectBtn().getText().equals(REJECT)){
					 borderPane.setBottom(new VBox());
					EventBusUtil.publish(createDataCollectionEvent(DataCollectionEventType.PDDA_REJECT));
					Logger.getLogger().check("Reject button is clicked");
				} 
			}
		});
		vBox.getChildren().add(getRejectBtn());

	}
	
	@Override
	public void populateCollectedData(InstalledPart installedPart) {
		if ((getOperation() == null ||
			 getOperation().getType().equals(OperationType.INSTRUCTION) || 
			 getOperation().getType().equals(OperationType.GALC_INSTRUCTION) )
			 && installedPart.getInstalledPartStatus().equals(InstalledPartStatus.OK)) {
			borderPane = new BorderPane();
			VBox vBox = new VBox(10);
			borderPane.setBottom(vBox);
			addRejectButton(vBox);
			this.setRight(borderPane);
		}
	}
}
