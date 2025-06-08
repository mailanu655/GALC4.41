package com.honda.galc.notification.service;

import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.enumtype.LotcontrolNotificationType;

public interface ILotControlRuleNotification extends INotificationService{
	
	public void execute(LotControlRule lotControlRule, LotcontrolNotificationType operationType);
}
