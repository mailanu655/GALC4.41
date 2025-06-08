package com.honda.galc.client.datacollection.observer;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.cache.PersistentCache;
import com.honda.galc.entity.product.InstalledPart;
/**
 * 
 * <h3>InstalledPartCache</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> InstalledPartCache description </p>
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
 * @author Paul Chou
 * May 6, 2010
 *
 */
public class InstalledPartCache extends PersistentCache{
	private static final Integer INSTALLED_PARTS_LAST_KEY = Integer.MIN_VALUE;
	private static InstalledPartCache INSTANCE;
	private InstalledPartCache() {
		super();
	}
	
	public  static InstalledPartCache getInstance(){
		if(INSTANCE == null)
			INSTANCE = new InstalledPartCache();
		
		return INSTANCE;
	}
	
	public void saveInstalledPart(List<InstalledPart> installedParts){
		int lastIndex = getLastIndex() + 1;
		put(lastIndex, installedParts);
		put(INSTALLED_PARTS_LAST_KEY, lastIndex);
		Logger.getLogger().info("Saved data into local cache.  Key:" + lastIndex);
	}
	
	public boolean isEmpty(){
		return getSize() == 0;
	}

	@SuppressWarnings("unchecked")
	public List<InstalledPart> getDuplicatedParts(String partName, String partNumber) {
		List<InstalledPart> list = new ArrayList<InstalledPart>();
		
		for(Object key : getKeys()){
			List<InstalledPart> installedParts = get(key, List.class);
			
			if(installedParts == null) continue;
			
			for( InstalledPart part: installedParts){
				
				if(partNumber.equals(part.getPartSerialNumber()) &&
						partName.equals(part.getId().getPartName()))
					list.add(part);
			}
			
		}
		return list;
	}
	
	public int getLastIndex(){
		return getIntValue(INSTALLED_PARTS_LAST_KEY) == null ? 0 : getIntValue(INSTALLED_PARTS_LAST_KEY);
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getKeys() {
		List<Integer> keys = new ArrayList<Integer>();
		keys.addAll(super.getKeys());
		if (keys.size() > 0) {
			if(keys.contains(INSTALLED_PARTS_LAST_KEY)) keys.remove(INSTALLED_PARTS_LAST_KEY);
		 }
		
		 return keys;
	}

	public int getSize() {
		return getKeys().size();
	}
	
	public void resetLastIndex(){
		put(INSTALLED_PARTS_LAST_KEY, 0);
	}

	@Override
	public void remove(Object key) {
	    int lastIndex = getLastIndex() - 1;
		super.remove(key);
		
		put(INSTALLED_PARTS_LAST_KEY, lastIndex);
		
	}

	public List<InstalledPart> getDuplicatedParts(List<String> partNames, String partNumber) {
		List<InstalledPart> list = new ArrayList<InstalledPart>();

		for(Object key : getKeys()){
			List<InstalledPart> installedParts = get(key, List.class);

			if(installedParts == null) continue;

			for( InstalledPart part: installedParts){

				if(partNumber.equals(part.getPartSerialNumber()) &&
						partNames.contains(part.getId().getPartName()))
					list.add(part);
			}

		}
		return list;
	}
	
}
