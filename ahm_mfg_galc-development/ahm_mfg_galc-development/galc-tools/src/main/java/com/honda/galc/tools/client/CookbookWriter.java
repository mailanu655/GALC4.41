package com.honda.galc.tools.client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.OpcConfigEntryDao;
import com.honda.galc.dao.conf.PrintAttributeFormatDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ApplicationTask;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.OpcConfigEntry;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DeviceDataType;
import com.honda.galc.entity.enumtype.OPCReadSource;
import com.honda.galc.entity.enumtype.OPCServerClientMode;
import com.honda.galc.entity.enumtype.OPCServerClientType;
import com.honda.galc.entity.enumtype.PrintAttributeType;
import com.honda.galc.service.ServiceFactory;

public class CookbookWriter {
	private static final String SPACE = "        ";
	private static final String SPACE2 = "                ";
	private static final String SPACE3 = "                        ";
	private static String NEWLINE = System.getProperty("line.separator");
	private String workingDirectory = ".";
	private	String[] overwriteOptions = {"Yes", "Yes To All", "Skip", "Skip All", "Cancel"};
	private int overwriteSelection = -1;
	private String filename = null;
	private StringBuilder builder;
	private List<ApplicationTask> applicationTasks;
	
	public CookbookWriter(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}
	
	public void createCookbook(String processPointId) {
		builder = new StringBuilder();
		applicationTasks = null;
		writeCookbook(processPointId);
	}

	public void createCookbooks(List<String> ids) {
		overwriteSelection = -1;
		for(String processPointId : ids) {
			filename = processPointId + ".txt";
			createCookbook(processPointId);
			if(overwriteSelection == 4) {
				System.out.println("Operation cancelled.");
				return;
			}
		}
	}

	public void writeCookbook(String processPointId) {
		
		if(FileHandler.isFileExist(workingDirectory, filename)) {
			if(overwriteSelection < 0) {
				overwriteSelection = JOptionPane.showOptionDialog(null, 
					filename + " exists in the directory. Do you want to overwrite it?", 
					"Please select a file overwrite option", 1,
					JOptionPane.DEFAULT_OPTION, null, overwriteOptions, null);
			}
			switch (overwriteSelection) {
			case 0:
				printCookbook(processPointId);
				overwriteSelection = -1;
				break;
			case 1:
				printCookbook(processPointId);
				break;
			case 2:
				System.out.println("Skipped cookbook " + processPointId);
				overwriteSelection = -1;
				break;
			case 3:
				System.out.println("Skipped cookbook " + processPointId);
			case 4:
			default :
			}
		} else {
			printCookbook(processPointId);
		}
	}
	
	private void printCookbook(String processPointId) {
		try {
			printProcessPoint(processPointId);
			printItem(NEWLINE);
			printDeviceConfiguration(processPointId);
			printItem(NEWLINE);
			printOPCConfiguration(processPointId);
			printItem(NEWLINE);
			printPrintAttributeFormat(processPointId);
			write(filename, builder.toString());
		} catch (Exception e) {
			System.out.println("Unable to create cookbook for process point: " + processPointId);
			e.printStackTrace();
		}
	}
	
	private void printProcessPoint(String processPointId) {
		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(processPointId);
		if(processPoint == null) {
			System.out.println("Process point does not exist.");
			return;
		}
		printItem("Process Point Configuration");
		printItem(1, "Process Point Id : ", processPointId);
		printItem(1, "Process Point Name : ", processPoint.getProcessPointName());
		printItem(1, "Process Point Type : ", processPoint.getProcessPointType());
		printItem(1, "Description : ", processPoint.getProcessPointDescription());
		printItem(1, "Passing Count : ", intToBooleanString(processPoint.getPassingCountFlag()));
		printItem(1, "Tracking Point : ", intToBooleanString(processPoint.getTrackingPointFlag()));
		printItem(1, "Recovery Point : ", intToBooleanString(processPoint.getRecoveryPointFlag()));
		printItem(1, "Backfill Process Point Id : ", processPoint.getBackFillProcessPointId());
		
		printItem(NEWLINE);
		printItem(1, "In Application Config", "");
		printApplicationDetails(processPointId);

		printComponentProperty(processPointId);

		printApplicationTasks(applicationTasks);
	}
	
	private void printApplicationDetails(String processPointId) {
		Application app = ServiceFactory.getDao(ApplicationDao.class).findByKey(processPointId);
		if(app != null) {
			printItem(2, "Screen Id : ", app.getScreenId());
			printItem(2, "Screen Class : ", app.getScreenClass());
			printItem(2, "Window Title : ", app.getWindowTitle());
			printItem(2, "Preload Index : ", app.getPreload());
			printItem(2, "Session Required : ", intToBooleanString(app.getSessionRequired()));
			printItem(2, "Persistent Session : ", intToBooleanString(app.getPersistentSession()));
			printItem(2, "Terminal Application Flag : ", intToBooleanString(app.getTerminalApplicationFlag()));
			applicationTasks = app.getApplicationTasks();
		}
	}
	
	private void printComponentProperty(String processPointId) {
		List<ComponentProperty> properties = ServiceFactory.getDao(ComponentPropertyDao.class).findAllByComponentId(processPointId);
		if(properties == null || properties.isEmpty()) {
			return;
		}
		printItem(NEWLINE);
		printItem(2, "Application Properties", "");
		for(ComponentProperty property : properties) {
			builder.append(SPACE3);
			builder.append(property.getPropertyKey());
			printItem(0, " = ", property.getPropertyValue());
			
		}
	}
	
	private void printApplicationTasks(List<ApplicationTask> tasks) {
		if(tasks != null && !tasks.isEmpty()) {
			for(ApplicationTask aTask : tasks) {
				printItem(NEWLINE);
				printItem(2, "Application Tasks", "");
				if(aTask.getTaskSpec() != null) {
					printItem(3, "Task Name : ", aTask.getTaskSpec().getTaskName());
				}
				printItem(3, "Input Event Flag : ", intToBooleanString(aTask.getInputEventExistFlag()));
				printItem(3, "Begin Flag : ", intToBooleanString(aTask.getBeginFlag()));
				printItem(3, "Commit Flag : ", intToBooleanString(aTask.getCommitFlag()));
				printItem(3, "Argument : ", aTask.getArgument());
				printItem("");
			}
		}
	}
	
	private void printDeviceConfiguration(String processPointId) {
		List<Device> devices = ServiceFactory.getDao(DeviceDao.class).findAllByProcessPointId(processPointId);
		
		for(Device aDevice : devices) {
			printItem("Device configuration");
			printItem(1, "Client ID : ", aDevice.getClientId());
			printItem(1, "Reply Client ID : ", aDevice.getReplyClientId());
			printItem(1, "Device Type : ", aDevice.getDeviceType().toString());
			printItem(1, "EIF IPAddress : ", aDevice.getEifIpAddress());
			printItem(1, "EIF Port : ", aDevice.getEifPort());
			printItem(1, "I/O Process point ID : ", aDevice.getIoProcessPointId());
			printItem(1, "Alias Name : ", aDevice.getAliasName());
			printItem(1, "Description : ", aDevice.getDeviceDescription());

			printDeviceDataFormat(aDevice);
			printReplyDeviceDataFormat(aDevice);
			printItem("");
		}
	}
	
	private void printDeviceDataFormat(Device device) {
		List<DeviceFormat> dataFormats = device.getDeviceDataFormats();
		if(dataFormats == null || dataFormats.isEmpty()) {
			return;
		}
		printItem(NEWLINE);
		printItem(2, "Device Data Format Config ", device.getClientId());
		for(DeviceFormat format : dataFormats) {
			builder.append(SPACE2);
			builder.append(format.getTagName());
			builder.append(" - ");
			builder.append(format.getDeviceTagType().toString());
			builder.append(" - ");
			builder.append(format.getTagValue());
//			printItem(0, " - ", DeviceDataType.getType(format.getDataTypeId()).name());
		}

	}
	
	private void printReplyDeviceDataFormat(Device device) {
		List<DeviceFormat> dataFormats = device.getReplyDeviceDataFormats();
		if(dataFormats == null || dataFormats.isEmpty()) {
			return;
		}
		printItem(NEWLINE);
		printItem(2, "Device Data Format Config ", device.getReplyClientId());
		for(DeviceFormat format : dataFormats) {
			builder.append(SPACE2);
			builder.append(format.getTagName());
			builder.append(" - ");
			builder.append(format.getDeviceTagType().toString());
			printItem(0, " - ", format.getTagValue());
		}

	}

	private void printOPCConfiguration(String processPointId) {
		List<OpcConfigEntry> configs;
		try {
			configs = ServiceFactory.getDao(OpcConfigEntryDao.class).findAllByProcessPointId(processPointId);
		} catch (Exception e) {
			configs = new ArrayList<OpcConfigEntry>();
		}
		
		for(OpcConfigEntry config : configs) {
			Device aDevice = ServiceFactory.getDao(DeviceDao.class).findByKey(config.getDeviceId());
			printItem("OPC Configuration Settings");
			printItem(1, "OPC In : ", "");
			printItem(1, "OPC Instance Name: " , config.getOpcInstanceName());
			printItem(1, "Data Ready Tag : ", config.getDataReadyTag());
			printItem(1, "Read Source : ", OPCReadSource.getType(config.getReadSourceId() == 2 ? 0 : 1));
			printItem(1, "Enabled : ", intToBooleanString(config.getEnabled()));
			printItem(1, "Needs Listener : ", intToBooleanString(config.getNeedsListener()));
			printItem(1, "Device ID : ", config.getDeviceId());
			printItem(1, "Process Point ID : ", processPointId);
	
			printItem(1, "Reply Device ID : ", aDevice.getReplyClientId());
			printItem(1, "Reply Data Ready Tag : ", config.getReplyDataReadyTag());
			printItem(1, "Process Complete Tag : ", config.getProcessCompleteTag());
			printItem(1, "Application Server Client Type : ", OPCServerClientType.getType(config.getServerClientTypeId() - 1));
			printItem(1, "Application Server Client Mode : ", OPCServerClientMode.getType(config.getServerClientModeId() - 1));
			printItem(1, "Application Server URL : ", config.getServerUrl());
			printItem(1, "Listener class : ", config.getListenerClass());
			printItem("");
		}
	}
	
	private void printPrintAttributeFormat(String processPointId) {
        List<PrintAttributeFormat> attributes = ServiceFactory.getDao(PrintAttributeFormatDao.class).findAllByFormId(processPointId);
        for(PrintAttributeFormat att : attributes) {
        	printItem("Print Attribute Format");
        	printItem(1, "Sequence : ", att.getSequenceNumber());
        	printItem(1, "Attribute : ", att.getAttribute());
        	printItem(1, "Attribute Type : ", PrintAttributeType.getType(att.getAttributeTypeId()));
        	printItem(1, "Attribute Value : ", att.getAttributeValue());
        	printItem(1, "Offset : ", att.getOffset());
        	printItem(1, "Length : ", att.getLength());
        	printItem("");
        }
	}
	
	private void printItem(String name) {
		builder.append(name);
		builder.append(NEWLINE);
	}

	private void printItem(int num, String name, String value) {
		for(int i = 0; i < num; i++) {
			builder.append(SPACE);
		}
		builder.append(name);
		if(value != null) {
			builder.append(value);
		}
		builder.append(NEWLINE);
	}

	private void printItem(int num, String name, Object value) {
		for(int i = 0; i < num; i++) {
			builder.append(SPACE);
		}
		builder.append(name);
		if(value != null) {
			builder.append(value.toString());
		}
		builder.append(NEWLINE);
	}

	private void printItem(int num, String name, int value) {
		for(int i = 0; i < num; i++) {
			builder.append(SPACE);
		}
		builder.append(name);
		builder.append(value);
		builder.append(NEWLINE);
	}

	private String intToBooleanString(int value) {
		return value == 1 ? "True" : "False";
	}
	
	private void write(String filename, String content)  {
		FileWriter writer = null;
        try {
        	File file = new File(workingDirectory, filename);
        	writer = new FileWriter(file);
            writer.write(content);
            System.out.println("Cookbook was saved to " + filename);
        } catch (IOException e) {
			System.out.println("Unable to write to file " + filename);
			e.printStackTrace();
        } finally {
			try {
				if(writer != null) {
					writer.flush();
					writer.close();
				}
			} catch (IOException e) {
				System.out.println("Unable to close file " + filename);
				e.printStackTrace();
			}
        }
    }


	public String getWorkingDirectory() {
		return workingDirectory;
	}

	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}
	
}
