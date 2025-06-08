package com.honda.galc.qics.mobile.client.tables;

import java.util.List;

import com.honda.galc.qics.mobile.client.events.DefectSelectedEvent;
import com.honda.galc.qics.mobile.client.events.PubSubBus;
import com.honda.galc.qics.mobile.client.utils.PhGap;
import com.honda.galc.qics.mobile.client.widgets.images.ImageResources;
import com.honda.galc.qics.mobile.shared.entity.DefectRepairResult;
import com.honda.galc.qics.mobile.shared.entity.DefectResult;
import com.honda.galc.qics.mobile.shared.entity.DefectStatus;
import com.smartgwt.mobile.client.data.DataSourceField;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.types.NavigationMode;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.widgets.grid.CellFormatter;
import com.smartgwt.mobile.client.widgets.grid.ListGridField;
import com.smartgwt.mobile.client.widgets.tableview.TableView;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickEvent;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickHandler;


/**
 * VinIssuesTable displays a table where each row is a "defect".  An
 * DefectSelectedEvent is fired when the row is selected.
 *  
 * @author vfc01346
 *
 */
public class VinIssuesTable extends TableView {
	
	

	   private static final DataSourceField 
	   	PART_GROUP_FIELD = new DataSourceField("partGroup", "Part Group"),
	    PART_FIELD = new DataSourceField("part", "Part"),
	    LOCATION_FIELD = new DataSourceField("location", "Location"),
	    DEFECT_FIELD = new DataSourceField("defect", "Defect"),
	    STATUS_FIELD = new DataSourceField("status", "Status"),
	    ID_FIELD = new DataSourceField("_id"),
	    ICON_FIELD = new DataSourceField("_id");
	   
       private static final ListGridField FULL_FIELD = new ListGridField("-fullField");
       
		static {
	    	PART_GROUP_FIELD.setType("string");
	    	PART_FIELD.setType("string");
	    	LOCATION_FIELD.setType("string");
	    	DEFECT_FIELD.setType("string");
	    	STATUS_FIELD.setType("string");
	    	ID_FIELD.setType("string");
	    	ICON_FIELD.setType("string");
	    	
	        FULL_FIELD.setCellFormatter(new CellFormatter() {
	            @Override
	            public String format(Object value, Record record, int rowNum, int fieldNum) {
	            	final String defectType = record.getAttribute("defectTypeName");
	            	final String inspectionPartName = record.getAttribute("inspectionPartName");
	            	final String inspectionPartLocation = record.getAttribute("inspectionPartLocationName");
	                return "<span style='font-weight:normal'> <b>" + defectType +" </b>" + inspectionPartLocation  + "<b>, " + inspectionPartName + "</b></span>";
	            }
	        });
	    }
		
		private Integer showDefectStatus = null;
		private List<DefectResult> defectResultList = null;
	    
	    public 	VinIssuesTable(List<DefectResult> defectResultList, Integer showDefectStatus ) {

	    	this.showDefectStatus = showDefectStatus;
	    	this.defectResultList = defectResultList;
	    	
    	    setTitleField(FULL_FIELD.getName());
    	   
	    	setAlign(Alignment.CENTER);

	    	setFields( FULL_FIELD );
		    	
	   
	        setNavigationMode(NavigationMode.WHOLE_RECORD);
	    	setSelectionType(SelectionStyle.NONE);
	    	setShowNavigation(true);
	    	setShowIcons(true);
	    	
    	
	        addRecordNavigationClickHandler(new RecordNavigationClickHandler() {
	            @Override
	            public void onRecordNavigationClick(RecordNavigationClickEvent event) {
	            	if ( event != null && event.getRecord() != null ) {
		            	PhGap.doTactalFeedback();
		                PubSubBus.EVENT_BUS.fireEvent(new DefectSelectedEvent( event.getRecord() ));
	            	}
	            }
	        });
	        
	        populate( defectResultList );
	      
	    }
	    

	    private void populate(List<DefectResult> defects ) {
	    	RecordList recordList = buildRecordList(defects );
	    	this.setData(recordList);
	    	this. _refreshRows();
	    }
	    
	    private RecordList buildRecordList( List<DefectResult> defectList ) {
	    	RecordList recordList = new RecordList();
	    	if ( defectList != null ) {
		    	for( DefectResult defect : defectList ) {
		    		
		    		// apply filter here, null means display all, otherwise display matching
		    		if ( defect != null && ( this.showDefectStatus == null ||  this.showDefectStatus.equals(defect.getDefectStatus() ))) {
			    		Record r = new Record();
			    		r.setAttribute("defectTypeName", defect.getId().getDefectTypeName());
			    		r.setAttribute("inspectionPartName", defect.getId().getInspectionPartName());
			    		r.setAttribute("inspectionPartLocationName", defect.getId().getInspectionPartLocationName());
						r.setAttribute("id", defect.getId().getDefectResultId());
						r.setAttribute("twoPartPairPart", defect.getId().getTwoPartPairPart());
						r.setAttribute("twoPartPairLocation", defect.getId().getTwoPartPairLocation());
						
			    		if ( defect.getDefectStatus() == DefectStatus.OUTSTANDING.getId()) {
							r.setAttribute("status", "Open");
							r.setAttribute("icon", ImageResources.INSTANCE.attention());
						} else {
							r.setAttribute("status", "Closed");
							r.setAttribute("icon", ImageResources.INSTANCE.check());
						}
			    		
			    		// Add Most recent repair result fields
			    		if ( defect.getDefectRepairResults() != null && defect.getDefectRepairResults().size() > 0 ) {
			    			int i = defect.getDefectRepairResults().size() - 1; 
			    			DefectRepairResult rr = defect.getDefectRepairResults().get(i);
			    			r.setAttribute("actualProblemName", rr.getActualProblemName());
			    			r.setAttribute("actualTimestamp", rr.getActualTimestamp());
			    			r.setAttribute("comment", rr.getComment());
			    			r.setAttribute("repairAssociateNo", rr.getRepairAssociateNo());
			    			r.setAttribute("repairDept", rr.getRepairDept());
			    			r.setAttribute("repairMethodName", rr.getRepairMethodName());
			    			r.setAttribute("repairProcessPointId", rr.getRepairProcessPointId());
			    			r.setAttribute("repairTime", rr.getRepairTime());
			    		}
						recordList.add(r);
		    		}
					
				}
	    	}
	    	return recordList;
	    }


		public Integer getShowDefectStatus() {
			return showDefectStatus;
		}


		public void setShowDefectStatus(Integer showDefectStatus) {
			this.showDefectStatus = showDefectStatus;
			// referesh the table
			this.populate( this.defectResultList );
			
		}
}