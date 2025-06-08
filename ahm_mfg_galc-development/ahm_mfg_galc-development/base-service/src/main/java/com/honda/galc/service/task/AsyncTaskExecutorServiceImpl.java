package com.honda.galc.service.task;

import java.util.Map;

import com.honda.galc.data.ApplicationContextProvider;



public class AsyncTaskExecutorServiceImpl implements AsyncTaskExecutorService {
	
	@Override
	public void execute(String taskName, String jobid, String replyToUrl) {
		 AsyncTaskHandler taskHandler =  (AsyncTaskHandler) ApplicationContextProvider.getBean("OifAsyncTaskHandler");
		 taskHandler.invoke(taskName, null, jobid, replyToUrl);
	}
	
	@Override
	public void execute(String taskName,Map<String,String> taskArgs, String jobid, String replyToUrl) {
		 AsyncTaskHandler taskHandler =  (AsyncTaskHandler) ApplicationContextProvider.getBean("OifAsyncTaskHandler");
		 taskHandler.invoke(taskName, taskArgs, jobid, replyToUrl);
	}
}



