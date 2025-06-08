package com.honda.galc.service.broadcast;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class JAXBContextSingletonHelper {
	private static JAXBContext instance=null;
	protected JAXBContextSingletonHelper(){	
	}
	public static synchronized JAXBContext getSciemetricContextInstance(String link)throws JAXBException{

		if(instance==null){
			instance=JAXBContext.newInstance(link);
		}
		return instance;
	}
	
}
