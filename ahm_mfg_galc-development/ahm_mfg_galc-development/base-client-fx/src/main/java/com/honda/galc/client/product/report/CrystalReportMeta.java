package com.honda.galc.client.product.report;


import java.util.HashMap;
import java.util.Map;

public class CrystalReportMeta
{

	private String _reportName = "";
	private Map _reportParams = null;
	private String _storedProcName = "";

	public CrystalReportMeta()
    {
        super();
    }
	
	public void setReportName(String sReportName)
	{
		_reportName = sReportName;
	}
	
	public String getReportName()
	{
		return _reportName;
	}
	
	public void setReportParams(Map params)
	{
		_reportParams = params;
	}
     
	public Map getReportParams()
	{
		if(_reportParams != null)
		{
		   return _reportParams;
		}
		else
		{
			return new HashMap();
		}
	}

	public void setStoredProcName(String sStoredProcName)
	{
		_storedProcName = sStoredProcName;
	}
	
	public String getStoredProcName()
	{
		return  _storedProcName;
	}	
}