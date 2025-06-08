package com.honda.galc.client.teamlead.checker.compare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import com.honda.galc.dto.PartCheckerDto;
/**
 * 
 * <h3>PartMapperConverter Class description</h3>
 * <p> PartMapperConverter: Mapper Converter for Part Checker Comparison</p>
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
public class PartMapperConverter extends AbstractMapperConverter<PartCheckerDto> {

	public PartMapperConverter(List<PartCheckerDto> fromDataList, List<PartCheckerDto> toDataList) {
		super(fromDataList, toDataList);
	}

	@Override
	public List<PartCheckerDtoMapper> getMapperList() {
		List<PartCheckerDtoMapper> mappedData = new CopyOnWriteArrayList<PartCheckerDtoMapper>();
		List<PartCheckerDto> dtoList = new ArrayList<PartCheckerDto>();
		dtoList.addAll(getFromDataList());
		dtoList.addAll(getToDataList());

		Set<String> keySet = new HashSet<String>();
		PartCheckerDtoMapper mapper;
		for(PartCheckerDto dto : dtoList) {
			if(!keySet.contains(dto.getKey())) {
				keySet.add(dto.getKey());
				mapper = new PartCheckerDtoMapper(dto.getOperationName().substring(0, 6), 
						dto.getPartNo(), dto.getPartItemNo(), dto.getPartSectionCode(), dto.getPartType(), 
						getCheckerDataByKey(dto.getKey(), true), getCheckerDataByKey(dto.getKey(), false));
				if(isMapperDataValid(mapper, PartCheckerDtoMapper.class)) {
					mappedData.add(mapper);
				}
			}
		}
		Collections.sort(mappedData);
		return mappedData;
	}
}
