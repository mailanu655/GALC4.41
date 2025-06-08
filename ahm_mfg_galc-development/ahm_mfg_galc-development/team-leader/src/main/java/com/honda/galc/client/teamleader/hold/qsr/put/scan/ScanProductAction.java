package com.honda.galc.client.teamleader.hold.qsr.put.scan;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.command.ChainCommand;
import com.honda.galc.client.product.command.Command;
import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.product.validator.RequiredValidator;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.qsr.QsrAction;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ScanProductAction</code> is ...
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>Karol Wozniak</TD>
 * <TD>Mar 29, 2010</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class ScanProductAction extends QsrAction<ScanProductPanel> implements ActionListener {

	private ChainCommand validator;
	private ProductTypeData productTypeData;
	
	public ScanProductAction(ScanProductPanel parentPanel) {
		super(parentPanel);
		this.validator = createValidator();
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {
		String inputNumber = getView().getInputPanel().getNumberTextField().getText();
		if (inputNumber != null) {
			inputNumber = StringUtils.trim(inputNumber);
			getView().getInputPanel().getNumberTextField().setText(inputNumber);
		}

		if (!UiUtils.isValid(getView().getInputPanel().getNumberTextField(), getValidator(), getMainWindow())) {
			return;
		}

		if (isProductAlreadyAdded(inputNumber)) {
			JOptionPane.showMessageDialog(getView(), "This product already has been added");
			getView().getInputPanel().getNumberTextField().selectAll();
			getView().getInputPanel().getNumberTextField().requestFocus();
			return;
		}

		try
		{			
			BaseProduct product = findProduct(inputNumber);
	
			if (product == null){
				ProductType type = this.getView().getProductType();
				productTypeData = ServiceFactory.getDao(ProductTypeDao.class).findByKey(type.toString());
				if(Config.isDisableProductIdCheck(type.toString())){
					if(productTypeData.isNumberValid(inputNumber)){
						product = ProductTypeUtil.createProduct(type.toString(), inputNumber);
					} else {
						getView().getMainWindow().setErrorMessage("Invalid Product ID: "+ inputNumber +". Length invalid.");
						return;
					}
				} else {
					getView().getMainWindow().setErrorMessage("Product does not exist in database");
					TextFieldState.ERROR.setState(getView().getInputPanel().getNumberTextField());
					getView().getInputPanel().getNumberTextField().selectAll();
					getView().getInputPanel().getNumberTextField().requestFocus();
					return;
				}
			}

			populateTable(product);
		
			getView().getInputPanel().getNumberTextField().setText("");
			getView().getInputPanel().getNumberTextField().requestFocus();
		} catch(ServiceInvocationException ex){
			Logger.getLogger( this.getView().getApplicationId() ).error(Arrays.toString(ex.getStackTrace()));
			getView().getMainWindow().setErrorMessage("Product does not exist in database");
			TextFieldState.ERROR.setState(getView().getInputPanel().getNumberTextField());
		}
	}

	protected boolean isProductAlreadyAdded(String inputNumber) {

		List<Map<String, Object>> data = getView().getProductPanel().getItems();
		for (Map<String, Object> map : data) {
			BaseProduct product = (BaseProduct) map.get("product");
			if (inputNumber.equals(product.getProductId())) {
				return true;
			}
			if (product instanceof DieCast) {
				DieCast dc = (DieCast) product;
				if (inputNumber.equals(dc.getDcSerialNumber())) {
					return true;
				}
				if (inputNumber.equals(dc.getMcSerialNumber())) {
					return true;
				}

			}
		}
		return false;
	}

	protected BaseProduct findProduct(String inputNumber) {
		ProductDao<?> productDao = getProductDao(getView().getProductType());
		BaseProduct product = productDao.findBySn(inputNumber);
		return product;
	}
	
	protected void populateTable(BaseProduct product) {
		List<Map<String, Object>> holdRecords = this.prepareHoldRecords(product);
		List<Map<String, Object>> currentTableResords = getView().getProductPanel().getItems();
		holdRecords.addAll(currentTableResords);
		getView().getProductPanel().reloadData(holdRecords);
	}

	protected ChainCommand createValidator() {
		List<Command> validators = new ArrayList<Command>();
		validators.add(new RequiredValidator());
		ChainCommand validator = ChainCommand.create(validators, "Product Number");
		validator.setShortCircuit(true);
		return validator;
	}

	public ChainCommand getValidator() {
		return validator;
	}
}
