package com.honda.galc.client.datacollection.state;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.honda.galc.client.common.IObserver;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.common.datacollection.data.DataCollectionError;
import com.honda.galc.client.common.exception.LotControlTaskException;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.fsm.FSMContext;
import com.honda.galc.client.datacollection.fsm.FsmType;
import com.honda.galc.client.datacollection.fsm.Notification;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartSpec;

/**
 * <h3>DataCollectionState</h3>
 * <h4>
 * Base class of data collection state.
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class DataCollectionState extends State {
	private static final long serialVersionUID = 1L;
	
	protected Timestamp startTimestamp;
	private transient Logger log = Logger.getLogger();
	private String applicationId = "";
	private static ConcurrentHashMap<String, DataCollectionStateBean> stateBeans = new ConcurrentHashMap<String, DataCollectionStateBean>();

	//Keep exceptions within an event notification
	private List<LotControlTaskException> exceptionList = new ArrayList<LotControlTaskException>();
	
	/**
	 * Notify observers of state change. 
	 * This method is also used to notify error state, e.g. error message.
	 * @param a 
	 */
	public void stateChanged(Action action) {
		log.debug("Enter stateChanged method in DataCollectionState: " + getStateBean().getClass());
		synchronized (getStateBean()) {
			log.debug("State changed to " + this.getClass().getSimpleName() + ": "+ action);
			getExceptionList().clear();
			getStateBean().setAction(action);
			getStateBean().notifyObservers(this);
			if(Action.REJECT.equals(action)){
				if(getStateBean().getProduct() != null && !getStateBean().getProduct().masterListEntitiesIsEmpty())
					getStateBean().getProduct().getMasterEntityList().pop();
				if(getStateBean().getProduct() != null && !getStateBean().getProduct().afterTrackingMasterListEntitiesIsEmpty())
					getStateBean().getProduct().getAfterTrackingMasterEntityList().pop();
			}
		}
		log.debug("Exit stateChanged method in DataCollectionState " + getStateBean());
	}
	
	public void addObserver(IObserver o){
		log.debug(" addObserver:" + (o == null ? null : o.getClass().getSimpleName()));
		getStateBean().addObserver(o);
	}
	
	public IObserver getObserver(Class o){
		log.debug(" getObserver:" + (o == null ? null : o.getClass().getSimpleName()));
		return getStateBean().getObserver(o);
	}
	
	public void cleanUp(){
		getStateBean().cleanUp();
		stateBeans.remove(this.applicationId);
	}
	
	public String getProductSpecCode(){
		return getStateBean().getProductSpecCode();
	}
	
	@Notification(type=FsmType.DEFAULT,action = Action.ERROR)
	public void error(Message message){
		synchronized (getStateBean()) {
			setMessage(message);
			getStateBean().getErrorList().add(new DataCollectionError(message, new Date()));
		}
	}
	
	@Notification(type=FsmType.DEFAULT,action = Action.ERROR)
	public void error(MessageType type, String msgId, String msg) {
		synchronized (getStateBean()) {
			setMessage(new Message(msgId, msg, type));
			getStateBean().getErrorList().add(new DataCollectionError(msgId, msg, new Date()));
		}
		
	}
	
	@Notification(action = Action.MESSAGE)
	public void message(Message message){
		synchronized (getStateBean()) {
			setMessage(message);
		}
	}
	
	// delegate method
	public List<LotControlRule> getLotControlRules() {
		return getStateBean().getLotControlRules();
	}
	
	public int getCurrentPartIndex() {
		return getStateBean().getCurrentPartIndex();
	}
	
	public int getCurrentTorqueIndex() {
		return getStateBean().getCurrentTorqueIndex();
	}
	
	public void clearMessage() {
		getStateBean().clearMessage();
	}

	
	
	public List<DataCollectionError> getErrorList() {
		return getStateBean().getErrorList();
	}

	public void setCurrentPartIndex(int currentPartIndex) {
		getStateBean().setCurrentPartIndex(currentPartIndex);
	}

	public void setCurrentTorqueIndex(int currentTorqueIndex) {
		getStateBean().setCurrentTorqueIndex(currentTorqueIndex);
	}

	public ProductBean getProduct() {
		return getStateBean().getProduct();
	}
	
	public Measurement getCurrentTorque()
	{
		return getCurrentInstallPart().getMeasurements().get(getCurrentInstallPart().getMeasurements().size() -1); //Last Torque
	}
	
	public String getProductId() {
		return getStateBean().getProduct() == null ? null : getStateBean().getProduct().getProductId();
	}
	
	public void setAction(Action action) {
		getStateBean().setAction(action);
		
	}
	
	public void setMessage(Message message){
		getStateBean().setMessage(message);
	}

	public Action getAction() {
		return getStateBean().getAction();
	}

	public void setProduct(ProductBean product) {
		getStateBean().setProduct(product);
	}

	public String getExpectedProductId() {
		return getStateBean().getExpectedProductId();
	}
	
	public void setExpectedProductId(String expectedProductId) {
		getStateBean().setExpectedProductId(expectedProductId);
	}
	
	public String getAfOnSeqNo() {
		return getStateBean().getAfOnSeqNo();
	}
	
	public void setAfOnSeqNo(String seqNo) {
		getStateBean().setAfOnSeqNo(seqNo);
	}
	
	public long getProductCount() {
		return getStateBean().getProductCount();
	}
	
	public void setProductCount(long count) {
		getStateBean().setProductCount(count);
	}
	
	public long getLotSize() {
		return getStateBean().getLotSize();
	}
	
	public void setLotSize(long size) {
		getStateBean().setLotSize(size);
	}
	
	public LotControlRule getCurrentLotControlRule() {
		return getStateBean().getLotControlRules().get(getCurrentPartIndex());
	}

	public String getExpectedSubId() {
		return getStateBean().getExpectedSubId();
	}

	public void setExpectedSubId(String expectedSubId) {
		getStateBean().setExpectedSubId(expectedSubId);
	}

	public List<PartSpec> getCurrentLotControlRulePartList() {
		LotControlRule rule = getStateBean().getLotControlRules().get(getCurrentPartIndex());
		return rule.getParts();
	}

	public int getCurrentPartScanFlag() {
		return getLotControlRules().get(getCurrentPartIndex()).getSerialNumberScanFlag();
	}

	public int getCurrentPartTorqueCount() {
		return getCurrentLotControlRulePartList().get(0).getMeasurementCount();
	}
	
	public int getCurrentPartMaxAttempts() {
		return getCurrentLotControlRulePartList().get(0).getPartMaxAttempts();
	}
	
	public int getCurrentPartScanCount() {
	    return getCurrentLotControlRulePartList().get(0).getScanCount();
	}
	
	public MeasurementSpec getCurrentMeasurementSpec(){
		if(getCurrentLotControlRulePartList().get(0).getMeasurementSpecs() == null) return  null;
		return getCurrentLotControlRulePartList().get(0).getMeasurementSpecs().get(getCurrentTorqueIndex());
	}
	
	public String getCurrentPartName() {
		if(getStateBean().getCurrentPartIndex() < 0 || getLotControlRules()==null || getLotControlRules().size()==0) return null;
		return getLotControlRules().get(getCurrentPartIndex()).getPartName().getPartName();
	}

	/**
	 * Add part to part list of current product
	 * @param part
	 */
	public void addToPartList(InstalledPart part) {
		InstalledPartId id = new InstalledPartId();
		id.setProductId(getProductId());
		id.setPartName(getLotControlRules().get(getCurrentPartIndex()).getPartName().getPartName());
		
		if(part != null ) {
			part.setId(id);
			part.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
			part.setStartTimestamp(startTimestamp);
		}
		
		addToList(getStateBean().getProduct().getPartList(), part, getCurrentPartIndex());
	}
	
	/**
	 * Add torque to torque list of current part
	 * @param list
	 * @param torque
	 */
	public void addToToqueList(List<Measurement> list, Measurement measurement) {
		MeasurementId id = new MeasurementId();
		id.setMeasurementSequenceNumber(getCurrentTorqueIndex() +1);
		id.setProductId(getProductId());
		id.setPartName( getLotControlRules().get(getCurrentPartIndex()).getPartName().getPartName());
		measurement.setId(id);
		measurement.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		list.add(measurement);
	}
	
	/**
	 * Get the part that current working on
	 * @return
	 */
	public InstalledPart getCurrentInstallPart(){
		if(getProduct() == null || getCurrentPartIndex() == -1) return null;
		if(getCurrentPartIndex() < getProduct().getPartList().size())
			return getProduct().getPartList().get(getCurrentPartIndex());
		else 
			return null;
	}
	
	/**
	 * help method to add object to list
	 * @param <T>
	 * @param list
	 * @param bean
	 * @param index
	 */
	protected <T> void addToList(List<T> list, T bean, int index) {
		if(list.size() <= index)
			list.add(index, bean);
		else
		{
			//replace only if there are different element
			if(bean != list.get(index))
				list.set(index, bean);
		}
	}
	
	/**
	 * Init and set Lot Control Rule for state machine
	 * @param productSpec
	 * @param rules
	 */
	public void initLotControlRules(String productSpec,	List<LotControlRule> rules) {
		getStateBean().setProductSpecCode(productSpec);
		getStateBean().setLotControlRules(rules);
		
	}

	/**
	 * Check lot control rule to find if Scan Part Serial Number is required for
	 * current part
	 * @return
	 */
	public boolean isScanPartSerialNumber() {
		return ( getCurrentPartScanFlag() >= PartSerialNumberScanType.PART.ordinal());
	}
	
	public void clearMessageAndHistory(){
		clearMessage();
		getStateBean().getErrorList().clear();
		getExceptionList().clear();
	}
	
	protected void resetCurrentTorqueIndex() {
		setCurrentTorqueIndex(-1);
	}
	
	/**
	 * Check lot control rule to find if the current part need to collect torque
	 * @return
	 */
	public boolean hasTorque(){
		return (getCurrentPartTorqueCount() > 0) ? true : false;
	}

	/**
	 * Check if the current part is the last one in lot control rule part list. 
	 * @return
	 */
	public boolean isLastPart() {
		// when user chooses REPAIR, skip the standard complete() methods done for the last part
		boolean isLast = (getCurrentPartIndex() >= getLotControlRules().size() -1);
		if (getStateBean().isRepair()) {
			logInfo("user chose REPAIR/CANCEL, skipping the standard complete() methods done for the last part");
			getStateBean().setRepair(false);
			InstalledPart currentPart = getProduct().getPartList().get(getCurrentPartIndex());
			for (Measurement measurement : currentPart.getMeasurements()) {
				if (!measurement.isStatus()) {
					if(isLast)setCurrentPartIndex(-1);
					return false;
				}
			}
		}
		return isLast;
	}
	
	public int getTorqueCountOnRules() {
		int count = 0;
		List<LotControlRule> lotControlRules = getLotControlRules();
		for(LotControlRule r: lotControlRules)
			count += r.getParts().get(0).getMeasurementCount();
		
		return count;
	}
	
	public int getScanCountOnRules() {
		int count = 0;
		List<LotControlRule> lotControlRules = getLotControlRules();
		for(LotControlRule r: lotControlRules)
			count += r.getSerialNumberScanFlag();
		
		return count;
	}
	/**
	 * Return the index of the first part has torque
	 * Return -1, if there is no torque at all
	 * @return
	 */
	public int getFirstPartIndexHasTorque() {
		int i = 0;
		for(i = 0; i < getLotControlRules().size(); i++){
			LotControlRule lotControlRule = getLotControlRules().get(i);
			if(lotControlRule.getParts().get(0).getMeasurementCount() > 0)
				return i;
		}
		return -1;
	}
	
	public void exception(LotControlTaskException e, boolean isStatusError){
		synchronized (getStateBean()) {
			getExceptionList().add(e);
			
			if(isStatusError)
				getStateBean().getErrorList().add(new DataCollectionError(new Message(e.getMessageId(), e.getMessage(), e.getMessageType()), new Date()));
		}

	}

	protected void setSkippedPart() {
		if(getCurrentInstallPart() == null){
			InstalledPart installedPart = new InstalledPart();
			installedPart.setSkipped(true);
			addToPartList(installedPart);
		} else {
			InstalledPart currentInstallPart = getCurrentInstallPart();
			currentInstallPart.setSkipped(true);
		}
	}

	protected void logDebug(String messageString) {
		Logger.getLogger().debug(messageString);
		
	}
	
	protected void logInfo(String messageString) {
		Logger.getLogger().info(messageString);
		
	}
	
	protected void logCheck(String messageString) {
		Logger.getLogger().check(messageString);
		
	}

	/**
	 * Map to user skip engine event
	 */
	public void skipProduct() {
		if(getStateBean().getProduct() != null) getStateBean().getProduct().setSkipped(true);
		setCurrentInstalledPartPassTime();
		logInfo(this.getClass().getSimpleName()+": skipProduct()");
	}

	public void setExceptionList(List<LotControlTaskException> exceptionList) {
		this.exceptionList = exceptionList;
	}
	
	public List<LotControlTaskException> getExceptionList() {
		return exceptionList;	
	}

	public Message getMessage() {
		return getStateBean().getMessage();
	}

	public boolean hasMessage() {
		return getStateBean().getMessage() != null;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	protected void setCurrentInstalledPartPassTime() {
		if(getCurrentInstallPart() != null){
			int passTime = Long.valueOf(System.currentTimeMillis() - getCurrentInstallPart().getStartTimestamp().getTime()).intValue();
			getCurrentInstallPart().setPassTime(passTime/1000);
		}
	}

	public boolean isSkippedProduct() {
		ProductBean product = getStateBean().getProduct();
		return product != null && product.isSkipped();
	}

	public void init(FSMContext fsmContext) {
		setDelayCount(fsmContext.getProperty().getDelayCount());
	}
	
	public DataCollectionStateBean getStateBean() {
		if (!stateBeans.containsKey(applicationId))
			stateBeans.put(applicationId, new DataCollectionStateBean());
		return stateBeans.get(applicationId);
	}
	
	protected void setDelayCount(int count){
		getStateBean().setDelayCount(count);
	}
	
	protected int getDelayCount(){
		return getStateBean().getDelayCount();
	}
	
	public boolean isNotAutoAdvanceRepairPart(){
		return !DataCollectionController.getInstance().getClientContext().getProperty().isAutoAdvancePart();
	}
	
	public boolean isSpecChanged() {
		return getStateBean().isSpecChanged();
	}

	public void setSpecChanged(boolean specChanged) {
		getStateBean().setSpecChanged(specChanged);
	}

	
	public boolean isForceSaveExpected() {
		return getProduct().isSkippedByCellOut();
		
	}
	
	public boolean isSaveNextExpected() {
		return isForceSaveExpected() || !isSkippedProduct();
	}
	
	public boolean isResetSequence() {
		return getStateBean().isResetSequence();
	}
	
	public void setResetSequence(boolean isResetSequence) {
		getStateBean().setResetSequence(isResetSequence);
	}
	
}