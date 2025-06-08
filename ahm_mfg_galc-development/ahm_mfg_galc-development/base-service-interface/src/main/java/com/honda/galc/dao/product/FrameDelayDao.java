
package com.honda.galc.dao.product;


import java.util.List;

import com.honda.galc.entity.product.FrameDelay;
import com.honda.galc.entity.product.FrameDelayId;
import com.honda.galc.service.IDaoService;

/*
* 
* @author Gangadhararao Gadde 
* @since May 19, 2014
*/
public interface FrameDelayDao extends IDaoService<FrameDelay, FrameDelayId> {

	public List<FrameDelay> findDelayedVinList(String vin, String processPointId);

	public List<Object[]> getStragglerList(String processPointId);


}
