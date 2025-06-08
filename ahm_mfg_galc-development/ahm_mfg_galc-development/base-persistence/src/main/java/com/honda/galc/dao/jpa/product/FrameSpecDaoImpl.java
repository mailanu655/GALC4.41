package com.honda.galc.dao.jpa.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.dto.ValidAvailableSpecForALtMto;
import com.honda.galc.dto.qi.QiMtcToEntryModelDto;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.ProductSpecCode;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.ProductSpecUtil;
import com.honda.galc.util.StringUtil;

/** * * 
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012
 */
public class FrameSpecDaoImpl extends ProductSpecDaoImpl<FrameSpec,String> implements FrameSpecDao{

	private String QUERY_SPEC_CODES_ONLY = "select a.productSpecCode, a.modelYearCode,a.modelCode,a.modelTypeCode,a.modelOptionCode,a.extColorCode,a.intColorCode from FrameSpec a";
	private String QUERY_ACTIVE_SPEC_CODES_ONLY = "select distinct a.product_spec_code, a.model_year_code,a.model_code,a.model_type_code,a.model_option_code,a.ext_color_code,a.int_color_code from GALADM.GAL144TBX a inner join GALADM.gal212tbx b on a.PRODUCT_SPEC_CODE = b.PRODUCT_SPEC_CODE";
	private final static String FIND_SUB_PRODUCT = "SELECT PRODUCT_SPEC_CODE MODEL_YEAR_CODE,MODEL_CODE,MODEL_TYPE_CODE,MODEL_OPTION_CODE," + 
			"EXT_COLOR_CODE,INT_COLOR_CODE FROM GALADM.GAL144TBX";
	private String QUERY_YEAR_CODE = "select distinct p.modelYearCode from FrameSpec p";
	private String QUERY_SPEC_CODES = "select distinct p.productSpecCode from FrameSpec p";

	private final String FIND_ALL_BY_PROCESS_POINT_ID = "select s from FrameSpec s, LotControlRule r " +
			"where r.id.processPointId = :processPointId and s.modelYearCode = r.id.modelYearCode and s.modelCode = r.id.modelCode";
	
	private final String FIND_BY_PRODUCT_ID = "select s from FrameSpec s, Frame f " +
			"where f.productId = :productId and f.productSpecCode = s.productSpecCode";

	private final String FIND_ALL_BY_PREFIX = "select s from FrameSpec s where s.productSpecCode like :specCodePrefix";
	private final String FIND_COLOR_DETAILS = "select distinct s.extColorCode, s.intColorCode, s.extColorDescription, s.intColorDescription, s.productSpecCode, s.salesExtColorCode "
			+ " from FrameSpec s where s.productSpecCode like :specCodePrefix order by s.extColorDescription";
	private final String FIND_TCU_DATA = "SELECT   C.* "
			+ "FROM GAL185TBX A, GAL143TBX B, GAL144TBX C  WHERE A.PRODUCT_ID = B.PRODUCT_ID AND B.PRODUCT_SPEC_CODE = C.PRODUCT_SPEC_CODE "
			+ "AND   A.INSTALLED_PART_STATUS = 1 AND A.PRODUCT_ID = ?1 ";
	private static final String FIND_ALL_MODELCODE_YEAR = "SELECT MODEL_CODE,MODEL_YEAR_DESCRIPTION, MODEL_YEAR_CODE FROM GALADM.GAL144TBX GROUP BY MODEL_CODE,MODEL_YEAR_DESCRIPTION, MODEL_YEAR_CODE";
    private static final String FIND_BY_FILTER_MODEL_YEAR_CODE = " select model_year_code, model_code, model_year_description, model_description, short_model_description from galadm.gal144tbx where concat(model_year_code, model_code) like ?1 or  model_year_description like ?1  or model_description like ?1 group by model_year_code, model_code, model_year_description, model_description, short_model_description";

	private static final String FIND_ALL_MATCHING_SPEC_WITH_ALT_ENGINE_SPEC = "select MODEL_YEAR_CODE, MODEL_CODE, MODEL_TYPE_CODE, ENGINE_MTO, ALT_ENGINE_MTO From GAL144TBX where (ALT_ENGINE_MTO in (select distinct(ALT_ENGINE_MTO) from gal144tbx where ENGINE_MTO = ?1) or ENGINE_MTO = ?1) and ALT_ENGINE_MTO IS NOT NULL group by MODEL_YEAR_CODE, MODEL_CODE, MODEL_TYPE_CODE, ALT_ENGINE_MTO, ENGINE_MTO";

	private final String FIND_MODEL_YEAR_CODE="SELECT DISTINCT MODEL_YEAR_CODE FROM GALADM.GAL144TBX WHERE MODEL_YEAR_DESCRIPTION >= (YEAR(CURRENT_DATE)-1)";

	private final String FIND_MODEL_CODE_S="SELECT DISTINCT MODEL_CODE FROM GALADM.GAL144TBX WHERE MODEL_YEAR_CODE in (@modelYearList@)";

	private static final String FIND_ALL_AFTER_MODEL_YEAR_DESCRIPTION = "select p from FrameSpec p where p.modelYearDescription >= :modelYearDescription";
	private static final String FIND_ALL_COLOR_CODES_AFTER_MODEL_YEAR_DESCRIPTION = "select distinct p.extColorCode from FrameSpec p where p.modelYearDescription >= :modelYearDescription order by p.extColorCode";

	private static final String FIND_FRAME_NO_PREFIX = "select distinct SUBSTR(FRAME_NO_PREFIX, 1, 5) as PREFIX from gal144tbx join gal212tbx on  gal144tbx.PRODUCT_SPEC_CODE = gal212tbx.product_spec_code";
	
	private static final String GET_PRODUCT_DETAILS = "select  g144.MODEL_CODE, g212.SEND_STATUS, g144.MODEL_YEAR_CODE,g144.MODEL_OPTION_CODE, g144.MODEL_TYPE_CODE, g216.PRODUCT_ID, g212.PRODUCTION_LOT, "
			+ "trim(g212.PRODUCT_SPEC_CODE) as PRODUCT_SPEC_CODE, g144.EXT_COLOR_CODE, g144.EXT_COLOR_DESCRIPTION, g144.INT_COLOR_CODE, trim(g144.INT_COLOR_DESCRIPTION) as "
			+ "INT_COLOR_DESCRIPTION, g144.SALES_MODEL_TYPE_CODE, g144.SALES_MODEL_CODE, g144.SALES_EXT_COLOR_CODE from galadm.GAL212TBX g212 left join galadm.GAL144TBX g144"
			+ " on g212.PRODUCT_SPEC_CODE = g144.PRODUCT_SPEC_CODE join galadm.GAL216TBX g216 on g216.PRODUCTION_LOT = g212.PRODUCTION_LOT  where g212.PRODUCTION_LOT "
			+ "in (select g216tbx.PRODUCTION_LOT from galadm.GAL216TBX g216tbx   where g216tbx.PRODUCT_ID = ?1 union select g143.PRODUCTION_LOT from  galadm.GAL143TBX"
			+ " g143 where g143.PRODUCT_ID = ?1)";
	
	private static final String FRAME_SPEC_CODE_UPDATE = "UPDATE galadm.GAL144TBX set PRODUCT_SPEC_CODE = ?1 , MODEL_YEAR_CODE = ?2, "
			+ "MODEL_CODE = ?3, MODEL_TYPE_CODE=?4, MODEL_OPTION_CODE=?5, EXT_COLOR_CODE=?6, INT_COLOR_CODE=?7 where PRODUCT_SPEC_CODE = ?8";

			
	@SuppressWarnings("unchecked")
	public List<String> findAllModelYearCodes() {

		return entityManager.createQuery(QUERY_YEAR_CODE).getResultList();

	}

	public List<FrameSpec> findAllByModelYearCode(String yearCode) {

		return findAll(Parameters.with("modelYearCode", yearCode));
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllProductSpecCodes() {
		return entityManager.createQuery(QUERY_SPEC_CODES).getResultList();
	}

	/**
	 * only populate the product spec code and individual year code, type code etc
	 * to save query time
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FrameSpec> findAllProductSpecCodesOnly(String productType){

		List<FrameSpec> frameSpecs = new ArrayList<FrameSpec>();
		List result =  findAllByQuery(QUERY_SPEC_CODES_ONLY);

		Iterator it = result.iterator();
		while(it.hasNext()) {
			Object[] objects = (Object[]) it.next();

			FrameSpec frameSpec = new FrameSpec();
			frameSpec.setProductSpecCode((String)objects[0]);
			frameSpec.setModelYearCode((String)objects[1]);
			frameSpec.setModelCode((String)objects[2]);
			frameSpec.setModelTypeCode((String)objects[3]);
			frameSpec.setModelOptionCode((String)objects[4]);
			frameSpec.setExtColorCode((String)objects[5]);
			frameSpec.setIntColorCode((String)objects[6]);
			frameSpecs.add(frameSpec);
		}

		return frameSpecs;
	}

	@SuppressWarnings("unchecked")
	public List<FrameSpec> findAllActiveProductSpecCodesOnly(String productType){

		List<FrameSpec> frameSpecs = new ArrayList<FrameSpec>();
		List result =  findAllByNativeQuery(QUERY_ACTIVE_SPEC_CODES_ONLY, null, Object[].class);

		Iterator it = result.iterator();
		while(it.hasNext()) {
			Object[] objects = (Object[]) it.next();

			FrameSpec frameSpec = new FrameSpec();
			frameSpec.setProductSpecCode((String)objects[0]);
			frameSpec.setModelYearCode((String)objects[1]);
			frameSpec.setModelCode((String)objects[2]);
			frameSpec.setModelTypeCode((String)objects[3]);
			frameSpec.setModelOptionCode((String)objects[4]);
			frameSpec.setExtColorCode((String)objects[5]);
			frameSpec.setIntColorCode((String)objects[6]);
			frameSpecs.add(frameSpec);
		}
		return frameSpecs;
	}

	public List<FrameSpec> findAllByProcessPointId(String processPointId) {
		return findAllByQuery(FIND_ALL_BY_PROCESS_POINT_ID, Parameters.with("processPointId", processPointId));
	}
	
	public FrameSpec findByProductId(String productId) {
		return findFirstByQuery(FIND_BY_PRODUCT_ID, Parameters.with("productId", productId));
	}

	public List<FrameSpec> findAllByPrefix(String prefix) {
		return findAllByQuery(FIND_ALL_BY_PREFIX, Parameters.with("specCodePrefix", prefix + "%"));
	}
	public FrameSpec findByProductSpecCode(String productSpecCode,String productType) {
		return findByKey(productSpecCode);
	}

	public FrameSpec findTCUData(String productId) {
		Parameters params = Parameters.with("1", productId);		
		FrameSpec spec = findFirstByNativeQuery(FIND_TCU_DATA, params, FrameSpec.class);
		return spec;
	}

	public Map<String, String> findAllModelCodeYear() {
		HashMap<String, String> modelCodeYearMap = new HashMap<String, String>();

		List<Object[]> modelCodeYearLst = findAllByNativeQuery(FIND_ALL_MODELCODE_YEAR, null, Object[].class);
		for(Object[] modelCodeYear : modelCodeYearLst){
			modelCodeYearMap.put(new String(modelCodeYear[0].toString().trim() + "-" +  modelCodeYear[1].toString().trim()), modelCodeYear[2].toString().trim());
		}
		return modelCodeYearMap;
	}

	public  String appendSqlClause(Parameters params,String query,String appendSql)
	{
		return (params.size()>0)?(query+" AND"+appendSql):(query+appendSql);
	}
	
	public  List<FrameSpec> findAllByYMTOCWildCard(String modelYear,String modelCode,String modelType,String modelOption,String extColor,String intColor)
	{
		Parameters params =new Parameters();
		String query= "SELECT * FROM GALADM.GAL144TBX WHERE  ";	
		if(modelYear!=null && modelYear.length()!=0 && !modelYear.equals("*"))
		{
			query=appendSqlClause(params,query," MODEL_YEAR_CODE=?1");
			params.put("1", modelYear)	;
		}
		if(modelCode!=null && modelCode.length()!=0 && !modelCode.equals("*"))
		{
			query=appendSqlClause(params,query," MODEL_CODE=?2");
			params.put("2", modelCode)	;
		}
		if(modelType!=null && modelType.length()!=0 && !modelType.equals("*"))
		{
			query=appendSqlClause(params,query," MODEL_TYPE_CODE=?3");
			params.put("3", modelType)	;
		}
		if(modelOption!=null && modelOption.length()!=0 && !modelOption.equals("*"))
		{
			query=appendSqlClause(params,query," MODEL_OPTION_CODE=?4");
			params.put("4", modelOption)	;
		}
		if(intColor!=null && intColor.length()!=0 && !intColor.equals("*"))
		{
			query=appendSqlClause(params,query," INT_COLOR_CODE=?5");
			params.put("5", intColor)	;
		}
		if(extColor!=null && extColor.length()!=0 && !extColor.equals("*"))
		{
			query=appendSqlClause(params,query," EXT_COLOR_CODE=?6");
			params.put("6", extColor)	;
		}
		if(params.size()==0)
		{
			query=query.replace("WHERE", "");
		}
		return	findAllByNativeQuery(query, params, FrameSpec.class);    
	}

	public  List<FrameSpec> findAllByMTOCWildCard(String modelCode,String modelType,String modelOption,String extColor,String intColor)
	{
		Parameters params =new Parameters();
		String query= "SELECT * FROM GALADM.GAL144TBX WHERE  ";	
		if(modelCode!=null && modelCode.length()!=0 && !modelCode.equals("*"))
		{
			query=appendSqlClause(params,query," MODEL_CODE=?1");
			params.put("1", modelCode)	;
		}
		if(modelType!=null && modelType.length()!=0 && !modelType.equals("*"))
		{
			query=appendSqlClause(params,query," MODEL_TYPE_CODE=?2");
			params.put("2", modelType)	;
		}
		if(modelOption!=null && modelOption.length()!=0 && !modelOption.equals("*"))
		{
			query=appendSqlClause(params,query," MODEL_OPTION_CODE=?3");
			params.put("3", modelOption)	;
		}
		if(intColor!=null && intColor.length()!=0 && !intColor.equals("*"))
		{
			query=appendSqlClause(params,query," INT_COLOR_CODE=?4");
			params.put("4", intColor)	;
		}
		if(extColor!=null && extColor.length()!=0 && !extColor.equals("*"))
		{
			query=appendSqlClause(params,query," EXT_COLOR_CODE=?5");
			params.put("extColor", extColor)	;
		}
		if(params.size()==0)
		{
			query=query.replace("WHERE", "");
		}
		return	findAllByNativeQuery(query, params, FrameSpec.class);    
	}

	/**
	 * returns where clause that matches product spec code for frame
	 */
	public String getSpecCodeMatchSql(String productSpecCode) {
		return " (trim(substr(r.product_spec_code,1,1)) in ('" + ProductSpecUtil.extractModelYearCode(productSpecCode) + "','','*'))" +
				" AND (trim(substr(r.product_spec_code,2,3)) in ('" + ProductSpecUtil.extractModelCode(productSpecCode) + "','','*'))" +
				" AND (trim(substr(r.product_spec_code,5,3)) in ('" + ProductSpecUtil.extractModelTypeCode(productSpecCode) + "','','*'))" +
				" AND (trim(substr(r.product_spec_code,8,3)) in ('" + ProductSpecUtil.extractModelOptionCode(productSpecCode) + "','','*'))" +
				" AND (trim(substr(r.product_spec_code,11,10)) in ('" + ProductSpecUtil.extractExtColorCode(productSpecCode) + "','','*'))" +
				" AND (trim(substr(r.product_spec_code,21,2)) in ('" + ProductSpecUtil.extractIntColorCode(productSpecCode) + "','','*'))";
	}

	/**
	 * This Method is used to populate available Mtc model based on filter
	 */
	public List<QiMtcToEntryModelDto> findAllMtcModelYearCodesByFilter(String filter, String productType) {
		Parameters param = Parameters.with("1","%"+StringUtils.trimToEmpty(filter)+"%");
		List<QiMtcToEntryModelDto> resultList = findAllByNativeQuery(FIND_BY_FILTER_MODEL_YEAR_CODE, param, QiMtcToEntryModelDto.class);
		return resultList;
	}

	public List<ValidAvailableSpecForALtMto> findAllByAltEngineMto(String altProductSpec) {
		Parameters param = Parameters.with("1", altProductSpec);
		List<Object[]> matchingSpecLst = findAllByNativeQuery(FIND_ALL_MATCHING_SPEC_WITH_ALT_ENGINE_SPEC, param, Object[].class);
		List<ValidAvailableSpecForALtMto> validAvailableMtos = new ArrayList<ValidAvailableSpecForALtMto>();

		Iterator it =matchingSpecLst.iterator(); 
		while(it.hasNext()){
			Object[] objects = (Object[]) it.next();
			ValidAvailableSpecForALtMto temp = new ValidAvailableSpecForALtMto();
			temp.setModelYearCode((String)objects[0]);
			temp.setModelCode((String)objects[1]);
			temp.setModelTypeCode((String)objects[2]);
			temp.setEngineMto((String)objects[3]);
			temp.setAltEngineMto((String)objects[4]);
			validAvailableMtos.add(temp);
		}
		return validAvailableMtos;
	}

	public List<String> findModelYearCodes() {
		return findAllByNativeQuery(FIND_MODEL_YEAR_CODE, null, String.class);
	}

	public List<String> findModelCodes(List<String> modelYearCodes) {
		return findAllByNativeQuery(FIND_MODEL_CODE_S.replace("@modelYearList@", StringUtil.toSqlInString(modelYearCodes)), null, String.class);
	}

	@Override
	public List<FrameSpec> findAllAfterModelYearDescription(String modelYearDescription) {
		return findAllByQuery(FIND_ALL_AFTER_MODEL_YEAR_DESCRIPTION, Parameters.with("modelYearDescription", modelYearDescription));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findAllColorCodesAfterModelYearDescription(String modelYearDescription) {
		return findResultListByQuery(FIND_ALL_COLOR_CODES_AFTER_MODEL_YEAR_DESCRIPTION, Parameters.with("modelYearDescription", modelYearDescription));
	}
	
	public List<String> findDistinctFramePrefix() {
		return findAllByNativeQuery(FIND_FRAME_NO_PREFIX, null, String.class);
	}
	
	public List<FrameSpec> findAllBySalesModelTypeExtColor(String salesModelCode, String salesModelTypeCode, String salesExtColorCode) {
		Parameters params = Parameters.with("salesModelCode", salesModelCode);
		params.put("salesModelTypeCode", salesModelTypeCode);
		params.put("salesExtColorCode", salesExtColorCode);
		return findAll(params);
	}
	
	public List<FrameSpec> findAllBySalesCommonModelTypeExtColor(String commonSalesModelCode, String salesModelTypeCode, String salesExtColorCode) {
		Parameters params = Parameters.with("commonSalesModelCode", commonSalesModelCode);
		params.put("salesModelTypeCode", salesModelTypeCode);
		params.put("salesExtColorCode", salesExtColorCode);
		return findAll(params);
	}
	
	
	public List<FrameSpecDto> getProductIdDetails(String productId){
		Parameters parameters = Parameters.with("1", productId);
		return findAllByNativeQuery(GET_PRODUCT_DETAILS, parameters, FrameSpecDto.class);
	}
	
	public List<FrameSpec> getColorDetails(String productSpecCode) {
		 List result =  findAllByQuery(FIND_COLOR_DETAILS, Parameters.with("specCodePrefix", productSpecCode + "%"));
		 List<FrameSpec> frameSpecs = new ArrayList<FrameSpec>();
		 HashSet<String> extColors = new HashSet<String>();
		 Iterator it = result.iterator();
			while(it.hasNext()) {
				Object[] objects = (Object[]) it.next();
				FrameSpec frameSpec = new FrameSpec();
				frameSpec.setExtColorCode((String)objects[0]);
				frameSpec.setIntColorCode((String)objects[1]);
				frameSpec.setExtColorDescription((String)objects[2]);
				frameSpec.setIntColorDescription((String)objects[3]);
				frameSpec.setProductSpecCode((String)objects[4]);
				frameSpec.setSalesExtColorCode((String)objects[5]);
				
				if(!extColors.contains(frameSpec.getExtColorCode()))
					frameSpecs.add(frameSpec);
				
				if(!extColors.contains(frameSpec.getExtColorCode()))
						extColors.add(frameSpec.getExtColorCode());
			}
		return frameSpecs;
	}
	
	
	public FrameSpec getFrameDetails(FrameSpecDto selectedFrameSpecDto) {
		
		Parameters parameters = Parameters.with("productSpecCode", selectedFrameSpecDto.getProductSpecCode())
				.put("extColorCode",selectedFrameSpecDto.getExtColorCode()).put("intColorCode", selectedFrameSpecDto.getIntColorCode())
				.put("salesExtColorCode", selectedFrameSpecDto.getSalesExtColorCode());
	
		List<FrameSpec> frameSpecList = findAll(parameters);
		if(frameSpecList.size() >= 1) {
			FrameSpec framespec = frameSpecList.get(0);
			return framespec;
		}
		
		return null;
	}
	
	@Transactional
	public void updateColorDetails(String productCodeForUpdate, FrameSpecDto selectedFrameSpecDto, List<String> productionLots) {
		ServiceFactory.getDao(ProductionLotDao.class).updateColorDetails(productCodeForUpdate, selectedFrameSpecDto, productionLots);
		ServiceFactory.getDao(PreProductionLotDao.class).updatePreProdLotDetails(productCodeForUpdate, selectedFrameSpecDto, productionLots);
		ServiceFactory.getDao(FrameDao.class).updateFrameDetails(productCodeForUpdate, selectedFrameSpecDto, productionLots);
		ServiceFactory.getDao(ProductResultDao.class).updateColorDetails(productCodeForUpdate, selectedFrameSpecDto, productionLots);
	}
	
	/**
	 * Native query used to update the primary key
	 */
	@Transactional
	public void updateFrameSpecCode(FrameSpec frameSpec, String oldSpecCode) {
		Parameters param = Parameters.with("1", frameSpec.getProductSpecCode());
		param.put("2", frameSpec.getModelYearCode()).put("3", frameSpec.getModelCode());
		param.put("4", frameSpec.getModelTypeCode()).put("5", frameSpec.getModelOptionCode());
		param.put("6", frameSpec.getExtColorCode()).put("7", frameSpec.getIntColorCode()).put("8", oldSpecCode);
		executeNative(FRAME_SPEC_CODE_UPDATE, param);
	}

}
