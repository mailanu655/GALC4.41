package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.storage.service.CarrierManagementService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: Jeffrey M Lutz
 * Date: 7/8/11
 */
public class StorageStateController {

    @Autowired
    private CarrierManagementService carrierManagementService;

    //private static StorageStateContext storageStateContext;

    public StorageStateController() {
    }

    /*
    public StorageState getStorageState() {
    	//2013-02-01:VB:StorageStateController is not being used, stale data was published directly in Lane Controller
        //return carrierManagementService.getStorageState();
    }

    public static void refreshStorageState() {
    	//2013-02-01:VB:StorageStateController is not being used, stale data was published directly in Lane Controller
        //EventBus.publish(new StaleDataMessage(false));
    }
    */
}
