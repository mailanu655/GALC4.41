package com.honda.galc.client.teamlead;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dto.ChangeFormDTO;
import com.honda.galc.client.dto.ChangeFormUnitDTO;
import com.honda.galc.client.dto.MCMeasurementDTO;
import com.honda.galc.client.dto.MCOperationMatrixDTO;
import com.honda.galc.client.dto.MCOperationPartRevisionDTO;
import com.honda.galc.client.dto.MCOperationRevisionDTO;
import com.honda.galc.client.dto.MCPddaPlatformDTO;
import com.honda.galc.client.dto.MCRevisionDTO;
import com.honda.galc.client.dto.MCStructureDTO;
import com.honda.galc.constant.PartType;
import com.honda.galc.dao.conf.MCViosMasterProcessDao;
import com.honda.galc.dto.MCRevisionDto;
import com.honda.galc.dto.PddaProcess;
import com.honda.galc.entity.conf.MCOperationMatrix;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationMeasurementId;
import com.honda.galc.entity.conf.MCOperationPartMatrix;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationPartRevisionId;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCOperationRevisionId;
import com.honda.galc.entity.conf.MCPddaPlatform;
import com.honda.galc.entity.conf.MCRevision;
import com.honda.galc.entity.conf.MCStructure;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.conf.MCViosMasterProcess;
import com.honda.galc.entity.conf.MCViosMasterProcessId;
import com.honda.galc.entity.enumtype.PartCheck;
import com.honda.galc.entity.pdda.ChangeForm;
import com.honda.galc.entity.pdda.ChangeFormUnit;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.pdda.GenericPddaDaoService;

public class DTOConverter {

	public static List<ChangeFormUnitDTO> convertChangeFormUnit(
			List<ChangeFormUnit> inList) {

		List<ChangeFormUnitDTO> outList = new ArrayList<ChangeFormUnitDTO>();

		for (ChangeFormUnit unit : inList) {

			ChangeFormUnitDTO dto = new ChangeFormUnitDTO();
			dto.setUnitId(unit.getUnitNo());
			dto.setDescription(unit.getChangeDesc());
			dto.setAsmProcNo(unit.getAsmProcNo());
			dto.setChangeFormId(String.valueOf(unit.getId().getChangeFormId()));
			dto.setApprProcMaintId(unit.getId().getApprovedProcMaintId());
			dto.setApprUnitMaintId(unit.getId().getApprovedUnitMaintId());
			outList.add(dto);

		}
		return outList;
	}

	public static List<MCOperationRevisionDTO> convertMcoperationRevision(
			List<MCOperationRevision> inList) {

		List<MCOperationRevisionDTO> outList = new ArrayList<MCOperationRevisionDTO>();

		for (MCOperationRevision op : inList) {

			MCOperationRevisionDTO dto = new MCOperationRevisionDTO();
			dto.setOperationName(op.getId().getOperationName());
			dto.setOpRevision(String.valueOf(op.getId().getOperationRevision()));
			dto.setRevId(String.valueOf(op.getRevisionId()));
			dto.setProcessor(op.getProcessor());
			dto.setView(op.getView());
			//Setting Operation revision status
			if(op.getDeprecated()!=null) {
				dto.setStatus("DEPRECATED");
			}
			else if(op.getApproved()!=null) {
				dto.setStatus("ACTIVE");
			}
			else {
				dto.setStatus("DEVELOPING");
			}
			dto.setDescription(op.getDescription());
			outList.add(dto);

		}
		return outList;
	}

	public static MCOperationRevision convertMcoperationRevisionDTO(
			MCOperationRevisionDTO in) {

		MCOperationRevision out = new MCOperationRevision();

		out.setProcessor(in.getProcessor());
		out.setView(in.getView());
		
		MCOperationRevisionId opRevId = new MCOperationRevisionId();
		opRevId.setOperationName(in.getOperationName());
		opRevId.setOperationRevision(Integer.valueOf(in.getOpRevision()));
		out.setRevisionId(Long.valueOf(in.getRevId()));
		out.setDescription(in.getDescription());
		
		out.setId(opRevId);

		return out;
	}

	public static List<MCOperationPartRevisionDTO> convertMcoperationPartRevision(
			List<MCOperationPartRevision> inList) {

		List<MCOperationPartRevisionDTO> outList = new ArrayList<MCOperationPartRevisionDTO>();

		for (MCOperationPartRevision opPart : inList) {

			MCOperationPartRevisionDTO dto = new MCOperationPartRevisionDTO();
			dto.setOperationName(opPart.getId().getOperationName());

			dto.setPartRev(String.valueOf(opPart.getId().getPartRevision()));
			dto.setRevId(String.valueOf(opPart.getRevisionId()));
			dto.setProcessor(opPart.getPartProcessor());
			dto.setView(opPart.getPartView());
			dto.setPartId(opPart.getId().getPartId());
			dto.setDescription(opPart.getPartDesc());
			dto.setPartMask(opPart.getPartMask());
			dto.setPartNo(opPart.getPartNo());
			dto.setSectionCode(opPart.getPartSectionCode());
			dto.setPartMask(opPart.getPartMask());
			dto.setPartCheck(opPart.getPartCheck().name());
			dto.setDeviceMsg(opPart.getDeviceMsg());
			dto.setPartItemNo(opPart.getPartItemNo());	
			dto.setPartType((opPart.getPartType()!=null)?opPart.getPartType().toString():"");
			dto.setPartItemNo(opPart.getPartItemNo());
			dto.setPartType((opPart.getPartType()!=null)?opPart.getPartType().toString():"");
			outList.add(dto);

		}
		return outList;
	}
	
	public static MCOperationPartRevision convertMcoperationPartRevisionDTO(
			MCOperationPartRevisionDTO in) {
	
			MCOperationPartRevision out = new MCOperationPartRevision();
			out.setPartProcessor(in.getProcessor());
			out.setPartView(in.getView());
					
			MCOperationPartRevisionId opPartRevId = new MCOperationPartRevisionId();
			opPartRevId.setOperationName(in.getOperationName());
			opPartRevId.setPartRevision(Integer.parseInt(in.getPartRev()));
			opPartRevId.setPartId(in.getPartId());
			
			out.setRevisionId(Long.valueOf(in.getRevId()));
			out.setPartDesc(in.getDescription());
			out.setPartNo(in.getPartNo());
			out.setPartMask(in.getPartMask());
			out.setPartCheck(PartCheck.get(in.getPartCheck()));
			out.setDeviceMsg(in.getDeviceMsg());
			out.setPartItemNo(in.getPartItemNo());
			
			out.setRevisionId(Long.parseLong(in.getRevId()));
			out.setPartSectionCode(in.getSectionCode());
			out.setPartType(Enum.valueOf(PartType.class, in.getPartType()));
						
			out.setId(opPartRevId);

		
		return out;
	}

	public static List<MCOperationMatrixDTO> convertMcoperationMatrix(
			List<MCOperationMatrix> inList) {

		List<MCOperationMatrixDTO> outList = new ArrayList<MCOperationMatrixDTO>();

		for (MCOperationMatrix opMatrix : inList) {

			MCOperationMatrixDTO dto = new MCOperationMatrixDTO();
			dto.setOperationName(opMatrix.getId().getOperationName());
			dto.setSpecCodeType(opMatrix.getId().getSpecCodeType());
			dto.setSpecCodeMask(opMatrix.getId().getSpecCodeMask());
			dto.setOperationRev(opMatrix.getId().getOperationRevision());
			dto.setPddaPlatFormId(opMatrix.getId().getPddaPlatformId());
			outList.add(dto);

		}
		return outList;
	}
	
	

	public static List<MCStructureDTO> convertMCStructure(
			List<MCStructure> inList) {

		List<MCStructureDTO> outList = new ArrayList<MCStructureDTO>();

		for (MCStructure structure : inList) {

			MCStructureDTO dto = new MCStructureDTO();
			dto.setOpName(structure.getId().getOperationName());
			dto.setOpRev(String.valueOf(structure.getId()
					.getOperationRevision()));
			dto.setPartId(structure.getId().getPartId());
			dto.setPartRev(String.valueOf(structure.getId().getPartRevision()));
			dto.setProcessPoint(structure.getId().getProcessPointId());
			dto.setRevision(String.valueOf(structure.getId().getRevision()));
			dto.setProductSpecCode(structure.getId().getProductSpecCode());
			outList.add(dto);

		}
		return outList;
	}

	public static List<MCOperationMatrixDTO> convertMcoperationPartMatrix(
			List<MCOperationPartMatrix> inList) {

		List<MCOperationMatrixDTO> outList = new ArrayList<MCOperationMatrixDTO>();

		for (MCOperationPartMatrix opPartMatrix : inList) {

			MCOperationMatrixDTO dto = new MCOperationMatrixDTO();
			dto.setOperationName(opPartMatrix.getId().getOperationName());
			dto.setSpecCodeType(opPartMatrix.getId().getSpecCodeType());
			dto.setSpecCodeMask(opPartMatrix.getId().getSpecCodeMask());
			outList.add(dto);

		}
		return outList;
	}
	
	public static List<MCOperationMatrixDTO> convertMcoperationPartMatrixEffDate(
			List<MCOperationPartMatrix> inList, List<Object[]> obj) {

		List<MCOperationMatrixDTO> outList = new ArrayList<MCOperationMatrixDTO>();
		int i=0;
		for (MCOperationPartMatrix opPartMatrix : inList) {
			MCOperationMatrixDTO dto = new MCOperationMatrixDTO();
			if(obj != null && obj.size()>0){
				Object[] objTemp=obj.get(i);
				if(objTemp!=null){
					if((objTemp[2].toString().trim()).equalsIgnoreCase(opPartMatrix.getId().getSpecCodeMask().trim())){
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						Date date1=(Date)objTemp[0];
						Date date2=(Date)objTemp[1];
						String s = formatter.format(date1);
						String s1 = formatter.format(date2);
						dto.setEffectiveBeginDate(s);
						dto.setEffectiveEndDate(s1);
						i++;
					}
				}
			}
			dto.setOperationName(opPartMatrix.getId().getOperationName());
			dto.setSpecCodeType(opPartMatrix.getId().getSpecCodeType());
			dto.setSpecCodeMask(opPartMatrix.getId().getSpecCodeMask());
			outList.add(dto);
		}
		return outList;
	}

	public static List<MCMeasurementDTO> convertMCOperationMeasurements(
			List<MCOperationMeasurement> inList) {

		List<MCMeasurementDTO> outList = new ArrayList<MCMeasurementDTO>();

		for (MCOperationMeasurement meas : inList) {

			MCMeasurementDTO dto = new MCMeasurementDTO();

			dto.setOperationName(meas.getId().getOperationName());
			dto.setPartRev(String.valueOf(meas.getId().getPartRevision()));
			dto.setProcessor(meas.getProcessor());
			dto.setView(meas.getView());
			dto.setMeasurementType(meas.getType());
			dto.setPartId(meas.getId().getPartId());
			dto.setDeviceId(meas.getDeviceId());
			dto.setDeviceMsg(meas.getDeviceMsg());
			dto.setMaxAttempts(String.valueOf(meas.getMaxAttempts()));
			dto.setMinLimit(String.valueOf(meas.getMinLimit()));
			dto.setMaxLimit(String.valueOf(meas.getMaxLimit()));
			dto.setSeqNumber(String
					.valueOf(meas.getId().getMeasurementSeqNum()));

			outList.add(dto);

		}
		return outList;
	}

	public static MCOperationMeasurement convertMCMeasurementDTO(
			MCMeasurementDTO meas) {

		MCOperationMeasurement dto = new MCOperationMeasurement();

		MCOperationMeasurementId measId = new MCOperationMeasurementId();
		measId.setOperationName(meas.getOperationName());
		measId.setOpMeasSeqNum(Integer.valueOf(meas.getSeqNumber()).intValue());
		measId.setPartId(meas.getPartId());
		measId.setPartRevision(Integer.valueOf(meas.getPartRev()).intValue());

		dto.setId(measId);
		dto.setProcessor(meas.getProcessor());
		dto.setView(meas.getView());

		dto.setDeviceId(meas.getDeviceId());
		dto.setDeviceMsg(meas.getDeviceMsg());
		dto.setMaxAttempts(Integer.valueOf(meas.getMaxAttempts()));
		if(meas.getMinLimit().length() > 0)
		dto.setMinLimit(Double.valueOf(meas.getMinLimit()));
		if(meas.getMaxLimit().length() > 0)
		dto.setMaxLimit(Double.valueOf(meas.getMaxLimit()));
		dto.setType(meas.getMeasurementType());

		return dto;
	}
	
	public static List<MCRevisionDTO> convertMCRevision(
			List<MCRevision> inList) {

		List<MCRevisionDTO> outList = new ArrayList<MCRevisionDTO>();

		for (MCRevision rev : inList) {
			if(rev!=null) {
				MCRevisionDTO dto = new MCRevisionDTO(rev);
				outList.add(dto);
			}
		}
		return outList;
	}
	
	
	public static List<ChangeFormUnitDTO> convertUnmappedChangeFormUnits(
			List<ChangeFormUnit> inList) {
		List<ChangeFormUnitDTO> outList = new ArrayList<ChangeFormUnitDTO>();
		if(inList!=null) {
			Set<String> hashSet = new TreeSet<String>();
			for (ChangeFormUnit unit : inList) {
				ChangeFormUnitDTO dto = new ChangeFormUnitDTO();
				dto.setUnitId(unit.getUnitNo());
				dto.setDescription(unit.getChangeDesc());
				dto.setAsmProcNo(unit.getAsmProcNo());
				dto.setChangeFormId(String.valueOf(unit.getId().getChangeFormId()));
				dto.setApprProcMaintId(unit.getId().getApprovedProcMaintId());
				dto.setApprUnitMaintId(unit.getId().getApprovedUnitMaintId());
				String key = unit.getId().getChangeFormId() + "_" + ((unit.getUnitNo()!=null)?unit.getUnitNo().trim().toLowerCase():"");
				if(!hashSet.contains(key)) {
					outList.add(dto);
				}
				hashSet.add(key);
			}
		}
		return outList;
	}
	
	public static List<ChangeFormUnitDTO> convertUnmappedChangeFormProcesses(
			List<ChangeFormUnit> inList) {
		List<ChangeFormUnitDTO> outList = new ArrayList<ChangeFormUnitDTO>();
		if(inList!=null) {
			Set<String> hashSet = new TreeSet<String>();
			for (ChangeFormUnit unit : inList) {
				ChangeFormUnitDTO dto = new ChangeFormUnitDTO();
				dto.setUnitId(unit.getUnitNo());
				dto.setDescription(unit.getChangeDesc());
				dto.setAsmProcNo(unit.getAsmProcNo());
				dto.setChangeFormId(String.valueOf(unit.getId().getChangeFormId()));
				dto.setApprProcMaintId(unit.getId().getApprovedProcMaintId());
				dto.setApprUnitMaintId(unit.getId().getApprovedUnitMaintId());
				String key = unit.getId().getChangeFormId() + "_" + ((unit.getProcess().getAsmProcNo()!=null)?unit.getProcess().getAsmProcNo().trim().toLowerCase():"");
				if(!hashSet.contains(key)) {
					outList.add(dto);
				}
				hashSet.add(key);
			}
		}
		return outList;
	}
	
	public static ChangeFormDetails convertChangeForm(
			List<ChangeForm> inList) {
		List<ChangeFormDTO> outList = new ArrayList<ChangeFormDTO>();
		Map<String, List<ChangeFormDTO>> deptCodeWiseChgFrms = new LinkedHashMap<String, List<ChangeFormDTO>>();
		deptCodeWiseChgFrms.put("All", null);
		if(inList!=null) {
			for (ChangeForm cf : inList) {
				ChangeFormDTO dto = new ChangeFormDTO();
				dto.setChangeFormId(cf.getId());
				dto.setProdSchQty(cf.getProdSchQty());
				dto.setModelYearDate(cf.getModelYearDate());
				dto.setPlantLocCode(cf.getPlantLocCode());
				dto.setDeptCode(cf.getDeptCode());
				dto.setChangeFormType(cf.getChangeFormType());
				dto.setProdAsmLineNo(cf.getProdAsmLineNo());
				dto.setVehicleModelCode(cf.getVehicleModelCode());
				dto.setControlNo(cf.getControlNo());
				
				
				String asmProcNumbers = "";
				String asmProcNames = "";
				List<PddaProcess> processList= ServiceFactory.getService(
						GenericPddaDaoService.class).getProcessDetailForChangeForm(cf.getId());
				if(processList!=null) {
					for(PddaProcess process: processList) {
						if(StringUtils.isNotBlank(process.getAsmProcNumber())) {
							if(asmProcNumbers.equals("")) {
								asmProcNumbers += process.getAsmProcNumber();
							}
							else {
								asmProcNumbers += ", " + process.getAsmProcNumber();
							}
						}
						if(StringUtils.isNotBlank(process.getAsmProcName())) {
							if(asmProcNames.equals("")) {
								asmProcNames += process.getAsmProcName();
							}
							else {
								asmProcNames += ", " + process.getAsmProcName();
							}
						}
					}
				}
				dto.setAsmProcNames(asmProcNames);
				dto.setAsmProcNumbers(asmProcNumbers);
				
				//Creating Dept Code wise Map
				List<ChangeFormDTO> cfList = null;
				if(deptCodeWiseChgFrms.containsKey(dto.getDeptCode())) {
					cfList = deptCodeWiseChgFrms.get(dto.getDeptCode());
				}
				else {
					cfList = new ArrayList<ChangeFormDTO>();
				}
				cfList.add(dto);
				deptCodeWiseChgFrms.put(dto.getDeptCode(), cfList);
					
				//Adding dto into the list
				outList.add(dto);
			}
		}
		deptCodeWiseChgFrms.put("All", outList);
		return new ChangeFormDetails(outList, deptCodeWiseChgFrms);
	}
	
	public static List<MCPddaPlatformDTO> convertPlatform(
			List<MCPddaPlatform> inList) {
		List<MCPddaPlatformDTO> outList = new ArrayList<MCPddaPlatformDTO>();
		if(inList!=null) {
			for (MCPddaPlatform platform : inList) {
				outList.add(convertPlatform(platform));
			}
		}
		return outList;
	}
	
	public static MCPddaPlatformDTO convertPlatform(MCPddaPlatform platform) {
		MCPddaPlatformDTO dto = new MCPddaPlatformDTO();
		dto.setPlatformId(platform.getPddaPlatformId());
		dto.setPlantLocCode(platform.getPlantLocCode());
		dto.setDeptCode(platform.getDeptCode());
		dto.setModelYearDate(platform.getModelYearDate());
		dto.setProductScheduleQty(platform.getProductScheduleQty());
		dto.setProductAsmLineNo(platform.getProductAsmLineNo());
		dto.setVehicleModelCode(platform.getVehicleModelCode());
		dto.setAsmProcessNo(platform.getAsmProcessNo());
		
		MCViosMasterPlatform masterPlat = new MCViosMasterPlatform(platform.getPlantLocCode(), platform.getDeptCode(), platform.getModelYearDate(), 
				platform.getProductScheduleQty(), platform.getProductAsmLineNo(), platform.getVehicleModelCode());
		//Get VIOS Master Process
		MCViosMasterProcess masterProc = ServiceFactory.getDao(MCViosMasterProcessDao.class).findByKey(new MCViosMasterProcessId(masterPlat.getGeneratedId(), platform.getAsmProcessNo()));
		if(masterProc != null) {
			dto.setProcessPointId(masterProc.getProcessPointId());
			dto.setProcessSeqNum(masterProc.getProcessSeqNum());
		}
		return dto;
	}
	
	public static PddaPlatformDetails convertPlatformByDept(List<MCPddaPlatform> inList){
		List<MCPddaPlatformDTO> outList = new ArrayList<MCPddaPlatformDTO>();
		Map<String, List<MCPddaPlatformDTO>> deptCodeWisePddaPlatforms = new LinkedHashMap<String, List<MCPddaPlatformDTO>>();
		deptCodeWisePddaPlatforms.put("All", null);
		if(inList!=null) {
			for (MCPddaPlatform platform : inList) {
				MCPddaPlatformDTO dto = convertPlatform(platform);
				//Creating Dept Code wise Map
				List<MCPddaPlatformDTO> cfList = null;
				if(deptCodeWisePddaPlatforms.containsKey(dto.getDeptCode())) {
					cfList = deptCodeWisePddaPlatforms.get(dto.getDeptCode());
				}
				else {
					cfList = new ArrayList<MCPddaPlatformDTO>();
				}
				cfList.add(dto);
				deptCodeWisePddaPlatforms.put(dto.getDeptCode(), cfList);
					
				//Adding dto into the list
				outList.add(dto);
			}
		}
		
		deptCodeWisePddaPlatforms.put("All", outList);
		return new PddaPlatformDetails(outList, deptCodeWisePddaPlatforms);
	}
	
	public static Map<String, Set<String>> convetMatrixInfoToModelModelTypesMapping(List<MCOperationMatrix> operationMatrixs){
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		for(MCOperationMatrix operationMatrix : operationMatrixs) {
			String modelCode =ProductSpec.extractModelYearCode(operationMatrix.getId().getSpecCodeMask())
					+ProductSpec.extractModelCode(operationMatrix.getId().getSpecCodeMask());
			String modelType = ProductSpec.extractModelTypeCode(operationMatrix.getId().getSpecCodeMask());
			if(map.containsKey(modelCode)) {
				map.get(modelCode).add(modelType);
			} else {
				Set<String> modelTypes = new TreeSet<String>();
				modelTypes.add(modelType);
				map.put(modelCode, modelTypes);
			}
		}
		
		return map;
	}
	
	public static Map<String, List<MCOperationMatrix>> convertMatrixInfoToSpecOperationMapping(List<MCOperationMatrix> operationMatrixInfo) {
		Map<String, List<MCOperationMatrix>> map = new HashMap<String, List<MCOperationMatrix>>();
		for (MCOperationMatrix matrix : operationMatrixInfo) {
			String specCodeMask = matrix.getId().getSpecCodeMask();

			if (map.containsKey(specCodeMask)) {
				List<MCOperationMatrix> list = map.get(specCodeMask);
				list.add(matrix);
			} else {
				List<MCOperationMatrix> list = new ArrayList<MCOperationMatrix>();
				list.add(matrix);
				map.put(specCodeMask, list);
			}
		}
		return map;
	}
	
	public static Map<String, List<MCOperationPartMatrix>> convertPartMatrixInfoToSpecOperationMapping(List<MCOperationPartMatrix> partMatrixInfo) {
		Map<String, List<MCOperationPartMatrix>> map = new HashMap<String, List<MCOperationPartMatrix>>();
		for (MCOperationPartMatrix matrix : partMatrixInfo) {
			String specCodeMask = matrix.getId().getSpecCodeMask();

			if (map.containsKey(specCodeMask)) {
				List<MCOperationPartMatrix> list = map.get(specCodeMask);
				list.add(matrix);
			} else {
				List<MCOperationPartMatrix> list = new ArrayList<MCOperationPartMatrix>();
				list.add(matrix);
				map.put(specCodeMask, list);
			}
		}
		return map;
	}


	public static List convertMCRevisionDto(List<MCRevisionDto> revisionList) {
		List<MCRevisionDTO> outList = new ArrayList<MCRevisionDTO>();

		for (MCRevisionDto rev : revisionList) {
			if(rev!=null) {
				MCRevisionDTO dto = new MCRevisionDTO(rev);
				outList.add(dto);
			}
		}
		return outList;
	}
	
	
	
}
