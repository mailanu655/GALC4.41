package com.honda.galc.client.qi.lotControl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import static com.honda.galc.client.product.action.ProductActionId.CANCEL;
import static com.honda.galc.client.product.action.ProductActionId.DIRECTPASS;

import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.qi.base.AbstractQiProcessController;
import com.honda.galc.client.qi.base.QiProcessModel;
import com.honda.galc.client.ui.EventBusUtil;


/**
 * 
 * <h3>LotControlManualRepairController</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlManualRepairController description </p>
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
 * <TR>
 * <TD>Pooja Patidar</TD>
 * <TD>Apr 15, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Pooja Patidar
 * @since Aug 31, 2017
 */
public class LotControlManualRepairController extends AbstractQiProcessController<QiProcessModel, LotControlManualRepairView> implements EventHandler<ActionEvent> {

	public LotControlManualRepairController(QiProcessModel model,LotControlManualRepairView view) {
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
		getView().reload();
	}
	
	public ProductActionId[] getProductActionIds(){
		if(isCancelBtnDisable())  {
			return new ProductActionId[]{DIRECTPASS};
		} else 
			return new ProductActionId[]{CANCEL,DIRECTPASS};
	}

}
