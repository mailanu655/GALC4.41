/**
 * 
 */
package com.honda.galc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author Subu Kathiresan
 * @date Dec 4, 2012
 */
public class StringUtilTest {
	
	@Test 
	public void bitArrayToCharsTest () {
		//assertTrue(StringUtil.binaryToChars("00000000").equals(new StringBuilder((char) 0x0)));
		assertTrue(StringUtil.bitArrayToChars("11111111").equals(StringUtil.bitArrayToChars("11111111")));
		assertEquals(StringUtil.bitArrayToChars("01000010"), new Character('B'));
		assertEquals(StringUtil.bitArrayToChars("01000011"), new Character('C'));
		assertEquals(StringUtil.bitArrayToChars("0100001001000011"), new Character('B') + new Character('C'));
		assertEquals(StringUtil.bitArrayToChars("100001001000011"), new Character('B') + new Character('C'));
		assertEquals(StringUtil.bitArrayToChars(" 100001001000011"), new Character('B') + new Character('C'));
		assertEquals(StringUtil.bitArrayToChars(" 100001001000011 "), new Character('B') + new Character('C'));
		assertEquals(StringUtil.bitArrayToChars("00100001101000011"), new Character((char) 0) + new Character('B') + new Character('C'));
		try {
			StringUtil.bitArrayToChars("011111009");
			fail();
		} catch(NumberFormatException ex) {}
		try {
			StringUtil.bitArrayToChars("+01111100");
			fail();
		} catch(NumberFormatException ex) {}
	}
	

	@Test
	public void replaceAllSpecialCharactes_whenMatches_thenCorrect() {		   
		assertEquals("352267085610672", StringUtil.removeAllSpecialCharacters("?35226-7085610672???"));	    
	}
	
	@Test 
	public void trimSpecialCharactersAtFrontOfString_whenMatches_thenCorrect() {		 
	     assertEquals("35226-7085610672???", StringUtil.trimSpecialCharacterAtFrontOnly("?--35226-7085610672???"));
	}
	
	@Test 
	public void trimSpecialCharactersAtEndOfString_whenMatches_thenCorrect() {
	     assertEquals("?35226-7085610672", StringUtil.trimSpecialCharacterAtEndOnly("?35226-7085610672???*"));
	}
	
	@Test 
	public void trimSpecialCharactersAtFrontAndEndOfString_whenMatches_thenCorrect() {		 
	     assertEquals("35226-7085610672", StringUtil.trimSpecialCharacterFrontAndEnd("?**$35226-7085610672???"));
	}
	
	
	
}
