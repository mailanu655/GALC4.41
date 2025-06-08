package com.honda.galc.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.kernel.DelegatingResultList;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.oif.BomDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.property.SubproductPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
/* @ver 0.2
* @author Gangadhararao Gadde
*/
public class SubproductUtil {
	PartSerialNumber partnumber;
	LotControlRule rule;
	protected BuildAttributeDao buildAttributeDao = null;
	protected ProcessPointDao processPointDao = null;
	protected FrameSpecDao frameSpecDao;
	protected PartNameDao partNameDao;
	protected PartSpecDao partSpecDao;
	protected PartSpec partSpec;
	protected BomDao bomDao;
	protected PartSpec matchedPartSpec;

	
	protected SubproductPropertyBean subproductProperty;


	public SubproductUtil() {
	}
	
	public SubproductUtil(PartSerialNumber partnumber, LotControlRule rule, PartSpec partSpec) {
		this.partnumber = partnumber;
		this.rule = rule;
		this.partSpec = partSpec;
		this.subproductProperty =PropertyService.getPropertyBean(SubproductPropertyBean.class, rule.getId().getProcessPointId());		
	}
		
	public boolean isPartSubproduct() {
		PartName installedPartName = getPartNameDao().findByKey(rule.getId().getPartName());
		if (installedPartName.getSubProductType() == null || installedPartName.getSubProductType().equalsIgnoreCase("")) {
			return false;
		} else {
			return true;
		}
	}

	public BaseProduct findSubproduct() {
		PartName installedPartName = getPartNameDao().findByKey(rule.getId().getPartName());
		String subProductTypeName = installedPartName.getSubProductType();
		BaseProduct subProduct = null;
		
		if (subProductTypeName != null && !subProductTypeName.equalsIgnoreCase("") && partnumber.getPartSn() != null) {  //If TRUE, Part is defined as subproduct.
			subProduct = ProductTypeUtil.getProductDao(subProductTypeName).findBySn(partnumber.getPartSn());
		} else {
			Logger.getLogger().debug("This is not a subproduct.");
		}

		return subProduct;
	}

	public boolean isValidSpecCode(String subProductName, BaseProduct subProduct, String parentProductSpecCode) {
		//Compare Spec Code of subproduct to what Spec Code is valid for parent product
		//Define the expected product spec to part spec whlie creating part specification under Part #
		if (subproductProperty.doesRequireSpecCode()) { //If spec code is not setup and not required, then SN validation is enough.
		String expectedSpecCode = "";
		String mbpnAttribute = "";
		matchedPartSpec = null;
		
			if (subProductName.equalsIgnoreCase(ProductType.ENGINE.getProductName())) {  //Engine spec codes come from FrameMasterSpec, therefore no need to use Build Attributes.
				expectedSpecCode = getFrameSpecDao().findByKey(parentProductSpecCode).getEngineMto();
			} else {
				try {
					if(subproductProperty.isValidateFromBom()) {
						String partNo = getBomDao().getBomPartNo(parentProductSpecCode, subProduct.getProductSpecCode());
						if(StringUtils.isEmpty(partNo)) {
							Logger.getLogger().error("Failed to validate Spec Code from Bom. Scanned - (" + subProduct.getProductSpecCode() + ")");
							return false;
						}
						
					} else if(!subproductProperty.isUseMainNoFromPartSpec()){
						mbpnAttribute = subproductProperty.getSpecCodeFromBuildAttrMap().get(subProductName);
						BuildAttributeCache bc  = new BuildAttributeCache();
						expectedSpecCode = bc.findAttributeValue(parentProductSpecCode, mbpnAttribute);
						if (!expectedSpecCode.contains(subProduct.getProductSpecCode())) { //If spec code is setup, then must validate.
							Logger.getLogger().error("Spec Code does not match expected Spec Code.  Scanned - (" + subProduct.getProductSpecCode() + ") Expected - (" + expectedSpecCode + ")");
							return false;
						}
					} else {
						return validateFromPartSpecs(subProduct);
					}
					
					return true;
				} catch (Exception e) {
					Logger.getLogger().error(e, "Expected Spec Code not setup for this part.");
					return false;
				}

			}
		} else {
			Logger.getLogger().debug("Does not require Spec Code Validation.");
		}

		return true;
	}

	private boolean validateFromPartSpecs(BaseProduct subProduct) {
		/**
		 * 1.	We select Part Spec (mbpn/part number) by comparing MBPN product Id with Part Mask in Part Spec
         * 2.	If the Part Mask is wild card or having still having multiple Part Specs, we will use the new ProductSpecUtil getMathedPartSpec() method to find the best matched Part Spec based on Mbpn spec code.
         **/
		
		List<PartSpec> pSpecs = new ArrayList<PartSpec>();
		for(PartSpec ps : rule.getParts()) {
			if(CommonPartUtility.verification(subProduct.getProductId(), ps.getPartSerialNumberMask(), PropertyService.getPartMaskWildcardFormat()))
				pSpecs.add(ps);
		}
		
		matchedPartSpec = ProductSpecUtil.getMatchedPartSpec(subProduct.getProductSpecCode(), pSpecs, PartSpec.class);
		if (matchedPartSpec == null) { //If spec code is setup, then must validate.
			Logger.getLogger().error("Spec Code does not match expected Spec Code from Part Spec.  Scanned - " + subProduct.getProductSpecCode());
			return false;
		} else {
			Logger.getLogger().info("Spec Code is validate from Part Spec. Scanned -(" + subProduct.getProductSpecCode() + ") Part Spec (" + matchedPartSpec.getId().getPartName() + matchedPartSpec.getId().getPartId() + ")");
		}
		
		return true;
	}

	public String getSubproductProductSpecCode (String subProductName, String parentProductSpecCode, String subProductProcessPointId) {
		String subProductSpecCode = "";
		String mbpnAttribute = "";
		SubproductPropertyBean subproductProperty = PropertyService.getPropertyBean(SubproductPropertyBean.class, subProductProcessPointId);
		
		try {
			mbpnAttribute = subproductProperty.getSpecCodeFromBuildAttrMap().get(subProductName);
			BuildAttributeCache bc  = new BuildAttributeCache();
			subProductSpecCode = bc.findAttributeValue(parentProductSpecCode, mbpnAttribute);
			
			/**
			 * this method is used by OIF tasks
			 * 
			 * NALC-1051 
			 * With the change to allow for comma delimited value in the build attribute need to change these to check if it is a comma delimited list.  
			 * If it is a comma delimited list then we should get the first spec in the list.  This would allow it to assign a spec and should match 
			 *   when doing the marriage validation.
			 */
			if(subProductSpecCode.contains(Delimiter.COMMA)) {
				String[] split = subProductSpecCode.split(Delimiter.COMMA);
				subProductSpecCode = split[0];
			}
		}catch (Exception e) {
			Logger.getLogger().info("Expected Spec Code not setup for this part and spec code. (" + subProductName + "/" + parentProductSpecCode + ")");
			
		}
		return subProductSpecCode;
	}
		
	public List<String> performSubProductChecks(String subProductTypeName, BaseProduct subProduct, String installProcessPointId) throws Exception {
		//Perform checks against subproduct.
		String[] checkTypes = getProductCheckPropertyBean(installProcessPointId).getProductCheckTypes();
		if(subProduct instanceof MbpnProduct){
			MbpnProduct product = ((MbpnProduct) subProduct);
			if(product.getExternalBuild() == 1){
				List<String> checkTypesList = new ArrayList<String>(Arrays.asList(checkTypes));
				checkTypesList.remove(ProductCheckType.RECURSIVE_INSTALLED_PART_CHECK.name());
				checkTypesList.remove(ProductCheckType.OUTSTANDING_PARTS_CHECK.name());
				checkTypesList.remove(ProductCheckType.OUTSTANDING_REQUIRED_PARTS_CHECK.name());
				checkTypes = checkTypesList.toArray(new String[0]);	
			}
		}
		return performSubProductChecks( subProductTypeName,  subProduct,  installProcessPointId, checkTypes);
		
	}
	
	public List<String> performSubProductChecks(String subProductTypeName, BaseProduct subProduct, String installProcessPointId,String[] checkTypes)
	{
		List<String> failedProductCheckList = new ArrayList<String>();		
		Map<String, Object> checkResults = ProductCheckUtil.check(subProduct,getProcessPointDao().findById(installProcessPointId),checkTypes);
		for (String checkType : checkResults.keySet()) {
			failedProductCheckList.add(checkType);
		}

		return failedProductCheckList;
	}
	
	public boolean performSubproductTracking(String subProductTypeName, BaseProduct subProduct, String installProcessPointId, String lotControlProcessPointId) throws Exception {
		try {
			PartName installedPartName = getPartNameDao().findByKey(rule.getId().getPartName());
			String subProductName = installedPartName.getSubProductType();
			if (!lotControlProcessPointId.equalsIgnoreCase("")) {
				subProduct.setLastPassingProcessPointId(lotControlProcessPointId);
			}
			ProductDao<BaseProduct> productDao = (ProductDao<BaseProduct>)ProductTypeUtil.getProductDao(subProductName);
			productDao.updateTrackingAttributes(subProduct);
			
			if (installProcessPointId != null && !installProcessPointId.equalsIgnoreCase("")){
				ServiceFactory.getService(TrackingService.class).track(ProductType.getType(subProductName), partnumber.getPartSn(), installProcessPointId);
			} else {
				Logger.getLogger().debug("Tracking process point not setup.");
			}
			return true;
		} catch (Exception e) {
			throw new Exception ("Could not perform tracking on subproduct.");
		}
	}
	
	private ProcessPointDao getProcessPointDao() {
		if (processPointDao == null) {
			processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		}
		return processPointDao;
	}
	
	private FrameSpecDao getFrameSpecDao() {
		if (frameSpecDao == null) {
			frameSpecDao = ServiceFactory.getDao(FrameSpecDao.class);
		}
		return frameSpecDao;
	}
	
	private PartNameDao getPartNameDao() {
		if (partNameDao == null) {
			partNameDao = ServiceFactory.getDao(PartNameDao.class);
		}
		return partNameDao;
	}
	
	public BomDao getBomDao() {
		if (bomDao == null) {
			bomDao = ServiceFactory.getDao(BomDao.class);
		}
		return bomDao;
	}
	
	
	protected ProductCheckPropertyBean getProductCheckPropertyBean(String processPointId) {
		return PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPointId);
	}
	
	private String getProductSpecUsingPartNo(){
		return partSpec.getPartNumber().toString().trim();
	}

	public PartSpec getPartSpec() {
		return partSpec;
	}
	
	public PartSpec getMatchedPartSpec() {
		return matchedPartSpec;
	}


}
