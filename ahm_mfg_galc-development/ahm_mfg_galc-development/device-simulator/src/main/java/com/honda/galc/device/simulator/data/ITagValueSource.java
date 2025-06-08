package com.honda.galc.device.simulator.data;

import java.util.List;

import com.honda.galc.data.DataContainer;


public interface ITagValueSource {

	/**
	 * Default entry when we have just one entry in tag value source 
	 */
	public String DEFAULT_ENTRY = "Default";

	/**
	 * Returns tag value based for given entry ID and a tag name<br/>
	 * The tag value can be used when populating reqest part of device panel
	 * 
	 * @param entry - tag value entry (ITagValueSource.DEFAULT_ENTRY can be used)
	 * @param tag - tag name
	 * @return tag value based on entry ID and a tag name
	 */
	public String getTagValue(String entry, String tag);

	/**
	 * Returns device client ID based on entry ID
	 * 
	 * @param entry - entry ID (ITagValueSource.DEFAULT_ENTRY can be used for one entry)
	 * @return device client ID for given entry ID
	 */
	public String getDeviceId(String entry);

	/**
	 * Returns list of all entry IDs defined in datasource
	 * 
	 * @return
	 */
	public List<String> getEntries();

	/**
	 * Allows tag value source to load data into its entries<br/>
	 * It can open dialog for loading from file, etc.
	 * 
	 * @param param - some object passed to the tag value source from
	 * invoker<br/>
	 * (It might be some GUI element)
	 * 
	 * 
	 */
	public void loadEntries(Object param);
	
	/**
	 * Allows tag value source to load data into its entries from properties file<br/>
	 * 
	 * @param filename - properties file name
	 * 
	 */
	public void loadEntriesFromFile(String  filename);

	
	/**
	 * Allows tag value source to store its entries<br/>
	 * It can open dialog for storing into file, etc.
	 * 
	 * @param param - some object passed to the tag value source from
	 * invoker<br/>
	 * (It might be some GUI element)
	 * 
	 * 
	 */
	public void saveEntries(Object param);
	

	/**
	 * Return a data container tag / vaue pairs for all entries
	 * @return
	 */
	public DataContainer toDataContainer();
	
}
