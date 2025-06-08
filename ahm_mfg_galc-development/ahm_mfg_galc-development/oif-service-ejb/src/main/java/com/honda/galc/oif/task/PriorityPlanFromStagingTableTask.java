 package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.PropertyException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.HostPriorityPlanDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.HostPriorityPlan;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.oif.task.OifAbstractTask;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;

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
public class PriorityPlanFromStagingTableTask extends OifAbstractTask implements IEventTaskExecutable{
	
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
	
	
	private int currentCount = 0;
	
	// all priority plans
	private List<HostPriorityPlan> hostPriorityPlanList =new ArrayList<HostPriorityPlan>();
	
	
	public PriorityPlanFromStagingTableTask(String name) {
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
		
		logger.info("start to process priority plan : " + getName());
		
		//	check component property
		checkProperties();
	 
		previousPreProductionLot = getDao(PreProductionLotDao.class)
			.findLastPreProductionLotByProcessLocation(getDestProcessLocation());

		// load the host priority plans
		loadHostPriorityPlans();
		
		if(!hostPriorityPlanList.isEmpty())
			// create product ids and production lots
			createProductionPlans(hostPriorityPlanList);
		else 
			logger.info("No new host priority Plans to be processed");
		
	}

	protected void loadHostPriorityPlans() {
		hostPriorityPlanList = getDao(HostPriorityPlanDao.class).findAllNewPlans(getPlanProcessLocation());
	}
	
	
	protected void createProductionPlans(List<HostPriorityPlan> hostPlans) {
		
		int lotCount = 0;
		for(HostPriorityPlan item : hostPlans) {
			
			createProductionPlan(item);
			item.setRowProcessed(true);
			
			// update host priority plan row processed flag to "Y" or "P"
			getDao(HostPriorityPlanDao.class).update(item);
			
			currentCount += item.getNoOfUnits();

			lotCount++;
			if(lotCount >= getLotBatchSize()) break;
		}
		logger.info("totally " + lotCount + " production lots  are created successfully");
		logger.info("totally " + currentCount + " products  are created successfully");
	}
	
	
	// create production lot and product ids based on the hostPriorityPlan data
	protected void createProductionPlan(HostPriorityPlan hostPriorityPlan) {
		
		// ignore remake production lot
		if(ProductType.KNUCKLE.toString().equalsIgnoreCase(getProductType()) && hostPriorityPlan.isRemake()) return;
		
		ProductionLot prodLot = hostPriorityPlan.deriveProductionLot(getDestProcessLocation());
		
		// save prodLot
		getDao(ProductionLotDao.class).save(prodLot);
		
		logger.info("Production lot " + prodLot.getProductionLot() + " is created");
		
		createProductIds(prodLot);
		
		PreProductionLot preProductionLot = hostPriorityPlan.derivePreProductionLot(getDestProcessLocation());
		
		// create a new pre production lot 
		getDao(PreProductionLotDao.class).appendPreProductionLot(previousPreProductionLot, preProductionLot);
		
		logger.info("Pre-production Lot " + preProductionLot.getProductionLot() + " is created");
				
		if(previousPreProductionLot != null)
			logger.info("updated pre-production lot " + previousPreProductionLot.getProductionLot() 
					+ "'s next production lot to " + previousPreProductionLot.getNextProductionLot());
		
		previousPreProductionLot = preProductionLot;
		
	}
	
	protected void createProductIds(ProductionLot productionLot) {
		
		if(ProductType.ENGINE.toString().equalsIgnoreCase(getProductType()))
			createEngines(productionLot);
		else if(ProductType.FRAME.toString().equalsIgnoreCase(getProductType()))
			createVehicles(productionLot);
		else createSubProducts(productionLot);
		
	}
	
	protected void createSubProducts(ProductionLot productionLot){
		
	}

 	
	protected void createVehicles(ProductionLot productionLot) {
		
		List<Frame> vehicles = new ArrayList<Frame>();
		int lotSize = productionLot.getLotSize();
		
		logger.info("Creating " + lotSize + " VINs. Starting from " + productionLot.getStartProductId());

		for(int i = 0; i < lotSize; i++) {
			Frame frame = createFrame(productionLot, createVIN(productionLot.getStartProductId(),i));
			vehicles.add(frame);
		}
		
		getDao(FrameDao.class).saveAll(vehicles);
		
		logger.info("Totally " + lotSize + " VINs are created");
		
	}
	
	protected String createVIN(String startProductId, int seq) {
		String vinType = startProductId.substring(0,11);
		int serialNumber = Integer.parseInt(startProductId.substring(11));
		String productId = vinType + StringUtils.leftPad(Integer.toString(serialNumber + seq), 6, "0");
		char checkDigit = calculateVinCheckDigit(productId);
		return  productId.substring(0,8) + checkDigit + productId.substring(9);
	}
	
	protected Frame createFrame(ProductionLot productionLot, String productId) {
		
		 Frame frame = new Frame();
		 frame.setProductId(productId);
		 frame.setProductionLot(productionLot.getProductionLot());
		 frame.setKdLotNumber(productionLot.getKdLotNumber());
		 frame.setPlanOffDate(productionLot.getPlanOffDate());
		 frame.setProductionDate(productionLot.getProductionDate());
		 frame.setLastPassingProcessPointId(getMSOffLineID());
		 frame.setTrackingStatus(getProperty(getMSOffProcessPointID()));
		
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
	
	protected char calculateVinCheckDigit(String vin) {
		
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

	protected void createEngines(ProductionLot productionLot) {
		
		List<Engine> engines = new ArrayList<Engine>();
		int lotSize = productionLot.getLotSize();

		logger.info("Creating " + lotSize + " EINs. Starting from " + productionLot.getStartProductId());

		for(int i = 0; i < lotSize; i++) {
			Engine engine = createEngine(productionLot, createEIN(productionLot.getStartProductId(),i));
			engines.add(engine);
		}
		
		getDao(EngineDao.class).saveAll(engines);
		
		logger.info("Totally " + lotSize + "EINs are created");
		
	}
	
	protected String createEIN(String startProductId, int seq){
		String engineType = startProductId.substring(0,4);
		int serialNumber = Integer.parseInt(startProductId.substring(5));
		return engineType + StringUtils.leftPad(Integer.toString(serialNumber + seq), 7, "0");
	}
	
	protected Engine createEngine(ProductionLot productionLot, String productId) {
		
		 Engine engine = new Engine();
		 engine.setProductId(productId);
		 engine.setProductionLot(productionLot.getProductionLot());
		 engine.setKdLotNumber(productionLot.getKdLotNumber());
		 engine.setPlanOffDate(productionLot.getPlanOffDate());
		 engine.setProductionDate(productionLot.getProductionDate());
		 engine.setLastPassingProcessPointId(getMSOffLineID());
		 engine.setTrackingStatus(getProperty(getMSOffProcessPointID()));
		
		 return engine;
		 
	}
	
	private void checkProperties() {
		refreshProperties();
		
		if(getPlanProcessLocation() == null) 
			throw new PropertyException("PLAN_PROCESS_LOCATION is not configured");
		if(getProductType() == null) 
			throw new PropertyException("PRODUCT_TYPE is not configured");
		
	}

	private String getPlanProcessLocation(){
		return getProperty("PLAN_PROCESS_LOCATION");
	}
	
	private String getDestProcessLocation(){
		return getProperty("DEST_PROCESS_LOCATION",getPlanProcessLocation());
	}
	
	private int getLotBatchSize(){
		return getPropertyInt("LOT_BATCH_SIZE", 1);
	}
	
	private String getProductType(){
		return getProperty("PRODUCT_TYPE");
	}
	
	private String getMSOffLineID(){
		return getProperty("MS_OFF_LINE_ID");
	}
	
	private String getMSOffProcessPointID(){
		return getProperty("MS_OFF_PROCESS_POINT_ID");
	}
	

}
