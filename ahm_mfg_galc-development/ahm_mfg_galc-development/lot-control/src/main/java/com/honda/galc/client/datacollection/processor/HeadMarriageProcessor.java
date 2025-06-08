package com.honda.galc.client.datacollection.processor;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.property.BuildResultCheckPropertyBean;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.HeadBuildResultDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.HeadBuildResult;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductCheckUtil;
/**
 * 
 * <h3>IpuAbstractStrategy Class description</h3>
 * <p> IpuAbstractStrategy description </p>
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
 * Nov 29, 2011
 *
 *
 */
public class HeadMarriageProcessor extends PartSerialNumberProcessor {

	public HeadMarriageProcessor(ClientContext context) {
		super(context);
		
	}
	
	public boolean confirmPartSerialNumber(PartSerialNumber partnumber){

		Head head = getDao(HeadDao.class).findByMCDCNumber(partnumber.getPartSn());
		if(head == null) handleException("Head does not exist!");
		// input sn may be MC number so replace with the right number 
		partnumber.setPartSn(head.getHeadId());
		installedPart.setPartSerialNumber(head.getHeadId());
		
		super.confirmPartSerialNumber(partnumber);
		validateHead(head);
		checkHeadBuildResult(head);
		checkHeadBuildAttributeResult(head);
		
		return true;
		
	}
	
	private void checkHeadBuildAttributeResult(Head head) {
		BuildResultCheckPropertyBean propertyBean = PropertyService.getPropertyBean(BuildResultCheckPropertyBean.class, ApplicationContext.getInstance().getProcessPointId());
		String[] partList = propertyBean.getBuildAttributeCheckPartList();
		if(partList == null || partList.length == 0) return;
		
		List<HeadBuildResult> headBuildResults = getDao(HeadBuildResultDao.class).findAllByProductId(head.getHeadId());
		BuildAttributeCache cache = new BuildAttributeCache();
		ProductBean product = getController().getState().getProduct();
		
		for(String part : partList) {
			if(StringUtils.isEmpty(part)) continue;
			BuildAttribute buildAttribute = cache.findById(product.getProductSpec(), part);
            if(buildAttribute == null) continue;
            HeadBuildResult headBuildResult = findBuildResult(part,headBuildResults);
            if(headBuildResult == null) handleException("Missing build result: " + part);
             if(!StringUtils.equalsIgnoreCase(headBuildResult.getResultValue(),buildAttribute.getAttributeValue()))
            	handleException("Part - " + part + " with value : " + headBuildResult.getResultValue() 
            			+ " does not match the required build attribute " +  buildAttribute.getAttributeValue());
	    }
		
	}

	private void validateHead(Head head) {
		if(!StringUtils.isEmpty(head.getEngineSerialNumber()))
			handleException("Head is already associated to engine " +  head.getEngineSerialNumber());
		if(head.getDefectStatus() != null && !head.isDirectPassStatus() && !head.isRepairedStatus())
			handleException("Head has defects");
	}

	private void checkHeadBuildResult(Head head) {
		ProductCheckPropertyBean propertyBean = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, ApplicationContext.getInstance().getProcessPointId());
		Map<String, String> checkPpIds = propertyBean.getSubProductCheckProcessPoint();
		if(checkPpIds == null || StringUtils.isEmpty(checkPpIds.get(ProductType.HEAD.name()))){
			handleException("Configuration Error: Missing Head Required Part check process point.");
		}
		
		ProcessPoint headCheckPpId = ServiceFactory.getDao(ProcessPointDao.class).findById(checkPpIds.get(ProductType.HEAD.name()));
		if(headCheckPpId == null)
			handleException("Configuration Error: Invalid Head Required Part check process point - " + checkPpIds.get(ProductType.HEAD.name()));
		ProductCheckUtil util = new ProductCheckUtil(head, headCheckPpId);
		List<String> checkResults = util.outstandingRequiredPartsCheck();
		if(checkResults != null && checkResults.size() > 0){
			StringBuilder missingPart = new StringBuilder();
			for(String p : checkResults)
				if(missingPart.length() > 0) 
					missingPart.append(",").append(p);
				else
					missingPart.append(p);
			
			handleException("Missing build result. " + missingPart.toString());
		}
		
	}

	private HeadBuildResult findBuildResult(String partName, List<HeadBuildResult> headBuildResults){
		for(HeadBuildResult result : headBuildResults) {
	    	 if(partName.equalsIgnoreCase(result.getPartName())) return result; 
	     }
		return null;
	}

}
