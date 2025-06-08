package com.honda.galc.entity.product;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.Tag;
import com.honda.galc.entity.AuditEntry;
    
/** * * 
* @author Xiaomei Ma
* @since Feb 27, 2020
*/
@Entity
@Table(name="BLOCK_BUILD_RESULTS_HIST_TBX")
public class BlockBuildResultHistory extends AuditEntry {
	@EmbeddedId
	private BlockBuildResultHistoryId id;

	@Column(name="RESULT_VALUE")
	@Tag(name="VALUE", optional = true)
	private String resultValue;
	
	@Column(name="INSTALLED_PART_STATUS")
	private Integer installedPartStatus;
	
	@Column(name="ASSOCIATE_NO")
	private String associateNo;
	
	@Column(name="PROCESS_POINT_ID")
	private String processPointId;

	private static final long serialVersionUID = 1L;

	public BlockBuildResultHistory() {
		super();
	}
	
	public BlockBuildResultHistory(String blockId,String partName, Date actualTimestamp) {
		id = new BlockBuildResultHistoryId(blockId,partName, actualTimestamp);
		id.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
	}
	
	public BlockBuildResultHistory Initialize(BlockBuildResult result) {
		BlockBuildResultHistory history = new BlockBuildResultHistory(result.getProductId(), result.getPartName(), result.getActualTimestamp());
		history.setResultValue(result.getResultValue());
		history.setInstalledPartStatus(result.getInstalledPartStatusId());
		history.setAssociateNo(result.getAssociateNo());
		history.setProcessPointId(result.getProcessPointId());
		
		return history;		
	}

	public BlockBuildResultHistory(BlockBuildResultHistoryId id) {
		this();
		this.id = id;
	}

	public BlockBuildResultHistoryId getId() {
		return id;
	}

	public void setId(BlockBuildResultHistoryId id) {
		this.id = id;
	}

	public String getResultValue() {
		return StringUtils.trim(this.resultValue);
	}

	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}

	public Integer getInstalledPartStatus() {
		return installedPartStatus;
	}

	public void setInstalledPartStatus(Integer installedPartStatus) {
		this.installedPartStatus = installedPartStatus;
	}

	public String getAssociateNo() {
		return StringUtils.trim(this.associateNo);
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((associateNo == null) ? 0 : associateNo.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((installedPartStatus == null) ? 0 : installedPartStatus);
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + ((resultValue == null) ? 0 : resultValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlockBuildResultHistory other = (BlockBuildResultHistory) obj;
		if (associateNo == null) {
			if (other.associateNo != null)
				return false;
		} else if (!associateNo.equals(other.associateNo))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (installedPartStatus == null) {
			if (other.installedPartStatus != null)
				return false;
		} else if (!installedPartStatus.equals(other.installedPartStatus))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (resultValue == null) {
			if (other.resultValue != null)
				return false;
		} else if (!resultValue.equals(other.resultValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BlockBuildResultHistory [" + id.toString() + ", resultValue=" + resultValue + ", installedPartStatus="
				+ installedPartStatus + ", associateNo=" + associateNo + ", processPointId=" + processPointId + "]";
	}

}
