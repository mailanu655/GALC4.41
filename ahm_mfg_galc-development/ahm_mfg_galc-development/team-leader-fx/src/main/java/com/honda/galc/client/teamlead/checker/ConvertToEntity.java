package com.honda.galc.client.teamlead.checker;

import com.honda.galc.checkers.ReactionType;
import com.honda.galc.dto.ApplicationCheckerDto;
import com.honda.galc.dto.MeasurementCheckerDto;
import com.honda.galc.dto.OperationCheckerDto;
import com.honda.galc.dto.PartCheckerDto;
import com.honda.galc.entity.conf.MCAppChecker;
import com.honda.galc.entity.conf.MCAppCheckerId;
import com.honda.galc.entity.conf.MCMeasurementChecker;
import com.honda.galc.entity.conf.MCMeasurementCheckerId;
import com.honda.galc.entity.conf.MCOperationChecker;
import com.honda.galc.entity.conf.MCOperationCheckerId;
import com.honda.galc.entity.conf.MCPartChecker;
import com.honda.galc.entity.conf.MCPartCheckerId;

public class ConvertToEntity {

	public static MCAppChecker convertToEntity(ApplicationCheckerDto appCheckerDto) {
		MCAppChecker appChecker = new MCAppChecker();
		MCAppCheckerId id = new MCAppCheckerId();
		id.setApplicationId(appCheckerDto.getApplicationId());
		id.setCheckPoint(appCheckerDto.getCheckPoint());
		id.setCheckSeq(appCheckerDto.getCheckSeq());
		appChecker.setId(id);
		appChecker.setChecker(appCheckerDto.getChecker());
		appChecker.setCheckName(appCheckerDto.getCheckName());
		ReactionType rt = ReactionType.getReactionType(appCheckerDto.getReactionType()); 
		appChecker.setReactionType(rt);
		return appChecker;
	}
	
	public static MCOperationChecker convertToEntity(OperationCheckerDto opCheckerDto) {
		MCOperationChecker oc = new MCOperationChecker();
		MCOperationCheckerId id = new MCOperationCheckerId();
		id.setOperationName(opCheckerDto.getOperationName());
		id.setOperationRevision(opCheckerDto.getOperationRevision());
		id.setCheckPoint(opCheckerDto.getCheckPoint());
		id.setCheckSeq(opCheckerDto.getCheckSeq());
		oc.setId(id);
		oc.setChecker(opCheckerDto.getChecker());
		oc.setCheckName(opCheckerDto.getCheckName());
		ReactionType rt = ReactionType.getReactionType(opCheckerDto.getReactionType()); 
		oc.setReactionType(rt);
		return oc;		
	}
	
	public static MCPartChecker convertToEntity(PartCheckerDto partCheckerDto) {
		MCPartChecker pc = new MCPartChecker();
		MCPartCheckerId id = new MCPartCheckerId();
		id.setOperationName(partCheckerDto.getOperationName());
		id.setOperationRevision(partCheckerDto.getOperationRevision());
		id.setCheckPoint(partCheckerDto.getCheckPoint());
		id.setCheckSeq(partCheckerDto.getCheckSeq());
		id.setPartId(partCheckerDto.getPartId());
		pc.setId(id);
		pc.setChecker(partCheckerDto.getChecker());
		pc.setCheckName(partCheckerDto.getCheckName());
		ReactionType rt = ReactionType.getReactionType(partCheckerDto.getReactionType()); 
		pc.setReactionType(rt);
		return pc;		
	}
	
	public static MCMeasurementChecker convertToEntity(MeasurementCheckerDto measCheckerDto) {
		MCMeasurementChecker mc = new MCMeasurementChecker();
		MCMeasurementCheckerId id = new MCMeasurementCheckerId();
		id.setOperationName(measCheckerDto.getOperationName());
		id.setOperationRevision(measCheckerDto.getOperationRevision());
		id.setCheckPoint(measCheckerDto.getCheckPoint());
		id.setCheckSeq(measCheckerDto.getCheckSeq());
		id.setPartId(measCheckerDto.getPartId());
		id.setCheckName(measCheckerDto.getCheckName());
		id.setMeasurementSeqNumber(measCheckerDto.getMeasurementSeqNum());
		mc.setId(id);
		mc.setChecker(measCheckerDto.getChecker());
		ReactionType rt = ReactionType.getReactionType(measCheckerDto.getReactionType()); 
		mc.setReactionType(rt);
		return mc;
	}
}
