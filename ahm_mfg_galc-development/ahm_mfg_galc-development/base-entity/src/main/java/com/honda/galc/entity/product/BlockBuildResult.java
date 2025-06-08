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
 * <h3>BlockBuildResult</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> BlockBuildResult description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Mar 27, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Mar 27, 2012
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="BLOCK_BUILD_RESULTS_TBX")
public class BlockBuildResult extends ProductBuildResult {
	@EmbeddedId
	private BlockBuildResultId id;

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

	public BlockBuildResult() {
		super();
	}
	
	public BlockBuildResult(String productId,String partName) {
		id = new BlockBuildResultId(productId,partName);
		setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		setInstalledPartStatus(InstalledPartStatus.BLANK);
    	defectRefId=0;
    	setQicsDefect(false);
	}

	public BlockBuildResult(BlockBuildResultId id) {
		this();
		this.id = id;
	}

	public BlockBuildResultId getId() {
		return this.id;
	}

	public void setId(BlockBuildResultId id) {
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
		return id.getBlockId();
	}

	@Override
	public void setPartName(String partName) {

		if(id == null) id = new BlockBuildResultId();
		id.setPartName(partName);
		
	}

	@Override
	public void setProductId(String productId) {
		
		if(id == null) id = new BlockBuildResultId();
		id.setBlockId(productId);
		
	}

	@Override
	public String getInstalledPartReason() {
		return "";//not appliable for head and block
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
