package com.honda.galc.client.teamleader.mbpn;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PreProductionLot;

public class MbpnProductChangeController implements ActionListener {
	private MainWindow window;
	private MbpnProductChangePanel panel;

	private static final ProductType PRODUCT_TYPE = ProductType.MBPN;

	private MbpnDao mbpnDao;
	private MbpnProductDao mbpnProductDao;
	private InstalledPartDao installedPartDao;
	private PreProductionLotDao preProdLotDao;
	
	public MbpnProductChangeController(MainWindow window, MbpnProductChangePanel panel) {
		this.window = window;
		this.panel = panel;

		initialize();
	}

	private void initialize() {
		try {
			this.addListeners();
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void addListeners() {
		panel.getProductIdBtn().addActionListener(this);
		panel.getNewSpecCodeComboBox().getComponent().addActionListener(this);
		panel.getUpdateBtn().addActionListener(this);
		panel.getCancelBtn().addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(panel.getProductIdBtn())) {
			showAndProcessManualSearch();
		} else if (e.getSource().equals(panel.getNewSpecCodeComboBox().getComponent())) {
			if (panel.getNewSpecCodeComboBox().getComponent().getSelectedIndex() == 0)
				panel.getUpdateBtn().setEnabled(false);
			else
				panel.getUpdateBtn().setEnabled(true);
		} else if (e.getSource().equals(panel.getUpdateBtn())) {
			String replacementSpecCode = (String) panel.getNewSpecCodeComboBox().getComponent().getSelectedItem();
			String productId = panel.getProductIdTxtField().getText();
			String currentSpecCode = panel.getCurrentSpecCodeTxtField().getComponent().getText();

			String confirm_message = "Are you sure you want to change the spec code of " + productId + " from " + currentSpecCode + " to " + replacementSpecCode + "?"
					+ "\nNote: This will set the build results for " + productId + " and marriage build result to NG";
			if (JOptionPane.showConfirmDialog(panel, confirm_message, "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				updateProductSpecCode(replacementSpecCode);
				panel.setCursor(Cursor.getDefaultCursor());
			}
		} else if (e.getSource().equals(panel.getCancelBtn())) {
			resetPanel();
		}
	}

	private void resetPanel() {
		//reset text fields
		panel.getProductIdTxtField().setText("");
		panel.getCurrentSpecCodeTxtField().getComponent().setText("");
		resetCurrentSpecCodeBg();
		panel.getOrderNoTxtField().getComponent().setText("");
		panel.getTrackingStatusTxtField().getComponent().setText("");
		panel.getLastProcessPointTxtField().getComponent().setText("");

		//reset and disable spec code combobox
		resetComboBox();

		//disable buttons
		panel.getUpdateBtn().setEnabled(false);
		panel.getCancelBtn().setText("Cancel");
		panel.getCancelBtn().setEnabled(false);

		//clear any panel message
		window.clearMessage();
	}

	private void resetComboBox() {
		panel.getNewSpecCodeComboBox().getComponent().removeAllItems();
		panel.getNewSpecCodeComboBox().getComponent().addItem("Select");
		panel.getNewSpecCodeComboBox().getComponent().setSelectedIndex(0);
		panel.getNewSpecCodeComboBox().setEnabled(false);
	}
	
	private void setComboBox(List<Mbpn> validSpecCodes, String currentSpecCode) {
		//populate combobox with valid spec codes
		for (Mbpn mbpn : validSpecCodes) {
			String validSpecCode = mbpn.getProductSpecCode();
			if (!validSpecCode.equals(currentSpecCode) && mbpn.getDisabled() != 1)
				panel.getNewSpecCodeComboBox().getComponent().addItem(validSpecCode);
		}
		panel.getNewSpecCodeComboBox().getComponent().setSelectedIndex(0);

		//enable new spec code combo box
		panel.getNewSpecCodeComboBox().setEnabled(true);
	}

	private void setTextFields(MbpnProduct product) {
		panel.getCurrentSpecCodeTxtField().getComponent().setText(product.getProductSpecCode());
		panel.getOrderNoTxtField().getComponent().setText(product.getCurrentOrderNo());
		panel.getTrackingStatusTxtField().getComponent().setText(product.getTrackingStatus());
		panel.getLastProcessPointTxtField().getComponent().setText(product.getLastPassingProcessPointId());
	}

	private void updateProductSpecCode(String specCode) {
		//update spec code for mbpn
		String productId = panel.getProductIdTxtField().getText().trim();
		MbpnProduct product = findProduct(productId);
		String currSpecCode = product.getCurrentProductSpecCode();
		
		product.setCurrentProductSpecCode(specCode);
		getMbpnProductDao().save(product);

		InstalledPartStatus ngStatus = InstalledPartStatus.NG;

		//find mbpn build result and set all parts status to not completed
		List<InstalledPart> mbpnBuildResult = getInstalledPartDao().findAllByProductId(productId);
		if (mbpnBuildResult.size() > 0) {
			//check if installed part status is NM (not married) ??
			for (InstalledPart p : mbpnBuildResult) {
				p.setInstalledPartStatus(ngStatus);
			}
			getInstalledPartDao().saveAll(mbpnBuildResult);
		}

		//find marriage build result and set status to not completed
		List<InstalledPart> marriageBuildResult = getInstalledPartDao().findAllByPartSerialNumber(productId);
		if (marriageBuildResult.size() > 0) {
			for (InstalledPart p : marriageBuildResult) {
				p.setInstalledPartStatus(ngStatus);
			}
			getInstalledPartDao().saveAll(marriageBuildResult);
		}

		panel.getLogger().info(productId + " spec code changed from " + currSpecCode + " to " + specCode + ".");
		window.setMessage("Spec code changed successfully.");

		panel.getCancelBtn().setText("Done");

		setTextFields(product);
		resetComboBox();
		resetCurrentSpecCodeBg();
	}

	private void showAndProcessManualSearch() {
		ManualProductEntryDialog productEntry = new ManualProductEntryDialog(panel.getMainWindow(), PRODUCT_TYPE.name(),ProductNumberDef.getProductNumberDef(PRODUCT_TYPE).get(0).getName());
		productEntry.setModal(true);
		productEntry.setVisible(true);
		String productId = productEntry.getResultProductId();
		if (!(productId == null || StringUtils.isEmpty(productId))) {
			resetPanel();

			//set mbpn text field
			panel.getProductIdTxtField().setText(productId);
			panel.getProductIdTxtField().requestFocusInWindow();

			//enable cancel button (for reset)
			panel.getCancelBtn().setEnabled(true);

			MbpnProduct mbpnProduct = findProduct(productId);

			//set text fields
			setTextFields(mbpnProduct);
			
			PreProductionLot prodLot = findPreProdLot(mbpnProduct.getCurrentOrderNo());
			
			if (mbpnProduct.getProductSpecCode().equals("")) {
				window.setErrorMessage("Cannot set spec code for product " + mbpnProduct.getProductId() + ". Current spec code is undefined. Please contact IT.");
			} else if (prodLot != null) {
				if (matchesProdLotSpecCode(prodLot, mbpnProduct)) {
					setCurrentSpecCodeBg(Color.GREEN);
					window.setMessage("Cannot change spec code. Current spec code matches the production lot.");
				} else {
					setCurrentSpecCodeBg(Color.RED);
					window.setErrorMessage("Spec code is invalid. Current spec code does not match production lot.");
					setComboBox(loadValidMbpn(mbpnProduct, matchesProdLotSpecCode(prodLot, mbpnProduct)), mbpnProduct.getCurrentProductSpecCode());
				}
			} else {
				//true for matchesProdLotSpecCode because production lot is undefined
				setComboBox(loadValidMbpn(mbpnProduct, true), mbpnProduct.getCurrentProductSpecCode());
			}
		}
	}
	
	//checks if MBPN current product spec code is valid based on the product's production lot
	private boolean matchesProdLotSpecCode(PreProductionLot prodLot, MbpnProduct product) {
		return prodLot.getProductSpecCode().equals(product.getCurrentProductSpecCode());
	}

	private List<Mbpn> loadValidMbpn(MbpnProduct product, boolean matchesProdLotSpecCode) {
		List<Mbpn> validMbpn = new ArrayList<>();
		
		//if current product spec code does not match prod lot spec code show only the valid spec code
		if (!matchesProdLotSpecCode) {
			//get the valid spec code via preprodlot
			String validSpecCode = findPreProdLot(product.getCurrentOrderNo()).getProductSpecCode();
			validMbpn.add(getMbpnDao().findByKey(validSpecCode));
		} else {
			//get main no from current spec code
			List<String> base5 = new ArrayList<>();
			base5.add(product.getCurrentProductSpecCode().substring(0, 5));

			//find all mbpn for that main no
			validMbpn = getMbpnDao().findAllByMainNo(base5);
		}
		
		validMbpn.sort((o1, o2) -> o1.getId().compareTo(o2.getId()));
		
		return validMbpn;
	}

	private MbpnProduct findProduct(String productId) {
		return getMbpnProductDao().findBySn(productId);
	}
	
	private PreProductionLot findPreProdLot(String prodLot) {
		return getPreProdLotDao().findByKey(prodLot);
	}

	private MbpnDao getMbpnDao() {
		if (mbpnDao == null) {
			mbpnDao = getDao(MbpnDao.class);
		}
		return mbpnDao;
	}

	private MbpnProductDao getMbpnProductDao() {
		if (mbpnProductDao == null) {
			mbpnProductDao = getDao(MbpnProductDao.class);
		}
		return mbpnProductDao;
	}

	private InstalledPartDao getInstalledPartDao() {
		if (installedPartDao == null) {
			installedPartDao = getDao(InstalledPartDao.class);
		}
		return installedPartDao;
	}
	
	private PreProductionLotDao getPreProdLotDao() {
		if (preProdLotDao == null) {
			preProdLotDao = getDao(PreProductionLotDao.class);
		}
		return preProdLotDao;
	}
	
	private void setCurrentSpecCodeBg(Color color) {
		panel.getCurrentSpecCodeTxtField().getComponent().setBackground(color);
	}
	
	private void resetCurrentSpecCodeBg() {
		panel.getCurrentSpecCodeTxtField().getComponent().setBackground(new Color(238, 238, 238));
	}

	protected void handleException(Exception e) {
		if(e != null) {
			panel.getLogger().error(e, "unexpected exception occurs: " + e.getMessage());
			window.setMessage(e.getMessage());
		} else {
			window.clearMessage();
		}
	}

}