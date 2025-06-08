package com.honda.galc.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 
 * <h3>DtoTag Class description</h3>
 * <p> DtoTag description </p>
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
 * Feb 12, 2015
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD})
public @interface DtoTag {
	
	public String DEF_UNNASSIGNED = "DEF_UNNASSIGNED";

	DtoType type() default DtoType.IN_OUT;
	
	/**
	 * a common mapping name for all DtoTypes.
	 * DtoType.IN - for input name
	 * DtoType.OUT- for output name
	 * DtoType.IN_OUT for in and out name
	 * When this is DEF_UNNASSIGNED, use underline field name
	 * @return
	 */
	String name() default DEF_UNNASSIGNED;
	
	/**
	 * output name - used to map to output formats
	 * When DtoType is either OUT or IN_OUT
	 * use this if a different name other than name() is needed 
	 * @return
	 */
	String outputName() default DEF_UNNASSIGNED;
	
	boolean optional() default true;
}
