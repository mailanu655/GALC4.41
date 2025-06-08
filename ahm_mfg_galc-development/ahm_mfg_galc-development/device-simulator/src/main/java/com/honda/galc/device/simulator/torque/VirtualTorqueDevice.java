package com.honda.galc.device.simulator.torque;

import java.net.*;
import java.io.*;

import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ui.event.Event;
import com.honda.galc.client.ui.event.EventType;
import com.honda.galc.device.simulator.utils.Logger;
import com.honda.galc.device.simulator.utils.Utils;

public class VirtualTorqueDevice extends Thread
{
	protected int _noOfClients = 0;
	
	private FileWriter _fOut = null;
	private String _deviceId;
	private int _port;
	protected ServerSocket _serverSocket = null;
	

	
	private static KepServerConnectionThread connectionThread = null;

	/**
	 * 
	 */
	public VirtualTorqueDevice(String deviceId,int port) {
		this._deviceId = deviceId;
		this._port = port;
	}

	/**
	 * 
	 */
	public void run()
	{		
		try
		{
			_fOut = new FileWriter(Utils.LOG_FILES_DIR + "TorqueDevice_" + _deviceId + "_" + _port + ".log", true);
		}
		catch(Exception ex)
		{
			Logger.log(ex.getMessage());
			ex.printStackTrace();
		}
		
		try
		{
			if (_serverSocket == null)
			{
				_serverSocket = new ServerSocket(_port);
				Logger.log("Virtual TorqueDevice " + _deviceId + " started on port " + _port);
				EventBus.publish(new Event(this, EventType.SUCCEEDED));
			}

			// while loop to listen for client connections
			// system will shut down if the client connections exceed 100
			while(_noOfClients <= 100)
			{
				// load a process point from the test database
				// and pass that along for the new server socket connection
				// to execute.  Re-use process points after the list is exhausted.
				connectionThread = new KepServerConnectionThread(_deviceId, _serverSocket.accept(), _fOut);
				connectionThread.start();
				Logger.log("Number of clients currently connected to " + _serverSocket.getLocalPort() + ": " + ++_noOfClients);					
			}
			
			_serverSocket.close();
		}
		catch(IOException ex)
		{
			EventBus.publish(new Event(this, EventType.FAILED));
			Logger.log(ex.getMessage());
			ex.printStackTrace();
		}
		
		
	}

	/**
	 * 
	 * @param device
	 */
	public static boolean activate(String deviceId,int port) 
	{
		try
		{
			// Create a virtual device on a new thread 
			Thread thread = new VirtualTorqueDevice(deviceId,port);
			thread.start();
			return true;
		}
		catch(Exception ex)
		{
			Logger.log(ex.getMessage());
			ex.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param interceptorPort
	 * @param device
	 */
	public static void activate(int interceptorPort, String deviceId, int port) 
	{		
		
		// Create a virtual device on a new thread 
		Thread thread = new VirtualTorqueDevice(deviceId,interceptorPort);
		thread.start();
		
		try
		{
			// start a command prompt shell for the TCP Reflector to intercept the
			// socket communication between Kepware and the Torque simulator
			String command = "cmd /c start " + Utils.CONTROL_FILES_DIR + "TCPReflector_Kepware.bat " + port + " " + interceptorPort;
			Runtime.getRuntime().exec(command);
		}
		catch(Exception ex)
		{
			Logger.log("Interceptor could not be initialized");
			Logger.log(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public static void sendTorque(Torque torque){
		connectionThread.sendTorque(torque);
	}
	public static void forceTorque(Torque torque){
		connectionThread.forceTorque(torque);
	}
	
	
	
}
