package com.honda.galc.client.teamlead.checker.compare;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dto.PartCheckerDto;
/**
 * 
 * <h3>PartCheckerDtoMapper Class description</h3>
 * <p> PartCheckerDtoMapper: Dto Mapper utility for Part Checker Comparison</p>
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
public class PartCheckerDtoMapper extends AbstractDtoMapper<PartCheckerDto> implements Comparable<PartCheckerDtoMapper>{
	
	private String unitNumber;
	private String partNo;
	private String partItemNo;
	private String partSectionCode;
	private String partType;
	private List<PartCheckerDto> fromPartCheckers = new ArrayList<PartCheckerDto>();
	private List<PartCheckerDto> toPartCheckers = new ArrayList<PartCheckerDto>();
	
	public PartCheckerDtoMapper(String unitNumber, String partNo, String partItemNo, String partSectionCode,
			String partType, List<PartCheckerDto> fromPartCheckers, List<PartCheckerDto> toPartCheckers) {
		super();
		this.unitNumber = unitNumber;
		this.partNo = partNo;
		this.partItemNo = partItemNo;
		this.partSectionCode = partSectionCode;
		this.partType = partType;
		setFromPartCheckers(fromPartCheckers);
		setToPartCheckers(toPartCheckers);
	}

	public String getUnitNumber() {
		return unitNumber;
	}
	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}
	public String getPartNo() {
		return partNo;
	}
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	public String getPartItemNo() {
		return partItemNo;
	}
	public void setPartItemNo(String partItemNo) {
		this.partItemNo = partItemNo;
	}
	public String getPartSectionCode() {
		return partSectionCode;
	}
	public void setPartSectionCode(String partSectionCode) {
		this.partSectionCode = partSectionCode;
	}
	public String getPartType() {
		return partType;
	}
	public void setPartType(String partType) {
		this.partType = partType;
	}
	public List<PartCheckerDto> getFromPartCheckers() {
		return fromPartCheckers;
	}
	public void setFromPartCheckers(List<PartCheckerDto> fromPartCheckers) {
		this.fromPartCheckers.addAll(fromPartCheckers);
	}
	public List<PartCheckerDto> getToPartCheckers() {
		return toPartCheckers;
	}
	public void setToPartCheckers(List<PartCheckerDto> toPartCheckers) {
		List<PartCheckerDto> addedList = new ArrayList<PartCheckerDto>();
		for(PartCheckerDto fromDto : this.fromPartCheckers) {
			PartCheckerDto toDto = getCheckerByCheckSeq(fromDto.getCheckSeq(), toPartCheckers);
			if(toDto != null) {
				this.toPartCheckers.add(toDto);
				addedList.add(toDto);
			} else {
				this.toPartCheckers.add(new PartCheckerDto());
			}
 		}
		toPartCheckers.removeAll(addedList);
		this.toPartCheckers.addAll(toPartCheckers);
	}
	private PartCheckerDto getCheckerByCheckSeq(int checkSeq, List<PartCheckerDto> dtoList) {
		for(PartCheckerDto dto : dtoList) {
			if(dto.getCheckSeq() == checkSeq) {
				return dto;
			}
		}
		return null;
	}
	
	public String getPartDetails() {
		return unitNumber+"\n"+partNo+"\n"+partItemNo+"\n"+partSectionCode+"\n"+partType;
	}
	@Override
	public String getKey() {
		return getPartDetails();
	}
	@Override
	public List<PartCheckerDto> getFromCheckers() {
		return getFromPartCheckers();
	}
	@Override
	public void setFromCheckers(List<PartCheckerDto> fromCheckers) {
		setFromPartCheckers(fromCheckers);
	}
	@Override
	public List<PartCheckerDto> getToCheckers() {
		return getToPartCheckers();
	}
	@Override
	public void setToCheckers(List<PartCheckerDto> toCheckers) {
		setToPartCheckers(toCheckers);
	}

	@Override
	public int compareTo(PartCheckerDtoMapper o) {
		return this.getKey().compareTo(o.getKey());
	}

}
