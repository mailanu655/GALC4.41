package com.honda.galc.qics.mobile.client;

import java.util.List;

import com.honda.galc.qics.mobile.client.events.CreateDefectRequestEvent;
import com.honda.galc.qics.mobile.client.events.PubSubBus;
import com.honda.galc.qics.mobile.client.tables.VinIssuesTable;
import com.honda.galc.qics.mobile.client.widgets.VinInfoPanel;
import com.honda.galc.qics.mobile.shared.entity.DefectResult;
import com.honda.galc.qics.mobile.shared.entity.DefectStatus;
import com.smartgwt.mobile.client.data.SortSpecifier;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.types.SortDirection;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.layout.VLayout;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton;

/**
 * VinIssueListPanel displays a selectable and filterable list of Vin Defects or Issues.  Selecting
 * From this panel a user may initiate: Edit an open issue, View a closed issue or
 * Create a new issue.
 */
public class VinIssueListPanel extends ScrollablePanel {

	
	
    final ToolStripButton createDefectButton = new ToolStripButton("Create New Issue");
    final ToolStripButton showOpenDefectsButton = new ToolStripButton("Show Open");
    final ToolStripButton showClosedDefectsButton = new ToolStripButton("Show Closed");
    final ToolStripButton showAllDefectsButton = new ToolStripButton("Show All");

	
	private VinIssuesTable vinIssuesTable; 
	
    public VinIssueListPanel( VinDefectsModel vinDefectsModel, List<DefectResult> defectResultList ) {
		super("Issues List");
		
		VLayout layout = new VLayout();
		// This panel contains Vin Info and a list of issues.
        layout.setWidth("100%");
        layout.setAlign(Alignment.LEFT);
 		VinInfoPanel vinInfoPanel = new VinInfoPanel("Vin Info", vinDefectsModel );


        vinIssuesTable = new VinIssuesTable(defectResultList, getFilterSetting() );

	
        layout.addMember(vinInfoPanel);
        layout.addMember( buildToolStrip() );
        layout.addMember( vinIssuesTable );
        addMember( layout );


        // do this to enable/disable the proper buttons
		setFilter(Settings.getDefectListFilter());

 	}
    
    private Integer getFilterSetting() {
    	String s = Settings.getDefectListFilter();
    	if ( s.equals( DefectStatus.OUTSTANDING.getName() )) {
    		return DefectStatus.OUTSTANDING.getId();
    	} else if ( s.equals( DefectStatus.REPAIRED.getName() )) {
    		return DefectStatus.REPAIRED.getId();
    	} else {
    		return null;
    	}
    }

    
    /**
     * This method remembers the filter selection, enables/disables buttons and
     * actually sets the table's filter criteria.
     * 
     * @param value a Settings filter list value.
     */
    private void setFilter( String value ) {
    	   showOpenDefectsButton.enable();
    	   showClosedDefectsButton.enable();
    	   showAllDefectsButton.enable();
   	
    	   if ( Settings.DEFECT_FILTER_VALUE_ALL.equals( value ) ) {
    		   showAllDefectsButton.disable();
    		   vinIssuesTable.setShowDefectStatus(null);
    	   } else if ( Settings.DEFECT_FILTER_VALUE_OPEN.equals( value ) ) {
    		   showOpenDefectsButton.disable();
    		   vinIssuesTable.setShowDefectStatus(DefectStatus.OUTSTANDING.getId());
    	   } else if ( Settings.DEFECT_FILTER_VALUE_CLOSED.equals( value ) ) {
    		   showClosedDefectsButton.disable();
    		   vinIssuesTable.setShowDefectStatus(DefectStatus.REPAIRED.getId());
    	   } 
           setSort();
    	   // remember the filter setting
    	   Settings.setDefectListFilter( value );
    	
    }
    
    private void setSort() {
    	SortSpecifier sortSpecifier = new SortSpecifier("description_ID", SortDirection.DESCENDING);
    	vinIssuesTable.setSort(sortSpecifier);
    }
    
    private ToolStrip buildToolStrip() {
        ToolStrip toolbar = new ToolStrip();
        
        createDefectButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            	// Request the creation of a new defect
            	PubSubBus.EVENT_BUS.fireEvent(new CreateDefectRequestEvent());
            }
        });
        
        showOpenDefectsButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            	setFilter( Settings.DEFECT_FILTER_VALUE_OPEN);
            	
            }
        });

        showClosedDefectsButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            	setFilter( Settings.DEFECT_FILTER_VALUE_CLOSED);
            }
        });

        showAllDefectsButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            	setFilter( Settings.DEFECT_FILTER_VALUE_ALL);
            }
        });

        toolbar.addButton(showAllDefectsButton);
        toolbar.addButton(showOpenDefectsButton);
        toolbar.addButton(showClosedDefectsButton);
        toolbar.addSpacer();
        toolbar.addButton(createDefectButton);
        
        return toolbar;
 
    }

    



 
	
}