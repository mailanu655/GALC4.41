package com.honda.galc.device.mitsubishi;

import com.honda.galc.client.device.property.MitshubishiDevicePropertyBean;

/**
 * 
 * <h3>QnASocketReader</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QnASocketReader description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Nov 5, 2010
 *
 */
public class QnASocketReader implements Runnable{
	
	private QnAUnsolicitedDriver driver;
	private boolean isRunning = true;
	private MitshubishiDevicePropertyBean property;
	
	public QnASocketReader(QnAUnsolicitedDriver driver,
			MitshubishiDevicePropertyBean property) {
		this.driver = driver;
		this.property = property;
		
	}

	public void run() {
		while(isRunning){
			try {
				byte[] received = driver.receiveDataFromPlc();
				driver.processReceived(received);
				
			} catch (Throwable e) {
				driver.getLogger().error(e, "Error: Failed to start QnA socket reader.");
				
				if(driver.isConnected())
					driver.disConnected();	
			}
		}
		
	}
	
	
	//Getter && Setters
	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public MitshubishiDevicePropertyBean getProperty() {
		return property;
	}
	
	
	
	
}
