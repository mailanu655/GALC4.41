package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.OIFSimpleParsingHelper;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.entity.enumtype.BuildStatus;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.oif.dto.PartReceivingProductDTO;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

public class PartReceivingTask extends OifTask<PartReceivingProductDTO> implements IEventTaskExecutable {

	
	private static String BUILD_SITE = "BUILD_SITE";
	private static String PARTIAL_BUILD = "PARTIAL_BUILD";
	
	private final String InterfaceId;
	private String buildSite = getProperty(BUILD_SITE);
	private Map<String, String> partialBuildMap= PropertyService.getPropertyMap(getComponentId(),PARTIAL_BUILD);
	
	Timestamp jobTs = null;
	
	public PartReceivingTask(String name) {
		super(name);
		InterfaceId = getProperty(OIFConstants.INTERFACE_ID);
	}

	@Override
	public void execute(Object[] args) {
		try {
			initData(PartReceivingProductDTO.class);
			jobTs = getService(GenericDaoService.class).getCurrentDBTime();
			MbpnProductDao mbpnProductDao = ServiceFactory.getDao(MbpnProductDao.class);
			if(getFilesFromMQ() == 0)
				return;
			// Get configured parsing data
					String partShipmentDef = getProperty(OIFConstants.PARSE_LINE_DEFS);
					OIFSimpleParsingHelper<PartReceivingProductDTO> parseHelper = new OIFSimpleParsingHelper<PartReceivingProductDTO>(
							PartReceivingProductDTO.class, partShipmentDef, logger);
					parseHelper.getParsingInfo();
			for (int count = 0; count < receivedFileList.length; count++) {
				String receivedFile = receivedFileList[count];
				
				if (receivedFile == null || receivedFile.trim().equals(""))
					continue;
				
		
					String resultPath = PropertyService.getProperty(
							OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT);
					List<String> receivedRecords = OIFFileUtility.loadRecordsFromFile(
							resultPath + receivedFile, logger);
					if (receivedRecords.isEmpty()) {
						logger.error("No records in received file: " + receivedFile);
						errorsCollector.error("No records in received file: "
								+ receivedFile);
						continue;
					}
	
					// Process files(s)
					List<PartReceivingProductDTO> partShipmentProductDTOList = new ArrayList<PartReceivingProductDTO>();
					for (String receivedRecord : receivedRecords) {
						PartReceivingProductDTO partShipmentProductDTO = new PartReceivingProductDTO();
						parseHelper.parseData(partShipmentProductDTO, receivedRecord);
						logger.info("Parsed Data : "+partShipmentProductDTO.toString() );
						partShipmentProductDTOList.add(partShipmentProductDTO);
						
					}
					
					// Update or Insert data
					for(PartReceivingProductDTO product : partShipmentProductDTOList){
						MbpnProduct mbpnProduct = mbpnProductDao.findBySn(product.getProductId());
						//delete if remove status
						if(product.getBuildStatus().trim().equalsIgnoreCase(BuildStatus.REMOVE.getName())){
							if(mbpnProduct != null){
								mbpnProductDao.remove(mbpnProduct);
								logger.info("removed MbpnProduct : " + product.getProductId());
							}
						}else{
							if(mbpnProduct != null){
								logger.info("Existing MbpnProduct : " + product.getProductId() +", updating spec code and order number");
								mbpnProduct.setCurrentProductSpecCode(product.getProductSpecCode().trim());
								mbpnProduct.setCurrentOrderNo(product.getOrderNumber().trim());
							}else{
								logger.info("new MbpnProduct : " + product.getProductId());
								mbpnProduct = new MbpnProduct();
								mbpnProduct.setProductId(product.getProductId().trim());
								mbpnProduct.setSn(product.getProductId().trim());
								mbpnProduct.setCurrentProductSpecCode(product.getProductSpecCode().trim());
								mbpnProduct.setCurrentOrderNo(product.getOrderNumber().trim());
							}
							if(product.getBuildStatus().trim().equalsIgnoreCase(BuildStatus.FINAL.getName())) 	{
								logger.info("setting External Build Flag to 1 : " + product.getProductId());
								mbpnProduct.setExternalBuild(1);
							}else if(product.getBuildStatus().trim().equalsIgnoreCase(BuildStatus.SCRAP.getName())){
								logger.info("setting Defect Status to SCRAP : " + product.getProductId());
								mbpnProduct.setDefectStatus(DefectStatus.SCRAP);
								mbpnProduct.setExternalBuild(1);
							}else {
								String partNamesString = partialBuildMap.get(product.getBuildStatus().trim());
								if(StringUtils.isNotEmpty(partNamesString)){
									List<String> partNamesList = Arrays.asList(partNamesString.split(","));
									List<String> processPartNames = new ArrayList<String>();
									
									for(String partName:partNamesList){
										if(partName.startsWith("%") && partName.endsWith("%")){
											
											String processName = partName.replace("%", " ");
											String partNames = partialBuildMap.get(processName.trim());
											if(StringUtils.isNotEmpty(partNames)){
												List<String> processPartNamesList = Arrays.asList(partNames.split(","));
												processPartNames.addAll(processPartNamesList);
											}
											
										}else{
											processPartNames.add(partName);
										}
									}
									logger.info("updating installed Parts for product : " + product.getProductId() +", with Build Status - "+product.getBuildStatus());
									updateInstalledParts(processPartNames, product.getProductId());
								}else{
									logger.info("No PartNames specified for product : " + product.getProductId() +", with Build Status - "+product.getBuildStatus());
								}
								mbpnProduct.setExternalBuild(0);
								logger.info("setting External Build Flag to 0 : " + product.getProductId());
							}
							
							mbpnProductDao.save(mbpnProduct);
							logger.info("saved Mbpn Product : " + product.getProductId());
						}
					}
						
				logger.info(String.format("File %d (%s) of %d total file(s) is processed.", count + 1, receivedFile, receivedFileList.length));
			}
			if(errorsCollector.getErrorList().isEmpty()){	
				updateLastProcessTimestamp(jobTs);
			}
			logger.info("Part Receiving data processed.");
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e, "Exception processing Part Receiving data");
			errorsCollector.error(e,  "Exception processing Part Receiving data");
		} finally {
			errorsCollector.sendEmail();
		}

	}

	private void updateInstalledParts(List<String> partNamesList, String productId) {
		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		for(String partName:partNamesList){
			InstalledPart part = installedPartDao.findById(productId, partName);
			if(part == null){
				 InstalledPartId partId  =  new InstalledPartId();
				 partId.setPartName(partName);
				 partId.setProductId(productId);
				 
				 part = new InstalledPart();
				 part.setId(partId);
				 part.setInstalledPartStatus(InstalledPartStatus.OK);
				 part.setInstalledPartReason("Built at another site - "+buildSite);
				 part.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
				 
				 installedPartDao.save(part);
			}
		}
		
	}

	protected int getFilesFromMQ() { 
		int filesCount = 0;

		refreshProperties();

		
//		Get list of objects created from received file
		receivedFileList = getFilesFromMQ(InterfaceId, getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH));

		if (receivedFileList != null)
			filesCount = receivedFileList.length;

		return filesCount;
	}
	
	
}