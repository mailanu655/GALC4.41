package com.honda.galc.client.teamleader.scrap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.ExceptionalOutDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ReuseProductResultDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.ReuseProductResult;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ValidatorUtils;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>UnscrapController</code> is ... .
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
 * @created Aug 22, 2013
 */
public class UnscrapController extends BaseListener<UnscrapPanel> implements ActionListener {

	private BaseProduct product;

	public UnscrapController(UnscrapPanel view) {
		super(view);
	}

	// === actions === //
	@Override
	protected void executeActionPerformed(ActionEvent ae) {

		if (ae.getSource().equals(getView().getProductTypeComboBox())) {
			productTypeSelected();
			return;
		}

		if (ae.getSource().equals(getView().getInputNumberTextField())) {
			findProduct();
			return;
		}

		if (ae.getSource().equals(getView().getSubmitButton())) {
			unscrapProduct();
			return;
		}

		if (ae.getSource().equals(getView().getNextButton())) {
			toIdle();
			return;
		}
	}

	// === actions implementation === //
	protected void productTypeSelected() {
		getView().getInputNumberTextField().setText("");
		getView().getInputNumberTextField().requestFocus();
	}

	protected void findProduct() {

		ProductType productType = (ProductType) getView().getProductTypeComboBox().getSelectedItem();
		if (productType == null) {
			return;
		}

		String inputNumber = getView().getInputNumberTextField().getText();
		inputNumber = StringUtils.trim(inputNumber);
		getView().getInputNumberTextField().setText(inputNumber);

		if (StringUtils.isBlank(inputNumber)) {
			String msg = String.format("Input Number is required.");
			TextFieldState.ERROR.setState(getView().getInputNumberTextField());
			getView().getMainWindow().setErrorMessage(msg);
			return;
		}

		BaseProduct product = null;
		ProductDao<?> productDao = ProductTypeUtil.getProductDao(productType);
		if (productDao instanceof DiecastDao) {
			DiecastDao<?> diecastDao = (DiecastDao<?>) productDao;
			product = diecastDao.findByMCDCNumber(inputNumber);
		} else {
			product = productDao.findByKey(inputNumber);
		}

		if (product == null) {
			String msg = String.format("%s does not exist for input number %s.", productType.getProductName(), inputNumber);
			getView().getInputNumberTextField().selectAll();
			TextFieldState.ERROR.setState(getView().getInputNumberTextField());
			getView().getMainWindow().setErrorMessage(msg);
			return;
		}

		if (!DefectStatus.SCRAP.equals(product.getDefectStatus())) {
			String msg = "Product is not scrapped.";
			getView().getMainWindow().setErrorMessage(msg);
			TextFieldState.ERROR.setState(getView().getInputNumberTextField());
			getView().getInputNumberTextField().selectAll();
			getView().getInputNumberTextField().requestFocus();
			return;
		}

		List<HoldResult> holdResults = ServiceFactory.getDao(HoldResultDao.class).findAllByProductId(product.getProductId());
		getView().getHoldPane().reloadData(holdResults);

		setProduct(product);
		toProcessing();
	}

	protected void unscrapProduct() {

		if (!validate()) {
			return;
		}

		int retCode = JOptionPane.showConfirmDialog(getView(), "Are you sure you want to unscrap product ?", "Unscrap Product", JOptionPane.YES_NO_OPTION);
		if (retCode == JOptionPane.NO_OPTION) {
			return;
		}

		String associateId = getView().getAssociateIdTextField().getText();
		String reason = getView().getReasonTextField().getText();
		ReuseProductResult reuseProductResult = new ReuseProductResult(getProduct().getProductId(), associateId, reason);
		ProductType productType = (ProductType) getView().getProductTypeComboBox().getSelectedItem();
		ProductDao<?> productDao = ProductTypeUtil.getProductDao(productType);

		DefectStatus defectStatus = calculateDefectStatus();

		ServiceFactory.getDao(ExceptionalOutDao.class).deleteAllByProductId(getProduct().getProductId());
		logUserAction("deleted all ExceptionalOut by product ID: " + getProduct().getProductId());
		productDao.updateDefectStatus(getProduct().getProductId(), defectStatus);
		logUserAction("updated defect status: " + getProduct().getProductId());
		reuseProductResult = ServiceFactory.getDao(ReuseProductResultDao.class).save(reuseProductResult);
		logUserAction(SAVED, reuseProductResult);

		String msg = String.format("Product %s has ben unscrapped.", getProduct());
		Logger.getLogger().info(msg);

		toIdle();
	}

	// === state api === //
	protected void toProcessing() {
		TextFieldState.READ_ONLY.setState(getView().getInputNumberTextField());
		TextFieldState.EDIT.setState(getView().getAssociateIdTextField());
		TextFieldState.EDIT.setState(getView().getReasonTextField());

		getView().getProductTypeComboBox().setEnabled(false);
		getView().getSubmitButton().setEnabled(true);
		getView().getNextButton().requestFocus();
		getView().getNextButton().setEnabled(true);
	}

	protected void toIdle() {
		resetModel();
		resetUi();
		productTypeSelected();
		getView().getInputNumberTextField().requestFocus();
	}

	protected void resetModel() {
		setProduct(null);
	}

	protected void resetUi() {
		getView().getInputNumberTextField().setText("");
		getView().getAssociateIdTextField().setText("");
		getView().getReasonTextField().setText("");
		getView().getHoldPane().removeData();

		TextFieldState.EDIT.setState(getView().getInputNumberTextField());
		TextFieldState.DISABLED.setState(getView().getAssociateIdTextField());
		TextFieldState.DISABLED.setState(getView().getReasonTextField());

		getView().getProductTypeComboBox().setEnabled(true);
		getView().getSubmitButton().setEnabled(false);
		getView().getNextButton().setEnabled(false);
	}

	// === supporting api === //
	protected boolean validate() {

		String associateId = StringUtils.trim(getView().getAssociateIdTextField().getText());
		getView().getAssociateIdTextField().setText(associateId);
		String reason = StringUtils.trim(getView().getReasonTextField().getText());
		getView().getReasonTextField().setText(reason);

		LinkedHashMap<JTextField, String> errorMsgs = new LinkedHashMap<JTextField, String>();

		if (StringUtils.isBlank(associateId)) {
			errorMsgs.put(getView().getAssociateIdTextField(), "Associate Id is required !");
			TextFieldState.ERROR.setState(getView().getAssociateIdTextField());
		}

		if (StringUtils.isBlank(reason)) {
			TextFieldState.ERROR.setState(getView().getReasonTextField());
			errorMsgs.put(getView().getReasonTextField(), "Move Reason is required !");
		}

		if (!errorMsgs.isEmpty()) {
			String msg = ValidatorUtils.formatMessages(new ArrayList<String>(errorMsgs.values()), " ");
			JTextField first = errorMsgs.keySet().iterator().next();
			first.requestFocus();
			first.selectAll();
			getView().getMainWindow().setErrorMessage(msg);
			return false;
		}
		return true;
	}

	protected DefectStatus calculateDefectStatus() {
		List<DefectResult> defects = ServiceFactory.getDao(DefectResultDao.class).findAllByProductId(getProduct().getProductId());
		if (defects == null || defects.isEmpty()) {
			return DefectStatus.REPAIRED;
		}
		for (DefectResult dr : defects) {
			if (dr.getDefectStatusValue() != DefectStatus.REPAIRED.getId()) {
				return DefectStatus.OUTSTANDING;
			}
		}
		return DefectStatus.REPAIRED;
	}

	public List<ProductType> getProductTypes() {
		String property = getView().getMainWindow().getApplicationProperty("PRODUCT_TYPE_LIST");

		if (StringUtils.isBlank(property)) {
			return getDefaultProductTypes();
		}
		List<ProductType> types = new ArrayList<ProductType>();
		for (String str : property.split(Delimiter.COMMA)) {
			ProductType pt = ProductType.getType(StringUtils.trim(str));
			if (pt != null) {
				types.add(pt);
			}
		}
		if (types.isEmpty()) {
			return getDefaultProductTypes();
		}
		return types;
	}

	protected List<ProductType> getDefaultProductTypes() {
		List<ProductType> types = new ArrayList<ProductType>();
		types.add(ProductType.BLOCK);
		types.add(ProductType.HEAD);
		return types;
	}

	// === get/set === //
	protected BaseProduct getProduct() {
		return product;
	}

	protected void setProduct(BaseProduct product) {
		this.product = product;
	}
}
