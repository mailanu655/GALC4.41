/**
 * 
 */
package com.honda.galc.service;

import java.util.ArrayList;

import com.honda.galc.entity.product.FactoryNewsCurrent;

/**
 * @author Subu Kathiresan
 * @Date Jun 7, 2012
 *
 */
public interface FactoryNewsUpdateService extends IService {
	
	public int getAsOfSecond();

	public int getAsOfMinute();
	
	public int getAsOfHour();

	public int getAsOfDay();

	public int getAsOfMonth();

	public int getAsOfYear();
	
	public int getCurrentInventory(String lineId);
	
	public int getCurrentInventory(String lineId, String plantName);
	
	public int getCurrentInventory(String lineId, int staleTolerance);
	
	public int getCurrentInventory(String lineId, String plantName, int staleTolerance);
	
	public int getAgedInventory(String lineId, int ageInMins);
	
	public int getAgedInventory(String lineId, int ageInMins, int staleTolerance);
	
	public int getTotalInventory(String divisionId);
	
	public int getTotalInventory(String divisionId, String plantName);
	
	public int getTotalInventory(String divisionId, int staleTolerance);
	
	public int getTotalInventory(String divisionId, String plantName, int staleTolerance);
	
	public ArrayList<FactoryNewsCurrent> getFactoryNews(String plantName);
}
