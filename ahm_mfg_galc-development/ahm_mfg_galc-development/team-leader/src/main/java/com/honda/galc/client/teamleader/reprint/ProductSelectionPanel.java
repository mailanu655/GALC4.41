package com.honda.galc.client.teamleader.reprint;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.honda.galc.client.teamleader.property.ReprintPropertyBean;
import com.honda.galc.client.ui.MainPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * <h3>ProductSelectionPanel Class description</h3>
 * <p> ProductSelectionPanel description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Apr 19, 2011
 *
 *
 */
public abstract class ProductSelectionPanel extends MainPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	protected JLabel selectionLabel;
	
	protected ButtonGroup selectionGroup;
	
	protected JPanel selectionPanel;
	
	protected JPanel productionDatePanel;

	protected DateSelectionPanel dateSelectionPanel;

	protected JButton productionDateOkButton;
	protected JButton prodLotOkButton;
	
	protected ObjectTablePane<ProductionLot> prodLotTablePane;
	
	protected JPanel itemListContainer;
	
	protected ObjectTablePane<? extends BaseProduct> objectTablePane;
	
	protected List<String> processLocations = new ArrayList<String>();
	
	protected ReprintPropertyBean reprintPropertyBean;
	
	
	public ProductSelectionPanel(String selectionStr, String[] selections, JPanel itemListContainer) {
		
		this.itemListContainer = itemListContainer;
		initComponents(selectionStr,selections);
		
	}
	
	protected void initComponents(String selectionStr, String[] selections) {
		
		
		setLayout(new BorderLayout());
		
		
		JPanel panel = new JPanel(new BorderLayout(0,10));
		panel.add(createSelectionLabel(selectionStr),BorderLayout.NORTH);
		panel.add(createSelectionGroup(selections),BorderLayout.SOUTH);

		ViewUtil.setInsets(panel, 50, 0, 0, 0);
		
		add(panel,BorderLayout.NORTH);
		selectionPanel = new JPanel(new BorderLayout());
		ViewUtil.setInsets(selectionPanel, 50, 0, 0, 0);
		add(selectionPanel,BorderLayout.CENTER);
		
		
	}
	
	protected JLabel createSelectionLabel(String selectionStr) {
		
		selectionLabel = new JLabel(selectionStr);
		selectionLabel.setFont(Fonts.DIALOG_BOLD_16);
		
		selectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		return selectionLabel;
		
	}
	
	protected JPanel createSelectionGroup(String[] selections) {
		
		JPanel optionPanel = new JPanel();
		optionPanel.setLayout(new BoxLayout(optionPanel,BoxLayout.Y_AXIS));
		if(selections == null) return optionPanel;
		optionPanel.setAlignmentX(LEFT_ALIGNMENT);
		selectionGroup = new ButtonGroup();
		
		boolean isFirst = true;
		
		for(String option : selections) {
			JRadioButton radioButton = new JRadioButton(option);
			radioButton.setAlignmentX(LEFT_ALIGNMENT);
			radioButton.setActionCommand(option);
			if(isFirst) {
				radioButton.setSelected(true);
				isFirst = false;
			}
			selectionGroup.add(radioButton);
			optionPanel.add(radioButton);
		}
		
		return optionPanel;
	}
	
	public ReprintPropertyBean getReprintPropertyBean() {
		return reprintPropertyBean;
	}

	public void setReprintPropertyBean(ReprintPropertyBean reprintPropertyBean) {
		this.reprintPropertyBean = reprintPropertyBean;
	}

	protected void addProductSelectionPanel(JPanel aPanel) {
		
		selectionPanel.removeAll();
		selectionPanel.add(aPanel,BorderLayout.CENTER);
		ViewUtil.setInsets(selectionPanel, 50, 0, 0, 0);
		selectionPanel.updateUI();
		
	}
	

	protected void createProductionLotTablePane(List<ProductionLot> prodLots) {
		
		ColumnMappings columnMappings  = ColumnMappings.with("KD Lot","kdLotNumber");
		columnMappings.put("Lot Number","lotNumber").put("Lot Size", "lotSize");
		
		//allow sorting
		prodLotTablePane = new ObjectTablePane<ProductionLot>(columnMappings.get(),false,true);
		prodLotTablePane.setAlignment(SwingConstants.CENTER);
		prodLotTablePane.setPreferredWidth(500);
		prodLotTablePane.reloadData(new SortedArrayList<ProductionLot>(prodLots,"getKdLotNumber"));
		
	}

	protected void createProductionDatePanel() {
		
		productionDateOkButton = createOkButton();
		
		prodLotOkButton = createOkButton();

		productionDatePanel = new JPanel(new BorderLayout());
		TitledBorder titledBorder = new TitledBorder("3.Select by Production Date");
		titledBorder.setTitleFont(Fonts.DIALOG_BOLD_16);
		productionDatePanel.setBorder(titledBorder);
		
		
		dateSelectionPanel = new DateSelectionPanel();
		
		dateSelectionPanel.setAlignmentY(BOTTOM_ALIGNMENT);
		dateSelectionPanel.setFont(Fonts.DIALOG_BOLD_14);
		
		JPanel upperPanel = new JPanel(new BorderLayout());
		JPanel p1 = new JPanel(new BorderLayout());
		p1.add(productionDateOkButton,BorderLayout.SOUTH);
		upperPanel.add(dateSelectionPanel,BorderLayout.WEST);
		upperPanel.add(p1,BorderLayout.EAST);
		
		productionDatePanel.add(upperPanel,BorderLayout.NORTH);
		
		createProductionLotTablePane(null);
		
		JPanel lowerPanel = new JPanel(new BorderLayout());
		lowerPanel.add(prodLotTablePane,BorderLayout.CENTER);
		
		JPanel p2 = new JPanel(new BorderLayout());
		p2.add(prodLotOkButton,BorderLayout.SOUTH);
		
		lowerPanel.add(p2,BorderLayout.EAST);
		
		productionDatePanel.add(lowerPanel,BorderLayout.CENTER);
	}

	protected JButton createOkButton() {
		
		JButton okButton = new JButton("OK");
		okButton.setAlignmentY(BOTTOM_ALIGNMENT);
		return okButton;
	}

	
	
	public void clearAllProductSections() {
		if(objectTablePane != null)
			objectTablePane.clearSelection();
	}
	
	public void selectAllProducts() {
		if(objectTablePane != null)
			objectTablePane.getTable().selectAll();
	}
	
	public List<? extends BaseProduct> getSelectedProducts() {
		
		List<? extends BaseProduct> products = new ArrayList<BaseProduct>();
		if(objectTablePane == null) return products;
		
		return objectTablePane.getSelectedItems();
		
	}
	
	protected void productionDateSelected() {
		prodLotTablePane.reloadData(findProductionLots());
		prodLotTablePane.clearSelection();
		objectTablePane.reloadData(null);

	}
	
	protected List<ProductionLot> findProductionLots() {
		Date date = dateSelectionPanel.getDate();	
		List<ProductionLot> prodLots = new SortedArrayList<ProductionLot>("getKdLotNumber");
		for(String processLocation :processLocations) {
			List<ProductionLot> lots = getDao(ProductionLotDao.class).findAll(processLocation,date);
			prodLots.addAll(lots);
		}
		return prodLots;
	}
	
	public void actionPerformed(ActionEvent e) {
		clearErrorMessage();
		try{
			doActionPerformed(e);
		}catch(Exception ex) {
			handleException(ex);
		}
		
	}
	
    public abstract void doActionPerformed(ActionEvent e);

}
