
package com.honda.galc.service.weld;

import com.honda.galc.dto.rest.ProductTrackDTO;

/**
 *
 * @author Wade Pei <br>
 * @date   Oct 22, 2013
 */
public interface WeldOrderService {

	/**
	 * This method is used for asynchronous tracking of Weld product.
	 * @param trackDTO
	 */
	ProductTrackDTO trackProduct(ProductTrackDTO trackDTO);

}
