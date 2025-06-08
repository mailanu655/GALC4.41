package com.honda.galc.service.msip.handler.outbound;

import java.util.Date;
import java.util.List;

import com.honda.galc.service.IService;
import com.honda.galc.service.msip.dto.outbound.IMsipOutboundDto;

/**
 * @author Jeffray Huang, Subu Kathiresan
 * @date Jun 15, 2017
 */
public interface IMsipOutboundHandler<T extends IMsipOutboundDto> extends IService {
	
	//used for schedule - once daily to fetch all (including header, body, summary, tail
	public <D extends IMsipOutboundDto> List<D> fetchDetails();
	
	public <D extends IMsipOutboundDto> List<D> fetchDetails(Date startTimestamp);
	
	//used for schedule - incrementally fetch for the time period 
	public <D extends IMsipOutboundDto> List<D> fetchDetails(Date startTimestamp, int duration);
	
	public <H extends IMsipOutboundDto> H fetchHeader();
	
	public <F extends IMsipOutboundDto> List<F> fetchFooter();
}
