package com.honda.galc.service.broadcast;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.honda.galc.data.DataContainer;

public interface IMqAssembler {
	@SuppressWarnings("serial")
	public final static Map<String,Class<? extends IMqAssembler>> MQ_ASSEMBLERS  =
		Collections.unmodifiableMap(
                new HashMap<String, Class<? extends IMqAssembler>>() {
                    {	
                    	put("Default",MqAssembler.class);
                     }
                });
	
	public DataContainer execute(DataContainer dc);
	
}
