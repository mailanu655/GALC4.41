package com.honda.galc.dao.jpa.conf;


import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.PrintAttributeFormatDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.conf.PrintAttributeFormatId;
import com.honda.galc.entity.enumtype.PrintAttributeType;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>PrintAttributeFormatDaoImpl Class description</h3>
 * <p> PrintAttributeFormatDaoImpl description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Dec 12, 2010
 *
 *
 */
public class PrintAttributeFormatDaoImpl extends BaseDaoImpl<PrintAttributeFormat, PrintAttributeFormatId> implements PrintAttributeFormatDao{

	private String FIND_BY_LAYER_ID = "select distinct p.* from feature_tbx f, layer_features_tbx lf, gal258tbx p where f.feature_id = lf.feature_id and rtrim(f.feature_type) || '.' || f.reference_type = p.formid and lf.layer_id = ?1";
	private String FIND_BY_FEATURE_ID = "select distinct p.* from feature_tbx f, gal258tbx p where rtrim(f.feature_type) || '.' || f.reference_type = p.formId and f.feature_id = ?1";
	private String FIND_DEFECT_ATTRIBUTES = "select p from PrintAttributeFormat p where p.id.formId like 'DEFECT%'";
	private String FIND_BY_FORMID_AND_LENGTH_RANGE = "select p from PrintAttributeFormat p where p.id.formId = :formId and p.length >= :minValue and p.length <= :maxValue";
	private String FIND_BY_FORM_ID_ORDER_BY_LENGTH = "select p from PrintAttributeFormat p where p.id.formId = :formId order by p.length";
	private String FIND_ALL_PRE_DEFINED = "select distinct attribute_value from galadm.gal258tbx where formid=?1 and attribute_type = ?2 ";

	public List<PrintAttributeFormat> findAllByFormId(String formId) {
		
		return findAll( Parameters.with("id.formId",formId),new String[]{"sequenceNumber"},true);
		
	}

	@Transactional
	public void removeAllByFormId(String formId) {
		delete(Parameters.with("id.formId", formId));
	}

	public String findByNativeQuery(String sqlStatement) {
		
		Object obj = this.findFirstByNativeQuery(sqlStatement, null, Object.class);

        return obj == null ? null : obj.toString();
        
	}
	
	public List<Map<String, Object>> findMapCollection(String sqlStatement) {
		return findResultMapByNativeQuery(sqlStatement, null);
	}
	
	public List<PrintAttributeFormat> findDefectPrintAttributes()
	{
		List<PrintAttributeFormat> objects = findAllByQuery(FIND_DEFECT_ATTRIBUTES);
		for(PrintAttributeFormat paf : objects)
		{
			if(paf.getAttributeType() == PrintAttributeType.SQL)
				paf.setAttributeValue(findByNativeQuery(paf.getAttributeValue()));
		}
		return objects == null ? null : objects;
		
	}
	
	public List<PrintAttributeFormat> findAllByFeatureId(String featureId, String referenceId) {
		Parameters params = Parameters.with("1", featureId);
		List<PrintAttributeFormat> objects = findAllByNativeQuery(FIND_BY_FEATURE_ID, params);
		
		for(PrintAttributeFormat paf : objects)
		{
			if(paf.getAttributeType() == PrintAttributeType.SQL)
				paf.setAttributeValue(findByNativeQuery(paf.getAttributeValue().replace("{REFERENCE_ID}", referenceId).replace("{FEATURE_ID}", featureId)));
		}
		return objects == null ? null : objects;
	}

	public List<PrintAttributeFormat> findAllByLayerId(String layerId) {
		Parameters params = Parameters.with("1", layerId);
		List<PrintAttributeFormat> objects = findAllByNativeQuery(FIND_BY_LAYER_ID, params);
		
		for(PrintAttributeFormat paf : objects)
		{
			if(paf.getAttributeType() == PrintAttributeType.SQL)
				paf.setAttributeValue(findByNativeQuery(paf.getAttributeValue()));
		}
		return objects == null ? null : objects;
	}

	public List<PrintAttributeFormat> findByFormIdAndLengthRange(String formId, Integer minValue, Integer maxValue) {
		return findAllByQuery(FIND_BY_FORMID_AND_LENGTH_RANGE, Parameters.with("formId", formId).put("minValue", minValue).put("maxValue", maxValue));
	}

	public Integer[] findLengthRangeByFormId(String formId) {
		List<PrintAttributeFormat> formats=findAllByQuery(FIND_BY_FORM_ID_ORDER_BY_LENGTH, Parameters.with("formId", formId));
		if(null==formats||formats.isEmpty()){
			return null;
		}
		Integer[] lengthRange=new Integer[2];
		lengthRange[0]=formats.get(0).getLength();
		lengthRange[1]=formats.get(formats.size()-1).getLength();
		return lengthRange;
	}

	@Transactional
	public void removeBySequenceNumber(String formId, int sequenceNumber) {
		Parameters params = Parameters.with("id.formId", formId);
		params.put("sequenceNumber", sequenceNumber);
		 
		delete(params);
		
	}

	public List<String> findAllPredefined(String formId, Integer attributeType) {
		Parameters params = Parameters.with("1", formId);
		params.put("2", attributeType);
		return findAllByNativeQuery(FIND_ALL_PRE_DEFINED, params, String.class);
	}

}
