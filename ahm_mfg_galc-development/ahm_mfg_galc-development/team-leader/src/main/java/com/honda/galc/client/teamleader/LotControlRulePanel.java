package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.ClientMain;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.LabeledListBox;
import com.honda.galc.client.ui.component.MbpnSelectionPanel;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.ProcessPointSelectiontPanel;
import com.honda.galc.client.ui.component.ProductSpecSelectionBase;
import com.honda.galc.client.ui.component.ProductSpecSelectionPanel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.component.ProductSpecSelectionPanel.YmtocColumns;
import com.honda.galc.client.ui.event.ProcessPointSelectionEvent;
import com.honda.galc.client.ui.event.ProductSpecSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.client.ui.event.UpdateEvent;
import com.honda.galc.client.ui.tablemodel.LotControlRuleTableModel;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.PartByProductSpecCodeDao;
import com.honda.galc.dao.product.PartDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartByProductSpecCode;
import com.honda.galc.entity.product.PartByProductSpecCodeId;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.DataCollectionService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.AuditLoggerUtil;
import com.honda.galc.util.ServerInfoUtil;
import com.honda.galc.util.SortedArrayList;
/**
 * 
 * <h3>LotControlRulePanel Class description</h3>
 * <p> LotControlRulePanel description </p>
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
 * Aug 16, 2010
 *
 *
 */
/** * * 
* @version 2
* @author Gangadhararao Gadde 
* @since Aug 08, 2017
*/
public class LotControlRulePanel extends TabbedPanel implements ListSelectionListener, ActionListener, TableModelListener{

	
	private static final long serialVersionUID = 1L;
	
	private static final String CREATE_RULE ="Create New Rule";
	private static final String ADD_PART = "Add Part to Rule";
	private static final String DELETE_RULE = "Delete Rules";
	private static final String DELETE_PART = "Delete Part";
	private static final String COPY_RULE = "Copy Rules";
	private static final String PASTE_RULE = "Paste Rules";
	private static final String MOVE_RULE_UP = "Move Rule Up";
	private static final String MOVE_RULE_DOWN = "Move Rule Down";
	
	protected ProcessPointSelectiontPanel processPointPanel;
	protected ProductSpecSelectionBase productSpecSelectionPanel;	
	protected PartNameSelectionPanel partNameSelectionPanel;
	protected TablePane partSpecSelectionPanel;
	protected TablePane rulePanel;
	protected TablePane rulePartPanel;
	protected TablePane measurementListPanel;
	protected JButton refreshButton;	
	
	private PartSpecTableModel partSpecTableModel;
	private LotControlRuleTableModel lotControlRuleTableModel;
	private PartByProductSpecTableModel partByProductSpecTableModel;
	private MeasurementSpecListTableModel measurementListTableModel;

	
	private int processPointPanelWidth = 1000;
	private int processPointPanelHeight = 50;
	
	private int refreshButtonWidth = 80;
	
	private int midPanelHeight = 250;
	private int lowerPanelHeight = 300;
	private Dimension screenDimension;
	
	private boolean isHeadLess = false;
	private String currentProductType;
	private boolean errorDetected = false;
	
	
	private Map<LotControlRuleId,LotControlRule> copyCache = new LinkedHashMap<LotControlRuleId,LotControlRule>();

	public LotControlRulePanel() {
		super("Lot Control Rule", KeyEvent.VK_L);
		screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		AnnotationProcessor.process(this);
		
	}

	public LotControlRulePanel(TabbedMainWindow mainWindow) {
		super("Lot Control Rule", KeyEvent.VK_L,mainWindow);
		screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		AnnotationProcessor.process(this);
		
	}
	
	protected void initComponents() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		Border border = BorderFactory.createEmptyBorder(4,4,4,4);
		
		Box box1  = Box.createHorizontalBox();
		box1.setBorder(border);
		box1.add(createProcessPointSelectionPanel());
		box1.add(createRefreshButton());
		add(box1);
		
		Box box21 = Box.createVerticalBox();
		box21.add(createPartSpecSelectionPanel());
		box21.add(createMeasurementListPanel());
		
		Box box2  = Box.createHorizontalBox();
		box2.setBorder(border);
		box2.add(createProductSpecSelectionPanel());
		box2.add(createPartSelectionPanel());		
		box2.add(box21);
		add(box2);
		
		
		
		Box box3  = Box.createHorizontalBox();
		box3.setBorder(border);
		box3.add(createRulePanel());
        box3.add(createRulePartPanel());
        
        add(box3);
	}
	
	private ProcessPointSelectiontPanel createProcessPointSelectionPanel() {
		String siteName = PropertyService.getSiteName();
		if(StringUtils.isEmpty(siteName)){
			MessageDialog.showError(this, "SITE_NAME is not defined in the System_Info property");
		}
		
		processPointPanel= new ProcessPointSelectiontPanel(siteName);
		processPointPanel.setSize(processPointPanelWidth, processPointPanelHeight);
		
		return processPointPanel;
	}
	
	private JButton createRefreshButton() {
		refreshButton = new JButton("Refresh");
//		refreshButton.setLocation(startX + processPointPanelWidth - refreshButtonWidth, startY);
		refreshButton.setToolTipText("Refresh Lot Control Rule on server cache for selected headless process point");
		refreshButton.setSize(refreshButtonWidth, processPointPanelHeight);
		refreshButton.setEnabled(false);
		return refreshButton;
	}

	
	private ProductSpecSelectionBase createProductSpecSelectionPanel() {
		if(isMbpnProduct())
			productSpecSelectionPanel = new MbpnSelectionPanel();
		else
			productSpecSelectionPanel = new ProductSpecSelectionPanel();
		productSpecSelectionPanel.setSize(processPointPanel.getWidth() / 2,midPanelHeight);
		Dimension dim = new Dimension(screenDimension.width / 2, screenDimension.height /2);
		productSpecSelectionPanel.setPreferredSize(dim);
		productSpecSelectionPanel.setMaximumSize(dim);
		return productSpecSelectionPanel;
		
	}
	
	private PartNameSelectionPanel createPartSelectionPanel() {
		partNameSelectionPanel = new PartNameSelectionPanel(processPointPanel.getWidth() / 4,midPanelHeight,
				new Dimension(screenDimension.width / 4, screenDimension.height /2));
		
		return partNameSelectionPanel;
	}
	
	private TablePane createPartSpecSelectionPanel() {
		partSpecSelectionPanel = new TablePane("Part Spec");
		partSpecSelectionPanel.setSize(processPointPanel.getWidth() / 4 , midPanelHeight);
		Dimension dim = new Dimension(screenDimension.width / 4, screenDimension.height /4);
		partSpecSelectionPanel.setPreferredSize(dim);
		partSpecSelectionPanel.setMaximumSize(dim);     
		
		return partSpecSelectionPanel;
	}
	
	private TablePane createMeasurementListPanel() {
		measurementListPanel = new TablePane("Measurement Spec");
		measurementListPanel.setSize(processPointPanel.getWidth() / 4 , midPanelHeight);
		Dimension dim = new Dimension(screenDimension.width / 4, screenDimension.height /4);
		measurementListPanel.setPreferredSize(dim);
		measurementListPanel.setMaximumSize(dim);        
		return measurementListPanel;
	}
	
	
	private TablePane createRulePanel(){
		rulePanel = new TablePane("Lot Control Rule",ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		rulePanel.getTable().setRowHeight(20);
		Dimension dim = new Dimension((int)(screenDimension.width * 0.75), screenDimension.height /2);
		rulePanel.setPreferredSize(dim);
		rulePanel.setMaximumSize(dim); 
		return rulePanel;
	}
	
	private TablePane createRulePartPanel(){
		rulePartPanel = new TablePane("Part List");
		rulePartPanel.setSize((int)(processPointPanel.getWidth() * 0.25),lowerPanelHeight);
		Dimension dim = new Dimension((int)(screenDimension.width * 0.25), screenDimension.height /2);
		rulePartPanel.setPreferredSize(dim);
		rulePartPanel.setMaximumSize(dim); 
	    return  rulePartPanel;
	}
	
	@Override
	public void onTabSelected() {
		if(isInitialized) 
		{	
			// Refresh Part, Part Spec and Measurement Spec information
			updatePartSelectionModel();
			return;
		}
		initComponents();
		productSpecSelectionPanel.registerProcessPointSelectionPanel(processPointPanel);
		addListeners();
		isInitialized = true;
	}
	
	private void addListeners() {
		
		MouseListener listener = createMouseListener();
		if(isMbpnProduct()){
			for(LabeledListBox lbox : productSpecSelectionPanel.getColumnBoxsList()){
				lbox.getComponent().addMouseListener(listener);
			}
		}else{
			productSpecSelectionPanel.getPanel(YmtocColumns.Year.name()).getComponent().addMouseListener(listener);
			productSpecSelectionPanel.getPanel(YmtocColumns.Model.name()).getComponent().addMouseListener(listener);
			productSpecSelectionPanel.getPanel(YmtocColumns.Model_Type.name()).getComponent().addMouseListener(listener);
		}
		partNameSelectionPanel.getPartSelectionPanel().getTable().addMouseListener(listener);
		partSpecSelectionPanel.getTable().addMouseListener(listener);
		rulePanel.getTable().addMouseListener(createRuleCopyPasteDeleteListener());
		rulePanel.addMouseListener(createRuleCopyPasteDeleteListener());
		rulePartPanel.getTable().addMouseListener(createPartListMouseListener());		
		partNameSelectionPanel.getPartSelectionPanel().addListSelectionListener(this);
		
		refreshButton.addActionListener(this);
	}
	
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource().equals(processPointPanel.getProcessPointComboBox().getComponent())) processPointSelected();
         if(e.getSource() instanceof JMenuItem) {
        	 Exception exception = null;
        	 try{
	        	 JMenuItem menuItem = (JMenuItem)e.getSource();
	        	 logUserAction("selected menu item: " + menuItem.getName());
	        	 if(menuItem.getName().equals(CREATE_RULE)) createRules();
	        	 else if(menuItem.getName().equals(ADD_PART)) createRules();
	        	 else if(menuItem.getName().equals(DELETE_RULE)) deleteRules();
	        	 else if(menuItem.getName().equals(DELETE_PART)) deletePart();
	        	 else if(menuItem.getName().equals(COPY_RULE)) copyRules();
	        	 else if(menuItem.getName().equals(PASTE_RULE)) pasteRules();
	        	 else if(menuItem.getName().equals(MOVE_RULE_UP)) moveRuleUp();
	        	 else if(menuItem.getName().equals(MOVE_RULE_DOWN)) moveRuleDown();
        	 }catch(Exception ex) {
        		 exception = ex;
        	 }
        	 handleException(exception);
         }
         
         if(e.getSource().equals(refreshButton)) processRefreshButton();
    }
    
    private void processRefreshButton() {
    	//added login to let the associate to make sure they want to refresh rule
    	if(LoginDialog.login() == LoginStatus.OK){
    		
    		ServiceFactory.getService(DataCollectionService.class).refreshLotControlRuleCache
    		(processPointPanel.getCurrentProcessPoint().getProcessPointId());
    		
    		refreshRuleCacheOnMultiServer(processPointPanel.getCurrentProcessPoint().getProcessPointId());
    		
    		if (!isHeadLess)
    			setErrorMessage("Headed clients will have to be restarted for changes to their Lot Control Rules to take effect");
    		
    		getLogger().info("Lot Control Rules for Process Point:", 
    				processPointPanel.getCurrentProcessPoint().getProcessPointId(),
    				" were refreshed by user:",
    				ClientMain.getInstance().getAccessControlManager().getUserName());
    	}
		
	}

    
    private boolean refreshRuleCacheOnMultiServer(String processPointId) {
			
    	Map<String,Exception> messages = ServerInfoUtil.refreshLotControlRule(processPointId);
    	for(Map.Entry<String, Exception> entry : messages.entrySet()) {
    		if(entry.getKey().equals("*")) 
    			getLogger().error(entry.getValue(),"Failed to refresh lot control cache at Process point : " + processPointId);
    		else if(entry.getValue() == null)
    			getLogger().info("Refresh lot control rule cache of Process point Id " + processPointId + " on server " + entry.getKey() + " Successfully");
    		else getLogger().error(entry.getValue(), "Failed to refresh lot control cache at Process point : " + processPointId + " on server " + entry.getKey());
			}
		return true;
    }
    

	private void deletePart() {
		PartByProductSpecCode part = partByProductSpecTableModel.getSelectedItem();
		if(part == null) return;
		LotControlRule rule =lotControlRuleTableModel.getSelectedItem();
		if(partByProductSpecTableModel.getRowCount() == 1) {
			if(rule == null) return;
			if(!MessageDialog.confirm(this, "Are you sure that you want to delete this part?"))
					return;
			logUserAction("confirmed to delete", part);
			List<LotControlRule> rules = getDao(LotControlRuleDao.class).findRuleByMtocAndPartName(part,rule.getPartNameString());
			if(rules.size() < 2){			
				getDao(PartByProductSpecCodeDao.class).remove(part);
				logUserAction(REMOVED, part);
				AuditLoggerUtil.logAuditInfo(part, null,"delete", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");

			}
			getDao(LotControlRuleDao.class).remove(rule);
			logUserAction(REMOVED, rule);
			AuditLoggerUtil.logAuditInfo(rule, null,"delete", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
			lotControlRuleTableModel.remove(rule);
			
		}else {
			getDao(PartByProductSpecCodeDao.class).remove(part);
			logUserAction(REMOVED, part);
			AuditLoggerUtil.logAuditInfo(part, null,"delete", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
			partByProductSpecTableModel.remove(part);
			checkDuplicateRule(rule);
		}
	}

	private void deleteRules() {
		
		List<LotControlRule> rules =lotControlRuleTableModel.getSelectedItems();
		if(rules.isEmpty()) return;
		if(!MessageDialog.confirm(this, "Are you sure that you want to delete selected lot control rules?"))
			return;
		logUserAction("confirmed to delete LotControlRules.");
		for(LotControlRule rule : rules) {
			List<PartByProductSpecCode> partByProductSpecs = rule.getPartByProductSpecs();
			for(PartByProductSpecCode partByProductSpecCode: partByProductSpecs){
						
				List<LotControlRule> lotControlRules = findLotControlRules(partByProductSpecCode);
				if(rules.containsAll(lotControlRules)){
					getDao(PartByProductSpecCodeDao.class).remove(partByProductSpecCode);
					logUserAction(REMOVED, partByProductSpecCode);
					AuditLoggerUtil.logAuditInfo( partByProductSpecCode, null,"delete", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");

				}else{
					getLogger().info("Cannot remove Part:"+partByProductSpecCode.toString() +" from GAL245Tbx as part is used by other LotControlRules");
				}
			}
		
			getDao(LotControlRuleDao.class).remove(rule);
			logUserAction(REMOVED, rule);
			AuditLoggerUtil.logAuditInfo(rule, null,"delete", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
			updateLotControlRulesSeqNumber(retrieveRulesByMtocProcessPointId(rule));
		}
		
		showLotControlRuleResult();
		
	}
	

	private List<LotControlRule> findLotControlRules(PartByProductSpecCode partByProductSpecCode) {
		ProductType productType = ProductType.valueOf(processPointPanel.selectedProductType());
		 
		switch(productType) {
		case MBPN:
		case MBPN_PART:
			return getDao(LotControlRuleDao.class).findRuleByMbpnAndPartName(partByProductSpecCode, partByProductSpecCode.getId().getPartName());
		default:
			return getDao(LotControlRuleDao.class).findRuleByYmtociAndPartName(partByProductSpecCode, partByProductSpecCode.getId().getPartName());
		}
		
	}
	
	private void performMove(boolean isMoveUp)
	{
		List<LotControlRule> selectedRulesList =lotControlRuleTableModel.getSelectedItems();
		
		if(selectedRulesList.isEmpty())
		{
			MessageDialog.showError(this, "No rule Selected");
			return;
		}
		else if(selectedRulesList.size()>1)
		{
			MessageDialog.showError(this, "Please select one rule at a time.");
			return;
		}
		else{
			List<LotControlRule> rulesList = retrieveRulesByMtocProcessPointId(lotControlRuleTableModel.getSelectedItem());
			int selectedIndex=rulesList.indexOf(lotControlRuleTableModel.getSelectedItem());
			if(isMoveUp && selectedIndex==0)
			{
				MessageDialog.showError(this, "Cannot move the first rule in list up.");
				return;
			}
			if(!isMoveUp && selectedIndex==rulesList.size()-1)
			{
				MessageDialog.showError(this, "Cannot move last rule in the list down.");
				return;
			}
			if (!MessageDialog.confirm(this, "Are you sure that you want to move selected lot control rule?"))
				return;
			if(isMoveUp)
				Collections.swap(rulesList,selectedIndex,selectedIndex-1);
			else
				Collections.swap(rulesList,selectedIndex,selectedIndex+1);
			updateLotControlRulesSeqNumber(rulesList);
			showLotControlRuleResult();
		}		
	}

	private void moveRuleUp()
	{
		performMove(true);

	}
	private void moveRuleDown()
	{
		performMove(false);
	}

	private void updateLotControlRulesSeqNumber(List<LotControlRule> rulesList) {
		int i=1;
		for(LotControlRule rule:rulesList)
		{
			rule.setSequenceNumber(i);
			rule.setUpdateUser(getUserName());
			i++;
		}
		if(!rulesList.isEmpty()) {
			getDao(LotControlRuleDao.class).saveAll(rulesList);
			logUserAction(SAVED, rulesList);
		}
	}

	private void createRules() {
    	List<LotControlRule> newRules = createLotControlRules();
    	
    	for(LotControlRule newRule : newRules){
    		checkDuplicateRule(newRule); 
    		if(!lotControlRuleTableModel.hasRule(newRule)) {
    			createRule(newRule);
    		} else {
    			createPart(newRule);
    		}
    		  	
    	}
    	showLotControlRuleResult();
    	rulePanel.getTable().getSelectionModel().setSelectionInterval(0, 0);
    	
    }
	
	private void copyRules() {
		copyCache.clear();
		List<LotControlRule> lotControlRuleList = lotControlRuleTableModel.getSelectedItems();
		for (LotControlRule rule : lotControlRuleList) {
			copyCache.put(rule.getId(), rule);
		}
	}

	private void pasteRules() {
		List<String> specCodes = ProductSpec.trimWildcard(productSpecSelectionPanel.buildSelectedProductSpecCodes());
		if(specCodes.size() == 0 || copyCache.isEmpty()) return;
		String pp = processPointPanel.getCurrentProcessPointId();
		
		for(String ymtoc : specCodes) {
			String specCode = ProductSpec.trimWildcard(ymtoc);
			for(LotControlRule rule : copyCache.values()) {
				// Set new selected YMTOC
				 rule.getId().setProductSpecCode(specCode);
				 if(!isMbpnProduct()){
					 String modelYearCode = ProductSpec.extractModelYearCode(specCode);
					rule.getId().setModelYearCode(StringUtils.isEmpty(modelYearCode) ? ProductSpec.WILDCARD : modelYearCode);
					String modelCode = ProductSpec.extractModelCode(specCode);
		        	rule.getId().setModelCode(StringUtils.isEmpty(modelCode) ? ProductSpec.WILDCARD : modelCode);
		        	String modelTypeCode = ProductSpec.extractModelTypeCode(specCode);
		        	rule.getId().setModelTypeCode(StringUtils.isEmpty(modelTypeCode) ? ProductSpec.WILDCARD : modelTypeCode);
		        	String modelOptionCode = ProductSpec.extractModelOptionCode(specCode);
		        	rule.getId().setModelOptionCode(StringUtils.isEmpty(modelOptionCode) ? ProductSpec.WILDCARD : modelOptionCode);
		        	String extColorCode = ProductSpec.extractExtColorCode(specCode);
		        	rule.getId().setExtColorCode(StringUtils.isEmpty(extColorCode) ? ProductSpec.WILDCARD : extColorCode);
		        	String intColorCode = ProductSpec.extractIntColorCode(specCode);
		        	rule.getId().setIntColorCode(StringUtils.isEmpty(intColorCode) ? ProductSpec.WILDCARD : intColorCode);
		        	rule.getId().setPartName(rule.getId().getPartName());
		        	rule.getPartName().setPartName(rule.getPartName().getPartName());
    			}
	        	// Check if the rule is used by other process point
				if(isRuleUsed(rule)) return;
				// Set new selected process point ID
				rule.getId().setProcessPointId(pp); 
				rule.setUpdateUser(getUserName());
		    	getDao(LotControlRuleDao.class).save(rule);
		    	logUserAction(SAVED, rule);
		    	AuditLoggerUtil.logAuditInfo( null,rule,"save", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
		    	pastePart(rule);
		    	checkDuplicateRule(rule);	
		    	updateLotControlRulesSeqNumber(retrieveRulesByMtocProcessPointId(rule));
			}
		}	
		
		showLotControlRuleResult();
	
	}
	
	private void createRule(LotControlRule newRule) {
		LotControlRule rule = lotControlRuleTableModel.findRule(newRule);
    	if(rule != null) {
    		newRule = rule;
    	}else {
    		//check if the rule is used by other process point
    		if(isRuleUsed(newRule)) return;
    	}
    	
		getLogger().info("Creating LotControlRule ...");
    	if(newRule.getSequenceNumber() == 1) newRule.setSequenceNumber(generateNewSeqNumber()); 
    	
    	createPart(newRule);
    	//BCC 10/14/11: In HMA's DB, CreateTimestamp cannot be null.
    	newRule.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
    	
    	newRule.setUpdateUser(getUserName());
    	getDao(LotControlRuleDao.class).insert(newRule);
    	logUserAction(INSERTED, newRule);
    	AuditLoggerUtil.logAuditInfo( null,newRule,"save", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
	}
	
	private void checkDuplicateRule(LotControlRule newRule) {
		if(newRule == null || newRule.getId().getProcessPointId() == null || newRule.getId().getPartName() == null) return;
		clearErrorMessage();
		LotControlRule toBeCheckedRule = null;
		List<LotControlRule> list = getDao(LotControlRuleDao.class).findAllByProcessPointIdAndPartName(newRule.getId().getProcessPointId(), newRule.getPartNameString());
		String duplicateRules = "";
		for(LotControlRule rule : list) {
			if(rule.getId().equals(newRule.getId())) {
				toBeCheckedRule = rule;
				break;
			}
		}
		if(toBeCheckedRule == null) return;	
		list.remove(toBeCheckedRule);
		
		for(LotControlRule rule : list) {
			if(isMbpnProduct() ? rule.functionEqualsForMBPN(newRule) : rule.functionEquals(newRule))
				duplicateRules = duplicateRules.concat("\n(" + 
														rule.getId().getProcessPointId() + ", " + 
														rule.getId().getPartName() + ", " + 
														rule.getId().getYMTO() + ")");
		}
		if (StringUtils.isBlank(duplicateRules)) return;
		
		errorDetected = true;
		this.getMainWindow().setErrorMessage("Duplicate rule(s) found: " + duplicateRules);
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				errorDetected = false;
			}
		});
	}
	
	private int generateNewSeqNumber() {
		int maxNum = 0;
		List<LotControlRule> lotControlRules =  retrieveSelectedLotControlRules(false);
		for (LotControlRule r : lotControlRules)
			if(r.getSequenceNumber() > maxNum) maxNum = r.getSequenceNumber();
			
		return maxNum +1;
	}

	private void createPart(LotControlRule rule) {
		PartByProductSpecCode partByProductSpecCode = new PartByProductSpecCode();
    	PartByProductSpecCodeId id = new PartByProductSpecCodeId();
    	id.setProductSpecCode(rule.getId().getProductSpecCode());
    	if(!isMbpnProduct()) id.setYmtoc(rule.getId());
    	
    	
    	if(isHeadLess && partSpecTableModel.getSelectedItem() == null) return;
    	
		getLogger().info("Creating a new PartByProductSpecCode ...");
    	id.setPartId(partSpecTableModel.getSelectedItem().getId().getPartId());
    	id.setPartName(partSpecTableModel.getSelectedItem().getId().getPartName());
    	partByProductSpecCode.setId(id);
    	partByProductSpecCode.setUpdateUser(getUserName());
    	PartByProductSpecCode oldSpecCode = getDao(PartByProductSpecCodeDao.class).findByKey(id);
    	getDao(PartByProductSpecCodeDao.class).save(partByProductSpecCode);
    	logUserAction(SAVED, partByProductSpecCode);
    	if(oldSpecCode ==null) {
    		AuditLoggerUtil.logAuditInfo(null,partByProductSpecCode,"save", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
    	} 
    	else {
    		AuditLoggerUtil.logAuditInfo(oldSpecCode,partByProductSpecCode,"update", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
    	}
	}
	
	private void pastePart(LotControlRule rule) {
		logUserAction("Pasting PartByProductSpecCode ...");
		List<PartByProductSpecCode> parts = rule.getPartByProductSpecs();
		List<PartByProductSpecCode> items = new ArrayList<PartByProductSpecCode>();
    	for (PartByProductSpecCode part : parts) {
    		if(!isMbpnProduct()) {
    			part.getId().setYmtoc(rule.getId());
    		}
           	part.getId().setProductSpecCode(rule.getId().getProductSpecCode());
           	part.setUpdateUser(getUserName());
    		items.add(part);
    	}
    	
		getDao(PartByProductSpecCodeDao.class).saveAll(items);
		logUserAction(SAVED, items);
		for(PartByProductSpecCode item:items) {
			AuditLoggerUtil.logAuditInfo(null,item,"save", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
		}
	}
	
    private boolean isRuleUsed(LotControlRule rule) {
    	String ppId = rule.getId().getProcessPointId();
    	String productType = processPointPanel.selectedProductType();
    	rule.getId().setProcessPointId(null);
    	List<LotControlRule> rules = getDao(LotControlRuleDao.class).findAllById(rule.getId(),productType);
    	rule.getId().setProcessPointId(ppId);
    	if(rules.size() > 0 && rulesBelongToSameDivisionAndLine(rules)) {
    		String msg="Lot Control is already defined for different process point. " + rules.get(0).getId().getPartName() + " - " + rules.get(0).getId().getProcessPointId()
    				+" Do you want to add this part to multiple Process points? ";
    		//@KM : Allowing multiple process points to assign same part
    		int response = JOptionPane.showConfirmDialog(this, msg,
					"Confirm", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
    		
    		if(response == JOptionPane.YES_OPTION)
    			return false;
    		else if (response == JOptionPane.NO_OPTION) {
				return true;
			} else if (response == JOptionPane.CLOSED_OPTION) {
				return true;
			}
    		Logger.getLogger().info(msg);
    		
    		return true;
    	}
    	return false;
    }
    
    private List<LotControlRule> createLotControlRules() {
    	
    	List<LotControlRule> rules = new ArrayList<LotControlRule>();
    	List<String> specCodes = productSpecSelectionPanel.buildSelectedProductSpecCodes();
    	
    	for(String productSpecCode : specCodes) {    		
    		String specCode = ProductSpec.trimWildcard(productSpecCode);
    		LotControlRule rule = new LotControlRule();
        	LotControlRuleId id = new LotControlRuleId();
        	
        	id.setPartName(partNameSelectionPanel.getPartNameTableModel().getSelectedItem().getPartName());
        	id.setProcessPointId(processPointPanel.getCurrentProcessPointId());
        	id.setProductSpecCode(specCode);
        	if(!isMbpnProduct()){
        		String modelYearCode = ProductSpec.extractModelYearCode(specCode);
	        	id.setModelYearCode(StringUtils.isEmpty(modelYearCode) ? ProductSpec.WILDCARD : modelYearCode);
	        	String modelCode = ProductSpec.extractModelCode(specCode);
	        	id.setModelCode(StringUtils.isEmpty(modelCode) ? ProductSpec.WILDCARD : modelCode);
	        	String modelTypeCode = ProductSpec.extractModelTypeCode(specCode);
	        	id.setModelTypeCode(StringUtils.isEmpty(modelTypeCode) ? ProductSpec.WILDCARD : modelTypeCode);
	        	String modelOptionCode = ProductSpec.extractModelOptionCode(specCode);
	        	id.setModelOptionCode(StringUtils.isEmpty(modelOptionCode) ? ProductSpec.WILDCARD : modelOptionCode);
	        	String extColorCode = ProductSpec.extractExtColorCode(specCode);
	        	id.setExtColorCode(StringUtils.isEmpty(extColorCode) ? ProductSpec.WILDCARD : extColorCode);
	        	String intColorCode = ProductSpec.extractIntColorCode(specCode);
	        	id.setIntColorCode(StringUtils.isEmpty(intColorCode) ? ProductSpec.WILDCARD : intColorCode);
        	}
        	
        	rule.setId(id);
        	if(rule.getSequenceNumber() <= 0) rule.setSequenceNumber(1);
        	rule.setPartConfirmFlag(partNameSelectionPanel.getPartNameTableModel().getSelectedItem().getPartConfirmCheck());
        	rules.add(rule);
            	
    	}
    	return rules;
    }
    
    private void processPointSelected() {
		ProcessPoint processPoint = processPointPanel.getCurrentProcessPoint();
		if(processPoint == null) return;
		getLogger().check("User selected process point: " + processPoint.getProcessPointId());
		initPartSelectionPanel();
		isHeadLess = isHeadLess(processPoint);
		List<LotControlRule> lotControlRules = getDao(LotControlRuleDao.class).findAllByProcessPoint(processPoint.getProcessPointId());
		lotControlRuleTableModel = new LotControlRuleTableModel(rulePanel.getTable(),lotControlRules);
		rulePanel.addListSelectionListener(this);
	}
    
    private void showLotControlRuleResult() {
    	Exception exception = null;    	
    	try{
	    	LotControlRule selectedRule =lotControlRuleTableModel == null ? null : lotControlRuleTableModel.getSelectedItem();
	    	List<LotControlRule> lotControlRules = retrieveSelectedLotControlRules(true);
			lotControlRuleTableModel = new LotControlRuleTableModel(isHeadLess,rulePanel.getTable(),lotControlRules);
			lotControlRuleTableModel.addTableModelListener(this);
			rulePanel.addListSelectionListener(this);
			
			if(selectedRule != null) { 
				lotControlRuleTableModel.selectItem(lotControlRuleTableModel.findRule(selectedRule));
			}
    	}catch(Exception ex) {
    		exception = ex;
    	}
    	handleException(exception);
    }
    
    private List<LotControlRule> retrieveSelectedLotControlRules(boolean isFilterPartName) {
    	
    	List<String> specCodes = productSpecSelectionPanel.buildSelectedProductSpecCodes();
    	List<LotControlRule> rules = new ArrayList<LotControlRule>();
    	List<ProcessPoint> ppList = processPointPanel.getApplicableProcessPoints();
    	List<LotControlRule> ruleList;

    	for(String productSpecCode : specCodes) {
    		
    		String specCode = ProductSpec.trimWildcard(productSpecCode);
    		LotControlRuleId id = new LotControlRuleId();
			if(ppList.size() == 1) {
				id.setProcessPointId(ppList.get(0).getProcessPointId());
			}
			id.setProductSpecCode(productSpecCode);
			if(!isMbpnProduct()){
				id.setModelYearCode(ProductSpec.extractModelYearCode(specCode));
				id.setModelCode(ProductSpec.extractModelCode(specCode));
				id.setModelTypeCode(ProductSpec.extractModelTypeCode(specCode));
				id.setModelOptionCode(ProductSpec.extractModelOptionCode(specCode));
				id.setExtColorCode(ProductSpec.extractExtColorCode(specCode));
				id.setIntColorCode(ProductSpec.extractIntColorCode(specCode));
			}
			
			PartName partName = (partNameSelectionPanel == null ||partNameSelectionPanel.getPartNameTableModel() ==null)? null : 
				partNameSelectionPanel.getPartNameTableModel().getSelectedItem();
			if(partName != null && isFilterPartName) id.setPartName(partName.getPartName());

			ruleList = getDao(LotControlRuleDao.class).findAllById(id,processPointPanel.selectedProductType());
			if(ppList.size() == 1) {
				rules.addAll(ruleList);
			} else {
	    		for(LotControlRule aRule : ruleList) {
	    			for(ProcessPoint pp : ppList) {
		    			if(pp.getId().equals(aRule.getId().getProcessPointId())) {
		    				rules.add(aRule);
		    			}
		    		}
		    	}
			} 
    	}
    	
    	return rules;
    	
    }
    
    private List<LotControlRule> retrieveRulesByMtocProcessPointId(LotControlRule selectedRule) {
    	List<LotControlRule> rules = new ArrayList<LotControlRule>();   	
    	LotControlRuleId id = new LotControlRuleId();
    	id.setProcessPointId(selectedRule.getId().getProcessPointId());
    	id.setProductSpecCode(selectedRule.getId().getProductSpecCode());
    	if(!isMbpnProduct()){
			id.setModelYearCode(selectedRule.getId().getModelYearCode());
			id.setModelCode(selectedRule.getId().getModelCode());
			id.setModelTypeCode(selectedRule.getId().getModelTypeCode());
			id.setModelOptionCode(selectedRule.getId().getModelOptionCode());
			id.setExtColorCode(selectedRule.getId().getExtColorCode());
			id.setIntColorCode(selectedRule.getId().getIntColorCode());
		}
     	rules=getDao(LotControlRuleDao.class).findAllById(id,processPointPanel.selectedProductType());
    	return rules;
    }

    private void initPartSelectionPanel() {
    	// 01-19-12 DJensen: Update part list each time a dept or process point is selected, so
    	//          that part specs for any given part can be refreshed w/o leaving screen
    	
		String productType = processPointPanel.selectedProductType();
		if(productType != null) {
			if(!productType.equalsIgnoreCase(currentProductType)) {
				currentProductType = productType;
				updatePartSelectionModel();
			}
		}
    }

    @EventSubscriber(eventClass=UpdateEvent.class)
    public void processUpdateEvent(UpdateEvent event){
    	if(event.isUpdatePartName())
    		updatePartSelectionModel();
    }
    
	private void updatePartSelectionModel() {
		if(partNameSelectionPanel == null) return;
		
		List<PartName> partNames = new ArrayList<PartName>();
		String productType = processPointPanel.selectedProductType();
		if(productType != null)	partNames = loadPartNames(productType);
		
		partNameSelectionPanel.update(partNames);
	}

	private List<PartName> loadPartNames(String productType) {
		return ServiceFactory.getDao(PartNameDao.class).findAllByProductType(productType);
	}    

    @EventSubscriber(eventClass=ProcessPointSelectionEvent.class)
    public void processPointSelectedPanelChanged(ProcessPointSelectionEvent event) {
    	if(event.isEventFromSource(SelectionEvent.DEPARTMENT_SELECTED, processPointPanel)){ 
        		initPartSelectionPanel();
    	} else if(event.isEventFromSource(SelectionEvent.PRODUCT_TYPE_SELECTED, processPointPanel)) {
    		initPartSelectionPanel();
    	}else if(event.isEventFromSource(SelectionEvent.PROCESSPOINT_SELECTED, processPointPanel)) {
    		if(processPointPanel.getCurrentProcessPoint() == null) return;
    		isHeadLess = isHeadLess(processPointPanel.getCurrentProcessPoint());
    		initPartSelectionPanel();
    		showLotControlRuleResult();
    		enableRefreshButton(processPointPanel.isProcessPointSelected());
    	}
    }
    
    private void enableRefreshButton(boolean headLess) {
    	//for now, Only enabled to fresh headless process point
		refreshButton.setEnabled(headLess);
		
	}

	private boolean isHeadLess(ProcessPoint processPoint) {
		if(processPoint == null ) return false;
    	return (getDao(TerminalDao.class).findFirstByProcessPointId(processPoint.getProcessPointId()) == null);
	}

    
    
    @EventSubscriber(eventClass=ProductSpecSelectionEvent.class)
    public void productSpecSelectedPanelChanged(ProductSpecSelectionEvent event) {
    	if(event.isEventFromSource(SelectionEvent.SELECTING, productSpecSelectionPanel) ||
    	   event.isEventFromSource(SelectionEvent.POPULATED, productSpecSelectionPanel) || 
    	   event.isEventFromSource(SelectionEvent.SELECTED, productSpecSelectionPanel)){ 
    		showLotControlRuleResult();
    	}
        	
    }
    
	private void showCopyPasteDeleteRulePopupMenu(MouseEvent e) {
		Logger.getLogger().info("LotControlRule Panel CopyPasteDeleteRulePopupMenu enabled");
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(COPY_RULE, isCopyEnabled()));
		popupMenu.add(createMenuItem(PASTE_RULE, isPasteEnabled() && !copyCache.isEmpty()));
		popupMenu.add(createMenuItem(DELETE_RULE, isPartSpecSelected()&& isRuleSelected()));
		popupMenu.add(createMenuItem(MOVE_RULE_UP, isMoveUpDownEnabled()));
		popupMenu.add(createMenuItem(MOVE_RULE_DOWN, isMoveUpDownEnabled()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
    
    private void showCreateRulePopupMenu(MouseEvent e) {
    	Logger.getLogger().info("PartSpecSelection Panel CreateRulePopupMenu enabled");
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(deriveAddRuleMenuName(),areAllSelected()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
    
    private void showDeletePartPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(DELETE_PART,partByProductSpecTableModel.getSelectedItem() != null));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
    
    private String deriveAddRuleMenuName() {
         return isAddPartEnabled() ? ADD_PART : CREATE_RULE;
         
    }
    
    private boolean isCopyEnabled() {
    	return  processPointPanel.isProcessPointSelected() && 
    	rulePanel.getTable().getSelectedRowCount() > 0;
    }
    
    private boolean isMoveUpDownEnabled() {
    		return (processPointPanel.isProcessPointSelected() && productSpecSelectionPanel.isProductSpecSelected() && isRuleSelected());    	
    }
   
    
	private boolean isPasteEnabled() {
		return productSpecSelectionPanel.isProductSpecSelected();
	}
 
    private boolean areAllSelected() {
    	return processPointPanel.isProcessPointSelected() && 
    	       productSpecSelectionPanel.isProductSpecSelected() &&
    	       (isPartSpecSelected() || isHeadLess);
    }
    
    private boolean isAddPartEnabled() {
    	return areAllSelected() && hasRule();
    }
    
    private boolean isRuleSelected() {
		return !lotControlRuleTableModel.getSelectedItems().isEmpty();
	}
    
    private boolean hasRule() {
    	List<LotControlRule> rules = createLotControlRules();
    	
    	for(LotControlRule rule : rules) {
    		if(lotControlRuleTableModel.hasRule(rule)) return true;
    	}
    	return false;
    }

    
    private boolean isPartSpecSelected() {
    	return (partSpecTableModel != null && partSpecTableModel.getSelectedItem() != null) ||
    	(isHeadLess && partNameSelectionPanel.getPartNameTableModel().getSelectedItem()!= null && 
    		(partSpecTableModel == null || partSpecTableModel.getRowCount() == 0));
    }
    
    private MouseListener createMouseListener(){
    	return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showCreateRulePopupMenu(e);
			}
		 }); 
 	}
    
    private MouseListener createRuleCopyPasteDeleteListener(){
    	return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showCopyPasteDeleteRulePopupMenu(e);
			}
		 });  
	}
    
    private MouseListener createPartListMouseListener(){
    	return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showDeletePartPopupMenu(e);
			}
		 });   
 	}
    
    public void valueChanged(ListSelectionEvent e) {
        
    	if(e.getValueIsAdjusting()) return;
    	Exception exception = null;
    	try{
	    	if(e.getSource() ==(partNameSelectionPanel.getPartSelectionPanel().getTable().getSelectionModel())){
	         //	 part name selected
				PartName partName = partNameSelectionPanel.getPartNameTableModel().getSelectedItem();
				loadPartSpecTable(partName);
					
	    	}else if(e.getSource() ==(rulePanel.getTable().getSelectionModel())){
	    		// rule selected
	    		LotControlRule lotControlRule = lotControlRuleTableModel.getSelectedItem();
	    		List<PartByProductSpecCode> parts = new ArrayList<PartByProductSpecCode>();
	    		if(isMbpnProduct()) parts = lotControlRule== null ? null : lotControlRule.getPartByProductSpecs();
	    		else parts = lotControlRule== null ? null : lotControlRule.getPartByProductSpecs();
	    		partByProductSpecTableModel = new PartByProductSpecTableModel(rulePartPanel.getTable(),parts);
	    		partByProductSpecTableModel.pack();
	    	}else if (e.getSource() == (partSpecSelectionPanel.getTable().getSelectionModel())) {
				PartSpec partSpec = partSpecTableModel.getSelectedItem();

				measurementListTableModel = new MeasurementSpecListTableModel(measurementListPanel.getTable(),
						partSpec == null ? null : partSpec.getMeasurementSpecs());
				measurementListTableModel.pack();
			}
    	}catch(Exception ex) {
    		exception = ex;
    	}
    	
    	handleException(exception);
    		
   }

	public void tableChanged(TableModelEvent e) {
		if(e.getSource() instanceof LotControlRuleTableModel) {
			LotControlRuleTableModel model =  (LotControlRuleTableModel)e.getSource();
			LotControlRule rule = model.getSelectedItem();
			if(rule == null) return;
			Exception exception = null;
			try{
				LotControlRule ruleBeforeUpdate = getDao(LotControlRuleDao.class).findByKey(rule.getId());
				validateLotControlRule(model);
				rule.setUpdateUser(getUserName());
				ServiceFactory.getDao(LotControlRuleDao.class).update(rule);
				logUserAction(UPDATED, rule);
				LotControlRule ruleAfterUpdate = getDao(LotControlRuleDao.class).findByKey(rule.getId());
				AuditLoggerUtil.logAuditInfo(ruleBeforeUpdate ,ruleAfterUpdate,"update", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
				checkDuplicateRule(rule);				
				if(e.getColumn() == 9) { // If Unique checkbox is clicked, sync GAL206TBX table. If at least one rule has unique part serial is checked, then update ALLOW_DUPLICATES coulmn in GAL206TBX to 1
					int uniqueFlag = rule.getSerialNumberUniqueFlag();
					String partName = rule.getPartName().getPartName();
					ServiceFactory.getDao(PartDao.class).updateAllowDuplicate(partName, uniqueFlag);
				}
			}catch(Exception ex) {
				exception = ex;
				model.rollback();
			}
			handleException(exception);
			
		}
	}

	private void validateLotControlRule(LotControlRuleTableModel model) {
		//validate Lot Control Rule Sequence Number
		LotControlRule currentrule = model.getSelectedItem();
		List<String> partNameList = new ArrayList<String>();
		
		List<LotControlRule> ruleList = isPartNameSelected() ? retrieveSelectedLotControlRules(false) : model.getItems();
		
		for(LotControlRule r : ruleList){
			if(currentrule.getSequenceNumber() == r.getSequenceNumber() &&
					!r.getPartNameString().equals(currentrule.getPartNameString()))
				partNameList.add(r.getPartNameString());
		}
		
		if(partNameList.size() > 0) throw new TaskException("Invalid Sequence Number:" + currentrule.getSequenceNumber() + " already used by parts:" + partNameList);
		//more validations if required 
	}

	private boolean isPartNameSelected() {
		return partNameSelectionPanel != null && partNameSelectionPanel.getPartNameTableModel() != null && 
			partNameSelectionPanel.getPartNameTableModel().getSelectedItem() != null;
		
	}

	private void loadPartSpecTable(PartName partName) {
		List<PartSpec> partSpecs = (partName == null) ? new ArrayList<PartSpec>() : partName.getAllPartSpecs();

		partSpecTableModel = new PartSpecTableModel(partSpecSelectionPanel.getTable(), partSpecs, false);
		showLotControlRuleResult();

		partSpecSelectionPanel.addListSelectionListener(this);

		if (measurementListTableModel != null
				&& partSpecTableModel.getSelectedItem() == null)
			measurementListTableModel.refresh(new ArrayList<MeasurementSpec>());
	}

	@Override
	protected void handleException (Exception e) {
		if(e != null) {
			getLogger().error(e, "unexpected exception occurs: " + e.getMessage() + " stack trace:" + getStackTrace(e));
			this.setErrorMessage(e.getMessage());
		} else if(!errorDetected) {
			clearErrorMessage();
		}
	}
	
	private boolean rulesBelongToSameDivisionAndLine(List<LotControlRule> rules){
		ProcessPoint selectedProcessPoint = (ProcessPoint)processPointPanel.getProcessPointComboBox().getComponent().getSelectedItem();
		SortedArrayList<ProcessPoint> allProcessPoints = processPointPanel.getAllProcessPoints();
		Map<String,ProcessPoint> pptMap = new HashMap<String,ProcessPoint>();
		for(int i=0;i<allProcessPoints.size();i++){
			ProcessPoint processPoint = allProcessPoints.get(i);
			pptMap.put(processPoint.getId(), processPoint);
		}
		
		boolean flag = false;
		for(LotControlRule rule :rules ){
			ProcessPoint tempPpt = pptMap.get(rule.getId().getProcessPointId());
			
			if(tempPpt != null && tempPpt.getDivisionId().trim().equalsIgnoreCase(selectedProcessPoint.getDivisionId().trim()) && tempPpt.getPlantName().trim().equalsIgnoreCase(selectedProcessPoint.getPlantName().trim())){
				flag= true;
				break;
			}
		}
		
		return flag;
	}
}
