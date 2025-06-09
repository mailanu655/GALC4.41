package com.honda.mfg.stamp.storage.service.utils;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public interface ServiceRoleWrapper extends Runnable  {

	boolean isPassive();

	boolean removeObserver(ServiceRoleObserver ob);

	void addObserver(ServiceRoleObserver newObserver);

	CountDownLatch getLatch();

	void init();

	void setSocketProperties(Properties socketProperties);

	void setPassive(boolean isPassive);


}
