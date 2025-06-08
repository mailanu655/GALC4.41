package com.honda.galc.client.teamlead.checker.compare;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dto.OperationCheckerDto;
/**
 * 
 * <h3>OperationCheckerDtoMapper Class description</h3>
 * <p> OperationCheckerDtoMapper: Dto Mapper utility for Operation Checker Comparison</p>
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
public class OperationCheckerDtoMapper extends AbstractDtoMapper<OperationCheckerDto> implements Comparable<OperationCheckerDtoMapper>{

	private String unitNumber;
	private List<OperationCheckerDto> fromOperationCheckers = new ArrayList<OperationCheckerDto>();
	private List<OperationCheckerDto> toOperationCheckers = new ArrayList<OperationCheckerDto>();

	public OperationCheckerDtoMapper(String unitNumber, List<OperationCheckerDto> fromOperationCheckers,
			List<OperationCheckerDto> toOperationCheckers) {
		super();
		this.unitNumber = unitNumber;
		setFromOperationCheckers(fromOperationCheckers);
		setToOperationCheckers(toOperationCheckers);
	}
	
	public String getUnitNumber() {
		return unitNumber;
	}
	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}
	public List<OperationCheckerDto> getFromOperationCheckers() {
		return fromOperationCheckers;
	}
	public void setFromOperationCheckers(List<OperationCheckerDto> fromOperationCheckers) {
		this.fromOperationCheckers.addAll(fromOperationCheckers);
	}
	public List<OperationCheckerDto> getToOperationCheckers() {
		return toOperationCheckers;
	}
	public void setToOperationCheckers(List<OperationCheckerDto> toOperationCheckers) {
		List<OperationCheckerDto> addedList = new ArrayList<OperationCheckerDto>();
		for(OperationCheckerDto fromDto : this.fromOperationCheckers) {
			OperationCheckerDto toDto = getCheckerByCheckSeq(fromDto.getCheckSeq(), toOperationCheckers);
			if(toDto != null) {
				this.toOperationCheckers.add(toDto);
				addedList.add(toDto);
			} else {
				this.toOperationCheckers.add(new OperationCheckerDto());
			}
		}
		toOperationCheckers.removeAll(addedList);
		this.toOperationCheckers.addAll(toOperationCheckers);
	}
	private OperationCheckerDto getCheckerByCheckSeq(int checkSeq, List<OperationCheckerDto> dtoList) {
		for(OperationCheckerDto dto : dtoList) {
			if(dto.getCheckSeq() == checkSeq) {
				return dto;
			}
		}
		return null;
	}

	@Override
	public String getKey() {
		return getUnitNumber();
	}

	@Override
	public List<OperationCheckerDto> getFromCheckers() {
		return getFromOperationCheckers();
	}

	@Override
	public void setFromCheckers(List<OperationCheckerDto> fromCheckers) {
		setFromOperationCheckers(fromCheckers);
	}

	@Override
	public List<OperationCheckerDto> getToCheckers() {
		return getToOperationCheckers();
	}

	@Override
	public void setToCheckers(List<OperationCheckerDto> toCheckers) {
		setToOperationCheckers(toCheckers);
	}

	@Override
	public int compareTo(OperationCheckerDtoMapper o) {
		return this.getKey().compareTo(o.getKey());
	}

}
