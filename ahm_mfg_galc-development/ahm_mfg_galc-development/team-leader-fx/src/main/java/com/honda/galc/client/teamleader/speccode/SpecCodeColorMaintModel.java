package com.honda.galc.client.teamleader.speccode;

import java.util.List;

import com.honda.galc.client.mvc.AbstractModel;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.service.ServiceFactory;

public class SpecCodeColorMaintModel extends AbstractModel {

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public List<FrameSpecDto> getFramSpecList(String productIdTextField) {
		return ServiceFactory.getDao(FrameSpecDao.class).getProductIdDetails(productIdTextField);
	}

}
