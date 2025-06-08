package com.honda.galc.service.msip.handler.inbound;

import java.util.List;

import com.honda.galc.service.msip.dto.inbound.IMsipInboundDto;
import com.honda.galc.service.IService;

/**
 * @author Subu Kathiresan
 * @date Mar 27, 2017
 */
public interface IMsipInboundHandler<T extends IMsipInboundDto> extends IService {
	
	public boolean execute(List<T> dtoList); 
}
