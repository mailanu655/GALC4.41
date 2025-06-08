package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.oif.BomDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.fif.Bom;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;

public class MbpnToFrameChecker extends AbstractBaseChecker<PartSerialScanData> {

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
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		
		//Fetching MTC Model and Type
		FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
		Frame frame = frameDao.findByKey(inputData.getProductId());
		String frameProductSpecCode = frame.getProductSpecCode();
		String mtcModel =ProductSpec.extractModelYearCode(frameProductSpecCode)
				+ProductSpec.extractModelCode(frameProductSpecCode);
		String mtcType = ProductSpec.extractModelTypeCode(frameProductSpecCode);
		
		//Fetching Part Number
		MbpnProductDao mbpnProductDao = ServiceFactory.getDao(MbpnProductDao.class);
		MbpnProduct mbpnProduct = mbpnProductDao.findByKey(inputData.getSerialNumber());
		if (mbpnProduct == null) {
			checkResults.add(createCheckResult("No Mbpn Product exists for serial number " + inputData.getSerialNumber()));
			return checkResults;
		}
		Mbpn mbpn = ServiceFactory.getDao(MbpnDao.class).findByKey(mbpnProduct.getCurrentProductSpecCode());
		if(mbpn == null) {
			checkResults.add(createCheckResult("No Mbpn exists for serial number " + inputData.getSerialNumber()));
			return checkResults;
		}
		String partNo = mbpn.getMbpn();
		
		//Fetching Valid BOM data
		BomDao bomDao = ServiceFactory.getDao(BomDao.class);
		List<Bom> validBom = bomDao.findAllBy(partNo, mtcModel, mtcType);
		
		//MBPN - Frame match check
		if(null == validBom || validBom.isEmpty()) {
			checkResults.add(createCheckResult("Frame product spec code does not match MBPN product spec code."));
		}

		return checkResults;
	}
}
