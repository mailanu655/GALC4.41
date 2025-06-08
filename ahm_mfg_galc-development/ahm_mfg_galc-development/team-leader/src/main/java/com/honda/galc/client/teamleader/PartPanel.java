package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.client.teamleader.model.RepairPart;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.client.ui.event.UpdateEvent;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.MeasurementSpecDao;
import com.honda.galc.dao.product.PartDao;
import com.honda.galc.dao.product.PartLinkDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.dao.product.RepairProcessPointDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.PartNameVisibleType;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.MeasurementSpecId;
import com.honda.galc.entity.product.Part;
import com.honda.galc.entity.product.PartId;
import com.honda.galc.entity.product.PartLink;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartSpecId;
import com.honda.galc.entity.product.RepairProcessPoint;
import com.honda.galc.entity.product.RepairProcessPointId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.AuditLoggerUtil;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.SortedArrayList;
/**
 * 
 * <h3>PartPanel Class description</h3>
 * <p> PartPanel description </p>
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
 * <TD>YX</TD>
 * <TD>05.14.2014</TD>
 * <TD>0.1</TD>
 * <TD>SR30946</TD>
 * <TD>Add timestamp for any changes of the parts'lot control rules in part Panel</TD> 
 * </TR> 
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Jun 11, 2010
 *
 *
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 04, 2016
 * ver 2
 */
public class PartPanel extends TabbedPanel implements ListSelectionListener, TableModelListener, ActionListener{

	
	private static final long serialVersionUID = 1L;
	private final static String CREATE_PART="Create Part";
	private final static String DELETE_PART="Delete Part";
	private final static String SHOW_REFERENCES="References";
	private final static String CREATE_PART_SPEC="Create Part Spec";
	private final static String DELETE_PART_SPEC="Delete Part Spec";
	private final static String UPDATE_MEAS_COUNT="Update Measurement count";
	private final static String CREATE_MEASUREMENT_SPEC="Create Measurement Spec";
	private final static String DELETE_MEASUREMENT_SPEC="Delete Measurement Spec";
	private final static String DUPLICATE_MEASUREMENT_SPEC="Duplicate Measurement Spec";
	private final static String COPY_MEASUREMENT_SPEC="Copy Measurement Spec";
	private final static Double MEAS_MIN_LIMIT = 0.0;
	private final static Double MEAS_MAX_LIMIT = 9999.0;
	private final static String GENERATE_PART_SPEC="Generate Part Specs from BOM";
	private final static String BLANK = "";
		
	private TablePane partNamePanel = new TablePane("Part Name");
	private TablePane partSpecPanel = new TablePane("Part Spec");
	private TablePane measurementSpecPanel = new TablePane("Measurement Spec");
	
	private PartNameTableModel partNameTableModel;
	private PartSpecTableModel partSpecTableModel= new PartSpecTableModel(partSpecPanel.getTable(),null);
	private MeasurementSpecTableModel measurementSpecTableModel = new MeasurementSpecTableModel(measurementSpecPanel.getTable(),null);	
	private CommonTlPropertyBean propertyBean;
	MeasurementSpec measurementSpecSelected = new MeasurementSpec(); // Validation for maximum limit greater than minimum limit
	private PartSpecPanelDialog partSpecPanelDialog = null;
	private GeneratePartSpecPanelDialog generatePartSpecPanelDialog = null;
	
	private List<PartName> partNameList;
	private JTextField partFilterInput;
	
	
	public PartPanel() {
		super("Part", KeyEvent.VK_P);
		initComponents();
	}
	
	public PartPanel(TabbedMainWindow mainWindow) {
		super("Part", KeyEvent.VK_P,mainWindow);
		initComponents();
	}
	
	protected void initComponents() {
		
		setLayout(new BorderLayout());
		JSplitPane topSplitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
				               createLeftPanel(), createRightPanel());
		topSplitPanel.setDividerLocation(380);
		topSplitPanel.setContinuousLayout(true);


		add(topSplitPanel,BorderLayout.CENTER);
		addListeners();
		
		//BCC 10/11/11: Added to get new property SYNC206TABLE
		propertyBean = PropertyService.getPropertyBean(CommonTlPropertyBean.class, "Default_CommonProperties");

	}
	
	private void addListeners() {
		
		partNamePanel.addListSelectionListener(this);
		partNamePanel.addMouseListener(createPartNameMouseListener());
		partNamePanel.getTable().addMouseListener(createPartNameMouseListener());
		partFilterInput.getDocument().addDocumentListener(createPartNameFilterListener());
		
		partSpecPanel.addListSelectionListener(this);
		partSpecPanel.addMouseListener(createPartSpecMouseListener());
		partSpecPanel.getTable().addMouseListener(createPartSpecMouseListener());
		
		measurementSpecPanel.addListSelectionListener(this);
		measurementSpecPanel.addMouseListener(createMeasurementSpecMouseListener());
		measurementSpecPanel.getTable().addMouseListener(createMeasurementSpecMouseListener());
	}
	
	private JPanel createLeftPanel() {		
		JPanel leftPanel = new JPanel(new MigLayout("insets 0, gap 0", "[][]",""));
		leftPanel.add(partNamePanel, "height : max, width : max, wrap");
		leftPanel.add(createPartFilterPanel());		
		return leftPanel;		
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
	
	private JPanel createRightPanel() {
		JPanel rightPanel = new JPanel(new BorderLayout());
		
		CommonPartUtility.PartMaskFormat maskFormat = CommonPartUtility.PartMaskFormat.getFormat(PropertyService.getPartMaskWildcardFormat());		
		//bak - 2015-07-15 - Added new Part Mask Options Key	
		if (maskFormat == CommonPartUtility.PartMaskFormat.MSEXEL) {
			rightPanel.add(new JLabel("<html><body>Part Mask Wildcard Options - <br># (Single Digit), ? (Single Character), * (One or More Characters)</body></html>"), BorderLayout.NORTH);
		}
		else if (maskFormat == CommonPartUtility.PartMaskFormat.MSEXEL_ENHANCED) {
			rightPanel.add(new JLabel("<html><body>Part Mask Wildcard Options - <br>? (Single Letter), # (Single Digit), % (Single Character)" +
				", * (Zero or More Characters), \\? ( ? ), \\#  ( # ), \\%  ( % ), \\*  ( * ), \\\\  ( \\ ), &lt;&lt; % ; n &gt;&gt; (n Characters)"+
					"<br> &lt;&lt;PRODUCT&gt;&gt; (Product Id), &lt;&lt;MODEL&gt;&gt; (Model Year + Model Code), " +
				"<br> &lt;&lt;MODELCODE&gt;&gt;, &lt;&lt;MODELYEAR&gt;&gt;, &lt;&lt;MODELTYPE&gt;&gt;, &lt;&lt;MODELOPTION&gt;&gt;, &lt;&lt;INTCOLOR&gt;&gt;, &lt;&lt;EXTCOLOR&gt;&gt;,"+
					" &lt;&lt;INTCOLORCODE|A;B&gt;&gt; (Interior ColorCode A or B)</body></html>"), BorderLayout.NORTH);	
		}
		else {
			rightPanel.add(new JLabel("<html><body>Part Mask Wildcard Options - <br># (Single Character), * (One or More Characters)</body></html>"), BorderLayout.NORTH);
		}
		
		partSpecPanel.getTable().setRowHeight(23);
		JSplitPane rightSplitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
				partSpecPanel, measurementSpecPanel);
		rightSplitPanel.setDividerLocation(380);
		rightSplitPanel.setContinuousLayout(true);
		add(rightSplitPanel,BorderLayout.CENTER);			
		rightPanel.add(rightSplitPanel);
		
		return rightPanel;
		
	}
	
	private MouseListener createPartNameMouseListener(){
		 return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showPartNamePopupMenu(e);
			}
		 });
	}
	
	private MouseListener createPartSpecMouseListener(){
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showPartSpecPopupMenu(e);
			}
		 }); 
	}
	
	public MouseListener createMeasurementSpecMouseListener(){
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showMeasurementSpecPopupMenu(e);
			}
		 }); 
	}
	
	private DocumentListener createPartNameFilterListener() {
		DocumentListener listener = new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {  
				onPartFilterChange();
			}
			public void removeUpdate(DocumentEvent e) { 
				onPartFilterChange(); 
			}
			public void changedUpdate(DocumentEvent e) { 
				onPartFilterChange();
			}
		};
		return listener;
	} 
	
	private void onPartFilterChange(){
		partNamePanel.clearSelection();
		triggerfilterParts();
	}
	
	private void showPartNamePopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(CREATE_PART,true));
		PartName partName = partNameTableModel.getSelectedItem();
		String name = partName == null ? "" : " \"" + partName.getPartName() + "\"";
		popupMenu.add(createMenuItem(DELETE_PART + name,isPartNameSelected()));
		popupMenu.add(createMenuItem(SHOW_REFERENCES,true));
		if(propertyBean.getGeneratePartSpec()) popupMenu.add(createMenuItem(GENERATE_PART_SPEC,true));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	private void showPartSpecPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(CREATE_PART_SPEC,isPartNameSelected()));
		PartSpec partSpec = partSpecTableModel.getSelectedItem();
		String name = partSpec == null ? "" : " \"" + partSpec.getId().getPartId() + "\"";
		popupMenu.add(createMenuItem(DELETE_PART_SPEC + name,isPartSpecSelected()));
		popupMenu.add(createMenuItem(COPY_MEASUREMENT_SPEC ,(isPartSpecSelected() && partSpec.getMeasurementCount() > 0)));
		popupMenu.add(createMenuItem(UPDATE_MEAS_COUNT ,(isPartSpecSelected() && partSpec.getMeasurementCount() == 0 
				&& measurementSpecTableModel.getRowCount() ==0) ));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
	
	private void showMeasurementSpecPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(CREATE_MEASUREMENT_SPEC,isPartSpecSelected()));
		MeasurementSpec mSpec = measurementSpecTableModel.getSelectedItem();
		String name = mSpec == null ? "" : " #" + mSpec.getId().getMeasurementSeqNum();
		popupMenu.add(createMenuItem(DELETE_MEASUREMENT_SPEC + name,isMeasurementSpecSelected()));
		popupMenu.add(createMenuItem(DUPLICATE_MEASUREMENT_SPEC + name, isMeasurementSpecSelected()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
	
	private boolean isPartNameSelected() {
		return partNamePanel.getTable().getSelectedRowCount() == 1;
	}
	
	private boolean isPartSpecSelected() {
		return partSpecPanel.getTable().getSelectedRowCount() == 1;
	}
	
	private boolean isMeasurementSpecSelected() {
		return measurementSpecPanel.getTable().getSelectedRowCount() == 1;
	}
	
	@Override
	public void onTabSelected() {
		if(!isInitialized) {
			partNameList = getPartNameListFromDB();
			partNameTableModel = new PartNameTableModel(partNamePanel.getTable(),isPartConfirmRequired(), partNameList,getApplicationProductTypeName());
			partNameTableModel.addTableModelListener(this);
			partNameTableModel.pack();
			isInitialized = true;
		}
	}
	
	private List<PartName> getPartNameListFromDB() {
		Exception ex = null;
		List<PartName> partNameList = new ArrayList<PartName>();
		try{
			partNameList = ServiceFactory.getDao(PartNameDao.class).findAllByProductType(getApplicationProductTypeName());
		}catch(Exception e) {
			ex = e;
		}
		handleException(ex);
		return new SortedArrayList<PartName>(partNameList,"getPartName");
	}
	
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}
		
		if(e.getSource() ==(partNamePanel.getTable().getSelectionModel())){
			// part name selected
			PartName partName = partNameTableModel.getSelectedItem();
			List<PartSpec> partSpecList = (partName == null) ? new ArrayList<PartSpec>() : partName.getAllPartSpecs();
			partSpecTableModel = new PartSpecTableModel(partSpecPanel.getTable(),partSpecList);
			partSpecTableModel.pack();
			partSpecTableModel.addTableModelListener(this);

		}else if(e.getSource() ==(partSpecPanel.getTable().getSelectionModel())){
			// part spec selected
			PartSpec partSpec = partSpecTableModel.getSelectedItem();
			List<MeasurementSpec> measurementSpecs = partSpec == null ? new ArrayList<MeasurementSpec>() : partSpec.getMeasurementSpecs();
			measurementSpecTableModel = new MeasurementSpecTableModel(measurementSpecPanel.getTable(),measurementSpecs);
			measurementSpecTableModel.pack();
			measurementSpecTableModel.addTableModelListener(this);
			
		}
		// Store previous minimum and maximum value to update if validation fails
		else if (e.getSource() == (measurementSpecPanel.getTable()
				.getSelectionModel())) {
			if (measurementSpecTableModel.getSelectedItem() != null) {
				measurementSpecSelected.setMinimumLimit(measurementSpecTableModel
						.getSelectedItem().getMinimumLimit());
				measurementSpecSelected.setMaximumLimit(measurementSpecTableModel
						.getSelectedItem().getMaximumLimit());
			}
		}
	}

	public void tableChanged(TableModelEvent e) {
		if (e.getSource() == null)
			return;
		
		if(e.getSource() instanceof PartNameTableModel) {
			handlePartNameTableChanged(e, (PartNameTableModel)e.getSource());
		}else if(e.getSource() instanceof PartSpecTableModel) {
			handlePartSpecTableChanged(e, (PartSpecTableModel)e.getSource());
		}else if(e.getSource() instanceof MeasurementSpecTableModel) {
			handleMeasurementSpecTableChanged(e, (MeasurementSpecTableModel)e.getSource());			
		}
	}

	private void handlePartNameTableChanged(TableModelEvent e, PartNameTableModel model) {
		try {
			final PartName partName = model.getSelectedItem();
			if(partName == null) return;
			String oldPartId  = model.getSelectedItem().getId();
			if(e.getType()==TableModelEvent.UPDATE){
				if((e.getColumn() == 4|| e.getColumn()==5 || e.getColumn()==6) && !confirmUpdate(e, model)) return; //pop-up for Part Confirm Only
				PartName oldPartName = ServiceFactory.getDao(PartNameDao.class).findByKey(oldPartId);
				ServiceFactory.getDao(PartNameDao.class).update(partName);
				logUserAction(UPDATED, partName);
				AuditLoggerUtil.logAuditInfo( oldPartName,partName,"update", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
				
				if(e.getColumn()==6) updateLotControlRules(partName);
				//SR30946 refresh Part Name Panel when deleting its part spec
				SwingUtilities.invokeLater(new Runnable(){
					public void run() {
						updatePartNameItemOnList(getPartNameFromDB(partName.getPartName()));
					}
				});
			}
		} catch(Exception ex) {
			if(model!=null)
				model.rollback();
			handleException(ex);
		}
		return;
	}

	private void updateLotControlRules(PartName partName) {
		LotControlRuleDao lotControlRuleDao = ServiceFactory.getDao(LotControlRuleDao.class);
		List<LotControlRule> rules = lotControlRuleDao.findAllByPartName(partName);
		List<LotControlRule> updatedRules = new ArrayList<LotControlRule>();
		
		for(LotControlRule rule:rules){
			if(rule.getPartConfirmFlag() != partName.getPartConfirmCheck()){
				rule.setPartConfirmFlag(partName.getPartConfirmCheck());
				rule.setUpdateUser(getUserName());
				updatedRules.add(rule);
			}
		}
		
		if(updatedRules.size() > 0) {
			lotControlRuleDao.saveAll(updatedRules);
			logUserAction(UPDATED, updatedRules);
			if(rules.size()==updatedRules.size()) {
				int length = rules.size();
	            for(int i =0;i<length;i++) {
	            	AuditLoggerUtil.logAuditInfo(rules.get(i) ,updatedRules.get(i),"update", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
	           }
			}
		}
	}

	private void handleMeasurementSpecTableChanged(TableModelEvent e, MeasurementSpecTableModel model) {
		try {
			MeasurementSpec measurementSpec = model.getSelectedItem();
			if(measurementSpec == null) return;
			// Validation for maximum limit greater than minimum limit
			if (measurementSpec.getMaximumLimit() <= measurementSpec
					.getMinimumLimit()) {
				MessageDialog
						.showError("Maximum Limit should be greater than Minimum Limit");
				measurementSpec.setMaximumLimit(measurementSpecSelected
						.getMaximumLimit());
				measurementSpec.setMinimumLimit(measurementSpecSelected
						.getMinimumLimit());
				return;
			}
		
			if(e.getType() == TableModelEvent.UPDATE){
				MeasurementSpec oldMeasurementSpec = 	ServiceFactory.getDao(MeasurementSpecDao.class).findByKey(measurementSpec.getId());
				ServiceFactory.getDao(MeasurementSpecDao.class).update(measurementSpec);
				logUserAction(UPDATED, measurementSpec);
				AuditLoggerUtil.logAuditInfo(oldMeasurementSpec,measurementSpec,"update", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");

				if(propertyBean.getSync206Table()){
					sync206Measurements(measurementSpec, model);					
				}
				
				//SR30946 refresh Part Name On List when its measurement changed
				updatePartNameItemOnList(getPartNameFromDB(measurementSpec.getId().getPartName()));
			}
		} catch(Exception ex) {
			if(model!=null) {
				model.rollback();
				getLogger().error("Rolled back: " + model.toString());
			}
			handleException(ex);
		}
		return;
	}

	private void handlePartSpecTableChanged(TableModelEvent e, PartSpecTableModel model) {
		try {
			PartSpec partSpec = model.getSelectedItem();
			if(partSpec == null) return;
			PartSpecId oldId = model.getSelectedItem().getId();
			//BCC 9/29/11 convert partSpec to part to update gal206tbx
			//This section needs to be put into another method
			PartSpecId partSpecId = partSpec.getId();
			String partId = partSpecId.getPartId();
			String partName = partSpecId.getPartName();
			PartId partID = new PartId();
			partID.setPartId(partId);
			partID.setPartName(partName);
			Part part = new Part();
			part.setId(partID);	
		
			if(e.getType()==TableModelEvent.UPDATE) {
				String partSerialNumberMask = partSpec.getPartSerialNumberMask().toUpperCase();
				partSpec.setPartSerialNumberMask(StringUtils.isNotEmpty(partSerialNumberMask)?parsePartSerialNumberMask(partSerialNumberMask):partSerialNumberMask);
				PartSpec oldPartSpec = ServiceFactory.getDao(PartSpecDao.class).findByKey(oldId);
				ServiceFactory.getDao(PartSpecDao.class).update(partSpec);
				logUserAction(UPDATED, partSpec);
				AuditLoggerUtil.logAuditInfo(oldPartSpec,partSpec,"update", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");

				
				//BCC 9/30/11: Map PartSpec data to Part and update Part 
				part.setPartDescription(partSpec.getPartDescription());
				if(propertyBean.getSync206Table()){
					part.setPartSerialNumberMask(CommonPartUtility.parsePartMask(partSerialNumberMask));
					part.setMeasurementCount(partSpec.getMeasurementCount());
					
				// START: SR#30768: Update Measurements in GAL206TBX table (5/12/2014)
				 	int i =1;
					for (MeasurementSpec limit : partSpec.getMeasurementSpecs()) {
						
						switch(i) {
							case 1: 
								part.setMaximumValue1(limit.getMaximumLimit());
								part.setMinimumValue1(limit.getMinimumLimit()); 
								break;
							case 2:
								part.setMaximumValue2(limit.getMaximumLimit());
								part.setMinimumValue2(limit.getMinimumLimit()); 
								break;
							case 3:
								part.setMaximumValue3(limit.getMaximumLimit());
								part.setMinimumValue3(limit.getMinimumLimit()); 
								break;
							case 4:
								part.setMaximumValue4(limit.getMaximumLimit());
								part.setMinimumValue4(limit.getMinimumLimit()); 
								break;
							case 5:
								part.setMaximumValue5(limit.getMaximumLimit());
								part.setMinimumValue5(limit.getMinimumLimit()); 
								break;
							case 6:
								part.setMaximumValue6(limit.getMaximumLimit());
								part.setMinimumValue6(limit.getMinimumLimit()); 
								break;
							case 7:
								part.setMaximumValue7(limit.getMaximumLimit());
								part.setMinimumValue7(limit.getMinimumLimit()); 
								break;
							case 8:
								part.setMaximumValue8(limit.getMaximumLimit());
								part.setMinimumValue8(limit.getMinimumLimit()); 
								break;
							case 9:
								part.setMaximumValue9(limit.getMaximumLimit());
								part.setMinimumValue9(limit.getMinimumLimit()); 
								break;
							case 10:
								part.setMaximumValue10(limit.getMaximumLimit());
								part.setMinimumValue10(limit.getMinimumLimit()); 
								break;
							default:
								break;
							}
						i++;
					}
					// END: SR#30768
					
					part.setAllowDuplicates(0);
					List<LotControlRule> rules = ServiceFactory.getDao(LotControlRuleDao.class).findRulesByPartNameAndUniqueFlag(partName, 1);
					if(null != rules && rules.size() > 0) {
						part.setAllowDuplicates(1);
					}
					part.setCreateTimestamp(part.getCreateTimestamp());
					part.setEntryTimestamp(new Timestamp(System.currentTimeMillis()));
					ServiceFactory.getDao(PartDao.class).update(part);
					logUserAction(UPDATED, part);
				}
				
			}
		} catch(Exception ex) {
			if(model!=null) {
				model.rollback();
				getLogger().error("Rolled back: " + model.toString());
			}
			handleException(ex);
		}
		return;
	}

	private boolean confirmUpdate(TableModelEvent e, BaseTableModel<?> model) {
		int index = model.getItems().indexOf((model.getSelectedItem()));
		Object newValue = model.getValueAt(index, e.getColumn());
		
		//generate confirmation message string
		StringBuilder msg = new StringBuilder("Please confirm change ");
  	    if(newValue  instanceof Boolean){
  	    	 msg.append(model.getColumnName(e.getColumn())).append(" to ").append(((Boolean)newValue) ? "\"checked\"." : "\"un-checked\".");
  	    	if(e.getColumn() == 6){
  	    		msg.append("\n"+model.getColumnName(e.getColumn())).append(" flag for all Lot Control Rules with selected part would also be changed to ").append(((Boolean)newValue) ? "\"checked\"." : "\"un-checked\".");
  	    	}
  	    }
		else
			msg.append(model.getColumnName(e.getColumn())).append("\" from \"").append(model.getCurrentValue()).append("\" to \"")
			.append(model.getValueAt(index, e.getColumn())).append("\"?");   
		
		boolean confirm = MessageDialog.confirm(this, msg.toString(), true);
		
		if(!confirm){
			if(model != null) model.rollback();
		}
		
		return confirm;
	}

	public void actionPerformed(ActionEvent e) {
		Exception exception = null;
		try{
			if(e.getSource() instanceof JMenuItem){
				JMenuItem menuItem = (JMenuItem)e.getSource();
				logUserAction("selected menu item: " + menuItem.getName());
				if(menuItem.getName().startsWith(CREATE_MEASUREMENT_SPEC)) createMeasurementSpec();
				else if(menuItem.getName().startsWith(DELETE_MEASUREMENT_SPEC)) deleteMeasurementSpec();
				else if(menuItem.getName().startsWith(DUPLICATE_MEASUREMENT_SPEC)) duplicateMeasurementSpec();
				else if(menuItem.getName().startsWith(CREATE_PART_SPEC)) createPartSpec();
				else if(menuItem.getName().startsWith(DELETE_PART_SPEC)) deletePartSpec();
				else if(menuItem.getName().startsWith(UPDATE_MEAS_COUNT)) updateMeasurementCount();
				else if(menuItem.getName().startsWith(CREATE_PART)) createPart();
				else if(menuItem.getName().startsWith(DELETE_PART)) deletePart();
				else if(menuItem.getName().startsWith(SHOW_REFERENCES)) showReferences();
				else if(menuItem.getName().startsWith(COPY_MEASUREMENT_SPEC)) copyMeasurementSpec();
				else if(menuItem.getName().startsWith(GENERATE_PART_SPEC)) generatePartSpec();
				
			}
			else if (partSpecPanelDialog!= null && e.getSource() == partSpecPanelDialog.getCancelButton()) {
				partSpecPanelDialog.dispose();
			} else if (partSpecPanelDialog!= null && e.getSource() == partSpecPanelDialog.getSaveButton()) {
				partSpecPanelDialog.copyMeasurementSpecs();
				partSpecPanelDialog.dispose();
				
				PartName partNameFromDB = getPartNameFromDB(partNameTableModel.getSelectedItem().getId());
				updatePartNameItemOnList(partNameFromDB);
				//refresh part name data on table panel
				partNameTableModel.selectItem(partNameFromDB);
			}else if (generatePartSpecPanelDialog!= null && e.getSource() == generatePartSpecPanelDialog.getCancelButton()) {
				generatePartSpecPanelDialog.dispose();
			} else if (generatePartSpecPanelDialog!= null && e.getSource() == generatePartSpecPanelDialog.getSaveButton()) {
				generatePartSpecPanelDialog.createPartSpecsFromBom();
				generatePartSpecPanelDialog.dispose();
				refreshPartSpecs();
			}
		}catch(Exception ex){
			exception = ex;
		}
		handleException(exception);
	}

	
	private void generatePartSpec() {
		String plantCode = propertyBean.getPlantCode();
		PartName partName = partNameTableModel.getSelectedItem();
		if(partName == null) return;
		generatePartSpecPanelDialog = new GeneratePartSpecPanelDialog(this.getMainWindow(), partName.getPartName(), plantCode, partName.getAllPartSpecs());
		generatePartSpecPanelDialog.getCancelButton().addActionListener(this);
		generatePartSpecPanelDialog.getSaveButton().addActionListener(this);
		generatePartSpecPanelDialog.setLocationRelativeTo(this);
		generatePartSpecPanelDialog.setVisible(true);
	}

	private void showReferences() { 
		PartName partName = partNameTableModel.getSelectedItem();
		if(partName == null) return;

		String processPointNames = getProcessPointsReferencingPart(partName);
	    if(processPointNames.length() > 0 ) {
			MessageDialog.showInfo(this,String.format("This part is referenced by Lot Control Rules for process point(s) %s.",processPointNames));
	    } else {
			MessageDialog.showInfo(this,"This part is not referenced by any Lot Control Rules");
	    }
    }
	
	private void deletePart() {
		PartName partName = partNameTableModel.getSelectedItem();
		if(partName == null) return;
		if(partSpecTableModel.getRowCount() > 0) {
			MessageDialog.showError(this,"This part has Part specs. Please delete part specs first!" );
			return;
		}
		
		String processPointNames = getProcessPointsReferencingPart(partName);
	    if(processPointNames.length() > 0 ) {
			MessageDialog.showError(this,String.format("This part is in use by Lot Control Rules for the process point(s) %s, please delete them first!",processPointNames));
			return;
	    }
		
		List<String> repairProcessPoints = ServiceFactory.getDao(RepairProcessPointDao.class).findRepairProcessPointForPartName(partName.getPartName());
		if (repairProcessPoints != null && !repairProcessPoints.isEmpty()) {
			MessageDialog.showError(this,String.format("This part is in use by Repair Process Points for the process point(s) %s, please delete them first!",repairProcessPoints));
			return;
		}
	    
		List<PartLink> linkedParts = ServiceFactory.getDao(PartLinkDao.class).findAllByParentPartName(partName.getPartName());
		if (linkedParts.size() > 0) {
			MessageDialog.showError(this, "This part has linked child parts. Please delete the child parts first!");
			return;
		}
		
		if(!MessageDialog.confirm(this, "Are you sure that you want to delete this part ?")) 
			return;
		
		ServiceFactory.getDao(PartNameDao.class).remove(partName);
		logUserAction(REMOVED, partName);
		AuditLoggerUtil.logAuditInfo( partName,null,"delete", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");

		
		partNameTableModel.remove(partName);
		partNameList.remove(partName);
		
		EventBus.publish(new UpdateEvent(this,UpdateEvent.DELETE_PART_NAME));
		
	}
	/*
	 * BCC 9/28/11 Added JOptionPane to turn on/off AF Confirm
	 * Added partName.setPartConfirmCheck
	 */
	private void createPart() {
		
		PartName partName = new PartName();

		String name = MessageDialog.showInputDialog("Input","Input Part Name : ",255,true);
		
		if (StringUtils.isNotEmpty(name)) {
			
			PartName part = ServiceFactory.getDao(PartNameDao.class).findByKey(name.trim());
			
			if(null != part && StringUtils.isNotEmpty(part.getPartName())) {
				String productTypeMsg = "";
				if(part.getProductTypeName() != null && part.getProductTypeName().trim() != "") {
					productTypeMsg = " For Product Type: " + part.getProductTypeName().trim();
				}
				MessageDialog.showError(this, "Part name \"" + name	+ "\" exists" + productTypeMsg + ". Please Input another name!");
				return;
			}
			
			partName.setPartName(name);
			partName.setWindowLabel(name);

			
			int repairCheckFlag = 0;
			boolean repairCheckConfirm = MessageDialog.confirm(this, "Is Repair Check required for the part: " + name +"?");
			repairCheckFlag = repairCheckConfirm ? 1 : 0;
			partName.setRepairCheck(repairCheckFlag);
			
			int partConfirmFlag = 0;

			if(isPartConfirmRequired()) {
				boolean yesNo = MessageDialog.confirm(this, "Is part " + name + " required to check?");
				partConfirmFlag = yesNo ? 1 : 0;
			}

			partName.setPartConfirmCheck(partConfirmFlag);
			int partVisible=0;		
			partVisible=selectPartVisibleType();
			partName.setPartVisible(partVisible);
			partName.setProductTypeName(getApplicationProductTypeName());
			PartName partNameFromDB = ServiceFactory.getDao(PartNameDao.class).save(partName);
			logUserAction(SAVED, partName);
			AuditLoggerUtil.logAuditInfo( null,partName,"save", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
		
			partNameList.add(partNameFromDB);
			
			//order list with new element
			partNameList = new SortedArrayList<PartName>(partNameList,"getPartName");
			partNameTableModel.refresh(partNameList);			
			partFilterInput.setText("");
			partNameTableModel.selectItem(partNameFromDB);			
			
			EventBus.publish(new UpdateEvent(this, UpdateEvent.CREATE_PART_NAME));
		}
	}
	
	private int selectPartVisibleType()
	{
		JComboBox partVisibleComboBox = new JComboBox();
		partVisibleComboBox.setEditable(false);
		partVisibleComboBox.setModel(new DefaultComboBoxModel(PartNameVisibleType.values()));
		JOptionPane.showConfirmDialog(this, partVisibleComboBox, "Please select Part Visible Type below:", JOptionPane.OK_CANCEL_OPTION);
		return ((PartNameVisibleType)partVisibleComboBox.getSelectedItem()).getId();
	}

	private void deletePartSpec() {
		PartSpec partSpec = partSpecTableModel.getSelectedItem();		
		if(partSpec == null) return;
		if(partSpec.getMeasurementSpecs().size() > 0) {
			MessageDialog.showInfo(partSpecPanel, "Please remove measurement specs first!");
			return;
		}
		//BCC 9/29/11 convert partSpec to part to remove from gal206tbx
		PartSpecId partSpecId = partSpec.getId();
		String partId = partSpecId.getPartId();
		String partName = partSpecId.getPartName();
		PartId partID = new PartId();
		partID.setPartId(partId);
		partID.setPartName(partName);
		Part part = new Part();
		part.setId(partID);
		
		PartName partNameItem = new PartName();
		partNameItem.setPartName(partName);
		
		String processPointNames = getProcessPointsReferencingPartSpec(partSpec);
	    if(processPointNames.length() > 0 ) {
			MessageDialog.showError(this,String.format("This part spec is in use by Lot Control Rules for the process point(s) %s, please delete them first!",processPointNames));
			return;
	    }
	    
	    String frameProductWithInstalledParts = ServiceFactory.getDao(FrameDao.class).findFirstByInstalledPart(partName.trim(),partId.trim());      
        if(StringUtils.isNotEmpty(frameProductWithInstalledParts)) {
           MessageDialog.showError(this,"This part spec has unshipped products, these products need shipped before part spec can be deleted! ");
            return;
        }
		
		if(!MessageDialog.confirm(this, "Are you sure that you want to delete this part spec?"))
				return;
		ServiceFactory.getDao(PartSpecDao.class).remove(partSpec);
		logUserAction(REMOVED, partSpec);
		AuditLoggerUtil.logAuditInfo( partSpec,null,"delete", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
		
		removeRepairPart(partSpec);
		
		partSpecTableModel.remove(partSpec);
		if (part != null) {
			if(propertyBean.getSync206Table())
			ServiceFactory.getDao(PartDao.class).remove(part);
			logUserAction(REMOVED, part);
			AuditLoggerUtil.logAuditInfo( part,null,"delete", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
		}		
		
		EventBus.publish(new UpdateEvent(this,UpdateEvent.UPDATE_PART));
		//SR30946 refresh Part Name Panel when deleting its part spec
		updatePartNameItemOnList(getPartNameFromDB(partName));
	}
	private void updateMeasurementCount() {
		PartSpec partSpec = partSpecTableModel.getSelectedItem();		
		if(partSpec == null) return;
		if(partSpec.getMeasurementSpecs().size() > 0) {
			MessageDialog.showInfo(partSpecPanel, "Measurement spec have entries!");
			return;
		}
		String processPointNames = getProcessPointsReferencingPartSpec(partSpec);
	    if(processPointNames.length() > 0 ) {
			MessageDialog.showError(this,String.format("This part spec is in use by Lot Control Rules for the process point(s) %s, please delete them first!",processPointNames));
			return;
	    }
	    PartSpecId oldId = partSpecTableModel.getSelectedItem().getId();
		String measInput =JOptionPane.showInputDialog(this, "Enter new measurement count");
		if(StringUtils.isEmpty(measInput)) return;
		int count = Integer.parseInt(measInput);
		if (count !=partSpec.getMeasurementCount()){
			if(!MessageDialog.confirm(this, "Are you sure that you want to update measurement count?"))
				return;
			partSpec.setMeasurementCount(count);
		} else {
			MessageDialog.showInfo(partSpecPanel, "Measurement count same as existing!");
			return;
		}
		
		PartSpec oldPartSpec = ServiceFactory.getDao(PartSpecDao.class).findByKey(oldId);
		ServiceFactory.getDao(PartSpecDao.class).update(partSpec);
		logUserAction(UPDATED, partSpec);
		AuditLoggerUtil.logAuditInfo(oldPartSpec,partSpec,"update", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
		
		EventBus.publish(new UpdateEvent(this,UpdateEvent.UPDATE_PART));
		//SR30946 refresh Part Name Panel when deleting its part spec
		updatePartNameItemOnList(getPartNameFromDB(partSpec.getId().getPartName()));
	}

	private String getProcessPointsReferencingPartSpec(PartSpec partSpec) {
		  LotControlRuleDao lotControlRuleDao =  ServiceFactory.getDao(LotControlRuleDao.class);
		  ProcessPointDao processPointRuleDao =  ServiceFactory.getDao(ProcessPointDao.class);
		   
	       List<LotControlRule> lotControlRuleList = lotControlRuleDao.findAllByPartName(partSpec.getId().getPartName());
	       
	       if (lotControlRuleList == null) {
	    	   return "";
	       }
	       
	       List<LotControlRule> refRules = new ArrayList<LotControlRule>();
	       for (LotControlRule lcr : lotControlRuleList) {
	    	   for(PartSpec ps : lcr.getParts()){
	    		   if(ps.getId().equals(partSpec.getId()))
	    			   refRules.add(lcr);	    				   
	    	   }
	       }
	    	
	       //generate reference process points message
	       StringBuilder sb = new StringBuilder();
	       for(LotControlRule r : refRules){
	    	   ProcessPoint processPoint = processPointRuleDao.findByKey(r.getId().getProcessPointId());
	    	  if(!sb.toString().contains(r.getId().getProcessPointId()))
	    	   {
	    		   sb.append(String.format("\n%s (%s)", (processPoint == null ? "" : processPoint.getProcessPointName()), r.getId().getProcessPointId()));
	    	   }
	       }
	       
	       return sb.toString();
	}

	private void createPartSpec() {
		PartName partName= partNameTableModel.getSelectedItem();
		if(partName == null) return;
		
		PartSpecId id = new PartSpecId();
		id.setPartName(partName.getPartName());
		id.setPartId(getNextPartId());
		PartSpec partSpec = new PartSpec();
		partSpec.setId(id);
		partSpec.setPartSerialNumberMask(BLANK);
		PartSpec insertedSpec = ServiceFactory.getDao(PartSpecDao.class).insert(partSpec);
		logUserAction(INSERTED, partSpec);
		AuditLoggerUtil.logAuditInfo( null,partSpec,"save", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
		partSpecTableModel.add(insertedSpec);
		
		createRepairPart(insertedSpec);

		if(propertyBean.getSync206Table()){
			PartId partId = new PartId();
			partId.setPartName(partName.getPartName());
			partId.setPartId(id.getPartId());
			//BCC 10/24/11: If part is already in 206 table, no need to recreate it. 
			try{
				ServiceFactory.getDao(PartDao.class).findByKey(partId).toString();
			}
			catch (Exception ex) {
				Part part = new Part();
				part.setId(partId);
				part.setAllowDuplicates(0); // This is now setup in Lot Control Rules
				part.setMeasurementCount(partSpec.getMeasurementCount());
				part.setEntryTimestamp(new Timestamp(System.currentTimeMillis()));
				part.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
				ServiceFactory.getDao(PartDao.class).insert(part);
				logUserAction(INSERTED, part);
				AuditLoggerUtil.logAuditInfo( null,part,"save", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");

			}
		}
		
		PartName partNameObj = getPartNameFromDB(partName.getPartName());
		updatePartNameItemOnList(partNameObj);
		partNameTableModel.selectItem(partNameObj);
		partSpecTableModel.selectItem(partSpec);
		
		EventBus.publish(new UpdateEvent(this,UpdateEvent.UPDATE_PART));
	}
	
	private String getNextPartId() {
		String partId = null;
		
		for(PartSpec partSpec :partSpecTableModel.getItems()){
			if(partId == null){
				partId = partSpec.getId().getPartId();
				if(partId.substring(1, 2).equalsIgnoreCase("~")){
					partId= null;
				}
			}
			else if(!partSpec.getId().getPartId().substring(1, 2).equalsIgnoreCase("~") && partSpec.getId().getPartId().compareTo(partId) > 0)
				partId = partSpec.getId().getPartId();
		}
		
		int num = 0;
		if(partId == null) partId = "A0000";
		else{
				num = Integer.parseInt(partId.substring(1,5)) + 1;
			}
		
		return partId.substring(0,1) + new DecimalFormat("0000").format(num);
	}
	
	private int getNextSequenceNumber() {
		int num = 0;
		for(MeasurementSpec spec : measurementSpecTableModel.getItems()) {
			if(spec.getId().getMeasurementSeqNum() > num)
				num = spec.getId().getMeasurementSeqNum();
		}
		return num + 1;
	}

	@SuppressWarnings("unchecked")
	private void deleteMeasurementSpec() {
		MeasurementSpec spec = measurementSpecTableModel.getSelectedItem();
		PartSpec partSpec = partSpecTableModel.getSelectedItem();
		if(spec == null) return;
		if (partSpec.getMeasurementCount() > 1 && partSpec.getMeasurementSpecs()
				.get(partSpec.getMeasurementSpecs().size() - 1).getId().getMeasurementSeqNum() != spec.getId().getMeasurementSeqNum()) {
			MessageDialog.showError("You can delete the measurement spec only in decreasing order");
			return;
		}
		if(!MessageDialog.confirm(this, "Are you sure that you want to delete this measurement spec?"))
				return;
		List<LotControlRule> lotControlRules = ServiceFactory.getDao(LotControlRuleDao.class).findAllByPartId(partSpec.getId().getPartId(),partSpec.getId().getPartName());
		//If the Measurement is already assigned to a ProcessPoint in a lot control rule a message will be shown to the associate.
		if (lotControlRules!= null && lotControlRules.size()>0) {
			Set<String> uniquePpIds = new HashSet<String>();
			for (LotControlRule lcr : lotControlRules) {
				uniquePpIds.add(lcr.getId().getProcessPointId());
			}
			StringBuilder sb = new StringBuilder("This Measurement Spec is in use by Part Spec for the process point(s):\n");
			for (String processPoint : uniquePpIds) {
				sb.append("- ").append(ServiceFactory.getDao(ProcessPointDao.class).findById(processPoint).getProcessPointName()).append(" (").append(processPoint).append(")\n");
			}
			sb.append("Please delete the conflicted lot control rules first.");
			MessageDialog.showError(sb.toString());
			return;
		}
		ServiceFactory.getDao(MeasurementSpecDao.class).remove(spec);
		logUserAction(REMOVED, spec);
		AuditLoggerUtil.logAuditInfo( spec,null,"delete", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
		measurementSpecTableModel.remove(spec);
		partSpec.getMeasurementSpecs().remove(spec);
		
		updatePartSpecMeasCount();
		
		//BCC 10/3/11
		if(propertyBean.getSync206Table()){
			BaseTableModel model = (BaseTableModel) measurementSpecTableModel;
			sync206Measurements(spec, model);
		}
		refreshPartSpecs();
	}
	
	private void duplicateMeasurementSpec() {
		MeasurementSpec spec = measurementSpecTableModel.getSelectedItem();
		PartSpec partSpec = partSpecTableModel.getSelectedItem(); 
		
		if(spec == null) return;
		List<LotControlRule> lotControlRules = ServiceFactory.getDao(LotControlRuleDao.class).findAllByPartId(partSpec.getId().getPartId(),partSpec.getId().getPartName());
		//If the Measurement is already assigned to a ProcessPoint in a lot control rule a message will be shown to the associate
		//Confirm with J.G to keep the same logic 
		if (lotControlRules!= null && lotControlRules.size()>0) {
			Set<String> uniquePpIds = new HashSet<String>();
			for (LotControlRule lcr : lotControlRules) {
				uniquePpIds.add(lcr.getId().getProcessPointId());
			}
			StringBuilder sb = new StringBuilder("This Part Spec is in use by process point(s):\n");
			for (String processPoint : uniquePpIds) {
				sb.append("- ").append(ServiceFactory.getDao(ProcessPointDao.class).findById(processPoint).getProcessPointName()).append(" (").append(processPoint).append(")\n");
			}
			sb.append("Please delete the conflicted lot control rules first.");
			MessageDialog.showError(sb.toString());
			return;
		}
		MeasurementSpecId id = new MeasurementSpecId();
		id.setPartName(spec.getId().getPartName());
		id.setPartId(spec.getId().getPartId());
		id.setMeasurementSeqNum(getNextSequenceNumber());
		MeasurementSpec newSpec = new MeasurementSpec();
		newSpec.setId(id);
		newSpec.setMinimumLimit(spec.getMinimumLimit());
		newSpec.setMaximumLimit(spec.getMaximumLimit());
		newSpec.setMaxAttempts(spec.getMaxAttempts());
		MeasurementSpec insertedSpec = ServiceFactory.getDao(MeasurementSpecDao.class).insert(newSpec);
		logUserAction(INSERTED, newSpec);
		AuditLoggerUtil.logAuditInfo( null,newSpec,"save", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
		measurementSpecTableModel.add(insertedSpec);			
		updatePartSpecMeasCount();
		updatePartNameItemOnList(getPartNameFromDB(partSpec.getId().getPartName()));
		
	}

	private void createMeasurementSpec() {
		PartSpec partSpec = partSpecTableModel.getSelectedItem();
		if(partSpec == null) return;
		List<LotControlRule> lotControlRules = ServiceFactory.getDao(LotControlRuleDao.class).findAllByPartId(partSpec.getId().getPartId(),partSpec.getId().getPartName());
		//If the Measurement is already assigned to a ProcessPoint in a lot control rule a message will be shown to the associate
		if (lotControlRules!= null && lotControlRules.size()>0) {
			Set<String> uniquePpIds = new HashSet<String>();
			for (LotControlRule lcr : lotControlRules) {
				uniquePpIds.add(lcr.getId().getProcessPointId());
			}
			StringBuilder sb = new StringBuilder("This Part Spec is in use by process point(s):\n");
			for (String processPoint : uniquePpIds) {
				sb.append("- ").append(ServiceFactory.getDao(ProcessPointDao.class).findById(processPoint).getProcessPointName()).append(" (").append(processPoint).append(")\n");
			}
			sb.append("Please delete the conflicted lot control rules first.");
			MessageDialog.showError(sb.toString());
			return;
		}
		MeasurementSpecId id = new MeasurementSpecId();
		id.setPartName(partSpec.getId().getPartName());
		id.setPartId(partSpec.getId().getPartId());
		id.setMeasurementSeqNum(getNextSequenceNumber());
		MeasurementSpec spec = new MeasurementSpec();
		spec.setId(id);
		spec.setMinimumLimit(MEAS_MIN_LIMIT);
		spec.setMaximumLimit(MEAS_MAX_LIMIT);
		MeasurementSpec insertedSpec = ServiceFactory.getDao(MeasurementSpecDao.class).insert(spec);
		logUserAction(INSERTED, spec);
		AuditLoggerUtil.logAuditInfo( null,spec,"save", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
		measurementSpecTableModel.add(insertedSpec);			
		updatePartSpecMeasCount();
		//SR30946 refresh Part Name List when its measurement created
		updatePartNameItemOnList(getPartNameFromDB(partSpec.getId().getPartName()));
		
	
	}
	
	private void copyMeasurementSpec() {
		PartSpec partSpec = partSpecTableModel.getSelectedItem();
		if(partSpec == null) return;
		List<MeasurementSpec> measurementSpecs = partSpec.getMeasurementSpecs();
		if(measurementSpecs == null || measurementSpecs.size() ==0) return;
		partSpecPanelDialog = new PartSpecPanelDialog(this.getMainWindow(), partSpec.getId().getPartId(),partSpec.getId().getPartName(),measurementSpecs);
		partSpecPanelDialog.getCancelButton().addActionListener(this);
		partSpecPanelDialog.getSaveButton().addActionListener(this);
		partSpecPanelDialog.setLocationRelativeTo(this);
		partSpecPanelDialog.setVisible(true);
	}
	
	private PartName getPartNameFromDB(String partName){
		return ServiceFactory.getDao(PartNameDao.class).findByKey(partName);
	}
	
	/**
	 * update Part Name List for a certain partName
	 * @param partName part name
	 */
	private void updatePartNameItemOnList(PartName partName) {
		if(partName==null) {
			return;
		}
		
		int index = -1;
		for(int i=0; i< partNameList.size(); i++) {
			if(partNameList.get(i).getPartName().equals(partName.getPartName())) {
				index = i;
				break;
			}
		}
		if(index==-1) {
			return;
		}
		
		partNameList.remove(index);
		partNameList.add(index, partName);
				
		partNameTableModel.refresh(partNameList);		
		triggerfilterParts();
	}
	
	@SuppressWarnings("unchecked")
	private void sync206Measurements(MeasurementSpec measurementSpec, BaseTableModel model) {
		Exception exception = null;
		try {
			// BCC 10/3/11: Sync 206 table
			PartId partId = new PartId();
			PartSpec partSpec = partSpecTableModel.getSelectedItem();
			if (partSpec == null) return;
			partId.setPartName(measurementSpec.getId().getPartName());
			partId.setPartId(measurementSpec.getId().getPartId());
			Part part = new Part();
			part.setId(partId);
			part = ServiceFactory.getDao(PartDao.class).findByKey(partId);
			part.setPartDescription(partSpec.getPartDescription());
			part.setPartSerialNumberMask(CommonPartUtility.parsePartMask(partSpec.getPartSerialNumberMask()));
			part.setAllowDuplicates(0);
			List<LotControlRule> rules = ServiceFactory.getDao(LotControlRuleDao.class).findRulesByPartNameAndUniqueFlag(partId.getPartName(), 1);
			if(null != rules && rules.size() > 0) {
				part.setAllowDuplicates(1);
			}
			
			part.setMeasurementCount(partSpec.getMeasurementCount());
			part.setEntryTimestamp(new Timestamp(System.currentTimeMillis()));

			int rowCount = ((MeasurementSpecTableModel) model).getRowCount();
			switch (1) {
			case 1:
				part.setMaximumValue1(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(0, 4).toString()));
				part.setMinimumValue1(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(0, 3).toString()));
				if (rowCount == 1) break;

			case 2:
				part.setMaximumValue2(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(1, 4).toString()));
				part.setMinimumValue2(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(1, 3).toString()));
				if (rowCount == 2) break;

			case 3:
				part.setMaximumValue3(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(2, 4).toString()));
				part.setMinimumValue3(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(2, 3).toString()));
				if (rowCount == 3) break;

			case 4:
				part.setMaximumValue4(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(3, 4).toString()));
				part.setMinimumValue4(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(3, 3).toString()));
				if (rowCount == 4) break;

			case 5:
				part.setMaximumValue5(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(4, 4).toString()));
				part.setMinimumValue5(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(4, 3).toString()));
				if (rowCount == 5) break;

			case 6:
				part.setMaximumValue6(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(5, 4).toString()));
				part.setMinimumValue6(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(5, 3).toString()));
				if (rowCount == 6) break;

			case 7:
				part.setMaximumValue7(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(6, 4).toString()));
				part.setMinimumValue7(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(6, 3).toString()));
				if (rowCount == 7) break;

			case 8:
				part.setMaximumValue8(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(7, 4).toString()));
				part.setMinimumValue8(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(7, 3).toString()));
				if (rowCount == 8) break;

			case 9:
				part.setMaximumValue9(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(8, 4).toString()));
				part.setMinimumValue9(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(8, 3).toString()));
				if (rowCount == 9) break;

			case 10:
				part.setMaximumValue10(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(9, 4).toString()));
				part.setMinimumValue10(Double.valueOf(((MeasurementSpecTableModel) model).getValueAt(9, 3).toString()));
				if (rowCount == 10)	break;
			}
			ServiceFactory.getDao(PartDao.class).update(part);
			logUserAction(UPDATED, part);
			AuditLoggerUtil.logAuditInfo( null,part,"update", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
		} catch (Exception ex) {
			getLogger().error("Unable to update Part: " + ex.getMessage());
			exception = ex;
			// roll back the change if exception happens
			model.rollback();
		}
		handleException(exception);
	}
	
	
	private String getProcessPointsReferencingPart( PartName partName ) {
		   LotControlRuleDao lotControlRuleDao =  ServiceFactory.getDao(LotControlRuleDao.class);
		   ProcessPointDao processPointRuleDao =  ServiceFactory.getDao(ProcessPointDao.class);
		   
	       List<LotControlRule> lotControlRuleList = lotControlRuleDao.findAllByPartName(partName);
	       
	       if (lotControlRuleList == null) {
	    	   return "";
	       }
	       
	       StringBuilder sb = new StringBuilder();
	       for (LotControlRule l : lotControlRuleList) {
	    	   if ( sb.indexOf(l.getId().getProcessPointId()) >= 0 ) {
	    		   continue;  // skip it if its already in the list
	    	   }
	    	   
	    	   ProcessPoint processPoint = processPointRuleDao.findByKey(l.getId().getProcessPointId());
	    	   String name="";
	    	   if (processPoint != null) {
	    		 name= processPoint.getProcessPointName();
	    	   }
	    	   String msg =String.format("\n%s (%s)",name,l.getId().getProcessPointId());
	    	   sb.append(msg);
	       }
	       return sb.toString();
	}
	
	private boolean isPartConfirmRequired(){
			return getPropertyBoolean("PART_CONFIRM_REQUIRED", true);
	}
	
	private void updatePartSpecMeasCount(){
		PartSpec partSpec = partSpecTableModel.getSelectedItem();
		if(partSpec == null) return;
		PartSpecId oldId = partSpecTableModel.getSelectedItem().getId();
		PartSpec oldPartSpec = ServiceFactory.getService(PartSpecDao.class).findByKey(oldId);
		partSpec.setMeasurementCount(measurementSpecTableModel.getRowCount());
		ServiceFactory.getService(PartSpecDao.class).save(partSpec);
		logUserAction(SAVED, partSpec);
		AuditLoggerUtil.logAuditInfo( oldPartSpec,partSpec,"update", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
	}
		
	private String parsePartSerialNumberMask(String partSerialNumberMask){
		StringBuffer partMask = new StringBuffer();
		char[] charArray = partSerialNumberMask.toCharArray();
		for(int i=0;i< charArray.length;i++){
			char c = charArray[i];
			if(c == CommonPartUtility.WILD_CARD_ESCAPE_CHAR){
				partMask.append(c); 
				partMask.append(charArray[i+1]);
				i++;
			}else if(CommonPartUtility.isWildChar(c)){
				if(partMask.length()>1 && charArray[i-1] == CommonPartUtility.WILD_CARD_ESCAPE_CHAR && charArray[i-2] == CommonPartUtility.WILD_CARD_ESCAPE_CHAR ){
					partMask.append(c);
				}else if(charArray.length > i+1 && c == CommonPartUtility.WILD_CARD_ONE_ANYTHING && charArray[i+1] == CommonPartUtility.PART_MASK_DELIMITER){
					partMask.append(c);
				}else{
					partMask.append("<<"+c+">>");
				}
			}else{
				if((c == '<' && charArray.length > i+4 && charArray[i+1] == '<' && CommonPartUtility.isWildChar(charArray[i+2]) && charArray[i+3] == '>'  && charArray[i+4] == '>')){
					partMask.append("<<"+charArray[i+2]+">>");
					i=i+4;
					continue;
				}else{
					partMask.append(c);
				}
			}
			
		}
					
		return partMask.toString();
	}
	
	private void triggerfilterParts() {		
		String filter = partFilterInput.getText();			
		partNameTableModel.refresh(filterPartNameList(filter));
	}
	
	private List<PartName> filterPartNameList(String filter) {
		if (StringUtils.isEmpty(filter)) {
			return partNameList;
		}
		List<PartName> filteredList = new ArrayList<PartName>();
		if (partNameList == null || partNameList.isEmpty()) {
			return filteredList;
		}
		for (PartName partName : partNameList) {
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
	
	public void refreshPartSpecs(){
	    	
	  	final PartName partNameFromDB = getPartNameFromDB(partNameTableModel.getSelectedItem().getId());
		    		
	  	SwingUtilities.invokeLater(new Runnable() {
	  			public void run() {
	  				try {
						partSpecTableModel.refresh(partNameFromDB.getAllPartSpecs());
						updatePartNameItemOnList(partNameFromDB);
					}catch(Exception e){
						handleException (e);
   					}
   				}
   			});
	 }
	 
	private void createRepairPart(PartSpec partSpec) {
		RepairProcessPointDao repairProcessPointDao =  getDao(RepairProcessPointDao.class);
		if(partSpec!= null) {
			List<String> processPoints = repairProcessPointDao.findRepairProcessPointForPartName(partSpec.getId().getPartName());
		
			if(processPoints != null && processPoints.size() > 0){
				for(String processPoint:processPoints){	
					RepairProcessPointId id = new RepairProcessPointId();
					id.setPartId(partSpec.getId().getPartId());
					id.setPartName(partSpec.getId().getPartName());
					id.setProcessPointId(processPoint);
					
					RepairProcessPoint repairProcessPoint = new RepairProcessPoint();
					repairProcessPoint.setId(id);
					repairProcessPoint.setSequenceNo(0);
					
					repairProcessPointDao.save(repairProcessPoint);
					logUserAction(SAVED, repairProcessPoint);
					AuditLoggerUtil.logAuditInfo( null,repairProcessPoint,"save", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
				}
			}
		}
		
	}
	
	private void removeRepairPart(PartSpec partSpec) {
		RepairProcessPointDao repairProcessPointDao =  getDao(RepairProcessPointDao.class);
		List<RepairProcessPoint> repairParts = repairProcessPointDao.findAllRepairPartsByPartNameAndPartId(partSpec.getId().getPartName(), partSpec.getId().getPartId());
		for(RepairProcessPoint part: repairParts){
			repairProcessPointDao.remove(part);
			logUserAction(REMOVED, part);
			AuditLoggerUtil.logAuditInfo( part,null,"delete", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
		}
		
	}
}