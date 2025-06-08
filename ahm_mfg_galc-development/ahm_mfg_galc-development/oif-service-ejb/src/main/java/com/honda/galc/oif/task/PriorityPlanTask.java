 package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.HostPriorityPlanDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.HostPriorityPlan;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.Knuckle;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * <h3>PriorityPlanTask Class description</h3>
 * <p> PriorityPlanTask description </p>
 * Priority plan task is an OIF task, which executes every day at 5:00 am.(The setting can be change)
 * It retreives data from HCM_TAG3051_TBX table to get the original priority production lans. 
 * every priority plan is converted to a pre-production lot in GAL212TBX (pre-production lot), production lot in GAL217TBX, 
 * its coresponding product ids in GAL131TBX (engine) table or GALC143TBX(frame).
 * if the task is running for plant 1, it also merges plant 1 and plant2 schedule and creates knuckle pre-production lot in GAL212TBX, 
 * production lot in GAL217TBX and corresponding knuckle serial numbers in SUB_PRODUCT tables.
 * When creating knuckle pre-production lot, The host priority plans in a same kd lot is reversed from the original production sequence
 * to keep the knuckles in the frame production lot sequence at knuckle shipping station.
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
 * Nov 5, 2010
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class PriorityPlanTask extends OifAbstractTask implements IEventTaskExecutable{
	
	private static String LINE_NUMBER_1 = "01";
	private static String LINE_NUMBER_2 = "02";
	private static String LINE_NUMBER_3 = "03";
	private static int KSN_DIGITS = 6;
	private static String PRODUCT_IDS_CREATED = "P";
	
	private static String PLANT2_MODEL_CODES = "PLANT2_MODEL_CODES";
	private static String PLANT1_COUNT = "P1_COUNT";
	private static String PLANT2_COUNT = "P2_COUNT";
	private static String PLANT1_FIRST = "P1_FIRST";
	private static String MERGE_NEXT_DAY_PLANT2_LOTS = "MERGE_NEXT_DAY_PLANT2_LOTS";
	private static String MS_OFF_LINE_ID = "MS_OFF_LINE_ID";
	private static String MS_OFF_PPID = "MS_OFF_PPID";
	private static String MS_FK_OFF_LINE_ID = "MS_FK_OFF_LINE_ID";
	private static String MS_FK_OFF_PPID = "MS_FK_OFF_PPID";
	private static String LOT_CREATION_DELAY = "LOT_CREATION_DELAY";
	private static String KNUCKLE_PROCESS_LOCATION = "KNUCKLE_PROCESS_LOCATION";
	
	private static Map<Character,Integer> letterValues = new HashMap<Character,Integer>();
	private static Map<Integer,Integer> positionFactors = new HashMap<Integer,Integer>();
	static {
		letterValues.put('A',1);
		letterValues.put('B',2);
		letterValues.put('C',3);
		letterValues.put('D',4);
		letterValues.put('E',5);
		letterValues.put('F',6);
		letterValues.put('G',7);
		letterValues.put('H',8);
		letterValues.put('J',1);
		letterValues.put('K',2);
		letterValues.put('L',3);
		letterValues.put('M',4);
		letterValues.put('N',5);
		letterValues.put('P',7);
		letterValues.put('R',9);
		letterValues.put('S',2);
		letterValues.put('T',3);
		letterValues.put('U',4);
		letterValues.put('V',5);
		letterValues.put('W',6);
		letterValues.put('X',7);
		letterValues.put('Y',8);
		letterValues.put('Z',9);
		letterValues.put('1',1);
		letterValues.put('2',2);
		letterValues.put('3',3);
		letterValues.put('4',4);
		letterValues.put('5',5);
		letterValues.put('6',6);
		letterValues.put('7',7);
		letterValues.put('8',8);
		letterValues.put('9',9);
		letterValues.put('0',0);

	}
	
	static {
		positionFactors.put(1,8);
		positionFactors.put(2,7);
		positionFactors.put(3,6);
		positionFactors.put(4,5);
		positionFactors.put(5,4);
		positionFactors.put(6,3);
		positionFactors.put(7,2);
		positionFactors.put(8,1);
		positionFactors.put(9,0);
		positionFactors.put(10,9);
		positionFactors.put(11,8);
		positionFactors.put(12,7);
		positionFactors.put(13,6);
		positionFactors.put(14,5);
		positionFactors.put(15,4);
		positionFactors.put(16,5);
		positionFactors.put(17,2);
	}
	
	private PreProductionLot previousPreProductionLot;
	
	private String currentAssemblyLineId;
	
	private String planProcessLocation;
	
	private PreProductionLot previousKnucklePreProductionLot;
	
	private int currentCount = 0;
	
	// all priority plans
	private List<HostPriorityPlan> hostPriorityPlanList =new ArrayList<HostPriorityPlan>();
	
	// all plant2 priority plans - in use only when plant 1 knuckle
	private List<HostPriorityPlan> hostPriorityPlanList2 = new ArrayList<HostPriorityPlan>();
	
	private boolean isPlant1;
	
	public PriorityPlanTask(String name) {
		super(name);
	}
	
	public void execute(Object[] args) {
		
		try{
			
			processPriorityPlan();
			
		}catch(TaskException e) {
			logger.emergency(e.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			logger.emergency(e,"Unexpected exception occured");
		}
		
	}
	
	private void processPriorityPlan() {
		
		logger.info("start to process priority plan");
		
		//	refresh component property
		refreshProperties();
	 
		// load the host priority plans
		initData();
		
		// create product ids and production lots
		createProductionLotAndProductIds();
		
		// if not plant 1 we are done
		if(!isPlant1) return;
		
		// need to resequence the knuckle pre production lots
		if(needToSort()) {
		
			// the logic to insert plant 2 lots into previous day's plant 1 lot
			// pending to be removed
			//	sortKnucklePreProductionLots();
			
			mergeNextDaySmallLot();
			int lotCount = completeProcess(hostPriorityPlanList);
			lotCount += completeProcess(hostPriorityPlanList2);
			logger.info("Finished processing " + lotCount + " host priority plans");
		}else 
			logger.info("No new host priority Plans to be processed");

		
	}

	private void initData() {

		// set the current assembly line Id
		currentAssemblyLineId = getProperty(OifTask.ASSEMBLY_LINE_ID, PropertyService.getAssemblyLineId());
		
		if(StringUtils.isEmpty(currentAssemblyLineId)) throw new TaskException("Could not find the ASSEMPLY_LINE_ID property");
		
		logger.info("Current assembly line Id is " + currentAssemblyLineId);
		
		isPlant1 = LINE_NUMBER_1.equalsIgnoreCase(currentAssemblyLineId);
		
		//	get the current plant 's priority plan including processed one
		hostPriorityPlanList.addAll(getHostPriorityPlans());
		
		if(isPlant1)hostPriorityPlanList2 = getPlant2HostPriorityPlans();

		if(!hostPriorityPlanList.isEmpty()){
			planProcessLocation = hostPriorityPlanList.get(0).getId().getPlanProcLoc();
			previousPreProductionLot = getDao(PreProductionLotDao.class).findLastPreProductionLotByProcessLocation(planProcessLocation);
		}
		if(isPlant1) previousKnucklePreProductionLot = 
			getDao(PreProductionLotDao.class).findLastPreProductionLotByProcessLocation(getKnuckleProcessLocation());
	
	}
	
	
	private void createProductionLotAndProductIds() {
		
		if(isPlant1){
			
			reverseKdLots(hostPriorityPlanList);
			reverseKdLots(hostPriorityPlanList2);
			
			createProductionLotAndProductIds(hostPriorityPlanList);
			createProductionLotAndProductIds(hostPriorityPlanList2);
		}else
			createProductionLotAndProductIds(hostPriorityPlanList);
		
		logger.info("totally " + currentCount + " products  are created successfully");
		
	}
	
	/**
	 * filtered out already processed rows
	 * @return
	 */
	private List<HostPriorityPlan> getHostPriorityPlans() {
		
		List<HostPriorityPlan> filteredPlans = new ArrayList<HostPriorityPlan>();
		List<HostPriorityPlan> plans = getDao(HostPriorityPlanDao.class).findAllByLineNumber(currentAssemblyLineId);
		
		if(!isPlant1) return plans;
		
		for(HostPriorityPlan item : plans) {
			if(!item.isRowProcessed() && !item.isRemake())
                    filteredPlans.add(item);
		}
		return filteredPlans;
	}
	
	private List<HostPriorityPlan> reverseKdLots(List<HostPriorityPlan> hostPlans) {
		
		List<HostPriorityPlan> sameDateLots = new SortedArrayList<HostPriorityPlan>("getKdLotSequence");
		List<HostPriorityPlan> allHostPlans = new ArrayList<HostPriorityPlan>();
		HostPriorityPlan currentLot = null;
		for(HostPriorityPlan item : hostPlans) {
			
			if(currentLot == null) {
				currentLot = item;
				sameDateLots.add(item);
			}else if(currentLot.isSameKdLot(item)) {
				sameDateLots.add(item);
			}else {
				Collections.reverse(sameDateLots);
				allHostPlans.addAll(sameDateLots);
				sameDateLots.clear();
				currentLot = item;
				sameDateLots.add(item);
			}
		}
		
		Collections.reverse(sameDateLots);
		allHostPlans.addAll(sameDateLots);
		
		hostPlans.clear();
		hostPlans.addAll(allHostPlans);
		
		return hostPlans;
	}
	
	private void createProductionLotAndProductIds(List<HostPriorityPlan> hostPlans) {
		
		for(HostPriorityPlan item : hostPlans) {
			
			//	if product ids had been created
			if(PRODUCT_IDS_CREATED.equals(item.getRowProcessed()) || item.isRowProcessed()) continue;
			
			if(isPlant1) {
				createKnuckleProductionPlan(item);
				//	 set product ids created
				item.setRowProcessed(PRODUCT_IDS_CREATED);
				
			}else {
				createProductionPlan(item);
				item.setRowProcessed(true);
			}
			
			// update host priority plan row processed flag to "Y" or "P"
			getDao(HostPriorityPlanDao.class).update(item);
			
			currentCount += item.getNoOfUnits();
			
			delay();
			
		}
		
		
	}
	

	
	
	private int completeProcess(List<HostPriorityPlan> hostPlans) {
		
		int lotCount = 0;
		for(HostPriorityPlan hostPriorityPlan : hostPlans) {
			// set row processed
			hostPriorityPlan.setRowProcessed(true);
			getDao(HostPriorityPlanDao.class).update(hostPriorityPlan);
			
			logger.info("updated host Priority Plan row processed : " + hostPriorityPlan.getKdLotNo());
			
			lotCount++;
		}
		return lotCount;
		
	}

	// the logic to insert plant 2 lots into previous day's plant 1 lot
	// pending to be removed
	
	private void sortKnucklePreProductionLots() {
		
		// move second day split lot together with previous day lot 
		if(!hostPriorityPlanList.isEmpty()) {
			HostPriorityPlan firstPlan = hostPriorityPlanList.get(0);
			insertSmallLot(firstPlan.getKdLotNo());
		}
		
		if(!hostPriorityPlanList.isEmpty()) {
			HostPriorityPlan firstPlan2 = hostPriorityPlanList2.get(0);
			if(insertSmallLot(firstPlan2.getKdLotNo())) {
				hostPriorityPlanList2.remove(firstPlan2) ;
				firstPlan2.setRowProcessed(true);
				getDao(HostPriorityPlanDao.class).update(firstPlan2);
				
				logger.info("updated host Priority Plan row processed : " + firstPlan2.getKdLotNo());
				
			}
		}
		
		Date date = hostPriorityPlanList2.get(0).getId().getAfaeOffDate();
		List<PreProductionLot> preProductionLots = findAllKnucklePreProductionLots(date);
		
		List<PreProductionLot> p1Lots = mergeNextDatePlant2Lots()? findP1KnucklePreProductionLots(preProductionLots, date):
			findSameDateKnucklePreProductionLots(preProductionLots, date);
		
		List<PreProductionLot> p2Lots = findP2PreProductionLots(hostPriorityPlanList2, preProductionLots);
		
		PreProductionLot firstLot = getDao(PreProductionLotDao.class).findParent(p1Lots.get(0).getProductionLot());
		
		List<PreProductionLot> mergedLots = mergePlant2KnuckleLots(p1Lots, p2Lots);
		if(firstLot != null) mergedLots.add(0,firstLot);
		updateNextPreproductionLots(mergedLots);
			
	}
	
	private void mergeNextDaySmallLot() {
		
		// move second day split lot together with previous day lot 
		if(!hostPriorityPlanList.isEmpty()) {
			HostPriorityPlan firstPlan = hostPriorityPlanList.get(0);
			insertSmallLot(firstPlan.getKdLotNo());
		}
		
		if(!hostPriorityPlanList2.isEmpty()) {
			HostPriorityPlan firstPlan2 = hostPriorityPlanList2.get(0);
			insertSmallLot(firstPlan2.getKdLotNo());
		}
	}
	
	// the logic to insert plant 2 lots into previous day's plant 1 lot
	// pending to be removed
	private void insertPlant2KnuckleLots() {
		
		Date date = hostPriorityPlanList2.get(0).getId().getAfaeOffDate();
		List<PreProductionLot> preProductionLots = findAllKnucklePreProductionLots(date);
		
		List<PreProductionLot> p1Lots = mergeNextDatePlant2Lots()? findP1KnucklePreProductionLots(preProductionLots, date):
			findSameDateKnucklePreProductionLots(preProductionLots, date);
		
		List<PreProductionLot> p2Lots = findP2PreProductionLots(hostPriorityPlanList2, preProductionLots);
		
		PreProductionLot firstLot = getDao(PreProductionLotDao.class).findParent(p1Lots.get(0).getProductionLot());
		
		List<PreProductionLot> mergedLots = mergePlant2KnuckleLots(p1Lots, p2Lots);
		if(firstLot != null) mergedLots.add(0,firstLot);
		updateNextPreproductionLots(mergedLots);

	}
	
	private PreProductionLot findPreviousLot(PreProductionLot lot) {
		
		PreProductionLot currentLot = lot;
		
		do{
			currentLot = getDao(PreProductionLotDao.class).findParent(currentLot.getProductionLot());
			if(currentLot == null || !currentLot.isSameKdLot(lot)) return currentLot;
		}while(true);
	}
	
	private boolean insertSmallLot(String kdLot) {
		
		List<PreProductionLot> items = getDao(PreProductionLotDao.class).findAllWithSameKdLot(kdLot);
		
		if(items.isEmpty()) return false;
		
		List<PreProductionLot> orderedLots = new SortedArrayList<PreProductionLot>(items,"getKdLotSequence");
		
		PreProductionLot firstLot = findPreviousLot(orderedLots.get(0));
		
		Collections.reverse(orderedLots);
		
		
		return updateAllNextProductionLots(firstLot,orderedLots);
		
	}
	
	private boolean updateAllNextProductionLots(PreProductionLot firstLot,List<PreProductionLot> orderedLots) {
		
		PreProductionLot beforeLot = firstLot;
		boolean flag = false;
		
		for(PreProductionLot item : orderedLots) {
			flag  |=insertPreProductionLot(beforeLot,item);
			beforeLot = item;
		}

		return flag;
	}
	
	
	private boolean insertPreProductionLot(PreProductionLot beforeLot,PreProductionLot afterLot) {
		
		List<PreProductionLot> changedLots = new ArrayList<PreProductionLot>();
		PreProductionLot item1 = getDao(PreProductionLotDao.class).findParent(afterLot.getProductionLot());
		
		if(beforeLot != null && item1 != null && item1.getProductionLot().equals(beforeLot.getProductionLot())) return false;
		if(item1 != null) {
			item1.setNextProductionLot(afterLot.getNextProductionLot());
			changedLots.add(item1);
		}
	
		if(beforeLot != null){
			afterLot.setNextProductionLot(beforeLot.getNextProductionLot());
			changedLots.add(afterLot);
			beforeLot.setNextProductionLot(afterLot.getProductionLot());
			changedLots.add(beforeLot);
		}
		if(!changedLots.isEmpty())getDao(PreProductionLotDao.class).updateAllNextProductionLots(changedLots);
		return !changedLots.isEmpty();
		
	}
	
	
	private void updateNextPreproductionLots(List<PreProductionLot> prodLots) {
		
		List<PreProductionLot> changedLots = new ArrayList<PreProductionLot>();
		for(int i = 0; i<prodLots.size();i++) {
			
			PreProductionLot item = prodLots.get(i);
			
			PreProductionLot nextItem = i + 1 < prodLots.size() ? prodLots.get(i+1) : null;
			
			String nextProdLot = nextItem == null ? null : nextItem.getProductionLot();
			
			if(!StringUtils.equals(item.getNextProductionLot(),nextProdLot)) {
				logger.info("Pre production lot " + item.getProductionLot() + "'s next production lot is changed from " 
						+ item.getNextProductionLot() + " to " + nextProdLot);
				item.setNextProductionLot(nextProdLot);
				changedLots.add(item);
			}
		}
		
		//	update all
		getDao(PreProductionLotDao.class).updateAllNextProductionLots(changedLots);
		
	}
	
	
	private List<PreProductionLot> findP2PreProductionLots(List<HostPriorityPlan> p2HostPlans, List<PreProductionLot> allLots) {
		
		List<PreProductionLot> p2Lots = new ArrayList<PreProductionLot>();
		for(HostPriorityPlan item : p2HostPlans) {
			
			PreProductionLot prodLot = findLot(allLots, item.deriveKnucklePreProductionLot().getProductionLot());
			if(prodLot != null)	p2Lots.add(prodLot);
		}
		return p2Lots;
	}
	
	private PreProductionLot findLot(List<PreProductionLot> allPlans, String productionLot) {
		for(PreProductionLot item : allPlans) {
			if(item.getProductionLot().equals(productionLot)) return item;
		}
		return null;
	}
	
	
	private boolean needToSort() {
		
		for(HostPriorityPlan item : hostPriorityPlanList2) {
			if(!item.isRowProcessed()) return true;
		}
		
		for(HostPriorityPlan item : hostPriorityPlanList) {
			if(!item.isRowProcessed()) return true;
		}
		
		return false;
	}
	
	private List<PreProductionLot> findAllKnucklePreProductionLots(Date productionDate) {
		
		List<PreProductionLot> items = new ArrayList<PreProductionLot>();
		Date previousDate = null;
		PreProductionLot currentLot = getDao(PreProductionLotDao.class).findLastPreProductionLotByProcessLocation(getKnuckleProcessLocation());
		if(currentLot == null) return items;
		items.add(currentLot);
		do {
			PreProductionLot item = getDao(PreProductionLotDao.class).findParent(currentLot.getProductionLot());
			if(item == null) return items;
			
			if(item.deriveProductionDate().before(productionDate)) {
				
				if(previousDate == null) previousDate = item.deriveProductionDate();
				else if(item.deriveProductionDate().before(previousDate)) return items;
			}
			items.add(0,item);
			currentLot = item;

			
		}while(true);
	}
	
	private List<PreProductionLot> findP1KnucklePreProductionLots(List<PreProductionLot> preProductionLots,Date previousDate) {
		
		List<PreProductionLot> p1Lots = new ArrayList<PreProductionLot>();
		for(PreProductionLot item : preProductionLots) {
			
			if(LINE_NUMBER_1.equals(item.deriveLineNumber())) p1Lots.add(item);
		}
		
		return p1Lots;
	}
	
	private List<PreProductionLot> findSameDateKnucklePreProductionLots(List<PreProductionLot> preProductionLots,Date previousDate) {
		
		List<PreProductionLot> p1Lots = new ArrayList<PreProductionLot>();
		for(PreProductionLot item : preProductionLots) {
			
			if(LINE_NUMBER_1.equals(item.deriveLineNumber()) && item.deriveProductionDate().equals(previousDate) || item.deriveProductionDate().after(previousDate) ) p1Lots.add(item);
		}
		
		return p1Lots;
	}
	
	private void delay() {
		int delay= getPropertyInt(LOT_CREATION_DELAY, 1005);
		if(delay < 100) delay = 1005;
		
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
		}
	}
	
	private List<PreProductionLot> mergePlant2KnuckleLots(List<PreProductionLot> p1Lots, List<PreProductionLot> p2Lots) {
		
		List<PreProductionLot> mergedLots = new ArrayList<PreProductionLot>();
		
		
		do{
			List<PreProductionLot> p1SameDateLots = getNextSameDateProductionLots(p1Lots);
			List<PreProductionLot> p2SameDateLots = getNextSameDateProductionLots(p2Lots);
			
			if(p1SameDateLots.isEmpty() && p2SameDateLots.isEmpty()) break;
			
			if(mergeNextDatePlant2Lots()) {
				do{
					if(!p1SameDateLots.isEmpty() && !p2SameDateLots.isEmpty()) {
					
						Date p1Date = p1SameDateLots.get(0).deriveProductionDate();
						Date p2Date = p2SameDateLots.get(0).deriveProductionDate();
						
						if(!p1Date.before(p2Date)) {
							
							mergedLots.addAll(p2SameDateLots);
							p2Lots.removeAll(p2SameDateLots);
							p2SameDateLots = getNextSameDateProductionLots(p2Lots);
						}else break;
					}else break;
				}while (true);
			}
			
			do{
				List<PreProductionLot> p1Group = findNextGroup(p1SameDateLots,getPlant1Count());
				List<PreProductionLot> p2Group = findNextGroup(p2SameDateLots,getPlant2Count());
				if(p1Group.isEmpty() && p2Group.isEmpty()) break;
				if(p1Group.isEmpty()) mergedLots.addAll(p2Group);
				else if(p2Group.isEmpty()) mergedLots.addAll(p1Group);
				else {
					if(isPlant1First()) {
						mergedLots.addAll(p1Group);
						mergedLots.addAll(p2Group);
					}else {
						mergedLots.addAll(p2Group);
						mergedLots.addAll(p1Group);
					}
				}
			}while(true);
		}while(true);
		
		return mergedLots;
	}
	
	private List<PreProductionLot> getNextSameDateProductionLots(List<PreProductionLot> prodLots) {
		
		List<PreProductionLot> sameDateLots = new ArrayList<PreProductionLot>();
		if(prodLots.isEmpty()) return sameDateLots;
		String dateString= prodLots.get(0).deriveProductDateString();
		PreProductionLot currentLot = null;
		for(PreProductionLot item : prodLots) {
			
			if(currentLot == null || currentLot.isSameKdLot(item)) {
				currentLot = item;
				sameDateLots.add(item);
			}else{
				if(item.deriveProductDateString().equals(dateString)){
					currentLot = item;
					sameDateLots.add(item);
				}else break;
			}
		}
		
		prodLots.removeAll(sameDateLots);
		
		return sameDateLots;
	}
	
	private List<PreProductionLot> findNextGroup(List<PreProductionLot> preProductionLots, int total) {
		
		List<PreProductionLot> nextGroup = new ArrayList<PreProductionLot>();
		
		if(preProductionLots.isEmpty()) return nextGroup;
		
		int count = 0;
		PreProductionLot currentLot = null;
		for(PreProductionLot item : preProductionLots) {
			
			if(currentLot == null || item.isSameKdLot(currentLot)){
				currentLot = item;
				nextGroup.add(item);
				count += item.getLotSize();
				continue;
			}else{
				if(count >= total) break;
				else if(item.deriveProductDateString().equals(currentLot.deriveProductDateString())){
					currentLot = item;
					nextGroup.add(item);
					count += item.getLotSize();
					continue;
				}
			}

		}
		
		preProductionLots.removeAll(nextGroup);
		
		return nextGroup;
			
	}
	
	
	private List<HostPriorityPlan> getPlant2HostPriorityPlans() {
		
		// find all plant 2 prority plan 
		List<HostPriorityPlan> hostPriorityPlans = getDao(HostPriorityPlanDao.class).findAllByLineNumber(LINE_NUMBER_2);
		
		
		List<String> modelCodes = getPlant2ModelCodes();
		
		List<HostPriorityPlan> filteredPlans = new ArrayList<HostPriorityPlan>();
		
		// filter out non civic lots and processed lots
		for(HostPriorityPlan item : hostPriorityPlans) {
			
			if(!item.isRowProcessed() && !item.isRemake()&& modelCodes.contains(item.getModelCode()))
            {    
                    filteredPlans.add(item);
            }
		}
		return filteredPlans;
	}
	
	
	// create production lot and product ids based on the hostPriorityPlan data
	private void createProductionPlan(HostPriorityPlan hostPriorityPlan) {
		
		ProductionLot prodLot = hostPriorityPlan.deriveProductionLot();
		
		// save prodLot
		getDao(ProductionLotDao.class).save(prodLot);
		
		logger.info("Production lot " + prodLot.getProductionLot() + " is created");
		
		createProductIds(hostPriorityPlan);
		
		PreProductionLot preProductionLot = hostPriorityPlan.derivePreProductionLot();
		
		// create a new pre production lot 
		getDao(PreProductionLotDao.class).save(preProductionLot);
		
		logger.info("Pre-production Lot " + preProductionLot.getProductionLot() + " is created");
		
	
		if(previousPreProductionLot != null ) {
			previousPreProductionLot.setNextProductionLot(preProductionLot.getProductionLot());
			getDao(PreProductionLotDao.class).update(previousPreProductionLot);
			
			logger.info("updated pre-production lot " + previousPreProductionLot.getProductionLot() 
					+ "'s next production lot to " + previousPreProductionLot.getNextProductionLot());

		}
		
		previousPreProductionLot = preProductionLot;
		
	}
	
	private void createProductIds(HostPriorityPlan hostPriorityPlan) {
		
		if(LINE_NUMBER_3.equalsIgnoreCase(currentAssemblyLineId)) 
			
			createEngines(hostPriorityPlan);
		
		else createVehicles(hostPriorityPlan);
		
	}

	private void createKnuckleProductionPlan(HostPriorityPlan hostPriorityPlan) {
		
		String startSerialNumber = createKnuckles(hostPriorityPlan);
				
		//	save knuckle product lot
		ProductionLot knuckleProdLot = hostPriorityPlan.deriveKnuckleProductionLot();
		knuckleProdLot.setProcessLocation(getKnuckleProcessLocation());
		knuckleProdLot.setStartProductId(startSerialNumber);
		getDao(ProductionLotDao.class).save(knuckleProdLot);
		
		logger.info("Knuckle product lot " + knuckleProdLot.getProductionLot() + " is created");
		
		
		PreProductionLot knucklePreProductionLot = hostPriorityPlan.deriveKnucklePreProductionLot();
		
		knucklePreProductionLot.setProcessLocation(getKnuckleProcessLocation());
		knucklePreProductionLot.setLineNo(LINE_NUMBER_1);
		knucklePreProductionLot.setStartProductId(startSerialNumber);
		
//			 create a new pre production lot 
		getDao(PreProductionLotDao.class).save(knucklePreProductionLot);
		
		logger.info("Knuckle pre-production Lot " + knucklePreProductionLot.getProductionLot() + " is created");
		
		updateNextKnucklePreProductionLot(knucklePreProductionLot);
	}
	
	private void updateNextKnucklePreProductionLot(PreProductionLot preProductionLot) {
		
		if(previousKnucklePreProductionLot != null ) {
			previousKnucklePreProductionLot.setNextProductionLot(preProductionLot.getProductionLot());
			getDao(PreProductionLotDao.class).update(previousKnucklePreProductionLot);
			
			logger.info("updated knuckle pre-production lot " + previousKnucklePreProductionLot.getProductionLot() 
					+ "'s next production lot to " + previousKnucklePreProductionLot.getNextProductionLot());
		}
		
		previousKnucklePreProductionLot = preProductionLot;
	}
	
 	
	private void createVehicles(HostPriorityPlan hostPriorityPlan) {
		
		List<Frame> vehicles = new ArrayList<Frame>();
		int lotSize = hostPriorityPlan.getNoOfUnits();
		String vinType = hostPriorityPlan.getStartVinNumber().substring(0,11);
		int serialNumber = Integer.parseInt(hostPriorityPlan.getStartVinNumber().substring(11));
		
		logger.info("Creating " + lotSize + " VINs. Starting from " + hostPriorityPlan.getStartVinNumber());

		for(int i = 0; i < lotSize; i++) {
			String productId = vinType + StringUtils.leftPad(Integer.toString(serialNumber), 6, "0");
			char checkDigit = calculateVinCheckDigit(productId);
			String vin = productId.substring(0,8) + checkDigit + productId.substring(9);
			Frame frame = createFrame(hostPriorityPlan, vin);
			vehicles.add(frame);
			serialNumber++;
		}
		
		getDao(FrameDao.class).saveAll(vehicles);
		
		logger.info("Totally " + lotSize + " VINs are created");
		
	}
	
	private Frame createFrame(HostPriorityPlan hostPriorityPlan, String productId) {
		
		 Frame frame = new Frame();
		 frame.setProductId(productId);
		 frame.setProductionLot(hostPriorityPlan.getId().deriveProductionLot());
		 frame.setKdLotNumber(hostPriorityPlan.getKdLotNo());
		 frame.setPlanOffDate(hostPriorityPlan.getId().getAfaeOffDate());
		 frame.setProductionDate(hostPriorityPlan.getId().getAfaeOffDate());
		 frame.setLastPassingProcessPointId(getProperty(MS_OFF_PPID));
		 frame.setTrackingStatus(getProperty(MS_OFF_LINE_ID));
		
		 return frame;
		 
	}
	
	/**
	 * calculate the check digit. Algorithm :
	 * Every char in the vin string is mapped to a number (letterValue) based on the value of the character(letterValues map). 
	 * Its position in the vin is also mapped to another number(positionFactor (positionFactors, start from 1).
	 * multiply letterValue * positionFactor, sum them over each char for the vin.
	 * The result is moduled by 11. The remaider is the check dit. If the remaider = 10, use 'X' as check digit. 
	 * 
	 * @param vin
	 * @return
	 */
	
	private char calculateVinCheckDigit(String vin) {
		
		int count = 0;
		for(int i = 0;i<vin.length();i++) {
			char digit =  vin.charAt(i);
			if(!letterValues.containsKey(digit)) {
				if(digit != '*')
					throw new TaskException("Illegal character " + digit + " in vin : " + vin);
				
			}else {
				int number = letterValues.get(digit);
				int positionFactor  = positionFactors.get(i+1);
				count += number * positionFactor;
			}
		}
		
		int remainder = count % 11;
		if(remainder == 10) return 'X';
		else return Character.forDigit(remainder,10);
	}

	private void createEngines(HostPriorityPlan hostPriorityPlan) {
		
		List<Engine> engines = new ArrayList<Engine>();
		int lotSize = hostPriorityPlan.getNoOfUnits();
		String engineType = hostPriorityPlan.getStartVinNumber().substring(0,5);
		int serialNumber = Integer.parseInt(hostPriorityPlan.getStartVinNumber().substring(5));

		logger.info("Creating " + lotSize + " EINs. Starting from " + hostPriorityPlan.getStartVinNumber());

		for(int i = 0; i < lotSize; i++) {
			String productId = engineType + StringUtils.leftPad(Integer.toString(serialNumber + i), 7, "0");
			Engine engine = createEngine(hostPriorityPlan, productId);
			engines.add(engine);
		}
		
		getDao(EngineDao.class).saveAll(engines);
		
		getDao(ProductResultDao.class).saveAll(createEngineHistories(engines));
		
		createEngineInprocessProducts(engines);
		
		logger.info("Totally " + lotSize + "EINs are created");
		
	}

	
	private void createEngineInprocessProducts(List<Engine> engines) {
		List<InProcessProduct> inProducts = new ArrayList<InProcessProduct>();
		InProcessProductDao inProcessProductDao = getDao(InProcessProductDao.class);
		
		InProcessProduct lastProduct =  inProcessProductDao.findLastForLine(getProperty(MS_OFF_LINE_ID));
		InProcessProduct tempProduct =  lastProduct;
		
		for(Engine engine : engines){
			if(tempProduct != null ) tempProduct.setNextProductId(engine.getProductId());
			InProcessProduct inProduct = new InProcessProduct(engine); 
			inProduct.setLastPassingProcessPointId(getProperty(MS_OFF_PPID));
			inProduct.setLineId(getProperty(MS_OFF_LINE_ID));
			
			inProducts.add(inProduct);
			tempProduct = inProduct;
		}
		
		if(lastProduct != null) inProducts.add(0,lastProduct);
		
		inProcessProductDao.saveAll(inProducts);
	}

	private List<ProductResult> createEngineHistories(List<Engine> engines) {
		List<ProductResult> histories = new ArrayList<ProductResult>();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		for(Engine engine : engines){
			ProductResult history = new ProductResult(engine.getProductId(), getProperty(MS_OFF_PPID));
			history.setProductSpecCode(engine.getProductSpecCode());
			history.setProductionLot(engine.getProductionLot());
			history.setProductionDate(engine.getProductionDate());
			history.setActualTimestamp(timestamp);
			history.setProcessCount(1);
			
			
			histories.add(history);
		}
		return histories;
	}

	private Engine createEngine(HostPriorityPlan hostPriorityPlan, String productId) {
		
		 Engine engine = new Engine();
		 engine.setProductId(productId);
		 engine.setProductionLot(hostPriorityPlan.getId().deriveProductionLot());
		 engine.setKdLotNumber(hostPriorityPlan.getKdLotNo());
		 engine.setPlanOffDate(hostPriorityPlan.getId().getAfaeOffDate());
		 engine.setProductionDate(hostPriorityPlan.getId().getAfaeOffDate());
		 engine.setProductSpecCode(hostPriorityPlan.getProductSpecCode());
		 engine.setLastPassingProcessPointId(getProperty(MS_OFF_PPID));
		 engine.setTrackingStatus(getProperty(MS_OFF_LINE_ID));
		 
		
		 return engine;
		 
	}

	
	private int getPlant1Count() {
		return getPropertyInt(PLANT1_COUNT);
	}
	
	private int getPlant2Count() {
		return getPropertyInt(PLANT2_COUNT);
	}
	
	private boolean isPlant1First() {
		return getPropertyBoolean(PLANT1_FIRST, true);
	}
	
	private String createKnuckles(HostPriorityPlan hostPriorityPlan) {
		
		List<SubProduct> subProducts = new ArrayList<SubProduct>();
		int lotSize = hostPriorityPlan.getNoOfUnits();
		
		String leftPrefix = getKnuckleBuildAttribute(hostPriorityPlan.getProductSpecCode(), BuildAttributeTag.KNUCKLE_LEFT_SIDE);
		String rightPrefix = getKnuckleBuildAttribute(hostPriorityPlan.getProductSpecCode(), BuildAttributeTag.KNUCKLE_RIGHT_SIDE);
		
		logger.info("Retrieved left side part number : " + leftPrefix + " for product spec code : " + hostPriorityPlan.getProductSpecCode());
		logger.info("Retrieved right side part number : " + rightPrefix + " for product spec code : " + hostPriorityPlan.getProductSpecCode());
		
		String modelYear = hostPriorityPlan.getModelYearCode();
		
		logger.info("Creating " + lotSize *2 + " Knuckle serial numbers");
		
		int leftSerialNumber = findNextSerialNumber(hostPriorityPlan.getId().deriveKnuckleProductionLot(),leftPrefix + modelYear);
		int rightSerialNumber = findNextSerialNumber(hostPriorityPlan.getId().deriveKnuckleProductionLot(),rightPrefix +  modelYear);
		
		//		set left and right start serial number
		String startNumber = "L"+StringUtils.leftPad(""+leftSerialNumber,KSN_DIGITS,"0")+"*R"+StringUtils.leftPad(""+rightSerialNumber,KSN_DIGITS,"0");
		
		logger.info("left knuckle serial numbers start from " + leftSerialNumber);
		logger.info("right knuckle serial numbers start from " + rightSerialNumber);
		
		
		for(int i = 0; i < lotSize; i++) {
			
			String leftProductId = leftPrefix + modelYear + StringUtils.leftPad(Integer.toString(leftSerialNumber), KSN_DIGITS, "0"); 
			SubProduct leftKnuckle = createKnuckle(hostPriorityPlan, leftProductId,Product.SUB_ID_LEFT);
			subProducts.add(leftKnuckle);
			
			leftSerialNumber++;
			
			String rightProductId = rightPrefix + modelYear + StringUtils.leftPad(Integer.toString(rightSerialNumber), KSN_DIGITS, "0"); 
			SubProduct rightKnuckle = createKnuckle(hostPriorityPlan, rightProductId,Product.SUB_ID_RIGHT);
			subProducts.add(rightKnuckle);
			
			rightSerialNumber ++;
		}
		
		getDao(SubProductDao.class).saveAll(subProducts);
		
		logger.info("" + lotSize*2 + " left and right knuckle serial numbers are created for host priority plan " + hostPriorityPlan.getKdLotNo());
		
		return startNumber;
		
	} 
	
	private SubProduct createKnuckle(HostPriorityPlan hostPriorityPlan, String productId,String subId) {
		
		SubProduct subProduct = new Knuckle();
		subProduct.setProductId(productId);
		subProduct.setProductionLot(hostPriorityPlan.getId().deriveKnuckleProductionLot());
		subProduct.setKdLotNumber(hostPriorityPlan.getKdLotNo());
		subProduct.setSubId(subId);
		subProduct.setProductSpecCode(hostPriorityPlan.getProductSpecCode());
		subProduct.setPlanOffDate(hostPriorityPlan.getId().getAfaeOffDate());
		subProduct.setProductionDate(hostPriorityPlan.getId().getAfaeOffDate());
		subProduct.setLastPassingProcessPointId(getProperty(MS_FK_OFF_PPID));
		subProduct.setTrackingStatus(getProperty(MS_FK_OFF_LINE_ID));
		
		 return subProduct;
		 
	}
	
	private String getKnuckleBuildAttribute(String productSpecCode, String side) {
		BuildAttributeCache bc = new BuildAttributeCache();
		String bFromCache = bc.findAttributeValue(productSpecCode, side);
		logger.info("Build Attribute: ", side, " Product Spec Code:", productSpecCode, " Build Attribute Value:", bFromCache);
		
		if(StringUtils.isEmpty(bFromCache)) {
			throw new TaskException("Could not find build attribute for product spec code : " + productSpecCode + " and attribute : " + side);
		}
		
		return bFromCache;
		
	}
	
	/**
	 * find next available serial number for the current partNumberPrefix
	 * first check if the product_ids of the production lot "productionLot" has been created. If it is created 
	 * get the min product_id of this lot so that we can recreate the product ids under this lot and update the items in the table
	 * If no current production lot in the table, just find the max production ids with the "partNumberPrefix"
	 * @param productionLot
	 * @param partNumberPrefix
	 * @return
	 */
	private int findNextSerialNumber(String productionLot,String partNumberPrefix) {
		String productId = null;
		productId = getDao(SubProductDao.class).findMinProductId(productionLot, partNumberPrefix);
		if(productId != null) return Integer.parseInt(productId.substring(partNumberPrefix.length()));
		else {
			productId = getDao(SubProductDao.class).findMaxProductId(partNumberPrefix);
			if(productId == null) return 1;
			else return Integer.parseInt(StringUtils.trim(productId.substring(partNumberPrefix.length()))) + 1;
		}
	}
	
	private List<String> getPlant2ModelCodes() {
		
		return Arrays.asList( getPropertyArray(PLANT2_MODEL_CODES));
		
	}

	private boolean mergeNextDatePlant2Lots() {
		return getPropertyBoolean(MERGE_NEXT_DAY_PLANT2_LOTS, true);
	}
	
	private String getKnuckleProcessLocation(){
		return getProperty(KNUCKLE_PROCESS_LOCATION,PreProductionLot.PROCESS_LOCATION_KNUCKLE_KR);
	}
	

}
