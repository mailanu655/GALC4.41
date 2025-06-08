package com.honda.galc.client.dc.mvc;

import static com.honda.galc.client.product.action.ProductActionId.SKIP;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import net.sf.ehcache.Element;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.dc.fsm.IDataCollectionFsm;
import com.honda.galc.client.dc.manager.DeviceDataManager;
import com.honda.galc.client.dc.observer.AbstractPersistenceManager;
import com.honda.galc.client.dc.observer.DataCollectionAudioManager;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.dc.property.DataCollectionPropertyBean;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.dc.view.AbstractViewManager;
import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.product.process.AbstractProcessController;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.ProductCancelledEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEventType;
import com.honda.galc.common.logging.TrainingDataCache;
import com.honda.galc.dao.conf.MCOpEfficiencyHistoryDao;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCPddaPlatformDao;
import com.honda.galc.dao.pdda.UnitDao;
import com.honda.galc.dto.TrainingDataDto;
import com.honda.galc.dto.UnitOfOperation;
import com.honda.galc.entity.conf.MCOpEfficiencyHistory;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCPddaPlatform;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.enumtype.OperationEfficiencyStatus;
import com.honda.galc.fsm.FSMBuilder;
import com.honda.galc.fsm.FxFsmContext;
import com.honda.galc.fsm.FsmType;
import com.honda.galc.fsm.IObserver;
import com.honda.galc.fsm.IState;
import com.honda.galc.openprotocol.model.LastTighteningResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>DataCollectionController Class description</h3>
 * <p> DataCollectionController description </p>
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
public class DataCollectionController extends AbstractProcessController<DataCollectionModel, AbstractDataCollectionView<?,?>>{
	
	private IDataCollectionFsm fsm;
	
	private FxFsmContext<DataCollectionModel> fsmContext;
	
	private AbstractViewManager viewManager;   // TODO Remove
	
	private DeviceDataManager deviceDataManager;  // TODO Remove
	
	private AbstractPersistenceManager persistenceManager;
	
	private DataCollectionAudioManager audioManager;
	
	private DataCollectionEventDispatcher eventDispatcher;
	
	private ConcurrentHashMap<String, OperationProcessor> operationProcessorMap; 
	
	private TrainingDataCache userTrainingCache = null;
	
	public DataCollectionController(DataCollectionModel model, AbstractDataCollectionView<?,?> view) {
		super(model, view);
		setProcessType(ProcessType.DC);
		init();
		EventBusUtil.register(this);
	}

	protected void init() {
		initManagers();
		initFSM();
		initEventHandlers();
	}
	
	protected void initFSM() {
		fsmContext = new FxFsmContext<DataCollectionModel>(FsmType.DEFAULT,getModel(),createObservers(),getLogger());
		fsm = FSMBuilder.buildFSM(IDataCollectionFsm.class,fsmContext);
	}
	
	protected void initManagers() {
		deviceDataManager = new DeviceDataManager(this);
		deviceDataManager.disableTorqueDevices();
		audioManager = new DataCollectionAudioManager(this);
		eventDispatcher = new DataCollectionEventDispatcher(this);
	}
	
	@Override
	public void initEventHandlers() {}

	public IDataCollectionFsm getFsm() {
		return fsm;
	}
	
	@Override
	protected void prepareExecute(){
		PDDAPropertyBean pddaPropertyBean = PropertyService.getPropertyBean(PDDAPropertyBean.class,getProcessPointId());
		getModel().setMfgInstructionLevel(pddaPropertyBean.getMfgInstructionLevel());
		getModel().addOperationsToProduct();
		operationProcessorMap = createOpProcessorsMap();
		getModel().setOpProcessors(operationProcessorMap);
		
		List<String> partNames = new ArrayList<String>();
		for (MCOperationRevision operation: getModel().getOperations()) {
			partNames.add(operation.getId().getOperationName());
		}
		getModel().setProcessTrainingMap(getUserNotTrainedUnits());
		populateInstalledPartsMap(partNames);
		super.prepareExecute();
	}
	
	@Override
	protected void postExecute() {}

	@Override
	public void startExecute() {
		super.startExecute();
		fsm.productIdOk();
	}
	
	private ConcurrentHashMap<String, Boolean> getUserNotTrainedUnits() {
		
		List<UnitOfOperation> unitOfOperationList = null; 
		List<TrainingDataDto> dataDto = new ArrayList<TrainingDataDto>() ;
		ConcurrentHashMap<String, Boolean> processTrngFlagMap = new ConcurrentHashMap<String, Boolean>();
		
		Element item = TrainingDataCache.getInstance().getCache().get(getProductModel().getApplicationContext().getUserId());
    	
    	if (item != null) {
    		dataDto = (List<TrainingDataDto>) item.getValue();
    	}
		if(null!=dataDto){
			for (int i = 0; i < dataDto.size(); i++) {
				TrainingDataDto  trainingDataDto = (TrainingDataDto) dataDto.get(i);
				unitOfOperationList = ServiceFactory.getService(UnitDao.class).getUnitOfOperationDetails(trainingDataDto);
				for (MCOperationRevision operation: getModel().getOperations()) {
					if(null!=trainingDataDto && null!=unitOfOperationList){
						for (int k = 0; k < unitOfOperationList.size(); k++) {
							UnitOfOperation unitNoObj = unitOfOperationList.get(k);
							if(operation.getUnitNo().equalsIgnoreCase(unitNoObj.getUnitNo())){
								if(trainingDataDto.getTrainingStatus() == 0){
									processTrngFlagMap.put(unitNoObj.getUnitNo(), false);
								}
							}
						}
					}
				}
			}
		}
		return processTrngFlagMap;
	}
	
	public TrainingDataCache getUserTrainingCache() {
		if (userTrainingCache == null)
			userTrainingCache = new TrainingDataCache();

		return userTrainingCache;
	}
	
	/*
	 * reset the view and model
	 * this happens when the product goes to IDLE state
	 */
	@Override
	protected void resetExecute() {
		if (operationProcessorMap != null) {
			for (String key : operationProcessorMap.keySet()) {
				OperationProcessor p = operationProcessorMap.get(key);
				if (p != null) {
					p.destroy();
				}
			}
		}
	
		if(!getState().equals(ProcessState.FINISHED))
			fsm.cancel();
	
		super.resetExecute();
	}
	
	/*
	 * TODO remove method
	 * process torque value from torque controller
	 */
	public Object processTorqueData(String deviceId, LastTighteningResult result){
		
		MCOperationMeasurement measurement = getModel().getCurrentMeasurement();
		if(measurement == null) {
			// no measurement spec
			return null;
		}
		
		if(!deviceId.equalsIgnoreCase(measurement.getDeviceId())) {
			// not the current torque controller
			// for multi torque
			return null;
		}
		
		return viewManager.getMeasurementProcessor(measurement).processReceived(result);
	}
	
	/**
	 * this is where we access observers from properties
	 * todo
	 * @return
	 */
	protected List<IObserver> createObservers() {
		List<IObserver> observers = new ArrayList<IObserver>();
		DataCollectionPropertyBean propertyBean = 
			PropertyService.getPropertyBean(DataCollectionPropertyBean.class, getProcessPointId());
		
		viewManager = (AbstractViewManager)createObserver(propertyBean.getViewManager());
		persistenceManager = (AbstractPersistenceManager) createObserver(propertyBean.getPersistentManager());
		observers.add(viewManager);
		observers.add(audioManager);
		observers.add(persistenceManager);
		observers.add(createObserver(propertyBean.getTorqueManager()));
		Map<String,String> extraObservers = propertyBean.getObservers();
		if(extraObservers != null) {
			for(String observerName : propertyBean.getObservers().values()) {
				observers.add(createObserver(observerName));
			}
		}
		return observers;
	}

	public IObserver createObserver(String className) {
		Class<?> clazz;
		IObserver observer = null;
		try {
			clazz = Class.forName(className);
			if(!IObserver.class.isAssignableFrom(clazz))
				displayErrorMessage(className + " is not IObserver interface");
			Class<?>[] parameterTypes = {this.getClass()};
			Object[] parameters = {this};
			Constructor<?> constructor = clazz.getConstructor(parameterTypes);
			observer = (IObserver) constructor.newInstance(parameters);
		} catch (Throwable e) {
			displayErrorMessage("Failed to create observer:" + className);
		}
		return observer;
	}
	
	public ProductActionId[] getProductActionIds(){
		if (PropertyService.getPropertyBean(DataCollectionPropertyBean.class, getProcessPointId()).isDisplaySkipButton()) {
			return new ProductActionId[]{SKIP};
		}
		return new ProductActionId[]{};
	}
	
	public void populateInstalledPartsMap(List<String> partNames) {
		
		List<InstalledPart> installedParts = getInstalledPartDao().findAllValidParts(getModel().getProductModel().getProductId(), partNames);
		installedParts = getMeasurementDao().findMeasurementsForInstalledParts(installedParts, true);
		
		for (InstalledPart installedPart: installedParts) {
			MCOperationRevision operation = getModel().getOperationsMap().get(installedPart.getPartName());
			getModel().getInstalledPartsMap().put(operation.getId().getOperationName(), installedPart);
			if (isDataCollectionComplete(installedPart, operation)) {
				getModel().getCompletedOpsMap().put(operation.getId().getOperationName(), true);
			}
		}
	}
	
	public boolean isDataCollectionComplete(InstalledPart installedPart, MCOperationRevision operation) {
		if (operation.getSelectedPart() == null && "OK".equals(installedPart.getInstalledPartStatus().toString()) ) return true;
		if (DataCollectionModel.hasScanPart(operation) && installedPart.getPartSerialNumber().equals("")) {
			return false;
		} 
		
		if (operation.getSelectedPart().getMeasurements() == null) {
			List<MCOperationMeasurement> measurements = ServiceFactory.getDao(MCOperationMeasurementDao.class).findAllMeasurementForOperationPartAndPartRevision(operation.getId().getOperationName(), 
					operation.getSelectedPart().getId().getPartId(),
					operation.getSelectedPart().getId().getPartRevision());
			operation.getSelectedPart().setMeasurements(measurements);
		}
		
		if (installedPart.getMeasurements().size() < operation.getSelectedPart().getMeasurements().size()) {
			return false;
		}
		return true;
	}

	public ConcurrentHashMap<String, OperationProcessor> createOpProcessorsMap() {
		ConcurrentHashMap<String, OperationProcessor> opProcessorMap = new ConcurrentHashMap<String, OperationProcessor>();
		
		for(MCOperationRevision operation: getModel().getOperations()) {
			opProcessorMap.put(operation.getId().getOperationName(), createOperationProcessor(operation));
		}
		return opProcessorMap;
	}
	
	protected OperationProcessor createOperationProcessor(MCOperationRevision operation) {
		String operationProcessor = operation.getProcessor();
		if(!StringUtils.isEmpty(operationProcessor)){ 
			try {
				Class<?> clazz = Class.forName(operationProcessor);
				if(ClassUtils.isAssignable(clazz, OperationProcessor.class )){
					Class<?>[] parameterTypes = {getClass(),MCOperationRevision.class};
					Object[] parameters = {this, operation};
					Constructor<?> constructor = clazz.getConstructor(parameterTypes);
					return (OperationProcessor)constructor.newInstance(parameters);
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new OperationProcessor(this, operation);
	}
	
	protected void processOperationReject(MCOperationRevision operation) {
		try {
			List<ProductBuildResult> list = getModel().getBuildResultList(operation.getId().getOperationName());
			if (list != null) {
			   for (ProductBuildResult item : list) {
				    if (item instanceof InstalledPart) {
				    	InstalledPart part = (InstalledPart) item;
				    	for (Measurement measurement : part.getMeasurements()) {
				    	   persistenceManager.deleteMeasurement(measurement.getId());
				    	}
				    	persistenceManager.deleteInstalledPart(part.getId());
				    }
				}
			}
			getModel().getCompletedOpsMap().remove(operation.getId().getOperationName());
			selectCurrentOperation();
		} catch (Exception e) {
			addErrorMessage("Failed to reject the current operation.");
			e.printStackTrace();
		}
	}
	
	/**
	 * execute the logic when an work unit is completed:<br/>
	 * <li>Save the product build results of the work unit to database.
	 * <li>Mark the work unit is completed in the data collection model.
	 * <li>Check if all of the work units have been completed or not. If yes, notify the framework to refresh the whole screen and get ready for new product id
	 * @param structure the MC structure
	 */
	protected void processOperationComplete(MCOperationRevision operation) {
		try {
			persistenceManager.saveCollectedData(operation);
			
			getModel().markComplete(operation.getId().getOperationName(), true);
			//Check whether the operation is skipped and set status accordingly
			if(getModel().isComplete(operation)) {
				//Mark status of this operation as complete
				getModel().setOpEfficiencyStatus(OperationEfficiencyStatus.UNIT_COMPLETE);
				OperationProcessor op = operationProcessorMap.get(operation.getId().getOperationName());
				op.completeOperation();
			}
			else {
				//Operation is skipped/invalid, mark status as unit incomplete
				getModel().setOpEfficiencyStatus(OperationEfficiencyStatus.UNIT_INCOMPLETE);
			}
			
			if (getModel().isDataCollectionComplete()) {
				if(getModel().isProductComplete()) {
					//Save and Reset operation efficiency history
					saveAndResetOpEfficiencyHist(true);
					// Nothing is skipped/invalid, mark status as product complete
					if(getProductModel().getProperty().isCreateOpEfficiencyHistory()) {
						//Add Summary Record
						getModel().saveSummaryEffHistoryRecord();
					}
				}
				finish();
			} else{
				selectNextOperation();
			}
		} catch (Exception e) {
			// Failed to save the data to DB
			addErrorMessage("Failed to complete the current operation.");
			e.printStackTrace();
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		getView().reload();
	}
	

	public void selectCurrentOperation() {
		if (getModel().getOperations() != null && !getModel().getOperations().isEmpty()) {
			int i = getModel().getCurrentOperationIndex();
			if(getModel().getProcessTrainingMap().containsKey(getModel().getOperations().get(i).getUnitNo())){
				getAudioManager().playUserNotTrained();
			}
			EventBusUtil.publish(new UnitNavigatorEvent(UnitNavigatorEventType.MOVETO, i));
		}
	}
	
	public void selectNextOperation() {
		if (getModel().getOperations() != null && !getModel().getOperations().isEmpty()) {
			
			for(int i = getModel().getCurrentOperationIndex(); i < getModel().getOperations().size(); i++) {
				if (!getModel().getCompletedOpsMap().containsKey(getModel().getOperations().get(i).getId().getOperationName())) {
					if(getModel().getProcessTrainingMap().containsKey(getModel().getOperations().get(i).getUnitNo())){
						getAudioManager().playUserNotTrained();
					}
						EventBusUtil.publish(new UnitNavigatorEvent(UnitNavigatorEventType.MOVETO, i));
						return;
					
				}
			}
			for(int j = 0; j < getModel().getCurrentOperationIndex(); j++) {
				if (!getModel().getCompletedOpsMap().containsKey(getModel().getOperations().get(j).getId().getOperationName())) {
					if(getModel().getProcessTrainingMap().containsKey(getModel().getOperations().get(j).getUnitNo())){
						getAudioManager().playUserNotTrained();
					}
						EventBusUtil.publish(new UnitNavigatorEvent(UnitNavigatorEventType.MOVETO, j));
						return;
					
				}
			}
			selectCurrentOperation();
		}
	}
	
	public void saveAndResetOpEfficiencyHist(boolean isLastUnit) {
		if(getProductModel().getProperty().isCreateOpEfficiencyHistory()) {
			MCOpEfficiencyHistory opEfficiencyHist = getModel().getOperationEfficiencyHistory();
			if(opEfficiencyHist!=null) {
				//Calculate and set Actual Time
				getModel().setOpEfficiencyActualTime();
				//Save operation Efficiency History if previous and current operation are not same (only for View)
				if(isLastUnit || 
						(!(opEfficiencyHist.getUnitNo().equalsIgnoreCase(getModel().getCurrentOperation().getUnitNo())
						&& opEfficiencyHist.getStatus().equals(OperationEfficiencyStatus.VIEW)))) {
					ServiceFactory.getDao(MCOpEfficiencyHistoryDao.class).save(opEfficiencyHist);
				}
				//Resetting operation efficiency after saving
				getModel().resetOpEfficiency();
			}
			if(isLastUnit) {
				getModel().resetDailyDeptNonWorkSchedules();
			}
		}
	}
	
	public void performOpEfficiencyHistReject(MCOperationRevision operation) {
		//checking property
		if(getProductModel().getProperty().isCreateOpEfficiencyHistory()) {
			MCOpEfficiencyHistory opEfficiencyHist = getModel().getOperationEfficiencyHistory();
			if(opEfficiencyHist!=null) {
				//Operation efficiency present, Product is not completed before
				getModel().setOpEfficiencyStatus(OperationEfficiencyStatus.UNIT_INCOMPLETE);
			}
			else {
				//Product was completed before, update product as incomplete
				ServiceFactory.getDao(MCOpEfficiencyHistoryDao.class).updateProductIncomplete(getProductModel().getProductId(), 
						getProductModel().getProcessPointId());
				//Create new object to capture next action
				loadOpEfficiencyHist(operation);
			}
		}
	}
	
	public void loadOpEfficiencyHist(MCOperationRevision operation) {
		//Save and Reset previous operation efficiency history
		saveAndResetOpEfficiencyHist(false);

		//Create Object only if 
		//1. Operation is not null
		//2. Property is set as true for creating efficiency record
		//3. Product is not completed
		if(operation!=null 
				&& getProductModel().getProperty().isCreateOpEfficiencyHistory()
				&& !getModel().isProductComplete()) {
			
			MCOpEfficiencyHistory opEfficiencyHist = new MCOpEfficiencyHistory();
			opEfficiencyHist.setProductId(getProductModel().getProductId());
			opEfficiencyHist.setProcessPointId(getProductModel().getProcessPointId());
			opEfficiencyHist.setHostName(getProductModel().getApplicationContext().getTerminalId());
			MCPddaPlatform platform = ServiceFactory.getDao(MCPddaPlatformDao.class).findByKey(operation.getStructure().getId().getPddaPlatformId());
			opEfficiencyHist.setAsmProcNo(platform.getAsmProcessNo());
			opEfficiencyHist.setUnitNo(operation.getUnitNo());
			opEfficiencyHist.setUnitTotTime(operation.getStructure().getOperationRevisionPlatform().getOperationTime());
			opEfficiencyHist.setStartTimestamp(new Date(System.currentTimeMillis()));
			opEfficiencyHist.setStatus(OperationEfficiencyStatus.VIEW);
			getModel().setOperationEfficiencyHistory(opEfficiencyHist);
		}
	}
	
	public void disableTorqueDevices() {
		deviceDataManager.disableTorqueDevices();
	}
	
	public IState<?> getCurrentState() {
		return fsmContext.getCurrentState();
	}
	
	public DeviceDataManager getDeviceDataManager() {
		return deviceDataManager;
	}
	
	public AbstractPersistenceManager getPersistenceManager() {
		return persistenceManager;
	}

	public void setPersistenceManager(AbstractPersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}
	
	public DataCollectionAudioManager getAudioManager() {
		return audioManager;
	}

	public void setAudioManager(DataCollectionAudioManager audioManager) {
		this.audioManager = audioManager;
	}

	public DataCollectionEventDispatcher getEventDispatcher() {
		return eventDispatcher;
	}

	public void setEventDispatcher(DataCollectionEventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}
	
	@Subscribe
	public void executeProductCancel(ProductCancelledEvent event) {
		//Saving operation efficiency history
		saveAndResetOpEfficiencyHist(true);
	}
	
}