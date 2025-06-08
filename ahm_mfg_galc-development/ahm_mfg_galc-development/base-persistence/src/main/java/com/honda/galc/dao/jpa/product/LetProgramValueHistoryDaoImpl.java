package com.honda.galc.dao.jpa.product;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LetProgramValueHistoryDao;
import com.honda.galc.entity.product.LetProgramValueHistory;
import com.honda.galc.entity.product.LetProgramValueHistoryId;
import com.honda.galc.service.Parameters;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public class LetProgramValueHistoryDaoImpl extends BaseDaoImpl<LetProgramValueHistory, LetProgramValueHistoryId> implements LetProgramValueHistoryDao {

	private static String GET_PROGRAM_VALUE_DATA="SELECT PRODUCT_ID, TEST_SEQ, INSPECTION_PGM_ID, INSPECTION_PARAM_ID, INSPECTION_PARAM_TYPE, INSPECTION_PARAM_VALUE, INSPECTION_PARAM_UNIT, CREATE_TIMESTAMP, UPDATE_TIMESTAMP  FROM GAL704TBXV WHERE END_TIMESTAMP = (SELECT END_TIMESTAMP FROM GAL701TBX WHERE PRODUCT_ID = ?1 AND TEST_SEQ = ?2 ) AND PRODUCT_ID = ?1 AND  TEST_SEQ = ?2 AND  INSPECTION_PGM_ID = ?3 AND  INSPECTION_PARAM_ID = ?4 AND  INSPECTION_PARAM_TYPE = ?5";                                                 	                                               
	private static String UPDATE_PROGRAM_VALUE_DATA="UPDATE GAL704TBXV SET PRODUCT_ID = ?1, TEST_SEQ = ?2, INSPECTION_PGM_ID = ?3, INSPECTION_PARAM_ID = ?4, INSPECTION_PARAM_VALUE = ?5 WHERE END_TIMESTAMP = (SELECT END_TIMESTAMP FROM GAL701TBX WHERE PRODUCT_ID = ?1 AND TEST_SEQ = ?2) AND PRODUCT_ID = ?1 AND TEST_SEQ = ?2 AND INSPECTION_PGM_ID = ?3 AND INSPECTION_PARAM_ID = ?4 AND INSPECTION_PARAM_TYPE = ?6";

	public Object[] getProgramValueData(String productId,Integer testSeq,Integer inspectionPgmId,Integer inspectionParamId,String inspectionParamType) 
	{
		Parameters param=Parameters.with("1",productId);
		param.put("2", testSeq);
		param.put("3", inspectionPgmId);
		param.put("4", inspectionParamId);
		param.put("5", inspectionParamType);
		return findFirstByNativeQuery(GET_PROGRAM_VALUE_DATA, param,Object[].class);
	}

	@Transactional
	public int updateProgramValueData(String productId ,Integer testSeq,Integer inspectionPgmId,Integer inspectionParamId,String inspectionParamValue,String inspectionParamType) 
	{
		Parameters param=Parameters.with("1",productId);
		param.put("2", testSeq);
		param.put("3", inspectionPgmId);
		param.put("4", inspectionParamId);
		param.put("5", inspectionParamValue);
		param.put("6", inspectionParamType);
		return executeNativeUpdate(UPDATE_PROGRAM_VALUE_DATA, param);

	}

}
