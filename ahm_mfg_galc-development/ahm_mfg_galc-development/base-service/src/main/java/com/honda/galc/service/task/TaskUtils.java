package com.honda.galc.service.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.util.KeyValue;
import com.honda.galc.util.OIFConstants;

/** * * 
* @version 1 
* @author Suriya Sena 
* @since Dec 5, 2018
*/
public class TaskUtils {
	
	public static List<Object> packExtraArgs(List<Object> args,Map<String,String> extraArgs){
		if (extraArgs != null) {
		   for (String key : extraArgs.keySet()) {
			     String value = extraArgs.get(key);
			     args.add(new KeyValue<String, String>(OIFConstants.OIF_XARG + key, value));
		   }
		}
		return args;
	}
	
	public static Map<String,String> unpackExtraArgs(Object [] args) {
		Map<String,String> map = null;
		if(args != null) {
		for (Object o : args ) {
		  if (map == null) {
		     map = new HashMap<String,String>(); 
		  }
		
		  if (o instanceof KeyValue<?,?>) {
			  KeyValue<String, String> keyValue =  (KeyValue<String,String>)o;
			  if (keyValue.getKey().startsWith(OIFConstants.OIF_XARG)) {
		        String normalizedKey = keyValue.getKey();
		        normalizedKey =  normalizedKey.replace(OIFConstants.OIF_XARG,"");
		        map.put(normalizedKey,keyValue.getValue());
			  }
		  	}
		  }
		}
		return map;
	}
}	
