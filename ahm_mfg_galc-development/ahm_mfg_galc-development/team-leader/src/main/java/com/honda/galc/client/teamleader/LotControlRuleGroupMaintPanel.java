package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.datacollection.view.info.LabeledTablePanel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.MbpnSelectionPanel;
import com.honda.galc.client.ui.component.ProcessPointSelectiontPanel;
import com.honda.galc.client.ui.component.ProductSpecSelectionBase;
import com.honda.galc.client.ui.component.ProductSpecSelectionPanel;
import com.honda.galc.client.ui.event.ProcessPointSelectionEvent;
import com.honda.galc.client.ui.event.ProductSpecSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>LotControlRuleGroupMaintPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlRuleGroupMaintPanel description </p>
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
 * <TD>Nov 11, 2016</TD>
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
 * @since Nov 11, 2016
 */

public class LotControlRuleGroupMaintPanel extends TabbedPanel 
implements ActionListener, ItemListener
{
	private static final long serialVersionUID = 1L;
	protected ProductSpecSelectionBase productSpecSelectionPanel;
	protected ProcessPointSelectiontPanel processPointPanel;
	protected LabeledComboBox groupPanel;
	private Dimension screenDimension;
	private JPanel partMgrPanel;
	private LabeledTablePanel groupPartTablePane;
	private LabeledTablePanel availablePartTablePane;
	private JPanel groupMgrButtonPanel;
	private JButton addToGroupButton;
	private JButton removeFromGroupButton;
	private String currentProductType;
	private String currentGroupId;
	
	
	public LotControlRuleGroupMaintPanel(TabbedMainWindow mainWindow) {
		super("Lot Control Rule Group", KeyEvent.VK_G,mainWindow);
		screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		AnnotationProcessor.process(this);
		
	}

	@Override
	public void onTabSelected() {
		try {
			if (isInitialized)	return;
			initComponents();
			productSpecSelectionPanel.registerProcessPointSelectionPanel(processPointPanel);
			addListeners();
			isInitialized = true;
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to start Lot Control Validation.");
			setErrorMessage("Exception to start lot control validation panel." + e.toString());
		}

		
	}

	private void addListeners() {
		getAddToGroupButton().addActionListener(this);
		getRemoveFromGroupButton().addActionListener(this);
		getGroupIdPanel().getComponent().addItemListener(this);
	
		
	}

	private void initComponents() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Border border = BorderFactory.createEtchedBorder();

		Box box1 = Box.createVerticalBox();
		box1.setBorder(border);
		box1.add(createProductSelectionPanel());
		box1.add(getGroupIdPanel());
		add(box1);
		
		Box box2 = Box.createVerticalBox();
		box2.setBorder(border);
		box2.add(createProductSpecSelectionPanel());
		box2.add(createPartMaintPanel());
		add(box2);
		
	}

	private LabeledComboBox getGroupIdPanel() {
		if(groupPanel == null){
			groupPanel = new LabeledComboBox("Group Id:");
			Dimension dimension = new Dimension(screenDimension.width/3,45);
			groupPanel.setPreferredSize(dimension);
			groupPanel.getComponent().setPreferredSize(new Dimension(screenDimension.width/3,30));
			groupPanel.setMaximumSize(dimension);
			groupPanel.setFont(Fonts.DIALOG_BOLD_14);
			
			groupPanel.getComponent().setEditable(true);
			
		}
		return groupPanel;
	}

	private ProcessPointSelectiontPanel createProductSelectionPanel() {

		String siteName = PropertyService.getSiteName();
		if(StringUtils.isEmpty(siteName)){
			MessageDialog.showError(this, "SITE_NAME is not defined in the System_Info property");
		}
		
		processPointPanel= new ProcessPointSelectiontPanel(siteName);
		Dimension dimension = new Dimension(screenDimension.width,45);
		processPointPanel.setPreferredSize(dimension);
		processPointPanel.setMaximumSize(dimension);
		processPointPanel.getProcessPointComboBox().setVisible(false);
		processPointPanel.setBorder(BorderFactory.createEmptyBorder());
		
		return processPointPanel;
	
	}

	private ProductSpecSelectionBase createProductSpecSelectionPanel() {
		if(isMbpnProduct())
			productSpecSelectionPanel = new MbpnSelectionPanel();
		else
			productSpecSelectionPanel = new ProductSpecSelectionPanel();
		
		Dimension dim = new Dimension(screenDimension.width, screenDimension.height /3);
		productSpecSelectionPanel.setPreferredSize(dim);
		productSpecSelectionPanel.setMaximumSize(dim);
		return productSpecSelectionPanel;
		
	}

	private Component createPartMaintPanel() {
		if(partMgrPanel == null){
			partMgrPanel = new JPanel(new FlowLayout());
			partMgrPanel.add(getAvailablePartsPanel());
			partMgrPanel.add(getPartMgrButtonPanel());
			partMgrPanel.add( getGroupPartPanel());
		}
		return partMgrPanel;
	}

	private LabeledTablePanel getGroupPartPanel() {
		if(groupPartTablePane == null){
			groupPartTablePane = new LabeledTablePanel("Group Parts");
			Dimension dim = new Dimension(screenDimension.width/3, screenDimension.height/3);
			groupPartTablePane.setPreferredSize(dim);
			groupPartTablePane.setMaximumSize(dim);
			
			new LotControlRulePartGroupTableModel(groupPartTablePane.getTablePanel().getTable(),new ArrayList<LotControlRule>());
		}
		return groupPartTablePane;
	}

	private Component getPartMgrButtonPanel() {
		if(groupMgrButtonPanel == null){
			groupMgrButtonPanel = new JPanel(new GridBagLayout());
			groupMgrButtonPanel.setLayout(new GridLayout(3, 1, 10, 10));
			groupMgrButtonPanel.add(getAddToGroupButton());
			groupMgrButtonPanel.add(getRemoveFromGroupButton());

		}
		return groupMgrButtonPanel;
	}
	
	public JButton getAddToGroupButton() {
		if(addToGroupButton == null){
			addToGroupButton = new JButton(">>");
		}
		return addToGroupButton;
	}

	
	public JButton getRemoveFromGroupButton() {
		if(removeFromGroupButton == null){
			removeFromGroupButton = new JButton("<<");
		}
		return removeFromGroupButton;
	}

	private LabeledTablePanel getAvailablePartsPanel() {
		if(availablePartTablePane == null){
			availablePartTablePane = new LabeledTablePanel("Available Parts");
			Dimension dim = new Dimension(screenDimension.width/3, screenDimension.height/3);
			availablePartTablePane.setPreferredSize(dim);
			availablePartTablePane.setMaximumSize(dim);
			
			new LotControlRulePartGroupTableModel(availablePartTablePane.getTablePanel().getTable(),new ArrayList<LotControlRule>());
			
		}
		return availablePartTablePane;
	}

	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource().equals(getAddToGroupButton())){
			List<LotControlRule> selectedItems = ((LotControlRulePartGroupTableModel)availablePartTablePane.getTablePanel().getTable().getModel()).getSelectedItems();
			if(selectedItems != null && selectedItems.size() > 0){
				for(LotControlRule r : selectedItems) {
					r.setGroupId(currentGroupId);
					r.setUpdateUser(getUserName());
				}
				getDao(LotControlRuleDao.class).saveAll(selectedItems);
				logUserAction(SAVED, selectedItems);
				updatePartResults(true);
			}
			
		} else if(e.getSource().equals(getRemoveFromGroupButton())){
			List<LotControlRule> removeItems = ((LotControlRulePartGroupTableModel)groupPartTablePane.getTablePanel().getTable().getModel()).getSelectedItems();
			if(removeItems != null && removeItems.size() > 0) {
				for(LotControlRule r : removeItems) {
					r.setGroupId(null);
					r.setUpdateUser(getUserName());
				}
				getDao(LotControlRuleDao.class).saveAll(removeItems);
				logUserAction(SAVED, removeItems);
				updatePartResults(true);
			}
		}
		
	}
	
	@EventSubscriber(eventClass=ProcessPointSelectionEvent.class)
    public void processPointSelectedPanelChanged(ProcessPointSelectionEvent event) {
		
    	if(event.isEventFromSource(SelectionEvent.PRODUCT_TYPE_SELECTED, processPointPanel)) {
    		updateGroupIdPanel();
    	}
	}

	@EventSubscriber(eventClass=ProductSpecSelectionEvent.class)
	public void productSpecSelectedPanelChanged(ProductSpecSelectionEvent event) {
		if(event.isEventFromSource(SelectionEvent.SELECTING, productSpecSelectionPanel) ||
				event.isEventFromSource(SelectionEvent.POPULATED, productSpecSelectionPanel) || 
				event.isEventFromSource(SelectionEvent.SELECTED, productSpecSelectionPanel)){ 
			updatePartResults(false);
		} 
	}


	private void updateGroupIdPanel() {
		String productType = processPointPanel.selectedProductType();
		if(productType != null) {
			if(!productType.equalsIgnoreCase(currentProductType)) {
				currentProductType = productType;
				updateGroupIdModel();
				
			}
			
		}
	}

	private void updateGroupIdModel() {
		List<String> groupIdsByProductType = ServiceFactory.getDao(LotControlRuleDao.class).findAllGroupIdsByProductType(currentProductType);
		List<String> groupIdList = new ArrayList<String>();
		groupIdList.add("");
		for(String groupId : groupIdsByProductType){
			groupId = StringUtils.trimToEmpty(groupId);
			if(!groupIdList.contains(groupId)) groupIdList.add(groupId);
		}
		
		groupPanel.getComponent().setModel(new DefaultComboBoxModel(groupIdList.toArray(new String[]{})));
		groupPanel.getComponent().setSelectedIndex(0);
	}

	public void itemStateChanged(ItemEvent e) {
		boolean groupChanged = false;
		if(e.getStateChange() == ItemEvent.SELECTED){
			currentGroupId = StringUtils.trim(e.getItem().toString());
			groupChanged = true;
		}
		
		if(groupChanged) updatePartResults(false);
	}
	
	private void updatePartResults(boolean groupedItemsChanged) {
		List<String> specCodes = productSpecSelectionPanel.buildSelectedProductSpecCodes();
		if(StringUtils.isEmpty(currentGroupId) && specCodes.isEmpty()) return;
		List<LotControlRule> availableRuleList = new ArrayList<LotControlRule>();
		List<LotControlRule> groupedRuleList = new ArrayList<LotControlRule>();
		
		
		for(String spec : specCodes){
			String specCode = ProductSpec.trimWildcard(spec);
			if(!specCode.endsWith("%")) specCode = specCode + "%";
			List<LotControlRule> allRules = getDao(LotControlRuleDao.class).findAllByProducttyepAndSpecCode(currentProductType, specCode);
			for(LotControlRule r : allRules)
				if(currentGroupId.equals(r.getGroupId()))
					groupedRuleList.add(r);
				else
					availableRuleList.add(r);
					
		}
		
		if(groupedItemsChanged && isNewGroupId()) {
			updateGroupIdModel();
			groupPanel.getComponent().setSelectedItem(currentGroupId);
		}
		new LotControlRulePartGroupTableModel(availablePartTablePane.getTablePanel().getTable(),availableRuleList);
		new LotControlRulePartGroupTableModel(groupPartTablePane.getTablePanel().getTable(),groupedRuleList);
		
		
	}

	private boolean isNewGroupId() {
		int size = groupPanel.getComponent().getModel().getSize();
		boolean exists = false;
		for(int i = 0; i < size; i++){
			if(StringUtils.equals( currentGroupId, (String)groupPanel.getComponent().getModel().getElementAt(i)))
				exists = true;
		}
		return !exists;
	}

	
}
