package com.honda.galc.client.dc.validator;

import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.conf.MCOperationPartRevision;

/**
 * @author Subu Kathiresan
 * @date Jun 23, 2014
 */
public interface IPartSnInputValidator extends InputValidator<PartSerialScanData>{

	public boolean validate(String ProductId, PartSerialScanData data, MCOperationPartRevision partSpec);
}
