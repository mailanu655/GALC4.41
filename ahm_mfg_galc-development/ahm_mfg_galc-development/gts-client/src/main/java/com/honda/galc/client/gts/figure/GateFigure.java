package com.honda.galc.client.gts.figure;

import java.util.Collection;
import java.util.LinkedList;

import org.jhotdraw.draw.*;

import com.honda.galc.entity.enumtype.GtsOrientation;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.action.NodeAction;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class GateFigure extends GtsAbstractFigure implements ActionListener{
    
    private static final long serialVersionUID = 1L;
    
	private EllipseFigure circle;
    private OpenCloseFigure openFigure;
    private OpenCloseFigure closeFigure;
    private GtsNode gate;
    
    private Timer timer;
    
  
  	
    public GateFigure(GtsNode aGate,int gateIconSize){
        this(aGate.getX(),aGate.getY(),aGate.isGateOpen(),aGate.getDirection(),gateIconSize);
        gate = aGate;
        this.addFigureListener(new GateFigureHandler(this));
    }
    
  	public GateFigure(int x, int y, boolean aFlag,GtsOrientation direction,int size){
      createFigure(x,y,aFlag,direction,size);
  }
  	
  	public void createFigure(){
       createFigure(gate.getX(),gate.getY(),gate.isGateOpen(),gate.getDirection(),
                       getModel().getPropertyBean().getGateIconSize()); 
    }
    
    public void createFigure(int x,int y,boolean aFlag, GtsOrientation direction, int size){
      circle = new EllipseFigure(x - (size /2.0),y - (size /2.0),size ,size); 
      openFigure = new OpenCloseFigure(x,y,true,direction,size);
      closeFigure = new OpenCloseFigure(x,y,false,direction,size);
      this.removeAllChildren();
      this.add(circle);
      this.add(getOpenCloseFigure(aFlag));
    }
    
    
    public GtsNode getNode(){
        return gate;
    }
    
    public void setNode(GtsNode gate) {
    	this.gate = gate;
    }
    
    public boolean isChanged() {
    	return gate.distance(getCenter()) > 0.0;
    }
    
    public boolean isChanged(GtsNode node) {
    	return isNodeId(node.getNodeId()) && node.distance(getCenter()) > 0.0;
    }
    
     	
  	/**
     * Handles a mouse double click.
     */
    
    public boolean handleMouseClick(Point2D.Double p, MouseEvent evt, DrawingView view) {
    	
        if(GtsDrawing.isProductionMode()) {
        	if(gate.isLaneClosed()) {
       	     JOptionPane.showMessageDialog(getView(), "Lane is closed. You are not allow to open/close gate",
                        "Warning Message", JOptionPane.WARNING_MESSAGE);
       	     return false;
        	}
        	toggleGate();
        	if(getModel().isUserControllable()){
        		getDrawing().getModel().toggleGateStatus(gate,gate.getStatus());
        	}
        }
    	
    	return false;
    }
    
    public void toggleGate(){
    	
    	
        if(getModel().isUserControllable()){
            
            // start timer to wait for the server to change gate status
            startTimer();
            
            this.remove(getOpenCloseFigure(gate.isGateOpen()));
            circle.setAttribute(AttributeKeys.FILL_COLOR,Color.yellow);
            invalidate();
          
        } else {
            JOptionPane.showMessageDialog(getView(), "You are not authorized to open/close gate. Please login as an authorized user!",
                            "Warning Message", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void startTimer(){
        
        stopTimer();

        timer = new Timer(getModel().getPropertyBean().getGateTimeoutDelay(),this);
        timer.setRepeats(false);
        timer.start();
        
    }
    
    public void stopTimer(){
        if(timer != null){
            timer.stop();
            timer = null;
        }
    }
    
    public boolean isNodeId(int nodeId){
        return (gate != null && gate.getId().getNodeId() == nodeId);
    }
    
    public void refreshChange(){

    	this.createFigure();
        
    }
    
    public Point2D.Double getCenter(){
        Rectangle2D.Double bounds = circle.getBounds();
        double x = bounds.x + bounds.width/2.0;
        double y = bounds.y + bounds.height/2.0;
         return new Point2D.Double(x,y);
    }
    
    public GtsNode updateNode() {
    	 this.gate.setX((int)getCenter().getX());
         this.gate.setY((int)getCenter().getY());
         return gate;
    }
    
    
    public void setGateStatus(int status){
        
        stopTimer();
        
          
        this.remove(getOpenCloseFigure(gate.isGateOpen()));
        
        circle.setAttribute(AttributeKeys.FILL_COLOR,AttributeKeys.FILL_COLOR.getDefaultValue());
        
        gate.setStatus(status);
        
        this.add(getOpenCloseFigure(gate.isGateOpen()));
    }
    
    private OpenCloseFigure getOpenCloseFigure(boolean aFlag){
        if(aFlag) return openFigure;
        else return closeFigure;
    }
    
    // EDITING
    @Override public Collection<Action> getActions(Point2D.Double p) {
        LinkedList<Action> actions = new LinkedList<Action>();
        if(GtsDrawing.isEditingMode()){
            actions.add(new NodeAction(this,p));
        }
        return actions;
    }
    
    /**
     * when timer expires, reset to its original gate state
     * @param e
     */
    
    public void actionPerformed(ActionEvent e) {
        circle.setAttribute(AttributeKeys.FILL_COLOR,AttributeKeys.FILL_COLOR.getDefaultValue());
        this.add(getOpenCloseFigure(gate.isGateOpen()));
        invalidate();
        JOptionPane.showMessageDialog(getView(), "The request to open/close gate is expired!",
                        "Warning Message", JOptionPane.WARNING_MESSAGE);
    }
    
     /**
      * track the node figure coordinate change and update the node object  
      * @author is08925
      *
      */
    
    private class GateFigureHandler extends FigureAdapter{
        private GateFigure owner;
        
        public GateFigureHandler(GateFigure figure){
            this.owner = figure;
        }
        
        public void figureChanged(FigureEvent e) {
        	
          GtsNode aNode = owner.getNode();
          aNode.setX((int)owner.getCenter().getX());
          aNode.setY((int)owner.getCenter().getY());
       }
    }
    
}
