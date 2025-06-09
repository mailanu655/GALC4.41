package com.honda.mfg.mesproxy;

/**
 * User: Adam S. Kendell
 * Date: 6/16/11
 */
public interface ConnectionManager {

    public boolean offerToInputQueue(String input);

    public String getMessageTerminator();

    public void addConnection(Connection c);

    public void removeConnection(Connection c);

    public void connectionError(Connection c, String msg);

}
