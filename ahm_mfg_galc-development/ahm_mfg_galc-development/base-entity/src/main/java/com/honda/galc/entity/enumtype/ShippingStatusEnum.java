package com.honda.galc.entity.enumtype;

public enum ShippingStatusEnum {	
	INIT(0, "SHIP", ""),
	S50A(1, "50A", "PR"),
	S60A(2, "60A", "RE"),
	S70A(3, "70A", "SC"),
	S75A(4, "75A", "DC"),
	S90A(-1, "90A", "IR"),
	S65A(5, "65A", "PC" );
	
	private int status;
	private String name;
	private String transactionType;
	
	ShippingStatusEnum(final int status, final String name, final String transactionType)
	{
		this.status 			=	status;
		this.name				=	name;
		this.transactionType	=	transactionType;
	}
	
	/**
	 * Method to get the enum with the value of the status
	 * @param status
	 * @return
	 */
	public static ShippingStatusEnum getShippingStatusByStatus(final int status)
	{
		for (ShippingStatusEnum statusEnum : ShippingStatusEnum.values()) {
			if ( statusEnum.getStatus() == status )
			{
				return statusEnum;
			}
		}
		return null;
	}
	
	
	/**
	 * Method to get the ShippingStatusEnum looking for transaction type
	 * @param transactionType
	 * @return
	 */
	public static ShippingStatusEnum getShippingStatusByTransactionType ( final String transactionType )
	{
		for ( ShippingStatusEnum shippingStatus : ShippingStatusEnum.values() ) 
		{
			if ( transactionType.equals ( shippingStatus.transactionType ))
			{
				return shippingStatus;
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
	public String getTransactionType ()
	{
		return transactionType;
	}
	
	
}
