package com.honda.galc.client.qics.view.screen;


import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.qics.view.action.AbstractPanelAction;
import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.dialog.IppTagUpdateDialog;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.product.IPPTagDao;
import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.product.IPPTag;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ServerInfoUtil;


public class IPPTagMainPanel extends QicsPanel {

	private static final long serialVersionUID = 1L;
	private static final String UPDATE_IPP_TAG ="Update Tag Number";
	private static final String DELETE_IPP_TAG ="Delete Tag Number";

	private ObjectTablePane<IPPTag> displayPane;
	private IPPInputPanel inputPane;
	private JPopupMenu popupMenu;
	
	public IPPTagMainPanel(QicsFrame frame) {
		super(frame);
		initialize();
	}


	public QicsViewId getQicsViewId() {
		return QicsViewId.IPPTAG;
	}

	protected void initialize() {
		setLayout(null);
		setSize(getTabPaneWidth(), getTabPaneHeight());
		inputPane = createInputPane();
		displayPane = createDisplayPane();
		popupMenu = createPopupMenu();
		add(getInputPane());
		add(getDisplayPane());
		mapActions();
		
	}


	public void startPanel() {
		
		List<IPPTag> list = getQicsController().selectIppHistory();
		getProductModel().setIppHistory(list);
		
        getDisplayPane().removeData();   
		getDisplayPane().reloadData(list);
		setButtonsState();
		UiUtils.requestFocus(getInputPane().getIppTagInputElement());
	}

	@Override
	public void setButtonsState() {
		super.setButtonsState();
		getQicsFrame().getMainPanel().setButtonsState();
	}

	public IPPInputPanel getInputPane() {
		return inputPane;
	}

	protected ObjectTablePane<IPPTag> createDisplayPane() {
		int width = 1000;
		int height = 400;
			
		ColumnMappings columnMappings = 
			ColumnMappings.with("Product ID", "productId")
						  .put("IPP Tag Number", "ippTagNo").put("Division ID", "divisionId")
						  .put("Actual Timestamp", "actualTimestamp");
						  

			
		ObjectTablePane<IPPTag> pane = new ObjectTablePane<IPPTag>(columnMappings.get(),true);
		
		pane.setSize(width, height);
		pane.setTitle("IPP Tag");
		pane.setLocation(0, getInputPane().getY() + getInputPane().getHeight());
		pane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		setColumnSize(pane);
		return pane;
	}

	protected void setColumnSize(ObjectTablePane<IPPTag> pane) {
		Map<String, Integer> columnWidths = new HashMap<String, Integer>();
		columnWidths.put("Product ID", 250);
		columnWidths.put("IPP Tag Number", 250);
		columnWidths.put("Division ID", 250);
		columnWidths.put("Actual Timestamp", 250);		
	}


	public IPPInputPanel createInputPane() {
		IPPInputPanel panel = new IPPInputPanel(1000, 100);
		panel.setLocation(0, 0);
		return panel;
	}

    protected JPopupMenu createPopupMenu() {
    	JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(UPDATE_IPP_TAG,false, null));
		popupMenu.addSeparator();
		popupMenu.add(createMenuItem(DELETE_IPP_TAG,false, null));
		return popupMenu;
    }
    
	protected JMenuItem createMenuItem(String name,boolean enabled, ActionListener listener) {
		JMenuItem menuItem = new JMenuItem(name);
		menuItem.setName(name);
		if (listener != null) {
			menuItem.addActionListener(listener);
		}
		menuItem.setEnabled(enabled);
		return menuItem;
	}
	
	protected void mapActions() {

		AbstractPanelAction action = new AbstractPanelAction(this) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void execute(ActionEvent e) {
				if (e.getSource().equals(getInputPane().getIppTagInputElement())) {
					String ippNumber = getInputPane().getIppTagInputElement().getText();
					ippNumber = StringUtils.trim(ippNumber);
					StringBuilder msg = new StringBuilder();
					if (!isIppNumberValid(ippNumber, msg)) {
						getQicsFrame().setErrorMessage(msg.toString());
					} else {
						getQicsController().submitIPP(ippNumber);
						getQicsFrame().getLogger().info("Successfully scanned IPP Tag:" + ippNumber);
						startPanel();
						getInputPane().getIppTagInputElement().setText("");
					}
					UiUtils.requestFocus(getInputPane().getIppTagInputElement());
				} else if (e.getSource() instanceof JMenuItem ) {
					JMenuItem item = (JMenuItem) e.getSource();
					if (UPDATE_IPP_TAG.equals(item.getName())) {
						displayIppTagUpdateDialog();
					} else if (DELETE_IPP_TAG.equals(item.getName())) {
						deleteIppTag();
					}
				}
			} 
		};
		getInputPane().getIppTagInputElement().setAction(action);
		
		Component[] items = getPopupMenu().getComponents();
		for (Component c : items) {
			if (c instanceof JMenuItem) {
				((JMenuItem) c).addActionListener(action);
			}
		}

		if (getQicsPropertyBean().isIppTagEditEnabled()) {
			PopupMenuMouseAdapter tagEditMenuListener = new PopupMenuMouseAdapter(new IPopupMenu() {
				public void showPopupMenu(MouseEvent e) {
					showAttributePopupMenu(e);
				}
			 }); 
			getDisplayPane().getTable().addMouseListener(tagEditMenuListener);
		}
		
		getInputPane().getIppTagInputElement().getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				processDocumentChange(e);
			}

			public void insertUpdate(DocumentEvent e) {
				processDocumentChange(e);
			}

			public void removeUpdate(DocumentEvent e) {
				processDocumentChange(e);
			}

			protected void processDocumentChange(DocumentEvent e) {
				if (StringUtils.isNotBlank(getQicsFrame().getMessage())) {
					getQicsFrame().clearMessage();
				}
			};
		});
	}
	
	public boolean isIppNumberValid(String ippNumber, StringBuilder msgOut) {
		if (StringUtils.isBlank(ippNumber)) {
			if (msgOut != null) {
				msgOut.append("IPP Tag Number cannot be blank");
			}
			return false;
		} else if (ippNumber.length() > 10) {
			if (msgOut != null) {
				msgOut.append("IPP Tag Number cannot exceed 10 digits");
			}
			return false;
		} else if (!StringUtils.isNumeric(ippNumber)) {
			if (msgOut != null) {
				msgOut.append("IPP Tag Number must be numeric");
			}
			return false;
		}
		return true;
	}

	protected void displayIppTagUpdateDialog() {
		IPPTag ippTag = getDisplayPane().getSelectedItem();
		if (ippTag == null) {
			return;
		}
		
		IppTagUpdateDialog dialog = new IppTagUpdateDialog(this, ippTag);
    	dialog.setLocationRelativeTo(this);
    	dialog.setVisible(true);
	}
	
	protected void deleteIppTag() {
		IPPTag ippTag = getDisplayPane().getSelectedItem();
		if (ippTag == null) {
			return;
		}
		int retCode = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete IPPTag ?", "IPPTag Delete", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}
		getLogger().info("excuting deleteIppTag(ippTagNo) function");
		getDao(IPPTagDao.class).remove(ippTag);
		startPanel();
	}
	
	protected void showAttributePopupMenu(MouseEvent e) {
		getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
		IPPTag tag = getDisplayPane().getSelectedItem();
		boolean enabled = false;
		boolean selected = tag != null;
		if (selected) {
			boolean editable = isIppTagEditable(tag);
			enabled = selected && editable;
		}

		Component[] items = getPopupMenu().getComponents();
		for (Component c : items) {
			if (c instanceof JMenuItem) {
				c.setEnabled(enabled);
			}
		}
	}
    
    protected boolean isIppTagEditable(IPPTag ippTag) {
    	if (ippTag == null) {
    		return false;
    	}
    	String oifIppTaskName = getQicsPropertyBean().getIppInfoOifTaskName();
    	if (StringUtils.isBlank(oifIppTaskName)) {
    		String msg = "QICS IPP Tag Edit - IPP_INFO_OIF_TASK_NAME property not configured";
    		getLogger().warn(msg);
    		return false;
    	}
       	String oifServerUrl = getQicsPropertyBean().getIppInfoOifTaskServerUrl();
    	if (StringUtils.isNotBlank(oifServerUrl)) {
    		oifServerUrl = oifServerUrl.trim();
   			oifServerUrl = oifServerUrl + "/"  + ServerInfoUtil.SERVICE_HANDLER;
    	}
    	
    	Timestamp lastExecutionTimestamp = getOifLastProcessingTime(oifServerUrl, oifIppTaskName);
    	if (lastExecutionTimestamp == null) {
    		String msg = "QICS IPP Tag Edit - OIF IPP Info Task - Last Execution time not set";
    		getLogger().warn(msg);
    		return false;
    	}
    	
    	Timestamp tagTs = ippTag.getActualTimestamp();
    	if (tagTs == null && ippTag.getCreateTimestamp() != null) {
    		tagTs = new Timestamp(ippTag.getCreateTimestamp().getTime());
    	}

    	String offProcessPointIdsString = getOffProcessPointId(oifServerUrl, oifIppTaskName);
    	if (StringUtils.isBlank(offProcessPointIdsString)) {
    		return tagTs.after(lastExecutionTimestamp);
    	}
    	
    	if (tagTs.after(lastExecutionTimestamp)) {
    		return true;
    	}
    	
    	String[] ppIds =  offProcessPointIdsString.split(Delimiter.COMMA);
    	ProductHistoryDao<? extends ProductHistory, ?> historyDao = ProductTypeUtil.getProductHistoryDao(getQicsController().getProductType());
    	List<ProductHistory> historyList = new ArrayList<ProductHistory>();
    	for (String processPointId : ppIds) {
    		List<? extends ProductHistory> list = historyDao.findAllByProductAndProcessPoint(ippTag.getId().getProductId(), processPointId);
    		if (list == null || list.isEmpty()) {
    			continue;
    		}
    		historyList.addAll(list);
    	}
    	if (historyList.isEmpty()) {
    		return true;
    	}
    	boolean editable = true;
		for (ProductHistory h : historyList) {
			if (editable && h.getActualTimestamp().before(lastExecutionTimestamp)) {
				editable = false;
				break;
			}
		}
    	return editable;
    }
    

 	protected Timestamp getOifLastProcessingTime(String oifServerUrl, String componentId) {
		String statusKey = ApplicationConstants.LAST_PROCESS_TIMESTAMP;
		ComponentStatusDao dao = null;
		if (StringUtils.isBlank(oifServerUrl)) {
			dao = ServiceFactory.getDao(ComponentStatusDao.class);
		} else {
			dao = HttpServiceProvider.getDao(oifServerUrl, ComponentStatusDao.class);
		}
		ComponentStatus status = dao.findByKey(componentId, statusKey);
		if (status == null || StringUtils.isBlank(status.getStatusValue())) {
			return null;
		}
		Timestamp ts = null;
		try {
			ts = Timestamp.valueOf(status.getStatusValue().trim());
		} catch(Exception e) {
			getLogger().error(e, "Error parsing to Timestamp " + status.getStatusValue());
		}
		return ts;
	}
    
 	protected String getOffProcessPointId(String oifServerUrl, String componentId) {
		String offProcessPointId = null;
		if (StringUtils.isBlank(oifServerUrl)) {
			offProcessPointId = PropertyService.getProperty(componentId, ApplicationConstants.PROCESS_POINT_OFF_SHORT);
		} else {
			ComponentPropertyDao propertyDao = HttpServiceProvider.getDao(oifServerUrl, ComponentPropertyDao.class);
			ComponentPropertyId id = new ComponentPropertyId(componentId, ApplicationConstants.PROCESS_POINT_OFF_SHORT);
	    	ComponentProperty property = propertyDao.findByKey(id);
			if (property != null) {
				offProcessPointId = property.getPropertyValue();
			}
		}
		return offProcessPointId;
	}
    
	public ObjectTablePane<IPPTag> getDisplayPane() {
		return displayPane;
	}


	public void setDisplayPane(ObjectTablePane<IPPTag> displayPane) {
		this.displayPane = displayPane;
	}


	public void setInputPane(IPPInputPanel inputPane) {
		this.inputPane = inputPane;
	}


	protected JPopupMenu getPopupMenu() {
		return popupMenu;
	}
}
