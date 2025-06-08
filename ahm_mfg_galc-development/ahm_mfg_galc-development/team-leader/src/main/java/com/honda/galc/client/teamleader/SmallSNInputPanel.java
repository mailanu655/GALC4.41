package com.honda.galc.client.teamleader;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;

/** * * 
* @version 0.1 
* @author Gangadhararao Gadde 
* @since Mar 27, 2013
*/

public class SmallSNInputPanel extends JPanel {

	public static final long serialVersionUID = 1L;
	public JButton snButton = null;
	public UpperCaseFieldBean snTextField = null;
	EventHandler eventHandler = new EventHandler();
	public ProductType productType=null;
	private java.awt.Frame owner;

	class EventHandler implements java.awt.event.ActionListener, java.awt.event.FocusListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == SmallSNInputPanel.this.getSNButton()) 
				connEtoC1(e);
		};
		public void focusGained(java.awt.event.FocusEvent e) {
			if (e.getSource() == SmallSNInputPanel.this.getSNTextField()) 
				connEtoC2(e);
		};
		public void focusLost(java.awt.event.FocusEvent e) {};
	};
	public EventListenerList listeners = new EventListenerList();
	public java.awt.Color FocusColor = java.awt.Color.white;

	public SmallSNInputPanel(java.awt.Frame owner) {
		super();
		this.owner = owner;
		initialize();
	}

	public SmallSNInputPanel(java.awt.Frame owner, java.awt.LayoutManager layout) {
		super(layout);
		this.owner = owner;
		initialize();
	}

	public SmallSNInputPanel(java.awt.Frame owner, java.awt.LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		this.owner = owner;
		initialize();
	}

	public SmallSNInputPanel(java.awt.Frame owner, boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		this.owner = owner;
		initialize();
	}

	public void addActionListener(ActionListener l) {
		listeners.add(ActionListener.class, l);
	}

	public void connEtoC1(java.awt.event.ActionEvent arg1) {
		try {
			this.snButtonActionPerformed(arg1);
		} catch (Exception ivjExc) {
			handleException(ivjExc);
		}
	}

	public void connEtoC2(java.awt.event.FocusEvent arg1) {
		try {
			this.snTextFieldFocusGained(arg1);
		} catch (Exception ivjExc) {
			handleException(ivjExc);
		}
	}

	public void fireActionPerformed(ActionEvent e) {
		Object[] listener = listeners.getListenerList();
		for(int i = listener.length - 2; i >= 0; i -= 2) {
			if (listener[i] == ActionListener.class) {
				((ActionListener)listener[i + 1]).actionPerformed(e);
			}
		}
	}

	public java.awt.Color getFocusColor() {
		return FocusColor;
	}

	public javax.swing.JButton getSNButton() {
		if (snButton == null) {
			try {
				snButton = new javax.swing.JButton();
				snButton.setName("JButtonSN");
				snButton.setText("");
				snButton.setMaximumSize(new java.awt.Dimension(75, 15));
				snButton.setPreferredSize(new java.awt.Dimension(75, 15));
				snButton.setBounds(  1,7,75,55  ) ;
				snButton.setMinimumSize(new java.awt.Dimension(75, 25));
				snButton.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
				snButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			} catch (Exception ivjExc) {
				handleException(ivjExc);
			}
		}
		return snButton;
	}

	public UpperCaseFieldBean getSNTextField() {
		if (snTextField == null) {
			try {
				snTextField = new UpperCaseFieldBean();
				snTextField.setName("JTextFieldSN");
				snTextField.setFont(new java.awt.Font("dialog", 0, 14));
				snTextField.setText(" ");
				snTextField.setFixedLength(false);
				snTextField.setMaximumLength(17);
				snTextField.setMaximumSize(new java.awt.Dimension(30, 15));
				snTextField.setPreferredSize(new java.awt.Dimension(30, 15));
				snTextField.setBounds(1, 7, 30, 15);
				snTextField.setMinimumSize(new java.awt.Dimension(30, 15));
				snTextField.setEnabled(false);
				snTextField.requestDefaultFocus();
			} catch (Exception ivjExc) {
				handleException(ivjExc);
			}
		}
		return snTextField;
	}

	public UpperCaseFieldBean getTextFieldSN() {
		return getSNTextField();
	}

	public void handleException(Exception exception) {
	}

	public void initConnections() throws java.lang.Exception {
		getSNButton().addActionListener(eventHandler);
		getSNTextField().addFocusListener(eventHandler);
	}

	public void initialize() {
		try {
			setName("Product Input");
			setLayout(new java.awt.BorderLayout());
			setSize(  70, 10);
			add(getSNTextField(), "Center");
			add(getSNButton(),"West");
			initConnections();
		} catch (Exception ivjExc) {
			handleException(ivjExc);
		}
	}

	public void snButtonActionPerformed(java.awt.event.ActionEvent actionEvent) {
		try {
			ManualProductEntryDialog manualProductEntry = new ManualProductEntryDialog(this.owner,productType.name(),ProductNumberDef.getProductNumberDef(productType).get(0).getName());
			manualProductEntry.setModal(true);
			manualProductEntry.setVisible(true);
	        getSNTextField().requestFocus();
			String SN =manualProductEntry.getResultProductId();
			if (!SN.equals("")) {
				getSNTextField().setText(SN);
				fireActionPerformed(new ActionEvent(this, 2001, "InputSN"));
			}
			return;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void snTextFieldFocusGained(java.awt.event.FocusEvent focusEvent) {
		getSNTextField().setBackground(FocusColor);
		return;
	}

	public void removeActionListener(ActionListener l) {
		listeners.remove(ActionListener.class, l);
	}

	public void setFocusColor(java.awt.Color newFocusColor) {
		FocusColor = newFocusColor;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
}