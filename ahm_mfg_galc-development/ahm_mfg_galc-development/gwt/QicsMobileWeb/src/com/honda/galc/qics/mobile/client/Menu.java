/**
 * 
 */
package com.honda.galc.qics.mobile.client;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.allen_sauer.gwt.log.client.Log;
import com.honda.galc.qics.mobile.client.widgets.RepairDepartmentEditorPanel;
import com.smartgwt.mobile.client.core.Function;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.types.NavigationMode;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.tableview.TableView;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickEvent;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickHandler;

/**
 * The Class Menu provides the launch point for auxiliary functionality, eg: About, 
 * Set Process Point, etc.
 */
public class Menu extends NavStack {

    private String titles[] = {
    		"About",
            "Process Point",
            "Repair Department",
            "Log Messages"
    };
    private List<Function<Panel>> functions = new ArrayList<Function<Panel>>();
    private Map<String, Function<Panel>> map = new HashMap<String,Function<Panel>>();
    private static Record createRecord(String id, String title) {
        Record record = new Record();
        record.setAttribute("_id", id);
        record.setAttribute("title", title);
        return record;
    }
    
    final NavStack navStack = this;

    @Override
    protected void create(String title, NavStack nav) {
        Function<Panel> function = map.get(title);
        if(function != null) {
            Panel panel = function.execute();
            if(panel != null) {
                nav.push(map.get(title).execute());
            } else {
            	Log.warn("Opps ... Panel was null");
            }
        } else {
        	Log.warn("Opps ... Function was null");
        }
    }

    public Menu(String title ) {
        super(title);
        ScrollablePanel menu = new ScrollablePanel("Menu");
        final TableView menuTable = new TableView();
        RecordList recordList = new RecordList();
        
        functions.add(new Function<Panel>(){
        	public Panel execute(){return new AboutPanel();}});
             
        functions.add(new Function<Panel>(){
         	public Panel execute(){return new ProcessPointEditorPanel();}});

        functions.add(new Function<Panel>(){
         	public Panel execute(){return new RepairDepartmentEditorPanel();}});

        functions.add(new Function<Panel>(){
         	public Panel execute(){return new LogViewPanel(navStack);}});

        
        for(int i = 0; i < titles.length; ++i) {
            recordList.add(createRecord(new Integer(i).toString(),titles[i]));
            map.put(titles[i], functions.get(i));
        }
        menuTable.setTitleField("title");
        menuTable.setShowNavigation(true);
        menuTable.setSelectionType(SelectionStyle.SINGLE);
        menuTable.setNavigationMode(NavigationMode.WHOLE_RECORD);
        menuTable.setParentNavStack(Menu.this);
        menuTable.setTableMode(TableMode.GROUPED);
        menuTable.setData(recordList);
        menuTable.addRecordNavigationClickHandler(new RecordNavigationClickHandler() {
            @Override
            public void onRecordNavigationClick(RecordNavigationClickEvent event) {
                final Record selectedRecord = event.getRecord();
                String title = selectedRecord.getAttribute("title");
                create(title, Menu.this);
            }
        });
        menu.addMember( menuTable);
        push( menu );
      
    }
}