package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.client.teamleader.transfer.LotControlRuleDisplayPanel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.FilteredLabeledComboBox;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.oif.property.ReplicateScheduleProperty;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.SortedArrayList;

import net.miginfocom.swing.MigLayout;

public class SchedReplicationUpdateMbpnPanel extends TabbedPanel implements
		 ActionListener {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger();

	private String siteName;
	
	private List<String> prodSpecCode = null;
	List<PreProductionLot> upcomingPreProductionLots = null;
	protected String defaultProductType;
	protected SortedArrayList<String> productTypes;
	protected Map<String, String> productTypeMap;
	
	protected ProductSpecData productSpecData = null;
	private Map<String, ProductSpecData> productSpecCache;
	
	protected HashMap<String, SortedArrayList<ProcessPoint>> processPoints;
	private SortedArrayList<String> procPointTags;
	
	private JPanel sourcePanel;
	private JPanel targetPanel;
	private JPanel buttonPanel;
	private CommonTlPropertyBean commonTlPropertyBean;
	private ComponentProperty selectedComponentProperty = null;
	ReplicateScheduleProperty propertyBean = null;
	private FilteredLabeledComboBox sourceSpecCodeComboBox;
	private LabeledComboBox targetSpecCodeComboBox;
	private LabeledComboBox targetProdLotComboBox;
	
	protected LotControlRuleDisplayPanel lcSrcPanel;
	protected LotControlRuleDisplayPanel lcDstPanel;

	private JButton updateButton;
	private JButton resetButton;
	
	private AtomicInteger actionCounter = new AtomicInteger(0);

	@Override
	public void onTabSelected() {
		if (isInitialized) return;
		if (StringUtils.isEmpty(this.siteName = PropertyService.getSiteName())) {
			MessageDialog.showError(this, "SITE_NAME property is not defined for System_Info component.");
			return;
		}
		this.prodSpecCode = ServiceFactory.getDao(MbpnDao.class).findAllProdSpecCode();
		this.productSpecCache = new HashMap<String, ProductSpecData>();
		this.initComponents();
		this.addActionListeners();
		this.isInitialized = true;
	}
	
	protected void initComponents() {
		this.setLayout(new MigLayout());
		this.add(this.getSourcePanel(), "span1, center, wrap");
		this.add(this.getTargetPanel(), "span2, center, wrap");
		this.add(this.getButtonPanel(), "span3, center, wrap");
	}
	
	public SchedReplicationUpdateMbpnPanel() {
		super("MBPN Live Update", KeyEvent.VK_L);
		AnnotationProcessor.process(this);
	}

	public SchedReplicationUpdateMbpnPanel(TabbedMainWindow mainWindow) {
		super("MBPN Live Update", KeyEvent.VK_L, mainWindow);
		loadProperty(PropertyService.getPropertyBean(CommonTlPropertyBean.class, getApplicationId()));
		AnnotationProcessor.process(this);
	}
	protected void  loadProperty(CommonTlPropertyBean bean) {
		this.commonTlPropertyBean =  bean; 
	}
	private JPanel getSourcePanel() {
		if (this.sourcePanel == null) {
			this.sourcePanel = new JPanel(new MigLayout("align 50% 50%,inset 0 0 0 0"));
			sourcePanel.setPreferredSize(new java.awt.Dimension(640, 70));
			sourcePanel.setMinimumSize(new java.awt.Dimension(640, 70));
			sourcePanel.setMaximumSize(new java.awt.Dimension(640, 70));
			this.sourcePanel.add(this.getSourceSpecCodeComboBox());
			this.sourcePanel.setBorder(new TitledBorder("Source"));
		}
		return this.sourcePanel;
	}
	
	private JPanel getTargetPanel() {
		if (this.targetPanel == null) {
			this.targetPanel = new JPanel(new MigLayout("align 50% 50%,inset 0 0 0 0"));
			targetPanel.setPreferredSize(new java.awt.Dimension(640, 70));
			targetPanel.setMinimumSize(new java.awt.Dimension(640, 70));
			targetPanel.setMaximumSize(new java.awt.Dimension(640, 70));
			this.targetPanel.add(this.getTargetSpecCodeComboBox());
			this.targetPanel.add(this.getTargetProdLotComboBox());
			this.targetPanel.setBorder(new TitledBorder("Target"));
		}
		return this.targetPanel;
	}
	private JPanel getButtonPanel() {
		if (this.buttonPanel == null) {
			this.buttonPanel = new JPanel(new MigLayout("align 50% 50%,inset 0 0 0 0"));
			buttonPanel.setPreferredSize(new java.awt.Dimension(640, 70));
			buttonPanel.setMinimumSize(new java.awt.Dimension(640, 70));
			buttonPanel.setMaximumSize(new java.awt.Dimension(640, 70));
			
			this.buttonPanel.add(this.getUpdateButton());
			this.buttonPanel.add(this.getResetButton());
		}
		return this.buttonPanel;
	}

	public JButton getUpdateButton() {
		if(updateButton == null){
			updateButton = new JButton("Update");
			updateButton.setName("Update");
			updateButton.setEnabled(false);
		}
		return updateButton;
	}
	public JButton getResetButton() {
		if(resetButton == null){
			resetButton = new JButton("Reset");
			resetButton.setName("Reset");
		}
		return resetButton;
	}
	
	private FilteredLabeledComboBox getSourceSpecCodeComboBox() {
		if (sourceSpecCodeComboBox == null) {
			sourceSpecCodeComboBox = new FilteredLabeledComboBox("Spec Code");
			sourceSpecCodeComboBox.setPreferredWidth(700);
			sourceSpecCodeComboBox.setMaxWidth(700);
			sourceSpecCodeComboBox.getComponent().setName("JProcessPointCombobox");
			sourceSpecCodeComboBox.getComponent().setPreferredSize(new Dimension(350,20));
            ComboBoxModel<String> model = new ComboBoxModel<String>(this.prodSpecCode);
            sourceSpecCodeComboBox.setModel(model);
            sourceSpecCodeComboBox.setSelectedIndex(-1);
            
        }
        return sourceSpecCodeComboBox;
	}

	private LabeledComboBox getTargetSpecCodeComboBox() {
		if (this.targetSpecCodeComboBox == null) {
			this.targetSpecCodeComboBox = this.createComboBox("Spec Code", "TargetSpecCodeComboBox");
			targetSpecCodeComboBox.setPreferredWidth(300);
			targetSpecCodeComboBox.setMaxWidth(300);
		}
		return this.targetSpecCodeComboBox;
	}

	private LabeledComboBox getTargetProdLotComboBox() {
		if (this.targetProdLotComboBox == null) {
			this.targetProdLotComboBox = this.createComboBox("Prod Lot", "TargetProdLotComboBox");
			targetProdLotComboBox.setPreferredWidth(300);
			targetProdLotComboBox.setMaxWidth(300);
		}
		return this.targetProdLotComboBox;
	}

	
	protected LabeledComboBox createComboBox(String label, String name) {
		Dimension dimension = new Dimension(100, 20);
		LabeledComboBox comboBox = new LabeledComboBox(label);
		comboBox.getComponent().setName(name);
		comboBox.getComponent().setBackground(Color.WHITE);
		comboBox.getComponent().setMinimumSize(new Dimension (50,20));
		comboBox.getComponent().setPreferredSize(dimension);
		return comboBox;
	}
	
	private void addActionListeners() {
		this.getSourceSpecCodeComboBox().getComponent().addActionListener(this);
		this.getTargetSpecCodeComboBox().getComponent().addActionListener(this);
		this.getTargetProdLotComboBox().getComponent().addActionListener(this);
		this.getUpdateButton().addActionListener(this);
		this.getResetButton().addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {	
		this.getMainWindow().clearMessage();
		if(e.getSource().equals(this.getSourceSpecCodeComboBox().getComponent())) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() { 
					sourceSpecCodeChanged(); 
					enableUpdateButton();
				}
			});
		} else if(e.getSource().equals(this.getTargetSpecCodeComboBox().getComponent())) {
			this.targetSpecCodeChanged();
			this.enableUpdateButton();
		} else if(e.getSource().equals(this.getTargetProdLotComboBox().getComponent())) {
			this.enableUpdateButton();
		} else if(e.getSource().equals(this.getUpdateButton())) {
			this.updateScheduleReplication();
		} else if(e.getSource().equals(this.getResetButton())) {
			this.reset();
		}
	} 

	/**
	 * 
	 */
	public void updateScheduleReplication() {
		String selectedSourceSpecCode = (String) getSourceSpecCodeComboBox().getComponent().getSelectedItem();
		String selectedTargetSpecCode = (String) getTargetSpecCodeComboBox().getComponent().getSelectedItem();
		String selectedTargetProdLot = null;
		try {
			if(null != getTargetProdLotComboBox().getComponent().getSelectedItem()) {
				PreProductionLot preProductionLot = (PreProductionLot)getTargetProdLotComboBox().getComponent().getSelectedItem();
				selectedTargetProdLot = preProductionLot.getProductionLot();
			}
		} catch(Exception e) {
			selectedTargetProdLot = null;
		}
		PreProductionLot selectedPreProductionLot = getDao(PreProductionLotDao.class).findByKey(selectedTargetProdLot);
		if(null != selectedSourceSpecCode && selectedSourceSpecCode.trim().length() > 0
				&& null != selectedTargetSpecCode && selectedTargetSpecCode.trim().length() > 0
				&& null != selectedTargetProdLot && selectedTargetProdLot.trim().length() > 0) {
			try {
				
				logger.info("Selected Source Spec Code: " + selectedSourceSpecCode);
				logger.info("Selected Target Spec Code: " + selectedTargetSpecCode);
				logger.info("Selected Production Lot: " + selectedTargetProdLot);
				
				String allLots = "";
				Mbpn targetMbpn = getDao(MbpnDao.class).findByKey(selectedTargetSpecCode);
				for(PreProductionLot upcomingPreProductionLot : this.upcomingPreProductionLots) {
					
					if(upcomingPreProductionLot.getSequence() >= selectedPreProductionLot.getSequence()) {
						String preProdLot = upcomingPreProductionLot.getProductionLot().trim();
						
						logger.info("Updating Lot: " + preProdLot);
					
						String newProductIdString = reCreateProductId(selectedTargetSpecCode, upcomingPreProductionLot, this.propertyBean);
						String newProductIdSubString = newProductIdString.trim().substring(0, newProductIdString.indexOf("production"));
						logger.info("Created New Product Id String: " + newProductIdString + ". Substring: " + newProductIdSubString);
						
						Map<String, String> oldAndNewProductIdMapping = new HashMap<String, String>();
						List<MbpnProduct> mbpnProductList = getDao(MbpnProductDao.class).findAllByOrderNo(preProdLot);
						getDao(MbpnProductDao.class).deleteProductionLot(preProdLot);
						for(MbpnProduct mbpnProduct: mbpnProductList ) {
							String oldProductId = mbpnProduct.getProductId();
							String newProductId = oldProductId.replace(oldProductId.substring(0, newProductIdSubString.length()), newProductIdSubString);
							oldAndNewProductIdMapping.put(oldProductId, newProductId);
							mbpnProduct.setProductId(newProductId);
							mbpnProduct.setCurrentProductSpecCode(selectedTargetSpecCode);
							mbpnProduct.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
							logger.info("MbpnProduct (MBPN_PRODUCT_TBX) Production Lot:" + mbpnProduct.getProductionLot() + ". Product Id: " + mbpnProduct.getProductId());
							getDao(MbpnProductDao.class).save(mbpnProduct);
						}
						
						List<ProductStampingSequence> productStampingSequenceList = getDao(ProductStampingSequenceDao.class).findAllByProductionLot(preProdLot);
						getDao(ProductStampingSequenceDao.class).deleteProductionLot(preProdLot);
						String startProductId = "";
						for(ProductStampingSequence productStampingSequence : productStampingSequenceList) {
							if(productStampingSequence.getStampingSequenceNumber() == 1) {
								startProductId = productStampingSequence.getProductId();
							}
							productStampingSequence.setProductId(oldAndNewProductIdMapping.get(productStampingSequence.getProductId()));
							productStampingSequence.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
							logger.info("ProductStampingSequence (216) Production Lot:" + productStampingSequence.getProductionLot() + ". Product Id:" + productStampingSequence.getProductId());
							getDao(ProductStampingSequenceDao.class).save(productStampingSequence);
						}
						
						upcomingPreProductionLot.setStartProductId(oldAndNewProductIdMapping.get(startProductId));
						upcomingPreProductionLot.setProductSpecCode(selectedTargetSpecCode);
						upcomingPreProductionLot.setMbpn(targetMbpn.getMbpn());
						upcomingPreProductionLot.setHesColor(targetMbpn.getHesColor());
						upcomingPreProductionLot.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
						logger.info("PreProductionLot(212) Product Id: " + upcomingPreProductionLot.getStartProductId());
						getDao(PreProductionLotDao.class).update(upcomingPreProductionLot);
						
						allLots = allLots + upcomingPreProductionLot.getProductionLot() + "   Lot Size:" + upcomingPreProductionLot.getLotSize() + "  Start Product Id: " + upcomingPreProductionLot.getStartProductId() + "\n";
						
					}
				}
				JOptionPane.showMessageDialog(this, "Total [" + this.upcomingPreProductionLots.size() + "] Lots has been updated: \n" + allLots );
				logger.info("Schedule Replication Update Completed...");
				logger.info("Total [" + upcomingPreProductionLots.size() + "] Lots has been updated: \n" + allLots );
			
			} catch(Exception e) {
				logger.error(e, "Error while updating Schedule Replication: " + e.getMessage() + " stack trace:" + e.getStackTrace());
				JOptionPane.showMessageDialog(this, "Exception occured. Please contact IT support" + e.getMessage());
			}
			
		}
		
	}
	
	private String reCreateProductId(String targetSpecCode, PreProductionLot targetPreProductionLot, ReplicateScheduleProperty componentProperty) {
		Mbpn targetMbpn = getDao(MbpnDao.class).findByKey(targetSpecCode);
		String mbpnPartEnumStr = componentProperty.getMbpnPartEnum();
		
		Map<String, String> subAssembleIdRuleMap = propertyBean.getSubAssembleIdRule(String.class);
		Map.Entry<String,String> entry1 = subAssembleIdRuleMap.entrySet().iterator().next();
    	String subAssembleRuleIdStr = entry1.getValue();
		
		Map<String, String> mbpnPartPrefixMap = propertyBean.getMbpnPartPrefix(String.class);
		Map.Entry<String,String> entry2 = mbpnPartPrefixMap.entrySet().iterator().next();
    	String mbpnPartPrefix = entry2.getValue();
		
		String partname = getMbpnPartName(targetSpecCode, mbpnPartEnumStr, mbpnPartPrefix);
		if (StringUtils.isEmpty(partname)) return "";
		String hesColor = targetPreProductionLot.deriveHesColor();

		String startProductIdPrefix = "";
		boolean matchFound= false;
		String[] subAssembleItRule = subAssembleRuleIdStr.split(",");
		for(int i = 0;i <subAssembleItRule.length; i++) {
			matchFound= false;
			if (subAssembleItRule[i].equals("partName")){	startProductIdPrefix = startProductIdPrefix+partname;matchFound= true;}
			if (subAssembleItRule[i].equals("LineNum1")){	startProductIdPrefix = startProductIdPrefix+targetPreProductionLot.getLineNo().substring(1, 2);matchFound= true;}
			if (subAssembleItRule[i].equals("LineNum2")){	startProductIdPrefix = startProductIdPrefix+targetPreProductionLot.getLineNo();matchFound= true;}
			if (subAssembleItRule[i].contains("mbpnMain")){   startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,targetSpecCode.substring(0, 5),subAssembleItRule[i]);matchFound= true;}
			if (subAssembleItRule[i].contains("mbpnClass")){   
				startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,targetSpecCode.substring(5, 8),subAssembleItRule[i]);
				matchFound= true; 
				}         
			if (subAssembleItRule[i].equals("mbpnPrototype1")){   startProductIdPrefix = startProductIdPrefix+targetSpecCode.substring(8, 9);matchFound= true; }        
			if (subAssembleItRule[i].contains("mbpnType")){  startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,targetSpecCode.substring(9, 13),subAssembleItRule[i]);matchFound= true; }             
			if (subAssembleItRule[i].contains("mbpnSupplementary")){   startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,targetSpecCode.substring(13, 15),subAssembleItRule[i]);matchFound= true; }        
			if (subAssembleItRule[i].contains("mbpnTarget")){   startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,targetSpecCode.substring(15, 17),subAssembleItRule[i]);matchFound= true;   }     
			if (subAssembleItRule[i].contains("mbpnHesColor")){   startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,hesColor,subAssembleItRule[i]);matchFound= true;  }
			if (subAssembleItRule[i].contains("mbpnMaskId")){   startProductIdPrefix = startProductIdPrefix+targetMbpn.getMaskId();matchFound= true; }
			if (!matchFound){startProductIdPrefix = startProductIdPrefix+subAssembleItRule[i];}
		}
		if(StringUtils.isEmpty(startProductIdPrefix)){
			String subAssembleIdRule =  subAssembleIdRuleMap.get(partname);
			if(!StringUtils.isEmpty(subAssembleIdRule)) startProductIdPrefix = subAssembleIdRule;
		}
		return startProductIdPrefix;
	}
	
	private String getMbpnSubstring(String startProductIdPrefix,
			String targetSpecCode, String ruleName) {
		if (StringUtils.isEmpty(targetSpecCode))
			return "";

		String[] ruleTokens = ruleName.split(":");
		if (ruleTokens.length == 2) {
			int pos = Integer.parseInt(ruleTokens[0].substring(ruleTokens[0]
					.length() - 1));
			int len = Integer.parseInt(ruleTokens[1]);
			if (StringUtils.isEmpty(targetSpecCode))

				return StringUtils.leftPad("", len, " "); 
			startProductIdPrefix = startProductIdPrefix
					+ targetSpecCode.substring(pos, len);
		} else {
			startProductIdPrefix = startProductIdPrefix + targetSpecCode;
		}
		return startProductIdPrefix;

	}

	private String getMbpnPartName(String targetSpecCode, String mbpnPartEnumStr, String mbpnMainNoLst) {
		String mbpnMainNo = targetSpecCode.substring(0, 5);
		String[] mbpnPartEnum = mbpnPartEnumStr.split(",");

		if (null == mbpnPartEnum || mbpnPartEnum.length == 0) return "";
		for (int i = 0;i<mbpnPartEnum.length;i++) {
			if (mbpnMainNoLst.indexOf(mbpnMainNo) != -1 ) return  mbpnPartEnum[i];
		}
		return "";
	}

	
	public void enableUpdateButton() {
		String selectedSourceSpecCode = (String) getSourceSpecCodeComboBox().getComponent().getSelectedItem();
		String selectedTargetSpecCode = (String) getTargetSpecCodeComboBox().getComponent().getSelectedItem();
		String selectedTargetProdLot = null;
		try {
			if(null != getTargetProdLotComboBox().getComponent().getSelectedItem()) {

				PreProductionLot preProductionLot = (PreProductionLot)getTargetProdLotComboBox().getComponent().getSelectedItem();
				selectedTargetProdLot = preProductionLot.getProductionLot();
				
			}
		} catch(Exception e) {
			selectedTargetProdLot = null;
		}
		
		if(null != selectedSourceSpecCode && selectedSourceSpecCode.trim().length() > 0
				&& null != selectedTargetSpecCode && selectedTargetSpecCode.trim().length() > 0
				&& null != selectedTargetProdLot && selectedTargetProdLot.trim().length() > 0) {
			
			logger.info("Selected Source Spec Code : " + selectedSourceSpecCode);
			logger.info("Selected Target Spec Code : " + selectedTargetSpecCode);
			logger.info("Selected Production Lot : " + selectedTargetProdLot);
			this.getUpdateButton().setEnabled(true);
			
		} else {
			this.getUpdateButton().setEnabled(false);
		}
	}
	
	
	
	
	public void sourceSpecCodeChanged() {
		String selectedSourceSpecCode = (String) getSourceSpecCodeComboBox().getComponent().getSelectedItem();
		Logger.getLogger().info(selectedSourceSpecCode + " is selected as Source Spec Code");
		this.targetSpecCodeComboBox.getComponent().setSelectedIndex(-1);
		this.targetProdLotComboBox.getComponent().setSelectedIndex(-1);
		
		if(selectedSourceSpecCode != null && selectedSourceSpecCode.length() >=5 ) {
			String planCode = "";
			String processLocation = "";
			this.upcomingPreProductionLots = getDao(PreProductionLotDao.class).findAllUpcomingLotsByProductSpecCode(selectedSourceSpecCode);
			if(upcomingPreProductionLots != null && upcomingPreProductionLots.size() > 0) {
				planCode = upcomingPreProductionLots.get(0).getPlanCode();
				processLocation = upcomingPreProductionLots.get(0).getProcessLocation();
			}

			List<String> prefixList = getPrefixList(planCode.trim(), processLocation.trim());
			List<String> distinctProdSpecList = ServiceFactory.getDao(PreProductionLotDao.class).findDistinctProdSpecCodeBySpecCodeAndStatus(prefixList, 2);

			List<PreProductionLot> productionLotList = new ArrayList<PreProductionLot>();
			productionLotList = ServiceFactory.getDao(PreProductionLotDao.class).findAllPreProductionLotsByProductSpecCode(selectedSourceSpecCode.trim());
			Logger.getLogger().info(productionLotList.toString());
			ComboBoxModel<PreProductionLot> model2 = new ComboBoxModel<PreProductionLot>(productionLotList, "getProductionLot");
			this.targetProdLotComboBox.getComponent().setModel(model2);
			this.targetProdLotComboBox.getComponent().setSelectedIndex(-1);
			this.targetProdLotComboBox.getComponent().setRenderer(model2);

			ComboBoxModel<String> model1 = new ComboBoxModel<String>(distinctProdSpecList);
			this.targetSpecCodeComboBox.getComponent().setModel(model1);
			this.targetSpecCodeComboBox.getComponent().setSelectedIndex(-1);
			this.targetSpecCodeComboBox.getComponent().setRenderer(model1);
		}
	}
	
	private List<String> getPrefixList(String specCode, String processLocation) {
	        List<String> componentIds = PropertyService.getPropertyList(getApplicationId(), "REPLICATATION_TASK_NAMES");
	        for(String componentId : componentIds) {
	            this.propertyBean = PropertyService.getPropertyBean(ReplicateScheduleProperty.class, componentId);
	            if(propertyBean.getTargetPlanCode().containsKey(processLocation) && propertyBean.getTargetPlanCode().containsValue(specCode)) {
	            	Map<String, String> mbpnPartPrefixMap = propertyBean.getMbpnPartPrefix(String.class);
	            	Map.Entry<String,String> entry = mbpnPartPrefixMap.entrySet().iterator().next();
	            	String value = entry.getValue();
	            	return Arrays.asList(value.split("\\s*,\\s*"));
	            }
	        }
	        return null;
	 }
	
	public void targetSpecCodeChanged() {
		this.targetProdLotComboBox.getComponent().setSelectedIndex(-1);
		
		String selectedTargetSpecCode = (String) getTargetSpecCodeComboBox().getComponent().getSelectedItem();
		Logger.getLogger().info(selectedTargetSpecCode + " is selected as Target Spec Code");
		
	}
	
	private void reset() {
		List<PreProductionLot> productionLotList = new ArrayList<PreProductionLot>();
		ComboBoxModel<PreProductionLot> model2 = new ComboBoxModel<PreProductionLot>(productionLotList, "getProductionLot");
		
		List<String> distinctProdSpecList = new ArrayList<String>();
		ComboBoxModel<String> model1 = new ComboBoxModel<String>(distinctProdSpecList);
		
		ComboBoxModel<String> model = new ComboBoxModel<String>(this.prodSpecCode);
		this.sourceSpecCodeComboBox.setModel(model);
        this.sourceSpecCodeComboBox.setSelectedIndex(-1);
        
		this.targetProdLotComboBox.getComponent().setModel(model2);
		this.targetProdLotComboBox.getComponent().setSelectedIndex(-1);
		this.targetProdLotComboBox.getComponent().setRenderer(model2);
		
		this.targetSpecCodeComboBox.getComponent().setModel(model1);
		this.targetSpecCodeComboBox.getComponent().setSelectedIndex(-1);
		this.targetSpecCodeComboBox.getComponent().setRenderer(model1);
	}
    
    protected void handleException (Exception e) {
		super.handleException(e);
	}
    
	protected Map<String, ProductSpecData> getProductSpecCache() {
		return this.productSpecCache;
	}
	
	
	protected AtomicInteger getActionCounter() {
		return this.actionCounter;
	}

	public void setActionCounter(int ctr) {
		getActionCounter().set(ctr);
	}
	
	@Override
    public void setCursor(Cursor cursor) {
		if (Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR).equals(cursor)) {
			this.setWaitCursor();
		} else if (Cursor.getDefaultCursor().equals(cursor)) {
			this.setDefaultCursor();
		} else {
			super.setCursor(cursor);
		}
    }
	
	public void setWaitCursor() {
		getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		getActionCounter().incrementAndGet();
	}

	public void setDefaultCursor() {
		getActionCounter().decrementAndGet();
		if (getActionCounter().get() < 1) {
			getRootPane().setCursor(Cursor.getDefaultCursor());
		}
	}
	
	protected String getProductType(String id) {
		return this.productTypeMap.containsKey(id) ? this.productTypeMap.get(id) : this.defaultProductType;
	}
}