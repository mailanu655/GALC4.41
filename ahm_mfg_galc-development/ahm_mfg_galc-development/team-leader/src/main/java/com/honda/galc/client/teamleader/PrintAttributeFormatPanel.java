package com.honda.galc.client.teamleader;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ComboBoxCellEditor;
import com.honda.galc.client.ui.component.ComboBoxCellRender;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.PrintAttributeFormatDao;
import com.honda.galc.data.PrintAttribute;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.enumtype.PrintAttributeType;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.property.PrintAttributeFormatPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * <h3>PrintAttributeFormatPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PrintAttributeFormatPanel description </p>
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
 * <TD>Mar 10, 2017</TD>
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
 * @since Mar 10, 2017
 */
public class PrintAttributeFormatPanel  extends TabbedPanel implements ActionListener, ItemListener, TableModelListener{
	private static final long serialVersionUID = 1L;
	private static final String ADD ="Add";
	private static final String DELETE = "Delete";
	private static final String UP = "Up";
	private static final String DOWN = "Down";
	private int typeCol = 2;
	private int valueCol = 3;
	private Dimension screenDimension;
	private LabeledComboBox formId;
	

	private JButton addButton;
	private JButton deleteButton;
	private JButton cloneButton;
	private JPanel formatMgrPanel;
	private TablePane formatPanel;
	private String currentFormId;
	private Map<PrintAttributeType, String[]> pafValueMap = new HashMap<PrintAttributeType, String[]>();
	private List<String> formIds;
	private CommonTlPropertyBean tlPropertyBean;
	private PrintAttributeFormatPropertyBean pafPropertyBean;
	
	enum Pre_Defined_FormIds {
		PRE_DEFINED_SQL, PRE_DEFINED_SQL_COLLECTION, PRE_DEFINED_JPQL, PRE_DEFINED_JPQL_COLLECTION;
		public static String PreDefined = "PRE_DEFINED_";
		
		public static String getPredefined(String name){
			return PreDefined + name;
		}
		
		public static boolean isPredefined(String name){
			for(Pre_Defined_FormIds pdf : Pre_Defined_FormIds.values())
				if(pdf.name().equals(name))
					return true;
			
			return false;
		}
		
		
	};
		
	
	public PrintAttributeFormatPanel(MainWindow mainWindow) {
		super("Print Attribute Format", KeyEvent.VK_P, mainWindow);
		screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		
	}

	
	public void actionPerformed(ActionEvent e) {
         if(e.getSource() instanceof JMenuItem) {
        	 Exception exception = null;
        	 try{
	        	 JMenuItem menuItem = (JMenuItem)e.getSource();
	        	 if(menuItem.getName().equals(ADD)) createPrintAttributeFormat();
	        	 else if(menuItem.getName().equals(DELETE)) deletePrintAttributeFormat();
	        	 else if(menuItem.getName().equals(UP)) moveUp();
	        	 else if(menuItem.getName().equals(DOWN)) moveDown();
	        	
        	 }catch(Exception ex) {
        		 exception = ex;
        	 }
        	 handleException(exception);
         }
         
         if(e.getSource().equals(deleteButton)) deleteForm();
         if(e.getSource().equals(cloneButton)) cloneForm();
         if(e.getSource().equals(addButton)) addForm();
	}


	private MouseListener createPrintAttributeFormatListener(){
    	return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showCreateRulePopupMenu(e);
			}
		 });  
	}
	
	private void showCreateRulePopupMenu(MouseEvent e) {
    	Logger.getLogger().info("PrintAttributeFormatPanel PopupMenu");
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(ADD, true));
		popupMenu.add(createMenuItem(DELETE, true));
		popupMenu.add(createMenuItem(UP, isUpEnabled()));
		popupMenu.add(createMenuItem(DOWN, isDownEnabled()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	
	private void cloneForm() {
		
		String newFormId= JOptionPane.showInputDialog("Please input clone Form Id: ");
		getLogger().info("clone from form id: " + currentFormId + " to new from id:" + newFormId);
		
		if(StringUtils.isEmpty(newFormId) || formIds.contains(newFormId)) {
			JOptionPane.showMessageDialog(null, "Invalid clone Form Id: " + newFormId, "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		
		List<PrintAttributeFormat> items = getPafTableModel().getItems();
		List<PrintAttributeFormat> cloneList = new ArrayList<PrintAttributeFormat>();
		for(PrintAttributeFormat item : items){
			PrintAttributeFormat newItem = item.clone();
			newItem.getId().setFormId(newFormId);
			cloneList.add(newItem);
		}
		
		getDao().saveAll(cloneList);
		logUserAction(SAVED, cloneList);
		updateFormIds();
		formId.getComponent().setSelectedItem(newFormId);
		updatePrintAttributeFormats();
		
	}


	private void deleteForm() {
		
		String deleteFormId= JOptionPane.showInputDialog("Please input delete Form Id: ");
		
		getLogger().info("delete form id: " + deleteFormId + " current Form Id: " + currentFormId);
		if(!currentFormId.equals(deleteFormId)){
			JOptionPane.showMessageDialog(null, "Invalid Form Id: " + deleteFormId, "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
			
		getDao().removeAllByFormId(currentFormId);
		logUserAction("removed PrintAttributeFormat by ID: " + currentFormId);
		updateFormIds();
		updatePrintAttributeFormats();
		
		
	}
	
	private void addForm() {
		getLogger().info("add print form");
		
		String newFormId= JOptionPane.showInputDialog("Please input new Form Id: ");
		
		if(StringUtils.isEmpty(newFormId) || formIds.contains(newFormId)) {
			JOptionPane.showMessageDialog(null, "Invalid new Form Id: " + newFormId, "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		PrintAttributeFormat newItem = addNewPrintAttributeFormat(newFormId, PrintAttributeType.None);
		
		updateFormIds();
		formId.getComponent().setSelectedItem(newFormId);
		updatePrintAttributeFormats();
		getPafTableModel().selectItem(newItem);
		
		
	}


	private PrintAttributeFormat addNewPrintAttributeFormat(String newFormId, PrintAttributeType type) {
		PrintAttributeFormat newItem = new PrintAttributeFormat(newFormId, "");
		newItem.setSequenceNumber(1);
		newItem.setAttributeType(type);
		newItem.setRequiredTypeId(0);
		getDao().save(newItem);
		logUserAction(SAVED, newItem);
		return newItem;
	}


	private void deletePrintAttributeFormat() {
		List<PrintAttributeFormat> items = getPafTableModel().getItems();

		PrintAttributeFormat selectedItem = getPafTableModel().getSelectedItem();
		getDao().remove(selectedItem);
		getLogger().info("delete : " + selectedItem);

		items.remove(selectedItem);
		for(PrintAttributeFormat item : items){
			if(item.getSequenceNumber() > selectedItem.getSequenceNumber())
				item.setSequenceNumber(item.getSequenceNumber() -1);
		}

		getDao().updateAll(items);
		logUserAction(UPDATED, items);

		updatePrintAttributeFormats();

	}


	private PrintAttributeFormatTableModel getPafTableModel() {
		return (PrintAttributeFormatTableModel)formatPanel.getTable().getModel();
	}


	private PrintAttributeFormatDao getDao() {
		return ServiceFactory.getDao(PrintAttributeFormatDao.class);
		
	}


	private void moveUp() {
		
		int index = getPafTableModel().getTable().getSelectedRow();

		List<PrintAttributeFormat> items = getPafTableModel().getItems();
		PrintAttributeFormat currentItem = items.get(index);
		PrintAttributeFormat previousItem = items.get(index -1);

		getLogger().info("move up : " + currentItem);
		
		currentItem.setSequenceNumber(previousItem.getSequenceNumber());
		previousItem.setSequenceNumber(previousItem.getSequenceNumber() + 1);

		getDao().saveAll(items);
		logUserAction(SAVED, items);
		
		updatePrintAttributeFormats();
		
	}
	
	private void moveDown() {
		
		int index = getPafTableModel().getTable().getSelectedRow();
		
		List<PrintAttributeFormat> items = getPafTableModel().getItems();
		PrintAttributeFormat currentItem = items.get(index);
		PrintAttributeFormat nextItem = items.get(index +1);
		getLogger().info("move down : " + currentItem);
		
		nextItem.setSequenceNumber(currentItem.getSequenceNumber());
		currentItem.setSequenceNumber(currentItem.getSequenceNumber() +1);
		
		getDao().saveAll(items);
		logUserAction(SAVED, items);
		
		updatePrintAttributeFormats();
		
		
	}


	private void createPrintAttributeFormat() {
		getLogger().info("add new Print Attribute Format");
		int selectedIndex = getPafTableModel().getTable().getSelectedRow();
		List<PrintAttributeFormat> items = getPafTableModel().getItems();
		PrintAttributeFormat currentItem =  items.get(selectedIndex);
		
		for(PrintAttributeFormat paf : items){
			if(paf.getSequenceNumber() > currentItem.getSequenceNumber())
				paf.setSequenceNumber(paf.getSequenceNumber() +1);
		}
		
		PrintAttributeFormat newItem = new PrintAttributeFormat(currentFormId, "");
		if(!StringUtils.isEmpty(currentItem.getId().getAttribute())){
			newItem.setSequenceNumber(currentItem.getSequenceNumber() + 1);
			newItem.setAttributeType(PrintAttributeType.None);
			newItem.setRequiredTypeId(0);
			items.add(newItem);

			getDao().saveAll(items);
			logUserAction(SAVED, items);

			updatePrintAttributeFormats();
			getPafTableModel().selectItem(newItem);
		}
		
	}


	private boolean isDownEnabled() {
		return formatPanel.getTable().getSelectedRow() != (formatPanel.getTable().getRowCount() -1);
	}


	private boolean isUpEnabled() {
		return formatPanel.getTable().getSelectedRow() != 0;
	}

	@Override
	public void onTabSelected() {
		try {
			if (isInitialized)	return;
			initComponents();
			addListeners();
			updateFormIds();
			isInitialized = true;
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to start Lot Control Validation.");
			setErrorMessage("Exception to start Print Attribute Formats panel." + e.toString());
		}

	}


	@SuppressWarnings("unchecked")
	private void updateFormIds() {
		//get form IDs filter from property
		String filterIds = getPAFPropertyBean().getFilterByFormIds();
		
		formIds = new ArrayList<String>();
		formIds.add("");
		Set<String> formIdList = new HashSet<String>();
		List<PrintAttributeFormat> allFormats = ServiceFactory.getDao(PrintAttributeFormatDao.class).findAll();
		for(PrintAttributeFormat format : allFormats){
			//check whether formID is defined in filter 
			String formId = format.getId().getFormId();
			if (filterByIds(formId, filterIds)) formIdList.add(formId);
		}


		for(Pre_Defined_FormIds pd : Pre_Defined_FormIds.values()){
			
			if(getTlPropertyBean().isSqlAdmin() && !formIdList.contains(pd.name()) && filterByIds(pd.name(), filterIds)){
				formIdList.add(pd.name());
				List<PrintAttributeFormat> formats = ServiceFactory.getDao(PrintAttributeFormatDao.class).findAllByFormId(pd.name());
				if(formats == null || formats.size() == 0)
					addNewPrintAttributeFormat(pd.name(), PrintAttributeType.valueOf(StringUtils.stripStart(pd.name(), Pre_Defined_FormIds.PreDefined)));
			} else if(!getTlPropertyBean().isSqlAdmin() && formIdList.contains(pd.name())){
				formIdList.remove(pd.name());
			}
		}


		formIds.addAll(new SortedArrayList<String>(formIdList));

		ComboBoxModel<String> model = new ComboBoxModel<String>(formIds);
		getFormId().getComponent().setModel(model);
		getFormId().getComponent().setSelectedIndex(-1);
		getFormId().getComponent().setRenderer(model);
	}


	private void addListeners() {
		getFormId().getComponent().addItemListener(this);
		getAddButton().addActionListener(this);
		getDeleteButton().addActionListener(this);
		getCloneButton().addActionListener(this);
		
		MouseListener poupMenuListener = createPrintAttributeFormatListener();
		formatPanel.getTable().addMouseListener(poupMenuListener);
	}


	private void initComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Border border = BorderFactory.createEtchedBorder();
		setBorder(border);
		add(createFormatManagePanel());
		add(createFormatPanel());
	}


	private Component createFormatPanel() {
		if(formatPanel == null){
			formatPanel = new TablePane("Print Attribute Form Configuration ", createPrintAttributeFormatTable());
			Dimension dimension = new Dimension(screenDimension.width,screenDimension.height - 140);
			formatPanel.setPreferredSize(dimension);
			formatPanel.setBorder(BorderFactory.createEmptyBorder());
			PrintAttributeFormatTableModel model = new PrintAttributeFormatTableModel(formatPanel.getTable(),new ArrayList<PrintAttributeFormat>());
			model.addTableModelListener(this);
		}
		return formatPanel;
	}


	private JPanel createFormatManagePanel() {
		if(formatMgrPanel == null){
			formatMgrPanel = new JPanel(new FlowLayout());
			Dimension dimension = new Dimension(screenDimension.width,45);
			formatMgrPanel.setPreferredSize(dimension);
			formatMgrPanel.setBorder(BorderFactory.createEmptyBorder());
			
			formatMgrPanel.add(getFormId());
			formatMgrPanel.add(getAddButton());
			formatMgrPanel.add(getDeleteButton());
			formatMgrPanel.add(getCloneButton());
		}
		return formatMgrPanel;
	}

	// ------------- getters & setters ------------------
	public LabeledComboBox getFormId() {
		if(formId == null){
			formId = new LabeledComboBox("Form Id:");
		}
		return formId;
	}


	public JButton getAddButton() {
		if(addButton == null)
			addButton = new JButton("Add");
		return addButton;
	}

	public JButton getDeleteButton() {
		if(deleteButton == null)
			deleteButton = new JButton("Delete");
		return deleteButton;
	}


	public JButton getCloneButton() {
		if(cloneButton == null)
			cloneButton = new JButton("Clone");
		return cloneButton;
	}
	
	public TablePane getFormatPanel() {
		return formatPanel;
	}



	public void itemStateChanged(ItemEvent e) {
		boolean groupChanged = false;
		if(e.getStateChange() == ItemEvent.SELECTED){
			currentFormId = StringUtils.trim(e.getItem().toString());
			groupChanged = true;
		}
		
		if(groupChanged) updatePrintAttributeFormats();
		
	}


	private void updatePrintAttributeFormats() {
		clearErrorMessage();
		if(StringUtils.isEmpty(currentFormId)){
			new PrintAttributeFormatTableModel(formatPanel.getTable(), new ArrayList<PrintAttributeFormat>());
			return;
		}
		
		List<PrintAttributeFormat> allFormats = ServiceFactory.getDao(PrintAttributeFormatDao.class).findAllByFormId(currentFormId);
		PrintAttributeFormatTableModel model = new PrintAttributeFormatTableModel(formatPanel.getTable(),allFormats);
		model.addTableModelListener(this);
		
	}
	
	 
	private String[] getPafValueList(PrintAttributeType type) {
		if(!pafValueMap.containsKey(type)) 
			pafValueMap.put(type, getPafValues(type));
		
		return pafValueMap.get(type);
	}

	private String[] getPafValues(PrintAttributeType attributeType) {
		switch(attributeType){
		case AttributeByProduct:
			return findAttributes(Product.class);
		case AttributeByEngine:
			return findAttributes(Engine.class);
		case AttributeByFrame:
			return findAttributes(Frame.class);
		case AttributeByEngineSpec:
			return findAttributes(EngineSpec.class);
		case AttributeByFrameSpec:
			return findAttributes(FrameSpec.class);
		case AttributeByProductionLot:
			return findAttributes(ProductionLot.class);
		case SQL:
		case SQL_COLLECTION:
		case JPQL:
		case JPQL_COLLECTION:
		    return getTlPropertyBean().isSqlAdmin() ? null : findPredefinedSql(Pre_Defined_FormIds.getPredefined(attributeType.name()), attributeType.getId());
		default:
			return null;
		}
	}

	private String[] findPredefinedSql(String formId, Integer attributeType) {
		
		List<String> results = getDao().findAllPredefined(formId, attributeType);
		
		//current existing
		List<String> existResults = getDao().findAllPredefined(currentFormId, attributeType);
		if(existResults != null && existResults.size() > 0){
			for(String exist : existResults)
				if(!results.contains(exist))
					results.add(exist);
		}
		
		String[] resultArray = new String[results.size()];
		results.toArray(resultArray);
		
		
		return results == null ? new String[]{} :  resultArray;
	}


	private String[] findAttributes(Class<?> attributeClass) {
    	List<String> mList = new ArrayList<String>();
    	Method[] methods = attributeClass.getMethods();
    	for(Method method : methods) {
    		Annotation annotation = method.getAnnotation(PrintAttribute.class);
    		if(annotation != null) {
    			mList.add(method.getName());
    		}
    	}
    	
    	return mList.toArray(new String[mList.size()]);
    }
	

	private JTable createPrintAttributeFormatTable(){
		JTable table = new JTable(){
			private static final long serialVersionUID = 1L;
			@Override
			public TableCellEditor getCellEditor(int row, int col){
				if(col == valueCol){
					Object typeValue = getModel().getValueAt(row, typeCol);
					PrintAttributeType type = PrintAttributeType.valueOf(typeValue.toString());
					String[] values = getPafValueList(type);
					return values == null ? super.getCellEditor(row, col) : new ComboBoxCellEditor(values,false);
				}
				else 
					return super.getCellEditor(row, col);
			}


			@Override
			public TableCellRenderer getCellRenderer(int row, int col){
				if(col == valueCol){
					Object typeValue = getModel().getValueAt(row, typeCol);
					PrintAttributeType type = PrintAttributeType.valueOf(typeValue.toString());
					String[] values = getPafValueList(type);
					if(values == null)
						return super.getCellRenderer(row, col);
					else {
						ComboBoxCellRender comboBoxCellRender = new ComboBoxCellRender(values);
						if(values.length > 0)
							comboBoxCellRender.setSelectedIndex(0);
						return comboBoxCellRender;
					}
				}
				else 
					return super.getCellRenderer(row, col);
			}
			
			public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend)
	        {
				super.changeSelection(rowIndex, columnIndex, toggle , extend);
	        }
		};
		
	    table.setRowSelectionAllowed(true);
	    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JTableHeader header = table.getTableHeader();
		header.setFont(new Font("Dialog", Font.BOLD, 12));
		table.setFont(new Font("Dialog", Font.BOLD, 12));
		table.setRowHeight(26);
		table.setRowMargin(2);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (!SwingUtilities.isLeftMouseButton(e)) {
					return;
				}
			}
		});
	

		return table;
	}
	
	public void tableChanged(TableModelEvent e) {
		if(e.getSource() instanceof PrintAttributeFormatTableModel) {
			PrintAttributeFormatTableModel model =  (PrintAttributeFormatTableModel)e.getSource();
			PrintAttributeFormat format = model.getSelectedItem();
			if(format == null) return;
			
			getLogger().info("updated item: " + format);
			
			Exception exception = null;
			try{
				
				validatePrintAttributeFormat(model);
				if(e.getColumn() == 1) //primary key changed
					ServiceFactory.getDao(PrintAttributeFormatDao.class).removeBySequenceNumber(format.getId().getFormId(), format.getSequenceNumber());
				if(e.getColumn() == 2){
					TableCellRenderer cellRenderer = formatPanel.getTable().getCellRenderer(e.getFirstRow(), 3);
					if(ComboBoxCellRender.class.isAssignableFrom(cellRenderer.getClass())){
						if(((ComboBoxCellRender)cellRenderer).getItemCount() > 0){
							((ComboBoxCellRender)cellRenderer).setSelectedIndex(0);
							format.setAttributeValue((String)((ComboBoxCellRender)cellRenderer).getItemAt(0));
							getPafTableModel().selectItem(format);
						}
					}
				}
				ServiceFactory.getDao(PrintAttributeFormatDao.class).save(format);
				logUserAction(SAVED, format);
			}catch(Exception ex) {
				exception = ex;
				model.rollback();
			}
			handleException(exception);
			
		}
	}


	private void validatePrintAttributeFormat(PrintAttributeFormatTableModel model) {
		
		PrintAttributeFormat format = model.getSelectedItem();
		List<PrintAttributeFormat> all = model.getItems();
		List<String> dupSeqList = new ArrayList<String>();
		List<String> dupAttrList = new ArrayList<String>();
		for (PrintAttributeFormat item : all){
			if(item.getSequenceNumber() == format.getSequenceNumber() && 
					!item.getAttribute().equals(format.getAttribute())){
				dupSeqList.add(item.getAttribute());
			}
			
			if(format != item && 
				item.getAttribute().equals(format.getAttribute())){
				dupAttrList.add(item.getAttribute());
			}
				
		}
		                                     
		if(dupSeqList.size() > 0)
			throw new TaskException("Invadid Sequence Number:" + format.getSequenceNumber() + " already used by attributes:" + dupSeqList);
		
		if(dupAttrList.size() > 0)
			throw new TaskException("Invadid Attriubte:" + format.getAttribute() + " already used by attributes:" + dupSeqList);
		
	}
	
	private boolean filterByIds(String id, String filters) {
		if (StringUtils.isEmpty(filters)) return true;
		
		String[] filterIds = filters.split(",");
		for (String filterId : filterIds) {
			if (filterId.trim().equals(id)) {
				return true;
			}
		}
		return false;
	}
	
	public PrintAttributeFormatPropertyBean getPAFPropertyBean() {
		if (pafPropertyBean == null) {
			pafPropertyBean = PropertyService.getPropertyBean(PrintAttributeFormatPropertyBean.class, window.getApplicationContext().getApplicationId());
		}
		return pafPropertyBean;
	}
	
	public CommonTlPropertyBean getTlPropertyBean() {
		if(tlPropertyBean == null)
			tlPropertyBean = PropertyService.getPropertyBean(CommonTlPropertyBean.class, window.getApplicationContext().getApplicationId());
		return tlPropertyBean;
	}


}
