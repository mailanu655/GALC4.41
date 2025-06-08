package com.honda.galc.client.teamleader.hold.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.HoldReasonMappingDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.HoldReasonMappingDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.QCAction;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.PropertyComparator;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>Config</code> is ...
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>Karol Wozniak</TD>
 * <TD>Jan 7, 2010</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class Config {

	private static Config instance;
	
	private static Map<String, Config> instances = new LinkedHashMap<String, Config>();

	private Map<Division, Map<ProductType, List<ProcessPoint>>> processPointIx;
	private Map<Division, Map<Line, List<HoldReasonMappingDto>>> holdReasonIx;
	private Map<Division, Map<String, Line>> lineIx;
	private Map<Division, List<ProductType>> diecastProductTypeIx;
	private Map<Division, String[]> machineIdIx;

	private QsrMaintenancePropertyBean property;
	private String applicationId;

	private Config() {

		this.processPointIx = new LinkedHashMap<Division, Map<ProductType, List<ProcessPoint>>>();
		this.holdReasonIx = new LinkedHashMap<Division, Map<Line, List<HoldReasonMappingDto>>>();
		this.diecastProductTypeIx = new LinkedHashMap<Division, List<ProductType>>();
		this.machineIdIx = new LinkedHashMap<Division, String[]>();
		this.lineIx = new LinkedHashMap<Division, Map<String, Line>>();
		this.applicationId = ApplicationContext.getInstance().getApplicationId();
		this.property = PropertyService.getPropertyBean(QsrMaintenancePropertyBean.class, applicationId);

		loadData();
		loadMachineIdMapping();
		loadHoldReasons();
	}

	private void putProcessPoint(Division division, ProductType productType, ProcessPoint processPoint) {
		if (division == null || productType == null || processPoint == null) {
			return;
		}
		Map<ProductType, List<ProcessPoint>> ix1 = getProcessPointIx().get(division);
		if (ix1 == null) {
			ix1 = new LinkedHashMap<ProductType, List<ProcessPoint>>();
			getProcessPointIx().put(division, ix1);
		}
		List<ProcessPoint> processPoints = ix1.get(productType);
		if (processPoints == null) {
			processPoints = new ArrayList<ProcessPoint>();
			ix1.put(productType, processPoints);
		}
		if (!processPoints.contains(processPoint)) {
			processPoints.add(processPoint);
		}
	}
	
	private void putLine(Division division, String lineId) {
		if(division == null || lineId == null) {
			return;
		}

		Map<String, Line> ix1 = getLineIx().get(division);
		if(ix1 == null) {
			ix1 = new LinkedHashMap<String, Line>();
			getLineIx().put(division, ix1);
		}

		Line line = ix1.get(lineId);
		if(line == null) {
			line = getLineDao().findByKey(lineId);
			ix1.put(line.getLineId(), line);
		}
	}

	private void putProductType(Map<Division, List<ProductType>> ix, Division division, ProductType productType) {
		if (ix == null || division == null || productType == null) {
			return;
		}
		List<ProductType> list = ix.get(division);
		if (list == null) {
			list = new ArrayList<ProductType>();
			getDiecastProductIx().put(division, list);
		}
		if (!list.contains(productType)) {
			list.add(productType);
		}
	}
	
	private void putHoldReasons(Division division, Line lineId, HoldReasonMappingDto holdReason) {		
		Map<Line, List<HoldReasonMappingDto>> ix = getHoldReasonIx().get(division);
		if(ix == null) {
			ix = new LinkedHashMap<Line, List<HoldReasonMappingDto>>();
			getHoldReasonIx().put(division, ix);
		}
		List<HoldReasonMappingDto> holdReasons = ix.get(lineId);
		if(holdReasons == null) {
			holdReasons = new ArrayList<HoldReasonMappingDto>();
			ix.put(lineId, holdReasons);
		}
		
		if (!holdReasons.contains(holdReason)) {
			holdReasons.add(holdReason);
		}
	}

	public List<ProductType> getProductTypes(Division division) {
		Map<ProductType, List<ProcessPoint>> map = getProcessPointIx().get(division);
		if (map == null) {
			return new ArrayList<ProductType>();
		}
		return new ArrayList<ProductType>(map.keySet());
	}

	public List<ProductType> getDiecastProductTypes(Division division) {
		return getInstance().getDiecastProductIx().get(division);
	}

	public List<ProcessPoint> getProcessPoints(Division division, ProductType productType) {

		Map<ProductType, List<ProcessPoint>> map = getProcessPointIx().get(division);
		if (map == null) {
			return new ArrayList<ProcessPoint>();
		}
		List<ProcessPoint> processPoints = map.get(productType);
		if (processPoints == null) {
			return new ArrayList<ProcessPoint>();
		}
		return processPoints;
	}
	
	public List<ProcessPoint> getProcessPoints(Division division, Line line, ProductType productType) {

		Map<ProductType, List<ProcessPoint>> map = getProcessPointIx().get(division);
		if (map == null) {
			return new ArrayList<ProcessPoint>();
		}
		List<ProcessPoint> processPoints = map.get(productType);
		if (processPoints == null) {
			return new ArrayList<ProcessPoint>();
		}

		List<ProcessPoint> filteredProcessPoints = new ArrayList<ProcessPoint>();
		for (ProcessPoint processPoint : processPoints) {
			if (processPoint == null) continue;
			if (processPoint.getLineId().equals(line.getLineId())) filteredProcessPoints.add(processPoint);
		}
		return filteredProcessPoints;
	}

	 public List<Line> getLines(Division division) {
			return ServiceFactory.getDao(LineDao.class).findAllByDivisionId(division,false);
	}

	 private List<Line> getLinesByQCAction(Map<Line, List<HoldReasonMappingDto>> holdReasonsMap, QCAction qcAction) {
		 Set<Line> lines = new HashSet<Line>();
		 for (Map.Entry<Line, List<HoldReasonMappingDto>> reasons : holdReasonsMap.entrySet()) {
			 for(HoldReasonMappingDto reason : reasons.getValue()) {
				 if(reason.getQcActionId() == Integer.parseInt(qcAction.getQcActionId())) {
					 lines.add(reasons.getKey());
				 }
			 }
		 }
		 return new ArrayList<Line>(lines);
	 }

	 private void loadData() {
		List<ProcessPoint> processPoints = ServiceFactory.getDao(ProcessPointDao.class).findAll();
		if (processPoints == null) return;

		Collections.sort(processPoints, new PropertyComparator<ProcessPoint>(ProcessPoint.class, "sequenceNumber"));
		
		Set<String> validProductTypes = ProductType.getProductTypeNames().keySet();
		ProductType defaultProductType = ApplicationContext.getInstance().getProductTypeData().getProductType();
		ArrayList<String> allowedProductTypes = new ArrayList<String>();
		
		for (String allowedProductType : this.property.getAllowedProductTypes()) {
			if (validProductTypes.contains(allowedProductType.trim()))
				allowedProductTypes.add(allowedProductType);
		}

		StringBuilder sb = new StringBuilder();
		for (ProcessPoint processPoint : processPoints) {
			if (processPoint == null) continue;

			String divisionId = processPoint.getDivisionId();
			if (StringUtils.isBlank(divisionId)) {
				sb.append(String.format("Invalid-Empty DivisionId for process point : %s\n", processPoint.getProcessPointId()));
				continue;
			}
			
			Division division = getProcessPointDivisionById(divisionId);
			if (division == null) {
				division = getDivisionDao().findByDivisionId(StringUtils.trim(divisionId));
			}

			if (division == null) {
				sb.append(String.format("Invalid Division : %s\n", divisionId));
				continue;
			}

			ProductType productType = null;
			SystemPropertyBean propertyBean = PropertyService.getPropertyBean(SystemPropertyBean.class, processPoint.getProcessPointId());
			String productTypeName = propertyBean.getProductType();
			
			if (!allowedProductTypes.isEmpty() && !allowedProductTypes.contains(productTypeName)) continue;
				
			if (StringUtils.isNotBlank(productTypeName)) {
				productType = ProductType.getType(productTypeName);
			}
			if (productType == null) {
				productType = defaultProductType;
			}
			putProcessPoint(division, productType, processPoint);
			
			if(processPoint.getLineId() != null || processPoint.getLineId() == "") { 
				putLine(division, processPoint.getLineId());
			}
			
			if (isDiecast(productType)) {
				putProductType(getDiecastProductIx(), division, productType);
			}
		}
	}
	
	private void loadHoldReasons() {
		List<HoldReasonMappingDto> holdReasonMappings = getHoldReasonMappingDao().findAllMappedReasons();
		if(holdReasonMappings == null) return;

		for(HoldReasonMappingDto holdReason : holdReasonMappings) {
			Division division = getHoldReasonsDivisionById(holdReason.getDivisionId());
			if(division == null) {
				division = getLineDivisionById(StringUtils.trim(holdReason.getDivisionId()));
			}

			if(division != null) {
				Line line = getLine(division, StringUtils.trim(holdReason.getLineId()));
				if(line != null) {
					putHoldReasons(division, line, holdReason);
				}
			}
		}
	}

	private void loadMachineIdMapping() {
		String[] mapping = property.getDivisionMachineIdMapping();
		if (mapping == null) {
			return;
		}

		for(String item : mapping) {
			if (StringUtils.isBlank(item)) {
				continue;
			}
			String[] ar = item.split(Delimiter.COLON);
			if (ar.length < 2) {
				continue;
			}
			String divisionId = ar[0];
			String machineIdsStr = ar[1];
			divisionId = StringUtils.trim(divisionId);
			if (StringUtils.isBlank(divisionId) || StringUtils.isBlank(machineIdsStr)) {
				continue;
			}

			Division div = getProcessPointDivisionById(divisionId);
			if (div != null) {
				String[] machineIds = machineIdsStr.split("[|]");
				machineIdIx.put(div, machineIds);
			}
		}
	}
	
	public List<HoldReasonMappingDto> getReasons(Map<Line, List<HoldReasonMappingDto>> map) {
		if(map == null) {
			return new ArrayList<HoldReasonMappingDto>();
		}
		Set<Line> lines = map.keySet();
		List<HoldReasonMappingDto> reasons = new ArrayList<HoldReasonMappingDto>();
		for(Line line : lines) {
			for(HoldReasonMappingDto reason : map.get(line)) {
				reasons.add(reason);
			}
		}
		return new ArrayList<HoldReasonMappingDto>(reasons);	
		}
	
	private List<HoldReasonMappingDto> filterReasons(List<HoldReasonMappingDto> reasonList, QCAction qcAction) {
		List<HoldReasonMappingDto> filteredHoldReasons = new ArrayList<HoldReasonMappingDto>();

		for(HoldReasonMappingDto reason : reasonList) {
			if(reason.getQcActionId() == Integer.parseInt(qcAction.getQcActionId())) {
				filteredHoldReasons.add(reason);
			}
		}
		return filteredHoldReasons;
	}

	private List<HoldReasonMappingDto> filterReasonsByLine(Map<Line, List<HoldReasonMappingDto>> map, Line line) {
		List<HoldReasonMappingDto> filteredReasons;
		filteredReasons = map.get(line);
		if(filteredReasons == null) {
			return new ArrayList<HoldReasonMappingDto>();
		}
		return filteredReasons;
	}

	// === static api === //
	public List<Division> getDivisions() {
		List<Division> divisions = new ArrayList<Division>(getProcessPointIx().keySet());
		Collections.sort(divisions, new PropertyComparator<Division>(Division.class, "sequenceNumber"));
		return divisions;
	}

	public List<Division> getDivisions(ProductType productType) {
		List<Division> list = new ArrayList<Division>(getProcessPointIx().keySet());
		List<Division> divisions = new ArrayList<Division>();
		for (Division div : list) {
			Map<ProductType, List<ProcessPoint>> map = getProcessPointIx().get(div);
			if (map == null) {
				continue;
			}
			if (map.keySet().contains(productType) && !divisions.contains(div)) {
				divisions.add(div);
			}
		}
		Collections.sort(divisions, new PropertyComparator<Division>(Division.class, "sequenceNumber"));
		return divisions;
	}
	
	public Line getLine(Division division, String lineId) {
		if(division == null || lineId == null) {
			return null;
		}
		
		Line line = null;
		Map<String, Line> map = getLineIx().get(division);
		if(map == null) {
			return null;
		}
		for(Map.Entry<String, Line> mappedLine : map.entrySet()) {
			if(mappedLine.getKey().equals(lineId)) {
				line = mappedLine.getValue();
				break;
			}
		}
		return line;
	}
	
	public static List<Line> getLinesByDivision(Division division) {
		Map<String, Line> map = getInstance().getLineIx().get(division);
		if(map.isEmpty()) {
			return new ArrayList<Line>();
		}
		return new ArrayList<Line>(map.values());
	}
	
	 public static List<Line> getLinesWithReasons(Division division, QCAction qcAction) {
		 Map<Line, List<HoldReasonMappingDto>> holdReasonsMap = getInstance().getHoldReasonIx().get(division);
		 if(holdReasonsMap == null) {
			 return new ArrayList<Line>();
		 }
		List<Line> lines = getInstance().getLinesByQCAction(holdReasonsMap, qcAction);
		Collections.sort(lines, new PropertyComparator<Line>(Line.class, "lineId"));
		return lines;
	 }
	
	 public List<Division> getDivisionsWithReasons(QCAction qcAction) {
		 List<Division> divisions = new ArrayList<Division>(getHoldReasonIx().keySet());
		 List<Division> filteredDivisions = new ArrayList<Division>();
		 for(Division division : divisions) {
			 Map<Line, List<HoldReasonMappingDto>> map = getHoldReasonIx().get(division);
			 List<HoldReasonMappingDto> reasons = getReasons(map);

			 for(HoldReasonMappingDto reason : reasons) {

				 if (reason.getQcActionId() == Integer.parseInt(qcAction.getQcActionId())) {
				 filteredDivisions.add(division);
				 break;
				 }
			 }
		 }
		 return filteredDivisions;
	 }
	
	public static List<HoldReasonMappingDto> getReasonsByLineAndQcAction(
			Division division, Line line, QCAction qcAction) {

		List<HoldReasonMappingDto> holdReasons = getInstance().filterReasonsByLine(getInstance().getHoldReasonIx().get(division), line);
		if(holdReasons.isEmpty()) {
			return new ArrayList<HoldReasonMappingDto>();
		}
		List<HoldReasonMappingDto> reasons =  getInstance().filterReasons(holdReasons, qcAction);

		Collections.sort(reasons, new PropertyComparator<HoldReasonMappingDto>(HoldReasonMappingDto.class, "holdReason"));
		return reasons;
	}
	
	public static List<HoldReasonMappingDto> getReasonsByQcAction(Division division, QCAction qcAction) {
		Map<Line, List<HoldReasonMappingDto>> map = getInstance().getHoldReasonIx().get(division);
		
		List<HoldReasonMappingDto> reasons = getInstance().getReasons(map);
		List<HoldReasonMappingDto> filteredReasons = getInstance().filterReasons(reasons, qcAction);

		Collections.sort(filteredReasons, new PropertyComparator<HoldReasonMappingDto>(HoldReasonMappingDto.class, "holdReason"));
		return filteredReasons;
	}

	public static List<Division> getDunnageDivisions() {
		List<Division> divisions = new ArrayList<Division>(getInstance().getDiecastProductIx().keySet());
		Collections.sort(divisions, new PropertyComparator<Division>(Division.class, "sequenceNumber"));
		return divisions;
	}

	public static List<Division> getDiecastDivisions() {
		List<Division> divisions = new ArrayList<Division>(getInstance().machineIdIx.keySet());
		Collections.sort(divisions, new PropertyComparator<Division>(Division.class, "sequenceNumber"));
		return divisions;
	}

	public static String[] getMachineIds(Division division) {
		return getInstance().machineIdIx.get(division);
	}

	public static int getMachineCount() {
		int count = 0;
		Iterator<Division> it = getInstance().machineIdIx.keySet().iterator();
		while (it.hasNext()) {
			Division key = it.next();
			String[] ids = getInstance().machineIdIx.get(key);
			int i = 0;
			if (ids != null) {
				i = ids.length;
			}
			if (i > count) {
				count = i;
			}
		}
		return count;
	}

	public static QsrMaintenancePropertyBean getProperty() {
		return getInstance().property;
	}
	
	public QsrMaintenancePropertyBean getPropertyBean() {
		return property;
	}
	
	public static boolean isDisableProductIdCheck(String productType) {
		Map<String, Boolean> disableProdIdCheck = getProperty().isDisableProductIdCheckMap(Boolean.class);
		if (disableProdIdCheck != null){
			if (disableProdIdCheck.get(productType) != null)
				return disableProdIdCheck.get(productType);
		}
		return false;
	}
	
	public static boolean isOwnerProductHold(String productType) {
		Map<String, Boolean> ownerProdHold;
		if ((ownerProdHold = getProperty().isOwnerProductHoldMap(Boolean.class)) != null){
			if (ownerProdHold.get(productType) != null)
				return ownerProdHold.get(productType);
		}
		return false;	
	}
	
	public static BaseProduct getOwnerProduct(BaseProduct product){
		ProductTypeData productTypeData;
		ProductType ownerProductType = null;
		BaseProduct ownerProduct = null;
		
		if (product == null){
			Logger.getLogger().info("Product record does not exist.");
		} else if (product.getOwnerProductId() == null){
			Logger.getLogger().info("Owner ID for product " + product.getProductId().toString() + " is not assigned.");
		} else if ((productTypeData = ServiceFactory.getDao(ProductTypeDao.class).findByKey(product.getProductType().toString())) == null){
			Logger.getLogger().error("Product type " + product.getProductType().toString() + " does not exist.");
		} else if ((ownerProductType = productTypeData.getOwnerProductType()) == null){
			Logger.getLogger().error("Owner product type for " + productTypeData.getId() + " does not exist.");
		} else if ((ownerProduct = ProductTypeUtil.getProductDao(ownerProductType).findBySn(product.getOwnerProductId())) == null){
			Logger.getLogger().error("Product " + product.getOwnerProductId() + " does not exist.");
		}
		return ownerProduct;
	}

	public static Config getInstance() {
		String newApplicationId = ApplicationContext.getInstance().getApplicationId();
		if (instance == null || !newApplicationId.equals(instance.applicationId))
			instance = new Config();
		return instance;
	}
	
	public static Config getInstance(String applicationId) {
		for(Map.Entry<String, Config> entry: instances.entrySet()) {
			if(entry.getKey().equals(applicationId)) return entry.getValue();
		}
		
		Config newInstance = new Config();
		instances.put(applicationId, newInstance);
		
		return newInstance;
	}
	
	

	protected Map<Division, Map<ProductType, List<ProcessPoint>>> getProcessPointIx() {
		return processPointIx;
	}
	
	protected Map<Division, Map<String, Line>> getLineIx() {
		return lineIx;
	}
	
	protected Map<Division, Map<Line, List<HoldReasonMappingDto>>> getHoldReasonIx() {
		return holdReasonIx;
	}

	protected Division getProcessPointDivisionById(String divisionId) {
		for (Division division : getProcessPointIx().keySet()) {
			if (division.getDivisionId().equals(divisionId.trim())) {
				return division;
			}
		}
		return null;
	}
	
	protected Division getLineDivisionById(String divisionId) {
		for ( Division division : getLineIx().keySet()) {
			if(division.getDivisionId().equals(divisionId)) {
				return division;
			}
		}
		return null;
	}
	
	protected Division getHoldReasonsDivisionById(String divisionId) {
		for (Division division : getHoldReasonIx().keySet()) {
			if (StringUtils.trim(division.getDivisionId()).equals(StringUtils.trim(divisionId))) {
				return division;
			}
		}
		return null;
	}

	protected Map<Division, List<ProductType>> getDiecastProductIx() {
		return diecastProductTypeIx;
	}

	public boolean isDiecast(ProductType productType) {
		return ProductTypeUtil.isInstanceOf(productType, DieCast.class);
	}
	
	public static List<Plant> getPlants() {
		List<Plant> plants = ServiceFactory.getDao(PlantDao.class).findAll();
		if ((plants != null) && (plants.size() >0)) {
			Collections.sort(plants, new PropertyComparator<Plant>(Plant.class, "plantName"));
		}			
		return plants;
	}
	
	public static List<String> getModelCodes(ProductType productType) {
		List<String> modelCodes = ProductTypeUtil.getProductSpecDao(productType).findAllModelCodes(productType.name());
		if ((modelCodes != null) && (modelCodes.size() >0)) {
			Collections.sort(modelCodes);
		}			
		return modelCodes;
	}
	
	public HoldReasonMappingDao getHoldReasonMappingDao() {
		return ServiceFactory.getDao(HoldReasonMappingDao.class);
	}
	
	public DivisionDao getDivisionDao() {
		return ServiceFactory.getDao(DivisionDao.class);
	}
	
	public LineDao getLineDao() {
		return ServiceFactory.getDao(LineDao.class);
	}
}
