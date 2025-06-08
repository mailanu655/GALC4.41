package com.honda.galc.test.common;

public abstract class HeadlessTestCase extends BaseTestCase {
    
      public void setProcessPointId(String processPointId) {
          
          processManager.setProcessPointId(processPointId);
          
      }
        
}
