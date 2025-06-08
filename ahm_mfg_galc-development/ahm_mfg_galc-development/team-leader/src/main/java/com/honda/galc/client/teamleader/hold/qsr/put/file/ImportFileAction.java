package com.honda.galc.client.teamleader.hold.qsr.put.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.dialog.NumberImportResultDialog;
import com.honda.galc.client.teamleader.hold.qsr.QsrAction;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ImportFileAction</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class ImportFileAction extends QsrAction<ImportFilePanel> implements ActionListener {

	public ImportFileAction(ImportFilePanel parentPanel) {
		super(parentPanel);
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {

		getView().getProductPanel().removeData();

		String fileName = getView().getInputPanel().getFileNameInput().getText();

		List<String> inputNumbers = readInputNumbers(fileName);
		List<String> duplicatedNumbers = new ArrayList<String>();
		List<String> duplicatedDcMcNumbers = new ArrayList<String>();
		List<String> deduplicatedNumbers = new ArrayList<String>();

		for (String str : inputNumbers) {
			if (!deduplicatedNumbers.contains(str)) {
				deduplicatedNumbers.add(str);
			} else {
				duplicatedNumbers.add(str);
			}
		}

		if (deduplicatedNumbers.size() == 0) {
			JOptionPane.showMessageDialog(getView(), "Selected File is Empty", "Empty File", JOptionPane.WARNING_MESSAGE);
			return;
		}

		List<BaseProduct> products = filterOnlyValids(deduplicatedNumbers);

		List<String> validNumbers = new ArrayList<String>();
		List<String> invalidNumbers = new ArrayList<String>();

		analyseInputNumbers(deduplicatedNumbers, products, validNumbers, invalidNumbers, duplicatedDcMcNumbers);

		if (products.size() == 0) {
			JOptionPane.showMessageDialog(getView(), "Selected file contains only invalid numbers", "Invalid Input Data", JOptionPane.WARNING_MESSAGE);
			return;
		}
		boolean submit = true;
		if (invalidNumbers.size() > 0 || duplicatedNumbers.size() > 0 || duplicatedDcMcNumbers.size() > 0) {

			StringBuilder msg = new StringBuilder();
			msg.append("Total numbers : ").append(inputNumbers.size());
			msg.append(", valid : ").append(validNumbers.size());
			msg.append(", invalid : ").append(invalidNumbers.size());
			msg.append(", duplicates : ").append(duplicatedNumbers.size());
			if (Config.getInstance().isDiecast(getView().getProductType())) {
				msg.append(", duplicated DC MC: ").append(duplicatedDcMcNumbers.size());
			}
			List<Map<String, String>> data = new ArrayList<Map<String, String>>();
			for (String str : invalidNumbers) {
				Map<String, String> row = new HashMap<String, String>();
				row.put("number", str);
				row.put("comment", "Invalid");
				data.add(row);
			}
			for (String str : duplicatedNumbers) {
				Map<String, String> row = new HashMap<String, String>();
				row.put("number", str);
				row.put("comment", "Duplicated");
				data.add(row);
			}
			for (String str : duplicatedDcMcNumbers) {
				Map<String, String> row = new HashMap<String, String>();
				row.put("number", str);
				row.put("comment", "Duplicated DC MC");
				data.add(row);
			}

			final NumberImportResultDialog dialog = new NumberImportResultDialog(getView(), "Validation Results", msg.toString(), data, fileName);

			dialog.setLocationRelativeTo(getView().getMainWindow());
			dialog.setVisible(true);
			if (!dialog.isSubmit()) {
				submit = false;
			}
		}

		if (submit) {
			populateTable(products);
		}

		fileName = getView().getInputPanel().getFileNameInput().getText();
		Division division = getView().getDivision();
		boolean uploadEnabled = division != null && !isEmpty(fileName);
		getView().getInputPanel().getImportButton().setEnabled(uploadEnabled);
	}

	protected List<String> readInputNumbers(String fileName) {

		List<String> inputNumbers = new ArrayList<String>();
		File file = new File(fileName);

		FileInputStream fstream = null;
		DataInputStream in = null;
		try {
			fstream = new FileInputStream(file);

			in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				strLine = strLine.trim();
				if (strLine.length() > 0) {
					inputNumbers.add(strLine);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e1) {
			}
		}
		return inputNumbers;
	}

	protected List<BaseProduct> filterOnlyValids(List<String> inputNumbers) {

		List<BaseProduct> products = new ArrayList<BaseProduct>();
		List<String> shippedproducts = new ArrayList<String>();
		List<String> shiplineIds = getShipLineIds();
		if (inputNumbers == null || inputNumbers.isEmpty()) {
			return products;
		}
		String productTypeStr = this.getView().getProductType().toString();
		ProductTypeData productTypeData = ServiceFactory.getDao(ProductTypeDao.class).findByKey(productTypeStr);
		ProductDao<?> productDao = getProductDao(getView().getProductType());
		
		for (String serialNumber : inputNumbers) {
			if(!productTypeData.isNumberValid(serialNumber)){
				continue;
			}
			
			if(getView().getProductType().equals(ProductType.ENGINE)) {
				BaseProduct engineProduct = productDao.findBySn(serialNumber);
				if(engineProduct  != null) {
				Engine engine = (Engine)engineProduct ;
				if(StringUtils.isNotBlank(engine.getVin())){
					Frame frame =  ServiceFactory.getDao(FrameDao.class).findBySn(engine.getVin());
					if(shiplineIds.contains(frame.getTrackingStatus())) {
						shippedproducts.add(engine.getId());
						continue;
					}else {
						products.add(engineProduct);	
					}
				}else {
					products.add(engineProduct);	
				}
					
				}else {
					products.add(ProductTypeUtil.createProduct(productTypeStr, serialNumber));
				}
				
			 }else {	
				if(Config.isDisableProductIdCheck(productTypeStr)){
					
					products.add(ProductTypeUtil.createProduct(productTypeStr, serialNumber));
				}else{
					BaseProduct product = productDao.findBySn(serialNumber);
					if (product != null && !products.contains(product)) {
							products.add(product);	
						}
					}
				}	
			
		}
		
		if(shippedproducts.size() > 0) {
			JOptionPane.showMessageDialog(getView(), "Selected file contains Engine Numbers assigned to Vins that are already shipped/scrapped", "Invalid Input Data", JOptionPane.WARNING_MESSAGE);
		}
		
		return products;
	}

	protected void analyseInputNumbers(List<String> numbers, List<BaseProduct> products, List<String> validNumbers, List<String> invalidNumbers, List<String> duplicatedDcMcNumbers) {
		invalidNumbers.addAll(numbers);
		Set<BaseProduct> processedProducts = new HashSet<BaseProduct>();
		for (BaseProduct product : products) {
			if (product instanceof DieCast) {
				DieCast dieCast = (DieCast) product;
				if (invalidNumbers.contains(dieCast.getDcSerialNumber())) {
					invalidNumbers.remove(dieCast.getDcSerialNumber());
					validNumbers.add(dieCast.getDcSerialNumber());
					processedProducts.add(dieCast);
				}
				if (invalidNumbers.contains(dieCast.getMcSerialNumber())) {
					if (processedProducts.contains(dieCast)) {
						duplicatedDcMcNumbers.add(String.format("%s : %s", dieCast.getDcSerialNumber(), dieCast.getMcSerialNumber()));
						invalidNumbers.remove(dieCast.getMcSerialNumber());
					} else {
						invalidNumbers.remove(dieCast.getMcSerialNumber());
						validNumbers.add(dieCast.getMcSerialNumber());
					}
				}
			} else {
				if (invalidNumbers.contains(product.getProductId())) {
					invalidNumbers.remove(product.getProductId());
					validNumbers.add(product.getProductId());
					processedProducts.add(product);
				}
			}
		}
	}

	protected void populateTable(List<BaseProduct> products) {
		List<Map<String, Object>> holdRecords = this.prepareHoldRecords(products);
		getView().getProductPanel().reloadData(holdRecords);
	}
	
	private List<String> getShipLineIds(){
		String shipLineId = Config.getProperty().getShipLineId();
		List<String> lineIdList = Arrays.asList(shipLineId.split(Delimiter.COMMA));
		
		return lineIdList;
	}
}
