package com.honda.galc.fsm;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * <h3>FiniteStateMachine Class description</h3>
 * <p> FiniteStateMachine description </p>
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
 * Feb 24, 2014
 *
 *
 */
@Retention(value=RetentionPolicy.RUNTIME)
@SuppressWarnings("unchecked")
public @interface FiniteStateMachine {    
	Class<? extends IState>[] states();    
	Class<? extends IState> initialState();
	Class<? extends IState> finalState();    
	//Object as final state flags the machine has no final state    Class<?> finalState() default Object.class;}
}