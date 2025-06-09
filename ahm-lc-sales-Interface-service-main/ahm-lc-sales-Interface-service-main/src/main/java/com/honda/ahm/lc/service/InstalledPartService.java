package com.honda.ahm.lc.service;

import com.honda.ahm.lc.enums.GalcDataType;
import com.honda.ahm.lc.enums.InstalledPartStatus;
import com.honda.ahm.lc.model.InstalledPart;
import org.json.JSONObject;
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "installedPartService")
public class InstalledPartService extends BaseGalcService<InstalledPart, String> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected Logger getLogger() {
    	  return logger;
    }

    public void updateInstalledPartStatus(String galcUrl, String productId, List<String> partNames, InstalledPartStatus installedPartStatus) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            List<JSONObject> jsonObjects = new ArrayList<>();

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("java.lang.String", productId);
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("java.util.List", partNames);
            JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("java.lang.Integer", installedPartStatus.getId());

            jsonObjects.add(jsonObject1);
            jsonObjects.add(jsonObject2);
            jsonObjects.add(jsonObject3);

            Integer response = getRestTemplate().postForObject(
                    getExternalSystemUrl(galcUrl,  GalcDataType.INSTALLED_PART.getDao(), "updateInstalledPartStatus"),
                    jsonObjects.toString(), Integer.class);
            getLogger().info("Updated installed part status for product "+productId+" with response "+ response);
        } catch (Exception e) {
            getLogger().error("Error updating installed part status or product {} ", productId);
            getLogger().error(e.getMessage());
        }
    }

}
