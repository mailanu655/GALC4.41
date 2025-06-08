package com.honda.galc.dto;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
/**
 * 
 * 
 * <h3>DtoProxy Class description</h3>
 * <p> DtoProxy description </p>
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
public class DtoProxy<T> implements InvocationHandler,Serializable{
	
	private static final long serialVersionUID = -2321539752519247579L;

	public Class<T> dtoClass;
	
	public Map<String,Object> objects;
	
	
	public DtoProxy(Class<T> dtoClass,Map<String,Object> objects){
		this.dtoClass = dtoClass;
		this.objects = objects;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		String name = DtoUtil.findInputName(method);
		return DtoUtil.convert(objects.get(name), method.getReturnType());
	}

}
