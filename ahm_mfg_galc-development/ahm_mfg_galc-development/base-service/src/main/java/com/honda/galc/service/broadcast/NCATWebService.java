package com.honda.galc.service.broadcast;

import ham.honda.com.HeadLightAlignmentBE;
import ham.honda.com.KemkraftDataBE;
import ham.honda.com.NCATCollectorLocator;
import ham.honda.com.NCATCollectorSoap;
import ham.honda.com.WheelAlignmentBE;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>NCATWebService Class description</h3>
 * <p> NCATWebService description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Aug 20, 2013
 *
 *
 */
public class NCATWebService implements IExternalService{
	
	private Logger logger;
	
	public DataContainer execute(String methodDisplayName,DataContainer dc) {
		try{
			logger = Logger.getLogger(dc.getString(DataContainerTag.PROCESS_POINT_ID));
			logger.info("start to invoke NCAT web service method " + methodDisplayName);
			
			basicExecute(methodDisplayName,dc);
		}catch(Exception ex){
			DataContainerUtil.error(logger, dc, ex, "Exception Occured");
		}
		return dc;
	}
	
	private void basicExecute(String methodDisplayName,DataContainer dc) throws ServiceException,RemoteException,MalformedURLException{
		if(HEAD_LIGHT_ALIGNMENT.equalsIgnoreCase(methodDisplayName))
			insertHeadLightAlignment(dc);
		else if(WHEEL_ALIGNMENT.equalsIgnoreCase(methodDisplayName))
			insertWheelAlignment(dc);
		else if(KEMKRAFT_DATA.equalsIgnoreCase(methodDisplayName))
			insertKemkraftData(dc);
		else {
			DataContainerUtil.error(logger, dc, "invalid NCAT web service Method \"" + methodDisplayName + "\" ");
			return;
		}
		logger.info(" invoke NCAT web service " + methodDisplayName + " successfully");
	}
	
	public void insertHeadLightAlignment(DataContainer dc) throws ServiceException,RemoteException,MalformedURLException{
		HeadLightAlignmentBE data = createHeadLightAlignmentData(dc);
		getWebService().headLightAlignmentInsert(data);
	}
	
	public void insertKemkraftData(DataContainer dc)throws ServiceException,RemoteException,MalformedURLException{
		KemkraftDataBE data = createKemkraftDataBEData(dc);
		getWebService().kemkraftDataInsert(data);
	}
	
	public WheelAlignmentBE insertWheelAlignment(DataContainer dc) throws ServiceException,RemoteException,MalformedURLException{
		WheelAlignmentBE item = createWheelAlignmentBEData(dc);
		getWebService().wheelAlignmentInsert(item);
		return item;
	}
	
	private HeadLightAlignmentBE createHeadLightAlignmentData(DataContainer dc){
		HeadLightAlignmentBE item = new HeadLightAlignmentBE();
		DataContainerUtil.populateObjectValues(item, dc, logger);
		return item;
	}
	
	private KemkraftDataBE createKemkraftDataBEData(DataContainer dc){
		KemkraftDataBE item = new KemkraftDataBE();
		DataContainerUtil.populateObjectValues(item, dc, logger);
		return item;
	}
	
	private WheelAlignmentBE createWheelAlignmentBEData(DataContainer dc){
		WheelAlignmentBE item = new WheelAlignmentBE();
		DataContainerUtil.populateObjectValues(item, dc, logger);
		return item;
	}
	
	private NCATCollectorSoap getWebService() throws ServiceException,RemoteException,MalformedURLException{
		String url = getURL();
		return StringUtils.isEmpty(url) ? new NCATCollectorLocator().getNCATCollectorSoap()
				: new NCATCollectorLocator().getNCATCollectorSoap(new URL(url));
	}
	
	private String getURL(){
		return PropertyService.getProperty("WEB_SERVICE", "NCAT_WEB_SERVICE_URL");
	}
}
