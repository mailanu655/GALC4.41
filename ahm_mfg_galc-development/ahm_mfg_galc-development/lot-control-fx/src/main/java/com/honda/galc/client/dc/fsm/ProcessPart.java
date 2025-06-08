package com.honda.galc.client.dc.fsm;

import com.honda.galc.entity.product.InstalledPart;

public class ProcessPart extends AbstractDataCollectionState implements IProcessPart{

	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	public void complete() {
		// TODO Auto-generated method stub
		
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	public void initState() {
		
	}

	public void initForRejection() {
		// TODO Auto-generated method stub
		
	}

	public void initRejectionFromTorque() {
		// TODO Auto-generated method stub
		
	}

	public void partSnNg(InstalledPart part, String msgId, String userMsg) {
		// TODO Auto-generated method stub
		
	}

	public void partSnOk(String partSN) {
		getModel().addInstalledPart(partSN);
	}

	public void receivedPartSn(InstalledPart part) {
		// TODO Auto-generated method stub
		
	}

	public void reject() {
		// TODO Auto-generated method stub
		
	}

	public void reject1() {
		// TODO Auto-generated method stub
		
	}

	public void skipCurrentInput() {
		// TODO Auto-generated method stub
		
	}

	public void skipPart() {
		// TODO Auto-generated method stub
		
	}

	public void skipProduct() {
		// TODO Auto-generated method stub
		
	}

	public void stateChanged() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Lot control rule specifies the part does not need to scan part Serial Number, 
	 * but need to collect torques
	 * 
	 * @return
	 */
	public boolean isNotScanPartSnAndHasTorque()
	{
		return isNotScanPart() && getCurrentOperation().getSelectedPart().hasMeasurements();
	}
	
	/**
	 * check if the part is configured to show part mark only
	 * @return
	 */
	public boolean isPartMark(){
		return isNotScanPart() && !getCurrentOperation().getSelectedPart().hasMeasurements();
	}
	
	private boolean isNotScanPart() {
		return false;
	}

}
