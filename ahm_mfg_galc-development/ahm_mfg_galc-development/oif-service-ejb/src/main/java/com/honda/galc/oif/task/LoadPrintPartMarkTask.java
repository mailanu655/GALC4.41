package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.oif.BomDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.PartByProductSpecCodeDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.entity.fif.Bom;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.PartByProductSpecCode;
import com.honda.galc.entity.product.PartByProductSpecCodeId;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;

public class LoadPrintPartMarkTask extends OifAbstractTask implements IEventTaskExecutable {

	private static final String PROCESS_POINT = "PROCESS_POINT";
	private static final String PLANT_CODE = "PLANT_CODE";
	protected OifErrorsCollector errorsCollector;

	public LoadPrintPartMarkTask(String name) {
		super(name);
		errorsCollector = new OifErrorsCollector(name);
	}

	public void execute(Object[] args) {
		try {
			String processPoint = getProperty(PROCESS_POINT, "");
			String plantCode = getProperty(PLANT_CODE, "");

			if (StringUtils.isEmpty(processPoint))
				errorsCollector.error("No Process Point Defined");
			if (StringUtils.isEmpty(plantCode))
				errorsCollector.error("No Plant Code Defined");
			if (errorsCollector.getErrorList().size() > 0)
				return;

			getLogger().info("Started processing Load Print Part Mark Task for ProcessPoint - " + processPoint
					+ " and Plant Code - " + plantCode);

			loadPrintPartMarkTask(processPoint, plantCode);

			getLogger().info("Finished processing Load Print Part Mark Task");
		} catch (Exception e) {
			getLogger().info(
					"Unexpected Exception Occurred  while running the Load Print Part Mark Task :" + e.getMessage());
			e.printStackTrace();
			errorsCollector.emergency(e, "Unexpected exception occured");
		} finally {
			// errorsCollector.error("test error notification ");
			errorsCollector.sendEmail();
		}

	}

	private void loadPrintPartMarkTask(String processPoint, String plantCode) {

		PartSpecDao partSpecDao = getDao(PartSpecDao.class);
		LotControlRuleDao lotControlRuleDao = getDao(LotControlRuleDao.class);
		PartByProductSpecCodeDao partByProductSpecCodeDao = getDao(PartByProductSpecCodeDao.class);
		BomDao bomDao = getDao(BomDao.class);
		Map<PartSpec, List<LotControlRule>> rulesToBeDeletedByPartSpec = new HashMap<PartSpec, List<LotControlRule>>();
		Map<PartSpec, List<PartByProductSpecCode>> partByProductSpecCodesToBeDeletedByPartSpec = new HashMap<PartSpec, List<PartByProductSpecCode>>();
		Map<PartSpec, List<Bom>> bomListByPartSpec = new HashMap<PartSpec, List<Bom>>();
		List<LotControlRule> rulesForProcessPoint = new ArrayList<LotControlRule>();
		List<PartByProductSpecCode> partByProductSpecCodesForProcessPoint = new ArrayList<PartByProductSpecCode>();

		List<PartSpec> parts = partSpecDao.findAllPartsWithPartNumberAndPartMark();
		if (parts.isEmpty())
			getLogger().info("No Parts With PartNumber And PartMark");

		// Loop through all of the parts with a part number and part mark or
		// image
		for (PartSpec part : parts) {
			try {
				String partName = part.getId().getPartName();
				String partId = part.getId().getPartId();
				String partNumber = part.getPartNumber();

				List<Bom> validModelBomList = bomDao.findAllValidModels(plantCode, partNumber);
				if (validModelBomList.isEmpty())
					getLogger().info("No Valid Models found for Plant - " + plantCode + " partNumber - " + partNumber);

				List<LotControlRule> rules = lotControlRuleDao.findAllByPartName(partName);
				if (rules.isEmpty())
					getLogger().info("No Rules Defined  for - " + partName);

				ArrayList<LotControlRule> rulesToBeDeleted = new ArrayList<LotControlRule>();
				ArrayList<PartByProductSpecCode> partByProductSpecCodesToBeDeleted = new ArrayList<PartByProductSpecCode>();

				// Loop through all lot control rules for the part (GAL246TBX)
				for (LotControlRule rule : rules) {

					List<PartByProductSpecCode> partsByProductSpec = rule.getPartByProductSpecs();
					// Loop through all of the rules for the product spec
					// (GAL245TBX)
					for (PartByProductSpecCode partByProductSpec : partsByProductSpec) {

						List<LotControlRule> rls = lotControlRuleDao.findRuleByMtocAndPartName(partByProductSpec,
								partName);
						if (rls.isEmpty())
							getLogger().info("No Rules Defined  for PartName - " + partName + " and mtoc - "
									+ partByProductSpec);

						boolean canDeletePart = true;
						for (LotControlRule rl : rls) {
							if (rl.getId().getProcessPointId().trim().equalsIgnoreCase(processPoint)) {
								// Remove lot control rule from virtual process
								// point
								getLogger().info("adding LotControl Rule - " + rl.getId().getProcessPointId()
										+ " install Part -" + partName + " for Mtoc -"
										+ partByProductSpec.getId().getProductSpecCode() + "to rulesToBedeleted List ");
								if (!rulesForProcessPoint.contains(rl)) {
									rulesToBeDeleted.add(rl);
									rulesForProcessPoint.add(rl);
								}
							} else {
								// Part spec is being used by another process
								// point
								// cannot delete
								getLogger().info(" Cannot Delete LotControl Rule - " + rl.getId().getProcessPointId()
										+ " install Part -" + partName + " for Mtoc -"
										+ partByProductSpec.getId().getProductSpecCode());
								canDeletePart = false;
							}
						}

						if (canDeletePart) {
							
							// No other process points are using this part spec,
							// can
							// delete and clean up
							if (!partByProductSpecCodesForProcessPoint.contains(partByProductSpec)) {
								getLogger().info("adding to partByProductSpecCodesToBeDeleted list :Part - " + partName
										+ " for Mtoc -" + partByProductSpec.getId().getProductSpecCode());
								partByProductSpecCodesToBeDeleted.add(partByProductSpec);
								partByProductSpecCodesForProcessPoint.add(partByProductSpec);
							}
						}
					}
				}
				if (rulesToBeDeleted.size() > 0)
					rulesToBeDeletedByPartSpec.put(part, rulesToBeDeleted);
				if (partByProductSpecCodesToBeDeleted.size() > 0)
					partByProductSpecCodesToBeDeletedByPartSpec.put(part, partByProductSpecCodesToBeDeleted);

				ArrayList<Bom> bomList = new ArrayList<Bom>();

				for (Bom bom : validModelBomList) {
					String bomModelCode = bom.getId().getMtcModel();
					if (bomModelCode != null
							&& partId.substring(0, 1).equalsIgnoreCase(bomModelCode.substring(0, 1).trim())
							&& partId.substring(1, 2).equalsIgnoreCase("~")) {
						bomList.add(bom);
					}
				}

				bomListByPartSpec.put(part, bomList);
			} catch (Exception e) {
				getLogger().error("Unexpected Exception Occurred  while running the Load Print Part Mark Task :"
						+ e.getMessage());
				e.printStackTrace();
				errorsCollector.emergency(e, "Unexpected exception occured");
			}
		}
		int sequence = 1;
		String partName = "";
		for (PartSpec part : parts) {
			try {
				if (StringUtils.isEmpty(partName))
					partName = part.getId().getPartName();

				if (!part.getId().getPartName().equalsIgnoreCase(partName)) {
					partName = part.getId().getPartName();
					sequence++;
				}

				List<LotControlRule> rules = rulesToBeDeletedByPartSpec.get(part);
				if (rules != null && rules.size() > 0) {
					for (LotControlRule rule : rules) {
						getLogger().info("deleting LotControl Rule - " + rule.getId().getProcessPointId()
								+ " install Part -" + rule.getPartName().getPartName());
						lotControlRuleDao.remove(rule);
					}
				}
				List<PartByProductSpecCode> partByProductSpecCodes = partByProductSpecCodesToBeDeletedByPartSpec
						.get(part);
				if (partByProductSpecCodes != null && partByProductSpecCodes.size() > 0) {
					for (PartByProductSpecCode partByProductSpecCode : partByProductSpecCodes) {

						getLogger().info("Deleting Part - " + partByProductSpecCode.getId().getPartName()
								+ " for Mtoc -" + partByProductSpecCode.getId().getProductSpecCode());
						partByProductSpecCodeDao.remove(partByProductSpecCode);

					}
				}

				List<Bom> bomList = bomListByPartSpec.get(part);
				if (bomList != null && bomList.size() > 0) {
					for (Bom bom : bomList) {
						createLotControlRule(bom, part, processPoint, sequence);
					}
				}

			} catch (Exception e) {
				getLogger().error("Unexpected Exception Occurred  while running the Load Print Part Mark Task :"
						+ e.getMessage());
				e.printStackTrace();
				errorsCollector.emergency(e, "Unexpected exception occured");
			}

		}
	}

	private Logger getLogger() {
		return Logger.getLogger(componentId);
	}

	private void createLotControlRule(Bom bom, PartSpec part, String processPoint, int sequence) {
		String modelYear = bom.getId().getMtcModel().substring(0, 1);
		String modelCode = bom.getId().getMtcModel().substring(1);
		String modelTypeCode = bom.getId().getMtcType();
		String modelOptionCode = bom.getId().getMtcOption();
		String extColorCode = bom.getId().getMtcColor();
		String intColorCode = bom.getId().getIntColorCode();

		String specCode = modelYear + modelCode;
		specCode += ProductSpec.padModelTypeCode(modelTypeCode);
		specCode += ProductSpec.padModelOptionCode(modelOptionCode);
		specCode += ProductSpec.padExtColorCode(extColorCode);
		specCode += ProductSpec.padIntColorCode(intColorCode);

		PartByProductSpecCode partByProductSpecCode = new PartByProductSpecCode();
		PartByProductSpecCodeId partByProductSpecCodeId = new PartByProductSpecCodeId();
		partByProductSpecCodeId.setProductSpecCode(specCode);
		partByProductSpecCodeId.setPartId(part.getId().getPartId());
		partByProductSpecCodeId.setPartName(part.getId().getPartName());
		partByProductSpecCodeId.setModelYearCode(StringUtils.isEmpty(modelYear) ? ProductSpec.WILDCARD : modelYear);
		partByProductSpecCodeId.setModelCode(StringUtils.isEmpty(modelCode) ? ProductSpec.WILDCARD : modelCode);
		partByProductSpecCodeId
				.setModelTypeCode(StringUtils.isEmpty(modelTypeCode) ? ProductSpec.WILDCARD : modelTypeCode);
		partByProductSpecCodeId
				.setModelOptionCode(StringUtils.isEmpty(modelOptionCode) ? ProductSpec.WILDCARD : modelOptionCode);
		partByProductSpecCodeId
				.setExtColorCode(StringUtils.isEmpty(extColorCode) ? ProductSpec.WILDCARD : extColorCode);
		partByProductSpecCodeId
				.setIntColorCode(StringUtils.isEmpty(intColorCode) ? ProductSpec.WILDCARD : intColorCode);
		partByProductSpecCode.setId(partByProductSpecCodeId);
		getDao(PartByProductSpecCodeDao.class).save(partByProductSpecCode);

		LotControlRule rule = new LotControlRule();
		LotControlRuleId lotControlRuleid = new LotControlRuleId();
		lotControlRuleid.setPartName(part.getId().getPartName());
		lotControlRuleid.setProcessPointId(processPoint);
		lotControlRuleid.setProductSpecCode(specCode);
		lotControlRuleid.setModelYearCode(StringUtils.isEmpty(modelYear) ? ProductSpec.WILDCARD : modelYear);
		lotControlRuleid.setModelCode(StringUtils.isEmpty(modelCode) ? ProductSpec.WILDCARD : modelCode);
		lotControlRuleid.setModelTypeCode(StringUtils.isEmpty(modelTypeCode) ? ProductSpec.WILDCARD : modelTypeCode);
		lotControlRuleid
				.setModelOptionCode(StringUtils.isEmpty(modelOptionCode) ? ProductSpec.WILDCARD : modelOptionCode);
		lotControlRuleid.setExtColorCode(StringUtils.isEmpty(extColorCode) ? ProductSpec.WILDCARD : extColorCode);
		lotControlRuleid.setIntColorCode(StringUtils.isEmpty(intColorCode) ? ProductSpec.WILDCARD : intColorCode);
		rule.setId(lotControlRuleid);
		rule.setSequenceNumber(sequence);
		getDao(LotControlRuleDao.class).save(rule);
	}
}