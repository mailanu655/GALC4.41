package com.honda.galc.client.teamleader.hold.qsr;

import java.io.Serializable;

import javax.swing.JComboBox;

import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.Division;

public interface QSRDialog extends Serializable {
	
	public JComboBox getReasonInput();
	
	public JComboBox getDepartmentComboBoxComponent();
	
	public JComboBox getLineComboBoxComponent();
	
	public QsrMaintenancePropertyBean getProperty();
	
	public Division getDivision();
	
	public Logger getLogger();

}
