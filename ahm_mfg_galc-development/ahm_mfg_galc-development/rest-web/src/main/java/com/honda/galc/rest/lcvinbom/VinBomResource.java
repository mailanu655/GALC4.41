package com.honda.galc.rest.lcvinbom;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.lcvinbom.DesignChangeRuleDao;
import com.honda.galc.dao.lcvinbom.LetCategoryCodeDao;
import com.honda.galc.dao.lcvinbom.LetPartialCheckDao;
import com.honda.galc.dao.lcvinbom.ModelLotDao;
import com.honda.galc.dao.lcvinbom.ModelPartApprovalDao;
import com.honda.galc.dao.lcvinbom.ModelPartDao;
import com.honda.galc.dao.lcvinbom.SystemRelationshipDao;
import com.honda.galc.dao.lcvinbom.VinBomPartDao;
import com.honda.galc.dao.lcvinbom.VinPartApprovalDao;
import com.honda.galc.dao.lcvinbom.VinPartDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.dto.IDto;
import com.honda.galc.dto.lcvinbom.BeamPartInputDto;
import com.honda.galc.dto.lcvinbom.DcmsDto;
import com.honda.galc.dto.lcvinbom.ErrorMessageDto;
import com.honda.galc.dto.lcvinbom.LetCategoryCodeDto;
import com.honda.galc.dto.lcvinbom.ModelPartLotAssignDto;
import com.honda.galc.dto.lcvinbom.ModelPartLotDto;
import com.honda.galc.dto.lcvinbom.ModelPartLotFilterDto;
import com.honda.galc.dto.lcvinbom.PartsByProductDto;
import com.honda.galc.dto.lcvinbom.SystemrelationshipDto;
import com.honda.galc.dto.lcvinbom.VinBomPartDto;
import com.honda.galc.dto.lcvinbom.VinBomPartSetDto;
import com.honda.galc.dto.lcvinbom.VinPartDto;
import com.honda.galc.dto.lcvinbom.VinPartFilterDto;
import com.honda.galc.entity.enumtype.VinBomActiveStatus;
import com.honda.galc.entity.enumtype.VinBomApprovalStatus;
import com.honda.galc.entity.enumtype.VinBomAuditLogChangeType;
import com.honda.galc.entity.lcvinbom.DesignChangeRule;
import com.honda.galc.entity.lcvinbom.LetCategoryCode;
import com.honda.galc.entity.lcvinbom.LetPartialCheck;
import com.honda.galc.entity.lcvinbom.ModelLot;
import com.honda.galc.entity.lcvinbom.ModelLotId;
import com.honda.galc.entity.lcvinbom.ModelPart;
import com.honda.galc.entity.lcvinbom.ModelPartApproval;
import com.honda.galc.entity.lcvinbom.SystemRelationship;
import com.honda.galc.entity.lcvinbom.VinBomPart;
import com.honda.galc.entity.lcvinbom.VinBomPartId;
import com.honda.galc.entity.lcvinbom.VinPart;
import com.honda.galc.entity.lcvinbom.VinPartApproval;
import com.honda.galc.entity.lcvinbom.VinPartId;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.rest.BaseRestResource;
import com.honda.galc.rest.json.JsonContentHandler;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.lcvinbom.VinBomService;
import com.honda.galc.service.lcvinbom.property.VinBomPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.lcvinbom.VinBomAuditLoggerUtil;

@Path("VinBom")
public class VinBomResource extends BaseRestResource {
		
	@GET
	@Path("/validateRuleSelection")
	@RolesAllowed("RestUser")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean validateRuleSelection(@Context HttpHeaders httpHeaders,@QueryParam("dcClass") String dcClass, 
			@QueryParam("reflash") int reflash, @QueryParam("interchangable") int interchangable, 
			@QueryParam("scrapParts") int scrapParts) {
		try {
			getLogger().info("REST request VinBom/validateRuleSelection received. Input dcClass: "+dcClass+ ", "+reflash+ ", "+interchangable+ ", "+scrapParts);
			return getVinBomService().validateRuleSelection(dcClass, reflash, interchangable, scrapParts);
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/validateRuleSelection REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/getDesignChange")
	@RolesAllowed("RestUser")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DcmsDto> getDesignChange(@Context HttpHeaders httpHeaders,@QueryParam("plantLocCode") String plantLocCode, 
			@QueryParam("designChangeNumber") String designChangeNumber) {
		try {
			getLogger().info("REST request VinBom/getDesignChange received. Input plantLocCode: "+plantLocCode+", designChangeNumber: "+designChangeNumber);
			return getVinBomService().retrieveDesignChange(plantLocCode, designChangeNumber);
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/getDesignChange REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/findAssignedInspectionsByCategoryCode")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<LetPartialCheck> findAssignedInspectionsByCategoryCode(@Context HttpHeaders httpHeaders,@QueryParam("categoryCodeId") long categoryCodeId) {
		try {
			getLogger().info("REST request VinBom/findAssignedInspectionsByCategoryCode received. Input="+categoryCodeId);
			return getLetPartialCheckDao().findAssignedInspectionsByCategoryCode(categoryCodeId);
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/findAssignedInspectionsByCategoryCode REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/getAllCategoryCodes")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<LetCategoryCode> getAllCategoryCodes(@Context HttpHeaders httpHeaders) {
		try {
			getLogger().info("REST request VinBom/getAllCategoryCodes received.");
			return getLetCategoryCodeDao().findAll();
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/getAllCategoryCodes REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/getPendingModelPartApprovals")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<ModelPartApproval> getPendingModelPartApprovals(@Context HttpHeaders httpHeaders) {
		try {
			getLogger().info("REST request VinBom/getPendingModelPartApprovals received.");
			return getVinBomService().getPendingModelPartApprovals();
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/getPendingModelPartApprovals REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/findAllPendingVinPartApprovals")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<VinPartApproval> findAllPendingVinPartApprovals(@Context HttpHeaders httpHeaders) {
		try {
			getLogger().info("REST request VinBom/findAllPendingVinPartApprovals received.");
			return getVinBomService().findAllPendingVinPartApprovals();
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/findAllPendingVinPartApprovals REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/getVinPartAndStatus")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<VinPartDto> getVinPartAndStatus(@Context HttpHeaders httpHeaders) {
		try {
			getLogger().info("REST request VinBom/getVinPartAndStatus received.");
			return getVinBomService().getVinPartAndStatus();
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/getVinPartAndStatus REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/findDistinctPartNumberBySystemName")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<VinBomPart> findDistinctPartNumberBySystemName(@Context HttpHeaders httpHeaders,@QueryParam("letSystemName") String letSystemName,@QueryParam("productId") String productId) {
		try {
			getLogger().info("REST request VinBom/findDistinctPartNumberBySystemName received. Input: "+letSystemName + ";productId="+productId);
			return getVinBomService().findDistinctPartNumberBySystemName(letSystemName, productId);
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/findDistinctPartNumberBySystemName REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/findAllActiveInterchangeble")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<ModelPart> findAllActiveInterchangeble(@Context HttpHeaders httpHeaders) {
		try {
			getLogger().info("REST request VinBom/findAllActiveInterchangeble received");
			return getModelPartDao().findAllActiveInterchangeble();
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/findAllActiveInterchangeble REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/findDistinctLetSystemName")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> findDistinctLetSystemName(@Context HttpHeaders httpHeaders) {
		try {
			getLogger().info("REST request VinBom/findDistinctLetSystemName received");
			return getVinBomPartDao().findDistinctLetSystemName();
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/findDistinctLetSystemName REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/findAllSystemRelation")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<SystemRelationship> findAllSystemRelationship(@Context HttpHeaders httpHeaders) {
		try {
			getLogger().info("REST request VinBom/findAllSystemRelation received");
			return getSystemRelationshipDao().findAllSystemRelationship();
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/findAllSystemRelation REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/getAllModelYears")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getAllModelYears(@Context HttpHeaders httpHeaders) {
		try {
			getLogger().info("REST request VinBom/getAllModelYears received");
			return getFrameSpecDao().findAllModelYearCodes();
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/getAllModelYears REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@POST
	@Path("/getModelCodesByModelYearCode")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getModelCodesByModelYearCode(@Context HttpHeaders httpHeaders,List<String> modelYearCodes) {
		try {
			getLogger().info("REST request VinBom/getModelCodesByModelYearCode received");
			return getFrameSpecDao().findModelCodes(modelYearCodes);
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/getModelCodesByModelYearCode REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/getModelTypeByYMTOC")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public Set<String> getModelTypeByYMTOC(@Context HttpHeaders httpHeaders,@QueryParam("modelYear") String modelYear, @QueryParam("modelCode") String modelCode, @QueryParam("modelType") String modelType, 
			@QueryParam("modelOption") String modelOption, @QueryParam("extColor") String extColor, @QueryParam("intColor") String intColor) {
		try {
			getLogger().info("REST request VinBom/getModelTypeByYMTOC received");
			List<FrameSpec> frameSpecs = getFrameSpecDao().findAllByYMTOCWildCard(modelYear, modelCode, modelType, modelOption, extColor, intColor);
			Set<String> modelTypes = new HashSet<String>();
			if(frameSpecs!=null && !frameSpecs.isEmpty()) {
				for(FrameSpec frameSpec: frameSpecs) {
					modelTypes.add(frameSpec.getModelTypeCode());
				}
			}
			return modelTypes;
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/getModelTypeByYMTOC REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@POST
	@Path("/findPartsByFilter")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<VinBomPartDto> findPartsByFilter(@Context HttpHeaders httpHeaders,VinBomPartDto vinBomPartDtoFilter) {
		try {
			getLogger().info("REST request VinBom/findPartsByFilter received");
			return getVinBomPartDao().findAllByFilter(vinBomPartDtoFilter);
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/findPartsByFilter REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@POST
	@Path("/findAllPartsByFilter")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<VinBomPartDto> findAllPartsByFilter(@Context HttpHeaders httpHeaders,VinBomPartDto vinBomPartDtoFilter) {
		try {
			getLogger().info("REST request VinBom/findAllPartsByFilter received");
			return getVinBomPartDao().findAllByFilterNative(vinBomPartDtoFilter);
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/findAllPartsByFilter REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@POST
	@Path("/createVinBomRules")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<ModelPart> createVinBomRules(@Context HttpHeaders httpHeaders,VinBomPartSetDto vinBomPartSet) {
		try  {
			getLogger().info("REST request VinBom/createVinBomRules received");
			List<ModelPart> modelPartList = getVinBomService().createVinBomRules(vinBomPartSet);
			getLogger().info("REST request VinBom/createVinBomRules completed");
			return modelPartList;
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/createVinBomRules REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
		

	}

	
	@GET
	@Path("/getAvailableLotAssignments")
	@RolesAllowed("RestUser")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ModelPartLotDto> getAvailableLotAssignments(@Context HttpHeaders httpHeaders,@QueryParam("pendingOnly") String pendingOnly) {
		try {
			getLogger().info("REST request VinBom/getAvailableLotAssignments received");
			return getVinBomService().getAvailableLotAssignments(Boolean.valueOf(pendingOnly));
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/getAvailableLotAssignments REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/getLines")
	@RolesAllowed("RestUser")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object[]> getLines(@Context HttpHeaders httpHeaders,@QueryParam("plantCode") String plantCode) {
		try {
			getLogger().info("REST request VinBom/getLines received, input: "+plantCode);
			return getVinBomService().getLines(plantCode);
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/getLines REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/getProductionLotsByProdDateAndLine")
	@RolesAllowed("RestUser")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PreProductionLot> getProductionLotsByProdDateAndLine(@Context HttpHeaders httpHeaders,@QueryParam("productionDate") String productionDate, 
			@QueryParam("lineNo") String lineNo) {
		try {
			getLogger().info("REST request VinBom/getProductionLotsByProdDateAndLine received");
			return getVinBomService().getProductionLotsByProdDateAndLine(CommonUtil.convertDate(productionDate), lineNo);
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/getProductionLotsByProdDateAndLine REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	

	/**
	 * The LET device will send the VIN and optionally a system name.
	 * Example: /getPartNumber?productId=5J8TC2H68ML022679&systemName=ADS_PROD
	 */
	@GET
	@Path("/getPartNumber")
	@RolesAllowed("RestUser")
	@Produces(MediaType.APPLICATION_JSON)
	public IDto getPartNumber(@Context HttpHeaders httpHeaders,@QueryParam("productId") String productId, @DefaultValue("")
			@QueryParam("systemName") String systemName) {
		try {
			getLogger().info("REST request VinBom/getPartNumber received: productId="+productId+"; systemName="+systemName);
			if(StringUtils.isBlank(productId)) {
				return new ErrorMessageDto("9001","Invalid Parameters");
			}
			PartsByProductDto partsByProductDto = getVinBomService().getPartsByProductForSystem(productId, systemName);
			if(partsByProductDto == null) {
				return new ErrorMessageDto("9099","Error Processing Request");
			}else if(partsByProductDto.getPartsList().size() == 0) {
				return new ErrorMessageDto("9011","No record Found");
			}
			return partsByProductDto;
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/getPartNumber REST request");
			return new ErrorMessageDto("9099","Error Processing Request");
		}
	}
	
	@GET
	@Path("/getPartNumberByProductIdAndSystemName")
	@RolesAllowed("RestUser")
	@Produces(MediaType.APPLICATION_JSON)
	public List<VinPartId> getPartNumberByProductIdAndSystemName(@Context HttpHeaders httpHeaders,@QueryParam("productId") String productId, @DefaultValue("")
			@QueryParam("systemName") String systemName) {
		try {
			getLogger().info("REST request VinBom/getPartNumber received: productId="+productId+"; systemName="+systemName);
			
			return getVinPartDao().getPartNumber(productId, systemName);
			
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/getPartNumber REST request");
			
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@POST
	@Path("/updateBeamPartData")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void updateBeamPartData(@Context HttpHeaders httpHeaders,BeamPartInputDto beamPartInputDto) {
		try  {
			getLogger().info("REST request VinBom/updateBeamPartData received: input="+beamPartInputDto);
			getVinBomService().updateBeamPartData(beamPartInputDto.getPlantLocCode(), beamPartInputDto.getDivision());
			getLogger().info("REST request VinBom/updateBeamPartData completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/updateBeamPartData REST request");
		}
	}
	
	@POST
	@Path("/updateCategoryInspections")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void updateCategoryInspections(@Context HttpHeaders httpHeaders,LetCategoryCodeDto letCategoryCodeDto) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/updateCategoryInspections received: input="+letCategoryCodeDto);
			getVinBomService().updateCategoryInspections(letCategoryCodeDto, userId);
			getLogger().info("REST request VinBom/updateCategoryInspections completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/updateCategoryInspections REST request");
		}
	}
	
	@POST
	@Path("/saveModelPart")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void saveModelPart(@Context HttpHeaders httpHeaders,ModelPart modelPart) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/saveModelPart received: input="+modelPart);
			modelPart = getModelPartDao().save(modelPart);
			VinBomAuditLoggerUtil.logAuditInfo(ModelPart.class, modelPart, userId, VinBomAuditLogChangeType.INSERT);
			getLogger().info("REST request VinBom/saveModelPart completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/saveModelPart REST request");
		}
	}
	
	@POST
	@Path("/saveModelLot")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void saveModelLot(@Context HttpHeaders httpHeaders,ModelLot modelLot) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/saveModelLot received: input="+modelLot);
			modelLot = getModelLotDao().saveModelLot(modelLot);
			VinBomAuditLoggerUtil.logAuditInfo(ModelLot.class, modelLot, userId, VinBomAuditLogChangeType.INSERT);
			getLogger().info("REST request VinBom/saveModelLot completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/saveModelLot REST request");
		}
	}
	
	@POST
	@Path("/saveVinPartApproval")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void saveVinPartApproval(@Context HttpHeaders httpHeaders,VinPartApproval vinPartApproval) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/saveVinPartApproval received: input="+vinPartApproval);
			getVinBomService().saveVinPartApproval(vinPartApproval,userId);
			getLogger().info("REST request VinBom/saveVinPartApproval completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/saveVinPartApproval REST request");
		}
	}
	
	@POST
	@Path("/saveMultiVinPart")
	@RolesAllowed("RestUser")
	@Consumes({ "application/json" })
	@Produces(MediaType.APPLICATION_JSON)
	public void saveMultiVinPart(@Context HttpHeaders httpHeaders, List<VinPart> vinPartList) {
		try {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/saveMultiVinPart received: input=" + vinPartList);
			getVinBomService().saveMultiVinPart(vinPartList, userId);
			getLogger().info("REST request VinBom/saveMultiVinPart completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/saveMultiVinPart REST request");
		}
	}
	
	@DELETE
	@Path("/removeMultiVinPart")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void removeMultiVinPart(@Context HttpHeaders httpHeaders,@DefaultValue("") @QueryParam("productSpecCode")String productSpecCode, @DefaultValue("") @QueryParam("partNumber")String partNumber, @DefaultValue("") @QueryParam("systemName")String systemName) {
		try {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/removeMultiVinPart received: input=" + partNumber + ", " + productSpecCode + ", " + systemName + " by user-" + userId);
			getVinBomService().removeMultiVinPart(partNumber, productSpecCode, systemName);
			getLogger().info("REST request VinBom/removeMultiVinPart completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/removeMultiVinPart REST request");
		}
	}
	
	@POST
	@Path("/saveMultiVinPartApproval")
	@RolesAllowed("RestUser")
	@Consumes({ "application/json" })
	@Produces(MediaType.APPLICATION_JSON)
	public void saveMultiVinPartApproval(@Context HttpHeaders httpHeaders, List<VinPartApproval> vinPartApproval) {
		try {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/saveMultiVinPartApproval received: input=" + vinPartApproval);
			getVinBomService().saveMultiVinPartApproval(vinPartApproval, userId);
			getLogger().info("REST request VinBom/saveMultiVinPartApproval completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/saveMultiVinPartApproval REST request");
		}
	}

	@POST
	@Path("/savePart")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void savePart(@Context HttpHeaders httpHeaders,VinBomPart vinBomPart) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/savePart received: input="+vinBomPart);
			vinBomPart = getVinBomPartDao().save(vinBomPart);
			VinBomAuditLoggerUtil.logAuditInfo(VinBomPart.class, vinBomPart, userId, VinBomAuditLogChangeType.INSERT);
			getLogger().info("REST request VinBom/savePart completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/savePart REST request");
		}
	}
	
	@POST
	@Path("/saveModelPartApproval")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void saveModelPartApproval(@Context HttpHeaders httpHeaders,ModelPartApproval modelPartApproval) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/saveModelPartApproval received: input="+modelPartApproval);
			getVinBomService().saveModelPartApproval(modelPartApproval,userId);
			getLogger().info("REST request VinBom/saveModelPartApproval completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/saveModelPartApproval REST request");
		}
	}
	
	@PUT
	@Path("/approveVinPartChange")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void approveVinPartChange(@Context HttpHeaders httpHeaders,VinPartApproval vinPartApproval) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/approveVinPartChange received: input="+vinPartApproval.getVinPartApprovalId()
					+", "+vinPartApproval.getApproveAssociateNumber());
			getVinBomService().approveVinPartChange(vinPartApproval.getVinPartApprovalId(), vinPartApproval.getApproveAssociateNumber());
			getLogger().info("REST request VinBom/approveVinPartChange completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/approveVinPartChange REST request");
		}
	}
	
	@PUT
	@Path("/approveMultiVinPartChange")
	@RolesAllowed("RestUser")
	@Consumes({ "application/json" })
	@Produces(MediaType.APPLICATION_JSON)
	public void approveMultiVinPartChange(@Context HttpHeaders httpHeaders, List<Long> vinPartApprovalIdList) {
		try {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/approveVinPartChange received: ");
			getVinBomService().approveMultiVinPartChange(vinPartApprovalIdList, userId);
			getLogger().info("REST request VinBom/approveMultiVinPartChange completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/approveMultiVinPartChange REST request");
		}
	}
	
	
	@POST
	@Path("/approveModelPartChange")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void approveModelPartChange(@Context HttpHeaders httpHeaders,ModelPartApproval modelPartApproval) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/approveModelPartChange received: input="+modelPartApproval.getModelPartApprovalId()
					+", "+modelPartApproval.getApproveAssociateNumber());
			getVinBomService().approveModelPartChange(modelPartApproval.getModelPartApprovalId(), modelPartApproval.getApproveAssociateNumber());
			getLogger().info("REST request VinBom/approveModelPartChange completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/approveModelPartChange REST request");
		}
	}
	
	@PUT
	@Path("/denyVinPartChange")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void denyVinPartChange(@Context HttpHeaders httpHeaders,VinPartApproval vinPartApproval) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/denyVinPartChange received: input="+vinPartApproval.getVinPartApprovalId()
					+", "+vinPartApproval.getApproveAssociateNumber());
			getVinPartApprovalDao().denyChange(vinPartApproval.getVinPartApprovalId(), vinPartApproval.getApproveAssociateNumber());
			getLogger().info("REST request VinBom/denyVinPartChange completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/denyVinPartChange REST request");
		}
	}
	
	@POST
	@Path("/denyModelPartChange")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void denyModelPartChange(@Context HttpHeaders httpHeaders,ModelPartApproval modelPartApproval) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/denyModelPartChange received: input="+modelPartApproval.getModelPartApprovalId()
					+", "+modelPartApproval.getApproveAssociateNumber());
			getVinBomService().denyModelPartChange(modelPartApproval.getModelPartApprovalId(), modelPartApproval.getApproveAssociateNumber());
			getLogger().info("REST request VinBom/denyModelPartChange completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/denyModelPartChange REST request");
		}
	}
	
	@PUT
	@Path("/setInterchangableInactive")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void setInterchangableInactive(@Context HttpHeaders httpHeaders,long modelPartId) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/setInterchangableInactive received: input="+modelPartId);
			getVinBomService().setInterchangableInactive(modelPartId,userId);
			getLogger().info("REST request VinBom/setInterchangableInactive completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/setInterchangableInactive REST request");
		}
	}
	
	/**
	 * URL: galcSiteUrl/RestWeb/v2/VinBom/putFlashResults
	 * JSON Input Example:
	 * {
	 *     "productId": "5J8TC2H68ML022679",
	 *     "EPS_SOFT": "39110TX5 A1**",
	 *     "ABSVSA_PROD": "57110TX5 K010"
	 * }
	 */
	@PUT
	@Path("/putFlashResults")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void putFlashResults(String jsonLetVinPart) {
		try  {
			getLogger().info("REST request VinBom/putFlashResults received: input="+jsonLetVinPart);
			
			DataContainer letVinPartDataContainer = JsonContentHandler.getGson().fromJson(jsonLetVinPart.toUpperCase(), DataContainer.class);
			getVinPartDao().putFlashResults(letVinPartDataContainer);
			VinBomAuditLoggerUtil.logAuditInfo(VinPart.class, jsonLetVinPart, null, VinBomAuditLogChangeType.UPDATE);
			getLogger().info("REST request VinBom/putFlashResults completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/putFlashResults REST request");
		}
	}
	
	@DELETE
	@Path("/removeLetCategory")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void removeLetCategory(@Context HttpHeaders httpHeaders,long categoryCodeId) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/removeLetCategory received: input="+categoryCodeId +" by user-"+userId);
			getVinBomService().removeLetCategory(categoryCodeId,userId);
			getLogger().info("REST request VinBom/removeLetCategory completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/removeLetCategory REST request");
		}
	}
	
	@DELETE
	@Path("/removePart")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void removePart(@Context HttpHeaders httpHeaders,VinBomPartId vinBomPartId) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/removePart received: input="+vinBomPartId+" by user-"+userId);
			getVinBomPartDao().removeByKey(vinBomPartId);
			VinBomAuditLoggerUtil.logAuditInfo(VinBomPart.class, vinBomPartId, userId, VinBomAuditLogChangeType.DELETE);
			getLogger().info("REST request VinBom/removePart completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/removePart REST request");
		}
	}
	
	@DELETE
	@Path("/deleteModelPartAssignment")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteModelPartAssignment(@Context HttpHeaders httpHeaders,@QueryParam("modelPartId")long modelPartId, @QueryParam("planCode")String planCode, @QueryParam("productionLot")String productionLot) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/deleteModelPartAssignment received: input="+modelPartId+" by user-"+userId);
			getVinBomService().deleteModelPartAssignment(modelPartId, planCode, productionLot,userId);
			getLogger().info("REST request VinBom/deleteModelPartAssignment completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/deleteModelPartAssignment REST request");
		}
	}
	
	@DELETE
	@Path("/removeVinPart")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void removeVinPart(@Context HttpHeaders httpHeaders,@QueryParam("productId")String productId, @QueryParam("dcPartNumber")String dcPartNumber, @QueryParam("letSystemName")String letSystemName) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/removeVinPart received: input="+productId+", "+dcPartNumber+", "+letSystemName+" by user-"+userId);
			getVinBomService().removeVinPart(productId,letSystemName,dcPartNumber,userId);
			getLogger().info("REST request VinBom/removeVinPar completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/removeVinPar REST request");
		}
	}
	
	@POST
	@Path("/denyMultiVinPartChanges")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void denyMultiVinPartChanges(@Context HttpHeaders httpHeaders,List<Long> vinPartApprovalIdList) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/denyMultiVinPart received: input="+vinPartApprovalIdList);
			getVinBomService().denyMultiVinPartChanges(vinPartApprovalIdList, userId);
			getLogger().info("REST request VinBom/denyMultiVinPart completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/denyMultiVinPart REST request");
		}
	}
	
	@GET
	@Path("/getAvailableDesignChangeRules")
	@RolesAllowed("RestUser")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DesignChangeRule> getAvailableDesignChangeRules(@Context HttpHeaders httpHeaders) {
		try {
			getLogger().info("REST request VinBom/getAvailableDesignChangeRules received");
			return getDesignChangeRuleDao().findAll();
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/getAvailableDesignChangeRules REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@POST
	@Path("/saveModelPartApprovalList")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void saveMultiModelPartApprovals(@Context HttpHeaders httpHeaders,List<ModelPartApproval> modelPartApprovalList) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/saveModelPartApprovalList received:");
			getVinBomService().saveMultiModelPartApproval(modelPartApprovalList, userId);
			getLogger().info("REST request VinBom/saveModelPartApproval completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/saveModelPartApproval REST request");
		}
	}
	
	@POST
	@Path("/saveModelLotsAndModelPartApprovals")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void saveModelLotsAndModelPartApprovals(@Context HttpHeaders httpHeaders,ModelPartLotAssignDto modelPartLotAssignDto) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/saveModelLotsAndModelPartApprovals received:");
			getVinBomService().saveModelLotAndModelPartApproval(modelPartLotAssignDto.getModelPartLotDtoList(), modelPartLotAssignDto.getSelectedPlanCode(), modelPartLotAssignDto.getSelectedProductionLot(), userId);
						
			getLogger().info("REST request VinBom/saveModelLot completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/saveModelLot REST request");
		}
	}
	
	@POST
	@Path("/approveMultiModelPartChange")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void approveMultiModelPartChange(@Context HttpHeaders httpHeaders,List<Long> modelPartApprovalIdList) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/approveMultiModelPartChange received: ");
			getVinBomService().approveMultiModelPartChange(modelPartApprovalIdList, userId);
			getLogger().info("REST request VinBom/approveModelPartChange completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/approveModelPartChange REST request");
		}
	}
	
	@POST
	@Path("/denyMultiModelPartChange")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void denyMultiModelPartChange(@Context HttpHeaders httpHeaders,List<Long> modelPartApprovalIdList) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("REST request VinBom/denyMultiModelPartChange received: ");
			getVinBomService().denyMultiModelPartChange(modelPartApprovalIdList, userId);
			getLogger().info("REST request VinBom/denyModelPartChange completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/denyModelPartChange REST request");
		}
	}
	
	@GET
	@Path("/getLotAssignmentFilters")
	@RolesAllowed("RestUser")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ModelPartLotFilterDto> getLotAssignmentFilters(@Context HttpHeaders httpHeaders) {

		try {
			getLogger().info("REST request VinBom/getLotAssignmentFilters received");
			
			return getVinBomService().getLotAssignmentFilters();
			
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/getLotAssignmentFilters REST request");
			
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/filterLotAssignments")
	@RolesAllowed("RestUser")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ModelPartLotDto> filterLotAssignments(@Context HttpHeaders httpHeaders,@DefaultValue("") @QueryParam("dcNumber") String dcNumber, @DefaultValue("")
			@QueryParam("systemName") String systemName, @DefaultValue("") @QueryParam("partNumber") String partNumber, @DefaultValue("") @QueryParam("model") String model, @DefaultValue("") @QueryParam("active") String active) {

		try {
			getLogger().info("REST request VinBom/filterLotAssignments received: dcNumber="+dcNumber+"; systemName="+systemName +"; dcPartNumber="+partNumber +"; model="+model +"; active="+active);
			
			return getVinBomService().filterLotAssignments(dcNumber, partNumber, systemName, model, active);
			
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/filterLotAssignments REST request");
			
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/getDistinctModels")
	@RolesAllowed("RestUser")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getDistinctModels(@Context HttpHeaders httpHeaders) {

		try {
			getLogger().info("REST request VinBom/getDistinctModels received:");

			return getModelPartDao().findAllDistinctModels();

		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/getDistinctModels REST request");

		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/getVinPartFilters")
	@RolesAllowed("RestUser")
	@Produces(MediaType.APPLICATION_JSON)
	public List<VinPartFilterDto> getVinPartFilters(@Context HttpHeaders httpHeaders) {

		try {
			getLogger().info("REST request VinBom/getVinPartFilters received:");
			
			return getVinBomService().getVinPartFilters();
			
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/getVinPartFilters REST request");
			
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/filterVinParts")
	@RolesAllowed("RestUser")
	@Produces(MediaType.APPLICATION_JSON)
	public List<VinPartDto> filterVinParts(@Context HttpHeaders httpHeaders,@DefaultValue("") @QueryParam("productId") String productId, @DefaultValue("")
			@QueryParam("systemName") String systemName, @DefaultValue("") @QueryParam("partNumber") String partNumber,
			@DefaultValue("") @QueryParam("productionLot") String productionLot, @DefaultValue("") @QueryParam("productSpecCode") String productSpecCode) {

		try {
			getLogger().info("REST request VinBom/filterVinParts received: productId=" + productId + "; systemName=" + systemName + "; dcPartNumber=" + partNumber + "; productionLot=" + productionLot + "; productSpecCode=" + productSpecCode);

			if (productId.isEmpty() && productionLot.isEmpty() && !productSpecCode.isEmpty()) {
				return getVinBomService().filterVinPartsByCriteria(partNumber, productSpecCode, systemName);
			} else {
				return getVinBomService().filterVinParts(productId, productionLot, partNumber, systemName);
			}

		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/filterVinParts REST request");

		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@GET
	@Path("/getDcmsPlantCodes")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getDcmsPlantCodes(@Context HttpHeaders httpHeaders) {
		try {
			getLogger().info("REST request VinBom/getDcmsPlantCodes received");
			String dcmsPlantCodes= PropertyService.getPropertyBean(VinBomPropertyBean.class).getDcmsPlantCodes();
			String[] plantCodes = StringUtils.split(dcmsPlantCodes,",");
			return Arrays.asList(plantCodes);
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/getDcmsPlantCodes REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}
	
	@POST
	@Path("/saveSystemRelationship")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void saveSystemRelationship(@Context HttpHeaders httpHeaders,List<SystemrelationshipDto> systemRelationshipDto) {
		try {
			getLogger().info("REST request VinBom/saveSystemRelationship received");
			getVinBomService().saveSystemRelationship(systemRelationshipDto);
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/saveSystemRelationship REST request");
		}
	}
	
	@DELETE
	@Path("/deleteSystemRelationship")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteSystemRelationship(@Context HttpHeaders httpHeaders,List<SystemrelationshipDto> systemRelationshipDto) {
		try {
			getLogger().info("REST request VinBom/deleteSystemRelationship received");
			getVinBomService().deleteSystemRelationship(systemRelationshipDto);
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/deleteSystemRelationship REST request");
		}
	}

	@POST
	@Path("/saveReturnToActive")
	@RolesAllowed("RestUser")
	@Consumes({"application/json"})
	@Produces(MediaType.APPLICATION_JSON)
	public void saveReturnToActive(@Context HttpHeaders httpHeaders,List<ModelPartApproval> modelPartApproval) {
		try  {
			String userId = httpHeaders.getHeaderString("userId");
			getLogger().info("Return to Active REST request VinBom/saveReturnToActive received: input="+modelPartApproval);
			getVinBomService().saveMultiModelPartApproval(modelPartApproval,userId);
			getLogger().info("Return to Active REST request VinBom/saveReturnToActive completed");
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process VinBom/saveReturnToActive REST request");
		}
	}
	
	protected VinBomService getVinBomService() {
		return ServiceFactory.getService(VinBomService.class);
	}
	
	protected VinPartDao getVinPartDao() {
		return ServiceFactory.getDao(VinPartDao.class);
	}
	
	protected VinBomPartDao getVinBomPartDao() {
		return ServiceFactory.getDao(VinBomPartDao.class);
	}
	
	protected ModelPartDao getModelPartDao() {
		return ServiceFactory.getDao(ModelPartDao.class);
	}
	
	protected ModelLotDao getModelLotDao() {
		return ServiceFactory.getDao(ModelLotDao.class);
	}
	
	protected ModelPartApprovalDao getModelPartApprovalDao() {
		return ServiceFactory.getDao(ModelPartApprovalDao.class);
	}
	
	protected VinPartApprovalDao getVinPartApprovalDao() {
		return ServiceFactory.getDao(VinPartApprovalDao.class);
	}
	
	protected LetCategoryCodeDao getLetCategoryCodeDao() {
		return ServiceFactory.getDao(LetCategoryCodeDao.class);
	}
	
	protected LetPartialCheckDao getLetPartialCheckDao() {
		return ServiceFactory.getDao(LetPartialCheckDao.class);
	}
	
	protected FrameSpecDao getFrameSpecDao() {
		return ServiceFactory.getDao(FrameSpecDao.class);
	}
	
	protected DesignChangeRuleDao getDesignChangeRuleDao() {
		return ServiceFactory.getDao(DesignChangeRuleDao.class);
	}
	
	protected SystemRelationshipDao getSystemRelationshipDao() {
		return ServiceFactory.getDao(SystemRelationshipDao.class);
	}

	
	protected Logger getLogger() {
		return getLogger(this.getClass().getSimpleName());
	}
}
