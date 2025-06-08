package com.honda.galc.client.teamleader.mtctomodelgroup;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.mvc.AbstractModel;
import com.honda.galc.client.teamleader.property.MtcToModelGroupPropertyBean;
import com.honda.galc.common.exception.PropertyException;
import com.honda.galc.dao.product.ModelGroupDao;
import com.honda.galc.dao.product.ModelGroupingDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dto.MtcToModelGroupDto;
import com.honda.galc.dto.qi.QiMtcToEntryModelDto;
import com.honda.galc.entity.product.ModelGroup;
import com.honda.galc.entity.product.ModelGroupId;
import com.honda.galc.entity.product.ModelGrouping;
import com.honda.galc.entity.product.ModelGroupingId;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3>MtcToModelGroupMaintenanceModel Class description</h3>
 * <p> MtcToModelGroupMaintenanceModel class manipulates the data in Model Group and Model grouping </p>
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
 * @author L&T Infotech<br>
 *         March 31, 2017
 * 
 * 
 */
public class MtcToModelGroupMaintenanceModel extends AbstractModel {

	protected MtcToModelGroupPropertyBean mtcToModelGroupPropertyBean;

	public MtcToModelGroupMaintenanceModel() {
		super();
	}


	/**
	 * This method is used to update new MtcList to ModelGroup.
	 * @param previousMtcList 
	 * @param previousMtcList	 
	 * 
	 * */
	public List<ModelGrouping> updateNewMtcListToModelGroup(List<String> previousMtcList, List<ModelGrouping> newModelGroupingList, String modelGroup, String system) {
		List<ModelGrouping> updatedModelGroupingList = new ArrayList<ModelGrouping>();
		List<ModelGrouping> modelGroupingList = findPreviousModelGroupGroupingList(previousMtcList, modelGroup, system);
		if (modelGroupingList != null && modelGroupingList.size() > 0) {
			removeAllModelGroupGrouping(modelGroupingList);
			for (ModelGrouping modelGroupGroup : modelGroupingList) {
				if (modelGroupGroup != null) {
					ModelGrouping modelGroupGrouping = new ModelGrouping();
					ModelGroupingId modelGroupGroupingId = new ModelGroupingId();
					modelGroupGroupingId.setModelGroup(StringUtils.trimToEmpty(modelGroupGroup.getId().getModelGroup()));
					modelGroupGroupingId.setMtcModel(StringUtils.trimToEmpty(modelGroupGroup.getId().getMtcModel()));
					modelGroupGrouping.setId(modelGroupGroupingId);
					modelGroupGrouping.getId().setSystem(system);
					modelGroupGrouping.setCreateUser(modelGroupGroup.getCreateUser());
					modelGroupGrouping.setUpdateUser(modelGroupGroup.getUpdateUser());
					updatedModelGroupingList.add(modelGroupGrouping);
				}
			}
		}
		if (modelGroupingList != null) {
			for (ModelGrouping modelGroupGroup : newModelGroupingList) {
				modelGroupGroup.getId().setSystem(system);
			}
			updateAllModelGrouping(newModelGroupingList);
		}
		return updatedModelGroupingList;
	}

	/**
	 * This method is used to get new combination Mtc Models.
	 */
	public List<ModelGrouping> getNewMGroupingOfMtcModels(List<ModelGrouping> existingAssignedMtcModelList, List<MtcToModelGroupDto> assignedModelGrouping,String modelGroup) {
		List<ModelGrouping> newCombinationOfMtcModels= new ArrayList<ModelGrouping>();
		if (null != assignedModelGrouping) {
			for (MtcToModelGroupDto dto : assignedModelGrouping) {
				if (null != dto) {
					ModelGrouping modelGroupGroup = new ModelGrouping();
					ModelGroupingId modelGroupGroupingId=new ModelGroupingId();
					modelGroupGroupingId.setModelGroup(modelGroup);
					modelGroupGroupingId.setMtcModel(dto.getMtcModel());
					modelGroupGroup.setId(modelGroupGroupingId);
					modelGroupGroup.setCreateUser(getUserId());
					if (null != existingAssignedMtcModelList) {
						if (!existingAssignedMtcModelList.contains(modelGroupGroup))
							newCombinationOfMtcModels.add(modelGroupGroup);
					} else {
						newCombinationOfMtcModels.add(modelGroupGroup);
					}
				}
			}
		}
		return newCombinationOfMtcModels;
	}

	/**
	 * This method is used to find existing assigned Mtc Models.
	 * @param selectedModelGroup 
	 */
	public List<ModelGrouping> findExistingAssignedMtcModelList(String selectedModelGroup, String selectedSystem) {
		List<ModelGrouping> existingAssignedMtcModelList = new ArrayList<ModelGrouping>();
		List<ModelGrouping> searchedModelGroupingList=findAllModelGroupingsByModelGroupAndSystem(selectedModelGroup, selectedSystem);
		if (null != searchedModelGroupingList) {
			ModelGrouping previousElement;
			ModelGroupingId modelGroupGroupingId;
			for (MtcToModelGroupDto element : setMtcModelToDto(searchedModelGroupingList)) {
				previousElement = new ModelGrouping();
				modelGroupGroupingId=new ModelGroupingId();
				modelGroupGroupingId.setModelGroup(selectedModelGroup);
				modelGroupGroupingId.setMtcModel(element.getMtcModel());
				previousElement.setId(modelGroupGroupingId);
				existingAssignedMtcModelList.add(previousElement);
			}
		} 
		return existingAssignedMtcModelList;
	}


	/**
	 * This method is used to populate available Mtc Models.
	 * @param filter
	 * @param productType
	 * @param productTypeName
	 * @param assignedMtcModelList
	 */
	public List<MtcToModelGroupDto> findAvailableMtcModelData(String filter, String productType, String productTypeName, List<MtcToModelGroupDto> assignedMtcModelList) {
		List<MtcToModelGroupDto> availableMtcModelList= new ArrayList<MtcToModelGroupDto>();
		List<MtcToModelGroupDto> mtcModelList = findAllByFilterAndProductType(filter, productType);
		if (ProductTypeUtil.isMbpnProduct(productTypeName)) {
			availableMtcModelList = findAllAvailableMtcByMbpn(mtcModelList);
		} 
		else {
			availableMtcModelList =findAvailableMtcByProduct(mtcModelList);
		}
		if (!availableMtcModelList.isEmpty()) {
			List<MtcToModelGroupDto> existingList = findExistingMtcModel(findAllModelGroupings());
			availableMtcModelList.removeAll(existingList);
			for(MtcToModelGroupDto assignedMtcModel : assignedMtcModelList){
				MtcToModelGroupDto newAssignedMTCModel = new MtcToModelGroupDto();
				newAssignedMTCModel.setModelYearCode(assignedMtcModel.getMtcModel().substring(0, 1));
				newAssignedMTCModel.setModelCode(assignedMtcModel.getMtcModel().substring(1));
				newAssignedMTCModel.setMtcModel(StringUtils.EMPTY);
				availableMtcModelList.remove(newAssignedMTCModel);
			}
		}
		return availableMtcModelList;
	}


	/**
	 * This method is used to populate existing Mtc Models.
	 * @param mtcModelList
	 */
	private List<MtcToModelGroupDto> findExistingMtcModel(List<ModelGrouping> mtcModelList) {
		List<MtcToModelGroupDto> assgndMtcList = new ArrayList<MtcToModelGroupDto>();
		if (mtcModelList != null) {
			MtcToModelGroupDto mtcToModelGroupDto;
			for (ModelGrouping object : mtcModelList) {
				mtcToModelGroupDto = new MtcToModelGroupDto();
				String mtcModel = object.getId().getMtcModel();
				mtcToModelGroupDto.setModelYearCode(StringUtils.trimToEmpty(mtcModel).substring(0, 1));
				mtcToModelGroupDto.setModelCode(StringUtils.trimToEmpty(mtcModel).substring(1, StringUtils.trimToEmpty(mtcModel).length()));
				assgndMtcList.add(mtcToModelGroupDto);
			}
		}
		return assgndMtcList;
	}

	/**
	 * This method is used to populate Mtc Models excluding null and empty values.
	 * @param availableMtcList
	 */
	private List<MtcToModelGroupDto> findAvailableMtcByProduct(List<MtcToModelGroupDto> availableMtcList) {
		List<MtcToModelGroupDto> availableMtcModelList=new ArrayList<MtcToModelGroupDto>();
		for(MtcToModelGroupDto dto :availableMtcList){
			if (null !=dto.getModelYearCode() && null !=dto.getModelCode()) {
				if (!StringUtils.isEmpty(StringUtils.trimToEmpty(dto.getModelYearCode()))&& !StringUtils.isEmpty(StringUtils.trimToEmpty(dto.getModelCode()))) {
					availableMtcModelList.add(dto);
				}
			}
		}
		return availableMtcModelList;
	}


	/**
	 * This method is used to populate Mtc Models excluding null and empty values for Mbpn.
	 * @param availableMtcListByMbpn
	 */
	private List<MtcToModelGroupDto> findAllAvailableMtcByMbpn(List<MtcToModelGroupDto> availableMtcListByMbpn) {
		List<MtcToModelGroupDto> availableMtcModelList=new ArrayList<MtcToModelGroupDto>();
		for(MtcToModelGroupDto dto :availableMtcListByMbpn){
			dto.setModelYearDescription("");
			if (null != dto.getClassNo() && !StringUtils.isEmpty(StringUtils.trimToEmpty(dto.getClassNo()))) {	
				dto.setModelYearCode(StringUtils.trimToEmpty(dto.getClassNo()).substring(0, 1));
				if (!(StringUtils.trimToEmpty(dto.getClassNo()).length() > 1))
					dto.setModelCode(null);
				else
					dto.setModelCode(StringUtils.trimToEmpty(dto.getClassNo()).substring(1,StringUtils.trimToEmpty(dto.getClassNo()).length()));
				availableMtcModelList.add(dto);
			}
		}
		return availableMtcModelList;
	}



	/**
	 * This method is used to set assigned MTC model and description to MtcToModelGroupDto.
	 * @param selectedModelGroup
	 * @param selectedProductType
	 * @param selectedSystem
	 */
	public List<MtcToModelGroupDto> setMtcModelYearDescriptionToDto(String selectedModelGroup, String selectedProductType, String selectedSystem) {
		List<MtcToModelGroupDto> assignedMtcModelList = new ArrayList<MtcToModelGroupDto>();
		List<ModelGrouping> assginedMtcModel=findAllModelGroupingsByModelGroupAndSystem(selectedModelGroup, selectedSystem);
		if (assginedMtcModel != null) {
			for (ModelGrouping modelGroupGrouping : assginedMtcModel) {
				MtcToModelGroupDto mtcToModelGroupDto = new MtcToModelGroupDto();
				mtcToModelGroupDto.setMtcModel(modelGroupGrouping.getId().getMtcModel());
				if (!(ProductTypeUtil.isMbpnProduct(selectedProductType))) {
					List<MtcToModelGroupDto> mtcToModelGroupDtoList=findAllByFilterAndProductType(modelGroupGrouping.getId().getMtcModel(), selectedProductType);
					if (mtcToModelGroupDtoList.size()>0) {
						mtcToModelGroupDto.setModelYearDescription(mtcToModelGroupDtoList.get(0).getModelYearDescription());
						mtcToModelGroupDto.setModelDescription(mtcToModelGroupDtoList.get(0).getModelDescription());
					}
				} else {
					mtcToModelGroupDto.setModelYearDescription("");
					mtcToModelGroupDto.setModelDescription("");
				}
				assignedMtcModelList.add(mtcToModelGroupDto);
			}
		}
		return assignedMtcModelList;
	}

	/**
	 * This method is used to fetch all the existing ModelGrouping by Mtc Models.
	 * @param previousMtcList
	 */
	public List<ModelGrouping> findPreviousModelGroupGroupingList(List<String> previousMtcList, String modelGroup, String system) {
		List<ModelGrouping> previousModelGroupGroupingList = new ArrayList<ModelGrouping>();
		for (String mtcModel : previousMtcList) {
			ModelGrouping modelGroupGrouping = getDao(ModelGroupingDao.class).findByKey(new ModelGroupingId(modelGroup, mtcModel, system));
			previousModelGroupGroupingList.add(modelGroupGrouping);
		}
		return previousModelGroupGroupingList;
	}

	public MtcToModelGroupPropertyBean getProperty() {
		if (this.mtcToModelGroupPropertyBean == null) {
			final String hostName = getApplicationContext().getTerminal().getHostName();
			this.mtcToModelGroupPropertyBean = PropertyService.getPropertyBean(MtcToModelGroupPropertyBean.class, hostName);
		}
		return this.mtcToModelGroupPropertyBean;
	}

	/**
	 * This method fetches the data of product types.
	 */
	public List<String> getProductTypes() {
		List<String> productTypes;
		try {
			productTypes = Arrays.asList(getProperty().getMtcToModelGroupProductTypes());
		} catch (PropertyException pe) {
			productTypes = getDao(ProductTypeDao.class).findAllProductTypes();
		}
		return productTypes;
	}

	/**
	 * This method fetches the model group systems.
	 */
	public List<String> getSystems() {
		List<String> systems;
		try {
			systems = Arrays.asList(getProperty().getMtcToModelGroupSystems());
		} catch (PropertyException pe) {
			systems = getDao(ModelGroupDao.class).findAllSystems();
		}
		return systems;
	}

	/**
	 * This method refresh the Model Group on selected Product type.
	 * @param productType
	 */
	public List<ModelGroup> findAllByProductTypeAndSystem(String productType, String system) {
		return getDao(ModelGroupDao.class).findAllByProductTypeAndSystem(productType, system);
	}

	/**
	 * This method fetches the all Model Grouping for a particular Model Group.
	 * @param ModelGroup
	 */
	public List<ModelGrouping> findAllModelGroupingsByModelGroup(String ModelGroup) {
		return getDao(ModelGroupingDao.class).findAllByModelGroup(ModelGroup);
	}

	/**
	 * This method fetches the all Model Grouping for a particular Model Group.
	 * @param modelGroup
	 * @param system
	 */
	public List<ModelGrouping> findAllModelGroupingsByModelGroupAndSystem(String modelGroup, String system) {
		return getDao(ModelGroupingDao.class).findAllByModelGroupAndSystem(modelGroup, system);
	}

	/**
	 * Returns any existing model groupings for the given mtc model, system and product type.
	 * @param mtcModel
	 * @param system
	 * @param productType
	 * @return
	 */
	public List<ModelGrouping> findExistingModelGroupings(String mtcModel, String system, String productType) {
		return getDao(ModelGroupingDao.class).findAllByMtcModelSystemAndProductType(mtcModel, system, productType);
	}

	/**
	 * This method sets the Available Mtc Model based on product type and system selections
	 * and filter.
	 * @param filter
	 * @param productType
	 */
	public List<MtcToModelGroupDto> findAllByFilterAndProductType(String filter, String productType) {
		List<MtcToModelGroupDto> availableMtcList = new ArrayList<MtcToModelGroupDto>();
		if (ProductTypeUtil.getProductSpecDao(productType) != null) {
			availableMtcList = convertQiDtoToNonQiDto(ProductTypeUtil.getProductSpecDao(productType).findAllMtcModelYearCodesByFilter(filter, productType));
		}
		return availableMtcList;
	}


	/**
	 * This method gets Model Group for a particular Product Type .
	 * @param status
	 * @param productType
	 */
	public List<ModelGroup> findAllByStatusProductTypeAndSystem(String status, String productType, String system) {
		return getDao(ModelGroupDao.class).findAllByStatusProductTypeAndSystem(status, productType, system);
	}

	/**
	 * This method is used to create a new Model Group .
	 * @param createdModelGroup
	 */
	public void createModelGroup(ModelGroup createdModelGroup) {
		getDao(ModelGroupDao.class).save(createdModelGroup);
	}

	/**
	 * This method is used to update an Model Group .
	 * @param selectedModelGroup
	 */
	public void updateModelGroup(ModelGroup selectedModelGroup) {
		getDao(ModelGroupDao.class).update(selectedModelGroup);

	}

	/**
	 * This method is used to check if the Model Group name already exists for the given system.
	 */
	public boolean isModelGroupExist(String modelName, String system) {
		ModelGroup existingModelGroup = getDao(ModelGroupDao.class).findByKey(new ModelGroupId(modelName, system));
		return existingModelGroup != null;
	}

	/**
	 * This method is used to fetch all the existing Model Group .
	 */
	public List<ModelGroup> findAllModelGroups() {
		return getDao(ModelGroupDao.class).findAll();
	}


	/**
	 * This method is used to fetch all the existing Model Grouping .
	 */
	public List<ModelGrouping> findAllModelGroupings() {
		return getDao(ModelGroupingDao.class).findAll();
	}


	/**
	 * This method is used to fetch existing ModelGrouping by Mtc Models.
	 * @param mtcModel
	 */
	public ModelGrouping  findModelGroupingByMtcModel(String mtcModel, String system) {
		return getDao(ModelGroupingDao.class).findModelGroupingByMtcModelAndSystem(mtcModel, system);
	}

	/**
	 * This method is used to remove data from ModelGroupGrouping .
	 * @param modelGroupGroupingList
	 */
	public void removeAllModelGroupGrouping(List<ModelGrouping> modelGroupGroupingList) {
		getDao(ModelGroupingDao.class).removeAll(modelGroupGroupingList);

	}

	/**
	 * This method is used to insert data in ModelGroupGrouping .
	 * @param newCombinationOfMtcModels
	 */
	public void updateAllModelGrouping(List<ModelGrouping> newCombinationOfMtcModels) {
		getDao(ModelGroupingDao.class).saveAll(newCombinationOfMtcModels);

	}

	/**
	 * This method is used to update ModelGroup Id.
	 * @param newModelGroupName
	 * @param userId
	 * @param oldModelGroupName
	 */
	public void updateModelGroupId(String newModelGroupName, String newModelGroupSystem, String userId, String oldModelGroupName, String oldModelGroupSystem) {
		getDao(ModelGroupDao.class).updateModelGroupId(newModelGroupName, newModelGroupSystem, userId, oldModelGroupName, oldModelGroupSystem);

	}
	/**
	 * This method is used to update ModelGrouping Id.
	 * @param newModelGroupName
	 * @param userId
	 * @param oldModelGroupName
	 */
	public void updateModelGroupingId(String newModelGroupName, String newModelGroupSystem, String userId, String oldModelGroupName, String oldModelGroupSystem) {
		getDao(ModelGroupingDao.class).updateModelGroupingId(newModelGroupName, newModelGroupSystem, userId, oldModelGroupName, oldModelGroupSystem);

	}

	/**
	 * This method is used to delete modelGroup.
	 * @param ModelGroup
	 */
	public void removeModelGroup(ModelGroup modelGroup) {
		List<ModelGrouping> assignedGroupings = getDao(ModelGroupingDao.class).findAllByModelGroupAndSystem(modelGroup.getModelGroup(), modelGroup.getSystem());
		getDao(ModelGroupingDao.class).removeAll(assignedGroupings);
		getDao(ModelGroupDao.class).remove(modelGroup);
	}

	/**
	 * This method is used to set ModelGroupGrouping to MtcToModelGroupDto.
	 * @param assginedMtcModel
	 */
	private List<MtcToModelGroupDto> setMtcModelToDto(List<ModelGrouping> assginedMtcModel) {
		List<MtcToModelGroupDto> assignedMtcModelList = new ArrayList<MtcToModelGroupDto>();
		if (assginedMtcModel != null) {
			for (ModelGrouping modelGroupGrouping : assginedMtcModel) {
				MtcToModelGroupDto mtcToModelGroupDto = new MtcToModelGroupDto();
				mtcToModelGroupDto.setMtcModel(modelGroupGrouping.getId().getMtcModel());
				assignedMtcModelList.add(mtcToModelGroupDto);
			}
		}
		return assignedMtcModelList;
	}

	/**
	 * This method is used convert QiDto To NonQiDto
	 * @param assignedQiMtcModelDtoList
	 */
	private List<MtcToModelGroupDto> convertQiDtoToNonQiDto(List<QiMtcToEntryModelDto> assignedQiMtcModelDtoList) {
		List<MtcToModelGroupDto> assignedMtcModelDtoList = new ArrayList<MtcToModelGroupDto>();
		MtcToModelGroupDto mtcToModelGroupDto;
		for (QiMtcToEntryModelDto qiMtcToEntryModelDto : assignedQiMtcModelDtoList) {
			mtcToModelGroupDto=new MtcToModelGroupDto();
			mtcToModelGroupDto.setModelCode(qiMtcToEntryModelDto.getModelCode());
			mtcToModelGroupDto.setMtcModel(qiMtcToEntryModelDto.getMtcModel());
			mtcToModelGroupDto.setModelYearCode(qiMtcToEntryModelDto.getModelYearCode());
			mtcToModelGroupDto.setModelYearDescription(qiMtcToEntryModelDto.getModelYearDescription());
			mtcToModelGroupDto.setModelDescription(qiMtcToEntryModelDto.getModelDescription());
			mtcToModelGroupDto.setShortModelDescription(qiMtcToEntryModelDto.getShortModelDescription());
			mtcToModelGroupDto.setClassNo(qiMtcToEntryModelDto.getClassNo());
			assignedMtcModelDtoList.add(mtcToModelGroupDto);
		}
		return assignedMtcModelDtoList;
	}

	public void reset() {
	}
}
