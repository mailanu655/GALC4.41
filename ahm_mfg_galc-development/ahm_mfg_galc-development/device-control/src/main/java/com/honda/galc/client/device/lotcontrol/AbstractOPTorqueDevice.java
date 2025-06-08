package com.honda.galc.client.device.lotcontrol;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import com.honda.galc.client.device.AbstractDevice;
import com.honda.galc.openprotocol.OPMessageHelper;
import com.honda.galc.openprotocol.OPMessageType;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * Feb 23, 2009
 */
public abstract class AbstractOPTorqueDevice extends AbstractDevice {
	public static final char PAD_CHAR = '0';
	
	public static final String PSET_TAG = "P_SET";
	
	public static final String VIN_TAG = "VIN";
	
	public static final String TIME_TAG = "TIME";
	
	private static final String JOB_NUMBER_TAG = "JOB_NUMBER";
	
	protected OPMessageHelper _opMessageHelper;
	
	private boolean _isMultiSpindle = false;
		
	private volatile boolean _isToolEnabled = false;
	
	private volatile boolean _isCommunicationStart = false;
			
	private String _currentInstructionCode = null;
	protected String _spindleId ="";
	protected Hashtable<String, String> _tags;
	
	/**
	 * Initialize communication with the device
	 */
	public void initCommunication() {	        
		try	{
			send(getOpMessageHelper().createMessage(OPMessageType.communicationStart));
			//bak - 20150729 - Send time to controller
			setTime();
		} catch(Exception ex)	{
			getLogger().emergency(ex, "Unable to Initialize communication with the Torque device due to " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	/**
	 * Subscribes this device to the last tightening result data published
	 * by the torque controller
	 */
	public void subscribeToTighteningResultData() {
		try	{
			if(isMultiSpindle())
				send(getOpMessageHelper().createMessage(OPMessageType.multiSpindleResultSubscribe, getTags()));
			else
				send(getOpMessageHelper().createMessage(OPMessageType.lastTighteningResultDataSubscribe,getTags() ));
		} catch(Exception ex)	{
			getLogger().error(ex.getMessage());
			getLogger().error("Unable to Subscribe to last tightening result data from the Torque device");
			ex.printStackTrace();
		}
	}
		
	/**
	 * Enables the torque controller
	 * Sets the pset value to the instruction code provided
	 * 
	 * @param instructionCode
	 */
	public void enable(String instructionCode) {
		try	{
			setCurrentInstructionCode(instructionCode);
			getLogger().check("Enable OPTorqueDevice:" + getId() + " PSet#:" + instructionCode);
			
			getTags().put(PSET_TAG, StringUtil.padLeft(instructionCode, 3, PAD_CHAR, true));		
		
			enable(getTags());
		} catch(Exception ex)	{
			getLogger().emergency(ex.getMessage());
			getLogger().emergency("Could not enable Torque device with instruction code: " + instructionCode);
			ex.printStackTrace();
		}
	}
	
	/**
	 * Enables the torque controller
	 * Sets the pset value to the default instruction code
	 */
	public void enable() {
		enable(getTags());	
	}
	
	/**
	 * Enable the torque controller for Job mode
	 */
	public void enableForJobMode() {
		try	{
			send(getOpMessageHelper().createMessage(OPMessageType.toolEnable,getTags()));
			setToolEnabled(true);
		} catch(Exception ex)	{
			getLogger().emergency(ex.getMessage());
			getLogger().emergency("Could not enable Torque device");
			ex.printStackTrace();
		}	
	}
	

	/**
	 * private helper method to select parameter set and enable tool
	 */
	private void enable(Hashtable<String, String> tags) {
		try	{
			send(getOpMessageHelper().createMessage(OPMessageType.selectParamSet, tags));
			send(getOpMessageHelper().createMessage(OPMessageType.toolEnable,tags));
			setToolEnabled(true);
		} catch(Exception ex)	{
			getLogger().emergency(ex.getMessage());
			getLogger().emergency("Could not enable Torque device");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Disables the torque controller
	 */
	public void disable() {
		try	{
			send(getOpMessageHelper().createMessage(OPMessageType.toolDisable,getTags()));
			setToolEnabled(false);
		} catch(Exception ex)	{
			getLogger().emergency(ex.getMessage());
			getLogger().emergency("Unable to disable Torque device");
			ex.printStackTrace();
		}		
	}

	/**
	 * Stops communication with the torque controller device
	 */
	public void stopCommunication() {
		try	{
			send(getOpMessageHelper().createMessage(OPMessageType.communicationStop,getTags()));
		} catch(Exception ex)	{
			getLogger().emergency(ex.getMessage());
			getLogger().emergency("Unable to stop communication with the Torque device");
			ex.printStackTrace();
		}
	}
		
	/**
	 * Assigns new job to device
	 * 
	 * @param jobNumber
	 */
	public void setJob(String jobNumber) {
		try	{
			getTags().put(JOB_NUMBER_TAG, StringUtil.padLeft(jobNumber, 2, PAD_CHAR, true));	
			send(getOpMessageHelper().createMessage(OPMessageType.selectJob, getTags()));
		} catch(Exception ex)	{
			getLogger().emergency(ex.getMessage());
			getLogger().emergency("Unable to set Torque Controller job: " + jobNumber );
			ex.printStackTrace();
		}
	}
	
	/**
	 * Aborts current job
	 */
	public void abortJob() {
		try	{
			send(getOpMessageHelper().createMessage(OPMessageType.abortJob,getTags()));
		} catch(Exception ex)	{
			getLogger().error(ex.getMessage());
			getLogger().error("Unable to abort job.");
			ex.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param vin
	 */
	public void requestVinDownload(String vin) {
		try	{
			getTags().put(VIN_TAG, vin);	
			send(getOpMessageHelper().createMessage(OPMessageType.requestVinDownload, getTags()));
		} catch(Exception ex)	{
			getLogger().emergency(ex.getMessage());
			getLogger().emergency("Unable to download Vin: " + vin );
			ex.printStackTrace();
		}		
	}
	
	/**
	 * 
	 * @param time
	 */
	public void setTime() {
		try	{
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");						
			getTags().put(TIME_TAG, dateFormat.format(new Date()));	
		
			//bak - 20150729 - Some controllers require reading time from controller before setting
			send(getOpMessageHelper().createMessage(OPMessageType.readTimeUploadRequest));
			
			send(getOpMessageHelper().createMessage(OPMessageType.setTime, getTags()));
		} catch(Exception ex)	{
			getLogger().emergency(ex.getMessage());
			getLogger().emergency("Unable to set Time.");
			ex.printStackTrace();
		}		
	}
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	public abstract boolean send(String msg);

	/**
	 * 
	 * @return
	 */
	public OPMessageHelper getOpMessageHelper() {
		if(_opMessageHelper == null)
			_opMessageHelper = new OPMessageHelper(getLogger());
		
		return _opMessageHelper;
	}

	/**
	 * 
	 * @return
	 */
	public String getCurrentInstructionCode() {
		return _currentInstructionCode;
	}

	/**
	 * 
	 * @param currentInstructionCode
	 */
	public void setCurrentInstructionCode(String currentInstructionCode) {
		_currentInstructionCode = currentInstructionCode;
	}

	/**
	 * 
	 * @param toolEnabled
	 */
	public void setToolEnabled(boolean toolEnabled) {
		_isToolEnabled = toolEnabled;
	}

	/**
	 * 
	 * @return isToolEnabled
	 */
	public boolean isToolEnabled() {
		return _isToolEnabled;
	}

	/**
	 * @param isMultiSpindle the isMultiSpindle to set
	 */
	protected void setMultiSpindle(boolean isMultiSpindle) {
		_isMultiSpindle = isMultiSpindle;
	}

	/**
	 * @return the isMultiSpindle
	 */
	protected boolean isMultiSpindle() {
		return _isMultiSpindle;
	}
	/**
	 * @param isCommStart true/false, status
	 */
	public void setCommunicationStart(boolean isCommStart) {
		_isCommunicationStart = isCommStart;
	}

	/**
	 * @return the status of communication start, has the ACK been received yet
	 */
	public boolean isCommunicationStart() {
		return _isCommunicationStart;
	}

	public Hashtable<String, String> getTags() {
		if(_tags != null) return _tags;
		_tags = new Hashtable<String, String>();
		return _tags;
	}
}
