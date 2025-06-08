package com.honda.galc.service.msip.handler.outbound;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameMTOCPriceMasterSpecDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.HostMtocDao;
import com.honda.galc.dao.product.IdCreateDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.PurchaseContractDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameMTOCPriceMasterSpec;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.HostMtoc;
import com.honda.galc.entity.product.IdCreate;
import com.honda.galc.entity.product.PurchaseContract;
import com.honda.galc.service.msip.dto.outbound.Gal103DetGroupDto;
import com.honda.galc.service.msip.dto.outbound.Gal103DetailDto;
import com.honda.galc.service.msip.dto.outbound.Gal103Dto;
import com.honda.galc.service.msip.dto.outbound.Gal103HeaderDto;
import com.honda.galc.service.msip.dto.outbound.Gal103SummaryDto;
import com.honda.galc.service.msip.dto.outbound.Gal103TailDto;
import com.honda.galc.service.msip.property.outbound.Gal103PropertyBean;
import com.honda.galc.service.property.PropertyService;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Gal103Handler extends BaseMsipOutboundHandler<Gal103PropertyBean>{
	
	/**
	 * This interface is only scheduled once a day at the end of the day
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Gal103Dto> fetchDetails() {
		List<Gal103Dto> dtoList = new ArrayList<Gal103Dto>();
		try{
			Timestamp now = getDBTimestamp();			
			Gal103Dto dto = new Gal103Dto();		
			List<ShippingStatus> shipStatusList = new ArrayList<ShippingStatus>();
			
			Map<String, List<PurchaseContract>> currentPurchaseContracts = new HashMap<String,List<PurchaseContract>>();			
			Map<String,List<Gal103DetailDto>> summaryMap = new HashMap<String,List<Gal103DetailDto>>();			
			
			int batchSeqNo = generateBatchSeqNumber();
			dto.setGal103HeaderRec(createHeader(now,batchSeqNo));
			shipStatusList = getDao(ShippingStatusDao.class).findNotInvoicedByShippingStatus(4);
			
			List<Gal103DetailDto> invoiceDetails = createInvoiceDetails(shipStatusList, currentPurchaseContracts, summaryMap, batchSeqNo);
			List<Gal103DetGroupDto> summaryDtoList = createInvoiceSummaries(summaryMap, now);
			
			int totalRecords = invoiceDetails.size() + summaryDtoList.size();
			dto.setGal103DetGroup(summaryDtoList);
			dto.setGal103TailerRec(createTail(totalRecords));
			
			// set invoiced record for now - what if MSIP fails to get invoice list? 
			updateInvoiced(shipStatusList);
			
			// update purchase contracts - what if MSIP fails to get invoice list? 
			savePurchaseContracts(currentPurchaseContracts);
			dtoList.add(dto);
			
			return dtoList;
		}catch(Exception e){
			dtoList.clear();
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			Gal103Dto dto = new Gal103Dto();
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			e.printStackTrace();
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}		
	}
	
	private List<Gal103DetailDto> createInvoiceDetails(List<ShippingStatus> shipStatusList, Map<String, List<PurchaseContract>> currentPurchaseContracts,
			Map<String,List<Gal103DetailDto>> summaryMap, int batchSeqNo) {

		List<Gal103DetailDto> invoiceList = new ArrayList<Gal103DetailDto>();
		
		String processPoint60A = PropertyService.getProperty(getComponentId(), "PROCESS_POINT_60A", "PA19024");
		String pp = PropertyService.getProperty(getComponentId(), "COLLECT_PROCESS_POINT", "PA19024");
		
		//create Invoice List
		
		if(shipStatusList==null || shipStatusList.isEmpty())  {
			getLogger().info(String.format("No uninvoiced records founds...exiting"));
			return invoiceList;
		}
		getLogger().info(String.format("retrieved uninvoiced: %d", shipStatusList.size()));
		int count = 1;
		
		int invoiceNo = generateInvoiceSeqNumber();
		
		//prepare to update current purchase orders with ship unit quantity
		for(ShippingStatus thisShipStatus : shipStatusList)  {
			
			String vin = thisShipStatus.getVin();
			List<HostMtoc> salesMtocList = getDao(HostMtocDao.class).findBySpecCode(vin);
			HostMtoc hostMtoc = (salesMtocList != null && !salesMtocList.isEmpty()) ? salesMtocList.get(0) : null;
			if(hostMtoc == null) {
				continue;
			}
			
			//get current PO information
			String pcKey = generateKey(hostMtoc);
			
			//fetch purchase contracts not already cached
			retrievePurchaseContracts(currentPurchaseContracts,pcKey,hostMtoc);			
			
			//do not use shop calendar for production date, use actual timestamp instead
			//some departments like PMC/VQ don't have 226 work schedule
			FrameMTOCPriceMasterSpec price = getDao(FrameMTOCPriceMasterSpecDao.class).findByProductIdAndSpecCode(vin, processPoint60A);
			
			PurchaseContract pContract = getLatestedPurchaseContract(currentPurchaseContracts,pcKey);
			if(pContract == null)  {
				String err = String.format("PurchaseContract could not be found for vin: %s", vin);
				getLogger().error(err);
			}else {
				
				Frame frame = getDao(FrameDao.class).findByKey(vin);
				Gal103DetailDto thisInvDTO = createInvoiceDetail(hostMtoc, price, thisShipStatus, frame, pContract,invoiceNo,batchSeqNo);
				if(thisInvDTO == null) continue;
				thisInvDTO.setDtlBatchSequence(String.format("%6d", count++));
				
				if(summaryMap.containsKey(price.getQuotationNo()))  {
					summaryMap.get(price.getQuotationNo()).add(thisInvDTO);
				}
				else  {
					List<Gal103DetailDto> dtoList = new ArrayList<Gal103DetailDto>();
					dtoList.add(thisInvDTO);
					summaryMap.put(price.getQuotationNo(), dtoList);
				}
				
				invoiceList.add(thisInvDTO);
			}
		}		
		return invoiceList;				
	}
		
	public int generateInvoiceSeqNumber()  {
		
		IdCreateDao idCreate = getDao(IdCreateDao.class);
		IdCreate id = idCreate.incrementId("GAL160TBX", "INVOICE_NO");
		return id != null ? id.getCurrentId() : 1;
		
	}
	
	private Gal103HeaderDto createHeader(Timestamp now, int batchSeqNo) {
		String nowDate = new SimpleDateFormat("yyMMdd").format(now.getTime());
		String nowTime = new SimpleDateFormat("HHmmss").format(now.getTime());
		
		Gal103HeaderDto invHeaderDTO = new Gal103HeaderDto();
		invHeaderDTO.setHdrRecordId("H");
		invHeaderDTO.setHdrTransCode("013");
		invHeaderDTO.setHdrBatchDate(nowDate);
		invHeaderDTO.setHdrBatchTime(nowTime);
		invHeaderDTO.setHdrBatchSequence(String.format("%6d", batchSeqNo));
		return invHeaderDTO;
	}
	
	private int generateBatchSeqNumber()  {
		
		IdCreate id = getDao(IdCreateDao.class).incrementId("GAL160TBX", "BATCH_SEQ_NO");
		return id != null ? id.getCurrentId() : 1;
	}


    private void retrievePurchaseContracts(Map<String, List<PurchaseContract>> currentPurchaseContracts, String pcKey, HostMtoc hostMtoc) {
    	if(currentPurchaseContracts.containsKey(pcKey)) return;
		
    	List<PurchaseContract> poList = getDao(PurchaseContractDao.class).findBySpecCode(hostMtoc.getSalesModelCode(),
					hostMtoc.getSalesModelTypeCode(),
					hostMtoc.getSalesExtColorCode());
		if(poList != null && !poList.isEmpty()) currentPurchaseContracts.put(pcKey, poList);
    }
    
    private PurchaseContract getLatestedPurchaseContract(Map<String, List<PurchaseContract>> currentPurchaseContracts, String pcKey) {
		//Loop through the list where ship_unit < order_unit found for the Purchase order
    	
    	if(!currentPurchaseContracts.containsKey(pcKey)) return null;
    		
    	List<PurchaseContract> pcList = currentPurchaseContracts.get(pcKey);
		for(PurchaseContract contract : pcList){
			if(contract.getShipUnit() < contract.getOrderUnit())return contract;
		}
		
		return null;
    }
    
	private String generateKey(HostMtoc hostMtoc){
		StringBuilder sb = new StringBuilder();
		sb.append(hostMtoc.getSalesModelCode())
		  .append(hostMtoc.getSalesModelTypeCode())
		  .append(hostMtoc.getSalesExtColorCode());
		return sb.toString();
	}

	private List<Gal103DetGroupDto> createInvoiceSummaries(Map<String,List<Gal103DetailDto>> summaryMap, Timestamp now)  {
		List<Gal103DetGroupDto> summaryDtoList = new ArrayList<Gal103DetGroupDto>();
		if(summaryMap == null || summaryMap.isEmpty())  return summaryDtoList;
		String nowDate = new SimpleDateFormat("yyMMdd").format(now.getTime());
		for(String thisQuotation : summaryMap.keySet())  {
			Gal103DetGroupDto groupDto = new Gal103DetGroupDto();
			List<Gal103DetailDto> thisInvDtoList = summaryMap.get(thisQuotation);
			double totalPrice = 0.0D;
			for(Gal103DetailDto thisInvDto : thisInvDtoList)  {
				totalPrice += thisInvDto.getPrice();
			}
			Gal103DetailDto refInvDto = thisInvDtoList.get(0);
			Gal103SummaryDto thisSummary = new Gal103SummaryDto();
			String sumPrice = String.format("%011.0f", totalPrice*100);
			thisSummary.setSumPrice(sumPrice);
			thisSummary.setSumInvoiceDate(nowDate);
			thisSummary.setSumInvoiceNo(refInvDto.getDtlInvoiceNo());
			thisSummary.setSumProcDate(nowDate);
			thisSummary.setSumQuotationNo(thisQuotation);
			thisSummary.setSumRecordId("S");
			thisSummary.setSumShipmentNo(refInvDto.getDtlFactoryShipmentNo());
			thisSummary.setSumShippingDate(refInvDto.getDtlShippingDate());
			thisSummary.setSumTotalQuantity(String.format("%07d",thisInvDtoList.size()));
			thisSummary.setSumTranceCode("013");
			
			groupDto.setGal103SummaryRec(thisSummary);
			groupDto.setGal103DetailRec(thisInvDtoList);
			
			summaryDtoList.add(groupDto);
		}
		return summaryDtoList;
	}
	
	private Gal103DetailDto createInvoiceDetail(HostMtoc thisSalesMtoc, FrameMTOCPriceMasterSpec price,
			ShippingStatus thisShipStatus, Frame frame, PurchaseContract pContract,int invoiceNo,int batchSeqNo )  {
		
		if(thisSalesMtoc == null)  return null;
		Gal103DetailDto thisInvDTO = new Gal103DetailDto();
		
		String productId = thisShipStatus.getVin();
		String actualDate = new SimpleDateFormat("yyMMdd").format(thisShipStatus.getActualTimestamp());
		String actualTime = new SimpleDateFormat("HHmmss").format(thisShipStatus.getActualTimestamp());
		
		thisInvDTO.setDtlRecordId("D");
		thisInvDTO.setDtlTranceCode("013");
		
		thisInvDTO.setDtlInspectionDate(actualDate);
		thisInvDTO.setDtlInspectionTime(actualTime);
		thisInvDTO.setDtlShippingDate(actualDate);
		thisInvDTO.setDtlShippingTime(actualTime);
		thisInvDTO.setDtlQuotationNo(rtrim(price.getQuotationNo()));
		thisInvDTO.setDtlPriceType(rtrim(price.getPriceType()));
		thisInvDTO.setDtlAhDestination(rtrim(thisSalesMtoc.getSalesModelTypeCode()));
		thisInvDTO.setDtlAhModel(rtrim(thisSalesMtoc.getSalesModelCode()));
		thisInvDTO.setDtlAhOption(rtrim(thisSalesMtoc.getSalesModelOptionCode()));
		thisInvDTO.setDtlAhColor(rtrim(thisSalesMtoc.getSalesExtColorCode()));
		thisInvDTO.setDtlInvoiceNo(String.format("%09d", invoiceNo));
		String dtlEngineNo = "";
		if(frame != null)  {
			thisInvDTO.setDtlFrameNo(ProductNumberDef.justifyJapaneseVIN(productId, isJapaneseVinLeftJustified()));
			String keyNo = getKeyNo(productId, frame);
			thisInvDTO.setDtlKeyNo(String.format("%4.4s", keyNo.trim()));
	
			dtlEngineNo = rtrim(frame.getEngineSerialNo());
			if(dtlEngineNo != null)  {
				if(dtlEngineNo.length() > 7)  {
					int len = dtlEngineNo.length();
					dtlEngineNo = dtlEngineNo.substring(len-7, len);
				}
			}
			else dtlEngineNo = "";
			thisInvDTO.setDtlEngineNo(dtlEngineNo);
		}
		thisInvDTO.setDtlCorrectionFlag("I");
		thisInvDTO.setDtlReceiptDate("000000");
		thisInvDTO.setDtlReceiptTime("000000");
		thisInvDTO.setDtlValidationDate("000000");
		thisInvDTO.setDtlFinalDate("000000");
		thisInvDTO.setDtlFinalReceiptFlg("F");
		
		double thisPrice = calculatePrice(price);
		String dtlPrice = String.format("%08.0f", thisPrice*100);
		thisInvDTO.setPrice(thisPrice);
		thisInvDTO.setDtlPrice(dtlPrice);
		FrameSpec fSpec = getDao(FrameSpecDao.class).findByKey(frame.getProductSpecCode());
		thisInvDTO.setDtlUniqueIdOption(fSpec.getModelOptionCode());
		
		thisInvDTO.setDtlAhmInvoiceNo(getInvoiceNumber(thisShipStatus, pContract));
		//invoice only if there is a PO to update the Ship unit count
		thisShipStatus.setInvoiced("Y");
	
		String shipNo = getShipmentNo(batchSeqNo);
		thisInvDTO.setDtlFactoryShipmentNo(shipNo);
		
		return thisInvDTO;
	}
	
	private String getInvoiceNumber(ShippingStatus thisShipStatus, PurchaseContract pContract){
		Timestamp shipConfirmTs = null;
		Date shipDate = thisShipStatus.getActualTimestamp();
		DecimalFormat seqFormatter = new DecimalFormat("00");
		if(shipDate != null)  {
			shipConfirmTs = new Timestamp(shipDate.getTime());
		}
		String ahmInvoiceNumber = null;
		
		if(pContract != null)  {
			updatePurchaseContract(pContract, shipConfirmTs);
			String pcPrefix = String.format("%7.7s", pContract.getPurchaseContractNumber());
			ahmInvoiceNumber = pcPrefix + seqFormatter.format(pContract.getInvoiceSequenceNumber());
		}else  {
			ahmInvoiceNumber = String.format("%9s", "");
		}
		return ahmInvoiceNumber.trim();
	}
	
	private double calculatePrice(FrameMTOCPriceMasterSpec price){
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
		return thisPrice;
	}
	
	private Gal103TailDto createTail(int totalRec)  {
		Gal103TailDto invTailDTO = new Gal103TailDto();
		invTailDTO.setTrlRecordId("T");
		invTailDTO.setTrlTransCode("013");
		invTailDTO.setTrlHashTotal(String.format("%6d", totalRec));
		return invTailDTO;
	}
	
	private void updatePurchaseContract(PurchaseContract pContract,Date pProductionDate) {

		int ahmInvSeqNum = pContract.getInvoiceSequenceNumber();
		int shipUnit = pContract.getShipUnit();
		Date shipDate = new java.sql.Date(pProductionDate.getTime());
				
		shipUnit++;
		ahmInvSeqNum = (ahmInvSeqNum % 99) + 1;
		pContract.setInvoiceSequenceNumber(ahmInvSeqNum);
		pContract.setShipDate(shipDate);
		pContract.setShipUnit(shipUnit);
		
	}
	
	private void savePurchaseContracts(Map<String,List<PurchaseContract>> purchaseContracts)  {
		int count = 0;
		Set<Entry<String, List<PurchaseContract>>> pcSet = purchaseContracts.entrySet();
		for(Entry<String, List<PurchaseContract>> entry : pcSet)  {
			if(entry != null && entry.getValue() != null && !entry.getValue().isEmpty())  {
				List<PurchaseContract> pcList = entry.getValue();
				for(PurchaseContract contract : pcList)
				{
					getDao(PurchaseContractDao.class).save(contract);
					getLogger().info("Saved purchase contract (313):" + contract.toString());
					count++;
				}
			}
		}
		getLogger().info("Saved purchase contract count:" + count);
	}
	
	private void updateInvoiced(List<ShippingStatus> shipStatusList)  {
		
		int count = 0;
		for (ShippingStatus shipStatus : shipStatusList)  {
			if(shipStatus != null)  {
				getDao(ShippingStatusDao.class).save(shipStatus);
				getLogger().info("Saved shipping status (263):" + shipStatus.toString());
				count++;
			}
		}
		getLogger().info("Saved shipping status count:" + count);
	}

	
	private String getShipmentNo(int batchSeqNo)  {
		String fShipNo = PropertyService.getProperty(getComponentId(), "FACTORY_SHP_NO", "");
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
	
	private String getKeyNo(String productId, Frame frame)  {
		String keyNo = "";
		String keyPartName = PropertyService.getProperty(getComponentId(), "KEY_SET_PART_NAME", "");
		if(keyPartName != null && !"".equals(keyPartName.trim()))  {
			keyNo = getDao(InstalledPartDao.class).getLatestPartSerialNumber(productId, keyPartName.trim());
		}
		if(keyNo == null || "".equals(keyNo.trim()))  {
			keyNo = frame.getKeyNo();
		}
		if(keyNo == null)  keyNo = "";
		
		if(keyNo != null)  {
			keyNo = keyNo.trim();
			if(keyNo.length() > 4)  {
				int len = keyNo.length();
				keyNo = keyNo.substring(len-4, len);
			}
		}		
		return keyNo;
	}
	
	private String rtrim(String s)  {
		if(s == null) return s;
		String rtrim = s.replaceAll("\\s+$","");
		return rtrim;
	}

}
