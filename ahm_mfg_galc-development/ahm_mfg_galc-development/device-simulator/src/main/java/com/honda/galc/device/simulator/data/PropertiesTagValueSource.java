package com.honda.galc.device.simulator.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;

public class PropertiesTagValueSource implements ITagValueSource{

	private static final long serialVersionUID = 5804692694290695890L;
	private Logger log = Logger.getLogger(PropertiesTagValueSource.class);
	private Properties prop;
	
	
	/* (non-Javadoc)
	 * @see com.honda.global.device.simulator.data.ITagValueSource#getDeviceId(java.lang.String)
	 */
	public String getDeviceId(String entry) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.honda.global.device.simulator.data.ITagValueSource#getEntries()
	 */
	public List<String> getEntries() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.honda.global.device.simulator.data.ITagValueSource#getTagValue(java.lang.String, java.lang.String)
	 */
	public String getTagValue(String entry, String tag) {
		// TODO Auto-generated method stub
		return prop.getProperty(entry+tag);
	}


	private void loadEntriesFromFile(File loadedFile) {
		FileInputStream inStream = null;
		try {
			inStream = new FileInputStream(loadedFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("Error finding the file to be loaded"); 
		}
        prop = new Properties();
        try {
			prop.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error loading the properties"); 
		}
	}
	
	public void loadEntriesFromFile(String  filename) {
		File loadedFile = new File(filename);
		loadEntriesFromFile(loadedFile);
    }
	
	
	/**
	 * convert to data container with key/value from properties
	 * @return
	 */
	public DataContainer toDataContainer()
	{
		DataContainer dc = new DefaultDataContainer();
		
		for( Map.Entry<Object, Object> e  : prop.entrySet())
		{
			String key = e.getKey().toString().replace(DEFAULT_ENTRY +".tag.", "");
			
			if(!key.equals(DEFAULT_ENTRY+".Device"))
			   dc.put(key, e.getValue());
		}
		
		return dc;
	}
	
	public void storeToXML(String filename)
	{
		try {
			FileOutputStream out = new FileOutputStream(filename); 
			prop.storeToXML(out, prop.get(DEFAULT_ENTRY+".Device") + " XML file.");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error writing to XML file.");
		}
	}
	
	public void loadFromXML(String filename)
	{
		FileInputStream inStream = null;
		try {
			inStream = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("Error finding the file to be loaded"); 
		}
        prop = new Properties();
        try {
			prop.loadFromXML(inStream);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error loading the properties"); 
		}
	}
	
	public static void main(String[] args)
	{
		PropertiesTagValueSource rs = new PropertiesTagValueSource();
		//rs.loadEntriesFromFile("C:/Documents and Settings/is015809/My Documents/MCDC/UC8_MCBlockONstp02_HCM");
		
		rs.loadEntries("/AE0EN15301EIN");
		
		System.out.println("DC: " + rs.toDataContainer());
		
		rs.storeToXML("C:/Documents and Settings/is015809/My Documents/MCDC/UC8_MCBlockONstp02_HCM.XML");
		
	}

	/**
	 * Load entries from resource
	 */
	public void loadEntries(Object param) {
		InputStream inStream = this.getClass().getResourceAsStream((String)param);
		prop = new Properties();
        try {
			prop.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error loading the properties"); 
		}
		
	}

	public void saveEntries(Object param) {
		// TODO Auto-generated method stub
		
	}

	public Properties getProperties() {
		return prop;
	}

	public void setProperties(Properties prop) {
		this.prop = prop;
	}
	
	


}
