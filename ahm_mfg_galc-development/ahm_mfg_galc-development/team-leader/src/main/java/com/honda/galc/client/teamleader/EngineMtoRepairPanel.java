package com.honda.galc.client.teamleader;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.ProductPanel;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.ValidAvailableSpecForALtMto;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>EngineMtoRepairPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EngineMtoRepairPanel description : Panel used to 
 *     fix the Engine MTO for process of uncommanization process
 * 
 * </p>
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
 * @author K Maharjan
 * Mar 24, 2017
 *
 */
public class EngineMtoRepairPanel extends TabbedPanel implements ActionListener, ListSelectionListener{
	
	private static final long serialVersionUID = 1L;
	private boolean initialized = false;
	
	private ProductPanel productIdPanel;
	private ObjectTablePane<ValidAvailableSpecForALtMto> matchedSpecPanel;
	private JPanel buttonPanel;
	private JButton submitButton;
	private JButton resetButton;
	private List<ValidAvailableSpecForALtMto> ValidAvailableSpecForALtMtos=new ArrayList<ValidAvailableSpecForALtMto>();
	
	public EngineMtoRepairPanel(TabbedMainWindow mainWin) {
		super("Repair Engine MTo", KeyEvent.VK_R, mainWin);
		initialize();
	}	

	private void initialize() {		
		try {
			initialized = true;

			initComponents();
			addListeners();
		
			getMainWindow().addWindowListener(new WindowAdapter() {			
				public void windowOpened(WindowEvent e){				
					getProductIdField().requestFocus();						
				}
			});		
			
		} catch (Exception e) {
			getLogger().error(e, "Exception to start EngineMtoRepairPanel");
		}
	}

	
	private void initComponents() {		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setMaximumSize(getPreferredSize());
		add(getProductIdPanel());		
		add(getMatchedSpecPanel());
		add(getButtonPanel());
		reset();
	}
	
	private void addListeners() {
		getProductIdField().addActionListener(this);
		getResetButton().addActionListener(this);
		getSubmitButton().addActionListener(this);	
	}
	
	private ProductPanel getProductIdPanel() {
		if(productIdPanel == null){
			productIdPanel = new ProductPanel(getMainWindow(), getProductTypeData());
		}
		return productIdPanel;	
	}	
	
	
	
	private ObjectTablePane<ValidAvailableSpecForALtMto> getMatchedSpecPanel() {
		
		if(matchedSpecPanel == null){
			matchedSpecPanel=createMatchedSpecTablePane();
		}
		return matchedSpecPanel;
	}
	
	private ObjectTablePane<ValidAvailableSpecForALtMto> createMatchedSpecTablePane() {
		ColumnMappings dftColumnMappings = ColumnMappings.with("#", "row_reverted")
				.put("Year", "modelYearCode")
				.put("Model", "modelCode")
				.put("Model Type", "modelTypeCode")
				.put("Engine Mto", "engineMto")
				.put("Alt Engine Mto", "altEngineMto");
		return new ObjectTablePane<ValidAvailableSpecForALtMto>("Matched Spec for Alt MTO",dftColumnMappings.get(),true,true);
	}
	
	public ObjectTablePane<ValidAvailableSpecForALtMto> getMatchedSpecPane() {
		return matchedSpecPanel;
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
			submitButton = new JButton("Apply");
			submitButton.setName("Submit");
		}
		return submitButton;
	}
	
	private UpperCaseFieldBean getProductIdField() {
		return getProductIdPanel().getProductIdField();
	}
	
	private UpperCaseFieldBean getProductSpecField() {
		return getProductIdPanel().getProductSpecField();
	}
	
	private ProductTypeData getProductTypeData(){
		return ServiceFactory.getDao(ProductTypeDao.class).findByKey(ProductType.ENGINE.name());
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
	
	private void assignMto(){
		EngineDao engineDao =  ServiceFactory.getDao(EngineDao.class);
		Engine engine = engineDao.findByKey(getProductIdField().getText().trim());
		String previousEngineSpec = engine.getProductSpecCode().toString().trim();
		if(StringUtils.isEmpty(engine.getVin())){		
			ValidAvailableSpecForALtMto mto = getMatchedSpecPane().getSelectedItem();
			if(!StringUtils.equals(mto.getEngineMto().toString().trim(),previousEngineSpec)){
				engine.setProductSpecCode(mto.getEngineMto());
				engineDao.update(engine);			
				getLogger().info("Product spec of Engine "+ engine.getProductId() +" is updated to "+ mto.getEngineMto() +" from : "+ previousEngineSpec);
			}
				reset();
				getMainWindow().getStatusMessagePanel().setMessage("Product spec of Engine "+ engine.getProductId() +" is updated to "+ mto.getEngineMto() +" from: "+ previousEngineSpec);
		}
		else{
			setError("Can't proceed the request since Engine "+ engine.getProductId() +" is already assigned to VIN "+ engine.getVin());
		}
	}
	
	private void enterResult() {
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure? Updating Engine spec with actual expected spec.", "Engine MTO repair", dialogButton);
		if(dialogResult == 0) {
			getLogger().info("Yes Option selected");
			assignMto();		  
		} else { 
			getLogger().info("No Option selected");
		}  
		
	}
	
	private void loadProductId() {
		try {			
			window.clearMessage();
			getProductIdPanel().getProductLookupButton().setEnabled(false);
			
			String productId = getProductIdField().getText();
			Logger.getLogger().info("Entered Product: "+ productId);
			
			checkProductId(productId);
			
			Engine product = checkProductOnServer(productId);
			if(product == null) {
				getProductIdField().requestFocus();
				getProductIdPanel().getProductLookupButton().setEnabled(true);
				throw new TaskException("Invalid product:" + productId);
			}

			getProductIdField().setText(product.getProductId());
			setCursor(new Cursor(Cursor.WAIT_CURSOR));	
			loadProductSpec(product);
			LoadMatchedSpec(product);
			getProductIdField().setStatus(true);
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			Logger.getLogger().info("Product: "+ productId +" is valid");
		} catch (TaskException e) {
			setError(e.getMessage());
			getLogger().warn(e.getMessage());			
			getProductIdField().setStatus(false);		
		}
	}
	
	/**
	 * Description
	 * 
	 * void
	 */
	private void LoadMatchedSpec(Engine product) {
		try{
			ValidAvailableSpecForALtMtos=ServiceFactory.getDao(FrameSpecDao.class).findAllByAltEngineMto(product.getProductSpecCode());
			getMatchedSpecPane().reloadData(ValidAvailableSpecForALtMtos);
			if(ValidAvailableSpecForALtMtos.isEmpty()){
				getSubmitButton().setEnabled(false);
				setError("Unable to find any other valid Spec for this engine");
				getProductIdField().requestFocus();
			}
		}catch(Exception e){
			setError(e.toString());
			getMatchedSpecPane().refresh();
		}
	}

	private void checkProductId(String productId) {
		if(!getProductTypeData().isNumberValid(productId)){	
			getProductIdPanel().getProductLookupButton().setEnabled(true);
			throw new TaskException("Invalid product id length:" + productId.length());
		}
	}

	private Engine checkProductOnServer(String productId) {
		try {			
			return getEngine(productId);
		} catch (Exception e) {
			String msg = "Failed to load: " + productId;
			Logger.getLogger().warn(e, msg);
			throw new TaskException(msg);
		}		
	}
	
	private void loadProductSpec(Engine product) {
		
		Logger.getLogger().info("Product Spec Code: "+ product.getProductSpecCode());
		renderProductSpecField(product.getProductSpecCode());

		getResetButton().setEnabled(true);
		getSubmitButton().setEnabled(true);
		
	}
	
	private void renderProductSpecField(String spec) {
		getProductSpecField().setText(spec);
	}
	
	private void reset() {
		getProductIdPanel().refresh();
		getProductIdField().requestFocus();
		getResetButton().setEnabled(false);
		getSubmitButton().setEnabled(false);
	
		getProductIdPanel().getProductLookupButton().setEnabled(true);
		window.clearMessage();
		getMatchedSpecPane().reloadData(null);
		
	}

	private void setError(String msg) {
		window.getStatusMessagePanel().setErrorMessageArea(msg);
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()){
			return;
		}
		
    	ListSelectionModel lsm = (ListSelectionModel) e.getSource();
    	if (!lsm.isSelectionEmpty()) {
            getLogger().debug("Product : "+getMatchedSpecPane().getSelectedString()+" is selected");
        }
		
	}
	
	public Engine getEngine(String productId){
		return ServiceFactory.getDao(EngineDao.class).findByKey(productId);
	}

	@Override
	public void onTabSelected() {
		Logger.getLogger().info("Engine MTO repair selected");
		if(initialized) return;		
		initialize();		
	}	
	
}
