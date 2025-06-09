/**
 * 
 */
package com.honda.mfg.stamp.conveyor.messages;

import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;

/**
 * @author VCC44349
 *
 */
public class EmptyCarrierReleaseRequest implements ServiceRequestMessage {

	/* (non-Javadoc)
	 * @see com.honda.mfg.stamp.conveyor.messages.ServiceRequestMessage#getTargetOp()
	 */
	
	/**
	 * @param targetOp
	 * @param releaseManager
	 * @param whichArea
	 */
	public EmptyCarrierReleaseRequest(StorageArea area, Boolean releaseMgr,
			CarrierUpdateOperations targetOp) {
		super();
		setTargetOp(targetOp);
		setReleaseManager(releaseMgr);
		setWhichArea(area);
	}

    /**
     * The target operation in CarrierManagementService to be invoked to process carrier update
     *
     */
	private CarrierUpdateOperations targetOp;
	
	@Override
	public CarrierUpdateOperations getTargetOp() {
		return targetOp;
	}

	public void setTargetOp(CarrierUpdateOperations targetOp) {
		this.targetOp = targetOp;
	}
	
	private Boolean releaseManager = null;

	public Boolean getReleaseManager() {
		return releaseManager;
	}

	public boolean isReleaseManager() {
		return releaseManager.booleanValue();
	}

	public void setReleaseManager(Boolean releaseManager) {
		this.releaseManager = releaseManager;
	}
	
	private StorageArea whichArea;

	public StorageArea getWhichArea() {
		return whichArea;
	}

	public void setWhichArea(StorageArea whichArea) {
		this.whichArea = whichArea;
	}
	
	private String source = "";

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
