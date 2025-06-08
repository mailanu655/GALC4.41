package com.honda.galc.device.simulator.client.view.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.honda.galc.data.DataContainer;
import com.honda.galc.device.simulator.data.ITagValueSource;


public class TestTagValueSource implements ITagValueSource {
	
	private static Logger log = Logger.getLogger(TestTagValueSource.class);

	Map<String, String> tagValues = new HashMap<String, String>();
	private static final String deviceId = "AE Schedule Download PLC";
	
	/**
	 * 
	 */
	public TestTagValueSource() {
		super();
		tagValues.put("LAST_DOWNLOADED_PROD_LOT_NO", "200701310030");
		tagValues.put("LOT_NO_CHECK_OVERRIDE", "0");
		tagValues.put("NO_OF_LOTS_TO_BE_DOWNLOADED", "05");
	}

	/* (non-Javadoc)
	 * @see com.honda.global.device.simulator.data.ITagValueSource#getDeviceId(java.lang.String)
	 */
	public String getDeviceId(String entry) {
		return deviceId;
	}

	/* (non-Javadoc)
	 * @see com.honda.global.device.simulator.data.ITagValueSource#getEntries()
	 */
	public List<String> getEntries() {
		return Arrays.asList(ITagValueSource.DEFAULT_ENTRY);
	}

	/* (non-Javadoc)
	 * @see com.honda.global.device.simulator.data.ITagValueSource#getTagValue(java.lang.String, java.lang.String)
	 */
	public String getTagValue(String entry, String tag) {
		return tagValues.get(tag);
	}

	/* (non-Javadoc)
	 * @see com.honda.global.device.simulator.data.ITagValueSource#loadEntries(java.lang.Object)
	 */
	public void loadEntries(Object param) {
		log.info("loaded tag values");		
	}

	/* (non-Javadoc)
	 * @see com.honda.global.device.simulator.data.ITagValueSource#saveEntries(java.lang.Object)
	 */
	public void saveEntries(Object param) {
		log.info("saved tag values");		
	}

	public DataContainer toDataContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	public void loadEntriesFromFile(String filename) {
		// TODO Auto-generated method stub
		
	}

}
