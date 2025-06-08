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
public interface PartsSelectionService extends IService {

	public DataContainer getBinDataByProductId(DefaultDataContainer data);
}
