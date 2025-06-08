package com.honda.galc.qi.cache;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.honda.galc.common.logging.Logger;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>LimitedSizeMemoryCache</code> is a memory map cache.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Aug 23, 2018
 */
public class LimitedSizeMemoryCache extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = 1L;
	private int capacity;
	private boolean countByValues;

	/**
	 * 
	 * @param maxCacheSize
	 * @param countByValues
	 *            - true - count of items on cache is calculated on values and if a value is an instance of collection then all collection items contribute to the count.
	 *            - false - count is a size of collection
	 */
	public LimitedSizeMemoryCache(int maxCacheSize, boolean countByValues) {
		super(100, 0.75f, true);
		this.capacity = maxCacheSize;
		this.countByValues = countByValues;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) get(key);
	}
	
	@Override
	protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
		if (getCapacity() < 0) {
			return false;
		}
		int itemCount = getItemCount();
		if (itemCount > getCapacity()) {
			if (Logger.getLogger().isDebugEnabled()) {
				Logger.getLogger().debug("Removing eldest entry from client cache, capacity:" + capacity + ", size: " + size() + ", item/value count:" + itemCount + ", eldest entry:" + eldest + ".");
			}
			return true;
		} else {
			return false;
		}
	}

	public int getItemCount() {
		if (isCountByValues()) {
			return getValuesCount();
		} else {
			return size();
		}
	}

	protected int getValuesCount() {
		int count = 0;
		for (Object value : values()) {
			if (value instanceof Collection) {
				count = count + ((Collection<?>) value).size();
			} else {
				count = count + 1;
			}
		}
		return count;
	}

	protected int getCapacity() {
		return capacity;
	}

	protected boolean isCountByValues() {
		return countByValues;
	}
}
