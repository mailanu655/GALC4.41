package com.honda.galc.client.datacollection.processor;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.common.datacollection.data.StatusMessage;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.observer.InstalledPartCache;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.common.exception.ServiceException;
import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.PrintFormDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IPrintDevice;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.device.events.IPrintDeviceListener;
import com.honda.galc.device.printer.AbstractPrintDevice;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.PrintForm;
import com.honda.galc.entity.conf.PrintFormId;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.LotControlRuleFlag;
import com.honda.galc.entity.enumtype.TemplateType;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.Template;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.printing.AttributeConvertor;
import com.honda.galc.service.printing.CompiledJasperPrintAttributeConvertor;
import com.honda.galc.service.printing.IPrintAttributeConvertor;
import com.honda.galc.service.printing.JasperPrintAttributeConvertor;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;

public class EnhancedLabelPrintProcessor extends ProcessorBase implements
		IProductIdProcessor {
	private static final String PART_SN_MESSAGE_ID = "PART_SN";
	public static final String MESSAGE_ID = "PRODUCT";
	public static final String printResult = "PrintCompleted";
	public static final String INSTALLEDPART = "InstalledPart";
	protected InstalledPart installedPart = null;
	protected ProductBean product = null;
	private int partIdx = 0;
	protected InstalledPartDao installedPartDao;
	private List<String> duplicateList = new ArrayList<String>();
	private IPrintDeviceListener printDeviceListener;

	private DataContainer dc;

	public EnhancedLabelPrintProcessor(ClientContext context) {
		super(context);
		init();
	}

	public void init() {
		installedPart = new InstalledPart();
		installedPart.setAssociateNo(context.getUserId());
		installedPart.setMeasurements(new ArrayList<Measurement>());
		partIdx = 0;
		duplicateList.clear();
	}

	public synchronized boolean execute(InputData data) {
		boolean retval = false;
		if (data instanceof ProductId) {
			retval = execute((ProductId) data);
		} else if (data instanceof PartSerialNumber) {
			retval = execute((PartSerialNumber) data);
		}
		return retval;
	}

	public synchronized boolean execute(PartSerialNumber partnumber) {
		Logger.getLogger().debug(
				"EnhancedLabelPrintProcessor : Enter confirmPartSerialNumber");
		try {
			DataCollectionState currentState = (DataCollectionState) getController()
					.getState();
			if (currentState instanceof ProcessTorque) {
				getController()
						.getFsm()
						.error(
								new Message(
										"Unexpected Part Serial number scan received, waiting for Torque"));
				return false;
			} else if (currentState instanceof ProcessProduct) {
				getController()
						.getFsm()
						.error(
								new Message(
										"Unexpected Part Serial number scan received, waiting for Product scan"));
				return false;
			}

			Logger.getLogger().info("Process part:" + partnumber.getPartSn());
			confirmPartSerialNumber(partnumber);
			getController().getFsm().partSnOk(installedPart);

			Logger
					.getLogger()
					.debug(
							"EnhancedLabelPrintProcessor:: Exit confirmPartSerialNumber ok");
			return true;

		} catch (TaskException te) {
			Logger.getLogger().error(te.getMessage());
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().partSnNg(installedPart,
					PART_SN_MESSAGE_ID, te.getMessage());
		} catch (SystemException se) {
			Logger.getLogger().error(se, se.getMessage());
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().error(
					new Message(PART_SN_MESSAGE_ID, se.getMessage()));
		} catch (Exception e) {
			Logger.getLogger().error(
					e,
					"ThreadID = " + Thread.currentThread().getName()
							+ " :: execute() : Exception : " + e.toString());
			getController().getFsm()
					.error(new Message("MSG01", e.getMessage()));
		} catch (Throwable t) {
			Logger.getLogger().error(
					t,
					"ThreadID = " + Thread.currentThread().getName()
							+ " :: execute() : Exception : " + t.toString());
			getController().getFsm()
					.error(new Message("MSG01", t.getMessage()));
		}
		Logger
				.getLogger()
				.debug(
						"EnhancedLabelPrintProcessor:: Exit confirmPartSerialNumber ng");
		return false;
	}

	@Override
	public synchronized boolean execute(ProductId productId) {
		Logger.getLogger().debug(
				"EnhancedLabelPrintProcessor : Enter LabelProductId");
		try {
			dc = new DefaultDataContainer();
			dc.put(DataContainerTag.PROCESS_POINT_ID, context
					.getProcessPointId());
			printLabel(productId);
			Logger.getLogger().debug(
					"EnhancedLabelPrintProcessor : Enter LabelProductId OK");
			return true;
		} catch (TaskException te) {
			Logger.getLogger().error(te.getMessage());
			product.setValidProductId(false);
			getController().getFsm().productIdNg(product, te.getTaskName(),
					te.getMessage());
		} catch (SystemException se) {
			Logger.getLogger().error(se, se.getMessage());
			getController().getFsm().error(
					new Message(MESSAGE_ID, se.getMessage()));
		} catch (Exception e) {
			Logger.getLogger().error(e, e.getMessage());
			getController().getFsm().error(
					new Message("MSG01", e.getCause().toString()));
		} catch (Throwable t) {
			Logger.getLogger().error(t, t.getMessage());
			getController().getFsm().error(
					new Message("MSG01", t.getCause().toString()));
		}

		Logger.getLogger().debug(
				"EnhancedLabelPrintProcessor : Exit LabelProductId ng");
		return false;
	}

	public void printLabel(ProductId productId) {
		try {
			if (StringUtils.isBlank(getController().getCurrentLotControlRule()
					.getDeviceId())) {
				getController().getFsm().message(
						new Message(printResult,
								"DeviceId not found in LotControlRule ",
								MessageType.WARN));
				Logger.getLogger()
						.info("DeviceId not found in LotControlRule ");
				return;
			}
			AbstractPrintDevice device = (AbstractPrintDevice) DeviceManager
					.getInstance().getDevice(
							getController().getCurrentLotControlRule()
									.getDeviceId());
			if (device != null) {
				if (!device.isEnabled()) {

					getController().getFsm().message(
							new Message(printResult, "Device not enabled",
									MessageType.WARN));
					Logger.getLogger().info("Device is not enabled");
					return;
				}
			}

			if (validateLabels(productId, device)) {
				if (getController().getCurrentLotControlRule()
						.getSerialNumberScanFlag() <= 0) {
					installedPart.setValidPartSerialNumber(true);
					getController().getFsm().partSnOk(installedPart);
				}
			}

		} catch (TaskException te) {
			throw te;
		} catch (Exception e) {
			throw new TaskException(e.getClass().toString(), this.getClass()
					.getSimpleName());
		}

	}

	private boolean validateLabels(ProductId productId,
			AbstractPrintDevice device) {
		boolean check = false;
		List<String> labels = new ArrayList<String>();
		String form = getController().getCurrentLotControlRule()
				.getPartNameString().trim();
		String processPointId = ApplicationContext.getInstance()
				.getProcessPointId().toString().trim();
		int prePrintQty = device.getPrePrintQty();
		if (prePrintQty == 0) {
			String label = productId.getProductId();
			check = checkPrintingStatus(label, form, prePrintQty, device);
		} else {
			int maxPrintsPerCycle = device.getMaxPrintsPerCycle();
			Line line = ServiceFactory.getDao(LineDao.class)
					.getByEntryProcessPointId(processPointId);
			if (line != null)
				labels = getEligibleProducts(line.getLineId(), prePrintQty,
						maxPrintsPerCycle, processPointId, form);
			check = labelsPrintingStatus(labels, form, prePrintQty, device);
		}

		return check;
	}

	private boolean labelsPrintingStatus(List<String> labels, String form,
			int prePrintQty, AbstractPrintDevice device) {
		boolean check = false;
		if (labels != null && labels.size() > 0) {
			for (String label : labels) {
				check = false;
				check = checkPrintingStatus(label, form, prePrintQty, device);
				if (!check)
					break;
			}
		} else {
			getController()
					.getFsm()
					.error(
							new Message(
									printResult,
									"No eligible print candidates due to configuration",
									MessageType.INFO));
			Logger.getLogger().info(
					"No eligible print candidates due to configuration");
			if (getController().getCurrentLotControlRule()
					.getSerialNumberScanFlag() <= 0) {
				installedPart.setValidPartSerialNumber(true);
				getController().getFsm().partSnOk(installedPart);
			}

		}
		return check;
	}

	private boolean checkPrintingStatus(String label, String form,
			int prePrintQty, AbstractPrintDevice device) {

		String printData = null;
		String mtoc = null;
		boolean check = false;
		String destinationPrinter = null;
		installedPart.setProductId(label.trim());
		installedPart.setPartName(form);

		dc.put(DataContainerTag.FORM_ID, form.trim());
		dc.put(DataContainerTag.PRODUCT_ID, label.trim());
		dc.put(DataContainerTag.TERMINAL, ApplicationContext.getInstance()
				.getHostName());

		installedPart.setPartId(getController().getCurrentLotControlRule()
				.getParts().get(0).getId().getPartId());

		if (prePrintQty > 0)
			mtoc = ServiceFactory.getDao(InProcessProductDao.class)
					.getProductSpecCode(label);
		else
			mtoc = getController().getState().getProductSpecCode();
		if (mtoc == null) {
			getController().getFsm().message(
					new Message(printResult, "No Mtoc found in DB",
							MessageType.INFO));
			Logger.getLogger().info("No Mtoc found in InProcessProduct table");
			return false;
		}
		dc.put(DataContainerTag.PRODUCT_SPEC_CODE, mtoc.trim());

		destinationPrinter = getDestinationPrinter(form, device);
		if (StringUtils.isEmpty(destinationPrinter)) {
			getLogger().info("DestinationPrinter not configured properly");
			getController().getFsm().message(
					new Message(printResult,
							"DestinationPrinter not configured",
							MessageType.INFO));
			return false;
		}
		dc.put(DataContainerTag.QUEUE_NAME, destinationPrinter);

		String printerName = PropertyService.getProperty(ApplicationContext
				.getInstance().getHostName(), dc.get(
				DataContainerTag.QUEUE_NAME).toString());

		dc.put(DataContainerTag.PRINTER_NAME, printerName);

		String buildAttributeValue = getBuildAttributeValue(form, mtoc);
		if (!setPartSerialNumber(buildAttributeValue))
			return false;
		String template = getTemplate(buildAttributeValue);
		if (template == null) {
			getController().getFsm().message(
					new Message(printResult, "No template found",
							MessageType.INFO));
			Logger.getLogger().info("No Template found in Template table");
			return false;
		}

		if (!hasPrintAttributes(form, buildAttributeValue))
			return false;
		String printQty = DataContainerUtil.getString(dc,
				DataContainerTag.PRINT_QUANTITY, "1");
		try {
			DataContainer dataContainer = new DefaultDataContainer();
			dataContainer = prepareDataFromDataContainer(dc);
			printData = getPrintDataFromDevice(device.getId(), dataContainer);
			if (printData == null) {
				getController().getFsm().message(
						new Message(printResult, "Final PrintData is null",
								MessageType.INFO));
				Logger.getLogger().info("printdata is null");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		check = printToDefaultPrinter(printData, device, Integer
				.parseInt(printQty), label.trim());

		if (check) {
			Logger.getLogger().info("Label Printed Successfully");
		}

		return check;
	}

	private DataContainer prepareDataFromDataContainer(DataContainer dc) {
		DataContainer dataContainer = new DefaultDataContainer();
		dataContainer.put(DataContainerTag.FORM_ID, dc
				.get(DataContainerTag.FORM_ID));
		dataContainer.put(DataContainerTag.TEMPLATE_NAME, dc
				.get(DataContainerTag.TEMPLATE_NAME));
		dataContainer.put(DataContainerTag.PRINTER_NAME, dc
				.get(DataContainerTag.PRINTER_NAME));
		Map<String, String> map = new HashMap<String, String>();
		map.putAll(DataContainerUtil.getAttributeMap(dc));
		dataContainer.put("KEY_VALUE_PAIR", map);

		return dataContainer;
	}

	private String getPrintDataFromDevice(String deviceName, DataContainer dc) {
		IPrintDevice iPrintDevice = null;

		iPrintDevice = (IPrintDevice) DeviceManager.getInstance().getDevice(
				deviceName);
		AbstractPrintDevice printDevice = (AbstractPrintDevice) iPrintDevice;
		return printDevice.getprintData(dc);
	}

	private String getTemplate(String templateName) {
		String tempName = null;
		TemplateDao templateDao = ServiceFactory.getDao(TemplateDao.class);
		Template template = templateDao
				.findTemplateByTemplateName(templateName);
		if (template != null && template.getTemplateDataBytes() != null) {
			tempName = template.getTemplateDataString();
			dc.put(DataContainerTag.TEMPLATE_NAME, template.getTemplateName());
		}
		return tempName;
	}

	private List<String> getEligibleProducts(String lineId, int prePrintQty,
			int maxPrintCycle, String processPointId, String partName) {
		List<String> eligibleLabels = new ArrayList<String>();
		List<? extends Product> list = new ArrayList<Product>();
		try {
			list = context.getDbManager().findProductByPartName(lineId,
					prePrintQty, maxPrintCycle, processPointId, partName);
		} catch (ServiceInvocationException sie) {
			Logger
					.getLogger()
					.info(
							"Exception occured to get Eligible Products from DB due to deadlock or timeout");
			sie.printStackTrace();
		} catch (Exception ex) {
			Logger.getLogger().info(
					"Exception occured to get Eligible Products from DB");
			ex.printStackTrace();
		}
		for (Product prod : list) {
			eligibleLabels.add(prod.getId());
			Logger.getLogger().info(
					"Product Eligible to print :: " + prod.getId());
		}

		return eligibleLabels;
	}

	private String getDestinationPrinter(String form, AbstractPrintDevice device) {
		String destinationPrinter = null;
		String availPrinters = null;
		availPrinters = device.getDestinationPrinter();
		if (!StringUtils.isBlank(availPrinters)) {
			PrintForm printform = getPrintFormDao().findByKey(
					new PrintFormId(form, availPrinters));
			if (printform != null) {
				destinationPrinter = device.getDestinationPrinter().toString()
						.trim();

			} else {
				getController().getFsm().message(
						new Message(printResult,
								"Device not found in print form",
								MessageType.INFO));
				Logger.getLogger().info("Device not found in print form");
			}
		} else {
			getController().getFsm().message(
					new Message(printResult,
							"Destination Printer not found in Configuration",
							MessageType.INFO));
			Logger.getLogger().info(
					"Destination Printer not found in Configuration");
		}
		return destinationPrinter;
	}

	private String getBuildAttributeValue(String attribute, String mtoc) {
		String buildAttributeValue = null;
		BuildAttribute buildAttribute = null;
		BuildAttributeDao buildAttributeDao = ServiceFactory
				.getDao(BuildAttributeDao.class);
		buildAttribute = buildAttributeDao.findById(attribute, mtoc);
		if (buildAttribute != null) {
			buildAttributeValue = buildAttribute.getAttributeValue();
		}
		return buildAttributeValue;
	}

	private boolean setPartSerialNumber(String buildAttributeValue) {
		if (buildAttributeValue == null) {
			getController().getFsm().message(
					new Message(printResult, "No BuildAttributeValue found",
							MessageType.INFO));
			Logger.getLogger().info(
					"BuildAttributeValue not found in GAL259TBX");
			return false;
		}
		installedPart.setPartSerialNumber(buildAttributeValue);
		installedPart.setValidPartSerialNumber(true);
		installedPart.setPartId(getController().getCurrentLotControlRule()
				.getParts().get(0).getId().getPartId());
		return true;
	}

	private Boolean hasPrintAttributes(String form, String templateName) {
		Template template = null;
		try {

			template = ServiceFactory.getDao(TemplateDao.class).findByKey(
					templateName);

			dc = getDataAssembler(template).convertFromPrintAttribute(form, dc);

		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Error occured at print attributes check");
		}
		List<String> templateParsedAttributes = getDataAssembler(template)
				.parseTemplateData(template.getTemplateDataString());
		if (DataContainerUtil.getAttributeMap(dc) == null) {
			if (templateParsedAttributes == null)
				return true;
			else {
				Logger.getLogger().info("No Template Attributes found");
				getController().getFsm().message(
						new Message(printResult,
								"No Template Attributes found",
								MessageType.INFO));
				return false;
			}
		}
		return true;
	}

	private IPrintAttributeConvertor getDataAssembler(Template template) {
		if (TemplateType.JASPER.toString().equalsIgnoreCase(template.getTemplateTypeString()))
			return new JasperPrintAttributeConvertor(Logger.getLogger());
		else if (TemplateType.COMPILED_JASPER.toString().equalsIgnoreCase(template.getTemplateTypeString()))
			return new CompiledJasperPrintAttributeConvertor(Logger.getLogger());
		else
			return new AttributeConvertor(Logger.getLogger());
	}

	private boolean printToDefaultPrinter(String printData,
			AbstractPrintDevice device, int printQty, String productId) {
		boolean check = false;
		if (device.getId() != null) {
			check = sendToPrintDevice(device, printData, printQty, productId);
		}
		return check;
	}

	private boolean sendToPrintDevice(AbstractPrintDevice printDevice,
			String dataToPrint, int printQty, String productId) {
		boolean isPrintSuccessful = false;
		try {
			PrintForm printform = getPrintFormDao().findByKey(
					createPrintFormId(printDevice));

			if (printform != null) {
				if (!printDevice.isActive()) {
					printDevice.activate();
					printDevice.requestControl(context.getAppContext()
							.getApplicationId(), printDeviceListener);
				}
				if (printDevice.isActive() && printDevice.isConnected()) {
					isPrintSuccessful = printDevice.print(dataToPrint,
							printQty, productId);
				}
			} else {
				Logger.getLogger().warn("Print Form not configured for device");
				getController().getFsm()
						.message(
								new Message(printResult,
										"Print Form not configured for the device: "
												+ printDevice.getId(),
										MessageType.WARN));
			}

			if (isPrintSuccessful) {
				installedPart.setInstalledPartStatus(InstalledPartStatus.BLANK);
				installedPart.setActualTimestamp(new Timestamp(System
						.currentTimeMillis()));
				getInstalledPartDao().save(installedPart);
				getController().getFsm().message(
						new Message(printResult, "Template sent to printer: "
								+ printDevice.getId(), MessageType.INFO));
				Logger.getLogger().check(
						"Template sent to printer: " + printDevice.getId());
			} else {
				Logger.getLogger().error("Printing Failed");
				getController().getFsm().message(
						new Message(printResult, "Printing Failed"
								+ printDevice.getId(), MessageType.ERROR));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error(ex, "Printing Failed");
		}

		return isPrintSuccessful;
	}

	private PrintFormId createPrintFormId(AbstractPrintDevice printDevice) {
		return new PrintFormId(dc.get(DataContainerTag.FORM_ID).toString(),
				printDevice.getDestinationPrinter());
	}

	private PrintFormDao getPrintFormDao() {
		return ServiceFactory.getDao(PrintFormDao.class);
	}

	public void registerDeviceListener(DeviceListener listener) {
		this.printDeviceListener = (IPrintDeviceListener) listener;
		registerPrintDevices(printDeviceListener);
	}

	private void registerPrintDevices(IPrintDeviceListener listener) {
		for (IDevice device : DeviceManager.getInstance().getDevices().values()) {
			if (device instanceof IPrintDevice) {
				((IPrintDevice) device).registerListener(context
						.getAppContext().getApplicationId(), listener);
				device.setApplicationId(context.getAppContext()
						.getApplicationId());
				((IPrintDevice) device).requestControl(context.getAppContext()
						.getApplicationId(), listener);
			}
		}
	}

	protected InstalledPartDao getInstalledPartDao() {
		if (installedPartDao == null)
			installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);

		return installedPartDao;
	}

	protected boolean confirmPartSerialNumber(PartSerialNumber partnumber) {
		checkPartSerialNumber(partnumber);

		if (isCheckDuplicatePart())
			checkDuplicatePart(partnumber.getPartSn());

		installedPart.setValidPartSerialNumber(true);
		return true;
	}

	private void checkPartSerialNumber(PartSerialNumber partnumber) {

		checkPartSerialNumberIsNotNull(partnumber);
		installedPart.setPartSerialNumber(partnumber.getPartSn());

		checkPartSerialNumberMask();
	}

	private void checkPartSerialNumberIsNotNull(PartSerialNumber partnumber) {
		if (partnumber == null || partnumber.getPartSn() == null)
			handleException("Received part serial number is null!");
	}

	private void handleException(String info) {
		throw new TaskException(info, this.getClass().getSimpleName());
	}

	private void checkPartSerialNumberMask() {
		List<PartSpec> parts = getController().getState()
				.getCurrentLotControlRulePartList();
		Iterator<PartSpec> it = parts.iterator();
		PartSpec part = null;
		List<String> masks = new ArrayList<String>();		

		partIdx = 0;
		while (it.hasNext()) {
			part = it.next();
			if (CommonPartUtility.verification(installedPart.getPartSerialNumber(), 
				part.getPartSerialNumberMask(), PropertyService.getPartMaskWildcardFormat(),getController().getState().getProduct().getBaseProduct())) {
				installedPart.setPartIndex(partIdx);
				installedPart.setValidPartSerialNumber(true);
				installedPart.setPartId(part.getId().getPartId());
				return;
			}
			partIdx++;
			masks.add(CommonPartUtility.parsePartMask(part.getPartSerialNumberMask()));
		}
		handleException("Part serial number:"
				+ installedPart.getPartSerialNumber()
				+ " verification failed. Masks:" + masks.toString());
	}

	protected boolean isCheckDuplicatePart() {
		return LotControlRuleFlag.ON.getId() == getController().getState()
				.getCurrentLotControlRule().getSerialNumberUniqueFlag();
	}

	private void checkDuplicatePart(String partnumber) {
		/**
		 * Don't bother to check on server if the part number is null or empty.
		 */
		if (StringUtils.isEmpty(partnumber))
			return;

		// Check duplicate part on Server
		if (context.isOnLine())
			checkDuplicatePartOnServer(partnumber);

		// Still check duplicate part in cache if required
		if (InstalledPartCache.getInstance().getSize() > 0)
			updateDuplicatePartList(InstalledPartCache.getInstance()
					.getDuplicatedParts(
							getController().getState().getCurrentPartName(),
							partnumber));

		if (duplicateList.size() > 0)
			handleDuplicatePart(partnumber, duplicateList);
	}

	private void checkDuplicatePartOnServer(String partnumber) {
		try {
			List<InstalledPart> installedPartList = context.getDbManager()
					.findDuplicatePartsByPartName(
							getController().getState().getCurrentPartName(),
							partnumber);
			duplicateList.clear();
			updateDuplicatePartList(installedPartList);
		} catch (ServiceTimeoutException e) {
			handleServerOfflineException(e);
		} catch (ServiceInvocationException sie) {
			handleServerOfflineException(sie);
		}
	}

	private void handleServerOfflineException(ServiceException se) {
		Logger.getLogger().info(se, "Server OffLine detected.");
		EventBus
				.publish(new StatusMessage(StatusMessage.SERVER_ON_LINE, false));
		context.setOffline();
	}

	private void updateDuplicatePartList(List<InstalledPart> installedPartList) {
		if (installedPartList == null)
			return;

		String productId = getController().getState().getProductId();
		for (InstalledPart p : installedPartList) {
			if (p != null && !(p.getId().getProductId().equals(productId)))
				duplicateList.add(p.getId().getProductId());
		}
	}

	private void handleDuplicatePart(String partnumber,
			List<String> duplicateList) {
		StringBuilder userMsg = new StringBuilder("Part:" + partnumber
				+ " already installed on ");

		for (int i = 0; i < duplicateList.size(); i++) {
			userMsg.append(duplicateList.get(i));
			if (i < duplicateList.size() - 1)
				userMsg.append(", ");
		}
		userMsg.append(".");

		handleException(userMsg.toString());
	}
}
