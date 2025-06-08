/**
 * 
 */
package com.honda.galc.service.metric;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.product.FactoryNewsCurrent;
import com.honda.galc.service.FactoryNewsUpdateService;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @Date Jun 7, 2012
 *
 */
public class FactoryNewsUpdateServiceImpl implements FactoryNewsUpdateService {
	
	private int DEFAULT_STALE_TOLERANCE = 30000;
	private LineDao _lineDao;
	private DivisionDao _divisionDao;
	private GpcsDivisionDao _gpcsDivisionDao;
	
	private Hashtable<String, Integer> _inventoryLineMap = new Hashtable<String, Integer>();
	private Hashtable<String, Integer> _agedInventoryLineMap = new Hashtable<String, Integer>();
	private Hashtable<String, Integer> _inventoryDivMap = new Hashtable<String, Integer>();
	private ArrayList<FactoryNewsCurrent> _factoryNewsCurrentList = new ArrayList<FactoryNewsCurrent>();
	private ConcurrentHashMap<String, Date> _lastUpdatedMap = new ConcurrentHashMap<String, Date>();
	private ConcurrentHashMap<String, String> _gpcsPlantCodeMap = new ConcurrentHashMap<String, String>();
	
	private int _asOfYear = 70;
	private int _asOfMonth = 1;
	private int _asOfDay = 1;
	private int _asOfHour = 0;
	private int _asOfMinute = 0;
	private int _asOfSecond = 0;

	private void refresh(String plantName) {
		refresh(DEFAULT_STALE_TOLERANCE, plantName);
	}
	
	private synchronized void refresh(int staleTolerance, String plantName) {
		if ((getLastUpdated(plantName).getTime() + staleTolerance) <= new Date().getTime()) {
			try {
				setFactoryNewsCurrentList(getLineDao().getFactoryNews(plantName, getGpcsPlantCode(plantName)));
				buildInventoryMaps();
				for(FactoryNewsCurrent fnc:  getFactoryNewsCurrentList()) {
					setLastUpdated(plantName, fnc.getAsOfDate());
					break;
				}
				getLogger().debug("Updated FactoryNews");
			} catch(Exception ex) {
				ex.printStackTrace();
				getLogger().error("Unable to refresh FactoryNews data: " + (ex.getMessage() == null ? "" : ex.getMessage()));
			}
		} else {
			getLogger().debug("Skipping FactoryNews update. Last Updated at: " + getLastUpdated(plantName).toString());
		}
	}
	
	private synchronized void refreshAgedInventory(int staleTolerance, String lineId, int ageInMins) {
		String inventoryKey = lineId.trim() + "-" + ageInMins;
		if ((getLastUpdated(inventoryKey).getTime() + staleTolerance) <= new Date().getTime()) {
			try {
				getAgedInventoryLineMap().put(inventoryKey, getLineDao().getAgedInventory(lineId, ageInMins));
				setLastUpdated(inventoryKey, new Date());
				getLogger().debug("Updated agedInventory for " + inventoryKey);
			} catch(Exception ex) {
				ex.printStackTrace();
				getLogger().error("Unable to refresh agedInventory data: " + (ex.getMessage() == null ? "" : ex.getMessage()));
			}
		} else {
			getLogger().debug("Skipping agedInventory update. Last Updated at: " + getLastUpdated(inventoryKey).toString());
		}
	}
	
	private void buildInventoryMaps() {
		Hashtable<String, Integer> inventoryLineMap = new Hashtable<String, Integer>();
		Hashtable<String, Integer> inventoryDivMap = new Hashtable<String, Integer>();
		for(FactoryNewsCurrent fnc:  getFactoryNewsCurrentList()) {
			// line inventory
			if (!inventoryLineMap.containsKey(fnc.getLineId())) {
				inventoryLineMap.put(fnc.getLineId(), fnc.getCurrentInventory());
			}
			// division inventory
			if (!inventoryDivMap.containsKey(fnc.getDivisionId())) {
				inventoryDivMap.put(fnc.getDivisionId(), fnc.getCurrentInventory());
			} else {
				int currentTotal = inventoryDivMap.get(fnc.getDivisionId());
				inventoryDivMap.put(fnc.getDivisionId(), currentTotal + fnc.getCurrentInventory());
			}
		}
		setInventoryLineMap(inventoryLineMap);
		setInventoryDivMap(inventoryDivMap);
	}
	
	private Hashtable<String, Integer> getInventoryLineMap() {
		return _inventoryLineMap;
	}
	
	private void setInventoryLineMap(Hashtable<String, Integer> map) {
		_inventoryLineMap = map;
	}
	
	private Hashtable<String, Integer> getAgedInventoryLineMap() {
		return _agedInventoryLineMap;
	}
	
	private Hashtable<String, Integer> getInventoryDivMap() {
		return _inventoryDivMap;
	}
	
	private void setInventoryDivMap(Hashtable<String, Integer> map) {
		_inventoryDivMap = map;
	}

	public void setLastUpdated(String inventoryKey, Date asOfDate) {
		String key = StringUtils.trimToEmpty(inventoryKey);
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
	
	public Date getLastUpdated(String inventoryKey) {
		String key = StringUtils.trimToEmpty(inventoryKey);
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
	
	public int getCurrentInventory(String lineId) {
		return getCurrentInventory(lineId, DEFAULT_STALE_TOLERANCE);
	}
	
	public int getCurrentInventory(String lineId, String plantName) {
		return getCurrentInventory(lineId, plantName, DEFAULT_STALE_TOLERANCE);
	}

	public int getCurrentInventory(String lineId, int staleTolerance) {
		return getCurrentInventory(lineId, getPlantNameFromLine(lineId), staleTolerance);
	}
	
	public int getCurrentInventory(String lineId, String plantName, int staleTolerance) {
		refresh(staleTolerance, plantName);
		if (getInventoryLineMap().containsKey(lineId.trim()))
			return getInventoryLineMap().get(lineId.trim());
		
		return -1;
	}

	public int getAgedInventory(String lineId, int ageInMins) {
		return getAgedInventory(lineId, ageInMins, DEFAULT_STALE_TOLERANCE);
	}

	public int getAgedInventory(String lineId, int ageInMins, int staleTolerance) {
		refreshAgedInventory(staleTolerance, lineId, ageInMins);
		if (getAgedInventoryLineMap().containsKey(lineId.trim() + "-" + ageInMins))
			return getAgedInventoryLineMap().get(lineId.trim() + "-" + ageInMins);
		
		return -1;
	}
	
	public int getTotalInventory(String divId) {
		return getTotalInventory(divId, DEFAULT_STALE_TOLERANCE);
	}
	
	public int getTotalInventory(String divId, String plantName) {
		return getTotalInventory(divId, getPlantNameFromDiv(divId), DEFAULT_STALE_TOLERANCE);
	}
	
	public int getTotalInventory(String divId, int staleTolerance) {
		return getTotalInventory(divId, getPlantNameFromDiv(divId), staleTolerance);
	}
	
	public int getTotalInventory(String divId, String plantName, int staleTolerance) {
		refresh(staleTolerance, plantName);
		if (getInventoryDivMap().containsKey(divId.trim()))
			return getInventoryDivMap().get(divId.trim());
		
		return -1;
	}

	public ArrayList<FactoryNewsCurrent> getFactoryNews(String plantName) {
		refresh(plantName);
		return getFactoryNewsCurrentList();
	}
	
	public String getGpcsPlantCode(String plantName) {
		if (!getGpcsPlantCodeMap().containsKey(plantName)) {
			String gpcsPlantCode = StringUtils.trimToEmpty(getGpcsDivisionDao().getGpcsPlantCode(plantName));
			getGpcsPlantCodeMap().put(plantName, gpcsPlantCode);
		}
		return getGpcsPlantCodeMap().get(plantName);
	}
	
	private ArrayList<FactoryNewsCurrent> getFactoryNewsCurrentList() {
		return _factoryNewsCurrentList;
	}

	private void setFactoryNewsCurrentList(ArrayList<FactoryNewsCurrent> factoryNewsCurrentList) {
		_factoryNewsCurrentList = factoryNewsCurrentList;
	}

	private ConcurrentHashMap<String, String> getGpcsPlantCodeMap() {
		return _gpcsPlantCodeMap;
	}

	private String getPlantNameFromLine(String lineId) {
		Line line = getLineDao().findByKey(lineId);
		return line.getPlantName();
	}
	
	private String getPlantNameFromDiv(String divId) {
		Division division = getDivisionDao().findByKey(divId);
		return division.getPlantName();
	}
	
	private LineDao getLineDao() {
		if(_lineDao == null)
			_lineDao = ServiceFactory.getDao(LineDao.class);
		return _lineDao;
	}
	
	private DivisionDao getDivisionDao() {
		if(_divisionDao == null)
			_divisionDao = ServiceFactory.getDao(DivisionDao.class);
		return _divisionDao;
	}
	
	private GpcsDivisionDao getGpcsDivisionDao() {
		if(_gpcsDivisionDao == null)
			_gpcsDivisionDao = ServiceFactory.getDao(GpcsDivisionDao.class);
		return _gpcsDivisionDao;
	}
}