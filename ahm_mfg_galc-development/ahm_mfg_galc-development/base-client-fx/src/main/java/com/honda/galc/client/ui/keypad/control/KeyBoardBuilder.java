package com.honda.galc.client.ui.keypad.control;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.util.Builder;

import com.honda.galc.client.ui.keypad.robot.IRobot;

public class KeyBoardBuilder implements Builder<KeyboardPane> {

	private Path layerPath;
	private Locale initLocale;
	private List<IRobot> iRobots = new ArrayList<IRobot>();
	private double initScale = 0.0;
	private String initLayout = "default";

	protected KeyBoardBuilder() {
	}

	public static KeyBoardBuilder create() {
		return new KeyBoardBuilder();
	}

	public KeyBoardBuilder layerPath(Path path) {
		layerPath = path;
		return this;
	}

	public KeyBoardBuilder initLocale(Locale locale) {
		initLocale = locale;
		return this;
	}

	public KeyBoardBuilder initScale(double scale) {
		initScale  = scale;
		return this;
	}
	
	public KeyBoardBuilder addIRobot(IRobot robot) {
		iRobots.add(robot);
		return this;
	}
	
	public KeyBoardBuilder initLayout(String layout) {
		initLayout  = layout;
		return this;
	}

	public KeyboardPane build() {
		KeyboardPane keyBoard = new KeyboardPane(layerPath, initLocale, initLayout);
		if (initScale > 0.0){
			keyBoard.setScale(initScale);
		}
		for (IRobot robot : iRobots) {
			keyBoard.addRobotHandler(robot);
		}
		return keyBoard;
	}

}
