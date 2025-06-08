package com.honda.galc.client.engine.shipping;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.mvc.AbstractModel;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.EngineFiringResultDao;
import com.honda.galc.dao.product.EngineManifestDao;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.dao.product.ShippingQuorumDao;
import com.honda.galc.dao.product.ShippingQuorumDetailDao;
import com.honda.galc.dao.product.ShippingTrailerDao;
import com.honda.galc.dao.product.ShippingTrailerInfoDao;
import com.honda.galc.dao.product.ShippingVanningScheduleDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.ShippingQuorumDetailStatus;
import com.honda.galc.entity.enumtype.ShippingQuorumStatus;
import com.honda.galc.entity.enumtype.ShippingTrailerInfoStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.EngineFiringResult;
import com.honda.galc.entity.product.EngineManifest;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ShippingQuorum;
import com.honda.galc.entity.product.ShippingQuorumDetail;
import com.honda.galc.entity.product.ShippingQuorumId;
import com.honda.galc.entity.product.ShippingTrailer;
import com.honda.galc.entity.product.ShippingTrailerInfo;
import com.honda.galc.entity.product.ShippingVanningSchedule;
import com.honda.galc.property.EngineShippingPropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.EngineShippingHelper;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.SortedArrayList;
import com.honda.galc.util.StringUtil;

/**
 * 
 * 
 * <h3>MCShippingModel Class description</h3>
 * <p> MCShippingModel description </p>
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
 * Sep 10, 2014
 *
 *
 */
public class EngineShippingModel extends AbstractModel{
	
	private static int QUORUM_ID_DELTA = 50000;
	public static String DEFAULT_PALLET_TYPE ="NP4";
	
	private EngineShippingHelper engineShippingHelper;
	
	
	private List<ShippingTrailerInfo> shippingTrailerInfoList;
	
	private List<ShippingQuorum> allQuorums = new ArrayList<ShippingQuorum>();
	
	public void reloadShippingTrailerInfoList() {
		shippingTrailerInfoList = findAllShippingTrailers();
	}
	
	private List<ShippingQuorum> findAllActiveQuorums() {
		allQuorums = getDao(ShippingQuorumDao.class).findAllActiveQuorums();
		List<ShippingQuorum> scheduledQuorums = findAllScheduledQuorums();
		if(!scheduledQuorums.isEmpty()){
			ShippingQuorum quorum = scheduledQuorums.get(0);
			if(quorum.getStatus() == ShippingQuorumStatus.WAITING) {
				quorum.setStatus(ShippingQuorumStatus.ALLOCATING);
				getDao(ShippingQuorumDao.class).
					updateStatus(quorum.getId().getQuorumDate(), quorum.getId().getQuorumId(), quorum.getStatus());
			}
		}
		return allQuorums;
	}
	
	public void reloadActiveQuorums() {
		allQuorums = findAllActiveQuorums();
		setTrailerNumberForAllQuorums();
	}
	
	private void setTrailerNumberForAllQuorums() {
		for(ShippingQuorum quorum :allQuorums) {
			if(quorum.getTrailerId() == ShippingTrailerInfo.TRAILER_ID_REPAIR) quorum.setTrailerNumber("REPAIR");
			else if(quorum.getTrailerId() == ShippingTrailerInfo.TRAILER_ID_EXCEPT) quorum.setTrailerNumber("EXCEP");
			for(ShippingTrailerInfo trailerInfo : shippingTrailerInfoList) {
				if(trailerInfo.getTrailerId() == quorum.getTrailerId()){
					quorum.setTrailerNumber(trailerInfo.getTrailerNumber());
					break;
				}
			}
		}
	}
	
	
	public List<ShippingQuorum> findAllScheduledQuorums() {
		List<ShippingQuorum> quorums = new ArrayList<ShippingQuorum>();
		for(ShippingQuorum quorum :allQuorums) {
			if(quorum.getStatus()!= ShippingQuorumStatus.DELAYED) quorums.add(quorum);
		}
		return quorums;
	}
	
	public List<ShippingQuorum> findAllDelayedQuorums() {
		List<ShippingQuorum> quorums = new ArrayList<ShippingQuorum>();
		for(ShippingQuorum quorum :allQuorums) {
			if(quorum.getStatus()== ShippingQuorumStatus.DELAYED) quorums.add(quorum);
		}
		return quorums;
	}
	
	private List<ShippingQuorum> findShippingQuorums(int trailerId) {
		List<ShippingQuorum> quorums =  getDao(ShippingQuorumDao.class).findAllByTrailerId(trailerId);
		return new SortedArrayList<ShippingQuorum>(quorums,"getTrailerRow");
	}
	
	private List<ShippingTrailerInfo> findAllShippingTrailers() {
		return getDao(ShippingTrailerInfoDao.class).findAllShippingTrailers();
	}
	
	public List<ShippingTrailerInfo> getAllShippingTrailers() {
		return shippingTrailerInfoList;
	}	
	
	public List<ShippingQuorumDetail> getActualShippingQuorumDetails(ShippingQuorum shippingQuorum) {
		List<ShippingQuorumDetail> details = shippingQuorum.getShippingQuorumDetails();
		if(shippingQuorum.getQuorumSize() >=details.size()) return details;
		else return details.subList(0, shippingQuorum.getQuorumSize());
	}
	
	public List<ShippingQuorum> findAllManualLoadQuorums() {
		List<ShippingQuorum> shippingQuorums = new SortedArrayList<ShippingQuorum>("getTrailerRowSeq");
		List<ShippingTrailerInfo> trailerInfos= findAllShippingTrailers();
		for(ShippingTrailerInfo trailerInfo :trailerInfos) {
			if(trailerInfo.getStatus().equals(ShippingTrailerInfoStatus.HOLD) || 
			   trailerInfo.getStatus().equals(ShippingTrailerInfoStatus.LOADING) ||
			   trailerInfo.getStatus().equals(ShippingTrailerInfoStatus.PCMP)) {
				List<ShippingQuorum> quorums = findShippingQuorums(trailerInfo.getTrailerId());
				for(ShippingQuorum quorum :quorums) {
					if(quorum.getStatus().equals(ShippingQuorumStatus.COMPLETE)||
					   quorum.getStatus().equals(ShippingQuorumStatus.INCOMPLETE)){
						quorum.setTrailerNumber(trailerInfo.getTrailerNumber());
						shippingQuorums.add(quorum);
					}
				}
			}	
		}
		return shippingQuorums;
	}
	
	public List<ShippingVanningSchedule> findAllActiveVanningSchedules() {
		List<ShippingVanningSchedule> schedules = getDao(ShippingVanningScheduleDao.class).findAllActiveVanningSchedules();
		
		for(ShippingVanningSchedule schedule :schedules) {
			schedule.setPlantCode(getPropertyBean().getShippingPlants().get(schedule.getPlant()));
		}
		
		return schedules;
	}
	
	public List<ShippingTrailer> findAvailableTrailers() {
		return getDao(ShippingTrailerDao.class).findAllAvaiableTrailers();
	}
	
	public String[] findAvailableTrailerNumbers() {
		List<ShippingTrailer> shippingTrailers = findAvailableTrailers();
		String[] trailerNumbers = new String[shippingTrailers.size()];
		for(int i = 0 ;i < shippingTrailers.size() ; i++) {
			trailerNumbers[i] = shippingTrailers.get(i).getTrailerNumber();
		}
		return trailerNumbers;
	}
	
	public void changeTrailerNumber(int trailerId, String trailerNumber) {
		getDao(ShippingTrailerInfoDao.class).changeTrailerNumer(trailerId, trailerNumber);
	}
	
	public List<ShippingVanningSchedule> findVanningSchedules(List<ShippingVanningSchedule> schedules , int trailerId) {
		List<ShippingVanningSchedule> selectedScheudles = new ArrayList<ShippingVanningSchedule>();
		for(ShippingVanningSchedule schedule :schedules) {
			if(schedule.getTrailerId() != null && trailerId == schedule.getTrailerId()) selectedScheudles.add(schedule);
		}
		return selectedScheudles;
	}
	
	public List<ShippingVanningSchedule> findNotAssignedVanningSchedules(List<ShippingVanningSchedule> schedules) {
		ShippingVanningSchedule firstSchedule = null;
		List<ShippingVanningSchedule> vanningSchedules = new ArrayList<ShippingVanningSchedule>();
		for(ShippingVanningSchedule schedule :schedules) {
			if(firstSchedule == null) {
				if(schedule.getTrailerId() == null) firstSchedule = schedule;
				else continue;
			}
			vanningSchedules.add(schedule);
		}
		return vanningSchedules;
	}
	
	public void updateQuorumStatus(ShippingQuorum quorum, ShippingQuorumStatus status){
		getDao(ShippingQuorumDao.class).
			updateStatus(quorum.getId().getQuorumDate(), quorum.getId().getQuorumId(), status);
	}
	
	/**
	 * release delayed quorum after "beforeQuorum" of the scheduled quorum list
	 */
	public void releaseDelayedQuorum(ShippingQuorum delayedQuorum, ShippingQuorum beforeQuorum){
		int index = allQuorums.indexOf(beforeQuorum) + 1;
		if(index <= 0) return;
		
		ShippingQuorum nextQuorum = index >= allQuorums.size() ? null: allQuorums.get(index);
		Date quorumDate = beforeQuorum.getId().getQuorumDate();
		int seq = 0;
		if(!delayedQuorum.equals(nextQuorum)) {
			seq = calculateSeqAfterQuorum(beforeQuorum,nextQuorum);
			
			getDao(ShippingQuorumDao.class).
				changeQuorum(delayedQuorum.getId().getQuorumDate(),delayedQuorum.getId().getQuorumId(),beforeQuorum.getId().getQuorumDate(),seq);
		} else {
			quorumDate = delayedQuorum.getId().getQuorumDate();
			seq = nextQuorum.getId().getQuorumId();
		}
		
		getDao(ShippingQuorumDao.class).updateStatus(quorumDate,seq, ShippingQuorumStatus.WAITING);
	}
	
	private int calculateSeqAfterQuorum(ShippingQuorum beforeQuorum) {
		int index = allQuorums.indexOf(beforeQuorum) + 1;
		if (index <= 0) return -1;
		ShippingQuorum nextQuorum = index  < allQuorums.size() ? allQuorums.get(index) : null; 
		return calculateSeqAfterQuorum(beforeQuorum,nextQuorum);
	}
	
	/**
	 * calculate seq number after the quorum.
	 * if there is no space after the quorum move all the quorums after
	 * @param quorum
	 * @return
	 */
	private int calculateSeqAfterQuorum(ShippingQuorum beforeQuorum, ShippingQuorum nextQuorum) {
		int seq = beforeQuorum.getId().getQuorumId() + QUORUM_ID_DELTA;
		if(nextQuorum != null){
			if(nextQuorum.getId().getQuorumDate().equals(beforeQuorum.getId().getQuorumDate())){
				seq = (nextQuorum.getId().getQuorumId() + beforeQuorum.getId().getQuorumId()) / 2;
				if(seq == nextQuorum.getId().getQuorumId() || seq == beforeQuorum.getId().getQuorumId()){
					// no space between 2 quorums
					getDao(ShippingQuorumDao.class).
						shiftQuorumSeq(nextQuorum.getId().getQuorumDate(),beforeQuorum.getId().getQuorumId(), QUORUM_ID_DELTA);
					seq = (nextQuorum.getId().getQuorumId() + beforeQuorum.getId().getQuorumId() +QUORUM_ID_DELTA) / 2;
				}
			}
		}
		return seq;
	}
	
	public void reset() {
		
	}
	
	public String getPalletType(String productSpecCode) {
		String modelCode = ProductSpec.extractModelCode(productSpecCode);
		String palletType = getPropertyBean().getPalletMap() == null ? 
				DEFAULT_PALLET_TYPE :getPropertyBean().getPalletMap().get(modelCode);  
		return StringUtils.isEmpty(palletType) ? DEFAULT_PALLET_TYPE : palletType;
	}
	
	public List<String> getAllPalletTypes(){
		Map<String,String> allPalletTypes = getPropertyBean().getPalletMap();
		if(allPalletTypes == null) allPalletTypes = new HashMap<String, String>();
		Set<String> palletTypes = new HashSet<String>(allPalletTypes.values());
		if(palletTypes.isEmpty()) palletTypes.add(DEFAULT_PALLET_TYPE);
		return new ArrayList<String>(palletTypes);
	}
	
	public void assignTrailer(String trailerNumber, List<ShippingVanningSchedule> schedules,ShippingVanningSchedule splitSchedule) {
		List<ShippingQuorum> shippingQuorums = createShippingQuorums(schedules);
		getDao(ShippingTrailerInfoDao.class).assignTrailer(trailerNumber, schedules, shippingQuorums,splitSchedule);
	}
	
	private List<ShippingQuorum> createShippingQuorums(List<ShippingVanningSchedule> schedules) {
		List<ShippingQuorum> shippingQuorums = new ArrayList<ShippingQuorum>();
		
		if(schedules.isEmpty()) return shippingQuorums;
		
		ShippingQuorum lastQuorum = findLastShippingQuorum();
		int quorumSeq = 1;
		
		ShippingQuorum shippingQuorum = createShippingQuorum(lastQuorum,schedules.get(0).getYmto());
		shippingQuorum.setTrailerRow(1);
		shippingQuorum.setQuorumSize(ShippingQuorum.DEFAULT_QUORUM_SIZE);
		
		for(ShippingVanningSchedule schedule :schedules) {
			for(int i=0;i<schedule.getSchQty();i++) {
				ShippingQuorumDetail detail = 
					new ShippingQuorumDetail(shippingQuorum.getId().getQuorumDate(),
							shippingQuorum.getId().getQuorumId(),
							convertQuorumSequence(schedule.getPlant(), quorumSeq -1));
				detail.setKdLot(schedule.getKdLot());
				detail.setYmto(schedule.getYmto());
				quorumSeq++;
				shippingQuorum.getShippingQuorumDetails().add(detail);
			    if(shippingQuorum.getShippingQuorumDetails().size() == ShippingQuorum.DEFAULT_QUORUM_SIZE){
			    	shippingQuorums.add(shippingQuorum);
			    	shippingQuorum = createShippingQuorum(shippingQuorum,schedule.getYmto());
			    	quorumSeq = 1;
			    }
			}
		}
		
		if(!shippingQuorum.getShippingQuorumDetails().isEmpty()) {
			shippingQuorums.add(shippingQuorum);
	    	shippingQuorum.setQuorumSize(shippingQuorum.getShippingQuorumDetails().size());
		}
		
		return shippingQuorums;
	}
	
	private int convertQuorumSequence(String plant, int number) {
		return getPropertyBean().getLoadingSequenceMap(Integer[].class).get(plant)[number];
	}
	
	public ShippingQuorum findLastShippingQuorum() {
		ShippingQuorum lastQuorum = getDao(ShippingQuorumDao.class).findLastShippingQuorum();
		Date currentDate =  new Date(Calendar.getInstance().getTimeInMillis());
		if(lastQuorum == null || !lastQuorum.getId().getQuorumDate().toString().equals(currentDate.toString())){
			lastQuorum = new ShippingQuorum(currentDate,0);
			allQuorums.add(lastQuorum);
		}
		return lastQuorum;
	}
	
	private ShippingQuorum createShippingQuorum(ShippingQuorum lastQuorum,String productSpecCode) {
		ShippingQuorum shippingQuorum = 
			new ShippingQuorum(lastQuorum.getId().getQuorumDate(),lastQuorum.getId().getQuorumId() + QUORUM_ID_DELTA);
		shippingQuorum.setTrailerRow(lastQuorum.getTrailerRow() + 1);
		shippingQuorum.setQuorumSize(ShippingQuorum.DEFAULT_QUORUM_SIZE);
		shippingQuorum.setPalletType(getPalletType(productSpecCode));
		return shippingQuorum;
	}
	
	private boolean isLastShippingTrailerInfo(int trailerId) {
		ShippingTrailerInfo lastTrailer = getDao(ShippingTrailerInfoDao.class).getLastShippingTrailerInfo();
		return lastTrailer != null && lastTrailer.getTrailerId() == trailerId;
	}
	
	public boolean deassignTrailer(int trailerId) {
		if(!isLastShippingTrailerInfo(trailerId)) return false;
		return getDao(ShippingTrailerInfoDao.class).deassignTrailer(trailerId) > 0;	
	}
	
	public int getTrailerSize(String plantCode) {
		Map<String,Integer> trailerSizeMap = getPropertyBean().getTrailerSizes(Integer.class);
		return trailerSizeMap.get(plantCode);
	}
	
	public void resizeQuorum(ShippingQuorum quorum, int newSize) {
		quorum.setQuorumSize(newSize);
		if(newSize==0 || (quorum.isLoading() && newSize == quorum.getLoadedCount() && newSize < ShippingQuorum.DEFAULT_QUORUM_SIZE)){
			quorum.setStatus(ShippingQuorumStatus.INCOMPLETE);
		};
		
		if(ShippingQuorumStatus.ALLOCATED.equals(quorum.getStatus())) quorum.setStatus(ShippingQuorumStatus.ALLOCATING);
		getDao(ShippingQuorumDao.class).save(quorum);
	}
	
	public void removeEnginesFromQuorum(ShippingQuorum quorum) {
		if(quorum == null || quorum.getShippingQuorumDetails() == null) return;
		
		int count = 0;
		
		List<ShippingQuorumDetail> details = quorum.getShippingQuorumDetails();
		for(ShippingQuorumDetail detail: details) {
			if(StringUtils.isEmpty(detail.getEngineNumber())) continue;
			detail.setEngineNumber(null);
			List<ShippingVanningSchedule> schedules = getDao(ShippingVanningScheduleDao.class).findVanningSchedules(quorum.getTrailerId(), detail.getKdLot());
			ShippingVanningSchedule schedule = findScheduleToRemove(detail.getKdLot(), schedules);
			if(schedule !=null) {
				schedule.setActQty(schedule.getActQty() -1);
				getDao(ShippingVanningScheduleDao.class).update(schedule);
				count++;
			}
		}
		
		ShippingTrailerInfo trailerInfo = getDao(ShippingTrailerInfoDao.class).findByKey(quorum.getTrailerId());
		trailerInfo.setActQty(trailerInfo.getActQty() - count);
		getDao(ShippingTrailerInfoDao.class).update(trailerInfo);
		
		getDao(ShippingQuorumDetailDao.class).saveAll(details);
	}
	
	public ShippingQuorum createRepairQuorum(ShippingQuorum beforeQuorum, int quorumSize, String palletType) {
		int seq = calculateSeqAfterQuorum(beforeQuorum);
		if(seq < 0) {
			beforeQuorum = findLastShippingQuorum();
			seq = beforeQuorum.getId().getQuorumId()+QUORUM_ID_DELTA;
		}
		ShippingQuorum quorum = new ShippingQuorum(beforeQuorum.getId().getQuorumDate(),seq);
		quorum.setTrailerRow(1);
		quorum.setQuorumSize(quorumSize);
		quorum.setPalletType(palletType);
		quorum.setTrailerId(ShippingTrailerInfo.TRAILER_ID_REPAIR);
		for(int i =1;i<=quorumSize;i++) {
			ShippingQuorumDetail detail = new ShippingQuorumDetail(
					quorum.getId().getQuorumDate(),
					quorum.getId().getQuorumId(),
					i);
			quorum.getShippingQuorumDetails().add(detail);
		}
		
		getDao(ShippingQuorumDao.class).saveWithDetail(quorum);
		
		return quorum;
	}
	
	public void createExceptionalQuorum(ShippingQuorum beforeQuorum,ShippingQuorum exceptionalQuorum){
		int seq = calculateSeqAfterQuorum(beforeQuorum);
		if(seq < 0) {
			beforeQuorum = findLastShippingQuorum();
			seq = beforeQuorum.getId().getQuorumId()+QUORUM_ID_DELTA;
		}
		ShippingQuorumId id = new ShippingQuorumId();
		id.setQuorumDate(beforeQuorum.getId().getQuorumDate());
		id.setQuorumId(seq);
		exceptionalQuorum.setId(id);
		exceptionalQuorum.setTrailerRow(1);
		exceptionalQuorum.setTrailerId(ShippingTrailerInfo.TRAILER_ID_EXCEPT);
		for(int i=0; i<exceptionalQuorum.getActualQuorumSize();i++) {
			exceptionalQuorum.getShippingQuorumDetails().get(i).getId().setQuorumDate(exceptionalQuorum.getId().getQuorumDate());
			exceptionalQuorum.getShippingQuorumDetails().get(i).getId().setQuorumId(exceptionalQuorum.getId().getQuorumId());
		}
		
		getDao(ShippingQuorumDao.class).saveWithDetail(exceptionalQuorum);
	}
	
	/*
	 * only allow first quorum in the scheduled quorum list to be completed
	 * as long as the quorum is completed , the corresponding quorum is changed LOADING status
	 * 
	 */
	public void completeQuorum(ShippingQuorum quorum) {
		quorum.setStatus(ShippingQuorumStatus.INCOMPLETE);
		getDao(ShippingQuorumDao.class).save(quorum);
		
		if(quorum.getTrailerId() == ShippingTrailerInfo.TRAILER_ID_EXCEPT || 
		   quorum.getTrailerId() == ShippingTrailerInfo.TRAILER_ID_REPAIR) return;
		
		ShippingTrailerInfo trailerInfo = findTrailerInfo(quorum.getTrailerId(), shippingTrailerInfoList);
		if(trailerInfo == null) return;
		
		if(ShippingTrailerInfoStatus.WAITING.equals(trailerInfo.getStatus()) ||
		   ShippingTrailerInfoStatus.HOLD.equals(trailerInfo.getStatus())){
			trailerInfo.setStatus(ShippingTrailerInfoStatus.LOADING);
			getDao(ShippingTrailerInfoDao.class).save(trailerInfo);
			
			updateOtherTrailerInfo(quorum.getTrailerId());
		}
		
	}
	
	public Engine findEngine(String ein) {
		return getDao(EngineDao.class).findByKey(ein);
	}
	
	public String formatLotSummary(List<ShippingVanningSchedule> scheduleList){
		
		String lotStr ="";
		int i = 0;
		boolean smallLotFlag = false;
		for(ShippingVanningSchedule schedule :scheduleList) {
			String index = schedule.getKdLotIndex();
			
			if(!smallLotFlag) i++;
			if(!smallLotFlag && !index.equals("1"))smallLotFlag = true;
			
			lotStr += "LOT"+i + (smallLotFlag ? "-" +index :"") + "|";
			lotStr += schedule.getKdLot() + "|";
			lotStr += ProductSpec.extractModelYearCode(schedule.getYmto());
			lotStr += ProductSpec.extractModelTypeCode(schedule.getYmto()) + "|";
			lotStr += schedule.getSchQty()+"|";
			
			if(smallLotFlag && index.equals("1")) smallLotFlag = false;
			
		}
		return lotStr.substring(0,lotStr.length() -1);
	}
	
	public String formatEngineTypes(int trailerId) {
		List<ShippingQuorum> quorumList = findShippingQuorums(trailerId);
		Collections.reverse(quorumList);
		String result ="";
		for(ShippingQuorum quorum : quorumList){
			for(int i = 0;i < ShippingQuorum.DEFAULT_QUORUM_SIZE;i++) {
				String ymto = i < quorum.getActualQuorumSize()
					? quorum.getShippingQuorumDetails().get(i).getYmto(): null;
				String modelCode = StringUtils.isEmpty(ymto) ? "-": ProductSpec.extractModelTypeCode(ymto);
				result += modelCode + "|";
			}
		}
		return result.substring(0,result.length() -1);
	}
	
	public void printVanningScheduleSheet(ShippingTrailerInfo trailerInfo,List<ShippingVanningSchedule> schedules){
		DataContainer dc = new DefaultDataContainer();
		dc.put("TRAILER",trailerInfo.getTrailerNumber());
		dc.put("SHIPPING_DATE", StringUtil.now("yyyy-MM-dd"));
		dc.put("ENGINE_LOTS",formatEngineTypes(trailerInfo.getTrailerId()));
		dc.put("LOTS_SUMMARY", formatLotSummary(schedules));
		
		ServiceFactory.getService(BroadcastService.class).
			broadcast(getPropertyBean().getPrinterPPID(), dc);
	}
	
	public void syncVanningSchedules() {
		getDao(ShippingVanningScheduleDao.class).syncVanningSchedule();
	}
	
	
	
	public List<String> checkRequiredParts(Engine engine) {
		return getDao(RequiredPartDao.class).findMissingRequiredParts(engine.getProductSpecCode(),
				getProcessPointId(), engine.getProductType(), engine.getProductId(), null);
	}
	
	public boolean checkTestFireResults(Engine engine) {
		List<EngineFiringResult> firingResults = getDao(EngineFiringResultDao.class).findAllByProductId(engine.getProductId());
		return !firingResults.isEmpty();
	}
	
	public void saveManualLoadEngines(ShippingQuorum quorum, List<String> eins) {
	//	getDao(ShippingQuorumDao.class).updateManualLoadEngines(quorum,eins);
		updateManualLoadEngines(quorum,eins);
	}
	
	public void updateManualLoadEngines(ShippingQuorum quorum, List<String> eins) {
		List<ShippingQuorum> quorums = new ArrayList<ShippingQuorum>();
		List<ShippingQuorumDetail> quorumDetails = new ArrayList<ShippingQuorumDetail>();
		
		List<ShippingTrailerInfo> trailerInfos = new ArrayList<ShippingTrailerInfo>();
		List<ShippingVanningSchedule> vanningSchedules = new ArrayList<ShippingVanningSchedule>();
		
		// update act qty of vanning schedule and trailer info for deleted engines
		for(ShippingQuorumDetail detail : quorum.getShippingQuorumDetails()){
			if(!StringUtils.isEmpty(detail.getEngineNumber()) && !eins.contains(detail.getEngineNumber())){
				ShippingTrailerInfo trailerInfo = findTrailerInfo(quorum.getTrailerId(), trailerInfos);
				trailerInfo.setActQty(trailerInfo.getActQty() - 1);
				decrementVanningSchedule(quorum.getTrailerId(), detail.getKdLot(), vanningSchedules);
			}
		}
		
		for(int i =0; i<quorum.getShippingQuorumDetails().size();i++){
			String ein = eins.get(i);
			ShippingQuorumDetail detail = quorum.getShippingQuorumDetails().get(i);
			detail.setEngineNumber(ein);
			detail.setStatus(ShippingQuorumDetailStatus.MANUAL_LOAD);
			
			List<ShippingQuorumDetail> loadedQuorums = getDao(ShippingQuorumDetailDao.class).findAllByEngineNumber(ein);
			boolean isUpdated = false;
			for(ShippingQuorumDetail loadedQuorumDetail : loadedQuorums) {
				if(!quorum.getShippingQuorumDetails().contains(loadedQuorumDetail)) {
					loadedQuorumDetail.setEngineNumber(null);
					loadedQuorumDetail.setStatus(ShippingQuorumDetailStatus.MANUAL_LOAD);
					ShippingQuorum loadedQuorum = findQuorum(loadedQuorumDetail.getId().getQuorumDate(), loadedQuorumDetail.getId().getQuorumId(), quorums); 
					loadedQuorum.setStatus(ShippingQuorumStatus.INCOMPLETE);
					ShippingTrailerInfo trailerInfo = findTrailerInfo(loadedQuorum.getTrailerId(), trailerInfos);
					if(trailerInfo != null)	trailerInfo.setActQty(trailerInfo.getActQty() - 1);
					decrementVanningSchedule(loadedQuorum.getTrailerId(), loadedQuorumDetail.getKdLot(), vanningSchedules); 
					quorumDetails.add(loadedQuorumDetail);
					isUpdated = true;
				}
			}
			if(isUpdated || loadedQuorums.isEmpty()) {
				ShippingTrailerInfo trailerInfo = findTrailerInfo(quorum.getTrailerId(), trailerInfos);
				trailerInfo.setActQty(trailerInfo.getActQty() + 1);
				incrementVanningSchedule(quorum.getTrailerId(), detail.getKdLot(), vanningSchedules);
			}
		}
		
		quorum.setStatus(ShippingQuorumStatus.COMPLETE);
		quorums.add(quorum);
		quorumDetails.addAll(quorum.getShippingQuorumDetails());
		
		getDao(ShippingQuorumDetailDao.class).saveAll(quorumDetails);
		getDao(ShippingQuorumDao.class).saveAll(quorums);
		getDao(ShippingVanningScheduleDao.class).saveAll(vanningSchedules);
		getDao(ShippingTrailerInfoDao.class).saveAll(trailerInfos);
	}
	
	private ShippingVanningSchedule findSchedule(int trailerId, String kdLot, List<ShippingVanningSchedule> schedules) {
		ShippingVanningSchedule schedule = getDao(ShippingVanningScheduleDao.class).findIncompleteSchedule(trailerId, kdLot);
		if(schedule == null ) return schedule;
		int index = schedules.indexOf(schedule);
		if(index >=0 ) schedule = schedules.get(index);
		else schedules.add(schedule);
		return schedule; 
	}
	
	private ShippingVanningSchedule incrementVanningSchedule(int trailerId, String kdLot, List<ShippingVanningSchedule> schedules) {
		if(trailerId < 0 || kdLot == null) return null;
		ShippingVanningSchedule schedule = findScheduleToAdd(kdLot,schedules);
		if(schedule == null) {
			List<ShippingVanningSchedule> loadedSchedules = getDao(ShippingVanningScheduleDao.class).findVanningSchedules(trailerId,kdLot);
			if(loadedSchedules.isEmpty()) return null;
			schedules.addAll(loadedSchedules);
			schedule = findScheduleToAdd(kdLot,schedules);
		}
		if(schedule != null ) schedule.setActQty(schedule.getActQty() + 1);
		return schedule;
	}
	
	private ShippingVanningSchedule decrementVanningSchedule(int trailerId, String kdLot, List<ShippingVanningSchedule> schedules) {
		if(trailerId < 0 || kdLot == null) return null;
		ShippingVanningSchedule schedule = findScheduleToRemove(kdLot,schedules);
		if(schedule == null) {
			List<ShippingVanningSchedule> loadedSchedules = getDao(ShippingVanningScheduleDao.class).findVanningSchedules(trailerId,kdLot);
			if(loadedSchedules.isEmpty()) return null;
			schedules.addAll(loadedSchedules);
			schedule = findScheduleToRemove(kdLot,schedules);
		}
		if(schedule != null ) schedule.setActQty(schedule.getActQty() -1);
		return schedule;
	}
	
	private ShippingVanningSchedule findScheduleToAdd(String kdLot, List<ShippingVanningSchedule> schedules) {
		for(int i=0; i< schedules.size();i++) {
			ShippingVanningSchedule schedule = schedules.get(i);
			if(schedule != null && kdLot.equalsIgnoreCase(schedule.getKdLot()) 
			   && schedule.getActQty() <schedule.getSchQty()) return schedule;
		}
		return null;
	}
	
	private ShippingVanningSchedule findScheduleToRemove(String kdLot, List<ShippingVanningSchedule> schedules) {
		for(int i=schedules.size()-1;i>=0;i--) {
			ShippingVanningSchedule schedule = schedules.get(i);
			if(schedule != null && kdLot.equalsIgnoreCase(schedule.getKdLot()) 
			   && schedule.getActQty() > 0) return schedule;
		}
		return null;
	}
	
	private ShippingTrailerInfo findTrailerInfo(int trailerId, List<ShippingTrailerInfo> trailerInfos) {
		ShippingTrailerInfo trailerInfo = new ShippingTrailerInfo();
		trailerInfo.setTrailerId(trailerId);
		int index = trailerInfos.indexOf(trailerInfo);
		if(index < 0){
			trailerInfo = getDao(ShippingTrailerInfoDao.class).findByKey(trailerId);
			if(trailerInfo != null) trailerInfos.add(trailerInfo);
		}else trailerInfo = trailerInfos.get(index);
		return trailerInfo;
	}
	
	private ShippingQuorum findQuorum(Date quorumDate, int quorumId, List<ShippingQuorum> quorums) {
		ShippingQuorum loadedQuorum = new ShippingQuorum(quorumDate,quorumId);
		int index = quorums.indexOf(loadedQuorum);
		if(index <0 ){
			loadedQuorum = getDao(ShippingQuorumDao.class).findByKey(loadedQuorum.getId());
			quorums.add(loadedQuorum);
		}else loadedQuorum = quorums.get(index);
		return loadedQuorum; 
	}
	
	public List<String> getAllMTOCsInTrackingArea() {
		return getDao(EngineDao.class).findAllEngineMTOCInTrackingArea(getPropertyBean().getTrackingArea());
	}
	
	public void updateOtherTrailerInfo(int trailerId) {
		reloadShippingTrailerInfoList();
		for(ShippingTrailerInfo trailerInfo : shippingTrailerInfoList) {
			if(trailerInfo.getStatus().equals(ShippingTrailerInfoStatus.LOADING) &&
			   trailerInfo.getTrailerId() != trailerId) {
				trailerInfo.setStatus(ShippingTrailerInfoStatus.HOLD);
				getDao(ShippingTrailerInfoDao.class).save(trailerInfo);
			}
		}
	}
	
	public void updateScheduleQuantity(int trailerId, String kdLot, List<ShippingVanningSchedule> schedules) {
		ShippingVanningSchedule schedule = findSchedule(trailerId, kdLot, schedules);
		if(schedule != null) {
			schedule.setActQty(schedule.getActQty() + 1);
			getDao(ShippingVanningScheduleDao.class).update(schedule);
		}  
	}
	
	public void completeTrailer(ShippingTrailerInfo trailerInfo) {
		List<ShippingQuorum> quorums = new ArrayList<ShippingQuorum>();
		List<EngineManifest> engineManifests = new ArrayList<EngineManifest>();
		for(ShippingQuorum quorum : getShippingQuorums(trailerInfo)) {
			if(quorum.getStatus().equals(ShippingQuorumStatus.DELAYED)) {
				quorum.setStatus(ShippingQuorumStatus.INCOMPLETE);
				quorums.add(quorum);
			}
			for(ShippingQuorumDetail detail:quorum.getShippingQuorumDetails()) {
				if(!StringUtils.isEmpty(detail.getEngineNumber())){
					track(ProductType.ENGINE, detail.getEngineNumber());
					
					try {
						EngineManifest em = getEngineShippingHelper().createEngineManifest(detail, applicationContext.getProcessPoint().getSiteName());
						engineManifests.add(em);
					} catch (Exception e) {
						getLogger().warn(e, "Failed to create Engine Manifest data:", detail.getEngineNumber());
					}
				}
				
			}
		}
		getDao(ShippingQuorumDao.class).saveAll(quorums);
		trailerInfo.setStatus(ShippingTrailerInfoStatus.COMPLETE);
		getDao(ShippingTrailerInfoDao.class).save(trailerInfo);
		
		try {
			getDao(EngineManifestDao.class).saveAll(engineManifests);
		} catch (Exception e) {
			getLogger().error(e, "Failed to save Engine Manifest Data.");
		}
		
		for(EngineManifest engineManifest : engineManifests) {
			getEngineShippingHelper().invokeBroadcast(engineManifest.getId().getEngineNo(), engineManifest.getEngineKdLot(), getProcessPointId());
		}
	}
	

	private EngineShippingHelper getEngineShippingHelper() {
		if(engineShippingHelper == null)
			engineShippingHelper = new EngineShippingHelper(getPropertyBean());
		
		return engineShippingHelper;
	}

	public String performProductCheck(ShippingTrailerInfo trailerInfo) {
		for(ShippingQuorum quorum : getShippingQuorums(trailerInfo)) {
			for(ShippingQuorumDetail detail:quorum.getShippingQuorumDetails()) {
				if(!StringUtils.isEmpty(detail.getEngineNumber())){
					Engine engine = findEngine(detail.getEngineNumber());
					if(engine == null) return "Invalid engine number: " + detail.getEngineNumber(); 
					String message = performProductCheck(engine);
					if(!StringUtils.isEmpty(message)) return message;
				}
			}
		}
		return "";
	
	}
	
	private List<ShippingQuorum> getShippingQuorums(ShippingTrailerInfo trailerInfo) {
		List<ShippingQuorum> quorums = trailerInfo.getShippingQuorums();
		if(trailerInfo.getShippingQuorums().isEmpty()) {
			quorums = findShippingQuorums(trailerInfo.getTrailerId());
			trailerInfo.setShippingQuorums(quorums);
		}
		return quorums; 
	}
	
	public String performProductCheck(String ein) {
		return performProductCheck(findEngine(ein));
	}
	
	public String performProductCheck(Engine engine) {
		Map<String,Object> checkResults = 
			ProductCheckUtil.check(engine, getProcessPoint(), getPropertyBean().getProductCheckTypes());
		return ProductCheckUtil.toErrorString(checkResults);
	}
	
	/*
	 * check the quorum mtoc and engine mtoc and return not matched quorum detail items
	 * original mtoc is saved in kdLot field for convienence
	 */
	public List<ShippingQuorumDetail> checkEngineModels(int trailerId) {
		return getDao(ShippingQuorumDetailDao.class).checkEngineModels(trailerId);
	}
	
	public EngineShippingPropertyBean getPropertyBean() {
		return PropertyService.getPropertyBean(EngineShippingPropertyBean.class, getProcessPointId());
	}
	
}
