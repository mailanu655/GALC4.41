package com.honda.galc.oif.task;

/**
 * 
 * <h3>GAL103InvoiceTask</h3>
 * <p> GAL103InvoiceTask is for GAL103 </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Vivek Bettada<br>
 * January 19, 2015
 *
 */

import java.io.File;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import java.util.Set;

import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.oif.InvoiceDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameMTOCPriceMasterSpecDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.HostMtocDao;
import com.honda.galc.dao.product.IdCreateDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.PurchaseContractDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.oif.Invoice;
import com.honda.galc.entity.oif.InvoiceDetail;
import com.honda.galc.entity.oif.InvoiceSummary;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameMTOCPriceMasterSpec;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.HostMtoc;
import com.honda.galc.entity.product.IdCreate;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.PurchaseContract;
import com.honda.galc.oif.dto.IOutputFormat;
import com.honda.galc.oif.dto.InvoiceDTO;
import com.honda.galc.oif.dto.InvoiceHeaderDTO;
import com.honda.galc.oif.dto.InvoiceSummaryDTO;
import com.honda.galc.oif.dto.InvoiceTailDTO;
import com.honda.galc.property.VQShipProcessorBean;
import com.honda.galc.service.CommonNameService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.OIFConstants;

public class GAL103InvoiceTask  extends OifTask<Object> implements IEventTaskExecutable {

	private static String serviceId = null;
	private boolean isDebug = false;
	private OifErrorsCollector errorsCollector;
	Boolean JapanVINLeftJustified = getPropertyBoolean(JAPAN_VIN_LEFT_JUSTIFIED, false);
	private VQShipProcessorBean vqShipPropertyBean;

	private int batchSeqNo = 1;
	/*
	 * String that contains the shipment number
	 * */
	/*
	 * Invoice Number
	 */
	private long invoiceNo = 1;
	/*
	 * Integer that contains the number of invoice records that have been created
	 */
	public GAL103InvoiceTask(String componentId) {
		super(componentId);
		serviceId = componentId;
		vqShipPropertyBean = PropertyService.getPropertyBean(VQShipProcessorBean.class, componentId);
	}

	public void execute(Object[] args) {
		initialize();
		exportRecord();
	}

	public void initialize() {
		logger.info("initializing GAL103InvoiceTask.");
		isDebug = getPropertyBoolean("DEBUG", true);

		if (isDebug) {
			logger.info("RES is set to "
					+ PropertyService.getProperty(
							OIFConstants.OIF_SYSTEM_PROPERTIES,
							OIFConstants.SEND));
			logger.info("GAL103SecondaryVIN is set to "
					+ getProperty("GAL103_MQ_INTERFACE_ID"));
			logger.info("ACTIVE_LINES is set to " + getProperty("ACTIVE_LINES"));
			logger.info("ACTIVE_LINES_URLS is set to "
					+ getProperty("ACTIVE_LINES_URLS"));

			logger.info("MQ_CONFIG is set to "
					+ PropertyService.getProperty(
							OIFConstants.OIF_SYSTEM_PROPERTIES,
							OIFConstants.MQ_CONFIG));
			logger.info("COLLECT_PROCESS_POINT is set to "
					+ getProperty("COLLECT_PROCESS_POINT"));
		}
		errorsCollector = new OifErrorsCollector(serviceId);
	}
	
	private String generateKey(HostMtoc hostMtoc){
		StringBuilder sb = new StringBuilder();
		sb.append(hostMtoc.getSalesModelCode())
		  .append(hostMtoc.getSalesModelTypeCode())
		  .append(hostMtoc.getSalesExtColorCode());
		return sb.toString();
	}

	private void exportRecord() {

		Timestamp now = getCurrentTime(true);
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis(now.getTime());
		// data from each line
		String[] activeLinesArr = getProperty("ACTIVE_LINES").split(",");
		List<String>  outRec = new ArrayList<String>();
		String processPoint60A = PropertyService.getProperty(serviceId, "PROCESS_POINT_60A", "");
		String pp75A = PropertyService.getProperty(serviceId, "SHIP_CONFIRM_75A", "SHIPCONFIRM75A");
		Invoice thisInv = null;
		boolean isTimeRange = false;
		
		Timestamp startTs = getPropertyTimestamp("START_TIMESTAMP");
		Timestamp endTs = getPropertyTimestamp("END_TIMESTAMP");
		
		HashMap<String,List<InvoiceDTO>> summaryMap = new HashMap<String,List<InvoiceDTO>>();
		ArrayList<InvoiceDTO>  masterDtoList = new ArrayList<InvoiceDTO>();
		
		if(startTs != null)  {
			if(endTs == null)  {
				endTs = now;
			}
			isTimeRange = true;
		}
		try {
			for (int i = 0; i < activeLinesArr.length; i++) {
				if(summaryMap != null)  {
					summaryMap.clear();
				}
				if(masterDtoList != null)  {
					masterDtoList.clear();
				}
			//create Invoice List

				ShippingStatusDao daoShipStatus = ServiceFactory.getDao(ShippingStatusDao.class);
				//get ShippingStatus records that are ship-confirmed (75A) and not invoiced.
				List<ShippingStatus> shipStatusList = null;
				if(isTimeRange)  {
					shipStatusList = daoShipStatus.findNotInvoicedBy75ATimestamp(4, startTs, endTs, pp75A);
				}
				else  {
					shipStatusList = daoShipStatus.findNotInvoicedByShippingStatus(4);
				}
				int listSize = 0;
				if(shipStatusList != null) listSize = shipStatusList.size();
				logger.info(String.format("retrieved uninvoiced: %d", listSize));
				if(listSize == 0)  {
					logger.info(String.format("No uninvoiced records founds...exiting"));
					return;
				}
				
				int count = 1;
				generateInvoiceSeqNumber();
				InvoiceHeaderDTO invHeader = createHeader(cal);
				thisInv = invHeader.getInvoice();
				thisInv.setInvoiceNo(invoiceNo);
				String opFormatDefKey = getProperty(OIFConstants.OUTPUT_FORMAT_DEFS);
				List<InvoiceHeaderDTO> headerDTOList = new ArrayList<InvoiceHeaderDTO>();
				headerDTOList.add(invHeader);
				List<String> headerRecList = createOutputRecords(InvoiceHeaderDTO.class, headerDTOList, opFormatDefKey);
				outRec.addAll(headerRecList);
				
				
				PurchaseContractDao purchaseDao = ServiceFactory.getDao(PurchaseContractDao.class);
				Map<String, List<PurchaseContract>> currentPurchaseContracts = new HashMap<String,List<PurchaseContract>>();
				HostMtocDao hostMtocDao = ServiceFactory.getDao(HostMtocDao.class);
				Map<String, HostMtoc> salesMtocMap = new HashMap<String,HostMtoc>();
				for(ShippingStatus status : shipStatusList){
					//get Sales information; for each Vin 
					List<HostMtoc> salesMtocList = hostMtocDao.findBySpecCode(status.getVin());
					if(salesMtocList != null && !salesMtocList.isEmpty())
					{
						HostMtoc hostMtoc = salesMtocList.get(0);
						salesMtocMap.put(status.getVin(), hostMtoc);
						//get current PO information
						String key = generateKey(hostMtoc);
						if(!currentPurchaseContracts.containsKey(key)){
							List<PurchaseContract> poList = purchaseDao.findBySpecCode(hostMtoc.getSalesModelCode(),
								hostMtoc.getSalesModelTypeCode(),
								hostMtoc.getSalesExtColorCode());
							if(poList != null && !poList.isEmpty()){
								currentPurchaseContracts.put(key, poList);
							}
						}
					}
				}
				//prepare to update current purchase orders with ship unit quantity
				FrameDao fDao = ServiceFactory.getDao(FrameDao.class);
				int numDetails = 0;
				for(ShippingStatus thisShipStatus : shipStatusList)  {
					String vin = thisShipStatus.getVin();
					//do not use shop calendar for production date, use actual timestamp instead
					//some departments like PMC/VQ don't have 226 work schedule
					FrameMTOCPriceMasterSpecDao priceDao = ServiceFactory.getDao(FrameMTOCPriceMasterSpecDao.class);
					FrameMTOCPriceMasterSpec price = priceDao.findByProductIdAndSpecCode(vin, processPoint60A);
					
					if(price == null)  {
						StringBuilder sb = new StringBuilder();
						String err = String.format("Price could not be found for vin: %s", vin);
						sb.append(err);
						sb.append("\nThis VIN will not be invoiced\n");
						logger.error(sb.toString());
						errorsCollector.error(sb.toString());
						setJobStatus(OifRunStatus.COMPLETE_WITH_ERRORS);
						continue;
					}
					if(salesMtocMap != null && !salesMtocMap.isEmpty() && salesMtocMap.get(vin) != null)  {
						String pcKey = generateKey(salesMtocMap.get(vin));
						PurchaseContract pContract = null;
						//Loop through the list where ship_unit < order_unit found for the Purchase order
						if(currentPurchaseContracts.containsKey(pcKey)){
							List<PurchaseContract> pcList = currentPurchaseContracts.get(pcKey);
							for(PurchaseContract contract : pcList){
								if(contract.getShipUnit() < contract.getOrderUnit()){
									pContract = contract;
									break;
								}
							}
						}
						
						if(pContract == null)  {
							StringBuilder sb = new StringBuilder();
							String err = String.format("PurchaseContract could not be found for vin: %s", vin);
							sb.append(err);
							sb.append("\nA blank AHM_invoice_number will be sent.  No action is needed\n");
							logger.error(sb.toString());
							errorsCollector.error(sb.toString());
							setJobStatus(OifRunStatus.COMPLETE_WITH_ERRORS);
						}
						
						Frame frame = fDao.findByKey(vin);
						InvoiceDTO thisInvDTO = createInvoiceDetail(salesMtocMap.get(vin), price, thisShipStatus, frame, pContract );
						if(thisInvDTO == null) continue;
						numDetails++;
						thisInvDTO.setDtlBatchSequence(String.format("%6d", count++));
						if(summaryMap.containsKey(price.getQuotationNo().trim()))  {
							summaryMap.get(price.getQuotationNo()).add(thisInvDTO);
						}
						else  {
							List<InvoiceDTO> dtoList = new ArrayList<InvoiceDTO>();
							dtoList.add(thisInvDTO);
							summaryMap.put(price.getQuotationNo(), dtoList);
						}
						masterDtoList.add(thisInvDTO);
						InvoiceDetail invDetail = thisInvDTO.getInvoiceDetail();
						invDetail.getId().setInvoiceNo(invoiceNo);
						if(thisInv != null)  {
							thisInv.addInvoiceDetail(invDetail);
						}
					}
				}
				thisInv.setNumDtlRec(numDetails);
				if(masterDtoList != null && !masterDtoList.isEmpty())  {
					List<InvoiceSummaryDTO> summaryDtoList = createInvoiceSummaries(summaryMap, cal);
					List<String> invRecList = createOutputRecords(InvoiceDTO.class, masterDtoList, opFormatDefKey);
					outRec.addAll(invRecList);
					if(summaryDtoList != null && !summaryDtoList.isEmpty())  {
						List<String> sumRecList = createOutputRecords(InvoiceSummaryDTO.class, summaryDtoList, opFormatDefKey);
						outRec.addAll(sumRecList);
						for(InvoiceSummaryDTO invSumDto : summaryDtoList)  {
							InvoiceSummary invS = invSumDto.getInvoiceSummary();
							invS.getId().setInvoiceNo(invoiceNo);

							thisInv.addInvoiceSummary(invS);
						}
						thisInv.setNumSummaryRec(summaryDtoList.size());
					}
					InvoiceTailDTO invTail = createTail(outRec.size());
					List<InvoiceTailDTO>tailDTOList = new ArrayList<InvoiceTailDTO>();
					tailDTOList.add(invTail);
					List<String> tailRecList = createOutputRecords(InvoiceTailDTO.class, tailDTOList, opFormatDefKey);
					outRec.addAll(tailRecList);
					updateComponentStatus("LAST_INVOICE_NO", String.valueOf(invoiceNo));
					updateLastProcessTimestamp(now);
					logger.info("Finished creating invoice, about to send");
					this.sendInvoice(outRec,listSize);
					setInvoiced(shipStatusList); 
					savePurchaseContracts(currentPurchaseContracts);
					InvoiceDao invoiceDao = ServiceFactory.getDao(InvoiceDao.class);
					invoiceDao.insert(thisInv);
				}
			}
		} catch (Exception e) {
			String error = "An Exception occurred while updating the invoice :"+e.getMessage();
			logger.error(error);
			errorsCollector.emergency(error);
		} finally {
			errorsCollector.sendEmail();
			if(summaryMap != null)  {
				summaryMap.clear();
			}
			if(masterDtoList != null)  {
				masterDtoList.clear();
			}
		}
	}
	
	
	private void setInvoiced(List<ShippingStatus> shipStatusList)  {
	
		ShippingStatusDao daoShipStatus = ServiceFactory.getDao(ShippingStatusDao.class);
		int count = 0;
		for (ShippingStatus shipStatus : shipStatusList)  {
			if(shipStatus != null)  {
				daoShipStatus.save(shipStatus);
				logger.info("Saved shipping status (263):" + shipStatus.toString());
				count++;
			}
		}
		logger.info("Saved shipping status count:" + count);
	}
	
	
	public void savePurchaseContracts(Map<String,List<PurchaseContract>> purchaseContracts)  {
		PurchaseContractDao pcDao = ServiceFactory.getDao(PurchaseContractDao.class);
		int count = 0;
		Set<Entry<String, List<PurchaseContract>>> pcSet = purchaseContracts.entrySet();
		for(Entry<String, List<PurchaseContract>> entry : pcSet)  {
			if(entry != null && entry.getValue() != null && !entry.getValue().isEmpty())  {
				List<PurchaseContract> pcList = entry.getValue();
				for(PurchaseContract contract : pcList)
				{
					pcDao.save(contract);
					logger.info("Saved purchase contract (313):" + contract.toString());
					count++;
				}
			}
		}
		logger.info("Saved purchase contract count:" + count);
	}
	
	private InvoiceDTO createInvoiceDetail(HostMtoc thisSalesMtoc, FrameMTOCPriceMasterSpec price,
				ShippingStatus thisShipStatus, Frame frame, PurchaseContract pContract )  {
		
		if(thisSalesMtoc == null)  return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
		DecimalFormat seqFormatter = new DecimalFormat("00");
		Timestamp shipConfirmTs = null;
		
		InvoiceDTO thisInvDTO = new InvoiceDTO();
		Date shipDate = thisShipStatus.getActualTimestamp();
		String productId = thisShipStatus.getVin();
		thisInvDTO.setDtlRecordId("D");
		thisInvDTO.setDtlTranceCode("013");
		if(shipDate != null)  {
			shipConfirmTs = new Timestamp(shipDate.getTime());
			thisInvDTO.setShippingTs(shipConfirmTs);
		}
		thisInvDTO.setDtlQuotationNo(rtrim(price.getQuotationNo()));
		thisInvDTO.setDtlPriceType(rtrim(price.getPriceType()));
		thisInvDTO.setDtlAhDestination(rtrim(thisSalesMtoc.getSalesModelTypeCode()));
		thisInvDTO.setDtlAhModel(rtrim(thisSalesMtoc.getSalesModelCode()));
		thisInvDTO.setDtlAhOption(rtrim(thisSalesMtoc.getSalesModelOptionCode()));
		thisInvDTO.setDtlAhColor(rtrim(thisSalesMtoc.getSalesExtColorCode()));
		thisInvDTO.setDtlInvoiceNo(String.format("%09d", invoiceNo));
		thisInvDTO.setInspectTs(getInSpectionTimestamp(thisShipStatus.getVin()));			
		
		String dtlEngineNo = "";
		if(frame != null)  {
			thisInvDTO.setDtlFrameNo(ProductNumberDef.justifyJapaneseVIN(productId, JapanVINLeftJustified.booleanValue()));
			String keyNo = getKeyNo(productId, frame);
			if(keyNo != null)  {
				keyNo = keyNo.trim();
				if(keyNo.length() > 4)  {
					int len = keyNo.length();
					keyNo = keyNo.substring(len-4, len);
				}
			}

			thisInvDTO.setDtlKeyNo(String.format("%4.4s", keyNo.trim()));
			dtlEngineNo = rtrim(frame.getEngineSerialNo());
			if(dtlEngineNo != null)  {
				if(dtlEngineNo.length() > 7)  {
					int len = dtlEngineNo.length();
					dtlEngineNo = dtlEngineNo.substring(len-7, len);
				}
			}
			else  {
				dtlEngineNo = "";
			}
			thisInvDTO.setDtlEngineNo(dtlEngineNo);
		}
		thisInvDTO.setDtlCorrectionFlag("I");
		thisInvDTO.setDtlReceiptDate("000000");
		thisInvDTO.setDtlReceiptTime("000000");
		thisInvDTO.setDtlValidationDate("000000");
		thisInvDTO.setDtlFinalDate("000000");
		thisInvDTO.setDtlFinalReceiptFlg("F");
		
		String sPrice = "0000000000000000";
		if(price != null && price.getPrice() != null)  {
			sPrice = price.getPrice();
		}
		StringBuffer salePrice = new StringBuffer(sPrice);
		if(salePrice != null && salePrice.indexOf(".") < 0 && salePrice.length() > 4)  {
			salePrice.insert(salePrice.length()-4, '.');
		}
		double thisPrice = Double.parseDouble(salePrice.toString());
		String dtlPrice = String.format("%.2f", thisPrice);
		thisPrice = Double.parseDouble(dtlPrice);
		int priceFieldLength = PropertyService.getPropertyInt(serviceId, "PRICE_FIELD_LENGTH", 8);
		dtlPrice = String.format("%0"+priceFieldLength+".0f", thisPrice*100);
		
		thisInvDTO.setPrice(thisPrice);
		thisInvDTO.setDtlPrice(dtlPrice);
		FrameSpecDao fSpecDao = ServiceFactory.getDao(FrameSpecDao.class);
		FrameSpec fSpec = fSpecDao.findByKey(frame.getProductSpecCode());
		thisInvDTO.setDtlUniqueIdOption(fSpec.getModelOptionCode());
		String ahmInvoiceNumber = null;
		if(pContract != null)  {
			updatePurchaseContract(pContract, shipConfirmTs);
			String pcPrefix = String.format("%7.7s", pContract.getPurchaseContractNumber());
			ahmInvoiceNumber = pcPrefix + seqFormatter.format(pContract.getInvoiceSequenceNumber());
		}
		else  {
			ahmInvoiceNumber = String.format("%9s", "");
		}
		thisInvDTO.setDtlAhmInvoiceNo(ahmInvoiceNumber);
		//invoice only if there is a PO to update the Ship unit count
		thisShipStatus.setInvoiced("Y");

		String shipNo = getShipmentNo(batchSeqNo);
		thisInvDTO.setDtlFactoryShipmentNo(shipNo);
		return thisInvDTO;
	}
	
	
	private <K extends IOutputFormat> void sendInvoice(List<String> outData, int listSize) throws Exception {
		try {
			String resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.SEND);
			String mqInterfaceID = getProperty(OIFConstants.INTERFACE_ID);
			Timestamp productionTimestamp = new java.sql.Timestamp(
					(new java.util.Date()).getTime());
			String strTimestampPart = new SimpleDateFormat("yyyyMMddHHmmss").format(productionTimestamp);
			String exportFileName = mqInterfaceID + strTimestampPart + ".oif";
			String mqConfig = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.MQ_CONFIG);
			String fileSpec = resultPath + File.separator + exportFileName;

			createExportFile(fileSpec, outData);
			sendFile(fileSpec, mqConfig,listSize);
		} catch (Exception e) {
			String errorStr = "Exception raised when sending interface file "
					+ " for the MQ interface. "
					+"  Exception is: " + e.getStackTrace();
			logger.error(errorStr);
			throw e;
		}
	}
	
	private String getKeyNo(String productId, Frame frame)  {
		String keyNo = "";
		String[] keyPartNames = vqShipPropertyBean.getKeySetPartName();
		if(keyPartNames != null) {
			InstalledPartDao ipDao = ServiceFactory.getDao(InstalledPartDao.class);
			for(int i=0; (i < keyPartNames.length && StringUtils.isBlank(keyNo)); i++) {
				keyNo = ServiceFactory.getService(CommonNameService.class).getLatestPartSerialNumberByCommonName(productId, StringUtils.trimToEmpty(keyPartNames[i]));
			}
		}
		if(keyNo == null || "".equals(keyNo.trim()))  {
			keyNo = frame.getKeyNo();
		}
		if(keyNo == null)  keyNo = "";
		return keyNo;
	}
	
	protected void updatePurchaseContract(
			PurchaseContract pContract,
			Date pProductionDate) {

			String pContractNumber = null;
			Date shipDate = null;
			int ahmInvSeqNum = 0;
			int shipUnit = 0;
			int orderUnit = 0;
			
			if(pContract == null)  return;
			
			try {
				
				//vRowCount = 1;
				pContractNumber = pContract.getPurchaseContractNumber();
				ahmInvSeqNum = pContract.getInvoiceSequenceNumber();
				orderUnit = pContract.getOrderUnit();
				shipUnit = pContract.getShipUnit();
				shipDate = new java.sql.Date(pProductionDate.getTime());
				
				if (isDebug){
				logger.info(
					"getting PurchaseContractNumber "
						+ pContractNumber
						+ " shipDate "
						+ shipDate.toString()
						+ " pProductionDate "
						+ pProductionDate.toString()
						+ " invSequenceNumber "
						+ ahmInvSeqNum
						+ " orderUnit "
						+ orderUnit
						+ " shipUnit "
						+ shipUnit);
				}
				shipUnit++;
				ahmInvSeqNum = (ahmInvSeqNum % 99) + 1;
				pContract.setInvoiceSequenceNumber(ahmInvSeqNum);
				pContract.setShipDate(shipDate);
				pContract.setShipUnit(shipUnit);

			} catch (Exception e) {
				logger.error(e, "exception in updatePurchaseContract");
			}
		}
	
	public List<InvoiceSummaryDTO> createInvoiceSummaries(HashMap<String,List<InvoiceDTO>> summaryMap, Calendar now)  {
		
		List<InvoiceSummaryDTO> summaryDtoList = new ArrayList<InvoiceSummaryDTO>();
		if(summaryMap == null || summaryMap.isEmpty())  return null;
		Timestamp nowTs = new Timestamp(now.getTimeInMillis());
		
		//construct aq calendar for next day
		Calendar nextDay = new GregorianCalendar();
		nextDay.setTimeInMillis(now.getTimeInMillis());
		nextDay.add(Calendar.DAY_OF_YEAR, 1);
		Timestamp nextDayTs = new Timestamp(nextDay.getTimeInMillis());
		
		for(String thisQuotation : summaryMap.keySet())  {
			List<InvoiceDTO> thisInvDtoList = summaryMap.get(thisQuotation);
			if(thisInvDtoList == null)  continue;
			double totalPrice = 0.0D;
			for(InvoiceDTO thisInvDto : thisInvDtoList)  {
				totalPrice += thisInvDto.getPrice();
			}
			InvoiceDTO refInvDto = thisInvDtoList.get(0);
			InvoiceSummaryDTO thisSummary = new InvoiceSummaryDTO();
			String sumPrice = String.format("%011.0f", totalPrice*100);
			thisSummary.setSumPrice(sumPrice);
			//thisSummary.setSumInvoiceNo(refInvDto.getDtlInvoiceNo());
			thisSummary.setProcTs(nowTs);
			thisSummary.setInvoiceTs(nextDayTs);
			thisSummary.setShipTs(refInvDto.getShippingTs());
			thisSummary.setSumQuotationNo(thisQuotation);
			thisSummary.setSumRecordId("S");
			thisSummary.setSumShipmentNo(refInvDto.getDtlFactoryShipmentNo());
			thisSummary.setSumTotalQuantity(String.format("%07d",thisInvDtoList.size()));
			thisSummary.setSumTranceCode("013");
			summaryDtoList.add(thisSummary);
		}
		return summaryDtoList;
	}
	
	public long generateInvoiceSeqNumber()  {
		
		IdCreateDao idCreate = ServiceFactory.getDao(IdCreateDao.class);
		IdCreate id = idCreate.incrementId("GAL160TBX", "INVOICE_NO");
		if(id != null)  {
			invoiceNo = id.getCurrentId();
		}
		return invoiceNo;
	}
	
	public int generateBatchSeqNumber()  {
		
		IdCreateDao idCreate = ServiceFactory.getDao(IdCreateDao.class);
		IdCreate id = idCreate.incrementId("GAL160TBX", "BATCH_SEQ_NO");
		if(id != null)  {
			batchSeqNo = id.getCurrentId();
		}
		return batchSeqNo;
	}
	
	public InvoiceHeaderDTO createHeader(Calendar now)  {
		Timestamp nowTs = new Timestamp(now.getTimeInMillis());
		InvoiceHeaderDTO invHeaderDTO = new InvoiceHeaderDTO();
		invHeaderDTO.setHdrRecordId("H");
		invHeaderDTO.setHdrTransCode("013");
		invHeaderDTO.setBatchTime(nowTs);
		invHeaderDTO.setInvoiceDate(nowTs);
		int batchSeqNo = generateBatchSeqNumber();
		invHeaderDTO.setHdrBatchSequence(String.format("%6d", batchSeqNo));
		return invHeaderDTO;
		
	}

	public InvoiceTailDTO createTail(int totalRec)  {
		InvoiceTailDTO invTailDTO = new InvoiceTailDTO();
		invTailDTO.setTrlRecordId("T");
		invTailDTO.setTrlTransCode("013");
		invTailDTO.setTrlHashTotal(String.format("%6d", totalRec));
		return invTailDTO;
	}


	public String getShipmentNo(int batchSeqNo)  {
		String fShipNo = PropertyService.getProperty(serviceId, "FACTORY_SHP_NO", "");
		String prefix = "";
		StringBuffer shipNo = new StringBuffer();
		if(fShipNo != null && !"".equals(fShipNo))  {
			prefix = fShipNo.substring(0,1);
		}
		shipNo.append(prefix);
		int seqNo = batchSeqNo % 100;
		shipNo.append(String.format("%02d", seqNo));
		return shipNo.toString();	
	}
	
	public String rtrim(String s)  {
		if(s == null) return s;
        String rtrim = s.replaceAll("\\s+$","");
        return rtrim;
	}
	
	public Timestamp getInSpectionTimestamp(String productId) {
		Timestamp inspectTs= null;
		String pp = PropertyService.getProperty(serviceId, "COLLECT_PROCESS_POINT", null);
		String[] ppList =StringUtils.isNotBlank(pp)?StringUtils.split(pp,","):new String[] {};
		ProductResultDao productResultDao = ServiceFactory.getDao(ProductResultDao.class);
		for(String ppId:ppList) {
			List<ProductResult> rList = productResultDao.findAllByProductAndProcessPoint(productId, ppId);
			if(rList != null && !rList.isEmpty())  {
				 inspectTs = rList.get(0).getActualTimestamp();
				 break;
			}
		}
		
		return inspectTs;
	}
}
