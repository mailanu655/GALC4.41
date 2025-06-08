package com.honda.galc.oif.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.oif.dto.GPP307DTO;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.PreProductionLotList;

/**
 * 
 * <h3>ComponentReleasedAssemblyTask Class description</h3>
 * <p> ComponentReleasedAssemblyTask description </p>
 * Component Released Assembly task is an OIF task for component plants, which executes every day between 2:00am and 2:30am.(The setting can be change)
 * It retrieves data from incoming interface N--GPC#HMAGAL#GPP307 to get the original priority production plans. 
 * Every priority plan is converted to production lot in GAL217TBX and 
 * its corresponding product ids in GAL131TBX (engine) table.
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
 * @author Larry Karpov 
 * @since Jan 5, 2014
 *
 *
 */
public class ComponentReleasedAssemblyTask extends BasePlanCodeTask<GPP307DTO> implements IEventTaskExecutable{
	private final static String PLANCODES = "PLAN_CODES";

	private enum PlanCodeElements {
		SiteId(1),
		LineId(2),
		ProcessLocation(4);
		
		private final int Id;
		
		PlanCodeElements(int Id) {
			this.Id = Id;
		}
	}
	
	public ComponentReleasedAssemblyTask(String name) {
		super(name);
	}
	
	public void execute(Object[] args) {
		logger.info("start to process priority plan");
		
		try{
			logger.info("Initiatlizing data ");
			initData(GPP307DTO.class);
			logger.info("Get files from the Queue ");
			if(getFilesFromMQ() == 0)
				return;
			
			processReleasedAssembly();				
				
		}catch(TaskException e) {
			logger.emergency(e.getMessage());
			errorsCollector.emergency(e.getMessage());
		}catch(Exception e) {
			logger.emergency(e,"Unexpected exception occured");
			errorsCollector.emergency(e, "Unexpected exception occured");
		} finally {
			errorsCollector.sendEmail();
		}
	}
	
	private void processReleasedAssembly() {
		int fileCount = 1;

		for (String receivedFile : receivedFileList) {
			if (receivedFile == null) 
				continue;
			
			logger.info(String.format("Processing File %d (%s) from %d total file(s).", fileCount++, receivedFile, receivedFileList.length));
			
			processFile(receivedFile);
			
			logger.info(String.format("File %d (%s) from %d total file(s) is processed.", fileCount++, receivedFile, receivedFileList.length));
		}
		
		logger.info("Component Released Assembly processed.");
	}

	private boolean validateList(List<PreProductionLot> PplList) {
		Set<String> check4Dups = new HashSet<String>();
		logger.info("Validating the list for duplicates ");
		// Duplicate record check
		for(PreProductionLot ppl : PplList) {
			if(check4Dups.contains(ppl.getProductionLot())) {
				String DupErr = String.format("Duplicate Production Lot: %s found in import file\nImport of Plan Code cancelled", ppl.getProductionLot());
				
				logger.error(DupErr);
				errorsCollector.error(DupErr);

				return false;
			}
			
			check4Dups.add(ppl.getProductionLot());
		}
		
		return true;
	}

	private String parsePlanCode(String PlanCode, PlanCodeElements Element) {
		logger.info("Parsing the PlanCode :"+ PlanCode);
		Pattern compile = Pattern.compile("^(.{4})(.{2})(.)(.{2})(.*)$");
		Matcher matcher = compile.matcher(PlanCode);
		matcher.find();
		
		return matcher.group(Element.Id);		
	}

	private void processFile(String recievedFile) {
		PreProductionLotList allWaitingPpll = new PreProductionLotList(logger);
		Map<String, PreProductionLotList> gpp307 = new HashMap<String, PreProductionLotList>();
		PreProductionLotList movingPpll = new PreProductionLotList(logger);
		Map<String, ArrayList<GPP307DTO>> receivedRecordsByPlanCode = new HashMap<String, ArrayList<GPP307DTO>>();
		
		try {
			receivedRecordsByPlanCode = getRecordsByPlanCode(recievedFile);
			logger.info("Getting all records from DB in WAITING status for the planCodes "+receivedRecordsByPlanCode.keySet().toArray(new String[receivedRecordsByPlanCode.size()]));
			allWaitingPpll.findAllBySendStatusAndPlanCodes(PreProductionLotSendStatus.WAITING, receivedRecordsByPlanCode.keySet().toArray(new String[receivedRecordsByPlanCode.size()]));
			//Loops through received records and compare them with DB. 
			// Created two Maps, one is the GPP307 map Object and MovingPpl object(lots moved from one plan to another)
			for(String planCode : receivedRecordsByPlanCode.keySet()) {
				logger.info("Started working on plan code :"+planCode);
				final String lineId = parsePlanCode(planCode, PlanCodeElements.LineId);
				//For each lot in the plan code X
				for(GPP307DTO plan307 : receivedRecordsByPlanCode.get(planCode)) {
					final int indxOfPpl;
					//Verify the lot from the file is in the DB
					if ((indxOfPpl = allWaitingPpll.indexOfKdLotNumberSpecCode(plan307)) >= 0) {
						logger.info("Working on Pre-Production Lot with KD Lot Number :"+plan307.getKdLotNumber());
						if (!gpp307.containsKey(planCode))
							gpp307.put(planCode, new PreProductionLotList(logger));

						if (!allWaitingPpll.get(indxOfPpl).getPlanCode().equals(planCode))
							movingPpll.add(allWaitingPpll.get(indxOfPpl));
						//Remove from DB list and put the lot in new tmp list
						gpp307.get(planCode).add(allWaitingPpll.remove(indxOfPpl));
						//setting the new plan code,line, demand type and lot number
						gpp307.get(planCode).get(gpp307.get(planCode).size() - 1).setPlanCode(planCode);
						gpp307.get(planCode).get(gpp307.get(planCode).size() - 1).setLineNo(lineId);
						gpp307.get(planCode).get(gpp307.get(planCode).size() - 1).setDemandType(plan307.getDemandType());
			            gpp307.get(planCode).get(gpp307.get(planCode).size() - 1).setLotNumber(plan307.getLotNumber());
					}else {
						logger.info("Pre-Production Lot with KD Lot Number :"+plan307.getKdLotNumber()+ ", Spec code : "+plan307.getProductSpecCode()+" does not exist. ");
						errorsCollector.error("Pre-Production Lot with KD Lot Number :"+plan307.getKdLotNumber()+ ", Spec code : "+plan307.getProductSpecCode()+" does not exist. \n ");
					}
				}
				logger.info("Finished processing records for plan code :"+planCode);
			}
			//Retains all other than the passing Active lots with sequence >0.
			allWaitingPpll.retainAll(allWaitingPpll.AllActive());
			//Delete the remaining from the schedule
			allWaitingPpll.RemoveFromSchedule().saveAll();
			
			//Fix the tails for the lots moving from one plan code to another
			PreProductionLotList ppllFinal = new PreProductionLotList(logger);
			
			ppllFinal.addAll(movingPpll.MoveProductionLots());
			
			if (!validateList(ppllFinal))
				return;

			// Save the records from the 307
			for (String planCode : gpp307.keySet()) {
				logger.info("Validating and Setting the next prod lot sequences for plan code :"+planCode);
				//check for duplicates on each plan code
				if (!validateList(gpp307.get(planCode)))
					return;
					
				gpp307.get(planCode).SetNextProductionLotSequence();

				ppllFinal.addAll(gpp307.get(planCode));
			}
			logger.info("Saving the records ");
			ppllFinal.saveAll();
			
		} catch (Exception e) {
			logger.error(e,  new LogRecord(String.format("Error while processesing file: %s", recievedFile)));
			errorsCollector.error(e.getMessage());
		}
	}
}
