package com.honda.galc.qics.mobile.client;

import java.util.List;

import org.gwt.beansbinding.core.client.util.AbstractBean;

import com.honda.galc.qics.mobile.shared.entity.DefectResult;
import com.smartgwt.mobile.client.data.Record;

/**
 * This class maintains the data for the Defects functionality.  It supports
 * property change event notifications.  This class is the "View-Model" for the
 * defects portion of the application.
 * 
 * @author vfc01346
 *
 */
public class VinDefectsModel extends AbstractBean {

	private String vin;
	private String mto;
	private String productionLot;
	private String processPoint;
	private String partGroup;
	private String part;
	private String partLocation;
	private String partDefect;
	private String otherPart;

	private Integer selectedDefectResultId;
	
		
	private Record defectRecord;
	
	/**
	 * This is the DefectResult received from the web service
	 * 
	 */
	private List<DefectResult> defectResultList;
	public List<DefectResult> getDefectResultList() {
		return defectResultList;
	}
	public void setDefectResultList(List<DefectResult> defectResultList) {
		this.defectResultList = defectResultList;
	}
	public Integer getSelectedDefectResultId() {
		return selectedDefectResultId;
	}
	public void setSelectedDefectResultId(Integer selectedDefectResultId) {
		this.selectedDefectResultId = selectedDefectResultId;
	}

	public DefectResult getSelectedDefectResult() {
		DefectResult defectResult = null;
		if ( this.selectedDefectResultId != null && this.defectResultList != null) {
			for( DefectResult dr : this.defectResultList ) {
				if ( dr.getId().getDefectResultId().equals( this.selectedDefectResultId )) {
					defectResult = dr;
					break;
				}
			}
		}
		return defectResult;
	}

	

	
	public String getPartDefect() {
		return partDefect;
	}
	public void setPartDefect(String partDefect) {
		String oldValue = this.partDefect;
		this.partDefect = partDefect;
		firePropertyChange("partDefect", oldValue, this.partDefect);

	}
	
	public String getOtherPart() {
		return otherPart;
	}
	public void setOtherPart(String otherPart) {
		String oldValue = this.otherPart;
		this.otherPart = otherPart;
		firePropertyChange("partDefectOtherPart", oldValue, this.otherPart);
	}

	public Record getDefectRecord() {
		return defectRecord;
	}
	public void setDefectRecord(Record defectRecord) {
		Record oldValue = this.getDefectRecord();
		this.defectRecord = defectRecord;
		firePropertyChange("defectRecord", oldValue, this.defectRecord);		
	}
	
	public String getProcessPoint() {
		return processPoint;
	}
	public void setProcessPoint(String processPoint) {
		String oldValue = this.processPoint;
		this.processPoint = processPoint;
		firePropertyChange("processPoint", oldValue, this.processPoint);
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		String oldValue = this.vin;
		this.vin = vin;
		firePropertyChange("vin", oldValue, this.vin);
	}
	
	public String getMto() {
		return mto;
	}
	public void setMto(String mto) {
		String oldValue = this.mto;
		this.mto = mto;
		firePropertyChange("mto", oldValue, this.mto);
	}
	
	public String getProductionLot() {
		return productionLot;
	}
	
	public void setProductionLot(String productionLot) {
		String oldValue = this.productionLot;
		this.productionLot = productionLot;
		firePropertyChange("productionLot", oldValue, this.productionLot);
	}
	
	
	public String getPartGroup() {
		return this.partGroup;
	}
	public void setPartGroup(String partGroup ) {
		String oldValue = this.getPartGroup();
		this.partGroup = partGroup;
		firePropertyChange("partGroup", oldValue, this.partGroup);		
	}
	
	public String getPart() {
		return this.part;
	}
	public void setPart(String part ) {
		String oldValue = this.getPart();
		this.part = part;
		firePropertyChange("part", oldValue, this.part);		
	}
	
	public String getPartLocation() {
		return this.partLocation;
	}
	public void setPartLocation(String partLocation ) {
		String oldValue = this.getPartLocation();
		this.partLocation = partLocation;
		firePropertyChange("partLocation", oldValue, this.partLocation);		
	}

	
	
	public String getModelCode() {
		String modelCode = null;
		if ( this.mto != null ) {
			return mto.substring(1,4);
		}
		return modelCode;
	}
}
