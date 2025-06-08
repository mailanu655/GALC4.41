package com.honda.galc.tools.client;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.entity.conf.AccessControlEntry;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ApplicationByTerminal;
import com.honda.galc.entity.conf.ApplicationMenuEntry;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.OpcConfigEntry;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Terminal;

/**
 * <h3>Class description</h3>
 * This is the class to hold GALC client configurations.
 * .
 * <h4>Description</h4>
 * <p>
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
public class GalcClientConfiguration {
	public static final String CURRENT_VERSION = "2";
	private ProcessPoint processPoint;
	private Application application;
	private List<ComponentProperty> properties;
	private List<Device> devices;
	private List<OpcConfigEntry> opcConfigs;
	private List<Terminal> terminals;
	private List<ApplicationByTerminal> appsByTerminal;
	private List<ApplicationMenuEntry> menuEntries;
	private List<BroadcastDestination> broadcastDestinations;
	private List<AccessControlEntry> accessControlEntries;
	private List<PrintAttributeFormat> printAttributeFormats;
	private String version;
	
	public GalcClientConfiguration() {
		super();
		version = CURRENT_VERSION;
	}
	
	public boolean validate() {
		
		// Validate process point
		
		// Validate application and tasks
		if(appsByTerminal != null) {
			for(ApplicationByTerminal appBT : appsByTerminal) {
				if(appBT.isDefaultApplication() && !appBT.getApplication().getApplicationId().equals(application.getApplicationId())) {
					return false;
				}
				
				if(!hasAppInMenuEntry(appBT.getApplication())) {
					return false;
				}
			}
		}
		
		// Validate component property
		for(Device aDevice : devices) {
			if(!processPoint.getDivisionId().equals(aDevice.getDivisionId())) {
				return false;
			}
			
			if(!processPoint.getProcessPointId().equals(aDevice.getIoProcessPointId())) {
				return false;
			}
		}
		
		return true;
	}

	public boolean isCurrentVersion() {
		return CURRENT_VERSION.equals(getVersion());
	}
	
	public String wrongVersionMessage() {
		StringBuilder builder = new StringBuilder();
		builder.append("This XML for ");
		builder.append(processPoint.getId());
		builder.append(" was built by version ");
		builder.append(getVersion());
		builder.append(". Current version of the tool is ");
		builder.append(CURRENT_VERSION);
		builder.append(". Please use previous version of the tool to restore it.");
		return builder.toString();
	}
	
	public void addApplicationByTerminal(ApplicationByTerminal app) {
		if(!hasApplicationByTerminal(app)) {
			getAppsByTerminal().add(app);
		}
	}
	
	private boolean hasApplicationByTerminal(ApplicationByTerminal applicationByTerminal) {
		for(ApplicationByTerminal app : getAppsByTerminal()) {
			if(app.getId().equals(applicationByTerminal.getId())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasAppInMenuEntry(Application app) {
		for(ApplicationMenuEntry menu : getMenuEntries()) {
			if(menu.getApplication().getApplicationId().equals(app.getApplicationId())) {
				return true;
			}
		}
		return false;
	}
	
	public void addAccessControlEntry(AccessControlEntry ace) {
		if(!hasAccessControlEntry(ace)) {
			getAccessControlEntries().add(ace);
		}
	}
	
	private boolean hasAccessControlEntry(AccessControlEntry ace) {
		for(AccessControlEntry entry : getAccessControlEntries()) {
			if(entry.getId().equals(ace.getId())) {
				return true;
			}
		}
		return false;
	}

	public ProcessPoint getProcessPoint() {
		return processPoint;
	}

	public void setProcessPoint(ProcessPoint processPoint) {
		this.processPoint = processPoint;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public List<ComponentProperty> getProperties() {
		if(properties == null) {
			properties = new ArrayList<ComponentProperty>();
		}
		return properties;
	}

	public void setProperties(List<ComponentProperty> properties) {
		this.properties = properties;
	}

	public List<Device> getDevices() {
		if(devices == null) {
			devices = new ArrayList<Device>();
		}
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public List<OpcConfigEntry> getOpcConfigs() {
		if(opcConfigs == null) {
			opcConfigs = new ArrayList<OpcConfigEntry>();
		}
		return opcConfigs;
	}

	public void setOpcConfigs(List<OpcConfigEntry> opcConfigs) {
		this.opcConfigs = opcConfigs;
	}

	public List<ApplicationByTerminal> getAppsByTerminal() {
		if(appsByTerminal == null) {
			appsByTerminal = new ArrayList<ApplicationByTerminal>();
		}
		return appsByTerminal;
	}

	public void setAppsByTerminal(List<ApplicationByTerminal> appsByTerminal) {
		this.appsByTerminal = appsByTerminal;
	}

	public List<ApplicationMenuEntry> getMenuEntries() {
		if(menuEntries == null) {
			menuEntries = new ArrayList<ApplicationMenuEntry>();
		}
		return menuEntries;
	}

	public void setMenuEntries(List<ApplicationMenuEntry> menuEntries) {
		this.menuEntries = menuEntries;
	}

	public List<BroadcastDestination> getBroadcastDestinations() {
		if(broadcastDestinations == null) {
			broadcastDestinations = new ArrayList<BroadcastDestination>();
		}
		return broadcastDestinations;
	}

	public void setBroadcastDestinations(List<BroadcastDestination> broadcastDestinations) {
		this.broadcastDestinations = broadcastDestinations;
	}

	public List<AccessControlEntry> getAccessControlEntries() {
		if(accessControlEntries == null) {
			accessControlEntries = new ArrayList<AccessControlEntry>();
		}
		return accessControlEntries;
	}

	public void setAccessControlEntries(List<AccessControlEntry> accessControlEntries) {
		this.accessControlEntries = accessControlEntries;
	}

	public List<PrintAttributeFormat> getPrintAttributeFormats() {
		if(printAttributeFormats == null) {
			printAttributeFormats = new ArrayList<PrintAttributeFormat>();
		}
		return printAttributeFormats;
	}

	public void setPrintAttributeFormats(List<PrintAttributeFormat> printAttributeFormats) {
		this.printAttributeFormats = printAttributeFormats;
	}

	public List<Terminal> getTerminals() {
		if(terminals == null) {
			terminals = new ArrayList<Terminal>();
		}
		return terminals;
	}

	public void setTerminals(List<Terminal> terminals) {
		this.terminals = terminals;
	}

	public String getVersion() {
		if(version == null) {
			version = "1";
		}
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
