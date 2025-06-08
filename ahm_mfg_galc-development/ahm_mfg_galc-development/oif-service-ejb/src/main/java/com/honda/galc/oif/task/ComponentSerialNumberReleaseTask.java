package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.oif.dto.GPP306DTO;
import com.honda.galc.oif.dto.PreProductionLotDTO;
import com.honda.galc.oif.dto.ProductionLotDTO;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;

/**
 * 
 * <h3>ComponentSerialNumberReleaseTask Class description</h3>
 * <p>
 * ComponentSerialNumberReleaseTask description
 * </p>
 * Component Serial Number Release Task task is an OIF task for component plant,
 * which executes every day between 8:30am and 10:00am.(The setting can be
 * change) It retrieves data from incoming interface N--GPC#HMAGAL#GPP306 to get
 * the original priority production plans. Every priority plan is converted to
 * production lot in GAL217TBX and preproduction lot in GAL212TBX. It also
 * builds Engine(GAL131TBX), Frame(GAL143TBX) objects.
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
 * @author Larry Karpov Jan 7, 2014
 *
 *
 */

public class ComponentSerialNumberReleaseTask extends BasePlanCodeTask<GPP306DTO> implements IEventTaskExecutable {

	private final static String AE_PROCESS_LOCATION = "AE";
	private final static String AM_PROCESS_LOCATION = "AM";

	public ComponentSerialNumberReleaseTask(String name) {
		super(name);
	}

	/**
	 * Executes GPP306 OIF task
	 * 
	 * @param args - parameters passed to the task
	 */
	public void execute(Object[] args) {
		try {
			initData(GPP306DTO.class);

			if (getFilesFromMQ() == 0)
				return;

			processComponentSerialNumberRelease();
		} catch (TaskException e) {
			logger.emergency(e.getMessage());
			errorsCollector.emergency(e.getMessage());
		} catch (Exception e) {
			logger.emergency(e, "Unexpected exception occured");
			errorsCollector.emergency(e, "Unexpected exception occured");
		} finally {
			errorsCollector.sendEmail();
		}
	}

	/**
	 * Loop over each file received from MQ.
	 * Loop over each Plan Code within each file. 
	 * Add {@link PreProductionLot}s to gal212tbx.
	 * Add {@link ProductionLot}s to gal217tbx.
	 * Add {@link Engine}s to gal131tbx.
	 */
	private void processComponentSerialNumberRelease() {
		logger.info("Processing Component Serial Number Release.");

		// Process one MQ file at a time
		for (int count = 0; count < receivedFileList.length; count++) {
			String receivedFile = receivedFileList[count];
			String currentPlanCode = new String();
			String currentRecordProductId = new String();
			try {

				logger.info(String.format("Processing file %d (%s) from %d total file(s).", count + 1, receivedFile,
						receivedFileList.length));

				// Get all the records for the interested Plan Codes from the file
				Map<String, ArrayList<GPP306DTO>> receivedRecordsByPlanCode = getRecordsByPlanCode(receivedFile);

				if(receivedRecordsByPlanCode != null && receivedRecordsByPlanCode.size() > 0)
					logger.info("Plan codes received: " + receivedRecordsByPlanCode.keySet().toString().replace("[", "").replace("]", ""));

				// Create the starting record for the plan code if it doesn't exist
				if (getPropertyBoolean(OIFConstants.INITIALIZE_TAIL, false))
					initTails(receivedRecordsByPlanCode);

				// Verify that each Plan Code has one and only one tail
				Map<String, PreProductionLotDTO> tailsByPlanCode = getTailsByPlanCode(receivedRecordsByPlanCode.keySet()
						.toArray(new String[receivedRecordsByPlanCode.keySet().size()]));
				if (tailsByPlanCode == null || tailsByPlanCode.isEmpty())
					throw new TaskException(String.format("Tails are not correct for at least one plan code: %s",
							StringUtils.join(receivedRecordsByPlanCode.keySet(), ",")));

				// Process one Plan Code at a time
				for (String planCode : receivedRecordsByPlanCode.keySet()) {
					currentPlanCode = planCode;
					logger.info(String.format("Processing Plan Code %s for file %s", planCode, receivedFile));
					PreProductionLotDTO tail = tailsByPlanCode.get(planCode);
					List<PreProductionLotDTO> newPplList = new ArrayList<PreProductionLotDTO>();
					List<ProductionLotDTO> newPlList = new ArrayList<ProductionLotDTO>();
					Set<String> changedLotNumbers = new HashSet<String>();

					// Start working with the individual record from the file
					for (GPP306DTO plan306 : receivedRecordsByPlanCode.get(planCode)) {
						currentRecordProductId = plan306.getStartProductId();
						if (getDao(PreProductionLotDao.class)
								.findByStartProductId(plan306.getStartProductId()) != null) {
							String Message = String.format(
									"Skipping... record with same starting product Id already exists: %s",
									plan306.getStartProductId());
							logger.info(Message);
							errorsCollector.error(Message);

							continue;
						}

						while (!changedLotNumbers.add(plan306.getLotNumber())
								|| getDao(PreProductionLotDao.class).findByKey(plan306.createProductionLot()) != null)
							plan306.setLotNumber(plan306.getLotNumber().substring(0, 8).concat(
									String.format("%04d", Integer.parseInt(plan306.getLotNumber().substring(8)) + 1)));

						plan306.setProductionLot(plan306.createProductionLot());

						// Pre Production Lot
						PreProductionLotDTO pplDto = new PreProductionLotDTO(plan306);

						tail.setNextProductionLot(pplDto.getProductionLot());
						pplDto.setSequence(tail.getSequence() + 1);

						newPplList.add(tail);

						tail = pplDto;

						tailsByPlanCode.remove(planCode);
						tailsByPlanCode.put(planCode, tail);

						// Production Lot
						ProductionLotDTO plDto = new ProductionLotDTO(plan306, logger);

						plDto.setProdLotKd(plDto.getProductionLot());
						newPlList.add(plDto);
					}

					// Add the new tail to the list
					newPplList.add(tail);

					// Create the PreProductionLot, ProductionLot, & Engines
					logger.info(String.format("Saving %d new lots for plan code %s.", newPplList.size(), planCode));
					serializeData(newPplList, newPlList);

					logger.info(String.format("Plan Code %s is processed.", planCode));
				}

				logger.info(String.format("File %d (%s) from %d total file(s) is processed.", count + 1, receivedFile,
						receivedFileList.length));

			} catch (Exception e) {
				logger.warn("Plan code before exception: " + currentPlanCode);
				logger.warn("Lot Product Id before exception: " + currentRecordProductId);
				logger.error(e, "Exception processing Component Serial Number Release data");
				errorsCollector.error(e, "Exception processing Component Serial Number Release data");
			}

		}

		logger.info("Component Serial Number Release processed.");
	}

	/**
	 * Save {@link PreProductionLot}s to gal212tbx.
	 * Save {@link ProductionLot}s to gal217tbx.
	 * Save {@link Engine}s to gal131tbx based off each {@link ProductionLot}.
	 * @param ppldtoList {@code List<PreProductionLotDTO>} List of {@link PreProductionLot}s to save.
	 * @param pldtoList {@code List<ProductionLotDTO>} List of {@link ProductionLot}s to save. {@link Engine}s to save derived from this.
	 */
	private void serializeData(List<PreProductionLotDTO> ppldtoList, List<ProductionLotDTO> pldtoList) {
		// Testing data integrity
		Set<String> productionLotSet = new HashSet<String>();

		for (PreProductionLotDTO ppldto : ppldtoList) {
			PreProductionLot ppl = ppldto.derivePreProductionLot();

			if (ppl == null || ppl.getProductionLot() == null)
				throw new TaskException("PreProductionLot is undefined.");

			String strProductionLot = ppl.getProductionLot();

			if (productionLotSet.contains(strProductionLot))
				throw new TaskException("Duplicate PreProductionLot.");

			productionLotSet.add(strProductionLot);
		}

		productionLotSet.clear();

		for (ProductionLotDTO pldto : pldtoList) {
			ProductionLot pl = pldto.deriveProductionLot();

			if (pl == null || pl.getProductionLot() == null)
				throw new TaskException("ProductionLot is undefined.");

			String strProductionLot = pl.getProductionLot();

			if (productionLotSet.contains(strProductionLot))
				throw new TaskException("Duplicate ProductionLot.");

			productionLotSet.add(strProductionLot);
		}

		// serialize data
		// PreProductionLot
		for (int n = 0; n < ppldtoList.size(); n++) {
			// The first item in the list is the current tail
			if (n == 0) {
				getDao(PreProductionLotDao.class).update(ppldtoList.get(n).derivePreProductionLot());
				logger.info("Updating PreProductionLot: " + ppldtoList.get(n).derivePreProductionLot().getProductionLot());
			}
			else {
				getDao(PreProductionLotDao.class).insert(ppldtoList.get(n).derivePreProductionLot());
				logger.info("Creating PreProductionLot: " + ppldtoList.get(n).derivePreProductionLot().getProductionLot());
			}
		}

		// ProductionLot
		for (ProductionLotDTO pldto : pldtoList) {
			ProductionLot pl = pldto.deriveProductionLot();
			getDao(ProductionLotDao.class).insert(pl);
			logger.info("Creating ProductionLot: " + pl.getProductionLot());

			logger.info("Creating products for: " + pl.getProcessLocation());
			if (pl.getProcessLocation().equals(AE_PROCESS_LOCATION))
				createEngines(pl);
			else if (pl.getProcessLocation().equals(AM_PROCESS_LOCATION))
				insertTransmissions(pldto);
		}
	}

	/**
	 * Saves {@link Engine}s to gal131tbx table based off a {@link ProductionLot}.
	 * @param pl {@link ProductionLot} to create engines from.
	 */
	private void createEngines(ProductionLot pl) {
		List<Engine> engines = new ArrayList<Engine>();
		int lotSize = pl.getLotSize();
		String engineType = pl.getStartProductId().substring(0, 5);
		int serialNumber = Integer.parseInt(pl.getStartProductId().substring(5));
		logger.info("Creating " + lotSize + " EINs. Starting from " + pl.getStartProductId());
		for (int i = 0; i < lotSize; i++) {
			String productId = engineType + String.format("%07d", serialNumber);
			serialNumber++;
			Engine engine = createEngine(pl, productId);
			engines.add(engine);
		}
		getDao(EngineDao.class).saveAll(engines);
		logger.info("" + lotSize + " EINs are created");
	}

	/**
	 * Loops over each plan code.
	 * If there are no {@link PreProductionLot}s for a plan code, creates a new tail and products for it.
	 * @param RecordsByPlanCode {@code Map<String, ArrayList<}{@link GPP306DTO}{@code >>}
	 */
	private void initTails(Map<String, ArrayList<GPP306DTO>> RecordsByPlanCode) {
		for (String planCode : RecordsByPlanCode.keySet()) {
			if (getDao(PreProductionLotDao.class).countByPlanCodeNextProductionLot(planCode, null) == 0) {
				GPP306DTO plan306 = RecordsByPlanCode.get(planCode).get(0);

				PreProductionLotDTO ppldto = new PreProductionLotDTO(plan306);
				PreProductionLot ppl = ppldto.derivePreProductionLot();
				ppl.setSequence(1);
				getDao(PreProductionLotDao.class).insert(ppl);

				ProductionLotDTO pldto = new ProductionLotDTO(plan306, logger);
				String strProductionLot = ppldto.getProductionLot();
				pldto.setProductionLot(strProductionLot);
				pldto.setProdLotKd(strProductionLot);
				ProductionLot pl = pldto.deriveProductionLot();

				createEngines(pl);

				getDao(ProductionLotDao.class).insert(pl);

				RecordsByPlanCode.get(planCode).remove(0);
			}
		}
	}
}
