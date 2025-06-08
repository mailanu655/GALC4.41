package com.honda.galc.data;

import javax.persistence.Transient;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * 
 * <h3>HiddenTransitionExclusionStrategy Class description</h3>
 * <p> HiddenTransitionExclusionStrategy description </p>
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
 * @author Paul Chou<br>
 * Oct.6, 2020
 *
 *
 */
public class HiddenTransitionExclusionStrategy implements ExclusionStrategy{
	public boolean shouldSkipClass(Class<?> clazz) {
		return clazz.getAnnotation(Transient.class) != null;
	}

	public boolean shouldSkipField(FieldAttributes f) {
		return f.getAnnotation(Transient.class) != null;
	}

}
