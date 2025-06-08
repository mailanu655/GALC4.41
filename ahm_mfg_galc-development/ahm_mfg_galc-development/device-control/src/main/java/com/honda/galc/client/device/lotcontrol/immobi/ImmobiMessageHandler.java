package com.honda.galc.client.device.lotcontrol.immobi;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.device.lotcontrol.immobi.ImmobiMessageItem;
import com.honda.galc.net.MessageHandler;

/**
 * @author Subu Kathiresan
 * @date March 15, 21017
 */
public class ImmobiMessageHandler extends MessageHandler<ImmobiMessageItem> {

	@Override
	public void processItem(ImmobiMessageItem item) {
		try {
			if (item != null) {
				item.getLogger().info("Processing Message: " +  StringUtils.trimToEmpty(item.getMessage()));

				ImmobiMessage immobiMsg = ImmobiMessageHelper.convertToBean(item.getMessage()); 
				item.getDevice().handleMessage(immobiMsg);
			} else {
				throw new NullPointerException("Received a null ImmobiMessageItem to process");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				item.getLogger().error(ex, "Unable to convert incoming IMMOBI message: " + StringUtils.trimToEmpty(item.getMessage()));
			} catch (Exception lex) {}
		}
	}
}