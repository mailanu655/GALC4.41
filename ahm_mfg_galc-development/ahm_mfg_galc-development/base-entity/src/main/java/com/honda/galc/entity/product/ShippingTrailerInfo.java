package com.honda.galc.entity.product;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.ShippingTrailerInfoStatus;

/**
 * 
 * <h3>ShippingTrailerInfo Class description</h3>
 * <p> ShippingTrailerInfo description </p>
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
 * Jun 1, 2012
 *
 *
 */
@Entity
@Table(name="TRAILER_INFO_TBX")
public class ShippingTrailerInfo extends AuditEntry {
	
	public static final int TRAILER_ID_REPAIR = -1;
	public static final int TRAILER_ID_EXCEPT = -2;
	
	@Id
	@Column(name="TRAILER_ID")
	private int trailerId;

	@Column(name="TRAILER_NUMBER")
	private String trailerNumber;

	@Column(name="SCH_QTY")
	private int schQty;

	@Column(name="ACT_QTY")
	private int actQty;

	@Column(name="STATUS")
	private int statusId;
	
	@OneToMany(targetEntity = ShippingQuorum.class,fetch = FetchType.LAZY,cascade={})
    @ElementJoinColumn(name="TRAILER_ID",referencedColumnName="TRAILER_ID",updatable=false,insertable=false)
    @OrderBy
	private List<ShippingQuorum> shippingQuorums;
	
	
	private static final long serialVersionUID = 1L;

	public ShippingTrailerInfo() {
		super();
	}

	public int getTrailerId() {
		return this.trailerId;
	}

	public void setTrailerId(int trailerId) {
		this.trailerId = trailerId;
	}
	
	public Integer getId() {
		return getTrailerId();
	}


	public String getTrailerNumber() {
		return StringUtils.trim(this.trailerNumber);
	}

	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}

	public int getSchQty() {
		return this.schQty;
	}

	public void setSchQty(int schQty) {
		this.schQty = schQty;
	}

	public int getActQty() {
		return this.actQty;
	}

	public void setActQty(int actQty) {
		this.actQty = actQty;
	}

	public int getStatusId() {
		return this.statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	public ShippingTrailerInfoStatus getStatus() {
		return ShippingTrailerInfoStatus.getType(statusId);
	}
	
	public void setStatus(ShippingTrailerInfoStatus status) {
		this.statusId = status.getId();
	}

	public List<ShippingQuorum> getShippingQuorums() {
		if(shippingQuorums == null) shippingQuorums = new ArrayList<ShippingQuorum>();
		return shippingQuorums;
	}

	public void setShippingQuorums(List<ShippingQuorum> shippingQuorums) {
		this.shippingQuorums = shippingQuorums;
	}
	
	public Date getAssignDate() {
		return new Date(getCreateTimestamp().getTime());
	}
	
	/**
     * Load one engine into the trailer
     *
     */
    
    public void loadEngine(){
        
        if(getActQty() >= getSchQty()) return;
    	this.actQty += 1;
        
        if(getSchQty() == getActQty()) 
            setStatus(ShippingTrailerInfoStatus.PCMP);
        else if(getStatus() != ShippingTrailerInfoStatus.LOADING)
                setStatus(ShippingTrailerInfoStatus.LOADING);
        
    }

	public String toString() {
		return toString(getTrailerId(),getTrailerNumber(),getSchQty(),getActQty(),getStatus());
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if ( ! (o instanceof ShippingTrailerInfo))	return false;
		
		ShippingTrailerInfo other = (ShippingTrailerInfo) o;
		return this.trailerId == other.trailerId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + trailerId;
		return result;
	}

}
