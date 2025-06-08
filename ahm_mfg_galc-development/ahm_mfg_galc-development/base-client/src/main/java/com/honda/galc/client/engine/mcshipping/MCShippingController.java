package com.honda.galc.client.engine.mcshipping;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.mvc.AbstractController;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductShipping;
import com.honda.galc.util.CommonPartUtility;

/**
 * 
 * 
 * <h3>MCShippingController Class description</h3>
 * <p> MCShippingController description </p>
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
 * Sep 10, 2014
 *
 *
 */
public class MCShippingController extends AbstractController<MCShippingModel, MCShippingView> implements ActionListener, ListSelectionListener {

	
	public MCShippingController(MCShippingModel model, MCShippingView view) {
		super(model, view);
	}

	public void activate() {
		getView().mapActions();
	}

	public void actionPerformed(ActionEvent e) {
		clearMessage();
		try{
			if(e.getSource().equals(getView().trailerComboBox.getComponent()))
				trailerNumberSelected();
			else if(e.getSource().equals(getView().dunnageTextField.getComponent()))
				dunnageInputed();
			else if(e.getSource().equals(getView().newButton))
				newTrailer();
			else if(e.getSource().equals(getView().modelFilterButton))
				MCShippingModelFilter.getInstance(this.getView().getModel().getPropertyBean().getAuthorizationGroup(),this.getView().getModel().getPropertyBean().getBuildAttrbuteKey());
			else if(e.getSource().equals(getView().removeButton))
				removeDunnage();
			else if(e.getSource().equals(getView().completeButton))
				completeTrailer();
		}catch(Exception ex) {
			showAndLogErrorMessage("Exception occured : " + ex.getMessage());
		}
	}
	
	public void loadActiveDunnages() {
		String trailerNumber = getSelectedTrailerNumber();
		getModel().reloadActiveDunnages();
		getView().trailerComboBox.getComponent().setSelectedIndex(-1);
		getView().trailerComboBox.setModel(getModel().getActiveTrailers(), 0);
		getView().trailerComboBox.getComponent().setSelectedItem(trailerNumber);
		setComponentStatus();
	}
	
	private boolean loadDunnageToTrailer(String dunnage, String trailerNumber) {
		List<String> prodTypesFromProperties = getModel().loadProductTypesFromProperties();
		//all dunnages already on trailer
		List<ProductShipping> dunnagesOnTrailer = getModel().selectDunnages(trailerNumber);
		//all products in the dunnage to be loaded onto trailer
		List<BaseProduct> products = getModel().getAllProductsInDunnage(dunnage, prodTypesFromProperties);
		
		getModel().validateDunnageInfo(dunnage, trailerNumber, dunnagesOnTrailer, products);
		
		//all product types in dunnage to be loaded onto trailer
		Map<String, String> productTypesInDunnage = getModel().findAllProdTypesInDunnage(dunnage, prodTypesFromProperties);
		String currentProduct = productTypesInDunnage.get("current");
		String otherProduct = productTypesInDunnage.get("other");
		
		//get total current and other product type dunnages on trailer
		int totalCurrentProductOnTrailer = getModel().countDunnagesByType(ProductType.getType(currentProduct), trailerNumber);
		int totalOtherProductOnTrailer = getModel().countDunnagesByType(ProductType.getType(otherProduct), trailerNumber);
		
		//get first product in dunnage (since dunnage can only have one product type)
		try {
			//no need to do this validation for every product in dunnage since dunnage can only have one product type
			getModel().validateAndCheckProduct(products.get(0), totalCurrentProductOnTrailer, totalOtherProductOnTrailer, dunnage);
		} catch (TaskException e) {
			//catch TaskException and show confirm dialog if DunnageSize TaskException is caught
			//show error message if any other TaskException is caught
			if (e.getTaskName() == null || !e.getTaskName().equals("Dunnage Size")) {
				showAndLogErrorMessage(e.getMessage());
				return false;
			} else {
				String message = e.getMessage() + ". Do you want to continue?";
				if (MessageDialog.confirm(getView().getMainWindow(), message)) {
					String authGroup = null;
					try {
						authGroup = getView().getModel().getPropertyBean().getPartialDunnageAuthorizationGroup();
					} finally {
						if (authGroup == null || !isAccessPermitted(authGroup)) {
							showAndLogErrorMessage("User not authorized to load dunnage. " + e.getMessage());
							return false;
						}
					}
				} else {
					return false;
				}
			}
		}
		
		//validate dunnage number against dunnage mask defined in configuration
		String dunnageIDMask = getView().getModel().getPropertyBean().getDunnageIdMask();
		if (!StringUtils.isEmpty(dunnageIDMask)) {
			if (!CommonPartUtility.simpleVerification(dunnage, dunnageIDMask)) {
				showAndLogErrorMessage("Dunnage number does not match dunnage ID mask.");
				return false;
			}
		}
		
		//if validations fail, below will not be executed
		Date shipDate = !dunnagesOnTrailer.isEmpty() ? dunnagesOnTrailer.get(0).getShipDate()
				: new Date(Calendar.getInstance().getTime().getTime());
		
		getModel().saveProducts(products, trailerNumber, shipDate);
		
		return true;
		
	}

	private void trailerNumberSelected() {
		String trailerNumber = getSelectedTrailerNumber();
		
		/** check trailer number
		 */
		try {
			getModel().checkTrailer(trailerNumber);
		} catch (Exception e) {
			showAndLogErrorMessage(e.getMessage());
			return;
		}
		
		
		List<ProductShipping> headDunnages= getModel().selectDunnages(ProductType.HEAD,trailerNumber);
		List<ProductShipping> blockDunnages= getModel().selectDunnages(ProductType.BLOCK,trailerNumber);
		List<ProductShipping> conrodDunnages= getModel().selectDunnages(ProductType.CONROD,trailerNumber);
		List<ProductShipping> crankshaftDunnages= getModel().selectDunnages(ProductType.CRANKSHAFT,trailerNumber);
		int countHead = getModel().getTotalCount(ProductType.HEAD,trailerNumber);
		int countBlock = getModel().getTotalCount(ProductType.BLOCK,trailerNumber);
		int countConrd = getModel().getTotalCount(ProductType.CONROD,trailerNumber);
		int countCrankshaft = getModel().getTotalCount(ProductType.CRANKSHAFT,trailerNumber);
		
		getView().headDunnageList.reloadData(headDunnages);
		getView().blockDunnageList.reloadData(blockDunnages);
		getView().conrodDunnageList.reloadData(conrodDunnages);
		getView().crankshaftDunnageList.reloadData(crankshaftDunnages);
		getView().headDunnageCountField.getComponent().setText(Integer.toString(countHead));
		getView().blockDunnageCountField.getComponent().setText(Integer.toString(countBlock));
		getView().conrodDunnageCountField.getComponent().setText(Integer.toString(countConrd));
		getView().crankshaftDunnageCountField.getComponent().setText(Integer.toString(countCrankshaft));
		getView().totalCountField.getComponent().setText(Integer.toString(countHead + countBlock + countConrd + countCrankshaft));
		setComponentStatus();
	}

	private void dunnageInputed() {
		String dunnage = getDunnageNumber();
		getLogger().info("Dunnage " + dunnage + " is received");
		try{
		    getModel().checkDunnage(dunnage);
			if (loadDunnageToTrailer(dunnage,getSelectedTrailerNumber())) {
				//only execute this if dunnage loaded to trailer successfully
				loadActiveDunnages();
				getView().dunnageTextField.getComponent().setText("");
				getView().dunnageTextField.getComponent().requestFocus();
				showAndLogInfo("dunnage " + dunnage + " is saved into trailer " + getSelectedTrailerNumber() + " successfully");
			}
		}catch(TaskException ex) {
			showAndLogErrorMessage(ex.getMessage());
		}catch(Exception ex) {
			showAndLogErrorMessage("Exception Occured : " + ex.getMessage());
		}finally{
			setComponentStatus();
		}
		
	}
	
	private String getSelectedTrailerNumber() {
		String trailerNumber = (String)getView().trailerComboBox.getComponent().getSelectedItem();
		return StringUtils.trim(trailerNumber);
	}
	
	private String getDunnageNumber() {
		String dunnage = getView().dunnageTextField.getComponent().getText();
		return StringUtils.trim(dunnage);
	}

	private void newTrailer() {
		getModel().checkActiveTrailerCount();
		getView().trailerComboBox.getComponent().setSelectedIndex(-1);
		getView().trailerComboBox.getComponent().setEditable(true);
	}

	private void removeDunnage() {
		ProductShipping productShipping = getView().headDunnageList.getSelectedItem();
		if(productShipping == null) productShipping = getView().blockDunnageList.getSelectedItem();
		if(productShipping == null) productShipping = getView().conrodDunnageList.getSelectedItem();
		if(productShipping == null) productShipping = getView().crankshaftDunnageList.getSelectedItem();
		if(productShipping == null) return;
		if(MessageDialog.confirm(getView().getMainWindow(), "Are you sure to remove Dunnage " + productShipping.getDunnage() + "?")){
			getModel().removeDunnage(getSelectedTrailerNumber(), productShipping.getDunnage());
			loadActiveDunnages();
			showAndLogInfo("Dunnage " + productShipping.getDunnage() + " is removed from trailer " + getSelectedTrailerNumber());
		}
	}

	private void completeTrailer() {
		String trailerNumber = getSelectedTrailerNumber();
		if(StringUtils.isEmpty(trailerNumber)) return;
		if(!MessageDialog.confirm(getView().getMainWindow(), "Are you sure to complete the trailer " + trailerNumber + "?")) 
			return;
			
		getView().getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try{
			getModel().checkConsumedDunnage(trailerNumber);
			getModel().completeTrailer(trailerNumber);
			getView().trailerComboBox.getComponent().setSelectedIndex(-1);
			loadActiveDunnages();
			showAndLogInfo("Complete the trailer " + trailerNumber + " successfully");
		}finally{
			getView().getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	private void setComponentStatus() {
		String trailerNumber = getSelectedTrailerNumber();
		boolean isTrailerNumberEmpty = StringUtils.isEmpty(trailerNumber);
		boolean notEmpty = !isTrailerNumberEmpty && !getModel().selectDunnages(trailerNumber).isEmpty();
		boolean isDunnageSelected = getView().headDunnageList.getSelectedItem() != null 
			|| getView().blockDunnageList.getSelectedItem() != null || getView().conrodDunnageList.getSelectedItem() != null 
					|| getView().crankshaftDunnageList.getSelectedItem() != null;
		
		getView().dunnageTextField.getComponent().setEditable(!isTrailerNumberEmpty);
		getView().dunnageTextField.getComponent().setEnabled(!isTrailerNumberEmpty);
		getView().dunnageTextField.getComponent().setFocusable(!isTrailerNumberEmpty);
		getView().completeButton.setEnabled(notEmpty);
		if(isTrailerNumberEmpty) getView().trailerComboBox.getComponent().requestFocusInWindow();
		else {
			getView().dunnageTextField.getComponent().selectAll();
			getView().dunnageTextField.getComponent().requestFocusInWindow();
		}
		getView().trailerComboBox.getComponent().setEditable(false);
		getView().removeButton.setEnabled(isDunnageSelected);
		
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) return;

		clearMessage();
		if(e.getSource().equals(getView().blockDunnageList.getTable().getSelectionModel())) {
			if(getView().blockDunnageList.getTable().getSelectedRowCount() > 0)
				getView().headDunnageList.getTable().clearSelection();
		}else if(e.getSource().equals(getView().headDunnageList.getTable().getSelectionModel())) {
			if(getView().headDunnageList.getTable().getSelectedRowCount() > 0)
				getView().blockDunnageList.getTable().clearSelection();
		}else if(e.getSource().equals(getView().conrodDunnageList.getTable().getSelectionModel())) {
			if(getView().conrodDunnageList.getTable().getSelectedRowCount() > 0)
				getView().crankshaftDunnageList.getTable().clearSelection();
		}else if(e.getSource().equals(getView().crankshaftDunnageList.getTable().getSelectionModel())) {
			if(getView().crankshaftDunnageList.getTable().getSelectedRowCount() > 0)
				getView().conrodDunnageList.getTable().clearSelection();
		}
		setComponentStatus();
	}
	
	private static Boolean isAccessPermitted(String authGroup){
		if(LoginDialog.login() == com.honda.galc.enumtype.LoginStatus.OK){
			if(ClientMain.getInstance().getAccessControlManager().isAuthorized(authGroup)) return true;
		}
		return false;
	}

}
