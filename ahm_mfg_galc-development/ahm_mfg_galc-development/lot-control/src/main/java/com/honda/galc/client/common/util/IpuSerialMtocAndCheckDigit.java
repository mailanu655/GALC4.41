package com.honda.galc.client.common.util;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.printing.PrintAttributeFormatter;

public class IpuSerialMtocAndCheckDigit implements PrintAttributeFormatter{
	private static Map<Character, Integer> checkDigitValues = createValueMap();
	
	private static Map<Character, Integer> createValueMap(){
		Map<Character, Integer> result = new HashMap<Character, Integer>();
		result.put('0', 0);
		result.put('1', 1);
		result.put('2', 2);
		result.put('3', 3);
		result.put('4', 4);
		result.put('5', 5);
		result.put('6', 6);
		result.put('7', 7);
		result.put('8', 8);
		result.put('9', 9);
		result.put('A', 10);
		result.put('B', 11);
		result.put('C', 12);
		result.put('D', 13);
		result.put('E', 14);
		result.put('F', 15);
		result.put('G', 16);
		result.put('H', 17);
		result.put('I', 18);
		result.put('J', 19);
		result.put('K', 20);
		result.put('L', 21);
		result.put('M', 22);
		result.put('N', 23);
		result.put('O', 24);
		result.put('P', 25);
		result.put('Q', 26);
		result.put('R', 27);
		result.put('S', 28);
		result.put('T', 29);
		result.put('U', 30);
		result.put('V', 31);
		result.put('W', 32);
		result.put('X', 33);
		result.put('Y', 34);
		result.put('Z', 35);
		result.put('-', 36);
		result.put('.', 37);
		result.put(' ', 38);
		result.put('$', 39);
		result.put('/', 40);
		result.put('+', 41);
		result.put('%', 42);
		return Collections.unmodifiableMap(result);
	}

	public String execute(DataContainer dc){
		SubProduct ipu = getDao(SubProductDao.class).findByKey(dc.get(DataContainerTag.PRODUCT_ID).toString());
		String ipuAndMtoc = ipu.getId()+ipu.getProductSpecCode();
		return appendMod43CheckDigit(ipuAndMtoc);
	}
	
	private String appendMod43CheckDigit(String input){
		char[] chars;
		int sum = 0;
		chars = input.toCharArray();
		for (char c : chars){
			sum += checkDigitValues.get(c);
		}
		Integer checkDigit = sum % 43;
		Character checkDigitValue = null;
		for (Character key : checkDigitValues.keySet()){
			if (checkDigitValues.get(key).equals(checkDigit)){
				checkDigitValue = key;
				break;
			}
		}
		String returnString = input;
		if (checkDigitValue!=null)
			returnString = returnString + checkDigitValue;
		return returnString;
	}
	
}
