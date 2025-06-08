package com.honda.galc.service;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.PrintAttributeFormatDao;
import com.honda.galc.dao.product.FeatureDao;
import com.honda.galc.entity.product.Feature;
import com.honda.galc.entity.product.FeaturePoint;
import com.honda.galc.entity.product.FeaturePointId;

public class LineVisualizationServiceImpl implements LineVisualizationService {

	private int DEFAULT_STALE_TOLERANCE = 60000;
	
	private ConcurrentHashMap<String, Date> _lastUpdatedMap = new ConcurrentHashMap<String, Date>();
	private ConcurrentHashMap<String, List<Feature>> _currentLineList = new ConcurrentHashMap<String, List<Feature>>();
	
	private int _asOfYear = 70;
	private int _asOfMonth = 1;
	private int _asOfDay = 1;
	private int _asOfHour = 0;
	private int _asOfMinute = 0;
	private int _asOfSecond = 0;
	
	private FeatureDao _featureDao;
	private PrintAttributeFormatDao _printAttributeFormatDao;
	
	public List<Feature> getLineVisualization(String plantName, String lineId, String featurePrefix, String trackingLayer) {
		refresh(plantName, lineId, featurePrefix, trackingLayer);
		return getLineCurrentList(plantName, lineId);
	}
	
	private List<Feature> getLineCurrentList(String plantName, String lineId) {
		return _currentLineList.get(plantName+lineId);
	}
	
	private void getLineList(String plantName, String lineId, String featurePrefix, String trackingLayer)
	{
		List<Feature> lineList = new ArrayList<Feature>();
		
		try {
			ConcurrentHashMap<String, String> bodyLocations = new ConcurrentHashMap<String, String>();
			List<Object[]> lineData = getFeatureDao().getLineData(lineId, trackingLayer);			
			List<Feature> trackingPoints = getFeatureDao().getFeaturesByLayerId(trackingLayer);
			List<Feature> locations = getFeatureDao().getLineLocations(featurePrefix);
			HashMap<String, String> trackingKey = new HashMap<String, String>();
			String lastKDLot = "";
			for(Feature feature : trackingPoints)
			{
				trackingKey.put(feature.getReferenceId(), feature.getFeaturePoints().get(0).getChildFeatureId());
			}
			
			int count = 0;
			for(Feature location : locations)
			{
				if(count >= lineData.size())
					break;
				
				String processPoint = getString(lineData.get(count)[3]).trim();
				if(processPoint.length() == 0)
					count++;
				
				processPoint = getString(lineData.get(count)[3]).trim();
				if(Integer.parseInt(trackingKey.get(processPoint).substring(17)) > Integer.parseInt(location.getReferenceId()))
				{
					bodyLocations.put(location.getFeatureId(), "");
					
				}
				else
				{
					Feature vinFeature = new Feature();
					vinFeature.setFeatureId(getString(lineData.get(count)[1]).trim());
					if(count < lineData.size() - 1)
					{
						if(!getString(lineData.get(count+1)[5]).trim().equals(getString(lineData.get(count)[5]).trim()))
							vinFeature.setReferenceId(Integer.toString(getInt(lineData.get(count)[4])));
						else
							vinFeature.setReferenceId("");
					}
					vinFeature.setReferenceType(trackingLayer);
					vinFeature.setFeatureType("POINT");
					List<FeaturePoint> featurePoints = new ArrayList<FeaturePoint>();
					FeaturePoint featurePoint = new FeaturePoint();
					FeaturePointId featurePointId = new FeaturePointId();
					featurePointId.setFeatureId(getString(lineData.get(count)[1]).trim());
					featurePointId.setFeatureSeq(0);
					featurePoint.setId(featurePointId);
					featurePoint.setXCoordinate((location.getFeaturePoints().get(0).getXCoordinate() + location.getFeaturePoints().get(1).getXCoordinate())/2);
					featurePoint.setYCoordinate((location.getFeaturePoints().get(1).getYCoordinate() + location.getFeaturePoints().get(2).getYCoordinate())/2);
					featurePoint.setZCoordinate(5);
					featurePoints.add(featurePoint);
					vinFeature.setFeaturePoints(featurePoints);
					vinFeature.setAttributes(getPrintAttributeFormatDao().findAllByFeatureId("STYLE_POINT", vinFeature.getFeatureId()));
					lineList.add(vinFeature);
					bodyLocations.put(location.getFeatureId(), getString(lineData.get(count)[1]).trim());
					lastKDLot = getString(lineData.get(count)[5]).trim();
					count++;
					
				}
				
			}
			
			setLineCurrentList(plantName, lineId, lineList);

		}                                                                                                                                                                                                                                                                                                                                                                                       
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void setLineCurrentList(String plantName, String lineId, List<Feature> lineList) {
		_currentLineList.put(plantName+lineId, lineList);
	}

	private void refresh(String plantName, String lineId, String featurePrefix, String trackingLayer) {
		refresh(DEFAULT_STALE_TOLERANCE, plantName, lineId, featurePrefix, trackingLayer);
	}
	
	private synchronized void refresh(int staleTolerance, String plantName, String lineId, String featurePrefix, String trackingLayer) {
		if ((getLastUpdated(plantName, lineId).getTime() + staleTolerance) <= new Date().getTime()) {
			try {
				getLineList(plantName, lineId, featurePrefix, trackingLayer);
				getLogger().debug("Updated Line Visualization for plant: " + plantName + " and line: " + lineId);
				setLastUpdated(plantName, lineId, new Date());
			} catch(Exception ex) {
				ex.printStackTrace();
				getLogger().error("Unable to refresh LineVisualization data: " + (ex.getMessage() == null ? "" : ex.getMessage()));
			}
		} else {
			getLogger().debug("Skipping LineVisualization update. Last Updated at: " + getLastUpdated(plantName, lineId).toString());
		}
	}
	
	public void setLastUpdated(String plantName, String lineId, Date asOfDate) {
		String key = StringUtils.trimToEmpty(plantName+lineId);
		_lastUpdatedMap.put(key, asOfDate);

		// last updated date broken down by time units
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy:MM:dd:HH:mm:ss");
		String[] tokens = dateFormat.format(asOfDate).split(":");
		setAsOfYear(new Integer(tokens[0]));
		setAsOfMonth(new Integer(tokens[1]));
		setAsOfDay(new Integer(tokens[2]));
		setAsOfHour(new Integer(tokens[3]));
		setAsOfMinute(new Integer(tokens[4]));
		setAsOfSecond(new Integer(tokens[5]));
	}
	
	public Date getLastUpdated(String plantName, String lineId) {
		String key = StringUtils.trimToEmpty(plantName + lineId);
		if (!_lastUpdatedMap.containsKey(key)) {
			_lastUpdatedMap.put(key, new Date(0));
		}
		return _lastUpdatedMap.get(key);
	}
	
	public int getAsOfSecond() {
		return _asOfSecond;
	}
	
	private void setAsOfSecond(int asOfSecond) {
		_asOfSecond = asOfSecond;
	}
	
	public int getAsOfMinute() {
		return _asOfMinute;
	}
	
	private void setAsOfMinute(int asOfMinute) {
		_asOfMinute = asOfMinute;
	}
	
	public int getAsOfHour() {
		return _asOfHour;
	}
	
	private void setAsOfHour(int asOfHour) {
		_asOfHour = asOfHour;
	}
	
	public int getAsOfDay() {
		return _asOfDay;
	}
	
	private void setAsOfDay(int asOfDay) {
		_asOfDay = asOfDay;
	}

	public int getAsOfMonth() {
		return _asOfMonth;
	}
	
	private void setAsOfMonth(int asOfMonth) {
		_asOfMonth = asOfMonth;
	}
	
	public int getAsOfYear() {
		return _asOfYear;
	}
	
	private void setAsOfYear(int asOfYear) {
		_asOfYear = asOfYear;
	}
	
	private FeatureDao getFeatureDao() {
		if(_featureDao == null)
			_featureDao = ServiceFactory.getDao(FeatureDao.class);
		return _featureDao;
	}
	
	private PrintAttributeFormatDao getPrintAttributeFormatDao()
	{
		if(_printAttributeFormatDao == null)
			_printAttributeFormatDao = ServiceFactory.getDao(PrintAttributeFormatDao.class);
		return _printAttributeFormatDao;
	}
	
    private String getString(Object val) {
    	if (val != null) {
    		return ((String) val).trim();
    	}
    	return "";
    }
    
    protected int getInt(Object val) {
    	if (val != null) {
    		if (val instanceof Integer)
    			return (Integer) val;
    		if (val instanceof BigDecimal)
    			return ((BigDecimal) val).intValue();
    		if (val instanceof Double)
    			return ((Double) val).intValue();
    		if (val instanceof String)
    			return Integer.parseInt((String) val);
    	} else {
    		return 0;
    	}
    	return 0;
    }

}
