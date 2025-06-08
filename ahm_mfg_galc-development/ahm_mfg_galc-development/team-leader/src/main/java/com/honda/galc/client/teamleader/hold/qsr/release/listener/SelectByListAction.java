package com.honda.galc.client.teamleader.hold.qsr.release.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.dialog.NumberImportResultDialog;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.util.ExtensionFileFilter;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>SelelctByListAction</code> is ...
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
 * <TD>Xiaofen Wang</TD>
 * <TD>June 22, 2011</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * <TR>
 * <TD>Karol Wozniak</TD>
 * <TD>November 2012</TD>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>Migrated to GALC Reg</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Xiaofen Wang
 */
public class SelectByListAction extends BaseListener<ReleasePanel> implements ActionListener {

	private String inputFolderPath;

	public SelectByListAction(ReleasePanel parentPanel) {
		super(parentPanel);
	}

	@Override
	public void executeActionPerformed(ActionEvent ae) {

		// read data from the file
		String fileName = getProductListFileName();
		if ("".equals(fileName))
			return;

		List<String> duplicatedNumbers = new ArrayList<String>();
		List<String> nonDuplicatedNumbers = new ArrayList<String>();

		readInputNumbers(fileName, duplicatedNumbers, nonDuplicatedNumbers);

		// analyze the data
		if (nonDuplicatedNumbers.size() == 0) {
			JOptionPane.showMessageDialog(getView(), "Selected File is Empty", "Empty File", JOptionPane.WARNING_MESSAGE);
			return;
		}

		List<BaseProduct> products = getProducts();

		List<String> validNumbers = new ArrayList<String>();
		List<String> invalidNumbers = new ArrayList<String>();
		List<String> duplicatedDcMcNumbers = new ArrayList<String>();
		analyseInputNumbers(nonDuplicatedNumbers, products, validNumbers, invalidNumbers, duplicatedDcMcNumbers);
		if (products.size() == 0) {
			JOptionPane.showMessageDialog(getView(), "Selected file contains only invalid numbers", "Invalid Input Data", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// pop up validation dialog,and send control to the dialog, the dialog
		// will send file result to highlight origanl list
		boolean submit = popupValidationDialog(validNumbers, invalidNumbers, duplicatedNumbers, nonDuplicatedNumbers, duplicatedDcMcNumbers, fileName);

		// select items in validNumbers list
		if (submit) {
			selectItemsInProductPanel(validNumbers);
		}
	}

	private String getProductListFileName() {
		String path = StringUtils.isBlank(getInputFolderPath()) ? path = "" : getInputFolderPath().trim();
		JFileChooser fc = new JFileChooser(path);
		String fileName = "";

		FileFilter filter = new ExtensionFileFilter("csv", "txt");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);

		int returnVal = fc.showOpenDialog(getView());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			fileName = (fc.getSelectedFile().getAbsolutePath());
			setInputFolderPath(fc.getSelectedFile().getParentFile().getAbsolutePath());
		}
		return fileName;
	}

	protected void readInputNumbers(String fileName, List<String> duplicatedNumbers, List<String> nonDuplicatedNumbers) {

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
					if (!nonDuplicatedNumbers.contains(strLine)) {
						nonDuplicatedNumbers.add(strLine);
					} else {
						duplicatedNumbers.add(strLine);
					}
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
	}

	protected List<BaseProduct> getProducts() {

		List<Map<String, Object>> rows = getView().getProductPanel().getItems();

		List<BaseProduct> products = new ArrayList<BaseProduct>();
		for (Map<String, Object> row : rows) {
			products.add((BaseProduct) row.get("product"));
		}
		return products;
	}

	protected void selectItemsInProductPanel(List<String> validNumbers) {
		List<Map<String, Object>> rows = getView().getProductPanel().getItems();
		getView().getProductPanel().clearSelection();
		for (Map<String, Object> row : rows) {
			BaseProduct product = (BaseProduct) row.get("product");
			if (product instanceof DieCast) {
				DieCast diecast = (DieCast) product;
				if (validNumbers.contains(diecast.getProductId()) || validNumbers.contains(diecast.getDcSerialNumber()) || validNumbers.contains(diecast.getMcSerialNumber())) {
					int ix = rows.indexOf(row);
					getView().getProductPanel().getTable().getSelectionModel().addSelectionInterval(ix, ix);
				}
			} else {
				if (validNumbers.contains(product.getProductId())) {
					int ix = rows.indexOf(row);
					getView().getProductPanel().getTable().getSelectionModel().addSelectionInterval(ix, ix);
				}
			}
		}
	}

	protected void analyseInputNumbers(List<String> numbers, List<BaseProduct> products, List<String> validNumbers, List<String> invalidNumbers, List<String> duplicatedDcMcNumbers) {
		invalidNumbers.addAll(numbers);
		Set<BaseProduct> processedProducts = new HashSet<BaseProduct>();
		for (BaseProduct product : products) {
			if (product instanceof DieCast) {
				DieCast diecast = (DieCast) product;
				if (invalidNumbers.contains(diecast.getDcSerialNumber())) {
					invalidNumbers.remove(diecast.getDcSerialNumber());
					validNumbers.add(diecast.getDcSerialNumber());
					processedProducts.add(diecast);
				}
				if (invalidNumbers.contains(diecast.getMcSerialNumber())) {
					if (processedProducts.contains(diecast)) {
						duplicatedDcMcNumbers.add(diecast.getDcSerialNumber() + " : " + diecast.getMcSerialNumber());
						invalidNumbers.remove(diecast.getMcSerialNumber());
					} else {
						invalidNumbers.remove(diecast.getMcSerialNumber());
						validNumbers.add(diecast.getMcSerialNumber());
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

	private boolean popupValidationDialog(List<String> validNumbers, List<String> invalidNumbers, List<String> duplicatedNumbers, List<String> nonDuplicatedNumbers, List<String> duplicatedDcMcNumbers, String filename) {
		if (invalidNumbers.isEmpty() && duplicatedNumbers.isEmpty() && duplicatedDcMcNumbers.isEmpty())
			return !validNumbers.isEmpty();
		String msg = "Total numbers : " + (duplicatedNumbers.size() + nonDuplicatedNumbers.size());
		msg += ", valid : " + validNumbers.size();
		msg += ", invalid : " + invalidNumbers.size();
		msg += ", duplicates : " + duplicatedNumbers.size();
		if (Config.getInstance().isDiecast(getView().getProductType())) {
			msg += ", duplicated DC MC: " + duplicatedDcMcNumbers.size();
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

		final NumberImportResultDialog dialog = new NumberImportResultDialog(getView(), "Validation Results", msg, data, filename);
		dialog.setLocationRelativeTo(getMainWindow());
		dialog.setVisible(true);
		if (dialog.isSubmit()) {

			if (getView().getProductPanel().getTable().getSelectedRowCount() > 0) {
				JOptionPane.showMessageDialog(getView(), "The current selection will be lost, only those in the list file will be selected!", "Information", JOptionPane.WARNING_MESSAGE);
				getView().getProductPanel().clearSelection();
			}
		}
		return dialog.isSubmit();
	}

	public String getInputFolderPath() {
		return inputFolderPath;
	}

	public void setInputFolderPath(String inputFolderPath) {
		this.inputFolderPath = inputFolderPath;
	}
}
