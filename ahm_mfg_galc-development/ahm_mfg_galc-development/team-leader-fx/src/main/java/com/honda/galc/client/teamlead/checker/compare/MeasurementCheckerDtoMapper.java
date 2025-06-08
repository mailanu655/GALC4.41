package com.honda.galc.client.teamlead.checker.compare;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dto.MeasurementCheckerDto;
/**
 * 
 * <h3>MeasurementCheckerDtoMapper Class description</h3>
 * <p> MeasurementCheckerDtoMapper: Dto Mapper utility for Measurement Checker Comparison</p>
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
public class MeasurementCheckerDtoMapper extends AbstractDtoMapper<MeasurementCheckerDto> implements Comparable<MeasurementCheckerDtoMapper>{

	private String unitNumber;
	private int measurementSeqNum;
	private String partNo;
	private String partItemNo;
	private String partSectionCode;
	private String partType;
	private List<MeasurementCheckerDto> fromMeasurementCheckers = new ArrayList<MeasurementCheckerDto>();
	private List<MeasurementCheckerDto> toMeasurementCheckers = new ArrayList<MeasurementCheckerDto>();

	public MeasurementCheckerDtoMapper(String unitNumber, int measurementSeqNum, String partNo, String partItemNo,
			String partSectionCode, String partType, List<MeasurementCheckerDto> fromMeasurementCheckers,
			List<MeasurementCheckerDto> toMeasurementCheckers) {
		super();
		this.unitNumber = unitNumber;
		this.measurementSeqNum = measurementSeqNum;
		this.partNo = partNo;
		this.partItemNo = partItemNo;
		this.partSectionCode = partSectionCode;
		this.partType = partType;
		setFromMeasurementCheckers(fromMeasurementCheckers);
		setToMeasurementCheckers(toMeasurementCheckers);
	}
	
	public String getUnitNumber() {
		return unitNumber;
	}
	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}
	public int getMeasurementSeqNum() {
		return measurementSeqNum;
	}
	public void setMeasurementSeqNum(int measurementSeqNum) {
		this.measurementSeqNum = measurementSeqNum;
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
	public List<MeasurementCheckerDto> getFromMeasurementCheckers() {
		return fromMeasurementCheckers;
	}
	public void setFromMeasurementCheckers(List<MeasurementCheckerDto> fromMeasurementCheckers) {
		this.fromMeasurementCheckers.addAll(fromMeasurementCheckers);
	}
	public List<MeasurementCheckerDto> getToMeasurementCheckers() {
		return toMeasurementCheckers;
	}
	public void setToMeasurementCheckers(List<MeasurementCheckerDto> toMeasurementCheckers) {
		List<MeasurementCheckerDto> addedList = new ArrayList<MeasurementCheckerDto>();
		for(MeasurementCheckerDto fromDto : this.fromMeasurementCheckers) {
			MeasurementCheckerDto toDto = getCheckerByCheckSeq(fromDto.getCheckSeq(), toMeasurementCheckers);
			if(toDto != null) {
				this.toMeasurementCheckers.add(toDto);
				addedList.add(toDto);
			} else {
				this.toMeasurementCheckers.add(new MeasurementCheckerDto());
			}
		}
		toMeasurementCheckers.removeAll(addedList);
		this.toMeasurementCheckers.addAll(toMeasurementCheckers);
	}
	private MeasurementCheckerDto getCheckerByCheckSeq(int checkSeq, List<MeasurementCheckerDto> dtoList) {
		for(MeasurementCheckerDto dto : dtoList) {
			if(dto.getCheckSeq() == checkSeq) {
				return dto;
			}
		}
		return null;
	}
	public String getMeasurementCheckerDetails() {
		return unitNumber+"\n"+measurementSeqNum+"\n"+partNo+"\n"+partItemNo+"\n"+partSectionCode+"\n"+partType;
	}

	@Override
	public String getKey() {
		return getMeasurementCheckerDetails();
	}

	@Override
	public List<MeasurementCheckerDto> getFromCheckers() {
		return getFromMeasurementCheckers();
	}

	@Override
	public void setFromCheckers(List<MeasurementCheckerDto> fromCheckers) {
		setFromMeasurementCheckers(fromCheckers);
	}

	@Override
	public List<MeasurementCheckerDto> getToCheckers() {
		return getToMeasurementCheckers();
	}

	@Override
	public void setToCheckers(List<MeasurementCheckerDto> toCheckers) {
		setToMeasurementCheckers(toCheckers);
	}

	@Override
	public int compareTo(MeasurementCheckerDtoMapper o) {
		return this.getKey().compareTo(o.getKey());
	}
}
