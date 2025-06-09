package com.honda.mfg.stamp.storage.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
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

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.Press;

@RequestMapping("/carriermes")
@Controller
public class CarrierMesController {
	private static final Logger LOG = LoggerFactory.getLogger(CarrierMesController.class);

	// @Autowired
	// private CarrierManagementService carrierManagementService;

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid CarrierMes carrierMes, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("carriermes", carrierMes);
			addDateTimeFormatPatterns(uiModel);
			return "carriermes/create";
		}
		uiModel.asMap().clear();
		carrierMes.persist();
		return "redirect:/carriermes/" + encodeUrlPathSegment(carrierMes.getId().toString(), httpServletRequest);
	}

	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm(Model uiModel) {
		uiModel.addAttribute("carriermes", new CarrierMes());
		addDateTimeFormatPatterns(uiModel);
		return "carriermes/create";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model uiModel) {
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("carriermes", CarrierMes.findCarrier(id));
		uiModel.addAttribute("itemId", id);
		return "carriermes/show";
	}

	@RequestMapping(params = "moving", method = RequestMethod.GET)
	public String moving(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		LOG.info("#################     About to LIST MOVING carriers!!!");

		List<CarrierMes> allCarriers = CarrierMes.findAllCarriers();
		List<CarrierMes> movingCarriers = new ArrayList<CarrierMes>();

		for (CarrierMes carrierMes : allCarriers) {
			if (!carrierMes.getCurrentLocation().equals(carrierMes.getDestination())) {
				movingCarriers.add(carrierMes);
			}
		}

		uiModel.addAttribute("carriermes", movingCarriers);

		addDateTimeFormatPatterns(uiModel);
		return "carriermes/list";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		LOG.info("#################     About to LIST carriers!!!");

		LOG.info("About to LIST!!!");
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			uiModel.addAttribute("carriermes",
					CarrierMes.findCarrierEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
			float nrOfPages = (float) CarrierMes.countCarriers() / sizeNo;
			if (nrOfPages > 20) {
				uiModel.addAttribute("maxPages", 20);
			} else {
				uiModel.addAttribute("maxPages",
						(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
			}

			uiModel.addAttribute("carriermes", CarrierMes.findAllCarriers());

			List<CarrierMes> carrierList = new ArrayList<CarrierMes>();
			for (CarrierMes carrierMes : CarrierMes.findAllCarriers()) {
				carrierList.add(carrierMes);
			}
			uiModel.addAttribute("carriermes", carrierList);
		}
		addDateTimeFormatPatterns(uiModel);
		return "carriermes/list";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@Valid CarrierMes carrierMes, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
		LOG.info("About to UPDATE!!!");
// todo Fix binding error problem
//        if (bindingResult.hasErrors()) {
//            LOG.info("Binding result has errors.");

//            uiModel.addAttribute("carriermes", carrierMes);
//            addDateTimeFormatPatterns(uiModel);

//            return "carriermes/update";
//        }
		LOG.info("No binding errors!");

		uiModel.asMap().clear();

		CarrierMes curCarrier = CarrierMes.findCarrier(carrierMes.getId());
		if (curCarrier != null && carrierMes.getCarrierNumber() == null) {
			carrierMes.setCarrierNumber(curCarrier.getCarrierNumber());
		}
		Long curLoc = curCarrier == null ? null : curCarrier.getCurrentLocation();
		LOG.info("carrierMes.getCurrentLocation(): " + carrierMes.getCurrentLocation());
		if (carrierMes.getCurrentLocation() != null && curLoc != null) {
			LOG.info("Setting current location:  " + curLoc);
			carrierMes.setCurrentLocation(curLoc);
		}
		// Carrier carrier = getCarrier(carrierMes);
		// carrierManagementService.saveCarrier(carrier);

		pause(2);

		return "redirect:/carriermes/" + encodeUrlPathSegment(carrierMes.getId().toString(), httpServletRequest);
	}

	private void pause(int secs) {
		long delta = System.currentTimeMillis();
		while (System.currentTimeMillis() - delta < (secs * 1000)) {
		}
	}

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {

		CarrierMes carrierMes = CarrierMes.findCarrier(id);
		uiModel.addAttribute("carriermes", carrierMes);

		addDateTimeFormatPatterns(uiModel);
		return "carriermes/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		CarrierMes.findCarrier(id).remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/carriermes";
	}

	@ModelAttribute("carriermes")
	public Collection<CarrierMes> populateCarriers() {
		return CarrierMes.findAllCarriers();
	}

	@ModelAttribute("carrierstatuses")
	public Collection<CarrierStatus> populateCarrierStatuses() {
		return Arrays.asList(CarrierStatus.class.getEnumConstants());
	}

	@ModelAttribute("dies")
	public Collection<Die> populateDies() {
		return Die.findAllDies();
	}

	@ModelAttribute("presses")
	public Collection<Press> populatePresses() {
		return Arrays.asList(Press.class.getEnumConstants());

	}

	@ModelAttribute("stops")
	public Collection<Stop> populateStops() {
		return Stop.findAllStops();
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute("carrier_loadtimestamp_date_format",
				DateTimeFormat.patternForStyle("SS", LocaleContextHolder.getLocale()));
		uiModel.addAttribute("carrier_unloadtimestamp_date_format",
				DateTimeFormat.patternForStyle("SS", LocaleContextHolder.getLocale()));
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

	private Carrier getCarrier(CarrierMes carrierMes) {
		Carrier carrier = new Carrier();
		// TODO:Get Carrier doesn't have maint bits.
		carrier.setId(carrierMes.getId());
		carrier.setCarrierNumber(carrierMes.getCarrierNumber());
		carrier.setQuantity(carrierMes.getQuantity());
		carrier.setDie(Die.findDie(new Long(carrierMes.getDieNumber())));
		carrier.setCurrentLocation(Stop.findStop(carrierMes.getCurrentLocation()));
		carrier.setDestination(Stop.findStop(carrierMes.getDestination()));
		carrier.setPress(Press.findByType(carrierMes.getOriginationLocation()));
		carrier.setCarrierStatus(CarrierStatus.findByType(carrierMes.getStatus()));
		carrier.setProductionRunNo(carrierMes.getProductionRunNumber());
		carrier.setStampingProductionRunTimestamp(carrierMes.getProductionRunDate());
		return carrier;
	}
}
