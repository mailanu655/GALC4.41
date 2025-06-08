/**
 * 
 */
package com.honda.galc.client.vinstamp;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.device.plc.IPlcDataReadyEventProcessor;
import com.honda.galc.client.device.plc.omron.PlcDataField;
import com.honda.galc.client.enumtype.FloorStampInfoCodes;
import com.honda.galc.client.events.FloorStampRequest;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.entity.enumtype.ProductStampingSendStatus;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Subu Kathiresan
 * @author Todd Roling
 * @author Gangadhararao Gadde
 * Oct 31, 2011
 */
public class FloorStampVinProcessor extends FloorStampBaseProcessor	
	implements IPlcDataReadyEventProcessor <FloorStampRequest> {
	
	private static final int DEFAULT_ERRORCODE = -1;
	private static final int VIN_READY = 0;
	private static final int INVALID_VIN = 1;
	private static final int VIN_NOT_IN_SCHEDULE = 2;
	private static final int NO_NEXT_VIN = 3;
	private static final int VIN_ALREADY_STAMPED = 4;
	private static final int VIN_ALREADY_SENT = 5;
	private static final int SKIPPED_VIN = 6;
	
	private int _errorCode = DEFAULT_ERRORCODE;

	private String _lastVin = "";
	private String _nextVin = "";
	private String _nextExpectedVin = "";

	public FloorStampVinProcessor() {
	}
	
	public synchronized boolean execute(FloorStampRequest deviceData) {
		String lastVin = deviceData.getLastVin();
		String nextVin;
		boolean autoSkip;

		do {
			autoSkip = false;
			setLastVin(lastVin);
			deviceData.setLastVin(lastVin);
			nextVin = retrieveNextVin(deviceData);
			getBean().put("nextVin", new StringBuilder(nextVin));
			if (_errorCode == VIN_ALREADY_STAMPED && PropertyService.getPropertyBoolean(getTrackingProcessPoint(), "SKIP_ALREADY_STAMPED_VIN", false)) {
				autoSkip = true;
				getLogger().warn("Vin " + nextVin + " has already been stamped.  Skipping " + nextVin + ".");
				lastVin = nextVin;
			}
		} while (autoSkip);
		
		switch (_errorCode) {
		case VIN_READY:
			updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_OK, getLastVin());
			break;
		case INVALID_VIN:
			getLogger().warn("Vin " + getLastVin() + " is not valid");
			// invalid VIN also not in schedule
		case VIN_NOT_IN_SCHEDULE:
			getLogger().warn("Vin " + getLastVin() + " is not in schedule");
			updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_INVALID, getLastVin());
			break;
		case NO_NEXT_VIN:
			getLogger().warn("Vin " + getLastVin() + " has no next vin");
			updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_NO_NEXT_VIN, getLastVin());
			break;
		case VIN_ALREADY_STAMPED:
			getLogger().warn("Vin " + getLastVin() + " has already been stamped ");
			updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_ALREADY_PROCESSED, getLastVin());
			break;
		case VIN_ALREADY_SENT:
			getLogger().warn("Vin " + getLastVin() + " has already been sent ");
			updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_ALREADY_PROCESSED, getLastVin());
			break;
		case SKIPPED_VIN:
			getLogger().warn("Skipped Vin " + _nextExpectedVin);
			updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_SKIPPED, _nextExpectedVin);
			break;
		case DEFAULT_ERRORCODE:		
		default:
			getLogger().warn("Invalid error code returned from getNextVin(): " + _errorCode);
			break;
		}		
		
		// unexpected null or empty vin
		if (nextVin == null || nextVin.equals("")) {
			updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_INVALID, getLastVin());
			return false;
		}
		
		try {
			Frame frame = getFrameDao().findByKey(nextVin);
			getBean().setProductId(frame.getProductId());
			getBean().setProductSpecCode(frame.getProductSpecCode());
		} catch(Exception ex) {
			ex.printStackTrace();
			updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_INVALID, getLastVin());
			return false;
		}
		
		getLogger().info("Retrieved next vin is: " + nextVin);
		getBean().put("rfidVin", new StringBuilder(nextVin));
		
		return true;
	}
	
	/**
	 * 
	 * @param deviceData
	 * @return
	 */
	private String retrieveNextVin(FloorStampRequest deviceData) {
		Object[] result = null;
		try {
			result = getPreProductionLotDao().findNextWeldOnProductId(deviceData.getLastVin(), deviceData.getSendStatus());
		} catch (Exception ex) {
			getLogger().error("Could not retrieve next product id for vin: " + (deviceData == null ? "" : deviceData.getLastVin()));
			ex.printStackTrace();
		}
		
		if (result == null) {
			getLogger().error("Null returned from getPreProductionLotDao().findNextWeldOnProductId() for vin: " 
					+ (deviceData == null ? "" : deviceData.getLastVin()));
			return "";
		}
		if (result.length < 4) {
			getLogger().error("Missing data from getPreProductionLotDao().findNextWeldOnProductId() for vin: " 
					+ (deviceData == null ? "" : deviceData.getLastVin()) + ", number of fields returned: " + result.length);
			return "";
		}
		
		try {
			if (result[2] != null)
				_nextExpectedVin = (String) result[2];
		
			if (result[1] != null)
				setNextVin((String) result[1]);
			else {
				getLogger().warn("Retrieved next vin is null for last vin " + deviceData.getLastVin());
				setNextVin(_nextExpectedVin);
			}	
	
			if (result[3] != null)
				_errorCode = (Integer) result[3];
			
			return getNextVin();
		} catch (Exception ex) {
			getLogger().error("Invalid results retrieved for vin: " + (deviceData == null ? "" : deviceData.getLastVin()));
			ex.printStackTrace();
			return "";
		}
	}
	
	public boolean updateRfidData() {
		boolean rfidDataOk = false;
		
		try {
			Frame frame = getFrameDao().findByKey(getNextVin());
			getBean().put("repairLevel", new StringBuilder("0")); // Requested to always be zero per WE Equipment
			
			if (rfidDataOk = frame != null) {
				if(rfidDataOk &= frame.getProductionLot() != null) {
					ProductionLot prodLot = getProductionLotDao().findByKey(frame.getProductionLot());
					if(rfidDataOk &= prodLot != null) {
						if(rfidDataOk &= prodLot.getProdLotKd() != null) {
							ProductionLot prodLotKd = getProductionLotDao().findByKey(prodLot.getProdLotKd());
							if(rfidDataOk &= prodLotKd.getLotNumber() != null)
								getBean().put("prodLotKd", new StringBuilder(prodLotKd.getLotNumber()));
							else {
								getLogger().warn("Prod lot kd lot number is null - RFID data could not be updated for request vin: " + getNextVin());
							}
						} else {
							getLogger().warn("Production lot prod lot kd is null - RFID data could not be updated for request vin: " + getNextVin());
						}
						getBean().put("prodLotKdQty", new StringBuilder("" + getProductionLotDao().findProdLotKdQty(prodLot)));						
					} else {
						getLogger().warn("Production lot could not be found - RFID data could not be updated for request vin: " + getNextVin());
					}
				} else {
					getLogger().warn("Frame production lot is null - RFID data could not be updated for request vin: " + getNextVin());
				}
				
				rfidDataOk &= setFrameSpecData(frame);
			} else {
				getLogger().warn("Frame is null - RFID data could not be updated for request vin: " + getNextVin());
			}
		}		
		catch(Exception ex) {
			getLogger().error("Exception occurred - RFID data could not be updated for request vin: " + getNextVin());
			ex.printStackTrace();
		}
		
		if(!rfidDataOk) {
			getLogger().warn("RFID data for requested Vin " + getLastVin() + " is incorrect");
			updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_RFID_NG, getNextVin());
		} else
			getLogger().info("VinStampRequest RFID data ok!");
		
		return rfidDataOk;
	}
	
	/**
	 * sets boundary marks and mtoc info when a valid frame is provided
	 * 
	 * @param frame
	 */
	private boolean setFrameSpecData(Frame frame) {
		boolean frameSpecDataOk = false;
		
		try {
			FrameSpec frameSpec = ServiceFactory.getDao(FrameSpecDao.class).findByKey(frame.getProductSpecCode());
			
			if(frameSpec != null) {
				String boundaryMark = frameSpec.getBoundaryMarkRequired();
		
				if(boundaryMark == null || boundaryMark.trim().equals(""))
					boundaryMark = " ";
				else
					boundaryMark = boundaryMark.trim().substring(0, 1);
		
				getBean().put("boundaryMarkLeft" , new StringBuilder(boundaryMark));
				getBean().put("boundaryMarkRight", new StringBuilder(boundaryMark));
				
				getBean().put("model", new StringBuilder(frameSpec.getModelYearCode() + frameSpec.getModelCode()));
				getBean().put("type", new StringBuilder(frameSpec.getModelTypeCode()));
				getBean().put("option", new StringBuilder(frameSpec.getModelOptionCode()));
				getBean().put("intColor", new StringBuilder(frameSpec.getIntColorCode()));
				getBean().put("extColor", new StringBuilder(frameSpec.getExtColorCode()));
				
				frameSpecDataOk = true;
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Unable to retrieve and set specific M T O IC EC data for VIN: " + 
					getNextVin() + " with product spec code: " + frame.getProductSpecCode());
		}
		
		return frameSpecDataOk;
	}

	protected boolean updateStatus() {
			return getNextVin() != null && !getNextVin().equals("") &&
				updateProductStartDate() & updateProductSendStatus() & updateProductionLotSendStatus() & updateProductionLotStatus();
	}
	
	private boolean updateProductStartDate() {
		try {
			getFrameDao().updateProductStartDate(getTrackingProcessPoint(), getNextVin());
			getLogger().info("Successfully updated product start date for vin: " + getNextVin());
			return true;
		}catch(Exception ex) {
			getLogger().error("Could not update product start date for vin: " + getNextVin());
			ex.printStackTrace();
		}
		return false;
	}
	
	private boolean updateProductSendStatus() {
		try {
			ProductStampingSequence prodStampSeq = getStampingSeqDao().findById(getProductionLot(), getNextVin());

			if (prodStampSeq.getSendStatus() == ProductStampingSendStatus.WAITING.getId()) {
				prodStampSeq.setSendStatus(ProductStampingSendStatus.SENT.getId());
				getStampingSeqDao().update(prodStampSeq);
				getLogger().info("Successfully updated send status to: " + ProductStampingSendStatus.SENT.getId() + 
						" for vin: " + getNextVin());
			} else {
				getLogger().info("Did not update send status to: " + ProductStampingSendStatus.SENT.getId() + 
						" for vin: " + getNextVin() + " because send status is not 0");
			}
			return true;
		}catch(Exception ex) {
			getLogger().error("Send status could not be updated for vin: " + getNextVin());
			ex.printStackTrace();
		}
		return false;
	}
	
	private boolean updateProductionLotSendStatus() {
		try {
			getPreProductionLotDao().updateSendStatus(getProductionLot(), ProductStampingSendStatus.SENT.getId());
			getPreProductionLotDao().updateSentTimestamp(getProductionLot());
			getLogger().info("Successfully updated send status to: " + ProductStampingSendStatus.SENT.getId() + 
					" for production lot: " + getProductionLot());
			return true;
		}catch(Exception ex) {
			getLogger().error("Send status could not be updated for production lot: " + getProductionLot());
			ex.printStackTrace();
		}
		return false;
	}
	
	// 217 update to emulate old ear
	private boolean updateProductionLotStatus() {
		try {
			if(getProductionLotDao().findByKey(getProductionLot()).getLotStatus() == 0) {
				getProductionLotDao().updateLotStatus(getProductionLot(), ProductStampingSendStatus.SENT.getId());
				getLogger().info("Successfully updated lot status (in gal217tbx) to: " + ProductStampingSendStatus.SENT.getId() + 
						" for production lot: " + getProductionLot());
			}
			else {
				getLogger().info("Did not update lot status (in gal217tbx) to: " + ProductStampingSendStatus.SENT.getId() + 
						" for production lot: " + getProductionLot() + " because lot status is not 0");
			}
			return true;
		}catch(Exception ex) {
			getLogger().error("Lot status could not be updated (in gal217tbx) for production lot: " + getProductionLot());
			ex.printStackTrace();
		}
		return false;
	}

	private String getProductionLot() {
		Frame frame = getFrameDao().findByKey(getNextVin());

		if(frame.getProductionLot() != null) {
			ProductionLot prodLot = getProductionLotDao().findByKey(frame.getProductionLot());
			if(prodLot != null) {
				if(prodLot.getProductionLot() != null) {
					return prodLot.getProductionLot();	
				}
			}
		}
		return "";
	}
	
	public String getNextVin() {
		return _nextVin;
	}

	public void setNextVin(String nextVin) {
		_nextVin = nextVin;
	}
	
	public String getLastVin() {
		return _lastVin;
	}
	
	public void setLastVin(String lastVin) {
		_lastVin = lastVin;
	}
	
	public void postPlcWrite(boolean writeSucceeded) {
		if (writeSucceeded) {
			updateStatus();
		}
	}

	public void validate() {
		updateRfidData();
		try {
			List<DeviceFormat> deviceFormats = findAllByTagTypes(getApplicationId(), getPlcDeviceId(), DeviceTagType.SQL, DeviceTagType.ATTR_BY_MTOC);
			for(DeviceFormat deviceFormat: deviceFormats){
				if (deviceFormat.getOffset() == 1) {
					PlcDataField dataField = getBean().getPlcDataFields().get(deviceFormat.getTag().trim().split("\\.")[1]);
					if (dataField.getValue() == null || dataField.getValue().equals("")) {
						updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_RFID_NG, getNextVin());
					}
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		getBean().put("eqDataReady", new StringBuilder("0"), DeviceTagType.PLC_EQ_DATA_READY);
		getBean().put("galcDataReady", new StringBuilder("1"), DeviceTagType.PLC_GALC_DATA_READY);
		getBean().put("infoCode", new StringBuilder(getInfoCode()));
		getBean().put("infoMessage", new StringBuilder(getInfoMessage()));
	}

	private List<DeviceFormat> findAllByTagTypes(String clientId, String tagPrefix, DeviceTagType... tagTypes) {
		List<DeviceFormat> deviceFormats = new ArrayList<DeviceFormat>();
		for (DeviceTagType tagType : tagTypes) {
			List<DeviceFormat> tagDeviceFormats = getDeviceFormatDao().findAllByTagType(clientId, tagPrefix, tagType);
			if (tagDeviceFormats != null) deviceFormats.addAll(tagDeviceFormats);
		}
		return deviceFormats;
	}
}
