/**
 * 
 */
package com.honda.galc.client.schedule;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.ProductPriorityPlanDao;
import com.honda.galc.entity.enumtype.PlanStatus;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.ProductPriorityPlan;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Zack chai
 * @date Nov 22, 2013
 */
public class SubAssemblyInventoryPanel extends ApplicationMainPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4833036979603638842L;

	private static final String panel_title = "2SD Weld Sub-Assy Inventory Summary";
	
	private String _panelName = "";
	
	private String _btnName = "Refresh";
	
	private SubAssemblyInventoryTableModel subAssemblyInventoryTableModel;
	
	private TablePane subAssemblyInventoryTablePane;
	
	private SubAssemblyInventoryDetailDialog dialog;
	
	private Dimension winDimension = null;
	
	public SubAssemblyInventoryPanel(DefaultWindow defaultWin) {
		super(defaultWin);
		winDimension = defaultWin.getPreferredSize();
		initializePanel();
	}
	
	protected void initializePanel() {
		
		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BorderLayout());
		
		layoutPanel.add(getLabel(), BorderLayout.NORTH);
		layoutPanel.add(getTablePanel(), BorderLayout.CENTER);
		layoutPanel.add(getRefreshButton(), BorderLayout.SOUTH);
		add(layoutPanel);
	}
	
	
	private List<MbpnProduct> populateTable() {
		String division = getProperty("DIVISION_ID").trim();
		List<MbpnProduct> itemList = ServiceFactory.getDao(MbpnProductDao.class).findSubAssemblyInventoryByDivisonId(division);
		List<Object[]> productPriorityPlanList = ServiceFactory.getDao(ProductPriorityPlanDao.class).findOrderNoAndActualByDivisionIdAndPlanStatus(division, PlanStatus.ASSIGNED);
		populateProductProgressActual(itemList, productPriorityPlanList);
		return itemList;
	}
	
	
	private void populateProductProgressActual(List<MbpnProduct> productProgressItemList, List<Object[]> productPriorityPlanList){
		if(productProgressItemList.isEmpty() || productPriorityPlanList.isEmpty()){
			return;
		}
		for(MbpnProduct item : productProgressItemList){
			item.setActual(getActualFromList(item.getCurrentOrderNo().trim(), productPriorityPlanList));
		}
	}
	
	private int getActualFromList(String currentOrderNo, List<Object[]> productPriorityPlanList){
		for(Object[] obj : productPriorityPlanList){
			if(((String)obj[0]).trim().equals(currentOrderNo)){
				return ((Integer)obj[1]).intValue();
			}
		}
		return 0;
	}
	
	
	private JPanel getRefreshButton(){
		JPanel panel = new JPanel(new BorderLayout());
		JButton btn = new JButton(_btnName);
		btn.setFont(Fonts.DIALOG_PLAIN_18);
		btn.setSize(150, 30);
		btn.addActionListener(this);
		panel.add(btn, BorderLayout.EAST);
		return panel;
	}
	
	private JLabel getLabel(){
		JLabel titleLabel = new JLabel(panel_title);
		titleLabel.setVisible(true);
		Font font = new Font(null, Font.BOLD, 24);
		titleLabel.setFont(font);
		titleLabel.setPreferredSize(new Dimension(100,60));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		return titleLabel;
	}
	
	private TablePane getTablePanel(){
		subAssemblyInventoryTablePane = new TablePane(getPanelName());
		subAssemblyInventoryTablePane.setPreferredHeight(500);
		subAssemblyInventoryTablePane.setPreferredWidth((int)winDimension.getWidth()-50);
		setSubAssemblyInventoryTableModel(new SubAssemblyInventoryTableModel(populateTable(), subAssemblyInventoryTablePane.getTable()));
		getSubAssemblyInventoryTableModel().pack();
		
		subAssemblyInventoryTablePane.getTable().addMouseListener(new RowMouseHandler());
		
		return subAssemblyInventoryTablePane;
	}
	
	public void refresh() {
		getSubAssemblyInventoryTableModel().refresh(populateTable());
	}
	
	private String getPanelName() {
		return _panelName;
	}
	
	
	public SubAssemblyInventoryTableModel getSubAssemblyInventoryTableModel() {
		return subAssemblyInventoryTableModel;
	}

	public void setSubAssemblyInventoryTableModel(SubAssemblyInventoryTableModel subAssemblyInventoryTableModel) {
		this.subAssemblyInventoryTableModel = subAssemblyInventoryTableModel;
	}

	public void actionPerformed(ActionEvent e) {
		JButton jbtn = (JButton)e.getSource();
		if(jbtn.getText().equals(_btnName)){
			refresh();
		}
	}
	
	
	private class RowMouseHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
        	// if right button of mouse click
        	if(e.isMetaDown()){
        		int row = getSelectedRow();
                if (row == -1) return;
                String zone = (String)getSubAssemblyInventoryTableModel().getValueAt(row, 0);
                String productSpecCode = (String)getSubAssemblyInventoryTableModel().getValueAt(row, 2);
                String orderNo = (String)getSubAssemblyInventoryTableModel().getValueAt(row, 4);
                getDialog(zone.trim(), productSpecCode.trim(), orderNo);
        	}
        }
        
        private int getSelectedRow() {
        	return subAssemblyInventoryTablePane.getTable().getSelectedRow();
        }
    }
	
	
	private SubAssemblyInventoryDetailDialog getDialog(String zoneName, String productSpecCode, String orderNo){
		if(dialog != null){
			dialog.setZoneName(zoneName);
			dialog.setProductSpecCode(productSpecCode);
			dialog.setOrderNo(orderNo);
			dialog.initComponents();
			return dialog;
		}
		dialog = new SubAssemblyInventoryDetailDialog(this, zoneName, productSpecCode, orderNo);
		return dialog;
	}
}
