package com.honda.galc.service.msip.handler.outbound;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameMTOCPriceMasterSpecDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.HostMtocDao;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameMTOCPriceMasterSpec;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.HostMtoc;
import com.honda.galc.service.msip.dto.outbound.SalesReturnsAmtDto;
import com.honda.galc.service.msip.dto.outbound.SalesReturnsDetGroup;
import com.honda.galc.service.msip.dto.outbound.SalesReturnsDetailDto;
import com.honda.galc.service.msip.dto.outbound.SalesReturnsDto;
import com.honda.galc.service.msip.dto.outbound.SalesReturnsHeaderDto;
import com.honda.galc.service.msip.dto.outbound.SalesReturnsTailerDto;
import com.honda.galc.service.msip.property.outbound.BaseSalesReturnsPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class BaseSalesReturnsHandler<P extends BaseSalesReturnsPropertyBean> extends BaseMsipOutboundHandler<P>{
	public final char FILLER = ' ';
	public final String SALES = "SALES";
	public final String RETURNS = "RETURNS";
	public int totInvoice = 0;
	public double totAmount = 0;
	public String dataType = null;
	
	BaseSalesReturnsHandler(){		
	}
	
	public String getDataType(){
		return dataType;
	}
	
	public void setDataType(String dataType){
		this.dataType = dataType;
	}
	
	public List<SalesReturnsDto> getSalesReturnsData() {
		List<SalesReturnsDto> dtoList = new ArrayList<SalesReturnsDto>();
		List<ShippingStatus> shipStatusList = new ArrayList<ShippingStatus>();
		shipStatusList = getDao(ShippingStatusDao.class).findNotInvoicedByShippingStatus(4);
		SalesReturnsDto invoiceDetails = createInvoiceDetails(shipStatusList);
		dtoList.add(invoiceDetails);
		return dtoList;		
	}
	
	private SalesReturnsDto createInvoiceDetails(List<ShippingStatus> shipStatusList) {
		Timestamp now = getDBTimestamp();
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis(now.getTime());
		SalesReturnsDto invoice = new SalesReturnsDto();
		List<SalesReturnsDetGroup> dataList = new ArrayList<SalesReturnsDetGroup>();
		
		String processPoint60A = PropertyService.getProperty(getComponentId(), "PROCESS_POINT_60A", "PA19024");
		String pp = PropertyService.getProperty(getComponentId(), "COLLECT_PROCESS_POINT", "PA19024");
		
		//create Invoice List
		if(shipStatusList==null || shipStatusList.isEmpty())  {
			getLogger().info(String.format("No uninvoiced records founds...exiting"));
			return invoice;
		}
		getLogger().info(String.format("retrieved uninvoiced: %d", shipStatusList.size()));
		int count = 1;		
		
		//prepare to update current purchase orders with ship unit quantity
		for(ShippingStatus thisShipStatus : shipStatusList)  {
			SalesReturnsDetGroup group = new SalesReturnsDetGroup();
			
			String vin = thisShipStatus.getVin();
			
			List<HostMtoc> salesMtocList = getDao(HostMtocDao.class).findBySpecCode(vin);
			HostMtoc hostMtoc = (salesMtocList != null && !salesMtocList.isEmpty()) ? salesMtocList.get(0) : null;
			
			if(hostMtoc == null) {
				getLogger().info("hostMtoc is null WITH VIN:"+vin+" PP:"+pp);
				continue;
			}
			FrameMTOCPriceMasterSpec price = getDao(FrameMTOCPriceMasterSpecDao.class).findByProductIdAndSpecCode(vin, processPoint60A);
			
			Frame frame = getDao(FrameDao.class).findByKey(vin);
			count++;
			SalesReturnsDetailDto thisInvDTO = createInvoiceDetail(hostMtoc, price, thisShipStatus, frame, count);
			if(thisInvDTO == null) continue;
			int lineNo = Integer.parseInt(frame.getLineNumber());
			group.setAmtRec(createAmt(price.getPrice(), lineNo));
			group.setDetailRec(thisInvDTO);
			
			dataList.add(group);
		}		
		invoice.setHeaderRec(createHeader(cal));
		invoice.setSalesReturnsDetGroup(dataList);
		invoice.setTailerRec(createTailer(count));
		return invoice;				
	}
	
	public SalesReturnsHeaderDto createHeader(Calendar now){
		SalesReturnsHeaderDto header = new SalesReturnsHeaderDto();
		String hRefDocNo = "";
		String refDate = new SimpleDateFormat("yyMMdd").format(now.getTime());
		if(getDataType().equalsIgnoreCase(SALES)){
			hRefDocNo = FILLER+"H13"+refDate;
			header.sethDocType("90");
		}else if(getDataType().equalsIgnoreCase(RETURNS)){
			hRefDocNo = FILLER+"H93"+refDate;
			header.sethDocType("9K");
		}
		String billDate = new SimpleDateFormat("MMddyyyy").format(now.getTime());
		
		header.sethBillingDate(billDate);
		header.sethDistChannel("00");
		header.sethDivision("00");
		header.sethDocuCurrency("CAD");
		header.sethLegacyCustNo("000012");
		header.sethRecordId("H");
		header.sethRefDocNo(StringUtil.padRight(hRefDocNo, 16, FILLER, true));
		header.sethSalesOrg("20FO");
		header.sethSourceCode("ALC");
		header.sethTaxIndicator("C0");
		header.sethTransCode("NAE");
		return header;
	}
	
	public SalesReturnsTailerDto createTailer(int totCount){
		SalesReturnsTailerDto tail = new SalesReturnsTailerDto();
		tail.settCurrency("CAD");
		tail.settLineCount(Integer.toString(totCount));
		tail.settRecordId("T");
		tail.settTotAmount(String.format("%013.2f", totAmount));
		tail.settTotInvoice(Integer.toString(totInvoice));		
		return tail;
	}
	
	public SalesReturnsAmtDto createAmt(String price, int lineno){
		SalesReturnsAmtDto amtDto = new SalesReturnsAmtDto();		
		
		String sPrice = "0000000000000000";
		if(price != null && price != null)  {
			sPrice = price;
		}
		StringBuffer salePrice = new StringBuffer(sPrice);
		if(salePrice != null && salePrice.indexOf(".") < 0 && salePrice.length() > 2)  {
			salePrice.insert(salePrice.length()-2, '.');
		}
		double thisPrice = Double.parseDouble(salePrice.toString());
		String dtlPrice = String.format("%012.2f", thisPrice);
		thisPrice = Double.parseDouble(dtlPrice);
		amtDto.setAmtAmount(dtlPrice);//TODO: AMOUNT TO BE CALCULATED AS IN GAL103
		if(getDataType().equalsIgnoreCase(RETURNS)){
			amtDto.setAmtAmountSign("-");
		}
		amtDto.setAmtArGlAcct("40000");
		if(lineno == 1){
			amtDto.setAmtPlant("");
		}else{
			amtDto.setAmtPlant("");
		}
		
		amtDto.setAmtPriceCurr("CAD");
		amtDto.setAmtRecordId("A");
		totAmount = totAmount + thisPrice;
		return amtDto;
	}
	
	private SalesReturnsDetailDto createInvoiceDetail(HostMtoc thisSalesMtoc, FrameMTOCPriceMasterSpec price,
			ShippingStatus thisShipStatus, Frame frame, int seq)  {
		if(thisSalesMtoc == null)  return null;
				
		FrameSpec fSpec = getDao(FrameSpecDao.class).findByKey(frame.getProductSpecCode());
		String dtlKeyCode = fSpec.getModelYearCode()+fSpec.getModelCode()+fSpec.getModelTypeCode();
		Date prodTs = thisShipStatus.getActualTimestamp();
		String adjComment = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").format(prodTs);
		SalesReturnsDetailDto detDto = new SalesReturnsDetailDto();
		detDto.setDtlAdjComment(adjComment);
		detDto.setDtlGmmColor(frame.getModelExtColorCode());
		detDto.setDtlKeyCode(dtlKeyCode);
		if(frame.getModelTypeCode().equalsIgnoreCase("KC")){
			detDto.setDtlLineExpClass("D");
		}else{
			detDto.setDtlLineExpClass("E");
		}
		detDto.setDtlLineItemNo(Integer.toString(seq));
		detDto.setDtlModelId(frame.getModelCode());
		detDto.setDtlModelType(frame.getModelTypeCode());
		detDto.setDtlPlantSource("H");
		detDto.setDtlQuantity("1");
		detDto.setDtlRecordId("D");
		detDto.setDtlVin(thisShipStatus.getVin());		
		return detDto;
	}
}
