package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class ServiceRoleDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<ServiceRole> data;

	public ServiceRole getNewTransientServiceRole(int index) {
		com.honda.mfg.stamp.conveyor.domain.ServiceRole obj = new com.honda.mfg.stamp.conveyor.domain.ServiceRole();
		setPort(obj, index);
		setIp(obj, index);
		setServiceName(obj, index);
		setDesignatedPrimary(obj, index);
		setFailoverOrder(obj, index);
		setCurrentActive(obj, index);
		setHostName(obj, index);
		return obj;
	}

	public void setPort(ServiceRole obj, int index) {
		int port = index;
		if (port > 65535) {
			port = 65535;
		}
		obj.setPort(port);
	}

	public void setIp(ServiceRole obj, int index) {
		java.lang.String ip = "ip_" + index;
		obj.setIp(ip);
	}

	public void setServiceName(ServiceRole obj, int index) {
		java.lang.String serviceName = "serviceName_" + index;
		obj.setServiceName(serviceName);
	}

	public void setDesignatedPrimary(ServiceRole obj, int index) {
		java.lang.Boolean designatedPrimary = Boolean.TRUE;
		obj.setDesignatedPrimary(designatedPrimary);
	}

	public void setFailoverOrder(ServiceRole obj, int index) {
		int failoverOrder = index;
		obj.setFailoverOrder(failoverOrder);
	}

	public void setCurrentActive(ServiceRole obj, int index) {
		java.lang.Boolean currentActive = Boolean.TRUE;
		obj.setCurrentActive(currentActive);
	}

	public void setHostName(ServiceRole obj, int index) {
		java.lang.String hostName = "hostName_" + index;
		obj.setHostName(hostName);
	}

	public ServiceRole getSpecificServiceRole(int index) {
		init();
		if (index < 0)
			index = 0;
		if (index > (data.size() - 1))
			index = data.size() - 1;
		ServiceRole obj = data.get(index);
		return ServiceRole.findServiceRole(obj.getId());
	}

	public ServiceRole getRandomServiceRole() {
		init();
		ServiceRole obj = data.get(rnd.nextInt(data.size()));
		return ServiceRole.findServiceRole(obj.getId());
	}

	public boolean modifyServiceRole(ServiceRole obj) {
		return false;
	}

	public void init() {
		data = com.honda.mfg.stamp.conveyor.domain.ServiceRole.findServiceRoleEntries(0, 10);
		if (data == null)
			throw new IllegalStateException("Find entries implementation for 'ServiceRole' illegally returned null");
		if (!data.isEmpty()) {
			return;
		}

		data = new java.util.ArrayList<com.honda.mfg.stamp.conveyor.domain.ServiceRole>();
		for (int i = 0; i < 10; i++) {
			com.honda.mfg.stamp.conveyor.domain.ServiceRole obj = getNewTransientServiceRole(i);
			obj.persist();
			obj.flush();
			data.add(obj);
		}
	}
}
