/**
 * 
 */
package com.honda.galc.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


/**
 * @author Cody Getz
 * @Date May 15, 2013
 *
 */
public class LineOverview implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String lineName = "";
	private String lineId = "";
	private String divisionName = "";
	private String divisionId = "";
	private String nextLineName = "";
	private Date _lastUpdated = new Date(0);
	private ArrayList<LineDetail> _lineDetails = new ArrayList<LineDetail>();
	
	public LineOverview(String lineId)
	{
		this.lineId = lineId;
	}
	
	public String getLineName() {
		return lineName;
	}
	
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	
	public String getLineId() {
		return lineId;
	}
	
	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	
	public String getDivisionName() {
		return divisionName;
	}
	
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	
	public String getDivisionId() {
		return divisionId;
	}
	
	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}
	
	public String getNextLineName() {
		return nextLineName;
	}

	public void setNextLineName(String nextLineName) {
		this.nextLineName = nextLineName;
	}
	
	public void setLineDetails(ArrayList<LineDetail> lineDetails)
	{
		_lineDetails = lineDetails;
	}
	
	public ArrayList<LineDetail> getLineDetails()
	{
		return _lineDetails;
	}
	
	public void setLastUpdated(Date lastUpdated) {
		_lastUpdated = lastUpdated;
	}
	
	public Date getLastUpdated() {
		return _lastUpdated;
	}
	

}
