package com.honda.mfg.device.plc.omron;

/**
 * User: Jeffrey M Lutz
 * Date: 4/8/11
 */
public interface FinsInitializeState {
    int getSourceNode();

    int getDestinationNode();

    boolean isInitialized();
}
