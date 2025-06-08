
package com.honda.galc.client.VehicleQuality;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;


/**
 * 
 * @author Gangadhararao Gadde
 * @date Jan 23, 2014
 */
public class VQConfirmationVinPanel extends JPanel implements FocusListener
{

	private static final long serialVersionUID = 1L;
	private JPanel vinLabelPanel = null;
	private JPanel vinButtonPanel = null;
	private JButton vinButton = null;
	private JLabel vinLabel = null;
	private UpperCaseFieldBean vinTextField = null;
	EventHandler eventHandler = new EventHandler();
	private EventListenerList listeners = new EventListenerList();
	private Color FocusColor = Color.blue;
	private MainWindow parent=null;

	public VQConfirmationVinPanel(MainWindow parent) 
	{
		super();
		this.parent=parent;
		initialize();
	}

	public VQConfirmationVinPanel(MainWindow parent,LayoutManager layout)
	{
		super(layout);
		this.parent=parent;
		initialize();
	}

	public VQConfirmationVinPanel(MainWindow parent,LayoutManager layout, boolean isDoubleBuffered)
	{
		super(layout, isDoubleBuffered);
		this.parent=parent;
		initialize();
	}

	public VQConfirmationVinPanel(MainWindow parent,boolean isDoubleBuffered)
	{
		super(isDoubleBuffered);
		this.parent=parent;
		initialize();
	}

	class EventHandler implements ActionListener, FocusListener
	{
		public void actionPerformed(java.awt.event.ActionEvent e)
		{
			if (e.getSource() == getJButtonVIN())
				vinButtonEventHandler(e);
		}

		public void focusGained(java.awt.event.FocusEvent e)
		{
			if (e.getSource() == getJTextFieldVIN())
				vinTextFieldFocusEventHandler(e);
		}

		public void focusLost(java.awt.event.FocusEvent e)
		{
		}
	}


	public void addActionListener(ActionListener l)
	{
		listeners.add(ActionListener.class, l);
	}

	private void vinButtonEventHandler(ActionEvent arg1)
	{
		try
		{
			this.vinButtonActionPerformed(arg1);
		} catch (Exception e)
		{
			handleException(e);
		}
	}

	private void vinTextFieldFocusEventHandler(FocusEvent event)
	{
		try
		{
			vinTextFieldFocusGained(event);
		} catch (Exception e)
		{
			handleException(e);
		}
	}

	protected void fireActionPerformed(ActionEvent e)
	{
		Object[] listener = listeners.getListenerList();
		for (int i = listener.length - 2; i >= 0; i -= 2)
		{
			if (listener[i] == ActionListener.class)
			{
				((ActionListener) listener[i + 1]).actionPerformed(e);
			}
		}
	}

	public Color getFocusColor()
	{
		return FocusColor;
	}

	private JButton getJButtonVIN()
	{
		if (vinButton == null)
		{
			try
			{
				vinButton = new javax.swing.JButton();
				vinButton.setName("JButtonVIN");
				vinButton.setText("VIN");
				vinButton.setMaximumSize(new java.awt.Dimension(100, 25));
				vinButton.setPreferredSize(new java.awt.Dimension(90, 30));
				vinButton.setBounds(1, 7, 90, 30);
				vinButton.setMinimumSize(new java.awt.Dimension(100, 25));
				vinButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			} catch (Exception e)
			{
				handleException(e);
			}
		}
		return vinButton;
	}

	private JLabel getJLabelVIN()
	{
		if (vinLabel == null)
		{
			try
			{
				vinLabel = new javax.swing.JLabel();
				vinLabel.setName("JLabelVIN");
				vinLabel.setFont(new java.awt.Font("dialog", 0, 36));
				vinLabel.setIconTextGap(40);
				vinLabel.setText(" ");
				vinLabel.setForeground(java.awt.Color.black);
			} catch (Exception e)
			{
				handleException(e);
			}
		}
		return vinLabel;
	}

	private UpperCaseFieldBean getJTextFieldVIN()
	{
		if (vinTextField == null)
		{
			try
			{
				vinTextField = new UpperCaseFieldBean();
				vinTextField.setName("JTextFieldVIN");
				vinTextField.setFont(new java.awt.Font("dialog", 0, 75));
				vinTextField.setText("");
				vinTextField.setMaximumLength(17);
				vinTextField.setActionCommand("VIN Entered");
				vinTextField.requestFocus();
			} catch (Exception e)
			{
				handleException(e);
			}
		}
		return vinTextField;
	}

	private JPanel getLabelPanel()
	{
		if (vinLabelPanel == null)
		{
			try
			{
				vinLabelPanel = new javax.swing.JPanel();
				vinLabelPanel.setName("LabelPanel");
				vinLabelPanel.setLayout(new java.awt.BorderLayout());
				vinLabelPanel.setMaximumSize(new java.awt.Dimension(100, 100));
				vinLabelPanel.setMinimumSize(new java.awt.Dimension(10, 10));
				getLabelPanel().add(getJLabelVIN(), "East");
				getLabelPanel().add(getVINButtonPanel(), "West");
			} catch (Exception e)
			{
				handleException(e);
			}
		}
		return vinLabelPanel;
	}


	public String getLabelText()
	{
		return getJLabelVIN().getText();
	}

	public JLabel getLabelVIN()
	{
		return getJLabelVIN();
	}

	public UpperCaseFieldBean getTextFieldVIN()
	{
		return getJTextFieldVIN();
	}

	private JPanel getVINButtonPanel()
	{
		if (vinButtonPanel == null)
		{
			try
			{
				vinButtonPanel = new javax.swing.JPanel();
				vinButtonPanel.setName("VINButtonPanel");
				vinButtonPanel.setPreferredSize(new java.awt.Dimension(90, 30));
				vinButtonPanel.setLayout(null);
				vinButtonPanel.setMinimumSize(new java.awt.Dimension(90, 30));
				getVINButtonPanel().add(getJButtonVIN(), getJButtonVIN().getName());             
			} catch (Exception e)
			{              
				handleException(e);
			}
		}
		return vinButtonPanel;
	}

	private void handleException(Exception e)
	{
		Logger.getLogger().error(e,"An error Occurred while processing the VQ Confirmation VIN Panel screen");
		e.printStackTrace();
		parent.setErrorMessage("An error Occurred while processing the VQ Confirmation VIN Panel screen");
	}

	private void initConnections() throws Exception
	{
		getJButtonVIN().addActionListener(eventHandler);
		getJTextFieldVIN().addFocusListener(eventHandler);
	}

	private void initialize()
	{
		try
		{
			setName("VINInput");
			setLayout(new java.awt.BorderLayout());
			setSize(1000, 200);
			add(getJTextFieldVIN(), "Center");
			add(getLabelPanel(), "West");
			addFocusListener(this);
			initConnections();
		} catch (Exception e)
		{
			handleException(e);
		}
	}

	public void focusGained(FocusEvent e) {
		System.out.println("focusGained");
		getJTextFieldVIN().requestFocusInWindow();
		return;		
	}

	public void focusLost(FocusEvent e) {
		return;
	}

	public boolean isEnabled()
	{
		return (getJButtonVIN().isEnabled() && getJTextFieldVIN().isEnabled());

	}

	public void vinButtonActionPerformed(ActionEvent actionEvent)
	{
		try
		{
			ManualProductEntryDialog manualProductEntry = new ManualProductEntryDialog(this.parent, ProductType.FRAME.getProductName(),ProductNumberDef.getProductNumberDef(ProductType.FRAME).get(0).getName());                                              
			manualProductEntry.setModal(true);
			manualProductEntry.setVisible(true);
			getJTextFieldVIN().requestFocus();
			String vin =manualProductEntry.getResultProductId();          
			if (!vin.equals("")) {
				getJTextFieldVIN().setText(vin);
				fireActionPerformed(new ActionEvent(this, 2001, "InputVIN"));
			}
			return;
		}
		catch (Exception e)
		{
			handleException(e);
		}
	}

	public void vinTextFieldFocusGained(FocusEvent focusEvent)
	{
		getJTextFieldVIN().setBackground(FocusColor);
		return;
	}

	public void removeActionListener(ActionListener l)
	{
		listeners.remove(ActionListener.class, l);
	}

	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		getJButtonVIN().setEnabled(enabled);
		getJTextFieldVIN().setEnabled(enabled);
	}

	public void setFocusColor(java.awt.Color newFocusColor)
	{
		if (isEnabled())
		{
			FocusColor = newFocusColor;
		}
	}

	public void setLabelText(String LabelText)
	{
		getJLabelVIN().setText(LabelText);
	}
	
	public void refreshObject(Component obj, String txt, int col) {
		Color colBuff = null;
		if (obj instanceof UpperCaseFieldBean) {
			if (col == 0) {
				((UpperCaseFieldBean)obj).setColor(new Color(204,204,204));
				((UpperCaseFieldBean)obj).setBackground(new Color(204,204,204));
			}
			else if (col == 1) {
				((UpperCaseFieldBean)obj).setColor(Color.white);
				((UpperCaseFieldBean)obj).setBackground(Color.white);
			}
			else if (col == 2) {
				((UpperCaseFieldBean)obj).setColor(Color.blue);
				((UpperCaseFieldBean)obj).setBackground(Color.blue);
			}
			else if (col == 3) {
				((UpperCaseFieldBean)obj).setColor(Color.green);
				((UpperCaseFieldBean)obj).setBackground(Color.green);
			}
			else if (col == 4) {
				((UpperCaseFieldBean)obj).setColor(Color.red);
				((UpperCaseFieldBean)obj).setBackground(Color.red);
			}
			else {
				colBuff = ((UpperCaseFieldBean)obj).getBackground();
				((UpperCaseFieldBean)obj).setColor(colBuff);
				((UpperCaseFieldBean)obj).setBackground(colBuff);
			}
			if (txt != null) {
				((UpperCaseFieldBean)obj).setText(txt);
			}
			((UpperCaseFieldBean)obj).setSelectionStart(0);
			((UpperCaseFieldBean)obj).setSelectionEnd(((UpperCaseFieldBean)obj).getMaximumLength() );
		}
	}

}
