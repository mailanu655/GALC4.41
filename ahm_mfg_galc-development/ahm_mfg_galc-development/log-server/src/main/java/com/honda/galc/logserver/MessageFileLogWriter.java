package com.honda.galc.logserver;

import com.honda.galc.common.logging.AbstractFileLogWriter;

/**
 * 
 * <h3>MessageFileLogWriter Class description</h3>
 * <p> MessageFileLogWriter takes log string and saves to a file </p>
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
 * Feb 4, 2010
 *
 */
public class MessageFileLogWriter extends AbstractFileLogWriter<String> {
    
    
    public MessageFileLogWriter(String path, String name) {
        super(path, name);
    }
    
    
    @Override
    public void processItem(String item) {
        super.log(item);
    }
    
    public void log(String item) {
            enqueue(item);
    }


}
