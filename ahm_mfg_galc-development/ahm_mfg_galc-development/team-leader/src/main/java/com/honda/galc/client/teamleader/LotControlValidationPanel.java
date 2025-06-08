package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ListModel;
import com.honda.galc.client.ui.component.SplitInfoPanel;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.ProcessPointSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.utils.LotControlRuleValidator;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.LotControlPartUtil;

/**
 * 
 * <h3>LotControlValidationPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlValidationPanel description </p>
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
 * <TD>May 13, 2011</TD>
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
 * @since May 13, 2011
 */
 
public class LotControlValidationPanel extends TabbedPanel implements ListSelectionListener, KeyListener{
	private static final long serialVersionUID = 1L;
	
	private SplitInfoPanel splitPanel;
	private ProcessPointAndProductSpecSelectionPanel selectionPanel;
	private LotCtrRuleValidationDetailPanel ruleValidationPanel;
	private List<LotControlRule> lotControlRules;

	private List<LotControlRule> selectedRules;
	
	public LotControlValidationPanel(TabbedMainWindow mainWindow) {
		super("Lot Control Validation", KeyEvent.VK_V,mainWindow);
		AnnotationProcessor.process(this);
		
	}

	@Override
	public void onTabSelected() {
		try {
			if (isInitialized)	return;
			initComponents();
			addListeners();
			isInitialized = true;
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to start Lot Control Validation.");
			setErrorMessage("Exception to start lot control validation panel." + e.toString());
		}

	}

	private void addListeners() {
		 splitPanel.getSelectionList().getComponent().addListSelectionListener(this);
		 getRuleValidationPanel().getPartSnField().addKeyListener(this);
		
	}

	private void initComponents() {
		//setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());

		add(getSelectionPanel(), BorderLayout.NORTH);
		add(getSplitPanel(), BorderLayout.CENTER);

	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	 @EventSubscriber(eventClass=SelectionEvent.class)
	 public void processProductSpecChange(SelectionEvent event){
		 if(event.isEventFromSource(SelectionEvent.SELECTED, selectionPanel)){
			 processNewSelection();
		 }
	 }

	 @EventSubscriber(eventClass=ProcessPointSelectionEvent.class)
	 public void processProcessPointChange(ProcessPointSelectionEvent event){
		 if(selectionPanel != null && 
				 event.isEventFromSource(SelectionEvent.PROCESSPOINT_SELECTED, selectionPanel.getProcessPointSelectionPanel())){
				
			 processNewSelection();
		 }
	 }

	 private void processNewSelection() {
		 ProcessPoint processPoint = (ProcessPoint)selectionPanel.getProcessPointSelectionPanel().getProcessPointComboBox().getComponent().getSelectedItem();
		 String productSepcCode = (String)selectionPanel.getSpecComBoBox().getSelectedItem();

		 if(processPoint == null || StringUtils.isEmpty(productSepcCode) ||
				 "No Selection".equals(processPoint.getProcessPointId())) 
			 return;

		 getRuleValidationPanel().setProcessPoint(processPoint);
		 loadLotControlRules(processPoint, productSepcCode);
	 }

	 
	 private void loadLotControlRules(ProcessPoint processPoint,
			 String productSepcCode) {
		 
		 getRuleValidationPanel().resetLotControlRule();
		 LotControlRuleDao dao = ServiceFactory.getDao(LotControlRuleDao.class);
		 List<LotControlRule> rules = dao.findAllByProcessPoint(processPoint.getProcessPointId());
		 BaseProductSpec spec = findProductSpec(productSepcCode);
		 List<LotControlRule> lotControlRules = spec == null ? new ArrayList<LotControlRule>() : LotControlPartUtil.getLotControlRuleByProductSpec(spec, rules);
		 ListModel<LotControlRule> listModel = new LotControlRuleListModel(lotControlRules, "getPartNameString");
		 splitPanel.getSelectionList().getComponent().setModel(listModel);
		 splitPanel.getSelectionList().getComponent().setCellRenderer(listModel);
		 
		 if(rules.size() == 0)//Ok to have no rule defined.
			 setMessage("No Lot Control Rule was configured for:" + productSepcCode);
		 else 
			 setMessage(null);
	 }

	 private BaseProductSpec findProductSpec(String spec) {
		 String prodType = selectionPanel.getSelectedProductType();
		 return (BaseProductSpec) ProductTypeUtil.getProductSpecDao(prodType).findByProductSpecCode(spec, prodType);
	 }
	 
	 public String getProductType(){
		 CommonTlPropertyBean bean = PropertyService.getPropertyBean(CommonTlPropertyBean.class);
		 return bean.getProductType();
	 }

	//-------------- getters ---------------------
	public ProcessPointAndProductSpecSelectionPanel getSelectionPanel() {
		if(selectionPanel == null){
			selectionPanel = new ProcessPointAndProductSpecSelectionPanel(getProductType());
		}
		return selectionPanel;
	}

	public List<LotControlRule> getLotControlRules() {
		return lotControlRules;
	}

	public SplitInfoPanel getSplitPanel() {
		if(splitPanel == null){
			splitPanel = new SplitInfoPanel();
			
			splitPanel.setDetailsPanel(getRuleValidationPanel());
			splitPanel.getSelectionList().getLabel().setText("Part Names:");
			splitPanel.initialize();
			
		}
		return splitPanel;
	}

	private LotCtrRuleValidationDetailPanel getRuleValidationPanel() {
		if(ruleValidationPanel == null){
			ruleValidationPanel = new LotCtrRuleValidationDetailPanel(this);
		}
		return ruleValidationPanel;
	}

	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource().equals(getSplitPanel().getSelectionList().getComponent()))
			partNameSelected();
		
	}

	private void partNameSelected() {
		Object[] rules =getSplitPanel().getSelectionList().getComponent().getSelectedValues();
		selectedRules = new  ArrayList<LotControlRule>();
		for(int i=0; i < rules.length; i++)
			selectedRules.add((LotControlRule)rules[i]);
			
		getRuleValidationPanel().setLotControlRules(selectedRules);
	}
	
	public void keyReleased(KeyEvent e) {
		if(KeyEvent.VK_ENTER == e.getKeyCode()) {
			String partSn = getRuleValidationPanel().getPartSnField().getText();
			validatePartSn(partSn, getRuleValidationPanel().getPartSnField());
		}
	}

	private void validatePartSn(String partSn, UpperCaseFieldBean bean) {
		if(selectedRules == null || selectedRules.size() == 0){
			setErrorMessage("Please select a part.");
			return;
		}
		
		if(StringUtils.isEmpty(partSn)) {
			setErrorMessage("Received part serial number is null!");
			renderNg(bean);
			return;
		} else {
			 String prodType = selectionPanel.getSelectedProductType();
			 String productSpecCode = (String)selectionPanel.getSpecComBoBox().getSelectedItem();
			 BaseProduct baseProduct = null;
			 if(prodType.equals(ProductType.MBPN.name())) {				 
				 MbpnProduct mbpnProduct = (MbpnProduct)ProductTypeUtil.createProduct(prodType, "productId");
				 mbpnProduct.setCurrentProductSpecCode(productSpecCode);			 
				 baseProduct = (BaseProduct)mbpnProduct;
			 }
			 else {
				 Product product = (Product)ProductTypeUtil.createProduct(prodType, "productId");	
				 product.setProductSpecCode(productSpecCode);
				 baseProduct = (BaseProduct)product;
			 }	 
			 for(PartSpec spec : selectedRules.get(0).getParts()){
					if(CommonPartUtility.verification(partSn, spec.getPartSerialNumberMask(), PropertyService.getPartMaskWildcardFormat(), baseProduct))
					{
						renderOk(bean);
						getRuleValidationPanel().setPartSepc(spec);
						return;
					}
			 }	 		
			setErrorMessage("Part serial number:" + partSn + " verification failed.");
			renderNg(bean);
			return;
			
		}
		
	}

	private void renderOk(UpperCaseFieldBean bean) {
		bean.setColor(Color.green);
		bean.setBackground(Color.green);
		
	}

	private void renderNg(UpperCaseFieldBean bean) {
		bean.setColor(Color.red);
		bean.setBackground(Color.red);
		bean.setSelectionStart(0);
		bean.setSelectionEnd(bean.getText().length());
		bean.setEnabled(true);
		bean.requestFocus();
		
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent e) {
		getRuleValidationPanel().getPartSnField().setBackground(Color.white);
		
	}
	
	public void setMessage(String msg){
		super.setMessage(msg);
	}
	
	public boolean isHeadedClient(){
		return getRuleValidationPanel().isHeadedClient();
	}
	
	class LotControlRuleListModel extends ListModel<LotControlRule>{
		LotControlRuleValidator validator = new LotControlRuleValidator();
		public LotControlRuleListModel(List<LotControlRule> objects, String methodName) {
			super(objects, methodName);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			// TODO Auto-generated method stub
			Component c = super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			
			if(!validator.validate((LotControlRule)value, isHeadedClient()))
				c.setBackground(Color.red);

			return c;
		}
		
	}

	public void setError(String msg) {
		super.setErrorMessage(msg);
		
	}
	

}
