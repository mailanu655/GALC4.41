package com.honda.galc.device;

import java.math.BigInteger;

public abstract class SmartCardReader {
	public abstract Integer getCardNumber();
	
	public abstract Integer getCardNumber(String cardString, Integer cardIdMaxLength);
	
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

	public static Long hexToDecimal(String hex) {
		Long decimal = Long.parseLong(hex, 16);

	    return decimal;
	}

	public static String decimalToHex(Long decimal) {
		String hex = Long.toHexString(decimal);
		
	    return hex;
	}
	
	public static String correctHexString(String hex) {
		return decimalToHex(hexToDecimal(hex));
	}

}

