/**
 * 
 */
package com.honda.galc.service;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;

/**
 * @author vf031824
 *
 */
public interface PartsLoadingService extends IService{

	public DataContainer execute(DefaultDataContainer data);

	public DataContainer execute(DataContainer data);

	public DataContainer getBinNameByPartNumber(DefaultDataContainer data);
}
