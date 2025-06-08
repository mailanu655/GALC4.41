package com.honda.galc.client.gts.view.action;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.entity.enumtype.GtsInspectionStatus;
import com.honda.galc.entity.enumtype.GtsProductStatus;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsProduct;
import com.honda.galc.entity.gts.GtsZone;


/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ZoneInfoTableModel</code> is ...
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
 * <TD>Mar 25, 2008</TD>
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

public class ZoneInfoTableModel extends AbstractTableModel{
    
    private static final long serialVersionUID = 1L;
    
    private GtsDrawing drawing;
    
    private List<GtsZone> zones;
    
    
    private String[] names = new String[]{"Passed & Released Bodies","Passed Inspection Status",
                                          "Failed Inspection Status - Small", "Failed Inspection Status - Medium",
                                          "Failed Inspection Status - Large", "Weld Inspection Status",
                                          "Failed Inspection Status","Unknown Inspection Status","Stop Inspection Status",
                                          "Released Product Status","Hold Product status", 
                                          "Scrap Product Status","Empty Carriers",
                                          "Unknown", "Total Carriers"};
    private int[][] data;
    
    public ZoneInfoTableModel(GtsDrawing drawing, List<GtsZone> zones){
        
        this.drawing = drawing;
    	this.zones = zones;
        
        calculate();

    }
    
    public String getColumnName(int column) {
        if(column == 0) return "Description";
        else if(column == getColumnCount() -1) return "Total";
        else return zones.get(column -1).getId().getZone();
    }   
    
    public int getColumnCount() {
        return zones.size() + 2;
    }
    
    public int getRowCount() {
        return names.length;
    }
    
    public String getHTMLToolTip(int columnIndex) {
        if(columnIndex == 0 || columnIndex == getColumnCount() -1) return null;
        
        GtsZone zone = zones.get(columnIndex -1);
        
        StringBuilder buf = new StringBuilder();
        buf.append("<html>");

        buf.append("Description: ");
        buf.append(zone.getZoneDescription());
        buf.append("<br>");
        buf.append("Lanes: <br>");
        
        for(String laneName : zone.getLaneNames()) {
            buf.append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp ");
            buf.append(laneName);
            buf.append("<br>");
        }
        
        buf.append("</html>");
        return buf.toString();
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex >= getRowCount()) return null;
        if(columnIndex == 0) return names[rowIndex];
        else return data[rowIndex][columnIndex -1];
    }
    
    private void calculate(){
        
        data = new int[names.length][getColumnCount()-1];
        
        int i = 0;
        for(GtsZone zone: zones) {
            String[] laneNames = zone.getLaneNames();
            List<GtsLaneCarrier> laneCarriers = new ArrayList<GtsLaneCarrier>();
            for(String laneName : laneNames) {
                GtsLane lane = drawing.getModel().findLane(laneName);
                if(lane != null) laneCarriers.addAll(lane.getLaneCarriers());
            }
            
            calculate(i,laneCarriers);
            
            i++;
        }
        
        for(int m = 0; m < names.length; m++) {
            int count = 0;
            for(int n = 0; n< zones.size();n++) {
                count += data[m][n];
            }
            
            data[m][zones.size()] = count;
        }
    }
    
    private void calculate(int i,List<GtsLaneCarrier> laneCarriers) {
        
        for(GtsLaneCarrier lc: laneCarriers) {
            
            GtsProduct product = lc.getProduct();
            
            if(product != null) {
                if(product.isReleased() && product.getInspectionStatus() == GtsInspectionStatus.PASS) data[0][i] += 1;
                if(product.getInspectionStatus() == GtsInspectionStatus.PASS)data[1][i] += 1;
                if(product.getInspectionStatus() == GtsInspectionStatus.FAIL_S) data[2][i] += 1;
                if(product.getInspectionStatus() == GtsInspectionStatus.FAIL_M) data[3][i] += 1;
                if(product.getInspectionStatus() == GtsInspectionStatus.FAIL_L) data[4][i] += 1;
                if(product.getInspectionStatus() == GtsInspectionStatus.FAIL) data[5][i] += 1;
                if(product.getInspectionStatus() == GtsInspectionStatus.WELD) data[6][i] += 1;
                if(product.getInspectionStatus() == GtsInspectionStatus.UNKNOWN) data[7][i] += 1;
                if(product.getInspectionStatus() == GtsInspectionStatus.STOP) data[8][i] +=1;
                if(product.isReleased()) data[9][i] += 1;
                if(product.getProductStatus() != GtsProductStatus.RELEASE) data[10][i] += 1;
                if(product.isScrapped()) data[11][i] += 1;
            }else {
                if(!lc.isUnknownCarrier()) data[12][i] += 1;
                else data[13][i] += 1;
            }
        }
        
        data[14][i] = laneCarriers.size();
        
    }

}