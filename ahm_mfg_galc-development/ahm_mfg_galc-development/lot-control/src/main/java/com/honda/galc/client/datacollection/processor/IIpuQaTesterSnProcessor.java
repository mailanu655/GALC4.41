/**
 * 
 */
package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.device.ipuqatester.model.UnitInTest;

/**
 * @author Subu Kathiresan
 * @Date Apr 26, 2012
 *
 */
public interface IIpuQaTesterSnProcessor extends IDataCollectionTaskProcessor<UnitInTest> {
	public boolean execute(UnitInTest torque);
}
