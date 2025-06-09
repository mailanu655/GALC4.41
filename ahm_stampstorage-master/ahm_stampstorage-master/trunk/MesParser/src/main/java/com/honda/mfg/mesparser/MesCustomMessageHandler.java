/*
 * Created on Aug 28, 2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.honda.mfg.mesparser;

/**
 * @author Sunil
 *         <p/>
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface MesCustomMessageHandler {


    public static final String GET_MAP_METHOD_NAME = "getMap";
    public static final String GET_ERROR_METHOD_NAME = "formatErrorMessage";
    public static final String META_DATA_EXTENSION = "_OUT";


    public String getMap(String sourceData);

    public String formatErrorMessage(String mapName, int errCode, String data);

}
