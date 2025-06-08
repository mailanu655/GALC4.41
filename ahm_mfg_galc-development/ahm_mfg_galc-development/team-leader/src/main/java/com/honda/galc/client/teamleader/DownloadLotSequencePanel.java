package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.product.DownloadLotSequenceDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.product.DownloadLotSequence;
import com.honda.galc.entity.product.EquipUnitFault;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * 
 * <h3> DownloadLotSequencePanel Class description</h3>
 * <h4> DownloadLotSequencePanel Description </h4>
 * <p>
 * <code>DownloadLotSequencePanel</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * 
 * @see
 * @ver 0.1
 * @author is08925
 * Nov 16, 2016
 */
public class DownloadLotSequencePanel extends TabbedPanel implements ListSelectionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String PROCESS_LOCATION = "PROCESS LOCATION";
	
	ObjectTablePane<DownloadLotSequence> downloadSequenceListPane;
	ObjectTablePane<PreProductionLot> preProductionLotListPane;
	
	LabeledTextField processLocationTextField = new LabeledTextField("Process Location:");
	LabeledTextField processPointIdTextField = new LabeledTextField("Process Point ID:");
	LabeledTextField endProductionLotTextField = new LabeledTextField("End Production Lot:");
	
	JButton saveButton = new JButton("SAVE");
	
	
	
	List<EquipUnitFault> equipUnitFaultList;
	
	public DownloadLotSequencePanel(TabbedMainWindow mainWindow) {
		super("Download Lot Sequence Maint Panel", KeyEvent.VK_D,mainWindow);
		initComponents();
		addListeners();
	}

	
	public void initComponents() {
		setLayout(new MigLayout("insets 50 20 50 20", "[grow,fill]"));
		downloadSequenceListPane = createDownloadLotSequenceListPane();
		preProductionLotListPane = createPreProductionLotListPane();
		
		processLocationTextField.setFont(Fonts.DIALOG_BOLD_20);
		processLocationTextField.setLabelPreferredWidth(200);
		processPointIdTextField.setFont(Fonts.DIALOG_BOLD_20);
		processPointIdTextField.setLabelPreferredWidth(200);
		endProductionLotTextField.setFont(Fonts.DIALOG_BOLD_20);
		endProductionLotTextField.setLabelPreferredWidth(200);
		
		
		add(downloadSequenceListPane, "wrap");
		add(processLocationTextField, "gaptop 100,wrap");
		add(processPointIdTextField, "wrap");
		add(endProductionLotTextField,"wrap");
		add(saveButton,"gaptop 30, gapbottom 30,gapleft 250, gapright 250, dock south");
		add(preProductionLotListPane,"dock east");
		
	}
	
	private void addListeners() {
		downloadSequenceListPane.addListSelectionListener(this);
		preProductionLotListPane.addListSelectionListener(this);
		
		saveButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource().equals(saveButton)) saveButtonClicked();
	}
	
	
	private void saveButtonClicked() {
		DownloadLotSequence lotSequence = downloadSequenceListPane.getSelectedItem();
		PreProductionLot prodLot = preProductionLotListPane.getSelectedItem();
		if(lotSequence == null) {
			setErrorMessage("Please select Process Point");
			return;
		}
		
		if(prodLot == null) {
			setErrorMessage("Please select pre production lot");
			return;
		}
		
		lotSequence.setEndProductionLot(prodLot.getProductionLot());
		
		getDao(DownloadLotSequenceDao.class).save(lotSequence);
		logUserAction(SAVED, lotSequence);
		
		setMessage("Lot " + lotSequence + " is updated");
		
		downloadSequenceListPane.reloadData(getDao(DownloadLotSequenceDao.class).findAllByProcessLocation(getProcessLocation()));
		
	}
	
	@Override
	public void onTabSelected() {
		loadData();
	}
	
	private void loadData() {
		downloadSequenceListPane.reloadData(getDao(DownloadLotSequenceDao.class).findAllByProcessLocation(getProcessLocation()));
		preProductionLotListPane.reloadData(findAllLots());
	}
	
	private void processPointSelected() {
		DownloadLotSequence lotSequence = downloadSequenceListPane.getSelectedItem();
		if(lotSequence == null) {
			setErrorMessage("Please select Process Point");
			return;
		}
		
		processLocationTextField.getComponent().setText(lotSequence.getProcessLocation());
		processPointIdTextField.getComponent().setText(lotSequence.getProcessPointId());
		
	}
	
	private void productionLotSelected() {
		PreProductionLot prodLot = preProductionLotListPane.getSelectedItem();
		if(prodLot == null) {
			setErrorMessage("Please select pre production lot");
			return;
		}
		endProductionLotTextField.getComponent().setText(prodLot.getProductionLot());
		
	}
	
	private ObjectTablePane<DownloadLotSequence> createDownloadLotSequenceListPane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Process Location", "processLocation")
					.put("Process Point ID", "processPointId")
					.put("End Production Lot","endProductionLot");
		
		ObjectTablePane<DownloadLotSequence> pane = new ObjectTablePane<DownloadLotSequence>(clumnMappings.get(),false);
		
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("sansserif", 1, 18));
		pane.getTable().setRowHeight(28);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}
	
	private ObjectTablePane<PreProductionLot> createPreProductionLotListPane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Production Lot", "productionLot")
				.put("Send Status", "sendStatus");
		
		ObjectTablePane<PreProductionLot> pane = new ObjectTablePane<PreProductionLot>(clumnMappings.get(),true);
		
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("sansserif", 1, 18));
		pane.getTable().setRowHeight(28);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}
	
	public void valueChanged(ListSelectionEvent event) {
		if (event.getValueIsAdjusting()) return;
		if(event.getSource().equals(downloadSequenceListPane.getTable().getSelectionModel()))
			processPointSelected();
		else if(event.getSource().equals(preProductionLotListPane.getTable().getSelectionModel()))
			productionLotSelected();
		
	}
	
	private String getProcessLocation() {
		return this.getProperty(PROCESS_LOCATION, "AE");
	}
	
	private List<PreProductionLot> findAllLots() {
		List<PreProductionLot> lots = getDao(PreProductionLotDao.class).findAllByProcessLocation(getProcessLocation());
		List<PreProductionLot> activeLots = new SortedArrayList<PreProductionLot>("getProductionLot");
		
		for(PreProductionLot lot : lots) {
			if(lot.getHoldStatus() == 1) activeLots.add(lot);
		}
		return activeLots;
	}
	

}
