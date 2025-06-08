/**
 * 
 */
package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.ProductTypeSelectionPanel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.client.ui.event.ProductTypeSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.product.ExtRequiredPartSpecDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.dto.ExtRequiredPartSpecDto;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.product.ExtRequiredPartSpec;
import com.honda.galc.entity.product.PartId;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.PartSpec;

import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * @author vf031824
 *
 */
public class ExternalRequiredPartPanel extends TabbedPanel implements  ListSelectionListener, TableModelListener, ActionListener{

	private static final long serialVersionUID = 1L;

	private static final String ADD_SPEC = "Add External Required Part Spec";
	private static final String DELETE_SPEC = "Delete External Required Part Spec";
	private String DB_ERROR_OCCURED = "A database error has occured in method: ";
	private static final String EXT_REQUIRED_PART_GROUP_NAME = "EXT_REQUIRED_PART_GROUP_NAME";
	private static final String BLANK = "";

	private boolean errorDetected = false;
	protected String defaultProductType;
	private String currentProductType;
	private int productTypePanelWidth = 1000;
	private int productTypePanelHeight = 50;

	protected ProductTypeSelectionPanel productTypeSelectionPanel;
	protected Map<String, String> productTypeMap;
	private List<String> partGroups = new ArrayList<String>();

	private TablePane partNamePanel = new TablePane("Part Name");
	private TablePane partSpecPanel = new TablePane("Part Spec");
	private TablePane requiredSpecPanel = new TablePane("External Required Part Spec");

	private ExtRequiredPartTableModel extRequiredPartTableModel;
	private ExtRequiredPartSpecTableModel extRequiredPartSpecTableModel;
	private PartSpecTableModel partSpecTableModel= new PartSpecTableModel(partSpecPanel.getTable(),null);

	private JTextField partFilterInput;

	private List<PartName> parts = new ArrayList<PartName>();
	private List<ExtRequiredPartSpecDto> requiredParts = new ArrayList<ExtRequiredPartSpecDto>();
	List<ExtRequiredPartSpecDto> requiredSpecs = new ArrayList<ExtRequiredPartSpecDto>();

	public ExternalRequiredPartPanel() {
		super("EXTERNAL_REQUIRED_PART_PANEL", KeyEvent.VK_R);
		AnnotationProcessor.process(this);
	}

	private void handleExtRequiredPartSpecTableChange(TableModelEvent e, ExtRequiredPartSpecTableModel model) {
		try {
			final ExtRequiredPartSpecDto requiredPartSpec = model.getSelectedItem();
			if(requiredPartSpec == null) return;
			if(e.getType() == TableModelEvent.UPDATE) {
				PartId partId = new PartId();
				ExtRequiredPartSpec extRequiredPartSpec = new ExtRequiredPartSpec();
				partId.setPartName(model.getSelectedItem().getPartName());
				partId.setPartId(model.getSelectedItem().getPartId());
				extRequiredPartSpec.setId(partId);
				extRequiredPartSpec.setPartGroup(model.getSelectedItem().getPartGroup());
				extRequiredPartSpec.setParseStrategy(model.getSelectedItem().getParseStrategy());
				extRequiredPartSpec.setParserInformation(model.getSelectedItem().getParserInformation());
				ServiceFactory.getDao(ExtRequiredPartSpecDao.class).update(extRequiredPartSpec);
			}
		}catch(Exception ex) {
			handleException(ex);
			if(model != null) 
				model.rollback();
		}
		return;
	}

	private void handleExtRequiredPartTableChange(TableModelEvent e, ExtRequiredPartTableModel model) {
		try{
			final PartName partName = model.getSelectedItem();
			if(partName == null) return;
			if(e.getType() == TableModelEvent.UPDATE) {
				if((e.getColumn() == 1|| e.getColumn()==2) && !confirmUpdate(e, model)) {
					return;
				}
				ServiceFactory.getDao(PartNameDao.class).update(partName);
			}
		} catch(Exception ex) {
			if(model!=null)
				model.rollback();
			handleException(ex);
		}
		return;
	}

	protected void initComponents() {

		setLayout(new BorderLayout());
		JSplitPane topSplitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				createLeftPanel(), createRightPanel());
		topSplitPanel.setDividerLocation(600);
		topSplitPanel.setContinuousLayout(true);

		add(topSplitPanel,BorderLayout.CENTER);
	}

	private JPanel createLeftPanel() {		
		JPanel leftPanel = new JPanel(new MigLayout("insets 0, gap 0", "[][]",""));

		leftPanel.add(createProductTypeSelectionPanel(),"height : 40, width : max, wrap");
		leftPanel.add(partNamePanel, "height : max, width : max, wrap");
		leftPanel.add(createPartFilterPanel());		
		return leftPanel;		
	}

	private JPanel createRightPanel() {
		JPanel rightPanel = new JPanel(new BorderLayout());

		partSpecPanel.getTable().setRowHeight(23);
		requiredSpecPanel.getTable().setRowHeight(23);

		JSplitPane rightSplitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
				partSpecPanel, requiredSpecPanel);
		rightSplitPanel.setDividerLocation(380);
		rightSplitPanel.setContinuousLayout(true);
		add(rightSplitPanel,BorderLayout.CENTER);			
		rightPanel.add(rightSplitPanel);

		return rightPanel;
	}

	protected Component createPartFilterPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("insets 0","5[][]2",""));
		panel.add(new JLabel("Part Filter:"));

		partFilterInput = new JTextField();
		partFilterInput.setDocument(new UpperCaseDocument(32));
		partFilterInput.setFont(Fonts.DIALOG_BOLD_12);

		panel.add(partFilterInput,"width : max");
		return panel;
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

	private void addListeners() {
		partNamePanel.addListSelectionListener(this);

		partFilterInput.getDocument().addDocumentListener(createPartNameFilerListener());

		partSpecPanel.getTable().addMouseListener(createPartSpecMenuListener());
		partSpecPanel.addListSelectionListener(this);

		requiredSpecPanel.addListSelectionListener(this);
		requiredSpecPanel.getTable().addMouseListener(createRequiredPartSpecMenuListener());
	}

	private void updatePartSelectionModel() {
		if(productTypeSelectionPanel == null) return;

		String productType = productTypeSelectionPanel.selectedProductType();
		if(productType != null) parts = getParts(productType);

		extRequiredPartTableModel = new ExtRequiredPartTableModel(partNamePanel.getTable(), parts);
		extRequiredPartTableModel.addTableModelListener(this);
		extRequiredPartTableModel.pack();
	}

	private void updateRequiredSpecsModel() {
		if(requiredSpecPanel == null) return;

		requiredParts = getAllRequiredSpecs();

		extRequiredPartSpecTableModel = new ExtRequiredPartSpecTableModel(requiredSpecPanel.getTable(), requiredParts, partGroups);
		extRequiredPartSpecTableModel.addTableModelListener(this);
		extRequiredPartSpecTableModel.pack();
	}

	private void updateRequiredSpecsModelByProductType() {
		if(requiredSpecPanel == null) return;

		requiredParts = getAllRequiredSpecsByProductType(productTypeSelectionPanel.selectedProductType());		

		extRequiredPartSpecTableModel = new ExtRequiredPartSpecTableModel(requiredSpecPanel.getTable(), requiredParts, partGroups);
		extRequiredPartSpecTableModel.pack();
	}

	private void initPartNamePanel() {
		String productType = productTypeSelectionPanel.selectedProductType();
		if(productType != null)
			if(!productType.equalsIgnoreCase(currentProductType)) {
				currentProductType = productType;
				updatePartSelectionModel();
			}
	}

	@EventSubscriber(eventClass=ProductTypeSelectionEvent.class)
	public void processProductTypeSelectionPanelChanged(ProductTypeSelectionEvent event) {
		if(event.isEventFromSource(SelectionEvent.PRODUCT_TYPE_SELECTED, productTypeSelectionPanel)) {
			initPartNamePanel();

			if(productTypeSelectionPanel.selectedProductType() != null)
				updateRequiredSpecsModelByProductType();
		}else if(event.isEventFromSource(SelectionEvent.PLANT_SELECTED, productTypeSelectionPanel)) {
			updateRequiredSpecsModel();
		}
	}

	private DocumentListener createPartNameFilerListener() {
		DocumentListener listener = new DocumentListener() {

			public void removeUpdate(DocumentEvent arg0) {
				onPartFilterChange();				
			}

			public void insertUpdate(DocumentEvent arg0) {
				onPartFilterChange();				
			}

			public void changedUpdate(DocumentEvent arg0) {
				onPartFilterChange();				
			}
		};
		return listener;
	}

	private void addSpec() {
		ExtRequiredPartSpec extRequiredPart= new ExtRequiredPartSpec();
		PartId partId = new PartId();

		partId.setPartName(partSpecTableModel.getSelectedItem().getId().getPartName());
		partId.setPartId(partSpecTableModel.getSelectedItem().getId().getPartId());
		extRequiredPart.setPartGroup("");
		extRequiredPart.setParseStrategy("");
		extRequiredPart.setParserInformation("");
		extRequiredPart.setId(partId);
		
		if(extRequiredPartSpecTableModel.hadRequiredPart(partId.getPartName(), partId.getPartId())) {
			MessageDialog.showError(this, "Part  \"" + 	partId.getPartName()	+ "\" with  \"" + partId.getPartId()	+ "\" Part Id exists. ");
		}else {
			saveSpec(extRequiredPart);
		}
	}
	
	private void deleteSpec() {
		PartId partId = new PartId();
		ExtRequiredPartSpec extRequiredPart= new ExtRequiredPartSpec();

		partId.setPartName(extRequiredPartSpecTableModel.getSelectedItem().getPartName());
		partId.setPartId(extRequiredPartSpecTableModel.getSelectedItem().getPartId());
		extRequiredPart.setParseStrategy(extRequiredPartSpecTableModel.getSelectedItem().getParseStrategy());
		extRequiredPart.setParserInformation(extRequiredPartSpecTableModel.getSelectedItem().getParserInformation());
		extRequiredPart.setId(partId);

		deleteSpec(extRequiredPart);
	}

	private void saveSpec(ExtRequiredPartSpec extRequiredPart) {
		ExtRequiredPartSpecDto requiredPartSpec = new ExtRequiredPartSpecDto();

		requiredPartSpec.setPartName(partSpecTableModel.getSelectedItem().getId().getPartName());
		requiredPartSpec.setPartId(partSpecTableModel.getSelectedItem().getId().getPartId());
		requiredPartSpec.setPartDescription(partSpecTableModel.getSelectedItem().getPartDescription());
		requiredPartSpec.setPartSerialNumberMask(partSpecTableModel.getSelectedItem().getPartSerialNumberMask());
		requiredPartSpec.setMaxAttempts(partSpecTableModel.getSelectedItem().getPartMaxAttempts());
		requiredPartSpec.setMeasurementCount(partSpecTableModel.getSelectedItem().getMeasurementCount());
		requiredPartSpec.setComment(partSpecTableModel.getSelectedItem().getComment());
		requiredPartSpec.setPartMark(partSpecTableModel.getSelectedItem().getPartMark());
		requiredPartSpec.setPartNumber(partSpecTableModel.getSelectedItem().getPartNumber());
		requiredPartSpec.setParseStragety(BLANK);
		requiredPartSpec.setParserInformation(BLANK);

		ServiceFactory.getDao(ExtRequiredPartSpecDao.class).save(extRequiredPart);

		requiredParts.add(requiredPartSpec);
		requiredSpecs.add(requiredPartSpec);
		extRequiredPartSpecTableModel.refresh(requiredSpecs);
	}

	private void onPartFilterChange(){
		partNamePanel.clearSelection();
		triggerfilterParts();
	}

	private void triggerfilterParts() {		
		String filter = partFilterInput.getText();			
		extRequiredPartTableModel.refresh(filterPartNameList(filter));
	}

	private List<PartName> filterPartNameList(String filter) {
		if (StringUtils.isEmpty(filter)) {
			return parts;
		}
		List<PartName> filteredList = new ArrayList<PartName>();
		if (parts == null || parts.isEmpty()) {
			return filteredList;
		}
		for (PartName partName : parts) {
			String name = partName.getPartName();
			if (name == null) {
				continue;
			}
			if (name.toUpperCase().contains(filter)) {
				filteredList.add(partName);
			}
		}
		return filteredList;
	}

	public void tableChanged(TableModelEvent e) {
		if(e.getSource() == null) return;

		if(e.getSource() instanceof ExtRequiredPartSpecTableModel) {
			handleExtRequiredPartSpecTableChange(e, (ExtRequiredPartSpecTableModel)e.getSource());
		}else if (e.getSource() instanceof ExtRequiredPartTableModel) {
			handleExtRequiredPartTableChange(e, (ExtRequiredPartTableModel)e.getSource());
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) {
			return;
		}
		if(e.getSource().equals(partNamePanel.getTable().getSelectionModel())){

			PartName selectedPartName = extRequiredPartTableModel.getSelectedItem();
			requiredSpecs = (selectedPartName == null) ? new ArrayList<ExtRequiredPartSpecDto>() : 
				getRequiredPartSpecsByProductTypeAndPartName(productTypeSelectionPanel.selectedProductType(),selectedPartName);
			extRequiredPartSpecTableModel = new ExtRequiredPartSpecTableModel(requiredSpecPanel.getTable(), requiredSpecs, partGroups);

			List<PartSpec> partSpecList = (selectedPartName == null) ? new ArrayList<PartSpec>() : getAllPartSpecs(selectedPartName);				
			partSpecTableModel = new PartSpecTableModel(partSpecPanel.getTable(),partSpecList, false);
			partSpecTableModel.pack();
			partSpecTableModel.addTableModelListener(this);

			extRequiredPartSpecTableModel.pack();
			extRequiredPartSpecTableModel.addTableModelListener(this);
		}
	}

	private MouseListener createPartSpecMenuListener() {
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e){
				showCreatePartSpecMenu(e);
			}
		});
	}

	private MouseListener createRequiredPartSpecMenuListener() {
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e){
				showCreateRequiredPartSpecMenu(e);
			}
		});
	}

	private void showCreatePartSpecMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(ADD_SPEC, true));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	private void showCreateRequiredPartSpecMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(DELETE_SPEC, true));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	public void actionPerformed(ActionEvent e) {
		Exception exception = null;
		if(e.getSource() instanceof JMenuItem) {
			try {
				JMenuItem menuItem= (JMenuItem)e.getSource();
				if(menuItem.getName().equals(ADD_SPEC)) addSpec();
				else if(menuItem.getName().equals(DELETE_SPEC)) deleteSpec();
			} catch(Exception ex) {
				exception = ex;
			}
			handleException(exception);
		}
	}
	
	@Override
	public void onTabSelected() {
		if(isInitialized) 
		{	
			updatePartSelectionModel();
			return;
		}

		initComponents();
		this.registerProcessPointSelectionPanel(productTypeSelectionPanel);
		getPartGroups();
		updatePartSelectionModel();
		updateRequiredSpecsModel();
		addListeners();
		isInitialized = true;
	}

	private boolean confirmUpdate(TableModelEvent e, ExtRequiredPartTableModel model) {

		int index = model.getItems().indexOf((model.getSelectedItem()));
		Object newValue = model.getValueAt(index, e.getColumn());
		StringBuilder msg = new StringBuilder("Please confirm change ");
		if(newValue  instanceof Boolean){
			msg.append(model.getColumnName(e.getColumn())).append(" to ").append(((Boolean)newValue) ? "\"checked\"." : "\"un-checked\".");
		}
		boolean confirm = MessageDialog.confirm(this, msg.toString(), true);

		if(!confirm){
			if(model != null) model.rollback();
		}
		return confirm;
	}

	private List<ExtRequiredPartSpecDto> getAllRequiredSpecs() {
		List<ExtRequiredPartSpecDto> reqParts = null;
		try {
			reqParts = ServiceFactory.getDao(ExtRequiredPartSpecDao.class).findAllRequiredSpecs();
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getAllRequiredSpecs";
			getLogger().error(e,msg);
			e.printStackTrace();
		}
		return reqParts;
	}

	private List<ExtRequiredPartSpecDto> getAllRequiredSpecsByProductType(String productType) {
		List<ExtRequiredPartSpecDto> reqPartSpec = null;
		try{
			reqPartSpec = ServiceFactory.getDao(ExtRequiredPartSpecDao.class).findAllRequiredSpecsByProductType(productType);
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getAllRequiredSpecsByProductType";
			getLogger().error(e,msg);
			e.printStackTrace();
		}
		return reqPartSpec;
	}

	private List<PartName> getParts(String productType) {
		List<PartName> partName = null;
		try{
			partName = ServiceFactory.getDao(PartNameDao.class).findAllByProductType(productType);
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getParts";
			getLogger().error(e,msg);
			e.printStackTrace();
		}
		return partName;
	}

	private List<PartSpec> getAllPartSpecs(PartName selectedPartName) {
		List<PartSpec> partSpec = null;
		try {
			partSpec = ServiceFactory.getDao(PartSpecDao.class).findAllByPartName(selectedPartName.getPartName());
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getAllPartSpecs";
			getLogger().error(e,msg);
			e.printStackTrace();	
		}
		return partSpec;
	}

	private List<ExtRequiredPartSpecDto> getRequiredPartSpecsByProductTypeAndPartName(String productType, PartName selectedPartName) {
		List<ExtRequiredPartSpecDto> extReqPartSpec = null;
		try{
			extReqPartSpec =  ServiceFactory.getDao(ExtRequiredPartSpecDao.class).findAllRequiredSpecsByProductTypeAndPartName(productType,
					selectedPartName.getPartName());
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getRequiredPartSpecsByProductTypeAndPartName";
			getLogger().error(e,msg);
			e.printStackTrace();
		}
		return extReqPartSpec;
	}

	private void getPartGroups() {
		try {
			List<ComponentProperty> propertyList = ServiceFactory.getDao(ComponentPropertyDao.class).
					findAllByPropertyKey(EXT_REQUIRED_PART_GROUP_NAME);
			Set<String> partsGroupSet = new HashSet<String>();
			for(ComponentProperty componentProperty : propertyList) {
				String[] property = componentProperty.getPropertyValue().split(",");
				for(String currentProperty : property) {
					partsGroupSet.add(StringUtils.trim(currentProperty.toString()));
				}
			}
			partGroups.add("");
			for(String partGroupSet : partsGroupSet) {
				partGroups.add(partGroupSet);
			}
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getPartGroups";
			getLogger().error(e,msg);
			e.printStackTrace();
		}
	}
	
	private void deleteSpec(ExtRequiredPartSpec extRequiredPart) {

		try{
		ServiceFactory.getDao(ExtRequiredPartSpecDao.class).remove(extRequiredPart);

		extRequiredPartSpecTableModel.remove(extRequiredPartSpecTableModel.getSelectedItem());
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "deleteSpec";
			getLogger().error(e,msg);
			e.printStackTrace();	
		}
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

	public void registerProcessPointSelectionPanel(ProductTypeSelectionPanel productTypeSelectionPanel) {
		// TODO Auto-generated method stub		
	}
}