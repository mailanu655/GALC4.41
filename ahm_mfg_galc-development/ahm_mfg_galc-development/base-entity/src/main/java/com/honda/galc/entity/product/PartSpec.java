package com.honda.galc.entity.product;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumns;

import com.honda.galc.data.PrintAttribute;
import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.AuditEntry;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="PART_SPEC_TBX")
@NamedQueries ({
    @NamedQuery(name="PartSpec.getPartByLotCtrRule", 
    		query="select p from PartSpec p, PartByProductSpecCode s where p.id.partName = s.id.partName and p.id.partId = s.id.partId and s.id.partName = :partName and s.id.modelYearCode = :modelYear and s.id.modelCode = :modelCode and s.id.modelTypeCode = :modelType and s.id.modelOptionCode = :optionCode and s.id.extColorCode = :extColor and s.id.intColorCode = :intColor")
})
public class PartSpec extends AuditEntry{
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey= true,sequence=1)
	private PartSpecId id;

	@Column(name="PART_DESCRIPTION")
	@Auditable(isPartOfPrimaryKey= false,sequence=1)
	private String partDescription;

	@Column(name="PART_SERIAL_NUMBER_MASK")
	@Auditable(isPartOfPrimaryKey= false,sequence=2)
	private String partSerialNumberMask;
	
	@Column(name="SCAN_COUNT")
	@Auditable(isPartOfPrimaryKey= false,sequence=3)
	private int scanCount;

	@Column(name="PART_MAX_ATTEMPTS")
	@Auditable(isPartOfPrimaryKey= false,sequence=4)
	private int partMaxAttempts;

	@Column(name="MEASUREMENT_COUNT")
	@Auditable(isPartOfPrimaryKey= false,sequence=5)
	private int measurementCount;

	@Column(name="ENTRY_TIMESTAMP")
	private Timestamp entryTimestamp;

	@Column(name = "COMMENT")
	@Auditable(isPartOfPrimaryKey= false,sequence=6)
	private String comment;
	
	@Column(name="PART_MARK")
	private String partMark;
	
	@Column(name="PART_NUMBER")
	@Auditable(isPartOfPrimaryKey= false,sequence=7)
	private String partNumber;
	
	@Column(name="DISPLAY_TYPE")
	@Auditable(isPartOfPrimaryKey= false,sequence=8)
	private String displayType;
	
	@Column(name="IMAGE_ID")
	@Auditable(isPartOfPrimaryKey= false,sequence=9)
	int imageId;

	@Column(name = "PARSE_STRATEGY")
	@Auditable(isPartOfPrimaryKey= false,sequence=10)
	private String parseStrategy;
	
	@Column(name = "PARSE_INFORMATION")
	@Auditable(isPartOfPrimaryKey= false,sequence=11)
	private String parserInformation;

	@Column(name = "PART_COLOR_CODE")
	@Auditable(isPartOfPrimaryKey= false,sequence=12)
	private String partColorCode;

	private static final long serialVersionUID = 1L;

	@OneToMany(targetEntity = MeasurementSpec.class,fetch = FetchType.EAGER)
    @ElementJoinColumns({
    	@ElementJoinColumn(name="PART_NAME",updatable=false,insertable=false),
    	@ElementJoinColumn(name="PART_ID",updatable=false,insertable=false)})
    @OrderBy
    private List<MeasurementSpec> measurementSpecs = new ArrayList<MeasurementSpec>();

	@Transient
	private List<MeasurementSpec> numberMeasurementSpecs;
	@Transient
	private List<MeasurementSpec> stringMeasurementSpecs;
	
	public PartSpec() {
		super();
	}

	public PartSpecId getId() {
		return this.id;
	}

	public void setId(PartSpecId id) {
		this.id = id;
	}

	public String getPartDescription() {
		return StringUtils.trim(partDescription);
	}

	public void setPartDescription(String partDescription) {
		this.partDescription = partDescription;
	}

	@PrintAttribute
	public String getPartSerialNumberMask() {
		return StringUtils.trim(this.partSerialNumberMask);
	}

	public void setPartSerialNumberMask(String partSerialNumberMask) {
		this.partSerialNumberMask = partSerialNumberMask;
	}
	
	public int getScanCount() {
		return scanCount;
	}

	public void setScanCount(int scanCount) {
		this.scanCount = scanCount;
	}

	public int getPartMaxAttempts() {
		return this.partMaxAttempts;
	}

	public void setPartMaxAttempts(int partMaxAttempts) {
		this.partMaxAttempts = partMaxAttempts;
	}

	public int getMeasurementCount() {
		return this.measurementCount;
	}

	public void setMeasurementCount(int measurementCount) {
		this.measurementCount = measurementCount;
	}

	public Timestamp getEntryTimestamp() {
		return this.entryTimestamp;
	}

	public void setEntryTimestamp(Timestamp entryTimestamp) {
		this.entryTimestamp = entryTimestamp;
	}

	public List<MeasurementSpec> getMeasurementSpecs() {
		return measurementSpecs;
	}

	public List<MeasurementSpec> getStringMeasurementSpecs() {
		if(stringMeasurementSpecs == null){
			stringMeasurementSpecs = new ArrayList<MeasurementSpec>();
			for(MeasurementSpec ms : measurementSpecs)
				if(ms.getMeasurementName() != null) stringMeasurementSpecs.add(ms);
		}
		return stringMeasurementSpecs;
	}
	
	public List<MeasurementSpec> getNumberMeasurementSpecs() {
		if(numberMeasurementSpecs == null){
			numberMeasurementSpecs = new ArrayList<MeasurementSpec>();
			for(MeasurementSpec ms : measurementSpecs)
				if(ms.getMeasurementName() == null) numberMeasurementSpecs.add(ms);
		}
		return numberMeasurementSpecs;
	}
	
	public void setMeasurementSpecs(List<MeasurementSpec> measurementSpecs) {
		this.measurementSpecs = measurementSpecs;
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@PrintAttribute
	public String getPartMark() {
		return partMark;
	}

	@PrintAttribute
	public String getPartNumber() {
		return partNumber;
	}

	public void setPartMark(String partMark) {
		this.partMark = partMark;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	
	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public String getParseStrategy() {
		return StringUtils.trim(parseStrategy);
	}

	public void setParseStrategy(String parseStrategy) {
		this.parseStrategy = parseStrategy;
	}
	
	public String getParserInformation() {
		return StringUtils.trim(parserInformation);
	}

	public void setParserInformation(String parserInformation) {
		this.parserInformation = parserInformation;
	}

	public String getPartColorCode() {
		return StringUtils.trim(partColorCode);
	}

	public void setPartColorCode(String partColorCode) {
		this.partColorCode = partColorCode;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getId().getPartName()).append(",");
		builder.append(getId().getPartId()).append(",");
		builder.append(StringUtils.trim(getPartDescription())).append(",");
		builder.append(getPartSerialNumberMask()).append(",");
		builder.append(getPartMaxAttempts()).append(",");
		builder.append(getMeasurementCount()).append(",");
		builder.append(getPartMark()).append(",");
		builder.append(getPartNumber()).append(",");
		builder.append(getDisplayType()).append(",");
		builder.append(getParseStrategy()).append(",");
		builder.append(getParserInformation()).append(",");
		builder.append(getPartColorCode()).append(",");
		builder.append(getScanCount()).append(",");		
		for(MeasurementSpec m : getMeasurementSpecs()){
			builder.append(",[");
			builder.append(m);
			builder.append("]");
		}
			
		return builder.toString();
	}
}
