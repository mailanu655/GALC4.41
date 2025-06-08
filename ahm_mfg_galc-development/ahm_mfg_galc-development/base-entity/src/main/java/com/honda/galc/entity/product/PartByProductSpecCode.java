package com.honda.galc.entity.product;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.honda.galc.dto.Auditable;

import com.honda.galc.entity.UserAuditEntry;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="GAL245TBX")
public class PartByProductSpecCode extends UserAuditEntry{
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey= true,sequence=1)
	private PartByProductSpecCodeId id;

	@SuppressWarnings("unused")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "MODEL_YEAR_CODE", referencedColumnName = "MODEL_YEAR_CODE",
                    unique = true, nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "MODEL_CODE", referencedColumnName = "MODEL_CODE",
                    unique = true, nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "MODEL_TYPE_CODE", referencedColumnName = "MODEL_TYPE_CODE",
                    unique = true, nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "MODEL_OPTION_CODE", referencedColumnName = "MODEL_OPTION_CODE",
                    unique = true, nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "EXT_COLOR_CODE", referencedColumnName = "EXT_COLOR_CODE",
                    unique = true, nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "INT_COLOR_CODE", referencedColumnName = "INT_COLOR_CODE",
                    unique = true, nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "PART_NAME", referencedColumnName = "PART_NAME",
                    unique = true, nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "PRODUCT_SPEC_CODE", referencedColumnName = "PRODUCT_SPEC_CODE",
            		unique = true, nullable = false, insertable = false, updatable = false)
    })
	private LotControlRule lotControlRule;
	
	@OneToOne(targetEntity = PartSpec.class,fetch = FetchType.EAGER)
	@JoinColumns({
		@JoinColumn(name="PART_NAME",referencedColumnName="PART_NAME"),
		@JoinColumn(name="PART_ID",referencedColumnName="PART_ID")
	})
    private PartSpec partSpec;
	
	public PartByProductSpecCode() {
		super();
	}

	public PartByProductSpecCodeId getId() {
		return this.id;
	}

	public void setId(PartByProductSpecCodeId id) {
		this.id = id;
	}
	
	public PartSpec getPartSpec() {
		return partSpec;
	}

	public void setPartSpec(PartSpec partSpec) {
		this.partSpec = partSpec;
	}

	public PartByProductSpecCode detach() {
		PartByProductSpecCode entity = new PartByProductSpecCode();
		entity.setId(id);
		return entity;
	}
	
	public String getProductSpecCode() {
		return id.getProductSpecCode();
	}

	@Override
	public String toString() {
		return toString(id.getProductSpecCode(),
						id.getModelYearCode(),
						id.getModelCode(),
						id.getModelTypeCode(),
						id.getModelOptionCode(),
						id.getExtColorCode(),
						id.getIntColorCode(),
						id.getPartName(),
						id.getPartId());
	}
}
