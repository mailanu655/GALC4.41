package com.honda.galc.client.vinstamp;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.device.plc.IPlcDataReadyEventProcessor;
import com.honda.galc.client.enumtype.FloorStampInfoCodes;
import com.honda.galc.client.events.FloorStampResultVerificationRequest;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.entity.enumtype.ProductStampingSendStatus;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductStampingSequenceId;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.on.WeldOnService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ReflectionUtils;
import com.honda.galc.util.SortedArrayList;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * Nov 2, 2012
 */
public class FloorStampResultVerificationProcessor extends FloorStampBaseProcessor 
implements IPlcDataReadyEventProcessor <FloorStampResultVerificationRequest> {

	private String _productionLot = "";
	private FloorStampResultVerificationRequest _deviceData = null;
	private SortedArrayList<DeviceFormat> _rfidAttributes = null;

	public FloorStampResultVerificationProcessor() {
	}

	public boolean execute(FloorStampResultVerificationRequest deviceData) {
		_deviceData = deviceData;

		try {
			if (!doesVinExist(getStampedVin())) {
				updateVinStampInfo(FloorStampInfoCodes.RESULT_VIN_INVALID, getStampedVin());
				return false;
			}

			String expectedVin = getExpectedVin();
			if(expectedVin == null || expectedVin.equals("")) {
				getLogger().warn("Next expected result VIN is [" + expectedVin + "]. Using supplied VIN [" + getStampedVin() + "]");
				expectedVin = getStampedVin();
			} else if (!expectedVin.equals(getStampedVin())) {
				updateVinStampInfo(FloorStampInfoCodes.RESULT_VIN_SKIPPED, expectedVin);
			}

			setProductionLot(getFrameDao().findByKey(getStampedVin()).getProductionLot());
			ProductStampingSequenceId id = new ProductStampingSequenceId(getProductionLot(), getStampedVin());
			ProductStampingSequence prodStampSeq = getStampingSeqDao().findByKey(id);

			switch (EnumUtil.getType(ProductStampingSendStatus.class, prodStampSeq.getSendStatus())) {
			case STAMPED:	// updates if skipped vin
				updateVinStampInfo(FloorStampInfoCodes.RESULT_VIN_ALREADY_PROCESSED, getStampedVin());
				break;
			case SENT:		// normal flow
				updateVinStampInfo(FloorStampInfoCodes.RESULT_VIN_OK, getStampedVin());
				break;
			case WAITING:	// should never happen - invalid vin - through to default
				getLogger().error("Result vin send status is 0");
			default:
				updateVinStampInfo(FloorStampInfoCodes.RESULT_VIN_INVALID, getStampedVin());
				return false;
			}

			updateStatus();
			return true;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	private String getExpectedVin() {
		try {
			// Sometimes this value will be null when the next VIN is not foreseeable
			return getExpectedProductDao().findByKey(getTrackingProcessPoint()).getProductId();
		} catch(Exception ex) {}
		return "";
	}

	protected void updateStatus() {
		try {
			Frame frame = getFrameDao().findByKey(getStampedVin());
			ProductHistory productHistory = ProductTypeUtil.createProductHistory(frame.getId(), getTrackingProcessPoint(), ProductType.FRAME);

			if (productHistory != null) {
				productHistory.setActualTimestamp(getStampedTime());
			}
			ServiceFactory.getService(WeldOnService.class).processProduct(frame, productHistory);
		} catch (Exception e) {
			getLogger().info("Send status not updated for Result vin: " + getStampedVin());
		}
	}

	public void postPlcWrite(boolean writeSucceeded) {
	}

	public void validate() {
		verifyResults();

		getBean().put("eqDataReady", new StringBuilder("0"), DeviceTagType.PLC_EQ_DATA_READY);
		getBean().put("galcDataReady", new StringBuilder("1"), DeviceTagType.PLC_GALC_DATA_READY);
		getBean().put("infoCode", new StringBuilder(getInfoCode()));
		getBean().put("infoMessage", new StringBuilder(getInfoMessage()));
	}

	private boolean verifyResults() {

		if(!isRfidDataConfigured())
			return true;

		boolean resultsOk = false;
		Frame frame = getFrameDao().findByKey(getStampedVin());

		try {
			if (resultsOk = frame != null) {
				if(resultsOk &= frame.getProductionLot() != null) {
					ProductionLot prodLot = getProductionLotDao().findByKey(frame.getProductionLot());			
					if(resultsOk &= prodLot != null) {
						if(resultsOk &= prodLot.getProductionLot() != null) {
							setProductionLot(prodLot.getProductionLot());
						}
						if(resultsOk) {
							resultsOk = resultsOk && getDeviceData().getRfidVin().equals(getStampedVin());
							resultsOk = resultsOk && getDeviceData().getProdLotKd().equals(getProductionLotDao().findByKey(prodLot.getProdLotKd()).getLotNumber());
							resultsOk = resultsOk && getDeviceData().getProdLotKdQty().equals(new Integer(getProductionLotDao().findProdLotKdQty(prodLot)).toString());
							resultsOk = resultsOk && checkFrameSpecData(frame);
							resultsOk = resultsOk && checkRfidAttributes();
						}
					} else {
						getLogger().warn("Production lot could not be found - RFID data could not be updated for request vin: " + getStampedVin());
					}
				} else {
					getLogger().warn("Frame production lot is null - RFID data could not be updated for request vin: " + getStampedVin());
				}
			} else {
				getLogger().warn("Frame is null - RFID data could not be updated for request vin: " + getStampedVin());
			}
		}
		catch(Exception ex) {
			getLogger().error("Exception occurred - RFID data could not be collected for result vin: " + getStampedVin());
			ex.printStackTrace();
		}

		if(!resultsOk) {
			getLogger().warn("Results data for stamped Vin " + getStampedVin() + " is incorrect");
			updateVinStampInfo(FloorStampInfoCodes.RESULT_VIN_RFID_NG, getStampedVin());
		} else
			getLogger().info("FloorStampResult data ok!");

		return resultsOk;
	}

	private boolean isRfidDataConfigured() {
		if (getConfiguredRfidAttributes().size() > 0)
			return true;
		else
			return false;
	}

	private SortedArrayList<DeviceFormat> getConfiguredRfidAttributes() {
		if (_rfidAttributes == null) {
			_rfidAttributes = new SortedArrayList<DeviceFormat>("getSequenceNumber");
			_rfidAttributes.addAll(findAllByTagTypes(getBean().getApplicationId(), getPlcDeviceId(), DeviceTagType.PLC_READ_ATTRIBUTE, DeviceTagType.ATTR_BY_MTOC));
		}
		return _rfidAttributes;
	}

	private List<DeviceFormat> findAllByTagTypes(String clientId, String tagPrefix, DeviceTagType... tagTypes) {
		List<DeviceFormat> deviceFormats = new ArrayList<DeviceFormat>();
		for (DeviceTagType tagType : tagTypes) {
			List<DeviceFormat> tagDeviceFormats = getDeviceFormatDao().findAllByTagType(clientId, tagPrefix, tagType);
			if (tagDeviceFormats != null) deviceFormats.addAll(tagDeviceFormats);
		}
		return deviceFormats;
	}

	private boolean checkFrameSpecData(Frame frame) {
		boolean mtocDataOk = true;

		try {
			FrameSpec frameSpec = ServiceFactory.getDao(FrameSpecDao.class).findByKey(frame.getProductSpecCode());
			mtocDataOk = mtocDataOk && getDeviceData().getModel().trim().equals(frameSpec.getModelYearCode().trim() + frameSpec.getModelCode().trim());
			mtocDataOk = mtocDataOk && getDeviceData().getType().trim().equals(frameSpec.getModelTypeCode().trim());
			mtocDataOk = mtocDataOk && getDeviceData().getOption().trim().equals(frameSpec.getModelOptionCode().trim());
			mtocDataOk = mtocDataOk && getDeviceData().getIntColor().trim().equals(frameSpec.getIntColorCode().trim());
			mtocDataOk = mtocDataOk && getDeviceData().getExtColor().trim().equals(frameSpec.getExtColorCode().trim());
		}
		catch(Exception ex) {
			mtocDataOk = false;
			getLogger().error("Unable to retrieve specific M T O IC EC data for VIN: " + 
					getStampedVin() + " with product spec code: " + frame.getProductSpecCode());
		}
		return mtocDataOk;
	}

	private boolean checkRfidAttributes() {
		for(DeviceFormat deviceFormat: getConfiguredRfidAttributes()){
			if (deviceFormat.getOffset() == 1) {		// validate only if offset (yes, offset is a bad name) is set to 1
				String fieldName;
				StringBuilder value, rfidReaderVal;
				switch (DeviceTagType.getType(deviceFormat.getTagType())) {
				case PLC_READ_ATTRIBUTE:
					fieldName = deviceFormat.getTag().trim().split("\\.")[2];
					value = getBean().getSubstitutionList().get(fieldName);
					rfidReaderVal = (StringBuilder) ReflectionUtils.invoke(getDeviceData(), "getAttribute", fieldName);
					if (!checkIfAttribsAreEqual(value, rfidReaderVal)) {
						getLogger().error("Retrieved " + fieldName + " attribute value " + StringUtil.stringToBitArray(rfidReaderVal) 
								+  " does not match expected value: " + StringUtil.stringToBitArray(value));
						return false;
					}
					break;
				case ATTR_BY_MTOC:
					fieldName = deviceFormat.getTag().trim().split("\\.")[1];
					value = getBean().getPlcDataFields().get(fieldName).getValue();
					rfidReaderVal = (StringBuilder) ReflectionUtils.invoke(getDeviceData(), "getAttribute", fieldName);
					if (!value.toString().trim().equals(rfidReaderVal.toString().trim())) {
						getLogger().error("Retrieved " + fieldName + " attribute value " + StringUtil.stringToBitArray(rfidReaderVal) 
								+  " does not match expected value: " + StringUtil.stringToBitArray(value));
						return false;
					}
					break;
				default:
					getLogger().error(String.format("Tag type %1$d is not valid for checkRfidAttributes().", deviceFormat.getTagType()));
					return false;
				}
			}
		}
		return true;
	}

	public boolean checkIfAttribsAreEqual(StringBuilder attrib1, StringBuilder attrib2) {
		if (attrib1.length() != attrib2.length())
			return false;

		for(int i = 0; i< attrib1.length(); i++) {
			if (attrib1.charAt(i) != attrib2.charAt(i)){
				return false;
			}
		}
		return true;
	}

	public Timestamp getStampedTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		String stampedTime = StringUtil.padLeft(getDeviceData().getYear(), 2,'0') + 
				StringUtil.padLeft(getDeviceData().getMonth(), 2,'0') +
				StringUtil.padLeft(getDeviceData().getDay(), 2,'0') +
				StringUtil.padLeft(getDeviceData().getHour(), 2,'0') +
				StringUtil.padLeft(getDeviceData().getMinute(), 2,'0') +
				StringUtil.padLeft(getDeviceData().getSeconds(), 2,'0');
		try {
			return new Timestamp(formatter.parse(stampedTime).getTime()); // TODO: Check formatting
		} catch (ParseException ex) {
			ex.printStackTrace();
			getLogger().warn(ex, "Could not parse Stamped time from device Data:" + ex.getMessage());
			return null;
		}
	}

	public String getStampedVin() {
		return StringUtils.trimToEmpty(getDeviceData().getStampedVin());
	}

	public FloorStampResultVerificationRequest getDeviceData() {
		return _deviceData;
	}

	public void setProductionLot(String productionLot) {
		_productionLot = productionLot;
	}

	public String getProductionLot() {
		return _productionLot;
	}
}
