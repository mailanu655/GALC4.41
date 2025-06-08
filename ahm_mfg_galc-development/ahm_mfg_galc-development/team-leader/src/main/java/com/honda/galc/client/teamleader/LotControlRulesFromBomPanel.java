package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.ProcessPointSelectiontPanel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.event.ProcessPointSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.client.ui.event.UpdateEvent;
import com.honda.galc.client.ui.tablemodel.LotControlRuleTableModel;
import com.honda.galc.client.teamleader.model.LotControlPartSpecRule;
import com.honda.galc.client.teamleader.model.LotControlPartSpecRuleTableModel;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.oif.BomDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.PartByProductSpecCodeDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.fif.Bom;
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

public class LotControlRulesFromBomPanel extends TabbedPanel
		implements ListSelectionListener, ActionListener, TableModelListener {

	private static final long serialVersionUID = 1L;
	private final static String COPY_PART_SPEC="Copy Part Spec";
	private Map<LotControlPartSpecRule, List<String>> partSpecProductSpecMap ;
	protected ProcessPointSelectiontPanel processPointPanel;
	protected PartNameSelectionPanel partNameSelectionPanel;
	protected TablePane partSpecSelectionPanel;
	protected TablePane rulePanel;
	protected TablePane productSpecPanel;
	protected TablePane measurementListPanel;
	protected JButton refreshButton, createRulesForSelectedButton, createRulesForAllButton;
	private LotControlPartSpecRuleTableModel lotControlRuleFromBomTableModel;
	private LotControlRuleTableModel lotControlRuleTableModel;
	private MeasurementSpecListTableModel measurementListTableModel;
	private ProductSpecTableModel productSpecTableModel;
	
	private CommonTlPropertyBean propertyBean;

	private int processPointPanelWidth = 1000;
	private int processPointPanelHeight = 50;

	private int refreshButtonWidth = 80;

	private int midPanelHeight = 250;
	private Dimension screenDimension;

	private boolean isHeadLess = false;
	private String currentProductType;
	private boolean errorDetected = false;
	private String plantCode = "";

	public LotControlRulesFromBomPanel() {
		super("Bom Lot Control Rule", KeyEvent.VK_L);
		screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		AnnotationProcessor.process(this);
	}

	public LotControlRulesFromBomPanel(TabbedMainWindow mainWindow) {
		super("Bom Lot Control Rule", KeyEvent.VK_L, mainWindow);
		screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		AnnotationProcessor.process(this);
	}

	protected void initComponents() {
		propertyBean = PropertyService.getPropertyBean(CommonTlPropertyBean.class, "Default_CommonProperties");
		plantCode = propertyBean.getPlantCode();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Border border = BorderFactory.createEmptyBorder(4, 4, 4, 4);

		Box box1 = Box.createHorizontalBox();
		box1.setBorder(border);
		box1.add(createProcessPointSelectionPanel());
		box1.add(createRefreshButton());
		add(box1);

		Box box22 = Box.createHorizontalBox();
		box22.add(createMeasurementListPanel());
		box22.add(createProductSpecPanel());

		Box box21 = Box.createVerticalBox();
		box21.add(createPartSpecSelectionPanel());
		box21.add(box22);

		Box box2 = Box.createHorizontalBox();
		box2.setBorder(border);
		box2.add(createPartSelectionPanel());
		box2.add(box21);
		add(box2);

		Box box3 = Box.createVerticalBox();
		box3.setBorder(border);
		box3.add(createRulePanel());
		add(box3);

		Box box4 = Box.createHorizontalBox();
		box4.setBorder(border);
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
		buttonPanel.add(createRulesForSelectedButton());
		buttonPanel.add(createRulesForAllButton());
		box4.add(buttonPanel);

		add(box4);

	}

	private ProcessPointSelectiontPanel createProcessPointSelectionPanel() {
		String siteName = PropertyService.getSiteName();
		if (StringUtils.isEmpty(siteName)) {
			MessageDialog.showError(this, "SITE_NAME is not defined in the System_Info property");
		}

		processPointPanel = new ProcessPointSelectiontPanel(siteName);
		processPointPanel.setSize(processPointPanelWidth, processPointPanelHeight);

		return processPointPanel;
	}

	private JButton createRefreshButton() {
		refreshButton = new JButton("Refresh");
		refreshButton.setToolTipText("Refresh Lot Control Rule on server cache for selected headless process point");
		refreshButton.setSize(refreshButtonWidth, processPointPanelHeight);
		refreshButton.setEnabled(false);
		return refreshButton;
	}
	
	private PartNameSelectionPanel createPartSelectionPanel() {
		partNameSelectionPanel = new PartNameSelectionPanel(processPointPanel.getWidth() / 4, midPanelHeight,
				new Dimension(screenDimension.width / 4, screenDimension.height / 2));

		return partNameSelectionPanel;
	}

	private TablePane createPartSpecSelectionPanel() {
		partSpecSelectionPanel = new TablePane("Part Spec");
		return partSpecSelectionPanel;
	}

	private TablePane createMeasurementListPanel() {
		measurementListPanel = new TablePane("Measurement Spec");
		return measurementListPanel;
	}

	private TablePane createRulePanel() {
		rulePanel = new TablePane("Lot Control Rule", ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		return rulePanel;
	}

	private TablePane createProductSpecPanel() {
		productSpecPanel = new TablePane("Product Spec");
		return productSpecPanel;
	}

	private JButton createRulesForSelectedButton() {
		createRulesForSelectedButton = new JButton("Create Rules For Selected");
		createRulesForSelectedButton.setToolTipText("Create Lot Control Rules for selected");
		createRulesForSelectedButton.setSize(refreshButtonWidth, processPointPanelHeight);
		createRulesForSelectedButton.setEnabled(false);
		return createRulesForSelectedButton;
	}

	private JButton createRulesForAllButton() {
		createRulesForAllButton = new JButton("Create Rules For All");
		createRulesForAllButton.setToolTipText("Create Lot Control Rule for All");
		createRulesForAllButton.setSize(refreshButtonWidth, processPointPanelHeight);
		createRulesForAllButton.setEnabled(false);
		return createRulesForAllButton;
	}

	@Override
	public void onTabSelected() {
		if (isInitialized) {
			return;
		}
		initComponents();
		addListeners();
		isInitialized = true;
	}

	private void addListeners() {

		partNameSelectionPanel.getPartSelectionPanel().addListSelectionListener(this);
		processPointPanel.getProcessPointComboBox().getComponent().addActionListener(this);
		refreshButton.addActionListener(this);
		createRulesForSelectedButton.addActionListener(this);
		createRulesForAllButton.addActionListener(this);
		partSpecSelectionPanel.addListSelectionListener(this);
		partSpecSelectionPanel.getTable().addMouseListener(createPartSpecMouseListener());
		partSpecSelectionPanel.addMouseListener(createPartSpecMouseListener());
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(processPointPanel.getProcessPointComboBox().getComponent()))
			processPointSelected();
		if (e.getSource().equals(refreshButton))
			processRefreshButton();
		if (e.getSource().equals(createRulesForSelectedButton))
			createLotControlRulesForSelected();
		if (e.getSource().equals(createRulesForAllButton))
			createLotControlRulesForAll();
		if(e.getSource() instanceof JMenuItem){
			JMenuItem menuItem = (JMenuItem)e.getSource();
		
			if(menuItem.getName().startsWith(COPY_PART_SPEC)) copyPartSpec();
						
		}
	}

	private void copyPartSpec() {
		List<LotControlPartSpecRule> lotControlPartSpecRules = lotControlRuleFromBomTableModel.getItems();
		List<LotControlPartSpecRule> updatedLotControlPartSpecRules = new ArrayList<LotControlPartSpecRule>();
		LotControlPartSpecRule configuredPartSpecRule = lotControlRuleFromBomTableModel.getSelectedItem();
		if(configuredPartSpecRule != null){
			for (LotControlPartSpecRule rule : lotControlPartSpecRules) {
				if(rule.isSelect()){
					rule.setSequenceNumber(configuredPartSpecRule.getSequenceNumber());
					rule.setDeviceId(configuredPartSpecRule.getDeviceId());
					rule.setExpectedInstallTime(configuredPartSpecRule.getExpectedInstallTime());
					rule.setInstructionCode(configuredPartSpecRule.getInstructionCode());
					rule.setSerialNumberScanFlag(configuredPartSpecRule.getSerialNumberScanFlag());
					rule.setStrategy(configuredPartSpecRule.getStrategy());
					rule.setSubId(configuredPartSpecRule.getSubId());
					rule.setSerialNumberUniqueFlag(configuredPartSpecRule.getSerialNumberUniqueFlag());
					rule.setVerificationFlag(configuredPartSpecRule.getVerificationFlag());
					rule.setPartConfirmFlag(configuredPartSpecRule.isPartConfirmFlag());
				}
				
				updatedLotControlPartSpecRules.add(rule);
			}
		}
		lotControlRuleFromBomTableModel.refresh(updatedLotControlPartSpecRules);
	}

	private void processRefreshButton() {

		if (LoginDialog.login() == LoginStatus.OK) {

			ServiceFactory.getService(DataCollectionService.class)
					.refreshLotControlRuleCache(processPointPanel.getCurrentProcessPoint().getProcessPointId());

			refreshRuleCacheOnMultiServer(processPointPanel.getCurrentProcessPoint().getProcessPointId());

			getLogger().info("Lot Control Rules for Process Point:",
					processPointPanel.getCurrentProcessPoint().getProcessPointId(), " were refreshed by user:",
					ClientMain.getInstance().getAccessControlManager().getUserName());
		}

	}

	private boolean refreshRuleCacheOnMultiServer(String processPointId) {

		Map<String, Exception> messages = ServerInfoUtil.refreshLotControlRule(processPointId);
		for (Map.Entry<String, Exception> entry : messages.entrySet()) {
			if (entry.getKey().equals("*"))
				getLogger().error(entry.getValue(),
						"Failed to refresh lot control cache at Process point : " + processPointId);
			else if (entry.getValue() == null)
				getLogger().info("Refresh lot control rule cache of Process point Id " + processPointId + " on server "
						+ entry.getKey() + " Successfully");
			else
				getLogger().error(entry.getValue(), "Failed to refresh lot control cache at Process point : "
						+ processPointId + " on server " + entry.getKey());
		}
		return true;
	}

	private List<LotControlRule> createLotControlRulesForPartSpec(LotControlPartSpecRule partSpecRule, List<String> specCodes) {

		List<LotControlRule> rules = new ArrayList<LotControlRule>();
		LotControlRuleDao lotControlRuleDao = getDao(LotControlRuleDao.class);
		PartSpec partSpec = partSpecRule.getPartSpec();
		deleteLotControlRulesForPart(partSpec);
			
		for (String productSpecCode : specCodes) {

			String specCode = ProductSpec.trimWildcard(productSpecCode);
			
			LotControlRuleId id = new LotControlRuleId();

			id.setPartName(partNameSelectionPanel.getPartNameTableModel().getSelectedItem().getPartName().trim());
			id.setProcessPointId(processPointPanel.getCurrentProcessPointId().trim());
			id.setProductSpecCode(specCode.trim());
			
			
				if (!isMbpnProduct()) {
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
				LotControlRule rule = lotControlRuleDao.findByKey(id);
				
				if(rule == null) {
					rule = new LotControlRule();
					rule.setId(id);
					rule.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
				}
					rule.setSequenceNumber(partSpecRule.getSequenceNumber());
					rule.setDeviceId(partSpecRule.getDeviceId());
					rule.setExpectedInstallTime(partSpecRule.getExpectedInstallTime());
					rule.setInstructionCode(partSpecRule.getInstructionCode());
					rule.setSerialNumberScanFlag(partSpecRule.getSerialNumberScanFlag());
					rule.setStrategy(partSpecRule.getStrategy());
					rule.setSubId(partSpecRule.getSubId());
					rule.setUnique(partSpecRule.getSerialNumberUniqueFlag());
					rule.setVerify(partSpecRule.getVerificationFlag());
					rule.setPartConfirm(partSpecRule.isPartConfirmFlag());
					rule.setUpdateUser(getUserName());
					rule.setQicsDefect(partSpecRule.isQiDefectFlag());

				
					lotControlRuleDao.save(rule);
					logUserAction(SAVED, rule);
					AuditLoggerUtil.logAuditInfo(null, rule, "save", getScreenName(), getUserName().toUpperCase(),"GALC","GALC_Maintenance");

					rules.add(rule);
			
			createPart(rule, partSpec);
			
		}
		
		return rules;
	}

	private void createLotControlRulesForAll() {
		String processPointId = processPointPanel.getCurrentProcessPointId();
		if(processPointId != null){
			String msg = "Are you sure you want to delete All rules assigned to processpoint and create new rules ";
			boolean confirm = MessageDialog.confirm(this, msg);
			if(confirm){
				List<LotControlPartSpecRule> lotControlRules = lotControlRuleFromBomTableModel.getItems();
				List<LotControlPartSpecRule> selectedLotControlRules = new ArrayList<LotControlPartSpecRule>();
				for(LotControlPartSpecRule lotControlRule:lotControlRules){
					lotControlRule.setSelect(true);
					selectedLotControlRules.add(lotControlRule);
				}
				lotControlRuleFromBomTableModel.refresh(selectedLotControlRules);
				createRulesInSeperateThread();

			}
		}
	}

	private void createLotControlRulesForSelected() {
		String processPointId = processPointPanel.getCurrentProcessPointId();
		if(processPointId != null) {
			String msg = "Are you sure you want to delete lot control rules for selected part spec and create new rules ";
			boolean confirm = MessageDialog.confirm(this, msg);
			if (confirm) {
				createRulesInSeperateThread();
			}
		}
	}

	private void deleteLotControlRulesForPart(PartSpec partSpec) {
        List<LotControlRule> lotControlRules = loadLotControlRules(partSpec.getId().getPartName());
        for (LotControlRule rule : lotControlRules) {
               List<PartByProductSpecCode> partByProductSpecs = rule.getPartByProductSpecs();
               for (PartByProductSpecCode part : partByProductSpecs) {
                     if (part.getId().getPartId().equals(partSpec.getId().getPartId())) {                                  
                            //Is this part Id being used at another process point??
                            List<LotControlRule> tmpRules = getDao(LotControlRuleDao.class).findAllByPartId(partSpec.getId().getPartId(), partSpec.getId().getPartName());
                            boolean deletePartSpec = true;
                            for (LotControlRule tmpRule : tmpRules) {
                                   if (!tmpRule.getId().getProcessPointId().toString().equals(processPointPanel.getCurrentProcessPoint().getId().toString())  
                                                 && tmpRule.getProductSpecCode().toString().equals(part.getId().getProductSpecCode().toString())) {
                                          //Assigned to another process point do not delete part Id 
                                          deletePartSpec = false;
                                          break;
                                   }                                        
                            }
                            
                            //Check if assigned to another process point, if not delete part id
                            if (deletePartSpec) {
                                 getDao(PartByProductSpecCodeDao.class).remove(part);
                                 logUserAction(REMOVED, part);
                                 AuditLoggerUtil.logAuditInfo(part, null, "delete", getScreenName(), getUserName().toUpperCase(),"GALC","GALC_Maintenance");
                            }
                     }
               }
               LotControlRule tempRule =  getDao(LotControlRuleDao.class).findByKey(rule.getId());
              if(tempRule.getPartByProductSpecs().isEmpty()) {
            	  getDao(LotControlRuleDao.class).remove(rule);
            	  logUserAction(REMOVED, rule);
            	  AuditLoggerUtil.logAuditInfo(rule, null, "delete", getScreenName(), getUserName().toUpperCase(),"GALC","GALC_Maintenance");
              }
               
        }
 }


	private PartByProductSpecCode createPart(LotControlRule rule, PartSpec partSpec) {
		
		getLogger().info("Creating PartByProductSpecCode for LotControlRule :"+rule.toString());
		PartByProductSpecCode partByProductSpecCode = new PartByProductSpecCode();
		PartByProductSpecCodeId id = new PartByProductSpecCodeId();
		id.setProductSpecCode(rule.getId().getProductSpecCode());
		if (!isMbpnProduct())id.setYmtoc(rule.getId());
		id.setPartId(partSpec.getId().getPartId());
		id.setPartName(partSpec.getId().getPartName());
		partByProductSpecCode.setId(id);
		getLogger().info("save/merge :"+partByProductSpecCode.toString());
		try{
			PartByProductSpecCode tempPartByproductSpecCode =  getDao(PartByProductSpecCodeDao.class).findByKey(id);
			if(tempPartByproductSpecCode == null){
				partByProductSpecCode.setUpdateUser(getUserName());
				getDao(PartByProductSpecCodeDao.class).save(partByProductSpecCode);
				logUserAction(SAVED, partByProductSpecCode);
				AuditLoggerUtil.logAuditInfo(null, partByProductSpecCode, "save", getScreenName(), getUserName().toUpperCase(),"GALC","GALC_Maintenance");
			}
		}
		catch(Exception e){
			getLogger().error(e.getMessage());
			e.printStackTrace();
		}

		return partByProductSpecCode;
	}

	private void processPointSelected() {
		ProcessPoint processPoint = processPointPanel.getCurrentProcessPoint();
		if (processPoint == null ){
			createRulesForSelectedButton.setEnabled(false);
			createRulesForAllButton.setEnabled(false);
			return;
		}else{
			initPartSelectionPanel();
			if (!processPoint.getProcessPointId().equals(ProcessPointSelectiontPanel.DUMMY_PROCESS_POINT_ID)){
				enableButtons();
				PartName partName = partNameSelectionPanel.getPartNameTableModel().getSelectedItem();
				if(partName != null){
					List<LotControlRule> lotControlRules =  loadLotControlRules(partName.getPartName());
					lotControlRuleTableModel = new LotControlRuleTableModel(rulePanel.getTable(), lotControlRules, false);
				}
			}
		}
	}

	private void enableButtons() {
		if (lotControlRuleFromBomTableModel != null) {
			boolean enableSelectedButton = false;
			List<LotControlPartSpecRule> lotControlRules = lotControlRuleFromBomTableModel.getItems();
			for(LotControlPartSpecRule lotControlRule:lotControlRules){
				if(lotControlRule.isSelect()){
					enableSelectedButton = true;
					break;
				}
			}
			if (enableSelectedButton && processPointPanel.getCurrentProcessPointId()!= null) {
				createRulesForSelectedButton.setEnabled(true);
			}else{
				createRulesForSelectedButton.setEnabled(false);
			}
		}
		PartName partName = partNameSelectionPanel.getPartNameTableModel().getSelectedItem();
		if(partName != null){
			if(lotControlRuleFromBomTableModel!= null && lotControlRuleFromBomTableModel.getItems().size() > 0 && processPointPanel.getCurrentProcessPointId()!= null) {
				createRulesForAllButton.setEnabled(true);
			}else{
				createRulesForAllButton.setEnabled(false);
			}
		}else{
			createRulesForAllButton.setEnabled(false);
		}
	}

	private void initPartSelectionPanel() {
		String productType = processPointPanel.selectedProductType();
		if (productType != null) {
			if (!productType.equalsIgnoreCase(currentProductType)) {
				currentProductType = productType;
				updatePartSelectionModel();
			}
		}
	}

	@EventSubscriber(eventClass = UpdateEvent.class)
	public void processUpdateEvent(UpdateEvent event) {
		if (event.isUpdatePartName()){
			updatePartSelectionModel();
			reset();
		}
	}

	private void updatePartSelectionModel() {
		if (partNameSelectionPanel == null)
			return;

		List<PartName> partNames = new ArrayList<PartName>();
		String productType = processPointPanel.selectedProductType();
		if (productType != null)
			partNames = loadPartNames(productType);

		PartName selectedPartName = partNameSelectionPanel.getPartNameTableModel() != null ?partNameSelectionPanel.getPartNameTableModel().getSelectedItem():null;
		partNameSelectionPanel.update(partNames);
		
		if(selectedPartName != null) {
			partNameSelectionPanel.getPartNameTableModel().selectItem(selectedPartName);
			loadLotControlPartSpecRules(selectedPartName) ;
		}
	}

	private List<PartName> loadPartNames(String productType) {
		return getDao(PartNameDao.class).findAllByProductType(productType);
	}

	private List<LotControlRule> loadLotControlRules(String partName) {
		return getDao(LotControlRuleDao.class).findAllByProcessPointIdAndPartName(processPointPanel.getCurrentProcessPoint().getId(), partName);

	}

	@EventSubscriber(eventClass = ProcessPointSelectionEvent.class)
	public void processPointSelectedPanelChanged(ProcessPointSelectionEvent event) {
		if (event.isEventFromSource(SelectionEvent.DEPARTMENT_SELECTED, processPointPanel)) {
			initPartSelectionPanel();
		} else if (event.isEventFromSource(SelectionEvent.PRODUCT_TYPE_SELECTED, processPointPanel)) {
			initPartSelectionPanel();
		} else if (event.isEventFromSource(SelectionEvent.PROCESSPOINT_SELECTED, processPointPanel)) {
			if (processPointPanel.getCurrentProcessPoint() == null)
			{
				return;
			}
			isHeadLess = isHeadLess(processPointPanel.getCurrentProcessPoint());
			initPartSelectionPanel();
			enableRefreshButton(isHeadLess);
			if(processPointPanel.getCurrentProcessPointId() == null){
				createRulesForSelectedButton.setEnabled(false);
				createRulesForAllButton.setEnabled(false);
			}else{
				enableButtons();
				PartName partName = partNameSelectionPanel.getPartNameTableModel().getSelectedItem();
				if(partName != null){
					List<LotControlRule> lotControlRules =  loadLotControlRules(partName.getPartName());
					lotControlRuleTableModel = new LotControlRuleTableModel(rulePanel.getTable(), lotControlRules, false);
				}
			}
		}
	}

	private void enableRefreshButton(boolean headLess) {
		// for now, Only enabled to fresh headless process point
		refreshButton.setEnabled(headLess);
	}

	private boolean isHeadLess(ProcessPoint processPoint) {
		if (processPoint == null)
			return false;
		return (getDao(TerminalDao.class).findFirstByProcessPointId(processPoint.getProcessPointId()) == null);
	}

	public void valueChanged(ListSelectionEvent e) {

		if (e.getValueIsAdjusting())
			return;
		Exception exception = null;
		try {
			if (e.getSource() == (partNameSelectionPanel.getPartSelectionPanel().getTable().getSelectionModel())) {
				// part name selected
				PartName partName = partNameSelectionPanel.getPartNameTableModel().getSelectedItem();
				loadLotControlRulePartSpecTable(partName);
				List<LotControlRule> lotControlRules = partName == null || processPointPanel.getCurrentProcessPointId() == null? new ArrayList<LotControlRule>():loadLotControlRules(partName.getPartName());
				lotControlRuleTableModel = new LotControlRuleTableModel(rulePanel.getTable(), lotControlRules, false);
				productSpecTableModel = new ProductSpecTableModel(productSpecPanel.getTable(), new ArrayList<String>());

			} else if (e.getSource() == (partSpecSelectionPanel.getTable().getSelectionModel())) {
				LotControlPartSpecRule lotControlRule = lotControlRuleFromBomTableModel.getSelectedItem();
				if(lotControlRule != null){
					PartSpec partSpec =lotControlRule.getPartSpec();
					measurementListTableModel = new MeasurementSpecListTableModel(measurementListPanel.getTable(),
							partSpec == null ? null : partSpec.getMeasurementSpecs());
					measurementListTableModel.pack();
	
					loadProductSpecTable(lotControlRule);
				}
			}
		} catch (Exception ex) {
			exception = ex;
		}
		enableButtons();
		handleException(exception);
	}

	public void tableChanged(TableModelEvent e) {
		if(e.getSource() instanceof LotControlPartSpecRuleTableModel){
			LotControlPartSpecRule lotControlRule = ((LotControlPartSpecRuleTableModel)e.getSource()).getSelectedItem();
			if(lotControlRule.isSelect() && (productSpecTableModel != null && productSpecTableModel.getItems().size() > 0) && processPointPanel.getCurrentProcessPointId()!= null){
				createRulesForSelectedButton.setEnabled(true);
			}else{
				createRulesForSelectedButton.setEnabled(false);
			}
		}
	}

	private void loadLotControlRulePartSpecTable(PartName partName) {
		if(partName != null){
			loadLotControlPartSpecRules(partName);
		}
	}

	private void loadLotControlPartSpecRules(final PartName partName) {
		final List<LotControlPartSpecRule> partSpecRules = new ArrayList<LotControlPartSpecRule>();
		PartSpecDao partSpecDao = getDao(PartSpecDao.class);
		List<PartSpec> partSpecList = partSpecDao.findAllByPartName(partName.getPartName());
		
		for (PartSpec partSpec : partSpecList) {
			if (StringUtils.isNotEmpty(partSpec.getPartNumber()) && partSpec.getId().getPartId().contains("~")) 
			{
				LotControlPartSpecRule partSpecRule = new LotControlPartSpecRule();
				partSpecRule.setPartSpec(partSpec);
				partSpecRule.setSequenceNumber(1);
				partSpecRules.add(partSpecRule);
			}
		}
		if(!partSpecRules.isEmpty()){
			getMainWindow().setMessage(" Please wait while application loads partspecs .....");
			final JDialog dialog = getWaitDialog("Please wait while application loads partSpecs....", "Please Wait...");
			Thread t = new Thread() {
				public void run() {
					try {
						loadProductSpecCodesForPartSpecs(partSpecRules);
						dialog.dispose();
						getMainWindow().clearMessage();
				
					} catch (Exception e) {
						dialog.dispose();
						getMainWindow().setErrorMessage(" unexpected Error occurred ");
					}
				}
			};
			t.start();
			dialog.setVisible(true);
		}
		lotControlRuleFromBomTableModel = new LotControlPartSpecRuleTableModel(partSpecSelectionPanel.getTable(),
				partSpecRules, true);
		if (measurementListTableModel != null && lotControlRuleFromBomTableModel.getSelectedItem() == null)
			measurementListTableModel.refresh(new ArrayList<MeasurementSpec>());
		partSpecSelectionPanel.addListSelectionListener(this);
		partSpecSelectionPanel.getTable().getModel().addTableModelListener(this);
	}

	private void loadProductSpecTable(LotControlPartSpecRule partSpecRule) {
		List<String> productSpecCodes = new ArrayList<String>();
		if (partSpecProductSpecMap != null) {
			productSpecCodes = partSpecProductSpecMap.get(partSpecRule);
		} else {
			productSpecCodes = getProductSpecCodes(partSpecRule.getPartSpec());
		}

		productSpecTableModel.refresh(productSpecCodes);
	}

	private List<String> getProductSpecCodes(PartSpec partSpec) {
		BomDao bomDao = getDao(BomDao.class);
		ProductSpecData productSpecData = new ProductSpecData(processPointPanel.selectedProductType());

		List<String> productSpecCodes = new ArrayList<String>();

		String partNumber =partSpec.getPartNumber();
		String partSpecModelYear = partSpec.getId().getPartId().substring(0, 1);
		String partSpecColorCode = partSpec.getPartColorCode();
		if (StringUtils.isNotEmpty(partNumber)) {
			String partNo = partNumber.substring(0, 11);
			List<Bom> bomList = bomDao.findAllValidModels(plantCode, partNo);
			for (Bom bom : bomList) {
				String modelYear = bom.getId().getMtcModel().substring(0, 1);
				String partColorCode = bom.getId().getPartColorCode();
				if(modelYear.equalsIgnoreCase(partSpecModelYear)
						&& (StringUtils.isBlank(partSpecColorCode) || partSpecColorCode.equalsIgnoreCase(partColorCode))){
					String modelCode = bom.getId().getMtcModel().substring(1, 4);
					String modelType = bom.getId().getMtcType();
					String modelOption = bom.getId().getMtcOption();
					String extColorCode = bom.getId().getMtcColor();
					String intColorCode = bom.getId().getIntColorCode();

					List<String> specCodes = productSpecData.getProductSpecData(modelYear, modelCode,
							new Object[] { modelType }, new Object[] { modelOption }, new Object[] { extColorCode },
							new Object[] { intColorCode });
					for(String spCode : specCodes) {
						spCode = spCode.replace("%", "").trim();
						if (!productSpecCodes.contains(spCode)) {
							productSpecCodes.add(spCode);
						}
					}
				}
			}
		}
		return productSpecCodes;
	}

	@Override
	protected void handleException(Exception e) {
		if (e != null) {
			getLogger().error(e, "unexpected exception occurs: " + e.getMessage() + " stack trace:" + getStackTrace(e));
			this.setErrorMessage(e.getMessage());
		} else if (!errorDetected) {
			clearErrorMessage();
		}
	}

	private JDialog getWaitDialog(String message, String title) {
		final JDialog dialog = new JDialog(this.getMainWindow(), title);
		dialog.setSize(400, 200);
		JProgressBar progressBar = new JProgressBar();
		progressBar.setForeground(Color.GREEN);
		progressBar.setIndeterminate(true);
		JPanel panel = new JPanel(new GridLayout(3, 1, 20, 10));
		panel.add(progressBar, BorderLayout.CENTER);
		JLabel messageLabel1 = new JLabel(message);
		messageLabel1.setFont(UiFactory.getInfoSmall().getLabelFont());
		panel.add(messageLabel1, BorderLayout.PAGE_START);
		dialog.add(panel);
		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		return dialog;
	}

	private void loadProductSpecCodesForPartSpecs(
		List<LotControlPartSpecRule> partSpecRules) {
		this.partSpecProductSpecMap = new HashMap<LotControlPartSpecRule, List<String>>();
		for (LotControlPartSpecRule rule : partSpecRules) {
			PartSpec partSpec = rule.getPartSpec();
			List<String> specCodes = getProductSpecCodes(partSpec);
			if (specCodes.isEmpty()) {
				this.partSpecProductSpecMap.put(rule, new ArrayList<String>());
			} else {
				this.partSpecProductSpecMap.put(rule, specCodes);
			}
		}

	}
	
	private List<String> getUniqueSpecCodes(List<String> specCodes,List<String> allSpecCodes){
		List<String> uniqueSpecCodes = new ArrayList<String>();
		for(String specCode:specCodes){
			if(!allSpecCodes.contains(specCode)){
				uniqueSpecCodes.add(specCode);
			}
		}
		return uniqueSpecCodes;
	}
	
	private void reset(){
		lotControlRuleFromBomTableModel.refresh(new ArrayList<LotControlPartSpecRule>());
		lotControlRuleTableModel.refresh(new ArrayList<LotControlRule>());
		measurementListTableModel.refresh(new ArrayList<MeasurementSpec>());
		productSpecTableModel.refresh(new ArrayList<String>());
		enableButtons();
	}
	private void createRulesInSeperateThread(){
		getMainWindow().setMessage(" Please wait while the rules are created .....");
		final JDialog dialog = getWaitDialog("Please wait for the rules to get created....", "Please Wait...");
	
		Thread t = new Thread() {
			public void run() {
				try {
					List<LotControlPartSpecRule> lotControlPartSpecRules = lotControlRuleFromBomTableModel.getItems();
					List<LotControlRule> rules = new ArrayList<LotControlRule>();
					List<String> allSpecCodes = new ArrayList<String>();
					for (LotControlPartSpecRule rule : lotControlPartSpecRules) {
						if(rule.isSelect()){
							List<String> specCodes = partSpecProductSpecMap != null ? partSpecProductSpecMap.get(rule)
									: new ArrayList<String>();
							List<String> uniqueSpecCodes = getUniqueSpecCodes(specCodes, allSpecCodes);
							if(uniqueSpecCodes.isEmpty()){
								getLogger().info(" Specodes already exist cannot create LotControlRules for partSpec - "+rule.toString());
							}
							rules.addAll(createLotControlRulesForPartSpec(rule,uniqueSpecCodes));
						}
					}
					lotControlRuleTableModel.refresh(rules);
					dialog.dispose();
					getMainWindow().clearMessage();
					getMainWindow().setMessage(" Rules Successfully Created");

				} catch (Exception e) {
					dialog.dispose();
					getMainWindow().setErrorMessage(" unexpected Error occurred ");
					handleException(e);
				}
			}
		};
		t.start();
		dialog.setVisible(true);

	}

	private MouseListener createPartSpecMouseListener(){
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showPartSpecPopupMenu(e);
			}
		
		 }); 
	}
	
	private void showPartSpecPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(COPY_PART_SPEC,isPartSpecSelected()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
	
	private boolean isPartSpecSelected() {
		return partSpecSelectionPanel.getTable().getSelectedRowCount() == 1;
	}
}
