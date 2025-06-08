package com.honda.galc.client.gts.view;

import java.util.Collection;
import java.util.LinkedList;

import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.ToolEvent;

/**
 * 
 * 
 * <h3>GtsDrawingEditor Class description</h3>
 * <p> GtsDrawingEditor description </p>
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
 * Jul 9, 2015
 *
 *
 */
public class GtsDrawingEditor extends DefaultDrawingEditor{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void toolDone(ToolEvent evt) {
       super.toolDone(evt);  
       Collection<Figure> draggedFigures = new LinkedList<Figure>(getActiveView().getSelectedFigures());
       
    }    
}
