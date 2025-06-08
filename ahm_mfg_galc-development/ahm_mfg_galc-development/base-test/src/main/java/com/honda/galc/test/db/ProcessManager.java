package com.honda.galc.test.db;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.device.simulator.utils.DevSimulatorUtil;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Terminal;


/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ProcessManager</code> is ...
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
 * <TD>Mar 10, 2009</TD>
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

public class ProcessManager {
    
    
    public static ProcessManager processManager;
    
    
    private List<Division> divisions= new ArrayList<Division>();
    private ProcessPoint processPoint;
    private String clientName;
    private Terminal terminal;
    public static ProcessManager getInstance() {
        
        if(processManager == null) processManager = new ProcessManager();
        return processManager;
    }
    
    
    public ProcessManager(){

    	ApplicationContextProvider.loadFromClassPathXml("Dao.xml");
    	divisions = getDao(DivisionDao.class).findAll();
    	
    }
    
    public List<Division> getAllDivisions() {
        return divisions;
    }
    
    
    public String getProcessPointId() {
        
        if(processPoint == null) return null;
        
        else return processPoint.getProcessPointId().trim();
        
    }
    
    
    public ProcessPoint getProcessPoint() {
        
        return processPoint;
        
    }
    
    /**
     * Set the client name of a GUI app
     * Use this to get the corresponding process point 
     * @param clientName
     */
    
    public void setClientName(String clientName) {
        this.clientName = clientName;
        
        deriveProcessPoint();
    }
    
    public String getClientName() {
        return clientName;
    }
    
    private void deriveProcessPoint() {
        
        terminal = getDao(TerminalDao.class).findByKey(clientName);
        terminal.setIpAddress(DevSimulatorUtil.getLocalHostIP());
        getDao(TerminalDao.class).save(terminal);
        this.processPoint = terminal.getProcessPoint();
        DeviceManager.getInstance().updateHostIps();
        
    }
    
    /**
     * Set process Point Id
     * Used for Headless tasks
     * @param processPointId
     */
    
    public void setProcessPointId(String processPointId) {
    	this.clientName = null;
    	this.terminal = null;
        this.processPoint = getDao(ProcessPointDao.class).findByKey(processPointId);
        DeviceManager.getInstance().updateHostIps();
    }
    
    public Terminal getTerminal() {
    	
    	return terminal;
    	
    }
}
