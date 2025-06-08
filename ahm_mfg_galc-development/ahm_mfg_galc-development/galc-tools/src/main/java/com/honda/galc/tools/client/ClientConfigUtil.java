package com.honda.galc.tools.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.dao.conf.AccessControlEntryDao;
import com.honda.galc.dao.conf.ApplicationByTerminalDao;
import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.dao.conf.ApplicationMenuDao;
import com.honda.galc.dao.conf.ApplicationTaskDao;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.OpcConfigEntryDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.conf.PrintAttributeFormatDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.SiteDao;
import com.honda.galc.dao.conf.TaskSpecDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.entity.conf.AccessControlEntry;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ApplicationByTerminal;
import com.honda.galc.entity.conf.ApplicationMenuEntry;
import com.honda.galc.entity.conf.ApplicationTask;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.OpcConfigEntry;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.ServiceFactory;
import com.thoughtworks.xstream.XStream;

/**
 * <h3>Class description</h3>
 * This is a utility tool for client configuration management.
 * .
 * <h4>Description</h4>
 * <p>
 *   GALC client configuration tool is used to backup,
 *   remove and restore client configurations.
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Aug 17, 2012</TD>
 * <TD>1.0</TD>
 * <TD>20120817</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Dylan Yang
 */

public class ClientConfigUtil {
	private XStream xStream;
	private GalcClientConfiguration clientConfiguration;
	private String workingDirectory = ".";
	private	String[] overwriteOptions = {"Yes", "Yes To All", "Skip", "Skip All", "Cancel"};
	private int overwriteSelection = -1;
	private String filename = null;
	private List<ApplicationByTerminal> allAppsByTerminal;
	private List<Terminal> allTerminals;
	private boolean includeOpcConfig = true;
	
	public ClientConfigUtil() {
		loadConfig();
		reset();
	}

/**
 * Restore process point	
 * @param filename
 */
	public void restoreFromFiles(File[] files) {
    	if(files != null) {
    		try {
            	for(File aFile : files) {
            		if(aFile.isFile() && aFile.canRead()) {
            			restoreProcessPointFromFile(aFile.getAbsolutePath());       			
            		}
            	}
    		} catch (ServiceTimeoutException e) {
    			JOptionPane.showMessageDialog(null, "Timeout. Unable to restore.");
    		} catch (Exception ex) {
    			JOptionPane.showMessageDialog(null, "Exception: " + ex.getMessage());
    		}
    	}
	}
	
	public void restoreProcessPointFromFile(String filename) {
		reset();
		FileHandler fileHandler = new FileHandler(null, filename);
		clientConfiguration = fileHandler.readConfiguration();
		if(clientConfiguration == null) {
			JOptionPane.showMessageDialog(null, "Unable to read configuration from file: " + filename);
			return;
		}
		
		if(!clientConfiguration.isCurrentVersion()) {
			JOptionPane.showMessageDialog(null, clientConfiguration.wrongVersionMessage());
			return;
		}
		
		if(clientConfiguration.getProcessPoint() != null) {
			String processPointId = clientConfiguration.getProcessPoint().getId();
			if(ServiceFactory.getDao(ProcessPointDao.class).findByKey(processPointId) != null) {
				StringBuilder builder = new StringBuilder();
				builder.append("Process point ");
				builder.append(processPointId);
				builder.append(" already exists. It is recommended to remove it first to avoid potential problems. \n");
				builder.append("Do you want to remove it now?");
				int result = JOptionPane.showConfirmDialog(null, builder.toString());
				switch(result) {
				case JOptionPane.YES_OPTION :
					removeProcessPoint(processPointId);
					clientConfiguration = fileHandler.readConfiguration();
					break;
				case JOptionPane.NO_OPTION :
					break;
				case JOptionPane.CANCEL_OPTION :
					System.out.println("Skipping process point: " + processPointId);
					return;
				default :
				}
			}
			updateDatabase();
		} else {
			System.out.println("No process point definition in " + filename);
		}
	}

	public void updateDatabase() {
		ServiceFactory.getDao(ProcessPointDao.class).save(clientConfiguration.getProcessPoint());

		updateApplication(clientConfiguration.getApplication());

		if(!clientConfiguration.getProperties().isEmpty()) {
			ServiceFactory.getDao(ComponentPropertyDao.class).saveAll(clientConfiguration.getProperties());
		}

		for(Device device : clientConfiguration.getDevices()) {
			updateDeviceFormats(device.getDeviceDataFormats());
			updateDeviceFormats(device.getReplyDeviceDataFormats());
			ServiceFactory.getDao(DeviceDao.class).save(device);
		}
		
		if(!clientConfiguration.getTerminals().isEmpty()) {
			ServiceFactory.getDao(TerminalDao.class).saveAll(clientConfiguration.getTerminals());
		}

		if(!clientConfiguration.getAppsByTerminal().isEmpty()) {
//			for(ApplicationByTerminal : clientConfiguration.getAppsByTerminal()) {
//				app.setApplication(ServiceFactory.getDao(ApplicationDao.class).findByKey(app.getApplicationId()));
//			}
			ServiceFactory.getDao(ApplicationByTerminalDao.class).saveAll(clientConfiguration.getAppsByTerminal());
		}
		
		if(!clientConfiguration.getMenuEntries().isEmpty()) {
			ServiceFactory.getDao(ApplicationMenuDao.class).saveAll(clientConfiguration.getMenuEntries());
		}

		if(!clientConfiguration.getBroadcastDestinations().isEmpty()) {
			ServiceFactory.getDao(BroadcastDestinationDao.class).saveAll(clientConfiguration.getBroadcastDestinations());
		}

		if(!clientConfiguration.getAccessControlEntries().isEmpty()) {
			ServiceFactory.getDao(AccessControlEntryDao.class).saveAll(clientConfiguration.getAccessControlEntries());
		}

		if(!clientConfiguration.getPrintAttributeFormats().isEmpty()) {
			ServiceFactory.getDao(PrintAttributeFormatDao.class).saveAll(clientConfiguration.getPrintAttributeFormats());
		}

//		if(includeOpcConfig) {
			for(OpcConfigEntry entry : clientConfiguration.getOpcConfigs()) {
				entry.setId(null);
				ServiceFactory.getDao(OpcConfigEntryDao.class).save(entry);
			}
//		}
		
		System.out.println("Finished restoring process point: " + clientConfiguration.getProcessPoint().getId());
	}
	
	private void updateApplication(Application application) {
		if(application != null) {
			List<ApplicationTask> tasks = application.getApplicationTasks();
			for(ApplicationTask task : tasks) {
				ServiceFactory.getDao(TaskSpecDao.class).save(task.getTaskSpec());
				ServiceFactory.getDao(ApplicationTaskDao.class).save(task);
			}

			ServiceFactory.getDao(ApplicationDao.class).save(application);
		}
	}
	
	private void updateDeviceFormats(List<DeviceFormat> deviceFormats) {
		if(deviceFormats != null && !deviceFormats.isEmpty()) {
			ServiceFactory.getDao(DeviceFormatDao.class).saveAll(deviceFormats);
		}
	}
	
	private DeviceFormat copyDeviceFormat(DeviceFormat deviceFormat) {
		DeviceFormat format = new DeviceFormat();
		format.setId(deviceFormat.getId());
		format.setSequenceNumber(deviceFormat.getSequenceNumber());
		format.setOffset(deviceFormat.getOffset());
		format.setLength(deviceFormat.getLength());
		format.setTagType(deviceFormat.getTagType());
		format.setTagValue(deviceFormat.getTagValue());
		format.setDataType(deviceFormat.getDataType());
		
		return format;
	}
	
	public boolean validateClientConfiguration() {
		ProcessPoint pp = clientConfiguration.getProcessPoint();
		ServiceFactory.getDao(SiteDao.class).findByKey(pp.getSiteName());
		ServiceFactory.getDao(GpcsDivisionDao.class).findByKey(pp.getProcessPointId());
		ServiceFactory.getDao(DivisionDao.class).findByDivisionId(pp.getDivisionId());
		ServiceFactory.getDao(LineDao.class).findByKey(pp.getLineId());
		if(pp.getBackFillProcessPointId() != null && !pp.getBackFillProcessPointId().trim().isEmpty()) {
			ServiceFactory.getDao(ProcessPointDao.class).findByKey(pp.getBackFillProcessPointId());
		}
		
		return true;
	}
	
/**
 * Save process points
 * @param ids
 */
	public void backupProcessPoints(List<String> ids) {
		overwriteSelection = -1;
		try {
			for(String id : ids) {
				filename = id + ".xml";
				saveProcessPoint(id);
				if(overwriteSelection == 4) {
					System.out.println("Operation cancelled.");
					return;
				}
			}
		} catch (ServiceTimeoutException e) {
			JOptionPane.showMessageDialog(null, "Timeout. Please verify if the server is available.");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Exception: " + ex.getMessage());
		}
	}
	
	public void saveProcessPoint(String processPointId) {
		
		if(FileHandler.isFileExist(workingDirectory, filename)) {
			if(overwriteSelection < 0) {
				overwriteSelection = JOptionPane.showOptionDialog(null, 
					filename + " exists in the directory. Do you want to overwrite it?", 
					"Please select a file overwrite option", 1,
					JOptionPane.DEFAULT_OPTION, null, overwriteOptions, null);
			}
			switch (overwriteSelection) {
			case 0:
				writeToFile(processPointId);
				overwriteSelection = -1;
				break;
			case 1:
				writeToFile(processPointId);
				break;
			case 2:
				System.out.println("Skipped backing up " + processPointId);
				overwriteSelection = -1;
				break;
			case 3:
				System.out.println("Skipped backing up " + processPointId);
			case 4:
			default :
					
			}
		} else {
			writeToFile(processPointId);
		}
	}
	
	private void writeToFile(String processPointId) {
		retrieveClientConfiguration(processPointId);
		String xmlString = xStream.toXML(clientConfiguration);
		FileHandler handler = new FileHandler(workingDirectory, filename);
		handler.write(xmlString);
	}
	
	private void retrieveClientConfiguration(String processPointId) {
		reset();
		
		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(processPointId);
		if(processPoint == null) {
			System.out.println("Process point " + processPointId + " does not exist.");
			return;
		}

		clientConfiguration.setProcessPoint(processPoint);

		// Application
		Application app = ServiceFactory.getDao(ApplicationDao.class).findByKey(processPointId);
		if(app != null) {
			clientConfiguration.setApplication(app);
		}
		
		clientConfiguration.setDevices(findDevices(processPointId));
		if(includeOpcConfig) {
			clientConfiguration.setOpcConfigs(findOpcConfigEntries(processPointId));
		}

		processApplicationByTerminal(processPointId);
		processTerminalApplications(processPointId);
		processAccessControlEntries();
		processMenuEntries(processPointId);
		
		clientConfiguration.setProperties(findComponentProperties(processPointId));
        clientConfiguration.setBroadcastDestinations(findBroadcastDestinations(processPointId));
        clientConfiguration.setPrintAttributeFormats(findPrintAttributeFormats(processPointId));
		
        cleanup();
	}

	private void cleanup() {
		for(ApplicationByTerminal app : clientConfiguration.getAppsByTerminal()) {
			app.setApplication(null);
		}
		for(Terminal terminal : clientConfiguration.getTerminals()) {
			terminal.setProcessPoint(null);
		}
		for(ApplicationMenuEntry entry : clientConfiguration.getMenuEntries()) {
			entry.setApplication(null);
		}
	}
	
	private List<OpcConfigEntry> findOpcConfigEntries(String processPointId) {
		List<OpcConfigEntry> list;
		try {
			list = ServiceFactory.getDao(OpcConfigEntryDao.class).findAllByProcessPointId(processPointId);
		} catch (Exception e) {
			list = new ArrayList<OpcConfigEntry>();
		}
		return list;
	}
	
	private List<Device> findDevices(String processPointId) {
		return ServiceFactory.getDao(DeviceDao.class).findAllByProcessPointId(processPointId);
	}
	
	private List<PrintAttributeFormat> findPrintAttributeFormats(String processPointId) {
	    return ServiceFactory.getDao(PrintAttributeFormatDao.class).findAllByFormId(processPointId);
	}
	
	private List<ComponentProperty> findComponentProperties(String processPointId) {
		List<ComponentProperty> properties = new ArrayList<ComponentProperty>();
		properties.addAll(ServiceFactory.getDao(ComponentPropertyDao.class).findAllByComponentId(processPointId));
		for(Terminal aTerminal : clientConfiguration.getTerminals()) {
			if(!processPointId.equals(aTerminal.getId())) {
				properties.addAll(ServiceFactory.getDao(ComponentPropertyDao.class).findAllByComponentId(aTerminal.getId()));
			}
		}
		return properties;
	}
	
	private List<BroadcastDestination> findBroadcastDestinations(String processPointId) {
	    return ServiceFactory.getDao(BroadcastDestinationDao.class).findAllByProcessPointId(processPointId);
	}
	
	private void processApplicationByTerminal(String processPointId) {
		for(ApplicationByTerminal app : getAllAppsByTerminal()) {
			if(processPointId.equals(app.getApplicationId())) {
				clientConfiguration.addApplicationByTerminal(app);
			}
		}
	}
	
	public void processTerminalApplications(String processPointId) {
		List<Terminal> terminals = findTerminals(processPointId);
		if(terminals.isEmpty()) {
			return;
		}
		clientConfiguration.setTerminals(terminals);
		for(Terminal aTerminal : terminals) {
			List<ApplicationByTerminal> list = ServiceFactory.getDao(ApplicationByTerminalDao.class).findAllByTerminal(aTerminal.getHostName());
			for(ApplicationByTerminal app : list) {
				clientConfiguration.addApplicationByTerminal(app);
			}
		}
	}
	
	private void processAccessControlEntries() {
		for(ApplicationByTerminal app : clientConfiguration.getAppsByTerminal()) {
			String screenId = app.getApplication().getScreenId();
			if(screenId != null) {
				for(AccessControlEntry entry : ServiceFactory.getDao(AccessControlEntryDao.class).findAllByScreenId(screenId)) {
					clientConfiguration.addAccessControlEntry(entry);
				}
			}
		}
	}
	
	private void processMenuEntries(String processPointId) {
		List<ApplicationMenuEntry> entries;
		for(Terminal terminal : clientConfiguration.getTerminals()) {
			entries = ServiceFactory.getDao(ApplicationMenuDao.class).findAllByClientId(terminal.getId());
			if(entries != null && !entries.isEmpty()) {
				clientConfiguration.getMenuEntries().addAll(entries);
			}
		}
	}

/**
 * Remove process points	
 * @param ids
 */
	public void removeProcessPoints(List<String> ids) {

		overwriteSelection = -1;
		try {
			for(String id : ids) {
				filename = id + ".xml";
				if(FileHandler.isFileExist(workingDirectory, filename)) {
					if(overwriteSelection < 0) {
						overwriteSelection = JOptionPane.showOptionDialog(null, 
							filename + " exists in the directory. Do you want to overwrite it?", 
							"Please select a file overwrite option", 1,
							JOptionPane.DEFAULT_OPTION, null, overwriteOptions, null);
					}
					switch (overwriteSelection) {
					case 0:
						writeToFile(id);
						removeProcessPoint(id);
						overwriteSelection = -1;
						break;
					case 1:
						writeToFile(id);
						removeProcessPoint(id);
						break;
					case 2:
						System.out.println("Skipped " + id);
						overwriteSelection = -1;
						break;
					case 3:
						System.out.println("Skipped " + id);
					case 4:
					default :
							
					}
				} else {
					writeToFile(id);
					removeProcessPoint(id);
				}
			}
		} catch (ServiceTimeoutException e) {
			JOptionPane.showMessageDialog(null, "Timeout. Unable to remove process points from server.");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Exception: " + ex.getMessage());
		}
	}
	
	public void removeProcessPoint(String processPointId) {
		reset();
		retrieveClientConfiguration(processPointId);
		
		List<String> list = terminalsHostProcessPoint(processPointId);
		if(list.size() > 1) {
			StringBuilder builder = new StringBuilder();
			builder.append("Following terminals use process point ");
			builder.append(processPointId);
			builder.append(".\n Do you really want to remove it from the system? \n");
			for(String name : list) {
				builder.append('\n');
				builder.append(name);
			}
			
			int result = JOptionPane.showConfirmDialog(null, builder.toString(), "Please confirm", JOptionPane.YES_NO_OPTION);
			if(JOptionPane.YES_OPTION != result) {
				return;
			}
		}
		
		// PrintAttributeFormat
        ServiceFactory.getDao(PrintAttributeFormatDao.class).removeAll(clientConfiguration.getPrintAttributeFormats());
		
		// BroadcastDestination
        ServiceFactory.getDao(BroadcastDestinationDao.class).removeAll(clientConfiguration.getBroadcastDestinations());
		
        // Component Property
		ServiceFactory.getDao(ComponentPropertyDao.class).removeAll(clientConfiguration.getProperties());

		// Menu Entry
		ServiceFactory.getDao(ApplicationMenuDao.class).removeAll(clientConfiguration.getMenuEntries());
		
		// ApplicationByTerminal
		ServiceFactory.getDao(ApplicationByTerminalDao.class).removeAll(clientConfiguration.getAppsByTerminal());
		
		// Terminal 
		ServiceFactory.getDao(TerminalDao.class).removeAll(clientConfiguration.getTerminals());
		
		// Device
		List<Device> devices = clientConfiguration.getDevices();
		if(devices != null && !devices.isEmpty()) {
			for(Device device : devices) {
				if(device.getDeviceDataFormats() != null) {
					ServiceFactory.getDao(DeviceFormatDao.class).removeAll(device.getDeviceDataFormats());
					device.setDeviceDataFormats(null);
				}
				if(device.getReplyDeviceDataFormats() != null) {
					ServiceFactory.getDao(DeviceFormatDao.class).removeAll(device.getReplyDeviceDataFormats());
					device.setReplyDeviceDataFormats(null);
				}

			}

			// Opc config
//			if(includeOpcConfig) {
				ServiceFactory.getDao(OpcConfigEntryDao.class).removeAll(clientConfiguration.getOpcConfigs());
//			}

			ServiceFactory.getDao(DeviceDao.class).removeAll(devices);
		}

		// Application
		removeApplication(clientConfiguration.getApplication());

		ServiceFactory.getDao(ProcessPointDao.class).remove(clientConfiguration.getProcessPoint());
		System.out.println("Finished removing process point: " + processPointId);
	}

	private List<String> terminalsHostProcessPoint(String processPointId) {
		List<String> terminals = new ArrayList<String>();
		for(ApplicationByTerminal app : getAllAppsByTerminal()) {
			if(processPointId.equals(app.getApplicationId())) {
				terminals.add(app.getHostName());
			}
		}
		return terminals;
	}
	
	private void removeApplication(Application application) {
		
		if(application == null) {
			return;
		}
		
		// ApplicationTask
		List<ApplicationTask> tasks = application.getApplicationTasks();
		if(tasks != null && !tasks.isEmpty()) {
			for(ApplicationTask aTask : tasks) {
				aTask.setTaskSpec(null);
			}
			ServiceFactory.getDao(ApplicationTaskDao.class).removeAll(tasks);
		}
		application.setApplicationTasks(null);
		
		// AccessControlEntry
		if(application.getScreenId() != null && !isScreenIdShared(application.getScreenId())) {
			List<AccessControlEntry> accessControlEntries = ServiceFactory.getDao(AccessControlEntryDao.class).findAllByScreenId(application.getScreenId());
			if(accessControlEntries != null && !accessControlEntries.isEmpty()) {
				ServiceFactory.getDao(AccessControlEntryDao.class).removeAll(accessControlEntries);
			}
		}

		ServiceFactory.getDao(ApplicationDao.class).remove(application);
	}


	private List<Terminal> findTerminals(String processPointId) {
		List<Terminal> list = new ArrayList<Terminal>();
		for(Terminal terminal : getAllTerminals()) {
			if(processPointId.equals(terminal.getLocatedProcessPointId())) {
				list.add(terminal);
			}
		}
		return list;
	}
	
	private boolean isScreenIdShared(String id) {
		int count = 0;
		for(Application app : ServiceFactory.getDao(ApplicationDao.class).findAll()) {
			if(id.equals(app.getScreenId())) {
				count++;
			}
		}
		return count > 1 ? true : false;
	}
	
	public void createCookbook(List<String> ids) {
		CookbookWriter writer = new CookbookWriter(workingDirectory);
		writer.createCookbooks(ids);
	}
	
	public void setServerUrl(String url) {
		HttpServiceProvider.setUrl(url);
	}
	
	public List<ProcessPoint> findAllProcessPoint() {
		List<ProcessPoint> list = new ArrayList<ProcessPoint>();
		try {
			list = ServiceFactory.getDao(ProcessPointDao.class).findAll();
		} catch (ServiceTimeoutException e) {
			JOptionPane.showMessageDialog(null, "Timeout. Unable to refresh process points from server.");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Exception: " + ex.getMessage());
		}
		return list;
	}
	
	public String getWorkingDirectory() {
		return workingDirectory;
	}

	public void setWorkingDirectory(String workDirectory) {
		this.workingDirectory = workDirectory;
	}

	private void loadConfig() {
		ApplicationContextProvider.loadFromClassPathXml("application.xml");
	}

	private void reset() {
		clientConfiguration = new GalcClientConfiguration();
		allAppsByTerminal = null;
		allTerminals = null;
		xStream = new XStream();
	}

	public List<ApplicationByTerminal> getAllAppsByTerminal() {
		if(allAppsByTerminal == null) {
			allAppsByTerminal = ServiceFactory.getDao(ApplicationByTerminalDao.class).findAll();
		}
		return allAppsByTerminal;
	}

	public void setAllAppsByTerminal(List<ApplicationByTerminal> allAppsByTerminal) {
		this.allAppsByTerminal = allAppsByTerminal;
	}

	public List<Terminal> getAllTerminals() {
		if(allTerminals == null) {
			allTerminals = ServiceFactory.getDao(TerminalDao.class).findAll();
		}
		return allTerminals;
	}

	public void setAllTerminals(List<Terminal> allTerminals) {
		this.allTerminals = allTerminals;
	}
}
