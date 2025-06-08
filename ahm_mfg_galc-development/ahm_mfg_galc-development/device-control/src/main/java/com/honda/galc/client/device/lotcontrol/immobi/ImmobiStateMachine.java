package com.honda.galc.client.device.lotcontrol.immobi;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.StringUtil;


/**
 * @author Gangadhararao Gadde, Subu Kathiresan
 * 
 */
public class ImmobiStateMachine {
		
	// possible states for the Immobi state machine
	public static final int INIT_STATE = -1;

	public static final int VIN_ACK = 0x0001;					// 0000 0000 0000 0001	VIN_ACK		<--
	public static final int VIN_NG = 0x0002;					// 0000 0000 0000 0010	VIN_NG		-->
	public static final int SEND_MTOC = 0x0004;					// 0000 0000 0000 0100	SEND_MTOC	-->	
	public static final int MTOC_ACK = 0x0008;					// 0000 0000 0000 1000	MTOC_ACK	<--
	public static final int SEND_KEY_ID = 0x0010;				// 0000 0000 0001 0000	SEND_KEY_ID	-->	
	public static final int KEY_ID_ACK = 0x0020;				// 0000 0000 0010 0000	KEY_ID_ACK	<--
	public static final int SEND_KEY_ID_OK = 0x0040;			// 0000 0000 0100 0000	KEY_ID_OK	<--
	public static final int KEY_ID_NG = 0x0080;					// 0000 0000 1000 0000	KEY_ID_NG	<--
	public static final int MTOC_NG = 0x0100;					// 0000 0001 0000 0000	MTOC_NG		-->
	public static final int SEND_MTOC_OK = 0x0200;				// 0000 0010 0000 0000	MTOC_OK		-->
	public static final int REG_DONE = 0x0400;					// 0000 0100 0000 0000	REG_DONE	<--
	public static final int SEND_REG_OK = 0x0800;				// 0000 1000 0000 0000	REG_OK		-->
	public static final int REG_NG = 0x1000;					// 0001 0000 0000 0000  REG_NG		-->
	public static final int ABORT = 0x2000;						// 0010 0000 0000 0000  ABORT		<--
	public static final int ERR = 0x4000; 						// 0100 0000 0000 0000  ERR			<--

	// states that require a 'send' to device
	public static final int READY_FOR_SEND_VIN = 0x0000;						// 0000 0000 0000 0000 
	public static final int READY_FOR_SEND_MTOC = 0x0001;						// 0000 0000 0000 0001 
	public static final int READY_FOR_SEND_MTO_OK = 0x000D;						// 0000 0000 0000 1101 
	public static final int READY_FOR_KEY_ID_OK = 0x003D;   					// 0000 0000 0011 1101 
	public static final int READY_FOR_SEND_REG_OK = 0x060D;						// 0000 0110 0000 1101 
	public static final int READY_FOR_SEND_REG_OK_WITH_KEYSCAN = 0x047D;		// 0000 0100 0111 1101    

	// bit mask that will dispose the thread
	public static final int COMPLETED_VIN_PROCESSING = 0x0E0D;					// 0000 1110 0000 1101	
	public static final int COMPLETED_VIN_PROCESSING_WITH_KEYSCAN = 0x0C7D;		// 0000 1100 0111 1101	

	private volatile int _currentState = -1;
	private String _displayMessage = "";
	private String _errorMessage = "";
	
	private static final int NUMBER_OF_BYTES_TO_PRINT = 2;
	
	/**
	 * reset state machine
	 */
	public void reset()
	{
		setCurrentState(INIT_STATE);
	}
	
	/**
	 * @param state the currentState to set
	 */
	public void setCurrentState(int state)
	{
		_currentState = state;
		Logger.getLogger().info("Statemachine    : " + printCurrentStateBinary(NUMBER_OF_BYTES_TO_PRINT * 8));
	}
	
	/**
	 * @return the currentState
	 */
	public int getCurrentState()
	{
		return _currentState;
	}
	
	/**
	 * @return the displayMessage
	 */
	public String getDisplayMessage()
	{
		return _displayMessage;
	}
		
	/**
	 * @param msg the displayMessage to set
	 */
	public void setDisplayMessage(String msg)
	{
		_displayMessage = msg;
	}
	
	/**
	 * @param msg the errorMessage to set
	 */
	public void setErrorMessage(String msg)
	{
		_errorMessage = msg;
	}
	
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage()
	{
		return _errorMessage;
	}
	
	/**
	 * @return the currentState as a binary string
	 */
	public String printCurrentStateBinary(int maxLength)
	{
		return StringUtil.padLeft(Integer.toBinaryString(_currentState), maxLength,'0');
	}
}

