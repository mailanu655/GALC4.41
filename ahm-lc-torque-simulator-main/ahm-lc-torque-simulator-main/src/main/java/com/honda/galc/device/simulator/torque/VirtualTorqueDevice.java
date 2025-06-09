package com.honda.galc.device.simulator.torque;

import java.net.*;
import java.io.*;

import com.honda.galc.device.simulator.utils.Logger;
import com.honda.galc.device.simulator.utils.Utils;

/**
 * Simulates the main functions of a Torque Controller
 * 
 * - Starts a server socket and listens for client connections
 * - Supports up to 100 simultaneous clients
 * - Creates a TorqueClientServiceThread for each client that is connnected
 * 
 * @author Subu Kathiresan
 * @date Jan 6, 2015
 */
public class VirtualTorqueDevice extends Thread {
	
	protected int noOfClients = 0;
	
	private FileWriter fOut = null;
	private String deviceId;
	private int port;
	protected ServerSocket serverSocket = null;
	
	private TorqueClientServiceThread torqueClientServiceThread = null;

	public VirtualTorqueDevice(String deviceId, int port) {
		this.deviceId = deviceId;
		this.port = port;
	}

	public void run() {
		try {
			fOut = new FileWriter(Utils.LOG_FILES_DIR + "TorqueDevice_" + deviceId + "_" + port + ".log", true);
		} catch(Exception ex) {
			Logger.log(ex.getMessage());
			ex.printStackTrace();
		}
		
		try {
			if (serverSocket == null) {
				serverSocket = new ServerSocket(port);
				Logger.log("Virtual TorqueDevice " + deviceId + " started on port " + port);
			}

			// while loop to listen for client connections
			// system will shut down if the client connections exceed 100
			while(noOfClients <= 100) {
				// load a process point from the test database
				// and pass that along for the new server socket connection
				// to execute.  Re-use process points after the list is exhausted.
				torqueClientServiceThread = new TorqueClientServiceThread(deviceId, serverSocket.accept(), fOut);
				torqueClientServiceThread.start();
				Logger.log("Number of clients currently connected to " + serverSocket.getLocalPort() + ": " + ++noOfClients);					
			}
			
			serverSocket.close();
		} catch(IOException ex) {
			Logger.log(ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @param device
	 */
	public static boolean activate(String deviceId, int port) {
		try {
			// Create a virtual device on a new thread 
			Thread thread = new VirtualTorqueDevice(deviceId, port);
			thread.start();
			return true;
		} catch(Exception ex) {
			Logger.log(ex.getMessage());
			ex.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * @param interceptorPort
	 * @param device
	 */
	public static void activate(int interceptorPort, String deviceId, int port) {
		// Create a virtual device on a new thread 
		Thread thread = new VirtualTorqueDevice(deviceId, interceptorPort);
		thread.start();
		
		try {
			// start a command prompt shell for the TCP Reflector to intercept the
			// socket communication between Torque client and the Torque simulator
			String command = "cmd /c start " + Utils.CONTROL_FILES_DIR + "TCPReflector_TorqueClient.bat " + port + " " + interceptorPort;
			Runtime.getRuntime().exec(command);
		} catch(Exception ex) {
			Logger.log("Interceptor could not be initialized");
			Logger.log(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public void sendTorque(Torque torque){
		torqueClientServiceThread.sendTorque(torque);
	}

	public void forceTorque(Torque torque){
		torqueClientServiceThread.forceTorque(torque);
	}
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
