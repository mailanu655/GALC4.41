package com.honda.galc.system.config.web.forms;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.entity.enumtype.ConditionType;
import com.honda.galc.entity.enumtype.DestinationType;

/**
 * Form bean for a Struts application.
 * @version 	1.0
 * @author
 */
public class BroadcastDestinationForm extends ActionForm

{
    
	private static final long serialVersionUID = 1L;
	
	private int sequenceNo;
	
	private String processPointID;
	
	private String argument=null;

	private String destinationID=null;
	
	private String requestID=null;
	
	private boolean autoEnabled;
	
	public String operation;

	public DestinationType destinationType;
	
	public int destinationTypeID;
	
		

	//Find flags
	private boolean findAll;
	
	private boolean findInDivision;
	
	private String findRequests = null;
	
	private String checkPoint;
	private String condition;
	private String conditionType;
	
	//Collections	
	private List<LabelValueBean> destinations = Collections.emptyList();
	
	private List<LabelValueBean> requests = Collections.emptyList();
	
	private List<LabelValueBean> arguments = Collections.emptyList();

	private List<CheckPoints> checkPoints = Collections.emptyList();
	
	private List<ConditionType> conditionTypes = Collections.emptyList();
	
	public int getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getProcessPointID() {
		return processPointID;
	}

	public void setProcessPointID(String processPointID) {
		this.processPointID = processPointID;
	}

	public String getArgument() {
		return argument;
	}

	public void setArgument(String argument) {
		this.argument = argument;
	}

	public String getDestinationID() {
		return destinationID;
	}

	public void setDestinationID(String destinationID) {
		this.destinationID = destinationID;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public boolean isAutoEnabled() {
		return autoEnabled;
	}

	public void setAutoEnabled(boolean autoEnabled) {
		this.autoEnabled = autoEnabled;
	}

    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
	
	public DestinationType getDestinationType() {
		return destinationType;
	}
	
	public int getDestinationTypeID() {
		return destinationTypeID;
	}

	public void setDestinationTypeID(int destinationTypeID) {
		this.destinationType = DestinationType.getType(destinationTypeID);
		this.destinationTypeID = destinationTypeID;
	}

	public boolean isFindAll() {
		return findAll;
	}

	public void setFindAll(boolean findAll) {
		this.findAll = findAll;
	}

	public boolean isFindInDivision() {
		return findInDivision;
	}

	public void setFindInDivision(boolean findInDivision) {
		this.findInDivision = findInDivision;
	}

	public String getFindRequests() {
		return findRequests;
	}

	public void setFindRequests(String findRequests) {
		this.findRequests = findRequests;
	}

	public List<LabelValueBean> getDestinations() {
		return destinations;
	}

	public void setDestinations(List<LabelValueBean> destinations) {
		this.destinations = destinations;
	}

	public List<LabelValueBean> getRequests() {
		return requests;
	}

	public void setRequests(List<LabelValueBean> requests) {
		this.requests = requests;
	}

	public List<LabelValueBean> getArguments() {
		return arguments;
	}

	public void setArguments(List<LabelValueBean> arguments) {
		this.arguments = arguments;
	}	
	
	 public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();       
        return errors;
    }
	 
	public String getCheckPoint() {
		return checkPoint;
	}

	public void setCheckPoint(String checkPoint) {
		this.checkPoint = checkPoint;
	}

	public List<CheckPoints> getCheckPoints() {
		return checkPoints;
	}

	public void setCheckPoints(List<CheckPoints> checkPoints) {
		this.checkPoints = checkPoints;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}

	public List<ConditionType> getConditionTypes() {
		return conditionTypes;
	}

	public void setConditionTypes(List<ConditionType> conditionTypes) {
		this.conditionTypes = conditionTypes;
	}	 
}
