package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * 
 * <h3>ShippingTrailerRack Class description</h3>
 * <p> ShippingTrailerRack description </p>
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
 * Jan 29, 2015
 *
 *
 */
@Entity
@Table(name="TRAILER_RACK_TBX")
public class ShippingTrailerRack extends AuditEntry {
	
	@EmbeddedId
    private ShippingTrailerRackId id;
	
	@Column(name="DESCRIPTION")
	private String description;

	@Column(name="QUANTITY")
	private int quantity;

	private static final long serialVersionUID = 1L;

	public ShippingTrailerRack() {
		super();
	}
	
	public ShippingTrailerRack(String trailerNumber,RackType rackType) {
		super();
		ShippingTrailerRackId id = new ShippingTrailerRackId();
		id.setTrailerNumber(trailerNumber);
		id.setRackType(rackType.toString());
		setId(id);
		setDescription(rackType.getDescription());
	}

	public ShippingTrailerRack(String trailerNumber,RackType rackType, int quantity) {
	   this(trailerNumber,rackType);	
	   setQuantity(quantity);
	}	
	
	public ShippingTrailerRackId getId() {
		return id;
	}
	
	public void setId(ShippingTrailerRackId id) {
		this.id = id;
	}

	public String getDescription() {
		return StringUtils.trim(description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public RackType getRackType() {
		return RackType.getType(id.getRackType());
	}

    public String toString() {
    	return toString(id.getTrailerNumber(),id.getRackType(),quantity);
    }

    public static enum RackType{
        
		Blue("Blue Carrier","Blue Engine"), 
        Gray("Gray Carrier","Gray Engine Pallet"),
        Hanger("Engine Hanger","Engine Hanger Bracket");
        
        private String shortDescription;
		private String description;
        
        private RackType(String shortDescription,String description) {
        	this.shortDescription = shortDescription;
        	this.description = description;
        }

		public String getDescription() {
			return description;
		}
		
		public String getShortDescription() {
			return shortDescription;
		}
		
		public static RackType getType(String name) {
			for(RackType type :RackType.values()) {
				if(type.toString().equalsIgnoreCase(name)) return type;
			}
			return null;
		}
    }
}
