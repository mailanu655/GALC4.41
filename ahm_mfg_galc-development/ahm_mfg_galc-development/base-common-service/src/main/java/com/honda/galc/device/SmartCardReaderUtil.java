package com.honda.galc.device;

import java.math.BigInteger;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProxCardDao;
import com.honda.galc.dto.BadgeInfoDto;
import com.honda.galc.entity.conf.ProxCard;
import com.honda.galc.property.BadgeInfoPropertyBean;
import com.honda.galc.service.BadgeService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>SmartCardReaderUtil</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> SmartCardReaderUtil description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Sep 24, 2018</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Sep 24, 2018
 */

public class SmartCardReaderUtil {
	private static final String HID_OMNIKEY_5427_CK_CL_0 = "HID OMNIKEY 5427 CK CL 0";

	public static String getUserId(String cardString, String cardName, Integer cardIdMaxLength ) {
        int cardNumber = 0;
		try {
			
			if(HID_OMNIKEY_5427_CK_CL_0.equals(cardName)) {
				cardName = SmartCardEnumFactory.OMNIKEY5427CK.name();
			}
			SmartCardReader smartCardReader = Enum.valueOf(SmartCardEnumFactory.class, cardName).getSmartCardReaderClass().newInstance();
			cardNumber = smartCardReader.getCardNumber(cardString, cardIdMaxLength);
			
			if(PropertyService.getPropertyBean(BadgeInfoPropertyBean.class).isAllowInsert() && mapProxCardNumber(cardNumber).isEmpty()) {
				BadgeInfoDto badgeInfo=ServiceFactory.getService(BadgeService.class).getUserByBadge(cardNumber);
				if(badgeInfo != null) {
					ProxCard entity = new ProxCard();
					entity.setUserId(badgeInfo.getUser_id());
					entity.setCardNumber((long) cardNumber);
					insertProxCardNumber(entity);
				}
			}
			return mapProxCardNumber(cardNumber);
		} catch (Exception e) {
			Logger.getLogger().error("Not able to get correct Card Number: " + CommonUtil.getStackTrace(e));
			cardNumber = -1;
		}
		return null;
	}
	
	private static String mapProxCardNumber(int cardNumber) {
		ProxCard card = ServiceFactory.getDao(ProxCardDao.class).findByKey((long)cardNumber);
	   	return card == null ? "" : card.getUserId();
	}
	
	private static void insertProxCardNumber(ProxCard entity) {
		ServiceFactory.getDao(ProxCardDao.class).insert(entity);
	}
	
	public static String hexToBinary(String hex) {
	    int len = hex.length() * 4;
	    String bin = new BigInteger(hex, 16).toString(2);

	    //left pad the string result with 0s if converting to BigInteger removes them.
	    if(bin.length() < len){
	        int diff = len - bin.length();
	        String pad = "";
	        for(int i = 0; i < diff; ++i){
	            pad = pad.concat("0");
	        }
	        bin = pad.concat(bin);
	    }
	    return bin;
	}
		
}
