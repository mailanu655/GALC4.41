package com.honda.ahm.lc.vdb.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;
/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>ProductHistoryDetails</code> is view class for Product History.
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Hemant Rajput
 * @created Apr 22, 2021
 */

@Entity
@Immutable
@Subselect(value="SELECT " + 
		"result.actual_timestamp, " + 
		"result.defect_id, " + 
		"result.process_point_id, " + 
		"result.process_point_name, " + 
		"result.product_id, " + 
		"result.actual_problem_description, " + 
		"result.current_status, " + 
		"result.dept_name, " + 
		"result.entry_status, " + 
		"result.full_description, " + 
		"result.transaction_type " + 
		"FROM " + 
		"(" + 
		"	SELECT " + 
		"	A.PRODUCT_ID as product_id, " + 
		"	'PROCESS SCAN' as transaction_type, " + 
		"	A.ACTUAL_TIMESTAMP as actual_timestamp, " + 
		"	A.PROCESS_POINT_ID as process_point_id, " + 
		"	B.PROCESS_POINT_NAME as process_point_name, " + 
		"	'' as defect_id, " + 
		"	'' as full_description, " + 
		"	'' as actual_problem_description, " + 
		"	B.DIVISION_NAME as dept_name, " + 
		"	'' as entry_status, " + 
		"	'' as current_status " + 
		"	FROM GAL215TBX A LEFT OUTER JOIN GAL214TBX B ON B.PROCESS_POINT_ID = A.PROCESS_POINT_ID " + 
		"	UNION ALL " + 
		"	SELECT E.PRODUCT_ID as product_id, " + 
		"	'DEFECT ENTRY' as transaction_type, " + 
		"	E.ACTUAL_TIMESTAMP as actual_timestamp, " + 
		"	F.PROCESS_POINT_ID as process_point_id, " + 
		"	F.PROCESS_POINT_NAME as process_point_name, " + 
		"	CAST(E.DEFECTRESULTID as VARCHAR(50)) as defect_id, " + 
		"	   trim((case when E.INSPECTION_PART_NAME is null then '' else trim(E.INSPECTION_PART_NAME)||' ' end) " + 
		"	  ||( case when E.INSPECTION_PART_LOCATION_NAME is null or E.INSPECTION_PART_LOCATION_NAME = '' then trim('') else trim(E.INSPECTION_PART_LOCATION_NAME)||' ' end ) " + 
		"	  ||( case when E.INSPECTION_PART_LOCATION2_NAME is null or E.INSPECTION_PART_LOCATION2_NAME = ''  then trim('') else trim(E.INSPECTION_PART_LOCATION2_NAME)||' ' end  ) " + 
		"	  ||( case when E.INSPECTION_PART2_NAME is null or E.INSPECTION_PART2_NAME = '' then trim('') else trim(E.INSPECTION_PART2_NAME)||' ' end  ) " + 
		"	  ||( case when E.INSPECTION_PART2_LOCATION_NAME is null or E.INSPECTION_PART2_LOCATION_NAME = '' then trim('') else trim(E.INSPECTION_PART2_LOCATION_NAME)||' ' end  ) " + 
		"	  ||( case when E.INSPECTION_PART2_LOCATION2_NAME is null or E.INSPECTION_PART2_LOCATION2_NAME = '' then trim('') else trim(E.INSPECTION_PART2_LOCATION2_NAME)||' ' end  ) " + 
		"	  ||( case when E.INSPECTION_PART3_NAME is null or E.INSPECTION_PART3_NAME = '' then trim('') else trim(E.INSPECTION_PART3_NAME)||' ' end  ) " + 
		"	  ||( case when E.DEFECT_TYPE_NAME is null or E.DEFECT_TYPE_NAME = '' then trim('') else trim(E.DEFECT_TYPE_NAME)||' ' end  ) " + 
		"	  ||( case when E.DEFECT_TYPE_NAME2 is null or E.DEFECT_TYPE_NAME2 = '' then trim('') else trim(E.DEFECT_TYPE_NAME2)||' ' end  )) as full_description, " + 
		"	trim((case when G.INSPECTION_PART_NAME is null then '' else trim(G.INSPECTION_PART_NAME)||' ' end) " + 
		"	  ||( case when G.INSPECTION_PART_LOCATION_NAME is null or G.INSPECTION_PART_LOCATION_NAME = '' then trim('') else trim(G.INSPECTION_PART_LOCATION_NAME)||' ' end ) " + 
		"	  ||( case when G.INSPECTION_PART_LOCATION2_NAME is null or G.INSPECTION_PART_LOCATION2_NAME = ''  then trim('') else trim(G.INSPECTION_PART_LOCATION2_NAME)||' ' end  ) " + 
		"	  ||( case when G.INSPECTION_PART2_NAME is null or G.INSPECTION_PART2_NAME = '' then trim('') else trim(G.INSPECTION_PART2_NAME)||' ' end  ) " + 
		"	  ||( case when G.INSPECTION_PART2_LOCATION_NAME is null or G.INSPECTION_PART2_LOCATION_NAME = '' then trim('') else trim(G.INSPECTION_PART2_LOCATION_NAME)||' ' end  ) " + 
		"	  ||( case when G.INSPECTION_PART2_LOCATION2_NAME is null or G.INSPECTION_PART2_LOCATION2_NAME = '' then trim('') else trim(G.INSPECTION_PART2_LOCATION2_NAME)||' ' end  ) " + 
		"	  ||( case when G.INSPECTION_PART3_NAME is null or G.INSPECTION_PART3_NAME = '' then trim('') else trim(G.INSPECTION_PART3_NAME)||' ' end  ) " + 
		"	||( case when G.DEFECT_TYPE_NAME is null or G.DEFECT_TYPE_NAME = '' then trim('') else trim(G.DEFECT_TYPE_NAME)||' ' end  ) " + 
		"	  ||( case when G.DEFECT_TYPE_NAME2 is null or G.DEFECT_TYPE_NAME2 = '' then trim('') else trim(G.DEFECT_TYPE_NAME2)||' ' end  )) as actual_problem_description, " + 
		"	E.RESPONSIBLE_DEPT as dept_name, " + 
		"	CASE WHEN E.ORIGINAL_DEFECT_STATUS = 1 THEN " + 
		"		'ENTERED REPAIRED' " + 
		"	WHEN E.ORIGINAL_DEFECT_STATUS = 5 THEN " + 
		"		'ENTERED OUTSTANDING' " + 
		"	WHEN E.ORIGINAL_DEFECT_STATUS = 8 THEN " + 
		"		'NON-REPAIRABLE' " + 
		"	END AS entry_status, " + 
		"	CASE WHEN E.CURRENT_DEFECT_STATUS = 6 THEN " + 
		"		'NOT FIXED' " + 
		"	WHEN E.CURRENT_DEFECT_STATUS = 7 THEN " + 
		"		'FIXED' " + 
		"	WHEN E.CURRENT_DEFECT_STATUS = 8 THEN " + 
		"		'NON-REPAIRABLE' " + 
		"	END AS current_status " + 
		"	FROM QI_DEFECT_RESULT_TBX E LEFT OUTER JOIN " + 
		"	GAL214TBX F on E.APPLICATION_ID = F.PROCESS_POINT_ID LEFT OUTER JOIN " + 
		"	QI_REPAIR_RESULT_TBX G ON E.PRODUCT_ID = G.PRODUCT_ID and E.DEFECTRESULTID = G.DEFECTRESULTID " + 
		"	UNION ALL " + 
		"	SELECT E.PRODUCT_ID as product_id, " + 
		"	'REPAIR ENTRY' as transaction_type, " + 
		"	H.REPAIR_TIMESTAMP as actual_timestamp, " + 
		"	F.PROCESS_POINT_ID as process_point_id, " + 
		"	F.PROCESS_POINT_NAME as process_point_name, " + 
		"	CAST(E.DEFECTRESULTID as VARCHAR(50)) as defect_id, " + 
		"	CONCAT(CONCAT(H.REPAIR_METHOD, '~'), H.COMMENT) as full_description, " + 
		"	trim((case when G.INSPECTION_PART_NAME is null then '' else trim(G.INSPECTION_PART_NAME)||' ' end) " + 
		"	  ||( case when G.INSPECTION_PART_LOCATION_NAME is null or G.INSPECTION_PART_LOCATION_NAME = '' then trim('') else trim(G.INSPECTION_PART_LOCATION_NAME)||' ' end ) " + 
		"	  ||( case when G.INSPECTION_PART_LOCATION2_NAME is null or G.INSPECTION_PART_LOCATION2_NAME = ''  then trim('') else trim(G.INSPECTION_PART_LOCATION2_NAME)||' ' end  ) " + 
		"	  ||( case when G.INSPECTION_PART2_NAME is null or G.INSPECTION_PART2_NAME = '' then trim('') else trim(G.INSPECTION_PART2_NAME)||' ' end  ) " + 
		"	  ||( case when G.INSPECTION_PART2_LOCATION_NAME is null or G.INSPECTION_PART2_LOCATION_NAME = '' then trim('') else trim(G.INSPECTION_PART2_LOCATION_NAME)||' ' end  ) " + 
		"	  ||( case when G.INSPECTION_PART2_LOCATION2_NAME is null or G.INSPECTION_PART2_LOCATION2_NAME = '' then trim('') else trim(G.INSPECTION_PART2_LOCATION2_NAME)||' ' end  ) " + 
		"	  ||( case when G.INSPECTION_PART3_NAME is null or G.INSPECTION_PART3_NAME = '' then trim('') else trim(G.INSPECTION_PART3_NAME)||' ' end  ) " + 
		"	||( case when G.DEFECT_TYPE_NAME is null or G.DEFECT_TYPE_NAME = '' then trim('') else trim(G.DEFECT_TYPE_NAME)||' ' end  ) " + 
		"	  ||( case when G.DEFECT_TYPE_NAME2 is null or G.DEFECT_TYPE_NAME2 = '' then trim('') else trim(G.DEFECT_TYPE_NAME2)||' ' end  )) as actual_problem_description, " + 
		"	E.RESPONSIBLE_DEPT as dept_name, " + 
		"	CASE WHEN G.ORIGINAL_DEFECT_STATUS = 1 THEN " + 
		"		'ENTERED REPAIRED' " + 
		"	WHEN G.ORIGINAL_DEFECT_STATUS = 5 THEN " + 
		"		'ENTERED OUTSTANDING' " + 
		"	WHEN G.ORIGINAL_DEFECT_STATUS = 8 THEN " + 
		"		'NON-REPAIRABLE' " + 
		"	END AS entry_status, " + 
		"	CASE WHEN G.CURRENT_DEFECT_STATUS = 6 THEN " + 
		"		'NOT FIXED' " + 
		"	WHEN G.CURRENT_DEFECT_STATUS = 7 THEN " + 
		"		'FIXED' " + 
		"	WHEN G.CURRENT_DEFECT_STATUS = 8 THEN " + 
		"		'NON-REPAIRABLE' " + 
		"	END AS current_status " + 
		"	FROM QI_DEFECT_RESULT_TBX E LEFT OUTER JOIN " + 
		"	GAL214TBX F on E.APPLICATION_ID = F.PROCESS_POINT_ID LEFT OUTER JOIN " + 
		"	QI_REPAIR_RESULT_TBX G ON E.PRODUCT_ID = G.PRODUCT_ID and E.DEFECTRESULTID = G.DEFECTRESULTID LEFT OUTER JOIN " + 
		"	QI_APPLIED_REPAIR_METHOD_TBX H ON G.REPAIR_ID = H.REPAIR_ID LEFT OUTER JOIN " + 
		"	GAL214TBX I on H.APPLICATION_ID = I.PROCESS_POINT_ID " + 
		"	WHERE not (G.PRODUCT_ID is Null) " + 
		"	UNION ALL " + 
		"	SELECT A.PRODUCT_ID as product_id, " + 
		"	'PARKING CHANGE' as transaction_type, " + 
		"	A.UPDATE_TIMESTAMP as actual_timestamp, " + 
		"	'' as process_point_id, " + 
		"	'' as process_point_name, " + 
		"	CAST(A.DEFECTRESULTID as VARCHAR(50)) as defect_id, " + 
		"	A.TARGET_REPAIR_AREA as full_description, " + 
		"	'' as actual_problem_description, " + 
		"	A.TARGET_RESP_DEPT as dept_name, " + 
		"	'' as entry_status, " + 
		"	'' as current_status " + 
		"	FROM QI_REPAIR_AREA_SPACE_TBX A " + 
		"	UNION ALL " + 
		"	SELECT A.PRODUCT_ID as product_id, " + 
		"	'PARKING CHANGE' as transaction_type, " + 
		"	A.ACTUAL_TIMESTAMP as actual_timestamp, " + 
		"	'' as process_point_id, " + 
		"	'' as process_point_name, " + 
		"	CAST(A.DEFECTRESULTID as VARCHAR(50)) as defect_id, " + 
		"	A.REPAIR_AREA_NAME as full_description, " + 
		"	'' as actual_problem_description, " + 
		"	B.DIVISION_NAME as dept_name, " + 
		"	'' as entry_status, " + 
		"	'' as current_status " + 
		"	FROM QI_REPAIR_AREA_SPACE_HIST_TBX A INNER JOIN " + 
		"	QI_REPAIR_AREA_TBX B on A.REPAIR_AREA_NAME = B.REPAIR_AREA_NAME " + 
		"	UNION ALL " + 
		"	SELECT " + 
		"	A.PRODUCT_ID as product_id, " + 
		"	'SCRAP/EXCEPTION' as transaction_type, " + 
		"	A.ACTUAL_TIMESTAMP as actual_timestamp, " + 
		"	B.PROCESS_POINT_ID as process_point_id, " + 
		"	B.PROCESS_POINT_NAME as process_point_name, " + 
		"	'N/A' AS defect_id, " + 
		"	CONCAT(CONCAT(A.EXCEPTIONAL_OUT_COMMENT,'~'),A.ASSOCIATE_NO) as full_description, " + 
		"	'' as actual_problem_description, " + 
		"	'PC' AS dept_name, " + 
		"	'' AS entry_status, " + 
		"	'' as current_status " + 
		"	FROM GAL136TBX A LEFT OUTER JOIN GAL214TBX B ON B.PROCESS_POINT_ID = A.PROCESS_POINT_ID " + 
		"	UNION ALL " + 
		"	SELECT " + 
		"	A.PRODUCT_ID as product_id, " + 
		"	CASE WHEN A.HOLD_TYPE = 0 THEN " + 
		"		'HOLD NOW' " + 
		"	WHEN A.HOLD_TYPE = 1 THEN " + 
		"		'HOLD AT SHIPPING' " + 
		"	END AS transaction_type, " + 
		"	A.ACTUAL_TIMESTAMP as actual_timestamp, " + 
		"	'' as process_point_id, " + 
		"	CASE WHEN (A.QSR_ID is NULL or A.QSR_ID = 0) THEN " + 
		"		'GALC LEGACY HOLD SCREEN' " + 
		"	Else " + 
		"		'REGIONAL GALC HOLD SCREEN' " + 
		"	END as process_point_name, " + 
		"	'N/A' AS defect_id, " + 
		"	A.HOLD_REASON AS full_description, " + 
		"	'' AS actual_problem_description, " + 
		"	'VQ' AS dept_name, " + 
		"	'' AS entry_status, " + 
		"	CASE WHEN A.RELEASE_FLAG = 0 THEN " + 
		"		'NOT RELEASED' " + 
		"	WHEN A.RELEASE_FLAG = 1 THEN " + 
		"		'RELEASED' " + 
		"	END AS current_status " + 
		"	FROM GAL147TBX A " + 
		") result " + 
		"ORDER BY result.actual_timestamp desc ")
public class ProductHistoryDetails implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private ProductHistoryDetailsId id;
	
	@Column(name="actual_timestamp", insertable = false, updatable = false)
	private LocalDateTime actualTimestamp;
	
	@Column(name="defect_id", insertable = false, updatable = false)
	private String defectId;
	
	@Column(name="process_point_id", insertable = false, updatable = false)
	private String processPointId;
	
	@Column(name="process_point_name", insertable = false, updatable = false)
	private String processPointName;
	
	@Column(name="product_id", insertable = false, updatable = false)
	private String productId;
	
	@Column(name="actual_problem_description")
	private String actualProblemDescription;
	
	@Column(name="current_status")
	private String currentStatus;
	
	@Column(name="dept_name")
	private String deptName;
	
	@Column(name="entry_status")
	private String entryStatus;
	
	@Column(name="full_description")
	private String fullDescription;
	
	@Column(name="transaction_type")
	private String transactionType;
	    
	public ProductHistoryDetails() {
		super();
	}

	@Override
	public String toString() {
		
		String str = getClass().getSimpleName() + "{";
		str = str + "id: " + getId();
        str = str + ", actualTimestamp: " + getId().getActualTimestamp();
        str = str + ", defectId: " + getDefectId();
        str = str + ", processPointId: " + getId().getProcessPointId();
        str = str + ", processPointName: " + getProcessPointName();
        str = str + ", productId: " + getProductId();
        str = str + ", actualProblemDescription: " + getActualProblemDescription();
        str = str + ", currentStatus: " + getCurrentStatus();
        str = str + ", deptName: " + getDeptName();
        str = str + ", entryStatus: " + getEntryStatus();
        str = str + ", fullDescription: " + getFullDescription();
        str = str + ", transactionType: " + getTransactionType();
        str = str + "}";
        return str;
		
	}

	public ProductHistoryDetailsId getId() {
		return id;
	}

	public void setId(ProductHistoryDetailsId id) {
		this.id = id;
	}

	public LocalDateTime getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(LocalDateTime actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	public String getDefectId() {
		return defectId;
	}

	public void setDefectId(String defectId) {
		this.defectId = defectId;
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getProcessPointName() {
		return processPointName;
	}

	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getActualProblemDescription() {
		return actualProblemDescription;
	}

	public void setActualProblemDescription(String actualProblemDescription) {
		this.actualProblemDescription = actualProblemDescription;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getEntryStatus() {
		return entryStatus;
	}

	public void setEntryStatus(String entryStatus) {
		this.entryStatus = entryStatus;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualProblemDescription == null) ? 0 : actualProblemDescription.hashCode());
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + ((currentStatus == null) ? 0 : currentStatus.hashCode());
		result = prime * result + ((defectId == null) ? 0 : defectId.hashCode());
		result = prime * result + ((deptName == null) ? 0 : deptName.hashCode());
		result = prime * result + ((entryStatus == null) ? 0 : entryStatus.hashCode());
		result = prime * result + ((fullDescription == null) ? 0 : fullDescription.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((processPointName == null) ? 0 : processPointName.hashCode());
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductHistoryDetails other = (ProductHistoryDetails) obj;
		if (actualProblemDescription == null) {
			if (other.actualProblemDescription != null)
				return false;
		} else if (!actualProblemDescription.equals(other.actualProblemDescription))
			return false;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (currentStatus == null) {
			if (other.currentStatus != null)
				return false;
		} else if (!currentStatus.equals(other.currentStatus))
			return false;
		if (defectId == null) {
			if (other.defectId != null)
				return false;
		} else if (!defectId.equals(other.defectId))
			return false;
		if (deptName == null) {
			if (other.deptName != null)
				return false;
		} else if (!deptName.equals(other.deptName))
			return false;
		if (entryStatus == null) {
			if (other.entryStatus != null)
				return false;
		} else if (!entryStatus.equals(other.entryStatus))
			return false;
		if (fullDescription == null) {
			if (other.fullDescription != null)
				return false;
		} else if (!fullDescription.equals(other.fullDescription))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (processPointName == null) {
			if (other.processPointName != null)
				return false;
		} else if (!processPointName.equals(other.processPointName))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (transactionType == null) {
			if (other.transactionType != null)
				return false;
		} else if (!transactionType.equals(other.transactionType))
			return false;
		return true;
	}
	
	
	
}