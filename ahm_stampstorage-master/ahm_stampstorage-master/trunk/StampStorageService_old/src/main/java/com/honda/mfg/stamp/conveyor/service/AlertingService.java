package com.honda.mfg.stamp.conveyor.service;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 4/4/12
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */
public interface AlertingService {

    void sendEmail(String emailAddress, String message);
}
