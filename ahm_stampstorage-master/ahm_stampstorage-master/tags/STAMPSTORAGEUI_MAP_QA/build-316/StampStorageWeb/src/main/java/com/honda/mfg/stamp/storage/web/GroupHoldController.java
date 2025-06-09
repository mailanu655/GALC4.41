package com.honda.mfg.stamp.storage.web;


import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Defect;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.GroupHoldFinderCriteria;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.DEFECT_TYPE;
import com.honda.mfg.stamp.conveyor.domain.enums.Press;
import com.honda.mfg.stamp.conveyor.domain.enums.REWORK_METHOD;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.storage.service.CarrierManagementService;
import com.honda.mfg.stamp.storage.service.CarrierManagementServiceProxy;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by IntelliJ IDEA.
 * User: vcc30690
 * Date: 2/1/12
 * Time: 3:26 PM
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/groupholds")
@Controller
public class GroupHoldController {
     private static final Logger LOG = LoggerFactory.getLogger(GroupHoldController.class);


    private static final String HOLD_STATUS_FILTER="statusFilter";
    private static final String HOLD_ROW_FILTER = "rowFilter";
    private static final String HOLD_NUMBER_AFTER_FILTER = "numberAfterFilter";
    private static final String HOLD_NUMBER_BEFORE_FILTER = "numberBeforeFilter";
    private static final String HOLD_RUN_NUMBER_FILTER = "prodRunNumberFilter";
    private static final String HOLD_RUN_DATE_FILTER = "prodRunDateFilter";
    private static final String HOLD_DEFECT_COUNT = "defectCount";
    private static final String HOLD_DEFECT = "defect_";
    private static final String HOLD_ROW_PRODRUN_FILTER = "rowAndProdRun";
    private static final String HOLD_ROBOT_FILTER = "robot";
    private static final int COOKIE_MAX_AGE =900;


    @Autowired
    CarrierManagementService carrierManagementService;
    @Autowired
    CarrierManagementServiceProxy carrierManagementServiceProxy;


	@RequestMapping(params = "ByRowAndProductionRunNoAndProductionRunDate", method = RequestMethod.GET)
    public String findCarrierByCriteriaform(Model uiModel, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        uiModel.addAttribute("rows", StorageRow.findAllStorageRows());
        uiModel.addAttribute("productionRunNumbers", CarrierMes.getProductionRunNumbers());
        uiModel.addAttribute("carrierstatuses", getCarrierStatuses());
        uiModel.addAttribute("robots", getPressRobotList());
        boolean mesHealthy = !carrierManagementService.isDisconnected();
        uiModel.addAttribute("meshealthy", mesHealthy);
        uiModel.addAttribute("alarmevent", carrierManagementService.getAlarmEventToDisplay());

        clearAllDefects(httpServletRequest, httpServletResponse);

        return "groupholds/list";
    }

	@RequestMapping(value = "/{carriernumber}", params = "ByRowAndProductionRunNoAndProductionRunDate", method = RequestMethod.GET)
    public String findCarrierByCriteriaform(@PathVariable("carriernumber") Integer carrierNumber, Model uiModel, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(carrierNumber);

        Press press = Press.findByType(carrierMes.getOriginationLocation().intValue());
        List<Press> pressList = new ArrayList<Press>();
        pressList.add(press);

        uiModel.addAttribute("rows", StorageRow.findAllStorageRows());
        uiModel.addAttribute("productionRunNumbers", CarrierMes.getProductionRunNumbers());
        uiModel.addAttribute("carrierstatuses", getCarrierStatuses());
        uiModel.addAttribute("robots", pressList);

        if (carrierMes != null) {

            uiModel.addAttribute("prodrunnumber", carrierMes.getProductionRunNumber());
            Stop currentLocation = Stop.findStop(carrierMes.getCurrentLocation());
            if (currentLocation.isRowStop()) {
                uiModel.addAttribute("row", StorageRow.findStorageRowsByStop(currentLocation));
                SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
                String runDate = SDF.format(carrierMes.getProductionRunDate());
                uiModel.addAttribute("prodrundate", runDate);
            } else {
                SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
                String runDate = SDF.format(carrierMes.getProductionRunDate());
                uiModel.addAttribute("prodrundate", runDate);
            }
        }
        clearAllDefects(httpServletRequest, httpServletResponse);
        boolean mesHealthy = !carrierManagementService.isDisconnected();
        uiModel.addAttribute("meshealthy", mesHealthy);
        uiModel.addAttribute("alarmevent", carrierManagementService.getAlarmEventToDisplay());

        return "groupholds/list";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String carrierlist(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        GroupHoldFinderCriteria finderCriteria = getGroupHoldFinderCriteria(httpServletRequest);

        List<Carrier> carrierList = carrierManagementService.getGroupHoldCarriers(finderCriteria, page, 12);
        saveFinderCriteria(finderCriteria, httpServletResponse);

        boolean allowDefectAdd = false;

        if (finderCriteria.getStatus().equals(CarrierStatus.INSPECTION_REQUIRED) || finderCriteria.getStatus().equals(CarrierStatus.ON_HOLD)) {
            allowDefectAdd = true;
            List<Defect> defectList = getDefectList(httpServletRequest);
            uiModel.addAttribute("defects", defectList);
            if (defectList.size() == 0) {
                uiModel.addAttribute("defectmessage", "Defects  Required ");
            }
        }
        List<Defect> defectList = getDefectList(httpServletRequest);
        uiModel.addAttribute("carriers", carrierList);
        uiModel.addAttribute("defects", defectList);
        uiModel.addAttribute("allowDefectAdd", allowDefectAdd);
        uiModel.addAttribute("finderCriteria", finderCriteria);

        String message = getCarriersInWeld(carrierList);
        uiModel.addAttribute("message", message);

        boolean mesHealthy = !carrierManagementService.isDisconnected();
        uiModel.addAttribute("meshealthy", mesHealthy);
        uiModel.addAttribute("alarmevent", carrierManagementService.getAlarmEventToDisplay());


        float nrOfPages = (float) CarrierMes.getGroupHoldCarrierCount(finderCriteria) / 12;
        if (nrOfPages > 20) {
            uiModel.addAttribute("maxPages", 20);
        } else {
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        }
        return "groupholds/carrierlist";
    }

	@RequestMapping(params = "find=ByRowAndProductionRunNoAndProductionRunDate", method = RequestMethod.GET)
    public String findCarrierByCriteria(@RequestParam("row") Long row, @RequestParam("radio1") String radio, @RequestParam(value = "productionRunNumber", required = true) Integer productionRunNumber, @RequestParam("robot") Press robot, @RequestParam("productionRunDate") String productionRunDate, @RequestParam("numberAfterRunDate") Integer numberAfterRunDate, @RequestParam("numberBeforeRunDate") Integer numberBeforeRunDate, @RequestParam("carrierStatus") CarrierStatus status, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model uiModel) {
        if (productionRunNumber == null) {
            uiModel.addAttribute("message", "productionRunNumber cannot be blank");
            return "redirect:/groupholds?ByRowAndProductionRunNoAndProductionRunDate=-";
        }

        LOG.debug(" find carrier By Criteria");
        GroupHoldFinderCriteria finderCriteria = new GroupHoldFinderCriteria();

        finderCriteria.setRow(StorageRow.findStorageRow(row));
        finderCriteria.setProductionRunDate(getRunDate(productionRunDate));
        finderCriteria.setProductionRunNumber(productionRunNumber);
        finderCriteria.setNumberAfterRunDate(numberAfterRunDate);
        finderCriteria.setNumberBeforeRunDate(numberBeforeRunDate);
        finderCriteria.setStatus(status);
        finderCriteria.setRobot(robot);
        finderCriteria.setRowAndProdRun(Boolean.parseBoolean(radio));
        clearAllDefects(httpServletRequest, httpServletResponse);

        List<Carrier> carrierList = carrierManagementService.getGroupHoldCarriers(finderCriteria, 1, 12);

        saveFinderCriteria(finderCriteria, httpServletResponse);

        boolean allowDefectAdd = false;

        if (status.equals(CarrierStatus.INSPECTION_REQUIRED) || status.equals(CarrierStatus.ON_HOLD)) {
            allowDefectAdd = true;
            List<Defect> defectList = getDefectList(httpServletRequest);
            uiModel.addAttribute("defects", defectList);
            if (defectList.size() == 0) {
                uiModel.addAttribute("defectmessage", "Defects  Required ");
            }
        }
        uiModel.addAttribute("carriers", carrierList);
        uiModel.addAttribute("allowDefectAdd", allowDefectAdd);
        uiModel.addAttribute("finderCriteria", finderCriteria);

        String message = getCarriersInWeld(carrierList);
        uiModel.addAttribute("message", message);

        boolean mesHealthy = !carrierManagementService.isDisconnected();
        uiModel.addAttribute("meshealthy", mesHealthy);
        uiModel.addAttribute("alarmevent", carrierManagementService.getAlarmEventToDisplay());


        float nrOfPages = (float) CarrierMes.getGroupHoldCarrierCount(finderCriteria) / 12;
        if (nrOfPages > 20) {
            uiModel.addAttribute("maxPages", 20);
        } else {
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        }
        return "groupholds/carrierlist";
    }

	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createDefectForm(Model uiModel, HttpServletRequest httpServletRequest) {
        Defect defect = new Defect();
        String imgUrl = "";
        String imageName = "";

        GroupHoldFinderCriteria finderCriteria = getGroupHoldFinderCriteria(httpServletRequest);
        Die die = CarrierMes.getDieByProductionRunNumber(finderCriteria.getProductionRunNumber());

        if (die != null) {
            imageName = die.getDescription();
            imgUrl = "/resources/images/" + die.getImageFileName();
        }
        uiModel.addAttribute("defect", defect);
        uiModel.addAttribute("imgUrl", imgUrl);
        uiModel.addAttribute("imgLabel", imageName);

        return "groupholds/createdefect";
    }

	@RequestMapping(method = RequestMethod.POST)
    public String create(Defect defect, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        saveDefect(defect, httpServletRequest, httpServletResponse);
        GroupHoldFinderCriteria finderCriteria = getGroupHoldFinderCriteria(httpServletRequest);
        float nrOfPages = (float) CarrierMes.getGroupHoldCarrierCount(finderCriteria) / 12;
        if (nrOfPages > 20) {
            uiModel.addAttribute("maxPages", 20);
        } else {
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        }
        uiModel.addAttribute("finderCriteria", finderCriteria);
        uiModel.addAttribute("page", 1);
        uiModel.addAttribute("size", 12);
        return "redirect:/groupholds";
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateDefectForm(@PathVariable("id") Long id, Model uiModel, HttpServletRequest httpServletRequest) {
        String imgUrl = "";
        String imageName = "";

        GroupHoldFinderCriteria finderCriteria = getGroupHoldFinderCriteria(httpServletRequest);
        Die die = CarrierMes.getDieByProductionRunNumber(finderCriteria.getProductionRunNumber());

        if (die != null) {
            imageName = die.getDescription();
            imgUrl = "/resources/images/" + die.getImageFileName();
        }

        uiModel.addAttribute("imgUrl", imgUrl);
        uiModel.addAttribute("imgLabel", imageName);
        Defect defect = getDefect(id, httpServletRequest);
        uiModel.addAttribute("defect", defect);

        return "groupholds/updatedefect";
    }

	@RequestMapping(method = RequestMethod.PUT)
    public String update(Defect defect, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        LOG.debug("attempting to update defect");
        updateDefect(defect, httpServletRequest, httpServletResponse);

        GroupHoldFinderCriteria finderCriteria = getGroupHoldFinderCriteria(httpServletRequest);
        float nrOfPages = (float) CarrierMes.getGroupHoldCarrierCount(finderCriteria) / 12;
        if (nrOfPages > 20) {
            uiModel.addAttribute("maxPages", 20);
        } else {
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        }
        uiModel.addAttribute("finderCriteria", finderCriteria);
        uiModel.addAttribute("page", 1);
        uiModel.addAttribute("size", 12);
        return "redirect:/groupholds";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, Model uiModel, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        LOG.debug("attempting to delete defect-" + id);
        deleteDefect(id, httpServletRequest, httpServletResponse);

        return "redirect:/groupholds";
    }

	@RequestMapping(params = "find=submitgrouphold", method = RequestMethod.GET)
    public String submitGroupHold(Model uiModel, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        GroupHoldFinderCriteria finderCriteria = getGroupHoldFinderCriteria(httpServletRequest);
        List<Defect> defectList = getDefectList(httpServletRequest);

        if (finderCriteria.getStatus().equals(CarrierStatus.INSPECTION_REQUIRED) || finderCriteria.getStatus().equals(CarrierStatus.ON_HOLD)) {
            if (defectList.size() == 0) {
                uiModel.addAttribute("page", 1);
                uiModel.addAttribute("size", 12);
                return "redirect:/groupholds";
            }
        }
        String user = httpServletRequest.getUserPrincipal().getName();
        List<Carrier> carrierList = carrierManagementService.getGroupHoldCarriers(finderCriteria, null, null);
        //carrierManagementService.sendBulkCarrierStatusUpdate(carrierList, finderCriteria.getStatus(), user);
        carrierManagementServiceProxy.sendBulkCarrierStatusUpdate(carrierList, finderCriteria.getStatus(), user);
        try {
            if (defectList.size() > 0) {  
                for (Carrier carrier : carrierList) {
                    for (Defect defect : defectList) {
                        LOG.debug(" persisting defect for carrier - " + carrier.getCarrierNumber());
                        Defect tempDefect = new Defect();
                        tempDefect.setDefectType(defect.getDefectType());
                        tempDefect.setReworkMethod(defect.getReworkMethod());
                        tempDefect.setXArea(defect.getXArea());
                        tempDefect.setYArea(defect.getYArea());
                        tempDefect.setCarrierNumber(carrier.getCarrierNumber());
                        tempDefect.setProductionRunNo(carrier.getProductionRunNo());
                        tempDefect.setDefectTimestamp(new Date(System.currentTimeMillis()));
                        tempDefect.setSource(user);
                        tempDefect.setNote(defect.getNote());
                        tempDefect.persist();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        clearAllDefects(httpServletRequest, httpServletResponse);
        StorageRow row = finderCriteria.getRow();
        if (row != null) {
            return "redirect:/lanes";
        }

        return "redirect:/carriers";
    }

	@ModelAttribute("defect_types")
    public Collection<DEFECT_TYPE>populateDEFECT_TYPEs() {
        return Arrays.asList(DEFECT_TYPE.class.getEnumConstants());
    }

	@ModelAttribute("rework_methods")
    public Collection<REWORK_METHOD>populateREWORK_METHODs() {
        return Arrays.asList(REWORK_METHOD.class.getEnumConstants());
    }

	private void saveDefect(Defect defect, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Cookie cookies[] = httpServletRequest.getCookies();
        String name = "";
        String value = "";

        Integer defectCount = 0;
        for (Cookie cookie : cookies) {
            name = cookie.getName();
            value = cookie.getValue();

            if (name.equals(HOLD_DEFECT_COUNT)) {
                defectCount = new Integer(Integer.parseInt(value));
                break;
            }
        }
        defectCount = defectCount + 1;
        String defectString = defect.getDefectType().type() + "-" + defect.getReworkMethod().method() + "-" + defect.getXArea().toString() + "-" + defect.getYArea() + "-" + defect.getNote();
        String cookieName = HOLD_DEFECT + defectCount.intValue();

        LOG.debug("saving defect-" + cookieName + "-" + defectString);
        Cookie defectCookie = new Cookie(cookieName, defectString);
        defectCookie.setMaxAge(COOKIE_MAX_AGE);

        Cookie defectCountCookie = new Cookie(HOLD_DEFECT_COUNT, defectCount.toString());
        defectCountCookie.setMaxAge(COOKIE_MAX_AGE);

        LOG.debug("setting defect cookie-" + defectCookie.getValue() + "-" + defectCountCookie.getValue());
        httpServletResponse.addCookie(defectCookie);
        httpServletResponse.addCookie(defectCountCookie);
    }

	private void updateDefect(Defect defect, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            long id = defect.getId();

            String defectString = defect.getDefectType().type() + "-" + defect.getReworkMethod().method() + "-" + defect.getXArea().toString() + "-" + defect.getYArea();
            String cookieName = HOLD_DEFECT + id;

            LOG.debug("updating defect-" + cookieName + "-" + defectString);
            Cookie defectCookie = new Cookie(cookieName, defectString);
            defectCookie.setMaxAge(COOKIE_MAX_AGE);

            LOG.debug("setting defect cookie-" + defectCookie.getValue());
            httpServletResponse.addCookie(defectCookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	private void deleteDefect(Long id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Cookie cookies[] = httpServletRequest.getCookies();
        String name = "";

        String cookieName = HOLD_DEFECT + id;

        for (Cookie cookie : cookies) {
            name = cookie.getName();
            if (name.equals(cookieName)) {
                LOG.debug("clearing defect cookie-" + name + "-" + cookie.getValue());
                cookie.setValue("blank");
                cookie.setMaxAge(1);
                LOG.debug("clearing defect cookie-" + name + "-" + cookie.getValue());
                httpServletResponse.addCookie(cookie);
            }
        }
    }

	private GroupHoldFinderCriteria getGroupHoldFinderCriteria(HttpServletRequest httpServletRequest) {
        Cookie cookies[] = httpServletRequest.getCookies();
        String name = "";
        String value = "";
        Integer prodRunNumber = null;
        Timestamp prodRunDate = null;
        StorageRow row = null;
        Integer numberAfter = null;
        Integer numberBefore = null;
        CarrierStatus status = null;
        Press robot = null;
        Boolean rowWithProdRun = null;
        for (Cookie cookie : cookies) {
            name = cookie.getName();
            value = cookie.getValue();
            if (value.length() > 0) {
                if (name.equals(HOLD_ROW_FILTER)) {
                    row = StorageRow.findStorageRow(Long.parseLong(value));
                }
                if (name.equals(HOLD_RUN_DATE_FILTER)) {
                    prodRunDate = getRunDate(value);
                }
                if (name.equals(HOLD_RUN_NUMBER_FILTER)) {
                    prodRunNumber = new Integer(Integer.parseInt(value));
                }
                if (name.equals(HOLD_NUMBER_AFTER_FILTER)) {
                    numberAfter = new Integer(Integer.parseInt(value));
                }
                if (name.equals(HOLD_NUMBER_BEFORE_FILTER)) {
                    numberBefore = new Integer(Integer.parseInt(value));
                }
                if (name.equals(HOLD_STATUS_FILTER)) {
                    status = CarrierStatus.findByType(Integer.parseInt(value));
                }
                if (name.equals(HOLD_ROBOT_FILTER)) {
                    robot = Press.findByType(Integer.parseInt(value));
                }
                if (name.equals(HOLD_ROW_PRODRUN_FILTER)) {
                    rowWithProdRun = Boolean.parseBoolean(value);
                }
            }
        }

        GroupHoldFinderCriteria finderCriteria = new GroupHoldFinderCriteria();
        finderCriteria.setRow(row);
        finderCriteria.setProductionRunNumber(prodRunNumber);
        finderCriteria.setProductionRunDate(prodRunDate);
        finderCriteria.setNumberAfterRunDate(numberAfter);
        finderCriteria.setNumberBeforeRunDate(numberBefore);
        finderCriteria.setStatus(status);
        finderCriteria.setRobot(robot);
        finderCriteria.setRowAndProdRun(rowWithProdRun);
        return finderCriteria;
    }

	private void saveFinderCriteria(GroupHoldFinderCriteria finderCriteria, HttpServletResponse httpServletResponse) {

        String row = finderCriteria.getRow() == null ? "" : finderCriteria.getRow().getId().toString();
        Cookie rowCookie = new Cookie(HOLD_ROW_FILTER, row);
        rowCookie.setMaxAge(COOKIE_MAX_AGE);

        String runNumber = finderCriteria.getProductionRunNumber() == null ? "" : finderCriteria.getProductionRunNumber().toString();
        Cookie runNumberCookie = new Cookie(HOLD_RUN_NUMBER_FILTER, runNumber);
        runNumberCookie.setMaxAge(COOKIE_MAX_AGE);

        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
        String runDate = finderCriteria.getProductionRunDate() == null ? "" : SDF.format(finderCriteria.getProductionRunDate());
        Cookie runDateCookie = new Cookie(HOLD_RUN_DATE_FILTER, runDate);
        runDateCookie.setMaxAge(COOKIE_MAX_AGE);

        String numberAfter = finderCriteria.getNumberAfterRunDate() == null ? "" : finderCriteria.getNumberAfterRunDate().toString();
        Cookie numberAfterCookie = new Cookie(HOLD_NUMBER_AFTER_FILTER, numberAfter);
        numberAfterCookie.setMaxAge(COOKIE_MAX_AGE);

        String numberBefore = finderCriteria.getNumberBeforeRunDate() == null ? "" : finderCriteria.getNumberBeforeRunDate().toString();
        Cookie numberBeforeCookie = new Cookie(HOLD_NUMBER_BEFORE_FILTER, numberBefore);
        numberBeforeCookie.setMaxAge(COOKIE_MAX_AGE);

        String carrierStatus = finderCriteria.getStatus() == null ? "" : (new Integer(finderCriteria.getStatus().type())).toString();
        Cookie carrierStatusCookie = new Cookie(HOLD_STATUS_FILTER, carrierStatus);
        carrierStatusCookie.setMaxAge(COOKIE_MAX_AGE);

        String robot = finderCriteria.getRobot() == null ? "" : (new Integer(finderCriteria.getRobot().type())).toString();
        Cookie robotCookie = new Cookie(HOLD_ROBOT_FILTER, robot);
        robotCookie.setMaxAge(COOKIE_MAX_AGE);

        String rowWithProdRun = finderCriteria.getRowAndProdRun() == null ? "" : finderCriteria.getRowAndProdRun().toString();
        Cookie rowWithProdRunCookie = new Cookie(HOLD_ROW_PRODRUN_FILTER, rowWithProdRun);
        rowWithProdRunCookie.setMaxAge(COOKIE_MAX_AGE);

        httpServletResponse.addCookie(rowCookie);
        httpServletResponse.addCookie(runNumberCookie);
        httpServletResponse.addCookie(runDateCookie);
        httpServletResponse.addCookie(numberAfterCookie);
        httpServletResponse.addCookie(numberBeforeCookie);
        httpServletResponse.addCookie(carrierStatusCookie);
        httpServletResponse.addCookie(robotCookie);
        httpServletResponse.addCookie(rowWithProdRunCookie);
    }

	private List<Defect>getDefectList(HttpServletRequest httpServletRequest) {
        List<Defect> defectList = new ArrayList<Defect>();
        Cookie cookies[] = httpServletRequest.getCookies();
        String name = "";
        String value = "";
        Integer count = 0;
        List<Cookie> defectCookies = new ArrayList<Cookie>();

        for (Cookie cookie : cookies) {
            name = cookie.getName();
            value = cookie.getValue();
//            LOG.debug("Cookies found-" + name + "-" + value);
            if (value.length() > 0) {

                if (name.startsWith(HOLD_DEFECT)) {
                    LOG.debug("Defect found-" + name);
                    defectCookies.add(cookie);
                }
                if (name.equals(HOLD_DEFECT_COUNT)) {
                    count = Integer.parseInt(value);
                }
            }
        }
        long id = 0L;
        if (count > 0) {
            for (Cookie defectCookie : defectCookies) {

                String defectString = defectCookie.getValue();
                System.out.println("Defect String-" + defectString);
                try {
                    if (defectString.length() > 0) {
                        if (!defectString.equals("blank")) {
                            List<String> defectValues = new ArrayList<String>();
                            StringTokenizer st = new StringTokenizer(defectString, "-");
                            System.out.println(" token count-" + st.countTokens());
                            while (st.hasMoreTokens()) {
                                String token = st.nextToken();
                                System.out.println("token-" + token);
                                defectValues.add(token);
                            }

                            if (defectValues.size() > 0) {
                                DEFECT_TYPE defectType = DEFECT_TYPE.findByType(Integer.parseInt(defectValues.get(0)));
                                REWORK_METHOD reworkMethod = defectValues.size() > 1 ? REWORK_METHOD.findByMethod(Integer.parseInt(defectValues.get(1))) : null;
                                Integer x_area = defectValues.size() > 2 ? new Integer(Integer.parseInt(defectValues.get(2))) : new Integer(0);
                                String y_area = defectValues.size() > 3 ? defectValues.get(3) : "";
                                String note = defectValues.size() > 4 ? defectValues.get(4) : "";

                                Defect defect = new Defect();
                                defect.setDefectType(defectType);
                                defect.setReworkMethod(reworkMethod);
                                defect.setXArea(x_area);
                                defect.setYArea(y_area);
                                defect.setNote(note);
                                id++;
                                defect.setId(id);
                                LOG.debug("Defect Added-" + defect);
                                defectList.add(defect);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return defectList;
    }

	private Timestamp getRunDate(String runDate) {
        Timestamp timestamp = null;
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
        try {
            if (runDate.length() > 0) {
                Date date = SDF.parse(runDate);
                timestamp = new Timestamp(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

	private void clearAllDefects(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        Cookie cookies[] = httpServletRequest.getCookies();
        String name = "";
        List<Cookie> defectCookies = new ArrayList<Cookie>();

        for (Cookie cookie : cookies) {
            name = cookie.getName();
            if (name.startsWith(HOLD_DEFECT)) {
                defectCookies.add(cookie);
            }
        }

        for (Cookie defectCookie : defectCookies) {
            defectCookie.setValue("");
            defectCookie.setMaxAge(0);
            httpServletResponse.addCookie(defectCookie);
        }

        Cookie defectCountCookie = new Cookie(HOLD_DEFECT_COUNT, "0");
        defectCountCookie.setMaxAge(COOKIE_MAX_AGE);

        httpServletResponse.addCookie(defectCountCookie);
    }

	private Defect getDefect(Long id, HttpServletRequest httpServletRequest) {

        Cookie cookies[] = httpServletRequest.getCookies();
        String name = "";
        String value = "";
        Cookie defectCookie = null;

        for (Cookie cookie : cookies) {
            name = cookie.getName();
            value = cookie.getValue();
//            LOG.debug("Cookies found-" + name + "-" + value);
            if (value.length() > 0) {

                if (name.startsWith(HOLD_DEFECT) && name.endsWith(id.toString())) {
//                    LOG.debug("Defect found-" + name);
                    defectCookie = cookie;
                    break;
                }
            }
        }
        Defect defect = new Defect();
        if (defectCookie != null) {

            String defectString = defectCookie.getValue();
//            LOG.debug("Defect String-" + defectString);
            if (defectString.length() > 0) {
                List<String> defectValues = new ArrayList<String>();
                StringTokenizer st = new StringTokenizer(defectString, "-");
                System.out.println(" token count-" + st.countTokens());
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    System.out.println("token-" + token);
                    defectValues.add(token);
                }

                if (defectValues.size() > 0) {
                    DEFECT_TYPE defectType = DEFECT_TYPE.findByType(Integer.parseInt(defectValues.get(0)));
                    REWORK_METHOD reworkMethod = defectValues.size() > 1 ? REWORK_METHOD.findByMethod(Integer.parseInt(defectValues.get(1))) : null;
                    Integer x_area = defectValues.size() > 2 ? new Integer(Integer.parseInt(defectValues.get(2))) : new Integer(0);
                    String y_area = defectValues.size() > 3 ? defectValues.get(3) : "";
                    String note = defectValues.size() > 4 ? defectValues.get(4) : "";

                    defect.setDefectType(defectType);
                    defect.setReworkMethod(reworkMethod);
                    defect.setXArea(x_area);
                    defect.setYArea(y_area);
                    defect.setNote(note);
                    defect.setId(id);
                }
            }
        }

        return defect;
    }

	private List<CarrierStatus>getCarrierStatuses() {
        List<CarrierStatus> carrierStatusList = new ArrayList<CarrierStatus>();
        carrierStatusList.add(CarrierStatus.INSPECTION_REQUIRED);
        carrierStatusList.add(CarrierStatus.ON_HOLD);
        carrierStatusList.add(CarrierStatus.SHIPPABLE);

        return carrierStatusList;
    }

	//    private List<String> GroupHoldController.getRows() {
//        List<String> rowNames = new ArrayList<String>();
//        List<StorageRow> rows = StorageRow.findAllStorageRows();
//
//        for (StorageRow row : rows) {
//            rowNames.add(row.getRowName());
//        }
//
//        return rowNames;
//    }

    private StorageRow getRow(String name) {
        List<StorageRow> rows = StorageRow.findAllStorageRows();
        StorageRow requestedRow = null;
        for (StorageRow row : rows) {
            if (row.getRowName().equals(name)) {
                requestedRow = row;
                break;
            }
        }
        return requestedRow;
    }

	private List<Press>getPressRobotList() {
        List<Press> pressList = new ArrayList<Press>();
        pressList.add(Press.PRESS_C_ROBOT_1);
        pressList.add(Press.PRESS_C_ROBOT_2);
        pressList.add(Press.PRESS_B_ROBOT_1);
        pressList.add(Press.PRESS_B_ROBOT_2);

        return pressList;
    }

	private String getCarriersInWeld(List<Carrier> carrierList) {
        String msg = "";
        List<Carrier> carriersInWeld = new ArrayList<Carrier>();

        for (Carrier carrier : carrierList) {
            if (carrier.getCurrentLocation().getStopArea().equals(StopArea.WELD_LINE_1) || carrier.getCurrentLocation().getStopArea().equals(StopArea.WELD_LINE_2)) {
                carriersInWeld.add(carrier);
            }
        }

        if (carriersInWeld.size() > 0) {
            msg = " Carriers In Weld Area -- ";
            for (Carrier c : carriersInWeld) {
                msg = msg + c.getCarrierNumber() + ",";
            }
        }

        return msg;
    }
}
