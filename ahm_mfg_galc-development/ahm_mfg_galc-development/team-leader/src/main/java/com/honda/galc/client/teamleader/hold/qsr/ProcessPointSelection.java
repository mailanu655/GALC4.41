package com.honda.galc.client.teamleader.hold.qsr;

import java.io.Serializable;

import javax.swing.JPanel;

import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.enumtype.QCAction;

public interface ProcessPointSelection extends Serializable{
	
	public MainWindow getMainWindow();
	
	public JPanel getParentPanel();
		
	public Division getDivision();
	
	public QCAction getQCAction();
	
	public QsrMaintenancePropertyBean getProperty();
		
}
