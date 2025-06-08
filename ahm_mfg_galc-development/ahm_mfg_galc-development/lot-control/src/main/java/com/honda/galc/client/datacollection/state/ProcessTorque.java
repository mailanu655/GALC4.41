package com.honda.galc.client.datacollection.state;

import com.honda.galc.client.audio.ClipPlayer;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.LotControlConstants;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.fsm.ITorqueDataCollectionEvent;
import com.honda.galc.client.datacollection.fsm.IUserControlEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.net.Request;

/**
 * <h3>TorqueDataCollection</h3>
 * <h4>
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
public class ProcessTorque extends DataCollectionState
implements ITorqueDataCollectionEvent, IUserControlEvent, IProcessTorque, IProcessTorqueClassic, IProcessTorqueRepair
{
	private static final long serialVersionUID = 1L;
	InstalledPart part;
	private boolean firstTorqueOnAllRulesRejected;

	//Total bad torques for current part
	private int partBadTorqueCount = 0;

	//Bad torques for current bolt
	private int badTorqueCount = 0;
	private String msgId;
	private String userMsg;



	/**
	 * Initialize torque collection state
	 * If the the current part does not collection torque, then transit to
	 * Part verification state to verify next part.
	 */
	public void init()
	{
		partBadTorqueCount = 0;
		badTorqueCount = 0;

		logDebug(this.getClass().getSimpleName()+": init()");
	}

	public boolean hasNoTorqueOnRules(){
		return getTorqueCountOnRules() == 0;
	}

	public void initStateClassic(){
		if(isLastPart()) setCurrentPartIndex(-1);
	    if(moveToTheNextPartHasTorque()) //working on the 1st part
	    {
	    	resetCurrentTorqueIndex();
	    	initState();
	    }
	}
	/**
	 * Map to torque OK event
	 * Transit to part serial number verification state if the current torque
	 * is the last torque.
	 */
	public void torqueOk(Measurement torque)
	{
		logInfo(this.getClass().getSimpleName()+": ok()");
		badTorqueCount = 0; //reset bad torque count
		addToToqueList(part.getMeasurements(), torque);

		clearMessage();
	}

	public void torqueOkPostAction()
	{
		setCurrentTorqueIndex(getCurrentTorqueIndex() + 1);
		//if(isLastTorqueOnCurrentPart()) complete();
	}

	public void torqueOkPostActionClassic()
	{
		setCurrentTorqueIndex(getCurrentTorqueIndex() + 1);
		logInfo("torqueOkPostActionClassic-torqueIndex:" + getCurrentTorqueIndex());

	}

	public void torqueSkipPostAction(){
		setCurrentTorqueIndex(getCurrentTorqueIndex() + 1);
	}

	public void torqueSkipPostActionClassic(){
		setCurrentTorqueIndex(getCurrentTorqueIndex() + 1);
		logInfo("torqueSkipPostActionClassic-torqueIndex:" + getCurrentTorqueIndex());
	}

	public boolean isLastPartAndLastTorque(){
		return isLastTorqueOnCurrentPart() && isLastPart();
	}

	public boolean isLastTorquedPartAndLastTorque(){
		return isLastTorqueOnCurrentPart() && isLastTorquedPart();
	}

	public boolean isLastTorquedPart(){
		for (int i = getCurrentPartIndex()+1; i<getLotControlRules().size(); i++){
			if(getLotControlRules().get(i).getParts().get(0).getMeasurementCount() > 0)
				return false;
		}
		return true;
	}

	/**
	 * Map to torque NG event
	 * Notify observers torque value NG and error message
	 */
	public void torqueNg(Measurement torque, String msgId, String userMsg) {
		logInfo(this.getClass().getSimpleName()+": ng()" + " msgId:" + msgId + " userMsg: " + userMsg);
		this.msgId = msgId;
		this.userMsg = userMsg;
		addToToqueList(part.getMeasurements(), torque);
		if(!msgId.equalsIgnoreCase(LotControlConstants.SCAN_DURING_TORQUE)){
			partBadTorqueCount++;
			badTorqueCount++;
		}
		if(getCurrentMeasurementSpec().getMaxAttempts() > 0){
			this.userMsg = this.userMsg +". Attempt " +badTorqueCount +" of " +getCurrentMeasurementSpec().getMaxAttempts();
		}
		setMessage(new Message(this.msgId, this.userMsg));
	}

	public boolean isTorqueMaxAttemptsExceeded(){

		return isPartMaxAttemptsExceeded() || isBoltMaxAttemptsExceeded();
	}

	private boolean isBoltMaxAttemptsExceeded() {
		//attempts <= 0 is treated as unlimited
		if(getCurrentMeasurementSpec().getMaxAttempts() <= 0) return false;
		else if(badTorqueCount <  getCurrentMeasurementSpec().getMaxAttempts())
			return false;

		msgId = LotControlConstants.MSG_MAX_MEASUREMENT_ATTEMPTS_EXCEEDED;
		userMsg = "The Maximum Attempts of NG Torques:" + getCurrentMeasurementSpec().getMaxAttempts() +
		          " on torque:" +getCurrentMeasurementSpec().getId().getMeasurementSeqNum() + " has been exceeded.";
		error(MessageType.EMERGENCY, msgId, userMsg);
		Logger.getLogger().info(msgId, ":", userMsg);

		return true;
	}

	private boolean isPartMaxAttemptsExceeded() {
		//attempts <= 0 is treated as unlimited
		if(getCurrentPartMaxAttempts() <= 0) return false;
		else if(partBadTorqueCount < getCurrentPartMaxAttempts()) return false;

		msgId = LotControlConstants.MSG_MAX_MEASUREMENT_ATTEMPTS_EXCEEDED;
		userMsg = "The Part Maximum Attempts of NG Torques:" + getCurrentPartMaxAttempts() + " has been exceeded.";
		error(MessageType.EMERGENCY, msgId, userMsg);
		Logger.getLogger().info(msgId, ":", userMsg);

		return true;
	}


	/**
	 * Last step in collection torque for current part.
	 * Disable gun/abort job should be done in completing collect torque.
	 *
	 */
	public void complete()
	{
		logInfo(this.getClass().getSimpleName()+": complete()");

	}

	/**
	 * Map to user skip part event
	 */
	public void skipPart() {
		setSkippedPart();
		
		while (!isLastTorqueOnCurrentPart()) {
			skipCurrentInput();
			torqueSkipPostActionClassic();
		}
		
		logInfo(this.getClass().getSimpleName()+": skipPart()");
	}
	
	@Override
	public void skipProduct() {
		skipPart();
		
		if(getStateBean().getProduct() != null) getStateBean().getProduct().setSkipped(true);
		setCurrentInstalledPartPassTime();
		logInfo(this.getClass().getSimpleName()+": skipProduct()");
	}

	/**
	 * Map to user cancel event
	 */
	public void cancel()
	{
		logInfo(this.getClass().getSimpleName()+": cancel()");

		DataCollectionController.getInstance().getClientContext().setManualRefresh(true);
		ClipPlayer.getInstance().flushAll();
	}


	/**
	 * Map to user Reject event
	 */
	public void reject()
	{
		if(isRejectingFirstTorqueOnAllRules())
		{
			firstTorqueOnAllRulesRejected = true;
			if (getScanCountOnRules()>0) {
				setCurrentTorqueIndex(-1);
				clearMessage();

			}
			return;
		}

		firstTorqueOnAllRulesRejected = false;

		clearMessage();
		if(getCurrentTorqueIndex() > 0)
			setCurrentTorqueIndex(getCurrentTorqueIndex() -1); //reverse back one torque
		else{
			//this is the 1st torque on current part
			if(moveToThePreviousPartHaveTorque())
				setCurrentTorqueIndex(getCurrentPartTorqueCount() -1); //set torque index to last torque
			else
				setCurrentTorqueIndex(-1);

		}
	}

	private boolean moveToThePreviousPartHaveTorque() {
		for(int i = getCurrentPartIndex(); i > 0; i--){

			setCurrentPartIndex(getCurrentPartIndex() - 1);
			part = getProduct().getPartList().get(getCurrentPartIndex());

			if(hasTorque()) return true;
		}
		return false;
	}

	public boolean isRejectingFirstTorqueOnAllRules(){
		return (isFirstTorqueOnPart()) && isFirstPartHasTorque();
	}

	private boolean isFirstPartHasTorque() {
		return getCurrentPartIndex() == getFirstPartIndexHasTorque();
	}

	public boolean isFirstTorqueOnPart() {
		return getCurrentTorqueIndex() == 0;
	}

	/**
	 * Initial torque collection state
	 *
	 */
	public void initState() {
		part = getProduct().getPartList().get(getCurrentPartIndex());
		logCheck(this.getClass().getSimpleName()+": Rule#:" + getCurrentPartIndex());
		logCheck(this.getClass().getSimpleName()+": Rule Part Name:" + getCurrentLotControlRule().getPartNameString());

		if(hasTorque())
		   setCurrentTorqueIndex(getCurrentTorqueIndex() + 1);

		clearMessage();
	}


	private boolean moveToTheNextPartHasTorque() {
		for(int i = 0; i < getLotControlRules().size(); i++){
			setCurrentPartIndex(getCurrentPartIndex() + 1);

			if(hasTorque())
				return true;
		}
		return false;
	}

	/**
	 * Check lot control rule to find if the current torque is the last
	 * torque for the current part
	 * @return
	 */
	public boolean isLastTorqueOnCurrentPart() {
		return getCurrentTorqueIndex() >= getCurrentPartTorqueCount();
	}

	public boolean isFirstTorqueOnCurrentPart() {
		return getCurrentTorqueIndex() == 0;
	}

	public boolean isLastTorqueOnCurrentPartAndIsTorqueMaxAttemptsExceeded() {
		boolean isLastTorqueOnCurrentPart = isLastTorqueOnCurrentPart();
		boolean isTorqueMaxAttemptsExceeded = isTorqueMaxAttemptsExceeded();
		boolean isAllowSkipProduct =isAllowSkipProduct();
		return (isLastTorqueOnCurrentPart && isTorqueMaxAttemptsExceeded && isAllowSkipProduct );
	}
	
	private boolean isAllowSkipProduct() {
		return DataCollectionController.getInstance().getClientContext().getProperty().isAllowSkipProduct();
	}

	/**
	 * Check if the current part has not torque to collect
	 * @return
	 */
	public boolean hasNoTorque() {
		return !hasTorque();
	}

	public boolean hasNoTorqueToRepair() {
		InstalledPart currentPart = getProduct().getPartList().get(getCurrentPartIndex());
		if (currentPart.getMeasurements() == null || currentPart.getMeasurements().isEmpty()) {
			return false; // if part has no measurements for its torques, then it has torques to repair
		}
		for (Measurement measurement : currentPart.getMeasurements()) {
			if (measurement.getMeasurementStatus() != MeasurementStatus.OK) {
				return false; // if a measurement is not OK, then the part has a torque to repair
			}
		}
		return true;
	}

	public boolean isFirstTorqueOnAllRulesRejected() {
		return firstTorqueOnAllRulesRejected;
	}

	public boolean isFirstTorqueOnAllRulesRejectedAndHasScanParts() {
		return isFirstTorqueOnAllRulesRejected() && hasPartScan();
	}

	private boolean hasPartScan() {
		for (LotControlRule rule: getLotControlRules()) {
			if (rule.getSerialNumberScanFlag() == 1){
				return true;
			}
		}
		return false;
	}

	public void abort() {
		setSkippedPart();
		logInfo(this.getClass().getSimpleName()+": abort()");
	}

	public void reject1(){
		setCurrentTorqueIndex(getCurrentTorqueIndex() -1);
	}

	public boolean isFirstTorqueOnCurrentPartRejected(){
		return getCurrentTorqueIndex() == -1;
	}

	private Measurement createMeasurementForskipInput(){
		Measurement measurement = new Measurement();
		MeasurementId id = new MeasurementId();
		measurement.setId(id);
		measurement.setPartSerialNumber("");
		measurement.setMeasurementStatus(MeasurementStatus.NG);
		measurement.setPartSerialNumber(part.getPartSerialNumber());
		return measurement;
	}

	public void skipCurrentInput() {
		logInfo(this.getClass().getSimpleName()+": skipCurrentInput()");
		if(DataCollectionController.getInstance().getClientContext().getProperty().isMeasurementOnSkip() || partBadTorqueCount == 0)
	   	{
			boolean measurementExists = false;
			for (Measurement m : part.getMeasurements()) {
				if (m.getId().getMeasurementSequenceNumber() == (getCurrentTorqueIndex()+1)) {
					measurementExists = true;
					break;
				}
			}
			
			if (!measurementExists) {
				partBadTorqueCount++;
				addToToqueList(part.getMeasurements(), createMeasurementForskipInput());
			}
	   	}
		badTorqueCount = 0;
	}

	public void continueDelay() {
		clearMessage();
	}

	public void runSkipCommand(){
		runInSeparateThread(new Request(DataCollectionController.getInstance().getClientContext().getProperty().getSkipPartActionCommand()));
	}

	protected void runInSeparateThread(final Request request) {
		Thread t = new Thread() {
			public void run() {
				DataCollectionController.getInstance(DataCollectionController.getInstance().getClientContext().getAppContext().getApplicationId()).received(request);

			}
		};

		t.start();
	}
	
	public void repairPartSelected() {
		logInfo(this.getClass().getSimpleName()+": repairPartSelected()");
		setCurrentPartIndex(-1);
	}
	
	public boolean isNotAutoAdvanceRepairAndTorqueMaxAttemptsExceeded(){
		return isTorqueMaxAttemptsExceeded() && isNotAutoAdvanceRepairPart();
	}
}
