package com.honda.galc.client.teamleader.transfer;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.client.teamleader.PartByProductSpecTableModel;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.tablemodel.LotControlRuleTableModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartByProductSpecCode;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.SortedArrayList;

import net.miginfocom.swing.MigLayout;

public class LotControlRuleDisplayPanel extends JPanel implements ActionListener, TableModelListener{
	private static final long serialVersionUID = 1L;
	protected JPanel topPanel;
	protected JPanel bottomPanel;
	
	protected JPanel lcDisplayPanel;
	protected JPanel procPointPanel;
	protected JPanel ymtocPanel;
	
	protected LabeledComboBox procPointComboBox;
	protected LabeledComboBox yearComboBox;
	protected LabeledComboBox modelComboBox;
	protected LabeledComboBox typeComboBox;
	protected LabeledComboBox optionComboBox;
	protected LabeledComboBox extColorComboBox;
	protected LabeledComboBox intColorComboBox;
	
	protected TablePane rulePanel;
	protected TablePane rulePartPanel;
	
	private List<LotControlRule> filteredLotControlRules = null;
	protected LotControlRuleTableModel lotControlRuleTableModel;
	protected PartByProductSpecTableModel partByProductSpecTableModel;
	
	LotControlRuleTransferPanel parentPanel;
	protected Boolean isSource;
	
	public LotControlRuleDisplayPanel(LotControlRuleTransferPanel parentPanel, Boolean isSource, String name) {
		super();
		this.parentPanel = parentPanel;
		this.isSource = isSource;
		this.setName(name);
		this.initComponent();
		this.addActionListeners();
	}
	
	public void initComponent() {
		this.setLayout(new MigLayout("align 50% 50%,inset 0 0 0 0"));
		this.add(this.getLCDisplayPanel(),"wrap");
	}
	
	protected JPanel getLCDisplayPanel() {
		String borderTitle = "Source";
		if (this.lcDisplayPanel == null) {
			this.getFilteredProcPointIds();
			this.lcDisplayPanel = new JPanel(new MigLayout("inset 0 0 0 0"));
			this.lcDisplayPanel.add(this.getTopPanel(),"wrap");
			this.lcDisplayPanel.add(this.getBottomPanel(),"wrap");
			if (!isSource) borderTitle = "Destination";
			TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), borderTitle);
			border.setTitleFont(Fonts.DIALOG_BOLD_24);
			this.lcDisplayPanel.setBorder(border);
		}
		return this.lcDisplayPanel;
	}
	
	protected JPanel getTopPanel() {
		if (this.topPanel == null) {
			this.topPanel = new JPanel(new MigLayout("inset 0 0 0 0"));
			this.topPanel.add(this.getProcPointPanel());
			this.topPanel.add(this.getYmtocPanel());
		}
		return this.topPanel;
	}
	
	protected JPanel getBottomPanel() {
		if (this.bottomPanel == null) {
			this.bottomPanel = new JPanel(new MigLayout("inset 0 0 0 0"));
			this.bottomPanel.add(this.getRulePanel());
			this.bottomPanel.add(this.getRulePartPanel(),"width 400:500:500");
		}
		return this.bottomPanel;
	}

	protected JPanel getProcPointPanel() {
		if (this.procPointPanel == null) {
			this.procPointPanel = new JPanel(new MigLayout("align 50% 50%,inset 0 0 0 0"));
			this.procPointPanel.add(this.procPointComboBox = this.createComboBox("ID", "ProcPointComboBox", 350), "width 350:500:500, wrap");
			this.procPointPanel.setBorder(new TitledBorder("Process Point"));
		}
		return this.procPointPanel;
	}
	
	protected JPanel getYmtocPanel() {
		if (this.ymtocPanel == null) {
			this.ymtocPanel = new JPanel(new MigLayout("align 50% 50%,inset 0 0 0 0"));
			this.ymtocPanel.add(this.yearComboBox = this.createComboBox("Year", "YearComboBox", 160));
			this.ymtocPanel.add(this.modelComboBox = this.createComboBox("Model", "ModelComboBox", 160));
			this.ymtocPanel.add(this.typeComboBox = this.createComboBox("Type", "TypeComboBox", 160));
			this.ymtocPanel.add(this.optionComboBox = this.createComboBox("Option", "OptionComboBox", 160));
			this.ymtocPanel.add(this.extColorComboBox = this.createComboBox("Ext Color", "ExtColorComboBox", 160));
			this.ymtocPanel.add(this.intColorComboBox = this.createComboBox("Int Color", "IntColorComboBox", 160), "wrap");
			this.ymtocPanel.setBorder(new TitledBorder("YMTOC"));
		}
		return this.ymtocPanel;
	}
	
	public TablePane getRulePanel() {
		if (this.rulePanel == null) {
			this.rulePanel = new TablePane("Lot Control Rule", ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			if (this.isSource!=null && !this.isSource) this.rulePanel.getTable().setName("DestRuleTable");
			else this.rulePanel.getTable().setName("RuleTable");
			this.rulePanel.getTable().setRowHeight(20);
			this.rulePanel.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
			if (!this.isSource) this.rulePanel.getTable().setBackground(new Color(235,235,235));
		}
		return rulePanel;
	}
	
	public TablePane getRulePartPanel() {
		if (this.rulePartPanel == null) {
			this.rulePartPanel = new TablePane("Part List");
			if (this.isSource!=null && !this.isSource) this.rulePartPanel.getTable().setName("RulePartTable");
			else this.rulePartPanel.getTable().setName("DestRulePartTable");
			this.rulePartPanel.getTable().setEnabled(false);
			if (!this.isSource) this.rulePartPanel.getTable().setBackground(new Color(235,235,235));
		}
		return this.rulePartPanel;
	}
	
	protected LabeledComboBox createComboBox(String label, String name, int preferredWidth) {
		Dimension dimension = new Dimension(preferredWidth, 20);
		LabeledComboBox comboBox = new LabeledComboBox(label);
		if (this.isSource!=null && !this.isSource) comboBox.getComponent().setName("Dest" + name);
		else comboBox.getComponent().setName(name);
		comboBox.getComponent().setBackground(Color.WHITE);
		comboBox.getComponent().setMinimumSize(new Dimension (50,20));
		comboBox.getComponent().setPreferredSize(dimension);
		return comboBox;
	}
	
	public LotControlRuleTableModel getLotControlRuleTableModel() {
		return this.lotControlRuleTableModel;
	}
	
	public LabeledComboBox getProcPointComboBox() {
		return this.procPointComboBox;
	}
	
	public LabeledComboBox getYearComboBox() {
		return this.yearComboBox;
	}
	
	public LabeledComboBox getModelComboBox() {
		return this.modelComboBox;
	}
	
	public LabeledComboBox getTypeComboBox() {
		return this.typeComboBox;
	}
	
	public LabeledComboBox getOptionComboBox() {
		return this.optionComboBox;
	}
	
	public LabeledComboBox getExtColorComboBox() {
		return this.extColorComboBox;
	}
	
	public LabeledComboBox getIntColorComboBox() {
		return this.intColorComboBox;
	}
	
	public String getSelectedProcPoint() {
		String selectedProcPoint = (String)this.getProcPointComboBox().getComponent().getSelectedItem();
		if (selectedProcPoint != null)
			selectedProcPoint = selectedProcPoint.split(" - ")[0].trim();
		return selectedProcPoint;
	}
	
	public String getSelectedYearCode() {
    	return (String) this.yearComboBox.getComponent().getSelectedItem();
    }
    
	public String getSelectedModelCode() {
    	return (String) this.modelComboBox.getComponent().getSelectedItem();
    }
    
	public String getSelectedTypeCode() {
    	return (String) this.typeComboBox.getComponent().getSelectedItem();
    }
    
	public String getSelectedOptionCode() {
    	return (String) this.optionComboBox.getComponent().getSelectedItem();
    }
    
	public String getSelectedExtColorCode() {
    	return (String) this.extColorComboBox.getComponent().getSelectedItem();
    }
    
	public String getSelectedIntColorCode() {
    	return (String) this.intColorComboBox.getComponent().getSelectedItem();
    }
	
	public String getSelectedYmtoc() {
		return
			StringUtils.trimToEmpty(this.getSelectedYearCode()) + 
			StringUtils.trimToEmpty(this.getSelectedModelCode()) +
			StringUtils.trimToEmpty(this.getSelectedTypeCode()) +
			StringUtils.trimToEmpty(this.getSelectedOptionCode()) +
			StringUtils.trimToEmpty(this.getSelectedExtColorCode()) +
			StringUtils.trimToEmpty(this.getSelectedIntColorCode());
	}
	
	public void showLotControlRuleResult(List<LotControlRule> srcRules) {
		this.lotControlRuleTableModel = new LotControlRuleTableModel(this.getRulePanel().getTable(), srcRules, false);
	}
	
	public void showLotControlRuleResult() {
		this.filteredLotControlRules = this.retrieveFilteredLotControlRules();
		this.lotControlRuleTableModel = new LotControlRuleTableModel(this.getRulePanel().getTable(), this.filteredLotControlRules, false);
	}
	
	private List<LotControlRule> retrieveFilteredLotControlRules() {

		List<String> specCodes = this.buildSelectedProductSpecCodes();
		List<LotControlRule> rules = new ArrayList<LotControlRule>();
		String selectedProcPoint = this.getSelectedProcPoint();
		List<LotControlRule> ruleList;

		for (String productSpecCode : specCodes) {
			String specCode = ProductSpec.trimWildcard(productSpecCode);
			ruleList = getDao(LotControlRuleDao.class).findAllByProductSpecCode(specCode);		
			if (selectedProcPoint != null && selectedProcPoint.equals(ProductSpec.WILDCARD)) {
				rules.addAll(ruleList);
			} else {
				for (LotControlRule rule : ruleList) {
					if (selectedProcPoint.trim().equalsIgnoreCase(rule.getId().getProcessPointId().trim())) rules.add(rule);
				}
			}
		}
		return rules;
	}
	
	public List<String> buildSelectedProductSpecCodes() {
		ArrayList<String> specCodes = new ArrayList<String>();
		if (this.parentPanel.productSpecData != null && this.getSelectedTypeCode() != null) {
			specCodes = (ArrayList<String>) this.parentPanel.productSpecData.getProductSpecData(
					this.getSelectedYearCode(), 
					this.getSelectedModelCode(),
					new Object[]{this.getSelectedTypeCode()},
					new Object[]{this.getSelectedOptionCode()},
					new Object[]{this.getSelectedExtColorCode()},
					new Object[]{this.getSelectedIntColorCode()});
		}
		return specCodes;
	}
	
	public void refreshRulePanel(List<LotControlRule> lotControlRules) {
		this.lotControlRuleTableModel = new LotControlRuleTableModel(this.getRulePanel().getTable(), lotControlRules,false);	
	}
	
	public void addActionListeners() {
		getProcPointComboBox().getComponent().addActionListener(this);
		getYearComboBox().getComponent().addActionListener(this);
		getModelComboBox().getComponent().addActionListener(this);
		getTypeComboBox().getComponent().addActionListener(this);
		getOptionComboBox().getComponent().addActionListener(this);
		getExtColorComboBox().getComponent().addActionListener(this);
		getIntColorComboBox().getComponent().addActionListener(this);
		this.getRulePanel().getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				displayParts();
				parentPanel.enableCopyButton();
			}
		});
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			this.parentPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			this.parentPanel.getMainWindow().clearMessage();
			
			if (e.getSource().equals(this.getYearComboBox().getComponent())) {
				this.yearChanged();
			} else if (e.getSource().equals(this.getProcPointComboBox().getComponent())) {
				this.procPointChanged();
			} else if (e.getSource().equals(this.getModelComboBox().getComponent())) {
				this.modelChanged();
			} else if (e.getSource().equals(this.getTypeComboBox().getComponent())) {
				this.typeChanged();
			} else if (e.getSource().equals(this.getOptionComboBox().getComponent())) {
				this.optionChanged();
			} else if (e.getSource().equals(this.getExtColorComboBox().getComponent())) {
				this.extColorChanged();
			} else if (e.getSource().equals(this.getIntColorComboBox().getComponent())) {
				this.intColorChanged();
			} 
		} finally {
			this.parentPanel.setCursor(Cursor.getDefaultCursor());
			this.parentPanel.enableCopyButton();
		}
	}
	
    public void valueChanged(ListSelectionEvent e) {
        
    	if(e.getValueIsAdjusting()) return;
    	Exception exception = null;
    	try{
	    	if(e.getSource() == this.getRulePanel().getTable().getSelectionModel()){
	    		LotControlRule lotControlRule = this.getLotControlRuleTableModel().getSelectedItem();
	    		List<PartByProductSpecCode> parts = new ArrayList<PartByProductSpecCode>();
	    		if(this.parentPanel.isMbpnProduct()) parts = lotControlRule== null ? null : lotControlRule.getPartByProductSpecs();
	    		else parts = lotControlRule== null ? null : lotControlRule.getPartByProductSpecs();
	    		this.partByProductSpecTableModel = new PartByProductSpecTableModel(this.getRulePartPanel().getTable(),parts);
	    		this.partByProductSpecTableModel.pack();
	    	}
    	}catch(Exception ex) {
    		exception = ex;
    	}
    	this.parentPanel.handleException(exception);	
	}
	
	public void procPointChanged() {
		Logger.getLogger().info(this.getName() + ": Process point [" + this.getSelectedProcPoint() + "] selected");
		if ((this.getSelectedProcPoint()) != null) {
			this.parentPanel.productSpecData = this.parentPanel.getProductSpecCache().get(this.parentPanel.getSelectedProductType());
			SortedArrayList<String> years = new SortedArrayList<String>(
					this.parentPanel.productSpecData.getModelYearCodes());
			years.remove(ProductSpec.WILDCARD);
			ComboBoxModel<String> model = new ComboBoxModel<String>(years);
			this.getYearComboBox().getComponent().setModel(model);
		}
		else{
			ComboBoxModel<String> model = new ComboBoxModel<String>(new ArrayList<String>());
			this.getYearComboBox().getComponent().setModel(model);
			this.getYearComboBox().getComponent().setSelectedIndex(-1);
			this.getModelComboBox().getComponent().setSelectedIndex(-1);
		}
		this.getYearComboBox().getComponent().setSelectedIndex(-1);
		this.getModelComboBox().getComponent().setSelectedIndex(-1);
		this.clearTables();
	}
	
	protected void yearChanged() {
		if ((this.getSelectedYearCode()) != null) {
			Logger.getLogger().info(this.getName() + ": Year [" + this.getSelectedYearCode() + "] is selected");
			SortedArrayList<String> models = new SortedArrayList<String>(this.parentPanel.productSpecData.getModelCodes(this.getSelectedYearCode()));
			if (this.isSource) models.remove(ProductSpec.WILDCARD);
			ComboBoxModel<String> model = new ComboBoxModel<String>(models);
			getModelComboBox().getComponent().setModel(model);
			
			if (this.isSource) {
				getModelComboBox().getComponent().setSelectedIndex(-1);
				this.clearTables();
			} else {
				this.getModelComboBox().getComponent().setSelectedItem(ProductSpec.WILDCARD);
				this.showLotControlRuleResult();
			}
		} else {
			ComboBoxModel<String> model = new ComboBoxModel<String>(new ArrayList<String>());
			getModelComboBox().getComponent().setModel(model);
			this.clearTables();
		}
	}
	
	protected void modelChanged() {
		if (!StringUtils.isEmpty(this.getSelectedModelCode())) {
			Logger.getLogger().info(this.getName() + ": Model [" +  this.getSelectedModelCode() + "] is selected");
			SortedArrayList<String> types = new SortedArrayList<String>(this.parentPanel.productSpecData.getModelTypeCodes(this.getSelectedYearCode(), this.getSelectedModelCode()));
			ComboBoxModel<String> type = new ComboBoxModel<String>(types);
			getTypeComboBox().getComponent().setModel(type);
			this.getTypeComboBox().getComponent().setSelectedItem(ProductSpec.WILDCARD);
			this.showLotControlRuleResult();
		} else {
			ComboBoxModel<String> type = new ComboBoxModel<String>(new ArrayList<String>());
			this.getTypeComboBox().getComponent().setModel(type);
			this.getTypeComboBox().getComponent().setSelectedIndex(-1);
			this.clearTables();
		}
	}
	
	protected void typeChanged() {
		if ((this.getSelectedTypeCode()) != null) {
			Logger.getLogger().info(this.getName() + ": Type [" +  this.getSelectedTypeCode() + "] is selected");
			SortedArrayList<String> options = new SortedArrayList<String>(this.parentPanel.productSpecData.getModelOptionCodes(this.getSelectedYearCode(), this.getSelectedModelCode(), new Object[]{this.getSelectedTypeCode()}));
			ComboBoxModel<String> option = new ComboBoxModel<String>(options);
			getOptionComboBox().getComponent().setModel(option);
			this.getOptionComboBox().getComponent().setSelectedItem(ProductSpec.WILDCARD);
			this.showLotControlRuleResult();
		} else {
			ComboBoxModel<String> option = new ComboBoxModel<String>(new ArrayList<String>());
			getOptionComboBox().getComponent().setModel(option);
			getOptionComboBox().getComponent().setSelectedIndex(-1);
		}
	}
	
	protected void optionChanged() {
		if ((this.getSelectedOptionCode()) != null) {
			Logger.getLogger().info(this.getName() + ": Option [" +  this.getSelectedOptionCode() + "] is selected");
			SortedArrayList<String> extColors = new SortedArrayList<String>(this.parentPanel.productSpecData.getModelExtColorCodes(this.getSelectedYearCode(), this.getSelectedModelCode(), new Object[]{this.getSelectedTypeCode()}, new Object[] {this.getSelectedOptionCode()}));
			ComboBoxModel<String> extColor = new ComboBoxModel<String>(extColors);
			getExtColorComboBox().getComponent().setModel(extColor);
			this.getExtColorComboBox().getComponent().setSelectedItem(ProductSpec.WILDCARD);
			this.showLotControlRuleResult();
		} else {
			ComboBoxModel<String> extColor = new ComboBoxModel<String>(new ArrayList<String>());
			getExtColorComboBox().getComponent().setModel(extColor);
			getExtColorComboBox().getComponent().setSelectedIndex(-1);
		}
	}
	
	protected void extColorChanged() {
		if ((this.getSelectedExtColorCode()) != null) {
			Logger.getLogger().info(this.getName() + ": Exterior color code [" +  this.getSelectedExtColorCode() + "] is selected");
			SortedArrayList<String> intColors = new SortedArrayList<String>(this.parentPanel.productSpecData.getModelIntColorCodes(this.getSelectedYearCode(), this.getSelectedModelCode(), new Object[]{this.getSelectedTypeCode()}, new Object[]{this.getSelectedOptionCode()}, new Object[]{this.getSelectedExtColorCode()}));
			ComboBoxModel<String> intColor = new ComboBoxModel<String>(intColors);
			getIntColorComboBox().getComponent().setModel(intColor);
			this.getIntColorComboBox().getComponent().setSelectedItem(ProductSpec.WILDCARD);
			this.showLotControlRuleResult();
		} else {
			ComboBoxModel<String> intColor = new ComboBoxModel<String>(new ArrayList<String>());
			getIntColorComboBox().getComponent().setModel(intColor);
			getIntColorComboBox().getComponent().setSelectedIndex(-1);	
		}
	}
	
	protected void intColorChanged() {
		if ((this.getSelectedIntColorCode()) != null){
			Logger.getLogger().info(this.getName() + ": Interior color code [" + getSelectedIntColorCode() + "] selected");	
			this.showLotControlRuleResult();
		}
	}
	
	public void tableChanged(TableModelEvent e) {
		if (e.getSource() instanceof LotControlRuleTableModel) {
			this.parentPanel.enableCopyButton();
			LotControlRuleTableModel model = (LotControlRuleTableModel) e.getSource();
			LotControlRule rule = model.getSelectedItem();
			if (rule == null)
				return;
			Exception exception = null;
			rule.setUpdateUser(this.parentPanel.getUserName());
			try {
				ServiceFactory.getDao(LotControlRuleDao.class).update(rule);
			} catch (Exception ex) {
				exception = ex;
				model.rollback();
			}
			this.parentPanel.handleException(exception);
		}
	}
	
	protected void resetPanel(SortedArrayList<String> procPointTags) {
		ComboBoxModel<String> model = new ComboBoxModel<String>(new ArrayList<String>());
		if (procPointTags != null)	model = new ComboBoxModel<String>(procPointTags);
		this.getProcPointComboBox().getComponent().setModel(model);
		this.getProcPointComboBox().getComponent().setSelectedIndex(-1);
		this.clearTables();
	}
	
	protected void clearTables() {
		this.lotControlRuleTableModel = new LotControlRuleTableModel(this.rulePanel.getTable(), new ArrayList<LotControlRule>());
		this.partByProductSpecTableModel = new PartByProductSpecTableModel(this.rulePartPanel.getTable(), new ArrayList<PartByProductSpecCode>());
	}
	
	protected void resetYmto(String product) {
		if (product != null) {
			SortedArrayList<String> years = new SortedArrayList<String>(this.parentPanel.productSpecData.getModelYearCodes());
			years.remove(ProductSpec.WILDCARD);
			ComboBoxModel<String> model = new ComboBoxModel<String>(years);
			this.yearComboBox.getComponent().setModel(model);
			this.yearComboBox.getComponent().setSelectedIndex(-1);
		}
		this.yearComboBox.getComponent().setSelectedIndex(-1);
	}
	
	protected void clearYmto() {
		ComboBoxModel<String> model = new ComboBoxModel<String>(new ArrayList<String>());
		this.yearComboBox.getComponent().setModel(model);		
	}
	
	protected String getProcessPointSortingMethodName() {
		CommonTlPropertyBean bean = PropertyService.getPropertyBean(CommonTlPropertyBean.class);
		return bean.getProcessPointSortingMethodName();
	}
	
	public List<String> getFilteredProcPointIds() {
		List<String> filteredIds = new ArrayList<String>();
		if (this.parentPanel.getSelectedProductType() != null) {
			for (ProcessPoint procPoint : this.parentPanel.processPoints.get(this.parentPanel.getSelectedProductType())){
				if (procPoint.getDivisionId().trim().equals(this.parentPanel.getSelectedDivision().trim()))
					filteredIds.add(procPoint.getId().trim());
			}
		}
		return filteredIds;
	}
	
	public void displayParts() {
    	List<LotControlRule> lotControlRules = this.getLotControlRuleTableModel().getSelectedItems();
    	if (lotControlRules == null || lotControlRules.size() != 1) {
    		((PartByProductSpecTableModel)this.getRulePartPanel().getTable().getModel()).refresh(null);
    		return;
    	}
    	LotControlRule lotControlRule = lotControlRules.get(0);
    	List<PartByProductSpecCode> parts = new ArrayList<PartByProductSpecCode>();
		if(this.parentPanel.isMbpnProduct()) parts = lotControlRule== null ? null : lotControlRule.getPartByProductSpecs();
		else parts = lotControlRule== null ? null : lotControlRule.getPartByProductSpecs();
		if (parts == null) return;
		this.partByProductSpecTableModel = new PartByProductSpecTableModel(this.getRulePartPanel().getTable(),parts);
		this.partByProductSpecTableModel.pack();
    }
}
