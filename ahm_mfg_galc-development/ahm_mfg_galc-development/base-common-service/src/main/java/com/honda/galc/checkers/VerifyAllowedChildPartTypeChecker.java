package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.MCOperationPartMatrixDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.StringUtil;

public class VerifyAllowedChildPartTypeChecker extends AbstractBaseChecker<PartSerialScanData> {

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public CheckerType getType() {
		return CheckerType.Part;
	}

	@Override
	public int getSequence() {
		return 0;
	}

	@Override
	public List<CheckResult> executeCheck(PartSerialScanData inputData) {
		List<CheckResult> checkResults = new ArrayList<>();
		String partSerialNumber = inputData.getPartName();
		String productId = inputData.getProductId();

		String isChildSpecCode = childSpecCodeCheck(productId);

		if (StringUtil.isNullOrEmpty(isChildSpecCode)) {
			checkResults.add(createCheckResult("Part SN doesn't exist."));
			return checkResults;
		}

		List<MCOperationPartRevision> partsList = getOperation().getParts();
		if (partsList == null || partsList.isEmpty()) {
			checkResults.add(createCheckResult("Unable to find the part details for the specified unit of work."));
			return checkResults;
		}

		List<String> partNumbers = new ArrayList<>();
		for (MCOperationPartRevision part : partsList) {
			partNumbers.add(part.getPartNo());
		}

		String[] partNumbersArr = partNumbers.toArray(new String[0]);
		if (!StringUtils.startsWithAny(isChildSpecCode, partNumbersArr)) {
			checkResults.add(createCheckResult("Child spec code not found in part numbers list."));
			return checkResults;
		}

		boolean isCorrectChildPartType = checkChildPartType(partSerialNumber, isChildSpecCode);
		if (!isCorrectChildPartType) {
			checkResults.add(createCheckResult("Incorrect child part type for unit of work."));
		}

		return checkResults;
	}

	private String childSpecCodeCheck(String productId) {
		MbpnProductDao mbpnProductDao = ServiceFactory.getDao(MbpnProductDao.class);
		String specCode = mbpnProductDao.findCurrentProductSpecCode(productId);

		return specCode;
	}

	private boolean checkChildPartType(String operationName, String productSpecCode) {
		MCOperationPartMatrixDao mcOperationPartMatrixDao = ServiceFactory.getDao(MCOperationPartMatrixDao.class);
		List<Object[]> result = mcOperationPartMatrixDao.findByChildPart(operationName, productSpecCode);

		for (Object[] record : result) {
			String parentPart = (String) record[1];
			String childPart = (String) record[2];
			return buildAttribute(parentPart, childPart);
		}

		return !result.isEmpty();
	}

	private boolean buildAttribute(String parentPart, String childPart) {
		BuildAttributeDao buildAttributeDao = ServiceFactory.getDao(BuildAttributeDao.class);
		List<Object[]> result = buildAttributeDao.findAllParentChildDetails(parentPart, childPart);
		return !result.isEmpty();
	}

}
