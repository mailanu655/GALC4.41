package com.honda.galc.client.dataRecovery;

import static com.honda.galc.client.product.action.ProductActionId.CANCEL;
import static com.honda.galc.client.product.action.ProductActionId.DIRECTPASS;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.qi.base.AbstractQiProcessController;
import com.honda.galc.client.qi.base.QiProcessModel;
import com.honda.galc.client.ui.EventBusUtil;

/**
 * <h3>RecoveryController Class description</h3>
 * <p> RecoveryController description </p>
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
 * @author L&T Infotech<br>
 *
 */
public class DieCastRecoveryController extends AbstractQiProcessController<QiProcessModel, DieCastRecoveryView> implements EventHandler<ActionEvent>  {

	public DieCastRecoveryController(QiProcessModel model, DieCastRecoveryView view) {
		super(model, view);
		EventBusUtil.register(this);
	}

	@Override
	public void handle(ActionEvent arg0) {
	}

	@Override
	public void initializeListeners() {
	}

	@Override
	public void initEventHandlers() {
		getView().renderProductPanel();
		getView().getDataPanel().getController().setProduct(getModel().getProductModel().getProduct());
		getView().getDataPanel().getController().selectBuildResults(getView().getDataPanel().getPartNames());
		getView().getDataPanel().setInputMode();
		
	}
	
	/**
	 * This method is used to create Product Panel Buttons
	 */
	public ProductActionId[] getProductActionIds(){
		if(isCancelBtnDisable())  {
			return new ProductActionId[]{DIRECTPASS};
		} else
		return new ProductActionId[]{CANCEL,DIRECTPASS};
	}
}
