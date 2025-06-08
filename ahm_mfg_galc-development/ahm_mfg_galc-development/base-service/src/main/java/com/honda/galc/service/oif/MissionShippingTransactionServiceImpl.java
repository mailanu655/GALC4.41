package com.honda.galc.service.oif;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.product.ExceptionalOutDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.dto.oif.MissionShippingTransactionDataDto;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.service.ServiceFactory;

public class MissionShippingTransactionServiceImpl implements IMissionShippingTransactionService {
	private static final String OK_MISION = "MOK";
	private static final String SCRAPPED_MISSION = "SCR";
	private static final String HOLD_NOW_MISSION = "NOW";
	private static final String HOLD_AT_SHIPPING_MISSION = "TQH";
	private Logger logger =	Logger.getLogger("MissionShippingTransactionServiceImpl");

	@Override
	public boolean sendMission50AShippingTransaction(String url,
			BaseProduct product, String processPoint) {
		
		MissionShippingTransactionDataDto data = new MissionShippingTransactionDataDto();
		data.setProductId(product.getProductId());
		data.setProcess("OFF");
		String status = OK_MISION;
		
		//Search for existing scrap for the given product id.
		List<ExceptionalOut> scraps = ServiceFactory.getDao(ExceptionalOutDao.class).findAllByProductId(product.getProductId());
		if(null != scraps && scraps.size() > 0){
			status = SCRAPPED_MISSION;
		}
		
		//Search for existing holds for the given product id.
		List<HoldResult> holdResults = ServiceFactory.getDao(HoldResultDao.class).findAllByProductAndReleaseFlag(product.getProductId(), false, HoldResultType.GENERIC_HOLD);
		if (null!=holdResults && holdResults.size()>0) {
			status = HOLD_AT_SHIPPING_MISSION;
			for (HoldResult holdResult : holdResults) {
				//If there is at least one hold with hold type = 0, then status is set to "NOW", 
				//meaning hold "now" has priority over "at shipping" hold.
				if(holdResult.getId().getHoldType() == 0){
					status = HOLD_NOW_MISSION;
					break;
				}
			}
		}
		data.setStatus(status);
		OutputStream os = null;
		BufferedReader br = null;
		try {
			URL hostUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) hostUrl.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			
			ObjectMapper mapper = new ObjectMapper();
			
			String strData = mapper.writeValueAsString(data);
			
			os = conn.getOutputStream();
			os.write(strData.getBytes());
			os.flush();
			
			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED && conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				logger.error("Failed : HTTP error code : "
					+ conn.getResponseCode());
				return false;
			}
			
			
			br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				output += br.readLine() + "\n";
			}
			logger.info("Response from server: "+url+":\n" + output);

			ShippingStatus shippingStatus = new ShippingStatus();
			shippingStatus.setVin(product.getProductId());
			shippingStatus.setInvoiced("N");
			shippingStatus.setStatus(1);
			
			ShippingStatusDao shippingStatusDao = ServiceFactory.getDao(ShippingStatusDao.class);
			try {
				shippingStatusDao.save(shippingStatus);
				logger.info("ShippingStatus correctly inserted for Product Id" + product.getProductId());
			} catch (Exception e) {
				logger.error("An error ocurred when trying to save shipping status for Product Id: " + product.getProductId() +" \nException: "+e.getMessage());
			}
			
			conn.disconnect();
			logger.info("Shipping transaction was correctly performed for product id:"+ product.getProductId());
			
		} catch (Exception e) {
			logger.error("An error was produced when trying to send shipping transaction for: " +product.getProductId() +". Exception: " + e.getLocalizedMessage());
			return false;
		} finally{
			try {
				br.close();
				os.close();
			} catch (IOException e) {logger.error(e.getMessage());}
		}
		return true;
	}
}
