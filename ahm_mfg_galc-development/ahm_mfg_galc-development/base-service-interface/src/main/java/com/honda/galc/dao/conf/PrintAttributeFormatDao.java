package com.honda.galc.dao.conf;


import java.util.List;
import java.util.Map;

import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.conf.PrintAttributeFormatId;
import com.honda.galc.service.IDaoService;

public interface PrintAttributeFormatDao extends IDaoService<PrintAttributeFormat, PrintAttributeFormatId> {
	
	public List<PrintAttributeFormat> findAllByFormId(String formId);
	
	public void removeAllByFormId(String formId);
	
	public List<PrintAttributeFormat> findAllByFeatureId(String featureId, String referenceId);
	
	public List<PrintAttributeFormat> findAllByLayerId(String layerId);
	
	public List<PrintAttributeFormat> findDefectPrintAttributes();
	
	/**
	 * run the native query to get the needed print attribute
	 * @param sqlStatement
	 * @return
	 */
	public String findByNativeQuery(String sqlStatement);
	
	public List<Map<String, Object>> findMapCollection(String sqlStatement);
	
	/**
	 * We reused the GAL258TBX to store the Shim-ID-Size mapping. Shim ID stored in Attribute, and Shim Size stored in
	 * Length. Here we will find the records whose lengths are between the minValue and maxValue.
	 * 
	 * @param formId
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public List<PrintAttributeFormat> findByFormIdAndLengthRange(String formId, Integer minValue, Integer maxValue);
	
	/**
	 * Find the min and max lengths under a given format ID.
	 * @param formId
	 * @return
	 */
	public Integer[] findLengthRangeByFormId(String formId);

	/**
	 * Remove the enity with the sequence number
	 * @param formatId 
	 * @param sequenceNumber
	 */
	public void removeBySequenceNumber(String formId, int sequenceNumber);

	/**
	 * Find all pred-defined SQLs for the given formId and attribute type
	 * @param formId
	 * @param attributeType
	 * @return
	 */
	public List<String> findAllPredefined(String formId, Integer attributeType);

	
	
}
