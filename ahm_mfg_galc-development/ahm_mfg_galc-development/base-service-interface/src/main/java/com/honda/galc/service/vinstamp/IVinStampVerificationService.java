/**
 * 
 */
package com.honda.galc.service.vinstamp;

import com.honda.galc.service.IService;

/**
 * @author Subu Kathiresan
 *
 */
public interface IVinStampVerificationService<T> extends IService {
	Boolean verify(T vin, T rfid);
}
