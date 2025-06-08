package com.honda.galc.service.broadcast.servertask;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.dao.product.QsrDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiEntryModelGroupingDao;
import com.honda.galc.dao.qi.QiExternalSystemDefectMapDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.QiExternalSystem;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.Conrod;
import com.honda.galc.entity.product.Crankshaft;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiEntryModelGrouping;
import com.honda.galc.entity.qi.QiExternalSystemDefectMap;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.HeadlessNaqService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

public class HoldToDefectTask implements IServerTask {
	private BaseProduct product;
	private ProcessPoint processPoint;
	private QiEntryModelGrouping entryModelGroup;
	private String defaultPartName;
	private String defaultDefectName;
	private ProductCheckPropertyBean productCheckPropertyBean;

	@Override
	public DataContainer execute(DataContainer dc) {
		product = (BaseProduct) dc.get(DataContainerTag.PRODUCT);
		if(product == null) {
			Logger.getLogger().warn("Current product is null");
			return dc;
		}
		
		String mtcModel = getMtcModel(product.getProductType().toString(), product.getProductId());
		if(StringUtils.isBlank(mtcModel)) {
			Logger.getLogger().warn("Invalid MTC Model code");
			return dc;
		}
		
		entryModelGroup = getDao(QiEntryModelGroupingDao.class).findByMtcModel(mtcModel, product.getProductType().toString());
		if (entryModelGroup == null) {
			Logger.getLogger().warn("Entry Model Group could not be found for MTC Model " + mtcModel + " and Product Type " + product.getProductType().toString());
			return dc;
		}
			
		String processPointId = dc.getString(DataContainerTag.PROCESS_POINT_ID);
		processPoint = StringUtils.isBlank(processPointId) ? null : ServiceFactory.getDao(ProcessPointDao.class).findById(processPointId);
		if (processPoint == null) {
			Logger.getLogger().warn("Process Point is null");
			return dc;
		}
		
		
		
		defaultPartName = getProductCheckPropertyBean().getHoldToDefectTaskPartName();		
		defaultDefectName = getProductCheckPropertyBean().getHoldToDefectTaskDefectName();	
		List<HoldResult> holdResults = getProductHolds(product, processPoint);
		if (!holdResults.isEmpty()) createDefects(product, holdResults); 
		return dc;
	}
	
	private List<HoldResult> getProductHolds(BaseProduct product, ProcessPoint processPoint) {
		return ServiceFactory.getDao(HoldResultDao.class).findAllByProductAndReleaseFlag(product.getProductId(), false, HoldResultType.GENERIC_HOLD);		
	}
	
	private void createDefects(BaseProduct product, List<HoldResult> holdResults) {
		ProductType productType = product.getProductType();
		List<InstalledPart> buildResults = new ArrayList<InstalledPart>();
		List<String> processedDefectNames = new ArrayList<String>();
		for (String respDepartment : getRespDepartmentSet(holdResults)) {
			String defectName = deriveDefectName(respDepartment);
			if (defectName == null || processedDefectNames.contains(defectName)) continue;
			processedDefectNames.add(defectName);
			List<Measurement> measurements = new ArrayList<Measurement>();
			Measurement measurement = new Measurement(); 
			measurement.setErrorCode(defectName);
			measurements.add(measurement);
			
			InstalledPart buildResult = (InstalledPart)ProductTypeUtil.createBuildResult(productType.getProductName(), product.getProductId(), defaultPartName);
			buildResult.setProcessPointId(processPoint.getId());
			buildResult.setInstalledPartStatus(InstalledPartStatus.NG);
			buildResult.setValidPartSerialNumber(false);
			buildResult.setProductType(product.getProductType().toString());
			buildResult.setErrorCode(defectName);
			buildResult.setDefectStatus(DefectStatus.OUTSTANDING.getId());
			buildResult.setQicsDefect(true);
			buildResult.setMeasurements(measurements);
			List<QiDefectResult> matchingDefects = getMatchingQsrDefects(defectName);
			if (matchingDefects == null || matchingDefects.isEmpty())
				buildResults.add(buildResult);
		}
		if (buildResults.isEmpty()) return;
		
		ServiceFactory.getService(HeadlessNaqService.class).saveDefectData(processPoint.getId(), productType, buildResults);
	}
	
	private Set<String> getRespDepartmentSet(List<HoldResult> holdResults){
		Set<String> respDepartmentSet = new HashSet<String>();
		Set<Integer> qsrIdSet = holdResults.stream().map(HoldResult::getQsrId).collect(Collectors.toSet());
		for (Integer qsrId : qsrIdSet) {
			Qsr qsr = ServiceFactory.getDao(QsrDao.class).findByKey(qsrId);
			String respDepartment = (qsr == null || StringUtils.isBlank(qsr.getResponsibleDepartment())) ? "" : qsr.getResponsibleDepartment();			
			respDepartmentSet.add(respDepartment);
		}
		return respDepartmentSet;
	}
	
	private String deriveDefectName(String respDepartment) {
		if (StringUtils.isBlank(respDepartment)) return defaultDefectName;
		
		Map<String, String> suffixByDpt = getProductCheckPropertyBean().getHoldToDefectTaskSuffixByDpt();
		String suffix = (suffixByDpt != null && suffixByDpt.containsKey(respDepartment)) ? suffixByDpt.get(respDepartment) : null;
		if (StringUtils.isBlank(suffix)) return defaultDefectName;
				
		String[] defectNameComponents = defaultDefectName.split(" ");
		if (defectNameComponents.length == 1 || defectNameComponents[defectNameComponents.length - 1].trim().equals(suffix.trim())) 
			return defaultDefectName;
		
		defectNameComponents[defectNameComponents.length - 1] = suffix;
		StringBuilder sb = new StringBuilder();
		for (String defectNameComponent : defectNameComponents) {
			sb.append(defectNameComponent.trim() + " ");
		}
		String derivedDefectName = sb.toString().trim();
		if (externalSystemDefectExists(derivedDefectName)) return derivedDefectName;
		if (externalSystemDefectExists(defaultDefectName)) return defaultDefectName;
		return null;
	}
	
	private Boolean externalSystemDefectExists(String defectName) {		
		QiExternalSystemDefectMap qiExternalSystemDefect = getDao(QiExternalSystemDefectMapDao.class).findByPartAndDefectCodeExternalSystemAndEntryModel(
			defaultPartName,
			defectName,
			QiExternalSystem.LOT_CONTROL.name(),
			entryModelGroup.getId().getEntryModel()
		);
		return qiExternalSystemDefect != null;
	}
	
	private String getMtcModel(String productType, String productId )  {
    	String mtcModel = StringUtils.EMPTY;
    	if(StringUtils.isBlank(productId) || StringUtils.isBlank(productType))  return StringUtils.EMPTY;
		BaseProduct	product = ProductTypeUtil.findProduct(productType, productId);
    	if(product == null)  return StringUtils.EMPTY;
		if(ProductType.MBPN.toString().equalsIgnoreCase(productType) && product instanceof MbpnProduct)  {
			mtcModel = product.getModelCode();
		}
		else if(isDieCastType(productType, product))  {
			mtcModel = product.getModelCode();
		}
		else if(!StringUtils.isBlank(product.getProductSpecCode())) {
			String productSpecCode = product.getProductSpecCode().trim();
			if(productSpecCode.length() > 3) {
				mtcModel = productSpecCode.substring(0,4);
			} else {
				mtcModel = productSpecCode;
			}
		}
		return mtcModel;
    }
	
	public boolean isDieCastType(String productType, BaseProduct product)  {
		if(
			   (ProductType.HEAD.toString().equalsIgnoreCase(productType) && product instanceof Head)
			|| (ProductType.BLOCK.toString().equalsIgnoreCase(productType) && product instanceof Block)
			|| (ProductType.CONROD.toString().equalsIgnoreCase(productType) && product instanceof Conrod)
			|| (ProductType.CRANKSHAFT.toString().equalsIgnoreCase(productType) && product instanceof Crankshaft)
		)  {
			return true;
		}
		else  {
			return false;
		}
    }
	
	private List<QiDefectResult> getMatchingQsrDefects(String defectName) {
		QiDefectResult defectResult = new QiDefectResult();
		defectResult.setInspectionPartName(defaultPartName);
		defectResult.setDefectTypeName(defectName);
		defectResult.setProductId(product.getProductId());	
		return ServiceFactory.getDao(QiDefectResultDao.class).findMatchingDefectsByNotFixed(defectResult);
	}
	
	private ProductCheckPropertyBean getProductCheckPropertyBean(){
		if (productCheckPropertyBean == null)
			productCheckPropertyBean = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPoint.getId());
		return productCheckPropertyBean;
	}
}
