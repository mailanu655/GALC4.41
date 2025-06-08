package com.honda.galc.client.product.pane;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.mvc.AbstractView;
import com.honda.galc.client.product.NavigateTabEvent;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.process.AbstractProcessView;
import com.honda.galc.client.schedule.EntryDepartmentEvent;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.event.SessionEvent;
import com.honda.galc.client.ui.event.SessionEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.QiInspectionUtils;
import com.honda.galc.client.utils.QiProgressBar;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.ProcessPointType;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ReflectionUtils;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Tab;

/**
 * 
 * 
 * <h3>QicsFxIdlePane Class description</h3>
 * <p> QicsFxIdlePane description </p>
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
 * @author Shweta Kadav<br>
 * Oct 6, 2016
 *
 *
 */
public class QiProductIdlePane extends AbstractProductIdlePane{

	private boolean isNavigationFromDefectEntry = false;
	private boolean isProductShipped = false;
	private boolean isProductPrevoiusLineInvalid = false;
	private ProcessPoint processPoint;
	private QiPropertyBean propertyBean;
	
	private AbstractProcessView<?,?> homeScreen=null;

	public QiProductIdlePane(ProductController productController) {
		super(productController);
		propertyBean = PropertyService.getPropertyBean(QiPropertyBean.class, productController.getModel().getApplicationContext().getProcessPointId());
		EventBusUtil.register(this);
		ProcessPointDao ppDao = ServiceFactory.getDao(ProcessPointDao.class);
		processPoint = ppDao.findByKey(getProductController().getProcessPointId());
	}
	
	
	@Override
	protected void initComponents() {
		setCenter(getHomeScreenWindow());
		disableAllTabs(false);
		if(isRepairStation())  {
			disableTab(ViewId.DEFECT_ENTRY.getViewLabel());
		}
		EventBusUtil.publishAndWait(new ProductEvent(null, ProductEventType.PRODUCT_RESET));
	}


	private Node getHomeScreenWindow() {		
		if(homeScreen==null)
		{		
			try {
				String className = "com.honda.galc.client.qi.homescreen.HomeScreenView";
				Class<?> clazz = Class.forName(className);
				homeScreen = (AbstractProcessView<?, ?>) ReflectionUtils.createInstance(clazz, getProductController().getView().getMainWindow());
			} catch (Exception e) {
				throw new SystemException("Unable to get tabbled javafx pane class from the class name", e);
			}		   
		}
		else
		{
			homeScreen.getController().getView().reload();
		}
		homeScreen.getModel().setProductModel(getProductController().getModel());
		return homeScreen;
	}

	@Subscribe
	public void isDirectPassedOrDone(ProductEvent event){
		if (null!=event && (event.getEventType().equals(ProductEventType.PRODUCT_DEFECT_DONE) || event.getEventType().equals(ProductEventType.PRODUCT_DIRECT_PASSED))) {
			isNavigationFromDefectEntry = true;
		}

	}
	
	@Subscribe
	public void isProductShipped(ProductEvent event){
		if (null!=event && (event.getEventType().equals(ProductEventType.PRODUCT_SHIPPED))) {
			isProductShipped = true;
		}
	}
	
	@Subscribe
	public void isProductPrevoiusLineInvalid(ProductEvent event){
		if (null!=event && (event.getEventType().equals(ProductEventType.PRODUCT_PREVIOUS_LINE_INVALID))) {
			isProductPrevoiusLineInvalid = true;
		}
	}

	@Override
	public void toIdle() {
		initComponents();
		resetPreCheck();
		if(isNavigationFromDefectEntry && PropertyService.getPropertyBean(QiPropertyBean.class,ApplicationContext.getInstance().getProcessPointId()).switchUserAfterScan()){
			Platform.runLater(new Runnable() {
				public void run() {
					LoginStatus loginStatus = LoginDialog.login(ClientMainFx.getInstance().getStage( getProductController().getModel().getApplicationContext().getApplicationId()), isLoginBypassAllowed(), isLoginBypassAllowed());

					if(loginStatus != LoginStatus.OK){
						return;
					} else {
						ClientMainFx.getInstance().resetContext();
						EventBusUtil.publish(new EntryDepartmentEvent(ApplicationContext.getInstance().getUserId(), QiConstant.ASSOCIATE_ID_SELECTED));
					}
				}
			});
		}
		EventBusUtil.publish(new SessionEvent(SessionEventType.SESSION_END));
	}

	private boolean isLoginBypassAllowed() {
		return PropertyService.getPropertyBean(ApplicationPropertyBean.class, ApplicationContext.getInstance().getProcessPointId()).isMapCardNumber() ? false : true;
	}

	public boolean isRepairStation()  {
		if(processPoint != null && processPoint.getProcessPointType() == ProcessPointType.QICSRepair)  {
			return true;
		}
		return false;
	}
	
	@Subscribe()
	public void onProductEvent(final ProductEvent event) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				AbstractView<?, ?> view = (AbstractView<?, ?>) getProductController().getView().getProductProcessPane().getSelectedProcessView();
				if (null!=event && event.getEventType().equals(ProductEventType.PRODUCT_INPUT_RECIEVED)) {
					disableAllTabs(false);
					if (!view.getViewLabel().equals(ViewId.DUNNAGE.getViewLabel()))  {
						disableTab(ViewId.DUNNAGE.getViewLabel());
					}
					disableTab(ViewId.PRODUCT_CHECK.getViewLabel());
					if(isRepairStation())  {
						disableTab(ViewId.DEFECT_ENTRY.getViewLabel());
					}
				}
				else if(null!=event && (event.getEventType().equals(ProductEventType.PRODUCT_DIRECT_PASSED) 
						|| event.getEventType().equals(ProductEventType.PRODUCT_DEFECT_DONE))) { 
					if(isProductPrevoiusLineInvalid){
						isProductPrevoiusLineInvalid = false;
						return;
					}
					if(isProductShipped){
						return;
					}
					if(QiInspectionUtils.isGdpProcessPoint(processPoint.getProcessPointId())){
						getProductController().updateGdpDefects();
					}
					if (getProductController().getView().getProductProcessPane().isNavigatedFromRepairEntry()) {
						EventBusUtil.publishAndWait(new ProductEvent("SUBMIT", ProductEventType.PRODUCT_REPAIR_DEFECT_DONE));
					} else {
						getProductController().resetQuantity();
						if (isTabPresent(ViewId.PRODUCT_CHECK.getViewLabel()) && (isListEmpty(propertyBean.getProductWarnCheckTypes())
								|| isListEmpty(propertyBean.getProductCheckTypes())) ) {
							if (!view.getViewLabel().equals(ViewId.PRODUCT_CHECK.getViewLabel())) {
								if (!view.getViewLabel().equals(ViewId.DUNNAGE.getViewLabel()))  {
									disableAllTabs(true);
									enableTab(ViewId.PRODUCT_CHECK.getViewLabel());
									navigateToTab(ViewId.PRODUCT_CHECK.getViewLabel());
								}
								
							}else {
								getProductController().finishProduct();
							}
								
						}
						
						else if(propertyBean.isDunnage() && !view.getViewLabel().equals(ViewId.DUNNAGE.getViewLabel()) ){
							navigateToTab(ViewId.DUNNAGE.getViewLabel());
						}
						
						else {
							getProductController().finishProduct();
						}
					}
				}
				else if(event.getEventType().equals(ProductEventType.PRODUCT_DIRECTPASS_READY) && view.getViewLabel().equals(ViewId.DUNNAGE.getViewLabel())) {
					String targetObject  = (String) event.getTargetObject();
					if(targetObject.equals("OK")){
						getProductController().getView().setProductButtons(view.getController().getProductActionIdsOnAccept());
					}else if(targetObject.equals("NG")){
						getProductController().getView().setProductButtons(view.getController().getProductActionIds());
					}
				} else if (event.getEventType().equals(ProductEventType.PRODUCT_CHECK_DONE)) {
					disableAllTabs(false);			
					QiProgressBar qiProgressBar = QiProgressBar.getInstance("Performing Done action on Product Check View.","Performing Done action on Product Check View",getProductController().getModel().getProductId(),null,true);
					try{
						qiProgressBar.initializeProgressBarStage(view.getMainWindow().getStage());
						qiProgressBar.showMe();
						getProductController().finishProduct();
					}
					finally{
						if(qiProgressBar != null)  {
							qiProgressBar.closeMe();
						}
						
					}
						
				} else if (event.getEventType().equals(ProductEventType.PRODUCT_SEND_TO_FINAL)) {
					disableAllTabs(false);			
					getProductController().getModel().setSendToFinal(true);
					getProductController().finishProduct();
				} 
				
				if (view.getViewLabel().equals(ViewId.DEFECT_ENTRY.getViewLabel()))
					view.resetFocus();
				
				isProductShipped=false;
			}
		});
	}

	/**
	 * This method will disable the specified tab.
	 * 
	 * @param tabName
	 */
	private void disableTab(String tabName) {
		for (Tab tab : getProductController().getView().getProductProcessPane().getTabPane().getTabs()) {
			if (tab.getText().equalsIgnoreCase(tabName)) {
				tab.setDisable(true);
			}
		}
	}
	
	/**
	 * This method will enable the specified tab.
	 * 
	 * @param tabName
	 */
	private void enableTab(String tabName) {
		for (Tab tab : getProductController().getView().getProductProcessPane().getTabPane().getTabs()) {
			if (tab.getText().equalsIgnoreCase(tabName)) {
				tab.setDisable(false);
			}
		}
	}
	
	private void disableAllTabs(boolean isDisable) {
		for (Tab tab : getProductController().getView().getProductProcessPane().getTabPane().getTabs()) {
			tab.setDisable(isDisable);
		}
	}
	
	private void resetPreCheck() {
		Tab preCheckTab = getTab(ViewId.PRE_CHECK.getViewLabel());
		if(preCheckTab != null)  {
			AbstractView<?,?> myView = (AbstractView<?,?>)(preCheckTab.getContent());
			myView.initView();
		}
	}
	
	private Tab getTab(String tabLabel) {
		Tab myTab = null;
		if(StringUtils.isBlank(tabLabel))  return myTab;
		for (Tab tab : getProductController().getView().getProductProcessPane().getTabPane().getTabs()) {
			if(tab.getText().equalsIgnoreCase(tabLabel))  {
				myTab = tab;
				break;
			}
		}
		return myTab;
	}
	
	/**
	 * This method is used to Enable and Navigate to tab provided.
	 * 
	 * @param tabName
	 */
	public void navigateToTab(String tabName) {
		for (Tab tab : getProductController().getView().getProductProcessPane().getTabPane().getTabs()) {
			if (tab.getText().equalsIgnoreCase(tabName)) {
				tab.setDisable(false);
				getProductController().getView().getProductProcessPane().getTabPane().getSelectionModel().select(tab);
			}
		}
		
	}


	public boolean isTabPresent(String tabName) {
		boolean isPresent = false;
		for (Tab tab : getProductController().getView().getProductProcessPane().getTabPane().getTabs()) {
			if (tab.getText().equalsIgnoreCase(tabName)) {
				isPresent = true;
			}
		}
		return isPresent;
	}
	
	/**
	 * This event will navigate to the tab specified or to the home screen.
	 * 
	 * @param event
	 */
	@Subscribe()
	public void navigateToPane(final NavigateTabEvent event) {
		if (null!=event && propertyBean.isDunnage()) {
			navigateToTab(event.getNavigateToTab());
		}
		else if(event.isSwitchProcessTab())  {
			String newTab = event.getNavigateToTab();
			if(event.isNextTab())  {
				Tab currentTab = getProductController().getView().getProductProcessPane().getTabPane().getSelectionModel().getSelectedItem();
				if(currentTab != null && currentTab.getText().equals(ViewId.PRE_CHECK.getViewLabel()))  {
					getProductController().getView().getProductProcessPane().getTabPane().getSelectionModel().selectNext();
				}
			}
			else if (!StringUtils.isBlank(newTab))  {
				navigateToTab(newTab);
			}
		}
		else {
			getProductController().finishProduct();
		}
	}
	
	private boolean isListEmpty(Map list){
		return ! (null != list ? list.isEmpty() :  true);
	}
	
	@Override
	public String getName() {
		return "";
	}
	
}
