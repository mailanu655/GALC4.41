package com.honda.galc.entity.enumtype;

public enum ShippingOnTimeStatus {	
	UNDER_24HR(0),
	OVER_24HR(1);
	
	private int status;
	
	ShippingOnTimeStatus(final int newStatus)
	{
		this.status = newStatus;
	}
	
	/**
	 * Method to get the enum with the value of the status
	 * @param status
	 * @return
	 */
	public static ShippingOnTimeStatus getShippingStatusByStatus(final int status)
	{
		for (ShippingOnTimeStatus statusEnum : ShippingOnTimeStatus.values()) {
			if ( statusEnum.getStatus() == status )
			{
				return statusEnum;
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

	
}
