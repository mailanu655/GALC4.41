package com.honda.galc.dto;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * <h3>DtoFactory Class description</h3>
 * <p> DtoFactory description </p>
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
public class DtoFactory {
	
	/**
	 * allows to create DtoProxy or Pojo Dto object
	 * @param <T>
	 * @param dtoClass
	 * @param objects
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  static <T> T getDto(Class<T> dtoClass,Map objects) {
		if (!dtoClass.isInterface())
			return (T)DtoUtil.createDto(dtoClass,objects);
		else 
			return (T) Proxy.newProxyInstance(dtoClass.getClassLoader(), new Class[] {dtoClass}, 
				new DtoProxy<T>(dtoClass,objects));
	}
	
	public static <T> List<T> getDtoList(Class<T> dtoClass, List<Map<String,Object>> objectList){
		List<T> dtoList = new ArrayList<T>();
		for(Map<String,Object> objects :objectList){
			dtoList.add(DtoFactory.getDto(dtoClass, objects));
		}
		return dtoList;
	}
	
}

