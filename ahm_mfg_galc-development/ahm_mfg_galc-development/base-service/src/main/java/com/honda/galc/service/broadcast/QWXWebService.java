package com.honda.galc.service.broadcast;

import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.rpc.ServiceException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.qwx.ws.IWebserviceGateway;
import com.honda.galc.service.qwx.ws.ServerResponse;
import com.honda.galc.service.qwx.ws.WebserviceGateway;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.StringUtil;
/**
 * 
 * <h3>QWXWebService Class description</h3>
 * <p> QWXWebService description </p>
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
 * @author K Maharjan<br>
 * Jan 03 14
 *
 *
 */
public class QWXWebService implements IExternalService{
private Logger logger;
private String ppid=null;
	
	public DataContainer execute(String methodDisplayName,DataContainer dc) {
		try{
			ppid=dc.getString(DataContainerTag.PROCESS_POINT_ID);
			logger = Logger.getLogger(ppid);
			logger.info("start to invoke QualityWorx web service method " + methodDisplayName);
			
			basicExecute(methodDisplayName,dc);
		}catch(Exception ex){
			DataContainerUtil.error(logger, dc, ex, "Exception Occured");
		}
		return dc;
	}
	
	private void basicExecute(String methodDisplayName,DataContainer dc) throws ServiceException,RemoteException,MalformedURLException, JAXBException{
		if(GATEWAY_SERVICE.equalsIgnoreCase(methodDisplayName)){
			sendQualityWorx(dc);
		}
		else {
			DataContainerUtil.error(logger, dc, "invalid QualityWorx web service Method \"" + methodDisplayName + "\" ");
			return;
		}
		logger.info(" invoke QualityWorx web service " + methodDisplayName + " successfully");
	}
	
	public void sendQualityWorx(DataContainer dc) throws ServiceException,RemoteException,MalformedURLException, JAXBException{
		String model=dc.getString(DataContainerTag.PRODUCT_SPEC_CODE);
		if(isShortProductSpec(ppid)){
			dc.put(DataContainerTag.PRODUCT_SPEC_CODE, StringUtils.substring(model, 0, 7));
			dc.put(DataContainerTag.PRODUCT_TYPE,StringUtil.properCase(dc.getString(DataContainerTag.PRODUCT_TYPE)));
			model=dc.getString(DataContainerTag.PRODUCT_SPEC_CODE);	
		}
		String partType=dc.getString(DataContainerTag.PRODUCT_TYPE);
		String serialNumber=dc.getString(DataContainerTag.PRODUCT_ID);
		ServerResponse sr=null;
		if(dc.containsKey(DataContainerTag.DEFECT_REPAIRED)){
			logger.info("Sending Defect repaired inforamtion to Qualityworx");
			String xmlString=getXML(dc);
			boolean validateXML=validateAgainstXSD(xmlString);
			if (validateXML) {
				logger.info("Calling storeSph Service");
				sr=getWSClient().storeSph(xmlString);
				processServiceResponse(sr);
				if (showViewer(ppid)){
					getWSClient().closeViewer();
					logger.info("Calling SPHViewer Service");
					sr=getWSClient().showSphViewer(serialNumber, model, partType);
					processServiceResponse(sr);
				}
			} else
				throw new ServiceException("Invalid XML");
		} else {
			if (showViewer(ppid)){
				logger.info("Calling SPHViewer Service");
				sr=getWSClient().showSphViewer(serialNumber, model, partType);
				processServiceResponse(sr);
			}
				
		}
		if(isOffProcess(ppid)&& !hasOutstanding(serialNumber)){
			logger.info("Calling SPHViewer Service");
			sr=getWSClient().autoRepairPart(serialNumber, model, partType);
			processServiceResponse(sr);
		}
			
	}
	
	public void processServiceResponse(ServerResponse sr) throws ServiceException{
		logger.info("Service ran:"+sr.getResponseType().getValue().toString()+" Status: "+sr.getResponseBody().getValue().toString());
		if(sr.getStatusCode()<0)
		throw new ServiceException("Unable to run service "+sr.getResponseType().getValue()+" due to "+sr.getResponseBody().getValue().toString());
	}
	
	public boolean hasOutstanding(String productId){
		DefectResultDao defectdao=ServiceFactory.getDao(DefectResultDao.class);
		List<DefectResult> defects=defectdao.findAllByProductId(productId);
		for(DefectResult defectResult : defects) {
			if(defectResult.isOutstandingStatus()) return true;
		}
		return false;
	}
	public ProductCheckUtil getProductCheckUtil(BaseProduct product, ProcessPoint processPoint){
		return new ProductCheckUtil(product, processPoint);
	}
	
	private IWebserviceGateway getWSClient()throws ServiceException,RemoteException,MalformedURLException{
		String url=getURL();	
		return StringUtils.isEmpty(url) ?new WebserviceGateway().getBasicHttpBindingIWebserviceGateway(getEndPoint())
				:new WebserviceGateway(new URL(url)).getBasicHttpBindingIWebserviceGateway(getEndPoint());
	}
	
	private String getURL(){
		return PropertyService.getProperty(ppid, "QWX_WEB_SERVICE_URL");
	}

	private String getEndPoint(){
    	return PropertyService.getProperty(ppid, "QWX_WEB_SERVICE_ENDPOINT");    	
    }
	
	private boolean showViewer(String pPID) {
		return PropertyService
				.getPropertyBoolean(pPID, "SHOW_SPHVIEWER", true);
	}
	
	private boolean isShortProductSpec(String pPID) {
		return PropertyService
				.getPropertyBoolean(pPID, "SHORT_PRODUCT_SPEC", false);
	}
	private boolean isOffProcess(String pPID) {
		return PropertyService
				.getPropertyBoolean(pPID, "SEND_ALL_CLEAR", false);
	}
	
	private String getXML(DataContainer dc) throws JAXBException{
		QWXXMLConvertor sxc=new QWXXMLConvertor();
		return sxc.convertToXML(dc);
	}
	
	private boolean validateAgainstXSD(String xml){
		try{
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(getClass().getClassLoader()
                    .getResourceAsStream("/SinglePart.xsd")));
            Validator validator  = schema.newValidator();
            Source source=new StreamSource(new StringReader(xml));
            validator.validate(source);
            return true;
        }catch(Exception exe){
        	logger.info("Invalid XML "+exe);
            return false;
        }
	}
}
