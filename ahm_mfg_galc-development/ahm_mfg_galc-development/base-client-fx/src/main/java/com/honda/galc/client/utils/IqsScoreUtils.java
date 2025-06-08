package com.honda.galc.client.utils;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.entity.enumtype.IqsScore;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class IqsScoreUtils {

	public static List<IqsScore> getActiveScores() {
		List<IqsScore> result = new ArrayList<IqsScore>();

		if(PropertyService.getPropertyBean(QiPropertyBean.class).is102Ki()) {
			result.add(IqsScore.IQS100);
			result.add(IqsScore.IQS90);
			result.add(IqsScore.IQS80);
			result.add(IqsScore.IQS70);
			result.add(IqsScore.IQS60);
			result.add(IqsScore.IQS50);
			result.add(IqsScore.IQS40);
		} else {
			result.add(IqsScore.IQS70);
			result.add(IqsScore.IQS65);
			result.add(IqsScore.IQS60);
			result.add(IqsScore.IQS55);
			result.add(IqsScore.IQS50);
			result.add(IqsScore.IQS40);
		}
		return result;
	}

}
