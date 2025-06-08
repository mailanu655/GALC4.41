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

/**
 * 
 * <h3>HeadBuildResult Class description</h3>
 * <p> HeadBuildResult description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Mar 2, 2011
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="HEAD_BUILD_RESULTS_TBX")
public class HeadBuildResult extends ProductBuildResult {
	@EmbeddedId
	private HeadBuildResultId id;

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

	public HeadBuildResult() {
		super();
	}

	public HeadBuildResult(String productId,String partName) {
		id = new HeadBuildResultId(productId,partName);
		setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		setInstalledPartStatus(InstalledPartStatus.BLANK);
    	defectRefId=0;
    	setQicsDefect(false);
	}

	public HeadBuildResult(HeadBuildResultId id) {
		this();
		this.id = id;
	}

	public HeadBuildResultId getId() {
		return this.id;
	}

	public void setId(HeadBuildResultId id) {
		this.id = id;
	}

	public String getResultValue() {
		return StringUtils.trim(this.resultValue);
	}

	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}

	@Override
	public String getProductId() {
		return id.getHeadId();
	}

	@Override
	public void setProductId(String productId) {
		if(id == null) id = new HeadBuildResultId();
		id.setHeadId(productId);
	}

	@Override
	public String getPartName() {
		return id.getPartName();
	}

	@Override
	public void setPartName(String partName) {
		if(id == null) id = new HeadBuildResultId();
		id.setPartName(partName);
	}

	@Override
	public String getInstalledPartReason() {
		return "";
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
