package com.honda.galc.data;

import static com.honda.galc.data.ProductType.BLOCK;
import static com.honda.galc.data.ProductType.BUMPER;
import static com.honda.galc.data.ProductType.CONROD;
import static com.honda.galc.data.ProductType.CRANKSHAFT;
import static com.honda.galc.data.ProductType.ENGINE;
import static com.honda.galc.data.ProductType.FRAME;
import static com.honda.galc.data.ProductType.HEAD;
import static com.honda.galc.data.ProductType.KNUCKLE;
import static com.honda.galc.data.ProductType.MBPN;
import static com.honda.galc.data.ProductType.MCASE;
import static com.honda.galc.data.ProductType.MISSION;
import static com.honda.galc.data.ProductType.MPDN;
import static com.honda.galc.data.ProductType.MPDR;
import static com.honda.galc.data.ProductType.PLASTICS;
import static com.honda.galc.data.ProductType.PSDN;
import static com.honda.galc.data.ProductType.PSDR;
import static com.honda.galc.data.ProductType.TCCASE;
import static com.honda.galc.data.ProductType.TDU;
import static com.honda.galc.data.ProductType.IPU_MBPN;
import static com.honda.galc.data.ProductType.BMP_MBPN;
import static com.honda.galc.data.ProductType.SUBFRAME;
import static com.honda.galc.data.ProductType.KNU_MBPN;
import static com.honda.galc.data.ProductType.FIPUCASE;
import static com.honda.galc.data.ProductType.RIPUCASE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.Delimiter;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ProductNumberDef</code> is product number format translation into
 * java structure.
 * 
 * </p>
 * <h4>Usage and Example</h4>
 * This class defines known product numbers as constants. <br />
 * To define new number definition in properties see
 * <code>ProductNumberDefFactory</code>.
 * <h4>Special Notes</h4>
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
 * <TD>Karol Wozniak</TD>
 * <TD>Mar 2, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class ProductNumberDef {

	public enum NumberType {
		DC, MC, IN, MBPN, BMP
	}

	public enum ProductNumberType {
		DCH,DCB,MCH,MCB,EIN,VIN,VIN_JPN,KIN,PLA,VIN_NCD,DCH_AN,MCH_AN,DCB_AN,DCB2_AN,DCB_HATC,MCB_AN,DCH_JP,DCH_HMA,DCB_HMA,MCH_HMA,MCB_HMA,CS_HMA,CR_HMA,MBPN,
		BMP,BPN,MIN,DCMCASE,DCTCCASE,MCMCASE,MCTCCASE,MPDR,MPDN,PSDR,PSDN,DCB_HMA_11,TDU_HMA,IPU_HMA,BUMPER_HMA,SUBFRAME_HMA,KNUCKLE_HMA,BUMPER_HCM,MBPN_HMA_13,
		CS_HMA_16,CR_HMA_16,GENERIC_HMA,GENERIC_AEP,BPA_ELP,DCH_12,DCCS,DCCR,AAP_AEP_HEAD,DCFIPUCASE,DCRIPUCASE,MCFIPUCASE,MCRIPUCASE;
	}
	
	public enum TokenType {
		MODEL, PART_LEVEL, PLANT, YEAR, MONTH, DAY, LINE, DIE, CORE, SEQUENCE, CHECK_DIGIT, REVISION, DATE, MANUFACTURER, BODY_TRANSMISSION_TYPE, VEHICLE_GRADE,PART_NUMBER, STATION, 
		HOUR, MINUTE, SECOND, TS /* Tenth of Second*/, TIME, JOURNAL_RANK, PIN_RANK, RANK, MEASUREMENT,SUB_ID,MOLD, PART_TYPE, SYMBOL, SHIFT, MATERIAL, REPAIR, MACHINE, MOULD, CAVITY,
		PART_CODE, VARIATION_CODE, DEPT, SR_NO, DAY_OF_YEAR, SUPPLIER,MODEL_CODE,COMPONENT;
	}

	private static Map<String, ProductNumberDef> instances = new LinkedHashMap<String, ProductNumberDef>();
	
	public static ProductNumberDef DCH = create("DCH", HEAD, NumberType.DC, 17, getDchNumberDef());
	public static ProductNumberDef DCB = create("DCB", BLOCK, NumberType.DC, 17, getDcbNumberDef());
	public static ProductNumberDef MCH = create("MCH", HEAD, NumberType.MC, 16, getMchNumberDef());
	public static ProductNumberDef MCB = create("MCB", BLOCK, NumberType.MC, 16, getMcbNumberDef());
	
	public static ProductNumberDef EIN = create("EIN", ENGINE, NumberType.IN, 12,getEinNumberDef());
	public static ProductNumberDef VIN = create("VIN", FRAME, NumberType.IN, 17,getVinNumberDef());
	public static ProductNumberDef VIN_JPN = create("VIN_JPN", FRAME, NumberType.IN, 11, getJpnVinNumberDef());
	public static ProductNumberDef VIN_NCD = create("VIN_NCD", FRAME, NumberType.IN, 17,getNcdVinNumberDef());
	public static ProductNumberDef KNU = create("KNU", KNUCKLE, NumberType.IN, 17,getKinNumberDef());
	public static ProductNumberDef BPN = create("MBPN", MBPN, NumberType.MBPN, 17,getMbpnNumberDef());
	public static ProductNumberDef BMP = create("BMP", BUMPER, NumberType.BMP, 17,getMbpnNumberDef());
	public static ProductNumberDef PLA = create("PLA", PLASTICS, NumberType.IN, 13, getNumberDef());

	public static ProductNumberDef MIN = create("MIN", MISSION, NumberType.IN, 11,getMinNumberDef());
	public static ProductNumberDef DCMCASE = create("DCMCASE", MCASE, NumberType.DC, 16,getDcCaseNumberDef());
	public static ProductNumberDef DCTCCASE = create("DCTCCASE", TCCASE, NumberType.DC, 16,getDcCaseNumberDef());
	public static ProductNumberDef MCMCASE = create("MCMCASE", MCASE, NumberType.MC, 13,getMcCaseNumberDef());
	public static ProductNumberDef MCTCCASE = create("MCTCCASE", TCCASE, NumberType.MC, 13,getMcCaseNumberDef());
	public static ProductNumberDef DCFIPUCASE = create("DCFIPUCASE", FIPUCASE, NumberType.DC, 18,getDcFrontIpuCaseNumberDef());
	public static ProductNumberDef DCRIPUCASE = create("DCRIPUCASE", RIPUCASE, NumberType.DC, 18,getDcRearIpuCaseNumberDef());
	public static ProductNumberDef MCFIPUCASE = create("MCFIPUCASE", FIPUCASE, NumberType.MC, 16,getMcFrontIpuCaseNumberDef());
	public static ProductNumberDef MCRIPUCASE = create("MCRIPUCASE", RIPUCASE, NumberType.MC, 16,getMcRearIpuCaseNumberDef());
	public static ProductNumberDef MPDRIN = create("MPDR", MPDR, NumberType.IN, 16,getPulleyNumberDef());
	public static ProductNumberDef MPDNIN = create("MPDN", MPDN, NumberType.IN, 16,getPulleyNumberDef());
	public static ProductNumberDef PSDRIN = create("PSDR", PSDR, NumberType.IN, 16,getPulleyNumberDef());
	public static ProductNumberDef PSDNIN = create("PSDN", PSDN, NumberType.IN, 16,getPulleyNumberDef());

	public static ProductNumberDef DCH_AN = create("DCH_AN", HEAD, NumberType.DC, 17, getDchAnNumberDef()); //Anna DC Head
	public static ProductNumberDef MCH_AN = create("MCH_AN", HEAD, NumberType.MC, 10, getMchAnNumberDef()); //Anna MC Head	
	public static ProductNumberDef DCB_AN = create("DCB_AN", BLOCK, NumberType.DC, 10, getDcbAnNumberDef()); //Anna DC Block
	public static ProductNumberDef MCB_AN = create("MCB_AN", BLOCK, NumberType.MC, 16, getMcbAnNumberDef()); //Anna MC Block	
	public static ProductNumberDef DCB2_AN = create("DCB2_AN", BLOCK, NumberType.DC, 17, getDcb2AnNumberDef()); //Anna DC Block Format 2

	public static ProductNumberDef DCH_JP = create("DCH_JP", HEAD, NumberType.DC, 7, getDchJpNumberDef());  //Japan Head
	
	public static ProductNumberDef DCB_HATC = create("DCB_HATC", BLOCK, NumberType.DC, 15, getDcbHatcNumberDef()); // Thailand Block

	public static ProductNumberDef DCH_HMA = create("DCH_HMA", HEAD, NumberType.DC, 11, getDch_HMA_NumberDef()); //HMA DC Head	
	public static ProductNumberDef DCH_12 = create("DCH_12", HEAD, NumberType.DC, 12, getDch_HMA_12_NumberDef()); //HMA DC Head 12
	public static ProductNumberDef DCB_HMA = create("DCB_HMA", BLOCK, NumberType.DC, 10, getDcb_HMA_NumberDef()); //HMA DC Block
	public static ProductNumberDef DCB_HMA_11 = create("DCB_HMA_11", BLOCK, NumberType.DC, 11, getDcb_HMA_11_NumberDef()); //HMA DC Block
	public static ProductNumberDef MCH_HMA = create("MCH_HMA", HEAD, NumberType.MC, 11, getMch_HMA_NumberDef());	//HMA MC Head
	public static ProductNumberDef MCB_HMA = create("MCB_HMA", BLOCK, NumberType.MC, 11, getMcb_HMA_NumberDef()); //HMA MC Block
	public static ProductNumberDef DCCS = create("DCCS", CRANKSHAFT, NumberType.DC, 18,getDcCsNumberDef());
	public static ProductNumberDef DCCR = create("DCCR", CONROD, NumberType.DC, 16,getDcCrNumberDef());
	public static ProductNumberDef CS_HMA = create("CS_HMA", CRANKSHAFT, NumberType.MC, 26, getCsHmaMcNumberDef()); //Crankshaft
	public static ProductNumberDef CR_HMA = create("CR_HMA", CONROD, NumberType.MC, 19, getCrHmaMcNumberDef()); //Connection Rod
	public static ProductNumberDef TDU_HMA = create("TDU_HMA", TDU, NumberType.MBPN, 17,getTduHmaNumberDef()); //HMA TDU
	public static ProductNumberDef IPU_HMA = create("IPU_HMA", IPU_MBPN, NumberType.MBPN, 17,getIpuHmaNumberDef()); //HMA IPU
	public static ProductNumberDef BUMPER_HMA = create("BUMPER_HMA", BMP_MBPN, NumberType.MBPN, 17,getBmpHmaNumberDef()); //HMA Bumper
	public static ProductNumberDef SUBFRAME_HMA = create("SUBFRAME_HMA", SUBFRAME, NumberType.MBPN, 13,getSubframeHmaNumberDef()); //HMA SubFrame
	public static ProductNumberDef KNUCKLE_HMA = create("KNUCKLE_HMA", KNU_MBPN, NumberType.MBPN, 13,getKnuckleHmaNumberDef()); //HMA Knuckle
	public static ProductNumberDef GENERIC_HMA = create("GENERIC_HMA", MBPN, NumberType.MBPN, 17,getGenericNumberDef()); //Generic 17 digit ID number
	public static ProductNumberDef GENERIC_AEP = create("GENERIC_AEP", MBPN, NumberType.MBPN, 16,getGenericNumberDef()); //Generic 16 digit ID number
	public static ProductNumberDef BUMPER_HCM = create("BUMPER_HCM", BUMPER, NumberType.BMP, 17,getBumperHcmNumberDef()); //HMA Knuckle
	public static ProductNumberDef AAP_AEP_HEAD = create("AAP_AEP_HEAD", MBPN, NumberType.MBPN, 12,getGenericNumberDef()); //AAP_AEP_HEAD 12 digit ID number
	
	public static ProductNumberDef MBPN_HMA_13 = create("MBPN_HMA_13", MBPN, NumberType.MBPN, 13,getNumberDef());
	public static ProductNumberDef CS_HMA_16 = create("CS_HMA_16", CRANKSHAFT, NumberType.DC, 16,getNumberDef()); 
	public static ProductNumberDef CR_HMA_16 = create("CR_HMA_16", CONROD, NumberType.DC, 16,getDcCrNumberDef());
	
	public static ProductNumberDef DCH_CSS = create("DCH_CSS", HEAD, NumberType.DC, 16, getDchCssNumberDef());
	public static ProductNumberDef DCB_CSS = create("DCB_CSS", BLOCK, NumberType.DC, 16, getDcbCssNumberDef());
	public static ProductNumberDef DCH_HUM = create("DCH_HUM", HEAD, NumberType.DC, 11, getDchHumNumberDef());
	public static ProductNumberDef DCB_HUM = create("DCB_HUM", BLOCK, NumberType.DC, 9, getDcbHumNumberDef()); 
	
	public static ProductNumberDef MBPN_PMC = create("MBPN_PMC", MBPN, NumberType.MBPN, 17,getMbpnPmcNumberDef());
	public static ProductNumberDef IPU_PMC = create("IPU_PMC", MBPN, NumberType.MBPN, 8,getIpuPmcNumberDef());
	
	public static ProductNumberDef BPA_ELP = create("BPA_ELP", MBPN, NumberType.MBPN, 9, getBpaElpNumberDef());
	
	private String name;
	private ProductType productType;
	private NumberType numberType;
	private int length;
	private Map<String, TokenDef> tokens = new LinkedHashMap<String, TokenDef>();
	private boolean checkDigit;

	// === private constructor === //
	private ProductNumberDef(String name, ProductType productType, NumberType numberType, int length, Map<String, TokenDef> tokens) {
		this.name = name;
		this.productType = productType;
		this.numberType = numberType;
		this.length = length;
		this.checkDigit = tokens.containsKey(TokenType.CHECK_DIGIT.name()) ? true : false;

		if (tokens != null) {
			this.tokens.putAll(tokens);
		}
	}
	
	// === factory/static methods === //
	public static ProductNumberDef create(String name, ProductType productType, NumberType numberType, int length) {
		ProductNumberDef def = new ProductNumberDef(name, productType, numberType, length, new LinkedHashMap<String, TokenDef>());
		getInstances().put(def.getName(), def);
		return def;
	}

	public static ProductNumberDef create(String name, ProductType productType, NumberType numberType, int length, Map<String, TokenDef> tokens) {
		ProductNumberDef def = new ProductNumberDef(name, productType, numberType, length, tokens);
		getInstances().put(def.getName(), def);
		return def;
	}
	

	public static List<ProductNumberDef> getProductNumberDefs(String names) {
		List<ProductNumberDef> defs = new ArrayList<ProductNumberDef>();
		if(StringUtils.isEmpty(names)) return defs;
		
		String[] nameList = names.split(Delimiter.COMMA);
		for(String name : nameList) {
			ProductNumberDef def = getInstances().get(name.trim());
			if(def != null) defs.add(def);
		}
		return defs;
	}

	private static Map<String, ProductNumberDef> getInstances() {
		return instances;
	}

	// === get === //
	public Map<String, TokenDef> getTokens() {
		return tokens;
	}

	public int getLength() {
		return length;
	}

	public String getName() {
		return name;
	}

	public NumberType getNumberType() {
		return numberType;
	}

	public ProductType getProductType() {
		return productType;
	}

	public static List<ProductNumberDef> getProductNumberDef(ProductType productType) {
		return filter(getInstances().values(), productType);
	}
	
	public static List<ProductNumberDef> getProductNumberDef(ProductType productType, NumberType numberType) {
		return filter(getInstances().values(), productType, numberType);
	}

	public static List<ProductNumberDef> filter(Collection<ProductNumberDef> list, ProductType productType) {
		List<ProductNumberDef> filteredList = new ArrayList<ProductNumberDef>();
		if (list == null || productType == null) {
			return filteredList;
		}
		for (ProductNumberDef def : list) {
			if (def == null) {
				continue;
			}
			if (productType.equals(def.getProductType())) {
				filteredList.add(def);
			}
		}
		return filteredList;
	}
	
	public static List<ProductNumberDef> filter(Collection<ProductNumberDef> list, ProductType productType, NumberType numberType) {
		List<ProductNumberDef> filteredList = new ArrayList<ProductNumberDef>();
		if (list == null || productType == null || numberType == null) {
			return filteredList;
		}
		for (ProductNumberDef def : list) {
			if (def == null) {
				continue;
			}
			if (productType.equals(def.getProductType()) && numberType.equals(def.getNumberType())) {
				filteredList.add(def);
			}
		}
		return filteredList;
	}
	
	private static void putToken(Map<String, TokenDef> map, TokenDef def) {
		if (map == null || def == null) {
			return;
		}
		map.put(def.getName(), def);
	}
	
	// ==== predefined product number definitions === //
	public static Map<String, TokenDef> getDchNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
		putToken(map, new TokenDef(TokenType.PART_LEVEL, 3, 1));
		putToken(map, new TokenDef(TokenType.PLANT, 4, 2));
		putToken(map, new TokenDef(TokenType.YEAR, 6, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 7, 1));
		putToken(map, new TokenDef(TokenType.DAY, 8, 2));
		putToken(map, new TokenDef(TokenType.DATE, 6, 4));
		putToken(map, new TokenDef(TokenType.LINE, 10, 1));
		putToken(map, new TokenDef(TokenType.DIE, 11, 1));
		putToken(map, new TokenDef(TokenType.CORE, 12, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 13, 3));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 16, 1));
		return map;
	}

	public static Map<String, TokenDef> getMchNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
		putToken(map, new TokenDef(TokenType.REVISION, 3, 1));
		putToken(map, new TokenDef(TokenType.PLANT, 4, 2));
		putToken(map, new TokenDef(TokenType.YEAR, 6, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 7, 1));
		putToken(map, new TokenDef(TokenType.DAY, 8, 2));
		putToken(map, new TokenDef(TokenType.DATE, 6, 4));
		putToken(map, new TokenDef(TokenType.LINE, 10, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 11, 4));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 15, 1));
		return map;
	}
	

	private static Map<String, TokenDef> getMbpnNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.PLANT, 0, 1));
		putToken(map, new TokenDef(TokenType.MACHINE, 1, 2));
		putToken(map, new TokenDef(TokenType.MOULD, 3, 2));
		putToken(map, new TokenDef(TokenType.CAVITY, 5, 1));
		putToken(map, new TokenDef(TokenType.YEAR, 6, 2));
		putToken(map, new TokenDef(TokenType.MONTH, 8, 2));
		putToken(map, new TokenDef(TokenType.DAY, 10, 2));
		putToken(map, new TokenDef(TokenType.DATE, 6, 6));
		putToken(map, new TokenDef(TokenType.VARIATION_CODE, 3, 3));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 12, 4));
		putToken(map, new TokenDef(TokenType.PART_CODE, 16, 1));

		return map;
	}
	
	private static Map<String, TokenDef> getMbpnPmcNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.PLANT, 0, 1));
		putToken(map, new TokenDef(TokenType.DEPT, 1, 1));
		putToken(map, new TokenDef(TokenType.STATION, 2, 1));
		putToken(map, new TokenDef(TokenType.YEAR, 3, 4));
		putToken(map, new TokenDef(TokenType.MONTH, 7, 2));
		putToken(map, new TokenDef(TokenType.DATE, 9, 2));
		putToken(map, new TokenDef(TokenType.HOUR, 11, 2));
		putToken(map, new TokenDef(TokenType.MINUTE, 13, 2));
		putToken(map, new TokenDef(TokenType.SECOND, 15, 2));
		
		return map;
	}
	
	private static Map<String, TokenDef> getIpuPmcNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.PART_CODE, 0, 2));
		putToken(map, new TokenDef(TokenType.SR_NO, 2, 6));
		return map;
	}
	
	private static Map<String, TokenDef> getBpaElpNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MOLD, 0, 2));
		putToken(map, new TokenDef(TokenType.DAY_OF_YEAR, 2, 3));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 5, 3));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 8, 1));
		return map;
	}
	
	
	
//////////////////////////////////
//HMA Definitions
	
public static Map<String, TokenDef> getMcb_HMA_NumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.PLANT, 0, 1));
		putToken(map, new TokenDef(TokenType.MODEL, 1, 1));
		putToken(map, new TokenDef(TokenType.YEAR, 2, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 3, 1));
		putToken(map, new TokenDef(TokenType.DAY, 4, 2));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 6, 4));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 10, 1));
		return map;
	}
	
	public static Map<String, TokenDef> getDcb_HMA_NumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.LINE, 0, 1));
		putToken(map, new TokenDef(TokenType.DIE, 1, 2));
		putToken(map, new TokenDef(TokenType.MONTH, 3, 1));
		putToken(map, new TokenDef(TokenType.DAY, 4, 2));		
		putToken(map, new TokenDef(TokenType.SEQUENCE, 6, 3));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 9, 1));
		return map;
	}

	public static Map<String, TokenDef> getDcb_HMA_11_NumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.LINE, 0, 1));
		putToken(map, new TokenDef(TokenType.DIE, 1, 2));
		putToken(map, new TokenDef(TokenType.MONTH, 3, 1));
		putToken(map, new TokenDef(TokenType.DAY, 4, 2));		
		putToken(map, new TokenDef(TokenType.SEQUENCE, 6, 3));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 9, 1));
		putToken(map, new TokenDef(TokenType.YEAR, 10, 1));
		
		return map;
	}
	public static Map<String, TokenDef> getDch_HMA_NumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MACHINE, 0, 1));
		putToken(map, new TokenDef(TokenType.MODEL, 1, 1));
		putToken(map, new TokenDef(TokenType.DIE, 2, 1));
		putToken(map, new TokenDef(TokenType.YEAR, 3, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 4, 1));
		putToken(map, new TokenDef(TokenType.DAY, 5, 2));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 7, 3));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 10, 1));
		return map;
	}
	
	public static Map<String, TokenDef> getDch_HMA_12_NumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MACHINE, 0, 1));
		putToken(map, new TokenDef(TokenType.MODEL, 1, 1));
		putToken(map, new TokenDef(TokenType.DIE, 2, 2));
		putToken(map, new TokenDef(TokenType.YEAR, 4, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 5, 1));
		putToken(map, new TokenDef(TokenType.DAY, 6, 2));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 8, 3));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 11, 1));
		return map;
	}

	public static Map<String, TokenDef> getMch_HMA_NumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.PLANT, 0, 1));
		putToken(map, new TokenDef(TokenType.MODEL, 1, 1));
		putToken(map, new TokenDef(TokenType.YEAR, 2, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 3, 1));
		putToken(map, new TokenDef(TokenType.DAY, 4, 2));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 6, 4));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 10, 1));
		return map;
	}
	
	public static Map<String, TokenDef> getCsHmaMcNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
		putToken(map, new TokenDef(TokenType.PART_LEVEL, 3, 1));
		putToken(map, new TokenDef(TokenType.SUPPLIER, 4, 2));
		putToken(map, new TokenDef(TokenType.YEAR,  6, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 7, 1));
		putToken(map, new TokenDef(TokenType.DAY,   8, 2));
		putToken(map, new TokenDef(TokenType.LINE, 10, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 11, 5));
		putToken(map, new TokenDef(TokenType.PIN_RANK, 16, 6));
		putToken(map, new TokenDef(TokenType.JOURNAL_RANK, 22, 4));
		return map;
	}
	
	public static Map<String, TokenDef> getDcCsNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
		putToken(map, new TokenDef(TokenType.PART_LEVEL, 3, 1));
		putToken(map, new TokenDef(TokenType.SUPPLIER, 4, 2));
		putToken(map, new TokenDef(TokenType.YEAR,  6, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 7, 1));
		putToken(map, new TokenDef(TokenType.DAY,   8, 2));
		putToken(map, new TokenDef(TokenType.LINE, 10, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 11, 4));
		putToken(map, new TokenDef(TokenType.DIE, 15, 2));
		putToken(map, new TokenDef(TokenType.MATERIAL, 17, 1));
		return map;
	}
	
	public static Map<String, TokenDef> getCrHmaMcNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
		putToken(map, new TokenDef(TokenType.PART_LEVEL, 3, 1));
		putToken(map, new TokenDef(TokenType.SUPPLIER, 4, 2));
		putToken(map, new TokenDef(TokenType.YEAR,  6, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 7, 1));
		putToken(map, new TokenDef(TokenType.DAY,   8, 2));
		putToken(map, new TokenDef(TokenType.LINE, 10, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 11, 5));
		putToken(map, new TokenDef(TokenType.RANK, 16, 1));
		putToken(map, new TokenDef(TokenType.MEASUREMENT, 17, 2));
		
		return map;
	}
	
	public static Map<String, TokenDef> getDcCrNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
		putToken(map, new TokenDef(TokenType.PART_LEVEL, 3, 1));
		putToken(map, new TokenDef(TokenType.SUPPLIER, 4, 2));
		putToken(map, new TokenDef(TokenType.YEAR,  6, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 7, 1));
		putToken(map, new TokenDef(TokenType.DAY,   8, 2));
		putToken(map, new TokenDef(TokenType.LINE, 10, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 11, 5));		
		return map;
	}
	
	private static Map<String, TokenDef> getTduHmaNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.PART_CODE, 0, 3));
		putToken(map, new TokenDef(TokenType.PART_TYPE, 3, 4));
		putToken(map, new TokenDef(TokenType.YEAR, 7, 2));
		putToken(map, new TokenDef(TokenType.MONTH, 9, 2));
		putToken(map, new TokenDef(TokenType.DAY, 11, 2));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 13, 4));
		
		return map;
	}
	
	private static Map<String, TokenDef> getIpuHmaNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.PART_CODE, 0, 3));
		putToken(map, new TokenDef(TokenType.LINE, 3, 1));
		putToken(map, new TokenDef(TokenType.PART_TYPE, 4, 4));
		putToken(map, new TokenDef(TokenType.YEAR, 8, 2));
		putToken(map, new TokenDef(TokenType.MONTH, 10, 1));
		putToken(map, new TokenDef(TokenType.DAY, 11, 2));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 13, 4));
		
		return map;
	}
	
	private static Map<String, TokenDef> getBmpHmaNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.PART_CODE, 0, 3));
		putToken(map, new TokenDef(TokenType.LINE, 3, 1));
		putToken(map, new TokenDef(TokenType.PART_TYPE, 4, 4));
		putToken(map, new TokenDef(TokenType.YEAR, 8, 2));
		putToken(map, new TokenDef(TokenType.MONTH, 10, 1));
		putToken(map, new TokenDef(TokenType.DAY, 11, 2));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 13, 4));
		
		return map;
	}
	
	private static Map<String, TokenDef> getSubframeHmaNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.SUB_ID, 0, 2));
		putToken(map, new TokenDef(TokenType.YEAR, 2, 2));
		putToken(map, new TokenDef(TokenType.MONTH, 4, 2));
		putToken(map, new TokenDef(TokenType.MODEL, 6, 2));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 8, 5));	
		
		return map;
	}
	
	private static Map<String, TokenDef> getKnuckleHmaNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.SUB_ID, 0, 2));
		putToken(map, new TokenDef(TokenType.YEAR, 2, 2));
		putToken(map, new TokenDef(TokenType.MONTH, 4, 2));
		putToken(map, new TokenDef(TokenType.MODEL, 6, 2));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 8, 5));		
		
		return map;
	}
	
	private static Map<String, TokenDef> getBumperHcmNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.PLANT, 0, 2));
		putToken(map, new TokenDef(TokenType.DATE, 2, 5));
		putToken(map, new TokenDef(TokenType.MOULD, 7, 2));
		putToken(map, new TokenDef(TokenType.TIME, 9, 6));
		putToken(map, new TokenDef(TokenType.SUB_ID, 15, 2));		
		
		return map;
	}
	
	private static Map<String, TokenDef> getGenericNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.SEQUENCE, 13, 4));		
		
		return map;
	}
	
//////////////////////////////////
// Japan Definitions
	
	private static Map<String, TokenDef> getDchJpNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		//contents unkonwn for now - PC
		return map;
	}


//////////////////////////////////
//AEP Definitions

	public static Map<String, TokenDef> getMchAnNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 1));
		putToken(map, new TokenDef(TokenType.YEAR, 1, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 2, 1));
		putToken(map, new TokenDef(TokenType.DAY, 3, 2));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 5, 4));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 9, 1));
		return map;
	}
		
	private static Map<String, TokenDef> getDchAnNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
		putToken(map, new TokenDef(TokenType.REVISION, 3, 1));
		putToken(map, new TokenDef(TokenType.PLANT, 4, 2));
		putToken(map, new TokenDef(TokenType.YEAR, 6, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 7, 1));
		putToken(map, new TokenDef(TokenType.DAY, 8, 2));
		putToken(map, new TokenDef(TokenType.LINE, 10, 1));
		putToken(map, new TokenDef(TokenType.DIE, 11, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 12, 4));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 16, 1));
		return map;
	}

	private static Map<String, TokenDef> getDchHumNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 1));
		putToken(map, new TokenDef(TokenType.YEAR, 1, 2));
		putToken(map, new TokenDef(TokenType.MONTH, 3, 2));
		putToken(map, new TokenDef(TokenType.DAY, 5, 2));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 7, 4));
		return map;
	}

	private static Map<String, TokenDef> getDchCssNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
		putToken(map, new TokenDef(TokenType.PLANT, 3, 3));
		putToken(map, new TokenDef(TokenType.YEAR, 6, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 7, 1));
		putToken(map, new TokenDef(TokenType.DAY, 8, 2));
		putToken(map, new TokenDef(TokenType.LINE, 10, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 11, 4));
		putToken(map, new TokenDef(TokenType.DIE, 15, 1));
		return map;
	}
	
	
	private static Map<String, TokenDef> getDcbHumNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 1));
		putToken(map, new TokenDef(TokenType.YEAR, 1, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 2, 2));
		putToken(map, new TokenDef(TokenType.DAY, 4, 2));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 6, 3));
		return map;
	}
	
	private static Map<String, TokenDef> getDcbCssNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
		putToken(map, new TokenDef(TokenType.DIE, 3, 2));
		putToken(map, new TokenDef(TokenType.YEAR, 5, 2));
		putToken(map, new TokenDef(TokenType.MONTH, 7, 2));
		putToken(map, new TokenDef(TokenType.DAY, 9, 2));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 11, 4));
		putToken(map, new TokenDef(TokenType.LINE, 15, 1));
		return map;		
	}
	
	private static Map<String, TokenDef> getMcbAnNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
		putToken(map, new TokenDef(TokenType.REVISION, 3, 1));
		putToken(map, new TokenDef(TokenType.PLANT, 4, 2));
		putToken(map, new TokenDef(TokenType.YEAR, 6, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 7, 1));
		putToken(map, new TokenDef(TokenType.DAY, 8, 2));
		putToken(map, new TokenDef(TokenType.DATE, 6, 4));
		putToken(map, new TokenDef(TokenType.LINE, 10, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 11, 4));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 15, 1));
		return map;
	}

	private static Map<String, TokenDef> getDcbAnNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.LINE, 0, 1));
		putToken(map, new TokenDef(TokenType.DIE, 1, 1));
		putToken(map, new TokenDef(TokenType.MODEL, 2, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 3, 1));
		putToken(map, new TokenDef(TokenType.DAY, 4, 2));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 6, 3));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 9, 1));
		return map;
	}
	//AEP block serial format 2-4 digit sequence
	public static Map<String, TokenDef> getDcb2AnNumberDef() {
			Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
			putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
			putToken(map, new TokenDef(TokenType.PART_LEVEL, 3, 1));
			putToken(map, new TokenDef(TokenType.PLANT, 4, 1));
			putToken(map, new TokenDef(TokenType.YEAR, 5, 1));
			putToken(map, new TokenDef(TokenType.MONTH, 6, 1));
			putToken(map, new TokenDef(TokenType.DAY, 7, 2));
			putToken(map, new TokenDef(TokenType.DATE, 5, 4));
			putToken(map, new TokenDef(TokenType.LINE, 9, 1));
			putToken(map, new TokenDef(TokenType.DIE, 10, 2));
			putToken(map, new TokenDef(TokenType.SEQUENCE, 12, 4));
			putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 16, 1));
			return map;
	}

//////////////////////////////////
	
	//Thailand Block Format
	public static Map<String, TokenDef> getDcbHatcNumberDef(){
			Map <String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
			putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
			putToken(map, new TokenDef(TokenType.DIE, 3, 2));
			putToken(map, new TokenDef(TokenType.YEAR, 5, 2));
			putToken(map, new TokenDef(TokenType.MONTH, 7, 2));
			putToken(map, new TokenDef(TokenType.DAY, 9, 2));
			putToken(map, new TokenDef(TokenType.MACHINE, 11, 1));
			putToken(map, new TokenDef(TokenType.SEQUENCE, 12, 3));
			return map;
			
	}

//////////////////////////////////

	public static Map<String, TokenDef> getDcbNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
		putToken(map, new TokenDef(TokenType.PART_LEVEL, 3, 1));
		putToken(map, new TokenDef(TokenType.PLANT, 4, 2));
		putToken(map, new TokenDef(TokenType.YEAR, 6, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 7, 1));
		putToken(map, new TokenDef(TokenType.DAY, 8, 2));
		putToken(map, new TokenDef(TokenType.DATE, 6, 4));
		putToken(map, new TokenDef(TokenType.LINE, 10, 1));
		putToken(map, new TokenDef(TokenType.DIE, 11, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 12, 4));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 16, 1));
		return map;		
	}

	public static Map<String, TokenDef> getMcbNumberDef() {
		return getMchNumberDef();
	}

	public static Map<String, TokenDef> getEinNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 5));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 5, 7));
		return map;
	}

	public static Map<String, TokenDef> getVinNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MANUFACTURER, 0, 3));
		putToken(map, new TokenDef(TokenType.MODEL, 3, 2));
		putToken(map, new TokenDef(TokenType.BODY_TRANSMISSION_TYPE, 6, 1));
		putToken(map, new TokenDef(TokenType.VEHICLE_GRADE, 7, 1));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 8, 1));
		putToken(map, new TokenDef(TokenType.YEAR, 9, 1));
		putToken(map, new TokenDef(TokenType.PLANT, 10, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 11, 6));

		return map;
	}
	
	public static Map<String, TokenDef> getJpnVinNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 5, 6));

		return map;
	}
	
	public static Map<String, TokenDef> getNcdVinNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MANUFACTURER, 0, 3));
		putToken(map, new TokenDef(TokenType.MODEL, 3, 2));
		putToken(map, new TokenDef(TokenType.BODY_TRANSMISSION_TYPE, 6, 1));
		putToken(map, new TokenDef(TokenType.VEHICLE_GRADE, 7, 1));
		putToken(map, new TokenDef(TokenType.YEAR, 9, 1));
		putToken(map, new TokenDef(TokenType.PLANT, 10, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 11, 6));

		return map;
	}

	public static Map<String, TokenDef> getKinNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.PART_NUMBER, 0, 10));
		putToken(map, new TokenDef(TokenType.YEAR, 10, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 11, 6));
		return map;
	}

//	Transmission Plant
	public static Map<String, TokenDef> getMinNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.PLANT, 0, 1));
		putToken(map, new TokenDef(TokenType.MODEL, 1, 2));
		putToken(map, new TokenDef(TokenType.PART_TYPE, 3, 1));
		putToken(map, new TokenDef(TokenType.SYMBOL, 4, 1));
		putToken(map, new TokenDef(TokenType.PART_NUMBER, 5, 1));
		putToken(map, new TokenDef(TokenType.YEAR, 6, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 7, 5));
		return map;
	}

	public static Map<String, TokenDef> getDcCaseNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.PLANT, 0, 1));
		putToken(map, new TokenDef(TokenType.MODEL, 1, 1));
		putToken(map, new TokenDef(TokenType.YEAR, 2, 2));
		putToken(map, new TokenDef(TokenType.MONTH, 4, 2));
		putToken(map, new TokenDef(TokenType.DAY, 6, 2));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 8, 4));
		putToken(map, new TokenDef(TokenType.SHIFT, 12, 1));
		putToken(map, new TokenDef(TokenType.STATION, 13, 2));
		putToken(map, new TokenDef(TokenType.REVISION, 15, 1));
		return map;
	}
	
	public static Map<String, TokenDef> getDcFrontIpuCaseNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
		putToken(map, new TokenDef(TokenType.PART_LEVEL, 3, 1));
		putToken(map, new TokenDef(TokenType.COMPONENT, 4, 1));
		putToken(map, new TokenDef(TokenType.PLANT, 5, 1));
		putToken(map, new TokenDef(TokenType.YEAR, 6, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 7, 1));
		putToken(map, new TokenDef(TokenType.DAY, 8, 2));
		putToken(map, new TokenDef(TokenType.MACHINE, 10, 2));
		putToken(map, new TokenDef(TokenType.DIE, 12, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 13, 4));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 17, 1));
		return map;
	}
	
	public static Map<String, TokenDef> getDcRearIpuCaseNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
		putToken(map, new TokenDef(TokenType.PART_LEVEL, 3, 1));
		putToken(map, new TokenDef(TokenType.COMPONENT, 4, 1));
		putToken(map, new TokenDef(TokenType.PLANT, 5, 1));
		putToken(map, new TokenDef(TokenType.YEAR, 6, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 7, 1));
		putToken(map, new TokenDef(TokenType.DAY, 8, 2));
		putToken(map, new TokenDef(TokenType.MACHINE, 10, 2));
		putToken(map, new TokenDef(TokenType.DIE, 12, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 13, 4));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 17, 1));
		return map;
	}
	
	public static Map<String, TokenDef> getMcFrontIpuCaseNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
		putToken(map, new TokenDef(TokenType.PART_LEVEL, 3, 1));
		putToken(map, new TokenDef(TokenType.COMPONENT, 4, 1));
		putToken(map, new TokenDef(TokenType.PLANT, 5, 1));
		putToken(map, new TokenDef(TokenType.YEAR, 6, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 7, 1));
		putToken(map, new TokenDef(TokenType.DAY, 8, 2));
		putToken(map, new TokenDef(TokenType.LINE, 10, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 11, 4));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 15, 1));
		return map;
	}
	
	public static Map<String, TokenDef> getMcRearIpuCaseNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.MODEL, 0, 3));
		putToken(map, new TokenDef(TokenType.PART_LEVEL, 3, 1));
		putToken(map, new TokenDef(TokenType.COMPONENT, 4, 1));
		putToken(map, new TokenDef(TokenType.PLANT, 5, 1));
		putToken(map, new TokenDef(TokenType.YEAR, 6, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 7, 1));
		putToken(map, new TokenDef(TokenType.DAY, 8, 2));
		putToken(map, new TokenDef(TokenType.LINE, 10, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 11, 4));
		putToken(map, new TokenDef(TokenType.CHECK_DIGIT, 15, 1));
		return map;
	}
	
	public static Map<String, TokenDef> getMcCaseNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.PART_TYPE, 0, 1));
		putToken(map, new TokenDef(TokenType.MODEL, 1, 3));
		putToken(map, new TokenDef(TokenType.YEAR, 4, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 5, 1));
		putToken(map, new TokenDef(TokenType.DAY, 6, 1));
		putToken(map, new TokenDef(TokenType.SHIFT, 7, 1));
		putToken(map, new TokenDef(TokenType.PLANT, 8, 1));
		putToken(map, new TokenDef(TokenType.LINE, 9, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 10, 3));
		return map;
	}
	
	public static Map<String, TokenDef> getPulleyNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		putToken(map, new TokenDef(TokenType.PART_TYPE, 0, 1));
		putToken(map, new TokenDef(TokenType.MODEL, 1, 3));
		putToken(map, new TokenDef(TokenType.MANUFACTURER, 4, 1)); 
		putToken(map, new TokenDef(TokenType.YEAR, 5, 1));
		putToken(map, new TokenDef(TokenType.MONTH, 6, 1));
		putToken(map, new TokenDef(TokenType.DAY, 7, 2));
		putToken(map, new TokenDef(TokenType.LINE, 9, 1));
		putToken(map, new TokenDef(TokenType.SEQUENCE, 10, 4));
		putToken(map, new TokenDef(TokenType.MATERIAL, 14, 1));
		putToken(map, new TokenDef(TokenType.REPAIR, 15, 1));
		return map;
	}	

	public static Map<String, TokenDef> getNumberDef() {
		Map<String, TokenDef> map = new LinkedHashMap<String, TokenDef>();
		return map;
	}

	// === Object overrides === //
	@Override
	public int hashCode() {
		if (name == null) {
			return 0;
		}
		return getName().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ProductNumberDef)) {
			return false;
		}
		ProductNumberDef def = (ProductNumberDef) o;
		String name = getName();
		String otherName = def.getName();
		return name == null ? otherName == null : name.equals(otherName);
	}

	@Override
	public String toString() {
		return getName();
	}

	// === business api === //
	public String getToken(String name, String number) {
		String noneExistingToken = null;
		// String noneExistingToken = "";
		String token = noneExistingToken;
		if (number == null) {
			return token;
		}
		if (name == null) {
			return token;
		}
		TokenDef def = getTokens().get(name);
		if (def == null) {
			return token;
		}
		
		number = number.trim();
		int length = number.length();
		if (def.getStartIx() > length -1) {
			return token;
		}
		
		int endIx =  def.getStartIx() + def.getLength() < length ? def.getStartIx() + def.getLength() : length;
		token = number.substring(def.getStartIx(), endIx);
		return token;
	}

	public String getModel(String number) {
		return getToken(TokenType.MODEL.name(), number);
	}

	public String getPartLevel(String number) {
		return getToken(TokenType.PART_LEVEL.name(), number);
	}

	public String getPlant(String number) {
		return getToken(TokenType.PLANT.name(), number);
	}

	public String getYear(String number) {
		return getToken(TokenType.YEAR.name(), number);
	}

	public String getMonth(String number) {
		return getToken(TokenType.MONTH.name(), number);
	}

	public String getDay(String number) {
		return getToken(TokenType.DAY.name(), number);
	}

	public String getLine(String number) {
		return getToken(TokenType.LINE.name(), number);
	}

	public String getDie(String number) {
		return getToken(TokenType.DIE.name(), number);
	}

	public String getSequence(String number) {
		return getToken(TokenType.SEQUENCE.name(), number);
	}
	
	public String getJournalRank(String number){
		return getToken(TokenType.JOURNAL_RANK.name(), number);		
	}
	
	public String getPinRank(String number){
		return getToken(TokenType.PIN_RANK.name(), number);
	}
	
	public String getRank(String number){
		return getToken(TokenType.RANK.name(), number);		
	}

	public String getMeasurement(String number){
		return getToken(TokenType.MEASUREMENT.name(), number);
	}

	public int getSequenceLength() {
		TokenDef def = getTokens().get(TokenType.SEQUENCE.name());
		return def == null ? 0 : def.getLength();
	}
	public String getCheckDigit(String number) {
		return getToken(TokenType.CHECK_DIGIT.name(), number);
	}

	public String getRevision(String number) {
		return getToken(TokenType.REVISION.name(), number);
	}

	public String getDate(String number) {
		return getToken(TokenType.DATE.name(), number);
	}

	public String getManufacturer(String number) {
		return getToken(TokenType.MANUFACTURER.name(), number);
	}

	public String getBodyTransmissionType(String number) {
		return getToken(TokenType.BODY_TRANSMISSION_TYPE.name(), number);
	}

	public String getVehicleGrade(String number) {
		return getToken(TokenType.VEHICLE_GRADE.name(), number);
	}
	
	public String getPartType(String number) {
		return getToken(TokenType.PART_TYPE.name(), number);
	}
	
	public String getSymbol(String number) {
		return getToken(TokenType.SYMBOL.name(), number);
	}
	
	public String getShift(String number) {
		return getToken(TokenType.SHIFT.name(), number);
	}
	
	public String getMaterial(String number) {
		return getToken(TokenType.MATERIAL.name(), number);
	}
	
	public String getSubId(String number) {
		return getToken(TokenType.SUB_ID.name(), number);
	}
	
	public String getMachine(String number) {
		return getToken(TokenType.MACHINE.name(), number);
	}
	
	public String getSupplier(String number) {
		return getToken(TokenType.SUPPLIER.name(), number);
	}

	public boolean isNumberValid(String number, ProductType productType, NumberType numberType) {
		List<ProductNumberDef> list = filter(getInstances().values(), productType, numberType);
		return isNumberValid(number, list);
	}
	
	public static boolean isNumberValid(String number, Collection<ProductNumberDef> productNumberDefs) {
		if (number == null) {
			return false;
		}
		
		if( productNumberDefs == null || productNumberDefs.size() == 0)
			return true;

		for (ProductNumberDef def : productNumberDefs) {
			if (def == null) {
				continue;
			}
			
			if (def.isNumberValid(number)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isNumberValid(String number) {
		if (number == null) return false;
		number = number.trim();
		int length = number.length();
		return length == getLength();
	}
	
	public static int getMaxLength(ProductType productType, NumberType numberType) {
		List<ProductNumberDef> list = getProductNumberDef(productType, numberType);
		return getMaxLength(list);
	}
	
	public static int getMaxLength(List<ProductNumberDef> list) {
		int length = 0;
		if (list == null) {
			return length;
		}
		for (ProductNumberDef def : list) {
			if (def == null) {
				continue;
			}
			if (def.getLength() > length) {
				length = def.getLength();
			}
		}
		return length;
	}	

	public boolean isCheckDigit() {
		return checkDigit;
	}
	
	public int getTokenStartIx(TokenType tokenType) {
		return getTokens().get(tokenType.name()).getStartIx();
	}
	
	public int getTokenLength(TokenType tokenType) {
		return getTokens().get(tokenType.name()).getLength();
	}
	
	public static String justifyJapaneseVIN(String productId, boolean isLeftJustified) {
		if (productId == null) return "";
		String trimmedProductId = StringUtils.trim(productId);
		if (ProductNumberDef.VIN_JPN.isNumberValid(trimmedProductId)) { //check the length to determine if Japan VIN
			if (isLeftJustified) { //left justification pad spaces to the right end
				productId = StringUtils.rightPad(trimmedProductId, ProductNumberDef.VIN.getLength());
			} else {
				productId = StringUtils.leftPad(trimmedProductId, ProductNumberDef.VIN.getLength());
			}
		}
		return productId; //if not Japanese VIN, return original productId without trim, including EIN
	}

	public boolean hasToken(TokenType token) {
		
		return tokens.containsKey(token.name());
	}
}

class TokenDef implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private int startIx;
	private int length;

	public TokenDef(ProductNumberDef.TokenType tokenType, int startIx, int length) {
		this.name = tokenType.name();
		this.startIx = startIx;
		this.length = length;
	}

	public String getName() {
		return name;
	}

	public int getStartIx() {
		return startIx;
	}

	public int getLength() {
		return length;
	}
	
}
