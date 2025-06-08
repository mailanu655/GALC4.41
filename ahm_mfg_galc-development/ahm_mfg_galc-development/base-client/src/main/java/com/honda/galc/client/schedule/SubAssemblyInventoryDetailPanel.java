/**
 * 
 */
package com.honda.galc.client.schedule;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Zack chai
 * @date Nov 22, 2013
 */
public class SubAssemblyInventoryDetailPanel extends ApplicationMainPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8618656735808572239L;

	public static final String panel_title = "2SD Weld Sub Assy Inventory Detail";
	
	private String zoneName;
	private String productSpecCode;
	private String orderNo;
	
	private String _panelName = "";
	
	private SubAssemblyInventoryDetailTableModel subAssemblyInventoryDetailTableModel;
	
	TablePane subAssemblyInventoryDetailTablePane;
	
	
	public SubAssemblyInventoryDetailPanel(String zoneName, String productSpecCode, String orderNo) {
		this.zoneName = zoneName;
		this.productSpecCode = productSpecCode;
		this.orderNo = orderNo;
		initializePanel();
	}
	
	protected void initializePanel() {
		
		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BorderLayout());
		
		layoutPanel.add(getLabel(), BorderLayout.NORTH);
		layoutPanel.add(getTablePanel(), BorderLayout.CENTER);
		add(layoutPanel);
	}
	
	private List<MbpnProduct> populateTable() {
		List<MbpnProduct> itemList = ServiceFactory.getDao(MbpnProductDao.class).findSubAssemblyInventoryDetailByZoneNameAndProductSpecCode(zoneName, productSpecCode, orderNo);
		return itemList;
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
		subAssemblyInventoryDetailTablePane = new TablePane(getPanelName());
		subAssemblyInventoryDetailTablePane.setPreferredHeight(500);
		subAssemblyInventoryDetailTablePane.setPreferredWidth(650);
		setSubAssemblyInventoryDetailTableModel(new SubAssemblyInventoryDetailTableModel(populateTable(), subAssemblyInventoryDetailTablePane.getTable()));
		getSubAssemblyInventoryDetailTableModel().pack();
		return subAssemblyInventoryDetailTablePane;
	}
	
	
	private String getPanelName() {
		return _panelName;
	}

	public SubAssemblyInventoryDetailTableModel getSubAssemblyInventoryDetailTableModel() {
		return subAssemblyInventoryDetailTableModel;
	}

	public void setSubAssemblyInventoryDetailTableModel(
			SubAssemblyInventoryDetailTableModel subAssemblyInventoryDetailTableModel) {
		this.subAssemblyInventoryDetailTableModel = subAssemblyInventoryDetailTableModel;
	}
	
}
