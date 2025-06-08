package com.honda.galc.device;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>Omnikey5427CK</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> This class has the logic to parse card hexadecimal to numeric number</p>
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
 * <TD>S. Sahoo</TD>
 * <TD>Feb 28, 2020</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Sudhir Sahoo
 * @since Feb 28, 2020
 */

public class Omnikey5427G2 extends SmartCardReader {

	public Omnikey5427G2() {
	}
	
	public Integer getCardNumber() {
		// Logic to get card number
		
		return -1;
	}

	public Integer getCardNumber(String cardString, Integer cardIdMaxLength ) {
        int cardNumber = 0;
		try {
			if((cardString.length() < cardIdMaxLength) || (!cardString.matches("^[a-zA-Z0-9]*$"))) {
         	   Logger.getLogger().info("5427G2 Reader - User entered card number in the back side of the card");
         	   cardString = cardString.replaceAll("[^a-zA-Z0-9]", "");
               cardNumber = Integer.parseInt(cardString); // this is the card number in back of the card
            } else {
            	String hexToBinary = hexToBinary(correctHexString(cardString));
				hexToBinary = hexToBinary.substring(0, hexToBinary.length()-1);
				cardNumber = Integer.parseInt(hexToBinary.substring(hexToBinary.length() - 20, hexToBinary.length()), 2);
            }
         } catch (Exception e) {
         	Logger.getLogger().error("Not able to get correct Card Number: " + CommonUtil.getStackTrace(e));
         	cardNumber = -1;
         }
		
		return cardNumber;
	}

}
