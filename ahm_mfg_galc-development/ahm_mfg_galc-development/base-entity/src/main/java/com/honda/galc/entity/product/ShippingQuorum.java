package com.honda.galc.entity.product;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumns;

import com.honda.galc.entity.AbstractEntity;
import com.honda.galc.entity.enumtype.ShippingQuorumDetailStatus;
import com.honda.galc.entity.enumtype.ShippingQuorumStatus;

/**
 * 
 * <h3>ShippingQuorum Class description</h3>
 * <p> ShippingQuorum description </p>
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
@Table(name="QUORUM_TBX")
public class ShippingQuorum extends AbstractEntity implements Comparable<ShippingQuorum>{

	private static final long serialVersionUID = 1L;

	public static int DEFAULT_QUORUM_SIZE = 6;
	
	@EmbeddedId
	private ShippingQuorumId id;

	@Column(name="TRAILER_ID")
	private int trailerId;

	@Column(name="TRAILER_ROW")
	private int trailerRow;

	@Column(name="PALLET_TYPE")
	private String palletType;

	@Column(name="QUORUM_SIZE")
	private int quorumSize;

	@Column(name="STATUS")
	private int statusId;
	
	@Transient
	private String trailerNumber;
	
	@OneToMany(targetEntity = ShippingQuorumDetail.class,fetch = FetchType.EAGER,cascade={})
    @ElementJoinColumns({
    	@ElementJoinColumn(name="QUORUM_DATE",referencedColumnName="QUORUM_DATE",updatable=false,insertable=false),
    	@ElementJoinColumn(name="QUORUM_ID",referencedColumnName="QUORUM_ID",updatable=false,insertable=false)})
    @OrderBy
	private List<ShippingQuorumDetail> shippingQuorumDetails;
	
	public ShippingQuorum() {
		super();
	}

	public ShippingQuorum(Date quorumDate, int quorumId) {
		this.id = new ShippingQuorumId();
		id.setQuorumDate(quorumDate);
		id.setQuorumId(quorumId);
	}
	
	public ShippingQuorumId getId() {
		return this.id;
	}

	public void setId(ShippingQuorumId id) {
		this.id = id;
	}

	public int getTrailerId() {
		return this.trailerId;
	}

	public void setTrailerId(int trailerId) {
		this.trailerId = trailerId;
	}

	public int getTrailerRow() {
		return this.trailerRow;
	}

	public void setTrailerRow(int trailerRow) {
		this.trailerRow = trailerRow;
	}

	public String getPalletType() {
		return StringUtils.trim(this.palletType);
	}

	public void setPalletType(String palletType) {
		this.palletType = palletType;
	}

	public int getQuorumSize() {
		return this.quorumSize;
	}

	public void setQuorumSize(int quorumSize) {
		this.quorumSize = quorumSize;
	}

	public int getStatusId() {
		return this.statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	public ShippingQuorumStatus getStatus() {
		return ShippingQuorumStatus.getType(statusId);
	}
	
	public void setStatus(ShippingQuorumStatus status) {
		this.statusId = status.getId();
	}
	
	public String getProductSpec(int seq) {
		for(ShippingQuorumDetail detail : shippingQuorumDetails) {
			if(detail.getQuorumSeq() == seq) return detail.getYmto();
		}
		return "";
	}
	
	public int getMaxSequence() {
		return shippingQuorumDetails.get(shippingQuorumDetails.size() -1).getQuorumSeq();
	}
	
	public boolean isWaiting(){
        return getStatus() == ShippingQuorumStatus.WAITING;
    }
    
    public boolean isLoading(){
        return getStatus() == ShippingQuorumStatus.LOADING;
    }

    public boolean isAllocating(){
        return getStatus() == ShippingQuorumStatus.ALLOCATING;
    }
    
    public boolean isAllocated(){
        return getStatus() == ShippingQuorumStatus.ALLOCATED;
    }
	
	public String getTrailerNumber() {
		return trailerNumber;
	}

	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}

	public List<ShippingQuorumDetail> getShippingQuorumDetails() {
		if(shippingQuorumDetails == null) shippingQuorumDetails = new ArrayList<ShippingQuorumDetail>();
		return shippingQuorumDetails;
	}

	public void setShippingQuorumDetails(List<ShippingQuorumDetail> shippingQuorumDetails) {
		this.shippingQuorumDetails = shippingQuorumDetails;
	}
	
	/**
	 * count the actual number of quorum details created
	 * quorum size will be changed after resizing
	 * @return
	 */
	public int getActualQuorumSize() {
		return getShippingQuorumDetails().size();
	}
	
	public ShippingQuorumDetail getNextQuorumDetail() {
		for(ShippingQuorumDetail detail: shippingQuorumDetails){
			if (StringUtils.isEmpty(detail.getEngineNumber()))
				return detail;
		}
		return null;
	}
	
	 /**
     * load an engine into the quorum
     * @param engineNumber
     * @param ymto
     * @return
     */
    
	public ShippingQuorumDetail loadEngine(String engineNumber,String ymto) {
        
        ShippingQuorumDetail detail = getNextQuorumDetail();
        if (detail == null) return null;
        if(detail.getYmto()!=null && !detail.getYmto().equalsIgnoreCase(ymto)){
            detail.setStatus(ShippingQuorumDetailStatus.MISS_LOAD);
        }
        detail.setEngineNumber(engineNumber);
        if (getQuorumSize() == getLoadedCount()){
            //when the quorum is finished, may be it is the true complete (full size or it is the repair or exceptional quorum) OR
            // not true complete (resized to smaller size)
            if(getQuorumSize()== getActualQuorumSize() || 
                    isExceptionalQuorum() ||
                    isRepairQuorum() &&
                    !isQuorumMisloaded())
                setStatus(ShippingQuorumStatus.COMPLETE);
            else 
                setStatus(ShippingQuorumStatus.INCOMPLETE);
        }else{
            setStatus(ShippingQuorumStatus.LOADING);
        }
        return detail;
    }
	
	public boolean isQuorumMisloaded(){
		for(ShippingQuorumDetail detail: shippingQuorumDetails){
			if(detail.getStatus().equals(ShippingQuorumDetailStatus.MISS_LOAD)){
    			return true;
    		}
		}
		return false;
	}
	
	/**
	 * get number of engines loaded
	 * @return
	 */
	public int getLoadedCount() {
		int count = 0;
		for(ShippingQuorumDetail detail : getShippingQuorumDetails()) {
			if(!StringUtils.isEmpty(detail.getEngineNumber())) count++;
		}
		return count;
	}
	
	public boolean isRepairQuorum(){
		return trailerId == ShippingTrailerInfo.TRAILER_ID_REPAIR;
	}
	
	public boolean isExceptionalQuorum(){
		return trailerId == ShippingTrailerInfo.TRAILER_ID_EXCEPT;
	}
	
	public  String getQuorumType(){
        if(getTrailerId() == ShippingTrailerInfo.TRAILER_ID_REPAIR) return "R";
        else if(getTrailerId() == ShippingTrailerInfo.TRAILER_ID_EXCEPT) return "E";
        return "N";
	}
	
	public int getTrailerRowSeq() {
		return getTrailerId() * 1000 + getTrailerRow(); 
	}
	
	public String toString() {
		return toString(id.getQuorumDate(),id.getQuorumId(),getTrailerRow(),getQuorumSize(),getPalletType(),getStatus());
	}

	public int compareTo(ShippingQuorum o) {
		int lastComp = o.getId().getQuorumDate().compareTo(this.getId().getQuorumDate());
		return lastComp != 0 ? lastComp : getId().getQuorumId() - o.getId().getQuorumId();
	}

}
