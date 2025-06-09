package com.honda.mfg.stamp.conveyor.messages;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.Press;

/**
 * A class representing a carrier update request.
 * For example:
 * <pre>
 *    CarrierUpdateRequestMessage carrierUpdReqMsg = new CarrierUpdateRequestMessage(carrier, targetOp);
 * </pre>
 *
 * @author  Vivek Bettada
 * @see     com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage
 * @see     com.honda.mfg.stamp.conveyor.domain.Carrier
 */
public class CarrierUpdateRequestMessage extends CarrierUpdateMessage implements ServiceRequestMessage {

    /*
     * constructs a carrier update request from Carrier
     * sets target operation
     *
     * @param     Carrier
     * @param     target operation
     * @return    new CarrierUpdateRequestMessage
     * @see		  com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage#CarrierUpdateMessage(Carrier)
     * @see		  com.honda.mfg.stamp.conveyor.messages.CarrierUpdateOperations
     */
	public CarrierUpdateRequestMessage(Carrier c, CarrierUpdateOperations targetOp) {
		super(c);
		this.setTargetOp(targetOp);
	}

    public Carrier getCarrier() {
            Carrier carrier = new Carrier();
            
            if(!isEmptyString(source))  {carrier.setSource(source);} 

            if(!isEmptyString(carrierNumber)) {carrier.setCarrierNumber(Integer.parseInt(carrierNumber)); }
            if(!isEmptyString(quantity)) {carrier.setQuantity(Integer.parseInt(quantity)); }
            
            Die die = null;
            if(!isEmptyString(dieNumber)) {die = Die.findDie(Long.parseLong(dieNumber));  }
            carrier.setDie(die);
            
            Stop currentStop = null;
            if(!isEmptyString(currentLocation)) {currentStop = Stop.findStop(Long.valueOf(currentLocation));}
            carrier.setCurrentLocation(currentStop);
            
            Stop destinationStop = null;
            if(!isEmptyString(destination)) {destinationStop = Stop.findStop(Long.valueOf(destination));}
            carrier.setDestination(destinationStop);
            //MG Add Maint Bits
            if(!isEmptyString(maintenanceBits)){carrier.setMaintenanceBits(Integer.parseInt(maintenanceBits));}
            if(!isEmptyString(status)) {carrier.setCarrierStatus(CarrierStatus.findByType(Integer.parseInt(status)));}
            if(!isEmptyString(originationLocation)) {carrier.setPress(Press.findByType(Integer.parseInt(originationLocation)));}
            if(!isEmptyString(productionRunNumber)) {carrier.setProductionRunNo(Integer.parseInt(productionRunNumber));}
            if(!isEmptyString(buffer)) {carrier.setBuffer(Integer.parseInt(buffer));}
            
            if(reprocess != null && "1".equals(reprocess.trim()))  {
            	carrier.setReprocess(true);
            }
            else  {
            	carrier.setReprocess(false);            	
            }
            
            try {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
                Date prodRunTS = null;
             	if(!isEmptyString(productionRunTimestamp))  {prodRunTS = new Date(formatter.parse(productionRunTimestamp).getTime());}
                carrier.setStampingProductionRunTimestamp(prodRunTS);
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return carrier;
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
	
    public String position = "";
    public Integer getPosition() {
    	Integer thisPostion = null;
    	if(!isEmptyString(position)) {
        	thisPostion = (Integer.valueOf(position));
        }
        return thisPostion;
	}

	public void setPosition(Integer thisPosition) {
		if(position != null) {
			this.position = String.valueOf(thisPosition);
		}
	}

	public String laneStop = "";

	public Stop getLaneStop() throws NumberFormatException  {
        Stop thisStop = null;
        if(!isEmptyString(laneStop)) {thisStop = Stop.findStop(Long.valueOf(laneStop));   }
        return thisStop;
	}

	public void setLaneStop(Long laneStopId) {
		if(laneStopId != null) {
			this.laneStop = String.valueOf(laneStopId);
		}
	}

}
