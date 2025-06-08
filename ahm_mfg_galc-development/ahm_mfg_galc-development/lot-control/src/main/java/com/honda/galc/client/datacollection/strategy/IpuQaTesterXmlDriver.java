package com.honda.galc.client.datacollection.strategy;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ipuqatester.IpuQaTesterDeviceListener;
import com.honda.galc.client.device.ipuqatester.IpuQaTesterSocketDevice;
import com.honda.galc.client.device.ipuqatester.model.IpuQaTestResult;
import com.honda.galc.client.device.ipuqatester.model.Process;
import com.honda.galc.client.device.ipuqatester.model.Test;
import com.honda.galc.client.device.ipuqatester.model.TestAttrib;
import com.honda.galc.client.device.ipuqatester.model.TestParam;
import com.honda.galc.client.device.ipuqatester.model.UnitInTest;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.QueueProcessor;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Subu Kathiresan
 * @author Gangadhararao Gadde
 * @Date Apr 23, 2012
 *
 */
public class IpuQaTesterXmlDriver extends QueueProcessor<StringBuffer> 
	implements IpuQaTesterDeviceListener {
	
	private static final String XML_START_STRING = "<UNIT_IN_TEST";
	private static final String XML_SAVE_LOCATION_KEY = "xmlSaveLocation";
	private static final String IPU_QA_TESTER_DEVICE_PREFIX = "ipuqatester";
	private static final String IPU_QA_TESTER_CLIENT_ID = "IPUQATESTER";
	private static final String METHOD_SETPARTSERIALNUMBER = "setPartSerialNumber";
	private static final String METHOD_ADDMEASUREMENT = "addMeasurement";
	
	protected DeviceFormatDao _deviceFormatDao = null;
	protected PartSpecDao _partSpecDao = null;
	private HashMap<String, String> _measurementValuesMap = new HashMap<String, String>(); 
	private HashMap<String, DeviceFormat> _deviceFormatMap = new HashMap<String, DeviceFormat>();
	private ArrayList<KeyValue<String, IpuQaTesterSocketDevice>> _ipuQaTesterDevicesList = new ArrayList<KeyValue<String, IpuQaTesterSocketDevice>>();
	private DataCollectionController _controller;
	private String _applicationId;
	private static IpuQaTesterXmlDriver _instance;
	
	private IpuQaTesterXmlDriver(String applicationId) {
		super();
		_applicationId = applicationId;
		startIpuQaTesterDevices();
		this.start();
	}
	
	/**
	 * returns the singleton instance of the driver
	 * @return
	 */
	public static IpuQaTesterXmlDriver getInstance(String applicationId) {
		if (_instance == null) 
			_instance = new IpuQaTesterXmlDriver(applicationId);
		
		return _instance;
	}
	
	/**
	 * activates all ipuqatester devices (socket servers) and starts
	 * listening for ipu qa test xml messages
	 */
	private void startIpuQaTesterDevices() {
		for (String deviceName: DeviceManager.getInstance().getDeviceNames()) {
			if (deviceName.startsWith(IPU_QA_TESTER_DEVICE_PREFIX)) {
				IpuQaTesterSocketDevice device = (IpuQaTesterSocketDevice) DeviceManager.getInstance().getDevice(deviceName);
				device.registerListener(this);
				device.activate();
				_ipuQaTesterDevicesList.add(new KeyValue<String, IpuQaTesterSocketDevice>(deviceName, device));
			}
		}
	}
	
	@Override
	public void processItem(StringBuffer ipuXmlStringBuf) {
		UnitInTest ipuUnit = null;

		try {
			int xmlStartIndex = getXmlStartIndex(ipuXmlStringBuf);
			String messageId = ipuXmlStringBuf.substring(0, xmlStartIndex).trim();
			String xmlString = ipuXmlStringBuf.substring(xmlStartIndex).trim();
			saveXml(messageId, xmlString);				
			
			ipuUnit = getUnitInTest(xmlString);
			setDeviceFormatMap(getAllDeviceFormats(IPU_QA_TESTER_CLIENT_ID));
			setMeasurementValuesMap(getAllMeasurementValues(ipuUnit));
			
			// send product information
			getController().received(ipuUnit);
				
			// send part information
			for (int i = 0; i < getController().getState().getLotControlRules().size(); i++) {
				if (getController().getState() instanceof ProcessPart) {
					String partName = getController().getCurrentLotControlRule().getPartName().getPartName().trim();
					getLogger().info(getController().getCurrentLotControlRule().getPartName().getPartName());
					IpuQaTestResult result = new IpuQaTestResult();
					
					InstalledPart installedPart = new InstalledPart();
					installedPart.setProductId(getController().getState().getProductId());
					installedPart.setPartName(partName);
					
					if(getDeviceFormatMap().containsKey(partName)) {
						DeviceFormat deviceFormatValue = getDeviceFormatMap().get(partName);
						String measurementValue = getMeasurementValuesMap().get(deviceFormatValue.getTagValue());						
						if (measurementValue == null) {
							installedPart.setInstalledPartStatus(InstalledPartStatus.MISSING);
							getLogger().info("Missing part in IPU QA Tester Xml file: " + partName);
						} else {
							installedPart = getPart(measurementValue, deviceFormatValue, getController().getState().getProductId()); 
						}
					}  else {
						installedPart.setInstalledPartStatus(InstalledPartStatus.MISSING);
						getLogger().info("Missing tag in DeviceFormat table: " + partName);
					}
					result.setInstalledPart(installedPart);
					getController().received(result);
				}
			}
			
			// send refresh command
			getController().received(new ProductId(getController().getState().getProductId()));
		} catch (Exception e) {
			Logger.getLogger().error(e.getMessage());
		}
	}
	
	/**
	 * returns a valid part based on the DeviceFormat and product provided
	 * 
	 * @param measurementValue
	 * @param deviceFormat
	 * @param productId
	 * @return
	 */
	private InstalledPart getPart(String measurementValue, DeviceFormat deviceFormat, String productId) {
		InstalledPart part = null;
		try{
			part = createNewInstalledPart(productId, deviceFormat); 
			String methodName = StringUtils.substring(deviceFormat.getId().getTag(), 0, deviceFormat.getId().getTag().indexOf("("));
			String methodParam = "";
			
			if(methodName.equals(METHOD_SETPARTSERIALNUMBER)) {
				methodParam = measurementValue;
			} else if (methodName.equals(METHOD_ADDMEASUREMENT)){
				methodParam = measurementValue + "," + StringUtils.substringBetween(deviceFormat.getId().getTag(), "(", ")"); 
			} else {
				getLogger().error("Invalid Device format tag: " + deviceFormat.getId().getTag());
				return null;
			}
			
			Method[] methods = part.getClass().getDeclaredMethods();
			Method method = null;

			for (Method tempMethod : methods) {
				if (tempMethod.getName().contains(methodName))
					method = tempMethod;
			}

			StringTokenizer st = new StringTokenizer(methodParam, ","); 
			ArrayList<String> tempList = new ArrayList<String>();

			while(st.hasMoreTokens()) { 
				tempList.add(st.nextToken()) ; 
			}
			String[] methodParamArray = tempList.toArray(new String[tempList.size()]);
			method.invoke(part, methodParamArray); 
		}catch(Exception e) {
			e.printStackTrace();
		}	    

		return part;
	}

	private InstalledPart createNewInstalledPart(String productId, DeviceFormat deviceFormat) {
		String partName = deviceFormat.getTagName();
		List<PartSpec> partSpecs = getPartSpecDao().findAllByPartName(partName);
		if (partSpecs.size() < 1) {
			Logger.getLogger().warn("PartSpec not found for: " + partName);
			return null;
		}
		InstalledPart installedPart = new InstalledPart();
		installedPart.setAssociateNo(getController().getClientContext().getUserId());
		installedPart.setValidPartSerialNumber(true);
		Iterator<PartSpec> it = partSpecs.iterator();
		PartSpec partSpec = null;
		while (it.hasNext()) {
			partSpec = it.next();
			installedPart.setId(new InstalledPartId(productId, partSpec.getId().getPartName()));
			installedPart.setPartId(partSpec.getId().getPartId());
		}		
		installedPart.setPartSerialNumber("");
		installedPart.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
		return installedPart;
	}
	
	public int getXmlStartIndex(StringBuffer xmlStringBuf) {
		return xmlStringBuf.indexOf(XML_START_STRING);
	}
	
	/**
	 * save xml file in the configured location, make directory if needed
	 * 
	 * @param messageId
	 * @param xmlString
	 */
	public void saveXml(String messageId, String xmlString) {
		String xmlSaveLoc = "";
		FileOutputStream fo = null;
		try {
			xmlSaveLoc = PropertyService.getProperty(getController().getClientContext().getAppContext().getTerminalId(), XML_SAVE_LOCATION_KEY);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
			Date now = new Date();
			String dateString = sdf.format(now);
			xmlSaveLoc += File.separator + dateString;
			makeDir(xmlSaveLoc);
			String filePath = getFilePath(xmlSaveLoc, messageId, now);
			fo = new FileOutputStream(filePath);
			fo.write(xmlString.getBytes());
			fo.flush();
			getLogger().info("Successfully saved xml file at " + filePath);
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error("Exception occurred while saving file to location: " + xmlSaveLoc);
		} finally {
			if (fo != null) {
				try {
					fo.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void makeDir(String xmlSaveLoc) {
		File folder = new File(xmlSaveLoc);
		if (!folder.exists()) {
			getLogger().info("Attempting to make directory: " + xmlSaveLoc);
			if (folder.mkdirs())
				getLogger().info("Successfully created directory: " + xmlSaveLoc);
			else
				getLogger().warn("Unable to create directory: " + xmlSaveLoc);
		}
	}
	
	private String getFilePath(String xmlSaveLoc, String messageId, Date date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmssSSS");
			String dateString = sdf.format(date);
			return xmlSaveLoc + File.separator + (messageId == null? "" : messageId) + "_" + dateString + ".xml";
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Could not retrieve the file path to save the IPU QA Tester xml message for " + messageId);
		}
		return "";
	}
	
	public UnitInTest getUnitInTest(String xmlMessage) {
		XStream xs = new XStream();
		xs.processAnnotations(UnitInTest.class);
		xs.processAnnotations(Process.class);
		xs.processAnnotations(com.honda.galc.client.device.ipuqatester.model.Test.class);
		xs.processAnnotations(TestParam.class);
		xs.processAnnotations(TestAttrib.class);
		
		try {
			return (UnitInTest) xs.fromXML(xmlMessage);
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		return null;
	}
	
	private HashMap<String, DeviceFormat> getAllDeviceFormats(String string) {
		List<DeviceFormat> deviceFormats = getDeviceFormatDao().findAllByDeviceId(IPU_QA_TESTER_CLIENT_ID);
		HashMap<String, DeviceFormat> deviceFormatMap = new HashMap<String, DeviceFormat>();
		Iterator<DeviceFormat> deviceFormatIter = deviceFormats.iterator();
		while(deviceFormatIter.hasNext()) {
			DeviceFormat deviceFormat = deviceFormatIter.next();			
			deviceFormatMap.put(deviceFormat.getTagName().trim(), deviceFormat);
		}
		return deviceFormatMap;
	}

	/**
	 * TODO: make this a utility method which can be re-used for any XML based entity
	 * 
	 * @param ipuUnit
	 * @return
	 */
	public HashMap<String, String> getAllMeasurementValues(UnitInTest ipuUnit) {
		HashMap<String, String> measurementValuesMap = new HashMap<String, String>(); 
		String measurementValue = null;
		try	{
			List<Process> processList = ipuUnit.getProcesses();
			for (Process process : processList) {
				List<Test> testList = process.getTests();
				for (Test test : testList) {
					List<TestParam> paramList = test.getTestParams();
					if (paramList != null && paramList.size() > 0) {
						for (TestParam testParam : paramList) {
							for(Field field : Arrays.asList(TestParam.class.getDeclaredFields())) {
								XStreamAlias annotation = field.getAnnotation(XStreamAlias.class);
								Class<?> params[] = {};
								Method method = testParam.getClass().getDeclaredMethod("get" + annotation.value(), params);
								Object ret = method.invoke(testParam, new Object[] {}); 
								measurementValue = (ret == null)? "" : ret.toString();
								measurementValuesMap.put((test.getId() + "." + testParam.getParam() + "." + annotation.value()).trim(), measurementValue);
							}	
						}
					}

					List<TestAttrib> attribList = test.getTestAttribs();
					if (attribList != null && attribList.size() > 0) {
						for (TestAttrib testAttrib : attribList) {
							for(Field field : Arrays.asList(TestAttrib.class.getDeclaredFields())) {
								XStreamAlias annotation = field.getAnnotation(XStreamAlias.class);
								Class<?> params[] = {};
								Method method = testAttrib.getClass().getDeclaredMethod("get" + annotation.value(), params);
								Object ret = method.invoke(testAttrib, new Object[] {}); 
								measurementValue = (ret == null)? "" : ret.toString();
								measurementValuesMap.put((test.getId() + "." + testAttrib.getAtt() + "." + annotation.value()).trim(), measurementValue);
							}	
						}
					}
				}
			}       			  			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return measurementValuesMap;
	}

	public String getListenerName() {
		return getClass().getSimpleName();
	}
	
	public void processIpuUnit(StringBuffer message) {
		String messageId = "";
		try {
			int xmlStartIndex = getXmlStartIndex(message);
			messageId = message.substring(0, xmlStartIndex).trim();
			getLogger().info("Received UnitInTest from IPU QA tester device. Adding to process queue: " + messageId);
			enqueue(message);
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error("Could not process IPU QA tester device data: " + messageId);
		}
	}
	
	public DeviceFormatDao getDeviceFormatDao() {
		if (_deviceFormatDao == null) {
			_deviceFormatDao = ServiceFactory.getDao(DeviceFormatDao.class);
		}
		return _deviceFormatDao;
	}
	
	private PartSpecDao getPartSpecDao() {
		if (_partSpecDao == null) {
			_partSpecDao = ServiceFactory.getDao(PartSpecDao.class);
		}
		return _partSpecDao;
	}

	public DataCollectionController getController() {
		if (_controller == null)
			return DataCollectionController.getInstance(getApplicationId());
		
		return _controller;
	}

	protected void setDeviceFormatMap(HashMap<String, DeviceFormat> deviceFormatMap) {
		_deviceFormatMap = deviceFormatMap;
	}

	protected HashMap<String, DeviceFormat> getDeviceFormatMap() {
		return _deviceFormatMap;
	}

	protected void setMeasurementValuesMap(HashMap<String, String> measurementValuesMap) {
		_measurementValuesMap = measurementValuesMap;
	}

	protected HashMap<String, String> getMeasurementValuesMap() {
		return _measurementValuesMap;
	}
	
	public String getApplicationId() {
		return _applicationId;
	}
}
