package com.honda.galc.client.dc.mvc;

import java.util.List;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ClickerControl;
import com.honda.galc.client.dc.control.AbstractUnitNavigatorControl;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.dc.property.DataCollectionPropertyBean;
import com.honda.galc.client.dc.view.DataCollectionViewUtil;
import com.honda.galc.client.dc.view.IDataCollectionWidget;
import com.honda.galc.client.dc.view.InstructionViewWidget;
import com.honda.galc.client.dc.view.LotControlOperationView;
import com.honda.galc.client.dc.view.OperationInfoFXBase;
import com.honda.galc.client.dc.view.OperationInfoWidgetFX;
import com.honda.galc.client.dc.view.OperationView;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.process.AbstractProcessView;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.event.KeypadEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.OperationType;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ReflectionUtils;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * 
 * <h3>DataCollectionView Class description</h3>
 * <p> DataCollectionView description </p>
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
 * Feb 24, 2014
 *
 *
 */
public abstract class AbstractDataCollectionView<M extends DataCollectionModel,C extends DataCollectionController> extends AbstractProcessView<M,C> {
	
	protected AbstractUnitNavigatorControl unitNavigator;
	protected BorderPane unitNavigationPane;
	protected OperationView currentOpView = null;
	protected OperationView opView = null;
	protected IDataCollectionWidget<OperationProcessor> currentDataCollectionPanel = null;

	public AbstractDataCollectionView(ViewId viewId, MainWindow window) {
		super(viewId, window);
	}

	@Override
	public void initView() {}

	@Override
	public void prepare() {
		EventBusUtil.register(this);

		// Activate the controller to listen all components (this+children)
		getController().activate();
	}
	
	@Override
	public void reset() {
		super.reset();
		//bak - 2015-10-22 - Add logic to ensure all torque devices are disabled
		Platform.runLater(new Runnable() {
			public void run() {
				getController().disableTorqueDevices();
			}
		});				
		
		EventBusUtil.unregister(this);
		if (opView != null) {
			opView.destroy();
			opView = null;
		}
	}
	
	@Override
	public void hide() {
		super.hide();
		EventBusUtil.unregister(this);
		if (opView != null) {
			opView.destroy();
			opView = null;
		}
	}

	@Override
	public void reload() {
		getModel().getOperationViewMap().clear();
		getModel().getDataCollectionViewMap().clear();
	}

	@Override
	public void start() {
		if (getDataCollectionPropertyBean().isUnitNavigatorEnabled()) {
			prepareUnitNavigator();
		}
	}

	abstract protected void prepareUnitNavigator();
	
	public void populateUnitNavigator(List<MCOperationRevision> searchedList) {}

	public void selectFirstOperation(List<MCOperationRevision> operations) {
		if (operations != null && !operations.isEmpty()) {
			int i = 0;
			for(MCOperationRevision operation: operations) {
				if (!getModel().getCompletedOpsMap().containsKey(operation.getId().getOperationName())) {
					unitNavigator.moveTo(i);
					if(getModel().getProcessTrainingMap().containsKey(operation.getUnitNo())){
						getController().getAudioManager().playUserNotTrained();
					}
					return;
				}
				i++;
			}
			unitNavigator.moveTo(0);
		}
	}
	
	@Subscribe
	public void handle(KeypadEvent event) {

		switch (event.getEventType()) {
			case KEY_LEFT:
				if(event.isNavigatorSelected()){
				   next();
				}
				break;

			case KEY_RIGHT:
				if(event.isNavigatorSelected()) {
				   previous();
				}
				break;
				
			case KEY_TOGGLEPANE:
				OperationInfoFXBase operationInfoNode = (OperationInfoFXBase)this.lookup(OperationInfoFXBase.id);
				if (event.isNavigatorSelected() || operationInfoNode == null) {
					unitNavigationPane.getStyleClass().add(ClickerControl.HIGHLIGHT_STYLE);
					unitNavigationPane.getStyleClass().remove(ClickerControl.NORMAL_STYLE);
				} else {
					unitNavigationPane.getStyleClass().remove(ClickerControl.HIGHLIGHT_STYLE);
					unitNavigationPane.getStyleClass().add(ClickerControl.NORMAL_STYLE);
				}
				break;
			case KEY_SKIPTASK:
				if(!event.isNavigatorSelected()) {					   
					   DataCollectionViewUtil viewUtil = new DataCollectionViewUtil(opView, getModel());	
					   Logger.getLogger().info("Associate has chosen to skip task " + opView.getCurrentInputFieldIndex());						   
					   viewUtil.notifySkipReject(((ISkipRejectInputView)opView).getSkipRejectButtons().get(opView.getCurrentInputFieldIndex()));					   
				}
				break;
			case KEY_SKIPOPERATION:
				if(!event.isNavigatorSelected()) {
					Logger.getLogger().info("Associate has chosen to skip operation (" + getModel().getCurrentOperationName() 
							+ ")");					
					next();					   
				}
				break;
			case KEY_SKIPPRODUCT:
				if(!event.isNavigatorSelected()) {	
					Logger.getLogger().info("Associate has chosen to skip the product (" + getModel().getProductModel().getProductId() + ")");
					getController().cancel();					
				}
				break;
			case KEY_PREVTASK:
				if(!event.isNavigatorSelected()) {					   
					   DataCollectionViewUtil viewUtil = new DataCollectionViewUtil(opView, getModel());					   
					   viewUtil.notifySkipReject(((ISkipRejectInputView)opView).getSkipRejectButtons().get(opView.getCurrentInputFieldIndex()-1));
					   Logger.getLogger().info("Associate has chosen to return to task " + (opView.getCurrentInputFieldIndex()-1));					   
				}
				break;
			case KEY_PREVOPERATION:
				if(!event.isNavigatorSelected()) {
					previous();
					Logger.getLogger().info("Associate has chosen to return to operation prior to (" + getModel().getCurrentOperationName() 
							+ ")");
				}
				break;
			default :
				break;
		}
	}
	
	@Subscribe
	public void handleEvent(UnitNavigatorEvent event) {
		getLogger().debug("UnitNavigatorWidget.handleEvent recvd : " + event.toString());

		switch (event.getType()) {
		case NEXT:
			next();
			break;

		case PREVIOUS:
			previous();
			break;

		case MOVETO:
			moveTo(event);
			break;
			
		case SELECTED:
			selected(event);
			break;
			
		default:
			break;
		}
	}

	public void next() {
		unitNavigator.next();
	}

	public void previous() {
		unitNavigator.previous();
	}

	public void setCurrent(int index) {
		unitNavigator.moveTo(index);
	}
	
	protected void selected(UnitNavigatorEvent event) {
		if (opView == null || event.getIndex() != getModel().getCurrentOperationIndex()) {
			loadSelectedOperation(event.getIndex());
		}
	}

	protected void loadSelectedOperation(int index) {
		getModel().setCurrentOperationIndex(index);
		if (getDataCollectionPropertyBean().isVisualOpsEnabled() && currentDataCollectionPanel != null) {
			if (currentDataCollectionPanel instanceof InstructionViewWidget) {
				((InstructionViewWidget) currentDataCollectionPanel).getTimer().stop();
			}
		}
		loadViews();
	}

	protected void loadViews() {
		final MCOperationRevision operation = getModel().getCurrentOperation();
		Platform.runLater(new Runnable() {
			public void run() {
				loadOperationView(operation);
			}
		});
	}

	protected void moveTo(UnitNavigatorEvent event) {
		int index;
		if (event.isLocateByIndex()) {
			index = event.getIndex(); 
		} else {
			index = structureIdToIndex(-1); 
		}
		setCurrent(index);
	}
	
	protected int structureIdToIndex(int structureId) {
		return 1;
	}
		
	protected void loadOperationView(MCOperationRevision operation) {
		try {
			createOperationView(operation);
			this.setCenter(opView);
			loadDataCollectionPanel(operation);
			//Creating Object to capture operation efficiency history
			getController().loadOpEfficiencyHist(operation);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void loadDataCollectionPanel(MCOperationRevision operation) {
		
		Platform.runLater(new Runnable() {
			public void run() {
				getController().disableTorqueDevices();
			}
		});
		
		IDataCollectionWidget<OperationProcessor> dcView = null;
		try {
			checkMeasurements(operation);
			//Unregistering current data collection panel
			if(currentDataCollectionPanel != null) {
				EventBusUtil.unregister(currentDataCollectionPanel);
			}
			dcView = createDataCollectionView(operation);
			getModel().getDataCollectionViewMap().put(operation, dcView);
			dcView.register();
			currentDataCollectionPanel = dcView;
			currentDataCollectionPanel.setFocusToExpectedInputField();
			
			if (getDataCollectionPropertyBean().isVisualOpsEnabled() && currentDataCollectionPanel != null) {
				if (currentDataCollectionPanel instanceof InstructionViewWidget) {
					((InstructionViewWidget) currentDataCollectionPanel).checkAutoComplete();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	protected void checkMeasurements(MCOperationRevision operation) {
		clearErrorMessage();
		if (operation.getType().equals(OperationType.GALC_SCAN)
				|| operation.getType().equals(OperationType.INSTRUCTION)
				|| operation.getType().equals(OperationType.GALC_AUTO_COMPLETE)
				|| operation.getType().equals(OperationType.GALC_MADE_FROM)) {
			
			if (getModel().getMeasurementCount(operation) > 0){
				setMessage("Measurements are configured for this operation. Please contact support.", Color.YELLOW);
			}
		}
		
		else if (operation.getType().equals(
				OperationType.GALC_SCAN_WITH_MEAS)
				|| operation.getType().equals(OperationType.GALC_MEAS)
				|| operation.getType().equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL)
				|| operation.getType().equals(OperationType.GALC_MEAS_MANUAL)){
			
			if (getModel().getMeasurementCount(operation) == 0){
				setErrorMessage("Measurements are not configured for this operation. Please contact support.");
			}
		}
	}
	
	protected void createOperationView(MCOperationRevision operation) throws ClassNotFoundException {
		Class<?> viewClass = Class.forName(operation.getView());
		if(viewClass != null){
			OperationProcessor opProcessor = getModel().getOpProcessors().get(operation.getId().getOperationName());
			opView = (OperationView) ReflectionUtils.createInstance(viewClass, new Object[] {opProcessor});
		}else {
			setErrorMessage("Widget class does not exist : " + operation.getView());
		}
	}
	
	@SuppressWarnings("unchecked")
	protected IDataCollectionWidget<OperationProcessor> createDataCollectionView(MCOperationRevision operation) {
		IDataCollectionWidget<OperationProcessor> dcView = null;
		//Reset the data collection view
		this.setRight(null);
		if(opView.getClass().equals(OperationInfoWidgetFX.class)) {
			OperationProcessor opProcessor = getModel().getOpProcessors().get(operation.getId().getOperationName());
			if (operation.getType().equals(OperationType.INSTRUCTION) ||
					operation.getType().equals(OperationType.GALC_INSTRUCTION) ||
					operation.getType().equals(OperationType.GALC_AUTO_COMPLETE) ||
					operation.getType().equals(OperationType.GALC_MADE_FROM)) {
				dcView = new InstructionViewWidget(opProcessor);
			} else {
				dcView = new LotControlOperationView(opProcessor);
			}
			this.setRight((Node) dcView);
		} else {
			dcView =  (IDataCollectionWidget<OperationProcessor>) opView.getDataCollectionPanel();
		}
		if(null == dcView) {
			this.setRight(null);
			dcView = opView;
		}
		populateCollectedData(operation, dcView);
		return dcView;
	}

	protected void populateCollectedData(MCOperationRevision operation, IDataCollectionWidget<OperationProcessor> iDcView) {
		if (getModel().getInstalledPartsMap().containsKey(operation.getId().getOperationName())) {
			iDcView.populateCollectedData(getModel().getInstalledPartsMap().get(operation.getId().getOperationName()));
		}
	}

	protected DataCollectionPropertyBean getDataCollectionPropertyBean() {
		return PropertyService.getPropertyBean(DataCollectionPropertyBean.class, getProcessPointId());
	}
	
	public OperationView getCurrentOpView() {
		return currentOpView;
	}

	public void setCurrentOpView(OperationView currentOpView) {
		this.currentOpView = currentOpView;
	}
	
	public IDataCollectionWidget<OperationProcessor> getCurrentDataCollectionPanel() {
		return currentDataCollectionPanel;
	}

	public void setCurrentDataCollectionPanel(IDataCollectionWidget<OperationProcessor> currentDataCollectionPanel) {
		this.currentDataCollectionPanel = currentDataCollectionPanel;
	}
}