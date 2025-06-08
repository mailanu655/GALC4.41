package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.product.EquipFaultCodeDao;
import com.honda.galc.dao.product.EquipUnitFaultDao;
import com.honda.galc.entity.product.EquipFaultCode;
import com.honda.galc.entity.product.EquipUnitFault;

/**
 * 
 * 
 * <h3> EquipAlarmManualImportPanel Class description</h3>
 * <h4> EquipAlarmManualImportPanel Description </h4>
 * <p>
 * <code>EquipAlarmManualImportPanel</code> is ...
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
 * Nov 2, 2016
 */
public class EquipAlarmManualImportPanel extends TabbedPanel implements ListSelectionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ObjectTablePane<EquipFaultCode> equipFaultCodesPane;
	LabeledTextField unitNameTextField = new LabeledTextField("Unit Name:");
	LabeledTextField faultCodeTextField = new LabeledTextField("Fault Code:");
	LabeledTextField faultDescTextField = new LabeledTextField("Fault Desc:");
	
	JButton importButton = new JButton("IMPORT");
	JButton saveButton = new JButton("SAVE");
	JButton deleteButton = new JButton("DELETE");
	JButton refreshButton = new JButton("REFRESH");
	
	public EquipAlarmManualImportPanel(TabbedMainWindow mainWindow) {
		super("Equipment Alarm Manual Import Panel", KeyEvent.VK_I,mainWindow);
		initComponents();
		addListeners();
	}
	
	public void initComponents() {
		setLayout(new MigLayout("insets 50 20 50 20", "[grow,fill]"));
		equipFaultCodesPane = createEquipFaultCodeListPane();
		unitNameTextField.setFont(Fonts.DIALOG_BOLD_20);
		faultCodeTextField.setFont(Fonts.DIALOG_BOLD_20);
		faultCodeTextField.setNumeric(10);
		faultDescTextField.setFont(Fonts.DIALOG_BOLD_20);
		add(equipFaultCodesPane,"span,wrap");
		add(unitNameTextField,"span,wrap");
		add(faultCodeTextField,"span,wrap");
		add(faultDescTextField,"span,wrap");
		add(importButton,"gaptop 50,gapleft 150");
		add(saveButton);
		add(deleteButton);
		add(refreshButton,"gapright 150");
		
	}
	
	public void addListeners() {
		equipFaultCodesPane.addListSelectionListener(this);
		importButton.addActionListener(this);
		saveButton.addActionListener(this);
		deleteButton.addActionListener(this);
		refreshButton.addActionListener(this);
		
	}
	
	public void actionPerformed(ActionEvent event) {
		clearErrorMessage();
		if(event.getSource().equals(importButton)) importButtonClicked();
		else if(event.getSource().equals(saveButton)) saveButtonClicked();
		else if(event.getSource().equals(deleteButton)) deleteButtonClicked();
		else if(event.getSource().equals(refreshButton)) refreshButtonClicked();
		
	}
	
	private void importButtonClicked() {
		List<EquipFaultCode> faultCodes = loadFromFile();
		if(!faultCodes.isEmpty()){
			getDao(EquipFaultCodeDao.class).saveAll(faultCodes);
			logUserAction(SAVED, faultCodes);
		}
	}
	
	@SuppressWarnings("resource")
	private List<EquipFaultCode> loadFromFile() {
		List<EquipFaultCode> faultCodes = new ArrayList<EquipFaultCode>();
		JFileChooser fc = new JFileChooser();
        fc.setDialogTitle ("Open File");
        fc.setFileSelectionMode ( JFileChooser.FILES_ONLY);
        int ret = fc.showOpenDialog (this);
        if (ret != JFileChooser.APPROVE_OPTION) {
            return faultCodes;
        }
        try {
        	File loadedFile = fc.getSelectedFile();
            BufferedReader reader = new BufferedReader (new FileReader (loadedFile));
			 
			String line;
			while ((line = reader.readLine ()) != null) {
				line = StringUtils.trim(line);
				if(line.length() >= 0 && !line.startsWith("#")){
					EquipFaultCode faultCode = convert(line);
					if(faultCode == null) return faultCodes;
					else faultCodes.add(faultCode);
				}
			}
			reader.close();
		}catch (Exception e) {
			setErrorMessage("Exception occurred: " + e.getMessage());
		}
        return faultCodes;
	}
	
	private EquipFaultCode convert(String lineString) {
		lineString = StringUtils.trim(lineString);
		if(StringUtils.isEmpty(lineString)) return null;
		
		String [] items = lineString.split(",");
		
		if(items.length < 3) {
			setErrorMessage("Input: [" + lineString + "] has wrong format");
			return null;
		}
		
		String unitName = StringUtils.trim(items[0]);
        
        EquipUnitFault unitFault = getDao(EquipUnitFaultDao.class).findByUnitName(unitName);
        if(unitFault == null) {
        	unitFault = createManualImportedUnitFault(unitName);
        }else {
        	if(!unitFault.isManualImport()){
        		setErrorMessage("unit name is not a manual imported unit - " + lineString);
        	    return null;
        	}
        }
        
        short code;
        try {
            code =Short.parseShort(items[1]);
        }catch(NumberFormatException e) {
            setErrorMessage("Input: \"" + lineString + "\" has wrong fault code : " + items[1] );
            return null;
        }
        
        EquipFaultCode faultCode = new EquipFaultCode(unitFault.getId(),code);
        faultCode.setFaultDescription(items[2]);
        faultCode.setControllable(true);
        
    	return faultCode;
	}
	
	private void saveButtonClicked() {
		String unitName = unitNameTextField.getComponent().getText();
		if(StringUtils.isEmpty(unitName)) {
			setErrorMessage("unit name is not defined");
			return;
		}
		Short unitId = getUnitId(unitName);
		if(unitId == null) {
			EquipUnitFault unitFault = getDao(EquipUnitFaultDao.class).findByUnitName(unitName);
			if(unitFault != null) setErrorMessage("Unit Name: " + unitName + " is not a manual import unit");
			createManualImportedUnitFault(unitName);
		}

		EquipFaultCode faultCode = new EquipFaultCode(unitId,Integer.valueOf(faultCodeTextField.getComponent().getText()));
		faultCode.setFaultDescription(faultDescTextField.getComponent().getText());
		getDao(EquipFaultCodeDao.class).save(faultCode);
		logUserAction(SAVED, faultCode);
		
		setMessage("Unit Name: " + unitName + "Fault Code : " + faultCodeTextField.getComponent().getText() + " is saved");
		loadData();
	}
	
	private EquipUnitFault createManualImportedUnitFault(String unitName) {
		Short unitId = getDao(EquipUnitFaultDao.class).minUnitId();
		unitId--;
		EquipUnitFault unitFault = new EquipUnitFault();
		unitFault.setUnitId(unitId);
		unitFault.setUnitName(unitName);
		unitFault.setManualImport(true);
		EquipUnitFault euf = getDao(EquipUnitFaultDao.class).save(unitFault);
		logUserAction(SAVED, unitFault);
		return euf;
	}
	
	
	
	private void deleteButtonClicked() {
		String unitName = unitNameTextField.getComponent().getText();
		Short unitId = getUnitId(unitName);
		
		if(unitId != null) {
			EquipFaultCode faultCode = new EquipFaultCode(unitId,Short.valueOf(faultCodeTextField.getComponent().getText()));
			getDao(EquipFaultCodeDao.class).remove(faultCode);
			logUserAction(REMOVED, faultCode);
			setMessage("Unit Name: " + unitName + "Fault Code : " + faultCodeTextField.getComponent().getText() + " is removed");
			loadData();
		}else {
			setErrorMessage("Unit name: " + unitName + " does not exist");
		}
		
	}
	
	private void refreshButtonClicked() {
		loadData();
	}
	
	private Short getUnitId(String unitName) {
		EquipUnitFault unitFault = getDao(EquipUnitFaultDao.class).findByUnitName(unitName);
		return unitFault == null ? null : unitFault.getId();
	}

	@Override
	public void onTabSelected() {
		loadData();
	}
	
	private ObjectTablePane<EquipFaultCode> createEquipFaultCodeListPane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Unit Name", "unitName").put("Fault Code", "faultCode")
				.put("Fault Description","faultDescription");
		
		ObjectTablePane<EquipFaultCode> pane = new ObjectTablePane<EquipFaultCode>(clumnMappings.get(),false);
		
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("sansserif", 1, 18));
		pane.getTable().setRowHeight(28);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}
	
	private void loadData() {
		equipFaultCodesPane.reloadData(getDao(EquipFaultCodeDao.class).findAllManualImported());
	}

	public void valueChanged(ListSelectionEvent event) {
		if(event.getValueIsAdjusting()) return;
		EquipFaultCode faultCode= equipFaultCodesPane.getSelectedItem();
		unitNameTextField.getComponent().setText(faultCode.getUnitName());
		faultCodeTextField.getComponent().setText(Integer.toString(faultCode.getFaultCode()));
		faultDescTextField.getComponent().setText(faultCode.getFaultDescription());
	}
	

}
