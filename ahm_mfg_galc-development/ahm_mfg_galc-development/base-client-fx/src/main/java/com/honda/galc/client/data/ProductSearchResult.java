package com.honda.galc.client.data;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.enumtype.ProductStatus;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.dao.qi.QiStationPreviousDefectDao;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiStationPreviousDefect;

public class ProductSearchResult {

	private static Map<String, List<QiRepairResultDto>> defectResultMap;

	private static Map<String, List<QiRepairResultDto>> defectsProcessing;

	private BaseProduct product;

	private DefectStatus defectStatus;

	private String errorMessage = "";

	private ProductStatus productStatus;
	private long lastKickoutId = 0;
	
	private String kickoutProcessPointName;
	
	private int kickoutDivisionSequence = Integer.MAX_VALUE;
	
	private int kickoutLineSequence = Integer.MAX_VALUE;
	
	private int KickoutProcessPointSequence = Integer.MAX_VALUE;
	
	private String searchProcessPointId = "";
	
	private Timestamp searchPpidTimestamp;
	
	private String trackingStatusName = "";

	private String productionLot = "";
	
	private Integer afOnSeqNum = 0;
	
	private static Map<String, String> trackingStatusMap;
	
	private static String currentWorkingProcessPointId = "";

	public ProductSearchResult(BaseProduct product) {
		this.product = product;
		this.productStatus = ProductStatus.NO_DEFECT;
		if(StringUtils.isBlank(product.getTrackingStatus())){
			this.trackingStatusName = "";
		} else {
			this.trackingStatusName = getTrackingStatusMap().get(product.getTrackingStatus());
			if (StringUtils.isBlank(this.trackingStatusName)) {
				this.trackingStatusName = getDao(LineDao.class).findByKey(product.getTrackingStatus()).getLineName().toString();
				getTrackingStatusMap().put(product.getTrackingStatus(), this.trackingStatusName);
			}
		}
		this.productionLot = "";
		this.afOnSeqNum = 0;
	}

	/**
	 * Get all the common defect for the List of products from the defect result map and
	 * puts them in the defect processing map.
	 * @param products
	 */
	public static List<QiRepairResultDto> initCommonDefects(List<BaseProduct> products) {
		clearDefectsProcessingMap();
		Set<QiCommonDefectResult> commonDefects = new HashSet<QiCommonDefectResult>();
		List<QiRepairResultDto> defectList =  new ArrayList<>();
		
		// filter defect results based on setting of previous defect visible
		List<QiStationPreviousDefect> previousDefectList = getDao(QiStationPreviousDefectDao.class).findAllByProcessPoint(getCurrentWorkingProcessPointId());
		List<String> entryDeptList = new ArrayList<String>();
		
		for(QiStationPreviousDefect qiStationPreviousDefect : previousDefectList ) {
			entryDeptList.add(qiStationPreviousDefect.getId().getEntryDivisionId());
		}

		if (products.size() == 1) {
			defectList = getDefectsForSingleProduct(products); // one product, load here
		} else {
			defectList =  new ArrayList<>(getDefectResults(products)); //multiple products, get from defectResultMap
		}
		if(!defectList.isEmpty()) {
			List<QiRepairResultDto> configuredDefectList = new ArrayList<QiRepairResultDto>();

			//first, seed the common defects with first products defects
			for(QiRepairResultDto defect : defectList) {
				if(products.get(0).getProductId().equals(defect.getProductId()) //get first product's defectss as init common defects
						&& entryDeptList.contains(defect.getEntryDept())) { // filter by entry dept
					commonDefects.add(new QiCommonDefectResult(defect));
				}
			}
			getDefectsProcessingMap().put(products.get(0).getProductId(), new ArrayList<QiRepairResultDto>());
			if(!commonDefects.isEmpty()) {  //for each common defect seeded from first product
				for(int i = 1; i < products.size(); i++) {  //for the remaining products
					BaseProduct product = products.get(i);
					List<QiCommonDefectResult> currentDefectList = new ArrayList<QiCommonDefectResult>();
					getDefectsProcessingMap().put(product.getProductId(), new ArrayList<QiRepairResultDto>());

					for(QiRepairResultDto defectResult : defectList) {  //create a list of common defects for the the current product
						if(defectResult.getProductId().equals(product.getProductId())) { //get next product's defect
							currentDefectList.add(new QiCommonDefectResult(defectResult));
						}
					}
					if(currentDefectList.isEmpty()) { //next product has no defect, there is no common defect 
						commonDefects.clear();
						break;
					}
					Iterator<QiCommonDefectResult> iter = commonDefects.iterator();  //iterator over common defects seeded from p0

					while(iter.hasNext()) {
						QiCommonDefectResult defect = iter.next();						
						if(!currentDefectList.contains(defect)) { //next product's defects doesn't have the common defect, remove the common defect from the list
							iter.remove();
						}
					} //if current product does not have the common defect, remove it since it is not common for all products
					if(commonDefects.isEmpty()) break;  //if any product has none of the common defects, there are no common defects
				}
			}

			if(!commonDefects.isEmpty()) {	// configured defects is the list of actual defects for each product that are common		
				for(QiRepairResultDto defect : defectList) {
					if(commonDefects.contains(new QiCommonDefectResult(defect))) {
						configuredDefectList.add(defect);
					}
				}
			}
			return configuredDefectList;
		}else return null;
	}

	/**
	 * @param products
	 * @return
	 */
	private static List<QiRepairResultDto> getDefectsForSingleProduct(List<BaseProduct> products) {
		return getDao(QiRepairResultDao.class).findAllDefectsByProductIds(Arrays.asList(products.get(0).getProductId()));
	}
	
	public static void initCommonChildDefect(List<QiRepairResultDto> commonMainDefects, List<BaseProduct> products) {
		List<QiRepairResultDto> removeList = new ArrayList<QiRepairResultDto>();
		if(products.size() > 1) {
			Set<QiCommonDefectResult> commonDefects = new HashSet<QiCommonDefectResult>();
			if (!commonMainDefects.isEmpty()) {
				for (QiRepairResultDto defect : commonMainDefects) {
					if (products.get(0).getProductId().equals(defect.getProductId())) {
						commonDefects.add(new QiCommonDefectResult(defect));
					}
				}
			}

			//Find all main defects which don't have same common child defect		
			if (!commonDefects.isEmpty()) {
				for (QiCommonDefectResult commonDefect : commonDefects) {
					QiRepairResultDto commomDefectForFirstProduct = new QiRepairResultDto();
					List<QiCommonDefectResult> childDefectsForFirstProduct = new ArrayList<QiCommonDefectResult>();
					boolean isChildDefectMatch = true;				
					for (QiRepairResultDto mainDefect : commonMainDefects) {
						if (commonDefect.equals(new QiCommonDefectResult(mainDefect))) {
							if(!isChildDefectMatch) {
								removeList.add(mainDefect);
							}else {
								if (commomDefectForFirstProduct == null
										|| commomDefectForFirstProduct.getDefectResultId() == 0) {
									commomDefectForFirstProduct = mainDefect;
									List<QiRepairResultDto> childDefects = mainDefect.getChildRepairResultList();
									if (childDefects != null && childDefects.size() > 0) {
										for (QiRepairResultDto childDefect : childDefects) {
											childDefectsForFirstProduct.add(new QiCommonDefectResult(childDefect));
										}
									}
								} else {
									List<QiRepairResultDto> childDefects = mainDefect.getChildRepairResultList();
									if (childDefects == null) {
										if (childDefectsForFirstProduct.size() > 0) {
											isChildDefectMatch = false;
										}

									} else {
										List<QiCommonDefectResult> currentCommonChildDefects = new ArrayList<QiCommonDefectResult>();
										for (QiRepairResultDto childDefect : childDefects) {
											currentCommonChildDefects.add(new QiCommonDefectResult(childDefect));
										}
										if (childDefectsForFirstProduct.size() != childDefects.size() || !childDefectsForFirstProduct.containsAll(currentCommonChildDefects)) {
											isChildDefectMatch = false;
										}
									}
								}
								if(!isChildDefectMatch) {
									removeList.add(mainDefect);
								}
							}
							
						}
					}
					if(!isChildDefectMatch && (commomDefectForFirstProduct !=null && commomDefectForFirstProduct.getDefectResultId() != 0)) {
						removeList.add(commomDefectForFirstProduct);
					}
				}
			}
		}
		
		commonMainDefects.removeAll(removeList);
		if(!commonMainDefects.isEmpty()) {
			for(QiRepairResultDto defect : commonMainDefects) {
				addNewDefect(defect);
			}
		}
	}

	/**
	 * Add a new defect to the current defects processing map.
	 * 
	 * @param defect
	 */
	public static void addNewDefect(QiDefectResult defectResult) {
		QiRepairResultDto defectResultDto = new QiRepairResultDto(defectResult, 0);
		defectResultDto.setActualTimestamp(defectResult.getActualTimestamp());
		ProductSearchResult.addNewDefect(defectResultDto);
	}

	/**
	 * Add a new defect to the current defects processing map.
	 * 
	 * @param defect
	 */
	public static void addNewDefect(QiRepairResultDto defect) {
		List<QiRepairResultDto> defectList = getDefectsProcessingMap().get(defect.getProductId());
		if(defectList == null) {
			defectList = new ArrayList<QiRepairResultDto>();
			getDefectsProcessingMap().put(defect.getProductId(), defectList);
		}

		if(!defectList.contains(defect)) {
			defectList.add(defect);
		}
	}

	/**
	 * Add a new child defect to the current defects processing map.
	 * 
	 * @param childDefect
	 * @return
	 */
	public static QiRepairResultDto addChildDefect(QiRepairResultDto childDefect) {
		List<QiRepairResultDto> defectList = getDefectsProcessing(childDefect.getProductId());
		List<QiRepairResultDto> childList = new ArrayList<QiRepairResultDto>();
		for(QiRepairResultDto defect : defectList) {
			if(defect.getDefectResultId() == childDefect.getDefectResultId()) {
				if(defect.getChildRepairResultList() == null) {
					defect.setChildRepairResultList(childList);
				}
				defect.getChildRepairResultList().add(childDefect);
				return defect;
			}
		}
		return null;
	}

	/**
	 * Remove all elements from the defect result map
	 */
	public static void clearDefectResultMap() 
	{
		getDefectResultMap().clear();
	}

	/**
	 * Remove all elements from the defects processing map
	 */
	public static void clearDefectsProcessingMap() {
		getDefectsProcessingMap().clear();
	}

	/**
	 * Updates the defect result dto objects in the defect processing map.
	 * If the defect does not exist in the 
	 * 
	 * @param defectResults
	 */
	public static void updateDefectsProcessingMap(List<QiDefectResult> defectResults) {
		for(QiDefectResult defectResult : defectResults) {
			QiRepairResultDto defectResultDto = new QiRepairResultDto(defectResult, 0);
			defectResultDto.setActualTimestamp(defectResult.getActualTimestamp());
			ProductSearchResult.updateDefectsProcessingMap(defectResultDto);
		}
	}

	/**
	 * 
	 * Updates the child defect with the provided QiRepairResultDto for the product id.
	 * If the defect does not exist, it will be added.
	 * @param productId
	 * @param defectResult
	 */
	private static void updateChildDefect(String productId, QiRepairResultDto defectResult) {
		QiRepairResultDto childDefect = getChildDefect(productId, defectResult.getDefectResultId(), defectResult.getRepairId());
		if(childDefect != null) {
			QiRepairResultDto parentDefect = getParentProductDefect(defectResult, productId);
			if(parentDefect != null) {
				parentDefect.getChildRepairResultList().remove(childDefect);
				parentDefect.getChildRepairResultList().add(defectResult);
			}
		} else  {
			addChildDefect(defectResult);
		}
	}
	/**
	 * 
	 * Updates the parent defect with the provided QiRepairResultDto for the product id.
	 * If the defect does not exist, it will be added.
	 * 
	 * @param productId
	 * @param defectResult
	 */
	private static void updateParentDefect(String productId, QiRepairResultDto defectResult) {
		QiRepairResultDto defect = getDefectResult(defectResult, productId);
		if(defect != null) {
			defectResult.setChildRepairResultList(defect.getChildRepairResultList());
			getDefectsProcessing(productId).remove(defect);
			getDefectsProcessing(productId).add(defectResult);
			replaceInMainMapByProductId(productId, defectResult);
		} else {
			addNewDefect(defectResult);
		}
	}

	/**
	 * Updates the defects processing map with the provided defect.
	 * If the defect does not exist, it will be added.
	 * 
	 * @param defectResult
	 * @return
	 */
	public static QiRepairResultDto updateDefectsProcessingMap(QiRepairResultDto defectResult) {

		if(isChildDefect(defectResult)) {
			updateChildDefect(defectResult.getProductId(), defectResult);
		} else {
			updateParentDefect(defectResult.getProductId(), defectResult);
		}
		return defectResult;
	}

	/**
	 * Update the defect the defect processing map
	 * 
	 * @param defectResult
	 * @return
	 */
	public static QiRepairResultDto updateDefectsProcessingMapS(QiRepairResultDto defectResult) {
		List<QiRepairResultDto> productDefects = getDefectsProcessingMap().get(defectResult.getProductId());
		if(productDefects == null) { 
			addNewDefect(defectResult);
		} else {
			QiRepairResultDto cachedDefect = new QiRepairResultDto();
			QiRepairResultDto currentDefectResult = new QiRepairResultDto();
			boolean found = false;
			ListIterator<QiRepairResultDto> mainIter = productDefects.listIterator();

			while(mainIter.hasNext()) {
				currentDefectResult = mainIter.next();

				if(currentDefectResult.getDefectResultId() == defectResult.getDefectResultId()) {
					found = true;
					cachedDefect = getDefectResult(currentDefectResult, defectResult.getProductId());
					break;
				}
			}
			if(found) {
				if(isChildDefect(defectResult)) {
					ListIterator<QiRepairResultDto> childIter = cachedDefect.getChildRepairResultList().listIterator();
					if(currentDefectResult.getChildRepairResultList() != null && !currentDefectResult.getChildRepairResultList().isEmpty()) {
						while (childIter.hasNext()) {
							QiRepairResultDto currentChildDefect = childIter.next();
							if(currentChildDefect.getRepairId() == defectResult.getRepairId()) {
								childIter.set(defectResult);
								break;
							}
						}
					}
				} else {
					productDefects.remove(currentDefectResult);
					productDefects.add(defectResult);
				}

			} else {
				productDefects.add(defectResult);
			}
		} 
		return defectResult;
	}

	/**
	 * 
	 * Checks if the defect is a parent or child.
	 * 
	 * @param defectResult
	 * @return
	 */
	public static boolean isChildDefect(QiRepairResultDto defectResult) {
		return defectResult.getRepairId() != 0;
	}

	/**
	 * Update the defect the defect processing map
	 * 
	 * @param defectResult
	 * @param repairId
	 * @return
	 */
	public static QiRepairResultDto updateDefectResult(QiDefectResult defectResult, long repairId) {
		QiRepairResultDto defectResultDto = new QiRepairResultDto(defectResult, repairId);
		defectResultDto.setActualTimestamp(new Timestamp(defectResult.getActualTimestamp().getTime()));
		defectResultDto.setRepairId(repairId);
		ProductSearchResult.updateDefectsProcessingMap(defectResultDto);
		return defectResultDto;
	}

	/**
	 * Updated the defect result map with the contents of the current defects processing map.
	 * The current contents of defectResultMap will be replaced with the contents of
	 * defectsProcessingMap and the defectsProcessingMap will be cleared.
	 */
	public static void updateDefectResultCache() {
		Set<String> productIds = getDefectsProcessingMap().keySet();
		for(String productId : productIds) {
			getDefectResultMap().remove(productId);
		}
		getDefectsProcessingMap().clear();
	}

	public static void removeChildDefect(long repairId) {
		for(String productId : getDefectsProcessingMap().keySet()) {
			for(QiRepairResultDto defect : getDefectsProcessingMap().get(productId)) {
				QiRepairResultDto childDefect = getChildDefect(defect.getProductId(), defect.getDefectResultId(), repairId);
				if(childDefect != null) {
					defect.getChildRepairResultList().remove(childDefect);
					defect.setRepairId(0);
				}
			}
		}
	}
	
	public static void removeChildDefect(long repairId, String productId) {
		for(QiRepairResultDto defect : getDefectsProcessingMap().get(productId)) {
			QiRepairResultDto childDefect = getChildDefect(defect.getProductId(), defect.getDefectResultId(), repairId);
			if(childDefect != null) {
				defect.getChildRepairResultList().remove(childDefect);
				defect.setRepairId(0);
				break;
			}
		}
	}

	/**
	 * Remove the processed products from cache
	 * 
	 */
	public static void removeProcessedProducts(List<BaseProduct> productList) {
		for(BaseProduct product :productList) {
			getDefectResultMap().remove(product.getProductId());
		}
		clearDefectsProcessingMap();
	}
	
	/**
	 * Remove the processed products from cache
	 * 
	 */
	public static void removeProcessedProducts() {
		for(String product: getDefectsProcessingMap().keySet()) {
			getDefectResultMap().remove(product);
		}
		clearDefectsProcessingMap();
	}

	/**
	 * Set the defect results cache
	 * 
	 * @param defectResults
	 */
	public static void setDefectResults(List<QiRepairResultDto> defectResults) {
		for(QiRepairResultDto defectResult : defectResults) {
			String productId = defectResult.getProductId();
			List<QiRepairResultDto> productDefects = getDefectResultMap().get(productId);
			if(productDefects== null || productDefects.isEmpty()) {
				productDefects = new ArrayList<QiRepairResultDto>();
				getDefectResultMap().put(productId, productDefects);
			}

			ListIterator<QiRepairResultDto> iter = productDefects.listIterator();
			boolean found = false;
			while(iter.hasNext()) {
				QiRepairResultDto productDefect = iter.next();
				if(productDefect.getDefectResultId() == defectResult.getDefectResultId())  {
					found = true;
				}
			}
			if(!found) {
				productDefects.add(defectResult);
			}
		} 
	}

	public static void updateDefectRepairStatusFixed(QiRepairResultDto defectResult) {
		defectResult.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
		updateDefectsProcessingMap(defectResult);
	}

	public static void updateChildDefectRepairStatusFixed(QiRepairResultDto childDefect, long repairId) {
		childDefect.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
		updateDefectsProcessingMap(childDefect);
	}

	/**
	 * Returns a list of the current defects for the Bulk Repair Entry Screen
	 * 
	 * @return
	 **/
	public static List<QiRepairResultDto> getDefectsProcessing() {
		List<QiRepairResultDto> returnList = new ArrayList<QiRepairResultDto>();
		for(List<QiRepairResultDto> defectList : getDefectsProcessingMap().values()) {
			returnList.addAll(defectList);
		}
		return returnList;
	}

	/**
	 * Returns a list of all defects in cache
	 * 
	 * @return
	 **/
	public static List<QiRepairResultDto> getAllDefectResults() {
		List<QiRepairResultDto> returnList = new ArrayList<QiRepairResultDto>();
		for(List<QiRepairResultDto> defectList : getDefectResultMap().values()) {
			returnList.addAll(defectList);
		}
		return returnList;
	}

	/**
	 * Returns a List of all the common defects in the current defects processing map
	 * 
	 * @param defect
	 * @return
	 */
	public static List<QiRepairResultDto> getCommonDefects(QiRepairResultDto defect) {
		List<QiRepairResultDto> returnDefectList = new ArrayList<QiRepairResultDto>();
		QiCommonDefectResult commonDefect = createQiCommonDefectResult(defect);
		List<QiRepairResultDto> defectList = getDefectsProcessing();
		for(QiRepairResultDto currentDefect : defectList) {
			if(createQiCommonDefectResult(currentDefect).equals(commonDefect)) {
				returnDefectList.add(currentDefect);
			}
		}

		return returnDefectList;
	}


	/**
	 * Returns a List of all duplicate defects for that product, in the current defects processing map
	 * 
	 * @param defect
	 * @return
	 */
	public static List<QiRepairResultDto> getDuplicateDefectsForProduct(QiRepairResultDto defect, String productId) {
		List<QiRepairResultDto> returnDefectList = new ArrayList<QiRepairResultDto>();
		QiCommonDefectResult commonDefect = createQiCommonDefectResult(defect);
		List<QiRepairResultDto> defectList = getDefectsProcessing(productId);
		for(QiRepairResultDto currentDefect : defectList) {
			if(createQiCommonDefectResult(currentDefect).equals(commonDefect)) {
				returnDefectList.add(currentDefect);
			}
		}

		return returnDefectList;
	}

	/**
	 * Returns the child defect for the product id by the repair id
	 * 
	 * @param productId
	 * @param repairId
	 * @return
	 */
	public static QiRepairResultDto getChildDefect(String productId, long repairId) {

		QiRepairResultDto defect = null;
		List<QiRepairResultDto> returnDefectList = getDefectsProcessing(productId);
		if(returnDefectList == null) {
			return null;
		}
		for(QiRepairResultDto currentDefect : returnDefectList) {
			if(currentDefect.getChildRepairResultList() != null) {
				for(QiRepairResultDto childDefect : currentDefect.getChildRepairResultList()) {
					if(childDefect.getRepairId() == repairId) {
						defect = childDefect;
						break;
					}					
				}
			}
			if(defect != null && defect.getRepairId() != 0) break;
		}
		return defect;
	}
	
	public static QiRepairResultDto getChildDefect(String productId, long defectResultId, long repairId) {
		QiRepairResultDto defect = null;
		List<QiRepairResultDto> returnDefectList = getDefectsProcessing(productId);
		if(returnDefectList == null) {
			return null;
		}
		for(QiRepairResultDto currentDefect : returnDefectList) {
			if(currentDefect.getDefectResultId() == defectResultId && currentDefect.getChildRepairResultList() != null) {
				for(QiRepairResultDto childDefect : currentDefect.getChildRepairResultList()) {
					if(childDefect.getRepairId() == repairId) {
						defect = childDefect;
						break;
					}					
				}
			}
			if(defect != null && defect.getRepairId() != 0) break;
		}
		return defect;
	}

	/**
	 * Returns the child defect for the product that matches the common child defect
	 * 
	 * @param parentDefect QiRepairResultDto
	 * @param commonChildDefect QiRepairResultDto
	 * @return
	 */
	public static QiRepairResultDto getChildDefect(QiRepairResultDto parentDefect, QiRepairResultDto commonChildDefect) {

		List<QiRepairResultDto> returnDefectList = getDefectsProcessing(parentDefect.getProductId());
		if(commonChildDefect == null) {
			return null;
		}
		for(QiRepairResultDto currentDefect : returnDefectList) {
			if(currentDefect.getChildRepairResultList() == null) {
				continue;
			}
			List<QiRepairResultDto> childDefectList = parentDefect.getChildRepairResultList();
			if(childDefectList != null) {
				for(QiRepairResultDto childDefect : childDefectList) {
					if(createQiCommonDefectResult(childDefect).equals(createQiCommonDefectResult(commonChildDefect))) {
						return childDefect;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns the defect Result Id for the common defect applied to the product id.
	 * 
	 * If non defect is found a 0 is returned
	 * 
	 * @param defectResult
	 * @param productId
	 * @return
	 */
	public static long getRepairResultId(QiRepairResultDto defectResult, QiRepairResultDto childDefectResult, String productId) {
		List<QiRepairResultDto> productDefects = getDefectsProcessingMap().get(productId);
		if(productDefects == null) {
			return 0;
		}
		QiCommonDefectResult commonChildDesectResult = createQiCommonDefectResult(childDefectResult);
		for(QiRepairResultDto currentDefectResult : productDefects) {
			if(createQiCommonDefectResult(currentDefectResult).equals(createQiCommonDefectResult(defectResult))) {
				QiRepairResultDto childDefect = getChildDefect(currentDefectResult, commonChildDesectResult);
				return childDefect.getRepairId();
			}
		}
		return 0;
	}

	/**
	 * Returns the defect Result Id for the common defect applied to the product id.
	 * 
	 * If non defect is found a 0 is returned
	 * 
	 * @param defectResult
	 * @param productId
	 * @return
	 */
	public static long getRepairResultId(QiRepairResultDto defectResult, QiCommonDefectResult childDefectResult, String productId) {
		List<QiRepairResultDto> productDefects = getDefectsProcessingMap().get(productId);
		if(productDefects == null) {
			return 0;
		}
		for(QiRepairResultDto currentDefectResult : productDefects) {
			if(createQiCommonDefectResult(currentDefectResult).equals(createQiCommonDefectResult(defectResult))) {
				QiRepairResultDto childDefect = getChildDefect(currentDefectResult, childDefectResult);
				return childDefect.getRepairId();
			}
		}
		return 0;
	}

	/**
	 * Returns the defect Result Id for the common defect applied to the product id.
	 * 
	 * If non defect is found a 0 is returned
	 * 
	 * @param defectResult
	 * @param productId
	 * @return
	 */
	public static long getDefectResultId(QiRepairResultDto defectResult, String productId) {
		List<QiRepairResultDto> productDefects = getDefectsProcessingMap().get(productId);
		if(productDefects == null) {
			return 0;
		}
		for(QiRepairResultDto currentDefectResult : productDefects) {
			if(createQiCommonDefectResult(currentDefectResult).equals(createQiCommonDefectResult(defectResult))) {
				return currentDefectResult.getDefectResultId();
			}
		}
		return 0;
	}

	public static QiRepairResultDto getDefectResult(QiRepairResultDto defectResult, String productId) {
		List<QiRepairResultDto> productDefects = getDefectsProcessingMap().get(productId);
		if(productDefects == null) {
			return null;
		}
		for(QiRepairResultDto currentDefectResult : productDefects) {
			if(currentDefectResult.getDefectResultId() == defectResult.getDefectResultId()) {
				return currentDefectResult;
			}
		}
		return null;
	}
	
	public static QiRepairResultDto getDefectResult(QiCommonDefectResult parentDefect, String productId) {
		List<QiRepairResultDto> productDefects = getDefectsProcessingMap().get(productId);
		if(productDefects == null) {
			return null;
		}
		for(QiRepairResultDto currentDefectResult : productDefects) {
			if(createQiCommonDefectResult(currentDefectResult).equals(parentDefect)) {
				return currentDefectResult;
			}
		}
		return null;
	}

	public static List<QiRepairResultDto> getDefectResultList(QiCommonDefectResult parentDefect, String productId) {
		short parentDefectStatus = parentDefect.getCurrentDefectStatus();
		List<QiRepairResultDto> productDefects = getDefectsProcessingMap().get(productId);
		if(productDefects == null || productDefects.isEmpty()) {
			return null;
		}
		List<QiRepairResultDto> duplicateDefects = new ArrayList<>();
		try {
			for (QiRepairResultDto currentDefectResult : productDefects) {
				//To account for the case where multiple repaired actual problems are being added
				//In this case, the first repaired actual problem would have fixed the main defect
				//so, we want to match the selected defect even when the defect status does not match
				//--where the defect will be NOT_FIXED when selected, but the cached defect has already been fixed
				QiCommonDefectResult qiCommonDR = createQiCommonDefectResult(currentDefectResult);
				if (qiCommonDR != null && qiCommonDR.getCurrentDefectStatus() == DefectStatus.FIXED.getId()) {
					parentDefect.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
				}
				if (qiCommonDR.equals(parentDefect)) {
					duplicateDefects.add(currentDefectResult);
				}
				parentDefect.setCurrentDefectStatus(parentDefectStatus); //reset original parent defect status
			} 
		} finally {
			parentDefect.setCurrentDefectStatus(parentDefectStatus); //whatever happens - reset original parent defect status
		}
		return duplicateDefects;
	}

	/**
	 * Returns the parent defect for the defect result and product id
	 * 
	 * @param defectResult
	 * @param productId
	 * @return
	 */
	public static QiRepairResultDto getActualParentDefect(QiRepairResultDto childDefectResult, String productId) {
		List<QiRepairResultDto> mainDefectList = getDefectsProcessing(productId);
		QiRepairResultDto parentDefect = null;
		if(mainDefectList !=null ) {
			for(QiRepairResultDto currentMainDefect : mainDefectList) {
				if(currentMainDefect.getDefectResultId() == childDefectResult.getDefectResultId())  {
					parentDefect = currentMainDefect;
					break;
				}
			}
		}
		return parentDefect;
	}
	
	/**
	 * Returns the parent defect for the defect result and product id
	 * 
	 * @param defectResult
	 * @param productId
	 * @return
	 */
	public static QiRepairResultDto getParentProductDefect(QiRepairResultDto childDefectResult, String productId) {
		QiCommonDefectResult commonChildDefectResult = createQiCommonDefectResult(childDefectResult);
		return getParentProductDefect(commonChildDefectResult, productId);
	}
	
	/**
	 * Returns any parent defect that has a matching child defect
	 * If there are duplicates, it returns the first one found
	 * @param defectResult
	 * @param productId
	 * @return
	 */
	public static QiRepairResultDto getParentProductDefect(QiCommonDefectResult commonChildDefectResult, String productId) {
		List<QiRepairResultDto> parentDefectList = getDefectsProcessing(productId);
		if(parentDefectList !=null ) {
			for(QiRepairResultDto currentParentDefect : parentDefectList) {
				QiRepairResultDto childDefect = getChildDefect(currentParentDefect, commonChildDefectResult);
				if(childDefect != null)  {
					if(commonChildDefectResult.equals(createQiCommonDefectResult(childDefect))) {
						return currentParentDefect;
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Returns parent defect that has a matching child defect and matching main defect
	 * If there are duplicates, it returns the first one found
	 * @param commonChildDefect
	 * @param commonMainDefect+
	 * @param productId
	 * @return
	 */
	public static QiRepairResultDto getParentProductDefect(QiCommonDefectResult commonChildDefectResult, QiCommonDefectResult commonMainDefect, String productId) {
		List<QiRepairResultDto> parentDefectList = getDefectsProcessing(productId);
		if(parentDefectList !=null ) {
			for(QiRepairResultDto currentParentDefect : parentDefectList) {
				if(commonMainDefect.equals(createQiCommonDefectResult(currentParentDefect))) {
					QiRepairResultDto childDefect = getChildDefect(currentParentDefect, commonChildDefectResult);
					if(childDefect != null)  {
						if(commonChildDefectResult.equals(createQiCommonDefectResult(childDefect))) {
							return currentParentDefect;
						}
					}
				}
			}
		}
		
		return null;
	}
	
	public static QiRepairResultDto getDefectResultByDefectId(long defectResultId, String productId)  {
		
		List<QiRepairResultDto> all = getDefectsProcessing(productId);
		if(all !=null ) {
			for(QiRepairResultDto currentDefect : all) {
				if(defectResultId == currentDefect.getDefectResultId())  return currentDefect;
			}
		}
		return null;
	}

	/**
	 * Returns a List all defects in the defect for product id
	 * 
	 * @param productId
	 * @return
	 */
	public static List<QiRepairResultDto> getDefectResults(String productId) {
		List<QiRepairResultDto> defectResults = getDefectResultMap().get(productId);
		if(defectResults == null) {
			defectResults = new ArrayList<QiRepairResultDto>();
		}
		return defectResults;
	}

	/**
	 * Returns a List all defects in the defect for product id
	 * 
	 * @param productId
	 * @return
	 */
	public static void replaceInMainMapByProductId(String productId, QiRepairResultDto updated) {
		List<QiRepairResultDto> defectResults = getDefectResultMap().get(productId);
		if(updated == null || defectResults == null || defectResults.size() == 0) return;
		else  {
			ListIterator<QiRepairResultDto> lit = defectResults.listIterator();
			if(lit == null)  return;
			while(lit.hasNext())  {
				QiRepairResultDto dto = lit.next();
				if(dto != null && dto.getDefectResultId() == updated.getDefectResultId())  {
					lit.set(updated);					
				}
			}
		}
	}

	public static QiRepairResultDto getChildDefect(QiRepairResultDto parentDefect, QiCommonDefectResult commonChildDefect) {
		if(parentDefect.getChildRepairResultList() != null) {
			for(QiRepairResultDto childDefect : parentDefect.getChildRepairResultList()) {
				if(createQiCommonDefectResult(childDefect).equals(commonChildDefect)) {
					return childDefect;
				}
			}
		}
		return null;
	}

	public static Map<String, List<QiRepairResultDto>> getDefectResultMap() {
		if(defectResultMap == null) {
			defectResultMap = new HashMap<String, List<QiRepairResultDto>>();
		}
		return defectResultMap;
	}

	private static Map<String, List<QiRepairResultDto>> getDefectsProcessingMap() {
		if(defectsProcessing == null) {
			defectsProcessing = new HashMap<String, List<QiRepairResultDto>>();
		}
		return defectsProcessing;
	}

	public static int getProcessedProductsCount() {
		return getDefectsProcessing().size();
	}

	public static QiCommonDefectResult createQiCommonDefectResult(QiRepairResultDto defectResult) {
		return new QiCommonDefectResult(defectResult);
	}

	public List<QiRepairResultDto> getDefectResults() {
		return getDefectResults(this.getProduct().getProductId());
	}
	
	private static List<QiRepairResultDto> getDefectResults(List<BaseProduct> productList) {
		List<QiRepairResultDto> defectList = new ArrayList<QiRepairResultDto>();
		for(BaseProduct product : productList) {
			List<QiRepairResultDto> productDefectList = getDefectResults(product.getProductId());
			if(!productDefectList.isEmpty()) {
				defectList.addAll(productDefectList);
			}
		}
		return defectList;
	}

	/**
	 * Returns a list of the defects that are currently being processed for the selected product
	 * @param qirapairResult
	 * @return
	 */
	public static List<QiRepairResultDto> getDefectsProcessing(String productId) {
		return getDefectsProcessingMap().get(productId);
	}

	public BaseProduct getProduct() {
		return product;
	}

	public void setProduct(BaseProduct product) {
		this.product = product;
	}

	public DefectStatus getDefectStatus() {
		return defectStatus;
	}

	public void setDefectStatus(DefectStatus defectStatus) {
		this.defectStatus = defectStatus;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public ProductStatus getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(ProductStatus productStatus) {
		this.productStatus = productStatus;
	}

	public long getLastKickoutId() {
		return lastKickoutId;
	}

	public void setLastKickoutId(long lastKickoutId) {
		this.lastKickoutId = lastKickoutId;
	}

	public String getKickoutProcessPointName() {
		return kickoutProcessPointName;
	}

	public void setKickoutProcessPointName(String kickoutProcessPointName) {
		this.kickoutProcessPointName = kickoutProcessPointName;
	}

	public int getKickoutDivisionSequence() {
		return kickoutDivisionSequence;
	}

	public void setKickoutDivisionSequence(int kickoutDivisionSequence) {
		this.kickoutDivisionSequence = kickoutDivisionSequence;
	}

	public int getKickoutLineSequence() {
		return kickoutLineSequence;
	}

	public void setKickoutLineSequence(int kickoutLineSequence) {
		this.kickoutLineSequence = kickoutLineSequence;
	}

	public int getKickoutProcessPointSequence() {
		return KickoutProcessPointSequence;
	}

	public void setKickoutProcessPointSequence(int kickoutProcessPointSequence) {
		KickoutProcessPointSequence = kickoutProcessPointSequence;
	}

	public String getSearchProcessPointId() {
		return searchProcessPointId;
	}

	public void setSearchProcessPointId(String searchProcessPointId) {
		this.searchProcessPointId = searchProcessPointId;
	}

	public Timestamp getSearchPpidTimestamp() {
		return searchPpidTimestamp;
	}

	public void setSearchPpidTimestamp(Timestamp searchPpidTimestamp) {
		this.searchPpidTimestamp = searchPpidTimestamp;
	}
	
	public String getFormattedProductUpdateTimestamp() {
		Date timestamp = product.getUpdateTimestamp();
		return timestamp == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(product.getUpdateTimestamp());
	}
	
	public String getFormattedSearchPpIdTimestamp() {
		Timestamp timestamp = getSearchPpidTimestamp();
				return timestamp == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
	}
	
	public String getTrackingStatusName() {
		return trackingStatusName;
	}

	public void setTrackingStatusName(String trackingStatusName) {
		this.trackingStatusName = trackingStatusName;
	}

	public String getDunnage() {
		String dunnage = product.getDunnage();
		return dunnage == null ? "" : dunnage;
	}
	
	/**
	 * @return the productionLot
	 */
	public String getProductionLot() {
		return productionLot;
	}

	/**
	 * @param productionLot the productionLot to set
	 */
	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	/**
	 * @return the afOnSeqNum
	 */
	public Integer getAfOnSeqNum() {
		return afOnSeqNum;
	}

	/**
	 * @param afOnSeqNum the afOnSeqNum to set
	 */
	public void setAfOnSeqNum(Integer afOnSeqNum) {
		this.afOnSeqNum = afOnSeqNum;
	}
	
	public static Map<String, String> getTrackingStatusMap() {
		if(trackingStatusMap == null) {
			trackingStatusMap = new HashMap<String, String>(); //key: tracking_status, value: tracking_status_name
		}
		return trackingStatusMap;
	}

	public static String getCurrentWorkingProcessPointId() {
		return currentWorkingProcessPointId;
	}

	public static void setCurrentWorkingProcessPointId(String currentWorkingProcessPointId) {
		ProductSearchResult.currentWorkingProcessPointId = currentWorkingProcessPointId;
	}
}