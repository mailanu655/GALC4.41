package com.honda.galc.client.teamleader;

import static com.honda.galc.common.logging.Logger.getLogger;
import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.PartSnExpirationDateChecker;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMain;
import com.honda.galc.client.audio.ClipPlayManager;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.teamleader.model.PartResult;
import com.honda.galc.client.teamleader.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.ui.component.Text;
import com.honda.galc.common.exception.BaseException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.product.AbnormalReasonDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.MbpnDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.dto.qi.QiRepairDefectDto;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.enumtype.AbnormalType;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.enumtype.QiExternalSystem;
import com.honda.galc.entity.enumtype.StrategyType;
import com.honda.galc.entity.product.AbnormalReason;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.property.SubproductPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.QiHeadlessDefectService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.LotControlPartUtil;
import com.honda.galc.util.ProductResultUtil;
import com.honda.galc.util.SubproductUtil;
/**
 * 
 * <h3>ManualLtCtrResultEnterViewManagerBase</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLtCtrResultEnterViewManagerBase description </p>
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
 * @author Paul Chou
 * Aug 18, 2010
 *
 */
public class ManualLtCtrResultEnterViewManagerBase implements IManualLtCtrResultEnterViewManager{
	public static final String REPAIRED = "Repaired";
	private static final String HL_STATUS_COMPELTED = "Completed";
	private static final String REBROADCAST_AT_REPAIR = "REBROADCAST_AT_REPAIR";
	private static final String BATTERY_EXP_TITLE = "Battery Expiration Alarm Override";
	private static final String ABNORMAL_REASON_TITLE = "Abnormal Reason";
	private final String INVALID_MEASUREMENT_SPEC_CONFIG_ON_PART_MSG = "Bad measurement spec configuration for part: %part_name%, please verify it";
	private int torqueIndex = -1;
	private int stringValueIndex = -1;
	protected PartResult partResult;
	protected List<PartResult> resultList = null;
	private String authorizedUser;
	protected PartSpec partSpec;
	protected Text text =  new Text("");
	protected ApplicationContext appContext;
	protected Application application;
	protected ManualLtCtrResultEnterView view;
	protected ManualLotControlRepairPanel parent;
	protected boolean partSnNg;
	protected boolean resetScreen;
	protected List<? extends ProductBuildResult> installedPartList;
	protected InstalledPartDao installedPartDao;
	protected MeasurementDao measurementDao;
	protected PartNameDao partNameDao;
	protected ManualLotControlRepairPropertyBean property;
	private ProductTypeData productTypeData;
	private ProductType productType;
	SubproductUtil subproductUtil;	
	protected BaseProduct product;
	private List<InstalledPart> overrideDuplicateParts;
	private List<AbnormalReason> abnormalReasonList = null;

	protected DataCollectionController controller;
	protected DataCollectionState dcState;
	protected ClientContext _clientContext;
	protected String strategy = null;

	public ManualLtCtrResultEnterViewManagerBase(ApplicationContext appContext,
			ManualLotControlRepairPropertyBean property, Application application) {
		super();
		this.appContext = appContext;
		this.property = property;
		this.application = application;

		initialize();
	}

	public ManualLtCtrResultEnterViewManagerBase(
			ApplicationContext applicationContext,
			ManualLotControlRepairPropertyBean property,
			Application application, ProductType currentProductType) {
		super();
		this.appContext = applicationContext;
		this.property = property;
		this.application = application;
		this.productType = currentProductType;
		initialize();
	}

	private void initialize() {
		ProductTypeData productTypeData = getProductTypeData();
		view = new ManualLtCtrResultEnterView(productTypeData, this, property);
		if (property.isDefaultTorqueValueButtonDisabled())
			view.getDataPanel().setTorqueValueButtonEnabled(false);
	}

	protected void validatePartSn() throws TaskException, Exception{
		validatePartSnLotControl();
		validatePartSnStrategy();
		if(getCurrentLotControlRule().isVerify()) validateSubProduct();
	}

	private void validateSubProduct() throws Exception {
		PartSerialNumber partnumber = new PartSerialNumber(view.getDataPanel().getPartSnField().getText());
		LotControlRule rule = getCurrentLotControlRule();
		subproductUtil = new SubproductUtil(partnumber, getCurrentLotControlRule(),partSpec);

		if (subproductUtil.isPartSubproduct()) {
			BaseProduct subProduct = subproductUtil.findSubproduct();
			if (subProduct == null) {
				throw new TaskException("Subproduct SN can not be found.");
			}

			if (!subproductUtil.isValidSpecCode(rule.getPartName().getSubProductType(), subProduct, view.getProductPanel().getProductSpecField().getText())) {
				throw new TaskException("Spec Code of part does not match expected Spec Code.");
			}

			SubproductPropertyBean subProductProperty = PropertyService.getPropertyBean(SubproductPropertyBean.class, getCurrentLotControlRule().getId().getProcessPointId());
			try {
				String installProcessPoint;
				Map<String, String> processPointMap = subProductProperty.getInstallProcessPointMap();
				if(processPointMap!=null && !processPointMap.isEmpty()) {
					if(!subProductProperty.isUseMainNoFromPartSpec())
						installProcessPoint = processPointMap.get(rule.getPartName().getSubProductType());
					else{
						installProcessPoint = processPointMap.get(getMainNo());
					}
					
					if(installProcessPoint!=null) {
					List<String> failedProductCheckList = subproductUtil.performSubProductChecks(rule.getPartName().getSubProductType(), subProduct, installProcessPoint);
					if(failedProductCheckList.size() > 0) {
						StringBuffer msg = new StringBuffer();
						msg.append(subProduct.getProductId() + " failed the following Product Checks : ");
						for (int i = 0; i < failedProductCheckList.size(); i++) {
							msg.append(failedProductCheckList.get(i));
							if (i != failedProductCheckList.size() - 1) {
								msg.append(", ");
							}
						}
						Logger.getLogger().info(msg.toString());
						throw new Exception (msg.toString());
					}
				  }
				} else {
					getLogger().info(new String[] {"Property not define for INSTALL_PROCESS_POINT_MAP"});
				}
			} catch (Exception e) {
				throw new TaskException(e.getMessage(), e);
			}
		}
	}

	private void validatePartSnStrategy() throws TaskException, Exception{
		try {
			doPartSnStrategyValidation();
		
		}catch(Exception e) {
			handleStrategyException(e);
		}
	}

	private void handleStrategyException(Exception e) throws TaskException, Exception{
		if(isBatteryExpException(e) && property.getBattExpOverrideAuthGroup().length > 0) {
			if(handleBatteryExpiredOverride(e))
				return;
		} 
		
		throw e;

	}

	private boolean handleBatteryExpiredOverride(Exception e) {
		String[] batteryExpOverrideAuthorizationGroups = this.property.getBattExpOverrideAuthGroup();

		final String message = e.getCause().getMessage() + "\nAuthorized associate login required to override.";
		
		if(showDialogAndOveridde(batteryExpOverrideAuthorizationGroups, message, BATTERY_EXP_TITLE, 
				this.view.getDataPanel(), AbnormalType.BATTERY_EXPIRATION.getName())) {
			
			String abnReason = MessageDialog.showInputDialog(this.view.getDataPanel(),ABNORMAL_REASON_TITLE,"Please Enter Abnormal Reason:",255, true, 
					new Font("Arial", Font.PLAIN, 24), new Dimension(500,50));
			StringBuilder sb = new StringBuilder();
			sb.append(getCurrentLotControlRule().getPartName().getPartName()).append(" - ");
			sb.append(view.getDataPanel().getPartSnField().getText()).append(" : ");
			sb.append(null == abnReason ? "" : abnReason);
			AbnormalReason abnormalReason = new AbnormalReason(view.getProductPanel().getProductIdField().getText(),
					AbnormalType.BATTERY_EXPIRATION.getName(),  
					sb.toString(), 
					this.authorizedUser, new Timestamp(System.currentTimeMillis()));
			if(abnormalReasonList == null) {
				abnormalReasonList = new ArrayList<AbnormalReason>();
			}
			abnormalReasonList.add(abnormalReason);
			
			return true;
			
		}
		return false;
	}



	private boolean isBatteryExpException(Exception e) {
		return e.getCause() != null && e.getCause().getMessage().startsWith(PartSnExpirationDateChecker.BATTERY_EXPIRED_ON);
	}

	private void doPartSnStrategyValidation() throws ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		String strategyClassName = null;
		try{
			strategy = getCurrentLotControlRule().getStrategy();
			if(StringUtils.isEmpty(strategy)) return;
			strategyClassName=StrategyType.valueOf(strategy).getCanonicalStrategyClassName();
		}
		catch(Exception e){
			Logger.getLogger().info(e, "strategy is not defined");
		}
		if(!StringUtils.isEmpty(strategy)){
			Class<?> clazz = Class.forName(strategyClassName);
			Method m = null;
			try{
				m = clazz.getMethod("validate",new Class[] { LotControlRule.class, String.class, ProductBuildResult.class });
			}
			catch(Exception e){
				Logger.getLogger().info("validate method is not defined");
			}
			if(m != null){
				Class<?>[] parameterTypes = {ClientContext.class};
				Object[] parameters = {createClientContextLight()};
				Constructor<?> constructor = clazz.getConstructor(parameterTypes);
				Object obj = constructor.newInstance(parameters);
				String productId = view.getProductPanel().getProductIdField().getText();
				InstalledPart installedPart = createInstalledPart();
				m.invoke(obj, getCurrentLotControlRule(), productId, installedPart);
			}
		}
	}

	public void confirmMeasurement() {

		try {
			view.setErrorMessageArea(null);
			double doubleToqueVlue = getDoubleTorqueValue(torqueIndex);
			checkTorqueValue(doubleToqueVlue);
			completeTorqueValueCheck();
		} catch(NumberFormatException ex) {
			view.getDataPanel().getTorqueFieldList().get(torqueIndex).setStatus(false);
			view.setErrorMessageArea("Invalid torque value. Please set a correct torque value.");
		} catch (BaseException te) {
			view.getDataPanel().getTorqueFieldList().get(torqueIndex).setStatus(false);
			view.setErrorMessageArea(te.getMessage());
		} catch(Exception e){
			Logger.getLogger().error(e, "Error to confirm torque value.");
			view.getDataPanel().getTorqueFieldList().get(torqueIndex).setStatus(false);
			view.setErrorMessageArea(e.toString());
		}
	}

	private void completeTorqueValueCheck() {
		view.setErrorMessageArea(null);
		view.getDataPanel().getTorqueFieldList().get(torqueIndex).setStatus(true);

		torqueIndex++;
		if(torqueIndex < getCurrentPartMeasurementCount())
			view.getDataPanel().getTorqueFieldList().get(torqueIndex).setText(new Text(""));
		else {
			torqueIndex = 0;
			if(getPartSpec().getStringMeasurementSpecs().size() ==0)
				view.getDataPanel().enableOperationButtons(true);
			else
				prepareStringValueColletion();
		}
	}

	private void checkTorqueValue(double doubleToqueVlue){
		MeasurementSpec measurementSpec = getCurrentMeasurementSpec(torqueIndex);
		if(measurementSpec.getMaximumLimit() == 0 && measurementSpec.getMinimumLimit() == 0) return; 
		if(doubleToqueVlue > measurementSpec.getMaximumLimit() || doubleToqueVlue < measurementSpec.getMinimumLimit())
			throw new TaskException("Invalid torque value " + doubleToqueVlue + " Max:" + 
					measurementSpec.getMaximumLimit() + " Min:" + measurementSpec.getMinimumLimit());

	}

	private MeasurementSpec getCurrentMeasurementSpec(int index) {
		return getPartSpec().getNumberMeasurementSpecs().get(index);
	}

	private PartSpec getPartSpec() {
		if(partSpec == null && 
				getCurrentLotControlRule().getParts() != null && 
				getCurrentLotControlRule().getParts().size() > 0
				)
			partSpec = getCurrentLotControlRule().getParts().get(0);

		return partSpec;
	}

	private PartSpec getPartSpec(PartResult result) {
		return (result.getLotControlRule().getParts() != null && result.getLotControlRule().getParts().size() > 0) ?
				getCurrentLotControlRule().getParts().get(0) : null;
	}

	private double getDoubleTorqueValue(int index) {
		return new Double(view.getDataPanel().getTorqueFieldList().get(index).getText().trim()).doubleValue();
	}

	public void saveUpdate() {
		try {

			view.setErrorMessageArea(null);

			if (isRequireLogin()) {
				LoginResult result = login(getAuthorizationGroup());
				if (LoginStatus.OK == result.getLoginStatus()) {
					if (!result.isAuthorized()) {
						// display no auth message if login is valid but not authorized to save build result
						JOptionPane.showMessageDialog(null, "You have no access permission to save build result.",
								"Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				} else {
					// return if login is not valid
					return;
				}
			}

			if(property.isSaveConfirmation() && !confirmUpdate()) return;

			installedPartList = getCollectedBuildResult();

			doSaveUpdate();

			if(resultList == null) {
				doSaveUpdateSubproduct();
				doBroadcast();
				view.dispose();
				parent.getController().updateCurrentRow();
			} else {
				doBroadcast();
				view.dispose();
				parent.getController().updateChangedRows();
			}

		} catch (Exception e) {
			view.setErrorMessageArea(e.toString());
			view.getDataPanel().getPartSnField().setStatus(false);
			Logger.getLogger().error(e, "Failed to save data into database:" + e.toString());
		}

	}

	private boolean isRequireLogin() {
		return isUserauthenticationNeededToSave() || getCurrentLotControlRule().getPartName().isTlConfirm();
	}

	private void doBroadcast() {
		String processPointId = getCurrentLotControlRule().getId().getProcessPointId();
		String broadcastDestinations = PropertyService.getProperty(processPointId, REBROADCAST_AT_REPAIR, "");

		if (broadcastDestinations != null && broadcastDestinations != "") {
			try {
				Logger.getLogger().info("Beginning broadcast from repair");

				List<String> broadcastDestinationList = java.util.Arrays.asList(broadcastDestinations.split(","));
				DataContainer dataContainer = new DefaultDataContainer();

				dataContainer.put(DataContainerTag.PRODUCT_ID, product.getProductId());
				dataContainer.put(DataContainerTag.PRODUCT_TYPE, product.getProductType().toString());
				dataContainer.put(DataContainerTag.PRODUCT, product);
				dataContainer.put(DataContainerTag.PRODUCT_SPEC_CODE, product.getProductSpecCode());

				for(BroadcastDestination destination: getService(BroadcastDestinationDao.class).findAllByProcessPointId(processPointId, true)) {
					if (broadcastDestinationList.contains(destination.getDestinationId())) {
						Logger.getLogger().info(String.format("Broadcasting %s at %s", destination.getDestinationId(), processPointId));

						getService(BroadcastService.class).broadcast(destination, processPointId, dataContainer);
					}
				}

			} catch (Exception e) {
				view.setErrorMessageArea(e.toString());

				Logger.getLogger().error(e, "Failed to broadcast from repair: " + e.toString());			
			} finally {
				Logger.getLogger().info("Completed broadcast from repair");
			}
		}
	}

	private void doSaveUpdateSubproduct() throws Exception {

		String subProductType = getCurrentLotControlRule().getPartName().getSubProductType();
		if (null != subProductType && !subProductType.equalsIgnoreCase("")){
			SubproductPropertyBean subProductProperty = PropertyService.getPropertyBean(SubproductPropertyBean.class, getCurrentLotControlRule().getId().getProcessPointId());
			String installProcessPoint="";
			Map<String, String> processPointMap = subProductProperty.getInstallProcessPointMap();
			if (processPointMap != null && !processPointMap.isEmpty()) {
				if (!subProductProperty.isUseMainNoFromPartSpec())
					installProcessPoint = processPointMap.get(getCurrentLotControlRule().getPartName().getSubProductType());
				else {
					installProcessPoint = processPointMap.get(getMainNo());
				}
				subproductUtil.performSubproductTracking(subProductType, subproductUtil.findSubproduct(), installProcessPoint, "");
			}
	  }
	}

	@SuppressWarnings("unchecked")
	protected void doSaveUpdate() {
		//Generic method - Default for Engine, Frame and Knuckles
		ProductResultUtil.saveAll(appContext.getApplicationId(), (List<InstalledPart>)installedPartList);
		
		if (this.overrideDuplicateParts != null) {
			for (InstalledPart part : this.overrideDuplicateParts) {
				// create an AbnormalReason for the original product id 
				final String originalProductId = part.getId().getProductId();
				AbnormalReason abnormalReason = new AbnormalReason(originalProductId, "Duplicate Part", part.getId().getPartName() + " - " + part.getPartSerialNumber(), this.authorizedUser, part.getActualTimestamp());
				ServiceFactory.getDao(AbnormalReasonDao.class).insert(abnormalReason);
				// remove the original GAL198TBX records
				List<Measurement> measurements = getMeasurementDao().findAll(part.getProductId(), part.getPartName());
				if (measurements != null) {
					for (Measurement measurement : measurements) {
						getMeasurementDao().remove(measurement);
					}
				}
				// remove the original GAL185TBX records
				getInstalledPartDao().remove(part);
			}
			this.overrideDuplicateParts.clear();
		}
		
		if(abnormalReasonList != null && abnormalReasonList.size() > 0) {
			ServiceFactory.getDao(AbnormalReasonDao.class).saveAll(abnormalReasonList);
			abnormalReasonList.clear();
			abnormalReasonList=null;
		}
		

		//check QI_DEFECT_FLAG from lot control (GAL246TBX)
		//if it is 1, repair NAQ defect
		if (partResult.getLotControlRule().isQicsDefect() && installedPartList.size() > 0) {
			List<String> productIdList = new ArrayList<String>();
			List<String> partNameList = new ArrayList<String>();
			for (InstalledPart installedPart : (List<InstalledPart>)installedPartList) {
				productIdList.add(installedPart.getProductId());
				partNameList.add(installedPart.getPartName());
			}
			
			List<Long> defectRefIds= ServiceFactory.getDao(InstalledPartDao.class).findDefectRefIds(productIdList, partNameList);
			
			List<QiRepairDefectDto> existingRepaired = new ArrayList<QiRepairDefectDto>();
			for (int i = 0; i < defectRefIds.size(); i++) {
				QiRepairDefectDto qiRepairDefectDto = new QiRepairDefectDto();
				qiRepairDefectDto.setExternalSystemKey(defectRefIds.get(i));
				qiRepairDefectDto.setAssociateId(getUser());
				qiRepairDefectDto.setExternalSystemName(QiExternalSystem.LOT_CONTROL.name());
				qiRepairDefectDto.setCurrentDefectStatus((short)DefectStatus.FIXED.getId());
				qiRepairDefectDto.setProcessPointId(this.application.getApplicationId());
				existingRepaired.add(qiRepairDefectDto);
			}
			
			if(!existingRepaired.isEmpty())  {
				try {
					ServiceFactory.getService(QiHeadlessDefectService.class).repairDefects(existingRepaired, true);
				} catch (Exception ex) {
					Logger.getLogger().error(ex, "Exception invoking QiHeadlessDefectServiceImpl");
				}		
			}
		}
		
		Logger.getLogger().info("Saved data into database by user:" + getUser() + 
				System.getProperty("line.separator") + installedPartList.get(0));
	}

	protected PartNameDao getPartNameDao() {
		if (partNameDao == null)
			partNameDao = ServiceFactory.getDao(PartNameDao.class);

		return partNameDao;
	}

	protected MeasurementDao getMeasurementDao() {
		if (measurementDao == null)
			measurementDao = ServiceFactory.getDao(MeasurementDao.class);

		return measurementDao;
	}

	protected InstalledPartDao getInstalledPartDao() {
		if(installedPartDao == null)
			installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);

		return installedPartDao;
	}

	protected String getUser() {
		if(!isRequireLogin()) return appContext.getUserId();
		else
			return authorizedUser == null ? appContext.getUserId() : authorizedUser;
	}

	protected boolean isUserauthenticationNeededToSave() {
		try {

			String clientStr = property.getClientsNeedAuthenticateUserToSave();
			if (StringUtils.isEmpty(clientStr)) 
				return false;

			String[] clients = clientStr.split(",");
			for (int i = 0; i < clients.length; i++) {
				if (appContext.getTerminal().getHostName().equals(clients[i].trim()))
					return true;
			}

		} catch (Exception e) {
			return false;
		}
		return false;
	}


	public LoginResult login(String[] authorizationGroups) {
		authorizedUser = null;
		LoginStatus loginStatus = LoginDialog.login();
		boolean authorized = false;

		if (LoginStatus.OK != loginStatus) {
			return new LoginResult(loginStatus, authorized);
		}
		if (authorizationGroups != null) {
			for (String authorizationGroup : authorizationGroups) {
				if (ClientMain.getInstance().getAccessControlManager().isAuthorized(authorizationGroup)) {
					authorized = true;
					break;
				}
			}
		}
		if (authorized) {
			authorizedUser = ClientMain.getInstance().getAccessControlManager().getUserName();
			Logger.getLogger().info("User:" + authorizedUser + " logged in.");
		}
		return new LoginResult(loginStatus, authorized);

	}

	private String[] getAuthorizationGroup() {
		return (getCurrentLotControlRule().getPartName().isTlConfirm()) ? 
				property.getTlConfirmAuthorizationGroup() : property.getAuthorizationGroup();
	}

	protected List<? extends ProductBuildResult> getCollectedBuildResult() {
		List<InstalledPart> list = new ArrayList<InstalledPart>();
		if(resultList == null) {
			InstalledPart installedPart = createInstalledPart();
			
			//restore defect_ref_id for part
			ProductBuildResult pbr = partResult.getBuildResult();
			if (pbr != null) {
				installedPart.setDefectRefId(((InstalledPart)pbr).getDefectRefId()); 
			}

				getInputPartAndTorqueData(installedPart);

			partResult.setBuildResult(installedPart);
			list.add(installedPart);
		} else {
			for(PartResult result : resultList) {
				InstalledPart installedPart = createInstalledPart(result);
				result.setBuildResult(installedPart);
				list.add(installedPart);
			}
		}
		return list;
	}

	protected void getInputPartAndTorqueData(InstalledPart installedPart) {
		if (view.getDataPanel().getPartSnField().isVisible())
			installedPart.setPartSerialNumber(view.getDataPanel().getPartSnField().getText());

		if (getCurrentPartMeasurementCount() > 0)
			collectedMeasurementData(installedPart);

		if(getStrValueMeasurementSpecs() != null && getStrValueMeasurementSpecs().size() > 0)
			collectStringValueMeasurementData(installedPart);
	}

	protected InstalledPart createInstalledPart() {
		InstalledPart installedPart = new InstalledPart();
		InstalledPartId id = new InstalledPartId();
		id.setProductId(view.getProductPanel().getProductIdField().getText());
		id.setPartName(getCurrentLotControlRule().getPartName().getPartName());
		installedPart.setId(id);
		installedPart.setPartId(getPartSpec() == null ? "" : getPartSpec().getId().getPartId());
		installedPart.setInstalledPartReason(REPAIRED);
		installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
		installedPart.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		installedPart.setAssociateNo(getUser());
		installedPart.setPartSerialNumber(view.getDataPanel().getPartSnField().getText());
		installedPart.setProductType(productType.name());
		return installedPart;
	}

	protected InstalledPart createInstalledPart(PartResult result) {
		InstalledPart installedPart = new InstalledPart();
		InstalledPartId id = new InstalledPartId();
		id.setProductId(view.getProductPanel().getProductIdField().getText());
		id.setPartName(result.getPartName());
		installedPart.setId(id);
		installedPart.setPartId(getPartSpec(result) == null ? "" : getPartSpec(result).getId().getPartId());
		installedPart.setInstalledPartReason(REPAIRED);
		installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
		installedPart.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		installedPart.setAssociateNo(getUser());
		installedPart.setPartSerialNumber(view.getDataPanel().getPartSnField().getText());
		installedPart.setProductType(productType.name());
		return installedPart;
	}

	protected LotControlRule getCurrentLotControlRule() {
		return partResult.getLotControlRule();
	}


	private void collectedMeasurementData(InstalledPart installedPart) {
		
		List<Measurement> existingMeasurementList = null;
		if (partResult.getBuildResult() != null ) {
			existingMeasurementList = ((InstalledPart)partResult.getBuildResult()).getMeasurements();
		}
		
		for(int i = 0; i < getCurrentPartMeasurementCount(); i++){
			Measurement measurement = new Measurement();
			MeasurementId id = new MeasurementId();
			id.setProductId(installedPart.getId().getProductId());
			id.setPartName(installedPart.getId().getPartName());
			
			int sequenceNumber = getNumberMeasurementSpecs().get(i).getId().getMeasurementSeqNum();
			id.setMeasurementSequenceNumber(sequenceNumber);
			measurement.setId(id);

			String torqueValue = view.getDataPanel().getTorqueFieldList().get(i).getText().trim();
			
			if(torqueValue!=null && !torqueValue.isEmpty())
			measurement.setMeasurementValue(getDoubleTorqueValue(i));
			
			measurement.setMeasurementStatus(MeasurementStatus.OK);

			//restore defect_ref_id for measurement
			if (existingMeasurementList != null &&existingMeasurementList.size() >0) {
				for (int j = 0; j < existingMeasurementList.size(); j++) {
					Measurement existingMeasuremnt = existingMeasurementList.get(j);
					if (sequenceNumber == existingMeasuremnt.getId().getMeasurementSequenceNumber()) {
						measurement.setDefectRefId(existingMeasuremnt.getDefectRefId());
						break;
					}
				}
			}
			
			installedPart.getMeasurements().add(measurement);
		}

	}

	private void collectStringValueMeasurementData(InstalledPart installedPart) {

		if(getStrValueMeasurementSpecs() == null || getStrValueMeasurementSpecs().size() == 0) return;

		for(int i = 0; i < getStrValueMeasurementSpecs().size(); i++){
			Measurement measurement = new Measurement();
			MeasurementId id = new MeasurementId();
			id.setProductId(installedPart.getId().getProductId());
			id.setPartName(installedPart.getId().getPartName());
			id.setMeasurementSequenceNumber(getStrValueMeasurementSpecs().get(i).getId().getMeasurementSeqNum());
			measurement.setId(id);

			measurement.setMeasurementStringValue(view.getDataPanel().getCurrentStringValueField(i).getText());
			measurement.setMeasurementName(getStrValueMeasurementSpecs().get(i).getMeasurementName());
			measurement.setMeasurementStatus(MeasurementStatus.OK);

			installedPart.getMeasurements().add(measurement.getId().getMeasurementSequenceNumber()-1,measurement);
		}



	}

	protected boolean confirmUpdate() {
		if(property.getDefaultYesButtonToSave())
			return MessageDialog.confirm(view, "Do You Really Want To Update Database ?", true);
		else
			return MessageDialog.confirm(view, "Do You Really Want To Update Database ?");			
	}

	public void showPartCommentConfirmDialog() {}

	public void confirmPartSn() {
		try {
			view.setErrorMessageArea(null);
			text = new Text(view.getDataPanel().getPartSnField().getText());
			validatePartSn();
			view.getDataPanel().getPartSnField().setStatus(true);
			partSnNg = false;

			if(getCurrentPartMeasurementCount() > 0){
				
				if(!isValidMeasurementSpecConfig()){
					showInvalidMeasurementConfigError();
					return;
				}
				
				prepareForTorqueCollection();
			}else{
				view.getDataPanel().enableOperationButtons(true);
			}			
		} catch (BaseException te) {
			Logger.getLogger().error(te, te.getMessage(), "Failed to confirm part serial number.");
			view.getDataPanel().getPartSnField().setStatus(false);
			view.setErrorMessageArea(te.getMessage());
		} catch(Exception e){
			Logger.getLogger().error(e, "Error to confirm part serial number.");
			view.getDataPanel().getPartSnField().setStatus(false);
			view.setErrorMessageArea(e.getCause().getMessage());
		}
	}

	public void confirmStringValue() {
		try {
			view.setErrorMessageArea(null);
			validateStringValue();
			view.getDataPanel().getStringvalueFieldList().get(stringValueIndex).setStatus(true);

			stringValueIndex++;
			if(stringValueIndex < getStringValueCount())
				view.getDataPanel().getStringvalueFieldList().get(stringValueIndex).setText(new Text(""));
			else {
				stringValueIndex = 0;
				view.getDataPanel().enableOperationButtons(true);
			}

		} catch (BaseException te) {
			Logger.getLogger().info(te.getMessage(), "Failed to confirm string value.");
			view.getDataPanel().getStringvalueFieldList().get(stringValueIndex).setStatus(false);
			view.setErrorMessageArea(te.getMessage());
		} catch(Exception e){
			Logger.getLogger().error(e, "Failed to confirm string value.");
			view.getDataPanel().getStringvalueFieldList().get(stringValueIndex).setStatus(false);
			view.setErrorMessageArea(e.toString());
		}
	}	


	private int getStringValueCount() {
		int retVal = 0;
		if(getPartSpec() != null && getPartSpec().getMeasurementSpecs() != null)  {
			retVal = getPartSpec().getStringMeasurementSpecs().size();
		}
		return retVal;
	}

	private void validateStringValue() {
		view.setErrorMessageArea(null);
		if(view.getDataPanel().getStringvalueFieldList().get(stringValueIndex).getText() == null)
			throw new TaskException("Invalid input string.");
	}

	private void prepareForTorqueCollection() {
		torqueIndex = 0;
		view.getDataPanel().enableTorqueFields(getCurrentPartMeasurementCount());
		text = new Text("");
	}

	private void prepareStringValueColletion() {
		stringValueIndex = 0;
		view.getDataPanel().enableStringValueFields(getStrValueMeasurementSpecs());

	}

	protected List<MeasurementSpec> getStrValueMeasurementSpecs() {
		return partResult.getStrValueMeasurements();
	}

	protected List<MeasurementSpec> getNumberMeasurementSpecs() {
		return partResult.getNumberMeasurements();
	}

	private int getCurrentPartMeasurementCount() {
		return partResult.getMeasurementCount();
	}

	protected void validatePartSnLotControl() {
		String partSn = view.getDataPanel().getPartSnField().getText();
		if(getCurrentLotControlRule().isVerify()){
			partSpec = checkPartSnMask(partSn);
			if(partSpec == null) {
				String msg = "failed part serial number verification for :" + partSn + " part Masks:" + getPartSnMaskList();
				Logger.getLogger().warn(msg);
				throw new TaskException("Verification error " + getPartSnMaskList());
			} else {
				String parsePartSn = CommonPartUtility.parsePartSerialNumber(partSpec,partSn);
				if (!partSn.equals(parsePartSn)) {
					view.getDataPanel().getPartSnField().setText(parsePartSn);
					partSn = parsePartSn;
				}
			}
		} else {
			if(getPartSepcs().size() > 0)
				partSpec = getPartSepcs().get(0);

		}

		if(getCurrentLotControlRule().isUnique())
			checkDuplicatePart(partSn);

	}

	private List<String> getPartSnMaskList() {
		List<String> masks = new ArrayList<String>();
		for(PartSpec spec : getPartSepcs())
			masks.add(CommonPartUtility.parsePartMask(spec.getPartSerialNumberMask()));

		return masks;
	}

	protected List<PartSpec> getPartSepcs() {
		if(getCurrentLotControlRule() == null || getCurrentLotControlRule().getPartName() == null)
		{
			throw new TaskException("Exception: missing lot control rule or part name");
		} else if(getCurrentLotControlRule().getParts() == null){
			throw new TaskException("Exception: missing part spec.");
		}

		return getCurrentLotControlRule().getParts();
	}

	private PartSpec checkPartSnMask(String partSn) {
		SystemPropertyBean sysBean = PropertyService.getPropertyBean(SystemPropertyBean.class,getCurrentLotControlRule().getId().getProcessPointId());
		return CommonPartUtility.verify(partSn, getPartSepcs(),PropertyService.getPartMaskWildcardFormat(),getCurrentLotControlRule().isDateScan(),
				sysBean.getExpirationDays(), product, sysBean.isUseParsedDataCheckPartMask());	
	}

	protected void checkDuplicatePart(String partSn) {
		List<String> partNames = new ArrayList<String>();
		if(partSpec != null)
			partNames = LotControlPartUtil.getPartNamesOfSamePartNumber(partSpec);
		else
			partNames.add(getCurrentLotControlRule().getPartName().getPartName());
		List<InstalledPart> duplicateParts = getInstalledPartDao().findAllByPartNameAndSerialNumber(partNames, partSn);

		if (duplicateParts != null && !duplicateParts.isEmpty()) {
			final String errorMessage;
			{
				java.util.Iterator<InstalledPart> duplicatePartIterator = duplicateParts.iterator();
				StringBuilder errorMessageBuilder = new StringBuilder("Duplicate part # with product");
				errorMessageBuilder.append(duplicateParts.size() > 1 ? "s: " : ": ");
				while (duplicatePartIterator.hasNext()) {
					InstalledPart duplicatePart = duplicatePartIterator.next();
					errorMessageBuilder.append(duplicatePart.getId().getProductId());
					if (duplicatePartIterator.hasNext()) {
						errorMessageBuilder.append(", ");
					}
				}
				errorMessage = errorMessageBuilder.toString();
			}
			if (!handleDuplicateOverride(duplicateParts, errorMessage)) {
				if (this.overrideDuplicateParts != null) {
					this.overrideDuplicateParts.clear();
				}
				throw new TaskException(errorMessage);
			}
		}
	}

	private boolean handleDuplicateOverride(final List<InstalledPart> duplicateParts, final String errorMessage) {
		String[] duplicatePartOverrideAuthorizationGroups = this.property.getDupPartOverrideAuthGroup();
		if (duplicatePartOverrideAuthorizationGroups.length > 0) {
			final String message = errorMessage + "\nAuthorized associate login required to override.";
			final String title = "Duplicate Part Alarm Override";
			// sound the alarm
			playNgSound();
			// prompt the teamleader to login
			if (JOptionPane.YES_OPTION == MessageDialog.showColoredConfirmDialog(this.view.getDataPanel(), message + "\nAre you sure you want to override the duplicate parts?", title, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, Color.YELLOW)) {
				LoginResult loginResult;
				do {
					// show the login dialog
					loginResult = login(duplicatePartOverrideAuthorizationGroups);
					if (LoginStatus.OK == loginResult.getLoginStatus()) {
						if (!loginResult.isAuthorized()) {
							// alarm if the valid login is not authorized
							playNgSound();
							MessageDialog.showColoredMessageDialog(this.view.getDataPanel(), message + "\nYou have no access permission to override the duplicate part alarm.", title, JOptionPane.ERROR_MESSAGE, Color.RED);
						}
					} else if (LoginStatus.CANCEL != loginResult.getLoginStatus()) {
						// alarm if the login is not valid
						playNgSound();
						MessageDialog.showColoredMessageDialog(this.view.getDataPanel(), message + "\nInvalid login: " + loginResult.getLoginStatus().name(), title, JOptionPane.ERROR_MESSAGE, Color.RED);
					}
				} while (LoginStatus.CANCEL != loginResult.getLoginStatus() &&
						(LoginStatus.OK == loginResult.getLoginStatus() && !loginResult.isAuthorized()));

				if (LoginStatus.OK == loginResult.getLoginStatus() && loginResult.isAuthorized()) {
					playOkSound();
					this.overrideDuplicateParts = duplicateParts;
					return true;
				}
			}
		}
		return false;
	}

	private void loadPartResult(ManualLotControlRepairPanel mview) {


		view.getProductPanel().getProductIdField().setText(new Text(mview.getProductIdField().getText(), true));
		view.getProductPanel().getProductSpecField().setText(mview.getProductSpecField().getText());
		view.getProductPanel().getSeqField().setText(mview.getSeqField().getText());
		view.getProductPanel().getSeqLabel().setVisible(true);
		view.getProductPanel().getSeqField().setVisible(true);
		loadInstalledPartResult();
	}

	private void loadInstalledPartResult() {
		if(isHeadlessQickFixStatusOnly()){
			loadHeadLessResult();

		} else {
			loadHeadedResult();
		}
	}

	private boolean isHeadlessQickFixStatusOnly() {
		return partResult.isHeadLess() && (partResult.isQuickFix() || partResult.isStatusOnly());
	}

	protected void loadHeadedResult() {

		view.getDataPanel().getPartNameTextArea().setText(getPartNameLabel());// +"   "+partmask);

		if(getCurrentLotControlRule().getSerialNumberScanType() != PartSerialNumberScanType.NONE){
			view.getDataPanel().setVisibleInstalledPart(true);
			if(partResult.getInstalledPart() != null && !partResult.getInstalledPart().getInstalledPartStatus().equals(InstalledPartStatus.REMOVED)){
				String partmask =  getInstaledPartSpec() != null? CommonPartUtility.parsePartMask(getInstaledPartSpec().getPartSerialNumberMask()):" ";
				view.getDataPanel().getPartNameTextArea().setText(getPartNameLabel() +" "+partmask);
				text = new Text(partResult.getInstalledPart().getPartSerialNumber());
				view.getDataPanel().getPartSnField().setText(text);

				if(getCurrentLotControlRule().isVerify()){
					if(!checkInstalledPartSerialNumber()){
						text = new Text(false);
						partSnNg = true;

						if(resetScreen) view.getDataPanel().getPartSnField().setStatus(false);
						return;
					} 
				} 
				if(!StringUtils.isBlank(partResult.getInstalledPart().getPartSerialNumber()))  {
					view.getDataPanel().getPartSnField().setStatus(true);
					view.getDataPanel().getPartSnField().setEnabled(false);
				}

			} else {
				LotControlRule lotControlRule =getCurrentLotControlRule();
				String partmask = CommonPartUtility.parsePartMask(lotControlRule.getPartMasks());
				view.getDataPanel().getPartNameTextArea().setText(getPartNameLabel() +" "+partmask);
				view.getDataPanel().enableInstalledPart();
				partSnNg = true;
				return;
			}

		} 		

		if(getStringValueCount() > 0){
			stringValueIndex = 0;
			view.getDataPanel().enableStringValueFields(partResult.getStrValueMeasurements());
			if(partResult.getInstalledPart() != null)
				loadStringValueFields();
		}
		
		if(isValidMeasurementSpecConfig()){
			torqueIndex = 0;
			if(getCurrentPartMeasurementCount() > 0){
				view.getDataPanel().enableTorqueFields(getCurrentPartMeasurementCount());
				if(partResult.getInstalledPart() != null){
					loadTorqueFields();
				}
			}
		}else{
			showInvalidMeasurementConfigError();
		}	

	}

	private void loadStringValueFields() {
		for(int i = 0; i < partResult.getInstalledPart().getMeasurements().size(); i++){

			Measurement measurement = partResult.getInstalledPart().getMeasurements().get(i);
			if(StringUtils.isEmpty(measurement.getMeasurementName())) continue;
			view.getDataPanel().getStringvalueFieldList().get(i).setText(measurement.getMeasurementStringValue());

			if(!view.getDataPanel().getMeasurementNameList().get(i).getText().equals(measurement.getMeasurementName()))
				Logger.getLogger().error("Load string value:" + i + " MeasurementName:", 
						view.getDataPanel().getMeasurementNameList().get(i).getText(), " MeasurementName from database:"
						,measurement.getMeasurementName() + measurement.getId().getMeasurementSequenceNumber(), " not equal.");

			if(!measurement.isStatus()){
				text = new Text(measurement.getMeasurementStringValue(), measurement.isStatus());
				stringValueIndex = i;
				return;
			}
		}

	}

	protected boolean checkInstalledPartSerialNumber() {
		PartSpec partSpec = getInstaledPartSpec();

		if(partSpec == null) return false;			

		if(CommonPartUtility.verification(partResult.getInstalledPart().getPartSerialNumber(), 
				partSpec.getPartSerialNumberMask(), PropertyService.getPartMaskWildcardFormat(), product)){
			view.getDataPanel().getPartSnField().setStatus(true);
			partSnNg = false;
			return true;
		}else {
			view.getDataPanel().getPartSnField().setStatus(false);
			text = new Text(false);
			partSnNg = true;
			return false;
		}


	}

	private void loadTorqueFields() {
		for(int i = 0; i < partResult.getInstalledPart().getMeasurements().size(); i++){

			Measurement measurement = partResult.getInstalledPart().getMeasurements().get(i);
			if(!StringUtils.isEmpty(measurement.getMeasurementName())) continue;
			DecimalFormat format = new DecimalFormat();
			format.setMaximumFractionDigits(2);
			format.setMinimumFractionDigits(2);
			String measurementValue = format.format(measurement.getMeasurementValue());
			view.getDataPanel().getTorqueFieldList().get(i).setText(measurementValue);
			view.getDataPanel().getTorqueFieldList().get(i).setStatus(measurement.isStatus());

			if(!measurement.isStatus()){
				text = new Text(measurementValue, measurement.isStatus());
				torqueIndex = i;
				return;
			}
		}
	}

	private PartSpec getInstaledPartSpec() {
		InstalledPart installedPart = partResult.getInstalledPart();
		for(PartSpec spec : getPartSepcs()){
			if(spec.getId().getPartName().equals(installedPart.getId().getPartName()) &&
					spec.getId().getPartId().equals(installedPart.getPartId()))
				return spec;
		}

		Logger.getLogger().warn("Failed to find PartSpec for installed part part:" + 
				installedPart.getId().getPartName() + " partId:" + installedPart.getPartId());

		return null;
	}

	protected void loadHeadLessResult() {
		if(resultList != null && resultList.size() > 0) {
			StringBuilder builder = new StringBuilder();
			builder.append(resultList.get(0).getPartName());
			for(int i = 1; i < resultList.size(); i++) {
				builder.append(System.getProperty("line.separator"));
				builder.append(resultList.get(i).getPartName());
			}
			view.getDataPanel().getPartNameTextArea().setText(builder.toString());
			view.getDataPanel().getPartNameTextArea().setCaretPosition(0);
		} else {
			view.getDataPanel().getPartNameTextArea().setText(getPartNameLabel());
		}
		view.getDataPanel().getJTextFieldHLCompleted().setText(text);

		view.getDataPanel().enableHeadLess(partResult.isStatusOnly());
	}

	protected String getPartNameLabel() {
		PartName partName = getCurrentLotControlRule().getPartName();
		return partName != null ? partName.getWindowLabel() : getCurrentLotControlRule().getPartNameString();
	}

	protected void showPartComment() {
		view.showPartComment( partResult.getLotControlRule().getParts().get(0).getComment());

	}

	protected boolean hasComment() {
		if(partResult.getLotControlRule() == null 
				|| partResult.getLotControlRule().getParts() == null 
				|| partResult.getLotControlRule().getParts().size() ==0)
			return false;

		return !StringUtils.isEmpty(partResult.getLotControlRule().getParts().get(0).getComment());
	}

	public void locateFocus() {
		if (partSnNg) {
			((LengthFieldBean)view.getDataPanel().getPartSnField()).setText(text);
		} else if(torqueIndex > 0) {
			((LengthFieldBean)view.getCurrentTorque(torqueIndex)).setText(text);
		} else if(stringValueIndex > 0)
			((LengthFieldBean)view.getCurrentStringValueField(stringValueIndex)).setText(text);
		else if(isHeadlessQickFixStatusOnly())
			view.getDataPanel().getButtonHlCompleted().requestFocus();

	}

	public void addDefaultTorqueValues() {
		try {
			view.setErrorMessageArea(null);
			
			for (int i = 0; i < getCurrentPartMeasurementCount(); i++) {
				String torqueValueStr = new DecimalFormat("0.00").format(getDefaultTorqueValue(i)).toString();
				Text text = new Text(torqueValueStr, true);
				view.getDataPanel().getTorqueFieldList().get(i).setText(text);
			}

			torqueIndex = 0;
			if(getPartSpec().getStringMeasurementSpecs().size() ==0)
				view.getDataPanel().enableOperationButtons(true);
			else
				prepareStringValueColletion();
	
		} catch (Exception e) {
			view.setErrorMessageArea(e.getMessage());
			Logger.getLogger().error(e, "Error trying to set default torque values");
		}
	}

	private Double getDefaultTorqueValue(int i){
		MeasurementSpec currentMeasurementSpec = getCurrentMeasurementSpec(i);
		return (currentMeasurementSpec.getMaximumLimit() + currentMeasurementSpec.getMinimumLimit())/2;
	}

	public void resetScreen() {
		resetScreen = true;

		view.setErrorMessageArea(null);
		view.getDataPanel().resetScreen();
		this.text = new Text("");
		if (this.overrideDuplicateParts != null) {
			this.overrideDuplicateParts.clear();
		}

		loadInstalledPartResult();

	}

	protected int getInstalledPartStatus()
	{
		int iStatus = 0;
		iStatus = view.getDataPanel().getJTextFieldHLCompleted().getText().equalsIgnoreCase(HL_STATUS_COMPELTED) ? 1 : 0;
		return iStatus; 
	}

	/**
	 * Save result for Head Less
	 *
	 */
	public void setHlStatus() {
		//error Message Area Clear
		view.setErrorMessageArea(null);
		view.getDataPanel().getJTextFieldHLCompleted().setText(new Text(HL_STATUS_COMPELTED, true));
		view.getDataPanel().enableOperationButtons(true);

	}

	public void subScreenClose(ActionEvent e) {
		view.dispose();
		parent.setVisible(true);

	}

	public void subScreenOpen(final ManualLotControlRepairPanel repairView, List<PartResult> resultList) {
		if(resultList == null || resultList.size() == 0) {
			return;
		}
		this.partResult = resultList.get(0);
		this.parent = repairView;
		this.resultList = resultList.size() > 1 ? resultList : null;
		this.text = new Text("");
		if (this.overrideDuplicateParts != null) {
			this.overrideDuplicateParts.clear();
		}

		product = ProductTypeUtil.getProductDao(getProductType()).findBySn(parent.getProductIdField().getText());

		init();
		loadPartResult(repairView);		
	}

	private void init() {
		// error Message Area Clear
		view.setErrorMessageArea(null);
		partSpec = null;
		resetScreen = false;
		partSnNg = false;
		view.getDataPanel().resetScreen();
		view.setVisible(true);
		installedPartList = null;

	}

	private ProductTypeData getProductTypeData() {
		if(productTypeData == null){
			for(ProductTypeData data : appContext.getProductTypeDataList())
				if(data.getProductTypeName().equals(property.getProductType())){
					productTypeData = data;
					break;
				}
		}
		return productTypeData;
	}

	public ProductType getProductType() {
		if (productType == null) {
			productType = ProductTypeCatalog.getProductType(property.getProductType());
		}
		return productType;
	}	

	private String getMainNo(){
		return MbpnDef.MAIN_NO.getValue(partSpec.getPartNumber().trim());
	}

	protected ClientContext createClientContextLight() {
		ClientContext ctx = new ClientContext();
		ctx.setAppContext(appContext);
		ctx.setProperty(PropertyService.getPropertyBean(TerminalPropertyBean.class, appContext.getApplicationId()));
		return ctx;
	}

	private void playOkSound() {
		try {
			ClipPlayManager.playOkSound();
		} catch (Exception e) {
			Logger.getLogger().error(e);
		}
	}

	private void playNgSound() {
		try {
			ClipPlayManager.playNgSound();
		} catch (Exception e) {
			Logger.getLogger().error(e);
		}
	}

	private class LoginResult {
		private final LoginStatus loginStatus;
		private final boolean authorized;
		public LoginResult(final LoginStatus loginStatus, final boolean authorized) {
			this.loginStatus = loginStatus;
			this.authorized = authorized;
		}
		public LoginStatus getLoginStatus() {
			return this.loginStatus;
		}
		public boolean isAuthorized() {
			return this.authorized;
		}
	}
	
	protected boolean isValidMeasurementSpecConfig(){
		return (getCurrentPartMeasurementCount() == getPartSpec().getNumberMeasurementSpecs().size());
	}
	
	protected void showInvalidMeasurementConfigError(){
		view.setErrorMessageArea(INVALID_MEASUREMENT_SPEC_CONFIG_ON_PART_MSG.replace("%part_name%", partResult.getPartName()));
		Logger.getLogger().error("Error on measurement spec config for part:" + partResult.getPartName());
	}
	
	public boolean showDialogAndOveridde(String[] overrideAuthorizationGroups, final String message,
			final String title, Component comp, String msg) {
		playNgSound();
		if (JOptionPane.YES_OPTION == MessageDialog.showColoredConfirmDialog(comp, message + "\nAre you sure you want to override the " + msg + " ?", title, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, Color.YELLOW)) {
			LoginResult loginResult;
			do {
				loginResult = login(overrideAuthorizationGroups);
				if (LoginStatus.OK == loginResult.getLoginStatus()) {
					if (!loginResult.isAuthorized()) {
						playNgSound();
						MessageDialog.showColoredMessageDialog(comp, message + "\nYou have no access permission to override the " + msg +".", title, JOptionPane.ERROR_MESSAGE, Color.RED);
					}
				} else if (LoginStatus.CANCEL != loginResult.getLoginStatus()) {
					playNgSound();
					MessageDialog.showColoredMessageDialog(comp, message + "\nInvalid login: " + loginResult.getLoginStatus().name(), title, JOptionPane.ERROR_MESSAGE, Color.RED);
				}
			} while (LoginStatus.CANCEL != loginResult.getLoginStatus() &&
					(LoginStatus.OK == loginResult.getLoginStatus() && !loginResult.isAuthorized()));

			if (LoginStatus.OK == loginResult.getLoginStatus() && loginResult.isAuthorized()) {
				playOkSound();
			
				return true;
			}
		}
		return false;
	}
}
