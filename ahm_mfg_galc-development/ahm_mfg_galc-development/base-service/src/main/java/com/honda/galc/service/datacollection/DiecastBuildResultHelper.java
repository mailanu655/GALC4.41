package com.honda.galc.service.datacollection;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.entity.product.BlockBuildResultId;
import com.honda.galc.entity.product.ConrodBuildResult;
import com.honda.galc.entity.product.ConrodBuildResultId;
import com.honda.galc.entity.product.CrankshaftBuildResult;
import com.honda.galc.entity.product.CrankshaftBuildResultId;
import com.honda.galc.entity.product.HeadBuildResult;
import com.honda.galc.entity.product.HeadBuildResultId;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3>DiecastBuildResultHelper</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DiecastBuildResultHelper description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Apr 17, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Apr 17, 2012
 */
public class DiecastBuildResultHelper extends DeviceHelperBase implements IDeviceHelper{
    
	public DiecastBuildResultHelper(Device device, HeadLessPropertyBean property,
			String processPointId, Logger logger, String associateNo) {
		super(device, property, processPointId, logger,associateNo);
	}

	public List<ProductBuildResult> getBuildResults(List<LotControlRule> rules) {

		getLogger().info(getProductName(), " product Id:", getProductId());

		List<ProductBuildResult> list = new ArrayList<ProductBuildResult>();
		if(property.isDeviceDriven() && rules == null) rules = deduceLotControlRules();
		
		List<String> partList = getPartList(rules);
		for(partIndex = 0; partIndex < partList.size(); partIndex++){
			nextPartIndex++;
			mappingPartName = partList.get(partIndex);
			currentRule = rules.get(partIndex);
			ProductBuildResult result = getDiecastBuildResult(mappingPartName, getValidProductId());
			if(result != null)  {
				result.setQicsDefect(currentRule.isQicsDefect());
				list.add(result);
			}
		}
		//get part overall status
		if(!StringUtils.isEmpty(property.getOverallStatusPartName()))
			list.add(getDiecastBuildOverallStatus(getValidProductId(), property.getOverallStatusPartName()));

		getLogger().info("BlockBuildResults from device:",System.getProperty("line.separator"), ProductBuildResult.toLogString(list));
		return list;
	
	}


	private ProductBuildResult getDiecastBuildOverallStatus(String productId, String partName) {
		ProductBuildResult result = ProductTypeUtil.createBuildResult(productType, productId, partName);
		populateProductResultOverallStatus(result);
		return result;
	}

	private ProductBuildResult getDiecastBuildResult(String location, String productId) {
		
		if(ProductType.HEAD.toString().equals(productType)){
			HeadBuildResult result = extract(HeadBuildResult.class);
			
			if(result != null){
				HeadBuildResultId id = new HeadBuildResultId(productId, location);
				result.setId(id);
				result.setProcessPointId(this.processPointId);	
				if(!associateNo.equals(""))
					result.setAssociateNo(associateNo);
				else if (! Boolean.parseBoolean(PropertyService.getProperty(this.processPointId, "INSTALLED_PART_ASSOCIATE_USING_USER_ID", 
						PropertyService.getProperty("Default_LotControl", "INSTALLED_PART_ASSOCIATE_USING_USER_ID", "True"))))
					if(result.getAssociateNo() == null) result.setAssociateNo(this.processPointId);
				result.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
				result.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
			}
			
			return result;
		} else if(ProductType.BLOCK.toString().equals(productType)){
			BlockBuildResult result = extract(BlockBuildResult.class);
			if (result != null) {
				BlockBuildResultId id = new BlockBuildResultId(productId, location);
				result.setId(id);
				result.setProcessPointId(this.processPointId);	
				if(!associateNo.equals(""))
					result.setAssociateNo(associateNo);
				else if (! Boolean.parseBoolean(PropertyService.getProperty(this.processPointId, "INSTALLED_PART_ASSOCIATE_USING_USER_ID", 
						PropertyService.getProperty("Default_LotControl", "INSTALLED_PART_ASSOCIATE_USING_USER_ID", "True"))))
					if(result.getAssociateNo() == null) result.setAssociateNo(this.processPointId);
				result.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
				result.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
			}
			return result;
		} else if(ProductType.CRANKSHAFT.toString().equals(productType)){
			CrankshaftBuildResult result = extract(CrankshaftBuildResult.class);
			if (result != null) {
				CrankshaftBuildResultId id = new CrankshaftBuildResultId(productId, location);
				result.setId(id);
				result.setProcessPointId(this.processPointId);	
				if(!associateNo.equals(""))
					result.setAssociateNo(associateNo);
				else if (! Boolean.parseBoolean(PropertyService.getProperty(this.processPointId, "INSTALLED_PART_ASSOCIATE_USING_USER_ID", 
						PropertyService.getProperty("Default_LotControl", "INSTALLED_PART_ASSOCIATE_USING_USER_ID", "True"))))
					if(result.getAssociateNo() == null) result.setAssociateNo(this.processPointId);
				result.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
				result.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
			}
			return result;
		} else if(ProductType.CONROD.toString().equals(productType)){
			ConrodBuildResult result = extract(ConrodBuildResult.class);
			if (result != null) {
				ConrodBuildResultId id = new ConrodBuildResultId(productId, location);
				result.setId(id);
				result.setProcessPointId(this.processPointId);	
				if(!associateNo.equals(""))
					result.setAssociateNo(associateNo);
				else if (! Boolean.parseBoolean(PropertyService.getProperty(this.processPointId, "INSTALLED_PART_ASSOCIATE_USING_USER_ID", 
						PropertyService.getProperty("Default_LotControl", "INSTALLED_PART_ASSOCIATE_USING_USER_ID", "True"))))
					if(result.getAssociateNo() == null) result.setAssociateNo(this.processPointId);
				result.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
				result.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
			}
			return result;
		} else {
			getLogger().warn("WARN: unspported product type:", productType);
		}
			
		return null;
	}

	@Override
	Class<?> getBuildResultIdClass() {
		return ProductType.HEAD.toString().equals(productType) ? HeadBuildResultId.class : BlockBuildResultId.class;
	}

	@Override
	String getPartNamesProperty() {
	
		return property.getLocations();
	}

	public List<ProductBuildResult> getBuildResultsMbpnProduct(
			List<String> installPartNames) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
