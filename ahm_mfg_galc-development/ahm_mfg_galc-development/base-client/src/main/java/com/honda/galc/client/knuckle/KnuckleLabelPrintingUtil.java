package com.honda.galc.client.knuckle;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.PrintingException;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.printing.PrintingUtil;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>KnuckleLabelPrintingUtil Class description</h3>
 * <p> KnuckleLabelPrintingUtil description </p>
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
 * @author Jeffray Huang<br>
 * Dec 2, 2010
 *
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class KnuckleLabelPrintingUtil {
	
	private static String KNUCKLE_LABEL_PRINTERS = "KNUCKLE_LABEL_PRINTERS";
	private static String PRINTER_NAME_LEFT = "PRINTER_NAME_LEFT";
	private static String PRINTER_NAME_RIGHT = "PRINTER_NAME_RIGHT";
	private static String PRIMARY_PRINTER_LEFT = "PRIMARY_PRINTER_LEFT";
	private static String PRIMARY_PRINTER_RIGHT = "PRIMARY_PRINTER_RIGHT";
	private static String SECONDARY_PRINTER_LEFT = "SECONDARY_PRINTER_LEFT";
	private static String SECONDARY_PRINTER_RIGHT = "SECONDARY_PRINTER_RIGHT";
	
	private static String PRINT_FORM_ID = "PRINT_FORM_ID";
	private static String PRINT_LOT_TRIGGER_OFFSET = "PRINT_LOT_TRIGGER_OFFSET";
	private static String LABEL_FEED_COUNT = "LABEL_FEED_COUNT";
	
	private PrintingUtil printingUtil;
	
	private String printerLeft = null;
	private String printerRight = null;
	
	
	private Integer defaultFeed = 0;
	private Integer  PRINT_LOT_MINIMUM_COUNT_FEED_DEFAULT=30;
	private static String PRINT_LOT_MINIMUM_COUNT_FEED="PRINT_LOT_MINIMUM_COUNT_FEED";
	
	public void print(SubProduct subProduct) {
		
		print("",subProduct);
		
	}
	
	public void reprint(SubProduct subProduct) {
		
		print("REPRINT",subProduct);
		
	}
	
	private void print(String reprint, SubProduct subProduct) {
		
		String sessionCookieValue = startSession();
		try{
			advance(subProduct.getSubId(),getLabelFeedCount());
			doPrint(reprint,subProduct);
		}finally{
				destroySession(sessionCookieValue);
		}
		
	}
	
	private String startSession() {
		
		String sessionCookieValue = ServiceFactory.createSession();
		
		getPrintingUtil().setSessionCookieValue(sessionCookieValue);
		
		return sessionCookieValue;
		
	}
	
	private void destroySession(String sessionCookieValue) {
		
		getPrintingUtil().setSessionCookieValue(null);
		ServiceFactory.destroySession(sessionCookieValue);
		
	}
	
	public void print(List<SubProduct> subProducts) {
		
		if(subProducts == null) return;
		
		print(subProducts,false);
	}
	
	
	public void reprint(List<SubProduct> subProducts) {
		
		if(subProducts == null) return;
		
		print(subProducts,true);
	}
	
	public void print(List<SubProduct> subProducts, boolean isReprint) {
		
		String sessionCookieValue = startSession();
		try{
			
			doPrint(subProducts,isReprint);
			
			getPrintingUtil().destroySession(sessionCookieValue);
			
		}finally{
		
			ServiceFactory.destroySession(sessionCookieValue);
			
		}
		
	}
	
	public void print(List<SubProduct> subProducts, boolean isReprint,boolean isPrimaryPrinter) {
		
		printerLeft = isPrimaryPrinter ? getPrimaryPrinterName(Product.SUB_ID_LEFT) : getSecondaryPrinterName(Product.SUB_ID_LEFT);
		printerRight = isPrimaryPrinter ? getPrimaryPrinterName(Product.SUB_ID_RIGHT) : getSecondaryPrinterName(Product.SUB_ID_RIGHT);
		
		String sessionCookieValue = startSession();
		try{
			
			doPrint(subProducts,isReprint);
			
			getPrintingUtil().destroySession(sessionCookieValue);
			
		}finally{
		
			ServiceFactory.destroySession(sessionCookieValue);
			
		}
		
		printerLeft = null;
		printerRight = null;
		
	}
	
	private void doPrint(List<SubProduct> subProducts,boolean isReprint) {
		
		String currentLeftProdLot = "";
		String currentRightProdLot = "";
		
		for(SubProduct subProduct : subProducts) {
			
			if(subProduct.getSubId().equals(Product.SUB_ID_LEFT)) {
				if(!subProduct.getProductionLot().equals(currentLeftProdLot))
					advance(Product.SUB_ID_LEFT,getLabelFeedCount());
				currentLeftProdLot = subProduct.getProductionLot();
			}else {
				if(!subProduct.getProductionLot().equals(currentRightProdLot))
					advance(Product.SUB_ID_RIGHT,getLabelFeedCount());
				currentRightProdLot = subProduct.getProductionLot();
			}
			
			doPrint(isReprint ?"REPRINT" :"",subProduct);
			
		}
		defaultFeed=PropertyService.getPropertyInt(KNUCKLE_LABEL_PRINTERS, PRINT_LOT_MINIMUM_COUNT_FEED, PRINT_LOT_MINIMUM_COUNT_FEED_DEFAULT);
		if(!isReprint && subProducts.size() < defaultFeed)
			advance(subProducts.get(0).getSubId(),defaultFeed - subProducts.size());
	}
	
	private void advance(String side, int count) {
		for(int i=0;i<count;i++)
			advance(side);
	}
	
	private void advance(String side) {
		
		DataContainer dc = new DefaultDataContainer();
		dc.put("PLANT", "");
		dc.put("KSN", "");
		dc.put("REPRINT", "");
		dc.put("PART_MARK", "");
		
		print(side,dc);
	
	}
	
	private void doPrint(String reprint, SubProduct subProduct) {
		
		
		BuildAttributeCache buildAttributeCache= getPrintingUtil().getBuildAttributeCache();
		
		String partMarkId = subProduct.getSubId().equals(Product.SUB_ID_LEFT)? 
				BuildAttributeTag.KNUCKLE_PART_MARK_LEFT : BuildAttributeTag.KNUCKLE_PART_MARK_RIGHT;
		
		buildAttributeCache.loadAttribute(partMarkId);
		
		BuildAttribute buildAttribute = buildAttributeCache.findById(subProduct.getProductSpecCode(), partMarkId);
		
		if(buildAttribute == null) throw new PrintingException("Build attribute not found for spec code : " + subProduct.getProductSpecCode() + " attribute : " + partMarkId);
		
		DataContainer dc = new DefaultDataContainer();
		dc.put("PLANT", subProduct.getPlantNumber());
		dc.put("KSN", subProduct.getId());
		dc.put("REPRINT", reprint);
		dc.put("PART_MARK", buildAttribute.getAttributeValue());
		
		print(subProduct.getSubId(),dc);
		
	}
	
	private void print(String side,DataContainer dc) {
		
		getPrintingUtil().print(getPrinterName(side), getFormId(), dc);
		
	}
	
	private PrintingUtil getPrintingUtil() {
		
		if(printingUtil == null) printingUtil = new PrintingUtil();
		
		return printingUtil;
		
	}
	
	
	private String getPrinterName(String side) {
		if(Product.SUB_ID_LEFT.equals(side) && !StringUtils.isEmpty(printerLeft)) return printerLeft;
		if(Product.SUB_ID_RIGHT.equals(side) && !StringUtils.isEmpty(printerRight)) return printerRight;
	
		return PropertyService.getProperty(KNUCKLE_LABEL_PRINTERS, Product.SUB_ID_LEFT.equals(side) ? PRINTER_NAME_LEFT : PRINTER_NAME_RIGHT);
		
	}
	
	private String getPrimaryPrinterName(String side) {
		
		return PropertyService.getProperty(KNUCKLE_LABEL_PRINTERS, Product.SUB_ID_LEFT.equals(side) ? PRIMARY_PRINTER_LEFT : PRIMARY_PRINTER_RIGHT);
		
	}
	
	private String getSecondaryPrinterName(String side) {
		
		return PropertyService.getProperty(KNUCKLE_LABEL_PRINTERS, Product.SUB_ID_LEFT.equals(side) ? SECONDARY_PRINTER_LEFT : SECONDARY_PRINTER_RIGHT);
		
	}
	
	private String getFormId() {
		
		return PropertyService.getProperty(KNUCKLE_LABEL_PRINTERS, PRINT_FORM_ID);
		
	}
	
	
	public static int getPrintLotTriggerOffset() {
		
		return PropertyService.getPropertyInt(KNUCKLE_LABEL_PRINTERS, PRINT_LOT_TRIGGER_OFFSET,20);

	}
	
	public static int getLabelFeedCount() {
		
		return PropertyService.getPropertyInt(KNUCKLE_LABEL_PRINTERS, LABEL_FEED_COUNT,2);

	}
	
}
