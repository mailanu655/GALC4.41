package com.honda.galc.service.mq.receiving;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.ProductSpecUtil;
/**
 * 
 * 
 * <h3>ReceivingEngineManifestServiceImpl Class description</h3>
 * <p> ReceivingEngineManifestServiceImpl description </p>
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
 * @author hcm_adm_008925<br>
 * Mar 16, 2018
 *
 *
 */
public class EngineManifestReceivingServiceImpl extends AbstractMessageReceivingServiceImpl {
	
	public EngineManifestReceivingServiceImpl() {
		super("ENGINE_MANIFEST_QUEUE");
	}

	@Override
	public void onMessage(Message message) {
		if(message instanceof TextMessage) {
			try {
				
				String msg = ((TextMessage)message).getText();
				getLogger().info("Receiving Text Msg " + msg);
				
				saveEngine(msg);
				
			}catch(JMSException ex) {
				getLogger().error(ex,"Unable to save engine data due to " + ex.getMessage());
			}
		}
	}
	
	@Transactional
	public void saveEngine(String message) {
		
		// 0-4 ENGINE_NO,MODEL_YEAR_CODE,ENGINE_MODEL,ENGINE_TYPE,ENGINE_OPTION
		// 5-9 MISSION_NO,COMPANY,PLANT,ENGINE_KD_LOT,ENGINE_FIRED_IND
		// 10-11 ENGINE_SOURCE,UNSHIP_STATUS
				
		
		String[] msgs = message.split(Delimiter.COMMA);
		String engineSn = msgs[0];
		
		Engine engine = ServiceFactory.getDao(EngineDao.class).findByKey(engineSn);
		
		if (engine == null) {
			engine = new Engine();
			engine.setProductId(engineSn);
		}
		engine.setProductionLot("");
		engine.setProductSpecCode(getProductSpecCode(msgs)); 
		engine.setMissionSerialNo(msgs[5]); // mission no
		engine.setKdLotNumber(msgs[8]);
		engine.setEngineFiringFlag(("1".equals(msgs[9]) ? (short)1 : 0));
		engine.setPlantCode(msgs[10]);
	
		ServiceFactory.getDao(EngineDao.class).save(engine);
		
		getLogger().info("engine manifest data is saved " + engine);
	}
	
	private String getProductSpecCode(String[] messages) {
		String productSpecCode = "";
		productSpecCode +=messages[1];                                // model year code
		productSpecCode +=ProductSpecUtil.padModelCode(messages[2]);  // model code
		productSpecCode +=ProductSpecUtil.padModelTypeCode(messages[3]);  // model type code
		productSpecCode +=ProductSpecUtil.padModelOptionCode(messages[4]);  // model code
		return productSpecCode;
	}
	


}
