package com.honda.galc.locationTracking.client;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.gwtopenmaps.openlayers.client.LonLat;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.*;
import com.honda.galc.locationTracking.shared.Feature;
import com.honda.galc.locationTracking.shared.FeaturePoint;

@Connection(name="LocationDatabase", version="1.0", description="Location Database", maxsize=100000)
public interface MapClientDataService extends DataService {

	@Update("CREATE TABLE IF NOT EXISTS FEATURE_POINTS_TBX ("
			+ "FEATURE_ID TEXT NOT NULL, "
			+ "FEATURE_SEQ INTEGER NOT NULL, "
			+ "X_COORDINATE REAL NOT NULL, "
			+ "Y_COORDINATE REAL NOT NULL, "
			+ "Z_COORDINATE REAL NOT NULL, "
			+ "CREATE_TIMESTAMP, "
			+ "UPDATE_TIMESTAMP)")
	void initFeaturePoints(VoidCallback callback);
	
	@Update("CREATE TABLE IF NOT EXISTS FEATURE_TBX ("
			+ "FEATURE_ID TEXT NOT NULL, "
			+ "FEATURE_TYPE TEXT NOT NULL, "
			+ "REFERENCE_ID TEXT NOT NULL, "
			+ "REFERENCE_TYPE TEXT NOT NULL, "
			+ "ENABLE_HISTORY INTEGER NOT NULL, "
			+ "CREATE_TIMESTAMP, "
			+ "UPDATE_TIMESTAMP)")
	void initFeature(VoidCallback callback);

		
	@Update(sql="INSERT INTO FEATURE_TBX (FEATURE_ID, FEATURE_TYPE, REFERENCE_ID, REFERENCE_TYPE, ENABLE_HISTORY) VALUES ({_.getFeatureId()}, {_.getFeatureType()}, {_.getReferenceId()}, {_.getReferenceType()}, {_.getEnableHistory()})", foreach="prod")
	void insertProducts(Collection<Feature> prod, RowIdListCallback callback);
	
	@Update("INSERT INTO FEATURE_TBX (FEATURE_ID, FEATURE_TYPE, REFERENCE_ID, REFERENCE_TYPE, ENABLE_HISTORY) VALUES ({prod.getFeatureId()}, {prod.getFeatureType()}, {prod.getReferenceId()}, {prod.getReferenceType()}, {prod.getEnableHistory()})")
	void insertProduct(Feature prod, RowIdListCallback callback);
	
	@Update(sql="INSERT INTO FEATURE_POINTS_TBX (FEATURE_ID, FEATURE_SEQ, X_COORDINATE, Y_COORDINATE, Z_COORDINATE) VALUES ({_.getId().getFeatureId()}, {_.getId().getFeatureSeq()}, {_.getXCoordinate()}, {_.getYCoordinate()}, {_.getZCoordinate()})", foreach="point")
	void insertProductLocations(Collection<FeaturePoint> point, RowIdListCallback callback);
	
	@Update("INSERT INTO FEATURE_POINTS_TBX (FEATURE_ID, FEATURE_SEQ, X_COORDINATE, Y_COORDINATE, Z_COORDINATE, CHILD_FEATURE_ID, SPATIAL_REFERENCE_SYSTEM) VALUES ({point.getId().getFeatureId()}, {point.getId().getFeatureSeq()}, {point.getXCoordinate()}, {point.getYCoordinate()}, {point.getZCoordinate()}, {point.getChildFeatureId()}, {point.getSpatialReferenceSystem()})")
	void insertProductLocation(FeaturePoint point, RowIdListCallback callback);

	@Select("SELECT * FROM FEATURE_POINTS_TBX")
	void getAllVINs(ListCallback<GenericRow> callback);
	
	@Select("SELECT * FROM FEATURE_POINTS_TBX WHERE FEATURE_ID IN({filterValues})")
	void getVIN(List<String> filterValues, ListCallback<GenericRow> callback);
	
	@Select("SELECT * FROM FEATURE_POINTS_TBX WHERE FEATURE_SEQ = 1")
	void getUnsavedVINs(ListCallback<GenericRow> callback);
	
	@Update("DELETE FROM FEATURE_POINTS_TBX WHERE FEATURE_ID = {prod.getFeatureId()}")
	void clearVIN(Feature prod, VoidCallback callback);
	
	@Update("DELETE FROM FEATURE_POINTS_TBX")
	void clearVINs(VoidCallback callback);
}
