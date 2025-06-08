package com.honda.galc.client.gts.view;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMain;
import com.honda.galc.client.gts.figure.ImageFactory;
import com.honda.galc.client.gts.property.GtsClientPropertyBean;
import com.honda.galc.common.message.Message;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.gts.GtsAreaDao;
import com.honda.galc.dao.gts.GtsCarrierDao;
import com.honda.galc.dao.gts.GtsClientListDao;
import com.honda.galc.dao.gts.GtsColorMapDao;
import com.honda.galc.dao.gts.GtsDecisionPointConditionDao;
import com.honda.galc.dao.gts.GtsDecisionPointDao;
import com.honda.galc.dao.gts.GtsIndicatorDao;
import com.honda.galc.dao.gts.GtsLabelDao;
import com.honda.galc.dao.gts.GtsLaneCarrierDao;
import com.honda.galc.dao.gts.GtsLaneDao;
import com.honda.galc.dao.gts.GtsLaneSegmentDao;
import com.honda.galc.dao.gts.GtsLaneSegmentMapDao;
import com.honda.galc.dao.gts.GtsMoveConditionDao;
import com.honda.galc.dao.gts.GtsMoveDao;
import com.honda.galc.dao.gts.GtsNodeDao;
import com.honda.galc.dao.gts.GtsOutlineImageDao;
import com.honda.galc.dao.gts.GtsOutlineMapDao;
import com.honda.galc.dao.gts.GtsShapeDao;
import com.honda.galc.dao.gts.GtsZoneDao;
import com.honda.galc.dao.product.CounterDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.GtsLaneType;
import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsCarrierId;
import com.honda.galc.entity.gts.GtsClientList;
import com.honda.galc.entity.gts.GtsClientListId;
import com.honda.galc.entity.gts.GtsColorMap;
import com.honda.galc.entity.gts.GtsDecisionPoint;
import com.honda.galc.entity.gts.GtsDecisionPointCondition;
import com.honda.galc.entity.gts.GtsDecisionPointId;
import com.honda.galc.entity.gts.GtsIndicator;
import com.honda.galc.entity.gts.GtsLabel;
import com.honda.galc.entity.gts.GtsLabelId;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsLaneSegment;
import com.honda.galc.entity.gts.GtsLaneSegmentMap;
import com.honda.galc.entity.gts.GtsMove;
import com.honda.galc.entity.gts.GtsMoveCondition;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.entity.gts.GtsOutlineImage;
import com.honda.galc.entity.gts.GtsOutlineMap;
import com.honda.galc.entity.gts.GtsProduct;
import com.honda.galc.entity.gts.GtsShape;
import com.honda.galc.entity.gts.GtsShapeId;
import com.honda.galc.entity.gts.GtsZone;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.property.GtsDefaultPropertyBean;
import com.honda.galc.service.gts.GtsEbs2TrackingService;
import com.honda.galc.service.gts.GtsEngineBodyTrackingService;
import com.honda.galc.service.gts.GtsPbs1TrackingService;
import com.honda.galc.service.gts.GtsPbs2TrackingService;
import com.honda.galc.service.gts.GtsTbs1TrackingService;
import com.honda.galc.service.gts.GtsWbs1TrackingService;
import com.honda.galc.service.gts.GtsWbs2TrackingService;
import com.honda.galc.service.gts.IBodyTrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * 
 * <h3>GtsTrackingModel Class description</h3>
 * <p> GtsTrackingModel description </p>
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
 * May 26, 2015
 *
 *
 */
public class GtsTrackingModel {
	
	private String trackingArea ="ESTS";
	private GtsArea area;
	private List<GtsLane> lanes = new ArrayList<GtsLane>();
	private List<GtsNode> nodes = new ArrayList<GtsNode>();
    private List<GtsLaneSegment> laneSegments = new ArrayList<GtsLaneSegment>();
    private List<GtsLabel> labels = new ArrayList<GtsLabel>();
    private List<GtsShape> shapes = new ArrayList<GtsShape>();
    private List<GtsProduct> products = new ArrayList<GtsProduct>();
    private List<GtsIndicator> indicators = new ArrayList<GtsIndicator>();
    private List<GtsMove> moves = new ArrayList<GtsMove>();
    private List<GtsMoveCondition> moveConditions = new ArrayList<GtsMoveCondition>();
    
    private boolean isInManualMode = false;
  	
    private ApplicationContext applicationContext;
    
    private Map<String, Class<? extends IBodyTrackingService>> bodyTrackingServiceMap = 
    	new HashMap<String,Class<? extends IBodyTrackingService>>()
    { 
 		private static final long serialVersionUID = 1L;

		{
    		put("ESTS",GtsEngineBodyTrackingService.class);
    		put("P1PBS",GtsPbs1TrackingService.class);
    		put("P1TBS",GtsTbs1TrackingService.class);
    		put("P1WBS",GtsWbs1TrackingService.class);
    		put("P2PBS",GtsPbs2TrackingService.class);
    		put("P2WBS",GtsWbs2TrackingService.class);
    		put("P2EBS",GtsEbs2TrackingService.class);
    	}
    };
    
    
	
    public GtsTrackingModel(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
    }
    
    private IBodyTrackingService getBodyTrackingService() {
    	Class<? extends IBodyTrackingService> clazz = bodyTrackingServiceMap.get(trackingArea);
    	return getService(clazz);
    }
    
   	public GtsArea getArea() {
		return area;
	}
   	
	public void setArea(GtsArea area) {
		this.area = area;
	}
	
	public List<GtsLane> getLanes() {
			return lanes;
	}
	
	public void setLanes(List<GtsLane> lanes) {
		this.lanes = lanes;
	}
	
	public List<GtsNode> getNodes() {
		return nodes;
	}
	
	public void setNodes(List<GtsNode> nodes) {
		this.nodes = nodes;
	}
	
	public void addNode(GtsNode node) {
		GtsNode aNode = this.findNode(node.getNodeId());
		if(aNode == null) this.nodes.add(node);
	}
	
	public List<GtsLaneSegment> getLaneSegments() {
		return laneSegments;
	}
	
	public void setLaneSegments(List<GtsLaneSegment> laneSegments) {
		this.laneSegments = laneSegments;
	}
	
	/**
     * get the lane segments which has not been assigned to any lane yet
     * @return
     */
    public List<GtsLaneSegment> getAvailableLaneSegments(GtsLane lane){
        List<GtsLaneSegment> segments = new SortedArrayList<GtsLaneSegment>("getLaneSegmentName");
        for(GtsLaneSegment segment:laneSegments){
        	if(lane == null) segments.add(segment);
        	else {
        		if(lane.getLaneType().equals(GtsLaneType.MOVE_PROGRESS)) {
        		    if(segment.getLane(lane.getLaneId()) == null) 
                           segments.add(segment);
        	   }else {
        		   if(segment.getLane() == null ) segments.add(segment);
        	   }
        	}
        	
        }
        return segments;
    }
	
	public List<GtsLabel> getLabels() {
		return labels;
	}
	
	public void setLabels(List<GtsLabel> labels) {
		this.labels = labels;
	}
	
	public List<GtsShape> getShapes() {
		return shapes;
	}
	
	public void setShapes(List<GtsShape> shapes) {
		this.shapes = shapes;
	}
	
	public List<GtsProduct> getProducts() {
		return products;
	}
	
	public void setProducts(List<GtsProduct> products) {
		this.products = products;
	}
	
	public List<GtsIndicator> getIndicators() {
		return indicators;
	}
	
	public void setIndicators(List<GtsIndicator> indicators) {
		this.indicators = indicators;
	}
	
	public List<GtsMove> getMoves() {
		return moves;
	}
	
	public void setMoves(List<GtsMove> moves) {
		this.moves = moves;
	}
	
	public List<GtsMoveCondition> getMoveConditions() {
		return moveConditions;
	}
	
	public void setMoveConditions(List<GtsMoveCondition> moveConditions) {
		this.moveConditions = moveConditions;
	}
	
	public boolean isInManualMode() {
		return isInManualMode;
	}

	public void setInManualMode(boolean isInManualMode) {
		this.isInManualMode = isInManualMode;
	}
	
	
	public boolean isControllAllowed() {
		return isUserControllable() &&
		        (!getPropertyBean().isAllowManualMode()
		     		|| isInManualMode());
	}
	
	public GtsNode getEntryNode(String laneName){
        
        String gate = "ENTRY-"+laneName;
        for(GtsNode node: this.getNodes()){
            if(node.isVisible() && node.getNodeName().equals(gate)) return node;
        }
        
        return null;
  
    }
	
	public GtsNode getExitNode(String laneName){
        
        String gate = "EXIT-"+laneName;
        for(GtsNode node: this.getNodes()){
            if(node.isVisible() && node.getNodeName().equals(gate)) return node;
        }
        
        return null;
  
    }
	
	public GtsNode getNearestNode(GtsNode node) {
		double distance = Double.MAX_VALUE;
        GtsNode tmpNode = null;
        
        for(GtsLaneSegment laneSegment :laneSegments) {
        	
        	if(node.getNodeId()!= laneSegment.getInputNodeId() && node.getNodeId()!= laneSegment.getOutputNodeId()){
                double dist = node.distance(laneSegment.getInputNode());
                if(dist < distance) {
                    distance = dist;
                    tmpNode = laneSegment.getInputNode();
                }
                
                dist = node.distance(laneSegment.getOutputNode());
                if(dist < distance) {
                    distance = dist;
                    tmpNode = laneSegment.getOutputNode();
                }
        	}

        }
        	
        return tmpNode;
	}
	
	public void setIndicator(GtsIndicator indicator) {
		for(GtsIndicator item : getIndicators()) {
			if(item.equals(indicator)) {
				item.setIndicatorValue(indicator.getIndicatorValue());
				return;
			}
		}
	}
	
	public GtsIndicator findMoveInIndicator(String inLaneName) {
		for(GtsIndicator item : getIndicators()) {
			if(item.isMoveInProgress() && item.isStatusOn() && item.getDestLaneName().equalsIgnoreCase(inLaneName)) {
				return item;
			}
		}
		return null;
	}
	
	 /**
     * test if a lane with laneName is closed
     * 
     * @param laneName
     * @return
     */
    
    public boolean areLaneGatesClosed(String laneName) {
        
        GtsNode node;
        
        node = getEntryNode(laneName);
        if(node != null && node.isGateOpen()) return false;
        node = getExitNode(laneName);
        if(node != null && node.isGateOpen()) return false;
        
        return true;
    }
    
    public boolean isLaneClosed(String laneName) {
    	 GtsNode node = getEntryNode(laneName);
         if(node != null && !node.isLaneClosed()) return false;
         node = getExitNode(laneName);
         if(node != null && !node.isLaneClosed()) return false;
         
         return true;
    }
    
    public boolean hasGates(String laneName) {
    	return getEntryNode(laneName) != null || getExitNode(laneName) != null;
         
    }
    
    public boolean isEntryGateOpen(String laneName){
        GtsNode node = getEntryNode(laneName);
        return node == null || node.isGateOpen();
    }
    
    public boolean isUnknowCarrier(String carrier) {
    	if(StringUtils.isEmpty(carrier)) return false;
    	return GtsCarrier.UNKNOWN.equalsIgnoreCase(carrier) ||
    	   getServerPropertyBean().getUnknownCarrier().equalsIgnoreCase(carrier);
    }
    
    public boolean isEmptyCarrier(String carrier) {
    	if(StringUtils.isEmpty(carrier)) return false;
    	String emptyCarrier = getServerPropertyBean().getEmptyCarrier();
    	if(StringUtils.isEmpty(emptyCarrier)) return false;
    	return emptyCarrier.equalsIgnoreCase(carrier);
    }
    
    public GtsLaneSegment getLaneSegmentByName(String name) {
    	for(GtsLaneSegment segment: getLaneSegments()) {
    		if(StringUtils.endsWithIgnoreCase(name, segment.getLaneSegmentName())) return segment;
    	}
    	return null;
    }
    
    
    public boolean isExitGateOpen(String laneName){
        GtsNode node = getExitNode(laneName);
        return node != null && node.isGateOpen();
    }
    
    public GtsLane findLane(String laneName){
        for(GtsLane lane:lanes){
            if(lane.getLaneId().equals(laneName)) return lane;
        }
        return null;
    }
    
    public void updateLaneCarriers(List<GtsProduct> products) {
    	
    	for(GtsProduct product : products) {
    		 for(GtsLane lane:lanes){
    	            for(GtsLaneCarrier laneCarrier:lane.getLaneCarriers()){
    	            	if(product.getId().getProductId().equalsIgnoreCase(laneCarrier.getProductId())){
    	            			laneCarrier.setProductSeq(product.getProductSeq());
    	            	        laneCarrier.setProduct(product);
    	            	}        
    	            }
    		 }
    	}
    	
    }
    
    public GtsCarrier findCarrier(String carrierId) {
    	if(StringUtils.isEmpty(carrierId)) return null;
    	int carrierNumber = NumberUtils.toInt(carrierId,-1);
    	if(carrierNumber > 0) return getDao(GtsCarrierDao.class).findByCarrierId(getCarrierArea(), carrierNumber);
    	return getDao(GtsCarrierDao.class).findByKey(new GtsCarrierId(getCarrierArea(),carrierId));
    }
    
    public GtsLaneCarrier hasDuplicatedLaneCarriers(String carrierId) {
    	if(isEmptyCarrier(carrierId)) return null;
    	for(GtsLaneCarrier laneCarrier :findAllLaneCarriers()) {
    		if(isSameCarrierId(carrierId,laneCarrier.getCarrierId())) 
    			return laneCarrier;
    	}
    	return null;
    }
    
    private boolean isSameCarrierId(String carrierId1, String carrierId2) {
    	if(StringUtils.equals(carrierId1, carrierId2)) return true;
    	if(!StringUtils.isNumeric(carrierId1) || !StringUtils.isNumeric(carrierId2)) return false;
    	return Integer.parseInt(carrierId1) == Integer.parseInt(carrierId2);
    }
    
    public GtsLane updateLane(GtsLane lane) {
    	return getDao(GtsLaneDao.class).update(lane);
    }
    
    public long getCurrentTime() {
    	Date date = getDao(GtsAreaDao.class).getDatabaseTimeStamp();
    	return date.getTime();
    }
    
    public List<String> findDuplicatedCarriers(){
	    List<String> carriers = new ArrayList<String>();
        List<String> duplicates = new ArrayList<String>();
        for(GtsLane lane:lanes){
            for(GtsLaneCarrier laneCarrier:lane.getLaneCarriers()){
            	if(isUnknowCarrier(laneCarrier.getCarrierId()) ||
            	   isEmptyCarrier(laneCarrier.getCarrierId())) 
            			 continue;
                if(carriers.contains(laneCarrier.getLaneCarrier())){
                    duplicates.add(laneCarrier.getLaneCarrier());
                }else {
                    carriers.add(laneCarrier.getLaneCarrier());
                }
            }
        }
        return duplicates;
    }
    
    public List<GtsLaneCarrier> findAllLaneCarriers() {
    	 List<GtsLaneCarrier> carriers = new ArrayList<GtsLaneCarrier>();
         for(GtsLane lane:lanes){
             for(GtsLaneCarrier laneCarrier:lane.getLaneCarriers()){
            	 carriers.add(laneCarrier);
             }
         }
         return carriers;
    }
    
    public List<String> findAllProductionLots() {
    	Set<String> lots = new HashSet<String>(); 
    	for(GtsLaneCarrier laneCarrier : findAllLaneCarriers()) {
    		if(laneCarrier.getProduct() != null)
    			lots.add(laneCarrier.getShortProductionLot());
    	}
    	return new SortedArrayList<String>(lots);
    }
    
    public List<String> findAllProductIds() {
    	Set<String> productIds = new HashSet<String>(); 
    	for(GtsLaneCarrier laneCarrier : findAllLaneCarriers()) {
    		if(laneCarrier.getProduct() != null)
    			productIds.add(laneCarrier.getProductId());
    	}
    	return new SortedArrayList<String>(productIds);
    }
    public List<String> findAllCarriers() {
    	Set<String> carriers = new HashSet<String>(); 
    	for(GtsLaneCarrier laneCarrier : findAllLaneCarriers()) {
    			carriers.add(laneCarrier.getCarrierId());
    	}
    	return new SortedArrayList<String>(carriers);
    }
    
    public String findIndicatorNameFromLabel(String label) {
    	 
    	Map<String, String> toggleLabelMap = getServerPropertyBean().getToggleLabelMap();
      	if(toggleLabelMap == null) return null;
      	
      	for(Entry<String,String> entry : toggleLabelMap.entrySet()) {
      		if(entry.getValue().equals(label)) return entry.getKey();
      	}
      	
      	return null;

    }
    
    public GtsIndicator findIndicator(String indicatorName) {
    	for(GtsIndicator indicator: getIndicators()) {
    		if(indicator.getIndicatorName().equals(indicatorName)) return indicator;
    	}
    	return null;
    }

    public List<String> findAllYmtos() {
    	Set<String> ymtos = new HashSet<String>(); 
    	for(GtsLaneCarrier laneCarrier : findAllLaneCarriers()) {
    		if(laneCarrier.getProduct() != null) {
    			ymtos.add(laneCarrier.getProductSpec());
    		}
    	}
    	return new SortedArrayList<String>(ymtos);
    }
    
    public List<String> findAllColorCodes() {
    	Set<String> colorCodes = new HashSet<String>(); 
    	for(GtsLaneCarrier laneCarrier : findAllLaneCarriers()) {
    		if(laneCarrier.getProduct() != null) {
    			String colorCode = ProductSpec.padExtColorCode(laneCarrier.getProduct().getExtColorCode()) 
    					+laneCarrier.getProduct().getIntColorCode(); 
    			colorCodes.add(colorCode);
    		}
    	}
    	return new SortedArrayList<String>(colorCodes);
    }
    
    public void labelAdded(GtsLabel label) {
    	GtsLabel aLabel = findLabel(label.getId().getLabelId());
        if(aLabel != null)labels.remove(aLabel);
        labels.add(label);
    }
    
    public void shapeAdded(GtsShape shape){
    	GtsShape aShape = findShape(shape.getId().getShapeId());
        if(aShape != null) shapes.remove(shape);
        shapes.add(shape);
    }
    
    public void laneSegmentAdded(GtsLaneSegment segment){
        GtsLaneSegment aSegment = findLaneSegment(segment.getId().getLaneSegmentId());
        if(aSegment != null) laneSegments.remove(aSegment);
        this.addNode(segment.getInputNode());
        this.addNode(segment.getOutputNode());
        laneSegments.add(segment);
    }
    
    public GtsLaneSegment laneSegmentRemoved(int segmentId){
        GtsLaneSegment segment = findLaneSegment(segmentId);
        if(segment == null) return null;
        this.laneSegmentRemoved(segment);
        return segment;
    }
    
    /**
     * remove a lane segment
     * @param segment
     */
    
    public void laneSegmentRemoved(GtsLaneSegment segment){
        
        if(segment == null ) return;
        
        this.laneSegments.remove(segment);
        segment.getInputNode().removeLaneSegment(segment);
        segment.getOutputNode().removeLaneSegment(segment);
        if(segment.getInputNode().getInputLaneSegments().size() == 0){
            this.removeNode(segment.getInputNode());
        }
        
        if(segment.getOutputNode().getInputLaneSegments().size() == 0){
            this.removeNode(segment.getOutputNode());
        }
        
        for(GtsLaneSegmentMap laneSegmentMap : segment.getLaneSegmentMaps()) {
        	laneSegmentMap.getLane().removeLaneSegmentMap(laneSegmentMap);
        }
    }
    
    public void removeNode(GtsNode node){
        this.nodes.remove(node);
    }
    
    public GtsLabel findLabel(int id) {
    	 for(GtsLabel label:labels){
             if(label.getId().getLabelId()== id) return label;
         }
         return null;
    }
 
    public List<GtsLabel> findLabels(String labelText) {
    	List<GtsLabel> items = new ArrayList<GtsLabel>();
   	 	
    	for(GtsLabel label:labels){
            if(label.getLabelText().equals(labelText)) items.add(label);
        }
        return items;
    }

    public GtsShape findShape(int id){
        for(GtsShape shape:shapes){
            if(shape.getId().getShapeId()== id) return shape;
        }
        return null;
    }
    
    public GtsNode findNode(int nodeId){
        for(GtsNode node:nodes){
            if(node.getId().getNodeId()== nodeId) return node;
        }
        return null;
    }
    
    public GtsLaneSegment replaceNode(int fromId, int toId) {
    	GtsNode fromNode = this.findNode(fromId);
    	GtsNode toNode = this.findNode(toId);
    	return replaceLaneSegmentNode(fromNode,toNode);
    }
    
    public GtsLaneSegment replaceLaneSegmentNode(GtsNode fromNode,GtsNode toNode){
        if(toNode == null) return null;
        GtsLaneSegment selectedLaneSegment = null;
        for(GtsLaneSegment segment:laneSegments){
            if(segment.getInputNodeId()==fromNode.getNodeId()){
                segment.setInputNode(toNode);
                segment.setInputNodeId(toNode.getNodeId());
                selectedLaneSegment = segment;
            }
            else if(segment.getOutputNodeId()==fromNode.getNodeId()){
                segment.setOutputNode(toNode);
                segment.setOutputNodeId(toNode.getNodeId());
                selectedLaneSegment = segment;
            }
        }
        this.removeNode(fromNode);
        return selectedLaneSegment;
        
    }
    
    public GtsLaneSegment findLaneSegment(int id){
        for(GtsLaneSegment laneSegment:laneSegments){
            if(laneSegment.getId().getLaneSegmentId()== id) return laneSegment;
        }
        return null;
    }
    
    public GtsLabel createLabel(int x, int y) {
    	GtsLabelId id = new GtsLabelId();
    	id.setTrackingArea(trackingArea);
    	id.setLabelId(-1);
    	GtsLabel label = new GtsLabel();
    	label.setId(id);
    	label.setX(x);
    	label.setY(y);
    	return getDao(GtsLabelDao.class).insert(label);
    }
    
    public GtsLabel createLabel(GtsLabel label) {
    	return getDao(GtsLabelDao.class).insert(label);
    }
    
    public GtsShape createShape(int x, int y) {
    	GtsShapeId id = new GtsShapeId();
    	id.setTrackingArea(trackingArea);
    	id.setShapeId(-1);
    	GtsShape shape = new GtsShape();
    	shape.setId(id);
    	shape.setX(x);
    	shape.setY(y);
    	return getDao(GtsShapeDao.class).insert(shape);
    }
    
    public GtsNode createNode(GtsNode node) {
    	GtsNode newNode =  getDao(GtsNodeDao.class).insert(node);
    	nodes.add(newNode);
    	return newNode;
    }
    
    public GtsShape createShape(GtsShape shape) {
    	return getDao(GtsShapeDao.class).insert(shape);
    }
    
    public GtsOutlineImage createOutlineImage(String description, byte[] bytes) {
    	GtsOutlineImage outlineImage = new GtsOutlineImage();
    	outlineImage.setImage(bytes);
    	outlineImage.setOutlineImageDescription(description);
    	return getDao(GtsOutlineImageDao.class).insert(outlineImage);
    }
    
    public GtsOutlineMap createOutlineMap(String modelCode, byte[] bytes) {
    	GtsOutlineMap outlineMap = new GtsOutlineMap(trackingArea,0);
    	outlineMap.setImageBytes(bytes);
    	outlineMap.setModelCode(modelCode);
    	return getDao(GtsOutlineMapDao.class).insert(outlineMap);
    }
    
    public GtsLaneSegment createLaneSegment(GtsLaneSegment laneSegment) {
    	laneSegment.getId().setTrackingArea(trackingArea);
        return getDao(GtsLaneSegmentDao.class).createLaneSegment(laneSegment);
    }
    
    public void createLaneSegmentMap(GtsLane lane,GtsLaneSegment laneSegment) {
    	
    	GtsLaneSegmentMap laneSegmentMap = new GtsLaneSegmentMap(trackingArea,lane.getLaneId(),lane.getMaxLaneMapSeq() + 1);
    	laneSegmentMap.setLaneSegmentId(laneSegment.getId().getLaneSegmentId());
    	laneSegmentMap.setLaneSegment(laneSegment);
    	lane.getLaneSegmentMaps().add(laneSegmentMap);
        laneSegmentMap.setLaneSegment(laneSegment);
        laneSegment.addLaneSegmentMap(laneSegmentMap);
        laneSegmentMap.setLane(lane);
        lane.setLaneCapacity(lane.getLaneCapacity() + laneSegment.getCapacity());
       
        getDao(GtsLaneDao.class).update(lane);
        
        getDao(GtsLaneSegmentMapDao.class).insert(laneSegmentMap);
    }
    
    public GtsMove createMove(String sourceLaneId,String destinationLaneId,int decisionPointId) {
    	GtsMove move = new GtsMove(trackingArea,sourceLaneId,destinationLaneId);
    	move.setDecisionPointId(decisionPointId);
    	return getDao(GtsMoveDao.class).insert(move);
    }
    
    public GtsMoveCondition createMoveCondition(String sourceLaneId,String destinationLaneId,String indicatorId) {
    	GtsMoveCondition moveCondition = new GtsMoveCondition(trackingArea,sourceLaneId,destinationLaneId,indicatorId);
    	return getDao(GtsMoveConditionDao.class).insert(moveCondition);
    }
    
    public void addCarrierByUser(String laneName,int position,String carrier) {
    	getBodyTrackingService().addCarrierByUser(laneName, position, carrier);
    }
    
    public void refreshPLCIndicators() {
    	try{
    		getBodyTrackingService().refreshPLCIndictors();
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
    public void createLane(GtsLane lane) {
    	lane.getId().setTrackingArea(trackingArea);
    	getDao(GtsLaneDao.class).insert(lane);
    	this.lanes.add(lane);
    }
    
    public GtsColorMap createColorMap(String colorCode) {
    	// color id will be created in daoimpl
    	GtsColorMap colorMap = new GtsColorMap(trackingArea);
    	colorMap.setColorCode(colorCode);
    	return getDao(GtsColorMapDao.class).insert(colorMap);
    }
    
    public GtsDecisionPoint createDecisionPoint() {
    	GtsDecisionPoint decisionPoint = new GtsDecisionPoint();
    	GtsDecisionPointId id = new GtsDecisionPointId();
    	id.setTrackingArea(trackingArea);
    	decisionPoint.setId(id);
    	return getDao(GtsDecisionPointDao.class).insert(decisionPoint);
    }
    
    public GtsDecisionPointCondition createDecisionPointCondition(int decisionPointId,String indicatorId) {
    	GtsDecisionPointCondition dpCondition = new GtsDecisionPointCondition(trackingArea,decisionPointId,indicatorId);
    	return getDao(GtsDecisionPointConditionDao.class).insert(dpCondition);
    }
    
    public GtsLabel updateLabel(GtsLabel label) {
    	return getDao(GtsLabelDao.class).update(label);
    }
    
    public void removeLabel(GtsLabel label) {
    	getDao(GtsLabelDao.class).remove(label);
    }
    
    public GtsShape updateShape(GtsShape shape) {
    	return getDao(GtsShapeDao.class).update(shape);
    }
    
    public void removeShape(GtsShape shape) {
    	getDao(GtsShapeDao.class).remove(shape);
    }
    
    public void removeLane(GtsLane lane) {
    	getDao(GtsLaneDao.class).remove(lane);
    	this.getLanes().remove(lane);
    }
    
    public void removeColorMap(GtsColorMap colorMap) {
    	getDao(GtsColorMapDao.class).remove(colorMap);
    }
    
    public void removeOutlineMap(GtsOutlineMap outlineMap) {
    	getDao(GtsOutlineMapDao.class).remove(outlineMap);
    }
    
    public void removeOutlineImage(GtsOutlineImage outlineImage) {
    	getDao(GtsOutlineImageDao.class).remove(outlineImage);
    }
    
    public void removeLaneSegment(GtsLaneSegment laneSegment) {
    	getDao(GtsLaneSegmentDao.class).remove(laneSegment);
    }
    
    public void removeLaneSegmentMap(GtsLane lane,GtsLaneSegment laneSegment) {
    	GtsLaneSegmentMap laneSegmentMap = laneSegment.getLaneSegmentMap(lane.getLaneId());
    	getDao(GtsLaneSegmentMapDao.class).remove(laneSegmentMap);
    	
    	laneSegment.removeLaneSegmentMap(laneSegmentMap);
        lane.getLaneSegmentMaps().remove(laneSegmentMap);
        lane.setLaneCapacity(lane.getLaneCapacity() - laneSegment.getCapacity());
        
        getDao(GtsLaneDao.class).update(lane);
    	
        List<GtsLaneSegmentMap> laneSegmentMaps = new ArrayList<GtsLaneSegmentMap>();
    	int laneSeq = 1;
        for(GtsLaneSegmentMap segmentMap :lane.getLaneSegmentMaps()) {
    	   segmentMap.getId().setLaneSeq(laneSeq++);
    	}
    	
    	if(!laneSegmentMaps.isEmpty()) {
    		getDao(GtsLaneSegmentMapDao.class).updateAll(laneSegmentMaps);
    	}
    	
    	
    	
    }
    
    public void swapLaneSegmentMap(GtsLaneSegmentMap laneSegmentMap1, GtsLaneSegmentMap laneSegmentMap2) {
    	int seq = laneSegmentMap1.getId().getLaneSeq();
    	laneSegmentMap1.getId().setLaneSeq((short)laneSegmentMap2.getId().getLaneSeq());
    	laneSegmentMap2.getId().setLaneSeq((short)seq);
    	List<GtsLaneSegmentMap> laneSegmantMaps = new ArrayList<GtsLaneSegmentMap>();
    	laneSegmantMaps.add(laneSegmentMap1);
    	laneSegmantMaps.add(laneSegmentMap2);
    	getDao(GtsLaneSegmentMapDao.class).updateAll(laneSegmantMaps);
    }
    
    public void removeDecisionPoint(GtsDecisionPoint decisionPoint) {
    	getDao(GtsDecisionPointDao.class).remove(decisionPoint);
    }
    
    public void removeDecisionPointCondition(GtsDecisionPointCondition dpCondition) {
    	getDao(GtsDecisionPointConditionDao.class).remove(dpCondition);
    }
    
    public void removeMove(GtsMove move) {
    	getDao(GtsMoveDao.class).remove(move);
    }
    
    public void removeMoveCondition(GtsMoveCondition moveCondition) {
    	getDao(GtsMoveConditionDao.class).remove(moveCondition);
    }
    
    public GtsLaneSegment updateLaneSegment(GtsLaneSegment laneSegment) {
    	GtsLaneSegment segment = getDao(GtsLaneSegmentDao.class).update(laneSegment);
    	return segment;
    }
    
    public void reverseLaneSegment(GtsLane lane) {
    	List<GtsLaneSegment> laneSegments = lane.reverseDirection();
    	getDao(GtsLaneSegmentDao.class).updateAll(laneSegments);
    }
    
    public void updateLaneSegments(List<GtsLaneSegment> laneSegments) {
    	getDao(GtsLaneSegmentDao.class).updateAll(laneSegments);
    }
    
    
    public GtsLaneSegment updateLaneSegmentNode(GtsLaneSegment laneSegment, int fromId, int toId) {
    	return getDao(GtsLaneSegmentDao.class).replaceNode(laneSegment, fromId, toId);
    }
    
    public void updateNodes(List<GtsNode> nodes) {
    	getDao(GtsNodeDao.class).updateAll(nodes);
    }
    
    public void updateNode(GtsNode node) {
    	List<GtsNode> nodes = new ArrayList<GtsNode>();
    	nodes.add(new GtsNode(node));
    	updateNodes(nodes);
    }
    
    public void updateInspectStatus(String productId, int inspectionStatus) {
    	getBodyTrackingService().updateProductInspectionStatus(productId, inspectionStatus);
    }
    
    public void updateCarrierType(String carrierId, int carrierType) {
    	getBodyTrackingService().updateCarrierType(carrierId, carrierType);
    }
    
    public void refreshProductDefectStatus(String productId) {
    	getBodyTrackingService().refreshProductDefectStatus(productId);
    }
    
    public void toggleGateStatus(GtsNode node, int status) {
    	if(node == null) return;
    	final GtsNode newNode = new GtsNode(node);
    	newNode.setStatus(status);
    	
    	Thread t = new Thread() {
			public void run() {
				getBodyTrackingService().toggleGateStatus(newNode);
			}
		};

		t.start();
    	
    }
    
//    public void addLaneCarriers(GtsLane lane,List<GtsLaneCarrier> carriers){
//        if(lane == null) return;
//        for(GtsLaneCarrier laneCarrier:carriers){
//            
//            GtsProduct product = laneCarrier.getCarrier().getProduct();
//            if(product == null && laneCarrier.getCarrier().getProductId() != null){
//                product = reqInvoker.getProduct(laneCarrier.getCarrier().getProductId());
//                if(product != null){
//                    area.addProduct(product);
//                }
//            }
//            laneCarrier.setLane(lane);
//            if(product != null)laneCarrier.getCarrier().setProduct(product);
//        }
//        lane.updateCarriers(carriers);
//    }
    
    public void initializeDummyCarriers(){
        for(GtsLane lane:lanes){
            lane.initializeDummyCarriers();
        }
    }
    
    public void initializeLaneCarriers() {
    	for(GtsLane lane:lanes){
    		reloadLaneCarriers(lane);
    	}    
    }
    
    public void reloadLaneCarriers(GtsLane lane) {
    	List<GtsLaneCarrier> laneCarriers = getDao(GtsLaneCarrierDao.class).findAll(lane.getId().getTrackingArea(), lane.getId().getLaneId(),getCarrierArea());
		for(GtsLaneCarrier laneCarrier : laneCarriers) {
			laneCarrier.setLane(lane);
		}
		lane.setLaneCarriers(laneCarriers);
    }
    
    public List<GtsIndicator> fetchAllIndicators() {
    	return getDao(GtsIndicatorDao.class).findAll(trackingArea);
    }
    
    public List<GtsColorMap> fetchAllColorMaps() {
    	return getDao(GtsColorMapDao.class).findAll(trackingArea);
    }
    
    public List<GtsOutlineMap> fetchAllOutlineMaps() {
    	return getDao(GtsOutlineMapDao.class).findAll(trackingArea);
    }
    
    public List<GtsOutlineImage> fetchAllOutlineImages() {
    	return getDao(GtsOutlineImageDao.class).findAll();
    }
    
    public List<GtsMove> fetchAllMoves() {
    	return getDao(GtsMoveDao.class).findAll(trackingArea);
    }
    
    public List<GtsDecisionPoint> fetchAllDecisionPoints() {
    	return getDao(GtsDecisionPointDao.class).findAll(trackingArea);
    }
    
    public List<GtsDecisionPointCondition> fetchAllDecisionPointConditions() {
    	return getDao(GtsDecisionPointConditionDao.class).findAll(trackingArea);
    }
    
    public List<GtsMoveCondition> fetchAllMoveConditions() {
    	return getDao(GtsMoveConditionDao.class).findAll(trackingArea);
    }
    
    public List<GtsZone> fetchAllZones() {
    	return getDao(GtsZoneDao.class).findAll(trackingArea);
    }
    
    public void updateOutlineMap(GtsOutlineMap outlineMap) {
    	getDao(GtsOutlineMapDao.class).update(outlineMap);
    }
    
    public void updateColorMap(GtsColorMap colorMap) {
    	getDao(GtsColorMapDao.class).update(colorMap);
    }
    
    public void updateOutlineImage(GtsOutlineImage outlineImage) {
    	getDao(GtsOutlineImageDao.class).update(outlineImage);
    }
    
    public void updateDecisionPointCondition(GtsDecisionPointCondition dpCondition) {
    	getDao(GtsDecisionPointConditionDao.class).update(dpCondition);
    }
    
    public void updateDecisionPoint(GtsDecisionPoint decisionPoint) {
    	getDao(GtsDecisionPointDao.class).update(decisionPoint);
    }
    
    public void updateMoveCondition(GtsMoveCondition moveCondition) {
    	getDao(GtsMoveConditionDao.class).update(moveCondition);
    }
    
    
    public Message correctCarrierByUser(GtsLaneCarrier laneCarrier, String newLabel){
    	GtsLaneCarrier copy = laneCarrier.copy();
    	copy.setLane(null);
    	return getBodyTrackingService().correctCarrierByUser(copy, newLabel);
    };
    
    public Message checkMovePossible(GtsMove move) {
    	return getBodyTrackingService().checkMovePossible(move);
    }
    
    public Message createMoveRequest(GtsMove move) {
    	return getBodyTrackingService().createMoveRequest(move);
    }
    
    public Message changeAssociation(String carrierId,String productId) {
    	return getBodyTrackingService().changeAssociation(carrierId, productId);
    }
    
    public Message removeCarrierByUser(GtsLaneCarrier laneCarrier, int position){
    	GtsLaneCarrier copy = new GtsLaneCarrier(laneCarrier);
    	copy.setLane(null);
    	
    	return getBodyTrackingService().removeCarrierByUser(copy, position);
    }
    
    public GtsClientList subscribe() {
    	GtsClientList client = new GtsClientList();
    	GtsClientListId id = new GtsClientListId();
    	id.setTrackingArea(trackingArea);
    	id.setClientIp(applicationContext.getLocalHostIp());
    	id.setClientPort(applicationContext.getTerminal().getPort());
    	client.setId(id);
    	client.setClientName(applicationContext.getHostName());
    	return getDao(GtsClientListDao.class).save(client);
    }
    
    public void unsubscribe(){
    	GtsClientListId id = new GtsClientListId();
    	id.setTrackingArea(trackingArea);
    	id.setClientIp(applicationContext.getLocalHostIp());
    	id.setClientPort(applicationContext.getTerminal().getPort());
    	getDao(GtsClientListDao.class).removeByKey(id);
    }
    
	public void loadAll() {
		trackingArea = getServerPropertyBean().getTrackingArea();
		area = getDao(GtsAreaDao.class).findByKey(trackingArea);
		indicators = getDao(GtsIndicatorDao.class).findAll(trackingArea);
		
		loadLaneSegmentsAndGates();
		
		labels = getDao(GtsLabelDao.class).findAll(trackingArea);
		shapes = getDao(GtsShapeDao.class).findAll(trackingArea);
		moves = getDao(GtsMoveDao.class).findAll(trackingArea);
        ImageFactory.getInstance().initilizeColorMap(fetchAllColorMaps());
        ImageFactory.getInstance().initilizeOutlineMap(fetchAllOutlineMaps());
	}
	
	public String getCarrierArea() {
		String areaName = getServerPropertyBean().getCarrierArea();
		return StringUtils.isEmpty(areaName) ? getServerPropertyBean().getTrackingArea() : areaName;
	}
	
	public void loadLaneSegmentsAndGates() {
		nodes = getDao(GtsNodeDao.class).findAll(trackingArea);
		laneSegments = getDao(GtsLaneSegmentDao.class).findAll(trackingArea);
		lanes = getDao(GtsLaneDao.class).findAll(trackingArea);
		
		setLaneSegmentNodes();
		setLaneSegmentMaps();
	}
	
	private void setLaneSegmentNodes() {
		GtsNode node;
		for(GtsLaneSegment laneSegment : laneSegments) {
			node = findNode(laneSegment.getInputNodeId());
			if(node != null) {
				laneSegment.setInputNode(node);
				node.addOutputLaneSegment(laneSegment);
			}
			node = findNode(laneSegment.getOutputNodeId());
			if(node != null) {
				laneSegment.setOutputNode(node);
				node.addInputLaneSegment(laneSegment);
			}
		}
	}
	
	private void setLaneSegmentMaps() {
		List<GtsLaneSegmentMap> laneSegmentMaps= getDao(GtsLaneSegmentMapDao.class).findAll(trackingArea);
		for(GtsLaneSegmentMap laneSegmentMap : laneSegmentMaps) {
			GtsLane lane = this.findLane(laneSegmentMap.getId().getLaneId());
			GtsLaneSegment laneSegment = findLaneSegment(laneSegmentMap.getLaneSegmentId());
			
			laneSegmentMap.setLaneSegment(laneSegment);
			laneSegment.addLaneSegmentMap(laneSegmentMap);
			if(lane != null) lane.addLaneSegmentMap(laneSegmentMap);
			laneSegmentMap.setLane(lane);
		}
	}
	
	public GtsClientPropertyBean getPropertyBean() {
		return PropertyService.getPropertyBean(GtsClientPropertyBean.class,applicationContext.getHostName());
	}
	
	public GtsDefaultPropertyBean getServerPropertyBean() {
		return PropertyService.getPropertyBean(GtsClientPropertyBean.class,applicationContext.getProcessPointId());
	}
	
	public boolean isUserControllable() {
		return ClientMain.getInstance().getAccessControlManager().isAuthorized(getPropertyBean().getTrackingControlSecurityGroupName());
	}
	
	private DailyDepartmentSchedule findSchedule(String processPointId) {
		ProcessPoint processPoint = getDao(ProcessPointDao.class).findById(processPointId);
		if(processPoint == null) return null;
		return getDao(DailyDepartmentScheduleDao.class).find(processPoint.getDivisionId(), new Timestamp(System.currentTimeMillis()));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Product> findAllProducts(String processPointId) {
		List<Product> products = new ArrayList<Product>();
		String productType = getPropertyBean().getProductType();
		ProductDao productDao = null;
		if(ProductType.FRAME.getProductName().equalsIgnoreCase(productType)) {
			productDao = getDao(FrameDao.class);
		}else if(ProductType.ENGINE.getProductName().equalsIgnoreCase(productType)) {
			productDao = getDao(EngineDao.class);
		}else return products; 
		
		List<? extends Product> items =  productDao.findAllByProcessPoint(processPointId);
		return new ArrayList<Product>(items);
	}
	
	public List<KeyValue<String,String>> findAllOutstandingDefects(String productId) {
		
		List<KeyValue<String,String>> items = new ArrayList<KeyValue<String,String>>();
		
		if(getServerPropertyBean().isCheckNAQDefect()) {
			List<QiDefectResult> results = getDao(QiDefectResultDao.class).findAllByProductId(productId, new ArrayList<Short>());
			for(QiDefectResult result : results) {
				if(result.getCurrentDefectStatus()== DefectStatus.OUTSTANDING.getId() ||
				   result.getCurrentDefectStatus()== DefectStatus.NOT_REPAIRED.getId() ||
				   result.getCurrentDefectStatus()== DefectStatus.NOT_FIXED.getId() ) {
					items.add(new KeyValue<String,String>(result.getInspectionPartName(),result.getDefectTypeName()));
				}
			}
		}else {
			List<DefectResult> defectResults = getDao(DefectResultDao.class).findAllByProductId(productId);
			for(DefectResult result : defectResults) {
				if(result.getDefectStatus()== DefectStatus.OUTSTANDING) {
					items.add(new KeyValue<String,String>(result.getInspectionPartName(),result.getDefectTypeName()));
				}
			}
		}
		
		return items;
	}
	
	
	public Long findCurrentCount(String processPointId) {
		DailyDepartmentSchedule schedule = findSchedule(processPointId);
		if(schedule == null) return -1L;
		return getDao(CounterDao.class).findCount(schedule.getId().getProductionDate(), processPointId);
	}
    
}
