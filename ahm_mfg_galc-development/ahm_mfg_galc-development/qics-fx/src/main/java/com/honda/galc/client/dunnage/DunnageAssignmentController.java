package com.honda.galc.client.dunnage;
/**
 * <h3>DunnageAssignmentController Class description</h3>
 * <p> DunnageAssignmentController description </p>
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
 * @author L&T Infotech<br>
 * May 14, 2017
 *
 */
import static com.honda.galc.client.product.action.ProductActionId.CANCEL;
import static com.honda.galc.client.product.action.ProductActionId.DIRECTPASS;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.qi.base.AbstractQiProcessController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.DunnageUtils;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.BeanUtils;
import com.honda.galc.util.PropertyComparator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

public class DunnageAssignmentController  extends AbstractQiProcessController<DunnageAssignmentModel, DunnageAssignmentView> implements EventHandler<ActionEvent> {

	private ClientAudioManager audioManager;


	public DunnageAssignmentController(DunnageAssignmentModel model, DunnageAssignmentView view) {
		super(model, view);
		this.audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class));
		EventBusUtil.register(this);
	}

	
	public void initEventHandlers() {
		clearMessage();
		addDunnageTableListener();
	}

	
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof UpperCaseFieldBean) {
				if(!getView().getDunnageNumberTextField().getText().isEmpty())
					loadDunnageProducts(getView().getDunnageNumberTextField().getText());
		}
		else if (actionEvent.getSource() instanceof LoggedButton){
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			if ("New Dunnage".equals(loggedButton.getText()))newDunnageAction();
			else if("Change Dunnage".equals(loggedButton.getText()))changeButtonAction();
			else if("Print".equals(loggedButton.getText()))printDunnage();	
		}

		else if (actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			if ("Remove".equals(menuItem.getText()))
				removeDunnage(actionEvent);
		}

	}


	private void addDunnageTableListener() {		
		getView().getDunnageeTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BaseProduct>() {
			public void changed(
					ObservableValue<? extends BaseProduct> observableValue, BaseProduct oldValue, BaseProduct newValue) {
				if(null != newValue){
					ObservableList<BaseProduct> selectedDunnage  =getView().getDunnageeTablePane().getSelectedItems();
					if(selectedDunnage == null || selectedDunnage.isEmpty()) {
						getView().getDunnageeTablePane().getTable().getContextMenu().getItems().clear();
					}else{
						addContextMenuItems();
					}
				}
			}
		});
	}


	private void addContextMenuItems()
	{
		List<String> menuItemsList = new ArrayList<String>();
		BaseProduct selectedDunnage = getView().getDunnageeTablePane().getSelectedItem();	
		if(selectedDunnage!=null){
			menuItemsList.add("Remove");
			getView().getDunnageeTablePane().getTable().getSelectionModel()
			.setSelectionMode(SelectionMode.MULTIPLE);
		}
		getView().getDunnageeTablePane().createContextMenu((String[]) menuItemsList.toArray(new String[menuItemsList.size()]), this);
	}

	/**
	 * This method is used to Autogenerate a Dunnage number
	 */
	private void newDunnageAction() {
		try{
			clearMessage();
			String dunnageNumber = StringUtils.trimToEmpty(getView().getDunnageNumberTextField().getText());
			if (!StringUtils.isEmpty(dunnageNumber) && getView().getDunnageeTablePane().getTable().getItems().size() < getDunnageCartQuantity()) {
				boolean confirm = MessageDialog.confirm(getView().getStage(),"Current dunnage is not full, are you sure you want to start new one ?");

				if(!confirm)
					return;
			}
			getView().getDunnageNumberTextField().setText("");
			String dunnage = generateDunnageNumber();
			getView().getDunnageNumberTextField().setText(dunnage);
			loadDunnageProducts(dunnage);
		} catch (Exception e) {
			handleException("An error occured in newDunnageAction() method", "Failed to create a New Dunnage", e);
		}
	}

	public ProductActionId[] getProductActionIds(){
		if(isCancelBtnDisable())  {
			return new ProductActionId[]{};
		} else 
			return new ProductActionId[]{CANCEL};
	}

	public ProductActionId[] getProductActionIdsOnAccept(){
		if(isCancelBtnDisable())  {
			return new ProductActionId[]{DIRECTPASS};
		} else 
			return new ProductActionId[]{CANCEL,DIRECTPASS};
	}

	/**
	 * This method is used to select the Last generated Dunnage Number.
	 */
	public String selectLastDunnageNumber() {
		String machineId = getModel().getProperty().getMachineId();
		return selectLastDunnageNumber(machineId, null);
	}


	public String selectLastDunnageNumber(String machineId, Date productionDate) {
		StringBuilder sb = new StringBuilder();
		sb.append(machineId);
		if (productionDate != null) {
			sb.append(DunnageUtils.DATE_FORMAT.format(productionDate));
		}
		sb.append("%");

		List<Map<String, Object>> list = getProductDao().selectDunnageInfo(sb.toString(), 1);
		String dunnageNumber = null;
		if (list != null && !list.isEmpty()) {
			Map<String, Object> row = list.get(0);
			Object o = row.get("dunnage");
			dunnageNumber = (String) o;
		}
		return dunnageNumber;
	}

	protected ProductDao<?> getProductDao() {
		return ProductTypeUtil.getProductDao(ProductType.getType(getModel().getProductType()));
	}

	public String generateDunnageNumber() {
		String machineId = getModel().getProperty().getMachineId();
		Date productionDate = getProductionDate();
		int sequence = 1;
		String lastDunnageNumber = selectLastDunnageNumber(machineId, productionDate);
		if (lastDunnageNumber != null) {
			Integer seq = DunnageUtils.parseSequence(lastDunnageNumber);
			if (seq != null) {
				sequence = seq + 1;
			}
		}
		String number = DunnageUtils.format(machineId, productionDate, sequence);
		return number;
	}

	/**
	 * This method is used to get the Production Date.If production Date is null then returns current date.
	 **/

	public Date getProductionDate() {
		Date productionDate = getModel().getProductionDate();

		if (productionDate != null) {
			return productionDate;
		}
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(Calendar.AM_PM, Calendar.AM);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		productionDate = new Date(cal.getTime().getTime());
		return productionDate;
	}

	/**
	 * This method is used to remove the Dunnage
	 */
	private void removeDunnage(ActionEvent actionEvent) {
		try{List<BaseProduct>  products= getView().getDunnageeTablePane().getSelectedItems();
		if (products != null && !products.isEmpty()) {

			if(!MessageDialog.confirm(getView().getStage(),"Are you sure to remove dunnage ?")) return;

			for (Object o : products) {
				BaseProduct product = (BaseProduct) o;
				if (product == null || product.getProductId() == null || product.getProductId().trim().length() == 0) {
					continue;
				}
				getModel().removeDunnageProduct(product);
			}

		}
		loadDunnageProducts(getView().getDunnageNumberTextField().getText());
		} catch (Exception e) {
			handleException("An error occured in removeDunnage() method", "Failed to Remove Dunnage", e);
		}
	}


	private void changeButtonAction() {
		clearMessage();
		getView().getDunnageNumberTextField().setDisable(false);
		getView().getDunnageeTablePane().getTable().getItems().clear();
		getView().getDunnageNumberTextField().setText(StringUtils.EMPTY);
		setButtonState();
	}

	/**
	 * This method is used to load data
	 * @param dunnageNumber
	 */
	public void loadDunnageProducts(String dunnageNumber) {
		try {
			clearMessage();
			String validationMessage = validateDunnageField(getView().getDunnageNumberTextField());
			if(!StringUtils.isEmpty(validationMessage)) {
				displayErrorMessage(validationMessage);
				playNgSound();
				return;
			}
			if(!getView().getDunnageNumberTextField().getText().isEmpty()) {
				List<BaseProduct> list = getModel().findAllDunnageProducts(dunnageNumber);
				getView().getDunnageeTablePane().getTable().getItems().clear();
				if (list != null && !list.isEmpty()) {
					Collections.sort(list, new PropertyComparator<BaseProduct>(BaseProduct.class, "updateTimestamp"));
					Collections.reverse(list);
				}
				getView().getDunnageeTablePane().setData(list);
				setButtonState();
				return;
			}

		}catch (Exception e) {
			handleException("An error occured in loadDunnageProducts() method", "Failed to load data", e);
		}
	}

	/**
	 * This method is used to execute action for Product Button: DIRECT PASS
	 */
	@Subscribe()
	public void onProductEvent(ProductEvent event){
		if (event == null)
			return;
		else{
			String targetObject  = (String) event.getTargetObject();
			if(event.getEventType().equals(ProductEventType.PRODUCT_DIRECT_PASSED) && targetObject.equals(ViewId.DUNNAGE.getViewLabel())){
				try{
					if (!isProductAlreadyOnPallet() && !getView().getDunnageNumberTextField().getText().isEmpty()) {
						int dunnageUpdatedRowCount = submitDunnage();
						if (dunnageUpdatedRowCount == 0) {
							loadDunnageProducts(getView().getDunnageNumberTextField().getText());
							displayErrorMessage("Dunnage completed by another process. Start new Dunnage");
							return;
						}
					}
				}catch (Exception e) {
					handleException("An error occured in Product Direct Pass method", "Failed to Direct Pass", e);
				}
			}
			
			}
		}
	

	/**
	 * This method is used to check whether Product Id is already assigned to the Dunnage.
	 */
	public boolean isProductAlreadyOnPallet() {
		List<? extends BaseProduct> list = getView().getDunnageeTablePane().getTable().getItems();
		if (list == null || list.isEmpty()) {
			return false;
		}
		for (BaseProduct product : list) {
			if (product == null) {
				continue;
			}
			boolean equals = BeanUtils.safeEquals(product.getId(),getModel().getProductId());
			if (equals) {
				return true;
			}
		}
		return false;
	}


	public int submitDunnage() {
		String productId = getModel().getProductId();
		String dunnageNumber = getView().getDunnageNumberTextField().getText();
		int dunnageCapacity= getModel().getDunnagePropertyBean().getDunnageCartQuantity();
		return getModel().updateDunnage(productId, dunnageNumber, dunnageCapacity);

	}

	public Integer getDunnageCartQuantity() {
		if (ProductType.BLOCK.equals(ProductType.getType(getModel().getProductType())))
			return getModel().getDunnagePropertyBean().getBlockDunnageCartQuantity();
		if (ProductType.HEAD.equals(ProductType.getType(getModel().getProductType())))
			return getModel().getDunnagePropertyBean().getHeadDunnageCartQuantity();

		return getModel().getDunnagePropertyBean().getDunnageCartQuantity();
	}


	/**
	 * This method is used to validate dunnage field
	 * @param textField
	 * @return
	 */
	public String validateDunnageField(TextField textField) {
		boolean autoGenerated = getModel().getDunnagePropertyBean().isDunnageNumberAutoGenerated();
		String text = StringUtils.trimToEmpty(textField.getText());
		int dunnageLength = getModel().getDunnagePropertyBean().getDunnageNumberLength();
		int machineIdLength = dunnageLength- DunnageUtils.SEQUENCE_PATTERN.length() - DunnageUtils.DATE_PATTERN.length();

		if (text.length() != dunnageLength) {
			return "Dunnage Field should be "+dunnageLength +" characters long";
		} else if(autoGenerated && getModel().getDunnagePropertyBean().isCheckDunnageId()) {
			String machineId = text.substring(0,machineIdLength);
			String date = text.substring(machineIdLength, machineIdLength+6);
			String serial = text.substring(dunnageLength-3, dunnageLength);

			if (!machineId.equals(getModel().getProperty().getMachineId())) {
				return "Not a valid Machine Name. Expected Machine Id is "+getModel().getProperty().getMachineId();
			}
			if (!isValidDate(date)) {
				return "Not a valid date. The date has to be in 'yyMMdd' format and cannot be future date";
			}
			Pattern serialPattern = Pattern.compile("^(00[1-9]|0[1-9][0-9]|[1-9][0-9][0-9])$");
			Matcher serialMatcher = serialPattern.matcher(serial);
			if (!serialMatcher.matches()) {
				return "Not a valid Serial Number";
			}
		}
		return StringUtils.EMPTY;
	}
	/**
	 * This method is used to validate date
	 * @param inDate
	 * @return
	 */
	private boolean isValidDate(String inDate) {
		java.util.Date inputDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
		dateFormat.setLenient(false);
		try {
			inputDate = dateFormat.parse(inDate.trim());
		} catch (Exception e) {
			return false;
		}
		if (inputDate.after(new java.util.Date()))
			return false;
		return true;
	}

	public void setButtonState(){
		String dunnageNumber = StringUtils.trimToEmpty(getView().getDunnageNumberTextField().getText());
		if (StringUtils.isEmpty(dunnageNumber)) {
			getView().getChangeDunnageButton().setDisable(true);
			getView().getDunnageNumberTextField().setDisable(false);
			getView().getDunnageNumberTextField().requestFocus();
			EventBusUtil.publish(new ProductEvent("NG", ProductEventType.PRODUCT_DIRECTPASS_READY));
		} else {
			getView().getChangeDunnageButton().setDisable(false);
			getView().getDunnageNumberTextField().setDisable(true);

			if (isProductAlreadyOnPallet()) {
				displayErrorMessage("Part already associated to Dunnage");
			} 
			else {
				if (getView().getDunnageeTablePane().getTable().getItems().size() > getDunnageCartQuantity() || getView().getDunnageeTablePane().getTable().getItems().size() == getDunnageCartQuantity() ) {
					displayErrorMessage("Dunnage Pallet is completed.");
					playWarnSound();
					if (getModel().getDunnagePropertyBean().isDunnageNumberAutoGenerated()) {
						getView().getNewDunnageButton().requestFocus();
					} else {
						getView().getChangeDunnageButton().requestFocus();
					}
				} else {
					List<BaseProduct> list = getView().getDunnageeTablePane().getTable().getItems();
					BaseProduct product =getProductModel().getProduct();
					if (!validateModelCode(product.getModelCode(), list)) {
						displayErrorMessage("product: "+product.getProductId() + " model code : "+product.getModelCode()+" does not match Dunnage model ");
						getView().getChangeDunnageButton().requestFocus();
						playNgSound();
						return;
					} else {
						EventBusUtil.publish(new ProductEvent("OK" ,ProductEventType.PRODUCT_DIRECTPASS_READY));
					}
				}
			}

		}
		if(!getDunnagePrinters().isEmpty()){
			getView().getPrintButton().setDisable(false);
		}
	}
	private void playNgSound(){

		if(PropertyService.getPropertyBean(QiPropertyBean.class,ApplicationContext.getInstance().getProcessPointId()).isSoundEnabled())
			getAudioManager().playNGSound();
	}

	private void playWarnSound(){

		if(PropertyService.getPropertyBean(QiPropertyBean.class,ApplicationContext.getInstance().getProcessPointId()).isSoundEnabled())
			getAudioManager().playWarnSound();
	}

	/**
	 * This method is used to validate model code
	 * @param modelCode
	 * @param list of Baseproduct
	 * @return
	 */
	private boolean validateModelCode(String modelCode, List<BaseProduct> list) {
		for (BaseProduct aproduct : list)
			if (modelCode == null || !modelCode.equals(aproduct.getModelCode())) {
				return false;
			}
		return true;
	}

	public ClientAudioManager getAudioManager() {
		return audioManager;
	}

	public void setAudioManager(ClientAudioManager audioManager) {
		this.audioManager = audioManager;
	}

	
	public void initializeListeners() {
		// TODO Auto-generated method stub
		
	}
	
	private void printDunnage() {
		if (getView().getDunnageeTablePane().getTable().getItems().size() == 0) {
			MessageDialog.showError(getView().getStage(), "There are no product(s) to send to printer.");
			return;
		}

		if (!MessageDialog.confirm(getView().getStage(), "Are you sure you want to print ?")) {
			return;
		}

		String dunnageNumber = getView().getDunnageNumberTextField().getText();
		String productTypeName  = getModel().getProductType();
		String processPointId = getModel().getApplicationContext().getProcessPointId();
		List<BaseProduct> data = getView().getDunnageeTablePane().getTable().getItems();
		List<Object> products = new ArrayList<Object>();
		for(BaseProduct item :data){
			products.add(item);
		}
		DunnageUtils.print(dunnageNumber,productTypeName,products,processPointId,getDunnagePrinters());
	}
	private List<BroadcastDestination> getDunnagePrinters(){
		String dunnagePrinter = getModel().getProperty().getDunnagePrinter();
		String dunnageForm = getModel().getProperty().getDunnageForm();
		String applicationId = getModel().getApplicationId();
		return DunnageUtils.getPrinters(dunnagePrinter,dunnageForm,applicationId);
	}

}


