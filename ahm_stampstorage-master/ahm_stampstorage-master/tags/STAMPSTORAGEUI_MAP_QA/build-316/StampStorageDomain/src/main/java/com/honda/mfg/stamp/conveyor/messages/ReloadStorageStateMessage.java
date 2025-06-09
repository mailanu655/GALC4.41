package com.honda.mfg.stamp.conveyor.messages;

import com.honda.mfg.stamp.conveyor.domain.Stop;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 4/12/12
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReloadStorageStateMessage {

    Stop currentLocation;

    public ReloadStorageStateMessage(Stop row){
        this.currentLocation = row;
    }

    public Stop getCurrentLocation(){
        return this.currentLocation;
    }
}
