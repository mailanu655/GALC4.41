/**
 * 
 */
package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.MbpnSelectionPanel;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.ProductSpecSelectionBase;
import com.honda.galc.client.ui.component.ProductSpecSelectionPanel;
import com.honda.galc.client.ui.component.ProductTypeSelectionPanel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.event.ProductSpecSelectionEvent;
import com.honda.galc.client.ui.event.ProductTypeSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.RuleExclusionDao;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.RuleExclusion;
import com.honda.galc.entity.product.RuleExclusionId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * @author vf031824
 *
 */
public class ExternalPartExclusionPanel extends TabbedPanel implements ListSelectionListener, ActionListener, TableModelListener{

	private static final long serialVersionUID = 1L;

	private static final String ADD_PART_EXCLUSION = "Add Part to Exclusion List";
	private static final String DELETE_RULE = "Delete Exclusion Rule";

	private Dimension screenDimension;
	private int productTypePanelWidth = 1000;
	private int productTypePanelHeight = 50;
	private int midPanelHeight = 250;
	private String currentProductType;

	protected ProductTypeSelectionPanel productTypeSelectionPanel;
	protected ProductSpecSelectionBase productSpecSelectionPanel;
	protected PartNameSelectionPanel partNameSelectionPanel;
	protected TablePane rulePanel;

	private RuleExclusionTableModel ruleExclusionTableModel;

	public ExternalPartExclusionPanel() {
		super("EXTERNAL_PART_EXCLUSION_PANEL", KeyEvent.VK_P);
		screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		AnnotationProcessor.process(this);
	}

	public void actionPerformed(ActionEvent e) {
		Exception exception = null;
		try{
			JMenuItem menuItem = (JMenuItem)e.getSource();	
			if(menuItem.getName().equals(ADD_PART_EXCLUSION)) addExcludedPart();
			else if(menuItem.getName().equals(DELETE_RULE)) deleteRules();

		}catch(Exception ex) {
			exception = ex;
		}
		handleException(exception);
	}

	@Override
	public void onTabSelected() {
		if(isInitialized) {
			return;
		}
		initComponents();
		productSpecSelectionPanel.registerProductTypeSelectionPanel(productTypeSelectionPanel);
		addListeners();
		isInitialized = true;
	}

	protected void initComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Border border = BorderFactory.createEmptyBorder(4, 4, 4, 4);

		Box box1  = Box.createHorizontalBox();
		box1.setBorder(border);
		box1.add(createProductTypeSelectionPanel());
		add(box1);

		Box box2  = Box.createHorizontalBox();
		box2.setBorder(border);
		box2.add(createProductSpecSelectionPanel());
		box2.add(createPartSelectionPanel());
		add(box2);

		Box box3  = Box.createHorizontalBox();
		box3.setBorder(border);
		box3.add(createRulePanel());

		add(box3);
	}

	private ProductTypeSelectionPanel createProductTypeSelectionPanel() {
		String siteName = PropertyService.getSiteName();
		if(StringUtils.isEmpty(siteName)){
			MessageDialog.showError(this, "SITE_NAME is not defined in the System_Info property");	
		}
		productTypeSelectionPanel = new ProductTypeSelectionPanel(siteName);
		productTypeSelectionPanel.setSize(productTypePanelWidth, productTypePanelHeight);

		return productTypeSelectionPanel;
	}

	private ProductSpecSelectionBase createProductSpecSelectionPanel() {
		if(isMbpnProduct())
			productSpecSelectionPanel = new MbpnSelectionPanel();
		else
			productSpecSelectionPanel = new ProductSpecSelectionPanel();
		productSpecSelectionPanel.setSize(productTypeSelectionPanel.getWidth() / 2,midPanelHeight);
		Dimension dim = new Dimension(screenDimension.width, screenDimension.height /2);
		productSpecSelectionPanel.setPreferredSize(dim);
		productSpecSelectionPanel.setMaximumSize(dim);
		return productSpecSelectionPanel;
	}

	private PartNameSelectionPanel createPartSelectionPanel() {
		partNameSelectionPanel = new PartNameSelectionPanel(productTypeSelectionPanel.getWidth() / 4,midPanelHeight,
				new Dimension(screenDimension.width / 4, screenDimension.height /2));
		return partNameSelectionPanel;
	}

	private TablePane createRulePanel(){
		rulePanel = new TablePane("Excluded Part List",ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		rulePanel.getTable().setRowHeight(20);
		Dimension dim = new Dimension((int)(screenDimension.width), screenDimension.height /2);
		rulePanel.setPreferredSize(dim);
		rulePanel.setMaximumSize(dim); 
		return rulePanel;
	}

	@EventSubscriber(eventClass=ProductTypeSelectionEvent.class)
	public void processProductTypeSelectionPanelChanged(ProductTypeSelectionEvent event) {
		if(event.isEventFromSource(SelectionEvent.PRODUCT_TYPE_SELECTED, productTypeSelectionPanel)) {
			initPartNamePanel();

			if(productTypeSelectionPanel.selectedProductType() != null)
				updateCommonPartNameModel();
		}
	}

	@EventSubscriber(eventClass=ProductSpecSelectionEvent.class)
	public void productSpecSelectedPanelChanged(ProductSpecSelectionEvent event) {
		if(event.isEventFromSource(SelectionEvent.SELECTING, productSpecSelectionPanel) ||
				event.isEventFromSource(SelectionEvent.POPULATED, productSpecSelectionPanel) || 
				event.isEventFromSource(SelectionEvent.SELECTED, productSpecSelectionPanel)){ 
			showLotControlRuleResult();
		}   	
	}

	private void initPartNamePanel() {
		String productType = productTypeSelectionPanel.selectedProductType();
		if(productType != null)
			if(!productType.equalsIgnoreCase(currentProductType)) {
				currentProductType = productType;
				updateCommonPartNameModel();
			}
	}

	private void updateCommonPartNameModel() {
		if(partNameSelectionPanel == null) return;
		List<PartName> commonParts = new ArrayList<PartName>();
		String productType = productTypeSelectionPanel.selectedProductType();
		if(productType != null) commonParts = getCommonPartsByProductType(productType);

		partNameSelectionPanel.update(commonParts);
	}

	private void addExcludedPart() {
		List<String> productSpecCode = productSpecSelectionPanel.buildSelectedProductSpecCodes();
		for(String currentSpec : productSpecCode) {
			RuleExclusion rule = new RuleExclusion();
			RuleExclusionId id = new RuleExclusionId();
			id.setPartName(partNameSelectionPanel.getPartNameTableModel().getSelectedItem().getPartName());
			id.setProductSpecCode(ProductSpec.trimWildcard(currentSpec));
			rule.setId(id);

			if (ruleExclusionTableModel.hasExcludedPart(id.getPartName(), id.getProductSpecCode())) {
				MessageDialog.showError(this, "Part  \"" + id.getPartName()	+ "\" with  \"" + id.getProductSpecCode()	+ "\" Product Spec exists. ");
			}else {
				ServiceFactory.getDao(RuleExclusionDao.class).save(rule);
				showLotControlRuleResult();
			}
		}
	}

	private void deleteRules() {
		List<RuleExclusion> rules = ruleExclusionTableModel.getSelectedItems();
		if(rules.isEmpty()) return;
		if(!MessageDialog.confirm(this, "Are you sure that you want to delete selected excluded rules?"))
			return;
		getDao(RuleExclusionDao.class).removeAll(rules);

		showLotControlRuleResult();
	}

	private  void showLotControlRuleResult() {
		Exception exception = null;
		try {
			List<RuleExclusion> lotControlRules = retrieveSelectedLotControlRules(true);
			ruleExclusionTableModel = new RuleExclusionTableModel(rulePanel.getTable(), lotControlRules);
		}catch(Exception ex) {
			exception = ex;
		}
		handleException(exception);
	}

	private List<RuleExclusion> retrieveSelectedLotControlRules(boolean isFilterPartName) {

		List<String> specCodes = productSpecSelectionPanel.buildSelectedProductSpecCodes();
		List<RuleExclusion> ruleList = new ArrayList<RuleExclusion>();

		for(String productSpecCode : specCodes) {
			RuleExclusionId id = new RuleExclusionId();
			RuleExclusion ruleExclusion = new RuleExclusion();

			PartName partName = (partNameSelectionPanel == null ||partNameSelectionPanel.getPartNameTableModel() ==null)? null : 
				partNameSelectionPanel.getPartNameTableModel().getSelectedItem();

			if(partName != null && isFilterPartName){
				id.setPartName(partName.getPartName());
			}
			id.setProductSpecCode(productSpecCode);
			ruleExclusion.setId(id);
			ruleList = getDao(RuleExclusionDao.class).findAllById(id, productTypeSelectionPanel.selectedProductType());
		}
		return ruleList;
	}

	private List<PartName> getCommonPartsByProductType(String productType) {
		return ServiceFactory.getDao(PartNameDao.class).findAllByProductTypeAndExternalReq(productType);
	}

	private void addListeners() {
		MouseListener listener = createMouseListener();

		partNameSelectionPanel.getPartSelectionPanel().getTable().addMouseListener(listener);
		partNameSelectionPanel.getPartSelectionPanel().addListSelectionListener(this);

		rulePanel.getTable().addMouseListener(createExcludedRuleListener());
	}

	private MouseListener createMouseListener(){
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showCreateRulePopupMenu(e);
			}
		}); 
	}

	private MouseListener createExcludedRuleListener(){
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showExcludedRulePopupMenu(e);
			}
		});   
	}

	private void showExcludedRulePopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(DELETE_RULE, isPartNameAndSpecSelected()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	private void showCreateRulePopupMenu(MouseEvent e) {
		Logger.getLogger().info("PartSpecSelection Panel CreateRulePopupMenu enabled");
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(ADD_PART_EXCLUSION,areAllSelected()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	private boolean areAllSelected() {
		return productTypeSelectionPanel.isProductTypeSelected() && 
				productSpecSelectionPanel.isProductSpecSelected();
	}

	public void tableChanged(TableModelEvent arg0) {
	}

	private boolean isPartNameAndSpecSelected() {
		return (partNameSelectionPanel.isPartNameSelected() &&
				productSpecSelectionPanel.isProductSpecSelected());
	}

	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		Exception exception = null;
		try {
			if(e.getSource() ==(partNameSelectionPanel.getPartSelectionPanel().getTable().getSelectionModel())) {
				showLotControlRuleResult();
			}
		}catch(Exception ex) {
			exception = ex;
		}
		handleException(exception);
	}
}