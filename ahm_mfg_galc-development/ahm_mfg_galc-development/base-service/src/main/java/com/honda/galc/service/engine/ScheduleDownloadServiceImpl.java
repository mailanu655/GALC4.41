package com.honda.galc.service.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.DownloadLotSequenceDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.product.DownloadLotSequence;
import com.honda.galc.entity.product.DownloadLotSequenceId;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.service.ScheduleDownloadService;
import com.honda.galc.service.datacollection.IoServiceBase;


/**
 * 
 * 
 * <h3>ScheduleDownloadServiceImpl Class description</h3>
 * <p> ScheduleDownloadServiceImpl description </p>
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
 * Apr 15, 2014
 *
 *
 */
public class ScheduleDownloadServiceImpl extends IoServiceBase implements ScheduleDownloadService{
	
	private static final int LENGTH_LOT_SIZE = 4;
	
	@Autowired
	PreProductionLotDao preProductionLotDao;
	
	@Autowired
	DownloadLotSequenceDao downloadLotSequenceDao;
	
	@Override
	public DataContainer processData() {
		
		if(StringUtils.isEmpty(getProcessLocation())) {
			getLogger().error("Missing PROCESS_LOCATION property");
			return sendDataIncomplete();
		}
		int count = getDownloadLotCount();
		if(count > getMaxDownloadLotCount()) {
			getLogger().error("Number of lots " + count + " exceeds the max data format configurations");
			return sendDataIncomplete();
		}else if(count <= 0){
			getLogger().error("Number of lots to be downloaded is 0");
			return sendDataIncomplete();
		}
		
		PreProductionLot lastLot = findLastPreProductionLot();
		
		if(lastLot == null){
			getLogger().error("Could not find the last pre-production lot");
			return sendDataIncomplete();
		}
		List<PreProductionLot> preProdLots = findPreProductionLots(lastLot, count);
		
		DataContainer dc = populateData(preProdLots);
		getLogger().info("sending lots : " + dc.toString());
		if(!preProdLots.isEmpty())
			updateDownloadSequence(preProdLots.get(preProdLots.size() - 1).getProductionLot());
		
		return dc;
	}
	
	private int getMaxDownloadLotCount() {
		int size = getDevice().getReplyDeviceDataFormats().size();
		if( size % 4 != 2) throw new TaskException("device data format configuration incorrect");
		return size / 4;
	}
	
	private int getDownloadLotCount() {
		return (Integer)getDevice().getInputValue(TagNames.NO_OF_LOTS_TO_BE_DOWNLOADED.name());
	}
	
	private String getLastStartProductId() {
		return StringUtils.trim((String)getDevice().getInputValue(TagNames.LAST_DOWNLOAD_STARTING_EIN.name()));
	}
	
	private List<PreProductionLot> findPreProductionLots(PreProductionLot lastLot,int count) {
		List<PreProductionLot> preProdLots = new ArrayList<PreProductionLot>();
		if (lastLot == null) {
			lastLot = preProductionLotDao.findFirstAvailableLot(getProcessLocation());
			if(lastLot == null) return preProdLots;
		}else lastLot = preProductionLotDao.findByKey(lastLot.getNextProductionLot());
	    
		preProdLots.add(lastLot);
		
		for(int i=1;i<count;i++) {
			if(lastLot != null && lastLot.getNextProductionLot() != null){
				lastLot = preProductionLotDao.findByKey(lastLot.getNextProductionLot());
			    preProdLots.add(lastLot);
			}else break;
		}
		return preProdLots; 
	}
	
	private DataContainer populateData(List<PreProductionLot> preProdLots) {
		int seq = 0;
		List<DeviceFormat> deviceFormats = getDevice().getReplyDeviceDataFormats();
		for(PreProductionLot preProdLot :preProdLots) {
			deviceFormats.get(seq++).setValue(preProdLot.getLotNumber());
			deviceFormats.get(seq++).setValue(preProdLot.getProductSpecCode());
			deviceFormats.get(seq++).setValue(StringUtils.leftPad(String.valueOf(preProdLot.getLotSize()),LENGTH_LOT_SIZE,"0"));
			deviceFormats.get(seq++).setValue(preProdLot.getStartProductId());
		}
		getDevice().setReplyValue(TagNames.NO_LOTS_DOWNLOADED.name(),preProdLots.size());
		getDevice().setReplyValue(TagNames.DATA_COLLECTION_COMPLETE.name(), "1");
		
		return getDevice().toReplyDataContainer(true);
	}
	
	private PreProductionLot findLastPreProductionLot() {
		String lastProduct = getLastStartProductId();
		if(!StringUtils.isEmpty(lastProduct)) 
			return preProductionLotDao.findByStartProductId(getLastStartProductId());
		
		DownloadLotSequence lot = downloadLotSequenceDao.findByKey(new DownloadLotSequenceId(getProcessLocation(),getProcessPointId()));
		if(lot == null)	return null;
		else return preProductionLotDao.findByKey(lot.getEndProductionLot());
	}
	
	private void updateDownloadSequence(String prodLot){
		DownloadLotSequence downloadLot = new DownloadLotSequence();
		downloadLot.setId(new DownloadLotSequenceId(getProcessLocation(),getProcessPointId()));
		downloadLot.setEndProductionLot(prodLot);
		downloadLotSequenceDao.save(downloadLot);
	}
	
	private DataContainer sendDataIncomplete(){
		getDevice().setReplyValue(TagNames.NO_LOTS_DOWNLOADED.name(),0);
		getDevice().setReplyValue(TagNames.DATA_COLLECTION_COMPLETE.name(), "0");
		return getDevice().toReplyDataContainer(true);
	}
	
	private String getProcessLocation() {
		return getProperty("PROCESS_LOCATION");
	}

}
