package com.honda.ahm.lc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honda.ahm.lc.enums.DefectStatus;
import com.honda.ahm.lc.enums.GalcDataType;
import com.honda.ahm.lc.model.ProcessPoint;
import com.honda.ahm.lc.model.QiDefectResult;
import com.honda.ahm.lc.model.QiRepairAreaSpace;
import org.json.JSONObject;
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service(value="naqDefectAndParkingService")
public class NaqDefectAndParkingService extends BaseGalcService<QiDefectResult, Long> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    protected Logger getLogger() {
    	  return logger;
    }
    public void createNaqDefectAndParking(String galcUrl, String productId, String naqDefectName, String processPointId, String repairArea) {
        createDefect(galcUrl, productId, naqDefectName, processPointId);
        List<QiDefectResult> defects = findDefects(galcUrl, productId, naqDefectName);
        if (defects.size() > 0) {
            long defectResultId = defects.get(0).getDefectResultId();
            QiRepairAreaSpace repairAreaSpace = getNaqParkingSpace(galcUrl, repairArea);
            if (repairAreaSpace != null) {
                updateNAQParking(galcUrl, defectResultId, repairAreaSpace, productId);
            } else {
                logger.error("No parking space left to update");
            }
        } else {
            logger.error("No Defect found for Factory Return");
        }
    }

    private void createDefect(String galcUrl, String productId, String naqDefectName, String processPointId) {
        List<JSONObject> jsonObjects = new ArrayList<>();

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("java.lang.String", productId);
        jsonObjects.add(jsonObject1);

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("java.lang.String", naqDefectName);
        jsonObjects.add(jsonObject2);

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("java.lang.String", processPointId);
        jsonObjects.add(jsonObject3);

        JSONObject jsonObject4 = new JSONObject();
        jsonObject4.put("java.lang.String", "90A"); // create user
        jsonObjects.add(jsonObject4);

        JSONObject jsonObject5 = new JSONObject();
        jsonObject5.put("java.lang.String", "VQ");
        jsonObjects.add(jsonObject5);

        ProcessPoint processPoint = getProcessPoint(galcUrl, processPointId);
        String div = processPoint!= null?processPoint.getDivisionId():"DIV";
        JSONObject jsonObject6 = new JSONObject();
        jsonObject6.put("java.lang.String", div);
        jsonObjects.add(jsonObject6);

        getRestTemplate().postForObject(
                getExternalSystemUrl(galcUrl, GalcDataType.QI_DEFECT.getDao(), "createDefect"),
                jsonObjects.toString(), Object.class);
    }

    @SuppressWarnings("unchecked")
	private List<QiDefectResult> findDefects(String galcUrl, String productId, String naqDefectName) {
        List<JSONObject> jsonObjects = new ArrayList<>();

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("java.lang.String", naqDefectName);
        jsonObjects.add(jsonObject1);

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("java.lang.String", productId);
        jsonObjects.add(jsonObject2);

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("java.lang.String", DefectStatus.NOT_FIXED.getId());
        jsonObjects.add(jsonObject3);

        List<LinkedHashMap> results = (List<LinkedHashMap>) getRestTemplate().postForObject(
                getExternalSystemUrl(galcUrl, GalcDataType.QI_DEFECT.getDao(), "createDefect"),
                jsonObjects.toString(), List.class);

        List<QiDefectResult> defectList = new ArrayList<>();
        for (LinkedHashMap m : results) {
            QiDefectResult st = new ObjectMapper().convertValue(m, QiDefectResult.class);
            defectList.add(st);
        }

        return defectList;
    }

    private ProcessPoint getProcessPoint(String galcUrl, String processPointId) {
        List<JSONObject> jsonObjects = new ArrayList<>();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("java.lang.String", processPointId);
        jsonObjects.add(jsonObject1);

        return getRestTemplate().postForObject(
                getExternalSystemUrl(galcUrl, GalcDataType.PROCESS_POINT.getDao(), "findById"),
                jsonObjects.toString(), ProcessPoint.class);
    }

    private QiRepairAreaSpace getNaqParkingSpace(String galcUrl, String repairArea) {
        List<JSONObject> jsonObjects = new ArrayList<>();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("java.lang.String", repairArea);
        jsonObjects.add(jsonObject1);

        return getRestTemplate().postForObject(
                getExternalSystemUrl(galcUrl, GalcDataType.QI_REPAIR_AREA.getDao(), "getNaqParkingSpace"),
                jsonObjects.toString(), QiRepairAreaSpace.class);
    }

    private void updateNAQParking(String galcUrl, long defectResultId, QiRepairAreaSpace qiRepairAreaSpace, String productId) {
        List<JSONObject> jsonObjects = new ArrayList<>();

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("java.lang.Long", defectResultId);
        jsonObjects.add(jsonObject1);

        JSONObject jsonObject2 = new JSONObject();
        jsonObject1.put("java.lang.String", productId);
        jsonObjects.add(jsonObject2);

        JSONObject jsonObject3 = new JSONObject();
        jsonObject1.put("java.lang.String", qiRepairAreaSpace.getId().getRepairAreaName());
        jsonObjects.add(jsonObject3);

        JSONObject jsonObject4 = new JSONObject();
        jsonObject1.put("java.lang.Integer", qiRepairAreaSpace.getId().getRepairArearSpace());
        jsonObjects.add(jsonObject4);

        JSONObject jsonObject5 = new JSONObject();
        jsonObject1.put("java.lang.Integer", qiRepairAreaSpace.getId().getRepairArearRow());
        jsonObjects.add(jsonObject5);

        JSONObject jsonObject6 = new JSONObject();
        jsonObject1.put("java.lang.String", "90A");
        jsonObjects.add(jsonObject6);

        getRestTemplate().postForObject(
                getExternalSystemUrl(galcUrl, GalcDataType.QI_REPAIR_AREA.getDao(), "updateNAQParking"),
                jsonObjects.toString(), Object.class);
    }

}
