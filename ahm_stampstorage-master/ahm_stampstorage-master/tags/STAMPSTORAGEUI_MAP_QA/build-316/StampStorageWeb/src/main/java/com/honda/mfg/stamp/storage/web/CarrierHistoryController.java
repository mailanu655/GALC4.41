package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.CarrierHistory;
import com.honda.mfg.stamp.conveyor.domain.CarrierHistoryFinderCriteria;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.Press;
import java.io.UnsupportedEncodingException;
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

@RequestMapping("/carrierhistories")
@Controller
public class CarrierHistoryController {

    private static final Logger LOG = LoggerFactory.getLogger(CarrierHistoryController.class);

    private static final String HISTORY_CARRIER_NUMBER_FILTER = "historyCarrierNumberFilter";
    private static final String HISTORY_CURRENT_LOCATION_FILTER = "historyCurrentLocationFilter";
    private static final String HISTORY_DEST_LOCATION_FILTER = "historyDestinationLocationFilter";
    private static final int COOKIE_MAX_AGE = 900;

    private CarrierHistoryFinderCriteria finderCriteria;

	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid CarrierHistory carrierHistory, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("carrierHistory", carrierHistory);
            return "carrierhistories/create";
        }
        uiModel.asMap().clear();
        carrierHistory.persist();
        return "redirect:/carrierhistories/" + encodeUrlPathSegment(carrierHistory.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        uiModel.addAttribute("carrierHistory", new CarrierHistory());
        return "carrierhistories/create";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("carrierhistory", CarrierHistory.findCarrierHistory(id));
        uiModel.addAttribute("itemId", id);
        return "carrierhistories/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model uiModel) {

        CarrierHistoryFinderCriteria finderCriteria = getSavedCarrierHistoryFinderCriteria(httpServletRequest);
        List<CarrierHistory> carrierHistoryList;

        if (page != null || size != null) {
            int sizeNo = size == null ? 50 : size.intValue();
            if (finderCriteria != null) {
                carrierHistoryList = CarrierHistory.findCarrierHistoryByCarrierNumber(finderCriteria, page, size);

                float nrOfPages = (float) CarrierHistory.getFindCarrierHistoryCount(finderCriteria) / sizeNo;
                if (nrOfPages > 20) {
                    uiModel.addAttribute("maxPages", 20);
                } else {
                    uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
                }
                uiModel.addAttribute("carrierNumber", finderCriteria.getCarrierNumber());
                uiModel.addAttribute("carrierhistories", carrierHistoryList);
            }
        } else {
            carrierHistoryList = CarrierHistory.findCarrierHistoryByCarrierNumber(finderCriteria, page, size);
            uiModel.addAttribute("carrierhistories", carrierHistoryList);
        }

        return "carrierhistories/list";
    }

	@RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid CarrierHistory carrierHistory, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("carrierHistory", carrierHistory);
            return "carrierhistories/update";
        }
        uiModel.asMap().clear();
        carrierHistory.merge();
        return "redirect:/carrierhistories/" + encodeUrlPathSegment(carrierHistory.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("carrierHistory", CarrierHistory.findCarrierHistory(id));
        return "carrierhistories/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        CarrierHistory.findCarrierHistory(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/carrierhistories";
    }

	@RequestMapping(params = {"find=ByCarrierNumber", "form"}, method = RequestMethod.GET)
    public String findCarrierHistoryByCriteriaForm(Model uiModel) {
        List<CarrierHistory> carrierHistoryList = new ArrayList<CarrierHistory>();
        for (CarrierHistory carrierHistory : CarrierHistory.findCarrierHistoryEntries(0, 50)) {
            carrierHistoryList.add(carrierHistory);
        }
        uiModel.addAttribute("carrierHistories", carrierHistoryList);

        return "carrierhistories/list";
    }

	@RequestMapping(params = "find=ByCarrierNumber", method = RequestMethod.GET)
    public String findCarrierHistoryByCriteria(
            @RequestParam("carrierNumber") Integer carrierNumber,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Model uiModel) {

        finderCriteria = new CarrierHistoryFinderCriteria();
        finderCriteria.setCarrierNumber(carrierNumber);

        List<CarrierHistory> carrierHistoryList = CarrierHistory.findCarrierHistoryByCarrierNumber(finderCriteria, 1, 50);

        float nrOfPages = (float) CarrierHistory.getFindCarrierHistoryCount(finderCriteria) / 50;
        if (nrOfPages > 20) {
            uiModel.addAttribute("maxPages", 20);
        } else {
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        }

        uiModel.addAttribute("carrierhistories", carrierHistoryList);
        uiModel.addAttribute("openfind", true);
        uiModel.addAttribute("carrierNumber", carrierNumber);

        saveCarrierHistoryFinderCriteria(finderCriteria, httpServletResponse);

        return "carrierhistories/list";
    }

	private CarrierHistoryFinderCriteria getSavedCarrierHistoryFinderCriteria(HttpServletRequest httpServletRequest) {

        Integer carrierNumber = null;
        CarrierStatus carrierStatus = null;
        Stop currentLocation = null;
        Stop destination = null;
        Die die = null;
        Press press = null;
        Integer productionRunNumber = null;
        CarrierHistoryFinderCriteria finderCriteria = null;

        Cookie[] cookies = httpServletRequest.getCookies();
        LOG.info("Displaying cookie information:");
        for (Cookie cookie : cookies) {

            String name = cookie.getName();
            String value = cookie.getValue();
            LOG.info("cookie Name-" + name);
            LOG.info("cookie value-" + value);
            if (name.equals(HISTORY_CARRIER_NUMBER_FILTER) && (value != null && value.length() > 0)) {
                carrierNumber = Integer.parseInt(value);
            }
            if (name.equals(HISTORY_CURRENT_LOCATION_FILTER) && (value != null && value.length() > 0)) {
                currentLocation = (Stop.findStop(Long.parseLong(value)));
            }
            if (name.equals(HISTORY_DEST_LOCATION_FILTER) && (value != null && value.length() > 0)) {
                destination = (Stop.findStop(Long.parseLong(value)));
            }
        }

        if (carrierNumber != null || carrierStatus != null || currentLocation != null || destination != null || die != null || press != null || productionRunNumber != null) {
            finderCriteria = new CarrierHistoryFinderCriteria();
            finderCriteria.setCarrierNumber(carrierNumber);
            finderCriteria.setCarrierStatus(carrierStatus);
            finderCriteria.setCurrentLocation(currentLocation);
            finderCriteria.setDestination(destination);
            finderCriteria.setDie(die);
            finderCriteria.setPress(press);
            finderCriteria.setProductionRunNo(productionRunNumber);
        }
        return finderCriteria;
    }

	private void saveCarrierHistoryFinderCriteria(CarrierHistoryFinderCriteria finderCriteria, HttpServletResponse httpServletResponse) {

        if (finderCriteria != null) {
            Integer carrierNumber = finderCriteria.getCarrierNumber();
            Stop currentLocation = finderCriteria.getCurrentLocation();
            Stop destination = finderCriteria.getDestination();

            String carrierNo = carrierNumber != null ? carrierNumber.toString() : "";
            String currentLoc = currentLocation != null ? currentLocation.getId().toString() : "";
            String destLoc = destination != null ? destination.getId().toString() : "";

            Cookie carrierNoCookie = new Cookie(HISTORY_CARRIER_NUMBER_FILTER, carrierNo);
            carrierNoCookie.setMaxAge(COOKIE_MAX_AGE);

            Cookie currentLocCookie = new Cookie(HISTORY_CURRENT_LOCATION_FILTER, currentLoc);
            currentLocCookie.setMaxAge(COOKIE_MAX_AGE);

            Cookie destLocCookie = new Cookie(HISTORY_DEST_LOCATION_FILTER, destLoc);
            destLocCookie.setMaxAge(COOKIE_MAX_AGE);

            httpServletResponse.addCookie(carrierNoCookie);
            httpServletResponse.addCookie(currentLocCookie);
            httpServletResponse.addCookie(destLocCookie);
        }
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }
}
