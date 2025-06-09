package com.honda.mfg.stamp.storage.web;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.honda.mfg.stamp.conveyor.domain.AuditErrorLog;
import com.honda.mfg.stamp.conveyor.domain.AuditErrorLogFinderCriteria;

@RequestMapping("/auditerrorlogs")
@Controller
public class AuditErrorLogController {
	private static final Logger LOG = LoggerFactory.getLogger(AuditErrorLogController.class);

	private static final String LOG_NODE_ID_FILTER = "logNodeIdFilter";
	private static final String LOG_SOURCE_FILTER = "logSourceFilter";
	private static final String LOG_SEVERITY_FILTER = "logSeverityFilter";
	private static final String LOG_MESSAGE_TEXT_FILTER = "logMessageTextFilter";
	private static final String LOG_BEGIN_TIMESTAMP_FILTER = "logBeginTimestampFilter";
	private static final String LOG_END_TIMESTAMP_FILTER = "logEndTimestampFilter";

	private static final int COOKIE_MAX_AGE = 900;

	// private AuditErrorLogFinderCriteria finderCriteria;

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid AuditErrorLog auditErrorLog, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("auditErrorLog", auditErrorLog);
			return "auditerrorlogs/create";
		}
		uiModel.asMap().clear();
		auditErrorLog.persist();
		return "redirect:/auditerrorlogs/" + encodeUrlPathSegment(auditErrorLog.getId().toString(), httpServletRequest);
	}

	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm(Model uiModel) {
		uiModel.addAttribute("auditErrorLog", new AuditErrorLog());
		return "auditerrorlogs/create";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("auditerrorlog", AuditErrorLog.findAuditErrorLog(id));
		uiModel.addAttribute("itemId", id);
		return "auditerrorlogs/show";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Model uiModel) {
		AuditErrorLogFinderCriteria finderCriteria = getSavedAuditErrorLogFinderCriteria(httpServletRequest);
		List<AuditErrorLog> auditErrorLogList;

		if (page != null || size != null) {
			int sizeNo = size == null ? 50 : size.intValue();
			if (finderCriteria != null) {
				auditErrorLogList = AuditErrorLog.findAuditErrorLogByCriteria(finderCriteria, page, size);

				float nrOfPages = (float) AuditErrorLog.findAuditErrorLogCount(finderCriteria) / sizeNo;
				if (nrOfPages > 20) {
					uiModel.addAttribute("maxPages", 20);
				} else {
					uiModel.addAttribute("maxPages",
							(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
				}
				uiModel.addAttribute("nodeId", finderCriteria.getNodeId());
				uiModel.addAttribute("source", finderCriteria.getSource());
				uiModel.addAttribute("severity", finderCriteria.getSeverity());
				uiModel.addAttribute("messageText", finderCriteria.getMessageText());
				uiModel.addAttribute("beginTimestamp", finderCriteria.getBeginTimestamp());
				uiModel.addAttribute("endTimestamp", finderCriteria.getEndTimestamp());
				uiModel.addAttribute("auditerrorlogs", auditErrorLogList);
			}
		} else {
			auditErrorLogList = AuditErrorLog.findAuditErrorLogByCriteria(finderCriteria, page, size);
			uiModel.addAttribute("carrierhistories", auditErrorLogList);
		}

		return "auditerrorlogs/list";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@Valid AuditErrorLog auditErrorLog, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("auditErrorLog", auditErrorLog);
			return "auditerrorlogs/update";
		}
		uiModel.asMap().clear();
		auditErrorLog.merge();
		return "redirect:/auditerrorlogs/" + encodeUrlPathSegment(auditErrorLog.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("auditErrorLog", AuditErrorLog.findAuditErrorLog(id));
		return "auditerrorlogs/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		AuditErrorLog.findAuditErrorLog(id).remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/auditerrorlogs";
	}

	@RequestMapping(params = { "find=ByCriteria", "form" }, method = RequestMethod.GET)
	public String findAuditErrorLogByCriteriaForm(Model uiModel) {

		List<AuditErrorLog> auditErrorLogList = new ArrayList<AuditErrorLog>();
		for (AuditErrorLog auditErrorLog : AuditErrorLog.findAuditErrorLogEntries(0, 50)) {
			auditErrorLogList.add(auditErrorLog);
		}
		uiModel.addAttribute("auditerrorlogs", auditErrorLogList);

		return "auditerrorlogs/list";
	}

	@RequestMapping(params = "find=ByCriteria", method = RequestMethod.GET)
	public String findAuditErrorLogByCriteria(@RequestParam("nodeId") String nodeId,
			@RequestParam("source") String source, @RequestParam("severity") String severity,
			@RequestParam("messageText") String messageText, @RequestParam("beginTimestamp") String beginTimestamp,
			@RequestParam("endTimestamp") String endTimestamp, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Model uiModel) {

		AuditErrorLogFinderCriteria finderCriteria = new AuditErrorLogFinderCriteria();

		if (nodeId.length() > 0)
			finderCriteria.setNodeId(nodeId);
		if (source.length() > 0)
			finderCriteria.setSource(source);
		if (severity.length() > 0)
			finderCriteria.setSeverity(Integer.parseInt(severity));
		if (messageText.length() > 0)
			finderCriteria.setMessageText(messageText);
		if (beginTimestamp.length() > 0)
			finderCriteria.setBeginTimestamp(Timestamp.valueOf(beginTimestamp));
		if (endTimestamp.length() > 0)
			finderCriteria.setEndTimestamp(Timestamp.valueOf(endTimestamp));

		List<AuditErrorLog> auditErrorLogList = AuditErrorLog.findAuditErrorLogByCriteria(finderCriteria, 1, 50);

		float nrOfPages = (float) AuditErrorLog.findAuditErrorLogCount(finderCriteria) / 50;
		if (nrOfPages > 20) {
			uiModel.addAttribute("maxPages", 20);
		} else {
			uiModel.addAttribute("maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		}

		uiModel.addAttribute("auditerrorlogs", auditErrorLogList);
		uiModel.addAttribute("openfind", true);
		uiModel.addAttribute("nodeId", nodeId);
		uiModel.addAttribute("source", source);
		uiModel.addAttribute("severity", severity);
		uiModel.addAttribute("messageText", messageText);
		uiModel.addAttribute("beginTimestamp", beginTimestamp);
		uiModel.addAttribute("endTimestamp", endTimestamp);

		saveAuditErrorLogFinderCriteria(finderCriteria, httpServletResponse);

		return "auditerrorlogs/list";
	}

	private AuditErrorLogFinderCriteria getSavedAuditErrorLogFinderCriteria(HttpServletRequest httpServletRequest) {

		String nodeId = null;
		String source = null;
		Integer severity = null;
		String messageText = null;
		Timestamp beginTimestamp = null;
		Timestamp endTimestamp = null;
		AuditErrorLogFinderCriteria finderCriteria = null;

		Cookie[] cookies = httpServletRequest.getCookies();
		LOG.info("Displaying cookie information:");
		for (Cookie cookie : cookies) {

			String name = cookie.getName();
			String value = cookie.getValue();
			LOG.info("cookie Name-" + name);
			LOG.info("cookie value-" + value);
			if (name.equals(LOG_NODE_ID_FILTER) && (value != null && value.length() > 0)) {
				nodeId = value;
			}
			if (name.equals(LOG_SOURCE_FILTER) && (value != null && value.length() > 0)) {
				source = value;
			}
			if (name.equals(LOG_SEVERITY_FILTER) && (value != null && value.length() > 0)) {
				severity = (Integer.parseInt(value));
			}
			if (name.equals(LOG_MESSAGE_TEXT_FILTER) && (value != null && value.length() > 0)) {
				messageText = value;
			}
			if (name.equals(LOG_BEGIN_TIMESTAMP_FILTER) && (value != null && value.length() > 0)) {
				beginTimestamp = Timestamp.valueOf(value);
			}
			if (name.equals(LOG_END_TIMESTAMP_FILTER) && (value != null && value.length() > 0)) {
				endTimestamp = Timestamp.valueOf(value);
			}
		}

		if (nodeId != null || source != null || severity != null || messageText != null || beginTimestamp != null
				|| endTimestamp != null) {
			finderCriteria = new AuditErrorLogFinderCriteria();
			finderCriteria.setNodeId(nodeId);
			finderCriteria.setSource(source);
			finderCriteria.setSeverity(severity);
			finderCriteria.setMessageText(messageText);
			finderCriteria.setBeginTimestamp(beginTimestamp);
			finderCriteria.setEndTimestamp(endTimestamp);
		}
		return finderCriteria;
	}

	private void saveAuditErrorLogFinderCriteria(AuditErrorLogFinderCriteria finderCriteria,
			HttpServletResponse httpServletResponse) {

		if (finderCriteria != null) {

			String nodeId = finderCriteria.getNodeId() != null ? finderCriteria.getNodeId() : "";
			String source = finderCriteria.getSource() != null ? finderCriteria.getSource() : "";
			String severity = finderCriteria.getSeverity() != null ? finderCriteria.getSeverity().toString() : "";
			String messageText = finderCriteria.getMessageText() != null ? finderCriteria.getMessageText() : "";
			String beginTimestamp = finderCriteria.getBeginTimestamp() != null
					? finderCriteria.getBeginTimestamp().toString()
					: "";
			String endTimestamp = finderCriteria.getEndTimestamp() != null ? finderCriteria.getEndTimestamp().toString()
					: "";

			Cookie nodeIdCookie = new Cookie(LOG_NODE_ID_FILTER, nodeId);
			nodeIdCookie.setMaxAge(COOKIE_MAX_AGE);

			Cookie sourceCookie = new Cookie(LOG_SOURCE_FILTER, source);
			sourceCookie.setMaxAge(COOKIE_MAX_AGE);

			Cookie severityCookie = new Cookie(LOG_SEVERITY_FILTER, severity);
			severityCookie.setMaxAge(COOKIE_MAX_AGE);

			Cookie messageTextCookie = new Cookie(LOG_MESSAGE_TEXT_FILTER, messageText);
			messageTextCookie.setMaxAge(COOKIE_MAX_AGE);

			Cookie beginTimestampCookie = new Cookie(LOG_BEGIN_TIMESTAMP_FILTER, beginTimestamp);
			beginTimestampCookie.setMaxAge(COOKIE_MAX_AGE);

			Cookie endTimestampCookie = new Cookie(LOG_END_TIMESTAMP_FILTER, endTimestamp);
			endTimestampCookie.setMaxAge(COOKIE_MAX_AGE);

			httpServletResponse.addCookie(nodeIdCookie);
			httpServletResponse.addCookie(sourceCookie);
			httpServletResponse.addCookie(severityCookie);
			httpServletResponse.addCookie(messageTextCookie);
			httpServletResponse.addCookie(beginTimestampCookie);
			httpServletResponse.addCookie(endTimestampCookie);
		}
	}

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest)
			throws UnsupportedEncodingException {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		return pathSegment;
	}
}
