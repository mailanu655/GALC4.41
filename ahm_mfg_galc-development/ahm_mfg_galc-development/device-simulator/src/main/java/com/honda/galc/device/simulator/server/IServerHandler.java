package com.honda.galc.device.simulator.server;

import com.honda.galc.data.DataContainer;


public interface IServerHandler {
	public DataContainer handleRequest(DataContainer data);
}
