package com.honda.galc.client.teamleader.knuckle;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.tablemodel.KeyValueTableModel;
import com.honda.galc.client.ui.tablemodel.SubProductShippingDetailTableModel;
import com.honda.galc.client.ui.tablemodel.SubProductShippingTableModel;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.SubProductShippingDao;
import com.honda.galc.dao.product.SubProductShippingDetailDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.EntityCache;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.SubProductShipping;
import com.honda.galc.entity.product.SubProductShippingDetail;
import com.honda.galc.entity.product.SubProductShippingId;
import com.honda.galc.service.printing.KnuckleShippingPrintingUtil;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.SortedArrayList;


public class KnuckleShippingQueryPanel extends TabbedPanel implements ListSelectionListener{

	
	private static final long serialVersionUID = 1L;
	
	private LabeledTextField knuckleInputField = new LabeledTextField("FIND KSN:",true);
	
	private List<SubProductShipping> shippings;
	private List<SubProductShippingDetail> shippingDetails;
	
	@SuppressWarnings("unchecked")
	private List<KeyValue> caseNumbers = new ArrayList<KeyValue>();
	
	private SubProductShippingTableModel shippingTableModel;
	private SubProductShippingDetailTableModel shippingDetailTableModel;
	private KeyValueTableModel caseNumberTableModel;
	
	private TablePane shippingTablePane;
	private TablePane shippingDetailTablePane;
	private TablePane caseNumberTablePane;
	
	private JButton reprintButton = new JButton("Reprint Packing List");
	
	private KnuckleShippingPrintingUtil printingUtil;
	
	public KnuckleShippingQueryPanel(){
		super("Knuckle Shipping Query", KeyEvent.VK_Q);
		initComponents();
		addListeners();
		
	}
	
	private void initComponents() {
		
		setLayout(new BorderLayout());
		add(createKnuckleInputField(),BorderLayout.NORTH);
		add(createLowerPanel(),BorderLayout.CENTER);
		
	}
	
	private void addListeners() {
		shippingTablePane.getTable().getSelectionModel().addListSelectionListener(this);
		knuckleInputField.getComponent().addActionListener(this);
		reprintButton.addActionListener(this);
		
	}
	
	private JPanel createLowerPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createShippingPane(),BorderLayout.NORTH);
		JPanel aPanel = new JPanel(new BorderLayout());
		aPanel.add(createLowerLeftPanel(),BorderLayout.WEST);
		aPanel.add(createShippingDetailPane(),BorderLayout.CENTER);
		panel.add(aPanel,BorderLayout.CENTER);
		return panel;
	}

	private TablePane createShippingPane() {
		
		shippingTablePane = new TablePane("Shipping Kd Lot List");
		shippingTablePane.setPreferredHeight(200);
		shippingTablePane.setMaxHeight(200);
		
		shippingTableModel = new SubProductShippingTableModel(shippingTablePane.getTable(),shippings);
		
		return shippingTablePane;
	}
	
	private JPanel createLowerLeftPanel() {
		
		JPanel panel = new JPanel(new BorderLayout());
		JPanel subPanel = new JPanel(new BorderLayout());
		reprintButton.setFont(Fonts.DIALOG_BOLD_24);
		subPanel.add(reprintButton,BorderLayout.EAST);
		panel.add(subPanel,BorderLayout.SOUTH);
		panel.add(createCaseNumberTablePane(),BorderLayout.NORTH);
		//added July 16th, 2012
		subPanel.setVisible(true);
		reprintButton.setVisible(true);
		return panel;
	}
	
	
	private TablePane createShippingDetailPane() {
	
		shippingDetailTablePane = new TablePane("Shipping Kd Lot List");
		shippingDetailTablePane.setPreferredWidth(600);
		shippingDetailTableModel = new SubProductShippingDetailTableModel(shippingDetailTablePane.getTable(),shippingDetails);
		
        return 	shippingDetailTablePane;	
	}
	
	private TablePane createCaseNumberTablePane() {
		caseNumberTablePane = new TablePane();
		caseNumberTableModel = new KeyValueTableModel(caseNumbers,"Side","Case Number",caseNumberTablePane.getTable());
		return caseNumberTablePane;
	}
	

	private Component createKnuckleInputField() {
		knuckleInputField.setFont(Fonts.DIALOG_PLAIN_33);
		
		return knuckleInputField;
	}

	
	@Override
	public void onTabSelected() {
		if(!isInitialized){
			
			isInitialized = true;
		}
		
		shippings = getDao(SubProductShippingDao.class).findAllKuckleShippingAndShipped();
		shippingTableModel.refresh(shippings);
		shippingTablePane.clearSelection();
		shippingTablePane.getTable().getSelectionModel().setSelectionInterval(0, 0);
	}
	
	

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == knuckleInputField.getComponent()) ksnInputed();
		if(e.getSource() == reprintButton) reprint();

	}
	
	@SuppressWarnings("unchecked")
	private void reprint() {
		
		List<SubProductShipping> shippingLots =  shippingTableModel.getSelectedItems();
		
		KeyValue<String,Integer> keyValue = caseNumberTableModel.getSelectedItem();
		if(keyValue == null || shippingLots.isEmpty()) return;
		
		List<SubProductShippingDetail> details = getCurrentCartShippingDetails(keyValue.getKey(),keyValue.getValue());

		getPrintingUtil().print(shippingTableModel.getSelectedItems(),details,getProcessLocation(shippingLots.get(0)));
		logUserAction("printed Knuckle shipping details");
	}
	
	private String getProcessLocation(SubProductShipping shippingLot) {
		PreProductionLot lot = getDao(PreProductionLotDao.class).findByKey(shippingLot.getProductionLot());
		return lot.getProcessLocation();
	}
	
	private KnuckleShippingPrintingUtil getPrintingUtil() {
		if(printingUtil == null) {
			printingUtil = new KnuckleShippingPrintingUtil();
		}
		return printingUtil;
	}
	
	private List<SubProductShippingDetail> getCurrentCartShippingDetails(String side,int caseNumber) {
		
		List<SubProductShippingDetail> details = getShippingDetails(side);
		
		List<SubProductShippingDetail> cartDetails = new ArrayList<SubProductShippingDetail>();
		int index = (caseNumber -1) * getCartSize();
		
		
		for(int i = 0;i<getCartSize();i++) {
			if(index + i < details.size())
				cartDetails.add(details.get(index + i));
			else break;
		}
		
		return cartDetails;
	}
	
	private List<SubProductShippingDetail> getShippingDetails(String side) {
		
		List<SubProductShippingDetail> filteredShippingDetails = new SortedArrayList<SubProductShippingDetail>("getProductSeqNo");
		for(SubProductShippingDetail item : shippingDetails) {
			if(item.getId().getSubId().equals(side)) filteredShippingDetails.add(item);
		}
		return filteredShippingDetails;
	}
	
	private void ksnInputed() {			
		setErrorMessage("");		
		String ksn = knuckleInputField.getComponent().getText();	    
		boolean isOk = verifyProduct(ksn);
		
		knuckleInputField.getComponent().selectAll();
	    
		if(!isOk) return;
		
		SubProductShippingDetail shippingDetail = getDao(SubProductShippingDetailDao.class).findByProductId(ksn);
		
		if(shippingDetail == null) {
			setErrorMessage("Knuckle " + ksn + " is not shipped");
			return;
		}
//		added - July 16, 2012-##################
		SubProductShipping shippingLot = new EntityCache<SubProductShipping,SubProductShippingId>(shippings).get(
				new SubProductShippingId(shippingDetail.getId().getKdLotNumber(),shippingDetail.getProductionLot()));
		
		shippingTableModel.selectItem(shippingLot);
	}
	
	private int getCartSize() {
		return PropertyService.getPropertyInt("KNUCKLE SHIPPING", "CART SIZE", 15);
	}
	
	private void setKnuckleInputFieldFocus() {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() { 
            	knuckleInputField.getComponent().requestFocusInWindow();
            };
        });
	}




	public void valueChanged(ListSelectionEvent e) {
		setErrorMessage("");	
		if(e.getValueIsAdjusting()) return;
		
		List<SubProductShipping> shippingLots = shippingTableModel.getSelectedItems();
			
		shippingDetails = findSameKdLotShippingDetails(shippingLots); 
		shippingDetailTableModel.refresh(shippingDetails);			
		refreshCaseNumberTablePane(getKdLotSize(shippingLots));
		shippingDetailTablePane.getTable().getSelectionModel().setSelectionInterval(0, 0);
		shippingDetailTablePane.makeSelectedItemVisible();
			// to avoid exception
		SwingUtilities.invokeLater(new Runnable() {
			public void run() { 
				shippingTablePane.getTable().updateUI();
			};
		});
			
		knuckleInputField.getComponent().requestFocusInWindow();
		
	}
	
	private int getKdLotSize(List<SubProductShipping> shippingLots) {
		int lotSize =0;
		for(SubProductShipping shipping : shippingLots) {
			lotSize += shipping.getSchQuantity();
		}
		return lotSize;
	}
	
	private List<SubProductShippingDetail> findSameKdLotShippingDetails(List<SubProductShipping> shippingLots) {
		List<SubProductShippingDetail> shippingDetails = new ArrayList<SubProductShippingDetail>();
		for(SubProductShipping item: shippingLots) {
			shippingDetails.addAll(getDao(SubProductShippingDetailDao.class).findAllByKdLotNumber("KNUCKLE",item.getKdLotNumber()));
		}
		List<SubProductShippingDetail> filteredShippingDetails = new ArrayList<SubProductShippingDetail>();
		for(SubProductShippingDetail detail : shippingDetails) {
			if(containsProductionLot(shippingLots,detail.getProductionLot()))
					filteredShippingDetails.add(detail);
		}
		return filteredShippingDetails;
	}
		
	private boolean containsProductionLot(List<SubProductShipping> shippingLots,String productionLot) {
		for(SubProductShipping shipping : shippingLots) {
			if(shipping.getProductionLot().equalsIgnoreCase(productionLot)) return true;
		}
		return false;
	}
	
	private void refreshCaseNumberTablePane(int lotSize) {
		
		int count = ((lotSize -1 )/ (getCartSize() * 2)) + 1;
		caseNumbers.clear();
		for(int i=0;i<count;i++) {
			caseNumbers.add(new KeyValue<String,Integer>(Product.SUB_ID_LEFT,i+1));
		}
		for(int i=0;i<count;i++) {
			caseNumbers.add(new KeyValue<String,Integer>(Product.SUB_ID_RIGHT,i+1));
		}
		caseNumberTableModel.refresh(caseNumbers);
		caseNumberTablePane.getTable().getSelectionModel().setSelectionInterval(0, 0);
	}
	
	// override this to resolve the problem to set focus for text field
	public void setVisible(boolean b) {
        super.setVisible(b);
        setKnuckleInputFieldFocus();
    }
	
	
	private boolean verifyProduct(String ksn) {
		if(StringUtils.isEmpty(ksn)){
	    	setErrorMessage("Knuckle serial number cannot be blank");
	    	return false;
	    }
	    if(ksn.length() != ProductType.KNUCKLE.getProductIdLength()) {
	    	setErrorMessage("Knuckle serial number length is " + ksn.length() + " Should be " + ProductType.KNUCKLE.getProductIdLength());
	    	return false;
	    }
	    
	    return true;
	}

}
