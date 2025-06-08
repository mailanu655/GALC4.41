package com.honda.galc.client.product.process.engine.bearing.select.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.product.process.engine.bearing.model.BearingModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.BearingSelectResultDao;
import com.honda.galc.dao.product.BlockBuildResultDao;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.LineSideContainerTag;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductNumberDef.NumberType;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.bearing.BearingMatrixCell;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BearingSelectResult;
import com.honda.galc.entity.product.BearingSelectResultId;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.BearingSelectPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingSelectModel</code> is ... .
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
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
public class BearingSelectModel extends BearingModel {

	private static final String DATE_TOKEN_PATTERN = "[0-9][0-9ABC][0-9][0-9]";
	private static final String LINE_TOKEN_PATTERN = "[0-9]";

	private Block block;

	private List<LotControlRule> rules;

	public BearingSelectModel(ApplicationContext applicationContext) {
		super(applicationContext);
		this.rules = new ArrayList<LotControlRule>();
		setProperty(PropertyService.getPropertyBean(BearingSelectPropertyBean.class, getApplicationContext().getProcessPointId()));
		init();
	}

	// === prepare model === //
	protected void init() {
		List<LotControlRule> rules = ServiceFactory.getDao(LotControlRuleDao.class).findAllByProcessPoint(getApplicationContext().getProcessPointId());
		if (rules != null) {
			getRules().addAll(rules);
		}
	}

	// === business api === //
	public List<String> installBlock(Block block, String userId) {

		List<String> messages = new ArrayList<String>();

		String blockPartName = getProperty().getInstalledBlockPartName();
		NumberType numberType = getProperty().getInstalledBlockPartNameSnType();

		InstalledPart installedPartBlock = new InstalledPart(getProduct().getProductId(), blockPartName);
		installedPartBlock.setPartSerialNumber(block.getSerialNumber(numberType));
		installedPartBlock.setInstalledPartStatus(InstalledPartStatus.OK);
		installedPartBlock.setAssociateNo(userId);

		BlockDao blockDao = ServiceFactory.getDao(BlockDao.class);
		block.setEngineSerialNumber(getProduct().getProductId());
		block = blockDao.save(block, installedPartBlock);
		setBlock(block);

		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);

		// === 185 migrate parts from mcb to ein
		try {
			installedPartDao.updateProductId(getProduct().getProductId(), block.getMcSerialNumber());
			try {
				MeasurementDao measurementDao = ServiceFactory.getDao(MeasurementDao.class);
				// === 198 migrate measurements from mcb to ein
				measurementDao.updateProductId(getProduct().getProductId(), block.getMcSerialNumber());
			} catch (Exception e2) {
				String msg2 = String.format("Failed to migrate measurements (198) from MCB %s to EIN %s ", block.getMcSerialNumber(), getProduct().getProductId());
				Logger.getLogger(getClass().getName()).warn(msg2);
				messages.add(msg2);
			}
		} catch (Exception e) {
			String msg = String.format("Failed to migrate installed parts (185) and measurements (198) from MCB %s  to EIN %s", block.getMcSerialNumber(), getProduct().getProductId());
			Logger.getLogger(getClass().getName()).warn(msg);
			messages.add(msg);
		}
		return messages;
	}

	public Map<Integer, String> selectBlockMeasurements() {
		Map<Integer, String> measurements = new LinkedHashMap<Integer, String>();
		Block block = getBlock();
		if (block == null) {
			return measurements;
		}
		BlockBuildResultDao blockBuildResultDao = ServiceFactory.getDao(BlockBuildResultDao.class);
		int mainBearingCount = getMainBearingCount();

		for (int i = 1; i <= mainBearingCount; i++) {
			String partName = String.format("%s %s", LineSideContainerTag.PART_CRANK_JOURNAL_PREFIX, i);
			BlockBuildResult blockBuildResult = blockBuildResultDao.findById(block.getBlockId(), partName);
			if (blockBuildResult != null) {
				measurements.put(i, blockBuildResult.getResultValue());
			}
		}
		return measurements;
	}

	public Map<Integer, String> selectConrodMeasurements(BaseProduct product) {
		Map<Integer, String> measurements = new LinkedHashMap<Integer, String>();
		MeasurementDao measurementDao = ServiceFactory.getDao(MeasurementDao.class);
		List<Measurement> list = measurementDao.findAll(product.getProductId(), "CON-ROD-CAPS");
		if (list == null || list.isEmpty()) {
			return measurements;
		}
		for (Measurement m : list) {
			if (m == null) {
				continue;
			}
			String value = String.valueOf(Double.valueOf(m.getMeasurementValue()).intValue());
			measurements.put(m.getId().getMeasurementSequenceNumber(), value);
		}
		return measurements;
	}

	public boolean isBearingSlectResultExists(BaseProduct product) {
		BearingSelectResultDao bsDao = ServiceFactory.getDao(BearingSelectResultDao.class);
		List<BearingSelectResult> list = bsDao.findAllByProductId(product.getProductId());
		if (list != null && !list.isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isNotProcessable(BaseProduct product) {
		// TODO implement logic
		return false;
	}

	// === supporting api === //
	public List<InstalledPart> assembleConrodSnParts(String productId, String userId, Map<Integer, String> indexedPartSerialNumbers) {

		List<InstalledPart> parts = new ArrayList<InstalledPart>();
		for (Integer ix : indexedPartSerialNumbers.keySet()) {
			String partName = String.format(LineSideContainerTag.CONROD_SERIAL_NUMBER_X, ix);
			String psn = indexedPartSerialNumbers.get(ix);
			InstalledPart ip = assembleInstalledPart(productId, partName, psn, userId);
			parts.add(ip);
		}
		return parts;
	}

	public BearingSelectResult assembleBearingSelectResult(String modelYearCode, String modelCode, Map<Integer, BearingMatrixCell> mainBearings, Map<Integer, BearingMatrixCell> conrodBearings) {

		BearingSelectResult result = new BearingSelectResult();

		BearingSelectResultId id = new BearingSelectResultId();
		id.setProductId(getProduct().getId());
		id.setActualTimestamp(new Timestamp(System.currentTimeMillis()));

		result.setId(id);
		result.setModelYearCode(modelYearCode);
		result.setModelCode(modelCode);

		for (Integer ix : mainBearings.keySet()) {
			BearingMatrixCell bmc = mainBearings.get(ix);
			setMainBearings(result, bmc, ix);
		}

		for (Integer ix : conrodBearings.keySet()) {
			BearingMatrixCell bmc = conrodBearings.get(ix);
			setConrodBearings(result, bmc, ix);
		}

		return result;
	}

	public BearingSelectResult assembleBearingSelectResult(BearingSelectResult result, String blockMeasurements, String crankMainMeasurements, String crankConrodMeasurements, String conrodMeasurements) {

		result.setJournalBlockMeasurements(blockMeasurements);
		result.setJournalCrankMeasurements(crankMainMeasurements);
		result.setConrodCrankMeasurements(crankConrodMeasurements);
		result.setConrodConsMeasurements(conrodMeasurements);

		return result;
	}


	public InstalledPart assembleInstalledPart(String productId, String partName, String psn, String userId) {
		InstalledPart part = new InstalledPart(productId, partName);
		part.setPartSerialNumber(psn);
		part.setInstalledPartStatus(InstalledPartStatus.OK);
		part.setAssociateNo(userId);
		return part;
	}

	public void setMainBearings(BearingSelectResult result, BearingMatrixCell bmc, int bearingIx) {
		if (result == null || bmc == null) {
			return;
		}
		switch (bearingIx) {
		case 1:
			result.setJournalUpperBearing01(bmc.getUpperBearing());
			result.setJournalLowerBearing01(bmc.getLowerBearing());
			break;
		case 2:
			result.setJournalUpperBearing02(bmc.getUpperBearing());
			result.setJournalLowerBearing02(bmc.getLowerBearing());
			break;
		case 3:
			result.setJournalUpperBearing03(bmc.getUpperBearing());
			result.setJournalLowerBearing03(bmc.getLowerBearing());
			break;
		case 4:
			result.setJournalUpperBearing04(bmc.getUpperBearing());
			result.setJournalLowerBearing04(bmc.getLowerBearing());
			break;
		case 5:
			result.setJournalUpperBearing05(bmc.getUpperBearing());
			result.setJournalLowerBearing05(bmc.getLowerBearing());
			break;
		case 6:
			result.setJournalUpperBearing06(bmc.getUpperBearing());
			result.setJournalLowerBearing06(bmc.getLowerBearing());
			break;
		}
	}

	public void setConrodBearings(BearingSelectResult result, BearingMatrixCell bmc, int bearingIx) {
		if (result == null || bmc == null) {
			return;
		}
		switch (bearingIx) {
		case 1:
			result.setConrodUpperBearing01(bmc.getUpperBearing());
			result.setConrodLowerBearing01(bmc.getLowerBearing());
			break;
		case 2:
			result.setConrodUpperBearing02(bmc.getUpperBearing());
			result.setConrodLowerBearing02(bmc.getLowerBearing());
			break;
		case 3:
			result.setConrodUpperBearing03(bmc.getUpperBearing());
			result.setConrodLowerBearing03(bmc.getLowerBearing());
			break;
		case 4:
			result.setConrodUpperBearing04(bmc.getUpperBearing());
			result.setConrodLowerBearing04(bmc.getLowerBearing());
			break;
		case 5:
			result.setConrodUpperBearing05(bmc.getUpperBearing());
			result.setConrodLowerBearing05(bmc.getLowerBearing());
			break;
		case 6:
			result.setConrodUpperBearing06(bmc.getUpperBearing());
			result.setConrodLowerBearing06(bmc.getLowerBearing());
			break;
		}
	}

	// === derived config === //
	public List<Integer> getBlockMainIxDisplaySequence() {
		List<Integer> list = getMainBearingIxSequence();
		if (getProperty().isBlockMeasurementsDisplayReversed()) {
			Collections.reverse(list);
		}
		return list;
	}

	public List<Integer> getCrankMainIxDisplaySequence() {
		List<Integer> list = getMainBearingIxSequence();
		if (getProperty().isCrankMeasurementsDisplayReversed()) {
			Collections.reverse(list);
		}
		return list;
	}

	public List<Integer> getCrankConrodIxDisplaySequence() {
		List<Integer> list = getConrodIxSequence();
		if (getProperty().isCrankMeasurementsDisplayReversed()) {
			Collections.reverse(list);
		}
		return list;
	}

	public List<Integer> getConrodIxDisplaySequence() {
		List<Integer> list = getConrodIxSequence();
		if (getProperty().isConrodMeasurementsDisplayReversed()) {
			Collections.reverse(list);
		}
		return list;
	}

	public ProductTypeData getBlockDataType() {
		return getDao(ProductTypeDao.class).findByKey(ProductType.BLOCK.getProductName());
	}

	public List<Integer> getBlockInputNumberLengths() {
		List<ProductTypeData> types = getApplicationContext().getProductTypeDataList();
		List<Integer> defaultList = new ArrayList<Integer>();
		defaultList.add(ProductNumberDef.MCB.getLength());
		ProductTypeData type = null;
		if (types == null) {
			return defaultList;
		}
		for (ProductTypeData pdt : types) {
			if (ProductType.BLOCK.name().equals(pdt.getId())) {
				type = pdt;
			}
		}
		if (type == null) {
			return defaultList;
		}
		List<ProductNumberDef> defs = type.getProductNumberDefs();
		if (defs == null || defs.isEmpty()) {
			return defaultList;
		}

		List<Integer> list = new ArrayList<Integer>();
		for (ProductNumberDef def : defs) {
			list.add(def.getLength());
		}
		if (list.isEmpty()) {
			return defaultList;
		}
		Collections.sort(list);
		return list;
	}

	public Integer getBlockInputNumberMaxLength() {
		List<Integer> lengths = getBlockInputNumberLengths();
		if (lengths.size() == 0) {
			return ProductNumberDef.MCB.getLength();
		}
		if (lengths.size() == 1) {
			return lengths.get(0);
		}
		Collections.sort(lengths);
		Collections.reverse(lengths);
		return lengths.get(0);
	}

	public boolean isMcbEditButtonEnabled() {
		// REMARK: always false,
		// it is enabled only for recovery, not for simple edit.
		return false;
	}

	// === get/set === //
	@Override
	public Engine getProduct() {
		return (Engine) super.getProduct();
	}

	public BearingSelectPropertyBean getProperty() {
		return (BearingSelectPropertyBean) super.getProperty();
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public String getDateTokenPattern() {
		return DATE_TOKEN_PATTERN;
	}

	public String getLineTokenPattern() {
		return LINE_TOKEN_PATTERN;
	}

	public List<LotControlRule> getRules() {
		return rules;
	}
}
