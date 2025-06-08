package com.honda.galc.service.msip.handler.inbound;


import java.util.List;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.oif.ParkChangeDao;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.inbound.Adc02065ADto;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public class Adc02065AHandler extends ReceivingTransactionMsipHandler<BaseMsipPropertyBean, Adc02065ADto> {
	
	public Adc02065AHandler() {}

	public boolean execute(List<Adc02065ADto> dtoList) {
		try {
			processShippingConfirm65A(dtoList);
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
	private void processShippingConfirm65A( List<Adc02065ADto> dtoList)
	{
		getLogger().info( "Processing the 60A transactions" );
		ParkChangeDao parkChangeDao	=	ServiceFactory.getDao( ParkChangeDao.class );
		for (Adc02065ADto dto : dtoList)
		{
			try
			{
				getLogger().info( "Processing the 65A message");
				//Merge the entity (save or update)
				parkChangeDao.save( dto.deriveParkChange() );
			}
			catch ( TaskException tx)
			{
				getLogger().error( "Error processing the message");
				getLogger().error( tx.getMessage() );
			}
		}
	}
}
