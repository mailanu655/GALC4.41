package com.honda.galc.rest.security;

import java.text.ParseException;

import javax.xml.bind.DatatypeConverter;

import com.honda.galc.constant.Delimiter;

/**
 * @author Subu Kathiresan
 * @date Mar 26, 2017
 */
public class BasicAuthDecoder {
	
	public static final int OFFSET = 0;
	public static final int SPLIT_MAX = 2;
	public static final String REPLACE_EXPR = "[B|b]asic ";
	
    public static String[] decode(String auth) throws ParseException {
    	
        auth = auth.replaceFirst(REPLACE_EXPR, "");
        byte[] decodedBytes = DatatypeConverter.parseBase64Binary(auth);
 
        //If decode fails throw parse exception
        if(decodedBytes == null || decodedBytes.length == 0){
            throw new ParseException ("Unable to parse empty authentication string", OFFSET);
        }
 
        return new String(decodedBytes).split(Delimiter.COLON, SPLIT_MAX);
    }
}
