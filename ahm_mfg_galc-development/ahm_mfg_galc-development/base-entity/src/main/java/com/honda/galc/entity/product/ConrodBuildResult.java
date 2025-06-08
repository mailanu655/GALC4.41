package com.honda.galc.entity.product;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.Tag;
import com.honda.galc.entity.enumtype.InstalledPartStatus;

@Entity
@Table(name="CONROD_BUILD_RESULTS_TBX")
public class ConrodBuildResult extends ProductBuildResult {
	@EmbeddedId
	private ConrodBuildResultId id;

	@Column(name="RESULT_VALUE")
	@Tag(name="VALUE", optional = true)
	private String resultValue;

    /**
     * this is a unique build result identifier if a QICS defect was created
     * used for all QICS messages
     */
    @Column(name = "DEFECT_REF_ID")
    private long defectRefId;
    
	private static final long serialVersionUID = 1L;

	public ConrodBuildResult() {
		super();
	}
	
	public ConrodBuildResult(String productId,String partName) {
		id = new ConrodBuildResultId(productId,partName);
		setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		setInstalledPartStatus(InstalledPartStatus.BLANK);
    	defectRefId=0;
    	setQicsDefect(false);
	}

	public ConrodBuildResult(ConrodBuildResultId id) {
		this();
		this.id = id;
	}

	public ConrodBuildResultId getId() {
		return this.id;
	}

	public void setId(ConrodBuildResultId id) {
		this.id = id;
	}

	public String getResultValue() {
		return StringUtils.trim(this.resultValue);
	}

	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}



	@Override
	public String getPartName() {
		return id.getPartName();
	}

	@Override
	public String getProductId() {
		return id.getConrodId();
	}

	@Override
	public void setPartName(String partName) {

		if(id == null) id = new ConrodBuildResultId();
		id.setPartName(partName);
		
	}

	@Override
	public void setProductId(String productId) {
		
		if(id == null) id = new ConrodBuildResultId();
		id.setConrodId(productId);
		
	}

	@Override
	public String getInstalledPartReason() {
		return "";//not appliable for conrod and crankshaft
	}

	@Override
	public String getPartSerialNumber() {
		return getResultValue();
	}

	@Override
	public List<Measurement> getMeasurements() {
		return null;
	}

	public long getDefectRefId() {
		return defectRefId;
	}

	public void setDefectRefId(long defectRefId) {
		this.defectRefId = defectRefId;
	}

}
