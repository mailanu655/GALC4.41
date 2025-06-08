package com.honda.galc.service.task;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.task.TaskExecutor;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.net.HttpClient;
import com.honda.galc.system.oif.svc.common.OifServiceFactory;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.OIFConstants;

public class AsyncTaskHandler {
	private TaskExecutor taskExecutor;
	private static final int SUCCCESS=1;
	private static final int FAILURE=-1;
	

	public AsyncTaskHandler(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	public void invoke(String taskName, Map<String,String> taskArgs, String jobid, String replyToUrl) {
	    taskExecutor.execute(new AsyncOifTask(taskName,taskArgs,jobid,replyToUrl));
	}
	
	private class AsyncOifTask implements Runnable {

		private String taskName;
		private String jobId;
		private String replyToUrl;
		private Map<String,String> taskArgs;
		private static final String JOB_ID_PARAM = "id";
		private static final String STATUS_PARAM = "status";

		AsyncOifTask(String taskName,Map<String,String> taskArgs, String jobid, String replyToUrl) {
			this.taskName = taskName;
			this.taskArgs = taskArgs;
			this.jobId = jobid;
			this.replyToUrl = replyToUrl;
		}

		public void run() {
			int status;
			try {
				String[] distributionInfo = taskName .split(OIFConstants.DISTRIBUTION_TASK_NAMES_SEP);
				List<Object> args = new ArrayList<Object>();
				
				args = TaskUtils.packExtraArgs(args,taskArgs);
				String userid = taskArgs == null ? String.format("remote-agent:jobid=%s",jobId) :taskArgs.get("userId");
				
				if (distributionInfo.length > 0 && OIFConstants.OIF_DISTRIBUTION.equalsIgnoreCase(distributionInfo[0])) {
					args.add(new KeyValue<String, String>(OIFConstants.INTERFACE_ID, distributionInfo[1]));
					OifServiceFactory.executeTask(distributionInfo[0],userid,args.toArray());
				} else {
					OifServiceFactory.executeTask(taskName,userid,args.toArray());
				}
				
				status=SUCCCESS;
				
			} catch (Exception e) {
				String message = String.format("Task failed. Event Name: %s, Name: %s , Reason: %s",jobId, taskName, e.toString());
				Logger.getLogger().error("OIF processing error ", "execute",message);
				status=FAILURE;
			}
			
			String url = String.format("%s?%s=%s&%s=%d",replyToUrl,JOB_ID_PARAM,jobId,STATUS_PARAM,status);
			
			if (replyToUrl != null && replyToUrl.length() >  0) {
			   Logger.getLogger().info("Sending notification", "execute",url);
			   HttpClient.get(url, HttpURLConnection.HTTP_OK);
			} else {
			   Logger.getLogger().info("Notification not sent replyToUrl was not provided", "execute",url);
			}
 		}
	}		
}
	





