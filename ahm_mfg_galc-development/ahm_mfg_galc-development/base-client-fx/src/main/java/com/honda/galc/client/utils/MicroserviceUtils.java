package com.honda.galc.client.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.qi.QiDefectResultImage;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * </TABLE>
 * @version 1.0
 * @author Dylan Yang
 * @see
 *
 */
public class MicroserviceUtils {

	private static final String POST_SUFFIX = "/post";
	private static final String GET_SUFFIX = "/get/";
	private static final String DELETE_SUFFIX = "/delete/";

	private static MicroserviceUtils instance;
	private Logger logger = Logger.getLogger();
	private String getUrl = null;
	private String deleteUrl = null;
	private String postUrl = null;
	
	private MicroserviceUtils() {
		initialize();
	}
	
	public static MicroserviceUtils getInstance() {
		if(instance == null) {
			instance = new MicroserviceUtils();
		}
		return instance;
	}
	
	public byte[] getFile(String name) {
		logger.info("Getting file from microservice: " + name);
		HttpGet request = new HttpGet(getUrl + name);
		byte[] imageData = null;
		try (CloseableHttpClient client = HttpClients.createDefault();
			 CloseableHttpResponse response = client.execute(request)) {
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() >= 300) {
				logger.error("Unable to get file from microservice: " + name + " - Invalid response");
				return null;
			}
			imageData = EntityUtils.toByteArray(response.getEntity());
			logger.info("Finished getting data from microservice: " + name);
		} catch (IOException e) {
			logger.error("Unable to get file: " + name + " - Exception: " + e.getMessage());
		}
		return imageData;
	}
	
	public String postFile(File file) {
		logger.info("Uploading file to microservice: " + file.getName());
		ResponseMessage responseMessage = null;
		HttpPost post = new HttpPost(postUrl);
		MultipartEntityBuilder builder;

		builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("filename", new StringBody(file.getName(), ContentType.TEXT_PLAIN));
		FileBody fileBody = new FileBody(file);
		builder.addPart("file", fileBody);
		HttpEntity entity = builder.build();
		post.setEntity(entity);
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		Reader reader = null;

		try {
			response = client.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();

			if(statusCode >= 300) {
				logger.error("Unable to upload. Please check your configuration. Error code: " + statusCode);
			} else {
				reader = new InputStreamReader(response.getEntity().getContent());
				Gson gson = new GsonBuilder().create();
				responseMessage = gson.fromJson(reader,  ResponseMessage.class);
			}
		} catch (Exception e1) {
			logger.error("Unable to upload file: " + file.getName() + " - Exception: " + e1.getMessage());
		} finally {
			try {
				if(reader != null) {
					reader.close();
				}
				if(response != null) {
					response.close();
				}
				if(client != null) {
					client.close();
				}
			} catch (Exception e) {
			}
		}
		return responseMessage == null ? null : getUrl + responseMessage.getMessage();
	}
	
	private boolean deleteFileByUrl(String url) {
		boolean result = false;
		if(url != null) {
			HttpDelete request = new HttpDelete(url);
			try (CloseableHttpClient client = HttpClients.createDefault();
				 CloseableHttpResponse response = client.execute(request)) {
				StatusLine statusLine = response.getStatusLine();
				if(statusLine.getStatusCode() >= 300) {
					logger.error("Unable to delete file: " + url);
				} else {
					result = true;
					logger.info("File deleted sucessfully: " + url);
				}
			} catch (IOException e) {
				logger.error("Unable to delete file: " + url + " - Exception: " + e.getMessage());
			}
		}
		
		return result;
	}

	public boolean deleteAllDefectResultImages(List<QiDefectResultImage> defectResultImages) {
		boolean result = true;
		for(QiDefectResultImage dri : defectResultImages) {
			result = result && deleteByImageUrl(dri.getId().getImageUrl());
		}
		return result;
	}
	
	public boolean deleteByImageUrl(String url) {
		if(url != null && url.startsWith(getUrl)) {
			String targetUrl = url.replaceFirst(getUrl, deleteUrl);
			return deleteFileByUrl(targetUrl);
		}
		logger.error("Invalid get URL: " + getUrl);
		return false;
	}

	public boolean deleteFile(String name) {
		if(name != null) {
			String targetUrl = deleteUrl + name;
			return deleteFileByUrl(targetUrl);
		}
		logger.error("Invalid file name: " + getUrl);
		return false;
	}
	
	public boolean isServiceAvailable() {
		return StringUtils.isNotEmpty(getUrl);
	}
	
	private void initialize() {
		String url = PropertyService.getPropertyBean(QiPropertyBean.class).getFileUploadUrl();
		if(StringUtils.isNotEmpty(url)) {
			getUrl = url + GET_SUFFIX;
			deleteUrl = url + DELETE_SUFFIX;
			postUrl = url + POST_SUFFIX;
		}
	}

	private class ResponseMessage {
		private String message;
		
		public ResponseMessage(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}
}

