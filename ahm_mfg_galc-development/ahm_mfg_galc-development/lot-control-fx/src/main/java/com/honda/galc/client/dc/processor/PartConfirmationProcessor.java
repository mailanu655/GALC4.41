package com.honda.galc.client.dc.processor;

import java.util.List;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.view.PartConfirmationView;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.ProductBuildResultDao;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3>PartConfirmationProcessor</h3>
 * <h3>The class is a operation processor to show measurement overall status. </h3>
 * <h4>  </h4>
 * <p> The operation view for PartConfirmationProcessor must be {@link PartConfirmationView}. </p>
 * <p> the overall status is OK only when <br/>
 * 	<li> The current part is already installed(a record in GAL185TBX).
 * 	<li> All of measurements(records in GAL195TBX) of the installed part is OK.
 * 	<li> The number of the measurements(record count in GAL195TBX) equals to MC_OP_PART_REV_TBX.MEASUREMENT_COUNT.
 * </p>
 * <br/>
 * <p>
 * If any of the above criteria fails, the overall status will be NG(Not Good).</p>
 * 
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
 * @see PartConfirmationView
 * @author Hale Xie
 * June 30, 2014
 *
 */
public class PartConfirmationProcessor extends OperationProcessor {
	protected ProductBuildResult productBuildResult;

	public ProductBuildResult getProductBuildResult() {
		return productBuildResult;
	}

	/**
	 * Instantiates a new part confirmation processor.
	 *
	 * @param controller the controller
	 * @param operation the operation
	 */
	public PartConfirmationProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
		initialize();
	}

	protected void initialize() {
	}

	public String getProductId() {
		return getController().getProductModel().getProductId();
	}

	/**
	 * Process the scanned product id. The function will try to read the product build result for the scanned product id.
	 */
	public void processProductId() {
		this.productBuildResult = null;
		ProductBuildResultDao<? extends ProductBuildResult, ?> productBuildResultDao = ProductTypeUtil
				.getProductBuildResultDao(getController().getProductModel().getProductType());

		ProductBuildResult result = productBuildResultDao.findById(
				getProductId(), getOperation().getId().getOperationName());
		if (result != null && result instanceof InstalledPart) {
			MeasurementDao measurementDao = ServiceFactory.getDao(MeasurementDao.class);
			List<Measurement> measurements = measurementDao.findAll(getProductId(), getOperation().getId().getOperationName());
			InstalledPart installedPart = (InstalledPart) result;
			installedPart.setMeasurements(measurements);
		}
		this.productBuildResult = result;
	}

	/**
	 * Gets the measurement overall status of the current product.
	 * <p>
	 * the overall status is OK only when <br/>
	 * <li>The current part is already installed(a record in GAL185TBX).
	 * <li>All of measurements(records in GAL195TBX) of the installed part is
	 * OK.
	 * <li>The number of the measurements(record count in GAL195TBX) equals to
	 * MC_OP_PART_REV_TBX.MEASUREMENT_COUNT.
	 * </p>
	 * <br/>
	 * <p>
	 * If any of the above criteria fails, the overall status will be NG(Not
	 * Good).
	 * </p>
	 * 
	 * @return the measurement overall status
	 */
	public MeasurementStatus getMeasurementOverallStatus() {
		if (productBuildResult == null) {
			//Part not installed
			return MeasurementStatus.NG;
		}

		MeasurementStatus status = MeasurementStatus.OK;
		if (productBuildResult instanceof InstalledPart) {
			InstalledPart installedPart = (InstalledPart) productBuildResult;
			List<Measurement> measurements = installedPart.getMeasurements();
			MCOperationRevision operation = getOperation();
			int measurementCount = operation.getSelectedPart()
					.getMeasurementCount();
			if ((measurements == null && measurementCount > 0)) {
				//Missing Measurements
				status = MeasurementStatus.NG;
			} else if (measurements != null
					&& measurements.size() < measurementCount) {
				//Missing Measurements
				status = MeasurementStatus.NG;
			} else if (measurements != null) {
				for (Measurement m : measurements) {
					if (m.getMeasurementStatus() != MeasurementStatus.OK) {
						//Wrong measurement status, so overall status is NG
						status = MeasurementStatus.NG;
						break;
					}
				}
			}
		}
		return status;
	}

	
	/**
	 * Finish work unit and notify the data collection framework
	 */
	public void finish() {
		completeOperation(true);
	}
}
