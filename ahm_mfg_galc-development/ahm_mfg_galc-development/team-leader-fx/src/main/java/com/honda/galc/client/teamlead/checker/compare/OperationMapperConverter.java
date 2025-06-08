package com.honda.galc.client.teamlead.checker.compare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import com.honda.galc.dto.OperationCheckerDto;
/**
 * 
 * <h3>OperationMapperConverter Class description</h3>
 * <p> OperationMapperConverter: Mapper Converter for Operation Checker Comparison</p>
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
public class OperationMapperConverter extends AbstractMapperConverter<OperationCheckerDto> {

	public OperationMapperConverter(List<OperationCheckerDto> fromDataList, List<OperationCheckerDto> toDataList) {
		super(fromDataList, toDataList);
	}

	@Override
	public List<OperationCheckerDtoMapper> getMapperList() {
		List<OperationCheckerDtoMapper> mappedData = new CopyOnWriteArrayList<OperationCheckerDtoMapper>();
		List<OperationCheckerDto> dtoList = new ArrayList<OperationCheckerDto>();
		dtoList.addAll(getFromDataList());
		dtoList.addAll(getToDataList());

		Set<String> keySet = new HashSet<String>();
		OperationCheckerDtoMapper mapper;
		for(OperationCheckerDto dto : dtoList) {
			if(!keySet.contains(dto.getKey())) {
				keySet.add(dto.getKey());
				mapper = new OperationCheckerDtoMapper(dto.getKey(), 
						getCheckerDataByKey(dto.getKey(), true), getCheckerDataByKey(dto.getKey(), false));
				if(isMapperDataValid(mapper, OperationCheckerDtoMapper.class)) {
					mappedData.add(mapper);
				}
			}
		}
		Collections.sort(mappedData);
		return mappedData;
	}
}
