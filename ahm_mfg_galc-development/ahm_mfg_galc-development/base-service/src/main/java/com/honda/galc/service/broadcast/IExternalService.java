package com.honda.galc.service.broadcast;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.honda.galc.data.DataContainer;

/**
 * 
 * <h3>IExternalService Class description</h3>
 * <p> IExternalService description </p>
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
 * K Maharjan
 * Jan 06, 2014
 */
public interface IExternalService {
	
	public final static String HEAD_LIGHT_ALIGNMENT = 	"HEAD_LIGHT_ALIGNMENT";
	public final static String WHEEL_ALIGNMENT		=	"WHEEL_ALIGNMENT";
	public final static String KEMKRAFT_DATA		=	"KEMKRAFT_DATA";
	public final static String GATEWAY_SERVICE		=	"GATEWAY_SERVICE";
	
	@SuppressWarnings("serial")
	public final static Map<String,Class<? extends IExternalService>> EXT_SERVICIES  =
		Collections.unmodifiableMap(
                new HashMap<String, Class<? extends IExternalService>>() {
                    {
                        put("NCAT_WS",NCATWebService.class);
                        put("QUALITYWORX_WS",QWXWebService.class);
                     }
                });
	public final static String[][] EXT_SERVICE_METHODS  =
	{ 	
		{"NCAT_WS",HEAD_LIGHT_ALIGNMENT},
		{"NCAT_WS",WHEEL_ALIGNMENT},
		{"NCAT_WS",KEMKRAFT_DATA},
		{"QUALITYWORX_WS",GATEWAY_SERVICE}
	};
	
	public DataContainer execute(String methodDisplayName,DataContainer dc);
}
