package com.honda.galc.device.dataformat;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

/**
 * Ubisense telegram - a message packet from Ubisense server
 * 
 * @author Bernard Leong
 * @date Oct 23, 2017
 */
public class UbisenseTelegram extends InputData implements Serializable{
	private static final int SUPPLIED_VIN_INVALID = 4;
	private static final int NO_SUBSEQUENT_VIN_AVAILABLE = 2;
	private static final long serialVersionUID = 1L;
	private static final String INIT_TELEGRAM = "TH0100000054*LBPINIT01";
	private static final String QUERY_TELEGRAM = "TH0100000054LBPQUERY01";
	private static final String ACK_TELEGRAM = "TH0100000028RESPONSE01000000";
	private static final String STATE_TELEGRAM = "LBPSTATE";
	private static final String READY_TELEGRAM = "LBPREADY";
	private static final String RESPONSE_TELEGRAM = "RESPONSE";
	private String telegram;
	
	public UbisenseTelegram () {}
	
	public UbisenseTelegram (String telegram) {
		this.telegram = telegram;
	}
	
	/**
	 * Left pad string data with asterik
	 * @return
	 */
	private String padTelegram(String data) {
		return StringUtils.leftPad(data, 32, '*');
	}
	
	/**
	 * Initialization Telegram 
	 * @return
	 */
	public String initTelegram(String clientId) {
		return INIT_TELEGRAM + padTelegram(clientId);
	}
	
	/**
	 * Query Telegram 
	 * @return
	 */
	public String queryTelegram(String productId) {
		return QUERY_TELEGRAM + padTelegram(productId);
	}
	
	/**
	 * Telegram heartbeat acknowledgement
	 * @return
	 */
	public String ackTelegram() {
		return ACK_TELEGRAM;
	}
	
	/*
	 * Answer the product ID from the receiver's telegram 
	 */
	public String getReceivedProductId() {
		return telegram.length() > 56
				? telegram.substring(39, 56)
				: "";
	}
	
	/**
	 * Telegram header validation
	 * @return
	 */
	public boolean isNotAValidTelegram() {
		// Header is incomplete or not defining the message body 
		if (telegram.length() < 12) return true;
		return (Integer.parseInt(telegram.substring(4, 12)) == 0);
	}

	/**
	 * Ready Telegram
	 * @return
	 */
	public boolean isReadyTelegram() {
		return (StringUtils.containsIgnoreCase(telegram, READY_TELEGRAM));
	}
	
	/**
	 * Retry telegram 
	 * @return
	 */
	public boolean isResponseTelegram() {
		return (StringUtils.containsIgnoreCase(telegram, RESPONSE_TELEGRAM));
	}
	
	/**
	 * Tool Status OR expected Product ID telegram
	 * @return
	 */
	public boolean isStateTelegram() {
		return (StringUtils.containsIgnoreCase(telegram, STATE_TELEGRAM));
	}
	
	/**
	 * Tool Status telegram indicating validation whether tool is IN or OUT of 
	 * the product cell
	 * @return
	 */
	public boolean isToolStatusTelegram() {
		return telegram.length() > 23 &&  (Integer.parseInt(telegram.substring(22, 23)) == 2);
	}
	
	/**
	 * Product ID telegram indicating LBPQUERY reply 
	 * @return
	 */
	public boolean isQueryReply() {
		return telegram.length() > 23 &&  (Integer.parseInt(telegram.substring(22, 23)) == 1);
	}
	
	/*
	 * Product ID telegram indicating that a VIN is NOT available from the server  
	 */
	public boolean isNoSubsequentVinQueryReply() {
		return getQueryReplyErrorCodeFromTelegram() == NO_SUBSEQUENT_VIN_AVAILABLE;
	}

	/*
	 * Product ID telegram indicating that the supplied Query VIN is NOT valid  
	 */
	public boolean isSuppliedQueryVinInvalid() {
		return getQueryReplyErrorCodeFromTelegram() == SUPPLIED_VIN_INVALID;
	}

	
	/*
	 * Answer the error code from the receiver's telegram; default to 4 (Completed VIN not known) if the telegram is not valid  
	 */
	public int getQueryReplyErrorCodeFromTelegram() {
		return (isQueryReply() && telegram.length() > 24)
			? Integer.parseInt(telegram.substring(23, 24))
			: 4;
	}
	
	public String toString() {
		return telegram + " isStateTelegram: " + isStateTelegram() + " isToolStatusTelegram:" + isToolStatusTelegram() + " isReadyTelegram:" + isReadyTelegram();
	}
}
