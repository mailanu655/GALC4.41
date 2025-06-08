package com.honda.galc.dao.jpa.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ShippingTransactionDao;
import com.honda.galc.entity.product.ShippingTransaction;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;

/**
 * Dao to processing the 50 transaction.
 * Query the vins are ready to send AH and fill the table GAL148TBX
 * 
 * @author vjc80020 - Anuar Vasquez Gomez
 *
 */
public class ShippingTransactionDaoImpl extends BaseDaoImpl<ShippingTransaction, String> implements ShippingTransactionDao{

	//query to get the vins are ready to shipping proccess 50A/B
	public final static String PROCESSING_VIN = new StringBuilder ( )
	.append( "select distinct VARCHAR_FORMAT(CURRENT date, 'yyMMdd') as DATE_STRING,"	)
	.append( "		VARCHAR_FORMAT(CURRENT timestamp, 'HHmiss') as TIME, " 				)
	//.append( "		'M' as sendLocation,	"										)
	//.append( "		'PR' as TRAN_TYPE,		"										)
	.append( "		gal143tbx.PRODUCT_ID as VIN,		"								)
	.append( "		gal144tbx.SALES_MODEL_CODE ,		"								)
	.append( "      gal144tbx.SALES_MODEL_TYPE_CODE ,	"								)
	.append( "		master_spec.SALES_MODEL_OPTION_CODE, "								)              
	.append( "		gal144tbx.SALES_EXT_COLOR_CODE as SALES_MODEL_COLOR_CODE,	"		)
	.append( "		substr(rtrim(gal143tbx.ENGINE_SERIAL_NO),6) as ENGINE_NO,	"		)
	.append( "		LPAD(ltrim(rtrim(gal143tbx.KEY_NO)),7,'0') AS KEY_NO,		"		)
	.append( "		VARCHAR_FORMAT(gal263tbx.ACTUAL_TIMESTAMP,'yyMMdd') as CIC_ISSU_DATA,")
	//.append( "		'50A' as ADC_PROCESS_CODE,	"										)
	.append( "		substr(gal217tbx.LINE_NO,2,1) as PRODUCTION_LINE_NO,	"			)
	.append( "		substr(gal143tbx.PRODUCTION_LOT,11,6) as PRODUCTION_DATE_STRING,"	)
	.append( "		substr(gal143tbx.PRODUCTION_LOT,17,3) as PRODUCTION_SEQUENCE,	"	)
	.append( "		'0'||substr(gal143tbx.PRODUCTION_LOT,20) as PRODUCTION_SUFFIX,	"	)
	.append( "		substr(gal143tbx.KD_LOT_NUMBER,6,1) as KD_LINE_NO,	"				)
	.append( "		substr(gal143tbx.KD_LOT_NUMBER,9,4) as KD_DATE,		"				)
	.append( "		substr(gal143tbx.KD_LOT_NUMBER,14,3) as KD_SEQ_NO,	"				)
	.append( "		substr(gal143tbx.KD_LOT_NUMBER,18) as KD_SUFFIX,	"				)
	.append( "		'' as VEHICLE_UNIT_ID,			"									)
	.append( "      '' as VEHICLE_COMM_UNIT,		"									)
	.append( "		VARCHAR_FORMAT(gal143tbx.ACTUAL_OFF_DATE,'yyMMdd') as AF_OFF_DATE, ")
	.append( "      nvl(cccPart.PART_SERIAL_NUMBER, ' ') as CCC_REG_NBR,	"			)
	//.append( "		' ' as partInstalled,						"						)
	.append( "		master_spec.PLANT_CODE_FRAME,	"									)
	.append( "		master_spec.MODEL_YEAR_CODE,	"									)
	.append( "		master_spec.MODEL_CODE,		"										)
	.append( "		master_spec.MODEL_TYPE_CODE,	"									)
	.append( "		master_spec.MODEL_OPTION_CODE,	"									)
	.append( "		master_spec.EXT_COLOR_CODE,		"									)
	.append( "		master_spec.INT_COLOR_CODE		"									)
	.append( "from	GAL143TBX gal143tbx							"						)
	.append( "		left join (									"						)
	.append( "     			select								"						)
	.append( "              	parts.PRODUCT_ID				"						)
	.append( "              	, parts.PART_SERIAL_NUMBER		"						)
	.append( "              from GAL185TBX parts				"						)
	.append( "              where PART_NAME = ?3				"						)//CCC_PART_NAME
	.append( "              ) cccPart							"						)
	.append( "    on cccPart.PRODUCT_ID = gal143tbx.PRODUCT_ID	"						)
	.append( "    join GAL144TBX gal144tbx on gal144tbx.product_spec_code = gal143tbx.product_spec_code	"	)
	.append( "    join GAL217TBX gal217tbx on gal143tbx.production_lot = gal217tbx.production_lot		"	)
	.append( "    join GAL263TBX gal263tbx on gal143tbx.product_id = gal263tbx.vin						"	)
	.append( "		join gal158tbx master_spec	"										)
	.append( "		on master_spec.PLANT_CODE_FRAME = gal144tbx.PLANT_CODE_GPCS		"	)
	.append( "		and master_spec.MODEL_YEAR_CODE = gal144tbx.MODEL_YEAR_CODE		"	)
	.append( "		and master_spec.MODEL_CODE = gal144tbx.MODEL_CODE				"	)
	.append( "		and master_spec.MODEL_TYPE_CODE = gal144tbx.MODEL_TYPE_CODE		"	)
	.append( "		and master_spec.MODEL_OPTION_CODE = gal144tbx.MODEL_OPTION_CODE	"	)
	.append( "		and master_spec.EXT_COLOR_CODE = gal144tbx.EXT_COLOR_CODE		"	)
	.append( "		and master_spec.INT_COLOR_CODE = gal144tbx.INT_COLOR_CODE		"	)
	.append( "where											"							)
	.append( "    gal263tbx.STATUS                                    = ?2          "   )//--:STATUS
	.append( "    and not exists       (                    "                           )
	.append( "                          SELECT *            "                           )
	.append( "                          FROM GAL148TBX gal148tbx             "   )
	.append( "                          WHERE gal148tbx.sended_Flg = ?1 "               )//:SEND_FLAG
	.append( "                          AND gal148tbx.VIN = gal263tbx.VIN           "   )
	.append( "                          )                                   "           )
	.toString();
	
	
	public final static String PROCESSING_VIN_LIST = new StringBuilder ( )
			.append( "select distinct VARCHAR_FORMAT(CURRENT date, 'yyMMdd') as DATE_STRING,"	)
			.append( "		VARCHAR_FORMAT(CURRENT timestamp, 'HHmiss') as TIME, " 				)
			//.append( "		'M' as sendLocation,	"										)
			//.append( "		'PR' as TRAN_TYPE,		"										)
			.append( "		gal143tbx.PRODUCT_ID as VIN,		"								)
			.append( "		gal144tbx.SALES_MODEL_CODE ,		"								)
			.append( "      gal144tbx.SALES_MODEL_TYPE_CODE ,	"								)
			.append( "		master_spec.SALES_MODEL_OPTION_CODE, "								)              
			.append( "		gal144tbx.SALES_EXT_COLOR_CODE as SALES_MODEL_COLOR_CODE,	"		)
			.append( "		substr(rtrim(gal143tbx.ENGINE_SERIAL_NO),6) as ENGINE_NO,	"		)
			.append( "		LPAD(ltrim(rtrim(gal143tbx.KEY_NO)),7,'0') AS KEY_NO,		"		)
			.append( "		VARCHAR_FORMAT(gal263tbx.ACTUAL_TIMESTAMP,'yyMMdd') as CIC_ISSU_DATA,")
			//.append( "		'50A' as ADC_PROCESS_CODE,	"										)
			.append( "		substr(gal217tbx.LINE_NO,2,1) as PRODUCTION_LINE_NO,	"			)
			.append( "		substr(gal143tbx.PRODUCTION_LOT,11,6) as PRODUCTION_DATE_STRING,"	)
			.append( "		substr(gal143tbx.PRODUCTION_LOT,17,3) as PRODUCTION_SEQUENCE,	"	)
			.append( "		'0'||substr(gal143tbx.PRODUCTION_LOT,20) as PRODUCTION_SUFFIX,	"	)
			.append( "		substr(gal143tbx.KD_LOT_NUMBER,6,1) as KD_LINE_NO,	"				)
			.append( "		substr(gal143tbx.KD_LOT_NUMBER,9,4) as KD_DATE,		"				)
			.append( "		substr(gal143tbx.KD_LOT_NUMBER,14,3) as KD_SEQ_NO,	"				)
			.append( "		substr(gal143tbx.KD_LOT_NUMBER,18) as KD_SUFFIX,	"				)
			.append( "		'' as VEHICLE_UNIT_ID,			"									)
			.append( "      '' as VEHICLE_COMM_UNIT,		"									)
			.append( "		VARCHAR_FORMAT(gal143tbx.ACTUAL_OFF_DATE,'yyMMdd') as AF_OFF_DATE, ")
			.append( "      nvl(cccPart.PART_SERIAL_NUMBER, ' ') as CCC_REG_NBR,	"			)
			//.append( "		' ' as partInstalled,						"						)
			.append( "		master_spec.PLANT_CODE_FRAME,	"									)
			.append( "		master_spec.MODEL_YEAR_CODE,	"									)
			.append( "		master_spec.MODEL_CODE,		"										)
			.append( "		master_spec.MODEL_TYPE_CODE,	"									)
			.append( "		master_spec.MODEL_OPTION_CODE,	"									)
			.append( "		master_spec.EXT_COLOR_CODE,		"									)
			.append( "		master_spec.INT_COLOR_CODE		"									)
			.append( "from	GAL143TBX gal143tbx							"						)
			.append( "		left join (									"						)
			.append( "     			select								"						)
			.append( "              	parts.PRODUCT_ID				"						)
			.append( "              	, parts.PART_SERIAL_NUMBER		"						)
			.append( "              from GAL185TBX parts				"						)
			.append( "              where PART_NAME IN (@commonNameList@)				"						)//CCC_PART_NAME
			.append( "              ) cccPart							"						)
			.append( "    on cccPart.PRODUCT_ID = gal143tbx.PRODUCT_ID	"						)
			.append( "    join GAL144TBX gal144tbx on gal144tbx.product_spec_code = gal143tbx.product_spec_code	"	)
			.append( "    join GAL217TBX gal217tbx on gal143tbx.production_lot = gal217tbx.production_lot		"	)
			.append( "    join GAL263TBX gal263tbx on gal143tbx.product_id = gal263tbx.vin						"	)
			.append( "		join gal158tbx master_spec	"										)
			.append( "		on master_spec.PLANT_CODE_FRAME = gal144tbx.PLANT_CODE_GPCS		"	)
			.append( "		and master_spec.MODEL_YEAR_CODE = gal144tbx.MODEL_YEAR_CODE		"	)
			.append( "		and master_spec.MODEL_CODE = gal144tbx.MODEL_CODE				"	)
			.append( "		and master_spec.MODEL_TYPE_CODE = gal144tbx.MODEL_TYPE_CODE		"	)
			.append( "		and master_spec.MODEL_OPTION_CODE = gal144tbx.MODEL_OPTION_CODE	"	)
			.append( "		and master_spec.EXT_COLOR_CODE = gal144tbx.EXT_COLOR_CODE		"	)
			.append( "		and master_spec.INT_COLOR_CODE = gal144tbx.INT_COLOR_CODE		"	)
			.append( "where											"							)
			.append( "    gal263tbx.STATUS                                    = ?2          "   )//--:STATUS
			.append( "    and not exists       (                    "                           )
			.append( "                          SELECT *            "                           )
			.append( "                          FROM GAL148TBX gal148tbx             "   )
			.append( "                          WHERE gal148tbx.sended_Flg = ?1 "               )//:SEND_FLAG
			.append( "                          AND gal148tbx.VIN = gal263tbx.VIN           "   )
			.append( "                          )                                   "           )
			.toString();

	public final static String PRICE_VIN = new StringBuilder()
	.append( "SELECT EFFECTIVE_DATE, SUBSTR(PRICE,6,9) AS PRICE FROM GAL268TBX WHERE "	)
	.append( "PLANT_CODE_FRAME = ?1 "													)
	.append( "AND MODEL_YEAR_CODE = ?2 "												)
	.append( "AND MODEL_CODE = ?3 "														)
	.append( "AND MODEL_TYPE_CODE = ?4 "												)
	.append( "AND MODEL_OPTION_CODE = ?5 "												)
	.append( "AND EXT_COLOR_CODE = ?6 "													)
	.append( "AND INT_COLOR_CODE = ?7 "													)
	.append( "AND EFFECTIVE_DATE <= ?8 "												)
	.append( "ORDER BY EFFECTIVE_DATE DESC FETCH FIRST 1 ROWS ONLY"						)
	.toString();

	private final String FIND_ALL_NOT_CONFIRMED = "SELECT T.* FROM GALADM.GAL263TBX S INNER JOIN GALADM.GAL148TBX T ON S.VIN=T.VIN WHERE S.STATUS IN (0,1) AND TIMESTAMPDIFF(2,CHAR(CURRENT TIMESTAMP - MAX(S.CREATE_TIMESTAMP,S.UPDATE_TIMESTAMP))) > ?1 AND T.PRINTED_FLAG='N' WITH UR for read only";
	
	/**
	 * Method to get all the vins with VQ Ship confirm and are ready to ship to YMS
	 */
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.NOT_SUPPORTED)
	public List<ShippingTransaction> get50ATransactionVin(	final Integer status
			, final Integer effectiveDate
			, final Character sendFlag
			, final String cccPartName
			) {
		Parameters parameters = Parameters.with( "1", sendFlag );
		parameters.put( "2", status			);
		parameters.put( "3", cccPartName	);

		List<ShippingTransaction> vins = new ArrayList<ShippingTransaction>();
		List<Object[]> results = executeNative(parameters, PROCESSING_VIN);
		if (results == null) return vins;
		for (Object[] data : results) {
			ShippingTransaction shippingTransaction = new ShippingTransaction();
			shippingTransaction.setDateString((String) data[0]);
			shippingTransaction.setTime((String) data[1]);
			shippingTransaction.setVin((String) data[2]);
			shippingTransaction.setSalesModelCode((String) data[3]);
			shippingTransaction.setSalesModelTypeCode((String) data[4]);
			shippingTransaction.setSalesModelOptionCode((String) data[5]);
			shippingTransaction.setSalesModelColorCode((String) data[6]);
			shippingTransaction.setEngineNumber((String) data[7]);
			shippingTransaction.setKeyNumber((String) data[8]);
			shippingTransaction.setCicIssuData((String) data[9]);
			shippingTransaction.setLineNumber((String) data[10]);
			shippingTransaction.setProductionDate((String) data[11]);
			shippingTransaction.setProductionSequenceNumber((String) data[12]);
			shippingTransaction.setProductionSuffix((String) data[13]);
			shippingTransaction.setKdLotLineNumber((String) data[14]);
			shippingTransaction.setKdLotDate((String) data[15]);
			shippingTransaction.setKdLotSequenceNumber((String) data[16]);
			shippingTransaction.setKdLotSuffix((String) data[17]);
			shippingTransaction.setVechicleUnitId(data[18] != null ? (String) data[18] : "");
			shippingTransaction.setVechicleCommonUnitId(data[19] != null ? (String) data[19] : "");
			shippingTransaction.setAfOffDate((String) data[20]);
			shippingTransaction.setCccRegNbr((String) data[21]);
			Object[] price = get50APriceVin((String) data[22],
					(String) data[23],
					(String) data[24],
					(String) data[25],
					(String) data[26],
					(String) data[27],
					(String) data[28],
					effectiveDate
					);
			shippingTransaction.setPriceString(price != null ? (String) price[1] : "");
			vins.add(shippingTransaction);
		}
		return vins;
	}
	
	
	/**
	 * Method to get all the vins with VQ Ship confirm and are ready to ship to YMS
	 */
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.NOT_SUPPORTED)
	public List<ShippingTransaction> get50ATransactionVin(	final Integer status
			, final Integer effectiveDate
			, final Character sendFlag
			, final List<String> cccPartNames
			) {
		Parameters parameters = Parameters.with( "1", sendFlag );
		parameters.put( "2", status			);
		
		List<ShippingTransaction> vins = new ArrayList<ShippingTransaction>();
		String finalQuery = PROCESSING_VIN_LIST.replaceAll( "@commonNameList@", StringUtil.toSqlInString(cccPartNames));
		List<Object[]> results = executeNative(parameters, finalQuery);
		if (results == null) return vins;
		for (Object[] data : results) {
			ShippingTransaction shippingTransaction = new ShippingTransaction();
			shippingTransaction.setDateString((String) data[0]);
			shippingTransaction.setTime((String) data[1]);
			shippingTransaction.setVin((String) data[2]);
			shippingTransaction.setSalesModelCode((String) data[3]);
			shippingTransaction.setSalesModelTypeCode((String) data[4]);
			shippingTransaction.setSalesModelOptionCode((String) data[5]);
			shippingTransaction.setSalesModelColorCode((String) data[6]);
			shippingTransaction.setEngineNumber((String) data[7]);
			shippingTransaction.setKeyNumber((String) data[8]);
			shippingTransaction.setCicIssuData((String) data[9]);
			shippingTransaction.setLineNumber((String) data[10]);
			shippingTransaction.setProductionDate((String) data[11]);
			shippingTransaction.setProductionSequenceNumber((String) data[12]);
			shippingTransaction.setProductionSuffix((String) data[13]);
			shippingTransaction.setKdLotLineNumber((String) data[14]);
			shippingTransaction.setKdLotDate((String) data[15]);
			shippingTransaction.setKdLotSequenceNumber((String) data[16]);
			shippingTransaction.setKdLotSuffix((String) data[17]);
			shippingTransaction.setVechicleUnitId(data[18] != null ? (String) data[18] : "");
			shippingTransaction.setVechicleCommonUnitId(data[19] != null ? (String) data[19] : "");
			shippingTransaction.setAfOffDate((String) data[20]);
			shippingTransaction.setCccRegNbr((String) data[21]);
			Object[] price = get50APriceVin((String) data[22],
					(String) data[23],
					(String) data[24],
					(String) data[25],
					(String) data[26],
					(String) data[27],
					(String) data[28],
					effectiveDate
					);
			shippingTransaction.setPriceString(price != null ? (String) price[1] : "");
			vins.add(shippingTransaction);
		}
		return vins;
	}

	private Object[] get50APriceVin(final String plantCodeFrame,
			final String modelYearCode,
			final String modelCode,
			final String modelTypeCode,
			final String modelOptionCode,
			final String extColorCode,
			final String intColorCode,
			final Integer effectiveDate) {
		Parameters parameters = Parameters.with("1", plantCodeFrame);
		parameters.put("2", modelYearCode);
		parameters.put("3", modelCode);
		parameters.put("4", modelTypeCode);
		parameters.put("5", modelOptionCode);
		parameters.put("6", extColorCode);
		parameters.put("7", intColorCode);
		parameters.put("8", effectiveDate);

		List<Object[]> prices = executeNative(parameters, PRICE_VIN);
		if (prices == null || prices.size() != 1) return null;
		return prices.get(0);
	}
	

	public List<ShippingTransaction> findAllNotConfirmedByCreateTimePassed(Integer timeFrame60A) {
		return findAllByNativeQuery(FIND_ALL_NOT_CONFIRMED,Parameters.with("1", timeFrame60A),ShippingTransaction.class);
	}


}
