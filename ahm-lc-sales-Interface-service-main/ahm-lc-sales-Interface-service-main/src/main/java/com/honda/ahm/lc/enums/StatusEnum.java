package com.honda.ahm.lc.enums;

public enum StatusEnum {

	INIT(0, "INIT", "INIT"),
	VQ_SHIP(1, "VQ-SHIP", "VQ SHIP"),
	AH_RCVD(2, "AH-RCVD", "AHM RECEIVING"),
	DLR_ASGN(3, "DLR-ASGN", "DEALER ASSIGNED (FTZ REL)"),
	AH_SHIP(4, "AH-SHIP","SHIPMENT CONFIRMED (FTZ CFRM)"),
	AH_RTN(-1, "AH-RTN", "FACTORY RETURN"),
	AH_PCHG(5, "AH-PCHG", "PARK CHANGE"),
	PPO_ON(6, "PPO-ON", "PPO ON"),
	PPO_OFF(7, "PPO-OFF", "PPO OFF"),
	SHIPPER(8, "SHIPPER"," SHIPPER"),
	ON_TRN(9, "ON-TRN", "LOADED TO TRAIN"),
	DLR_RCPT(10, "DLR-RCPT", "DEALER RECEIPT"),
	DLR_RTN(-2, "DLR-RTN", "DEALER RETURN"),
	AF_OFF(-3,"AF-OFF","ASSEMBLY OFF");

	private int status;
	private String type;
	private String name;

	StatusEnum(final int status, final String type, final String name) {
		this.status = status;
		this.type = type;
		this.name = name;
	}

	/**
	 * Method to get the enum with the value of the status
	 * 
	 * @param status
	 * @return
	 */
	public static StatusEnum getShippingStatusByStatus(final int status) {
		for (StatusEnum statusEnum : StatusEnum.values()) {
			if (statusEnum.getStatus() == status) {
				return statusEnum;
			}
		}
		return null;
	}

	/**
	 * Method to get the StatusEnum looking for transaction type
	 * 
	 * @param Type
	 * @return
	 */
	public static StatusEnum getStatusByType(final String type) {
		for (StatusEnum status : StatusEnum.values()) {
			if (type.equals(status.type)) {
				return status;
			}
		}
		return null;
	}

	/**
	 * @return the getStatus
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the transaction type
	 */
	public String getType() {
		return type;
	}

}
