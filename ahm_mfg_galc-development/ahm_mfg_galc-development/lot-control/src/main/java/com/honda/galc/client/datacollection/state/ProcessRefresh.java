/**
 * 
 */
package com.honda.galc.client.datacollection.state;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.fsm.IUserControlEvent;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.net.Request;

/**
 * @author Subu Kathiresan
 * @Date May 1, 2012
 *
 */
public class ProcessRefresh extends DataCollectionState 
	implements IUserControlEvent, IProcessRefreshClassic, IProcessRefresh, IProcessRefreshHeadless, IProcessRefreshRepair {
	
	private static final long serialVersionUID = 1L;

	public static final String REFRESH_DELAY_MSG_ID = "REFRESH_DELAY"; 
	public volatile Integer delayCount = 0;
	
	/**
	 * Initialize refresh state
	 */
	public void init() {
		logDebug(this.getClass().getSimpleName()+": init()");
		delayCount = getDelayCount();
		if (delayCount > 0)
			setMessage(new Message(REFRESH_DELAY_MSG_ID,"Refreshing Screen in " + delayCount + " seconds.",MessageType.INFO));
	}

	public boolean isDelayContinued(){
		return (delayCount>0);
	}
	
	public boolean isDelayComplete(){
		return (delayCount<=0);
	}
	
	public synchronized void continueDelay() {
		if(getLotControlRules().size()>0 && getCurrentPartIndex() > -1){
			if("NO_ACTION".equals(getCurrentLotControlRule().getStrategy()) && delayCount > 5){
				setMessage(new Message(REFRESH_DELAY_MSG_ID,"No Action Required",MessageType.INFO));
			} else {
				setMessage(new Message(REFRESH_DELAY_MSG_ID,"Refreshing Screen in " + delayCount + " seconds.",MessageType.INFO));
			}
		} else {
			setMessage(new Message(REFRESH_DELAY_MSG_ID,"Refreshing Screen in " + delayCount + " seconds.",MessageType.INFO));
		}
		
		Thread t = new Thread(){
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				}
				if (--delayCount >= 0)
					DataCollectionController.getInstance(getApplicationId()).received(new Request("continueDelay"));
			}
		};
		t.start();
	}

	public void skipPart() {
		logInfo(this.getClass().getSimpleName()+": skipPart()");
	}
	
	/**
	 * Map to user cancel event
	 */
	public void cancel() {
		logInfo(this.getClass().getSimpleName()+": cancel()");
		DataCollectionController.getInstance(getApplicationId()).getClientContext().setManualRefresh(true);
	}


	public void complete() {
		logInfo(this.getClass().getSimpleName()+": complete()");
	}
	
	public void initState() {
		logInfo(this.getClass().getSimpleName()+": initState()");
	}

	public void abort() {
		logInfo(this.getClass().getSimpleName()+": abort()");
	}
	
	public void reject() {
		logInfo(this.getClass().getSimpleName()+": reject()");
		clearMessage();
	}

	public void reject1(){
		logInfo(this.getClass().getSimpleName()+": reject1()");
	}

	public void skipCurrentInput() {
		logInfo(this.getClass().getSimpleName()+": skipCurrentInput()");
	}
	
	public boolean hasTorqueRules(){
		return getTorqueCountOnRules()>0;
	}
	
	public boolean hasScanRules(){
		int scanRules = 0;
		for (LotControlRule rule : getLotControlRules()){
			if (rule.getSerialNumberScanFlag()==1) 
				scanRules++;
		}
		return scanRules>0;
	}

	public void messageSentOk() {
		logInfo(this.getClass().getSimpleName()+": messageSentOk()");
	}
	
	public void messageSentNg() {
		logInfo(this.getClass().getSimpleName()+": messageSentNg()");
	}
}

