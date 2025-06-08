package com.honda.galc.common.simulation;

import java.awt.Point;
import java.io.Serializable;

import org.apache.commons.lang.math.NumberUtils;


/**
 * 
 * <h3>SimulationEvent Class description</h3>
 * <p> SimulationEvent description </p>
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
 * May 14, 2012
 *
 *
 */
public abstract class RobotGuiEvent implements Serializable, ISimulationEvent {
	
	private static final long serialVersionUID = -7042723194904902342L;

	private String _eventName = "";
	
	private String _window;
	
	private String _dialog;
	
	private String _optionPane;
	
	private String _printComponents;
	
	private String _componentName;
	
	private boolean _hasEnterKey;
	
	private String _mouseButton;
	
	private int _mouseClickDelay;

	private String _parameterValue1 = "";

	private String _parameterValue2 = "";

	private String _parameterValue3 = "";

	public RobotGuiEvent() {
		super();
	}

	public RobotGuiEvent(String name, String componentName, boolean hasEnterKey, 
			String mouseButton, String value1, String value2, String value3) {
		super();
		_eventName = name;
		_componentName = componentName;
		_mouseButton = mouseButton;
		_hasEnterKey = hasEnterKey;
		_parameterValue1 = value1;
		_parameterValue2 = value2;
		_parameterValue3 = value3;
	}

	public String getEventName() {
		return _eventName;
	}

	public void setEventName(String name) {
		_eventName = name;
	}

	public String getParameterValue1() {
		return _parameterValue1;
	}
	
	public Integer getParameterValueInt1() {
		return toInteger(_parameterValue1);
	}
	
	public Boolean getParameterValueBoolean1() {
		return toBoolean(_parameterValue1);
	}
	
	public Integer getParameterValueInt2() {
		return toInteger(_parameterValue2);
	}
	
	public Boolean getParameterValueBoolean2() {
		return toBoolean(_parameterValue2);
	}
	
	public Point getPoint() {
		if(!NumberUtils.isNumber(_parameterValue1) || !NumberUtils.isNumber(_parameterValue2)) return null;
		return new Point(NumberUtils.toInt(_parameterValue1),NumberUtils.toInt(_parameterValue2));
	}
	
	public void setParameterValue1(String value1) {
		_parameterValue1 = value1;
	}

	public String getParameterValue2() {
		return _parameterValue2;
	}

	public void setParameterValue2(String value2) {
		_parameterValue2 = value2;
	}
	
	public String getParameterValue3() {
		return _parameterValue3;
	}

	public void setParameterValue3(String value3) {
		_parameterValue3 = value3;
	}

	public String getComponentName() {
		return _componentName;
	}

	public void setComponentName(String name) {
		_componentName = name;
	}
	
	public boolean hasEnterKey() {
		return _hasEnterKey;
	}
	
	public void setHasEnterKey(boolean hasEnterKey) {
		_hasEnterKey = hasEnterKey;
	}
	
	public String getMouseButton() {
		return _mouseButton;
	}

	public void setMouseButton(String button) {
		_mouseButton = button;
	}
	
	public String getWindow() {
		return _window;
	}

	public void setWindow(String window) {
		this._window = window;
	}

	public String getDialog() {
		return _dialog;
	}

	public void setDialog(String dialog) {
		this._dialog = dialog;
	}

	public String getOptionPane() {
		return _optionPane;
	}

	public void setOptionPane(String pane) {
		_optionPane = pane;
	}

	public String getPrintComponents() {
		return _printComponents;
	}

	public void setPrintComponents(String components) {
		_printComponents = components;
	}
	
	public int getMouseClickDelay() {
		return _mouseClickDelay;
	}

	public void setMouseClickDelay(int mouseClickDelay) {
		_mouseClickDelay = mouseClickDelay;
	}
	
	public Boolean isPrintComponents() {
		Boolean b = toBoolean(_printComponents);
		return b == null ? false : b;
	}
	
	public boolean isContainerComponentDefined() {
		return !isNull(_window) || !isNull(_dialog) || !isNull(_optionPane);
	}

	private Integer toInteger(String param) {
		return NumberUtils.isNumber(param) ? NumberUtils.toInt(param) : null; 
	}
	
	private boolean isNull(String text){
		return text == null;
	}
	
	private Boolean toBoolean(String param) {
		return param != null ? 
		       (param.equalsIgnoreCase("YES") ||
		        param.equalsIgnoreCase("Y") ||
		        param.equalsIgnoreCase("T") ||
		        param.equalsIgnoreCase("TRUE")) : null; 
	}
	
	public abstract void execute(ISimulationProcessor processor);
}

 