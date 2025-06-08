package com.honda.galc.client.datacollection.fsm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>Transition</code> is the interface defined for Data Collection 
 * state machine transition 
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>Paul Chou</TD>
 * <TD>Sep 10, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Paul Chou
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transition {  

	FsmType type() default FsmType.DEFAULT;

	Class<?> state();
	//Guarded condition for next state
	String condition() default "N/A";
	String init() default "init";

}
