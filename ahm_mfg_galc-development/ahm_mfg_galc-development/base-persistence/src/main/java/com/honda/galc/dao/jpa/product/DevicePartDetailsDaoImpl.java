package com.honda.galc.dao.jpa.product;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.DevicePartDetailsDao;
import com.honda.galc.entity.product.DevicePartDetails;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.service.Parameters;

public class DevicePartDetailsDaoImpl  extends BaseDaoImpl<DevicePartDetails, LotControlRule> implements DevicePartDetailsDao {

	private static final String QUERY ="select a.PART_NAME as partName ,c.PART_SERIAL_NUMBER_MASK as partSerialNumberMask , "+
	"a.INSTRUCTION_CODE as instructionCode , C.PART_MAX_ATTEMPTS as partMaxAttempts , "+ 
	"d.MEASUREMENT_SEQ_NUM as measurementSeqNum , d.MINIMUM_LIMIT as minimumLimit , " +
	"d.MAXIMUM_LIMIT as maximumLimit , d.MAX_ATTEMPTS as maxAttempts "+ 
	"from galadm.gal246tbx a join gal245tbx b on a.PART_NAME=b.PART_NAME "+
	"and a.MODEL_YEAR_CODE=b.MODEL_YEAR_CODE and a.MODEL_CODE=b.MODEL_CODE "+
	"and a.MODEL_TYPE_CODE=b.MODEL_TYPE_CODE and a.MODEL_OPTION_CODE=b.MODEL_OPTION_CODE "+
	"and a.EXT_COLOR_CODE=b.EXT_COLOR_CODE and a.INT_COLOR_CODE=b.INT_COLOR_CODE "+
	"join galadm.part_spec_tbx c on b.PART_NAME=c.PART_NAME and b.PART_ID=c.PART_ID "+
	"left join galadm.measurement_spec_tbx d on b.PART_NAME=d.PART_NAME "+
	"and b.PART_ID=d.PART_ID where a.PROCESS_POINT_ID=?1 "+
	"and a.MODEL_YEAR_CODE=?2 and a.MODEL_CODE=?3 and a.MODEL_OPTION_CODE=?4 "+
	"and a.MODEL_TYPE_CODE=?5 and a.EXT_COLOR_CODE=?6 and a.INT_COLOR_CODE=?7 "+
	"and a.INSTRUCTION_CODE<>'0' order by a.SEQUENCE_NUMBER, c.PART_DESCRIPTION, d.MEASUREMENT_SEQ_NUM";

	public List<DevicePartDetails> findAllPartDetailsByLotControlRule(LotControlRule rule) {

		Parameters params = new Parameters();
		params.put("1", rule.getId().getProcessPointId());
		params.put("2", rule.getId().getModelYearCode());
		params.put("3", rule.getId().getModelCode());
		params.put("4", rule.getId().getModelOptionCode());
		params.put("5", rule.getId().getModelTypeCode());
		params.put("6", rule.getId().getExtColorCode());
		params.put("7", rule.getId().getIntColorCode());

		List <DevicePartDetails> result = findAllByNativeQuery(QUERY, params);

		int rowCount = result.size();
		if (rowCount > 0) {
			return result;			
		} else {
			System.out.println("No Part Details Available for current rule");
		}
		return new ArrayList<DevicePartDetails>();

	}


}
