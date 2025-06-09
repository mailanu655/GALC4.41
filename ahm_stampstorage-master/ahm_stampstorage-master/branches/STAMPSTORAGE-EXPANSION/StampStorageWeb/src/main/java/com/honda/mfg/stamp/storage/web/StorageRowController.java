package com.honda.mfg.stamp.storage.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.storage.service.CarrierManagementServiceProxy;

@RequestMapping("/storagerows")
@Controller
public class StorageRowController {
	@Autowired
	CarrierManagementServiceProxy carrierManagementServiceProxy;

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid StorageRow storageRow, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("storageRow", storageRow);
			return "storagerows/create";
		}
		uiModel.asMap().clear();
		storageRow.persist();
		return "redirect:/storagerows/" + encodeUrlPathSegment(storageRow.getId().toString(), httpServletRequest);
	}

	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm(Model uiModel) {
		uiModel.addAttribute("storageRow", new StorageRow());
		List dependencies = new ArrayList();
		if (Stop.countStops() == 0) {
			dependencies.add(new String[] { "stop", "stops" });
		}
		uiModel.addAttribute("dependencies", dependencies);
		return "storagerows/create";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("storagerow", StorageRow.findStorageRow(id));
		uiModel.addAttribute("itemId", id);
		return "storagerows/show";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			uiModel.addAttribute("storagerows",
					StorageRow.findStorageRowEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
			float nrOfPages = (float) StorageRow.countStorageRows() / sizeNo;
			uiModel.addAttribute("maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		} else {
			uiModel.addAttribute("storagerows", StorageRow.findAllStorageRows());
		}
		return "storagerows/list";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(StorageRow storageRow, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("storageRow", storageRow);
			return "storagerows/update";
		}
		uiModel.asMap().clear();
		StorageRow tempRow = StorageRow.findStorageRow(storageRow.getId());
		storageRow.setCapacity(tempRow.getCapacity());
		storageRow.setStorageArea(tempRow.getStorageArea());
		StorageRow row = storageRow.merge();
		carrierManagementServiceProxy.updateRow(row);
		return "redirect:/storagerows/" + encodeUrlPathSegment(storageRow.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("storageRow", StorageRow.findStorageRow(id));
		return "storagerows/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		StorageRow.findStorageRow(id).remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/storagerows";
	}

	@ModelAttribute("stops")
	public Collection<Stop> populateStops() {
		return Stop.findAllStops();
	}

	@ModelAttribute("storageareas")
	public Collection<StorageArea> populateStorageAreas() {
		return Arrays.asList(StorageArea.class.getEnumConstants());
	}

	@ModelAttribute("stopavailabilitys")
	public Collection<StopAvailability> populateStopAvailabilitys() {
		return Arrays.asList(StopAvailability.class.getEnumConstants());
	}

	@ModelAttribute("storagerows")
	public Collection<StorageRow> populateStorageRows() {
		return StorageRow.findAllStorageRows();
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
