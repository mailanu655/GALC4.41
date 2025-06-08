package com.honda.galc.device.simulator.client.view.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JFileChooser;

import org.apache.log4j.Logger;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.simulator.data.ITagValueSource;

public class PropertyFileTagValueSource  implements ITagValueSource {
	private static final long serialVersionUID = 5804692694290695890L;
	private Logger log = Logger.getLogger(PropertyFileTagValueSource.class);
	private Properties prop;
	private JFileChooser fc = new JFileChooser();
	
	
	/* (non-Javadoc)
	 * @see com.honda.global.device.simulator.data.ITagValueSource#getDeviceId(java.lang.String)
	 */
	public String getDeviceId(String entry) {
	    if(prop == null) return null;
	    return (String)prop.get(DEFAULT_ENTRY+".Device");
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

	/* (non-Javadoc)
	 * @see com.honda.global.device.simulator.data.ITagValueSource#loadEntries(java.lang.Object)
	 */
	public void loadEntries(Object param) {
        fc.setDialogTitle ("Open File");
        fc.setFileSelectionMode ( JFileChooser.FILES_ONLY);
        int ret = fc.showOpenDialog (null);
        if (ret != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        File loadedFile = fc.getSelectedFile();
        loadEntriesFromFile(loadedFile);
        
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

	/* (non-Javadoc)
	 * @see com.honda.global.device.simulator.data.ITagValueSource#saveEntries(java.lang.Object)
	 */
	public void saveEntries(Object param) {
		FileOutputStream out = null; 
		PrintStream p = null; 
		Properties prop = (Properties) param;
		try {			
			int ret = fc.showSaveDialog(null);
			if (ret != JFileChooser.APPROVE_OPTION) {
                return;
            }
			File fFile = fc.getSelectedFile();
			out = new FileOutputStream(fFile); 
			prop.store(out, "This is the saved test data");
			p = new PrintStream(out);
			p.flush();
			p.close();
		} catch (Exception e) { 
			e.printStackTrace();
			log.error("Error writing to file"); 
		} 	
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
		PropertyFileTagValueSource rs = new PropertyFileTagValueSource();
		rs.loadEntries("C:/Documents and Settings/is015809/My Documents/MCDC/UC8_MCBlockONstp02_HCM");
		
		System.out.println("DC: " + rs.toDataContainer());
		
		rs.storeToXML("C:/Documents and Settings/is015809/My Documents/MCDC/UC8_MCBlockONstp02_HCM.XML");
		
	}
}
