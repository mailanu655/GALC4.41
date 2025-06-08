package com.honda.galc.client.teamleader.hold.qsr.release.vinseq;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.PartNameSelectionPanel;
import com.honda.galc.client.teamleader.PartNameTableModel;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.service.ServiceFactory;


public class MassPartDataRemovalDialog extends JDialog implements Serializable, ActionListener,ListSelectionListener{
	
	protected PartNameSelectionPanel partNameSelectionPanel;
	protected TablePane assignedPartNameSelectionPanel;
	protected JButton addPartButton, removePartButton,cancelButton,submitButton;
	private PartNameTableModel assignedPartNameTableModel;
	private List<PartName> partNames;
	private String productType;
	private boolean enabled=true;
	private List<HoldResult> holdResults;
	
	public MassPartDataRemovalDialog (ReleasePanel parentPanel, String title,String productType, List<HoldResult> holdResults) {
		super(parentPanel.getMainWindow(), title, true);
		this.productType = productType;
		this.holdResults = holdResults;
		setSize(800, 800);
		initComponents();
		
		updatePartSelectionModel();
		addListeners();
	}
	
	protected void initComponents() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 40));
		panel.setBorder(BorderFactory.createEtchedBorder());
	
		Border border = BorderFactory.createEmptyBorder(4, 4, 4, 4);

		Box box21 = Box.createVerticalBox();
		box21.setBorder(border);
		JPanel buttonPanel2 = new JPanel(new GridLayout(6,1, 10, 40));
		buttonPanel2.add(new Label());
		buttonPanel2.add(new Label());
		buttonPanel2.add(createAddPartButton());
		buttonPanel2.add(createRemovePartButton());
		buttonPanel2.add(new Label());
		buttonPanel2.add(new Label());
		box21.add(buttonPanel2);
		
		Box box2 = Box.createHorizontalBox();
		box2.setBorder(border);
		box2.add(createPartSelectionPanel());
		box2.add(box21);
		box2.add(createAssignedPartSelectionPanel());
		panel.add(box2);
		
		Box box3 = Box.createHorizontalBox();
		box3.add(createSubmitButton());
		box3.add(new Label());
		box3.add(createCancelButton());
		
		panel.add(box3);
		
		this.setContentPane(panel);
			
	}
	

	private void addListeners() {
		partNameSelectionPanel.getPartSelectionPanel().addListSelectionListener(this);
		assignedPartNameSelectionPanel.addListSelectionListener(this);
		addPartButton.addActionListener(this);
		removePartButton.addActionListener(this);
		cancelButton.addActionListener(this);
		submitButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addPartButton))
			assignPart();
		else if (e.getSource().equals(removePartButton))
			deassignPart();
		else if (e.getSource().equals(cancelButton))
			cancelAction();
		else if(e.getSource().equals(submitButton))
			removePartData();
	}
	
	private void removePartData() {
		try {
			StringBuilder msg = new StringBuilder();
			msg.append("Are you sure you want remove Part Data");
			int retCode = JOptionPane.showConfirmDialog(this, msg, "Remove Parts", JOptionPane.YES_NO_OPTION);

			if (retCode != JOptionPane.YES_OPTION) {
				return;
			}	
			List<PartName> assignedPartList = assignedPartNameTableModel.getItems();
			List<String> selectedPartNames = new ArrayList<String>();
			for(PartName partName:assignedPartList) {
				selectedPartNames.add(partName.getPartName());
			}
			for(HoldResult holdResult: holdResults) {
				List<InstalledPart> installedParts = getInstalledPartDao().findAllByProductIdAndPartNames(holdResult.getId().getProductId(), selectedPartNames);
				if(installedParts.size() > 0)removeInstalledPartData(installedParts);
			
				List<Measurement> measurements = getMeasurementDao().findAllByProductIdAndPartNames(holdResult.getId().getProductId(), selectedPartNames);
				if(measurements.size() > 0)removeMeasurementData(measurements);
			}
			JOptionPane.showMessageDialog(this, "Successfully removed Part and Measurement Data");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			this.dispose();
		}
		
	}

	private void cancelAction() {
		this.dispose();
	}

	private void updatePartSelectionModel() {
		if (partNameSelectionPanel == null)
			return;
			if (this.productType != null)
			partNames = loadPartNames(productType);

		partNameSelectionPanel.update(partNames);
	}

	private List<PartName> loadPartNames(String productType) {
		return getDao(PartNameDao.class).findAllByProductType(StringUtils.upperCase(productType));
	}

	private void deassignPart() {
		PartName assignedPartName = assignedPartNameTableModel.getSelectedItem();
		List<PartName> assignedPartList = assignedPartNameTableModel.getItems();
		assignedPartList.remove(assignedPartName);
		assignedPartNameTableModel.refresh(assignedPartList);
		
		List<PartName> partList = partNames!= null && partNames.size() > 0 ?partNames: partNameSelectionPanel.getPartNameTableModel().getItems();
		partList.add(assignedPartName);
		partNameSelectionPanel.update(partList);

	}

	
	private void assignPart() {
		PartName partName = partNameSelectionPanel.getPartNameTableModel().getSelectedItem();
		List<PartName> partList = partNames!= null && partNames.size() > 0 ?partNames: partNameSelectionPanel.getPartNameTableModel().getItems();
		partList.remove(partName);
		partNameSelectionPanel.update(partList);
	
		if(assignedPartNameTableModel == null)assignedPartNameTableModel =  new PartNameTableModel(assignedPartNameSelectionPanel.getTable(),new ArrayList<PartName>(),true);
		List<PartName> assignedPartList = assignedPartNameTableModel.getItems();
		assignedPartList.add(partName);
		assignedPartNameTableModel.refresh(assignedPartList);

	}
	
	private PartNameSelectionPanel createPartSelectionPanel() {
		partNameSelectionPanel = new PartNameSelectionPanel(300, 600,
				new Dimension(300, 600));
		
		return partNameSelectionPanel;
	}
	
	private TablePane createAssignedPartSelectionPanel() {
		assignedPartNameSelectionPanel = new TablePane("Part Data to Remove"); 
		assignedPartNameSelectionPanel.setSize(300, 600);
		assignedPartNameSelectionPanel.setPreferredSize(new Dimension(300, 500));
		
		return assignedPartNameSelectionPanel ;
	}
	
	private JButton createAddPartButton() {
		addPartButton = new JButton(" >> ");
		addPartButton.setToolTipText("add part");
		addPartButton.setSize(100, 30);
		addPartButton.setEnabled(false);
		return addPartButton;
	}
	
	private JButton createRemovePartButton() {
		removePartButton = new JButton(" << ");
		removePartButton.setToolTipText("remove part");
		removePartButton.setSize(100, 30);
		removePartButton.setEnabled(false);
		return removePartButton;
	}
	
	protected JButton createCancelButton() {
		cancelButton = new JButton("Cancel");
		cancelButton.setToolTipText("Cancel");
		cancelButton.setSize(100, 30);
		cancelButton.setEnabled(true);
		cancelButton.setMnemonic(KeyEvent.VK_C);
		return cancelButton;
	}

	protected JButton createSubmitButton() {
		submitButton = new JButton("Submit");
		submitButton.setToolTipText("Submit");
		submitButton.setSize(100, 30);
		submitButton.setEnabled(false);
		submitButton.setMnemonic(KeyEvent.VK_S);
		return submitButton;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(enabled) enableButtons() ;
		
	}

	private void enableButtons() {
			if(partNameSelectionPanel.getPartNameTableModel().getSelectedItem() != null) {
				addPartButton.setEnabled(true);
			}else{
				addPartButton.setEnabled(false);
			}
			if(assignedPartNameTableModel != null && assignedPartNameTableModel.getSelectedItem() != null){
				removePartButton.setEnabled(true);
				submitButton.setEnabled(true);
			}else{
				removePartButton.setEnabled(false);
			}
	
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		partNameSelectionPanel.getPartSelectionPanel().setEnabled(enabled);
		assignedPartNameSelectionPanel.setEnabled(enabled);
		addPartButton.setEnabled(enabled);
		removePartButton.setEnabled(enabled);
	}
	
	private InstalledPartDao getInstalledPartDao() {
		return ServiceFactory.getDao(InstalledPartDao.class);
	}
	
	private MeasurementDao getMeasurementDao() {
		return ServiceFactory.getDao(MeasurementDao.class);
	}
	
	protected void removeInstalledPartData(List<InstalledPart> installedPartsToRemove) {
		List<InstalledPart> installedParts = new ArrayList<InstalledPart>();
		for(InstalledPart installedPart:installedParts) {
			installedPart.setPartSerialNumber(null);
			installedPart.setInstalledPartStatus(InstalledPartStatus.REMOVED);
			installedPart.setActualTimestamp(null);
			installedParts.add(installedPart);
		}
		getInstalledPartDao().saveAll(installedParts);
	}

	protected void removeMeasurementData(List<Measurement> measurementsToRemove) {
		
		List<Measurement> measurements = new ArrayList<Measurement>();
		for(Measurement measurement : measurementsToRemove){
			measurement.setMeasurementAngle(0.0);
			measurement.setMeasurementValue(0.0);
			measurement.setPartSerialNumber(null);
			measurement.setMeasurementName(null);
			measurement.setMeasurementStringValue(null);
			measurement.setActualTimestamp(null);
			measurement.setMeasurementStatus(MeasurementStatus.REMOVED);
			measurements.add(measurement);
		}
		getMeasurementDao().saveAll(measurements);
	}
}
