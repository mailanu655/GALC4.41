/*
 * Created on Aug 28, 2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.honda.mfg.mesparser;

import java.util.HashMap;

/**
 * @author Sunil
 *         <p/>
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MesCustomDataHolder {

    public final static int DW_TRANSFORM_UNSUCCESFUL = -4322;

    String listenerMapName;
    String sequence;
    HashMap items;
    int status;
    String errorMessage;

    public MesCustomDataHolder(String evName, String seqNum, HashMap itemList) {
        listenerMapName = evName;
        sequence = seqNum;
        items = itemList;
        status = 0;
        errorMessage = "";
    }

    /**
     * @return
     */
    public String getEventName() {
        return listenerMapName;
    }

    /**
     * @return
     */
    public HashMap getItems() {
        return items;
    }

    /**
     * @return
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * @return
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @return
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param string
     */
    public void setErrorMessage(String string) {
        errorMessage = string;
    }

    /**
     * @param i
     */
    public void setStatus(int i) {
        status = i;
    }

}
