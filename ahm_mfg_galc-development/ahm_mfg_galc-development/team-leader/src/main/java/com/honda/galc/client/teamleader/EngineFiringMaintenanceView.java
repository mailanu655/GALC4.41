package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.EngineFiringResultDao;
import com.honda.galc.dao.product.ReuseProductResultDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.EngineFiringResult;
import com.honda.galc.entity.product.ReuseProductResult;

/**
 * 
 * <h3>EngineFiringMaintenanceView Class description</h3>
 * <p> EngineFiringMaintenanceView description </p>
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
 * @author Jeffray Huang<br>
 * Jul 28, 2011
 *
 *
 */
public class EngineFiringMaintenanceView extends MainWindow {

	private static final long serialVersionUID = 1L;

	private LabeledUpperCaseTextField einTextField = new LabeledUpperCaseTextField("EIN");
	
	private JButton cancelButton = new JButton("Cancel");
	
	private JButton saveButton = new JButton("Save");
	
	private JCheckBox firingCheckBox = new JCheckBox("Firing Required");
	
	private LabeledTextField associateIdTextField = new LabeledTextField("Associate ID");
	
	private LabeledTextField reasonTextField = new LabeledTextField("Reason");
	
	private ObjectTablePane<EngineFiringResult> firingResultPanel;
	
	private Engine engine = null;
	
	public EngineFiringMaintenanceView(ApplicationContext appContext,
			Application application) {
		super(appContext, application,true);
		
		setSize(1024,768);

		initClientPanel();
		
		addActionListners();
	}
	
	private void initClientPanel() {
		
		JPanel panel = new JPanel(new BorderLayout());
		
		panel.add(createFiringFlagPanel(),BorderLayout.NORTH);
		panel.add(createFiringResultTablePane(),BorderLayout.CENTER);
		
		setClientPanel(panel);
		
		setEinValid(false);
	}
	
	private void addActionListners() {
		
		cancelButton.addActionListener(this);
		saveButton.addActionListener(this);
		firingCheckBox.addActionListener(this);
		einTextField.getComponent().addActionListener(this);
		
	}
	
	private JPanel createFiringFlagPanel() {
		
		JPanel panel = new JPanel(new BorderLayout());

		panel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Firing Flag Info"));
		
		panel.add(createProductPanel(),BorderLayout.NORTH);
		panel.add(createFiringPanel(),BorderLayout.SOUTH);
		
		return panel;
		
	}
	
	private JComponent configUppercaseTextField(LabeledUpperCaseTextField textField) {
		
		textField.setFont(Fonts.DIALOG_PLAIN_36);
		return textField;
	}
	
	private JComponent configTextField(LabeledTextField textField) {
		
		textField.setFont(Fonts.DIALOG_BOLD_16);
		return textField;
		
	}
	
	private JComponent configCheckBox(JCheckBox checkBox) {
		
		checkBox.setFont(Fonts.DIALOG_BOLD_16);
		return checkBox;
		
	}
	
	private JComponent configButton(JButton button) {
		
		button.setFont(Fonts.DIALOG_BOLD_22);
		ViewUtil.setPreferredWidth(button, 200);
		
		return button;
	}
	
	private JPanel createProductPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(configUppercaseTextField(einTextField));
		panel.add(configButton(cancelButton));
		panel.add(Box.createHorizontalStrut(20));
		panel.add(configButton(saveButton));
		
		return panel;
	}
	
	private JPanel createFiringPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(100));
		panel.add(configCheckBox(firingCheckBox));
		panel.add(configTextField(associateIdTextField));
		panel.add(configTextField(reasonTextField));
		
		return panel;
		
	}
	
	private TablePane createFiringResultTablePane() {
		
		ColumnMappings columnMappings = 
			ColumnMappings.with("Test Id", "resultId").put("Test Type", "firingTestType")
						  .put("Bench No", "firingBenchNo").put("Firing Status", "firingStatus")
						  .put("Notes","firingNotes").put("Associate No","associateNo").put("Date", "actualTimestamp");
		

		firingResultPanel =  new ObjectTablePane<EngineFiringResult>(columnMappings.get());

		return firingResultPanel;

	}
	
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		clearMessage();
		
		try{
			if(e.getSource() == cancelButton) cancel();
			else if(e.getSource() == firingCheckBox) firingFlagChanged();
			else if(e.getSource() == saveButton) save();
			else if(e.getSource() == einTextField.getComponent()) einEntered();
		}catch(Exception ex) {
			handleException(ex);
		}
	}
	
	private void firingFlagChanged() {
		
		saveButton.setEnabled(engine != null && engine.getEngineFiringFlag() != (firingCheckBox.isSelected() ? 1: 0));
		
	}
	
	private void cancel(){
		
		engine = null;
		firingResultPanel.removeData();
		einTextField.getComponent().setText("");
		firingCheckBox.setSelected(false);
		associateIdTextField.getComponent().setText("");
		reasonTextField.getComponent().setText("");
		einTextField.getComponent().requestFocus();
		setEinValid(false);
		
	}
	
	private void save() {
		
		String associateNo = associateIdTextField.getComponent().getText();
		if(StringUtils.isEmpty(associateNo)) {
			setErrorMessage("Please Enter Associate ID");
			return;
		};
		String reason = reasonTextField.getComponent().getText();
		if(StringUtils.isEmpty(reason)) {
			setErrorMessage("Please enter reason");
			return;
		}
		
		ReuseProductResult result = new ReuseProductResult(engine.getId(),associateNo,reason);
		
		getDao(ReuseProductResultDao.class).save(result);
		
		if(engine != null){
			engine.setEngineFiringFlag(firingCheckBox.isSelected() ? (short)1: (short)0);
			getDao(EngineDao.class).save(engine);
		}
		cancel();
		
	}
	
	private void einEntered() {
		String ein = StringUtils.trim(einTextField.getComponent().getText());
		if(!validateEIN(ein)) return;
		
		engine = getDao(EngineDao.class).findByKey(ein);
		
		if(engine == null) {
			einTextField.getComponent().selectAll();
			setErrorMessage("Engine is not found");
		}
		
		List<EngineFiringResult> firingResults = 
			getDao(EngineFiringResultDao.class).findAllByProductId(engine.getId());
		
		firingResultPanel.reloadData(firingResults);
		
		firingCheckBox.setSelected(engine.getEngineFiringFlag() >= 1);
		setEinValid(true);
		
	}
	
	private boolean validateEIN(String ein) {
		
		if(!ProductNumberDef.EIN.isNumberValid(ein)) {
			
			if(!StringUtils.isEmpty(ein))
				einTextField.getComponent().selectAll();
			
			setErrorMessage("Engine number is invalid");
			return false;
		}
		
		return true;
		
	}
	
	private void setEinValid(boolean aFlag) {
		
		einTextField.getComponent().setEditable(!aFlag);
		
		if(!aFlag) einTextField.getComponent().setBackground(Color.BLUE);
		else einTextField.getComponent().setColor(Color.GREEN);
		einTextField.getComponent().setForeground(aFlag ? Color.BLACK : Color.WHITE);
		
		cancelButton.setEnabled(aFlag);
		enableSystemMenu(!aFlag);
		firingCheckBox.setEnabled(aFlag);
		if(!aFlag) saveButton.setEnabled(false);
		
	}

}
