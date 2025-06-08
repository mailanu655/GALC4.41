/**
 * 
 */
package com.honda.galc.device.simulator.torque;

import java.io.FileWriter;

import com.honda.galc.device.simulator.utils.Logger;
import com.honda.galc.util.StringUtil;

/**
 * @author Kathiresan Subu
 *
 */
public class StateMachine
{
	private FileWriter _fOut = null;
	private int _currentState = 0x0000;
	
	public static final int READY_FOR_TORQUE_READING = 0x000F;		// 0000 1111
	
	public static final int COMMUNICATION_START = 0x0001;			// 0000 0001	MSGID 0001
	public static final int SUBSCRIBED_TO_DEVICE = 0x0002;			// 0000 0010	MSGID 0060
	public static final int SELECTED_PARAM_SET = 0x0004;			// 0000 0100	MSGID 0018
	public static final int SELECT_JOB = 0x000C;                    // 0000 1100    MSGID 0038
	public static final int DEVICE_ENABLED = 0x0008;				// 0000 1000	MSGID 0043		
	
	public static final int SUBSCRIBED_TO_PSET = 0x0010;			// 0001	0000	MSGID 0014
	public static final int PARAMETER_SET_SELECTED_ACK = 0x0020;	// 0010	0000	MSGID 0016
	public static final int JOB_INFO_ACKNOWLEDGE = 0x0030;          // 0011 0000    MSGID 0036       
	
	/**
	 * 
	 * @param fOut
	 */
	public StateMachine(FileWriter fOut)
	{
		_fOut = fOut;
	}
	
	/**
	 * @param state the currentState to set
	 */
	public void setCurrentState(int state)
	{
		_currentState = state;

		Logger.log(_fOut, "Current State        : " + StringUtil.padLeft(printCurrentStateBinary(), 8, '0', true));
		Logger.log(_fOut, "Ready State bit Mask : " + StringUtil.padLeft(printReadyStateBinary(), 8, '0', true));
	}
	
	/**
	 * @return the currentState
	 */
	public int getCurrentState()
	{
		return _currentState;
	}
	
	/**
	 * @return the currentState as a binary string
	 */
	public String printCurrentStateBinary()
	{
		return Integer.toBinaryString(_currentState);
	}
	
	/**
	 * @return the ready state as a binary string
	 */
	public String printReadyStateBinary()
	{
		return Integer.toBinaryString(READY_FOR_TORQUE_READING);
	}
	
	/**
	 * disables the device 
	 */
	public void disableDevice()
	{
		// disable the device by INVERTING the device enabled bit
		setCurrentState(getCurrentState() & ~StateMachine.DEVICE_ENABLED);
	}
}
