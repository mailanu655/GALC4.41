package com.honda.galc.service.msip.handler.inbound;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.entity.product.FrameMTOCMasterSpec;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.service.msip.dto.inbound.Gmm111Dto;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/**
 * @author Subu Kathiresan
 * @date May 3, 2017
 */
public class Gmm111Handler extends BaseMsipInboundHandler<BaseMsipPropertyBean, Gmm111Dto> {
	
	public Gmm111Handler() {}

	public boolean execute(List<Gmm111Dto> dtoList) {
		
		List<FrameMTOCMasterSpec> mtocSpecList = new ArrayList<FrameMTOCMasterSpec>();
		List<FrameSpec> specList = new ArrayList<FrameSpec>();
		try {
			for(Gmm111Dto dto: dtoList) {
				FrameMTOCMasterSpec mtocMasterSpec = dto.deriveframeMtocMasterSpec();
				mtocSpecList.add(mtocMasterSpec);
			
				FrameSpec frameSpec = dto.deriveFrameSpec();
				specList.add(frameSpec);
			}
			getFrameMtocMasterSpecDao().insertAll(mtocSpecList);
			getFrameSpecDao().insertAll(specList);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			return false;
		}
		return true;
	}
	
}
