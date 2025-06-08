package com.honda.galc.device.simulator.client.view;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.device.simulator.server.SimulatorServerManager;
import com.honda.galc.entity.conf.Device;

public class SimulatorServerListView extends JDialog {
	
	private static final long serialVersionUID = 8872016825736146383L;
	Logger log = Logger.getLogger(SimulatorServerListView.class);  //  @jve:decl-index=0:
	
	/* server list - Client Id is the key */
	private SimulatorServerManager _sevManager;
	private List<Device> devices;
	private JPanel jPaneServerList = null;
	private JLabel jLabelServerList = null;
	private JScrollPane jScrollPaneListTable = null;
	private JTable jTableServers = null;
	private JPanel jButtonPanel = null;
	private JButton jButtonStop = null;
	private JButton jButtonStopAll = null;
	private int iPort = 0;
	

	public SimulatorServerListView(SimulatorServerManager servers, List<Device> devices) throws HeadlessException {
		super();
		this._sevManager = servers;
		this.devices = devices;
		initialize();
	}
	
	/**
	 * initialize the view
	 *
	 */
	private void initialize()
	{
		setName("Simulator Server List");
		this.setContentPane(getJScrollPaneServerList());
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(400, 200);
		setTitle("GALC Device Simulator: Simulator Server List");			
		setContentPane(getJScrollPaneServerList());
		addListeners();
	}

	/**
	 * This method initializes jScrollPaneServerList	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JPanel getJScrollPaneServerList() {
		if (jPaneServerList == null) {			
				jLabelServerList = new JLabel();
				jLabelServerList.setText("Simulator Servers");
				jPaneServerList = new JPanel();				
				jPaneServerList.setLayout(new BorderLayout());
				jPaneServerList.add(jLabelServerList, BorderLayout.NORTH);
				jPaneServerList.add(getJScrollPaneListTable(), BorderLayout.CENTER);
				jPaneServerList.add(getJButtonPanel(), BorderLayout.SOUTH);
		}
		return jPaneServerList;
	}

	/**
	 * This method initializes jScrollPaneListTable	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneListTable() {
		if (jScrollPaneListTable == null) {
			jScrollPaneListTable = new JScrollPane();
			jScrollPaneListTable.setViewportView(getJTableServers());
		}
		return jScrollPaneListTable;
	}
	
	private void addListeners(){
		getJButtonStop().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(iPort != 0)
				{
				   stopServer(iPort);
				}
			}
		});
		getJButtonStopAll().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopAllServers();
				
			}
		});
	}

	/**
	 * This method initializes jTableServers	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTableServers() {
		if (jTableServers == null) {
			jTableServers = new JTable();
			DeviceTableModel tableModel = new DeviceTableModel(devices,jTableServers);
			jTableServers.setLocation(new Point(0, 0));
			jTableServers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableModel.pack();
            ListSelectionModel rowSM = jTableServers.getSelectionModel();
			rowSM.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					// Ignore extra messages.
					if (e.getValueIsAdjusting())
						return;
					ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					if (lsm.isSelectionEmpty()) {
						log.info("No rows are selected.");
					} else {
						int selectedRow = lsm.getMinSelectionIndex();
						
                        //mark the current selected server port
						iPort = (Integer) jTableServers.getModel().getValueAt(selectedRow, 0);							
					}
				}
			});
		}
		return jTableServers;
	}
	

	/**
	 * This method initializes jButtonPanel	
	 * 	
	 * @return javax.swing.jPanel	
	 */
	private JPanel getJButtonPanel() {
		if (jButtonPanel == null) {
			jButtonPanel = new JPanel();			
			jButtonPanel.add(getJButtonStop(), null);			
			jButtonPanel.add(getJButtonStopAll(), null);
		}
		return jButtonPanel;
	}

	
	/**
	 * This method initializes jButtonStop	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonStop() {
		if (jButtonStop == null) {
			jButtonStop = new JButton();
			jButtonStop.setText("Stop");
		}
		return jButtonStop;
	}

	/**
	 * This method initializes jButtonStopAll	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonStopAll() {
		if (jButtonStopAll == null) {
			jButtonStopAll = new JButton();
			jButtonStopAll.setText("Stop All");
		}
		return jButtonStopAll;
	}
	/**
	 * Stop the current selected server
	 *
	 */
	public void stopServer(int port)
	{		
		_sevManager.stopServer(port);
		jTableServers.updateUI();
				
	}
	
	/**
	 * Stop all current running servers
	 *
	 */
	public void stopAllServers()
	{
		_sevManager.stopAllServers();
		
        jTableServers.updateUI();
		
	}
	
	 private class DeviceTableModel extends SortableTableModel<Device>{
        private static final long serialVersionUID = 1L;
        
        public DeviceTableModel(List<Device> devices, JTable table){
            super(devices,new String[] {"Port", "Running Status", "Device Id"},table);
        }
        
        public boolean isCellEditable (int row, int column){
            return column == 2;
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(rowIndex >= getRowCount()) return null;
            Device device = items.get(rowIndex);
            switch(columnIndex){
                case 0: return device.getEifPort();
                case 1: return _sevManager.isServerRunning(device.getEifPort()) ? "Running" : "Stopped";
                case 2: return device.getClientId();
            }
            return null;
        }
	 }
	
}
