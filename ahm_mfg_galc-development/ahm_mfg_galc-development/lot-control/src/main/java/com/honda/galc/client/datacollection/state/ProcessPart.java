package com.honda.galc.client.datacollection.state;

import java.sql.Timestamp;
import java.util.List;

import com.honda.galc.client.audio.ClipPlayer;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.fsm.IPartVerificationEvent;
import com.honda.galc.client.datacollection.fsm.IUserControlEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.util.LotControlPartUtil;
/**
 * <h3>ProcessPart</h3>
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
public class ProcessPart extends DataCollectionState 
implements IUserControlEvent, IPartVerificationEvent, IProcessPart, IProcessPartClassic, IProcessPartHeadless, IProcessPartRepair
{
	private static final long serialVersionUID = 1L;

	/**
	 * Initialize part serial number verification state
	 * Transit to Torque collection state directly if the current part does not require to scan
	 * part Serial Number and need to collect torque
	 */
	public void init()
	{
		logDebug(this.getClass().getSimpleName()+": init()");
		startTimestamp = new Timestamp(System.currentTimeMillis());
	}

	/**
	 * Set initial state for part verification 
	 *
	 */
	public void initRepairState() {
		
		
		clearMessage();
		if(getLotControlRules().size() > 0){ 
			resetCurrentTorqueIndex();
			setCurrentPartIndex(0);
			if(!isScanPartSerialNumber()) {
				InstalledPart part = createInstalledPartForNotScanPsn();
				addToPartList(part);
				LotControlPartUtil.loadMeasurementsForPart(part);
			}
		}
	}

	/**
	 * Set initial state for part verification 
	 *
	 */
	public void initState() {
		setCurrentPartIndex(getCurrentPartIndex()+1);
		resetCurrentTorqueIndex();
		clearMessage();
		logDebug(this.getClass().getSimpleName()+": Rule#:" + getCurrentPartIndex());
		logCheck(this.getClass().getSimpleName()+": Rule Part Name:" + getCurrentPartName());

		if(!isScanPartSerialNumber())
			addToPartList(createInstalledPartForNotScanPsn());
	}
	/**
	 * Set initial state for part verification 
	 *
	 */
	public void completeRepair() {
		setCurrentPartIndex(-1);
		clearMessage();
	}
	

	/**
	 * check if the part is configured to show part mark only
	 * @return
	 */
	public boolean isPartMark(){
		return !isScanPartSerialNumber() && !hasTorque();
	}
	
	private InstalledPart createInstalledPartForNotScanPsn() {
		InstalledPart part = new InstalledPart();
		part.setValidPartSerialNumber(true);
		part.setPartId(getCurrentLotControlRulePartList().get(0).getId().getPartId());
		return part;
	}

	private InstalledPart createInstalledPartForSkipPsn() {
		InstalledPart part = new InstalledPart();
		part.setValidPartSerialNumber(false);
		part.setPartId(getCurrentLotControlRulePartList().get(0).getId().getPartId());
		return part;
	}

	public void initStateClassic() {
		
		moveToTheFirstPartToScanSn();

		resetCurrentTorqueIndex();
		clearMessage();
		logDebug(this.getClass().getSimpleName()+": Rule#:" + getCurrentPartIndex());
		logCheck(this.getClass().getSimpleName()+": Rule Part Name:" + getCurrentLotControlRule().getPartNameString());

	}

	private boolean moveToTheFirstPartToScanSn() {
		for(int i = getCurrentPartIndex(); i < getLotControlRules().size()-1; i++){
			setCurrentPartIndex(getCurrentPartIndex() + 1);

			if(!isScanPartSerialNumber()&& hasTorque())
			{
				addToPartList(createInstalledPartForNotScanPsn());
				
			} else {
				return true;
			}
		}
		return false;

	}

	/**
	 * Map to part serial number received event and notify observers
	 */
	public void receivedPartSn(InstalledPart part) {
		addToPartList(part);
	}
	
	/**
	 * Map to part serial number verification OK event
	 * Set Part OK action and notify observers.
	 * Transit to Torque Collection state if the part need to collection torque, 
	 * otherwise, complete the current part collection.
	 * 
	 */
	public void partSnOk(InstalledPart part) {
		logPartStatus(part, "OK");
		addToPartList(part);
		clearMessage();
	}
	
	public void partsSnOk(List<InstalledPart> parts, InstalledPart part) {
		int i=0;
		if(getStateBean().getProduct().getDerivedPartList().size()>0)
			i = getStateBean().getProduct().getDerivedPartList().size();
		
		for(InstalledPart ip: parts){
			logPartStatus(ip, "OK");
			if(ip != null ) {
				ip.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
				ip.setStartTimestamp(startTimestamp);
			}
			
			addToList(getStateBean().getProduct().getDerivedPartList(), ip, i++);
			clearMessage();
		}
		part.setPartSerialNumber(part.getPartSerialNumber().substring(0, 29));
		
		addToPartList(part);
	}
	/**
	 * Map to part serial number verification NG event
	 * Set part part NG action and notify observers
	 * 
	 */
	public void partSnNg(InstalledPart part, String msgId, String userMsg) {
		logPartStatus(part, "NG");
		addToPartList(part);
		setMessage(new Message(msgId, userMsg));
	}
	
	/**
	 * Map to part serial number verification MISSING event
	 */
	public void partSnMissing(InstalledPart part) {
		logPartStatus(part, "MISSING");
		addToPartList(part);
		clearMessage();
	}

	private void logPartStatus(InstalledPart part, String message) {
		String installedPartInfo = "";
		try {
			installedPartInfo = "["  
			+ getStateBean().getProduct().getProductId() 
			+ ","
			+ getCurrentLotControlRule().getPartNameString() 
			+ ",";	
		} catch(Exception ex) {}

		if (part != null && part.getPartSerialNumber() != null) {
			logInfo(this.getClass().getSimpleName() + " " + message + ": " + installedPartInfo + part.getPartSerialNumber() + "]");
		} else {
			logInfo(this.getClass().getSimpleName() + " " + message + ": " + installedPartInfo + "UNKNOWN]");
		}
	}
	
	public boolean isNotLastPart(){
		return !isLastPart();
	}
	
	/**
	 * Internal state to complete part serial number verification.
	 * Last step in part verification. 
	 * Overall part installed status is set based on part serial number status and
	 * torque collection status. Part processing time is also set. 
	 * 
	 * Transit to process Product state to complete the production if the 
	 * current part is the last part, otherwise start to verify next part.
	 *
	 */
	public void complete() {
		logInfo(this.getClass().getSimpleName()+": complete()");
		setCurrentInstalledPartPassTime();
	}
	
	/**
	 * Set skip part action and notify observers
	 * Map to user skip engine event
	 */
	public void skipPart() {
		logInfo(this.getClass().getSimpleName()+": skipPart()");
		
		setSkippedPart();
		setCurrentInstalledPartPassTime();
	}

	/**
	 * Set skip part action and notify observers
	 * Map to user skip engine event
	 */
	public void skipCurrentInput() {
		logInfo(this.getClass().getSimpleName()+": skipCurrentInput()");
		InstalledPart part = createInstalledPartForSkipPsn();
		addToPartList(part);
		clearMessage();
	}

	/**
	 * Set cancel action and notify observers
	 * Map to user cancel event
	 */
	public void cancel()
	{
		logInfo(this.getClass().getSimpleName()+": cancel()");
		DataCollectionController.getInstance(getApplicationId()).getClientContext().setManualRefresh(true);
		ClipPlayer.getInstance().flushAll();
	}
	
	/**
	 * Initialize user Reject
	 */
	public void initForRejection()
	{
		logDebug(this.getClass().getSimpleName()+": initForRejection()");
		moveToTheLastPartScanPartSn(getLotControlRules().size() );
	}
	

	private boolean moveToTheLastPartScanPartSn(int lastPart) {
		for(int i = lastPart - 1; i >= 0; i--){
			setCurrentPartIndex(i);
			if(isScanPartSerialNumber())
			{
				return true;
			}
		}
		setCurrentPartIndex(lastPart);
		Logger.getLogger().debug(this.getClass().getSimpleName(),"No part to reject, stay on the 1st part.");
		//throw new TaskException("No Part to reject!", this.getClass().getSimpleName());
		return false;
	}
	
	/**
	 * Map to user reject
	 */
	public void reject(){
		moveToTheLastPartScanPartSn(getCurrentPartIndex());
	}

	/**
	 * Lot control rule specifies the part does not need to scan part Serial Number, 
	 * but need to collect torques
	 * 
	 * @return
	 */
	public boolean isNotScanPartSnAndHasTorque()
	{
		return !isScanPartSerialNumber() && hasTorque();
	}
	
	public boolean isLastPartAndNotScanPartSnAndHasTorque()
	{
		return isLastPart() && !isScanPartSerialNumber() && getTorqueCountOnRules() > 0;
	} 
	
	public boolean isLastPartAndHasTorqueOnRules(){
		return isLastPart() &&  getTorqueCountOnRules() > 0;
	}
	
	public boolean isLastPartAndNoTorqueOnRules(){
		return isLastPart() && getTorqueCountOnRules() <= 0;
	}

	public void initRejectionFromTorque(){
		logDebug(this.getClass().getSimpleName()+": initRejectionFromTorque()");
		if(!isScanPartSerialNumber()){
			setCurrentPartIndex(getCurrentPartIndex() -1);
		}
	}


	public void reject1(){
		setCurrentPartIndex(getCurrentPartIndex() -1);
	}


	public void continueDelay() {
		clearMessage();
	}

	public void autoProcess() {
		DataCollectionController.getInstance(getApplicationId()).received(new ProductId(getStateBean().getProduct().getProductId()));
		
	}

	public void partNoAction() {
		logInfo(this.getClass().getSimpleName()+": partNoAction()");
		setCurrentInstalledPartPassTime();
	}


	public void repairPartSelected() {
		logInfo(this.getClass().getSimpleName()+": repairPartSelected()");
		setCurrentPartIndex(-1);
	}


	public void nextRepair() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isRepairAndNotScanPartSnAndHasTorque()
	{
		if(getCurrentPartIndex()>-1){
			return !isScanPartSerialNumber() && hasTorque();
		}
		return false;
	}

	/**
	 * Map to part serial number received event and notify observers
	 */
	public void partSnOkButWait(InstalledPart part) {
		addToPartList(part);
	}
	
	public void receivedBypass(InstalledPart partBean) {}	
	public void receivedAuto(InstalledPart partBean) {}
	
}
