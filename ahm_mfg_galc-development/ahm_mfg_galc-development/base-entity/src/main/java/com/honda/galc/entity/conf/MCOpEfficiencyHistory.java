package com.honda.galc.entity.conf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.enumtype.OperationEfficiencyStatus;
import com.honda.galc.util.ToStringUtil;

/**
 * @author Alok Ghode
 * @date Dec 18, 2015
 */
@Entity
@Table(name="MC_OP_EFFICIENCY_HIST_TBX")
public class MCOpEfficiencyHistory extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="OP_EFFICIENCY_HIST_ID")
	private long opEfficiencyHistId;
	
	@Column(name="PRODUCT_ID", nullable=false, length=17)
	private String productId;
	
	@Column(name="HOST_NAME", length=32)
	private String hostName;
	
	@Column(name="PROCESS_POINT_ID", nullable=false, length=16)
	private String processPointId;
	
	@Column(name="ASM_PROC_NO", nullable=false, length=5)
	private String asmProcNo;
	
	@Column(name="UNIT_NO", nullable=false, length=6)
	private String unitNo;
	
	@Column(name="UNIT_TOT_TIME", nullable=false)
	private double unitTotTime;
	
	@Column(name="START_TIMESTAMP", nullable=false)
	private Date startTimestamp;
	
	@Column(name="END_TIMESTAMP", nullable=false)
	private Date endTimestamp;
	
	@Column(name="ACTUAL_TIME", nullable=false)
	private double actualTime;
	
	@Column(name="STATUS", nullable=false, length=20)
	@Enumerated(EnumType.STRING)
	private OperationEfficiencyStatus status;
	
	@Column(name="ASSOCIATE_NO", nullable=false, length=11)
	private String associateNo;

	public long getOpEfficiencyHistId() {
		return opEfficiencyHistId;
	}

	public void setOpEfficiencyHistId(long opEfficiencyHistId) {
		this.opEfficiencyHistId = opEfficiencyHistId;
	}

	public String getProductId() {
		return StringUtils.trim(productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getHostName() {
        return StringUtils.trim(hostName);
    }
    
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

	public String getProcessPointId() {
		return StringUtils.trim(processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getAsmProcNo() {
		return StringUtils.trim(asmProcNo);
	}

	public void setAsmProcNo(String asmProcNo) {
		this.asmProcNo = asmProcNo;
	}

	public String getUnitNo() {
		return StringUtils.trim(unitNo);
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	public double getUnitTotTime() {
		return unitTotTime;
	}

	public void setUnitTotTime(double unitTotTime) {
		this.unitTotTime = unitTotTime;
	}

	public Date getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(Date startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public Date getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(Date endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public double getActualTime() {
		return actualTime;
	}

	public void setActualTime(double actualTime) {
		this.actualTime = actualTime;
	}

	public OperationEfficiencyStatus getStatus() {
		return status;
	}

	public void setStatus(OperationEfficiencyStatus status) {
		this.status = status;
	}

	public String getAssociateNo() {
		return StringUtils.trim(associateNo);
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}

	public Object getId() {
		return this.opEfficiencyHistId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime * result
				+ ((hostName == null) ? 0 : hostName.hashCode());
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result
				+ ((asmProcNo == null) ? 0 : asmProcNo.hashCode());
		result = prime * result
				+ ((unitNo == null) ? 0 : unitNo.hashCode());
		long temp;
		temp = Double.doubleToLongBits(unitTotTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((startTimestamp == null) ? 0 : startTimestamp.hashCode());
		result = prime * result
				+ ((endTimestamp == null) ? 0 : endTimestamp.hashCode());
		temp = Double.doubleToLongBits(actualTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((associateNo == null) ? 0 : associateNo.hashCode());
		result = prime * result + (int) (opEfficiencyHistId ^ (opEfficiencyHistId >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MCOpEfficiencyHistory other = (MCOpEfficiencyHistory) obj;
		if (opEfficiencyHistId != other.opEfficiencyHistId) {
			return false;
		}
		if (productId == null) {
			if (other.productId != null) {
				return false;
			}
		} else if (!productId.equals(other.productId)) {
			return false;
		}
		if (hostName == null) {
			if (other.hostName != null) {
				return false;
			}
		} else if (!hostName.equals(other.hostName)) {
			return false;
		}
		if (processPointId == null) {
			if (other.processPointId != null) {
				return false;
			}
		} else if (!processPointId.equals(other.processPointId)) {
			return false;
		}
		if (asmProcNo == null) {
			if (other.asmProcNo != null) {
				return false;
			}
		} else if (!asmProcNo.equals(other.asmProcNo)) {
			return false;
		}
		if (unitNo == null) {
			if (other.unitNo != null) {
				return false;
			}
		} else if (!unitNo.equals(other.unitNo)) {
			return false;
		}
		if (Double.doubleToLongBits(unitTotTime) != Double
				.doubleToLongBits(other.unitTotTime)) {
			return false;
		}
		if (startTimestamp == null) {
			if (other.startTimestamp != null) {
				return false;
			}
		} else if (!startTimestamp.equals(other.startTimestamp)) {
			return false;
		}
		if (endTimestamp == null) {
			if (other.endTimestamp != null) {
				return false;
			}
		} else if (!endTimestamp.equals(other.endTimestamp)) {
			return false;
		}
		if (Double.doubleToLongBits(actualTime) != Double
				.doubleToLongBits(other.actualTime)) {
			return false;
		}
		if (status == null) {
			if (other.status != null) {
				return false;
			}
		} else if (!status.equals(other.status)) {
			return false;
		}
		if (associateNo == null) {
			if (other.associateNo != null) {
				return false;
			}
		} else if (!associateNo.equals(other.associateNo)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
