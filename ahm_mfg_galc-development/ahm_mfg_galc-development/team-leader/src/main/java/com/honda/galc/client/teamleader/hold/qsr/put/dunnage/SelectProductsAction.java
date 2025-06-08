package com.honda.galc.client.teamleader.hold.qsr.put.dunnage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.command.ChainCommand;
import com.honda.galc.client.product.command.Command;
import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.product.validator.AlphaNumericValidator;
import com.honda.galc.client.product.validator.RequiredValidator;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>SelectProductsAction</code> is ... .
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Apr 30, 2013
 */
public class SelectProductsAction extends BaseListener<HoldDunnagePanel> implements ActionListener {

	private ChainCommand validator;
	static final String LAST_PROCESS_POINT_NAME_TAG = "lastProcessPointName";
	
	public SelectProductsAction(HoldDunnagePanel parentPanel) {
		super(parentPanel);
		this.validator = createValidator();
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {

		getView().getProductPanel().removeData();

		String dunnageNumber = getView().getInputPanel().getNumberTextField().getText();

		if (dunnageNumber != null) {
			dunnageNumber = StringUtils.trim(dunnageNumber);
			getView().getInputPanel().getNumberTextField().setText(dunnageNumber);
		}

		if (!UiUtils.isValid(getView().getInputPanel().getNumberTextField(), getValidator(), getMainWindow())) {
			return;
		}

		List<BaseProduct> products = selectProducts(dunnageNumber);

		if (products == null || products.isEmpty()) {
			getView().getMainWindow().setErrorMessage("No Products exist for Dunnage");
			TextFieldState.ERROR.setState(getView().getInputPanel().getNumberTextField());
			getView().getInputPanel().getNumberTextField().selectAll();
			getView().getInputPanel().getNumberTextField().requestFocus();
			return;
		}

		populateTable(products);
		getView().getInputPanel().getNumberTextField().requestFocus();
	}

	protected List<BaseProduct> selectProducts(String dunnageNumber) {
		ProductDao<? extends BaseProduct> dao = getProductDao(getView().getProductType());
		List<? extends BaseProduct> list = dao.findAllByDunnage(dunnageNumber);
		List<BaseProduct> products = new ArrayList<BaseProduct>();
		if (list != null) {
			products.addAll(list);
		}
		return products;
	}

	protected void populateTable(List<BaseProduct> products) {
		List<Map<String, Object>> list = getView().getProductPanel().getItems();
		for (BaseProduct product : products) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("product", product);
			map.put(LAST_PROCESS_POINT_NAME_TAG, product.getLastPassingProcessPointId() == null ? "" : getProcessPointName(product.getLastPassingProcessPointId()));

			list.add(map);
		}
		getView().getProductPanel().reloadData(list);
	}

	protected ChainCommand createValidator() {
		List<Command> validators = new ArrayList<Command>();
		validators.add(new RequiredValidator());
		validators.add(new AlphaNumericValidator());
		ChainCommand validator = ChainCommand.create(validators, "Dunnage");
		validator.setShortCircuit(true);
		return validator;
	}
	
	protected Object getProcessPointName(String processPointId) {
		return  ServiceFactory.getDao(ProcessPointDao.class).findById(processPointId).getProcessPointName();
	}

	// === get/set === //
	public ChainCommand getValidator() {
		return validator;
	}
}
