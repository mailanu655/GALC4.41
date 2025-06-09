/**
 * 
 */
package com.honda.mfg.stamp.conveyor.messages;

import com.honda.mfg.stamp.conveyor.domain.Stop;

/**
 * @author VCC44349
 *
 */
public class ReleaseCarriersRequestMessage implements ServiceRequestMessage {

	
	/**
	 * @param laneStopId
	 * @param releaseCount
	 * @param destinationStopId
	 * @param targetOp
	 */
	public ReleaseCarriersRequestMessage(Long laneStopId, Integer releaseCount,
			Long destinationStopId, CarrierUpdateOperations targetOp) {
		super();
		this.laneStopId = laneStopId;
		this.releaseCount = releaseCount;
		this.destinationStopId = destinationStopId;
		this.targetOp = targetOp;
	}

	/* (non-Javadoc)
	 * @see com.honda.mfg.stamp.conveyor.messages.ServiceRequestMessage#getTargetOp()
	 */

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
	
	private Long laneStopId;
	public Long getLaneStopId() {
		return laneStopId;
	}

	public void setLaneStopId(Long laneStopId) {
		this.laneStopId = laneStopId;
	}
	public Stop getLaneStop() {
        Stop thisStop = null;
        if(laneStopId != null) {thisStop = Stop.findStop(Long.valueOf(laneStopId));}
        return thisStop;
	}

	private Integer releaseCount;
	public Integer getReleaseCount() {
		return releaseCount;
	}

	public void setReleaseCount(Integer releaseCount) {
		this.releaseCount = releaseCount;
	}

	private Long destinationStopId;
	public Long getDestinationStopId() {
		return destinationStopId;
	}

	public void setDestinationStopId(Long destinationStopId) {
		this.destinationStopId = destinationStopId;
	}

    public Stop getDestination(){
       Stop thisStop = null;
        if(destinationStopId != null) {thisStop = Stop.findStop(Long.valueOf(destinationStopId));}
        return thisStop;
    }
	private String source;
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	

}
