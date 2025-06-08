package com.honda.galc.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerXMLUtil;

/**
 * 
 * <h3>DataContainerSocketSender Class description</h3>
 * <p> DataContainerSocketSender description </p>
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
 * Apr 13, 2010
 *
 */

public class DataContainerSocketSender extends SocketSender<DataContainer>{

boolean isDurable = true;

	public DataContainerSocketSender(String ip,int port) {
		super(new SocketClient(ip,port));
		this.isDurable = false;
	}
	
	public DataContainerSocketSender(String ip,int port,int sotimeout) {
		super(new SocketClient(ip,port));
		socketClient.setSoTimeout(sotimeout);
		this.isDurable = false;
	}

	public DataContainerSocketSender(SocketClient socketClient) {
		super(socketClient);
	}
	public DataContainer syncSend(DataContainer dc) {
		try {
			
			basicSend(dc);
			return receive();
		}finally{
			if(!isDurable) socketClient.close();	
		}
	}
	
	
	public void send(DataContainer dc) {
		try {
			basicSend(dc);
		}finally{
			if(!isDurable) socketClient.close();	
		}
	}
	
	private void basicSend(DataContainer dc) {
		OutputStream outputStream = socketClient.getOutputStream();
		DataContainerXMLUtil.convertToXML(dc,outputStream);
		try {
			outputStream.flush();
		} catch (IOException e) {
			 Logger.getLogger().error(e, "Could not send the datacontainer request for the data :"+ dc.toString());
			
		}
	}

	@Override
	protected DataContainer receive() {
		return DataContainerXMLUtil.readDeviceDataFromXML(socketClient.getInputStream());
	}
	
	
	

}
