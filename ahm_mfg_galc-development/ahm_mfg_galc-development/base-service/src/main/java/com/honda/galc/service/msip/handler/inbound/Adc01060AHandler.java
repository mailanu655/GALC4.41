package com.honda.galc.service.msip.handler.inbound;


import java.util.List;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.service.msip.dto.inbound.Adc01060ADto;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public class Adc01060AHandler extends ReceivingTransactionMsipHandler<BaseMsipPropertyBean, Adc01060ADto> {
	
	public Adc01060AHandler() {}

	public boolean execute(List<Adc01060ADto> dtoList) {
		try {
			processShippingConfirm60A(dtoList);
		} catch (Exception ex) {
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * Method to process the 60a message
	 */
	private void processShippingConfirm60A( List<Adc01060ADto> dtoList)
	{
		getLogger().info( "Processing the 60A transactions" );
		for (Adc01060ADto dto : dtoList)
		{
			try
			{
				getLogger().info( "Processing the 60A message");
				dto	= parseCommonAttriburesFromMessage( dto );
				shippingConfirm60A( dto.getProductId(), dto.getActualTimestamp(),  getPropertyBean().getOffProcessPoint());
			}
			catch ( TaskException tx)
			{
				getLogger().error( "Error processing the message");
				getLogger().error( tx.getMessage() );
			}
		}
	}
}
