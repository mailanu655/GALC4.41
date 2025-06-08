package com.honda.galc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.net.DataContainerSocketSender;
import com.honda.galc.service.datacollection.IoServiceBase;
import com.honda.galc.service.property.PropertyService;
/**
 * 
 * 
 * <h3>DeviceDataRoutingServiceImpl Class description</h3>
 * <p> DeviceDataRoutingServiceImpl description </p>
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
 * Mar 24, 2014
 *
 *
 */
public class DeviceDataRoutingServiceImpl extends IoServiceBase implements DeviceDataRoutingService{

	@Autowired
	TerminalDao terminalDao;
	
	String terminalId;
	
	@Override
	public DataContainer processData() {
		Terminal terminal = getTerminal();
		if(terminal == null) {
			getLogger().error("Could not find a terminal with io process point id : " + getProcessPointId());
			return null;
		} else return syncSend(terminal);
	}

	public void asyncAllExecute(DefaultDataContainer data) {
		super.asyncAllExecute(data);
	}

	public void asyncExecute(DefaultDataContainer data) {
		super.asyncExecute(data);
	}
	
	public void asyncAllProcessData() {
		List<Terminal> terminals = terminalDao.findAllByProcessPointId(getProcessPointId());
		if(terminals == null || terminals.isEmpty()) {
			getLogger().error("Could not find a terminal with io process point id : " + getProcessPointId());
		} else send(terminals);
	}
	
	public void asyncProcessData() {
		Terminal terminal = terminalDao.findFirstByProcessPointId(getProcessPointId());
		if(terminal == null) {
			getLogger().error("Could not find a terminal with io process point id : " + getProcessPointId());
		} else send(terminal);
	}

	public DataContainer execute(DefaultDataContainer data) {
		if(data.getString("BROADCAST_TERMINAL_ID") != null) {
			this.terminalId = data.getString("BROADCAST_TERMINAL_ID");
		}
		return super.execute(data);
	}
	
	public void send(List<Terminal> terminals) {
		for (Terminal terminal : terminals) {
			send(terminal);
		}
	}
	
	public void send(Terminal terminal) {
		try{
			DataContainerSocketSender socketSender = 
				new DataContainerSocketSender(terminal.getIpAddress(),terminal.getPort(),getTimeout());
			socketSender.send(getData());
			getLogger().info("sent data : " +getData().toString());
		}catch(Exception e) {
			getLogger().error(e,"Failed to async send data " + getData().toString() + " to terminal " + terminal );
		}
	}
	
	protected DataContainer syncSend(Terminal terminal) {
		DataContainer returnDC = null;
		try{
			DataContainerSocketSender socketSender
				= new DataContainerSocketSender(terminal.getIpAddress(),terminal.getPort(),getTimeout());
			returnDC = socketSender.syncSend(getData());
			getLogger().info("sent data : " + getData().toString());
		}catch(Exception e) {
			getLogger().error(e,"Failed to sync send data " + getData().toString() + " to terminal " + terminal );
			return dataCollectionInComplete();
		}
		if(returnDC != null) getLogger().info("return data : " + returnDC.toString());
		else {
			getLogger().error("return data is null");
			return dataCollectionInComplete();
		}
		return returnDC;
	}
	
	private int getTimeout() {
		//socket read timeout in millisecs
		return PropertyService.getPropertyInt(getProcessPointId(), "SO_TIME_OUT", 3000);
	}

	public Terminal getTerminal() {
        Terminal terminal = null;
        if(this.terminalId != null && this.terminalId.trim().length() > 0) {
        	terminal = terminalDao.findByKey(this.terminalId);
        } else {
        	terminal = terminalDao.findFirstByProcessPointId(getProcessPointId());
        }
        return terminal;
	}

}
