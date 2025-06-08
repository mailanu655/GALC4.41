package com.honda.galc.oif.task.ah;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.ShippingTransactionDao;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.ShippingTransaction;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.OIFConstants;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ShippingTransactionPrintingTask</code>
 *    Sub class of ShippingTransactionTask. It's has passed in argument of send location 
 *    to support multiple printing locations on CH / AMH side.
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Paul Chou</TD>
 * <TD>Nov.21, 2022</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Paul Chou
 */
public class ShippingTransactionPrintingTask extends ShippingTransactionTask {
    private String sendLocation = null;
	public ShippingTransactionPrintingTask(String name) {
		super(name);
	}
	
	public void execute(Object[] args)
	{
		extractSendLocation(args);
		super.execute(args);
	}

	private void extractSendLocation(Object[] args) {
		if(args != null) {
			for(Object o: args) {
				if(o instanceof KeyValue) {
					KeyValue kv = (KeyValue<String, String>)o;
					logger.info("passed in ARGS:" + kv.getKey() + ":" + kv.getValue());
					if(TagNames.SEND_LOCATION.name().equals(kv.getKey().toString().replace(OIFConstants.OIF_XARG, "")))
						sendLocation=(String)kv.getValue();
				}
					
			}
			
		}
	}
	
	protected String getSendLocation(ShippingTransaction shippingTransaction) {
		return !StringUtils.isEmpty(sendLocation) ? sendLocation : retrieveSendLocation(shippingTransaction);
	}
		
	private String retrieveSendLocation(ShippingTransaction shippingTransaction) {
		ShippingTransaction shipTx = ServiceFactory.getDao(ShippingTransactionDao.class).findByKey(shippingTransaction.getId());
		if(shipTx != null)
			return shipTx.getSendLocation(); 
		else
			logger.warn("Can't find shipping transaction and send location for re-run oif.");
		return ""; 
	}

}
