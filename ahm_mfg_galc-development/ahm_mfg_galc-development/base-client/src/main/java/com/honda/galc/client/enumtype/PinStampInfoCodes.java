/**
 * 
 */
package com.honda.galc.client.enumtype;

import com.honda.galc.common.logging.Logger;

/**
 * @author Subu Kathiresan
 * @date Sep 5, 2013
 */
public enum PinStampInfoCodes {

		// Ordered least significant to most significant
		STAMPING_OK 		("1", "PIN Stamping of VIN @VIN_INFO@ was successful"),
		STAMPING_FAILED		("2", "PIN Stamping of VIN @VIN_INFO@ failed");

		private final String _infoCode;
		private final String _infoMsg;

		private PinStampInfoCodes(String code, String msg) {
			_infoCode = code;
			_infoMsg = msg;
		}

		public String getInfoCode() {
			return _infoCode;
		}

		public String getInfoMessage(String vinInfo) {
			String infoMessage = _infoMsg;
			
			if (vinInfo != null) {
				infoMessage = infoMessage.replace("@VIN_INFO@", vinInfo);
			}
			if(infoMessage.length() > 40) {
				infoMessage = infoMessage.substring(0, 40);
			}
			
			Logger.getLogger().info("Returning info message: " + infoMessage);
			return infoMessage;
		}
	}

