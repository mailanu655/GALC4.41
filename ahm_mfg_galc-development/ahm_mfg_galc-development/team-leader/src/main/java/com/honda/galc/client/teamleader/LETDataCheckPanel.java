/**
 * 
 */
package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

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
import com.honda.galc.dao.product.ExtRequiredPartSpecDao;
import com.honda.galc.dao.product.LetInspectionParamDao;
import com.honda.galc.dao.product.LetInspectionProgramDao;
import com.honda.galc.dao.product.LetPartCheckSpecDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.dto.ExtRequiredPartSpecDto;
import com.honda.galc.dto.LetRequiredPartSpecsDto;
import com.honda.galc.entity.product.LetInspectionParam;
import com.honda.galc.entity.product.LetInspectionProgram;
import com.honda.galc.entity.product.LetPartCheckSpec;
import com.honda.galc.entity.product.LetPartCheckSpecId;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * @author vf031824
 *
 */
public class LETDataCheckPanel extends TabbedPanel implements TableModelListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;

	private static String ADD_LET_DATA = "Add LET Data to Spec";
	private static String DELETE_LET_DATA = "Delete LET Required Spec";
	private String DB_ERROR_OCCURED = "A database error has occured in method: ";

	private int productTypePanelWidth = 1000;
	private int productTypePanelHeight = 50;
	private String currentProductType;

	private TablePane partNamePanel = new TablePane("LET Required Parts", true);
	private TablePane letProgramNamePanel;
	private TablePane letParamNamePanel;
	private TablePane partSpecListPanel;

	private TablePane partSpecPanel;

	protected ProductTypeSelectionPanel productTypeSelectionPanel;

	private PartNameTableModel partNameTableModel;
	private ExtRequiredPartSpecTableModel partSpecTableModel;
	private LETInspectionProgramTableModel letInspectionProgramTableModel;
	private LETInspectionParamTableModel letInstpectionParamTableModel;
	private LETRequiredPartsTableModel letRequiredPartsTableModel;

	private JTextField partFilterInput;
	private JTextField programFilterInput;
	private JTextField paramFilterInput;

	private List<PartName> requiredParts = new ArrayList<PartName>();
	private List<ExtRequiredPartSpecDto> partSpecs = new ArrayList<ExtRequiredPartSpecDto>();
	private List<LetInspectionProgram> letInspectionProgram = new ArrayList<LetInspectionProgram>();
	private List<LetInspectionParam> letInspectionParam = new ArrayList<LetInspectionParam>();
	private List<LetRequiredPartSpecsDto> letRequiredPartSpecs = new ArrayList<LetRequiredPartSpecsDto>(); 

	public LETDataCheckPanel() {
		super("LET_DATA_CHECK_PANEL", KeyEvent.VK_L);
		AnnotationProcessor.process(this);
	}

	@Override
	public void onTabSelected() {
		if(isInitialized){
			return;
		}
		initComponents();
		loadLETData();
		loadLETRequiredPartSpecs();
		this.registerProcessPointSelectionPanel(productTypeSelectionPanel);

		addListeners();
		isInitialized = true;
	}

	protected void initComponents() {
		setLayout(new BorderLayout());
		JSplitPane topSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				createLeftPanel(), createRightPanel());
		topSplitPane.setDividerLocation(600);
		topSplitPane.setContinuousLayout(true);
		add(topSplitPane,BorderLayout.CENTER);
	}

	private TablePane createProductSpecPanel() {
		if(partSpecPanel == null){
			partSpecPanel = new TablePane("Part Specs");
		}
		return partSpecPanel;
	}

	private Component createLeftPanel() {
		JPanel leftPanel = new JPanel(new MigLayout("insets 0, gap 0", "[][]",""));

		leftPanel.add(createProductTypeSelectionPanel(), "height : 40, width : max, wrap");
		leftPanel.add(partNamePanel, "height : max, width : max, wrap");
		leftPanel.add(createPartFilterPanel(), "wrap");
		return leftPanel;
	}

	private Component createLETProgramSelectionPanel() {
		JPanel letProgramSelectionPanel = new JPanel(new MigLayout("insets 0, gap 0", "[][]",""));
		letProgramNamePanel  = new TablePane("LET Program Name");
		letProgramSelectionPanel.add(letProgramNamePanel, "height : max, width : max, wrap");

		letProgramSelectionPanel.add(createProgramFilterPanel());

		return letProgramSelectionPanel;
	}

	private Component createLETParamSelectionPanel() {
		JPanel letParamSelectionPanel = new JPanel(new MigLayout("insets 0, gap 0", "[][]",""));

		letParamNamePanel  = new TablePane("LET Param Name");
		letParamSelectionPanel.add(letParamNamePanel,"height : max, width : max, wrap");
		letParamSelectionPanel.add(createParamFilterPanel());
		return letParamSelectionPanel;
	}

	private Component createRightPanel() {
		JPanel rightPanel = new JPanel(new MigLayout("insets 0, gap 0", "5[][]2",""));

		rightPanel.add(createProductSpecPanel(), "height : max , width : max");
		rightPanel.add(createLETProgramSelectionPanel());
		rightPanel.add(createLETParamSelectionPanel(), "wrap");	
		rightPanel.add(createPartSpecList(), "height : max , width : max, span");
		return rightPanel;
	}

	private ProductTypeSelectionPanel createProductTypeSelectionPanel() {
		String siteName = PropertyService.getSiteName();
		if(StringUtils.isEmpty(siteName)) {
			MessageDialog.showError(this, "SITE_NAME is not defined in the System_Info property");	
		}
		productTypeSelectionPanel = new ProductTypeSelectionPanel(siteName);
		productTypeSelectionPanel.setSize(productTypePanelWidth, productTypePanelHeight);

		return productTypeSelectionPanel;
	}

	protected TablePane createPartSpecList() {
		if(partSpecListPanel == null){
			partSpecListPanel = new TablePane("LET Required Part Specs");
		}
		return partSpecListPanel;
	}

	protected Component createPartFilterPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("insets 0","0[][]0",""));
		panel.add(new JLabel("Part Filter:"));

		partFilterInput = new JTextField();
		partFilterInput.setDocument(new UpperCaseDocument(32));
		partFilterInput.setFont(Fonts.DIALOG_BOLD_12);

		panel.add(partFilterInput,"width : max");
		return panel;
	}

	protected Component createProgramFilterPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("insets 0","[][]",""));
		panel.add(new JLabel("Program Filter:"));

		programFilterInput = new JTextField();
		programFilterInput.setDocument(new UpperCaseDocument(32));
		programFilterInput.setFont(Fonts.DIALOG_BOLD_12);

		panel.add(programFilterInput,"width : max");
		return panel;
	}

	protected Component createParamFilterPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("insets 0","0[][]0",""));
		panel.add(new JLabel("Pram Filter:"));

		paramFilterInput = new JTextField();
		paramFilterInput.setDocument(new UpperCaseDocument(32));
		paramFilterInput.setFont(Fonts.DIALOG_BOLD_12);

		panel.add(paramFilterInput,"width : max");
		return panel;
	}

	private void initPartNamePanel() {
		String productType = productTypeSelectionPanel.selectedProductType();
		if(productType != null)
			if(!productType.equalsIgnoreCase(currentProductType)) {
				currentProductType = productType;
				updateLetRequiredPartsModel();
			}
	}

	private void updateLetRequiredPartsModel() {
		if(productTypeSelectionPanel.selectedProductType() == null) return;
		String productType = productTypeSelectionPanel.selectedProductType();
		if(productType != null) requiredParts = getLETRequiredPartsByProductType(productType);

		partNameTableModel = new PartNameTableModel(partNamePanel.getTable(), requiredParts, true);
		partNameTableModel.addTableModelListener(this);
		partNameTableModel.pack();
	}

	private void loadLETData() {
		letInspectionProgram = getLetProgramNames();
		letInspectionProgramTableModel = new LETInspectionProgramTableModel(letProgramNamePanel.getTable(), letInspectionProgram);

		letInspectionParam = getLetParamNames();
		letInstpectionParamTableModel = new LETInspectionParamTableModel(letParamNamePanel.getTable(), letInspectionParam);

		letInspectionProgramTableModel.addTableModelListener(this);
		letInspectionProgramTableModel.pack();

		letInstpectionParamTableModel.addTableModelListener(this);
		letInspectionProgramTableModel.pack();
	}

	private void loadLETRequiredPartSpecs() {
		letRequiredPartSpecs = getLetRequiredPartSpecs();
		letRequiredPartsTableModel = new LETRequiredPartsTableModel(partSpecListPanel.getTable(), letRequiredPartSpecs);
		letRequiredPartsTableModel.pack();
	}

	private void loadPartSpecTable(PartName partName) {
		if(partName != null) {
			partSpecs = getRequiredPartSpecsByProductTypeAndPartName(productTypeSelectionPanel.selectedProductType(), partName);
		}
		partSpecTableModel = new ExtRequiredPartSpecTableModel(partSpecPanel.getTable(), partSpecs, true);
		partSpecTableModel.addTableModelListener(this);
		partSpecTableModel.pack();
	}

	private void updateRequiredPartSpecTable(PartName partName) {
		if(partName != null) {
			letRequiredPartSpecs = getLETRequiredSpecsByPartName(partName);
			letRequiredPartsTableModel = new LETRequiredPartsTableModel(partSpecListPanel.getTable(), letRequiredPartSpecs);
			letRequiredPartsTableModel.pack();
			letRequiredPartsTableModel.addTableModelListener(this);
		} else {
			loadLETRequiredPartSpecs();
			partSpecs.clear();
			partSpecTableModel = new ExtRequiredPartSpecTableModel(partSpecPanel.getTable(), partSpecs, true);
		}
	}
	
	private void updateRequiredPartSpecTableByPartSpec(String partName) {
		if(partName != null) {
			String partSpec = partSpecTableModel.getSelectedItem().getPartId();
			letRequiredPartSpecs = getLETRequiredSpecsByPartNameAndSpec(partName, partSpec);
			letRequiredPartsTableModel = new LETRequiredPartsTableModel(partSpecListPanel.getTable(), letRequiredPartSpecs);
			letRequiredPartsTableModel.pack();
			letRequiredPartsTableModel.addTableModelListener(this);
		}else {
				loadLETRequiredPartSpecs();
				partSpecs.clear();
				partSpecTableModel = new ExtRequiredPartSpecTableModel(partSpecPanel.getTable(), partSpecs, true);
		}
	}

	@EventSubscriber(eventClass=ProductTypeSelectionEvent.class)
	public void processProductTypeSelectionPanelChanged(ProductTypeSelectionEvent event) {
		if(event.isEventFromSource(SelectionEvent.PRODUCT_TYPE_SELECTED, productTypeSelectionPanel)) {
			initPartNamePanel();
		}
	}

	private void triggerfilterParts() {		
		String filter = partFilterInput.getText();			
		partNameTableModel.refresh(filterPartNameList(filter));
	}

	private void triggerfilterPrograms() {		
		String filter = programFilterInput.getText();			
		letInspectionProgramTableModel.refresh(filterProgramList(filter));
	}

	private void triggerfilterParams() {		
		String filter = paramFilterInput.getText();			
		letInstpectionParamTableModel.refresh(filterParamList(filter));
	}

	private List<PartName> filterPartNameList(String filter) {
		if (StringUtils.isEmpty(filter)) {
			return requiredParts;
		}
		List<PartName> filteredList = new ArrayList<PartName>();
		if (requiredParts == null || requiredParts.isEmpty()) {
			return filteredList;
		}
		for (PartName partName : requiredParts) {
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

	private List<LetInspectionParam> filterParamList(String filter) {
		if (StringUtils.isEmpty(filter)) {
			return letInspectionParam;
		}
		List<LetInspectionParam> filteredList = new ArrayList<LetInspectionParam>();
		if (letInspectionParam == null || letInspectionParam.isEmpty()) {
			return filteredList;
		}
		for (LetInspectionParam LETParam : letInspectionParam) {
			String name = LETParam.getInspectionParamName();
			if (name == null) {
				continue;
			}
			if (name.toUpperCase().contains(filter)) {
				filteredList.add(LETParam);
			}
		}
		return filteredList;
	}

	private List<LetInspectionProgram> filterProgramList(String filter) {
		if (StringUtils.isEmpty(filter)) {
			return letInspectionProgram;
		}
		List<LetInspectionProgram> filteredList = new ArrayList<LetInspectionProgram>();
		if (letInspectionProgram == null || letInspectionProgram.isEmpty()) {
			return filteredList;
		}
		for (LetInspectionProgram LETProgram : letInspectionProgram) {
			String program = LETProgram.getInspectionPgmName();
			if (program == null) {
				continue;
			}
			if (program.toUpperCase().contains(filter)) {
				filteredList.add(LETProgram);
			}
		}
		return filteredList;
	}

	public void actionPerformed(ActionEvent e) {
		Exception exception = null;
		try{
			JMenuItem menuItem = (JMenuItem)e.getSource();	
			if(menuItem.getName().equals(ADD_LET_DATA)) addLETData();
			else if(menuItem.getName().equals(DELETE_LET_DATA)) deleteLETData();

		}catch(Exception ex) {
			exception = ex;
		}
		handleException(exception);
	}

	private void addLETData() {

		LetPartCheckSpec part = new LetPartCheckSpec();
		LetPartCheckSpecId letPartCheckSpecId = new LetPartCheckSpecId();

		letPartCheckSpecId.setPartName(partSpecTableModel.getSelectedItem().getPartName());
		letPartCheckSpecId.setPartId(partSpecTableModel.getSelectedItem().getPartId());
		letPartCheckSpecId.setInspectionProgramId(letInspectionProgramTableModel.getSelectedItem().getInspectionPgmId());
		letPartCheckSpecId.setInspectionParamId(letInstpectionParamTableModel.getSelectedItem().getInspectionParamId());
		part.setId(letPartCheckSpecId);
		part.setSequenceNumber(generateNewSequenceNumber());

		if(letRequiredPartsTableModel.hasRequiredPart(letPartCheckSpecId.getPartName(), letPartCheckSpecId.getPartId(),
				letInspectionProgramTableModel.getSelectedItem().getInspectionPgmName(),
				letInstpectionParamTableModel.getSelectedItem().getInspectionParamName())){

			MessageDialog.showError(this, "Part  \"" + 	letPartCheckSpecId.getPartName()	+ " already exist with program and param combination");
			
		}else if(letRequiredPartsTableModel.hasProgramName(letInspectionProgramTableModel.getSelectedItem().getInspectionPgmName())){
			
			MessageDialog.showError(this, "Part  \"" + 	letInspectionProgramTableModel.getSelectedItem().getInspectionPgmName()
					+ " can't be used. Only one program per part spec.");
		}else {
			ServiceFactory.getDao(LetPartCheckSpecDao.class).save(part);

			LetRequiredPartSpecsDto  letRequiredSpec = new LetRequiredPartSpecsDto();
			letRequiredSpec.setPartName(partSpecTableModel.getSelectedItem().getPartName());
			letRequiredSpec.setPartId(partSpecTableModel.getSelectedItem().getPartId());
			letRequiredSpec.setDesctiption(partSpecTableModel.getSelectedItem().getPartDescription());
			letRequiredSpec.setPartSerialNumberMask(partSpecTableModel.getSelectedItem().getPartSerialNumberMask());
			letRequiredSpec.setInspectionPgmName(letInspectionProgramTableModel.getSelectedItem().getInspectionPgmName());
			letRequiredSpec.setInspectionParamName(letInstpectionParamTableModel.getSelectedItem().getInspectionParamName());
			letRequiredSpec.setSequenceNumber(part.getSequenceNumber());

			letRequiredPartSpecs.add(letRequiredSpec);
			letRequiredPartsTableModel.refresh(letRequiredPartSpecs);
		}
	}
	private void deleteLETData() {
		LetPartCheckSpecId letPartCheckSpecId = new LetPartCheckSpecId();

		letPartCheckSpecId.setPartName(letRequiredPartsTableModel.getSelectedItem().getPartName());
		letPartCheckSpecId.setPartId(letRequiredPartsTableModel.getSelectedItem().getPartId());
		letPartCheckSpecId.setInspectionProgramId(
				getProgramId(letRequiredPartsTableModel.getSelectedItem().getInspectionPgmName()));
		letPartCheckSpecId.setInspectionParamId(getparamId(letRequiredPartsTableModel.getSelectedItem().getInspectionParamName()));

		if(!MessageDialog.confirm(this, "Are you sure that you want to delete selected LET required part?")) {
			return;
		}else {
			ServiceFactory.getDao(LetPartCheckSpecDao.class).removeByKey(letPartCheckSpecId);

			letRequiredPartsTableModel.remove(letRequiredPartsTableModel.getSelectedItem());
			reorderSequenceNumbers();
		}
	}

	private int generateNewSequenceNumber() {
		return letRequiredPartsTableModel.getRowCount() + 1;
	}

	private void reorderSequenceNumbers() {
		int i = 1;
		for(LetRequiredPartSpecsDto spec: letRequiredPartSpecs) {
			spec.setSequenceNumber(i);
			updateDb(spec);
			i++;
		}
	}

	private void updateDb(LetRequiredPartSpecsDto spec) {

		LetPartCheckSpec part = new LetPartCheckSpec();
		LetPartCheckSpecId letPartCheckSpecId = new LetPartCheckSpecId();

		letPartCheckSpecId.setPartName(spec.getPartName());
		letPartCheckSpecId.setPartId(spec.getPartId());
		letPartCheckSpecId.setInspectionProgramId(getProgramId(spec.getInspectionPgmName()));
		letPartCheckSpecId.setInspectionParamId(getparamId(spec.getInspectionParamName()));
		part.setId(letPartCheckSpecId);
		part.setSequenceNumber(spec.getSequenceNumber());
		part.setParamType(spec.getParamType());

		ServiceFactory.getDao(LetPartCheckSpecDao.class).update(part);
	}

	private void addListeners() {
		MouseListener listener = createMouseListener();

		partFilterInput.getDocument().addDocumentListener(createPartNameFilterListener());
		programFilterInput.getDocument().addDocumentListener(createProgramFilterListener());
		paramFilterInput.getDocument().addDocumentListener(createParamFilterListener());

		partNamePanel.addListSelectionListener(this);
		
		partSpecPanel.addListSelectionListener(this);

		letParamNamePanel.getTable().addMouseListener(listener);
		letProgramNamePanel.getTable().addMouseListener(listener);

		letRequiredPartsTableModel.addTableModelListener(this);
		partSpecListPanel.getTable().addMouseListener(deleteLETRequiredSpecMouseListener());
	}

	private MouseListener deleteLETRequiredSpecMouseListener() {
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showdeleteLETRequiredMouseMenu(e);
			}
			private void showdeleteLETRequiredMouseMenu(MouseEvent e) {
				JPopupMenu popupMenu = new JPopupMenu();
				popupMenu.add(createMenuItem(DELETE_LET_DATA, isPartSelected()));
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	private MouseListener createMouseListener(){
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showAddPopupMenu(e);
			}
		}); 
	}

	private void showAddPopupMenu(MouseEvent e)	 {

		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(ADD_LET_DATA,areAllSelected()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	private boolean areAllSelected() {
		return isPartSpecSelected() && isLETParamNameSelected()
				&& isLETProgramNameSelected();
	}

	public void tableChanged(TableModelEvent e) {
		if(e.getSource() == null) return;

		if(e.getSource() instanceof LETRequiredPartsTableModel){
			handleLETRequiredPartsTableModel(e, (LETRequiredPartsTableModel)e.getSource());
		}
	}

	private void handleLETRequiredPartsTableModel(TableModelEvent e,
			LETRequiredPartsTableModel model) {

		try {
			final LetRequiredPartSpecsDto letRequiredPart = model.getSelectedItem();
			if(letRequiredPart == null) return;
			if(e.getType() == TableModelEvent.UPDATE) {
				LetPartCheckSpecId id = new LetPartCheckSpecId();
				LetPartCheckSpec spec = new LetPartCheckSpec();
				id.setPartName(model.getSelectedItem().getPartName());
				id.setPartId(model.getSelectedItem().getPartId());
				id.setInspectionProgramId(getProgramId(letRequiredPartsTableModel.getSelectedItem().getInspectionPgmName()));
				id.setInspectionParamId(getparamId(letRequiredPartsTableModel.getSelectedItem().getInspectionParamName()));

				spec.setId(id);
				spec.setParamType(model.getSelectedItem().getParamType());
				spec.setSequenceNumber(model.getSelectedItem().getSequenceNumber());

				ServiceFactory.getDao(LetPartCheckSpecDao.class).update(spec);
			}

		}catch (Exception ex) {
			ex.printStackTrace();
			if(model != null) {
				model.rollback();
			}
		}
	}

	private DocumentListener createPartNameFilterListener() {
		DocumentListener listener = new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {  
				onPartNameFilterChange();
			}
			public void removeUpdate(DocumentEvent e) { 
				onPartNameFilterChange(); 
			}
			public void changedUpdate(DocumentEvent e) { 
				onPartNameFilterChange();
			}
		};
		return listener;
	} 

	private DocumentListener createProgramFilterListener() {
		DocumentListener listener = new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {  
				onProgramFilterChange();
			}
			public void removeUpdate(DocumentEvent e) { 
				onProgramFilterChange(); 
			}
			public void changedUpdate(DocumentEvent e) { 
				onProgramFilterChange();
			}
		};
		return listener;
	} 

	private DocumentListener createParamFilterListener() {
		DocumentListener listener = new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {  
				onParamFilterChange();
			}
			public void removeUpdate(DocumentEvent e) { 
				onParamFilterChange(); 
			}
			public void changedUpdate(DocumentEvent e) { 
				onParamFilterChange();
			}
		};
		return listener;
	} 

	private void onPartNameFilterChange(){
		partNamePanel.clearSelection();
		triggerfilterParts();
	}

	private void onProgramFilterChange(){
		letProgramNamePanel.clearSelection();
		triggerfilterPrograms();
	}

	private void onParamFilterChange(){
		letParamNamePanel.clearSelection();
		triggerfilterParams();
	}

	public void valueChanged(ListSelectionEvent e) {

		if(e.getValueIsAdjusting()) return;
		Exception exception = null;
		try{
			if(e.getSource().equals(partNamePanel.getTable().getSelectionModel())){

				PartName partName = partNameTableModel.getSelectedItem();
				loadPartSpecTable(partName);
				updateRequiredPartSpecTable(partName);
			} else if(e.getSource().equals(partSpecPanel.getTable().getSelectionModel())) {
				String  partName =  (partNameTableModel.getSelectedItem() == null) ? null :
					(partNameTableModel.getSelectedItem().getPartName());
				
				updateRequiredPartSpecTableByPartSpec(partName);
			}

		}catch(Exception ex) {
			exception = ex;
		}
		handleException(exception);
	}

	private List<LetInspectionProgram> getLetProgramNames() {
		List<LetInspectionProgram> letInspectionPrograms = null;
		try{
			letInspectionPrograms = ServiceFactory.getDao(LetInspectionProgramDao.class).findAll();
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getLetProgramNames";
			getLogger().error(e, msg);
			e.printStackTrace();
		}
		return letInspectionPrograms;
	}

	private List<LetInspectionParam> getLetParamNames() {
		List<LetInspectionParam>  letInspectionParams = null;
		try {
			letInspectionParams = ServiceFactory.getDao(LetInspectionParamDao.class).findAll();
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getLetParamNames";
			getLogger().error(e, msg);
			e.printStackTrace();
		}
		return letInspectionParams;
	}

	private List<LetRequiredPartSpecsDto> getLetRequiredPartSpecs() {
		List<LetRequiredPartSpecsDto> letRequiredPartSpecs = null;
		try{
			letRequiredPartSpecs = ServiceFactory.getDao(PartSpecDao.class).findAllWithLETRequired();
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getLetRequiredPartSpecs";
			getLogger().error(e, msg);
			e.printStackTrace();
		}
		return letRequiredPartSpecs;
	}

	private List<LetRequiredPartSpecsDto> getLETRequiredSpecsByPartName(PartName partName) {
		List<LetRequiredPartSpecsDto>  letRequiredPartSpecs = null;
		try{
			letRequiredPartSpecs = ServiceFactory.getDao(PartSpecDao.class).findByPartNameWithLETRequired(partName.getPartName());
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getLETRequiredSpecsByPartName";
			getLogger().error(e, msg);
			e.printStackTrace();
		}
		return letRequiredPartSpecs;
	}
	
	private List<LetRequiredPartSpecsDto> getLETRequiredSpecsByPartNameAndSpec(String partName, String partSpec) {
		List<LetRequiredPartSpecsDto>  letRequiredPartSpecs = null;
		try {
			letRequiredPartSpecs = ServiceFactory.getDao(PartSpecDao.class).findByPartNameWithLETRequiredByPartSpec(partName, partSpec);
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getLETRequiredSpecsByPartNameAndSpec";
			getLogger().error(e, msg);
			e.printStackTrace();
		}
		return letRequiredPartSpecs;
	}
	

	private List<PartName> getLETRequiredPartsByProductType(String productType) {
		List<PartName> partNames = null;
		try{
			partNames = ServiceFactory.getDao(PartNameDao.class).findAllByProductTypeAndLETReq(productType);
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getLETRequiredPartsByProductType";
			getLogger().error(e, msg);
			e.printStackTrace();
		}
		return partNames;
	}

	private int getProgramId(String programName) {
		LetInspectionProgram program = null;
		try{
			program = ServiceFactory.getDao(LetInspectionProgramDao.class).findPgmIdByName(programName);
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getProgramId";
			getLogger().error(e, msg);
			e.printStackTrace();
		}
		return program.getInspectionPgmId();
	}

	private int getparamId(String paramName) {
		LetInspectionParam param = null;
		try{
			param = ServiceFactory.getDao(LetInspectionParamDao.class).findParamIdByName(paramName);
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getparamId";
			getLogger().error(e, msg);
			e.printStackTrace();
		} 
		return param.getInspectionParamId();
	}

	private List<ExtRequiredPartSpecDto> getRequiredPartSpecsByProductTypeAndPartName(
			String selectedProductType, PartName partName) {
		List<ExtRequiredPartSpecDto> extRequiredPartSpecs = null;
		try{
			extRequiredPartSpecs = ServiceFactory.getDao(ExtRequiredPartSpecDao.class).
					findAllRequiredSpecsByProductTypeAndPartName(selectedProductType, partName.getPartName());
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getRequiredPartSpecsByProductTypeAndPartName";
			getLogger().error(e, msg);
			e.printStackTrace();
		}
		return extRequiredPartSpecs;
	}

	private boolean isLETParamNameSelected() {
		return letParamNamePanel.getTable().getSelectedRowCount() == 1;
	}

	private boolean isLETProgramNameSelected() {
		return letProgramNamePanel.getTable().getSelectedRowCount() == 1;
	}

	private boolean isPartSpecSelected() {
		return partSpecPanel.getTable().getSelectedRowCount() == 1;
	}

	private boolean isPartSelected() {
		return partNamePanel.getTable().getSelectedRowCount() == 1;
	}

	public void registerProcessPointSelectionPanel(ProductTypeSelectionPanel productTypeSelectionPanel) {
		// TODO Auto-generated method stub		
	}
}