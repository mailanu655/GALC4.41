package com.honda.galc.client.qi.defectentry;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.mvc.AbstractView;
import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiConstant;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public  class CancelPromptActionHandler {
	
	ActionEvent actionEvent;
	ProductController productController;
	EventHandler<ActionEvent> handlerClass;
	
	public CancelPromptActionHandler(ActionEvent actionEvent,ProductController productController,EventHandler<ActionEvent> handlerClass) {
		this.actionEvent=actionEvent;
		this.productController=productController;
		this.handlerClass =  handlerClass;
	}
	
	public void perfromCancelPromptAction() {
		if(actionEvent.getSource() instanceof LoggedButton)  {
			
			LoggedButton btn = (LoggedButton) actionEvent.getSource();
			if(btn.getId().equalsIgnoreCase(ProductActionId.CANCEL_DONE.getActionName()))  {
				CancelPromptDialog dialog = new CancelPromptDialog(QiConstant.CREATE,null,this.productController,handlerClass, true);
				dialog.showDialog();
			}else if(btn.getId().equalsIgnoreCase(ProductActionId.CANCEL_DIRECT_PASS.getActionName()))  {
				CancelPromptDialog dialog = new CancelPromptDialog(QiConstant.CREATE,null,this.productController,handlerClass, false);
				dialog.showDialog();
			}else if (btn.getId().equalsIgnoreCase(QiConstant.BTN_CONTINUE_DEFECT_ENTRY))  {
				Stage stage = (Stage) (btn.getScene().getWindow());
				stage.close();
			} else if(btn.getId().equalsIgnoreCase(QiConstant.BTN_DIRECT_PASS)) {
				Stage stage = (Stage)(btn.getScene().getWindow());
				stage.close();
								
				AbstractView<?, ?> view = (AbstractView<?, ?>) this.productController.getView().getProductProcessPane().getSelectedProcessView();
				EventBusUtil.publishAndWait(new ProductEvent(view.getViewLabel(), ProductEventType.PRODUCT_DIRECT_PASSED));
				
			} else if(btn.getId().equalsIgnoreCase(QiConstant.BTN_DONE)) {
				Stage stage = (Stage)(btn.getScene().getWindow());
				stage.close();

				AbstractView<?, ?> view = (AbstractView<?, ?>) this.productController.getView().getProductProcessPane().getSelectedProcessView();
				EventBusUtil.publishAndWait(new ProductEvent(view.getViewLabel(), ProductEventType.PRODUCT_DEFECT_DONE));
				
			} else if (btn.getId().equalsIgnoreCase(QiConstant.BTN_CANCEL))  {
				Stage stage = (Stage)(btn.getScene().getWindow());
				stage.close();				
				if (this.productController.getView().getProductProcessPane().isNavigatedFromRepairEntry()) {
					EventBusUtil.publishAndWait(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_REPAIR_DEFECT_DONE));
				} else {
					this.productController.cancelWithPrompt();

				}			
			} 

		}


	}
}
