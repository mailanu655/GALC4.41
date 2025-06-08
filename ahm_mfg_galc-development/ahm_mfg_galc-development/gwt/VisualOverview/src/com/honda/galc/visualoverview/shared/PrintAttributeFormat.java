package com.honda.galc.visualoverview.shared;

import java.util.Map;
import java.util.TreeMap;

public class PrintAttributeFormat {
	   
		private PrintAttributeFormatId id;
	    private int offset;
	    private int length;
	    private int attributeTypeId;
	    private String attributeValue;
	    private int sequenceNumber;   
	    private Map<String,String> attributeMethods = new TreeMap<String,String>();
		public PrintAttributeFormatId getId() {
			return id;
		}
		public void setId(PrintAttributeFormatId id) {
			this.id = id;
		}
		public int getOffset() {
			return offset;
		}
		public void setOffset(int offset) {
			this.offset = offset;
		}
		public int getLength() {
			return length;
		}
		public void setLength(int length) {
			this.length = length;
		}
		public int getAttributeTypeId() {
			return attributeTypeId;
		}
		public void setAttributeTypeId(int attributeTypeId) {
			this.attributeTypeId = attributeTypeId;
		}
		public String getAttributeValue() {
			return attributeValue;
		}
		public void setAttributeValue(String attributeValue) {
			this.attributeValue = attributeValue;
		}
		public int getSequenceNumber() {
			return sequenceNumber;
		}
		public void setSequenceNumber(int sequenceNumber) {
			this.sequenceNumber = sequenceNumber;
		}
		public Map<String, String> getAttributeMethods() {
			return attributeMethods;
		}
		public void setAttributeMethods(Map<String, String> attributeMethods) {
			this.attributeMethods = attributeMethods;
		}
	    
	    
}
