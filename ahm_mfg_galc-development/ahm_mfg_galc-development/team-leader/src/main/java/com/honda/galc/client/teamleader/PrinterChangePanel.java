package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.property.ReprintPropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.BroadcastDestinationId;
import com.honda.galc.service.property.PropertyService;

import net.miginfocom.swing.MigLayout;

/**
 * 
 *
 * <h3>PrinterChangePanel Class description</h3>
 * <p>
 * PrinterChangePanel description
 * </p>
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
 *
 * </TABLE>
 * 
 */
public class PrinterChangePanel extends TabbedPanel implements ListSelectionListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private JButton saveButton = new JButton("Save");

	private LabeledComboBox formComboBox = new LabeledComboBox("Form ID");
	private LabeledComboBox printerComboBox = new LabeledComboBox("Printer");

	private List<String> formList = new ArrayList<String>();
	private List<String> printerList = new ArrayList<String>();
	private Map<String, String> printerMap = new HashMap<String, String>(); 
	
	private List<BroadcastDestination> broadcastList = new ArrayList<BroadcastDestination>();
	
	private ReprintPropertyBean reprintPropertyBean;
	private String applicableProcessPoint = StringUtils.EMPTY;

	public PrinterChangePanel(TabbedMainWindow mainWindow) {
		super("Printer Change", KeyEvent.VK_C, mainWindow);
		initComponents();
	}

	@Override
	public void onTabSelected() {
		if (isInitialized)
			return;
		loadData();
		addListeners();
		isInitialized = true;
	}

	private void initComponents() {
		setLayout(new MigLayout("insets 20 200 20 200", "[grow,fill]"));
		int labelWidth = 150;

		formComboBox.setFont(getLabelFont());
		formComboBox.setLabelPreferredWidth(labelWidth);

		printerComboBox.setFont(getLabelFont());
		printerComboBox.setLabelPreferredWidth(labelWidth);
		printerComboBox.setModel(new ComboBoxModel<String>(printerList), -1);
		
		JLabel jlbLabel = new JLabel("Select the printer to be active for the FormID"); 
		jlbLabel.setFont(getLabelFont());
		jlbLabel.setHorizontalTextPosition(JLabel.CENTER);
		add(jlbLabel, "span 2,wrap");
		add(formComboBox, "wrap");
		add(printerComboBox, "wrap");
		add(createButtonPanel(), "gapleft 200,span");

	}
	
	private void addListeners() {
		saveButton.addActionListener(this);
		formComboBox.getComponent().addActionListener(this);
	}
	
	private void loadData() {
		reprintPropertyBean=PropertyService.getPropertyBean(ReprintPropertyBean.class,getMainWindow().getApplicationContext().getTerminal().getId());
		applicableProcessPoint = reprintPropertyBean.getApplicableProcessPoint();
		
		printerMap = PropertyService.getPropertyMap(getMainWindow().getApplicationContext().getTerminal().getId(),"PRINTER");
		formList = new ArrayList<String>(printerMap.keySet());
		formComboBox.setModel(new ComboBoxModel<String>(formList), -1);
		
		if(formList.size() == 1){
			formComboBox.setModel(new ComboBoxModel<String>(formList), 0);
			loadAllPrinters();			
			setActivePrinter();			
		}
	}

	private void setActivePrinter() {
		String formName = (String)formComboBox.getComponent().getSelectedItem();
		broadcastList = getDao(BroadcastDestinationDao.class).findAllByProcessPointId(applicableProcessPoint, true);
		String activePrinter = StringUtils.EMPTY;
		for (BroadcastDestination partName : broadcastList) {
			if(StringUtils.equalsIgnoreCase(formName, partName.getRequestId())){
				activePrinter = partName.getDestinationId();
			}
		}
		if(StringUtils.isNotBlank(activePrinter)) {
			printerComboBox.setModel(printerList, findPrinterIndex(activePrinter));
		} else {
			printerComboBox.setModel(printerList, -1);
		}
	}
	
	private int findPrinterIndex(String activePrinter) {
		for(int i = 0; i< this.printerList.size(); i++) {
			String printer = this.printerList.get(i);
			if(StringUtils.equalsIgnoreCase(printer,activePrinter)) return i;
		}
		return 0;
	}

	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.setPreferredSize(new Dimension(200, 60));
		buttonPanel.add(Box.createHorizontalStrut(150));
		buttonPanel.add(saveButton);
		return buttonPanel;
	}

	private Font getLabelFont() {
		return new Font("sansserif", 1, 20);
	}

	public void actionPerformed(ActionEvent e) {
		Object eventSource = e.getSource();
		if (eventSource instanceof JComboBox && formList.size() > 1) {
			printerComboBox.getComponent().setSelectedItem(null);
			loadAllPrinters();
			setActivePrinter();
		} else if (eventSource instanceof JButton) {
			if (eventSource.equals(saveButton))
				saveChange();
		}
	}
	
	private void loadAllPrinters() {
		this.printerList.clear();
		String formName = (String)formComboBox.getComponent().getSelectedItem();
		String printerNames = printerMap.get(formName);
		if(printerNames != null && !StringUtils.isEmpty(printerNames)){
			String[] names = printerNames.split(",");
			for (String printerName : names) {
				this.printerList.add(printerName);
			}
		}
	}

	private void saveChange() {
		String formName = (String)formComboBox.getComponent().getSelectedItem();
		String printerName = (String)printerComboBox.getComponent().getSelectedItem();
		boolean flag = false;
		if(StringUtils.isEmpty(formName) && StringUtils.isEmpty(printerName)) {
			MessageDialog.showError("Please select both FORM ID and PRINTER");
			return;
		} else if(StringUtils.isEmpty(formName)) {
			MessageDialog.showError("Please select FORM ID");
			return;
		} else if(StringUtils.isEmpty(printerName)) {
			MessageDialog.showError("Please select PRINTER");
			return;
		} else if(StringUtils.isEmpty(applicableProcessPoint)) {
			MessageDialog.showError("Please update the APPLICABLE_PROCESS_POINT in properties");
			return;
		}
		
		if (!MessageDialog.confirm(this, "Do you really want to update the form id : "+formName+" to the printer: "+printerName +" for the process point: "+applicableProcessPoint)) {
			return;
		}
		
		List<BroadcastDestination> list = getDao(BroadcastDestinationDao.class).findAllByProcessPointId(applicableProcessPoint, true);
		if(list == null || list.size() == 0) {
			MessageDialog.showError("Applicable Process Point does not exist in the Broadcast Table");
			return;
		}
		for (BroadcastDestination broadCast : list) {
			if(StringUtils.equalsIgnoreCase(formName, broadCast.getRequestId())){
				flag = true;
			}
		}
		if(!flag){
			MessageDialog.showError("Selected FormID does not exist in the Broadcast Table");
			return;
		}
		updateBroadcast(formName, printerName, list, applicableProcessPoint);
		
		String additionalProcessPoint = reprintPropertyBean.getAdditionalProcessPoints();
		if(StringUtils.isNotEmpty(additionalProcessPoint)){
			String[] additionalPP = additionalProcessPoint.split(",");
			for(int i = 0; i < additionalPP.length; i++) {
				List<BroadcastDestination> alist = getDao(BroadcastDestinationDao.class).findAllByProcessPointId(additionalProcessPoint);
				if(alist == null || alist.size() == 0) {
					MessageDialog.showError("Additional Process Point does not exist in the Broadcast Table");
					return;
				}
				updateBroadcast(formName, printerName, alist, additionalProcessPoint);
			}
		}
	}
	
	private void updateBroadcast(String formName, String printerName, List<BroadcastDestination> list, String processPoint){
		
		BroadcastDestination destinationData = new BroadcastDestination();
		destinationData.setId(new BroadcastDestinationId());
		for (BroadcastDestination broadCast : list) {
			if(StringUtils.equalsIgnoreCase(formName, broadCast.getRequestId())){
				destinationData.getId().setSequenceNumber(broadCast.getSequenceNumber());
				destinationData.setArgument(broadCast.getArgument());
				destinationData.setAutoEnabled(broadCast.isAutoEnabled());
			}
		}
		destinationData.getId().setProcessPointId(processPoint);
		destinationData.setDestinationId(printerName);
		destinationData.setRequestId(formName);
		destinationData.setDestinationTypeId(1);
		getDao(BroadcastDestinationDao.class).update(destinationData);
		logUserAction(UPDATED, destinationData);
		setMessage("Successfully updated Form Id : " + formName + " with printer " + printerName);
	}

	protected void setMessage(String message) {
		if (getMainWindow() != null)
			getMainWindow().setMessage(message);
	}

	public void valueChanged(ListSelectionEvent arg0) {

	}

}