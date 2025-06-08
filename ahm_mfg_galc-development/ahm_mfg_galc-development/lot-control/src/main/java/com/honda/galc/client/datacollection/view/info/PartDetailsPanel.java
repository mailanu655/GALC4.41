package com.honda.galc.client.datacollection.view.info;
/**
 * 
 * <h3>PartDetailsPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>PartDetailsPanel </p>
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
 * <TD>Meghana G</TD>
 * <TD>Mar 8, 2011</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD>Modified the code to do following changes:
 * a. added logic to enable and disable test mode
 * b. added logic to store and restore the device state.
 * c. modified the logic to refresh panel and release resources based on test mode.</TD>
 * </TR>  
 * </TABLE>
 *   
 * @author Meghana Ghanekar
 * Feb 16, 2011
 *
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.honda.galc.client.common.component.LabeledLabelPanel;
import com.honda.galc.client.common.data.PartDetailsTableModel;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.lotcontrol.ITorqueDeviceListener;
import com.honda.galc.client.device.lotcontrol.TorqueDeviceStatusInfo;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.constant.DeviceMessageSeverity;
import com.honda.galc.device.exception.DeviceInUseException;
import com.honda.galc.entity.product.DevicePartDetails;
import com.honda.galc.openprotocol.model.CommandAccepted;
import com.honda.galc.openprotocol.model.LastTighteningResult;
import com.honda.galc.openprotocol.model.MultiSpindleResultUpload;
import com.honda.galc.openprotocol.model.OPCommandError;

public class PartDetailsPanel extends JPanel implements ListSelectionListener, ActionListener,ITorqueDeviceListener {
	private static final long serialVersionUID = 1L;
	private String torQueDeviceId = null;
	private TablePane partDetailsTablePanel;
	private JButton enableDeviceButton;
	private JButton disableDeviceButton;
	private LabeledLabelPanel torqueField;
	private Dimension preferredSize = new Dimension(450,100);
	private TorqueConfigPanel configPanel;
	private TorqueSocketDevice torqueDevice;
	private static List<ITorqueDeviceListener> listeners = new ArrayList<ITorqueDeviceListener>();
	private boolean testMode = false;
	private static Map<String,Integer> deviceAccessKeyList = new HashMap<String, Integer>();
	ClientContext context;
	private String dev_instructionCode;
	private boolean dev_isenabled;
	private String currentInstructionCode ="0";
	public PartDetailsPanel(TorqueConfigPanel configPanel,ClientContext context) {
		super();
		this.configPanel = configPanel;
		this.context = context;
		torQueDeviceId = configPanel.getDevice().getDeviceId();
		initialize();
		refreshPanel();
		initConnections();

		try {
			setupDeviceManager();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void initialize() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new TitledBorder("PartDetails"));
		setSize(preferredSize);
		add(getPartDetailsTablePanel());
		add(new JPanel());
		add(getTorqueDevicePanel());
	}

	public TablePane getPartDetailsTablePanel() {
		if(partDetailsTablePanel == null){
			partDetailsTablePanel = new TablePane();
			partDetailsTablePanel.setPreferredSize(preferredSize );
			partDetailsTablePanel.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			ListSelectionModel listModel = partDetailsTablePanel.getTable().getSelectionModel();
			listModel.addListSelectionListener(this);
			partDetailsTablePanel.getTable().setDefaultRenderer(Object.class, new ColumnResizer());
			List<DevicePartDetails> partList = new ArrayList<DevicePartDetails>();
			new PartDetailsTableModel(partList, partDetailsTablePanel.getTable());
		}
		return partDetailsTablePanel;
	}

	public void valueChanged(ListSelectionEvent e) {
		int i = partDetailsTablePanel.getTable().getSelectedRow();
		if (i != -1) {
			if(!testMode){
				testMode = true;
				registerDevice();
				storeDeviceState();
			}
			getEnableDeviceButton().setEnabled(true);
			getEnableDeviceButton().setVisible(true);
			getDisableDeviceButton().setEnabled(true);
			getDisableDeviceButton().setVisible(true);
			getTorquePanel().setVisible(true);
		}

	}
	public JButton getEnableDeviceButton() {
		if (enableDeviceButton == null) {
			enableDeviceButton = new JButton("EnableDevice");
		}
		return enableDeviceButton;
	}

	public JButton getDisableDeviceButton() {
		if (disableDeviceButton == null) {
			disableDeviceButton = new JButton("DisableDevice");
		}
		return disableDeviceButton;
	}
	public LabeledLabelPanel getTorquePanel() {
		if (torqueField == null) {
			torqueField = new LabeledLabelPanel("Result","0.00");
			torqueField.setBorder(new EtchedBorder());
		}
		return torqueField;
	}

	private void initConnections() {
		getDisableDeviceButton().addActionListener(this);
		getEnableDeviceButton().addActionListener(this);
	}

	private void registerDevice(){
		try{
			getTorqueDevice();
			if(torqueDevice !=null){
				if(testMode){
					getTorqueDevice().registerListener(this);
					getTorqueDevice().requestControl(this);
				}
			}
		}catch(Exception ex){
			context.getCurrentViewManager().setErrorMessage("Could not register as listener to toruque device " + torQueDeviceId,DeviceMessageSeverity.error);
		}
	}

	private void unregisterDevice(){
		try{
			if(listeners != null){
				if(listeners.contains(this)){
					getTorqueDevice().unregisterListener(this);
				}
			}
		}catch (Exception e) {
			context.getCurrentViewManager().setErrorMessage("Could not unregister as listener to toruque device " + torQueDeviceId,DeviceMessageSeverity.error);
		}
	}
	private void setupDeviceManager() throws Exception {

		if (getTorqueDevice() == null)
			throw new Exception("No devices setup for this terminal");
		getTorqueDevice().startDevice();
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getEnableDeviceButton()) {
			enableDevice(e);
		}
		if (e.getSource() == getDisableDeviceButton()) {
			disableDevice(e);
		}

	}

	private void enableDevice(ActionEvent e) {
		torqueField.getValueLabel().setText("");
		torqueField.getValueLabel().setBackground(SystemColor.activeCaptionBorder);
		int i = getPartDetailsTablePanel().getTable().getSelectedRow();
		if (i != -1) {
			currentInstructionCode = (String) getPartDetailsTablePanel().getTable().getValueAt(i, 2);
			try {
				getTorqueDevice().enable(currentInstructionCode);
			} catch (DeviceInUseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			configPanel.getPSetPanel().getValueLabel().setText(currentInstructionCode);
    		configPanel.getDevice().setCurrentInstructionCode(currentInstructionCode);
		}
	} 

	public String getInstructionCode(){
		return currentInstructionCode;
	}
	private void disableDevice(ActionEvent e) {
		torqueField.getValueLabel().setText("");
		torqueField.getValueLabel().setBackground(java.awt.SystemColor.activeCaptionBorder);
		int i = getPartDetailsTablePanel().getTable().getSelectedRow();
		if (i != -1) {
			try {
				getTorqueDevice().disable();
			} catch (DeviceInUseException e1) {
				e1.printStackTrace();
			}
		}
	}

	private JPanel getTorqueDevicePanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER,30,5));
		panel.add(new JPanel().add(getEnableDeviceButton()));
		panel.add(new JPanel().add(getDisableDeviceButton()));
		panel.add(getTorquePanel());
		return panel;
	}

	private GridBagConstraints getConstraint(int gridx, int gridy, int gridwidth, double weightx) {
		GridBagConstraints c = new GridBagConstraints();		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx =weightx;

		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;	
		c.gridheight = 1;
		return c;
	}
	public void refreshPanel(){
		testMode = false;
		partDetailsTablePanel.clearSelection();
		getEnableDeviceButton().setVisible(false);
		getDisableDeviceButton().setVisible(false);
		getTorquePanel().setValue("0.00",null);
		getTorquePanel().setVisible(false);
		repaint();
	}

	public String getId() {
		//modify this method to return client id and applocation id
		return context.getAppContext().getTerminalId() ;
	}

	public void handleStatusChange(String deviceId,TorqueDeviceStatusInfo statusInfo) {
		try{
			switch (statusInfo.getMessageSeverity()) {
			case info:
				// clear the error message area
				context.getCurrentViewManager().setErrorMessage(null,DeviceMessageSeverity.info);
				break;

			case warning:
				// set warning (may be with a different color other than red)
				context.getCurrentViewManager().setErrorMessage(statusInfo.getMessage(),DeviceMessageSeverity.warning);
				break;

			case error:
				// check for ignorable errors
				if (statusInfo.getCommandError() != null){
					if (statusInfo.getCommandError().equals(OPCommandError.clientAlreadyConnected) ||
							statusInfo.getCommandError().equals(OPCommandError.lastTighteningResultSubAlreadyExists)){
						break;
					}
					// set error message (with red color)

					if (statusInfo.getCommandError().equals(OPCommandError.psetCannotBeSet)) {
						String msg = " Pset# "+getInstructionCode() +" cannot be set";
						context.getCurrentViewManager().setErrorMessage(msg,DeviceMessageSeverity.error);
						getTorqueDevice().disable();
						break;
					}
				}
			}
		}catch(DeviceInUseException ex) {
				context.getCurrentViewManager().setErrorMessage("Device " + ex.getDeviceName() + " is in use by " + ex.getApplicationName(),DeviceMessageSeverity.error);
			}
		}

	public void processLastTighteningResult(String deviceId,LastTighteningResult result) {

//		if (deviceAccessKeyList.get(torQueDeviceId) == -1)
//			return;
		try {
			Double dblTorqueValue = result.getTorque();
			int i = getPartDetailsTablePanel().getTable().getSelectedRow();
			if (i != -1) {
				if ((getPartDetailsTablePanel().getTable().getValueAt(i, 5) != null)
						&& (getPartDetailsTablePanel().getTable().getValueAt(i, 6) != null)) {
					Double maxTorqueValue = (Double) getPartDetailsTablePanel().getTable()
					.getValueAt(i, 6);
					Double minTorqueValue = (Double) getPartDetailsTablePanel().getTable()
					.getValueAt(i, 5);
					if ((dblTorqueValue <= maxTorqueValue)
							&& (dblTorqueValue >= minTorqueValue)) {
						getTorquePanel().setValue(dblTorqueValue.toString(),Color.GREEN);

					} else {
						getTorquePanel().setValue(dblTorqueValue.toString(),Color.red);
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("MSG02"+"\n"+ex.getMessage()+ "\n" +this.getClass().getName()+ "\n update");
		}

	}

	public void processMultiSpindleResult(String deviceId,
			MultiSpindleResultUpload multiSpindleResult) {
		// TODO Auto-generated method stub

	}

	public String getApplicationName() {
		//verify if this correct
		return context.getAppContext().getHostName();
	}

	public Integer getDeviceAccessKey() {
		if(deviceAccessKeyList.containsKey(torQueDeviceId)){
			return deviceAccessKeyList.get(torQueDeviceId);
		}else{
			return -1;
		}
	}

	public TorqueSocketDevice getTorqueDevice() throws DeviceInUseException{
		//if(getDeviceAccessKey() == -1){
			//Integer deviceAccessKey = DeviceManager.getInstance().requestExclusiveAccess(torQueDeviceId, this).intValue();
			//deviceAccessKeyList.put(torQueDeviceId, deviceAccessKey);
			//torqueDevice =  (TorqueSocketDevice) DeviceManager.getInstance().getDevice(torQueDeviceId, this);
		//}
		torqueDevice =  (TorqueSocketDevice) DeviceManager.getInstance().getDevice(torQueDeviceId);
		return torqueDevice;
	}	

	public void releaseExclusiveDeviceAccess() {
		try{
			unregisterDevice();
			if(testMode){
				torqueDevice.releaseControl();
				testMode = false;
			}
			listeners.clear();		
		}catch(Exception ex){
//			Logger.warning("Could not unregister as listener to toruque device " + torQueDeviceId);	
			}
		if (!DeviceManager.getInstance().releaseExclusiveAccess(torQueDeviceId, this)){
//			Logger.warning("Unable to release Torque device exclusive access by " + this.getClass().toString());
		}
	}

	public Integer getDeviceAccessKey(String deviceId){
		if(deviceAccessKeyList.containsKey(deviceId))
		{
			return deviceAccessKeyList.get(deviceId);
		}else{
			return -1;
		}
	}

	public void setTestMode(boolean testMode){
		this.testMode = testMode;
	}

	public boolean getTestMode(){
		return testMode;
	}
	
	public void activateTestMode() {

		refreshPanel();
		if(!testMode){
			testMode = true;
			registerDevice();
			storeDeviceState();
		}
	}

	private void storeDeviceState() {
		if(configPanel.getDevice()!= null){
			dev_instructionCode = configPanel.getDevice().getCurrentInstructionCode();
			dev_isenabled = configPanel.getDevice().isToolEnabled();
		}

	}
	public void restoreDeviceState(){
		
		releaseExclusiveDeviceAccess();
	}

	public void controlGranted(String deviceId) {
		// TODO Auto-generated method stub
		
	}

	public void controlRevoked(String deviceId) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void handleCommandAccepted(String deviceId, CommandAccepted commandAccepted) {
		// TODO Auto-generated method stub
		
	}
}
class ColumnResizer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			Component c = super.getTableCellRendererComponent(table,
					value, isSelected, hasFocus, row, column);

			int w = c.getPreferredSize().width  + table.getIntercellSpacing().width;
			TableColumn col = table.getColumnModel().getColumn(column);
			col.setPreferredWidth(Math.max(col.getPreferredWidth(), w));
			return c;
		}
	}

