package com.honda.galc.client.product.pane;

import java.util.Map;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.mvc.AbstractView;
import com.honda.galc.client.product.NavigateTabEvent;
import com.honda.galc.client.product.mvc.BulkProductController;
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
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ReflectionUtils;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Tab;

public class QiBulkProductIdlePane extends AbstractProductIdlePane {

	private boolean isNavigationFromDefectEntry = false;
	private boolean isProductShipped = false;
	private String processPointId;

	private QiPropertyBean propertyBean;

	private AbstractProcessView<?,?> homeScreen;

	public QiBulkProductIdlePane(BulkProductController productController) {
		super(productController);
		processPointId = productController.getModel().getApplicationContext().getProcessPointId();
		propertyBean = PropertyService.getPropertyBean(QiPropertyBean.class, processPointId);
		EventBusUtil.register(this);	}

	@Override
	protected void initComponents() {
		setCenter(getHomeScreenWindow());
	}

	@Override
	public void toIdle() {
		initComponents();
		if(isNavigationFromDefectEntry && PropertyService.getPropertyBean(QiPropertyBean.class,ApplicationContext.getInstance().getProcessPointId()).switchUserAfterScan()){
			Platform.runLater(new Runnable() {
				public void run() {
					LoginStatus loginStatus = LoginDialog.login(ClientMainFx.getInstance().getStage( getProductController().getModel().getApplicationContext().getApplicationId()), isLoginBypassAllowed(), isLoginBypassAllowed());

					if(loginStatus == LoginStatus.OK){
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

	private Node getHomeScreenWindow() {		
		if(homeScreen==null)
		{		
			try {
				String className = "com.honda.galc.client.qi.homescreen.BulkProductHomeScreenView";
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

	@Override
	public String getName() {
		return "";
	}

	private boolean isListEmpty(Map list){
		return ! (null != list ? list.isEmpty() :  true);
	}

	@Subscribe()
	public void onProductEvent(final ProductEvent event) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				AbstractView<?, ?> view = (AbstractView<?, ?>) getProductController().getView().getProductProcessPane().getSelectedProcessView();
				if (null!=event && event.getEventType().equals(ProductEventType.PRODUCT_INPUT_RECIEVED)) {
					if (!view.getViewLabel().equals(ViewId.DUNNAGE.getViewLabel()))
						disableTab(ViewId.DUNNAGE.getViewLabel());

					if (!view.getViewLabel().equals(ViewId.PRODUCT_CHECK.getViewLabel()))
						disableTab(ViewId.PRODUCT_CHECK.getViewLabel());
				}
				else if(null!=event && (event.getEventType().equals(ProductEventType.PRODUCT_DIRECT_PASSED) 
						|| event.getEventType().equals(ProductEventType.PRODUCT_DEFECT_DONE))) { 
					if(isProductShipped){
						return;
					}
					if(QiInspectionUtils.isGdpProcessPoint(processPointId)) {
						getProductController().updateGdpDefects();
					}
					if (getProductController().getView().getProductProcessPane().isNavigatedFromRepairEntry()) {
						EventBusUtil.publishAndWait(new ProductEvent("SUBMIT", ProductEventType.PRODUCT_REPAIR_DEFECT_DONE));
					} else {
						getProductController().resetQuantity();
						if (isTabPresent(ViewId.PRODUCT_CHECK.getViewLabel()) && (isListEmpty(propertyBean.getProductWarnCheckTypes())
								|| isListEmpty(propertyBean.getProductCheckTypes())) ) {
							if (!view.getViewLabel().equals(ViewId.PRODUCT_CHECK.getViewLabel())) {
								if (!view.getViewLabel().equals(ViewId.DUNNAGE.getViewLabel()))
									navigateToTab(ViewId.PRODUCT_CHECK.getViewLabel());

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
					
					getProductController().finishProduct();
				} else if (event.getEventType().equals(ProductEventType.PRODUCT_SEND_TO_FINAL)) {
					getProductController().getModel().setSendToFinal(true);
					getProductController().finishProduct();
				} 
				isProductShipped=false;
			}
		});
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
		} else {
			getProductController().finishProduct();
		}
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
}
