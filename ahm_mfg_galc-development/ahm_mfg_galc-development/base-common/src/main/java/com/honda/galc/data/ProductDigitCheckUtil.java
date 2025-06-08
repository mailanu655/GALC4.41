package com.honda.galc.data;

import java.util.HashMap;
import java.util.Map;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.data.ProductNumberDef.TokenType;

public class ProductDigitCheckUtil {
	private static Map<Character,Integer> letterValues = new HashMap<Character,Integer>();
	private static Map<Integer,Integer> positionFactors = new HashMap<Integer,Integer>();
	static {
		letterValues.put('A',1);
		letterValues.put('B',2);
		letterValues.put('C',3);
		letterValues.put('D',4);
		letterValues.put('E',5);
		letterValues.put('F',6);
		letterValues.put('G',7);
		letterValues.put('H',8);
		letterValues.put('J',1);
		letterValues.put('K',2);
		letterValues.put('L',3);
		letterValues.put('M',4);
		letterValues.put('N',5);
		letterValues.put('P',7);
		letterValues.put('R',9);
		letterValues.put('S',2);
		letterValues.put('T',3);
		letterValues.put('U',4);
		letterValues.put('V',5);
		letterValues.put('W',6);
		letterValues.put('X',7);
		letterValues.put('Y',8);
		letterValues.put('Z',9);
		letterValues.put('1',1);
		letterValues.put('2',2);
		letterValues.put('3',3);
		letterValues.put('4',4);
		letterValues.put('5',5);
		letterValues.put('6',6);
		letterValues.put('7',7);
		letterValues.put('8',8);
		letterValues.put('9',9);
		letterValues.put('0',0);

	}
	
	static {
		positionFactors.put(1,8);
		positionFactors.put(2,7);
		positionFactors.put(3,6);
		positionFactors.put(4,5);
		positionFactors.put(5,4);
		positionFactors.put(6,3);
		positionFactors.put(7,2);
		positionFactors.put(8,10);
		positionFactors.put(9,0);
		positionFactors.put(10,9);
		positionFactors.put(11,8);
		positionFactors.put(12,7);
		positionFactors.put(13,6);
		positionFactors.put(14,5);
		positionFactors.put(15,4);
		positionFactors.put(16,3);
		positionFactors.put(17,2);
	}
	
	/**
	 * calculate the check digit. Algorithm :
	 * Every char in the vin string is mapped to a number (letterValue) based on the value of the character(letterValues map). 
	 * Its position in the vin is also mapped to another number(positionFactor (positionFactors, start from 1).
	 * multiply letterValue * positionFactor, sum them over each char for the vin.
	 * The result is moduled by 11. The remaider is the check dit. If the remaider = 10, use 'X' as check digit. 
	 * 
	 * @param vin
	 * @return
	 */

	public static char calculateVinCheckDigit(String vin) {

		int count = 0;
		for(int i = 0;i<vin.length();i++) {
			char digit =  vin.charAt(i);
			if(!letterValues.containsKey(digit)) {
				if(digit != '*')
					throw new TaskException("Illegal character " + digit + " in vin : " + vin);

			}else {
				int number = letterValues.get(digit);
				int positionFactor  = positionFactors.get(i+1);
				count += number * positionFactor;
			}
		}

		int remainder = count % 11;
		if(remainder == 10) return 'X';
		else return Character.forDigit(remainder,10);
	} 
	
	public static boolean isCheckDigitValid(String number) {
		if (number == null || number.length() == 0) {
			return false;
		}
		long sum = 0;
		number = number.trim();
		for (int ix = 0; ix < number.length() - 1; ix++) {
			sum += (long) number.charAt(ix);
		}
		char checkDigit = number.charAt(number.length() - 1);
		int i = (int) (sum % 26) + 'A';
		char c = (char) i;
		return c == checkDigit;
	}
	
	public static char calculateCheckDigit(String number) {
		long sum = 0;
		if (number != null) {
			number = number.trim();
			for (int ix = 0; ix < number.length() - 1; ix++) {
				sum += (long) number.charAt(ix);
			}
		}
		int i = (int) (sum % 26) + 'A';
		char c = (char) i;
		return c;
	}
	
	public static boolean check(ProductNumberDef def, String number){
		
		if(!def.getTokens().containsKey(TokenType.CHECK_DIGIT.name())) return true;
		
		if(def == ProductNumberDef.VIN)
			return def.getToken(TokenType.CHECK_DIGIT.name(), number).equals(Character.toString(calculateVinCheckDigit(number)));
		else if(def == ProductNumberDef.BPN)
			return def.getToken(TokenType.CHECK_DIGIT.name(), number).equals(Character.toString(calculateMbpnCheckDigit(number)));
		else 
			return def.getToken(TokenType.CHECK_DIGIT.name(), number).equals(Character.toString(calculateCheckDigit(number)));
		
	}

	private static char calculateMbpnCheckDigit(String number) {
		return 'I'; //todo
	}

}
