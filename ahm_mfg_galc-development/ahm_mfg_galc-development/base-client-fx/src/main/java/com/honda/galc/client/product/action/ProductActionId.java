package com.honda.galc.client.product.action;

import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.util.ReflectionUtils;

/**
 * 
 * 
 * <h3>ActionId Class description</h3>
 * <p> ActionId description </p>
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
 * Mar 10, 2014
 *
 *
 */
public enum ProductActionId {

    CANCEL("CANCEL","com.honda.galc.client.product.action.CancelAction"),
	CANCEL_DIRECT_PASS("Cancel","com.honda.galc.client.qi.defectentry.CancelPromptDirectPassAction"), 
	CANCEL_DONE("Cancel Done","com.honda.galc.client.qi.defectentry.CancelPromptDoneAction"), 
	COMPLETE("COMPLETE","com.honda.galc.client.product.action.CompleteAction"),
	SKIP("SKIP","com.honda.galc.client.product.action.SkipAction"),
	SUBMIT("DONE","com.honda.galc.client.product.action.SubmitAction"),
	SCRAP("SCRAP","com.honda.galc.client.product.action.ScrapAction"),
	DIRECTPASS("DIRECT PASS","com.honda.galc.client.product.action.DirectPassAction"),
	KEYBOARD("KEYBOARD","com.honda.galc.client.product.action.KeyboardAction"),
	PRODUCT_CHECK_DONE("DONE","com.honda.galc.client.product.action.ProductCheckDoneAction"),
	SEND_FINAL("SEND TO FINAL","com.honda.galc.client.product.action.SendToFinalAction"),
	UPDATE_REPAIR_AREA("UPDATE REPAIR AREA","com.honda.galc.client.product.action.UpdateRepairAreaAction");

	private String actionName;
	private String  actionClassName;
	
	
	private ProductActionId(String actionName,String actionClassName) {
		this.actionName = actionName;
		this.actionClassName = actionClassName;
	}
	
	public String getActionName() {
		return actionName;
	}
	
	public String getActionClassName() {
		return actionClassName;
	}
	

	public AbstractProductAction createProductAction(ProductController controller) {
		
		Class<?> clazz = getProductActionClass();
		return (AbstractProductAction)ReflectionUtils.createInstance(clazz, new Class<?>[] {ProductController.class}, controller);
	}
	
	private Class<?> getProductActionClass(){
		try {
			Class<?> clazz = Class.forName(actionClassName);
			if(AbstractProductAction.class.isAssignableFrom(clazz)) 
				return clazz;
		} catch (ClassNotFoundException e) {
			 
		}
		return null;
	}
	
	public static ProductActionId getActionId(String actionName) {
		
		for(ProductActionId actionId :values()) {
			if(actionId.name().equalsIgnoreCase(actionName))	
				return actionId;
		}
		return null;
	}
	
}
