package com.honda.galc.client.product.process.engine.bearing.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.product.process.model.ProcessModel;
import com.honda.galc.property.BearingPropertyBean;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingModel</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class BearingModel extends ProcessModel {

	private BearingPropertyBean property;

	public BearingModel(ApplicationContext applicationContext) {
		super(applicationContext);
	}

	// === config api === //
	public int getMainBearingCount() {
		int defaultCount = getProperty().getMainBearingCount();
		int count = defaultCount;
		return count;
	}

	public int getConrodCount() {
		int defaultCount = getProperty().getConrodCount();
		int count = defaultCount;
		return count;
	}

	public List<Integer> getMainBearingIxSequence() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < getMainBearingCount(); i++) {
			list.add(i + 1);
		}
		return list;
	}

	public List<Integer> getConrodIxSequence() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < getConrodCount(); i++) {
			list.add(i + 1);
		}
		return list;
	}

	// === get/set === //
	protected BearingPropertyBean getProperty() {
		return property;
	}

	protected void setProperty(BearingPropertyBean property) {
		this.property = property;
	}
}
