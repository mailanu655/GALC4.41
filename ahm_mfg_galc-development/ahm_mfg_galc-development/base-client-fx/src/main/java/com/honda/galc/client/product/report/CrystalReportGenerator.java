package com.honda.galc.client.product.report;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;


public class CrystalReportGenerator
{
	private static final String LANGUAGE ="ENGLISH";
    public CrystalReportGenerator(){
    }

    
    
    private IEnterpriseSession generateNewIEnterpriseSession()  {
    	IEnterpriseSession enterpriseSession = null;
    	SystemPropertyBean systemPropertyBean =  PropertyService.getPropertyBean(SystemPropertyBean.class);
    	try{
    		String userId = systemPropertyBean.getCrystalReportUserid();
    		String password = systemPropertyBean.getCrystalReportPassword();
    		String cmsName = systemPropertyBean.getCrystalReportCmsName();
    		String authenticationType = systemPropertyBean.getCrystalReportAuthenticationType();
    		ISessionMgr sessionMgr = CrystalEnterprise.getSessionMgr();
    		enterpriseSession = sessionMgr.logon(userId, password, cmsName, authenticationType);
    		}catch (Exception e){
    			Logger.getLogger().error("Error generating the Crystal report enterprise connection ");
    	}
    	return enterpriseSession;
    }
    
    
    public IEnterpriseSession getEnterpriseSession() 
    {
        IEnterpriseSession enterpriseSession = null;
        enterpriseSession = generateNewIEnterpriseSession();
        return enterpriseSession;
    }
    
    public String getInfoViewURL(Map<String,String> oReportMeta) throws UnsupportedEncodingException    {
    	SystemPropertyBean systemPropertyBean =  PropertyService.getPropertyBean(SystemPropertyBean.class);
    	String url = systemPropertyBean.getProcessChangeReportUrl();
    	StringBuffer sbURL = new StringBuffer();
		IEnterpriseSession enterpriseSession = getEnterpriseSession();			
		String logonToken = null;
		if(null!=enterpriseSession){
			try {
				logonToken = enterpriseSession.getLogonTokenMgr().getDefaultToken();
			} catch (Exception e) {
				Logger.getLogger().error("Error generating the Crystal report default token ");
			}
			sbURL.append(url);
			sbURL.append("&apstoken=");								
	    	sbURL.append(logonToken);								
	    	sbURL.append("&cmd=EXPORT&EXPORT_FMT=U2FPDF:0");		
	    	sbURL.append("&id=");									
	    	sbURL.append(systemPropertyBean.getReportId());
	  
	    	Iterator entries = oReportMeta.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry pairs = (Map.Entry) entries.next();
				sbURL.append("&promptex-" + pairs.getKey().toString().trim() + "=\"" + URLEncoder.encode(pairs.getValue().toString(),"UTF-8") +"\"");
			}
			sbURL.append("&promptex-LANGUAGE=\""+LANGUAGE+"\"");
			sbURL.append("&promptex-SCHEMA="+"\""+systemPropertyBean.getCrystalReportSchema()+"\"");
	    	return sbURL.toString();
		}else{
			return "";
		}
		
    }
    
    
}
