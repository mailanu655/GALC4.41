package com.honda.galc.util;

public class PriorityPlanUtils {

	
	public static boolean isCheckDigitNeeded(String productId) {
		
		if (productId.trim().length()>9)			
		{
			String checkDigitChar=Character.toString(productId.charAt(8));
			if(checkDigitChar.trim().equals("*"))
				return true;
		}
		return false;
	}

}
