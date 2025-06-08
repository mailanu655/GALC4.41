/**
 * 
 */
package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * @date Dec 8, 2011
 */
@Entity
@Table(name = "PLC_MEMORY_MAP_TBX")
public class PlcMemoryMapItem extends AuditEntry {

	private static final long serialVersionUID = 9099386478606401051L;

	@Id
	@Column(name = "METRIC")
	private String metricId;

	@Column(name = "DATA_TYPE")
	private String dataType;

	@Column(name = "MEMORY_BANK")
	private String memoryBank;

	@Column(name = "START_ADDRESS")
	private String startAddress;

	@Column(name = "BIT")
	private Integer bitIndex;

	@Column(name = "BYTE_ORDER")
	private String byteOrder;

	@Column(name = "LENGTH")
	private Integer length;
	
	@Transient
	private Integer operationType;
	
	@Transient
	private Integer sequence;

	@Transient
	private Integer dataReady;
	
	@Transient
	private String value;

	public Object getId() {
		return getMetricId();
	}
	
	public String getMetricId() {
		return metricId;
	}
	
	public void setMetricId(String metricId) {
		this.metricId = metricId;
	}
	
	public String getDataType() {
		return dataType;
	}
	
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public String getMemoryBank() {
		return memoryBank;
	}
	
	public void setMemoryBank(String memoryBank) {
		this.memoryBank = memoryBank;
	}
	
	public String getStartAddress() {
		return startAddress;
	}
	
	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}
	
	public Integer getBitIndex() {
		if (bitIndex == null)
			bitIndex = -1;
		
		return bitIndex;
	}
	
	public void setBitIndex(Integer bitIndex) {
		this.bitIndex = bitIndex;
	}
	
	public String getByteOrder() {
		if (byteOrder == null)
			byteOrder = "H";
		
		return byteOrder;
	}
	
	public void setByteOrder(String byteOrder) {
		this.byteOrder = byteOrder;
	}
	
	public Integer getLength() {
		return length;
	}
	
	public void setLength(Integer length) {
		this.length = length;
	}
	
	public Integer getOperationType() {
		return operationType;
	}
	
	public void setOperationType(Integer operType) {
		this.operationType = operType;
	}
	
	public Integer getSequence() {
		return sequence;
	}
	
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	
	public String getMemSequence() {
		return this.memoryBank + this.startAddress;
	}
	
	public Boolean isDataReady() {
		if (dataReady > 0)
			return true;
		else
			return false;
	}
	
	public void setDataReady(Integer dataReady) {
		this.dataReady = dataReady;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
