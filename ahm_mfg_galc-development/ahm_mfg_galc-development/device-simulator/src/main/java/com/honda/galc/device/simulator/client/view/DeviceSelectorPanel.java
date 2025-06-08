package com.honda.galc.device.simulator.client.view;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;

import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.conf.SiteDao;
import com.honda.galc.device.simulator.client.ui.GalcPanel;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.Site;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JLabel;

import static com.honda.galc.service.ServiceFactory.getDao;

/**
 * <h3>DeviceSelectorPanel</h3>
 * <h4> </h4>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Sep. 23, 2008</TD>
 * <TD>Version 0.2</TD>
 * <TD></TD>
 * <TD>Update to support device simulator configuration.</TD>
 * </TR>
 * </TABLE>
 */
public class DeviceSelectorPanel extends GalcPanel implements IDeviceSelectListener, ActionListener  {
    
    private static final long serialVersionUID = -5702894470521059242L;
    Logger log = Logger.getLogger(DeviceSelectorPanel.class);  //  @jve:decl-index=0:    
    	
    private static final String _site = "Site";
    private static final String _plant = "Plant";
    private static final String _division = "Division";  //  @jve:decl-index=0:
    
        
    private JScrollPane deviceListTablePane = null;
    private JTable deviceListTable = null;
    private JComboBox jComboBox = null;
    private JComboBox jComboBox1 = null;
    private JComboBox jComboBox2 = null;    
    private ArrayList<IDeviceSelectable> _deviceSel = new ArrayList<IDeviceSelectable>();
	private JPanel jPanelDevSel = null;  //  @jve:decl-index=0:visual-constraint="108,36"
	private JLabel jLabelSite = null;
	private JLabel jLabelPlant = null;
	private JLabel jLabelDivision = null;  
	
	private Map<String,List<Device>> allDevices = new HashMap<String,List<Device>>();
	
	private DeviceTableModel deviceTableModel;
    
    /**
     * This method initializes 
     */
    public DeviceSelectorPanel() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {  
          
    	this.setLayout(new BorderLayout());
    	this.add(getJPanelDevSel(),java.awt.BorderLayout.NORTH);        	
    	this.add(getDeviceListTablePane(), java.awt.BorderLayout.CENTER);
        	
    }

 

    /* (non-Javadoc)
     * @see com.honda.global.device.simulator.IDeviceSelectListener#addDeviceSelectListener(com.honda.global.device.simulator.IDeviceSelectable)
     */
    public void addDeviceSelectListener(IDeviceSelectable deviceSelectable) {
        this._deviceSel.add(deviceSelectable);        
    }
    /**     
     *  
     * @return javax.swing.void
     */
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
         if(cb.equals(jComboBox)){   
            Site site = (Site)cb.getSelectedItem();
            //update plant list and reset division point list
            if(site != null){
                updatePlantList(site.getSiteName(), null);
                resetDeviceListTable();                
            }
        } else if(cb.equals(jComboBox1)){
        	Site site = (Site)jComboBox.getSelectedItem();
    		Plant plant = (Plant)cb.getSelectedItem();
        	
        	if(site != null && plant!=null){
                updateDivisionList(site.getSiteName(),plant.getPlantName(), null);           
        	}
        } else if(cb.equals(jComboBox2)) {
        	Division division = (Division) cb.getSelectedItem();
            updateDeviceListTable(division.getDivisionId());
        }
     }
    

    /**
     * This method initializes deviceListTablePane  
     *  
     * @return javax.swing.JScrollPane  
     */
    public JScrollPane getDeviceListTablePane() {
        if (deviceListTablePane == null) {
  			deviceListTablePane = new JScrollPane();				
			deviceListTablePane.setViewportView(getDeviceListTable());
			deviceListTablePane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    }       
        return deviceListTablePane;
    }
 
    /**
     * This method initializes deviceListTable  
     *  
     * @return javax.swing.JTable   
     */
    private JTable getDeviceListTable() {
        if (deviceListTable == null){
            deviceListTable = new JTable();
            deviceListTable.setLocation(new Point(0, 0));
            deviceListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            ListSelectionModel rowSM = deviceListTable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    // Ignore extra messages.
                    if (e.getValueIsAdjusting()) return;
                    ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                    int selectedRow = lsm.getMinSelectionIndex();
                    for(IDeviceSelectable devSel :_deviceSel)
                         devSel.setDevice(deviceTableModel.getItem(selectedRow));
                        
                    //reset the message area; 
                    //if it's diplaying an error message, then the message is removed
                    displayMessage("");
                    //log.info("setDevice is called - When row :" + selectedRow + " is now selected.");                     
                    
                }
            });
        }
        
        resetDeviceListTable();
        
        return deviceListTable;
    }
    
     /**
     * This method initializes jComboBox    
     *  
     * @return javax.swing.JComboBox    
     */
    private JComboBox getJComboBox() {              
        
        if (jComboBox == null) {
            jComboBox = createComboBox(_site, "Select Site");
            updateSiteList(null);
        }
        return jComboBox;
        
    }

    /**
     * This method initializes jComboBox1   
     *  
     * @return javax.swing.JComboBox    
     */
    private JComboBox getJComboBox1() {
         
        if (jComboBox1 == null) jComboBox1 = createComboBox(_plant, "Select Plant");
        return jComboBox1;

    }

    /**
     * This method initializes jComboBox2   
     *  
     * @return javax.swing.JComboBox    
     */
    private JComboBox getJComboBox2() {
        
        if (jComboBox2 == null) jComboBox2 = createComboBox(_division, "Select Division");
        return jComboBox2;
        
    }
    
    private JComboBox createComboBox(String name, String toolTipText) {
        JComboBox comboBox = new JComboBox();
        comboBox.addActionListener(this);
        comboBox.setName(name);
        comboBox.setToolTipText(toolTipText);
        comboBox.setPreferredSize(new Dimension(150, 25));
        return comboBox;
    }
    
    /**
     * This method update site seletion list   
     *  
     * @return void    
     */
    public void updateSiteList(String sSelected)
    {

        List<Site> sites = getDao(SiteDao.class).findAll();
		
	    ComboBoxModel<Site> aModel = new ComboBoxModel<Site>(sites,"getSiteName");
	    
	    jComboBox.setModel(aModel);
	    jComboBox.setRenderer(aModel);    
	    

	    if (sSelected != null) {
	        for(Site site: sites) {
	            if(site.getSiteName().trim().equals(sSelected)) 
	                jComboBox.setSelectedItem(site);
	        }
		} else {
			jComboBox.setSelectedItem(null);
			
			if(jComboBox1 != null){
				ComboBoxModel<Plant> aPlantModel = new ComboBoxModel<Plant>(new ArrayList<Plant>(),"getPlantName");
				jComboBox1.setModel(aPlantModel);
			}
			if(jComboBox2 != null){
				ComboBoxModel<Division> aDeviceModel = new ComboBoxModel<Division>(new ArrayList<Division>(),"getDivisionName");
				jComboBox2.setModel(aDeviceModel);
				 resetDeviceListTable();
			}
		}

      
    }
 
    /**
     * This method update plant seletion list   
     *  
     * @return void    
     */
    private void updatePlantList(String sSite, String sSelected)
    {
		List<Plant> plants = getDao(PlantDao.class).findAllBySite(sSite);
		
		ComboBoxModel<Plant> aModel = new ComboBoxModel<Plant>(plants,"getPlantName");
		
	    jComboBox1.setModel(aModel);
	    jComboBox1.setRenderer(aModel);   

	    if (sSelected != null) {
            for(Plant plant: plants) {
                if(plant.getPlantName().trim().equals(sSelected)) 
                    jComboBox1.setSelectedItem(plant);
            }
        }else {
        	jComboBox1.getModel().setSelectedItem(null);
        }


    }

    /**
	 * This method update division seletion list
	 * 
	 * @return void
	 */
    private void updateDivisionList(String sSite, String sPlant, String sSelected)
    {    	
   		List<Division> divisions = getDao(DivisionDao.class).findById(sSite,sPlant);
			
		ComboBoxModel<Division> aModel = new ComboBoxModel<Division>(divisions,"getDivisionName");

	      jComboBox2.setModel(aModel);
	      jComboBox2.setRenderer(aModel);  

	      if (sSelected != null) {
	            for(Division division: divisions) {
	                if(division.getDivisionId().trim().equals(sSelected)) 
	                    jComboBox2.setSelectedItem(division);
	            }
	        } 

    }
    
    
    /**
     * This method update device seletion table   
     *  
     * @return void    
     */
    private void updateDeviceListTable(String divisionId) {  
        
        deviceTableModel = new DeviceTableModel(findDevices(divisionId),deviceListTable);
        
    }
    
    
    private List<Device> findDevices(String divisionId) {
        //if(allDevices.containsKey(divisionId)) return allDevices.get(divisionId);
        List<Device> devices = new ArrayList<Device>();
       // openJPA returns unmodifiable list so copy makes a modifiable list
        for(Device dev : getDao(DeviceDao.class).findAllByDivisionId(divisionId)){
            devices.add(dev);
        }
        allDevices.put(divisionId, devices);
        return devices;
    }
    
    
    /**
     * This method reset device seletion table   
     *  
     * @return void    
     */
    private void resetDeviceListTable()
    {
        deviceTableModel = new DeviceTableModel(null,deviceListTable);
        deviceListTable.setModel(deviceTableModel);
    }
 
  
 

	/**
	 * This method initializes jPanelDevSel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelDevSel() {
		if (jPanelDevSel == null) {
			jLabelDivision = new JLabel();
			jLabelDivision.setText(_division);
			jLabelPlant = new JLabel();
			jLabelPlant.setText(_plant);
			jLabelSite = new JLabel();
			jLabelSite.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
			jLabelSite.setText(_site);
			jPanelDevSel = new JPanel();
			jPanelDevSel.setLayout(new GridLayout(2, 3));				
			jPanelDevSel.add(jLabelSite, null);
			jPanelDevSel.add(jLabelPlant, null);
			jPanelDevSel.add(jLabelDivision, null);
			jPanelDevSel.add(getJComboBox());
			jPanelDevSel.add(getJComboBox1());
			jPanelDevSel.add(getJComboBox2());		
		}
		return jPanelDevSel;
	}
    
	public void setDeviceSelection(String deviceId) {	
		String sSite = null;
		String sPlant = null;
		String sDivisionId = null;
		deviceId = deviceId.trim();
		
		// retrieve Device object
		Device device = getDao(DeviceDao.class).findByKey(deviceId);
		Division division = getDao(DivisionDao.class).findByKey(device.getDivisionId());
		sSite = division.getSiteName().trim();
		sPlant = division.getPlantName().trim();
		sDivisionId = division.getDivisionId().trim();

		// set site dropdown selection (it should be populated already)		
		updateSiteList(sSite);
		
		// populate and update plant dropdown 
		updatePlantList(sSite, sPlant);		
			
		// populate and update division dropdown		
		updateDivisionList(sSite, sPlant, sDivisionId);
		
		selectDevice(deviceId);
		
	}
	
	private void selectDevice(String deviceId) {
	    int index = 0;
	    for(Device dev :deviceTableModel.getItems()){
	        if(dev.getClientId().trim().equals(deviceId)){
	            deviceListTable.getSelectionModel().setSelectionInterval(index,index);
	            return;
	        }
	        index++;
	    }
	}
	
    private class DeviceTableModel extends SortableTableModel<Device>{
        private static final long serialVersionUID = 1L;
        
        public DeviceTableModel(List<Device> devices, JTable table){
            super(devices,new String[] {"ClientId ", "ProcessPoint ", "ReplyClientId "},table);
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(rowIndex >= getRowCount()) return null;
            Device device = items.get(rowIndex);
            if(device==null) return null;
            switch(columnIndex){
                case 0: return device.getClientId()==null?null:device.getClientId().trim();
                case 1: return device.getIoProcessPointId()==null?null:device.getIoProcessPointId().trim();
                case 2: return device.getReplyClientId()==null?null:device.getReplyClientId().trim();
            }
            return null;
       }
    }

	public Device getDevice(String clientId) {
		for(Device dev :deviceTableModel.getItems()){
		     if(dev.getClientId().trim().equals(clientId)){
		           return dev;
		    }
		}
		
		return null;
	}

}  //  @jve:decl-index=0:visual-constraint="108,15"
