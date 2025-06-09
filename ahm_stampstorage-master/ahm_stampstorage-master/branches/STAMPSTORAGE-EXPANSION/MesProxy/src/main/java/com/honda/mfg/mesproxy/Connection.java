package com.honda.mfg.mesproxy;

/**
 * User: Adam S. Kendell
 * Date: 6/16/11
 */
public interface Connection {
    public boolean offerToInputQueue(String input);
    public String getConnectionName();
    public void writeToOutputStream(String output);
    public void stop();
}
