package com.honda.galc.oif.dto;

import java.sql.Timestamp;

import com.honda.galc.util.GPCSData;

public class ReceivingTransactionCommonDTO
{
	private String productId;
	private Timestamp actualTimestamp;
	
	@GPCSData ( "DATE" )
	private String messageDate;
	@GPCSData ( "TIME" )
	private String messageTime;
	@GPCSData ( "VIN" )
	private String vin;
	@GPCSData ( "FRAME" )
	private String frame;
	@GPCSData ( "DEALER_NO" )
	private String dealerNo;
	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	/**
	 * @return the messageDate
	 */
	public String getMessageDate() {
		return messageDate;
	}
	/**
	 * @param messageDate the messageDate to set
	 */
	public void setMessageDate(String messageDate) {
		this.messageDate = messageDate;
	}
	/**
	 * @return the messageTime
	 */
	public String getMessageTime() {
		return messageTime;
	}
	/**
	 * @param messageTime the messageTime to set
	 */
	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}
	/**
	 * @return the actualTimestamp
	 */
	public Timestamp getActualTimestamp() {
		return actualTimestamp;
	}
	/**
	 * @param actualTimestamp the actualTimestamp to set
	 */
	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}
	/**
	 * @return the vin
	 */
	public String getVin() {
		return vin;
	}
	/**
	 * @param vin the vin to set
	 */
	public void setVin(String vin) {
		this.vin = vin;
	}
	/**
	 * @return the frame
	 */
	public String getFrame() {
		return frame;
	}
	/**
	 * @param frame the frame to set
	 */
	public void setFrame(String frame) {
		this.frame = frame;
	}
	/**
	 * @return the dealer number
	 */
	public String getDealerNo() {
		return dealerNo;
	}
	/**
	 * @param dealerNo the dealer number to set
	 */
	public void setDealerNo(String dealerNo) {
		this.dealerNo = dealerNo;
	}
}
