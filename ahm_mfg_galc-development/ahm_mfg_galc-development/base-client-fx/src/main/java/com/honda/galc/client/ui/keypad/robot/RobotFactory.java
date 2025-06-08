package com.honda.galc.client.ui.keypad.robot;

public class RobotFactory {

	public static IRobot createFXRobot(){
		return new FXRobotHandler();
	}
	
	public static IRobot createAWTRobot(){
		return new AWTRobotHandler();
	}
	
	public static IRobot createNativeAsciiRobot(){
		return new NativeAsciiRobotHandler();
	}
}
