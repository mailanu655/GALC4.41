package com.honda.galc.dao.product;

import java.sql.Timestamp;
import java.util.List;
import com.honda.galc.entity.product.LetPassCriteriaMto;
import com.honda.galc.entity.product.LetPassCriteriaMtoId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public interface LetPassCriteriaMtoDao extends IDaoService<LetPassCriteriaMto, LetPassCriteriaMtoId> {

	public Object[] findLetPassCriteriaForMto(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp effectiveTime );
	public List<Object[]> findLetPassCriteriaProgramData(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp effectiveTime ); 
	public List<Object[]> findLetPassCriteriaForMtoEffTime(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp effectiveTime ) ;
	public List<Object[]> findLetPassCritPgrm(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp effectiveTime );
	public List<Object[]> findLetPassCriteriaBySearchParams(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,String checkStr,String startRow,String endRow);
	public Integer getYMTOSearchCount(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,String checkStr ) ;
	public List<LetPassCriteriaMto> findLetPassCriteriaGreaterEqualEffecTime(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp effectiveTime) ;
	public int updateLetPassCriteriaExpTime(Timestamp newExpTime,String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp effectiveTime,Timestamp oldExpTime );
	public int updateLetPassCriteriaEffTime(Timestamp newEffTime,String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp oldEffTime,Timestamp expTime ) ;
	public int deleteLetPassCriteriaMto(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp effTime ) ;
}