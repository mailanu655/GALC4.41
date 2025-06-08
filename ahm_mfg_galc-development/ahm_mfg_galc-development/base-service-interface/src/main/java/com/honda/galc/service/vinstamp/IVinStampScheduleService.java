/**
 * 
 */
package com.honda.galc.service.vinstamp;

import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.IService;

/**
 * @author Subu Kathiresan
 *
 */
public interface IVinStampScheduleService<T extends BaseProduct> extends IService {
	public T getSchedule(T currentVin);
}
