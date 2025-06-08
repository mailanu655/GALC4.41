package com.honda.galc.dto.lcvinbom;

import com.honda.galc.dto.IDto;

public class PartsDto implements IDto {
	
	private static final long serialVersionUID = 1L;

		private String systemName;
		private String partNumber;
		
		public PartsDto(String letSystemName, String dcPartNumber){
			this.systemName = letSystemName;
			this.partNumber = dcPartNumber;
		}

		public String getSystemName() {
			return systemName;
		}

		public void setSystemName(String letSystemName) {
			this.systemName = letSystemName;
		}

		public String getPartNumber() {
			return partNumber;
		}

		public void setPartNumber(String dcPartNumber) {
			this.partNumber = dcPartNumber;
		}

		@Override
		public String toString() {
			return "Parts [systemName=" + systemName + ", partNumber=" + partNumber + "]";
		}
		
}
