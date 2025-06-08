package com.honda.galc.dao.jpa.product;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.client.enumtype.LetTotalStatus;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LetMessageDao;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.LetMessage;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Jan 21, 2016
 */
public class LetMessageDaoImpl extends BaseDaoImpl<LetMessage, Long> implements LetMessageDao {

	private static final String GET_NEAREST_MESSAGE = "SELECT MESSAGE_ID, MIN(ABS(let.ACTUAL_TIMESTAMP - CAST(?1 AS TIMESTAMP))) AS MINVAL FROM GALADM.LET_MESSAGE_TBX let  GROUP by let.MESSAGE_ID order by MINVAL fetch first 1 row only";

	private static final String GET_IDS_BY_STATUS_AND_TS = "select e.messageId from LetMessage e where e.actualTimestamp >= :actualTimestamp  and e.totalStatus in (:totalStatus)";

	private static final String GET_ALL_MSG = "select msg.MESSAGE_ID from GALADM.LET_MESSAGE_TBX msg,GALADM.LET_SPOOL_TBX spool where spool.SPOOL_ID = msg.SPOOL_ID " +
			"and msg.TOTAL_STATUS LIKE ?1 and spool.SPOOL_ID = ?2 and msg.PRODUCT_ID  like ?3 ORDER BY msg.ACTUAL_TIMESTAMP desc fetch first %d row only";

	private static final String UPDATE_MESSAGE_STATUS = "UPDATE GALADM.LET_MESSAGE_TBX MSG SET MSG.TOTAL_STATUS =?1, MSG.DURATION =?2 WHERE MSG.MESSAGE_ID =?3";

	private final String GET_ALL_LET_MESSAGES = "SELECT * FROM ("
			+ "select  MSG.MESSAGE_ID,MSG.TOTAL_STATUS,MSG.TERMINAL_ID,MSG.IP_ADDRESS,MSG.PRODUCT_ID,MSG.ACTUAL_TIMESTAMP,"
			+ "MSG.DURATION,MSG.BUILD_CODE,MSG.MAC_ADDRESS,MSG.MESSAGE_HEADER,MSG.MESSAGE_TYPE,MSG.MESSAGE_REPLY, "
			+ "ROW_NUMBER() OVER (ORDER BY ACTUAL_TIMESTAMP desc) AS ROWNUM "
			+ "from GALADM.LET_MESSAGE_TBX MSG ,GALADM.LET_SPOOL_TBX SPOOL where MSG.SPOOL_ID = SPOOL.SPOOL_ID and SPOOL.SPOOL_ID = ?1  "
			+ " AND MSG.TOTAL_STATUS like ?2 AND MSG.PRODUCT_ID like ?3 ORDER BY ACTUAL_TIMESTAMP desc "
			+ ") AS A WHERE A.rownum BETWEEN  ?4 AND  ?5";
	
    public static final String SELECT_IDS_BY_TEST_XML = "with let_tbl as ( \n" + 
            "select message_id, product_id \n" + 
            "from let_message_tbx \n" + 
            ", xmltable ('$d/UNIT_IN_TEST/PROCESS/TESTS/TEST' PASSING XML_MESSAGE_BODY AS \"d\" \n" + 
            "    columns \n" + 
            "--test  \n" + 
            "   test varchar(255) path '@Test' \n" + 
            "   ,test_end_time timestamp path '@TestEndTime' \n" + 
            "   ,mfg_id varchar(255) path '../../../@MfgID' \n" +
            "   ,production varchar(255) path '../../../@Production' \n" +				
            ") as xml_tbl \n" + 
            "where test = ?1 \n" +
            "and total_status = '" + LetTotalStatus.OK.name() + "' \n";

    public static final String SELECT_RESULTS_DATA_BY_TEST_XML = "select message_id , product_id \n" + 
            "-- unit \n" +
            ",xml_tbl.vin, xml_tbl.seq_step_file, xml_tbl.production  \n" +
            "-- process \n" +
            ",xml_tbl.process, xml_tbl.cal, xml_tbl.cell  \n" +
            "-- test \n" +
            ",xml_tbl.test, xml_tbl.test_status, xml_tbl.test_end_time  \n" +
            ", varchar(xml2clob(xml_tbl.attrs)) as attrs  \n" +
            ", CLOB(xml2clob(xml_tbl.params)) as params  \n" +
            ", varchar(xml2clob(xml_tbl.fault_codes)) as fault_codes \n" +
            "from let_message_tbx \n" + 
            ", xmltable ('$d/UNIT_IN_TEST/PROCESS/TESTS/TEST' PASSING XML_MESSAGE_BODY AS \"d\" \n" + 
            "    columns \n" + 
            "--unit_in_test    \n" + 
            "    vin varchar(255) path '../../../@VIN' \n" + 
            "    ,seq_step_file varchar(255) path '../../../@SeqStepFile' \n" + 
            "    ,production  varchar(255) path '../../../@Production' \n" + 
            "--process    \n" + 
            "    ,process varchar(255) path '../../@Process' \n" + 
            "    ,cal varchar(255) path '../../@Cal' \n" +				
            "    ,cell varchar(255) path '../../@Cell' \n" + 
            "--test     \n" + 
            "   ,test varchar(255) path '@Test' \n" + 
            "   ,test_status varchar(255) path '@Status' \n" + 
            "   ,test_end_time timestamp path '@TestEndTime' \n" +
            "   ,attrs xml path 'TEST_ATTRIBUTES/TEST_ATTRIBUTE' \n" + 				
            "   ,params xml path 'TEST_PARAMS/TEST_PARAM'  \n" +
            "   ,fault_codes xml path 'FAULT_CODES/FAULT_CODE'  \n" +
            ") as xml_tbl \n" + 
            "where test = ?1 \n";	
	
	private static final String FIND_BY_MESSAGE_ID = "SELECT MESSAGE_ID, TERMINAL_ID, MESSAGE_TYPE, SPOOL_ID, IP_ADDRESS, PRODUCT_ID, BUILD_CODE, MAC_ADDRESS, "
			+ "TOTAL_STATUS, MESSAGE_HEADER, MESSAGE_REPLY, DURATION, xmlserialize(xml_message_body as clob) as XML_MESSAGE_BODY, "
			+ "EXCEPTION_MESSAGE_BODY, ACTUAL_TIMESTAMP, CREATE_TIMESTAMP, UPDATE_TIMESTAMP " 
			+ "FROM LET_MESSAGE_TBX WHERE MESSAGE_ID =?1";
	
	private static final String FIND_ALL_BY_PRODUCT_ID = "SELECT MESSAGE_ID, TERMINAL_ID, MESSAGE_TYPE, SPOOL_ID, IP_ADDRESS, PRODUCT_ID, BUILD_CODE, MAC_ADDRESS, "
			+ "TOTAL_STATUS, MESSAGE_HEADER, MESSAGE_REPLY, DURATION, xmlserialize(xml_message_body as clob) as XML_MESSAGE_BODY, "
			+ "EXCEPTION_MESSAGE_BODY, ACTUAL_TIMESTAMP, CREATE_TIMESTAMP, UPDATE_TIMESTAMP " 
			+ "FROM LET_MESSAGE_TBX WHERE PRODUCT_ID =?1";

	private static final String FIND_ALL_BY_PRODUCT_ID_AND_TOTAL_STATUS = "SELECT MESSAGE_ID, TERMINAL_ID, MESSAGE_TYPE, SPOOL_ID, IP_ADDRESS, PRODUCT_ID, BUILD_CODE, MAC_ADDRESS, "
			+ "TOTAL_STATUS, MESSAGE_HEADER, MESSAGE_REPLY, DURATION, xmlserialize(xml_message_body as clob) as XML_MESSAGE_BODY, "
			+ "EXCEPTION_MESSAGE_BODY, ACTUAL_TIMESTAMP, CREATE_TIMESTAMP, UPDATE_TIMESTAMP " 
			+ "FROM LET_MESSAGE_TBX WHERE PRODUCT_ID = ?1 AND TOTAL_STATUS = ?2";

	public LetMessage findDataByTimeStamp(Timestamp selectedActualTime) {
		Parameters params = Parameters.with("1", selectedActualTime);
		return findFirstByNativeQuery(GET_NEAREST_MESSAGE, params);
	}

	@Override
	public LetMessage findByKey(Long id) { 
		return findByMessageId(id); 
	}

	public LetMessage findByMessageId(long id) {
		Parameters params = Parameters.with("1", id);
		return findFirstByNativeQuery(FIND_BY_MESSAGE_ID, params);
	}

	@SuppressWarnings("unchecked")
	public List<Long> findMsgIdsByStatusAndTs(Timestamp actualTimestamp, List<String> status) {
		Parameters params = Parameters.with("actualTimestamp", actualTimestamp);
		params.put("totalStatus", status);
		List<Long> result = findResultListByQuery(GET_IDS_BY_STATUS_AND_TS, params);
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findAllMessages(Integer spoolId,String status, String productTxt, long startIndex, long endIndex) {
		Parameters params = Parameters.with("1", spoolId).put("2", status).put("3", productTxt).put("4", startIndex).put("5", endIndex);
		return findResultListByNativeQuery(GET_ALL_LET_MESSAGES, params);
	}
	
	public long getTotalLetMsgCount(Integer spoolId, String status, String productTxt) {
		Parameters params = Parameters.with("spoolId", spoolId);
		if (!status.equalsIgnoreCase("ALL")) {
			params.put("totalStatus", status);
		}
		if (StringUtils.isNotBlank(productTxt)) {
			params.put("productId", productTxt.trim());
		}
		return count(params);
	}

	@Transactional
	public int updateStatusAndDurationByMessageId(long messageId, String status, double duration) {
		Parameters params = Parameters.with("1", status).put("2", duration).put("3", messageId);
		return executeNativeUpdate(UPDATE_MESSAGE_STATUS, params);
	}

	@SuppressWarnings("unchecked")
	public List<Long> findAllMsg(Integer spoolId, String status,String productTxt,int maxRecords) {
		Parameters params = Parameters.with("1", status).put("2", spoolId).put("3", productTxt);
		return findResultListByNativeQuery(String.format(GET_ALL_MSG, maxRecords), params);
	}

    @SuppressWarnings("unchecked")
    public List<Long> findAllMessageIds(String programName, String productId, FrameSpec spec, Timestamp startDate, Timestamp endDate, String mfgId, String production) {
        String sql = SELECT_IDS_BY_TEST_XML;
        Parameters params = new Parameters();
        params.put("1", programName);

        int paramIx = 2;
        if (StringUtils.isNotBlank(productId)) {
            sql = sql + "and product_id = ?" + paramIx + " \n";
            params.put(String.valueOf(paramIx++), productId);
        }
        if (startDate != null) {
            sql = sql + "and test_end_time >= ?" + paramIx + " \n";
            params.put(String.valueOf(paramIx++), startDate);
        }
        if (endDate != null) {
            sql = sql + "and test_end_time < ?" + paramIx + " \n";
            params.put(String.valueOf(paramIx++), endDate);
        }
        mfgId = StringUtils.trimToEmpty(mfgId);
        if (StringUtils.isNotBlank(mfgId)) {
            if (mfgId.length() < 3) {
                sql = sql + "and mfg_id like ?" + paramIx + " \n";
                mfgId = '%' + mfgId;
            } else {
                sql = sql + "and mfg_id = ?" + paramIx + " \n";
            }
            params.put(String.valueOf(paramIx++), mfgId);
        }
        production = StringUtils.trimToEmpty(production);
        if (StringUtils.isNotBlank(production)) {
            sql = sql + "and production = ?" + paramIx + " \n";
            params.put(String.valueOf(paramIx++), production);
        }

        sql = sql + ") \n";
        sql = sql + "select message_id \n" + "from let_tbl lt \n";

        String specFilter = "";
        if (spec != null) {
            String operator = "where";
            if (StringUtils.isNotBlank(spec.getModelYearCode()) && !"*".equals(spec.getModelYearCode())) {
                specFilter = specFilter + operator + " ps.model_year_code = ?" + paramIx + " \n";
                params.put(String.valueOf(paramIx++), spec.getModelYearCode());
                operator = "and";
            }
            if (StringUtils.isNotBlank(spec.getModelCode()) && !"*".equals(spec.getModelCode())) {
                specFilter = specFilter + operator + " ps.model_code = ?" + paramIx + " \n";
                params.put(String.valueOf(paramIx++), spec.getModelCode());
                operator = "and";
            }
            if (StringUtils.isNotBlank(spec.getModelTypeCode()) && !"*".equals(spec.getModelTypeCode())) {
                specFilter = specFilter + operator + " ps.model_type_code = ?" + paramIx + " \n";
                params.put(String.valueOf(paramIx++), spec.getModelTypeCode());
                operator = "and";
            }
            if (StringUtils.isNotBlank(spec.getModelOptionCode()) && !"*".equals(spec.getModelOptionCode())) {
                specFilter = specFilter + operator + " ps.model_option_code = ?" + paramIx + " \n";
                params.put(String.valueOf(paramIx++), spec.getModelOptionCode());
                operator = "and";
            }
        }
        if (StringUtils.isNotBlank(specFilter)) {
            sql = sql + "-- spec filter \n" 
                + "left join gal143tbx f on f.product_id = lt.product_id \n" 
                + "left join gal144tbx ps on ps.product_spec_code = f.product_spec_code \n";
            sql = sql + specFilter;
        }

        sql = sql + "for read only";
        List<Long> list = findResultListByNativeQuery(sql, params);
        return list;
    }
	
    public List<Map<String, Object>> findAllProgramResultData(String programName, List<Long> messageIds) {
        String sql = SELECT_RESULTS_DATA_BY_TEST_XML;
        sql = sql + "and message_id in ( " + StringUtils.join(messageIds, ",") + " ) ";
        Parameters params = new Parameters();
        params.put("1", programName);
        sql = "with let_tbl as ( \n" + sql + "\n)\n";
        sql = sql + "select \n" + "ps.model_year_code \n" 
            + ",ps.model_code \n" 
            + ",ps.model_type_code \n" 
            + ",ps.model_option_code \n" 
            + ",l.*\n" 
            + "from let_tbl l \n"
            + "left join gal143tbx p on p.product_id = l.product_id \n" 
            + "left join gal144tbx ps on ps.product_spec_code = p.product_spec_code \n" 
            + "for read only ";
        List<Map<String, Object>> list = findResultMapByNativeQuery(sql, params);
        return list;
    }
    
    public List<LetMessage> findAllByProductId(String productId) {
        Parameters params = new Parameters();
		params.put("1", productId);
		return findAllByNativeQuery(FIND_ALL_BY_PRODUCT_ID, params);
    }
    
    public List<LetMessage> findAll(String productId, String totalStatus) {
        Parameters params = new Parameters();
        params.put("1", productId);
        params.put("2", totalStatus);
		return findAllByNativeQuery(FIND_ALL_BY_PRODUCT_ID_AND_TOTAL_STATUS, params);
    }
}