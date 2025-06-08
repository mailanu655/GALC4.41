package com.honda.galc.service.msip.handler.inbound;


import java.util.List;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.service.msip.dto.inbound.Adc02275ADto;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public class Adc02275AHandler extends ReceivingTransactionMsipHandler<BaseMsipPropertyBean, Adc02275ADto> {
	
	public Adc02275AHandler() {}

	public boolean execute(List<Adc02275ADto> dtoList) {
		try {
			processShipmentConfirm75A(dtoList);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * Method to process the 60a message
	 */
	private void processShipmentConfirm75A( List<Adc02275ADto> dtoList)
	{
		getLogger().info( "Processing the 75A transactions" );
		for (Adc02275ADto dto : dtoList)
		{
			try
			{
				getLogger().info( "Processing the 75A message");
				dto	= parseCommonAttriburesFromMessage( dto );
				shipmentConfirm75A(dto.getProductId()
						, dto.getActualTimestamp()
						, getPropertyBean().getOffProcessPoint()
						, dto.getMessageDate()
						, dto.getMessageTime()
						, dto.getDealerNo()
						);
			}
			catch ( TaskException tx)
			{
				getLogger().error( "Error processing the message");
				getLogger().error( tx.getMessage() );
			}
		}
	}
}
