package com.honda.galc.test.common;

import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.data.cache.CacheFactory;
import com.honda.galc.test.dao.TestCache;

public class CacheTest {

    /**
     * @param args
     */
    
    private static TestCache testCache;
    public static void main(String[] args) {
        
        ApplicationContextProvider.loadFromClassPathXml("application.xml");
        CacheFactory.setPath("c:\\honda\\cache\\ESTS Edit");
        long start = System.currentTimeMillis();
        System.out.println("Cache creation and initlisztion");
        System.out.println("cache start " + start);
        testCache = new TestCache();
//        testCache.addProductionLot();
        testCache.addInstalledPart();
        System.out.println("Cache size: " + testCache.getSize());
        long end = System.currentTimeMillis();
        System.out.println("cache end " + end + " time elapsed : " + (end -start));
        
        
        for(int i= 0;i<5;i++) list(i);
        
    }
    
    public static void list(int i) {
        long start = System.currentTimeMillis();
        System.out.println();
        if(i == 0 )System.out.println("First time getCache is longer because it loads data into mememory cache");
        System.out.println("cache start " + start);
        testCache.listPart();
        long end = System.currentTimeMillis();
        System.out.println("cache end " + end + " time elapsed : " + (end -start) + " milli seconds");
    }
    

}
