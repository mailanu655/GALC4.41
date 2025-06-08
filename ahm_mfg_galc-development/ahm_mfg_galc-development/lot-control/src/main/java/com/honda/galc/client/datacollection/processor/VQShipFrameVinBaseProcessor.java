package com.honda.galc.client.datacollection.processor;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.PreviousLineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.PreviousLine;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.service.ServiceFactory;

public abstract class VQShipFrameVinBaseProcessor extends FrameVinProcessor {

	public VQShipFrameVinBaseProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
	}

	protected void validateVinScanType(ProductId productId) {			
		}
	protected boolean processFrameVin(ProductId productId){
		Logger.getLogger().info(PROCESS_PRODUCT + productId);
		super.validateVinScanType(productId);
		String scannedVin = productId.getProductId();
		ProductBean productBean = new ProductBean();
		productBean.setProductId(scannedVin);

		if (StringUtils.trim(scannedVin).length() < getCommonPropertyBean().getMaxProductSnLength()) {
			getController().getFsm().productIdNg(productBean, MESSAGE_ID,
					"Incorrect VIN length.");
			return false;
		}
		
		String scannedVinWithI = scannedVin;
		
		if (scannedVin.length() > getCommonPropertyBean().getMaxProductSnLength()) {

			scannedVin = StringUtils.trim(scannedVin);
			if (scannedVin.length() > getCommonPropertyBean().getMaxProductSnLength()) {

				scannedVin = scannedVin.substring(scannedVin.length()
						- getCommonPropertyBean().getMaxProductSnLength(), scannedVin.length());

			}
		}
		productId.setProductId(scannedVin);
		FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
		Frame frame = frameDao.findByKey(productId.getProductId());
		if (frame == null) {
			handleException("Unable to get Frame record for product id [" + productId.getProductId() + "]");
		}
		
		FrameSpecDao frameSpec = ServiceFactory.getDao(FrameSpecDao.class);
		FrameSpec spec = frameSpec.findByKey(frame.getProductSpecCode());
		String salesModelTypeCode = spec.getSalesModelTypeCode();
		String excludeSalesModel = property.getExcludeSalesModelType();
		Boolean certLabelCheck = true;

		if(StringUtils.isNotBlank(excludeSalesModel)) {
			String[] listSalesModelType = excludeSalesModel.trim().split(",");
			for(String code : listSalesModelType) {
				if(code.trim().equalsIgnoreCase(salesModelTypeCode)) {
					certLabelCheck = false;
					break;
				}
			}
		}		
		
		if (getProductCheckPropertyBean().isCertLabelCheck() && certLabelCheck) {

			String leadingVinChars = property.getLeadingCertLabelChars();
			boolean isCertLabel = false;
			if(StringUtils.isNotBlank(leadingVinChars)){
				String[] vinChars = leadingVinChars.trim().split(",");

				for(String c:vinChars){
					if (scannedVinWithI.trim().toUpperCase().startsWith(c)) {
						isCertLabel = true;
						break;
					}
				}
				if(!isCertLabel) {
					getController().getFsm().productIdNg(productBean, MESSAGE_ID, "Cert Label was not scanned");
					return false;
				}
			}
		}

		ProcessPoint confirmationCheckProcessPoint = ServiceFactory.getDao(
				ProcessPointDao.class).findByKey(getFrameLinePropertyBean().getVqConfirmationProcessPointId());

		PreviousLineDao previousLineDao = ServiceFactory.getDao(PreviousLineDao.class);
		ProcessPoint vqShipProcessPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(StringUtils.trim(context.getProcessPointId()));

		boolean validPreviousLine = false;

		List<PreviousLine> previousLineList = previousLineDao.findAllByLineId(vqShipProcessPoint.getLineId());
		Iterator<PreviousLine> iter = previousLineList.iterator();
		while (iter.hasNext()) {
			String previousLine = StringUtils.trim(iter.next().getId().getPreviousLineId());
			if (previousLine == null) {
				continue;
			}
			if (previousLine.equals(StringUtils.trim(frame.getTrackingStatus()))) {
				validPreviousLine = true;
				break;
			}
		}
		if (!validPreviousLine) {
			LineDao lineDao = ServiceFactory.getDao(LineDao.class);
			DivisionDao divisionDao = ServiceFactory.getDao(DivisionDao.class);

			Line vinLine = lineDao.findByKey(StringUtils.trim(frame.getTrackingStatus()));
			Line vqShipLine = lineDao.findByKey(StringUtils.trim(vqShipProcessPoint.getLineId()));

			String vinDivisionId = StringUtils.trim(vinLine.getDivisionId());
			String vqShipDivisionId = StringUtils.trim(vqShipLine.getDivisionId());
			Division vinDivision = divisionDao.findByKey(vinDivisionId);
			Division vqShipDivision = divisionDao.findByKey(vqShipDivisionId);
			Integer vinDivisionSeqNumber = vinDivision.getSequenceNumber();
			Integer vqShipDivisionSeqNumber = vqShipDivision.getSequenceNumber();
			Integer vinLineSeqNumber = vinLine.getLineSequenceNumber();
			Integer vqShipLineSeqNumber = vqShipLine.getLineSequenceNumber();

			if (vinDivisionSeqNumber == vqShipDivisionSeqNumber) {
				if (vinLineSeqNumber >= vqShipLineSeqNumber) {
					getController().getFsm().productIdNg(productBean, MESSAGE_ID, "VIN is currently in " + vinLine.getId() + " status.  It is past " 
							+ (vqShipProcessPoint == null ? "the VQ Shipping process point" : vqShipProcessPoint.getProcessPointName()));
					return false;
				} else if (vinLineSeqNumber < vqShipLineSeqNumber) {
					getController().getFsm().productIdNg(productBean, MESSAGE_ID, "VIN is currently in " + vinLine.getId() + " status.  It must be processed at " 
							+ (confirmationCheckProcessPoint == null ? "the VQ Confirmation process point" : confirmationCheckProcessPoint.getProcessPointName()) 
							+ " before " + (vqShipProcessPoint == null ? "the VQ Shipping process point" : vqShipProcessPoint.getProcessPointName()));
					return false;
				}

			} else {
				if (vinDivisionSeqNumber > vqShipDivisionSeqNumber) {
					getController().getFsm().productIdNg(productBean, MESSAGE_ID, "VIN is currently in " + vinLine.getId() + " status.  It is past " 
							+ (vqShipProcessPoint == null ? "the VQ Shipping process point" : vqShipProcessPoint.getProcessPointName()));
					return false;
				} else if (vinDivisionSeqNumber < vqShipDivisionSeqNumber) {
					getController().getFsm().productIdNg(productBean, MESSAGE_ID, "VIN is currently in " + vinLine.getId() + " status.  It must be processed at " 
							+ (confirmationCheckProcessPoint == null ? "the VQ Confirmation process point" : confirmationCheckProcessPoint.getProcessPointName()) 
							+ " before " + (vqShipProcessPoint == null ? "the VQ Shipping process point" : vqShipProcessPoint.getProcessPointName()));
					return false;
				}
			}
		}

		Logger.getLogger().debug(
				"VQShipFrameVinProcessor : Exit execute method");

		return true;
	}

	/** NALC-1574-MAX_PRODUCT_SN_LENGTH is set to 17 ( I cannot add both 17 and length 11)*/
	@Deprecated
	protected void checkProductIdLength() {
		if (product.getProductId().length() != getCommonPropertyBean().getMaxProductSnLength()) {
			String msg = "Invalid Product Id: " + product.getProductId() + ", length invalid.";
			handleException(msg);
		}
	}
}
