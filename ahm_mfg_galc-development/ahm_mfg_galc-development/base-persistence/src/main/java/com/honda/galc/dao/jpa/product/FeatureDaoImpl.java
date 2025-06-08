package com.honda.galc.dao.jpa.product;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.FeatureDao;
import com.honda.galc.entity.product.Feature;
import com.honda.galc.service.Parameters;


public class FeatureDaoImpl extends BaseDaoImpl<Feature, String> implements FeatureDao {
	
	private static final String FIND_BY_LAYER_SQL = "select f from Feature f, LayerFeatures lf where f.featureId = lf.id.featureId and lf.id.layerId = :layerId and lf.visible = 1";
	private static final String FIND_BY_ID_SQL = "select f from Feature f, LayerFeatures lf where f.featureId = lf.id.featureId and lf.id.featureId = :featureId";
	private static final String FIND_LOCATIONS = "SELECT FEATURE_TBX.* FROM LAYER_FEATURES_TBX LEFT OUTER JOIN FEATURE_TBX ON " +
		"LAYER_FEATURES_TBX.FEATURE_ID = FEATURE_TBX.FEATURE_ID WHERE LAYER_FEATURES_TBX.LAYER_ID = ?1 ORDER BY INTEGER(REFERENCE_ID) WITH CS FOR READ ONLY";
	
	private static String GET_LINE_DATA = "WITH SortedList (PRODUCT_ID, NEXT_PRODUCT_ID, Level) " +
     "AS (SELECT TAIL.PRODUCT_ID, TAIL.NEXT_PRODUCT_ID, 1 AS Level " +
     "      FROM GAL176TBX TAIL " +
     "     WHERE TAIL.NEXT_PRODUCT_ID IS NULL AND tail.LINE_ID = ?1 " +
     "    UNION ALL " +
     "    SELECT ll.PRODUCT_ID, ll.NEXT_PRODUCT_ID, s.Level + 1 AS Level " +
     "      FROM GAL176TBX ll, SortedList AS s " +
     "     WHERE ll.NEXT_PRODUCT_ID = s.PRODUCT_ID AND ll.LINE_ID = ?1) " +
     "	SELECT t.Level, t.PRODUCT_ID, t.ACTUAL_TIMESTAMP, t.PROCESS_POINT_ID, t.AF_ON_SEQUENCE_NUMBER, t.PROD_LOT_KD " +
     "	FROM (SELECT rownumber () " +
     "          OVER (PARTITION BY history.PRODUCT_ID " +
     "                ORDER BY Level DESC, ALL_SEQ_NUM DESC) " +
     "             AS rownum, " +
     "          SortedList.Level, " +
     "          SortedList.PRODUCT_ID, " +
     "          history.ACTUAL_TIMESTAMP, " +
     "          history.PROCESS_POINT_ID, " +
     "          frame.AF_ON_SEQUENCE_NUMBER, " +
     "          productionLots.PROD_LOT_KD " +
     "     FROM SortedList " +
     "          LEFT JOIN GALADM.GAL215TBX history " +
     "             ON     SortedList.PRODUCT_ID = history.PRODUCT_ID " +
     "          LEFT JOIN (SELECT P.PROCESS_POINT_ID, " +
     "                            P.PROCESS_POINT_NAME, " +
     "                                (COALESCE (D.SEQUENCE_NUMBER, 0) + 1) " +
     "                              * 1000000 " +
     "                            +   (  COALESCE (L.LINE_SEQUENCE_NUMBER, 0) " +
     "                                 + 1) " +
     "                              * 1000 " +
     "                            + (COALESCE (P.SEQUENCE_NUMBER, 0) + 1) " +
     "                               ALL_SEQ_NUM " +
     "                       FROM GALADM.GAL214TBX P " +
     "                            LEFT JOIN GALADM.GAL195TBX L " +
     "                               ON P.LINE_ID = L.LINE_ID " +
     "                            LEFT JOIN GALADM.GAL128TBX D " +
     "                               ON L.DIVISION_ID = D.DIVISION_ID " +
     "                     ORDER BY D.SEQUENCE_NUMBER, " +
     "                              L.LINE_SEQUENCE_NUMBER, " +
     "                              P.SEQUENCE_NUMBER) process " +
     "             ON process.PROCESS_POINT_ID = history.PROCESS_POINT_ID " +
     "             LEFT JOIN GALADM.GAL143TBX frame " +
     "             ON SortedList.PRODUCT_ID = frame.PRODUCT_ID " +
     "             LEFT JOIN GALADM.GAL217TBX productionLots " +
     "             ON frame.PRODUCTION_LOT = productionLots.PRODUCTION_LOT " +
     "             WHERE history.PROCESS_POINT_ID IN " +
     "             (SELECT REFERENCE_ID FROM LAYER_FEATURES_TBX LEFT JOIN " +
     "  		    FEATURE_TBX ON LAYER_FEATURES_TBX.FEATURE_ID = FEATURE_TBX.FEATURE_ID " +
     "				WHERE LAYER_ID = ?2) " +
     "   ORDER BY Level DESC) t " +
     "WHERE t.rownum = 1 " +
     "ORDER BY 1 DESC " +
     "WITH CS FOR READ ONLY";
	
	public List<Feature> getFeaturesById(String featureId)
	{
		Parameters params = Parameters.with("featureId", featureId);
		List<Feature> objects = findAllByQuery(FIND_BY_ID_SQL, params);
		return objects == null ? null : objects;
	}
	
	public List<Feature> getFeaturesByLayerId(String layerId)
	{
		Parameters params = Parameters.with("layerId", layerId);
		List<Feature> objects = findAllByQuery(FIND_BY_LAYER_SQL, params);
		return objects == null ? null : objects;
		
	}
	
	public List<Feature> getLineLocations(String featurePrefix)
	{
		Parameters params = Parameters.with("1", featurePrefix);
		List<Feature> objects = findAllByNativeQuery(FIND_LOCATIONS, params);
		return objects == null ? null : objects;
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<Object[]> getLineData(String lineId, String trackingLayer) {
		
		getLogger().info("About to execute getLineData() query");
		Parameters parameters =  new Parameters();
		parameters.put("1", lineId.trim());
		parameters.put("2", trackingLayer.trim());
		List<Object[]> lineData = findAllByNativeQuery(GET_LINE_DATA, parameters, Object[].class);
		getLogger().info("Finished executing getLineData() query");

		return lineData;
	}
	
	public List<Feature> findAllFeatures(String featureType){
		List<Feature> featureIdData = findAll(Parameters.with("featureType", featureType)); 
		return featureIdData;
	}

}
