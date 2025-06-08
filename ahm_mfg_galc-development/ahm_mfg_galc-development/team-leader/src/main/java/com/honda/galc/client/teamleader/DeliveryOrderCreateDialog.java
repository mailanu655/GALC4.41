/**
 * 
 */
package com.honda.galc.client.teamleader;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.LineBorder;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.PurchaseContractDao;
import com.honda.galc.entity.product.PurchaseContract;
import com.honda.galc.property.DeliveryOrderAdjustmentPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

import net.miginfocom.swing.MigLayout;

/**
 * 
 */
public class DeliveryOrderCreateDialog extends JDialog implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DeliveryOrderAdjustmentPanel parentPanel;
	
	private LabeledTextField salesModel;  				//sales_model_code
	private LabeledTextField salesExtColor;				//sales_ext_color_code
	private LabeledTextField purchaseContractNumber;
	private LabeledTextField salesModelType;			//sales_model_type_code
	private LabeledTextField salesIntColor;				//sales_int_color_code
	private LabeledTextField orderUnit;
	
	//order due date
	private JPanel dueDatePickerPanel;
	private JLabel dueDatePickerLabel;
	private JDatePanelImpl orderDueDatePanel;
	private JDatePickerImpl orderDueDatePicker;
	
	//order receive date
	private JPanel receiveDatePickerPanel;
	private JLabel receiveDatePickerLabel;
	private JDatePanelImpl orderReceiveDatePanel;
	private JDatePickerImpl orderReceiveDatePicker;
	
	private JButton createButton;
	private JButton cancelButton;
	
	private DeliveryOrderAdjustmentPropertyBean deliveryOrderAdjustmentPropertyBean;

	/**
	 * 
	 */
	public DeliveryOrderCreateDialog(DeliveryOrderAdjustmentPanel parentPanel, String title) {
		super(parentPanel.getMainWindow(), title, true);
		this.parentPanel = parentPanel;
		setSize(525, 675);
		initView();
		addListeners();
		initComponents();
		
		deliveryOrderAdjustmentPropertyBean = PropertyService.getPropertyBean(DeliveryOrderAdjustmentPropertyBean.class, parentPanel.getApplicationId());
	}

	protected void initView() {
		setLayout(new MigLayout("center", "[fill]"));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		
		getRootPane().setDefaultButton(getCancelButton());
	}

	protected void initComponents() {
		add(getSalesModel());
		add(getSalesModelType(), "wrap");
		add(getSalesExtColor());
		add(getSalesIntColor(), "wrap 50");
		
		add(new JSeparator(), "span, wrap 50");
		
		add(getPurchaseContractNumber(), "span 2, wrap");
		add(getOrderUnit(), "span 2, wrap");
		add(getDueDatePickerPanel(), "span 2, wrap");
		add(getReceiveDatePickerPanel(), "span 2, wrap 40");
		add(getCreateButton());
		add(getCancelButton());
	}
	
	protected void addListeners() {
		getCreateButton().addActionListener(this);
		getCancelButton().addActionListener(this);
	}

	public DeliveryOrderAdjustmentPanel getParentPanel() {
		return parentPanel;
	}
	
//	private String getFromParentPanel(String textField) {
//		return getParentPanel().getFilteredFrameSpecTable().getSelectedItem();
//	}

	public LabeledTextField getSalesModel() {
		if (salesModel == null) {
			salesModel = new LabeledTextField("Sales Model", false);
			salesModel.getComponent().setEditable(false);
			
			String salesModelCode = getParentPanel().getFilteredFrameSpecTable().getSelectedItem().getSalesModelCode();
			salesModel.getComponent().setText(salesModelCode);
		}
		return salesModel;
	}

	public LabeledTextField getSalesExtColor() {
		if (salesExtColor == null) {
			salesExtColor = new LabeledTextField("Sales Ext Color", false);
			salesExtColor.getComponent().setEditable(false);
			
			String salesExtColorCode = getParentPanel().getFilteredFrameSpecTable().getSelectedItem().getSalesExtColorCode();
			salesExtColor.getComponent().setText(salesExtColorCode);
		}
		return salesExtColor;
	}

	public LabeledTextField getPurchaseContractNumber() {
		if (purchaseContractNumber == null) {
			purchaseContractNumber = new LabeledTextField("Purchase Contract Number", false);
		}
		return purchaseContractNumber;
	}

	public LabeledTextField getSalesModelType() {
		if (salesModelType == null) {
			salesModelType = new LabeledTextField("Sales Model Type", false);
			salesModelType.getComponent().setEditable(false);
			
			String salesModelTypeCode = getParentPanel().getFilteredFrameSpecTable().getSelectedItem().getSalesModelTypeCode();
			salesModelType.getComponent().setText(salesModelTypeCode);
		}
		return salesModelType;
	}

	public LabeledTextField getSalesIntColor() {
		if (salesIntColor == null) {
			salesIntColor = new LabeledTextField("Sales Int Color", false);
			salesIntColor.getComponent().setEditable(false);
			
			String salesIntColorCode = getParentPanel().getFilteredFrameSpecTable().getSelectedItem().getSalesIntColorCode();
			salesIntColor.getComponent().setText(salesIntColorCode);
		}
		return salesIntColor;
	}

	public LabeledTextField getOrderUnit() {
		if (orderUnit == null) {
			orderUnit = new LabeledTextField("Order Unit", false);
		}
		return orderUnit;
	}
	
	public JDatePanelImpl getOrderDueDatePanel() {
		if (orderDueDatePanel == null) {
			UtilDateModel dateModel = new UtilDateModel();
			Properties p = new Properties();
			p.put("text.today", "Today");
			p.put("text.month", "Month");
			p.put("text.year", "Year");
			
			if (parentPanel.getSelectedDate() != null) {
				dateModel.setValue(parentPanel.getSelectedDate());
			}
			
			orderDueDatePanel = new JDatePanelImpl(dateModel, p);
		}
		return orderDueDatePanel;
	}

	/**
	 * @return the orderDueDatePicker
	 */
	public JDatePickerImpl getOrderDueDatePicker() {
		if (orderDueDatePicker == null) {
			orderDueDatePicker = new JDatePickerImpl(getOrderDueDatePanel(), new DeliveryOrderDateLabelFormatter());
		}
		return orderDueDatePicker;
	}
	
	public JLabel getDueDatePickerLabel() {
		if (dueDatePickerLabel == null) {
			dueDatePickerLabel = new JLabel("Order Due Date");
		}
		return dueDatePickerLabel;
	}

	/**
	 * @return the dueDatePickerPanel
	 */
	public JPanel getDueDatePickerPanel() {
		if (dueDatePickerPanel == null) {
			dueDatePickerPanel = new JPanel();
			dueDatePickerPanel.setLayout(new MigLayout("center", "[fill, grow]"));
			dueDatePickerPanel.add(getDueDatePickerLabel(), "wrap 10");
			dueDatePickerPanel.add(getOrderDueDatePicker());
		}
		return dueDatePickerPanel;
	}
	
	public JDatePanelImpl getOrderReceiveDatePanel() {
		if (orderReceiveDatePanel == null) {
			UtilDateModel dateModel = new UtilDateModel();
			Properties p = new Properties();
			p.put("text.today", "Today");
			p.put("text.month", "Month");
			p.put("text.year", "Year");
			orderReceiveDatePanel = new JDatePanelImpl(dateModel, p);
		}
		return orderReceiveDatePanel;
	}

	/**
	 * @return the orderDueDate
	 */
	public JDatePickerImpl getOrderReceiveDatePicker() {
		if (orderReceiveDatePicker == null) {
			orderReceiveDatePicker = new JDatePickerImpl(getOrderReceiveDatePanel(), new DeliveryOrderDateLabelFormatter());
		}
		return orderReceiveDatePicker;
	}
	
	public JLabel getReceiveDatePickerLabel() {
		if (receiveDatePickerLabel == null) {
			receiveDatePickerLabel = new JLabel("Order Receive Date");
		}
		return receiveDatePickerLabel;
	}

	/**
	 * @return the datePickerPanel
	 */
	public JPanel getReceiveDatePickerPanel() {
		if (receiveDatePickerPanel == null) {
			receiveDatePickerPanel = new JPanel();
			receiveDatePickerPanel.setLayout(new MigLayout("center", "[fill, grow]"));
			receiveDatePickerPanel.add(getReceiveDatePickerLabel(), "wrap 10");
			receiveDatePickerPanel.add(getOrderReceiveDatePicker());
		}
		return receiveDatePickerPanel;
	}

	public JButton getCreateButton() {
		if (createButton == null) {
			createButton = new JButton("Create");
		}
		return createButton;
	}

	public JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton("Cancel");
		}
		return cancelButton;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//validate all fields?
		if (e.getSource().equals(getCreateButton())) {
			String orderUnitStr = getOrderUnit().getComponent().getText();
			String purchaseContractNumStr = getPurchaseContractNumber().getComponent().getText();
			if (!isInteger(purchaseContractNumStr) || purchaseContractNumStr.length() != 7) {
				getOrderUnit().getComponent().setBorder(new LineBorder(Color.gray, 1));
				getPurchaseContractNumber().getComponent().setBorder(new LineBorder(Color.red, 1));
				JOptionPane.showMessageDialog(this, "Purchase contract number must be a number of length 7.");
			} else if (!isInteger(orderUnitStr)) {
				getPurchaseContractNumber().getComponent().setBorder(new LineBorder(Color.gray, 1));
				getOrderUnit().getComponent().setBorder(new LineBorder(Color.red, 1));
				JOptionPane.showMessageDialog(this, "Order Unit must be a number.");
			} else {
				createPurchaseContract();
				dispose();
			}
		} else if (e.getSource().equals(getCancelButton())) {
			dispose();
		}
	}

	private void createPurchaseContract() {
		PurchaseContract contract = new PurchaseContract();
		
		//get all fields
		
		//received from frame spec
		String salesModelCode = getSalesModel().getComponent().getText();
		String salesModelTypeCode = getSalesModelType().getComponent().getText();
		String salesExtColorCode = getSalesExtColor().getComponent().getText();
		String salesIntColorCode = getSalesIntColor().getComponent().getText();
		
		//user input
		String purchaseContractNumber = getPurchaseContractNumber().getComponent().getText();
		String orderUnitStr = getOrderUnit().getComponent().getText();
		int orderUnit = Integer.parseInt(orderUnitStr);
		
		//configurable properties
		String salesModelOptionCode = this.deliveryOrderAdjustmentPropertyBean.getSalesModelOptionCode();
		String subsCode = this.deliveryOrderAdjustmentPropertyBean.getSubsCode();
		String shipper = this.deliveryOrderAdjustmentPropertyBean.getShipper();
		String invoiceSequenceNumber = this.deliveryOrderAdjustmentPropertyBean.getInvoiceSequenceNumber();
		String currency = this.deliveryOrderAdjustmentPropertyBean.getCurrency();
		String currencySettlement = this.deliveryOrderAdjustmentPropertyBean.getCurrencySettlement();
		String deliveryFormCode = this.deliveryOrderAdjustmentPropertyBean.getDeliveryFormCode();
		String productGroupCode = this.deliveryOrderAdjustmentPropertyBean.getProductGroupCode();
		
		//check properties
		if (salesModelOptionCode.length() > 3) {
			getParentPanel().getMainWindow().setErrorMessage("SALES_MODEL_OPTION_CODE is invalid. Please check the property.");
		} else if (subsCode.length() > 2) {
			getParentPanel().getMainWindow().setErrorMessage("SUBS_CODE is invalid. Please check the property.");
		} else if (shipper.length() > 3) {
			getParentPanel().getMainWindow().setErrorMessage("SHIPPER is invalid. Please check the property.");
		} else if (!isInteger(invoiceSequenceNumber)) {
			getParentPanel().getMainWindow().setErrorMessage("INVOICE_SEQUENCE_NUMBER is invalid. Please check the property.");
		} else if (currency.length() > 3) {
			getParentPanel().getMainWindow().setErrorMessage("CURRENCY is invalid. Please check the property.");
		} else if (currencySettlement.length() > 3) {
			getParentPanel().getMainWindow().setErrorMessage("CURRENCY_SETTLEMENT is invalid. Please check the property.");
		} else if (deliveryFormCode.length() > 1) {
			getParentPanel().getMainWindow().setErrorMessage("DELIVERY_FORM_CODE is invalid. Please check the property.");
		} else if (productGroupCode.length() > 1) {
			getParentPanel().getMainWindow().setErrorMessage("PRODUCT_GROUP_CODE is invalid. Please check the property.");
		} else {
			contract.setSalesModelCode(salesModelCode);
			contract.setSalesModelTypeCode(salesModelTypeCode);
			contract.setSalesExtColorCode(salesExtColorCode);
			contract.setSalesIntColorCode(salesIntColorCode);
			contract.setPurchaseContractNumber(purchaseContractNumber);
			contract.setOrderUnit(orderUnit);
			contract.setOrderDueDate(getSelectedDate("due"));
			contract.setReceiveDate(getSelectedDate("receive"));
			contract.setSalesModelOptionCode(salesModelOptionCode);
			contract.setSubsCode(subsCode);
			contract.setShipper(shipper);
			contract.setInvoiceSequenceNumber(Integer.parseInt(invoiceSequenceNumber));
			contract.setCurrency(currency);
			contract.setCurrencySettlement(currencySettlement);
			contract.setDeliveryFormCode(deliveryFormCode);
			contract.setProductGroupCode(productGroupCode);
			try {
				ServiceFactory.getDao(PurchaseContractDao.class).save(contract);
			} catch (Exception e) {
				getParentPanel().getLogger().error(e, "Exception while creating purhcase contract.");
				getParentPanel().getMainWindow().setErrorMessage("An error occurred while creating purchase contract.");
			}
		}
	}
	
	private java.sql.Date getSelectedDate(String type) {
		java.util.Date selectedDate;
		if (type.equals("due"))
			selectedDate = (java.util.Date) getOrderDueDatePanel().getModel().getValue();
		else
			selectedDate = (java.util.Date) getOrderReceiveDatePanel().getModel().getValue();
		
		if (selectedDate == null) return null;
		
		return new java.sql.Date(selectedDate.getTime());
	}
	
	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}
