package com.honda.galc.client.product.process;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.mvc.AbstractController;
import com.honda.galc.client.product.ProcessEvent;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.dao.conf.MCProductStructureDao;
import com.honda.galc.dao.conf.MCProductStructureForProcessPointDao;
import com.honda.galc.dao.conf.MCStructureDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.entity.conf.MCProductStructureForProcessPoint;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProcessControllerAdapter</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */
public abstract class AbstractProcessController<M extends AbstractProcessModel,V extends AbstractProcessView<?,?>> extends AbstractController<M,V> implements IProcessController<M, V>{

	private int mnemonicKey;
	private String processName;
	
	private MCStructureDao mcStructureDao;
	
	private MCProductStructureDao mcProductStructureDao;
	
	private MCProductStructureForProcessPointDao mcProductStructureForProcessPointDao;
	
	private InstalledPartDao installedPartDao;
	
	private MeasurementDao measurementDao;
	
	// === controlling components === //
	private ProcessState state;
	private boolean required;
	
	private ProcessType processType = ProcessType.OTHER;
	
	private Map<ProcessState, Set<ProcessState>> transitions;

	public AbstractProcessController(M model,V view) {
		super(model,view);
		this.state = ProcessState.IDLE;
		this.transitions = defineTransitions();
	}

	// === transitions see defineTransitions() === //
	public void prepare(ProductModel productModel) {
		clearMessages();
		getModel().setProductModel(productModel);
		prepareExecute();
		postExecute();
		setStateNotify(ProcessState.READY);
	}

	public void update() {
		clearMessage();
		if (ProcessState.READY.equals(getState())) {
			start();
			return;
		}
		if (ProcessState.IDLE.equals(getState())) {
			return;
		}
		updateExecute();
		postExecute();
	}

	public void start() {
		clearMessages();
		getLogger().info(String.format("%s start processing product: %s", getProcessName(), getProductModel().getProduct()));
		startExecute();
		postExecute();
		setStateNotify(ProcessState.PROCESSING);
	}

	public void finish() {
		clearMessages();
		finishExecute();
		if (!isErrorExists()) {
			getLogger().info(String.format("%s finished processing product: %s", getProcessName(), getProductModel().getProduct()));
			setStateNotify(ProcessState.FINISHED);
		}
		postExecute();
	}

	public void reset() {
		clearMessages();
		resetExecute();
		postExecute();
		setStateNotify(ProcessState.IDLE);
	}
	
	public void cancel()
	{
		setStateNotify(ProcessState.CANCELLED);
	}

	/*
	 * This is executed after product is inputed and validated
	 * This method will do the initial data preparation and view preparation
	 * every process controller will be invoked no matter if this is the current controller
	 */
	protected void prepareExecute(){
		getView().prepare();
	}
	
	/**
	 * this method is only invoked for the current active controller
	 * This allows the process controller to execute the current business logic 
	 */
	protected void startExecute() {
		getView().start();
	}

	/**
	 * This allows to reload the data and view
	 * This happens when user clicks the view to make the view active
	 */
	protected void updateExecute() {
		getView().reload();
	}

	protected void finishExecute() {
	}

	/*
	 * reset the view and model
	 * this happens when the product goes to IDEL state
	 */
	protected void resetExecute() {
		clearModel();
		getView().hide();
	}

	// === implementation common === //
	protected void postExecute() {
		processMessages();
		requestFocus();
	}

	public ProductModel getProductModel() {
		return getModel().getProductModel();
	}

	protected void clearModel() {
		getModel().reset();
	}

	public boolean isInState(ProcessState state) {
		if (getState() != null) {
			return getState().equals(state);
		}
		return getState() == state;
	}

	protected boolean isValidTransition(ProcessState start, ProcessState end) {
		if (start == null) {
			return false;
		}

		Set<ProcessState> resultStates = getTransitions().get(start);
		if (resultStates == null) {
			return false;
		}
		return resultStates.contains(end);
	}

	protected Map<ProcessState, Set<ProcessState>> defineTransitions() {
		Map<ProcessState, Set<ProcessState>> transitions = new HashMap<ProcessState, Set<ProcessState>>();

		Set<ProcessState> fromIdle = new HashSet<ProcessState>();
		fromIdle.add(ProcessState.READY);

		Set<ProcessState> fromReady = new HashSet<ProcessState>();
		fromReady.add(ProcessState.PROCESSING);
		fromReady.add(ProcessState.IDLE);

		Set<ProcessState> fromProcessing = new HashSet<ProcessState>();
		fromProcessing.add(ProcessState.FINISHED);
		fromProcessing.add(ProcessState.IDLE);

		Set<ProcessState> fromFinished = new HashSet<ProcessState>();
		fromFinished.add(ProcessState.IDLE);

		transitions.put(ProcessState.IDLE, fromIdle);
		transitions.put(ProcessState.READY, fromReady);
		transitions.put(ProcessState.PROCESSING, fromProcessing);
		transitions.put(ProcessState.FINISHED, fromFinished);

		return transitions;
	}

	// === config === //
	public boolean isActive() {
		return ProcessState.READY.equals(getState()) || ProcessState.PROCESSING.equals(getState());
	}

	public boolean isRequired() {
		return required;
	}
	
	public boolean isQICS() {
		return processType == ProcessType.QICS;
	}
	
	public boolean isDataCollection() {
		return processType == ProcessType.DC;
	}
	
	public boolean isProcessInstruction() {
		return processType == ProcessType.PROCESS_INSTRUCTION;
	}
	
	public boolean isOtherProcess() {
		return processType == ProcessType.OTHER;
	}
	
	public ProcessType getProcessType() {
		return processType;
	}
	
	public void setProcessType(ProcessType processType) {
		this.processType = processType;
	}

	protected void setRequired(boolean required) {
		this.required = required;
	}

	public String getProcessName() {
		if (StringUtils.isEmpty(this.processName)) {
			return getView().getClass().getSimpleName();
		}
		return this.processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}
	
	public String getProcessPointId() {
		return getView().getProcessPointId();
	}

	public ProcessState getState() {
		synchronized (state) {
			return state;
		}
	}

	protected void setState(ProcessState state) {
		synchronized (state) {
			this.state = state;
		}
	}

	protected void setStateNotify(ProcessState state) {
		if (state.equals(getState())) {
			return;
		}
		setState(state);
		stateChangedNotify();
	}

	protected void stateChangedNotify() {
		ProcessEvent event = new ProcessEvent();
		if (getProductModel().getProduct() != null) {
			event.setProductId(getProductModel().getProduct().getProductId());
		}
		event.setProcessName(getProcessName());
		event.setProcessState(getState());
		EventBusUtil.publish(event);
	}

	
	public Map<ProcessState, Set<ProcessState>> getTransitions() {
		return transitions;
	}

	public int getMnemonicKey() {
		return mnemonicKey;
	}

	public void setMnemonicKey(int mnemonicKey) {
		this.mnemonicKey = mnemonicKey;
	}
	
	public MCProductStructureDao getMCProductStructureDao() {
		if(mcProductStructureDao == null)
			mcProductStructureDao = ServiceFactory.getDao(MCProductStructureDao.class);
		return mcProductStructureDao;
	}
	
	public MCProductStructureForProcessPointDao getMCProductStructureForProcessPointDao() {
		if(mcProductStructureForProcessPointDao == null)
			mcProductStructureForProcessPointDao = ServiceFactory.getDao(MCProductStructureForProcessPointDao.class);
		return mcProductStructureForProcessPointDao;
	}
	
	public MCStructureDao getMCStructureDao() {
		if(mcStructureDao == null)
			mcStructureDao = ServiceFactory.getDao(MCStructureDao.class);
		return mcStructureDao;
	}
	
	public InstalledPartDao getInstalledPartDao() {
		if(installedPartDao == null)
			installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		return installedPartDao;
	}
	
	public MeasurementDao getMeasurementDao() {
		if(measurementDao == null)
			measurementDao = ServiceFactory.getDao(MeasurementDao.class);
		return measurementDao;
	}
}
