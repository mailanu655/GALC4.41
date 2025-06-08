package com.honda.galc.device.printer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.device.AbstractDevice;
import com.honda.galc.client.device.property.PrinterDevicePropertyBean;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.device.IPrintDevice;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.service.ServiceFactory;


public abstract class AbstractPrintDevice extends AbstractDevice implements IPrintDevice{
	
	protected String _hostName = "";
	protected String _destinationPrinter = "";
	protected String _templateName = "";
	protected String _printersList = "";

	protected int _port;
	protected int _maxPrintsPerCycle;
	protected int _prePrintQty;
	
	private Map<String, Device> _availablePrinters = new HashMap<String, Device>();

	public abstract boolean print(String printData, int printQuantity, String productId);
	
	
	public abstract String getprintData(DataContainer dc);
	
	public void setDeviceProperty(DevicePropertyBean propertyBean) {
				
		PrinterDevicePropertyBean property = (PrinterDevicePropertyBean) propertyBean;

		_id = property.getDeviceId();
		getLogger().info("id: " + getId());
		_maxPrintsPerCycle = property.getMaxPrintsPerCycle();
		getLogger().info("maxPrintsPerCycle: " + getMaxPrintsPerCycle());
		_enabled = property.isEnabled();
		getLogger().info("enabled: " + isEnabled());
		_printersList = property.getAvailPrinters();
		getLogger().info("availPrinters: " + getPrintersList());
		_prePrintQty = property.getPrePrintQty();
		getLogger().info("prePrintQty: " + getPrePrintQty());
		_destinationPrinter = property.getDestinationPrinter();
		getLogger().info("destinationPrinter: " + getDestinationPrinter());
		_templateName = property.getTemplateName();
		getLogger().info("templateName: " + getTemplateName());
		_deviceType = property.getType();
		getLogger().info("deviceType: " + getType());

		populateAvailablePrinters();
	}

	/**
	 * populate available printers
	 */
	private void populateAvailablePrinters() {
		List<String> printers = new ArrayList<String>();
			
		if(getPrintersList().length() > 0){
			if(getPrintersList().contains(",")){
				printers = Arrays.asList(getPrintersList().split(","));
			} else{
				printers.add(getPrintersList());
			}
			
			DeviceDao deviceDao = ServiceFactory.getDao(DeviceDao.class);
			for (String printerName : printers) {
				try {
					Device device = deviceDao.findByKey(printerName.trim());
					getAvailablePrinters().put(printerName, device);
				} catch (Exception ex) {
					getLogger().info("Device not found: " + ex.getMessage());
				}
			}
		}
	}
	
	public String getHostName() {
		return _hostName;
	}

	public Integer getPort() {
		return _port;
	}
	
	public void setHostName(String hostName) {
		_hostName = hostName;
	}
	
	public void setPort(Integer port) {
		_port = port;
	}
	
	public int getPrePrintQty() {
		return _prePrintQty;
	}

	public void setPrePrintQty(int printQty) {
		_prePrintQty = printQty;
	}

	public String getDestinationPrinter() {
		return _destinationPrinter;
	}

	public void setDestinationPrinter(String printer) {
		_destinationPrinter = printer;
	}

	public String getTemplateName() {
		return _templateName;
	}

	public void setTemplateName(String templateName) {
		_templateName = templateName;
	}
	
	public String getPrintersList() {
		return _printersList;
	}

	public void setPrintersList(String printersList) {
		_printersList = printersList;
	}

	public int getMaxPrintsPerCycle() {
		return _maxPrintsPerCycle;
	}

	public void setMaxPrintsPerCycle(int printsPerCycle) {
		_maxPrintsPerCycle = printsPerCycle;
	}

	protected void setAvailablePrinters(Map<String, Device> printerMap) {
		_availablePrinters = printerMap;
	}

	protected Map<String, Device> getAvailablePrinters() {
		return _availablePrinters;
	}

}
