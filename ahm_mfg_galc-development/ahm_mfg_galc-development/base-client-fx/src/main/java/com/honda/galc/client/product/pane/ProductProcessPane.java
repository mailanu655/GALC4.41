package com.honda.galc.client.product.pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.enumtype.ObservableListChangeEventType;
import com.honda.galc.client.mvc.AbstractView;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.process.AbstractProcessView;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.ApplicationMainPane;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.event.CancelEvent;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.enumtype.ApplicationType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.util.ReflectionUtils;

/**
 * 
 * 
 * <h3>ProductProcessPane Class description</h3>
 * <p> ProductProcessPane description </p>
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
 * Mar 13, 2014
 *
 *
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */
public class ProductProcessPane extends ApplicationMainPane{

	private ProductController productController;

	private TabPane tabPane;

	/*
	 * This field will be used to override behavior of Cancel and Submit action if navigation has started from repair entry.<br>
	 * So instead of going to home screen control will go to repair entry tab.
	 */
	private boolean isNavigatedFromRepairEntry = false;

	private List<AbstractView<?,?>> views = new ArrayList<AbstractView<?,?>>();

	public ProductProcessPane(ProductController productController) {
		super(productController.getView().getMainWindow());
		this.productController = productController;

		initComponents();

		mapActions();
		EventBusUtil.register(this);
		
		// check if station is repair station
		if (isRepairStation() && tabPane.getTabs() != null) {
			if(tabPane.getTabs().get(getTabIndex(ViewId.DEFECT_ENTRY)) != null)
				tabPane.getTabs().get(getTabIndex(ViewId.DEFECT_ENTRY)).setDisable(true);
		}
	}

	private void initComponents() {
		tabPane = createTabPane();

		String[] panels = getProductController().getModel().getProperty().getProcessViews();
		if(panels == null || panels.length == 0) return;
		loadViews(panels);

		validateViews();

		if(views.size() > 1) setCenter(tabPane);
		else setCenter(views.get(0));

	}


	protected ProductController getProductController() {
		return productController;
	}

	protected void setProductController(ProductController productController) {
		this.productController = productController;
	}

	public TabPane getTabPane() {
		return tabPane;
	}


	public void loadViews(String... args) {
		if (args == null) return;

		for (String name : args) {
			if (StringUtils.isEmpty(name)) continue;
			AbstractView<?,?> view = createView(name);
			if(view == null) continue;
			views.add(view);
		}

		if(views.size() <= 1) return;

		for(AbstractView<?, ?> view : views) {
			Tab tab = new Tab(view.getViewId().getViewLabel());
			tab.setContent(view);
			tabPane.getTabs().add(tab);
		}
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
	}

	protected AbstractView<?,?> createView(String viewName) {
		viewName = viewName.trim();
		Class<?> viewClass = ViewId.getViewClass(viewName);
		if(viewClass == null){
			setErrorMessage("View class does not exisit : " + viewName);
			return null;
		}else {
			if(AbstractView.class.isAssignableFrom(viewClass))
				return (AbstractView<?,?>)ReflectionUtils
						.createInstance(viewClass,new Class<?>[] {MainWindow.class},getMainWindow());
			else {
				setErrorMessage("View class cannot be created : " + viewName);
			}
		}

		return null;
	}

	private void validateViews() {
		boolean hasQICS = false;
		for(AbstractProcessView<?,?> processView : getProcessViews()) {
			if(processView.getController().isQICS()) hasQICS = true;
			if(processView.getController().isDataCollection()){
				if(hasQICS) {
					setErrorMessage("QICS process has to configured after data collection process");
					return;
				}
			}
		}	
	}


	protected TabPane createTabPane() {
		TabPane pane = new TabPane();
		pane.setMinSize(200, 200);
		pane.requestFocus();
		return pane;
	}

	protected void mapActions() {

		tabPane.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<Tab>() {
					public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
						if(oldTab != null) ((AbstractView<?,?>)oldTab.getContent()).hide();
						AbstractView<?,?> view = (AbstractView<?,?>)newTab.getContent();
						view.reload();
						if(view!=null && view.getViewLabel()!=null)
						{
						  Logger.getLogger().check(view.getViewLabel()+ " view loaded successfully");
						}
						getProductController().getView().setProductButtons(view.getController().getProductActionIds());
					
						if (view.getViewLabel().equals(ViewId.DEFECT_ENTRY.getViewLabel()))
							view.resetFocus();
					};
				}
				);	


	}

	public Node getSelectedProcessView() {
		if(views.size()==1) return views.get(0);
		return tabPane.getSelectionModel().getSelectedItem().getContent();
	}

	protected Tab getFirstEnabledView(int startIx) {
		if (startIx < 0) startIx = 0;
		int index = 0;
		for(Tab tab : getTabPane().getTabs()) {
			if(index < startIx) continue;
			if(!tab.isDisabled()) return tab;
			if(AbstractProcessView.class.isAssignableFrom(tab.getContent().getClass())) {
				AbstractProcessView<?,?> processView = (AbstractProcessView<?,?>)tab.getContent();
				if(processView.getController().isActive()) return tab;
			}	
			else return tab;
		}
		return null;
	}

	protected Tab getFirstEnabledProcessView(int startIx) {
		if (startIx < 0) startIx = 0;
		int index = 0;
		for(Tab tab : getTabPane().getTabs()) {
			if(index < startIx || tab.isDisabled()) continue;

			if(AbstractProcessView.class.isAssignableFrom(tab.getContent().getClass())) {
				AbstractProcessView<?,?> processView = (AbstractProcessView<?,?>)tab.getContent();
				if(processView.getController().isActive()) return tab;
			}
		}
		return null;
	}

	protected Tab getFirstEnabledProcessView() {
		return (getFirstEnabledProcessView(0));
	}

	protected Tab getFirstEnabledView() {
		return getFirstEnabledView(0);
	}

	public void selectProcessView(int index) {
		Tab tab = getTabPane().getTabs().get(index);
		selectProcessView(tab);
	}

	protected void selectProcessView(Tab tab) {
		AbstractView<?,?> view = (AbstractView<?,?>)tab.getContent();
		getProductController().getView().setProductButtons(view.getController().getProductActionIds());
		getTabPane().getSelectionModel().select(tab);
	}

	protected void setFirstEnabledViewSelected() {
		Tab tab = getFirstEnabledView();
		if (tab != null) {
			selectProcessView(tab);
		}
	}
	
	public void updateSelectedView() {
		Tab currentView = getTabPane().getSelectionModel().getSelectedItem();
		((AbstractProcessView<?,?>)(currentView.getContent())).getController().update();
	}

	public void setFirstEnabledProcessViewSelected() {
		Tab previousSelection = getTabPane().getSelectionModel().getSelectedItem();
		Tab newSelection = getFirstEnabledProcessView();
		if(newSelection == null) {
			if(views.size() == 1){
				AbstractProcessView<?,?> processView = getProcessView(views.get(0));
				if(processView != null)
					processView.getController().update();
				getProductController().getView().setProductButtons(processView.getController().getProductActionIds());
			}

			return;
		}

		disableDependentTabs(newSelection,true);

		selectProcessView(newSelection);
		if (newSelection.equals(previousSelection)) {
			((AbstractProcessView<?,?>)(newSelection.getContent())).getController().update();
		}
	}


	protected void disableDependentTabs(Tab selectedTab,boolean toDisable) {
		AbstractProcessView<?,?> processView = getProcessView(selectedTab.getContent());
		if(processView == null || !processView.getController().isDataCollection()) return;
		boolean isDC = false;
		for(Tab tab : getTabPane().getTabs()) {
			if(tab == selectedTab) isDC = true;
			else if(isDC) {
				AbstractProcessView<?,?> view = getProcessView(tab.getContent());
				if(view != null && view.getController().isQICS())
					tab.setDisable(toDisable);
			}
		}
	}

	private AbstractProcessView<?,?> getProcessView(Object object) {
		return AbstractProcessView.class.isAssignableFrom(object.getClass()) ? 
				(AbstractProcessView<?,?>)(object) : null;

	}

	public void selectNextEnabledView() {
		Tab tab = getNextEnabledView();
		if(tab != null) selectProcessView(tab);
	}

	protected Tab getNextEnabledView() {
		// enable QICS tabs
		disableDependentTabs(getTabPane().getSelectionModel().getSelectedItem(), false);

		int selectedIx = getTabPane().getSelectionModel().getSelectedIndex();
		Tab view = getFirstEnabledProcessView(selectedIx + 1);
		if (view == null) {
			view = getFirstEnabledProcessView();
		}
		if (view == null) {
			view = getFirstEnabledView(selectedIx + 1);
		}
		if (view == null) {
			view = getFirstEnabledView();
		}
		return view;
	}

	public void resetProcessViews() {
		for(AbstractProcessView<?,?> processView : getProcessViews()) {
			processView.getController().reset();
			processView.reset();
		}
	}

	public void prepareProcessViews(BaseProduct product) {
		for(AbstractView<?,?> view : views) {
			if(AbstractProcessView.class.isAssignableFrom(view.getClass())) {
				AbstractProcessView<?,?> processView = (AbstractProcessView<?,?>)view;
				processView.getController().prepare(getProductController().getModel());
			}
		}
	}

	// === protected api === //
	public List<AbstractProcessView<?,?>> getActiveProcessViews() {
		List<AbstractProcessView<?,?>> activeProcessViews = new ArrayList<AbstractProcessView<?,?>>();
		for(AbstractProcessView<?,?> processView : getProcessViews()) {
			if (processView.getController().isActive() && processView.getController().isRequired()) {
				activeProcessViews.add(processView);
			}
		}
		return activeProcessViews;
	}

	public List<AbstractProcessView<?,?>> getProcessViews() {
		List<AbstractProcessView<?,?>> processViews = new ArrayList<AbstractProcessView<?,?>>();
		for(AbstractView<?,?> view : views) {
			if(AbstractProcessView.class.isAssignableFrom(view.getClass())) {
				processViews.add((AbstractProcessView<?,?>) view);
			}
		}
		return processViews;
	}
	
	/**
	 * For Product Panel Button Toggling 
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	@Subscribe()
	public void onProductEvent(ProductEvent event) {
		AbstractView<?, ?> view = getDefaultView();
		Object argument = event == null 
				? null
				:  event.getArgument(ObservableListChangeEventType.CHANGE_SELECTION);
		boolean shouldAllowChangeOfFocus = argument == null
				? true
				: (Boolean)argument;

		if(event!=null && (event.getEventType().equals(ProductEventType.PRODUCT_DEFECT_ACCEPT) 
				|| event.getEventType().equals(ProductEventType.PRODUCT_APPLY_RECENT_DEFECT))) {
			getProductController().getView().setProductButtons(view.getController().getProductActionIdsOnAccept(), shouldAllowChangeOfFocus);
		}
		else if(event!=null && event.getEventType().equals(ProductEventType.PRODUCT_DEFECT_VOID_ALL)){
			getProductController().getView().setProductButtons(view.getController().getProductActionIds(), shouldAllowChangeOfFocus);
		}
	}
	
	private AbstractView<?, ?> getDefaultView() {
		AbstractView<?, ?> view ;
		if(views.size() == 1){
			view = (AbstractView<?, ?>) views.get(0);
		}else{
			view = (AbstractView<?, ?>) tabPane.getSelectionModel().getSelectedItem().getContent();
		}
		return view;
	}


	/**
	 * This method is used to get view index in tab list.
	 * 
	 * @param viewId
	 * @return
	 */
	private int getTabIndex(ViewId viewId) {
		int viewCounter = 0;
		for (AbstractView<?, ?> view : views) {
			if (view.getViewId().equals(viewId))
				break;
			viewCounter++;
		}
		return viewCounter;
	}

	/**
	 * Method will be used to get view object based on given ViewId.
	 * 
	 * @param viewId
	 * @return tabView
	 */
	private AbstractView<?, ?> getTabView(ViewId viewId) {
		AbstractView<?, ?> tabView = null;
		for (AbstractView<?, ?> view : views) {
			if (view.getViewId().equals(viewId)) {
				tabView = view;
				break;
			}
		}
		return tabView;
	}

	/**
	 * This method will be invoked at {@link ProductEventType#PRODUCT_REPAIR_DEFECT} event and perform
	 * following task-<br>
	 * <li>Select Defect Entry Tab</li>
	 * <li>Pass event information to defect controller</li>
	 * <li>Disabled all other tabs</li>
	 * 
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	@Subscribe()
	public void onRepairDefectEvent(ProductEvent event) {
		if (event != null && event.getEventType().equals(ProductEventType.PRODUCT_REPAIR_DEFECT)) {
			// select defect entry tab
			selectProcessView(tabPane.getTabs().get(getTabIndex(ViewId.DEFECT_ENTRY)));

			this.isNavigatedFromRepairEntry = true;

			getTabView(ViewId.DEFECT_ENTRY).getController()
			.setProductEventDetails((Map<String, Object>) event.getTargetObject());

			// disable all the tabs other than defect entry
			for (Tab tab : tabPane.getTabs()) {
				if (tab.getText().equalsIgnoreCase(ViewId.DEFECT_ENTRY.getViewLabel())) {
					tab.setDisable(false);
				} else {
					tab.setDisable(true);
				}
			}
		}
	}

	/**
	 * This method will be invoked when defect creation is done and navigation
	 * should go to Repair Entry tab. It will subscribe to
	 * {@link ProductEventType#PRODUCT_REPAIR_DEFECT_DONE} event and perform
	 * following task-
	 * <li>Select Repair Entry Tab</li>
	 * <li>set event information to null</li>
	 * <li>Enable all other tabs</li>
	 * <li>Call controller to refresh data</li>
	 * 
	 * @param event
	 */
	@Subscribe()
	public void onRepairDefectCompletionEvent(ProductEvent event) {
		if (event != null && event.getEventType().equals(ProductEventType.PRODUCT_REPAIR_DEFECT_DONE)) {
			//Clear error message before load Repair Entry
			EventBusUtil.publish(new StatusMessageEvent(StringUtils.EMPTY, StatusMessageEventType.CLEAR));
			
			// select repair entry tab and reload data
			selectProcessView(tabPane.getTabs().get(getTabIndex(ViewId.REPAIR_ENTRY)));

			this.isNavigatedFromRepairEntry = false;
			getTabView(ViewId.DEFECT_ENTRY).getController().setProductEventDetails(null);

			// enable all the tabs
			for (Tab tab : tabPane.getTabs()) {
				tab.setDisable(false);
			}

			// Reload data to reset assignActualProblemFlag in RepairEntryController
			getTabView(ViewId.REPAIR_ENTRY).getController().initEventHandlers();
			
			// check if station is repair station
			if (isRepairStation())
				tabPane.getTabs().get(getTabIndex(ViewId.DEFECT_ENTRY)).setDisable(true);
					
		}
	}

	public boolean isNavigatedFromRepairEntry() {
		return isNavigatedFromRepairEntry;
	}

	public void setNavigatedFromRepairEntry(boolean isNavigatedFromRepairEntry) {
		this.isNavigatedFromRepairEntry = isNavigatedFromRepairEntry;
	}

	@Subscribe()
	public void onRepairMethodDialogCancel(CancelEvent cancelEvent){
		if (cancelEvent != null) {
			getProductController().cancel();
		}
	}
	
	/**
	 * This method will be used to check whether station is repair station or
	 * defect station.
	 * 
	 * @return isRapirStation
	 */
	private boolean isRepairStation() {
		String applicationId = this.productController.getModel().getApplicationContext().getApplicationId();
		ApplicationType applicationType = this.productController.getModel().getApplicationContext()
				.getApplication(applicationId).getApplicationType();

		return ApplicationType.QICS_REPAIR.equals(applicationType);
	}

}
