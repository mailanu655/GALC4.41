package com.honda.galc.test.dao;

import java.util.List;

import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.data.cache.PersistentCache;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.ServiceFactory;


public class TestCache extends PersistentCache{
    
    private int count = 1000;

	public TestCache(){
        super();
    }
    
    public void addProductionLot() {
        
        ProductionLotDao prodLotDao = ServiceFactory.getDao(ProductionLotDao.class);
        List<ProductionLot> lots = prodLotDao.findAll();
        for(ProductionLot lot : lots){
            lot.setCreateTimestamp(null);
            lot.setUpdateTimestamp(null);
            lot.setKdLotNumber(null);
            lot.setLotNumber(null);
            lot.setPlanOffDate(null);
            this.put("prod_lot" + getSize() , lot);
        }
        flush();
    }
    
    public void addInstalledPart() {
        
        InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
        InstalledPartId id = new InstalledPartId();
        id.setProductId("R18A14800848");
        id.setPartName("MISSION");
        InstalledPart part = installedPartDao.findByKey(id);
        
        for (int j = 0; j < 4; j++) { //4 torque for test
			Measurement m = new Measurement();
			part.getMeasurements().add(m);
		}
        
		for(int i = 0; i < count ; i++){
 
            this.put("part" + getSize() , part);
        }
        flush();
    }
    
    public void list(){
        int size = getSize();
        System.out.println("List size :" + size);
        for(int i= 0; i<size;i++) {
            ProductionLot obj =  get("prod_lot"+i,ProductionLot.class); 
 //           System.out.println(obj.getProductionLot());
           // Object obj = cache.getQuiet("prod_lot"+i);
          // Object obj = "test";  
            if(obj == null) System.out.println("Error");
        }
    }
    
    public void listPart(){
        int size = getSize();
        System.out.println("List size :" + size);
        for(int i= 0; i<size;i++) {
            InstalledPart obj = get("part"+i,InstalledPart.class);  
 //           System.out.println(obj.getProductionLot());
           // Object obj = cache.getQuiet("prod_lot"+i);
          // Object obj = "test";  
            if(obj == null) System.out.println("Error");
        }
    }
}
