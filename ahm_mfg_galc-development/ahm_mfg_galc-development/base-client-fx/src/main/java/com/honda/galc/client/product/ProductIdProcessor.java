package com.honda.galc.client.product;

import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductTypeData;

/**
 * 
 * 
 * <h3>ProductIdProcessor Class description</h3>
 * <p> ProductIdProcessor description </p>
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
 * Mar 11, 2014
 *
 *
 */
public class ProductIdProcessor extends AbstractProductIdProcessor{

	protected BaseProduct product;

	public ProductIdProcessor(ProductController productIdController) {
		super(productIdController);
	}

	public boolean validateProduct(){
		return true;
	}

	@Override
	public void processInputNumber(ProductEvent event) {

		setProductIdInputNumber(event);
		product = validateAndCreateBaseProduct();

		if (product == null) {
			ProductTypeData productTypeData = getProductController().getView().getMainWindow().getApplicationContext().getProductTypeData();
			String productType = productTypeData == null ? "Product" : productTypeData.getProductTypeLabel();
			String msg = String.format("%s does not exist for: %s", productType, getProductController().getView().getInputPane().getProductIdField().getText());
			getProductController().getView().setErrorMessage(msg, getProductController().getView().getInputPane().getProductIdField());
			//play NG sound
			getProductController().getAudioManager().playNGSound();
			return ;
		}

		if(!validateProduct())  {			
			getProductController().getAudioManager().playNGSound();	//play NG sound		
		}
		else  {
			getProductController().getAudioManager().playOKSound();  //play OK sound
		}
	}

	@Override
	public void productReset(ProductEvent event) {
		this.product = null;
	}

	protected void setProductIdInputNumber(ProductEvent event) {
		if (null!=event) {
			getProductController().getView().getInputPane().getProductIdField().setText((String)event.getTargetObject());
		}
	}

}
