package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.AuditEntry;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL206TBX")
@NamedQueries ({
    @NamedQuery(name="Part.getPartByLotCtrRule", 
    		query="select p from Part p, PartByProductSpecCode s where p.id.partName = s.id.partName and p.id.partId = s.id.partId and s.id.partName = :partName and s.id.modelYearCode = :modelYear and s.id.modelCode = :modelCode and s.id.modelTypeCode = :modelType and s.id.modelOptionCode = :optionCode and s.id.extColorCode = :extColor and s.id.intColorCode = :intColor")
})
public class Part extends AuditEntry {
    @EmbeddedId
    @Auditable(isPartOfPrimaryKey= true,sequence=1)
    private PartId id;

    @Column(name = "PART_DESCRIPTION")
    @Auditable(isPartOfPrimaryKey= false,sequence=2)
    private String partDescription;

    @Column(name = "PART_SERIAL_NUMBER_MASK")
    @Auditable(isPartOfPrimaryKey= false,sequence=3)
    private String partSerialNumberMask;

    @Column(name = "ENTRY_TIMESTAMP")
    private Date entryTimestamp;

    @Column(name = "MAXIMUM_VALUE1")
    private double maximumValue1;

    @Column(name = "MINIMUM_VALUE1")
    private double minimumValue1;

    @Column(name = "MAXIMUM_VALUE2")
    private double maximumValue2;

    @Column(name = "MINIMUM_VALUE2")
    private double minimumValue2;

    @Column(name = "MAXIMUM_VALUE3")
    private double maximumValue3;

    @Column(name = "MINIMUM_VALUE3")
    private double minimumValue3;

    @Column(name = "MAXIMUM_VALUE4")
    private double maximumValue4;

    @Column(name = "MINIMUM_VALUE4")
    private double minimumValue4;

    @Column(name = "MAXIMUM_VALUE5")
    private double maximumValue5;

    @Column(name = "MINIMUM_VALUE5")
    private double minimumValue5;

    @Column(name = "MAXIMUM_VALUE6")
    private double maximumValue6;

    @Column(name = "MINIMUM_VALUE6")
    private double minimumValue6;

    @Column(name = "MAXIMUM_VALUE7")
    private double maximumValue7;

    @Column(name = "MINIMUM_VALUE7")
    private double minimumValue7;

    @Column(name = "MAXIMUM_VALUE8")
    private double maximumValue8;

    @Column(name = "MINIMUM_VALUE8")
    private double minimumValue8;

    @Column(name = "MAXIMUM_VALUE9")
    private double maximumValue9;

    @Column(name = "MINIMUM_VALUE9")
    private double minimumValue9;

    @Column(name = "MAXIMUM_VALUE10")
    private double maximumValue10;

    @Column(name = "MINIMUM_VALUE10")
    private double minimumValue10;

    @Column(name = "MEASUREMENT_COUNT")
    private int measurementCount;
    
    @Column(name = "ALLOW_DUPLICATES")
    private int allowDuplicates;    


    /*    @Column(name = "COMMENT")
    private String comment;*/

//	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
//    @JoinColumns({
//         @JoinColumn(name = "PART_NAME", referencedColumnName="PART_NAME", 
//        		unique = true, nullable = false, insertable = false, updatable = false)
//    })    
//	private PartName partName;

    private static final long serialVersionUID = 1L;

    public Part() {
        super();
    }

    public PartId getId() {
        return this.id;
    }

    public void setId(PartId id) {
        this.id = id;
    }

    public String getPartDescription() {
        return StringUtils.trim(partDescription);
    }

    public void setPartDescription(String partDescription) {
        this.partDescription = partDescription;
    }

    public String getPartSerialNumberMask() {
        return StringUtils.trim(this.partSerialNumberMask);
    }

    public void setPartSerialNumberMask(String partSerialNumberMask) {
        this.partSerialNumberMask = partSerialNumberMask;
    }

    public Date getEntryTimestamp() {
        return this.entryTimestamp;
    }

    public void setEntryTimestamp(Date entryTimestamp) {
        this.entryTimestamp = entryTimestamp;
    }

    public double getMaximumValue1() {
        return this.maximumValue1;
    }

    public void setMaximumValue1(double maximumValue1) {
        this.maximumValue1 = maximumValue1;
    }

    public double getMinimumValue1() {
        return this.minimumValue1;
    }

    public void setMinimumValue1(double minimumValue1) {
        this.minimumValue1 = minimumValue1;
    }

    public double getMaximumValue2() {
        return this.maximumValue2;
    }

    public void setMaximumValue2(double maximumValue2) {
        this.maximumValue2 = maximumValue2;
    }

    public double getMinimumValue2() {
        return this.minimumValue2;
    }

    public void setMinimumValue2(double minimumValue2) {
        this.minimumValue2 = minimumValue2;
    }

    public double getMaximumValue3() {
        return this.maximumValue3;
    }

    public void setMaximumValue3(double maximumValue3) {
        this.maximumValue3 = maximumValue3;
    }

    public double getMinimumValue3() {
        return this.minimumValue3;
    }

    public void setMinimumValue3(double minimumValue3) {
        this.minimumValue3 = minimumValue3;
    }

    public double getMaximumValue4() {
        return this.maximumValue4;
    }

    public void setMaximumValue4(double maximumValue4) {
        this.maximumValue4 = maximumValue4;
    }

    public double getMinimumValue4() {
        return this.minimumValue4;
    }

    public void setMinimumValue4(double minimumValue4) {
        this.minimumValue4 = minimumValue4;
    }

    public double getMaximumValue5() {
        return this.maximumValue5;
    }

    public void setMaximumValue5(double maximumValue5) {
        this.maximumValue5 = maximumValue5;
    }

    public double getMinimumValue5() {
        return this.minimumValue5;
    }

    public void setMinimumValue5(double minimumValue5) {
        this.minimumValue5 = minimumValue5;
    }

    public double getMaximumValue6() {
        return this.maximumValue6;
    }

    public void setMaximumValue6(double maximumValue6) {
        this.maximumValue6 = maximumValue6;
    }

    public double getMinimumValue6() {
        return this.minimumValue6;
    }

    public void setMinimumValue6(double minimumValue6) {
        this.minimumValue6 = minimumValue6;
    }

    public double getMaximumValue7() {
        return this.maximumValue7;
    }

    public void setMaximumValue7(double maximumValue7) {
        this.maximumValue7 = maximumValue7;
    }

    public double getMinimumValue7() {
        return this.minimumValue7;
    }

    public void setMinimumValue7(double minimumValue7) {
        this.minimumValue7 = minimumValue7;
    }

    public double getMaximumValue8() {
        return this.maximumValue8;
    }

    public void setMaximumValue8(double maximumValue8) {
        this.maximumValue8 = maximumValue8;
    }

    public double getMinimumValue8() {
        return this.minimumValue8;
    }

    public void setMinimumValue8(double minimumValue8) {
        this.minimumValue8 = minimumValue8;
    }

    public double getMaximumValue9() {
        return this.maximumValue9;
    }

    public void setMaximumValue9(double maximumValue9) {
        this.maximumValue9 = maximumValue9;
    }

    public double getMinimumValue9() {
        return this.minimumValue9;
    }

    public void setMinimumValue9(double minimumValue9) {
        this.minimumValue9 = minimumValue9;
    }

    public double getMaximumValue10() {
        return this.maximumValue10;
    }

    public void setMaximumValue10(double maximumValue10) {
        this.maximumValue10 = maximumValue10;
    }

    public double getMinimumValue10() {
        return this.minimumValue10;
    }

    public void setMinimumValue10(double minimumValue10) {
        this.minimumValue10 = minimumValue10;
    }

    public int getMeasurementCount() {
        return this.measurementCount;
    }

    public void setMeasurementCount(int measurementCount) {
        this.measurementCount = measurementCount;
    }
    
    public void setAllowDuplicates(int allowDuplicates){
    	this.allowDuplicates = allowDuplicates;
    }
    
    public int getAllowDuplicates()
    {
    	return this.allowDuplicates;
    }    

//	public PartName getPartName() {
//		return partName;
//	}
//
//	public void setPartName(PartName partName) {
//		this.partName = partName;
//	}
 
/*	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}*/

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(id).append(",");
		builder.append(StringUtils.trim(partDescription)).append(",");
		builder.append(partSerialNumberMask).append(",");
		builder.append(measurementCount);
		builder.append(allowDuplicates);		
		appendMinMaxValues(builder);		
			
		return builder.toString();
	}

	private void appendMinMaxValues(StringBuilder builder) {
		if(maximumValue1 > 0){
			appendMinMaxValue(builder, minimumValue1, maximumValue1);
		}
		if(maximumValue2 > 0){
			appendMinMaxValue(builder, minimumValue2, maximumValue2);
		}
		if(maximumValue3 > 0){
			appendMinMaxValue(builder, minimumValue3, maximumValue3);
		}
		if(maximumValue4 > 0){
			appendMinMaxValue(builder, minimumValue4, maximumValue4);
		}
		if(maximumValue5 > 0){
			appendMinMaxValue(builder, minimumValue5, maximumValue5);
		}
		if(maximumValue6 > 0){
			appendMinMaxValue(builder, minimumValue6, maximumValue6);
		}
		if(maximumValue7 > 0){
			appendMinMaxValue(builder, minimumValue7, maximumValue7);
		}
		if(maximumValue8 > 0){
			appendMinMaxValue(builder, minimumValue8, maximumValue8);
		}
		if(maximumValue9 > 0){
			appendMinMaxValue(builder, minimumValue9, maximumValue9);
		}
		if(maximumValue10 > 0){
			appendMinMaxValue(builder, minimumValue10, maximumValue10);
		}
	}

	private void appendMinMaxValue(StringBuilder builder, double min, double max) {
		builder.append(",").append(min).append(":").append(max);
	}
    
    

}
