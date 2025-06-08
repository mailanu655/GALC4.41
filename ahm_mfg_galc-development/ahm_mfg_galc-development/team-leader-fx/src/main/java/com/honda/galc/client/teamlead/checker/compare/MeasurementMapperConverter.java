package com.honda.galc.client.teamlead.checker.compare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import com.honda.galc.dto.MeasurementCheckerDto;
/**
 * 
 * <h3>MeasurementMapperConverter Class description</h3>
 * <p> MeasurementMapperConverter: Mapper Converter for Measurement Checker Comparison</p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * </TABLE>
 *   
 * @author Hemant Kumar<br>
 * Apr 30, 2018
 *
 */
public class MeasurementMapperConverter extends AbstractMapperConverter<MeasurementCheckerDto> {

	public MeasurementMapperConverter(List<MeasurementCheckerDto> fromDataList, List<MeasurementCheckerDto> toDataList) {
		super(fromDataList, toDataList);
	}

	/**
	 * This method is used to get mapper list to populate parent table
	 */
	@Override
	public List<MeasurementCheckerDtoMapper> getMapperList() {
		List<MeasurementCheckerDtoMapper> mappedData = new CopyOnWriteArrayList<MeasurementCheckerDtoMapper>();
		List<MeasurementCheckerDto> dtoList = new ArrayList<MeasurementCheckerDto>();
		dtoList.addAll(getFromDataList());
		dtoList.addAll(getToDataList());

		Set<String> keySet = new HashSet<String>();
		MeasurementCheckerDtoMapper mapper;
		for(MeasurementCheckerDto dto : dtoList) {
			if(!keySet.contains(dto.getKey())) {
				keySet.add(dto.getKey());
				mapper = new MeasurementCheckerDtoMapper(dto.getOperationName().substring(0, 6), dto.getMeasurementSeqNum(), 
						dto.getPartNo(), dto.getPartItemNo(), dto.getPartSectionCode(), dto.getPartType(), 
						getCheckerDataByKey(dto.getKey(), true), getCheckerDataByKey(dto.getKey(), false));
				if(isMapperDataValid(mapper, MeasurementCheckerDtoMapper.class)) {
					mappedData.add(mapper);
				}
			}
		}
		Collections.sort(mappedData);
		return mappedData;
	}
}
