package com.honda.galc.service.datacollection.task;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.BearingSelectResultDao;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.bearing.BearingMatrixCellDao;
import com.honda.galc.dao.product.bearing.BearingMatrixDao;
import com.honda.galc.dao.product.bearing.BearingPartDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.bearing.BearingMatrix;
import com.honda.galc.entity.bearing.BearingMatrixCell;
import com.honda.galc.entity.bearing.BearingMatrixCellId;
import com.honda.galc.entity.bearing.BearingMatrixId;
import com.honda.galc.entity.bearing.BearingPart;
import com.honda.galc.entity.bearing.BearingRankType;
import com.honda.galc.entity.bearing.BearingType;
import com.honda.galc.entity.product.BearingSelectResult;
import com.honda.galc.entity.product.BearingSelectResultId;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;

public class BearingSelectTask extends CollectorTask {

	HashMap<String, BearingMatrixCell> bmList = new HashMap<String, BearingMatrixCell>();
	BearingMatrix bm = new BearingMatrix();
	BearingMatrixId bmId = new BearingMatrixId();
	BearingMatrixDao bmDao = ServiceFactory.getDao(BearingMatrixDao.class);
	ProductType ipType;
	DiecastDao installedProductDao;
	DieCast installedProduct;

	public BearingSelectTask(HeadlessDataCollectionContext context,
			String processPointId) {
		super(context, processPointId);
		this.context = context;
	}

	@Override
	public void execute() {
		collectRanks();

	}

	private void collectRanks() {
		boolean useAbbreviation = PropertyService.getPropertyBoolean("prop_BearingSelect","USE_ABBREVIATIONS", true);
		String ein = context.get(TagNames.PRODUCT_ID.name()).toString();
		
		EngineDao engineDao = ServiceFactory.getDao(EngineDao.class);
		Engine engineID = engineDao.findByKey(ein);
		
		String modelYearCode = ProductSpec.extractModelYearCode(engineID.getProductSpecCode());
		String modelCode = ProductSpec.extractModelCode(engineID.getProductSpecCode());
		String modelTypeCode = ProductSpec.extractModelTypeCode(engineID.getProductSpecCode());

		BearingSelectResultDao bsrDao = ServiceFactory.getDao(BearingSelectResultDao.class);
		BearingSelectResult bsr = bsrDao.findByProductId(ein);

		BearingSelectResultId bsrId = new BearingSelectResultId();

		if (bsr == null) {
			bsr = new BearingSelectResult();
			bsrId.setProductId(ein);
			bsrId.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
			bsr.setId(bsrId);
			bsr.setModelYearCode(modelYearCode);
			bsr.setModelCode(modelCode);
		} else {
			bsrId = bsr.getId();
		}
		
		//Set property if ranks are to be retrived from serial number.
		ipType = ProductType.getType(PropertyService.getProperty(processPointId, TagNames.INSTALLED_PRODUCT.name(), ""));
		if (null != ipType) {
			if (ipType.getProductName().equals(ProductType.CRANKSHAFT.getProductName())) {
				for(ProductBuildResult buildResult : context.getBuildResults()) {
					String productId = buildResult.getPartSerialNumber();
					List<ProductNumberDef> productNumberDefs = ProductNumberDef.getProductNumberDef(ipType);
					for (ProductNumberDef def : productNumberDefs) {
						if (def.isNumberValid(productId)) {
							context.put(BearingRankType.Main_Crank.name(), def.getJournalRank(productId));
							context.put(BearingRankType.Conrod_Crank.name(), def.getPinRank(productId));
						}
					}
				}
			} else if (ipType.getProductName().equals(ProductType.CONROD.getProductName())){
				//Order of conrods in lot control rules, determines order of ranks.
				//If all conrods are not being sent at same time, further work will need to be done.
				String conrodRanks = "";
				for(ProductBuildResult buildResult : context.getBuildResults()) {
					String productId = buildResult.getPartSerialNumber();
					List<ProductNumberDef> productNumberDefs = ProductNumberDef.getProductNumberDef(ipType);
					for (ProductNumberDef def : productNumberDefs) {
						if (def.isNumberValid(productId)) {
							conrodRanks += def.getRank(productId);
						}
					}
				}
				context.put(BearingRankType.Conrod_Cons.name(),conrodRanks);
			}
		}
		
		if (doesContainRankData(BearingType.Main+"_Measurements")) parseData(context.get(BearingType.Main+"_Measurements").toString(), BearingRankType.Main_Block.name(), BearingRankType.Main_Crank.name());
		if (doesContainRankData(BearingType.Conrod+"_Measurements")) parseData(context.get(BearingType.Conrod+"_Measurements").toString(), BearingRankType.Conrod_Cons.name(), BearingRankType.Conrod_Crank.name());

		if (doesContainRankData(BearingRankType.Main_Block.name())) bsr.setJournalBlockMeasurements(context.get(BearingRankType.Main_Block.name()).toString());
		if (doesContainRankData(BearingRankType.Main_Crank.name())) bsr.setJournalCrankMeasurements(context.get(BearingRankType.Main_Crank.name()).toString());
		if (doesContainRankData(BearingRankType.Conrod_Crank.name())) bsr.setConrodCrankMeasurements(context.get(BearingRankType.Conrod_Crank.name()).toString());
		if (doesContainRankData(BearingRankType.Conrod_Cons.name())) bsr.setConrodConsMeasurements(context.get(BearingRankType.Conrod_Cons.name()).toString());

				
		getLogger().debug("BEFORE: " + bsr.toString());
		
		bmId.setModelYearCode(modelYearCode);
		bmId.setModelCode(modelCode);

		bm = bmDao.findByKey(bmId);
		
		if (bm == null) {
			getLogger().error("Bearing Matrix Info not setup for this product spec code. ("+ bmId.toString()+ ")" );
			bsrDao.save(bsr);
			throw new TaskException("Bearing Matrix Info not setup.");
		}
		
		BearingPartDao bpDao = ServiceFactory.getDao(BearingPartDao.class);
		BearingPart bpUpper = new BearingPart();
		BearingPart bpLower = new BearingPart();

		if (bsr.getJournalBlockMeasurements() != null && bsr.getJournalBlockMeasurements().length() == bm.getNumberOfMainBearings() && bsr.getJournalCrankMeasurements() != null &&	bsr.getJournalCrankMeasurements().length() == bm.getNumberOfMainBearings()) {
			for (int i=1; i <= bm.getNumberOfMainBearings(); i++) {
				BearingMatrixCell mainMatrixCell = selectBearings(BearingType.Main, modelYearCode, modelCode, modelTypeCode, String.valueOf(i), bsr.getJournalBlockMeasurements().substring(i-1, i), bsr.getJournalCrankMeasurements().substring(i-1, i));				
				bpUpper = bpDao.findByKey(mainMatrixCell.getUpperBearing());
				bpLower = bpDao.findByKey(mainMatrixCell.getLowerBearing());
				context.put(BearingType.Main+"_Rank_"+Integer.toString(i), bsr.getJournalBlockMeasurements().substring(i-1, i)+ bsr.getJournalCrankMeasurements().substring(i-1, i));				
				if (useAbbreviation) {
					context.put(BearingType.Main+"_Colors_"+Integer.toString(i), 
							(PropertyService.getProperty("prop_BearingSelect", bpUpper.getColor(), "--") + " " +
									PropertyService.getProperty("prop_BearingSelect", bpLower.getColor(), "--")));
				} else {
					context.put(BearingType.Main+"_Colors_"+Integer.toString(i), (bpUpper.getColor() + " " + bpLower.getColor()));
				}
				
				switch(i) {
					case 1:{
						bsr.setJournalUpperBearing01(bpUpper.getId());
						bsr.setJournalLowerBearing01(bpLower.getId());
						break;
					}
					case 2:{
						bsr.setJournalUpperBearing02(bpUpper.getId());
						bsr.setJournalLowerBearing02(bpLower.getId());
						break;
					}
					case 3:{
						bsr.setJournalUpperBearing03(bpUpper.getId());
						bsr.setJournalLowerBearing03(bpLower.getId());
						break;
					}
					case 4:{
						bsr.setJournalUpperBearing04(bpUpper.getId());
						bsr.setJournalLowerBearing04(bpLower.getId());
						break;
					}
					case 5:{
						bsr.setJournalUpperBearing05(bpUpper.getId());
						bsr.setJournalLowerBearing05(bpLower.getId());
						break;
					}
					case 6:{
						bsr.setJournalUpperBearing06(bpUpper.getId());
						bsr.setJournalLowerBearing06(bpLower.getId());
						break;
					}
				}
			}
		}
		
		if (bsr.getConrodCrankMeasurements() != null && bsr.getConrodCrankMeasurements().length() == bm.getNumberOfConrods() && bsr.getConrodConsMeasurements() != null && bsr.getConrodConsMeasurements().length() == bm.getNumberOfConrods()) {
			for (int i=1; i <= bm.getNumberOfConrods(); i++) {
				BearingMatrixCell conrodMatrixCell = selectBearings(BearingType.Conrod, modelYearCode, modelCode, modelTypeCode, "*", bsr.getConrodCrankMeasurements().substring(i-1, i), bsr.getConrodConsMeasurements().substring(i-1, i));
				bpUpper = bpDao.findByKey(conrodMatrixCell.getUpperBearing());
				bpLower = bpDao.findByKey(conrodMatrixCell.getLowerBearing());
				context.put(BearingType.Conrod+"_Rank_"+Integer.toString(i), bsr.getConrodCrankMeasurements().substring(i-1, i)+ bsr.getConrodConsMeasurements().substring(i-1, i));
				if (useAbbreviation) {
					context.put(BearingType.Conrod+"_Colors_"+Integer.toString(i), 
							(PropertyService.getProperty("prop_BearingSelect", bpUpper.getColor(), "--") + " " +
									PropertyService.getProperty("prop_BearingSelect", bpLower.getColor(), "--")));
				} else {
					context.put(BearingType.Conrod+"_Colors_"+Integer.toString(i), (bpUpper.getColor() + " " + bpLower.getColor()));
				}
				
				switch(i) {
					case 1:{
						bsr.setConrodUpperBearing01(bpUpper.getId());
						bsr.setConrodLowerBearing01(bpLower.getId());
						break;
					}
					case 2:{
						bsr.setConrodUpperBearing02(bpUpper.getId());
						bsr.setConrodLowerBearing02(bpLower.getId());
						break;
					}
					case 3:{
						bsr.setConrodUpperBearing03(bpUpper.getId());
						bsr.setConrodLowerBearing03(bpLower.getId());
						break;
					}
					case 4:{
						bsr.setConrodUpperBearing04(bpUpper.getId());
						bsr.setConrodLowerBearing04(bpLower.getId());
						break;
					}
					case 5:{
						bsr.setConrodUpperBearing05(bpUpper.getId());
						bsr.setConrodLowerBearing05(bpLower.getId());
						break;
					}
					case 6:{
						bsr.setConrodUpperBearing06(bpUpper.getId());
						bsr.setConrodLowerBearing06(bpLower.getId());
						break;
					}
				}
			}
		}
		
		getLogger().info("AFTER Data Collection: " + bsr.toString());
		bsrDao.save(bsr);
	}
	
	
	private boolean doesContainRankData(String rankName) {
		if (context.containsKey(rankName) &&  context.get(rankName)!= null &&  !context.get(rankName).toString().equals("")) {
			getLogger().info(rankName + " Ranks: " + context.get(rankName).toString());
			return true;
		} else {
			return false;
		}
	}

	private void parseData(String measurements, String tag1, String tag2) {
		String string1 = "";
		String string2 = "";
		
		for (int i = 0; i < measurements.length(); i++) {
			if (i%2 == 1) {
				string2 += measurements.substring(i, i+1);
			} else {
				string1 += measurements.substring(i, i+1);
			}
		}
		
		context.put(tag1, string1);
		context.put(tag2, string2);
	}

	public BearingMatrixCell selectBearings(BearingType bearingType, String modelYearCode, String modelCode, String modelTypeCode, String position, String columnMeasurement, String rowMeasurement) {

		BearingMatrixCellDao dao = ServiceFactory.getDao(BearingMatrixCellDao.class);
		BearingMatrixCellId id = new BearingMatrixCellId();
		id.setModelYearCode(modelYearCode);
		id.setModelCode(modelCode);
		id.setModelTypeCode(modelTypeCode);
		id.setJournalPosition("*");
		id.setBearingType(bearingType.name());

		//Order in which to search for bearing colors
		//Y, M, T, Pos
		//Y, M, T, *
		//Y, M, *, Pos
		//Y, M, *, *

		BearingMatrixCell matrixCell = new BearingMatrixCell();
			for (int i=0; i < 4; i++) {
				switch(i) {
					case 0: {
						id.setModelTypeCode(modelTypeCode);
						id.setJournalPosition(String.valueOf(position));
						break;
					}
					case 1: {
						id.setModelTypeCode(modelTypeCode);
						id.setJournalPosition("*");
						break;
					}
					case 2: {
						id.setModelTypeCode("*");
						id.setJournalPosition(String.valueOf(position));
						break;
					}
					case 3: default: {
						id.setModelTypeCode("*");
						id.setJournalPosition("*");
						break;
					}
				}
				
				if (StringUtil.isNumeric(rowMeasurement) && bearingType.equals(BearingType.Main)) {
					id.setRowMeasurement(rowMeasurement);
					id.setColumnMeasurement(columnMeasurement);
				} else {
					id.setRowMeasurement(columnMeasurement);
					id.setColumnMeasurement(rowMeasurement);
				}

				matrixCell = dao.findByKey(id);
				if (matrixCell == null)
					continue;
				else {
					getLogger().info("Parameters: " + modelYearCode+ modelCode+ modelTypeCode+ " " + bearingType + position +
							" Measurements: " + id.getColumnMeasurement() + id.getRowMeasurement() + " Bearing selected: " + matrixCell.toString());
					return matrixCell;
				}
			}
		if (matrixCell == null) {
			getLogger().error("Bearing Matrix not setup for this product spec code. ("+ modelYearCode+ modelCode+ modelTypeCode+ ")" );
			throw new TaskException("Bearing Matrix not setup.");
		}
		return matrixCell;
	}
}
