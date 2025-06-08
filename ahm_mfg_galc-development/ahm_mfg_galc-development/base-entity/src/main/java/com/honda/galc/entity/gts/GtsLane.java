package com.honda.galc.entity.gts;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.GtsLaneType;
import com.honda.galc.entity.enumtype.GtsOrientation;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * 
 * <h3>GtsLane Class description</h3>
 * <p> GtsLane description </p>
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
 * Nov 16, 2014
 *
 *
 */
@Entity
@Table(name="GTS_LANE_TBX")
public class GtsLane extends AuditEntry {
	@EmbeddedId
	private GtsLaneId id;

	@Column(name="LANE_DESCRIPTION")
	private String laneDescription;

	@Column(name="LANE_CAPACITY")
	private int laneCapacity;

	@Column(name="LANE_TYPE")
	private int laneType;

	@Column(name="ENTRY_GATE_ID")
	private int entryGateId;

	@Column(name="EXIT_GATE_ID")
	private int exitGateId;

	@Column(name="CARRIER_PRESENT")
	private int carrierPresent;

	@Column(name="IS_PHYSICALLY_FULL")
	private int isPhysicallyFull;

	@Transient
	private List<GtsLaneSegmentMap> laneSegmentMaps = new SortedArrayList<GtsLaneSegmentMap>("getLaneSeq");

//	@OneToMany(fetch = FetchType.EAGER,mappedBy="lane")
	private List<GtsLaneCarrier> laneCarriers = new ArrayList<GtsLaneCarrier>();

	private static final long serialVersionUID = 1L;

	public GtsLane() {
		super();
	}
	
	public GtsLane(String laneId) {
		id = new GtsLaneId();
		id.setLaneId(laneId);
	}

	public GtsLaneId getId() {
		return this.id;
	}

	public void setId(GtsLaneId id) {
		this.id = id;
	}

	public String getLaneDescription() {
		return this.laneDescription;
	}

	public void setLaneDescription(String laneDescription) {
		this.laneDescription = laneDescription;
	}

	public int getLaneCapacity() {
		return this.laneCapacity;
	}

	public void setLaneCapacity(int laneCapacity) {
		this.laneCapacity = laneCapacity;
	}
	
	public String getLaneName() {
		return id.getLaneId();
	}

	public int getLaneTypeValue() {
		return this.laneType;
	}

	public void setLaneTypeValue(int laneType) {
		this.laneType = laneType;
	}
	
	public GtsLaneType getLaneType(){
		return GtsLaneType.getType(laneType);
	}
	
	public void setLaneType(GtsLaneType laneType) {
		this.laneType = laneType.getId();
	}
	
	public boolean isEntryLane() {
		return GtsLaneType.ENTRY == getLaneType();
	}

	public boolean isExitLane() {
		return GtsLaneType.EXIT == getLaneType();
	}
	
	public int getEntryGateId() {
		return this.entryGateId;
	}

	public void setEntryGateId(int entryGateId) {
		this.entryGateId = entryGateId;
	}

	public int getExitGateId() {
		return this.exitGateId;
	}

	public void setExitGateId(int exitGateId) {
		this.exitGateId = exitGateId;
	}

	public int getCarrierPresent() {
		return this.carrierPresent;
	}

	public void setCarrierPresent(int carrierPresent) {
		this.carrierPresent = carrierPresent;
	}

	public int getIsPhysicallyFull() {
		return this.isPhysicallyFull;
	}

	public void setIsPhysicallyFull(short isPhysicallyFull) {
		this.isPhysicallyFull = isPhysicallyFull;
	}
	
	 /**
     * get the direction of the carrier
     * the direction is either EAST or WEST, SOUTH,NORTH based on the shape of the line segment on which 
     * the carrier is placed
     * @param carrier
     * @return direction - EAST or WEST or SOUTH or NORTH
     */
    
    public GtsOrientation getDirection(GtsLaneCarrier carrier){
        int position = carrier.getPosition() + 1;
        int pos = 0;
        for (GtsLaneSegmentMap laneSegmentMap: laneSegmentMaps){
        	GtsLaneSegment laneSegment = laneSegmentMap.getLaneSegment();
            if (position >= pos && position <= pos + laneSegment.getCapacity()) {
                return laneSegment.getDirectionForPosition(position - pos);
            }
            pos +=laneSegment.getCapacity();
            
        }
        
        return GtsOrientation.EAST;
    }
    
    /**
     * get the direction of the carrier
     * the direction is either EAST or WEST based on the shape of the line segment on which 
     * the carrier is placed
     * @param carrier
     * @return direction - EAST or WEST
     */
    
    public GtsOrientation getHorizontalDirection(GtsLaneCarrier carrier){
        int position = carrier.getPosition() + 1;
        int pos = 0;
        for (GtsLaneSegmentMap laneSegmentMap: laneSegmentMaps){
        	GtsLaneSegment laneSegment = laneSegmentMap.getLaneSegment();
           if (position >= pos && position <= pos + laneSegment.getCapacity()) {
                return laneSegment.getDirection();
            }
            pos +=laneSegment.getCapacity();
            
        }
        return GtsOrientation.EAST;
    }
    
    /**
     * get the position of the carrier on this lane
     * position starts from 0
     * @param carrier
     * @return position
     */
    
	public int getPosition(GtsLaneCarrier carrier){
		return laneCarriers.indexOf(carrier);
	}
	
	/**
     * Returns the coordinate of the point to locate the carrier.
     *
     * @param carrier the carrier
     * @return coordinate(x,y)
     */
	
	public Point2D.Double getPoint(GtsLaneCarrier carrier){
		int position = carrier.getPosition() + 1;
		int pos = 0;
		for (GtsLaneSegmentMap laneSegmentMap: laneSegmentMaps){
			GtsLaneSegment laneSegment = laneSegmentMap.getLaneSegment();
			if (position >= pos && position <= pos + laneSegment.getCapacity()) {
            	return laneSegment.getPointForPosition(position - pos);
            }
			pos +=laneSegment.getCapacity();
            
		}
		return null;
		
	}
	
	/**
     * get input node of the lane - escape laneSegments with 0 capacity
     * those lane segments are normally transfer lane
     * @return
	 */
    
    public GtsNode getInputNode(){
        
        if (laneSegmentMaps.isEmpty()) return null;
        
        return laneSegmentMaps.get(laneSegmentMaps.size() -1).getLaneSegment().getInputNode();

    }
 
    /**
     * get output node of the lane - escape laneSegments with 0 capacity
     * those lane segments are normally transfer lane
     * @return
     */
    
    public GtsNode getOutputNode(){
        
        if (laneSegmentMaps.isEmpty()) return null;
        
        return laneSegmentMaps.get(0).getLaneSegment().getOutputNode();
    }
    
    /**
     * check if aNode is the input node of this lane
     * @param aNode
     * @return
     */
    
    public boolean isInputNode(GtsNode aNode){
        GtsNode node = getInputNode();
        return node != null && node.equals(aNode);
    }

    /**
     * check if aNode is the output node of this lane
     * @param aNode
     * @return
     */
    
    public boolean isOutputNode(GtsNode aNode){
        GtsNode node = getOutputNode();
        return node != null && node.equals(aNode);
    }
    
   public boolean isCarrierVisible(GtsLaneCarrier carrier){
        
        int position = carrier.getPosition() + 1;
        int pos = 0;
        for (GtsLaneSegmentMap laneSegmentMap: laneSegmentMaps){
            if (position >= pos && position <= pos + laneSegmentMap.getLaneSegment().getCapacity()) {
                return laneSegmentMap.getLaneSegment().isCarrierVisible();
            }
            pos +=laneSegmentMap.getLaneSegment().getCapacity();
            
        }
        
        return false;
    }
 
   	public boolean isFull(){
   		return laneCarriers.size() >= laneCapacity;
   	}
    
    public String getLaneId() {
    	return id.getLaneId();
    }
    
    public void removeLaneSegmentMap(GtsLaneSegmentMap laneSegmentMap){
		laneSegmentMaps.remove(laneSegmentMap);
		laneCapacity = countLaneCapacity();
	}
    
    public void addLaneSegmentMap(GtsLaneSegmentMap laneSegmentMap) {
    	laneSegmentMaps.add(laneSegmentMap);
    	laneCapacity = countLaneCapacity();
    }
    
    private int countLaneCapacity() {
    	int count = 0;
    	for (GtsLaneSegmentMap laneSegmentMap: laneSegmentMaps){
            count += laneSegmentMap.getLaneSegment().getCapacity();
    	}
    	return count;
    }

	 /**
     * create dummy carrier for GBS layout editing
     *
     */
    
    public void initializeDummyCarriers(){
        laneCarriers.clear();
        for(int i=0;i<getLaneCapacity();i++){
            GtsLaneCarrier laneCarrier = new GtsLaneCarrier(this.id.getTrackingArea(),this.id.getLaneId());
            laneCarrier.setPosition(i);
            laneCarrier.setLane(this);
            laneCarrier.setLaneCarrier("Dummy");
            laneCarrier.setCarrier(new GtsCarrier("Dummy"));
            laneCarriers.add(laneCarrier);
        }
    }

    public List<GtsLaneSegmentMap> getLaneSegmentMaps() {
		return laneSegmentMaps;
	}
    
    public int getMaxLaneMapSeq() {
    	int seq = 0;
    	
    	for(GtsLaneSegmentMap laneSegmentMap : getLaneSegmentMaps()) {
    		if(laneSegmentMap.getLaneSeq() > seq) seq = laneSegmentMap.getLaneSeq();
    	}
    	
    	return seq;
    	
    }
    
    public List<GtsLaneSegment> getLaneSegments() {
    	List<GtsLaneSegment> laneSegments = new ArrayList<GtsLaneSegment>();
    	for(GtsLaneSegmentMap laneSegmentMap : laneSegmentMaps) {
    		laneSegments.add(laneSegmentMap.getLaneSegment());
    	}
		return laneSegments;
	}
    
    public void setLaneSegmentMaps(List<GtsLaneSegmentMap> laneSegmentMaps) {
		this.laneSegmentMaps = laneSegmentMaps;
	}
    
    /**
     * move the lane segment up in the laneSegments list
     * @param index
     */
    
    public void moveUpLaneSegment(int index){
        
        if(index <=0 || index >= getLaneSegmentMaps().size()) return;
        
        GtsLaneSegmentMap segmentMap = laneSegmentMaps.remove(index);
        laneSegmentMaps.add(index-1,segmentMap);
    }
    
    public List<GtsLaneSegment> reverseDirection() {
    	List<GtsLaneSegment> laneSegments = new ArrayList<GtsLaneSegment>();
    	for (GtsLaneSegmentMap laneSegmentMap : laneSegmentMaps) {
    		GtsLaneSegment laneSegment = laneSegmentMap.getLaneSegment();
    		if(laneSegment!=null) {
    			laneSegment.reverseDirection();
    			laneSegments.add(laneSegment);
    		}
    	}
    	return laneSegments;
    }

	public List<GtsLaneCarrier> getLaneCarriers() {
		if(laneCarriers == null) laneCarriers = new ArrayList<GtsLaneCarrier>();
		return this.laneCarriers;
	}
	
	public GtsLaneCarrier getHeadCarrier() {
		return getLaneCarriers().isEmpty() ? null : getLaneCarriers().get(0);
	}
	
	public GtsLaneCarrier getTailCarrier() {
		return getLaneCarriers().isEmpty() ? null : getLaneCarriers().get(getLaneCarriers().size() - 1);
	}
	
	public GtsLaneCarrier getReleasedTailCarrier() {
		for(int i=getLaneCarriers().size() - 1;i>=0;i--) {
		GtsLaneCarrier laneCarrier = getLaneCarriers().get(i);
		if(laneCarrier.getProduct() != null && laneCarrier.getProduct().isReleased()) 
			return laneCarrier;
		}
		return null;
	}

	public int getAvailableSpaces() {
		return getLaneCapacity() - getLaneCarriers().size();
	}

	public void setLaneCarriers(List<GtsLaneCarrier> laneCarriers) {
		this.laneCarriers = laneCarriers;
	}
	
	public String getProdLotOfLastReleasedProduct() {
		GtsLaneCarrier lc = getReleasedTailCarrier();
		return lc != null ? lc.getProductionLot() : "";
	}
	
	public int sameLastLotCount(String prodLot) {
		if(getLaneCarriers().isEmpty()) return 0;
		int count = 0;
		for(int i=getLaneCarriers().size() - 1;i>=0;i--) {
			if(prodLot.equalsIgnoreCase(getLaneCarriers().get(i).getProductionLot())) {
				count++; 
			}else break;
		}
		return count;
	}
	
	public boolean isSameLastReleasedProductLot(String prodLot) {
		if(getLaneCarriers().isEmpty()) return false;	
		if (prodLot.equalsIgnoreCase(getProdLotOfLastReleasedProduct()))
					return true;
		return false;
	}
	
	public int sameLotCount(String prodLot) {
		if(getLaneCarriers().isEmpty()) return 0;
		int count = 0;
		for(GtsLaneCarrier laneCarrier : getLaneCarriers()) {
			if(prodLot.equalsIgnoreCase(laneCarrier.getProductionLot())) {
				count++; 
			};
		}
		return count;
	}
	
	public int sameReleasedProductLotCount(String prodLot) {
		if(getLaneCarriers().isEmpty()) return 0;
		int count = 0;
		for(GtsLaneCarrier laneCarrier : getLaneCarriers()) {
			if (laneCarrier.getProduct() != null && laneCarrier.getProduct().isReleased()) {
				if(prodLot.equalsIgnoreCase(laneCarrier.getProductionLot())) {
					count++; 
				}
			};
		}
		return count;
	}
	
	public String toString() {
		return toString(getId().getTrackingArea(),getId().getLaneId(),getLaneCapacity(),getEntryGateId(),
				getExitGateId(),getCarrierPresent());
	}
}
