package com.honda.galc.dao.jpa.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dto.qi.QiMtcToEntryModelDto;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.service.Parameters;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class EngineSpecDaoImpl extends ProductSpecDaoImpl<EngineSpec,String> implements EngineSpecDao{

	String QUERY_SPEC_CODES_ONLY = "select a.productSpecCode, a.modelYearCode,a.modelCode,a.modelTypeCode,a.modelOptionCode from EngineSpec a";
    String QUERY_YEAR_CODE = "select distinct p.modelYearCode from EngineSpec p";
	String QUERY_SPEC_CODES = "select distinct p.productSpecCode from EngineSpec p";
	
	private final String FIND_ALL_BY_PROCESS_POINT_ID = "select s from EngineSpec s, LotControlRule r " +
			"where r.id.processPointId = :processPointId and s.modelYearCode = r.id.modelYearCode and s.modelCode = r.id.modelCode";
	
	private final String FIND_ALL_BY_PREFIX = "select s from EngineSpec s where s.productSpecCode like :specCodePrefix";
	private static final String FIND_BY_YEAR_MODEL_CODE = "select e.modelYearCode, e.modelCode from EngineSpec e group by e.modelYearCode, e.modelCode "
			+ "order by e.modelYearCode, e.modelCode";
	private static final String FIND_BY_YEAR_MODEL_TYPE_CODE = "select e.modelTypeCode from EngineSpec e where e.modelYearCode = :modelYearCode and e.modelCode = :modelCode group by e.modelYearCode, e.modelCode, e.modelTypeCode";
	private static final String FIND_ALL_MODELCODE_YEAR = "SELECT MODEL_CODE,MODEL_YEAR_DESCRIPTION, MODEL_YEAR_CODE FROM GALADM.GAL133TBX GROUP BY MODEL_CODE,MODEL_YEAR_DESCRIPTION, MODEL_YEAR_CODE";
	private static String FIND_HOST_MTOC_ENGINE = "select " +
		"PLANT_CODE_ENGINE,ENGINE_MODEL_YEAR_CODE,ENGINE_MODEL_CODE,ENGINE_MODEL_TYPE_CODE,ENGINE_MODEL_OPTION,ENGINE_MODEL_YEAR_DESCRIPTION,ENGINE_NO_PREFIX,TRANSMISSION,TRANSMISSION_DESCRIPTION," +
		"GEAR_SHIFT,GEAR_SHIFT_DESCRIPTION,DISPLACEMENT,DISPLACEMENT_COMMENT,PLANT_CODE_MISSION,MISSION_MODEL_YEAR_CODE,MISSION_MODEL_CODE,MISSION_PROTOTYPE_CODE,MISSION_F_E,MISSION_MODEL_TYPE_CODE," +
		"ENGINE_PROTOTYPE_CODE,ENGINE_F_E" +
		" from (select t.*,rownumber() over () as rn from galadm.gal157tbx t) as x where rn between ?1 and ?2";
	private static final String FIND_BY_FILTER_MODEL_YEAR_CODE = " select model_year_code, model_code,model_year_description from galadm.gal133tbx  where concat(model_year_code, model_code) like ?1 or model_year_description like ?1 group by model_year_code, model_code,model_year_description ";
	private static final String ENGINE_SPEC_CODE_UPDATE = "UPDATE galadm.GAL133TBX set PRODUCT_SPEC_CODE = ?1 , MODEL_YEAR_CODE = ?2, "
			+ "MODEL_CODE = ?3, MODEL_TYPE_CODE=?4, MODEL_OPTION_CODE=?5 where PRODUCT_SPEC_CODE = ?6";
	
	private static final String FIND_BY_PRODUCT_ID ="select s from EngineSpec s, Engine e where e.productSpecCode = s.productSpecCode and e.productId = :productId";
	
	@SuppressWarnings("unchecked")
    public List<String> findAllModelYearCodes() {
        
        return entityManager.createQuery(QUERY_YEAR_CODE).getResultList();
        
    }

	public List<EngineSpec> findAllByModelYearCode(String yearCode) {

		return findAll(Parameters.with("modelYearCode", yearCode));
		
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllProductSpecCodes() {
		
        return entityManager.createQuery(QUERY_SPEC_CODES).getResultList();
       
	}

	public List<EngineSpec> findAllProductSpecCodesOnly(String productType) {
		
		List<EngineSpec> engineSpecs = new ArrayList<EngineSpec>();
    	List<?> result =  findAllByQuery(QUERY_SPEC_CODES_ONLY);
    	
    	Iterator<?> it = result.iterator();
    	while(it.hasNext()) {
    		Object[] objects = (Object[]) it.next();
    		
    		EngineSpec engineSpec = new EngineSpec();
    		engineSpec.setProductSpecCode((String)objects[0]);
    		engineSpec.setModelYearCode((String)objects[1]);
    		engineSpec.setModelCode((String)objects[2]);
    		engineSpec.setModelTypeCode((String)objects[3]);
    		engineSpec.setModelOptionCode((String)objects[4]);
    		engineSpecs.add(engineSpec);
    	}
    	
    	return engineSpecs;
		
	}

	public List<EngineSpec> findAllByProcessPointId(String processPointId) {
		return findAllByQuery(FIND_ALL_BY_PROCESS_POINT_ID, Parameters.with("processPointId", processPointId));
	}

	public List<String> findModelTypeCodes(String yearCode, String modelCode) {
		List<String> list = new ArrayList<String>();
		Query query = entityManager.createQuery(FIND_BY_YEAR_MODEL_TYPE_CODE);
		query.setParameter("modelYearCode", yearCode);
		query.setParameter("modelCode", modelCode);
		@SuppressWarnings("unchecked")
		List<String> resultList = query.getResultList();
		if (resultList != null) {
			for (String item : resultList) {
				list.add(item);
			}
		}
		return list;
	}
	public List<EngineSpec> findAllByPrefix(String prefix) {
		return findAllByQuery(FIND_ALL_BY_PREFIX, Parameters.with("specCodePrefix", prefix + "%"));
	}
	public EngineSpec findByProductSpecCode(String productSpecCode,String productType) {
		return findByKey(productSpecCode);
	}
	
	public List<Map<String, String>> findAllYearModelCodes() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Query query = entityManager.createQuery(FIND_BY_YEAR_MODEL_CODE);
		List<?> resultList = query.getResultList();
		if (resultList != null) {
			for (Object item : resultList) {
				Object[] row = (Object[]) item;
				Map<String, String> map = new HashMap<String, String>();
				map.put("modelYearCode", (String)row[0]);
				map.put("modelCode", (String)row[1]);
				list.add(map);
			}
		}
		return list;
	}
	/**
	 * This Method is used to populate Mtc model based on filter
	 * 
	 * @return
	 */
	public List<QiMtcToEntryModelDto> findAllMtcModelYearCodesByFilter(String filter,
			String productType) {
		Parameters param = Parameters.with("1", "%"+StringUtils.trimToEmpty(filter)+"%");
		List<QiMtcToEntryModelDto> resultList = findAllByNativeQuery(FIND_BY_FILTER_MODEL_YEAR_CODE,param,QiMtcToEntryModelDto.class);
		return resultList;
	}
	public Map<String, String> findAllModelCodeYear() {
		HashMap<String, String> modelCodeYearMap = new HashMap<String, String>();

		List<Object[]> modelCodeYearLst = findAllByNativeQuery(FIND_ALL_MODELCODE_YEAR, null, Object[].class);
		for(Object[] modelCodeYear : modelCodeYearLst){
			modelCodeYearMap.put(new String(modelCodeYear[0].toString().trim() + "-" +  modelCodeYear[1].toString().trim()), modelCodeYear[2].toString().trim());
		}
		return modelCodeYearMap;
	}
	
	public List<Object[]> findEngineHostMto(int start, int end) {
		Parameters param = Parameters.with("1", start);
		param.put("2", end);
		return super.executeNative(param,FIND_HOST_MTOC_ENGINE);
	}
	/**
	 * Native query used to update the primary key
	 */
	@Transactional
	public void updateEngineSpecCode(EngineSpec engineSpec, String oldSpecCode){
		Parameters param = Parameters.with("1", engineSpec.getProductSpecCode());
		param.put("2", engineSpec.getModelYearCode()).put("3", engineSpec.getModelCode());
		param.put("4", engineSpec.getModelTypeCode()).put("5", engineSpec.getModelOptionCode()).put("6", oldSpecCode);
		executeNative(ENGINE_SPEC_CODE_UPDATE, param);
	}
	
	@Override
	public EngineSpec findByProductId(String productId) {
		return findFirstByQuery(FIND_BY_PRODUCT_ID, Parameters.with("productId", productId));
	}
}
