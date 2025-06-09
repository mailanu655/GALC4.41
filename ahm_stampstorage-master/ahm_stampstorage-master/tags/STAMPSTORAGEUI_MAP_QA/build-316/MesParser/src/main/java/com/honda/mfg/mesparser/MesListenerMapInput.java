package com.honda.mfg.mesparser;

import com.ils_tech.dw.ei.DWCustomDataHolder;

/**
 * User: Adam S. Kendell
 * Date: 5/17/11
 */
public interface MesListenerMapInput {

    public DWCustomDataHolder processInput(String inputString);

}
