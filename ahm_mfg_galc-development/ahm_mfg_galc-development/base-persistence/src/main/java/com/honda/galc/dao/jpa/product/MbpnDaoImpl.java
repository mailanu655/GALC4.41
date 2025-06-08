package com.honda.galc.dao.jpa.product;

import java.sql.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.dto.qi.QiMtcToEntryModelDto;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.MbpnSpecCodeUtil;
import com.honda.galc.util.StringUtil;

/**
 * @author Gangadhararao Gadde 
 * @date Nov 1, 2012
 */
public class MbpnDaoImpl extends BaseDaoImpl<Mbpn, String> implements MbpnDao{

	protected static final String QUERY_MODEL_CODES = "select distinct p.classNo from ";
	
	private static final String FIND_ALL_DATES = 
			"SELECT DISTINCT(DATE(A.CREATE_TIMESTAMP)) AS PRODUCTION_DATE FROM GALADM.GAL215TBX A, GALADM.MBPN_PRODUCT_TBX B " +
			"WHERE A.PRODUCT_ID = B.PRODUCT_ID AND A.PROCESS_POINT_ID in (@ProcessPointIds@) AND DATE(A.CREATE_TIMESTAMP) < ?1 ORDER BY PRODUCTION_DATE ASC";
		
	
	private static final String FIND_CLASS_NO_BY_FILTER = "select distinct CLASS_NO from GALADM.MBPN_TBX where CLASS_NO like ?1";
	
	private static final String FIND_ALL_MBPN_DESC_BY_MAIN_NO_AND_CLASS_NO = "select * from GALADM.MBPN_TBX where MAIN_NO = ?1 AND CLASS_NO LIKE ?2";
	
	private static final String FIND_NON_BLANK_MBPN_DESC_BY_MAIN_NO_AND_CLASS_NO = "select * from GALADM.MBPN_TBX where MAIN_NO = ?1 AND CLASS_NO = ?2 AND (DESCRIPTION IS NOT NULL and DESCRIPTION != '')";
	
	private final static String FIND_ALL_MAIN_NO  = "select distinct mbpn.mainNo from Mbpn mbpn order by mbpn.mainNo";
	
	private final static String FIND_ALL_CLASS_NO  = "select distinct mbpn.classNo from Mbpn mbpn order by mbpn.classNo";
	
	private final static String FIND_ALL_PROTOTYPE_CODE  = "select distinct mbpn.prototypeCode from Mbpn mbpn order by mbpn.prototypeCode";
	
	private final static String FIND_ALL_TYPE_NO  = "select distinct mbpn.typeNo from Mbpn mbpn order by mbpn.typeNo";
	
	private final static String FIND_ALL_SUPPLEMENTARY_NO  = "select distinct mbpn.supplementaryNo from Mbpn mbpn order by mbpn.supplementaryNo";
	
	private final static String FIND_ALL_TARGET_NO  = "select distinct mbpn.targetNo from Mbpn mbpn order by mbpn.targetNo";
	
	private final static String FIND_ALL_HES_COLOR  = "select distinct mbpn.hesColor from Mbpn mbpn order by mbpn.hesColor";
	
	private static String UPDATE_MBPN = "update MBPN_TBX  set MAIN_NO = ?1, CLASS_NO= ?2 , " +
			"PROTOTYPE_CODE = ?3, TYPE_NO =?4 , SUPPLEMENTARY_NO =?5, " +
			"TARGET_NO =?6, HES_COLOR = ?7, MBPN = ?8, PRODUCT_SPEC_CODE = ?9, DESCRIPTION =?11 where PRODUCT_SPEC_CODE= ?10";
	
	private static final String FIND_ALL_DISTINCT_MBPN_BY_MAIN_NO = "select DISTINCT CLASS_NO from MBPN_TBX  where MAIN_NO = ?1 " +
	 "and DESCRIPTION IS NOT NULL and DESCRIPTION != '' and CLASS_NO in (select MTC_MODEL from QI_STATION_ENTRY_SCREEN_TBX a, QI_ENTRY_MODEL_GROUPING_TBX b " + 
	 "where a.ENTRY_MODEL = b.ENTRY_MODEL and b.IS_USED = 1 and a.PROCESS_POINT_ID = ?2 and a.DIVISION_ID = ?3)";

	private static final String FIND_MBPN_BY_PRODUCT_TYPE =  "select m from Mbpn m, MbpnProductType t where t.productType = :productType and t.id.mainNo = m.mainNo order by m.productSpecCode";
	
	private static final String FIND_ALL_BY_MAIN_NO = "select m from Mbpn m where m.mainNo in ( :mainNo)";
	
	private static final String FIND_ALL_BY_PREFIX = "select s from Mbpn s where s.productSpecCode like :specCodePrefix";
	
	private final static String FIND_ALL_PROD_SPEC_CODE  = "select distinct mbpn.productSpecCode from Mbpn mbpn order by mbpn.productSpecCode";
	
	private final String FIND_BY_PRODUCT_ID = "select s from Mbpn s, MbpnProduct p " +
			"where p.productId = :productId and p.currentProductSpecCode = s.productSpecCode";

	public List<String> findAllModelCodes(String productType) {
		return findAllClassNos();
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllClassNos() {
		return entityManager.createQuery(QUERY_MODEL_CODES + entityClass.getSimpleName() + " p").getResultList();
	}

	public List<Mbpn> findAllProductSpecCodesOnly(String productType) {
		if(!ProductTypeCatalog.isMbpnSubProduct(productType))
			return findAll(null, new String[]{"productSpecCode"});
		else
			return findAllMbpns(productType);
	}

	@SuppressWarnings("unchecked")
	private List<Mbpn> findAllMbpns(String productType) {
		return findResultListByQuery(FIND_MBPN_BY_PRODUCT_TYPE, Parameters.with("productType", productType));
	}

	public Mbpn findByProductSpecCode(String productSpecCode, String productType) {
		
		return findByKey(productSpecCode);
	}
	
	
	/**
	 * @RGALCDEV-1628
	 * This alternative implementation of findByProductSpecCode is used
	 * when an isolated data source is required by the execution flow
	 * of the invoking service.
	 * 
	 * @param productSpecCode
	 * @param productType
	 * @return
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Mbpn findByProductSpecCode_NoTxn(String productSpecCode,String productType) {
		
		return findByProductSpecCode(productSpecCode, productType);
	}

	@SuppressWarnings("static-access")
	public List<Date> findAllProductionDates(String processPointId, Date date) {
		return findAllByNativeQuery(FIND_ALL_DATES.replace("@ProcessPointIds@", StringUtil.toSqlInString(processPointId)), new Parameters().with("1", date), Date.class);
	}
	
	public String getSpecCodeMatchSql(String productSpecCode) {
	    return " substr(r.product_spec_code,1,5) = '" + MbpnSpecCodeUtil.getMainNo(productSpecCode) + "'" +
	    " AND (substr(r.product_spec_code,6,3) = '" + MbpnSpecCodeUtil.getClassNo(productSpecCode) + "' OR trim(substr(r.product_spec_code,6,3)) = '')" +
	    " AND (substr(r.product_spec_code,9,1) = '" + MbpnSpecCodeUtil.getProtoTypeCode(productSpecCode) + "' OR trim(substr(r.product_spec_code,9,1)) = '')" +
	    " AND (trim(substr(r.product_spec_code,10,4)) = '" + MbpnSpecCodeUtil.getTypeNo(productSpecCode) + "' OR trim(substr(r.product_spec_code,10,4)) = '')" +
	    " AND (trim(substr(r.product_spec_code,14,2)) = '" + MbpnSpecCodeUtil.getSupplementaryNo(productSpecCode) + "' OR trim(substr(r.product_spec_code,14,2)) = '')" +
	    " AND (trim(substr(r.product_spec_code,16,2)) = '" + MbpnSpecCodeUtil.getTargetNo(productSpecCode) + "' OR trim(substr(r.product_spec_code,16,2)) = '')" +
	    " AND (trim(substr(r.product_spec_code,18,10)) = '" + MbpnSpecCodeUtil.getHesColor(productSpecCode) + "' OR trim(substr(r.product_spec_code,18,10)) = '')";
	}
	/**
	 * This Method is used to populate available Mtc model based on filter
	 * 
	 * @return
	 */
	public List<QiMtcToEntryModelDto> findAllMtcModelYearCodesByFilter(String filter, String productType){
			Parameters param = Parameters.with("1", "%" + StringUtils.trimToEmpty(filter) + "%");
	List<QiMtcToEntryModelDto> resultList = findAllByNativeQuery(FIND_CLASS_NO_BY_FILTER, param,QiMtcToEntryModelDto.class);
	return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findAllMainNo() {
		return findResultListByQuery(FIND_ALL_MAIN_NO,null);
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllClassNo() {
		return findResultListByQuery(FIND_ALL_CLASS_NO,null);
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllProtoTypeCode() {
		return findResultListByQuery(FIND_ALL_PROTOTYPE_CODE,null);
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllTypeNo() {
		return findResultListByQuery(FIND_ALL_TYPE_NO,null);
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllSupplementaryNo() {
		return findResultListByQuery(FIND_ALL_SUPPLEMENTARY_NO,null);
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllTargetNo() {
		return findResultListByQuery(FIND_ALL_TARGET_NO,null);
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllHesColor() {
		return findResultListByQuery(FIND_ALL_HES_COLOR,null);
	}

	public List<Mbpn> findAllMbpn() {
		return findAll(null, new String[]{"productSpecCode"}, true);
	}
	
	@Transactional
	public void updateMbpnData(Mbpn mbpn, String productSpecCode) {
		Parameters params = Parameters.with("1", mbpn.getMainNo())
				.put("2", mbpn.getClassNo()).put("3",mbpn.getPrototypeCode())
				.put("4", mbpn.getTypeNo()).put("5",mbpn.getSupplementaryNo())
				.put("6", mbpn.getTargetNo())
				.put("7", mbpn.getHesColor()).put("8",mbpn.getMbpn()).put("9",mbpn.getProductSpecCode()).put("10",productSpecCode).put("11", mbpn.getDescription());
		executeNativeUpdate(UPDATE_MBPN, params);
	}
	
	@SuppressWarnings("static-access")
	public List<Mbpn> findAllDescByMainNoAndClassNo(String mainPartNo, String classNo) {
		return findAllByNativeQuery(FIND_ALL_MBPN_DESC_BY_MAIN_NO_AND_CLASS_NO, new Parameters().with("1", mainPartNo).put("2",  "%" + StringUtils.trimToEmpty(classNo) + "%"));
	}
	
	@SuppressWarnings("static-access")
	public List<Mbpn> findNonBlankDescByMainNoAndClassNo(String mainPartNo, String classNo) {
		return findAllByNativeQuery(FIND_NON_BLANK_MBPN_DESC_BY_MAIN_NO_AND_CLASS_NO, new Parameters().with("1", mainPartNo).put("2",  StringUtils.trimToEmpty(classNo)));
	}

	public List<String> findAllClassNoByMainNo(String mainPartNo, String processPointId, String entryDept) {
		Parameters params = Parameters.with("1", mainPartNo)
				.put("2",  processPointId)
				.put("3", entryDept);
		return findAllByNativeQuery(FIND_ALL_DISTINCT_MBPN_BY_MAIN_NO, params, String.class);
	}

	public Mbpn findSpecCodeByMainNoAndClassNo(String mainPartNo, String classNo) {
		return findFirst(Parameters.with("mainNo", mainPartNo) .put("classNo", classNo));
               
	}
	
	public List<Mbpn> findAllByMainNo(List<String> mainNo) {
		return findAllByQuery(FIND_ALL_BY_MAIN_NO, Parameters.with("mainNo", mainNo));
	}
	
	public Mbpn findSpecCodeByMainNoClassNoAndHesColour(String mainPartNo, String classNo, String hesColour) {
		return findFirst(Parameters.with("mainNo", mainPartNo) .put("classNo", classNo).put("hesColor", hesColour));              
	}
	
	public List<Mbpn> findAllByPrefix(String prefix) {
		return findAllByQuery(FIND_ALL_BY_PREFIX,Parameters.with("specCodePrefix", prefix + "%"));
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllProdSpecCode() {
		return findResultListByQuery(FIND_ALL_PROD_SPEC_CODE,null);
	}

	@Override
	public Mbpn findByProductId(String productId) {
		return  findFirstByQuery(FIND_BY_PRODUCT_ID, Parameters.with("productId", productId));
	}
}
