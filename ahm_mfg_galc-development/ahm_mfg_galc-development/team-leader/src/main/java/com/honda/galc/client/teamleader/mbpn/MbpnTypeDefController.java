package com.honda.galc.client.teamleader.mbpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.teamleader.mbpn.MbpnTypeManagementPanel.ProductIdNumberDefEvent;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.MbpnProductTypeDao;
import com.honda.galc.dao.product.ProductIdNumberDefDao;
import com.honda.galc.dao.product.ProductTypeToNumberDefDao;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.entity.product.MbpnProductType;
import com.honda.galc.entity.product.ProductIdNumberDef;
import com.honda.galc.entity.product.ProductTypeToNumberDef;
import com.honda.galc.entity.product.ProductTypeToNumberDefId;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>MbpnTypeDefController</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MbpnTypeDefController description </p>
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
 * <TD>May 31, 2017</TD>
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
 * @since May 31, 2017
 */
public class MbpnTypeDefController implements ActionListener, TreeSelectionListener, TableModelListener,ListSelectionListener {
	private MbpnTypeTreePanel mbpnTypeTree;
	private MbpnTypeManagementPanel mbpnTypeMgrPane;
	private MbpnProductType updatingProductType;
	private MainWindow window;
	private HashMap<String,ProductIdNumberDef> productNumberDefs = new HashMap<String,ProductIdNumberDef>();
	
	public MbpnTypeDefController(MainWindow window, MbpnTypeTreePanel mbpnTypeTree, MbpnTypeManagementPanel mbpnTypeManagementPanel) {
		this.mbpnTypeTree = mbpnTypeTree;
		this.mbpnTypeMgrPane = mbpnTypeManagementPanel;
		this.window = window;
		
		initialize();
	}

	private void initialize() {
		try {
			this.addListeners();
			this.loadData();
			this.initializePanel();
		} catch (Exception e) {
			handleException(e);
		}
	}
	
	private void addListeners() {
		mbpnTypeTree.getTree().addTreeSelectionListener(this);
		
		//MBPN type management buttons
		mbpnTypeMgrPane.getNewButton().addActionListener(this);
		mbpnTypeMgrPane.getRemoveButton().addActionListener(this);
		mbpnTypeMgrPane.getUpdateButton().addActionListener(this);
		mbpnTypeMgrPane.getSaveButton().addActionListener(this);
		enableDeleteUpdateButton(null);
		mbpnTypeMgrPane.getSaveButton().setEnabled(false);
		
		//Product Id Def
		mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().getTable().addMouseListener(createProdIdNumberDefMouseListener());
		
		mbpnTypeMgrPane.getProdDefAssignButton().addActionListener(this);
		mbpnTypeMgrPane.getProdDefDeassignButton().addActionListener(this);
		
		mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().addListSelectionListener(this);
		mbpnTypeMgrPane.getAssignedProductNoDefTablePanel().getTablePanel().addListSelectionListener(this);
		mbpnTypeMgrPane.getMbpnTypePanel().getTablePanel().addListSelectionListener(this);
		
	}
	
	private void loadData() {
		List<ProductIdNumberDef> defs = ServiceFactory.getDao(ProductIdNumberDefDao.class).findAll();
		if (defs != null && !defs.isEmpty()) {
			for (ProductIdNumberDef def : defs) {
				this.productNumberDefs.put(def.getProductIdDef(), def);
			}
		}
	}
	
	private void initializePanel() {
		new ProductNumberDefTableModel(null, mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().getTable(), true);
		this.mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().getTable().getModel().addTableModelListener(this);
		((ProductNumberDefTableModel)this.mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().getTable().getModel()).setColumnEditable(0, false);

		this.changeProductNumberDefAssignButtonState();
		this.changeProductNumberDefDeassignButtonState();
	}
	
	private JPopupMenu createPopupMenu(ProductIdNumberDefEvent[] productIdNumberDefEvents, JTable table) {
		JPopupMenu menu = new JPopupMenu();
		for(ProductIdNumberDefEvent menuItem : productIdNumberDefEvents) {
			JMenuItem item = new JMenuItem();
			item.setName(menuItem.name());
			item.setText(menuItem.name());
			item.setEnabled(menuItem == ProductIdNumberDefEvent.ADD ? true : isMenuItemEnabled(menuItem,table));
			item.addActionListener(this);
			menu.add(item);
		}
		return menu;
	}

	private boolean isMenuItemEnabled(ProductIdNumberDefEvent menuItem, JTable table) {
		return table.getSelectedRowCount() > 0; 
	}

	
	private MouseListener createProdIdNumberDefMouseListener() {
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				JPopupMenu popupMenu = createPopupMenu(ProductIdNumberDefEvent.values(), mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().getTable());
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		 });   
	}
	
	private void enableDeleteUpdateButton(Object selectedType) {
		mbpnTypeMgrPane.getUpdateButton().setEnabled(selectedType != null);
		mbpnTypeMgrPane.getRemoveButton().setEnabled(selectedType != null);
		
	}
	private void deassignProductNumberDef() {
		MbpnProductType mbpnType = getSelectedMbpnProductType();
		ProductIdNumberDef prodIdNumberDef = getSelectedProductNumberDef(mbpnTypeMgrPane.getAssignedProductNoDefTablePanel().getTablePanel().getTable());
		ServiceFactory.getDao(ProductTypeToNumberDefDao.class).removeByKey(new ProductTypeToNumberDefId(mbpnType.getId().getMainNo(), prodIdNumberDef.getProductIdDef()));
		refreshProductNoDefPanel();
		selectProductNumberDef(mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().getTable(), prodIdNumberDef.getId());
		mbpnTypeMgrPane.getProdDefDeassignButton().setEnabled(false);
		Logger.getLogger().info("Product Number Def: ", prodIdNumberDef.getProductIdDef(), " deassigned from: ", mbpnType.toString(), " by user: ", getUser());
		
	}
	
	private void refreshProductNoDefPanel() {
		TreeMap<String,ProductIdNumberDef> unassignedDefs = new TreeMap<String,ProductIdNumberDef>();
		TreeMap<String,ProductIdNumberDef> assignedDefs = new TreeMap<String,ProductIdNumberDef>();
		
		MbpnProductType selectedType = getSelectedMbpnProductType();
		if (selectedType != null) {
			unassignedDefs.putAll(this.productNumberDefs);
			for (ProductIdNumberDef assignedDef : ServiceFactory.getDao(ProductTypeToNumberDefDao.class).findByMainNo(selectedType.getId().getMainNo())) {
				if (unassignedDefs.containsKey(assignedDef.getId())) 
					unassignedDefs.remove(assignedDef.getId());
				assignedDefs.put(assignedDef.getId(),assignedDef);
			}
		}
		refreshProductNoDefTable(new ArrayList<ProductIdNumberDef>(unassignedDefs.values()));
		refreshAssignedProductNoDefTable(new ArrayList<ProductIdNumberDef>(assignedDefs.values()));
		
		changeProductNumberDefAssignButtonState();
		changeProductNumberDefDeassignButtonState();
	}
	
	private void refreshProductNoDefTable(List<ProductIdNumberDef> data) {
		((ProductNumberDefTableModel)(mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().getTable().getModel())).refresh(data);
	}

	private void refreshAssignedProductNoDefTable(List<ProductIdNumberDef> data) {
		((ProductNumberDefTableModel)(mbpnTypeMgrPane.getAssignedProductNoDefTablePanel().getTablePanel().getTable().getModel())).refresh(data);
	}

	private void assignProductNumberDef() {
		MbpnProductType mbpnType = getSelectedMbpnProductType();
		ProductIdNumberDef productNumberDef = getSelectedProductNumberDef(mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().getTable());
		ProductTypeToNumberDef productTypeToNumberDef = new ProductTypeToNumberDef(mbpnType.getId().getMainNo(),productNumberDef.getProductIdDef());
		ServiceFactory.getDao(ProductTypeToNumberDefDao.class).save(productTypeToNumberDef);
		
		mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().getTable().clearSelection();
		refreshProductNoDefPanel();
		
		selectProductNumberDef(mbpnTypeMgrPane.getAssignedProductNoDefTablePanel().getTablePanel().getTable(), productNumberDef.getProductIdDef());
		mbpnTypeMgrPane.getProdDefAssignButton().setEnabled(false);
		Logger.getLogger().info("Product Number Def: ", productNumberDef.getProductIdDef(), " assigned to: ", mbpnType.toString(), " by user: ", getUser());
	}
	
	private void selectProductNumberDef(JTable table, String defId) {
		for (int i = 0 ; i < table.getRowCount(); i++) {
			if (!defId.equals((String)table.getValueAt(i,0))) continue;
			table.setRowSelectionInterval(i,i);
			return;
		}
	}

	private ProductIdNumberDef getSelectedProductNumberDef(JTable table) {
		ProductNumberDefTableModel prodNumberModel = (ProductNumberDefTableModel)table.getModel();
		ProductIdNumberDef productNumberDef = prodNumberModel.getSelectedItem();
		return productNumberDef;
	}

	private MbpnProductType getSelectedMbpnProductType() {
		MbpnTableModel mbpnTypeModel = (MbpnTableModel)mbpnTypeMgrPane.getMbpnTypePanel().getTablePanel().getTable().getModel();
		MbpnProductType mbpnType = mbpnTypeModel.getSelectedItem();
		return mbpnType;
	}

	private void deleteProductIdNumberDef() {
		ProductIdNumberDef selectedItem = getSelectedProductNumberDef(mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().getTable());
		if (selectedItem == null) return;
		
		ServiceFactory.getDao(ProductIdNumberDefDao.class).remove(selectedItem);
		ServiceFactory.getDao(ProductTypeToNumberDefDao.class).removeByProductIdDef(selectedItem.getProductIdDef());
		
		productNumberDefs.remove(selectedItem.getProductIdDef());
		
		ProductNumberDefTableModel model = (ProductNumberDefTableModel)mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().getTable().getModel();
		model.getItems().remove(selectedItem);
		model.fireTableDataChanged();
		Logger.getLogger().info("Product Number Def: ", selectedItem.toString(), " deleted by user: ", getUser());
		
	}

	private void addProductIdNumberDef() {
		final ProductNumberDefCreateDialog dialog = new ProductNumberDefCreateDialog(this.window, this.productNumberDefs.keySet());
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);
		dialog.setVisible(true);
		ProductIdNumberDef newDef = dialog.getNewDef();
		if (newDef == null) return;
		ProductNumberDefTableModel model = (ProductNumberDefTableModel)mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().getTable().getModel();
		this.productNumberDefs.put(newDef.getId(), newDef);
		model.getItems().add(newDef);
		model.fireTableDataChanged();
		this.refreshProductNoDefPanel();
		this.selectProductNumberDef(mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().getTable(), newDef.getId());
		
		Logger.getLogger().info("Product Number Definition " + newDef.toString() + " is added by user: ", getUser());
	}
	
	private void changeProductNumberDefDeassignButtonState() {
		mbpnTypeMgrPane.getProdDefDeassignButton().setEnabled(hasProdDef());
	}

	private boolean hasProdDef() {
		return mbpnTypeMgrPane.getAssignedProductNoDefTablePanel().getTablePanel().getTable().getSelectedRowCount() > 0 &&
				mbpnTypeMgrPane.getMbpnTypePanel().getTablePanel().getTable().getSelectedRowCount() > 0;
	}

	private void changeProductNumberDefAssignButtonState() {
			mbpnTypeMgrPane.getProdDefAssignButton().setEnabled(canEnableProdDefAdd());
	}

	private boolean canEnableProdDefAdd() {
		return mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().getTable().getSelectedRowCount() > 0 &&
				mbpnTypeMgrPane.getMbpnTypePanel().getTablePanel().getTable().getSelectedRowCount() > 0;
	}
	
	private void updateMbpnProductTypeTable(MbpnProductType selectedMbpnProductType) {
		List<MbpnProductType> newList = new ArrayList<MbpnProductType>();
		if(selectedMbpnProductType != null)
			newList.add(selectedMbpnProductType);
		
        new MbpnTableModel(newList, mbpnTypeMgrPane.getMbpnTypePanel().getTablePanel().getTable(), getAllMbpnProductTypes(), getAvailableMainNos(), getAvailableMainNos(), false);
        
        if(selectedMbpnProductType != null)
        	mbpnTypeMgrPane.getMbpnTypePanel().getTablePanel().getTable().setRowSelectionInterval(0, 0);
	}
	

	private boolean validateMbpnProductType(MbpnProductType mbpnProductType) {
		return !(StringUtils.isEmpty(mbpnProductType.getId().getMainNo()) || 
				StringUtils.isEmpty(mbpnProductType.getProductType()) || 
				mbpnProductType.getId().getMainNo().equals(mbpnProductType.getId().getOwnerMainNo()) ||
				isProductTypeAssigned(mbpnProductType));
	}

	private boolean isProductTypeAssigned(MbpnProductType mbpnProductType) {
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> en = mbpnTypeTree.getTop().preorderEnumeration();
		while(en.hasMoreElements()){
			DefaultMutableTreeNode nextElement = en.nextElement();
			if(nextElement.getUserObject() instanceof MbpnProductType){
				if(((MbpnProductType)nextElement.getUserObject()).getId().getMainNo().equals(StringUtils.trim(mbpnProductType.getId().getMainNo()))){
					Logger.getLogger().warn("MainNo:", mbpnProductType.getId().getMainNo(), " already assigned Mbpn product type", ((MbpnProductType)nextElement.getUserObject()).getProductType());
						return true;
				}
			} 
		}
		return false;
	}

	private void updateTreeNode(MbpnProductType mbpnProductType) {
		if(updatingProductType == null){
			DefaultMutableTreeNode parentNode = findParentTreeNode( mbpnTypeTree.getTop(), mbpnProductType);
			if(parentNode ==  mbpnTypeTree.getTop()){
				parentNode = new DefaultMutableTreeNode(mbpnProductType.getProductType());
				mbpnTypeTree.getTop().add(parentNode);
			}
			parentNode.add(new DefaultMutableTreeNode(mbpnProductType));
		}
		
		mbpnTypeTree.reload();
	}
	
	private void updateTreeNodeDelete(DefaultMutableTreeNode mbpnProductTypeNode) {
		DefaultMutableTreeNode parentNode = findParentTreeNode( mbpnTypeTree.getTop(), (MbpnProductType)mbpnProductTypeNode.getUserObject());
		if(mbpnProductTypeNode != null && parentNode != null)
			parentNode.remove(mbpnProductTypeNode);
		
		if(parentNode.isLeaf())
			mbpnTypeTree.getTop().remove(parentNode);
		
		mbpnTypeTree.reload();
		
		
	}
	
	private MbpnProductType getCurrentMbpnProductType() {
		int selectedRow = mbpnTypeMgrPane.getMbpnTypePanel().getTablePanel().getTable().getSelectedRow();
		MbpnProductType mbpnProductType = ((MbpnTableModel)(mbpnTypeMgrPane.getMbpnTypePanel().getTablePanel().getTable().getModel())).getItem(selectedRow);
		return mbpnProductType;
	}
	
	@SuppressWarnings("unchecked")
	private DefaultMutableTreeNode findParentTreeNode(DefaultMutableTreeNode parent, MbpnProductType mbpnProductType) {
		Enumeration<DefaultMutableTreeNode> en = parent.preorderEnumeration();
		while(en.hasMoreElements()){
			DefaultMutableTreeNode nextElement = en.nextElement();
			if(nextElement.getUserObject() instanceof String){
				if(mbpnProductType.getProductType().equals(nextElement.getUserObject().toString()))
						return nextElement;
			}
            //disabled for the node tree - owner main no
			//if(((MbpnProductType)nextElement.getUserObject()).getId().getMainNo().equals(mbpnProductType.getId().getOwnerMainNo()))
			//	return nextElement;
		}
		
		return parent;
	}
	
	private String[] getAvailableMainNos() {
		 List<String> allMainNos = ServiceFactory.getDao(MbpnDao.class).findAllMainNo();
		
		 String[] mainNoArray = new String[]{};
		 if(allMainNos != null){
			 mainNoArray = allMainNos.toArray(new String[allMainNos.size()]);
		 }
		 
		 return mainNoArray;
	}
	
	private String[] getAllMbpnProductTypes() {
		 List<String> allProductTypes = ServiceFactory.getDao(MbpnProductTypeDao.class).findAllProductTypes();
		
		 String[] productTypeArray = new String[]{};
		 if(allProductTypes != null){
			 productTypeArray = allProductTypes.toArray(new String[allProductTypes.size()]);
		 }
		 
		 return productTypeArray;
	}
	
	private String getUser() {
		return ClientMain.getInstance().getAccessControlManager().getUserName();
	}

	protected void handleException (Exception e) {
		if(e != null) {
			Logger.getLogger().error(e, "unexpected exception occurs: " + e.getMessage());
			this.window.setMessage(e.getMessage());
		} else {
			window.clearMessage();
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		Exception exception = null;
		try{
			handleException(exception);
			if(e.getSource() ==(mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().getTable().getSelectionModel())){
				changeProductNumberDefAssignButtonState();
				if (mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().getTable().getSelectedRowCount() > 0) 
					mbpnTypeMgrPane.getAssignedProductNoDefTablePanel().getTablePanel().getTable().clearSelection();
			}else if(e.getSource() ==(mbpnTypeMgrPane.getAssignedProductNoDefTablePanel().getTablePanel().getTable().getSelectionModel())){
				changeProductNumberDefDeassignButtonState();
				if (mbpnTypeMgrPane.getAssignedProductNoDefTablePanel().getTablePanel().getTable().getSelectedRowCount() > 0) 
					mbpnTypeMgrPane.getProductNoDefTablePanel().getTablePanel().getTable().clearSelection();
			}else if(e.getSource() ==(mbpnTypeMgrPane.getMbpnTypePanel().getTablePanel().getTable().getSelectionModel())){
				refreshProductNoDefPanel();
			}
		}catch(Exception ex) {
			exception = ex;
		}

		handleException(exception);
	}

	public void tableChanged(TableModelEvent e) {
		if(e.getSource() instanceof ProductNumberDefTableModel) {
			ProductNumberDefTableModel model =  (ProductNumberDefTableModel)e.getSource();
			ProductIdNumberDef numberDef = model.getSelectedItem();
			if(numberDef == null) return;
			Exception exception = null;
			try{
				handleException(exception);
				ServiceFactory.getDao(ProductIdNumberDefDao.class).update(numberDef);
				Logger.getLogger().info("Product Number Def: ", numberDef.toString(), " updated by user: ", getUser());
			}catch(Exception ex) {
				exception = ex;
				model.rollback();
			}
			handleException(exception);
			
		}
	}

	public void valueChanged(TreeSelectionEvent arg0) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)mbpnTypeTree.getTree().getLastSelectedPathComponent();

        if (node == null) return;
      
        handleException(null);
        Object nodeInfo = node.getUserObject();
        if(nodeInfo == null || nodeInfo instanceof String) {
        	updateMbpnProductTypeTable(null);
        	enableDeleteUpdateButton(null);
        	return; //ignore the top node
        }
        
        MbpnProductType selectedMbpnProductType = (MbpnProductType)nodeInfo;
        updateMbpnProductTypeTable(selectedMbpnProductType);
        enableDeleteUpdateButton(selectedMbpnProductType);
	}

	public void actionPerformed(ActionEvent ae) {
		handleException(null);
		if(ae.getSource() == mbpnTypeMgrPane.getNewButton()) {
			newMbpnProductType();
		} else if(ae.getSource() == mbpnTypeMgrPane.getRemoveButton()) {
			removeMbpnProductType();
		} else if(ae.getSource() ==  mbpnTypeMgrPane.getUpdateButton()){
			updateMbpnProductType();
		} else if(ae.getSource() == mbpnTypeMgrPane.getSaveButton()){
			saveMbpnProductType();
		} else if(ae.getSource() == mbpnTypeMgrPane.getProdDefAssignButton()){
			assignProductNumberDef();
		} else if(ae.getSource() == mbpnTypeMgrPane.getProdDefDeassignButton()){
			deassignProductNumberDef();
		}	
		
		if(ae.getSource() instanceof JMenuItem) {
			processPopupMenuItem(ae);
		}
	}

	private void processPopupMenuItem(ActionEvent ae) {
		Exception exception = null;
		try{
			JMenuItem menuItem = (JMenuItem)ae.getSource();
			if(menuItem.getName().equals(ProductIdNumberDefEvent.ADD.name())) addProductIdNumberDef();
			else if(menuItem.getName().equals(ProductIdNumberDefEvent.REMOVE.name())) deleteProductIdNumberDef();
		}catch(Exception ex) {
			exception = ex;
		}
		handleException(exception);
	}

	private void saveMbpnProductType() {
		MbpnProductType mbpnProductType = getCurrentMbpnProductType();
		
		if(validateMbpnProductType(mbpnProductType))
			ServiceFactory.getDao(MbpnProductTypeDao.class).save(mbpnProductType);
		else{
			JOptionPane.showMessageDialog(mbpnTypeMgrPane, mbpnProductType.toString() + " is invalid.", "Invalid Mbpn Product Type", JOptionPane.WARNING_MESSAGE);
			Logger.getLogger().info("Invalid Mbpn Product Type:", mbpnProductType.toString(), " ", mbpnProductType.getId().getOwnerMainNo());
			
			if(updatingProductType != null) {
				mbpnProductType.setId(updatingProductType.getId());
				mbpnProductType.setProductType(updatingProductType.getProductType());
			}
			
			return;
		}

		updateTreeNode(mbpnProductType);
		if(updatingProductType != null){
			ServiceFactory.getDao(MbpnProductTypeDao.class).remove(updatingProductType);
			updatingProductType = null;
			
			Logger.getLogger().info("Mbpn Product Type:", mbpnProductType.toString(), " ", mbpnProductType.getId().getOwnerMainNo(),  " was updated by user:", getUser());
		} else 
			Logger.getLogger().info("Mbpn Product Type:", mbpnProductType.toString(), " ", mbpnProductType.getId().getOwnerMainNo(),  " was added by user:", getUser());
		

		updateMbpnProductTypeTable(mbpnProductType);
		mbpnTypeMgrPane.getSaveButton().setEnabled(false);
		mbpnTypeMgrPane.getUpdateButton().setEnabled(false);
		mbpnTypeMgrPane.getRemoveButton().setEnabled(true);
		
		ProductTypeCatalog.update();
	}

	private void updateMbpnProductType() {
		MbpnTableModel tableModel = (MbpnTableModel)mbpnTypeMgrPane.getMbpnTypePanel().getTablePanel().getTable().getModel();
		if(getCurrentMbpnProductType() != null)
			updatingProductType =(MbpnProductType)getCurrentMbpnProductType().deepCopy();
		tableModel.setEditable(true);
		tableModel.fireTableDataChanged();
		mbpnTypeMgrPane.getSaveButton().setEnabled(true);
	}

	private void removeMbpnProductType() {
		MbpnProductType deletingProductType = getCurrentMbpnProductType();
		ServiceFactory.getDao(MbpnProductTypeDao.class).remove(deletingProductType);
		ServiceFactory.getDao(ProductTypeToNumberDefDao.class).removeByMainNo(deletingProductType.getId().getMainNo());
		Logger.getLogger().info("Mbpn Product Type:", deletingProductType.toString(), " ", deletingProductType.getId().getOwnerMainNo(),  " was removed by user:", getUser());
		
		DefaultMutableTreeNode currentDeletingNode = (DefaultMutableTreeNode)mbpnTypeTree.getTree().getLastSelectedPathComponent();
		
		if(currentDeletingNode == null){
			JOptionPane.showMessageDialog(mbpnTypeMgrPane, "Please select tree node, for example:" + deletingProductType.toString() + ", to delete", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(!currentDeletingNode.isLeaf()){
			JOptionPane.showMessageDialog(mbpnTypeMgrPane, deletingProductType.toString() + " has child type defined.", "Invalid Main No", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		updateTreeNodeDelete(currentDeletingNode);
		List<MbpnProductType> newList = new ArrayList<MbpnProductType>();
		new MbpnTableModel(newList, mbpnTypeMgrPane.getMbpnTypePanel().getTablePanel().getTable(), null,  null, null, true);
		this.refreshProductNoDefPanel();
		mbpnTypeMgrPane.getSaveButton().setEnabled(false);
		
		ProductTypeCatalog.update();
	}

	public void newMbpnProductType() {
		//this mbpn tree function is disabled for now 
		String ownerMainNo = "";
		if(mbpnTypeMgrPane.getMbpnTypePanel().getTablePanel().getTable().getSelectedRowCount() > 0) {
			ownerMainNo = (String)mbpnTypeMgrPane.getMbpnTypePanel().getTablePanel().getTable().getModel().getValueAt(0, 0);
		}
		MbpnProductType newMbpnProdTYpe =  new MbpnProductType();
		List<MbpnProductType> newList = new ArrayList<MbpnProductType>();
		newList.add(newMbpnProdTYpe);
		new MbpnTableModel(newList, mbpnTypeMgrPane.getMbpnTypePanel().getTablePanel().getTable(), getAllMbpnProductTypes(), getAvailableMainNos(), getAvailableMainNos(), true);
		mbpnTypeMgrPane.getMbpnTypePanel().getTablePanel().getTable().setRowSelectionInterval(0, 0);

		mbpnTypeMgrPane.getSaveButton().setEnabled(true);
		updatingProductType = null;

		Logger.getLogger().info("New Mbpn Product Type is called by: ", getUser());
	}

}