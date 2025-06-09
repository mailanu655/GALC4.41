package com.honda.mfg.device.mesmodule.messages;

/**
 * User: vcc30690
 * Date: 4/12/11
 */
public class GeneralMessage extends MesMessageBase {

    // usage:  new GeneralMessage("GeneralMessage{ a:'{b:'{c:'hi'}'}' }")
    // usage:  new GeneralMessage("...")
    public GeneralMessage(String message) {
        super(message);
    }
}
