package com.honda.mfg.mesproxy;

/**
 * User: Adam S. Kendell
 * Date: 6/16/11
 */
public interface ConnectionAgentManager {
    public boolean offerToInputQueue(String input);

    public void agentHasDied();
}
