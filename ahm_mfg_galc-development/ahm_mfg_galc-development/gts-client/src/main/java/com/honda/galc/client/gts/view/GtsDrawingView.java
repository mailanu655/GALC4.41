package com.honda.galc.client.gts.view;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JOptionPane;
import javax.swing.JViewport;
import javax.swing.Timer;

import org.jhotdraw.draw.DefaultDrawingView;

import com.honda.galc.client.gts.figure.GateFigure;
import com.honda.galc.entity.gts.GtsNode;



/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>GtsDrawingView</code> handles mouse and key events from the user.
 * It subclasses the DefaultDrawingView and tracks the figures
 * change so that when move released, the change will be passed
 * to the server side and saved to the database and published to
 * the other GUI client.<br>
 * It also allows the user to drag the area layout or fit all the
 * figures into the current view
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
 * <TD>Jeffray Huang</TD>
 * <TD>Feb 26, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */

public class GtsDrawingView extends DefaultDrawingView implements ActionListener{
           
    private static final long serialVersionUID = 1L;
    
    private final String ServerConnectionTag = "Server Connection";
    
    private GtsTrackingController controller;
    
 //   private List<ComStatus> comStatusList = new ArrayList<ComStatus>();
    
    // if all Communication Status ok
    private boolean allComStatus = true;
    
    // timer for checking all of the communication status
    private Timer timer= new Timer(1000,this);
    
    private GtsStatusPanel statusPanel;
    
    
    
    public GtsDrawingView(){
        super();
    }
    
    public GtsDrawingView(GtsTrackingController controller){
        super();
        this.controller = controller;
        
        
    }
    
//    public void initComStatusList(){
//        
//        if(GtsDrawing.isProductionMode()){
//            
//            
//            comStatusList.add(new ComStatus(ServerConnectionTag,PropertyList.getInstance().getClientHeartbeat() + 5000));
//            
//            
//            List<Property> props = PropertyList.getInstance().getHeartbeatProperties();
//            
//            for(Property property : props){
//                
//                comStatusList.add(new ComStatus(property.getPropertyId(),property.getIntegerValue()));
//            }
//            
//            updateComStatusPanel();
//            
//            // start the timer to check all the communications
//            timer.start();
//        }
//    }
    
    public GtsTrackingController getController(){
        return controller;
    }
    
    public GtsDrawing getDrawing(){
        return (GtsDrawing)super.getDrawing();
    }
    
    
    public void setStatusPanel(GtsStatusPanel statusPanel){
        
        this.statusPanel = statusPanel;
        
 //       initComStatusList();
        
    }
    
    public void delete() {
       // override the default delete. not allow delete if the user clicks "DEL" button
    }
        
    /**
     * display the message published from the server
     * @param message
     */
    
//    public void messagePublished(Message message){
//        if(statusPanel != null) statusPanel.addMessage(message);
//        new MessageBox(message);
//    }
//    
    
    /**
     * process the message of gate status changed
     * GateFigure with id "nodeId" will be changed based on "status"
     * Notification from GALC server
     * @param nodeId
     * @param status
     */
    
    public void gateStatusChanged(int nodeId,int status){
        GateFigure figure =getDrawing().findGateFigure(nodeId);
        if(figure != null) figure.setGateStatus(status);
    }
    
    public void gateUpdated(GtsNode node){
        getDrawing().updateGateFigure(node);
    }
    
//    /**
//     * process the lane carrier changed message
//     * @param laneName - lane name
//     * @param carrierNumbers - carrier list
//     */
//    
//    public void laneCarrierChanged(String laneName,List<GtsLaneCarrier> carriers){
//        
//        controller.addLaneCarriers(laneName, carriers);
//        getDrawing().updateLaneCarriers(getArea().findLane(laneName));
//        
//        // update the Move In progress
//        for(Indicator indicator :getArea().findIndicators(GtsIndicatorType.MP)){
//            if(indicator.getStatus() && indicator.getDestLaneName().equals(laneName)){
//                getDrawing().updateMoveStatus(new Move(indicator));
//                break;
//            }
//        }
//    }
//    
    
//    public void associationChanged(GtsCarrier carrier){
//        
//        for(LaneCarrier lc:getArea().findCarriers(carrier.getLabel())){
//            
//            String productId = carrier.getProductId();
//            
//            if(lc.getProduct() != null) getArea().removeProduct(lc.getProduct());
//            
//            lc.getCarrier().setProductId(carrier.getProductId());
//            
//            if(productId != null) {
//                
//                Product product = getArea().findProduct(productId);
//                if(product == null ){
//                    product = this.getRequestInvoker().getProduct(productId);
//                    if(product != null){
//                        getArea().addProduct(product);
//                    }
//                }
//                
//                lc.getCarrier().setProduct(product);
//                
//            }else lc.getCarrier().setProduct(null);
//            
//            this.getDrawing().updateLaneCarriers(lc.getLane());
//            
//        }
//        
//        // remove nonexist product
//        getArea().cleanupProducts();
//    }
    
//    private void labelCreated(Label label){
//        getArea().addLabel(label);
//        getDrawing().add(new LabelFigure(label));
//    }
//    
//    private void labelUpdated(Label label){
//        getArea().addLabel(label);
//        LabelFigure figure = getDrawing().findLabelFigure(label.getId());
//        if(figure != null)getDrawing().remove(figure);
//        getDrawing().add(getDrawing().getMaxIndexOfLabels(),new LabelFigure(label));
//    }
//    
//    private void labelRemoved(Label label){
//        LabelFigure figure = getDrawing().findLabelFigure(label.getId());
//        if(figure != null) getDrawing().removeLabel(figure);
//    }
//    
//    public void shapeCreated(Shape shape){
//        getArea().addShape(shape);
//        getDrawing().add(new ShapeFigure(shape));
//    }
//    
//    private void shapeUpdated(Shape shape){
//        getArea().addShape(shape);
//        ShapeFigure figure = getDrawing().findShapeFigure(shape.getId());
//        if(figure != null)getDrawing().remove(figure);
//        getDrawing().add(getDrawing().getMaxIndexOfShapes(),new ShapeFigure(shape));
//    }
//    
//    
//    private void shapeRemoved(Shape shape){
//        ShapeFigure figure = getDrawing().findShapeFigure(shape.getId());
//        if(figure != null) getDrawing().removeShape(figure);
//    }
//    
//    public void laneInfoUpdated(Lane lane){
// 
//        Lane aLane = getArea().findLane(lane.getLaneName());
//        aLane.setDescription(lane.getDescription());
//        aLane.setCapacity(lane.getCapacity());
//        aLane.setLaneType(lane.getLaneType());
// 
//    }
    
//    public void laneSegmentCreated(LaneSegment segment){
//  
//        getArea().addLaneSegment(segment);
//        segment.setInputNode(getNode(segment.getInputNodeId()));
//        segment.setOutputNode(getNode(segment.getOutputNodeId()));
//        getDrawing().add(new LaneSegmentFigure(segment));
// 
//    }
//    
//    public void laneSegmentRemoved(int segmentId){
//        
//        getArea().removeLaneSegment(segmentId);
//        
//        getDrawing().removeLaneSegmentFigure(segmentId);
//        
//        getDrawing().refreshLayout();
//    }
//    
//    public void laneSegmentsUpdated(List<LaneSegment> segments){
//        
//        for(LaneSegment segment:segments){
//            LaneSegmentFigure figure = getDrawing().findLaneSegmentFigure(segment.getId());
//            segment.setInputNode(getNode(segment.getInputNodeId()));
//            segment.setOutputNode(getNode(segment.getOutputNodeId()));
//            segment.setLane(figure.getLaneSegment().getLane());
//            if(segment.getPowerFlag() != figure.getLaneSegment().getPowerFlag()) 
//                figure.setPoweredFlag(segment.getPowerFlag());
//            figure.getLaneSegment().replace(segment);
//        }
//        
//        getDrawing().refreshLayout();
//        
//    }
//    
//    public void laneSegmentMapChanged(String laneId){
//        
//        Lane lane = getArea().findLane(laneId);
//        
//        if (lane == null) return;
//        List<LaneSegmentMap> maps = this.getRequestInvoker().getLaneSegmentMap(laneId);
//        
//        if(maps == null) return;
//        
//        lane.getLaneSegments().clear();
//        for(LaneSegmentMap map: maps) {
//            
//            LaneSegment segment = getArea().findLaneSegment(map.getLaneSegmentId());
//            
//            if(segment != null) lane.addLaneSegment(segment);
//            
//        }
//        
//        lane.calculateCapacity();
//        
//        
//        getDrawing().initCarriers();
//        
//        
//    }
//    
//    public void modelUpdated(IModel model) {
//        
//        if(model instanceof Lane) laneInfoUpdated((Lane)model);
//        else if(model instanceof Label) labelUpdated((Label)model);
//        else if(model instanceof Shape) shapeUpdated((Shape)model);
//        else if(model instanceof ColorMap) colorMapUpdated((ColorMap)model);
//        else if(model instanceof OutlineMap) outlineMapUpdated((OutlineMap)model);
//        
//    }
//    
//    public void modelRemoved(IModel model) {
//        
//        if(model instanceof Label) labelRemoved((Label)model);
//        else if(model instanceof Shape) shapeRemoved((Shape)model);
//        else if(model instanceof OutlineMap) outlineMapRemoved((OutlineMap)model);
//        else if(model instanceof ColorMap) colorMapRemoved((ColorMap)model);
//        
//        
//    }
//    
//    public void nodesUpdated(List<Node> nodes){
//        for(Node node:nodes){
//            getArea().addNode(node);
//        }
//        getDrawing().refreshLayout();
//    }
//    
//    public void indicatorChanged(Indicator indicator){
//        
//        getArea().updateIndicator(indicator);
//        
//        if(indicator.isMoveInProgress()){
//            
//            getDrawing().updateMoveStatus(new Move(indicator));
//            
//        }else {
//            
//            if(indicator.isHeartbeat()) {
//                
//                ComStatus comStatus = findComStatus(indicator.getName());
//                
//                if(comStatus != null) comStatus.heatbeatReceived();
//                
//            }
//        }
//    }
//    
//    public void heartbeatReceived() {
//        
//        System.out.println("heartbeat server received");
//    	ComStatus comStatus = findComStatus(ServerConnectionTag);
//        comStatus.heatbeatReceived();
//        
//    }
//    
//    private ComStatus findComStatus(String name){
//        
//        for(ComStatus comStatus : comStatusList){
//            if(comStatus.getName().equals(name)) return comStatus;
//        }
//        ComStatus comStatus = new ComStatus(ServerConnectionTag);
//        comStatusList.add(comStatus);
//        return comStatus;
//    }
//    
//    public void moveStatusChanged(Move move){
//        getDrawing().updateMoveStatus(move);
//    }
//    
//    public void nodeReplaced(int fromId,int toId){
//        
//        getArea().replaceNode(fromId,toId);
//        getDrawing().refreshLayout();
//    }
//    
//    private void colorMapRemoved(ColorMap colorMap){
//        ImageFactory.getInstance().removeColorMap(colorMap.getId());
//        getDrawing().refreshLayout();
//    }
//    
//    private void colorMapUpdated(ColorMap map){
//        ImageFactory.getInstance().updateColorMap(map);
//        getDrawing().refreshLayout();
//    }
//    
//    private void outlineMapRemoved(OutlineMap map){
//        ImageFactory.getInstance().removeOutlineMap(map);
//        getDrawing().refreshLayout();
//    }
//    
//    private void outlineMapUpdated(OutlineMap map){
//        ImageFactory.getInstance().updateOutlineMap(map);
//        getDrawing().refreshLayout();
//    }
//    
//    public void productStatusChanged(List<Product> products){
//        
//        for(Product product:products){
//            Product newProduct = getArea().findProduct(product.getProductId());
//            newProduct.setSequence(product.getSequence());
//            newProduct.setInspectionStatus(product.getInspectionStatus());
//            newProduct.setDefectStatus(product.getDefectStatus());
//            newProduct.setProductStatus(product.getProductStatus());
//        }
//        
//        getDrawing().refreshProductStatus();
//    }
    
//    private Node getNode(int nodeId){
//        Node aNode = getArea().findNode(nodeId);
//        if(aNode != null) return aNode;
//        Node node = getDrawing().getRequestInvoker().getNode(nodeId);
//        getArea().addNode(node);
//        return node;
//    }
//    
//    
    /**
     * Change the scale factor to fit all the figures in the current panel 
     *
     */
    
    public void fitAll(){
        
        Rectangle2D.Double rect = super.getDrawingArea();
        Double factor1 = (getParent().getWidth())/ rect.getWidth();
        Double factor2 = (getParent().getHeight() / rect.getHeight());
        this.setScaleFactor(Math.min(factor1, factor2));
    }
    
    public Rectangle2D.Double getDrawingArea() {
    	return super.getDrawingArea();
    }
    
    /**
     * drag the area layout on responding the 
     * @param dx
     * @param dy
     */
    
    public void dragArea(int dx,int dy){
        
        JViewport viewport;
        if (getParent() instanceof JViewport) {
            viewport = (JViewport) getParent();
        }else return;
        
        Point viewPos = viewport.getViewPosition();
        int newX = Math.min(Math.max(viewPos.x - dx, 0), getWidth() - viewport.getWidth());
        int newY = Math.min(Math.max(viewPos.y - dy, 0), getHeight() - viewport.getHeight());
        
        viewport.setViewPosition(new Point(newX, newY)); 
        
    }

    /**
     * timer - check comunication status
     * @param e
     */
    
    public void actionPerformed(ActionEvent e) {
        boolean aFlag = checkComStatusList();
        allComStatus = aFlag;
//        updateComStatusPanel();
//        ComStatus comStatus = findComStatus(ServerConnectionTag);
//        if(comStatus.isHeartbeatLost())shutdown();
    }
    
    private void shutdown() {
    	timer.stop();
    	this.setEnabled(false);
    	JOptionPane.showMessageDialog(this, 
    			"<HTML><H2>Client state is out of Sync!<br>" + "Client will be shut down!<br>"
    			+"Please restart!</H2></HTML>","Lost Connection to Server!",
    			JOptionPane.ERROR_MESSAGE);
    	System.exit(1);
    }
    
    private boolean checkComStatusList(){
        
        boolean aFlag = true;
        
//        for(ComStatus comStatus : comStatusList) {
//            aFlag &= !comStatus.isHeartbeatLost();
//        }
 
        return aFlag;
    }
    
    private void updateComStatusPanel(){
        
        if(this.statusPanel != null) {
   //         statusPanel.updateComStatus(allComStatus,comStatusList);
        }
    }
    
    

}
