package com.honda.galc.client.engine.aeon;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.BlockBuildResultDao;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * 
 * <h3>BlockReceivingModel Class description</h3>
 * <p> BlockReceivingModel description </p>
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
 * Jan 14, 2016
 *
 *
 */
public class BlockReceivingModel {
	
	private ApplicationContext applicationContext;
	
	private final String AEP_MC_NUMBER_MASK = "5A20A";
	private final String AEP_MODEL_CODE = "5A2";
	
	private final String BLOCK_BORE_MEASURE = "BLOCK BORE MEASURE";
	protected final int BORE_MEASUREMENT_COUNT = 4;
	private final List<String> validBoreMeasurementValues = Arrays.asList(new String[] {"A","B"});
	
	
	protected final String CRANK_JOURNAL = "CRANK JOURNAL";
	protected final int CRANK_JOURNAL_COUNT = 5;
	private final List<String> validCrankJournalValues = Arrays.asList(new String[]{"A","B","C","D"});
	
	private boolean isNewBlock = true;
	
	protected Block block;
	
	public BlockReceivingModel(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	public Block validateBlock(String mcNumber) {
		if(!isAEPBlock(mcNumber)) {
			throw new TaskException("MC Number " + mcNumber  + " is not a valid AEP block ");
		}
		
		block = getDao(BlockDao.class).findByMCDCNumber(mcNumber);
		isNewBlock = block == null;
		if(isNewBlock) block = createBlock(mcNumber);
		
		block.setModelCode(AEP_MODEL_CODE);
		block.setTrackingStatus(applicationContext.getProcessPoint().getLineId());
		block.setLastPassingProcessPointId(applicationContext.getProcessPointId());
	
		return block != null ? block : createBlock(mcNumber);
	}
	
	public boolean isNewBlock() {
		 return isNewBlock;
	}
	
	public void resetData() {
		this.block = null;
		this.isNewBlock = true;
	}
	
	public boolean isAEPBlock(String mcNumber) {
		return !StringUtils.isEmpty(mcNumber) && mcNumber.startsWith(AEP_MC_NUMBER_MASK);
	}
	
	public String getBlockBoreMeasurement() {
		if(block == null || isNewBlock) return "";
		BlockBuildResult result = getDao(BlockBuildResultDao.class).findById(block.getBlockId(), BLOCK_BORE_MEASURE);
		return result == null ? "" : result.getResultValue();
	}
	
	public List<BlockBuildResult> getCrankJournal() {
		List<BlockBuildResult> results = new ArrayList<BlockBuildResult>();
		if(block == null || isNewBlock) return results;
		for(int i=0;i<CRANK_JOURNAL_COUNT;i++) {
			String partName = CRANK_JOURNAL + " " + (i+1);
			BlockBuildResult result = getDao(BlockBuildResultDao.class).findById(block.getBlockId(), partName);
			if(result != null) results.add(result);
		}
		return results;
	}
	
	private Block createBlock(String mcNumber) {
		Block block = new Block(mcNumber);
		block.setDcSerialNumber(mcNumber);
		block.setMcSerialNumber(mcNumber);
		return block;
	}
	
	public boolean checkBoreMeasureMent(String text) {
		return validBoreMeasurementValues.contains(text); 
	}

	public boolean checkCrankJournal(String text) {
		return validCrankJournalValues.contains(text); 
	}
	
	public String getCrankJournal(List<BlockBuildResult> crankJournals,int index) {
		String partName = CRANK_JOURNAL + " " + (index + 1);
		for(BlockBuildResult result : crankJournals) {
			if(result.getPartName().equalsIgnoreCase(partName)) return result.getResultValue();
		}
		return "";
	}
	
	public void saveData(String boreMeasurement, List<String> crankJournals) {
		getDao(BlockDao.class).save(block);
		
		List<BlockBuildResult> results = new ArrayList<BlockBuildResult>();
		results.add(createBuildResult(BLOCK_BORE_MEASURE, boreMeasurement));
		
		for(int i=0; i<CRANK_JOURNAL_COUNT;i++) {
			if(i >= crankJournals.size()) break;
			results.add(createBuildResult(CRANK_JOURNAL + " " + (i + 1), crankJournals.get(i)));
		}
		
		getDao(BlockBuildResultDao.class).saveAll(results);
		
	}
	
	private BlockBuildResult createBuildResult(String partName, String value) {
		BlockBuildResult result = (BlockBuildResult) ProductTypeUtil.createBuildResult("BLOCK", block.getBlockId(), partName);
		result.setResultValue(value);
		result.setAssociateNo(applicationContext.getUserId());
		result.setInstalledPartStatus(InstalledPartStatus.OK);
		return result;
	}
	
}
