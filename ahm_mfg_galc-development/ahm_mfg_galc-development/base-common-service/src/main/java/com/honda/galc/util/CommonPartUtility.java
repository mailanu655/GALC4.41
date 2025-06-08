package com.honda.galc.util;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.lcvinbom.VinPartDao;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.entity.enumtype.ParseStrategyType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
/**
 * 
 * <h3>CommonPartUtility</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CommonPartUtility description </p>
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
 * <TD>Mar 11, 2013</TD>
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
 * @since Mar 11, 2013
 */
public class CommonPartUtility {
	public final static char MULTIPLE_PART_MASK_DELIMITER=',';
	public final static char PART_MASK_DELIMITER = ';';
	public final static char WILD_CARD_ESCAPE_CHAR = '\\';
	public final static char WILD_CARD_ONE_CHAR = '?';
	public final static char WILD_CARD_MULTI_CHARS = '*';
	public final static char WILD_CARD_ONE_NUMBER = '#';
	public final static char WILD_CARD_ONE_ALPHANUMERIC = '^';
	public final static char WILD_CARD_ONE_ANYTHING = '%';
	public final static int RESULT_ERR = -1;
	
	public final static String REPLACEMENT_PRODUCT = "<<PRODUCT>>";
	public final static String REPLACEMENT_DESTINATION = "<<DESTINATION>>";
	public final static String REPLACEMENT_MODEL = "<<MODEL>>";
	public final static String REPLACEMENT_MODELCODE = "<<MODELCODE>>";
	public final static String REPLACEMENT_MODELYEAR = "<<MODELYEAR>>";
	public final static String REPLACEMENT_MODELTYPE = "<<MODELTYPE>>";
	public final static String REPLACEMENT_MODELOPTION = "<<MODELOPTION>>";
	public final static String REPLACEMENT_INTERIORCOLOR = "<<INTCOLOR>>";
	public final static String REPLACEMENT_EXTERIORCOLOR = "<<EXTCOLOR>>";
	public final static String REPLACEMENT_TRIMINTERIORCOLOR = "<<TRIMINTCOLOR>>";
	public final static String REPLACEMENT_TRIMEXTERIORCOLOR = "<<TRIMEXTCOLOR>>";
	public final static String REPLACEMENT_TRIMMODELOPTION = "<<TRIMMODELOPTION>>";
	
	//2016-02-01 - BAK - Add replacement constants for MBPN product type
	public final static String REPLACEMENT_HESCOLOR = "<<HESCOLOR>>";
	public final static String REPLACEMENT_MAINNUMBER = "<<MAINNO>>";
	public final static String REPLACEMENT_CLASSNUMBER = "<<CLASSNO>>";
	public final static String REPLACEMENT_PROTOTYPECODE = "<<PROTOTYPECODE>>";
	public final static String REPLACEMENT_TYPENUMBER = "<<TYPENO>>";
	public final static String REPLACEMENT_SUPPLEMENTARYNUMBER = "<<SUPPNO>>";
	public final static String REPLACEMENT_TARGETNUMBER = "<<TARGETNO>>";
	
	public final static String REPLACEMENT_PRODUCTSPEC = "<<PRODUCTSPEC>>";
	public final static String REPLACEMENT_SUMSPART = "<<SUMS_PART;{LETSystemName}>>";
	
	public final static String MULTI_PARSE_REGX = "\\}\\s?;\\s?\\{";
	
	private static PartMaskFormat maskFormat = PartMaskFormat.DEFAULT;
	
	public static enum PartMaskFormat{MSEXEL, DEFAULT, MSEXEL_ENHANCED; 
	public static PartMaskFormat getFormat(String formatName) {

		for(PartMaskFormat format :values()) {
			if(format.name().equalsIgnoreCase(formatName))	
				return format;
		}
		return DEFAULT;
	}}
	
	public static boolean verification(String aSerialNumber, String aMask, String pmfStr) throws SystemException {
		maskFormat = PartMaskFormat.getFormat(pmfStr);
		
		String[] masks = getPartMasks(aMask, null);
				
		for (String mask : masks) {
			if (maskFormat.equals(PartMaskFormat.MSEXEL)) {
				if (msexelVerification(aSerialNumber.toUpperCase(), mask.trim().toUpperCase())) {
					return true;
				}
			} else {
				if (simpleVerification(aSerialNumber.toUpperCase(), mask.trim().toUpperCase())) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean verification(String aSerialNumber, String aMask, String pmfStr, BaseProduct product) throws SystemException {
		maskFormat = PartMaskFormat.getFormat(pmfStr);
		
		String[] masks = getPartMasks(aMask, product);
		
		for (String mask : masks) {
		
			if (maskFormat.equals(PartMaskFormat.MSEXEL)) {
				if (msexelVerification(aSerialNumber.toUpperCase(), mask.trim().toUpperCase())) {
					return true;
				}
			} else {
				
				if (simpleVerification(aSerialNumber.toUpperCase(), mask.trim().toUpperCase(), product, pmfStr)) {
					return true;
				}
			}
		}

		return false;
	}
	
/**
 * Method that is used to compare the input date + N days is valid with compare to current date or not 

*/
	public static boolean verifyDateMask(String aSerialNumber, String aMask, int days) throws SystemException {
		
		try {
			/*
			 	replace the uppercase Y and D to lowercase for DateMask
			 	since all assigned partmask is converted to uppercase automatically
				eg. valid dates = yyyyMMdd, ddMMyyyy, dd-MM-yyyy, dd/MM/yyyy etc
			*/
			aMask = aMask.replace('Y', 'y').replace('D', 'd');
			DateFormat formatter = new SimpleDateFormat(aMask);
			formatter.setLenient(false);
			Date scanDate = null;
			scanDate = (Date) formatter.parse(aSerialNumber);		
			
			Calendar validDate = Calendar.getInstance();
			validDate.setTime(scanDate); 
			validDate.add(Calendar.DATE, days); // Adding N valid days from properties. Default 0
			
			Date today =new Date(); //get Current date
			Date todayWithZeroTime = null;
			todayWithZeroTime = formatter.parse(formatter.format(today));		
			return validDate.getTime().compareTo(todayWithZeroTime)>=0;
		} catch (Exception e) {
			String msgString = e.getMessage();
			Logger.getLogger().error(msgString);
			return false;
		}
		
	}
	
	public static String parse(String partnumber, String parsestrategy, List<ParseInfo> parseInfos) {
		ParseStrategyType parseStrategy = ParseStrategyType.valueOf(parsestrategy);
		StringBuilder sb = new StringBuilder();
		try {
			for (ParseInfo pif : parseInfos) {
				switch (parseStrategy) {
				case DELIMITED:
					sb.append(parseDelimited(partnumber, pif));
					break;
				case FIXED_LENGTH:
					sb.append(parseFixedLength(partnumber, pif));
					break;
				case TAG_VALUE:
					sb.append(parseTagValue(partnumber, pif));
					break;
				default:
					return partnumber;
				}
			} 
		} catch(Exception e) {
			
		}
		
		if(sb.length() > 0 ) 
			return sb.toString();
		else 
			return partnumber;
    }

	private static String parseDelimited(String partnumber, ParseInfo parseInfo) throws Exception{
		String delimiter = parseInfo.getDelimiter();
		int index = parseInfo.getIndex();

		if (!StringUtils.contains(partnumber, delimiter)) {
			Logger.getLogger().warn("Input String: " + partnumber + " does not contain delimiter " + delimiter);
			throw new Exception();
		}
		String[] partsns = StringUtils.split(partnumber, delimiter);
		if (partsns == null || partsns.length < 1 || index < 1 || partsns.length < index) {
			Logger.getLogger().warn("Invalid Delimiter " + delimiter + " and Index " + index + " for input string: "  + partnumber);
			return partnumber;
		}
		return partsns[index-1];
    }
	
	private static String parseFixedLength(String partnumber, ParseInfo parseInfo) throws Exception {
		int offset = parseInfo.getOffset();
		int length = parseInfo.getLength();
		int minLength = parseInfo.getMinPartSnLength();

		if(partnumber.length() < minLength)
			throw new Exception();
		
		if (offset < 0 
				|| length < 1 
				|| (partnumber.length() < (offset + length))) {
			Logger.getLogger().warn("Invalid Offset " + offset + " and Length " + length + " for input string: "  + partnumber);
			throw new Exception();
		}
		return partnumber.substring(offset, offset + length);
    }
	
	private static String parseTagValue(String partnumber, ParseInfo parseInfo) throws Exception {
		String partSn = "";
		if (parseInfo == null) return partnumber;
		DocumentBuilder db;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(partnumber));
	
			Document doc = db.parse(is);
			//  <tagname>value</tagname>
			NodeList nodes = doc.getElementsByTagName(parseInfo.getParseString().toUpperCase());
			if (null == nodes) return partnumber;
			partSn = nodes.item(0).getTextContent();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
		return partSn;
    }

	private static boolean msexelVerification(String serialNumber, String mask) {
		if ((mask.length() == 0) && (serialNumber.length() == 0) || 
			 mask.length() > serialNumber.length()	||
			 mask.indexOf("*") < 0 && mask.length() < serialNumber.length())
		return false;
		
		return doMsexelVerification(new StringBuffer(serialNumber), new StringBuffer(mask), false);
	}
	
	private static boolean doMsexelVerification(StringBuffer snBuf, StringBuffer maskBuf,	boolean isMultiWildCard) {
		if(isMultiWildCard){
			if(maskBuf.length() == 0)
				return true;
			else if(snBuf.length() == 0) 
				return false;
			else {
				if(maskBuf.charAt(0) == WILD_CARD_MULTI_CHARS) return doMsexelVerification(snBuf.deleteCharAt(0), maskBuf.deleteCharAt(0), true);
				else if(compareChar(snBuf.charAt(0), maskBuf.charAt(0))) 
					return doMsexelVerification(snBuf.deleteCharAt(0), maskBuf.deleteCharAt(0), false);
				else
					return doMsexelVerification(snBuf.deleteCharAt(0), maskBuf, true);
			}
			
		} else {
			if(maskBuf.length() == 0 && snBuf.length() == 0) return true;
			
			else if(maskBuf.length() > 0 && snBuf.length() > 0){
				if(maskBuf.charAt(0) == WILD_CARD_MULTI_CHARS) return doMsexelVerification(snBuf.deleteCharAt(0), maskBuf.deleteCharAt(0), true);
				else if(compareChar(snBuf.charAt(0), maskBuf.charAt(0))) 
					return doMsexelVerification(snBuf.deleteCharAt(0), maskBuf.deleteCharAt(0), false);
				else
					return false;
			} else 
				return false;
		}
	}

	private static boolean compareChar(char charToCompare, char mask) {
		if(mask == WILD_CARD_ONE_NUMBER) return charToCompare >= '0' && charToCompare <= '9';
		else return mask == '?' || charToCompare == mask;
	}
	
	//bak - 20150702 - overloaded simpleVerification to pass product
	public static boolean simpleVerification(String aSerialNumber, String aMask, BaseProduct product, String pmfStr) {
		maskFormat = PartMaskFormat.getFormat(pmfStr);
		
		if (product != null) {
			aMask = replacePartMaskConstants(aMask, product);
		}	
		
		return simpleVerification(aSerialNumber, aMask);				
	}
	
	//bak - 20150702 - added method to replace part mask constants with actual value
	//all of these constants can only be replaced during runtime after the product scan
	public static String replacePartMaskConstants(String aMask, BaseProduct product) {		
		try {
			aMask = aMask.toUpperCase();
			aMask = StringUtils.replace(aMask, REPLACEMENT_PRODUCT, product.getProductId());			
			
			switch (product.getProductType()) {
			case FRAME:
				aMask = StringUtils.replace(aMask, REPLACEMENT_DESTINATION, "K" + StringUtils.trim(product.getProductSpecCode().substring(4,5)));				
				aMask = StringUtils.replace(aMask, REPLACEMENT_MODEL, ProductSpec.extractModelYearCode(product.getProductSpecCode()) + 
						ProductSpec.padModelCode(ProductSpec.extractModelCode(product.getProductSpecCode())));
				aMask = StringUtils.replace(aMask, REPLACEMENT_MODELYEAR, ProductSpec.extractModelYearCode(product.getProductSpecCode()));
				aMask = StringUtils.replace(aMask, REPLACEMENT_MODELCODE, ProductSpec.padModelCode(ProductSpec.extractModelCode(product.getProductSpecCode())));
				aMask = StringUtils.replace(aMask, REPLACEMENT_MODELTYPE, ProductSpec.padModelTypeCode(ProductSpec.extractModelTypeCode(product.getProductSpecCode())));
				aMask = StringUtils.replace(aMask, REPLACEMENT_INTERIORCOLOR, ProductSpec.padIntColorCode(ProductSpec.extractIntColorCode(product.getProductSpecCode())));
				aMask = StringUtils.replace(aMask, REPLACEMENT_EXTERIORCOLOR, ProductSpec.padExtColorCode(getExteriorColorCode(product.getProductSpecCode())));
				aMask = StringUtils.replace(aMask, REPLACEMENT_MODELOPTION, ProductSpec.padModelOptionCode(ProductSpec.extractModelOptionCode(product.getProductSpecCode())));
				aMask = StringUtils.replace(aMask, REPLACEMENT_TRIMINTERIORCOLOR, StringUtils.stripEnd(ProductSpec.extractIntColorCode(product.getProductSpecCode())," "));
				aMask = StringUtils.replace(aMask, REPLACEMENT_TRIMEXTERIORCOLOR, getExteriorColorCode(product.getProductSpecCode()));
				aMask = StringUtils.replace(aMask, REPLACEMENT_TRIMMODELOPTION, StringUtils.stripEnd(ProductSpec.extractModelOptionCode(product.getProductSpecCode())," "));
				aMask = StringUtils.replace(aMask, REPLACEMENT_PRODUCTSPEC,product.getProductSpecCode());
				
				break;
			case ENGINE:
				aMask = StringUtils.replace(aMask, REPLACEMENT_MODEL, ProductSpec.extractModelYearCode(product.getProductSpecCode()) + 
						ProductSpec.padModelCode(ProductSpec.extractModelCode(product.getProductSpecCode())));
				aMask = StringUtils.replace(aMask, REPLACEMENT_MODELYEAR, ProductSpec.extractModelYearCode(product.getProductSpecCode()));
				aMask = StringUtils.replace(aMask, REPLACEMENT_MODELCODE, ProductSpec.padModelCode(ProductSpec.extractModelCode(product.getProductSpecCode())));
				aMask = StringUtils.replace(aMask, REPLACEMENT_MODELTYPE, ProductSpec.padModelTypeCode(ProductSpec.extractModelTypeCode(product.getProductSpecCode())));
				aMask = StringUtils.replace(aMask, REPLACEMENT_MODELOPTION, ProductSpec.padModelOptionCode(ProductSpec.extractModelOptionCode(product.getProductSpecCode())));				
				aMask = StringUtils.replace(aMask, REPLACEMENT_TRIMMODELOPTION, StringUtils.stripEnd(ProductSpec.extractModelOptionCode(product.getProductSpecCode())," "));
				aMask = StringUtils.replace(aMask, REPLACEMENT_PRODUCTSPEC,product.getProductSpecCode());
				break;
			case MBPN:
				//2016-02-01 - BAK - Added replacements for MBPN
				Mbpn mbpn = ServiceFactory.getDao(MbpnDao.class).findByKey(product.getProductSpecCode());				
				aMask = StringUtils.replace(aMask, REPLACEMENT_HESCOLOR, StringUtils.trim(mbpn.getHesColor()));
				aMask = StringUtils.replace(aMask, REPLACEMENT_MAINNUMBER, StringUtils.trim(mbpn.getMainNo()));
				aMask = StringUtils.replace(aMask, REPLACEMENT_CLASSNUMBER, StringUtils.trim(mbpn.getClassNo()));
				aMask = StringUtils.replace(aMask, REPLACEMENT_PROTOTYPECODE, StringUtils.trim(mbpn.getPrototypeCode()));
				aMask = StringUtils.replace(aMask, REPLACEMENT_TYPENUMBER, StringUtils.trim(mbpn.getTypeNo()));
				aMask = StringUtils.replace(aMask, REPLACEMENT_SUPPLEMENTARYNUMBER, StringUtils.trim(mbpn.getSupplementaryNo()));
				aMask = StringUtils.replace(aMask, REPLACEMENT_TARGETNUMBER, StringUtils.trim(mbpn.getTargetNo()));
				aMask = StringUtils.replace(aMask, REPLACEMENT_PRODUCTSPEC,product.getProductSpecCode());
				break;
			default : 
				break;
			}
						
			return aMask;
		} catch (Exception e) {
			// set Log			
			String msgString = e.getMessage();
			Logger.getLogger().error(msgString);	
			return aMask;
		}	
	}

	public static boolean simpleVerification(String aSerialNumber, String aMask) {
		try {
			aSerialNumber = StringUtils.stripEnd(aSerialNumber, " ").toUpperCase();
			aMask = StringUtils.stripEnd(aMask, " ").toUpperCase();
			
			int iStrLen1 = 0;
			int iStrLen2 = 0;
			int iIndex1, iIndex2;			
			String strComp = null;
			boolean bCompFlag = false;

			// check length
			if ((aMask.length() > 0) && (aSerialNumber.length() == 0))
				return false;
			if ((aMask.length() == 0) && (aSerialNumber.length() == 0))
				return false;
			//if mask does not contain escape characters and ends with '*' and format Type is not msexel_enhanced, then  string length of mask cannot be greater than string length of serial number  
			if(aMask.length() > aSerialNumber.length() && !hasWildCharactersWithPrecedingEscapeCharacters(aMask) &&
					aMask.lastIndexOf(WILD_CARD_MULTI_CHARS) == (aMask.length()-1) && !maskFormat.equals(PartMaskFormat.MSEXEL_ENHANCED))
				return false;
			//if mask does not contain '*', then  string length of mask cannot be greater than string length of serial number
			if (aMask.indexOf(WILD_CARD_MULTI_CHARS) < 0 && aMask.length() < aSerialNumber.length())
				return false;
			//if mask contain only '*' then accept any string
			if ((aMask.length() == 1 && aMask.charAt(0) == WILD_CARD_MULTI_CHARS) && aSerialNumber.length() > 0)
				return true;
			

			// comparison
			for (;(iStrLen1 < aMask.length()) && (iStrLen2 < aSerialNumber.length());) {
				if (aMask.charAt(iStrLen1) == WILD_CARD_MULTI_CHARS) {
					iStrLen1++;
					//iStrLen2++;
				}
				else {
					// get comparison character
					boolean endingMultiChars = false; 
					for (iIndex1 = iStrLen1;(iIndex1 < aMask.length()) ; iIndex1++) {
						if(aMask.charAt(iIndex1) == WILD_CARD_MULTI_CHARS){
							endingMultiChars = true;
							if(iIndex1 > 0 && aMask.charAt(iIndex1-1) != WILD_CARD_ESCAPE_CHAR)	break;
						}
					}
									
					if(hasPrecedingEscapeCharacters(aMask, WILD_CARD_MULTI_CHARS)){
						strComp = aMask.substring(iStrLen1, iIndex1).replace(WILD_CARD_ESCAPE_CHAR+"*", "*");
					}else{
						strComp = aMask.substring(iStrLen1, iIndex1);
					}
					// comparison
					bCompFlag = false;
					int limit = hasWildCharactersWithPrecedingEscapeCharacters(strComp)?aSerialNumber.length() - (strComp.length()-StringUtils.countMatches(strComp, "\\")):aSerialNumber.length() - strComp.length();
					for (iIndex1 = iStrLen2; iIndex1 <= limit && bCompFlag == false; iIndex1++) {
						bCompFlag = true;
						for (iIndex2 = 0; iIndex2 < strComp.length() && bCompFlag == true; iIndex2++) {
							switch (strComp.charAt(iIndex2)) {
							case WILD_CARD_ESCAPE_CHAR:
								
									if(strComp.charAt(iIndex2+1) != aSerialNumber.charAt(iIndex1 + iIndex2)){
										return false;
									}else{
										strComp = strComp.substring(0, iIndex2)+strComp.substring(iIndex2+1,strComp.length() );
									}
								
								break;
							case WILD_CARD_ONE_NUMBER:
								//Only check for a digit if using the enhanced part mask format
								if (maskFormat == PartMaskFormat.MSEXEL_ENHANCED){
									if (Character.isDigit(aSerialNumber.charAt(iIndex1 + iIndex2)) != true) {
										if (iStrLen1 == 0 && iStrLen2 == 0) {
											return false;
										}
										bCompFlag = false;
									}
								}
								break;
							case WILD_CARD_ONE_CHAR:
								if (Character.isLetter(aSerialNumber.charAt(iIndex1 + iIndex2)) != true) {
									if (iStrLen1 == 0 && iStrLen2 == 0) {
										return false;
									}
									bCompFlag = false;
								}
								break;
							case WILD_CARD_ONE_ALPHANUMERIC:
								String str = aSerialNumber.substring(iIndex1 + iIndex2, iIndex1 + iIndex2 + 1);
								if(!StringUtils.isAlphanumeric(str)) {
									bCompFlag = false;
								}
								break;
							case WILD_CARD_ONE_ANYTHING:
								//Accepts any single character, will always be true
								break;
							default:
								if (aSerialNumber.charAt(iIndex1 + iIndex2) != strComp.charAt(iIndex2)) {
									if (iStrLen1 == 0 && iStrLen2 == 0) {
										return false;
									}
									bCompFlag = false;
								} else {
									/**
									 * continue to verify to cover the following scenario 
									 * mask EZ*9 part sn: EZ19ABC9
									 */
									int tmpInt2 = strComp.length() + iIndex1;
									if(!endingMultiChars && tmpInt2 < aSerialNumber.length()) bCompFlag = false;
								}
								break;
							}							
						}
					}
					// check comparison result
					if (bCompFlag == true) {
						iStrLen1 = iStrLen1 + strComp.length()+StringUtils.countMatches(aMask, "\\");
						iStrLen2 = strComp.length() + iIndex1 - 1;
						
						if(!endingMultiChars && iStrLen2 < aSerialNumber.length()) return false; 
					}
					else {
						return false;
					}
				}
			}
			return (!completedMaskValidation(aMask, iStrLen1)) ? false : true;
		} catch (Exception e) {
			// set Log
			e.printStackTrace();
			String msgID = "SYSOT000";
			String msgString = e.getMessage();
			Logger.getLogger().error(msgString);
			throw new SystemException(msgID);
		}
	}
	
	private static boolean completedMaskValidation(String mask, int iStrLen1) {
		if(iStrLen1 > (mask.length() -1)) return true;
		String remainMask = mask.substring(iStrLen1).replace("\\", "");
		return StringUtils.isEmpty(remainMask) || remainMask.equals(Character.toString(WILD_CARD_MULTI_CHARS));
	}

	/**
	 *
	 * It is checked whether a serial number is suitable for the form of the mask.
	 * Return value is the index number of the mask list of the parameter.
	 * 
	 * <BR> Masknumber can have wild cards, such as ' # ' or ' * '.
	 *  This method compares ehch character of serialnumber with each character of 
	 *  a masknumber of the MaskList in order. If there is a ' * ' in a masknumber, 
	 *  subsequent characters are not compared. If there is a ' # ' in a masknumber, 
	 *  the character of a serialnumber at the same location as ' # ' in a masknumber is 
	 *  not compared but sebsequent characters are compared. If there is a masknumber which 
	 *  matches a serialnumber, return the index of the masknumber of the MaskList.
	 *  
	 * <BR> ex) serialnumber ----12345
		<BR>        masknumbers---AB###, 5678, 123*
		  <BR> In this case, 2 is returned. 
	 * @return int
	 * @param aSerialNumber java.lang.String
	 * @param aMaskList java.util.Vector
	 * @exception com.honda.global.galc.common.SystemException The exception description.
	 */
	public static int verification(String aSerialNumber,Vector<?> aMaskList, String partMaskFormat) throws SystemException {
		try {
			Enumeration<?> e;
			int index;
			for (index = 0, e = aMaskList.elements(); e.hasMoreElements(); index++) {
				if (verification(aSerialNumber, (String) e.nextElement(), partMaskFormat))
					return index;
			}
			return RESULT_ERR;
		} catch (Exception e) {
			// set Log
			String msgID = "SYSOT000";
			String msgString = e.getMessage();
			Logger.getLogger().error(msgString);
			throw new SystemException(msgID);
		}
	}
	
	public static PartSpec verify(String partSn, List<PartSpec> partSpecs, String partMaskFormat) {
		try {
			String parsepartSn = partSn;
			for (PartSpec spec : partSpecs) {
				parsepartSn = parsePartSerialNumber(spec,partSn);
				if (null == parsepartSn) return null;
				if (verification(parsepartSn, spec.getPartSerialNumberMask(), partMaskFormat))
					return spec;
			}
			return null;
		} catch (Exception e) {
			// set Log
			String msgString = e.getMessage();
			Logger.getLogger().error(msgString);
			throw new SystemException("Faild to verify part serial number, check configuration.", e);
		}
	}
	
	public static PartSpec verify(String partSn, List<PartSpec> partSpecs, String partMaskFormat, boolean isDateScan, 
			int validDays, BaseProduct product) {
	     return verify(partSn, partSpecs, partMaskFormat, isDateScan, validDays, product, true);
	}
	
	public static PartSpec verify(String partSn, List<PartSpec> partSpecs, String partMaskFormat, boolean isDateScan, 
			int validDays, BaseProduct product, boolean useParsedToCheck) {
		try {
			String partSnToCheck = partSn;
			for (PartSpec spec : partSpecs) {
				if(!StringUtils.isEmpty(spec.getParseStrategy()) && 
						ParseStrategyType.valueOf(spec.getParseStrategy()) == ParseStrategyType.FIXED_LENGTH) {
					int minLength = CommonPartUtility.getMinPartSnLength(spec); 
					if(partSn.length() < minLength) {
						Logger.getLogger().info("Input Date :"+partSn +" is not valid - Minimum part sn length check failed.");
						continue;
					}
				}
				
				partSnToCheck = useParsedToCheck ?  parsePartSerialNumber(spec,partSn) : partSn;
				if (null == partSnToCheck) return null;
				
				if (isDateScan){
					if(verifyDateMask(partSnToCheck, spec.getPartSerialNumberMask(), validDays))
						return spec;
				}
				else if (verification(partSnToCheck, spec.getPartSerialNumberMask(), partMaskFormat, product))
					return spec;
			}
			return null;
		} catch (Exception e) {
			// set Log
			String msgString = e.getMessage();
			Logger.getLogger().error(msgString);
			throw new SystemException("Faild to verify part serial number, check configuration.", e);
		}
	}
	
	public static  <T> String parsePartSerialNumber(T partSpec, String partSn ) {
		if(StringUtils.isNotEmpty((String)ReflectionUtils.invoke(partSpec, "getParseStrategy", new Object[]{})) &&
				StringUtils.isNotEmpty((String)ReflectionUtils.invoke(partSpec, "getParserInformation", new Object[]{}))){

		String parseStrategy = (String)ReflectionUtils.invoke(partSpec, "getParseStrategy", new Object[]{});
		List<ParseInfo> parseInfos = getParseInfos(partSpec);
		return parse(partSn, parseStrategy, parseInfos);
		}
		return partSn;
	}
	
	public static <T> List<ParseInfo> getParseInfos(T partSpec) {
		List<ParseInfo> inforList = new ArrayList<ParseInfo>();
		if(StringUtils.isNotEmpty((String)ReflectionUtils.invoke(partSpec, "getParseStrategy", new Object[]{})) &&
				StringUtils.isNotEmpty((String)ReflectionUtils.invoke(partSpec, "getParserInformation", new Object[]{}))){
			for(String s : getParseStringList(partSpec)){
				if(!StringUtils.isEmpty(s)) {
					inforList.add(new ParseInfo(StringUtils.trim(s)));
				}
			}
		}
		return inforList;
	}
	
	private static <T> List<String> getParseStringList(T partSpec) {
		ParseStrategyType strategyType = ParseStrategyType.valueOf((String)ReflectionUtils.invoke(partSpec, "getParseStrategy", new Object[]{}));
		if(strategyType == ParseStrategyType.TAG_VALUE) {
			String[] tokens = ((String) ReflectionUtils.invoke(partSpec, "getParserInformation", new Object[]{})).split(Delimiter.SEMI_COLON);
			return Arrays.asList(tokens);
		}
		else {
			List<String> list = new ArrayList<String>();
			String[] split = ((String) ReflectionUtils.invoke(partSpec, "getParserInformation", new Object[]{})).split(MULTI_PARSE_REGX);
			for(String s : split) {
				s = s.replaceFirst("^\\s+", ""); //trim off leading space if needed
				if(!s.startsWith("{")) s="{" + s;
			    if(!s.endsWith("}")) s = s+"}";
			    list.add(s);
			}
			return list;
		}
	}
	
	public static int getMinPartSnLength(PartSpec partSpec) {
		int minLength = -1;
		for(ParseInfo info : getParseInfos(partSpec))
			if(info.getMinPartSnLength() > minLength) minLength = info.getMinPartSnLength();
		
		return minLength;
	}

	public static List<String> checkDuplicatePart(ProductBuildResult productResult,
			List<? extends ProductBuildResult> list) {
		
		List<String> duplicatedList = new ArrayList<String>();
		if (list != null) {
			for (ProductBuildResult buildResult : list) {
				if (!buildResult.getProductId().equals(
						productResult.getProductId())) {
					duplicatedList.add(buildResult.getProductId());
				}
			}
		}
		return duplicatedList;
	}
	
	private static String getExteriorColorCode(String productSpecCode){
		String extColorCode = StringUtils.stripEnd(ProductSpec.extractExtColorCode(productSpecCode)," ");
		String exceptionChars = PropertyService.getExtColorExceptionChars();
		if(exceptionChars.length() > 0){
		String[] exChars = exceptionChars.split(",");
			for(int i=0;i<exChars.length;i++){
				if(extColorCode.endsWith(exChars[i])){
					extColorCode = extColorCode.substring(0, extColorCode.length()-1);
					break;
				}
			}
		}
		return extColorCode;
	}
	
	private static List<String> getIntColorCodeMasks(String productSpecCode, String intColorCodeMask){
		
		List<String> options = new ArrayList<String>();
		Pattern p = Pattern.compile("(?<=\\|)((.[^|]*;)+.[^|>]*)");
		 Matcher m = p.matcher(intColorCodeMask);
		 int start = 0, end = 0;
		 ArrayList<String> mask = new ArrayList<String>(); 
		 int i=0;
		 while(m.find())
		 {
			 mask.add(m.group());
			 if(start == 0)start = m.start();
			 end = m.end();
			 i++;
		 }
		 	
		 if(mask.size()>0){
			String colorCode = ProductSpec.extractIntColorCode(productSpecCode);
			
			String intColor =  intColorCodeMask.substring(start-15, start) +intColorCodeMask.substring(end, end+2) ;
			if(intColor.toUpperCase().equalsIgnoreCase("<<INTCOLORCODE|>>")){
			String prefix = intColorCodeMask.substring(0,start-15);
			String suffix = intColorCodeMask.substring(end+2,intColorCodeMask.length());
			
			for(String msk:mask){
				 String[] tempMasks = msk.split(String.valueOf(PART_MASK_DELIMITER));
				 boolean validOptions = false;
				 for(String temp: tempMasks){
					 if(temp.equalsIgnoreCase(colorCode)){
						 validOptions = true; break;
					 }
				 }
				 if(validOptions){
					 for(String temp: tempMasks){
						 options.add(prefix+temp+suffix);
				  }
				 }
			}
			 String c = prefix+colorCode+suffix;
			 if(!options.contains(c)) options.add(c);
			}
		 }else{
			 options.add(intColorCodeMask);
		 }
		 return options;
	}
	
	
	public static String parsePartMask(String partSerialNumberMask){
		String amask = StringUtils.replace(partSerialNumberMask,"<<"+CommonPartUtility.WILD_CARD_MULTI_CHARS+">>",String.valueOf(CommonPartUtility.WILD_CARD_MULTI_CHARS));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_ALPHANUMERIC+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_ALPHANUMERIC));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_ANYTHING+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_ANYTHING));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_CHAR+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_CHAR));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_NUMBER+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_NUMBER));		 
		 
		 Pattern p = Pattern.compile("<<.+;\\d+>>");
		 String tmpmask = "";
		 
		 if(!StringUtils.contains(amask, "<<")){		 
			 return amask;
		 }
		 String[] masks = amask.split("<<");
		 for(String pmask : masks){
			 if(StringUtils.contains(pmask, ">>")){
				 pmask = "<<"+pmask;				 
				 Matcher m = p.matcher(pmask);
				 String mask = "";
				 if(m.find()){  
					 mask = m.group(0); 
				 }else{ 
					 tmpmask += pmask; 
				 }
				 if(mask.length()>0){
					 String parsedMask="";
					 String[] temp = mask.substring(2, mask.length()-2).split(String.valueOf(PART_MASK_DELIMITER));
					 if(temp.length == 2){
						 String wildChar = temp[0];
						 Integer count = Integer.parseInt(temp[1]);							 
						 for(int i=0;i<count;i++){
							 parsedMask= parsedMask+wildChar;
						 }
					 }			 
					 tmpmask += StringUtils.replace(pmask, mask, parsedMask);
				 }
			 }else{
				 tmpmask += pmask;
			 }			 
			 amask = tmpmask;
		 }
		 return amask;		
	}
	
	public static String parsePartMaskDisplay(String partSerialNumberMask){
		String amask = StringUtils.replace(partSerialNumberMask,"<<"+CommonPartUtility.WILD_CARD_MULTI_CHARS+">>",String.valueOf(CommonPartUtility.WILD_CARD_MULTI_CHARS));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_ALPHANUMERIC+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_ALPHANUMERIC));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_ANYTHING+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_ANYTHING));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_CHAR+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_CHAR));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_NUMBER+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_NUMBER));		 
		 	 
		 return amask;		
	}
	
	public static boolean hasWildCharactersWithPrecedingEscapeCharacters(String partSerialNumberMask){
		StringBuffer partMask = new StringBuffer();
		for(char c: partSerialNumberMask.toCharArray()){
			if(isWildChar(c) || c == WILD_CARD_ESCAPE_CHAR){
				if(hasPrecedingEscapeCharacters(partSerialNumberMask, c)){
					return true;
				}
			}else{
				partMask.append(c);
			}
			
		}
		return false;
	}
	
	public static boolean hasPrecedingEscapeCharacters(String partMask, char wildChar){
		int escapeCharindex = partMask.indexOf(WILD_CARD_ESCAPE_CHAR);
		
		if(escapeCharindex > 0 && partMask.length() > 1 && (partMask.charAt(escapeCharindex + 1) == wildChar)){
			return true;
		}
		return false;
	}
	
	public static boolean isWildChar(char c){
		boolean wildChar = false;
		switch(c){
		case CommonPartUtility.WILD_CARD_MULTI_CHARS:
			wildChar= true;
			break;
		case CommonPartUtility.WILD_CARD_ONE_ALPHANUMERIC:
			wildChar= true;
			break;
		case CommonPartUtility.WILD_CARD_ONE_ANYTHING:
			wildChar= true;
			break;
		case CommonPartUtility.WILD_CARD_ONE_CHAR:
			wildChar= true;
			break;
		case CommonPartUtility.WILD_CARD_ONE_NUMBER:
			wildChar= true;
			break;
		default:
			break;
		}
		
		return wildChar;
	}
	
	private static String[] getPartMasks(String aMask, BaseProduct product){
		String[] masks;
		if (aMask.contains(String.valueOf(MULTIPLE_PART_MASK_DELIMITER))) {
			String escapeDelimiter = String.valueOf(WILD_CARD_ESCAPE_CHAR)+String.valueOf(MULTIPLE_PART_MASK_DELIMITER);
			if(!aMask.contains(escapeDelimiter)){
				masks = aMask.split(String.valueOf(MULTIPLE_PART_MASK_DELIMITER));
			}else{
				List<String> masksList = new ArrayList<String>();
				StringBuffer maskBuffer = new StringBuffer();
				for(int i=0;i<aMask.length();i++){
					
					char c = aMask.charAt(i);
					if(c== MULTIPLE_PART_MASK_DELIMITER && (i> 0 && aMask.charAt(i-1) != WILD_CARD_ESCAPE_CHAR)){
						masksList.add(maskBuffer.toString());
						maskBuffer.setLength(0);
					}else{
						maskBuffer.append(c);
					}
				}
				if(maskBuffer.length()> 0)masksList.add(maskBuffer.toString());
				masks = new String[masksList.size()];int i=0;
				for(String m: masksList){
					masks[i] = m;i++;
				}
				
			}

		} else {
			masks = new String[1];
			masks[0] = aMask;
		}
		
		List<String> parsedMasks = new ArrayList<String>();
		for(String mask: masks){			
			String parsed = parsePartMask(mask);
			if(product!= null){
				List<String> intColorCodeMasks = getIntColorCodeMasks(product.getProductSpecCode(),parsed.trim());
				if(intColorCodeMasks.size()> 0) parsedMasks.addAll(intColorCodeMasks);
				
				List<String> sumsPartMasks = getSumsPartMask(product, parsed.trim());
				if(sumsPartMasks.size()> 0) parsedMasks.addAll(sumsPartMasks);
			}else{
				parsedMasks.add(parsed);
			}
		}
		String[] allMasks = new String[parsedMasks.size()];
		int i=0;
		for(String parsedMask:parsedMasks){
			allMasks[i] = parsedMask;
			i++;
		}
		
		return allMasks;
	}

	private static List<String> getSumsPartMask(BaseProduct product, String partMask) {
		List<String> options = new ArrayList<String>();
		Pattern p = Pattern.compile("(?<=)((.*;)+.[^|>]*)");
		 Matcher m = p.matcher(partMask);
		 int start = 0, end = 0;
		 ArrayList<String> mask = new ArrayList<String>(); 
		 int i=0;
		 while(m.find())
		 {
			 mask.add(m.group());
			 if(start == 0)start = m.start();
			 end = m.end();
			 i++;
		 }
		List<String> allMasks = new ArrayList<String>();
		String maskPrefix =  partMask.length() > 12?partMask.substring(0, 12): "";
		if(maskPrefix.length() > 0 && maskPrefix.toUpperCase().equalsIgnoreCase("<<SUMS_PART;")){
			String maskSuffix = partMask.substring(12,end);
			
			String[] multiParts = maskSuffix.split(",");
			
			for(String systemName:multiParts) {
				List<String> partNumbers = getPartNumberForPart(product.getProductId(),systemName);
				allMasks.addAll(partNumbers);
			}
		
		}
		
		return allMasks;
	}

	private static List<String> getPartNumberForPart(String productId, String systemName) {
		return ServiceFactory.getDao(VinPartDao.class).getPartNumbersByProductIdAndSystemName(productId,systemName);
	}

}