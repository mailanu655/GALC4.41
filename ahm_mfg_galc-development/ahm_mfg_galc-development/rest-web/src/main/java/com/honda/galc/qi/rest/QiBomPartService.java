package com.honda.galc.qi.rest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.task.AsyncTaskExecutorService;

/**
 * 
 * <h3>QiBomPartService Class description</h3>
 * <p>
 * QiBomPartService description
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 * 
 * @author Justin Jiang<br>
 *         February 28, 2020
 *
 */

@Path("/qiBomPartService")
public class QiBomPartService {
	@Context
	private HttpServletRequest httpServletRequest;

	private Logger logger;

	/**
	 * Default constructor.
	 */
	public QiBomPartService() {
		logger = getLogger();
	}

	/**
	 * PUT method to batch insert
	 */
	@PUT
	@Path("insertBatch")
	@Consumes({ "application/json" })
	@Produces("text/html")
	public Response insertBatch(String jsonArrayString, @Context UriInfo uriInfo) {
		try {
			if (StringUtils.isBlank(jsonArrayString)) {
				return Response.status(Status.BAD_REQUEST).build();
			}

			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("PROCESS_TYPE", "INSERT_BATCH");
			paramMap.put("DATA", jsonArrayString);
			ServiceFactory.getService(AsyncTaskExecutorService.class).execute("OIF_QI_BOM_PART", paramMap, null, "");
		} catch (Exception e) {
			logger.error(e);
			return Response.status(Status.BAD_REQUEST).build();
		}
		return Response.status(Status.OK).build();
	}

	/**
	 * PUT method to batch merge (update or insert)
	 */
	@PUT
	@Path("mergeBatch")
	@Consumes({ "application/json" })
	@Produces("text/html")
	public Response mergeBatch(String jsonArrayString, @Context UriInfo uriInfo) {
		try {
			if (StringUtils.isBlank(jsonArrayString)) {
				return Response.status(Status.BAD_REQUEST).build();
			}

			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("PROCESS_TYPE", "MERGE_BATCH");
			paramMap.put("DATA", jsonArrayString);
			ServiceFactory.getService(AsyncTaskExecutorService.class).execute("OIF_QI_BOM_PART", paramMap, null, "");
		} catch (Exception e) {
			logger.error(e);
			return Response.status(Status.BAD_REQUEST).build();
		}
		return Response.status(Status.OK).build();
	}

	/**
	 * PUT method to delete all records in table
	 */
	@PUT
	@Path("deleteAll")
	@Consumes({ "application/json" })
	@Produces("text/html")
	public Response deleteAll(String jsonArrayString, @Context UriInfo uriInfo) {
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("PROCESS_TYPE", "DELETE_ALL");
			ServiceFactory.getService(AsyncTaskExecutorService.class).execute("OIF_QI_BOM_PART", paramMap, null, "");
		} catch (Exception e) {
			logger.error(e);
			return Response.status(Status.BAD_REQUEST).build();
		}
		return Response.status(Status.OK).build();
	}

	public Logger getLogger() {
		return Logger.getLogger(this.getClass().getSimpleName());
	}
}