package com.honda.galc.dao.product;

import java.sql.Timestamp;
import java.util.List;

import com.honda.galc.entity.product.LetProgramResultValueId;
import com.honda.galc.entity.product.LetResult;
import com.honda.galc.entity.product.LetResultId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public interface LetResultDao extends IDaoService<LetResult, LetResultId> {

	public LetResult save(LetResult entity);
	public int getProductCount(String productId);
	public Timestamp getMaxEndTimestamp(String productId) ;
	public List<Object[]> getLetPassCriteriaProgramDataForMto(String yearCode,String modelCode,String typeCode,String optionCode,Timestamp timestamp);	
	public List<Object[]> getLetInspectionResults(String strProductId,String strTestSeq,String strInspectionPgmId);
	public Object[] getLetProduct(String seriesFrame);
	public Object[] getLetProductSeriesFrame(String productId );
	public List<Object[]> getVinMtoData(String strProductId);
	public List<Object[]> getLetResultByProductId(String strProductId);
	public List<Object[]> getFaultDataByProductIdTestSeq(String strProductId,int testSeq);
	public List<LetResult> findLetResultByProductIdSeq(String productId,Integer seqNum);
	public Object[] getLetProgramResultByProductIdSeqNum(String productId,Integer testSeq) ;
	public int deleteLetProgramResultByProductIdSeqNum(String productId,Integer testSeq);
	public Object[] getLetProgramResultValueByProductIdSeqNum(String productId,Integer testSeq) ;
	
	public List<Object[]> getLetProgramResultValueByProductIdParamId(String productId,Integer paramId) ;
	public List<Object[]> getLetProgramResultValueByProductIdParamIdEndTime(String productId,Integer paramId, Timestamp startTime, Timestamp endTime) ;
	public List<Object[]> getLetProgramResultValueByParamIdEndTime(Integer paramId, Timestamp startTime, Timestamp endTime) ;
	public Object[] getLetProgramResultValueById(LetProgramResultValueId id) ;
	
	public int deleteLetProgramResultValueByProductIdSeqNum(String productId,Integer testSeq);
	public LetResult findLetResultByProductIdSeqNum(String productId,Integer testSeq);
	public LetResult findFirstByProductIdTimestamp(String productId, Timestamp endTimestamp);
	public List<Object[]> findDistinctLetResultByProductIdSeq(String productId) ;
	public List<Object[]> getLetManualInputResultData(String productId,Integer programId) ;
	public List<Object[]> getInspectionDetailResults(String productId,Integer programId);
	public int updateProgramResultByEndTimestamp(String productId ,Integer testSeq,Integer inspectionPgmId,String processStatus);
	public int updateLetResultProduction(String productId ,Integer testSeq,String production);
	public List<Object[]> getLotOutListData(String reqYear,String reqModel,String reqType,String reqOption,String sqlDateFrom,String sqlDateTo,Integer pgmId);
	public int getFaultListCntData(String reqYear,String reqModel,String reqType,String reqOption,String sqlDateFrom,String sqlDateTo,Integer pgmId);
	public List<Object[]> getFaultResultListData(String reqYear,String reqModel,String reqType,String reqOption,String sqlDateFrom,String sqlDateTo,Integer pgmId);
	public int deleteLetResultByProductIdSeqNum(String productId,Integer testSeq);
	public String getInspectionResult(String productId, int pgmId, int paramId, int pgmStatus);
	public boolean insertIfNotExist(LetResult letResult);
}