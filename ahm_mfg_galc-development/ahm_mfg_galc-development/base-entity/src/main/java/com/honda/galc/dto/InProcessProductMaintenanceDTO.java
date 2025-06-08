package com.honda.galc.dto;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Jan 04, 2016
 */
public  class InProcessProductMaintenanceDTO implements IDto{
	
	    private static final long serialVersionUID = 1L;
	
	    @DtoTag(outputName ="LINE_NAME")
        private  String lineName;
	    
	    @DtoTag(outputName ="LINE_ID")
        private  String lineId;
	    
	    @DtoTag(outputName ="PRODUCT_ID")
        private  String productId;
	    
	    @DtoTag(outputName ="NEXT_PRODUCT_ID")
        private  String nextProductId;
	    
		@DtoTag(outputName ="AF_ON_SEQUENCE_NUMBER")
        private  Integer afOnSequenceNumber;
        
		public Integer getAfOnSequenceNumber() {
			return afOnSequenceNumber;
		}

		public void setAfOnSequenceNumber(Integer afOnSequenceNumber) {
			this.afOnSequenceNumber = afOnSequenceNumber;
		}

		public InProcessProductMaintenanceDTO() {
			super();
		}
		
		public InProcessProductMaintenanceDTO(String lineName, String lineId,String productId, Integer seqNo) {
			super();
			this.lineName = lineName;
			this.lineId = lineId;
			this.productId = productId;
			this.afOnSequenceNumber = seqNo;
		}
		
		public String getLineName() {
			return lineName;
		}
		
		public void setLineName(String lineName) {
			this.lineName = lineName;
		}
		
		public String getLineId() {
			return lineId;
		}
		
		public void setLineId(String lineId) {
			this.lineId = lineId;
		}
		
		public String getProductId() {
			return productId;
		}
		
		public void setProductId(String productId) {
			this.productId = productId;
		}
		
		
		
		public String getNextProductId() {
			return nextProductId;
		}

		public void setNextProductId(String nextProductId) {
			this.nextProductId = nextProductId;
		}
       
    }
