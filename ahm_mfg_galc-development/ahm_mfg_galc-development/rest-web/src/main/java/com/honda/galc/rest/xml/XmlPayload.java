package com.honda.galc.rest.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author Subu Kathiresan
 * @date Feb 19, 2015
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused"})
public class XmlPayload {
	
	private static final long serialVersionUID = 5009045690762523314L;
	
	@XStreamImplicit
	private List restParams = new ArrayList();
	
	public XmlPayload() {}

	public List getRestParams() {
		return restParams;
	}

	public void setRestParams(ArrayList<Object> restParams) {
		this.restParams = restParams;
	}
	
	public void addParam(Object obj) {
		getRestParams().add(obj);
	}
	
	public void removeParam(Object obj) {
		if (getRestParams().contains(obj)) {
			getRestParams().remove(obj);
		}
	}
}
