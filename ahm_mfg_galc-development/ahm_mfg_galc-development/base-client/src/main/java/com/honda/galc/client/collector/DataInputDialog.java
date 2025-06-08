package com.honda.galc.client.collector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;

/**
 * 
 * <h3>InputDataDialog</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> InputDataDialog description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Apr 19, 2021</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD>
 * </TR>
 * 
 * </TABLE>
 * 
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Apr.19, 2021
 */
public class DataInputDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -644844823470723064L;
	private JScrollPane dataInputPanel; 
	private JPanel contentPanel;
	private JPanel buttonPanel;
	private ProductType productType;
	private JButton okButton;
	private JButton cancelButton;
	private CollectorClientPropertyBean property;
	private Map<String, LabeledTextField> fieldMap = new HashMap<String, LabeledTextField>();
	private List<LabeledTextField> fieldList = new ArrayList<LabeledTextField>(); 
	private boolean cancelled = true;

	private Logger logger;
	private Device device;

	public DataInputDialog(JFrame frame, Device device, CollectorClientPropertyBean property) {
		super(frame, true);
		this.property = property;
		this.device = device;
		initialize();
	}

	private void initialize() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(760, 460);
		setTitle("Last Lot Selection");
		setLocationRelativeTo(getOwner());
		productType = ProductTypeCatalog.getProductType(property.getProductType());
		setContentPane(getContentPanel());
		getOkButton().addActionListener(this);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}


	public JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setLayout(new BorderLayout());
			contentPanel.add(getDataInputPanel(), BorderLayout.CENTER);
			contentPanel.add(getButtonPanel(), BorderLayout.SOUTH);
		}
		return contentPanel;
	}

	private Component getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getOkButton());
		}
		return buttonPanel;
	}


	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton("OK");
		}

		return okButton;
	}

	
	public JButton getCancelButton() {
		if(cancelButton == null)
			cancelButton = new JButton("CANCEL");
		return cancelButton;
	}

	public JScrollPane getDataInputPanel() {
		if(dataInputPanel == null) {
			JPanel panel = new JPanel();
			dataInputPanel = new JScrollPane(panel);
			panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
			
			Map<String, String> dataInputMap = property.getDataInputMap();
			
			if(dataInputMap == null || dataInputMap.size() == 0 ) return dataInputPanel;
			
			//there is no order in map, so let's use seq in device
			for(DeviceFormat df : device.getDeviceDataFormats()) {
				if(property.getDataInputMap().keySet().contains(df.getTag())){
					
					LabeledTextField newLabeledTextField = createLabeledTextField(property.getDataInputMap().get(df.getTag()));
					newLabeledTextField.getComponent().addActionListener(this);
					fieldMap.put(df.getTag(), newLabeledTextField);
					fieldList.add(newLabeledTextField);
					panel.add(newLabeledTextField);
				}

			}
			
		}
		return dataInputPanel;
	}
	
	private LabeledTextField createLabeledTextField(String label) {
		LabeledTextField labeledTextField =  new LabeledTextField(label);
		labeledTextField.setFont(new Font("dialog", 0, 26));
		labeledTextField.setLabelPreferredWidth(340);
		labeledTextField.setInsets(0, 10, 0, 10);
		labeledTextField.getComponent().setHorizontalAlignment(JTextField.LEFT);
		labeledTextField.getComponent().setBackground(Color.blue);
		labeledTextField.getComponent().setForeground(Color.white);
		labeledTextField.getComponent().setPreferredSize(new Dimension(300, 30));
		labeledTextField.getComponent().setEnabled(true);
		return labeledTextField;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getOkButton()) {
			
			Logger.getLogger().info("OK - input data.");
			
			completeInputData();
		
		} else if (e.getSource() == getCancelButton()) {
            Logger.getLogger().info("CANCEL - input data.");
			
			cancelInputData();
		} else {

			int position = getLabeledTextField(e.getSource());

			if(position == -1) return;

			if(position >= 0 && (position +1) < fieldList.size()) {
				fieldList.get(position +1).getComponent().requestFocus();
			} else if((position +1 ) == fieldList.size()) {
				completeInputData();
			}
		}

	}



	private void cancelInputData() {
		// TODO Auto-generated method stub
		device = null;
		this.dispose();
	}

	private void completeInputData() {
		this.cancelled = false;
		for(String key : fieldMap.keySet()) {
			DeviceFormat format = device.getInputDeviceFormat(key);
			if(format != null )
				format.setValue(fieldMap.get(key).getComponent().getText());
		}
		
		this.dispose();
	}

	private int getLabeledTextField(Object source) {
		for(int i = 0; i < fieldList.size(); i++)
			if(fieldList.get(i).getComponent() == source )
				return i;
		
		return -1;
	}
	
	

	public boolean isCancelled() {
		return cancelled;
	}


	public void open(Device device) {
		this.device = device;
		this.cancelled = true;
		for(LabeledTextField ltf : fieldList)
			ltf.getComponent().setText("");
		
		this.pack();
		this.setVisible(true);

	}

	private Logger getLogger() {
		if (logger == null)
			logger = Logger.getLogger();
		return logger;
	}


	protected ProductType getProductType() {
		return productType;
	}

	protected CollectorClientPropertyBean getProperty() {
		return property;
	}
	
}
