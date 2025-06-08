package com.honda.galc.client.dc.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.property.BearingPropertyBean;
import com.honda.galc.dao.product.bearing.BearingMatrixDao;
import com.honda.galc.entity.bearing.BearingMatrix;
import com.honda.galc.entity.bearing.BearingMatrixId;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * 
 * <h3>BearingProcessor</h3>
 * <h3>The class is the parent operation processor for the bearing pick and bearing select  </h3>
 * <h4>  </h4>
 * <p> The operation processor contains the common functions shared by bearing select and bearing pick</p>
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
 * @see BearingPickProcessor
 * @see BearingSelectProcessor
 * @author Hale Xie
 * May 30, 2014
 *
 */
public abstract class BearingProcessor extends OperationProcessor {

	private BearingPropertyBean property;
	
	private final Image IMG_DONE = new Image("resource/images/common/confirm.png");
	private final Image IMG_REJECT = new Image("resource/images/common/reject.png");
	private static final double NORMALIZED_IMAGE_SIZE = 30.0;
	
	public BearingProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
	}

	
	/**
	 * Gets the main bearing count.
	 *
	 * @return the main bearing count
	 */
	public int getMainBearingCount() {
	    int defaultCount = getProperty().getMainBearingCount();
	    if(getProperty().getUseBearingMatrixForBearingAndConrodCounts()) {
	        //Fetch Main Bearing count from GAL106TBX (BearingMatrix)
	        BearingMatrix bearingMatrix = getBearingMatrix();
	        if(bearingMatrix!= null && bearingMatrix.getNumberOfMainBearings() != 0) {
	            return bearingMatrix.getNumberOfMainBearings();
	        }
	    }
	    return defaultCount;
	}

	/**
	 * Gets the conrod count.
	 *
	 * @return the conrod count
	 */
	public int getConrodCount() {
	    int defaultCount = getProperty().getConrodCount();
	    if(getProperty().getUseBearingMatrixForBearingAndConrodCounts()) {
	        //Fetch conrod count from GAL106TBX (BearingMatrix)
	        BearingMatrix bearingMatrix = getBearingMatrix();
	        if(bearingMatrix!= null && bearingMatrix.getNumberOfConrods() != 0) {
	            return bearingMatrix.getNumberOfConrods();
	        }
	    }
	    return defaultCount;
	}
	
	public List<Integer> getFiringIx() {
		List<Integer> list;
		
		if (getConrodCount() == 6) {
			list = new ArrayList<Integer>(Arrays.asList(1, 4, 2, 5, 3, 6));
		}
		else {
			list = new ArrayList<Integer>();
			for(int i = 1; i <= getConrodCount(); i++) {
				list.add(i);
			}
		}
		
		return list;
	}

	protected BearingMatrix getBearingMatrix() {
	    BearingMatrixDao bearingMatrixDao = ServiceFactory.getDao(BearingMatrixDao.class);
	    BearingMatrixId bearingMatrixId = new BearingMatrixId();
	    String modelYearCode = ProductSpec.extractModelYearCode(getController()
	            .getProductModel().getProduct().getProductSpecCode());
	    String modelCode = getController().getProductModel().getProduct()
	            .getModelCode();
	    bearingMatrixId.setModelYearCode(modelYearCode);
	    bearingMatrixId.setModelCode(modelCode);
	    return bearingMatrixDao.findByKey(bearingMatrixId);
	}
	
	public List<Integer> getMainBearingIxSequence() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < getMainBearingCount(); i++) {
			list.add(i + 1);
		}
		return list;
	}

	/**
	 * Gets the conrod index sequence.
	 *
	 * @return the conrod index sequence
	 */
	public List<Integer> getConrodIxSequence() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < getConrodCount(); i++) {
			list.add(i + 1);
		}
		return list;
	}

	protected BearingPropertyBean getProperty() {
		return property;
	}

	protected void setProperty(BearingPropertyBean property) {
		this.property = property;
	}
	
	public ApplicationContext getApplicationContext() {
		return this.getController().getModel().getProductModel().getApplicationContext();
	}
	
	public Image getDoneImage() {
		return IMG_DONE;
	}


	public Image getRejectImage() {
		return IMG_REJECT;
	}

	public ImageView getImageView(Image img) {
	   return normalizeImage(new ImageView(img));
	}
	
	public ImageView normalizeImage(ImageView imageView) {
		if (imageView.getImage() != null && ( imageView.getImage().getWidth() != NORMALIZED_IMAGE_SIZE || imageView.getImage().getWidth() != NORMALIZED_IMAGE_SIZE)) {
			imageView.setPreserveRatio(true);
			imageView.setFitHeight(NORMALIZED_IMAGE_SIZE);
			imageView.setFitWidth(NORMALIZED_IMAGE_SIZE);
		}
		return imageView;
	}
}