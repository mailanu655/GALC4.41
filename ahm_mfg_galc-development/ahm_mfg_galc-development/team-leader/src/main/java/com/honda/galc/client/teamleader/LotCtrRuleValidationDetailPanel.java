package com.honda.galc.client.teamleader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.tablemodel.LotControlRuleTableModel;
import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.device.utils.LotControlRuleValidator;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.LotControlRuleFlag;
import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.CommonPartUtility;

/**
 * 
 * <h3>LotCtrRuleValidationDetailPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotCtrRuleValidationDetailPanel description </p>
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
 * <TD>May 16, 2011</TD>
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
 * @since May 16, 2011
 */

public class LotCtrRuleValidationDetailPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private LabeledTextField partMarkField;
	private LabeledTextField partMaskField;
	private JPanel partMaskPanel;
	private JPanel partMarkPanel;
	private UpperCaseFieldBean partSnField;
	private TablePane rulePanel;
	private TablePane partSpecPanel;
	private TablePane measurementSpecPanel;
	private LotControlValidationPanel mainPanel; 
	private LotControlRuleValidator validator = new LotControlRuleValidator();
	private JLabel processPointTypeLabel;
	private boolean headedClient;//headed or headless client

	public LotCtrRuleValidationDetailPanel(LotControlValidationPanel mainPanel) {
		super();
		this.mainPanel = mainPanel;
		initialize();
	}

	private void initialize() {
		
		initComponents();
		
	}

	private void initComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(getPartMarkPanel());
		add(getPartMaskPanel());
		add(new JSeparator(JSeparator.HORIZONTAL));
		add(Box.createVerticalStrut(50));
		add(getRultPanel());
		add(getPartSpecPanel());
		add(getMeasurementSpecPanel());
		
	}
	
	//--------------getters & setters ------------------
	public LabeledTextField getPartMarkField() {
		if(partMarkField == null){
			partMarkField = new LabeledTextField("Part Mark ");
			partMarkField.getComponent().setEditable(false);
			partMarkField.getComponent().setEnabled(true);
			partMarkField.getComponent().setPreferredSize(new Dimension(300, 30));
			partMarkField.setFont(Fonts.DIALOG_BOLD_16);
		}
		
		return partMarkField;
	}
	
	public LabeledTextField getPartMaskField() {
		if(partMaskField == null){
			partMaskField = new LabeledTextField("Part Mask ");
			partMaskField.getComponent().setEditable(false);
			partMaskField.setPreferredSize(new Dimension(450, 50));
			partMaskField.getLabel().setFont(Fonts.DIALOG_BOLD_16);
			partMaskField.getComponent().setFont(Fonts.DIALOG_BOLD_12);
		}
		
		return partMaskField;
	}
	
	public UpperCaseFieldBean getPartSnField() {
		if(partSnField == null){
			partSnField = new UpperCaseFieldBean();
			partSnField.setPreferredSize(new Dimension(360, 30));
			partSnField.setFont(Fonts.DIALOG_BOLD_16);
			partSnField.setEnabled(false);
			partSnField.setBackground(this.getBackground());
		}

		return partSnField;
	}
	
	public TablePane getRultPanel() {
		if(rulePanel == null)
		{
			rulePanel = new TablePane();
			new LotControlRuleTableModel(rulePanel.getTable(),null);
			
		}
		
		return rulePanel;
	}
	

	public TablePane getPartSpecPanel() {
		if(partSpecPanel == null){
			partSpecPanel = new TablePane();
			new PartSpecTableModel(partSpecPanel.getTable(),null, false, true);
		}
		return partSpecPanel;
	}
	

	public TablePane getMeasurementSpecPanel() {
		if(measurementSpecPanel == null){
			measurementSpecPanel = new TablePane();
			new MeasurementSpecTableModel(measurementSpecPanel.getTable(),null);
			
		}
		return measurementSpecPanel;
	}

	public JPanel getPartMaskPanel() {
		if(partMaskPanel == null)
		{
			partMaskPanel = new JPanel();
			partMaskPanel.setLayout(new FlowLayout());
			partMaskPanel.add(getPartMaskField());
			partMaskPanel.add(Box.createHorizontalStrut(10));
			partMaskPanel.add(getPartSnField());
			
		}
		return partMaskPanel;
	}

	public JPanel getPartMarkPanel() {
		if(partMarkPanel == null){
			partMarkPanel = new JPanel();
			partMarkPanel.setLayout(new FlowLayout());
			partMarkPanel.add(getPartMarkField());
			partMarkPanel.add(Box.createHorizontalStrut(275));
			partMarkPanel.add(getProcessPointTypeField());
		}
		return partMarkPanel;
	}

	private JLabel getProcessPointTypeField() {
		if(processPointTypeLabel == null){
			processPointTypeLabel = new JLabel();
			processPointTypeLabel.setOpaque(true);
			processPointTypeLabel.setPreferredSize(new Dimension(160, 30));
			processPointTypeLabel.setHorizontalAlignment(JLabel.CENTER);
			processPointTypeLabel.setFont(Fonts.DIALOG_BOLD_16);
		}
		
		return processPointTypeLabel;
	}

	public void setLotControlRules(List<LotControlRule> selectedRules) {
		if(selectedRules.size() <= 0) return;
		reset();
		renderRules(selectedRules);
		
		getPartMaskField().getComponent().setText(getMasksString(selectedRules));
		getPartMaskField().getComponent().setCaretPosition(0);
		
		renderPart(selectedRules.get(0));
	}

	public void reset() {
		getPartMarkField().getComponent().setText("");
		getPartMaskField().getComponent().setText("");
		getPartSnField().setText("");
		getPartSnField().setBackground(this.getBackground());
		getPartMarkField().getComponent().setBackground(this.getBackground());
		fieldControl(false, false, false);
		mainPanel.setMessage(null);
		
	}

	private void renderRules(List<LotControlRule> selectedRules) {
		if(selectedRules == null || selectedRules.size() == 0 ) return;
		new LotControlRuleTableModel(rulePanel.getTable(),selectedRules, false);
		
		if(selectedRules.get(0).getParts() == null || selectedRules.get(0).getParts().size() == 0) return;
		new PartSpecTableModel(partSpecPanel.getTable(),selectedRules.get(0).getParts(),false, true);
		
		if(selectedRules.get(0).getParts().get(0).getMeasurementCount() > 0)
			new MeasurementSpecTableModel(measurementSpecPanel.getTable(),selectedRules.get(0).getParts().get(0).getMeasurementSpecs());
		else {
			new MeasurementSpecTableModel(measurementSpecPanel.getTable(),null);
		}
	
	}
	
	private void renderPart(LotControlRule rule) {
		fieldControl(false, false, false);
		
		if(!validator.validate(rule, isHeadedClient())){
			mainPanel.setError(validator.getMessage());
			return;
		}
		
		if(!isHeadedClient()) return;
		
		//strategy
		if(!StringUtils.isEmpty(rule.getStrategy())){
			mainPanel.setMessage("Strategy!");
			return;
		}
		
		//Part mark
		if(rule.getSerialNumberScanFlag() == LotControlRuleFlag.OFF.getId() &&
				rule.getParts().get(0).getMeasurementCount() <= 0 &&
				!StringUtils.isEmpty(rule.getParts().get(0).getPartSerialNumberMask())){
			getPartMarkField().getComponent().setText(rule.getParts().get(0).getPartSerialNumberMask());
			getPartMarkField().getComponent().setBackground(Color.white);
			fieldControl(true, false, false);
			mainPanel.setMessage("Part Mark");
		} else if(rule.getSerialNumberScanFlag() == LotControlRuleFlag.OFF.getId() &&
				rule.getParts().get(0).getMeasurementCount() > 0 ){
			
			mainPanel.setMessage("Torque Only");
		} else if(rule.getSerialNumberScanType() != PartSerialNumberScanType.NONE && rule.isVerify()){
			getPartSnField().setEnabled(true);
			getPartSnField().setBackground(Color.white);
			mainPanel.setMessage("Verify part serial number");
			fieldControl(false, false, true);
		}
 		
	}
	
	public void fieldControl(boolean partMark, boolean partMask, boolean partSn){
		getPartMarkField().setEnabled(partMark);
		getPartMaskField().setEnabled(partMask);
		getPartSnField().setEnabled(partSn);
	}

	private String getMasksString(List<LotControlRule> selectedValues) {
		if(selectedValues.size() <= 0) return "";
		
		StringBuilder sb = new StringBuilder();
		for(PartSpec spec : selectedValues.get(0).getParts()){
			if(spec.getPartSerialNumberMask()==null) continue;
			if(sb.length() > 0) sb.append(",");
			sb.append(CommonPartUtility.parsePartMask(spec.getPartSerialNumberMask()));
		}
		return sb.toString();
	}

	public void setPartSepc(PartSpec spec) {
		mainPanel.setMessage(null);
		PartSpecTableModel model = (PartSpecTableModel)partSpecPanel.getTable(). getModel();
		model.selectItem(spec);
		partSpecPanel.getTable().setSelectionBackground(Color.yellow);
		
		if(spec.getMeasurementCount() > 0 )
			new MeasurementSpecTableModel(measurementSpecPanel.getTable(),spec.getMeasurementSpecs(), false);
		else
			new MeasurementSpecTableModel(measurementSpecPanel.getTable(),null);
		
	}

	public void setProcessPoint(final ProcessPoint processPoint) {
		reset();
		headedClient = isHeadedClient(processPoint);
		renderProcessPointTypeField();
	}

	private void renderProcessPointTypeField() {
		if(headedClient){
			getProcessPointTypeField().setText("LOT CONTROL");
			getProcessPointTypeField().setBackground(Color.green);
		} else {
			getProcessPointTypeField().setText("HEAD LESS");
			getProcessPointTypeField().setBackground(Color.lightGray);
		}
		
	}

	private boolean isHeadedClient(ProcessPoint processPoint) {
		Application application = ServiceFactory.getDao(ApplicationDao.class).findByKey(processPoint.getProcessPointId());
		return application == null || !StringUtils.isEmpty(application.getScreenClass());
	}

	public boolean isHeadedClient() {
		return headedClient;
	}

	public void resetLotControlRule() {
		new LotControlRuleTableModel(rulePanel.getTable(),null);
		new PartSpecTableModel(partSpecPanel.getTable(),null,false, true);
		new MeasurementSpecTableModel(measurementSpecPanel.getTable(),null);
		
	}

}
