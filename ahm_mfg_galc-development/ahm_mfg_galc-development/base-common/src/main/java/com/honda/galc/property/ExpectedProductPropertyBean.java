package com.honda.galc.property;


/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductPropertyBean</code> is ... .
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
@PropertyBean(componentId ="DEFAULT_EXPECTED_PRODUCT")
public interface ExpectedProductPropertyBean extends IProperty {

	/**
	 * Flag indicates next expected product id is display and checked 
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isCheckExpectedProductId();
	
	/**
	 * Indicates if auto processing of products is turned on
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isAutoProcessExpectedProduct();
	
	/**
	 * Flag indicates AF ON SEQ NO exist and checked
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isAfOnSeqNumExist();
	
	/**
	 * Flag indicates If check product Id is already process when validate
	 * product id
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isCheckProcessedProduct();
	
	/**
	 * The IExpectedProductManager implementation for JavaFx data collection clients.
	 */
	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.product.ExpectedProductManager")
	String getExpectedProductManager();
	
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isExpectedProductSequenceResetAllowed(); 
	
	/**
	  * Flag indicate if save next product id into the expected product table; 
	  * false means current product is saved into the expected product table
	  *  
	  * @return
	  */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isSaveNextProductAsExpectedProduct();
	
	/**
	 * Save the last passing product id into expected product table to provide
	 * data for line side monitor.
	 * Notes, this attribute should NOT turn on if next expected product is enabled.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isSaveLastProduct();
	
	/**
	 * Get Process Point Id entering product sequence
	 * @return
	 */
	public String getInProductSequenceId();
	
	/**
	 * Get Process Point Id Out product sequence
	 * @return
	 */
	public String getOutProductSequenceId();
	
	/**
	 * Show the Product Sequence on Input Pane
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isShowProductSequence();
	
	/**
	 * Get Next Product Id from GAL216TBX table based on Stamping_Sequence column
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isNextProdByStampSeq();
	
	/**
	 * Update SEND_STATUS in GAL216TBX table
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isUpdateSendStatus();
	
	/**
	 * Show the Product Bypass button on Input Pane
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isShowProductBypass();
}
