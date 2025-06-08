package com.honda.galc.client.gts.view.action;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

import com.honda.galc.client.gts.view.GtsTrackingModel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.util.KeyValue;

/**
 * 
 * @author hcm_adm_008925
 *
 */
public class AfonInforDialog extends JDialog implements ActionListener,ComponentListener{
    
    private static final long serialVersionUID = 1L;
    
    private static Point location = new Point(20,50);
    
    private static  AfonInforDialog instance;
    
    private static String LANE_pJ = "pJ";
    private static String LANE_pO = "pO";
    
    GtsTrackingModel model;
    
    ObjectTablePane<KeyValue<String,String>> vinTablePane;
    
    public AfonInforDialog(GtsTrackingModel model){
    	this.model = model;
   // 	setSize(1000, 350);
        initComponent();
         pack();
        this.setAlwaysOnTop(true);
        this.setTitle("NEAR AFON VINS");
        this.setLocation(location);
        this.addComponentListener(this);
        this.reload();
    }
    
    public static AfonInforDialog getInstance() {
    	return instance;
    }
    
    private void initComponent(){
    	ColumnMappings columnMappings = ColumnMappings.with("LANE_POS", "key").put("SHORT VIN","value");
    	vinTablePane = new ObjectTablePane<KeyValue<String,String>>(columnMappings.get(), false);
    	this.getContentPane().add(vinTablePane);
    	vinTablePane.setPreferredSize(new Dimension(240,95));
    	vinTablePane.getTable().setFont(Fonts.DIALOG_BOLD_26);
    	vinTablePane.getTable().setRowHeight(30);
    	vinTablePane.getColumnHeader().setVisible(false);
   }
    
    public void reload() {
    	List<GtsLaneCarrier> pJlaneCarriers = model.findLane(LANE_pJ).getLaneCarriers();
    	List<GtsLaneCarrier> pOlaneCarriers = model.findLane(LANE_pO).getLaneCarriers();
    	
    	List<KeyValue<String,String>> vinList = new ArrayList<KeyValue<String,String>>();
    	vinList.add(new KeyValue<String, String>(LANE_pJ+"1", pJlaneCarriers.size()>0 ? pJlaneCarriers.get(0).getShortProdId() : "" ));
    	vinList.add(new KeyValue<String, String>(LANE_pJ+"2", pJlaneCarriers.size()>1 ? pJlaneCarriers.get(1).getShortProdId() : "" ));
    	vinList.add(new KeyValue<String, String>(LANE_pO+"1", pOlaneCarriers.size()>0 ? pOlaneCarriers.get(0).getShortProdId() : "" ));
    	vinTablePane.reloadData(vinList);
    }
    
    public static void reloadData(String laneName) {
    	
    	if(instance != null && (LANE_pJ.equalsIgnoreCase(laneName) || LANE_pO.equalsIgnoreCase(laneName))) {
    		getInstance().reload();
    	}
    }
    
    public static void openDialog(GtsTrackingModel model) {
    	if(instance == null) {
    		instance = new AfonInforDialog(model);
    	}
    	instance.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
       close();
    }
    
    private void close(){
        this.setVisible(false);
    }

    public void componentHidden(ComponentEvent e) {
    }
    
    public void componentMoved(ComponentEvent e) {
        if(e.getSource() == this){
            location = this.getLocation();
        }
    }
    
    public void componentResized(ComponentEvent e) {
    }
    
    public void componentShown(ComponentEvent e) {
    }
    
  
}
