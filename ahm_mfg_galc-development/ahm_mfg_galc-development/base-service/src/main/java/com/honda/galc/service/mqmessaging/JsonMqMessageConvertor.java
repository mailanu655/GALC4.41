package com.honda.galc.service.mqmessaging;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerJSONUtil;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.service.mq.IMqMessageConvertor;

public class JsonMqMessageConvertor implements IMqMessageConvertor {

	@Override
	public String convert(DataContainer dc) throws Exception {
		dc.remove(DataContainerTag.TAG_LIST);
		return DataContainerJSONUtil.convertToJSON(dc,true);
	}

}
