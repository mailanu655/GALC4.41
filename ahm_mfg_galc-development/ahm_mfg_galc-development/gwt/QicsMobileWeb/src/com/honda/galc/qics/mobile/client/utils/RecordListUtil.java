package com.honda.galc.qics.mobile.client.utils;

import java.util.List;

import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;

public class RecordListUtil {

	public static RecordList buildRecordList( List<String> list, String attributeName ) {
	   	RecordList recordList = new RecordList();
	   	if ( list != null ) {
	    	for( String s : list ) {
		    	if ( s != null ) {
		    		Record r = new Record();
		    		r.setAttribute(attributeName, s);
		  			recordList.add(r);
	    		}
			}
	   	}
    	return recordList;
	}
}
