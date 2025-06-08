package com.honda.galc.service.msip.handler.inbound;


import java.util.List;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.service.msip.dto.inbound.Adc02390ADto;
import com.honda.galc.service.msip.handler.inbound.ReceivingTransactionMsipHandler;
import com.honda.galc.service.msip.property.inbound.Adc02390APropertyBean;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public class Adc02390AHandler extends ReceivingTransactionMsipHandler<Adc02390APropertyBean, Adc02390ADto> {
	
	public Adc02390AHandler() {}

	public boolean execute(List<Adc02390ADto> dtoList) {
		try {
			processFactoryReturn90A(dtoList);
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
	private void processFactoryReturn90A( List<Adc02390ADto> dtoList)
	{
		getLogger().info( "Processing the 900A transactions" );
		for (Adc02390ADto dto : dtoList)
		{
			try
			{
				getLogger().info( "Processing the 90A message");
				dto	= parseCommonAttriburesFromMessage( dto );
				factoryReturn90A( dto.getProductId()
						, dto.getActualTimestamp()
						, getPropertyBean().getOffProcessPoint()
						, getPropertyBean().getProcessPointList()
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
