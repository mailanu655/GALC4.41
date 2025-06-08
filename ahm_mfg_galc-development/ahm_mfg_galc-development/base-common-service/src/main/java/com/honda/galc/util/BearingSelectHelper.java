package com.honda.galc.util;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.BearingSelectResultDao;
import com.honda.galc.dao.product.BlockBuildResultDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.bearing.BearingMatrixCellDao;
import com.honda.galc.data.LineSideContainerTag;
import com.honda.galc.entity.bearing.BearingMatrixCell;
import com.honda.galc.entity.bearing.BearingMatrixCellId;
import com.honda.galc.entity.bearing.BearingType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BearingSelectResult;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.property.BearingSelectPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
/**
 * 
 * <h3>BearingSelectHelper</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> BearingSelectHelper description </p>
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
 * <TD>Mar 14, 2018</TD>
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
 * @since Mar 14, 2018
 */
public class BearingSelectHelper {
	public static final int CRANK_MAIN_MEASUREMENTS_START_IX = 16;
	public static final int CRANK_CONROD_MEASUREMENTS_START_IX = 21;
	private BearingSelectPropertyBean property;
	Logger logger;

	
	public BearingSelectHelper(String ppId, Logger logger) {
		super();
		property = PropertyService.getPropertyBean(BearingSelectPropertyBean.class, ppId);
		this.logger = logger;
	}


	public String selectBlockMeasurements(Block block, int mainBearingCount) {
		StringBuilder sb = new StringBuilder();
		if (block == null) {
			return sb.toString();
		}
		BlockBuildResultDao blockBuildResultDao = ServiceFactory.getDao(BlockBuildResultDao.class);

		for (int i = 1; i <= mainBearingCount; i++) {
			String partName = String.format("%s %s", LineSideContainerTag.PART_CRANK_JOURNAL_PREFIX, i);
			BlockBuildResult blockBuildResult = blockBuildResultDao.findById(block.getBlockId(), partName);
			if (blockBuildResult != null) {
				sb.append(blockBuildResult.getResultValue());
			}
		}
		return sb.toString();
	}
	
	public String selectConrodMeasurements(BaseProduct product) {
		MeasurementDao measurementDao = ServiceFactory.getDao(MeasurementDao.class);
		StringBuilder sb = new StringBuilder();
		List<Measurement> list = measurementDao.findAllOrderBySequence(product.getProductId(),LineSideContainerTag.CON_ROD_CAPS, true);
		if (list == null || list.isEmpty()) {
			return sb.toString();
		}
		for (Measurement m : list) {
			if (m == null) {
				continue;
			}
			String value = String.valueOf(Double.valueOf(m.getMeasurementValue()).intValue());
			sb.append(StringUtils.trim(value));
		}
		return sb.toString();
	}
	
	public String deriveCrankMainMeasurements(String crankSn) throws TaskException { //JournalCrank

		String crankSnTypeKey = parseCrankSnTypeKey(crankSn);
		int crankMainStartIx = getCrankMeasurementsStartIx(crankSnTypeKey, property.getCrankMainMeasurementsStartIx(Integer.class), CRANK_MAIN_MEASUREMENTS_START_IX);
		
		int crankMainEndIx = crankMainStartIx + property.getMainBearingCount();
		
		if (crankMainStartIx < 0 || crankMainEndIx > crankSn.length()) {
			String msg = "Could not calculate Crank Ranking from Crank SN, please check configuration for CRANK_LENGTH, CRANKSHAFT_JOURNAL_POSITION";
			throw new TaskException(msg); 
		} else {
			return crankSn.substring(crankMainStartIx, crankMainEndIx);
		}
	}
	
	public String deriveCrankConrodMeasurements(String crankSn) throws TaskException {//ConrodCrank

		String crankSnTypeKey = parseCrankSnTypeKey(crankSn);
		int crankConrodStartIx = getCrankMeasurementsStartIx(crankSnTypeKey, property.getCrankConrodMeasurementsStartIx(Integer.class), CRANK_CONROD_MEASUREMENTS_START_IX);
		int crankConrodEndIx = crankConrodStartIx + property.getConrodCount();
		
		if (crankConrodStartIx < 0 || crankConrodEndIx > crankSn.length()) {
			String msg = "Could not calculate Crank Ranking from Crank SN, please check configuration for CRANK_LENGTH, CRANKSHAFT_CONROD_POSITION";
			throw new TaskException(msg); 
		} else {
			return crankSn.substring(crankConrodStartIx, crankConrodEndIx);
		}
	}
	
	public int getCrankMeasurementsStartIx(String crankSnTypeKey, Map<String, Integer> crankMeasurementsStartIxMap, int defaultIx) {
		if (crankMeasurementsStartIxMap == null) {
			return defaultIx;
		}
		if (StringUtils.isNotBlank(crankSnTypeKey)) {
			Integer value = crankMeasurementsStartIxMap.get(crankSnTypeKey);
			if (value != null) {
				return value;
			}
		}
		Integer value = crankMeasurementsStartIxMap.get("*");
		if (value != null) {
			return value;
		}
		return defaultIx;
	}
	
	public BearingSelectResult removeBearingSelectResult(String productId){
		BearingSelectResult result =  ServiceFactory.getDao(BearingSelectResultDao.class).findByProductId(productId);
		ServiceFactory.getDao(BearingSelectResultDao.class).remove(result);
		return result;
	}
	
	public BearingSelectResult saveBearingSelectResult(BearingSelectResult result){
		result.getId().setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		return  ServiceFactory.getDao(BearingSelectResultDao.class).save(result);
	}
	
	public void removeBockMeasurements(String productId){
		BearingSelectResult bearingSelectResult = removeBearingSelectResult(productId);
		if(bearingSelectResult == null) return;
		bearingSelectResult.setJournalBlockMeasurements(null); 
		for(int i=0; i < property.getMainBearingCount(); i++){
			setMainBearings(bearingSelectResult, null, i+1);
		}
		saveBearingSelectResult(bearingSelectResult);
	}
	
	public void removeConrodMeasurements(String productId){
		BearingSelectResult bearingSelectResult = removeBearingSelectResult(productId);
		bearingSelectResult.setConrodConsMeasurements(null);
		for(int i=0; i < property.getConrodCount(); i++){
			setConrodBearings(bearingSelectResult, null, i+1);
		}
		
		saveBearingSelectResult(bearingSelectResult);
		
		
	}
	
	public void removeCrankMainMeasurements(String productId){
		BearingSelectResult bearingSelectResult = removeBearingSelectResult(productId);
		bearingSelectResult.setJournalCrankMeasurements(null);
		bearingSelectResult.setConrodCrankMeasurements(null);
		
		if(bearingSelectResult == null) return;
		
		for(int i=0; i < property.getMainBearingCount(); i++){
			setMainBearings(bearingSelectResult, null, i+1);
		}
		
		for(int i=0; i < property.getConrodCount(); i++){
			setConrodBearings(bearingSelectResult, null, i+1);
		}
		
		saveBearingSelectResult(bearingSelectResult);
	}
	
	public void updateBockMeasurements(BaseProduct product, Block block){
		BearingSelectResult bearingSelectResult = removeBearingSelectResult(product.getProductId());
		bearingSelectResult.setJournalBlockMeasurements(selectBlockMeasurements(block, property.getMainBearingCount()));
		
		if(!StringUtils.isEmpty(bearingSelectResult.getJournalBlockMeasurements()) && 
				!StringUtils.isEmpty(bearingSelectResult.getJournalCrankMeasurements())) {
			assemblyMainBearing(bearingSelectResult, product.getProductSpecCode());
		}
		saveBearingSelectResult(bearingSelectResult);
	}
	
	public void updateConrodMeasurements(BaseProduct product){
		BearingSelectResult bearingSelectResult = removeBearingSelectResult(product.getProductId());
		bearingSelectResult.setConrodConsMeasurements(selectConrodMeasurements(product));
		
		if(!StringUtils.isEmpty(bearingSelectResult.getConrodCrankMeasurements()) && 
				!StringUtils.isEmpty(bearingSelectResult.getConrodConsMeasurements())){
			assemblyConrodBearing(bearingSelectResult, product.getProductSpecCode());
			
		}
		saveBearingSelectResult(bearingSelectResult);
		
	}
	
	public void updateCrankMainMeasurements(BaseProduct product, String crankSn){
		BearingSelectResult bearingSelectResult = removeBearingSelectResult(product.getProductId());
		bearingSelectResult.setJournalCrankMeasurements(deriveCrankMainMeasurements(crankSn));
		bearingSelectResult.setConrodCrankMeasurements(deriveCrankConrodMeasurements(crankSn));
		
		if(!StringUtils.isEmpty(bearingSelectResult.getJournalBlockMeasurements()) && 
				!StringUtils.isEmpty(bearingSelectResult.getJournalCrankMeasurements()))
			assemblyMainBearing(bearingSelectResult, product.getProductSpecCode());
		
		
		if(!StringUtils.isEmpty(bearingSelectResult.getConrodCrankMeasurements()) && 
				!StringUtils.isEmpty(bearingSelectResult.getConrodConsMeasurements()))
			assemblyConrodBearing(bearingSelectResult, product.getProductSpecCode());

		saveBearingSelectResult(bearingSelectResult);
	}
	
	
	public void setMainBearings(BearingSelectResult result, BearingMatrixCell bmc, int bearingIx) {
		if (result == null) {
			return;
		}
		
		String upper = (bmc == null? "" : bmc.getUpperBearing()); 
		String lower = (bmc == null? "" : bmc.getLowerBearing());
		
		switch (bearingIx) {
		case 1:
			result.setJournalUpperBearing01(upper);
			result.setJournalLowerBearing01(lower);
			break;
		case 2:
			result.setJournalUpperBearing02(upper);
			result.setJournalLowerBearing02(lower);
			break;
		case 3:
			result.setJournalUpperBearing03(upper);
			result.setJournalLowerBearing03(lower);
			break;
		case 4:
			result.setJournalUpperBearing04(upper);
			result.setJournalLowerBearing04(lower);
			break;
		case 5:
			result.setJournalUpperBearing05(upper);
			result.setJournalLowerBearing05(lower);
			break;
		case 6:
			result.setJournalUpperBearing06(upper);
			result.setJournalLowerBearing06(lower);
			break;
		}
	}
	
	public void setConrodBearings(BearingSelectResult result, BearingMatrixCell bmc, int bearingIx) {
		if (result == null) {
			return;
		}
		
		String upper = (bmc == null? "" : bmc.getUpperBearing()); 
		String lower = (bmc == null? "" : bmc.getLowerBearing());
		switch (bearingIx) {
		case 1:
			result.setConrodUpperBearing01(upper);
			result.setConrodLowerBearing01(lower);
			break;
		case 2:
			result.setConrodUpperBearing02(upper);
			result.setConrodLowerBearing02(lower);
			break;
		case 3:
			result.setConrodUpperBearing03(upper);
			result.setConrodLowerBearing03(lower);
			break;
		case 4:
			result.setConrodUpperBearing04(upper);
			result.setConrodLowerBearing04(lower);
			break;
		case 5:
			result.setConrodUpperBearing05(upper);
			result.setConrodLowerBearing05(lower);
			break;
		case 6:
			result.setConrodUpperBearing06(upper);
			result.setConrodLowerBearing06(lower);
			break;
		}
	}
	
	public void assemblyMainBearing(BearingSelectResult bsr, String specCode ){
		for(int i=0; i < property.getMainBearingCount(); i++){
			BearingMatrixCell mainMatrixCell = selectBearing(BearingType.Main, ProductSpec.extractModelYearCode(specCode), 
					ProductSpec.extractModelCode(specCode), ProductSpec.extractModelTypeCode(specCode), String.valueOf(i+1),
					bsr.getJournalBlockMeasurements().substring(i, i+1), bsr.getJournalCrankMeasurements().substring(i, i+1));
				
			setMainBearings(bsr, mainMatrixCell, i+1);
		}
	} 
	
	public void assemblyConrodBearing(BearingSelectResult bsr, String specCode ){
		for(int i=0; i < property.getConrodCount(); i++){
			BearingMatrixCell mainMatrixCell = selectBearing(BearingType.Conrod, ProductSpec.extractModelYearCode(specCode), 
					ProductSpec.extractModelCode(specCode), ProductSpec.extractModelTypeCode(specCode), String.valueOf(i+1),
					bsr.getConrodConsMeasurements().substring(i, i+1),bsr.getConrodCrankMeasurements().substring(i, i+1));
			setConrodBearings(bsr, mainMatrixCell, i+1);
		}
	} 
	
	public String parseCrankSnTypeKey(String crankSn) {
		if (StringUtils.isBlank(crankSn)) {
			return null;
		}
		int[] keyIx = StringUtil.toIntArray(property.getCrankSnTypeIx());
		if (keyIx == null || keyIx.length < 2) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; (i + 1) < keyIx.length; i = i + 2) {
			int startIx = keyIx[i];
			int length = keyIx[i + 1];
			if (startIx < 0 || length < 0 || (startIx + length) > crankSn.length()) {
				continue;
			}
			String token = crankSn.substring(startIx, startIx + length);
			sb.append(token);
		}
		if (sb.length() > 0) {
			return sb.toString();
		}
		return null;
	}
	
	public static BearingMatrixCell selectBearing(BearingType bearingType, String modelYearCode, String modelCode, String modelTypeCode, String journalPosition, String columnMeasurement, String rowMeasurement) {

		BearingMatrixCellDao dao = ServiceFactory.getDao(BearingMatrixCellDao.class);
		BearingMatrixCellId id = new BearingMatrixCellId();
		id.setModelYearCode(modelYearCode);
		id.setModelCode(modelCode);
		id.setBearingType(bearingType.name());
		id.setModelTypeCode(modelTypeCode);
		id.setJournalPosition(journalPosition);
		id.setColumnMeasurement(columnMeasurement);
		id.setRowMeasurement(rowMeasurement);

		//Y, M, T, Pos
		BearingMatrixCell matrixCell = dao.findByKey(id);
		if (matrixCell != null) {
			return matrixCell;
		}
		//Y, M, T, *
		id.setModelTypeCode(modelTypeCode);
		id.setJournalPosition("*");
		matrixCell = dao.findByKey(id);
		if (matrixCell != null) {
			return matrixCell;
		}
		//Y, M, *, Pos
		id.setModelTypeCode("*");
		id.setJournalPosition(journalPosition);
		matrixCell = dao.findByKey(id);
		if (matrixCell != null) {
			return matrixCell;
		}
		//Y, M, *, *
		id.setModelTypeCode("*");
		id.setJournalPosition("*");
		matrixCell = dao.findByKey(id);
		return matrixCell;
	}

	public BearingSelectPropertyBean getProperty() {
		return property;
	}


}
