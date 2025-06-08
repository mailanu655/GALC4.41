package com.honda.galc.client.datacollection.view;

import javax.swing.ImageIcon;

import com.honda.galc.client.datacollection.property.ImagePropertyBean;
import com.honda.galc.common.logging.Logger;
/**
 * 
 * <h3>DataCollectionImageManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DataCollectionImageManager description </p>
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
 * Jun 7, 2010
 *
 */
public class DataCollectionImageManager {
	ImageIcon imageOK;
	ImageIcon imageNG;
	ImageIcon imageBLANK;
	ImagePropertyBean propertyBean;
	public DataCollectionImageManager(ImagePropertyBean propertyBean)
	{
		this.propertyBean = propertyBean;
		initializer();
	}

	private void initializer() {
		loadImages();
	}

	private void loadImages() {
		try {
			imageOK = new ImageIcon(getClass().getResource(propertyBean.getOkImage()));
			imageNG = new ImageIcon(getClass().getResource(propertyBean.getNgImage()));
			imageBLANK = new ImageIcon(getClass().getResource(propertyBean.getBlankImage()));
		} catch (Exception e) {
			traceLog("ThreadID = "+Thread.currentThread().getName()+ " exception to get imageIcon: " + e);
		}	
		
	}

	private void traceLog(String msg) {
		Logger.getLogger().debug(msg);
		
	}

	public ImageIcon getImageBlank() {
		return imageBLANK;
	}

	public void setImageBlank(ImageIcon imageBLANK) {
		this.imageBLANK = imageBLANK;
	}

	public ImageIcon getImageNg() {
		return imageNG;
	}

	public void setImageNg(ImageIcon imageNG) {
		this.imageNG = imageNG;
	}

	public ImageIcon getImageOk() {
		return imageOK;
	}

	public void setImageOk(ImageIcon imageOK) {
		this.imageOK = imageOK;
	}
	

}
