package com.honda.galc.client.teamleader;


import javax.swing.JTextField;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import com.honda.galc.client.teamleader.history.ProductHistoryMaintenancePanel;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.service.property.PropertyService;
import junit.framework.TestCase;


@PrepareForTest({PropertyService.class,FrameDao.class, ProductResultDao.class,ProcessPointDao.class, ApplicationMainPanel.class})
public class ProductHistoryMaintenancePanelTest extends TestCase{
	
	private ProductHistoryMaintenancePanel testView;
	
	/**
	 * @author Gangadhararao Gadde
	 * @date Oct 26,2018
	 * 
	 * Tests Name(ID) for ProductTextField for simulation purposes
	 */
	@Test	
	public void testName_testProductIdTextField( ){
/*		testView = new ProductHistoryMaintenancePanel();
		JTextField productIdTextField=testView.createProductIdTextField(17);
		assertEquals(productIdTextField.getName(),"productIdTextField");
*/	}

	/**
	 * @author Gangadhararao Gadde
	 * @date Oct 26,2018
	 * 
	 * Tests Name(ID) for ActualTimestampTextField for simulation purposes
	 */
	@Test	
	public void testName_testActualNameTextField( ){
/*		testView = new ProductHistoryMaintenancePanel();
		JTextField actualTimestampTextField=testView.createActualTimestampTextField();
		assertEquals(actualTimestampTextField.getName(),"actualTimestampTextField");
*/	}

	/**
	 * @author Gangadhararao Gadde
	 * @date Oct 26,2018
	 * 
	 * Tests Name(ID) for ProductHistoryPane for simulation purposes
	 */
	@Test	
	public void testName_testProductHistoryPane( ){
/*		testView = new ProductHistoryMaintenancePanel();
		ObjectTablePane<ProductHistory> productHistoryPane=testView.createProductHistoryPane();
		assertEquals(productHistoryPane.getTable().getName(),"productHistoryTable");
*/	}

}
