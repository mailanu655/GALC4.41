package com.honda.galc.service.task;


import java.util.Map;

import com.honda.galc.service.IService;


public interface AsyncTaskExecutorService extends IService {
    void  execute(String taskName, String jobId, String replyToUrl);
    void  execute(String taskName,Map<String,String> taskArgs, String jobId, String replyToUrl);
}

