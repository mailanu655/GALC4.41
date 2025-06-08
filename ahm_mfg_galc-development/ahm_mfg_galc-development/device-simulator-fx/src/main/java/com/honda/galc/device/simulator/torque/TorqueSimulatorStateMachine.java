package com.honda.galc.device.simulator.torque;

import java.io.FileWriter;

import com.honda.galc.device.simulator.utils.Logger;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Jan 06, 2015
 */
public class TorqueSimulatorStateMachine {

	private FileWriter fOut = null;
	private int currentState = 0x0000;
	
	public static final int READY_FOR_TORQUE_READING = 0x000F;		// 0000 1111
	
	public static final int COMMUNICATION_START = 0x0001;			// 0000 0001	MSGID 0001
	public static final int SUBSCRIBED_TO_DEVICE = 0x0002;			// 0000 0010	MSGID 0060
	public static final int SELECTED_PARAM_SET = 0x0004;			// 0000 0100	MSGID 0018
	public static final int DEVICE_ENABLED = 0x0008;				// 0000 1000	MSGID 0043		
	
	public static final int SUBSCRIBED_TO_PSET = 0x0010;			// 0001	0000	MSGID 0014
	public static final int PARAMETER_SET_SELECTED_ACK = 0x0020;	// 0010	0000	MSGID 0016
	public static final int SELECT_JOB = 0x0040;                    // 0100 0000    MSGID 0038
	public static final int JOB_INFO_ACKNOWLEDGE = 0x0080;          // 1000 0000    MSGID 0036       
	
	public TorqueSimulatorStateMachine(FileWriter fOut) {
		this.fOut = fOut;
	}
	
	public int getCurrentState() {
		return currentState;
	}
	
	public void setCurrentState(int state) {
		this.currentState = state;

		Logger.log(fOut, "Current State        : " + StringUtil.padLeft(printCurrentStateBinary(), 8, '0', true));
		Logger.log(fOut, "Ready State bit Mask : " + StringUtil.padLeft(printReadyStateBinary(), 8, '0', true));
	}
	
	public String printCurrentStateBinary() {
		return Integer.toBinaryString(currentState);
	}
	
	public String printReadyStateBinary() {
		return Integer.toBinaryString(READY_FOR_TORQUE_READING);
	}
	
	public void disableDevice() {
		// disable the device by INVERTING the device enabled bit
		setCurrentState(getCurrentState() & ~TorqueSimulatorStateMachine.DEVICE_ENABLED);
	}
}
