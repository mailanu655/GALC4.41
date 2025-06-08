package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.view.GtsDrawingView;

public class SearchAction extends AbstractAction{
    
    private static final long serialVersionUID = 1L;

    private GtsDrawingView view;
    
    public SearchAction(GtsDrawingView view){
        this.view = view;
        this.putValue(Action.NAME, getName());
    }
    
    private String getName(){
        return "Search...";
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        SearchDialog dialog = new SearchDialog(view);
        dialog.setVisible(true);
        view.getDrawing().highlightCarriers();
    }
}
