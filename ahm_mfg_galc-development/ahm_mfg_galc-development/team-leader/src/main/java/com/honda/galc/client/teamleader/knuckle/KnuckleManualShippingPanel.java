package com.honda.galc.client.teamleader.knuckle;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.tablemodel.SubProductShippingDetailTableModel;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.dao.product.SubProductShippingDao;
import com.honda.galc.dao.product.SubProductShippingDetailDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.EntityCache;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.BuildAttributeId;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.entity.product.SubProductShipping;
import com.honda.galc.entity.product.SubProductShippingDetail;
import com.honda.galc.entity.product.SubProductShippingId;
import com.honda.galc.service.TrackingService;

/**
 * 
 * <h3>KnuckleManualShippingPanel Class description</h3>
 * <p> KnuckleManualShippingPanel description </p>
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
 * Dec 9, 2010
 *
 *
 */

public class KnuckleManualShippingPanel extends TabbedPanel implements ListSelectionListener{

	private static final long serialVersionUID = 1L;
	
	private static String P1_SHIPPING_PPID = "AF0FK16601";
	
	private LabeledTextField knuckleInputField = new LabeledTextField("KSN:",false);
	
	private LabeledTextField partNumberField = new LabeledTextField("Part Number Mask: ",false);
	
	
	private List<SubProductShippingDetail> shippingDetails;
	private SubProductShippingDetailTableModel shippingDetailTableModel;
	private TablePane shippingDetailTablePane;
	
	private BuildAttributeCache buildAttributeList;
	
	private EntityCache<SubProductShipping,SubProductShippingId> subProductShippingList;
	
	
	public KnuckleManualShippingPanel(){
		super("Knuckle Manual Shipping", KeyEvent.VK_S);
		initComponents();
		addListeners();
		
	}
	
	


	private void initComponents() {
		
		setLayout(new BorderLayout());
		add(createShippingDetailPane(),BorderLayout.WEST);
		add(createRightPanel(),BorderLayout.CENTER);
		
	}
	
	private void addListeners() {
		
		shippingDetailTablePane.getTable().getSelectionModel().addListSelectionListener(this);
		knuckleInputField.getComponent().addActionListener(this);
		
	}

	private TablePane createShippingDetailPane() {
	
		shippingDetailTablePane = new TablePane("Shipping Kd Lot List");
		shippingDetailTablePane.setPreferredWidth(600);
		shippingDetailTableModel = new SubProductShippingDetailTableModel(shippingDetailTablePane.getTable(),shippingDetails);
		
        return 	shippingDetailTablePane;	
	}
	
	private JPanel createRightPanel() {
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new TitledBorder("Knuckle Input"));
		
		JPanel subPanel = new JPanel(new BorderLayout());
		subPanel.add(createKnuckleInputField(),BorderLayout.NORTH);
		subPanel.add(createPartNumberField(),BorderLayout.SOUTH);
		panel.add(subPanel,BorderLayout.NORTH);
		
		return panel;
		
	}
	
	


	private Component createKnuckleInputField() {
		knuckleInputField.setFont(Fonts.DIALOG_PLAIN_33);
		
		return knuckleInputField;
	}

	
	private Component createPartNumberField() {
		
		partNumberField.setFont(Fonts.DIALOG_PLAIN_33);
		partNumberField.getComponent().setBackground(Color.CYAN);
		partNumberField.getComponent().setEditable(false);
		return partNumberField;
		
	}

	
	@Override
	public void onTabSelected() {
		if(!isInitialized){
			
			shippingDetails = getDao(SubProductShippingDetailDao.class).findAllNotShipped("KNUCKLE");
			
			buildAttributeList = new BuildAttributeCache(
					BuildAttributeTag.KNUCKLE_LEFT_SIDE,
					BuildAttributeTag.KNUCKLE_RIGHT_SIDE);
			subProductShippingList = new EntityCache<SubProductShipping, SubProductShippingId>(getDao(SubProductShippingDao.class));
			shippingDetailTableModel.refresh(shippingDetails);
			shippingDetailTablePane.getTable().getSelectionModel().setSelectionInterval(0, 0);
			isInitialized = true;
		}
		
	}
	
	

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == knuckleInputField.getComponent()) ksnInputed();

	}
	
	private void ksnInputed() {
		
		String ksn = knuckleInputField.getComponent().getText();
	    
		boolean isOk = verifyProduct(ksn);
		
	//	knuckleInputField.getComponent().setBackground(!isOk ? Color.RED : Color.WHITE);
		knuckleInputField.getComponent().selectAll();
	    
		if(!isOk) return;
		
		saveKnuckle(ksn);
		
		
		setMessage( "Knuckle " + ksn + " saved successfully");
		
		
	
	}
	
	private void saveKnuckle(String ksn) {
		
		SubProductShippingDetail shippingDetail = shippingDetailTableModel.getSelectedItem();
		if(shippingDetail == null) return;
		
		shippingDetail.setProductId(ksn);
		getDao(SubProductShippingDetailDao.class).save(shippingDetail);
		logUserAction(SAVED, shippingDetail);
		
		SubProductShipping subProductShipping = subProductShippingList.findByKey(
				new SubProductShippingId(shippingDetail.getId().getKdLotNumber(),
						shippingDetail.getProductionLot()));
		if(subProductShipping == null) return;
		subProductShipping.incrementActQuantity();
		getDao(SubProductShippingDao.class).update(subProductShipping);
		logUserAction(UPDATED, subProductShipping);
		
		shippingDetails.remove(shippingDetail);
		shippingDetailTableModel.refresh(shippingDetails);
		
		getLogger().info("Knuckle " + ksn + " saved successfully for kd lot " + shippingDetail.getId().getKdLotNumber());
		
		getService(TrackingService.class).track(ProductType.KNUCKLE, ksn, getShippingProcessPointId());
		clearErrorMessage();	
	}

	
	private void setKnuckleInputFieldFocus() {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() { 
            	knuckleInputField.getComponent().requestFocusInWindow();
            };
        });
	}




	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		
		clearErrorMessage();
		SubProductShippingDetail shippingDetail = shippingDetailTableModel.getSelectedItem();
		if(shippingDetail != null) {
			SubProductShipping item = subProductShippingList.findByKey(
					new SubProductShippingId(shippingDetail.getId().getKdLotNumber(),shippingDetail.getProductionLot()));
			if(item == null) return;
			String partNumber = null;
			if(shippingDetail.getId().getSubId().equals(Product.SUB_ID_LEFT)){
				partNumber = getKnucklePrefix(item.getProductSpecCode(),BuildAttributeTag.KNUCKLE_LEFT_SIDE); 
			}else 
				partNumber = getKnucklePrefix(item.getProductSpecCode(),BuildAttributeTag.KNUCKLE_RIGHT_SIDE); 
			
			partNumberField.getComponent().setText(partNumber);
			knuckleInputField.getComponent().setText("");
		}
		
		knuckleInputField.getComponent().requestFocusInWindow();
		
	}
	
	// override this to resolve the problem to set focus for text field
	public void setVisible(boolean b) {
        super.setVisible(b);
        setKnuckleInputFieldFocus();
    }
	
	
	private String getKnucklePrefix(String productSpecCode, String tag) {
		
		String mto = ProductSpec.trimColorCode(productSpecCode);
		BuildAttribute buildAttribute = buildAttributeList.findByKey(new BuildAttributeId(tag,mto)); 
		if(buildAttribute == null) return null;
		return buildAttribute.getAttributeValue()+ProductSpec.extractModelYearCode(mto);

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
	    
	    if(StringUtils.isEmpty(partNumberField.getComponent().getText())) {
	    	setErrorMessage("No production Lot to be shipped");
	    	return false;
	    }
	    if(!SubProduct.getPartNumberPrefix(ksn).equals(partNumberField.getComponent().getText())) {
	    	setErrorMessage("Knuckle serial number's part number prefix should be " + partNumberField.getComponent().getText());
	    	return false;
	    }
	    
	    // check if the knuckle is loaded already
	    SubProductShippingDetail shippingDetail = getDao(SubProductShippingDetailDao.class).findByProductId(ksn);
	    if(shippingDetail != null) {
	    	setErrorMessage("Knuckle serial number is already loaded to kd lot : " + shippingDetail.getId().getKdLotNumber());
	    	return false;
	    }
	    
 // check if the knuckle serial is valid
	    
	    
	    SubProduct subProduct = getDao(SubProductDao.class).findByKey(ksn);
	    if(subProduct == null) {
	    	setErrorMessage("KSN " + ksn + " does not exist");
	    	return false;
	    }
	    
	    // check if it is a knuckle product
	    if(!subProduct.getProductType().equals(ProductType.KNUCKLE)) {
	    	setErrorMessage("KSN " + ksn + " is not a knuckle product and is a " + subProduct.getProductType());
	    	return false;
	    }
	    
	    // check if the knuckle has missing required parts
	    List<String> missingRequiredParts = getDao(RequiredPartDao.class).findMissingRequiredParts(
	    		           subProduct.getProductSpecCode(), getShippingProcessPointId(), ProductType.KNUCKLE,ksn,subProduct.getSubId());
	    if(!missingRequiredParts.isEmpty()) {
	    	setErrorMessage("Missing required parts : " + missingRequiredParts.toString());
	    	return false;
	    }
	    return true;
	    
	}
	
	private String getShippingProcessPointId() {
		 ProcessPoint processPoint= getMainWindow().getApplicationContext().getTerminal().getProcessPoint();
		 return processPoint != null? processPoint.getProcessPointId() : P1_SHIPPING_PPID;
	}

}
