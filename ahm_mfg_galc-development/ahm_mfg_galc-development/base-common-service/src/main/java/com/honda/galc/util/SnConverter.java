package com.honda.galc.util;

import static com.honda.galc.data.ProductNumberDef.DCB;
import static com.honda.galc.data.ProductNumberDef.DCB_CSS;
import static com.honda.galc.data.ProductNumberDef.DCB_HUM;
import static com.honda.galc.data.ProductNumberDef.DCH;
import static com.honda.galc.data.ProductNumberDef.DCH_CSS;
import static com.honda.galc.data.ProductNumberDef.DCH_HUM;
import static com.honda.galc.data.ProductNumberDef.TokenType.MODEL;
import static com.honda.galc.data.ProductNumberDef.TokenType.PART_LEVEL;
import static com.honda.galc.data.ProductNumberDef.TokenType.PLANT;
import static com.honda.galc.data.ProductNumberDef.TokenType.SEQUENCE;
import static com.honda.galc.util.SnConverter.ConversionType.MODEL_ATTR;
import static com.honda.galc.util.SnConverter.ConversionType.SUB_STR;
import static com.honda.galc.util.SnConverter.ConversionType.TO_HEX;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.honda.galc.common.exception.DataConversionException;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.data.ProductDigitCheckUtil;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductNumberDef.TokenType;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.property.SnConverterPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>SnConverter</code> is  used to convert serial numbers between two different formats. <br />
 * It has 4 predefined conversions :
 * <table>
 * <tr>
 * <th>Product Type</th><th>Source</th><th>Conversion ConfigId</th><th>Target Number</th><th>Description</th>
 * </tr>
 * <tr align='center'><td>Head</td><td>CSS</td><td>DCH_CSS.DCH</td><td>DCH</td><td></td></tr>
 * <tr align='center'><td>Block</td><td>CSS</td><td>DCB_CSS.DCB</td><td>DCB</td><td></td></tr>
 * <tr align='center'><td>Head</td><td>HUM</td><td>DCH_HUM.DCH</td><td>DCH</td><td></td></tr>
 * <tr align='center'><td>Block</td><td>HUM</td><td>DCB_HUM.DCB</td><td>DCB</td><td></td></tr>
 * </table>
 * <pre>
 * User may supply new conversion configurations as mapped json formatted properties. 
 * For example :
 * CONVERSION_CONFIG{DCH_CSS.DCH} : [{tokenName:MODEL,conversionType:MODEL_ATTR,startIx:0,length:3,value:MODEL_CSS},{tokenName:PART .....]}
 * CONVERSION_CONFIG{DCH_HUM.DCH} : [{tokenName:MODEL,conversionType:MODEL_ATTR,startIx:0,length:1,value:MODEL_HUM},{tokenName:PART .....]}
 * </p>
 * <h4>Usage and Example</h4> 
 * <code>
 * String inputSn = "K1409290139";
 * String conversionId = "DCH_HUM.DCH";
 * String convertedSn = SnConverterUtility.convert(inputSn, conversionId);  // when loaded from properties, default propertyComponentId will be used : 'SN_CONVERTER'
 * or  
 * String convertedSn = SnConverterUtility.convert(inputSn, conversionId, propertyComponentId); 
 * </code>
 * </pre>
 * 
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Apr 8, 2014
 */
public class SnConverter {

	public enum ConversionType {
		SUB_STR, TO_HEX, FIXED_VALUE, MAPPED_VALUE, CHECK_DIGIT, YEAR, MONTH, DAY, MODEL_ATTR
	};

	private static Map<String, SnConverterConfig[]> configs = new LinkedHashMap<String, SnConverterConfig[]>();
	private static Map<String, SnConverterConfig[]> predefinedConfigs = new LinkedHashMap<String, SnConverterConfig[]>();

	static {
		createPredifinedCofigs();
	}

	// === public static conversion api === //
	public static String convert(String inputString, String configId) {
		SnConverterConfig[] config = getConfig(configId, null);
		return convert(inputString, config);
	}

	public static String convert(String inputString, String configId, String propertyComponentId) {
		SnConverterConfig[] config = getConfig(configId, propertyComponentId);
		return convert(inputString, config);
	}

	// === protected conversion api === //
	protected static String convert(String inputString, SnConverterConfig[] config) {
		StringBuilder output = new StringBuilder();
		for (SnConverterConfig tokenConfig : config) {
			String token = convert(inputString, tokenConfig, output.toString());
			output.append(token);
		}
		return output.toString();
	}

	protected static String convert(String inputString, SnConverterConfig tokenConfig, final String output) {
		String token = null;
		try {
			switch (tokenConfig.getConversionType()) {
			case TO_HEX:
				token = toHex(inputString, tokenConfig);
				break;
			case YEAR:
				token = year(inputString, tokenConfig);
				break;
			case MONTH:
				token = month(inputString, tokenConfig);
				break;
			case DAY:
				token = day(inputString, tokenConfig);
				break;
			case FIXED_VALUE:
				token = tokenConfig.getValue();
				break;
			case MAPPED_VALUE:
				token = mappedValue(inputString, tokenConfig);
				break;
			case CHECK_DIGIT:
				token = String.valueOf(ProductDigitCheckUtil.calculateCheckDigit(output.toString() + "_"));
				break;
			case MODEL_ATTR:
				token = modelAttr(inputString, tokenConfig);
				break;
			default:
				token = inputString.substring(tokenConfig.getStartIx(), tokenConfig.getStartIx() + tokenConfig.getLength());
			}
		} catch (Exception e) {
			throw new DataConversionException(String.format("Failed to convert token:%s for input:%s.", tokenConfig, inputString), e);
		}
		if (token == null) {
			throw new DataConversionException(String.format("Could not convert token:%s for input:%s.", tokenConfig, inputString));
		}
		return token;
	}

	// === conversions === //
	protected static String modelAttr(String inputString, SnConverterConfig tokenConfig) {
		String value = inputString.substring(tokenConfig.getStartIx(), tokenConfig.getStartIx() + tokenConfig.getLength());
		String model = null;
		List<BuildAttribute> list = ServiceFactory.getDao(BuildAttributeDao.class).findAllByAttribute(tokenConfig.getValue());
		for (BuildAttribute ba : list) {
			if (ba.getAttributeValue().equals(value)) {
				model = ba.getModelCode();
				break;
			}
		}
		if (model == null) {
			throw new DataConversionException(String.format("BuildAttribute:%s not defined for value:%s.", tokenConfig.getValue(), value));
		}
		return model;
	}

	protected static String toHex(String inputString, SnConverterConfig tokenConfig) {
		String token = inputString.substring(tokenConfig.getStartIx(), tokenConfig.getStartIx() + tokenConfig.getLength());
		token = Integer.toHexString(Integer.valueOf(token)).toUpperCase();
		return token;
	}

	protected static String year(String inputString, SnConverterConfig tokenConfig) {
		String token = String.valueOf(GregorianCalendar.getInstance().get(Calendar.YEAR));
		if (tokenConfig.getLength() != null) {
			token = new StringBuilder(token).reverse().toString();
			token = token.substring(0, tokenConfig.getLength());
			token = new StringBuilder(token).reverse().toString();
		}
		return token;
	}

	protected static String month(String inputString, SnConverterConfig tokenConfig) {
		String token = String.valueOf(GregorianCalendar.getInstance().get(Calendar.MONTH) + 1);
		if (tokenConfig.getLength() != null && tokenConfig.getLength() == 1) {
			token = Integer.toHexString(Integer.valueOf(token)).toUpperCase();
		} else {
			token = StringUtil.padLeft(token, 2, '0');
		}
		return token;
	}

	protected static String day(String inputString, SnConverterConfig tokenConfig) {
		String token = String.valueOf(GregorianCalendar.getInstance().get(Calendar.DAY_OF_MONTH));
		if (token != null) {
			token = StringUtil.padLeft(token, 2, '0');
		}
		return token;
	}

	protected static String mappedValue(String inputString, SnConverterConfig tokenConfig) {
		String key = inputString.substring(tokenConfig.getStartIx(), tokenConfig.getStartIx() + tokenConfig.getLength());
		String token = tokenConfig.getMappedValue(key);
		return token;
	}

	// === get/set === //
	protected static SnConverterConfig[] getConfig(String configId, String propertyComponentId) {
		SnConverterConfig[] config = getConfigs().get(configId);
		if (config != null) {
			return config;
		}

		SnConverterPropertyBean properties = null;
		if (StringUtils.isNotBlank(propertyComponentId)) {
			properties = PropertyService.getPropertyBean(SnConverterPropertyBean.class, propertyComponentId);
		} else {
			properties = PropertyService.getPropertyBean(SnConverterPropertyBean.class);
		}

		String configStr = null;
		if (properties.getConversionConfig() != null) {
			configStr = properties.getConversionConfig().get(configId);
		}

		if (StringUtils.isNotBlank(configStr)) {
			try {
				config = new Gson().fromJson(configStr, SnConverterConfig[].class);
			} catch (Exception e) {
				throw new DataConversionException(String.format("Failed to convert from Json to Config - configId:%s, json : %s.", configId, configStr));
			}
		} else {
			config = getPredefinedConfigs().remove(configId);
		}

		if (config == null) {
			throw new DataConversionException(String.format("There is no config defined for: %s.", configId));
		}

		getConfigs().put(configId, config);
		return config;
	}

	protected static Map<String, SnConverterConfig[]> getConfigs() {
		return configs;
	}

	protected static Map<String, SnConverterConfig[]> getPredefinedConfigs() {
		return predefinedConfigs;
	}

	// === predefined conversions === //
	protected static void createPredifinedCofigs() {

		// === DC CSS to DC numbers === //
		getPredefinedConfigs().put(DCH_CSS.getName() + "." + DCH.getName(), createDcConversionConfig(DCH_CSS, "MODEL_CSS", "1", null, null));
		getPredefinedConfigs().put(DCB_CSS.getName() + "." + DCB.getName(), createDcConversionConfig(DCB_CSS, "MODEL_CSS", "1", null, null));

		// === DC HUM to DC numbers === //
		getPredefinedConfigs().put(DCH_HUM.getName() + "." + DCH.getName(), createDcConversionConfig(DCH_HUM, "MODEL_HUM", "1", "1", "1"));
		getPredefinedConfigs().put(DCB_HUM.getName() + "." + DCB.getName(), createDcConversionConfig(DCB_HUM, "MODEL_HUM", "1", "1", "1"));
	}

	// === utility methods === //
	protected static SnConverterConfig createConfig(String tokenName, ConversionType conversionType, Integer startIx, Integer length, String value) {
		SnConverterConfig config = new SnConverterConfig();
		config.setTokenName(tokenName);
		config.setConversionType(conversionType);
		config.setStartIx(startIx);
		config.setLength(length);
		config.setValue(value);
		return config;
	}

	protected static SnConverterConfig createConfig(ProductNumberDef numDef, TokenType tokenType, ConversionType conversionType, String value) {
		return createConfig(tokenType.name(), conversionType, numDef.getTokenStartIx(tokenType), numDef.getTokenLength(tokenType), value);
	}

	// === DC CSS/HUM to DC numbers conversion === //
	protected static SnConverterConfig[] createDcConversionConfig(ProductNumberDef sourceNumDef, String modelAttrName, String partLevel, String line, String die) {
		List<SnConverterConfig> configs = new ArrayList<SnConverterConfig>();
		configs.add(createConfig(sourceNumDef, MODEL, MODEL_ATTR, modelAttrName));
		configs.add(createConfig(PART_LEVEL.name(), ConversionType.FIXED_VALUE, null, null, partLevel));
		configs.add(createConfig(PLANT.name(), ConversionType.FIXED_VALUE, null, null, "FS"));

		SnConverterConfig yearConfig = createConfig(sourceNumDef, TokenType.YEAR, SUB_STR, null);
		if (2 == yearConfig.getLength()) {
			yearConfig.setStartIx(yearConfig.getStartIx() + 1);
			yearConfig.setLength(1);
		}
		configs.add(yearConfig);
		SnConverterConfig monthConfig = createConfig(sourceNumDef, TokenType.MONTH, SUB_STR, null);
		if (2 == monthConfig.getLength()) {
			monthConfig.setConversionType(TO_HEX);
		}
		configs.add(monthConfig);
		configs.add(createConfig(sourceNumDef, TokenType.DAY, SUB_STR, null));
		if (line != null) {
			configs.add(createConfig(TokenType.LINE.name(), ConversionType.FIXED_VALUE, null, null, line));
		} else {
			configs.add(createConfig(sourceNumDef, TokenType.LINE, SUB_STR, null));
		}
		if (die != null) {
			configs.add(createConfig(TokenType.DIE.name(), ConversionType.FIXED_VALUE, null, null, die));
		} else {
			SnConverterConfig dieConfig = createConfig(sourceNumDef, TokenType.DIE, SUB_STR, null);
			if (2 == dieConfig.getLength()) {
				dieConfig.setStartIx(dieConfig.getStartIx() + 1);
				dieConfig.setLength(1);
			}
			configs.add(dieConfig);
		}
		if (sourceNumDef.getTokenLength(SEQUENCE) == 3) {
			configs.add(createConfig("LEFT_PAD", ConversionType.FIXED_VALUE, null, null, "0"));
		}
		configs.add(createConfig(sourceNumDef, TokenType.SEQUENCE, SUB_STR, null));
		configs.add(createConfig(TokenType.CHECK_DIGIT.name(), ConversionType.CHECK_DIGIT, null, null, null));
		return configs.toArray(new SnConverterConfig[] {});
	}
}
