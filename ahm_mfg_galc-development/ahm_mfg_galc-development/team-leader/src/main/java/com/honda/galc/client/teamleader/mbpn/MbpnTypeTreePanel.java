package com.honda.galc.client.teamleader.mbpn;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.honda.galc.client.ui.component.SimpleTree;
import com.honda.galc.dao.product.MbpnProductTypeDao;
import com.honda.galc.entity.product.MbpnProductType;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>MbpnTypeTreePanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MbpnTypeTreePanel description </p>
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
 * <TD>May 26, 2017</TD>
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
 * @since May 26, 2017
 */
public class MbpnTypeTreePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private JTree tree;
	private JScrollPane scrollPanel;
	private DefaultMutableTreeNode top;
	private TreeMap<String, List<MbpnProductType>> mbpnProductTypeMap;
    
  

	public MbpnTypeTreePanel() {
    	setLayout(new BorderLayout());
		//Create the nodes.
        top = new DefaultMutableTreeNode("MBPN Types");
        this.loadMbpnProductTypes();
        this.refreshNodes();

        //Create a tree that allows one selection at a time.
        tree = new SimpleTree(top);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        scrollPanel = new JScrollPane(tree);
        this.add(scrollPanel, BorderLayout.CENTER);
        
        expandAllNodes();
    }
	
	private void loadMbpnProductTypes() {
		this.mbpnProductTypeMap = new TreeMap<String, List<MbpnProductType>>();
		List<MbpnProductType> mbpnProductTypes = ServiceFactory.getDao(MbpnProductTypeDao.class).findAll();
		for(MbpnProductType mbpnProductType : sortMbpnProductTypes(mbpnProductTypes)){
			this.addMbpnProductTypeToMap(mbpnProductType);
		}
	}
	
	private List<MbpnProductType> sortMbpnProductTypes(List<MbpnProductType> mbpnProductTypeList){
		List<MbpnProductType> result = new ArrayList<MbpnProductType>();
		TreeMap<String, MbpnProductType> sortedTypes = new TreeMap<String, MbpnProductType>();
		for (MbpnProductType type : mbpnProductTypeList) {
			sortedTypes.put(type.getId().getMainNo(), type);
		}
		result.addAll(sortedTypes.values());
		return result;
	}
	
	private void addMbpnProductTypeToMap(MbpnProductType mbpnProductType) {
		if(!this.mbpnProductTypeMap.containsKey(mbpnProductType.getProductType()))
			this.mbpnProductTypeMap.put(mbpnProductType.getProductType(), new ArrayList<MbpnProductType>());
		
		List<MbpnProductType> mbpnProductTypeList = this.mbpnProductTypeMap.get(mbpnProductType.getProductType());
		mbpnProductTypeList.add(mbpnProductType);
		this.mbpnProductTypeMap.put(mbpnProductType.getProductType(), sortMbpnProductTypes(mbpnProductTypeList));
	}
	
	public void addNode(MbpnProductType mbpnProductType) {
		this.addMbpnProductTypeToMap(mbpnProductType);
		this.refreshNodes();
		this.reload();
	}
	
	private void removeMbpnProductTypeFromMap(MbpnProductType mbpnProductType) {
		if(!this.mbpnProductTypeMap.containsKey(mbpnProductType.getProductType())) return;
		List<MbpnProductType> mbpnProductTypes = this.mbpnProductTypeMap.get(mbpnProductType.getProductType());
		for (MbpnProductType productType : mbpnProductTypes){
			if (productType.getId().equals(mbpnProductType.getId())) {
				mbpnProductTypes.remove(productType);
				break;
			}
		}
		if (mbpnProductTypes.isEmpty())
			this.mbpnProductTypeMap.remove(mbpnProductType.getProductType());
		else 
			this.mbpnProductTypeMap.put(mbpnProductType.getProductType(), mbpnProductTypes);
	}
	
	public void removeNode(MbpnProductType mbpnProductType) {
		this.removeMbpnProductTypeFromMap(mbpnProductType);
		this.refreshNodes();
		this.reload();
	}

	public void refreshNodes() {
		this.top.removeAllChildren();
		for(String productType : this.getMbpnProductTypeMap().keySet()) {
			DefaultMutableTreeNode productTypeNode = new DefaultMutableTreeNode(productType);
			this.top.add(productTypeNode);
			for(MbpnProductType mbpnProductType : this.mbpnProductTypeMap.get(productType)){
				productTypeNode.add(new DefaultMutableTreeNode(mbpnProductType));
			}
		}	
	}

	public void expandAllNodes() {
	    int j = tree.getRowCount();
	    int i = 0;
	    while(i < j) {
	        tree.expandRow(i);
	        i += 1;
	        j = tree.getRowCount();
	    }
	}
	
	public void reload() {
		DefaultTreeModel root = (DefaultTreeModel)getTree().getModel();
		root.reload((TreeNode)root.getRoot());
		expandAllNodes();
	}
	
	// ================ getter/setter ==========================
	
	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}

	public DefaultMutableTreeNode getTop() {
		return top;
	}

	public void setTop(DefaultMutableTreeNode top) {
		this.top = top;
	}

	private TreeMap<String, List<MbpnProductType>> getMbpnProductTypeMap() {
		if(this.mbpnProductTypeMap == null)
			this.mbpnProductTypeMap = new TreeMap<String, List<MbpnProductType>>();
		return this.mbpnProductTypeMap;
	}
}