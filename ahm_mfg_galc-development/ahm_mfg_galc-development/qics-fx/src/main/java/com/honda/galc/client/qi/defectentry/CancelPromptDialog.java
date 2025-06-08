package com.honda.galc.client.qi.defectentry;

import com.honda.galc.client.product.NavigateTabEvent;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.qi.base.QiFxDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CancelPromptDialog extends QiFxDialog<DefectEntryModel> implements EventHandler<ActionEvent>{

	private LoggedButton contEnteringDefectBtn;
	private LoggedButton doneWithProductBtn;
	private LoggedButton directPassProductBtn;
	private LoggedButton cancelBtn;
	private ProductController productController;
	private EventHandler<ActionEvent> myHandlerClass;
	
	
	public CancelPromptDialog(String title, DefectEntryModel model,ProductController productController,EventHandler<ActionEvent> handlerClass, boolean isDefectEntered) {
		super(title, productController.getView().getApplicationId(), model);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
	
		myHandlerClass = handlerClass;
		initComponents(isDefectEntered);
		setModel(model);		
		this.productController=productController;
	}


	private void initComponents(boolean isDefectEntered) {
		

		VBox outerPane = new VBox();
		
		outerPane.getChildren().addAll(createButtonContainer(isDefectEntered));
		
		outerPane.setPrefSize(400, 200);
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
		
		outerPane.setVisible(true);
		
	}
	
	private VBox createButtonContainer(boolean isDefectEntered) {
		VBox buttonContainer = new VBox();
		contEnteringDefectBtn = UiFactory.createButton("Continue Entering Defects");
		
		contEnteringDefectBtn.setOnAction(myHandlerClass);
		
		cancelBtn = UiFactory.createButton("Do Not report on this Product(Cancel)");
		cancelBtn.setOnAction(myHandlerClass);
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(5,5,5,5));
		buttonContainer.setSpacing(10);
		
		if(isDefectEntered) {
			doneWithProductBtn = UiFactory.createButton("Done With Product");
			doneWithProductBtn.setOnAction(myHandlerClass);
			buttonContainer.getChildren().addAll(contEnteringDefectBtn, doneWithProductBtn, cancelBtn);
		} else {
			directPassProductBtn = UiFactory.createButton("Direct Pass Product");
			directPassProductBtn.setOnAction(myHandlerClass);
			buttonContainer.getChildren().addAll(contEnteringDefectBtn,directPassProductBtn,cancelBtn);
		}
		
		
		return buttonContainer;
	
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedButton) {
			
			if(actionEvent.getSource().equals(contEnteringDefectBtn)){
				Stage stage = (Stage) contEnteringDefectBtn.getScene().getWindow();
				stage.close();
				
			}else if(actionEvent.getSource().equals(directPassProductBtn)) {
				
			    Stage stage = (Stage) directPassProductBtn.getScene().getWindow();
				stage.close();
				
				NavigateTabEvent processEvent = new NavigateTabEvent();
				processEvent.setNavigateToTab(ViewId.PRODUCT_CHECK.getViewLabel());
				EventBusUtil.publish(processEvent);
			} else if(actionEvent.getSource().equals(doneWithProductBtn)) {
				
				Stage stage = (Stage) doneWithProductBtn.getScene().getWindow();
				stage.close();
				
				NavigateTabEvent processEvent = new NavigateTabEvent();
				processEvent.setNavigateToTab(ViewId.PRODUCT_CHECK.getViewLabel());
				EventBusUtil.publish(processEvent);
			} else if(actionEvent.getSource().equals(cancelBtn)) {
				Stage stage = (Stage) cancelBtn.getScene().getWindow();
				stage.close();
				productController.cancelWithPrompt();
			} else {
				//TODO no valid action selected
			}
		} 
	}		
}

