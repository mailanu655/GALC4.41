/**
 * 
 */
package com.honda.galc.client.assembly;

import java.util.List;
import java.util.Vector;

import com.honda.galc.client.device.plc.IPlcDataReadyEventProcessor;
import com.honda.galc.client.enumtype.RfidErrorCodes;
import com.honda.galc.client.events.AfRfidWriteRequest;
import com.honda.galc.client.headless.PlcDataReadyEventProcessorBase;
import com.honda.galc.dao.product.EngineFiringResultDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.entity.product.EngineFiringResult;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.StringUtil;


/**
 * 
 * @author Gangadhararao Gadde
 * @date Nov 29, 2012
 */
public class AfRfidWriteRequestProcessor extends PlcDataReadyEventProcessorBase implements IPlcDataReadyEventProcessor<AfRfidWriteRequest> {

	public static String TRACKING_PROCESS_POINT_KEY = "TRACKING_PROCESS_POINT";
	public static String REREQUEST_RELOAD = "REREQUEST_RELOAD";

	public synchronized boolean execute( AfRfidWriteRequest deviceData) {


		boolean success = false;
		try {
			String vin = null;
			String mtoc = null;
			String mto=null;
			String type=null;
			String option=null;
			String resultId=null;
			String afOnSequenceNum=null;

			Vector<Integer> errorCodes = new Vector<Integer>();
			Vector<String> errorMessages = new Vector<String>();

			vin = deviceData.getVin().trim();
			getLogger().info("Recieved VIN");
			FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
			Frame frame = frameDao.findByKey(vin);
			if(frame==null)
			{
				errorCodes.add(RfidErrorCodes.VIN_NOT_VALID.getValue());
				errorMessages.add(RfidErrorCodes.VIN_NOT_VALID.getErrorMessage());
				getLogger().info("Invalid VIN");
			}else
			{
				mtoc =frame.getProductSpecCode();
				if(mtoc==null)
				{
					errorCodes.add(RfidErrorCodes.MTOC_MISSING.getValue());
					errorMessages.add(RfidErrorCodes.MTOC_MISSING.getErrorMessage());
					getLogger().info("MTOC is missing for the product");
				}else
				{
					if(mtoc.length()>=9)
					{
						if(mtoc.substring(4,5).equals(" "))
						{
							type=mtoc.substring(5,7);
						}
						else
						{
							type=mtoc.substring(4,7);
						}		
						mto=mtoc.substring(1,10)+mtoc.substring(0,1);
					}
				}

				if(frame.getAfOnSequenceNumber()!=null)
				{
					afOnSequenceNum = frame.getAfOnSequenceNumber().toString();
					if(StringUtil.padLeft(afOnSequenceNum,4,'0' ,false).equals("0000"))
					{
						afOnSequenceNum="";
					}
					else
					{
						afOnSequenceNum= StringUtil.padLeft(afOnSequenceNum,4,'0' ,false);
					}
				}
				else
				{
					errorCodes.add(RfidErrorCodes.BODY_SEQ_NUMBER_MISSING.getValue());		
					errorMessages.add(RfidErrorCodes.BODY_SEQ_NUMBER_MISSING.getErrorMessage());
					getLogger().info("AFON Sequence number is missing for the VIN");
				}

				String engineSerialNumber=frame.getEngineSerialNo();
				if (engineSerialNumber != null) 
				{
					EngineFiringResultDao engineFiringResultDao = ServiceFactory.getDao(EngineFiringResultDao.class);
					List<EngineFiringResult> engineFiringResultList = engineFiringResultDao.findAllByProductId(engineSerialNumber);
					if (engineFiringResultList != null&& engineFiringResultList.size() > 0) 
					{
						EngineFiringResult engineFiringResult = engineFiringResultList.get(0);
						resultId = String.valueOf(engineFiringResult.getResultId());						
					} else 
					{
						errorCodes.add(RfidErrorCodes.TESTFIRE_MISSING.getValue());
						errorMessages.add(RfidErrorCodes.TESTFIRE_MISSING.getErrorMessage());
						getLogger().info("Test Fire is missing for the VIN");
					}
				} else 
				{
					errorCodes.add(RfidErrorCodes.ENGINE_SERIALNUM_MISSING.getValue());
					errorMessages.add(RfidErrorCodes.ENGINE_SERIALNUM_MISSING.getErrorMessage());
					getLogger().info("Engine Serial Number is missing for VIN");
				}				  

			}


			getBean().put("vin", new StringBuilder(vin==null?"":vin));
			getBean().put("vinCopy", new StringBuilder(vin==null?"":vin));
			getBean().put("mto", new StringBuilder(mto==null?"":mto));
			getBean().put("resultId", new StringBuilder(resultId==null?"":resultId));
			getBean().setProductSpecCode(mtoc);
			getBean().getSubstitutionList().put("TEST_FIRE",new StringBuilder(resultId==null?"0":resultId));
			getBean().put("afOnSequenceNum", new StringBuilder(afOnSequenceNum==null?"":afOnSequenceNum));
			if(errorCodes.size()>0)
			{
				getBean().put("errorCode", new StringBuilder(errorCodes.get(0).toString()));
			}
			else
			{
				getBean().put("errorCode", new StringBuilder(RfidErrorCodes.NO_ERRORS.getValue()));
			}
			if(errorMessages.size()>0)
			{
				getBean().put("errorMessage", new StringBuilder(errorMessages.get(0).toString()));
			}
			else
			{
				getBean().put("errorMessage", new StringBuilder(RfidErrorCodes.NO_ERRORS.getErrorMessage()));
			}



			success = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Error processing AF RFID Rewrite process");
		} finally {
			getBean().put("eqDataReady", new StringBuilder("0"));
			if (success) {
				getBean().put("galcDataReady", new StringBuilder("1"),DeviceTagType.PLC_GALC_DATA_READY);
				getLogger().info("AF Rfid Write Process Successful");
			}
		}
		return success;
	}

	public void postPlcWrite(boolean writeSucceeded) {
	}

	public void validate() {
	}
}
