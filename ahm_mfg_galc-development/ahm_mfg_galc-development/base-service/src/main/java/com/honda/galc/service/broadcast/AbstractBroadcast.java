package com.honda.galc.service.broadcast;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.enumtype.TemplateType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.Template;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.mq.MqInstalledPartInfo;
import com.honda.galc.service.printing.AttributeConvertor;
import com.honda.galc.service.printing.IPrintAttributeConvertor;
import com.honda.galc.service.printing.JasperPrintAttributeConvertor;
import com.honda.galc.service.printing.CompiledJasperPrintAttributeConvertor;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.LotControlPartUtil;

/**
 * 
 * <h3>AbstractBroadcast Class description</h3>
 * <p> AbstractBroadcast description </p>
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
 * Aug 20, 2013
 *
 *
 */

public abstract class AbstractBroadcast {
	
	protected BroadcastDestination destination;
	protected DataContainer dc;
	protected String processPointId;
	protected Logger logger;
	private BuildAttributeCache buildAttributeList;
	
	public AbstractBroadcast(BroadcastDestination destination, String processPointId,DataContainer dc){
		this.destination = destination;
		this.dc = clone(dc);
		initCache();
		this.processPointId = processPointId;
		prepareData(processPointId, this.dc);
		logger = Logger.getLogger(processPointId);
	}
	
	private void initCache() {
		this.buildAttributeList = new BuildAttributeCache();
	}
	
	public DataContainer execute(){
		logger.info("invoking " + destination.getDestinationTypeName() + " broadcast service");
		DataContainer dataContainer = calculateAttributes(dc);
		logger.debug("calculate attributes successful");
		return send(dataContainer);
	}
	
	protected DataContainer calculateAttributes(DataContainer dc) {
		logger.debug("calculateAttributes method called");
		return getDataAssembler(getTemplate(getTemplateName(destination.getRequestId(),dc))).convertFromPrintAttribute(destination.getRequestId(), dc);
	}
	
	private IPrintAttributeConvertor getDataAssembler(Template template) {
		if(template != null && TemplateType.JASPER.toString().equalsIgnoreCase(template.getTemplateTypeString()))
			return new JasperPrintAttributeConvertor(logger);
		else if(template != null && TemplateType.COMPILED_JASPER.toString().equalsIgnoreCase(template.getTemplateTypeString()))
			return new CompiledJasperPrintAttributeConvertor(logger);
		else 
			return new AttributeConvertor(logger);
	}
	
	private String mapAttribute(String attribute, String productSpecCode) {
		BuildAttribute buildAttribute = null;
		try{
			buildAttribute = buildAttributeList.findById(productSpecCode,attribute);
		} catch(Exception ex){
			ex.printStackTrace();
			logger.error("exception occured to get buildattributevalue"+ ex.getMessage());
		}
		return buildAttribute == null ? attribute : buildAttribute.getAttributeValue();
	}

	
	private String getTemplateName(String formId, DataContainer dc) {
		String templateID = mapAttribute(formId,dc.getString(DataContainerTag.PRODUCT_SPEC_CODE));
		logger.debug("templateId:"+ templateID);
		logger.debug("formId:"+ formId);
		if(StringUtils.isEmpty(templateID)) return formId;
		return templateID;
	}
	
	private Template getTemplate(String templateName){
		logger.debug("BroadcastTemplateName:" + templateName);
		return ServiceFactory.getDao(TemplateDao.class).findByKey(templateName);
	}
	
	@SuppressWarnings("rawtypes")
	public static void prepareData(String processPointId,DataContainer dc) {	
		if(!dc.containsKey(DataContainerTag.PRODUCT_ID)) return;
		if(!dc.containsKey(DataContainerTag.PRODUCT_TYPE))
			dc.put(DataContainerTag.PRODUCT_TYPE, findProductType(processPointId));
		if(!dc.containsKey(DataContainerTag.PRODUCT)){
			ProductDao productDao = ProductTypeUtil.getProductDao((String)dc.get(DataContainerTag.PRODUCT_TYPE));
			BaseProduct product = (BaseProduct)productDao.findBySn(dc.getString(DataContainerTag.PRODUCT_ID));
			if(product == null) return;
			dc.put(DataContainerTag.PRODUCT,product);
			dc.put(DataContainerTag.PRODUCT_SPEC_CODE,(product == null ? null : product.getProductSpecCode()));
			BaseProductSpecDao productSpecDao = ProductTypeUtil.getProductSpecDao((String)dc.get(DataContainerTag.PRODUCT_TYPE));
			BaseProductSpec	productSpec = (product == null ? null : (BaseProductSpec)productSpecDao.findByProductSpecCode(product.getProductSpecCode(), dc.get(DataContainerTag.PRODUCT_TYPE).toString()));
			dc.put(DataContainerTag.PRODUCT_SPEC, productSpec);
		}
		if(!dc.containsKey(ProductionLot.class)){
			if(dc.containsKey(DataContainerTag.PRODUCT)) {
				BaseProduct product = (BaseProduct) dc.get(DataContainerTag.PRODUCT);
				if(StringUtils.isNotBlank(product.getProductionLot())) {
					ProductionLot productionLot = ServiceFactory.getDao(ProductionLotDao.class).findByKey(product.getProductionLot());
					if(productionLot != null) {
						dc.put(ProductionLot.class, productionLot);
						dc.put(DataContainerTag.PRODUCTION_LOT,productionLot.getProductionLot());
					
						if(product.getProdLot() == null) product.setProdLot(productionLot);
					}
				}
			}
		}
		if(!dc.containsKey(DataContainerTag.PROCESS_POINT_ID))
			dc.put(DataContainerTag.PROCESS_POINT_ID, processPointId);
		
		if(PropertyService.getPropertyBoolean(processPointId,"SET_INSTALLED_PART",false))
			getInstalledPart(processPointId,dc);
	}
	
	//ADDED for MQ Broadcast
	@SuppressWarnings("rawtypes")
	private static void getInstalledPart(String processPointId, DataContainer dc){	
		List<String> partnames=new ArrayList<String>();
		List<MqInstalledPartInfo> installpartsInfo=new ArrayList<MqInstalledPartInfo>();
		LotControlRuleDao dao = ServiceFactory.getDao(LotControlRuleDao.class);
		List<LotControlRule> allLotControlRules = dao.findAllForRule(processPointId);	
		ProductSpec productSpec =(ProductSpec)dc.get(DataContainerTag.PRODUCT_SPEC);
		//Calculate the Product Spec of the Product
		if(productSpec==null){
			Product product = (Product)dc.get(DataContainerTag.PRODUCT);
			BaseProductSpecDao productSpecDao = ProductTypeUtil.getProductSpecDao((String)dc.get(DataContainerTag.PRODUCT_TYPE));
			productSpec = (ProductSpec)productSpecDao.findByProductSpecCode(product.getProductSpecCode(), dc.get(DataContainerTag.PRODUCT_TYPE).toString());
		}
		List<LotControlRule> lotControlRules=LotControlPartUtil.getLotControlRuleByProductSpec(productSpec, allLotControlRules);
		for(LotControlRule lcr:lotControlRules){
			String part=lcr.getId().getPartName();
			partnames.add(part);
		}
		InstalledPartDao dao1 = ServiceFactory.getDao(InstalledPartDao.class);
		List<InstalledPart> installParts =dao1.findAllByProductIdAndPartNames(dc.getString(DataContainerTag.PRODUCT_ID), partnames);
		if(installParts==null || installParts.isEmpty()){
			return;
		}
		for(InstalledPart ip: installParts){
			String partName=ip.getId().getPartName();
			String partSerialNumber=ip.getPartSerialNumber();
			String partStatus=ip.getInstalledPartStatusId().toString();
			String timestamp=ip.getActualTimestamp().toString();
			installpartsInfo.add(new MqInstalledPartInfo(partName,partSerialNumber,partStatus,timestamp));				
		}
		dc.put(DataContainerTag.INSTALLED_PART,installpartsInfo);
	}
	
	private static String findProductType(String processPointId) {	
		ApplicationPropertyBean propertyBean = 
			PropertyService.getPropertyBean(ApplicationPropertyBean.class, processPointId);
		return propertyBean.getProductType();
	}
	
	private DataContainer clone(DataContainer dataContainer) {
		DataContainer dc = new DefaultDataContainer();
		dc.putAll(dataContainer);
		return dc;
	}
	
	protected String[] getArguments(){
		return StringUtils.isEmpty(destination.getArgument()) ? 
				new String[0] : destination.getArgument().split(Delimiter.COMMA);
	}
	
	public abstract DataContainer send(DataContainer dc);
	
}
