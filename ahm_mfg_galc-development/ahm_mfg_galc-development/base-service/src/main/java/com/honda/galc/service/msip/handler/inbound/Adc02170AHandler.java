package com.honda.galc.service.msip.handler.inbound;


import java.util.List;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.service.msip.dto.inbound.Adc02170ADto;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public class Adc02170AHandler extends ReceivingTransactionMsipHandler<BaseMsipPropertyBean, Adc02170ADto> {
	
	public Adc02170AHandler() {}

	public boolean execute(List<Adc02170ADto> dtoList) {
		try {
			processDealerAssign70A(dtoList);
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
	private void processDealerAssign70A( List<Adc02170ADto> dtoList)
	{
		getLogger().info( "Processing the 70A transactions" );
		for (Adc02170ADto dto : dtoList)
		{
			try
			{
				getLogger().info( "Processing the 70A message");
				dto	= parseCommonAttriburesFromMessage( dto );
				dealerAssigned70A( dto.getProductId()
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
