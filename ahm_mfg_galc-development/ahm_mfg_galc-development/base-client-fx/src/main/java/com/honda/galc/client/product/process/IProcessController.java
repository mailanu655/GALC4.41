package com.honda.galc.client.product.process;


import com.honda.galc.client.mvc.IController;
import com.honda.galc.client.mvc.IModel;
import com.honda.galc.client.product.mvc.ProductModel;

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
 * @author Jeffray Huang
 */
public interface IProcessController<M extends IModel,T extends IProcessView<?,?>> extends IController<M,T>{

	public enum ProcessType{
		DC,         	 		// Data Collection
		PROCESS_INSTRUCTION, 	// Display process instruction 
		QICS,			 		// QICS Process
		OTHER			 
	};
	
	public enum ProcessState {
		IDLE, READY, PROCESSING, FINISHED ,CANCELLED
	};

	// prepare : start : finish : reset
	// prepare : update : start: finish : reset
	// prepare : start : update : finish : reset
	// prepare : update : start : reset
	// ---------------------------------------------------
	// IDLE->READY : prepare
	// READY->PROCESSING : start
	// READY->PROCESSING : update
	// PROCESSING->PROCESSING : update
	// PROCESSING->FINISHED : finish
	// FINISHED->IDLE : reset
	
	// === conrolling === //
	// Set if this process is required to complete the product at the current proces 
	// point ID.
	public boolean isRequired();

	public boolean isActive();
	
	// === process === //
	public void prepare(ProductModel productModel);

	public void update();
	
	public void start();

	public void finish();

	public void reset();

	// === get/set === //
	public String getProcessName();
	
	public boolean isDataCollection();
	
	public boolean isProcessInstruction();
	
	public boolean isQICS();
	
	public boolean isOtherProcess();
	
	public ProcessType getProcessType();
	
	public ProcessState getState();
	
	public T getView();
	
	public int getMnemonicKey();
}
