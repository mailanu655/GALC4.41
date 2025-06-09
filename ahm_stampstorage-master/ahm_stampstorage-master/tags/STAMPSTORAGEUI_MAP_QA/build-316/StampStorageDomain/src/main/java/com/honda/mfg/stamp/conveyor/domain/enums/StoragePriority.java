package com.honda.mfg.stamp.conveyor.domain.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines priority based on Production Volume
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 2/28/13
 * Time: 11:32 AM
 */
public class StoragePriority {
    public enum Priority {ZERO, ONE, TWO, THREE, FOUR, FIVE}
     PartProductionVolume  volume ;
     public static final Map<PartProductionVolume,Map<Priority, StorageArea>> VOLUME_AREA_PRIORITY_MAP = new HashMap<PartProductionVolume, Map<Priority, StorageArea>>();

     static{
        Map<Priority, StorageArea> highVolumeDieAreaPriority = new HashMap<Priority, StorageArea>() ;
         highVolumeDieAreaPriority.put(Priority.ONE, StorageArea.S_AREA ) ;
         highVolumeDieAreaPriority.put(Priority.TWO, StorageArea.B_AREA ) ;
         highVolumeDieAreaPriority.put(Priority.THREE, StorageArea.C_LOW) ;
         highVolumeDieAreaPriority.put(Priority.FOUR, StorageArea.C_HIGH) ;
         highVolumeDieAreaPriority.put(Priority.FIVE, StorageArea.A_AREA) ;

         Map<Priority, StorageArea> mediumVolumeDieAreaPriority = new HashMap<Priority, StorageArea>() ;
         mediumVolumeDieAreaPriority.put(Priority.ONE, StorageArea.C_LOW ) ;
         mediumVolumeDieAreaPriority.put(Priority.TWO, StorageArea.B_AREA ) ;
         mediumVolumeDieAreaPriority.put(Priority.THREE, StorageArea.C_HIGH) ;
         mediumVolumeDieAreaPriority.put(Priority.FOUR, StorageArea.A_AREA) ;
         mediumVolumeDieAreaPriority.put(Priority.FIVE, StorageArea.S_AREA) ;


         Map<Priority, StorageArea> lowVolumeDieAreaPriority = new HashMap<Priority, StorageArea>() ;
         lowVolumeDieAreaPriority.put(Priority.ONE, StorageArea.C_HIGH ) ;
         lowVolumeDieAreaPriority.put(Priority.TWO, StorageArea.C_LOW ) ;
         lowVolumeDieAreaPriority.put(Priority.THREE, StorageArea.B_AREA) ;
         lowVolumeDieAreaPriority.put(Priority.FOUR, StorageArea.A_AREA) ;
         lowVolumeDieAreaPriority.put(Priority.FIVE, StorageArea.S_AREA) ;

         Map<Priority, StorageArea> emptyCarrierAreaPriority = new HashMap<Priority, StorageArea>() ;
         emptyCarrierAreaPriority.put(Priority.ONE, StorageArea.A_AREA ) ;
         emptyCarrierAreaPriority.put(Priority.TWO, StorageArea.C_LOW ) ;
         emptyCarrierAreaPriority.put(Priority.THREE, StorageArea.B_AREA) ;
         emptyCarrierAreaPriority.put(Priority.FOUR, StorageArea.C_HIGH) ;




         VOLUME_AREA_PRIORITY_MAP.put(PartProductionVolume.HIGH_VOLUME, highVolumeDieAreaPriority);
         VOLUME_AREA_PRIORITY_MAP.put(PartProductionVolume.MEDIUM_VOLUME, mediumVolumeDieAreaPriority);
         VOLUME_AREA_PRIORITY_MAP.put(PartProductionVolume.LOW_VOLUME, lowVolumeDieAreaPriority);
         VOLUME_AREA_PRIORITY_MAP.put(PartProductionVolume.EMPTY, emptyCarrierAreaPriority);
     }


    public static StorageArea getStorageAreaByPriorityForVolume(PartProductionVolume partProductionVolume, Priority priority){
      return (VOLUME_AREA_PRIORITY_MAP.get(partProductionVolume)).get(priority);
    }
/**
 * 
 * @param partProductionVolume
 * @param area
 * @return Map with numbered priority area to store in.
 */
     public static Priority getStoragePriorityByArea(PartProductionVolume partProductionVolume, StorageArea area){

      Priority p= null;
      Map<Priority, StorageArea> areaPriority  = VOLUME_AREA_PRIORITY_MAP.get(partProductionVolume);

         for(Priority priority: areaPriority.keySet()){
             if(areaPriority.get(priority).equals(area)){
                 p = priority;
                 break;
             }
         }
      return p;
    }
}
