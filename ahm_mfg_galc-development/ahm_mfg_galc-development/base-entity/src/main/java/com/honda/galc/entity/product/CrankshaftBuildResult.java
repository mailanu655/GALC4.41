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
@Table(name="CRANKSHAFT_BUILD_RESULTS_TBX")
public class CrankshaftBuildResult extends ProductBuildResult {
	@EmbeddedId
	private CrankshaftBuildResultId id;

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

	public CrankshaftBuildResult() {
		super();
	}
	
	public CrankshaftBuildResult(String productId,String partName) {
		id = new CrankshaftBuildResultId(productId,partName);
		setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		setInstalledPartStatus(InstalledPartStatus.BLANK);
    	defectRefId=0;
    	setQicsDefect(false);
	}

	public CrankshaftBuildResult(CrankshaftBuildResultId id) {
		this();
		this.id = id;
	}

	public CrankshaftBuildResultId getId() {
		return this.id;
	}

	public void setId(CrankshaftBuildResultId id) {
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
		return id.getCrankshaftId();
	}

	@Override
	public void setPartName(String partName) {

		if(id == null) id = new CrankshaftBuildResultId();
		id.setPartName(partName);
		
	}

	@Override
	public void setProductId(String productId) {
		
		if(id == null) id = new CrankshaftBuildResultId();
		id.setCrankshaftId(productId);
		
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
