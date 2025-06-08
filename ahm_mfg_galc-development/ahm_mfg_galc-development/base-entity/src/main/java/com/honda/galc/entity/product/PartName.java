package com.honda.galc.entity.product;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;

import com.honda.galc.data.ProductType;
import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.PartNameVisibleType;

/** * * 
/**
 * 
 * <h3>PartPanel Class description</h3>
 * <p> PartPanel description </p>
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
 * <TD>YX</TD>
 * <TD>05.14.2014</TD>
 * <TD>0.1</TD>
 * <TD>SR30946</TD>
 * <TD>Add method to get timestamp for any changes of the parts'lot control rules</TD> 
 * </TR> 
 *
 * </TABLE>
*
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL261TBX")
public class PartName extends AuditEntry{
    @Id
    @Column(name = "PART_NAME")
    @Auditable(isPartOfPrimaryKey= true,sequence=1)
    private String partName;

    @Column(name = "PRODUCT_TYPE")
    @Auditable(isPartOfPrimaryKey= false,sequence=2)
    String productType;
    
	@Column(name = "PART_CONFIRM_CHECK")
	@Auditable(isPartOfPrimaryKey= false,sequence=3)
    private int partConfirmCheck;

    @Column(name = "WINDOW_LABEL")
    @Auditable(isPartOfPrimaryKey= false,sequence=4)
    private String windowLabel;
    
    @Column (name = "SUB_PRODUCT_TYPE")
    @Auditable(isPartOfPrimaryKey= false,sequence=5)
    private String subProductType;
    
    @Column(name = "PART_VISIBLE")
    @Auditable(isPartOfPrimaryKey= false,sequence=6)
    private int partVisible;
    
    @Column(name = "REPAIR_CHECK")
    @Auditable(isPartOfPrimaryKey= false,sequence=7)
    private int repairCheck;
    
    @Column(name = "EXTERNAL_REQUIRED")
    @Auditable(isPartOfPrimaryKey= false,sequence=8)
    private int extRequired;
    
    @Column(name = "LET_CHECK_REQUIRED")
    @Auditable(isPartOfPrimaryKey= false,sequence=9)
    private int LETRequired;
    
   

	@OneToMany(targetEntity = PartSpec.class,fetch = FetchType.EAGER)
    @ElementJoinColumn(name="PART_NAME")
    @OrderBy
    private List<PartSpec> allPartSpecs = new ArrayList<PartSpec>();

    @Transient
    private List<PartSpec> parts = new ArrayList<PartSpec>();

    private static final long serialVersionUID = 1L;

    public PartName() {
        super();
    }

    public PartName(String name, String productType) {
		this.partName = name;
		this.productType = productType;
	}

	public String getPartName() {
        return StringUtils.trim(this.partName);
    }
    
    public String getId() {
    	return getPartName();
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getProductTypeName() {
		return StringUtils.trim(productType);
	}
    
    public ProductType getProductType() {
    	return ProductType.getType(getProductTypeName());
    }

	public void setProductTypeName(String productTypeName) {
		this.productType = productTypeName;
	}

    public int getPartConfirmCheck() {
        return this.partConfirmCheck;
    }

    public void setPartConfirmCheck(int partConfirmCheck) {
        this.partConfirmCheck = partConfirmCheck;
    }

    public String getWindowLabel() {
        return StringUtils.trim(this.windowLabel);
    }

    public void setWindowLabel(String windowLabel) {
        this.windowLabel = windowLabel;
    }
    
	public void setSubProductType(String subProductType) {
		this.subProductType = subProductType;
	}

	public String getSubProductType() {
		return StringUtils.trim(this.subProductType);
	}

	public List<PartSpec> getAllPartSpecs() {
		return allPartSpecs;
	}

	public void setAllPartSpecs(List<PartSpec> allPartSpecs) {
		this.allPartSpecs = allPartSpecs;
	}
 
//    public List<PartSpec> getParts() {
//        return parts;
//    }
//
//    public void setParts(List<PartSpec> parts) {
//        for (PartSpec part : parts) {
//            this.parts.add(part);
//        }
//    }
    
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(partName).append(",");
		builder.append(partConfirmCheck).append(",");
		builder.append(StringUtils.trim(windowLabel)).append(",");
		builder.append(subProductType).append(",");
		builder.append(repairCheck).append(",");
		builder.append(partVisible).append(",");
		for(PartSpec p : parts){
			builder.append(",[");
			builder.append(p);
			builder.append("]");
		}
		
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.getPartName() == null) ? 0 : this.getPartName().hashCode());
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
		PartName other = (PartName) obj;
		if (partName == null) {
			if (other.partName != null)
				return false;
		} else if (!this.getPartName().equals(other.getPartName()))
			return false;
		return true;
	}

	/**
	 * get the latest timestamp from all its part specs and measurement specs 
	 */
	public Date getLotControlUpdateTimestamp() {
		Timestamp latest = getCreateTimestamp();
		if(getUpdateTimestamp()!=null && latest.before(getUpdateTimestamp())) {
			latest = getUpdateTimestamp();
		}
		
		for(PartSpec spec: this.allPartSpecs) {
			if(latest==null ||  (spec.getCreateTimestamp()!=null && latest.before(spec.getCreateTimestamp()))) {
				latest = spec.getCreateTimestamp();
			}
			if(latest==null ||  (spec.getUpdateTimestamp()!=null && latest.before(spec.getUpdateTimestamp()))) {
				latest = spec.getUpdateTimestamp();
			}
			for(MeasurementSpec mSpec: spec.getMeasurementSpecs()) {
				if(latest==null ||  (mSpec.getCreateTimestamp()!=null && latest.before(mSpec.getCreateTimestamp()))) {
					latest = mSpec.getCreateTimestamp();
				}
				if(latest==null ||  (mSpec.getUpdateTimestamp()!=null && latest.before(mSpec.getUpdateTimestamp()))) {
					latest = mSpec.getUpdateTimestamp();
				}
			}
		}
		return latest;
	}

	
	 public int getPartVisible() {
			return partVisible;
		}

		public void setPartVisible(int partVisible) {
			this.partVisible = partVisible;
		}
		
		public PartNameVisibleType getPartVisibleType() {
			return PartNameVisibleType.getType(partVisible);
		}
		
		public int getRepairCheck() {
			return repairCheck;
		}

		public void setRepairCheck(int repairCheck) {
			this.repairCheck = repairCheck;
		}
		
		public int getExternalRequired() {
			return this.extRequired;
		}
		
		public void setExternalRequired(int externalRequired) {
			this.extRequired = externalRequired;
		}
		
		public int getLETCheckRequired() {
			return this.LETRequired;
		}
		
		public void setLETCheckRequired(int letCheckRequired) {
			this.LETRequired = letCheckRequired;
		}

		public boolean isTlConfirm() {
			return getPartVisibleType() == PartNameVisibleType.TL_CONFIRM;

		}
}
