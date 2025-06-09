package com.honda.ahm.lc.vdb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gal195tbx")
public class Line {
    @Id
    @Column(name = "line_id")
    private Long lineId;

    @Column(name = "line_name")
    private String lineName;

    @Column(name = "division_id")
    private Long divisionId;

    @Column(name = "line_sequence_number")
    private Integer lineSequenceNumber;

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public Integer getLineSequenceNumber() {
		return lineSequenceNumber;
	}

	public void setLineSequenceNumber(Integer lineSequenceNumber) {
		this.lineSequenceNumber = lineSequenceNumber;
	}

    
}

