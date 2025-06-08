package com.honda.galc.device.bbscale;

import com.honda.galc.client.device.AbstractTextSocketDevice;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.net.TextSocketReceiver;

public class BBScaleSocketDevice extends AbstractTextSocketDevice {

	private final String START_CONT_MODE = "S";
	private final String END_CONT_MODE = "E";
	private final String REQUEST_WEIGHT = "P";
	
	
	/* (non-Javadoc)
	 * @see com.honda.galc.client.device.lotcontrol.IPlainSocketDevice#startSocketReceiver()
	 */
	@Override
	public void startSocketReceiver() {
		_socketReceiver = new TextSocketReceiver(getSocket(), new BBScaleMessageHandler(this), TextSocketReceiver.END_OF_LINE, getLogger());
		Thread t = new Thread(_socketReceiver);
		t.setDaemon(true);
		t.start();
	}
	
	
    public void setContinousMode(boolean flag) {
    	String cmd = flag ? START_CONT_MODE : END_CONT_MODE; 
    	send(cmd + "\r\n");
    }
    
    
    public void requestWeights(String vin) {
    	send(REQUEST_WEIGHT + vin + "\r\n");
    }

    public void clear()  {
    	if(deviceListeners != null && !deviceListeners.isEmpty())  {
    		deviceListeners.clear();
    	}
    }
    
    /**
     * end session with scales device
     */
     public void closeConnection()  {
    	 closeSocket();
     }
}
