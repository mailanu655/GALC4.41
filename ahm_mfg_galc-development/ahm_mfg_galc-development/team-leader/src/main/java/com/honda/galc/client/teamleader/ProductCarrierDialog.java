package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.property.ProductCarrierPropertyBean;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ProductPanel;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;

import com.honda.galc.dao.product.ProductCarrierDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.ProductCarrier;
import com.honda.galc.entity.product.ProductCarrierId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonUtil;

public class ProductCarrierDialog extends JDialog implements ActionListener {
		
	private static final long serialVersionUID = 1L;
	
	private ProductPanel productIdPanel;
	private JPanel buttonPanel;
	private JButton submitButton;
	private JButton resetButton;
	
	private ProductCarrierPropertyBean property;
	private ProductType productType;
	private BaseProduct product;
	private ProductSpec productSpec;
	private ProductTypeData productTypeData;
	private MainWindow window;

	private String carrierId;
	
	public ProductCarrierDialog(Frame owner,String carrierId, MainWindow window,ProductCarrierPropertyBean property) {
		this(owner,carrierId,window,property, false);
	}	
	
	public ProductCarrierDialog(Frame owner,String carrierId,MainWindow window,ProductCarrierPropertyBean property, boolean editcarrierId) {
		super(owner, "Build carrierId",true);
		this.property = property;
		this.carrierId = carrierId;
		this.window=window;
		setSize(1024,230);
		initComponents();
		addListeners();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getMainWindow().addWindowListener(new WindowAdapter() {			
			public void windowOpened(WindowEvent e){				
				getProductIdField().requestFocus();						
			}
		});		
	}
	
	private void initComponents() {		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(getProductIdPanel(),BorderLayout.NORTH);
		panel.add(getButtonPanel(),BorderLayout.SOUTH);
		add(panel,BorderLayout.CENTER);
		reset();
	}
	
	private void addListeners() {
		getProductIdField().addActionListener(this);
		getResetButton().addActionListener(this);
		getSubmitButton().addActionListener(this);	
	}
	
	private Component getButtonPanel() {
		if(buttonPanel == null){
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 120, 10));
			buttonPanel.add(getResetButton());
			buttonPanel.add(getSubmitButton());			
		}
		return buttonPanel;
	}
		
	private JButton getResetButton() {
		if(resetButton == null){
			resetButton = new JButton("Reset");
			resetButton.setName("Reset");
		}
		return resetButton;
	}
	
	private JButton getSubmitButton() {
		if(submitButton == null){
			submitButton = new JButton("Add");
			submitButton.setName("Submit");
		}
		return submitButton;
	}
	
	private ProductPanel getProductIdPanel() {
		if(productIdPanel == null){
			productIdPanel = new ProductPanel(getMainWindow(), getProductTypeData());
		}
		return productIdPanel;	
	}
	
	private UpperCaseFieldBean getProductIdField() {
		return getProductIdPanel().getProductIdField();
	}

	private UpperCaseFieldBean getProductSpecField() {
		return getProductIdPanel().getProductSpecField();
	}
	
	private String getCarrierId() {
		return carrierId;
	}
	
	private ProductTypeData getProductTypeData(){
		if(productTypeData == null){
			for(ProductTypeData type : getMainWindow().getApplicationContext().getProductTypeDataList()){
				if(type.getProductTypeName().equals(property.getProductType())){
					productTypeData = type;
					break;
				}
			}
		}
		
		return productTypeData;
	}

	private ProductType getProductType() {
		if(productType == null){
			productType = ProductType.valueOf(property.getProductType());
		}
		return productType;
	}
	
	private MainWindow getMainWindow() {
		return window;
	}	

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() ==  getResetButton()){
			reset();
		} else if(e.getSource() ==  getProductIdField()){
			loadProductId();
		} else if(e.getSource() ==  getSubmitButton()){
			enterResult();
		}
	}
	
	private void enterResult() {
		Logger.getLogger().info("Start adding Product "+product.getProductId().toString()+" to rack: "+ getCarrierId());
		if(StringUtils.isEmpty(getCarrierId())){
			Logger.getLogger().error("The Rack Id is empty");
			setError("The Rack Id is empty. Enter Rack number..");
			return;
		}
		
		try {			
			ProductCarrier productCarrier=null;
			//check if product is already married to another carrier
			productCarrier=getProductCarrierDao().findfirstProduct(product.getProductId());
			if(productCarrier!=null) {
				String message="Product " + product.getProductId().toString()
						+" already married to Carrier " + productCarrier.getId().getCarrierId().toString();
				showMessage(message);
				return;
			}
			//Check if the carrier has exceed the maximum number of products
			List<ProductCarrier> results=getProductCarrierDao().findEnginesInCarrier(getCarrierId());
			if(isExceedMaxNumberofProductInRack(results.size())){
				String message="Exceed maximum Number of Products("	
						+ property.getNumberOfProductsInRack() 
						+ ") in Carrier "+getCarrierId();
				showMessage(message);
				return;				
			}			
			
			//Check if carrier allowed to married to different products with different Product Spec
			if(property.isAllowMultiProductSpec()){
				for(ProductCarrier result: results){
					if(!product.getProductSpecCode().equals(checkProductOnServer(result.getId().getProductId()).getProductSpecCode())){
						String message="Product " 
								+ product.getProductId() 
								+ " has different product spec than another products in carrier "
								+ getCarrierId();						
						showMessage(message);
						return;
					}
				}
			}
			
			//Add/Update the record 
			String ppid= StringUtils.isEmpty(property.getTrackingProcessPoint().trim()) ? getMainWindow().getApplication().getApplicationId(): property.getTrackingProcessPoint().trim();
			if(productCarrier==null) {
				productCarrier=new ProductCarrier();
				ProductCarrierId productCarrierId=new ProductCarrierId(product.getProductId(),getCarrierId(),new Timestamp(new Date().getTime()));
				productCarrier.setId(productCarrierId);
			}
			productCarrier.getId().setCarrierId(getCarrierId());
			productCarrier.getId().setProductId(product.getProductId());
			productCarrier.setProcessPointId(ppid);

			getProductCarrierDao().save(productCarrier);
			if(!StringUtils.isEmpty(property.getTrackingProcessPoint().trim())){
				getService(TrackingService.class).track(getProductType(), product.getProductId(), ppid);
				Logger.getLogger().info("Tracking status updated for product= "+product.getProductId()+", process point= "+ppid);
			}
			else Logger.getLogger().info("No Tracking Process point. Tracking failed");
			Logger.getLogger().info("Finish adding Product "+product.getProductId().toString()+" to rack: "+ getCarrierId());
			this.setVisible(false);
			this.dispose();				
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception due to:");
			setError(e.toString());
			reset();
		}
	}
	
	private void showMessage(String message){
		MessageDialog.showInfo(this, message);	
		setError(message);
		reset();
	}
	
	private ProductCarrierDao getProductCarrierDao(){
		return ServiceFactory.getDao(ProductCarrierDao.class);
	}
	
	private void loadProductId() {
		try {			
			getMainWindow().clearMessage();
			getProductIdPanel().getProductLookupButton().setEnabled(false);
			
			String productId = getProductIdField().getText();
			Logger.getLogger().info("Entered Product: "+ productId);
			
			checkProductId(productId);
			
			product = checkProductOnServer(productId);
			if(product == null) {
				getProductIdField().requestFocus();
				getProductIdPanel().getProductLookupButton().setEnabled(true);
				throw new TaskException("Invalid product:" + productId);
			}
			checkValidLine(product.getTrackingStatus(),productId);
			getProductIdField().setText(product.getProductId());
			setCursor(new Cursor(Cursor.WAIT_CURSOR));	
			loadProductSpec();
			getProductIdField().setStatus(true);
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			Logger.getLogger().info("Product: "+ productId +" is valid");
		} catch (TaskException e) {
			setError(e.getMessage());
			Logger.getLogger().warn(e.getMessage());
			
			getProductIdField().setStatus(false);		
			return;
		}
	}
	
	private void checkProductId(String productId) {
		if(!productTypeData.isNumberValid(productId)){	
			getProductIdPanel().getProductLookupButton().setEnabled(true);
			throw new TaskException("Invalid product id length:" + productId.length());
		}
	}
	
	private void checkValidLine(String lineId, String productId) {
		if(!StringUtils.isEmpty(property.getValidLines())){
			if(!CommonUtil.isInList(lineId, property.getValidLines())){	
				getProductIdPanel().getProductLookupButton().setEnabled(true);
				throw new TaskException("Product "+productId+" came from invalid Line: " + lineId +". Expected: " + property.getValidLines());
			}
		}
	}
	
	private BaseProduct checkProductOnServer(String productId) {
		try {			
			ProductDao<? extends BaseProduct> productDao = ProductTypeUtil.getProductDao(getProductType());
			return productDao.findByKey(productId);
		} catch (Exception e) {
			String msg = "Failed to load " + property.getProductType() +	": " + productId;
			Logger.getLogger().warn(e, msg);
			throw new TaskException(msg);
		}		
	}
	
	private void loadProductSpec() {
		if(isNewProductSpec(product)){
			productSpec = loadProductSpec(product);
			if(productSpec == null)
				throw new TaskException("Product Spec is not defined:" + product.getProductSpecCode());
		}
		Logger.getLogger().info("Product Spec Code: "+ productSpec );
		renderProductSpecField();

		getResetButton().setEnabled(true);
		getSubmitButton().setEnabled(true);
		
	}
	
	private ProductSpec loadProductSpec(BaseProduct product) {
		BaseProductSpecDao productSpecDao = ProductTypeUtil.getProductSpecDao(productTypeData.getProductType());
		return (ProductSpec)productSpecDao.findByProductSpecCode(product.getProductSpecCode(), property.getProductType());
		
	}
	
	private void renderProductSpecField() {
		getProductSpecField().setText(product.getProductSpecCode());
	}
	
	private boolean isNewProductSpec(BaseProduct product) {
		if(productSpec == null) return true;
		return !product.getProductSpecCode().equals(productSpec.getProductSpecCode());
	}
	
	private boolean isExceedMaxNumberofProductInRack(int noOfProductinRack) {
		if(noOfProductinRack>=property.getNumberOfProductsInRack())	return true;
		else return false;
	}
	
	private void reset() {
		getProductIdPanel().refresh();
		getProductIdField().requestFocus();
		getResetButton().setEnabled(false);
		getSubmitButton().setEnabled(false);
	
		getProductIdPanel().getProductLookupButton().setEnabled(true);
	}

	private void setError(String msg) {
		getMainWindow().getStatusMessagePanel().setErrorMessageArea(msg);
	}
}
