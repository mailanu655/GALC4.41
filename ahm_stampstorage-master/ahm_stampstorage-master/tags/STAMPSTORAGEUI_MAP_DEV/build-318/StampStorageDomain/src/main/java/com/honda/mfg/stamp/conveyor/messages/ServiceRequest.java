/**
 * 
 */
package com.honda.mfg.stamp.conveyor.messages;

/**
 * @author VCC44349
 *
 */
public class ServiceRequest implements ServiceRequestMessage {

	/* (non-Javadoc)
	 * @see com.honda.mfg.stamp.conveyor.messages.ServiceRequestMessage#getTargetOp()
	 */

	/**
	 * @param targetOp
	 * @param user
	 */
	public ServiceRequest(CarrierUpdateOperations targetOp, String user) {
		super();
		this.targetOp = targetOp;
		this.user = user;
	}

	public boolean isEmptyString(String s)  {
    	return (s == null || s.trim().isEmpty());
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
	
	private String user;
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
}
