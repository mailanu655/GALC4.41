package com.honda.galc.device.simulator.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.logging.Logger;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerXMLUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.simulator.data.DataContainerCommonUtil;


/**
 * <h3>OPCApplicationServerRouterClient</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>

 * <p>The OPCApplicationServerRouterClient is the implementation of the EquipmentApplicationServerClient interface
 * that OPC components will use when they want to send their business logic requests
 * through the client GUI applications, and share the HTTP Session Cookie maintained
 * by the client. </p>
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
 * <TD>martinek</TD>
 * <TD>Mar 9, 2004</TD>
 * <TD>insert version</TD>
 * <TD>@OIM50</TD>
 * <TD>Initial release</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Oct 12, 2005</TD>
 * <TD></TD>
 * <TD>@JM08</TD>
 * <TD>Add description</TD>
 * </TR>
 * <TR>
* <TD>martinek</TD>
* <TD>Jun 4, 2007</TD>
* <TD></TD>
* <TD>@JM200710</TD>
* <TD>Created from EIRouterClient with additional XML support</TD>
* </TR>
 * <TR>
 * <TD>lasenko</TD>
 * <TD>Dec 04, 2007</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL010</TD>
 * <TD>Introduced DataContainerTag.EQUIPMENT_TYPE</TD>
 * </TR>
 * </TABLE>
 * 
 * @modelguid {EDE4EBD3-C1A4-4FEE-990A-F485A012A46D} 
 */
public class OPCApplicationServerRouterClient implements EquipmentApplicationServerClient, HTTPRequestDispatcherTags {
	
	
	public static final String XML_UTILITIES_PROPERTIES_LOCATION= "xmlUtilitiesPropertiesLocation";
	
	private java.util.logging.Logger logger = null;
	
	//LogFileWriter logFileWriter = null;
	
	/**
	 * Trace control flag
	 */
	private boolean trace = false;


	/**
	 * 
	 * @modelguid {4DF71608-7EDF-4CBE-A76D-DC6445778FCF}
	 */
	private java.lang.String routerAddress;

	/**
	 * 
	 * @modelguid {176AB4F7-CE14-4509-9DA5-65D1152C6C86}
	 */
	private int routerPort = 0;
	
	/**
	 * 
	 */
	private boolean useXML = false;
	
	
	public OPCApplicationServerRouterClient(String url, boolean useXMLSetting, boolean traceSetting)
		throws java.net.MalformedURLException
	{
		this.trace = traceSetting;	
		
		
		
		
		if (url == null)
		{
		   throw new MalformedURLException("Invalid Router Client URL: null");	
		}
		
		int idx = url.indexOf(":");
		
		if (idx < 0)
		{
			throw new MalformedURLException("Invalid Router Client URL: "+url);	
		} 
		
		routerAddress = url.substring(0,idx);
		
		// At worst, port will be empty
		String port = url.substring(idx+1);
		
		if (port.length() == 0)
		{
			throw new MalformedURLException("Invalid Router Client URL: "+url);	
		}
		else
		{
		   try {
			 routerPort = Integer.parseInt(port);
		   }
		   catch (NumberFormatException e)
		   {
			throw new MalformedURLException("Invalid Router Client URL: "+url);
		   }	
		}
		

		

		this.useXML = useXMLSetting;
		
		
	}

	/**
	 * Transmit DataContainer to the request router.
	 * @param aData Transmitting DataContainer to Servlet
	 * @return Return from Servlet
	 * @throws IOException When input/output error accrues at 
	 * the time of establishing connection of Host. 
	 * @exception com.honda.global.galc.common.GALCException When
	 * the abnormal accrues in the system.
	 * @modelguid {08100333-F418-44AB-BC1E-23A8470C9B2D}
	 */
	public DataContainer send(DataContainer aData)
		throws java.io.IOException, SystemException
	{
		Socket s = null;
		OutputStream os = null;
		InputStream is = null;
		InputStreamReader isreader = null;
		BufferedReader breader = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		
		DataContainer result = null;
		
		String step = "OPCApplicationServerRouterClient.send()";
		
		long initTime = System.currentTimeMillis();
		long openTime = 0;
		long getOutputTime = 0;
		long writeObjectTime = 0;
		long getInputTime = 0;
		long readObjectTime = 0;
		
		try {
			 if (trace)
			 {
				System.out.println("OPCApplicationServerRouterClient.send()");
				logMessage("OPCApplicationServerRouterClient.send()");
			 }
			 
			 step = "OPCApplicationServerRouterClient.send() open socket";
			 s = new Socket(this.routerAddress, this.routerPort);
			 openTime = System.currentTimeMillis();
			
			 step = "OPCApplicationServerRouterClient.send() get output stream";
			 os = s.getOutputStream();
			 getOutputTime = System.currentTimeMillis(); 
		    
			 
			 if (!useXML)  // @JM200708
			 {
				 oos = new ObjectOutputStream(os);
				 step = "OPCApplicationServerRouterClient.send() writing data container";
				 oos.writeObject(aData);
				 oos.flush();
				
			 }
			 else
			 {
				 step = "OPCApplicationServerRouterClient.send() writing data container";
				 DataContainerXMLUtil.convertToXML(aData, os);
				 os.flush();
				 
				 
			 }
 
			 writeObjectTime = System.currentTimeMillis();
		     
			 step = "OPCApplicationServerRouterClient.send() getting input stream";
			 is = s.getInputStream();
			 
			 if (!useXML) {
				ois = new ObjectInputStream(is);
				getInputTime = System.currentTimeMillis();

				step = "OPCApplicationServerRouterClient.send() reading object stream";
				result = (DataContainer) ois.readObject();
				readObjectTime = System.currentTimeMillis();
			} else {
//				getInputTime = System.currentTimeMillis();
//				step = "OPCApplicationServerRouterClient: reading XML response";
//
//				// Read the contents of the input stream into
//				// a temporary buffer and pass that to xmlUtilities
//				// We do this so we know when to quit reading from stream
//				isreader = new InputStreamReader(is);
//
//				breader = new BufferedReader(isreader);
//
//				StringBuffer inputBuffer = new StringBuffer();
//				String line = breader.readLine();
//				while (line != null) {
//					inputBuffer.append(line);
//					inputBuffer.append("\n");
//
//					if (line.indexOf(END_DATACONTAINER_XML) > 0) {
//						// We've reached the end of the data container
//						break;
//					}
//
//					line = breader.readLine();
//				}

				result = DataContainerXMLUtil.readDeviceDataFromXML(is);
				readObjectTime = System.currentTimeMillis();
			}
			 
			 
		}
		catch (ClassNotFoundException e)
		{
			logMessage(step, e);
								
		}
		catch (java.io.IOException e)
		{
		     
			logMessage(step, e);
			e.printStackTrace(System.out);
		}
		catch (Exception e)
		{
			logMessage(step,e);
			e.printStackTrace(System.out);
		}
		finally
		{
			if (oos != null)
			{
			   try {oos.close();} catch (Exception e) {}	
			}
				
			if (ois != null)
			{
			   try {ois.close();} catch (Exception e) {}	
			}	
			
			if (breader != null)
			{
				try {breader.close(); } catch (Exception e) {}
			}
			
			if (isreader != null)
			{
				try {isreader.close();} catch (Exception e) {}
			}			
			
			if (os != null)
			{
			   try {os.close();} catch (Exception e) {}	
			}
				
			if (is != null)
			{
			   try {is.close();} catch (Exception e) {}	
			}	
		
			if (s != null)
			{
			   try {s.close();} catch (Exception e) {}	
			}
			
			if (trace)
			{
				System.out.println("Time to open socket       " + (openTime - initTime));
				System.out.println("Time to get output stream " + (getOutputTime - openTime));
				System.out.println("Time to do write          " + (writeObjectTime - getOutputTime));
				System.out.println("Time to get input stream  " + (getInputTime - writeObjectTime));
				System.out.println("Time to read input        " + (readObjectTime - getInputTime));

				System.out.println("\nTotal time                 " + (readObjectTime - initTime));
                
                
				logMessage("Time to open socket       " + (openTime - initTime));
				logMessage("Time to get output stream " + (getOutputTime - openTime));
				logMessage("Time to do write          " + (writeObjectTime - getOutputTime));
				logMessage("Time to get input stream  " + (getInputTime - writeObjectTime));
				logMessage("Time to read input        " + (readObjectTime - getInputTime));

				logMessage("Total time                 " + (readObjectTime - initTime));
			}
		}
		return result;		
	}
	
	/**
	 * Give the data transmitting function for EquipmentInterface.
	 * <P>
	 * The argument except DataContainer is all put() with
	 * the contents of DataContainerTag as a key to DataContainer of
	 * the argument.
	 * @return 
	 * @param aClient ClientID
	 * @param aData Transmitting DataContainer
	 * @exception java.io.IOException When input/output error accrues at
	 * the time of establishing the connection with Host.
	 * @modelguid {12AA7C4F-BAFF-4CBC-8E97-10CA2634961A}
	 */
	public DataContainer transmit(
		String aProcessPointID,
		String aClient,
		DataContainer aData)
		throws java.io.IOException
	{
		// Note: this code is the same as HTTPClient
		if (aData == null) {
			aData = new DefaultDataContainer();
		}
    
		aData.put(DataContainerTag.CLIENT_ID, aClient);
		DataContainerCommonUtil.setEquipmentOpc(aData);  // @RL010
		aData.put( HTTP_REQUEST_TYPE, R_TYPE_TRANSMIT);
    	
		try {
			return send(aData);
		} catch (SystemException e) {
		   return null;
		}
	 
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (2001/06/05 11:30:23)
	 * @return com.honda.global.galc.common.data.DataContainer
	 * @param aClient java.lang.String
	 * @param aData com.honda.global.galc.common.data.DataContainer
	 * @exception java.io.IOException The exception description.
	 * @modelguid {E5DEA60E-C9E3-4F8E-9C79-7A6D3ADF7320}
	 */
	public DataContainer transmitAsync(
		String aProcessPointId,
		String aClient,
		DataContainer aData)
		throws java.io.IOException
	{
		// Note: this is the same logic as HTTPClient
		if (aData == null) {
			aData = new DefaultDataContainer();
		}

		aData.put(DataContainerTag.EI_SYNC_MODE, DataContainerTag.EI_ASYNC);

		return transmit(aProcessPointId, aClient, aData);
	}

	
	public DataContainer transmitStateless(String aProcessPointId, String aClient, DataContainer aData) throws IOException {
		aData.put(DataContainerTag.CLIENT_ID, aClient);
		DataContainerCommonUtil.setEquipmentOpc(aData);  // @RL010
		aData.put(
			 HTTP_REQUEST_TYPE,
			 R_TYPE_TRANSMIT_STATELESS);
		try {
			return send(aData);
		} catch (SystemException e) {
			return null;
		}
	}

	public DataContainer transmitStatelessAsync(String aProcessPointId, String aClient, DataContainer aData) throws IOException {
		if (aData == null) {
	        aData = new DefaultDataContainer();
	    }

	    aData.put(DataContainerTag.EI_SYNC_MODE, DataContainerTag.EI_ASYNC);

	    return transmitStateless(aProcessPointId, aClient, aData);
	}
	
	
	
	
	/**
	 * @JM083 - Log the message
	 * @param text
	 * @param e
	 */
	private void logMessage(String text, Exception e)
	{
		if (e != null)
		{
			getLogger().severe(text+ " " + e.toString());
		}
		else
		{
			getLogger().severe(text);
		}
	}

		
	
	/**
	 * @JM083 - Log the message
	 * @param text
	 */
	private void logMessage(String text)
	{
		try {
//			if (logFileWriter == null)
//			{
//			  // Thread name is set up by EI components to reflect process point
//			  String name = Thread.currentThread().getName() + "_OPCApplicationServerRouterClient";
//			  logFileWriter = new LogFileWriter(logDirectory, name);
//			}
//			
//			logFileWriter.log(text);
//			
//			logFileWriter.close();
			
			getLogger().info(text);
		}
		catch (Exception e2)
		{
		   System.out.println("Problem logging "+e2.toString());
		   
		   System.out.println("  Log message: "+text);
		   
		  
		}
	}

	public boolean isUseXML() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setUseXML(boolean useXML) {
		// TODO Auto-generated method stub
		
	}

	public void setLogger(Logger newLogger) {
		
		logger = newLogger;
		
	}
	
	protected java.util.logging.Logger getLogger() {
		
		if (logger == null)
		{
			logger = Logger.getLogger("com.honda.global.galc.system.opc");
			logger.setLevel(java.util.logging.Level.FINEST);
		}
		
		return logger;
	}



//	public static void main(String[] parms)
//	{
//		try {
//		
//	    
//		DataContainer dc = new DefaultDataContainer();
//	    
//           
//		dc.put(DataContainerTag.APPLICATION_ID, "PROPSRVC");
//		dc.put(DataContainerTag.PROCESS_POINT_ID, "PROPSRVC");
//       
//	   for (int i=0; i<1; i++)
//	   {
//		DataContainer rdc = client.transmit("PROPSRVC", "TESTCL", dc);
//            
//		System.out.println(rdc);
//	    
//		rdc = client.transmitAsync("PROPSRVC","TESTCL",dc);
//	    
//		System.out.println(rdc);
//	   }
//	    
//		}
//		catch (Exception e)
//		{
//		   System.out.println(e);
//		   e.printStackTrace(System.out);
//		}
//	    
//	}
}


