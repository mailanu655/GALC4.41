package com.honda.galc.client.product.process.controller;

import java.awt.Component;

import com.honda.galc.entity.product.BaseProduct;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProcessController</code> is ... .
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
public interface ProcessController<T extends Component> {

	public enum State {
		IDLE, READY, PROCESSING, FINISHED, ALREADY_PROCESSED, NOT_PROCESSABLE, TRANSITIONING
	};

	// prepare : start : finish : reset
	// prepare : update : start: finish : reset
	// prepare : start : update : finish : reset
	// prepare : update : start : reset
	// prepare : alreadyProcessed : reset
	// prepare : notProcessable : reset
	// ---------------------------------------------------
	// IDLE->READY : prepare
	// READY->PROCESSING : start
	// READY->PROCESSING : update
	// PROCESSING->PROCESSING : update
	// PROCESSING->FINISHED : finish
	// FINISHED->IDLE : reset
	// READY->ALREADY_PROCESSED : start/alreadyProcessed
	// READY->NOT_PROCESSABLE : start/notProcessable
	// ALREADY_PROCESSED->IDLE : reset
	// NOT_PROCESSABLE->IDLE : reset

	// === conrolling === //
	public boolean isRequired();

	public boolean isActive();

	// === process === //
	public void prepare(BaseProduct product);

	public void update();

	public void start();

	public void finish();

	public void reset();

	public void alreadyProcessed();

	public void notProcessable();

	// === get/set === //
	public String getProcessName();
	public State getState();
	public T getView();
	public int getMnemonicKey();
}
