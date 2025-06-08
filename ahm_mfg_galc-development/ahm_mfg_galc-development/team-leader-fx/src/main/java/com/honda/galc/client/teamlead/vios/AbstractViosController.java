package com.honda.galc.client.teamlead.vios;

import com.honda.galc.client.mvc.AbstractController;
/**
 * <h3>AbstractViosController Class description</h3>
 * <p>
 * Abstract class for Vios Controller
 * </p>
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
 * @author Hemant Kumar<br>
 *        Aug 28, 2018
 */
public abstract class AbstractViosController<M extends AbstractViosModel, V extends AbstractViosTabbedView<?, ?>> extends AbstractController<M, V> {

	public AbstractViosController(M model, V view) {
		super(model, view);
	}

}
