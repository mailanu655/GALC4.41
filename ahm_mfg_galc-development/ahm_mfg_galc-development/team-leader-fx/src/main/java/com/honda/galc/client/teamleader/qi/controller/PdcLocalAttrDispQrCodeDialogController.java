package com.honda.galc.client.teamleader.qi.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.honda.galc.client.teamleader.qi.model.PdcLocalAttributeMaintModel;
import com.honda.galc.client.teamleader.qi.view.PdcLocalAttrDispQrCodeDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.PdcRegionalAttributeMaintDto;
import com.honda.galc.entity.enumtype.QiDefectEntryScanType;

/**
 * 
 * <h3>PdcLocalAttributeDisplayQrCodeDialogController Class description</h3>
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
 * @author LnTInfotech<br>
 * 
 */
public class PdcLocalAttrDispQrCodeDialogController  extends QiDialogController<PdcLocalAttributeMaintModel, PdcLocalAttrDispQrCodeDialog> {
	
	public PdcLocalAttrDispQrCodeDialogController(PdcLocalAttributeMaintModel model, PdcLocalAttrDispQrCodeDialog dialog) {
		super();
		setModel(model);
		setDialog(dialog);
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if( actionEvent.getSource().equals(getDialog().getExportButton())) exportButtonAction(actionEvent);
		else if(actionEvent.getSource().equals(getDialog().getCancelButton())) cancelButtonAction(actionEvent);
	}

	@Override
	public void initListeners() {
		clearDisplayMessage();
	}
	
	/**
	 * This method is used to close popup.
	 */
	public void cancelButtonAction(ActionEvent event) {
		LoggedButton cancelButton = getDialog().getCancelButton();
		try {
			Stage stage = (Stage) cancelButton.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action", e);
		}
	}
	
	/**
	 * This method is used to export an excel file as specified location.
	 */
	
	public void exportButtonAction(ActionEvent event) {
		EventBusUtil.publish(new StatusMessageEvent("", StatusMessageEventType.CLEAR));
		try {
			String excelFileName = getExcelFileName();
			if(excelFileName == null) {
				EventBusUtil.publish(new StatusMessageEvent("Export file name was not specified.", StatusMessageEventType.DIALOG_ERROR));
				return;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			String sheetName = QiConstant.DISPLAY_QR_CODE;//name of sheet
			XSSFSheet sheet = workbook.createSheet(sheetName) ;
			ObservableList<PdcRegionalAttributeMaintDto> dataList = getDialog().getDisplayQrCodePane().getTable().getItems();
			
			CellStyle headerStyle = workbook.createCellStyle();//Create header style
			setStyleproperties(headerStyle);
			CellStyle cellStyle = workbook.createCellStyle();//Create data cell style
			setStyleproperties(cellStyle);
		    Font font = workbook.createFont();//Create font
		    font.setBoldweight(Font.BOLDWEIGHT_BOLD);//Make font bold
		    headerStyle.setFont(font);
		    XSSFRow headerRow=sheet.createRow(0);
		    createCell(sheet,headerRow.createCell(0),"#",headerStyle);
		    createCell(sheet,headerRow.createCell(1),"Part Defect Description",headerStyle);
		    createCell(sheet,headerRow.createCell(2),"QR Code",headerStyle);
			//iterating r number of rows
			int rowSize = dataList.size();
			for (int r=1;r <= rowSize ; r++ )
			{
				PdcRegionalAttributeMaintDto dto=dataList.get(r-1);
				XSSFRow row=sheet.createRow(r);
				createCell(sheet,row.createCell(0),r,cellStyle);
				createCell(sheet,row.createCell(1),dto.getPartDefectDesc(),cellStyle);
				XSSFCell cell=createCell(sheet,row.createCell(2),"",cellStyle);
				Integer pdcId = dto.getLocalAttributeId();
				ImageView imageView = getQRCode(pdcId+"-"+getDialog().getEntryScreen()+"-"+dto.getTextEntryMenu());
				addImageToExcel(workbook, sheet, r, imageView,cell,2);
				sheet.setColumnWidth(1, 40*256);
			}
			int rowIndex=rowSize+2;
			int cellIndex=1;
			createDefectStatusForAction(workbook, sheet, cellStyle, rowSize,rowIndex, cellIndex);
			cellIndex=1;
			createCells(workbook, sheet, cellStyle,rowSize, rowIndex, cellIndex);
		    FileOutputStream fileOut = null;
			fileOut = new FileOutputStream(excelFileName);
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
			EventBusUtil.publish(new StatusMessageEvent("Exported to Excel successfully.", StatusMessageEventType.DIALOG_INFO));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to create cells for Action -- Done,DirectPass.
	 */
	private void createDefectStatusForAction(XSSFWorkbook workbook,XSSFSheet sheet, CellStyle cellStyle, int rowSize, int rowIndex,int cellIndex) throws IOException {
		XSSFRow actionRow=sheet.createRow(rowIndex);
		if(getDialog().getDoneCheckBox().isSelected()){
			 createCell(sheet,actionRow.createCell(cellIndex),QiDefectEntryScanType.DONE.getName(),cellStyle);
			 addImageToExcel(workbook, sheet, rowSize+2, getQRCode(QiDefectEntryScanType.DONE.getName()),actionRow.createCell(cellIndex+1),cellIndex+1);
			 cellIndex=cellIndex+2;
		}
		if(getDialog().getDirectPassCheckBox().isSelected()){
			createCell(sheet,actionRow.createCell(cellIndex),QiDefectEntryScanType.DIRECT_PASS.getName(),cellStyle);
			addImageToExcel(workbook, sheet, rowSize+2, getQRCode(QiDefectEntryScanType.DIRECT_PASS.getName()),actionRow.createCell(cellIndex+1),cellIndex+1);
			 cellIndex=cellIndex+2;
		}

		if(getDialog().getVoidLastCheckBox().isSelected()){
			createCell(sheet,actionRow.createCell(cellIndex),QiDefectEntryScanType.VOID_LAST.getName(),cellStyle);
			addImageToExcel(workbook, sheet, rowSize+2, getQRCode(QiDefectEntryScanType.VOID_LAST.getName()),actionRow.createCell(cellIndex+1),cellIndex+1);
		}
	}

	/**
	 * This method is used to create cells for Defect Status -- Repaired,not Repaired , Non Repairable
	 */
	private void createCells(XSSFWorkbook workbook,XSSFSheet sheet, CellStyle cellStyle, int rowSize, int rowIndex,int cellIndex) throws IOException {
		XSSFRow statusRow=sheet.createRow(rowIndex+1);
		if(getDialog().getRepairedCheckBox().isSelected()){
			cellIndex = createCellForDefectStatus(workbook, sheet, cellStyle,
					rowSize, cellIndex, statusRow,QiDefectEntryScanType.REPAIRED.getName());
		}
		if(getDialog().getNotRepairedCheckBox().isSelected()){
			cellIndex = createCellForDefectStatus(workbook, sheet, cellStyle,
					rowSize, cellIndex, statusRow,QiDefectEntryScanType.NOT_REPAIRED.getName());
		}
		if(getDialog().getNonRepairableCheckBox().isSelected()){
			cellIndex = createCellForDefectStatus(workbook, sheet, cellStyle,
					rowSize, cellIndex, statusRow,QiDefectEntryScanType.NON_REPAIRABLE.getName());
		}
	}

	private int createCellForDefectStatus(XSSFWorkbook workbook,
			XSSFSheet sheet, CellStyle cellStyle, int rowSize, int cellIndex,
			XSSFRow statusRow, String defectType) throws IOException {
		createCell(sheet,statusRow.createCell(cellIndex),defectType,cellStyle);
		addImageToExcel(workbook, sheet, rowSize+3, getQRCode(defectType),statusRow.createCell(cellIndex+1),cellIndex+1);
		cellIndex=cellIndex+2;
		return cellIndex;
	}

	/**
	 * This method is used to get excel file name and location from user.
	 */
	private String getExcelFileName() {
		FileChooser fileChooser = new FileChooser();
		//fileChooser.setInitialDirectory(new File("C:/")); 
		fileChooser.setInitialDirectory(File.listRoots()[0]);
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Files","*.xlsx");
		fileChooser.getExtensionFilters().add(extFilter);
		File file=fileChooser.showSaveDialog(null);
		return file == null ? null : file.getAbsolutePath();
	}

	/**
	 * This method is used to create cells for Action -- Done,DirectPass.
	 */
	private XSSFCell createCell(XSSFSheet sheet, XSSFCell cell, Object cellValue, CellStyle cellStyle) {
		cell.setCellValue(cellValue.toString());
		cell.setCellStyle(cellStyle);
		return cell;
	}

	private void addImageToExcel(XSSFWorkbook workbook, XSSFSheet sheet, int r,ImageView imageView, XSSFCell cell,int colIndex) throws IOException {
		BufferedImage bImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		ImageIO.write(bImage, "png", s);
		byte[] bytes  = s.toByteArray();
		s.close();
		//Adds a picture to the workbook
		int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
		//Returns an object that handles instantiating concrete classes
		CreationHelper helper = workbook.getCreationHelper();
		//Creates the top-level drawing patriarch.
		Drawing drawing = sheet.createDrawingPatriarch();
		//Create an anchor that is attached to the worksheet
		ClientAnchor anchor = helper.createClientAnchor();
		//set top-left corner for the image
		anchor.setCol1(colIndex);
		anchor.setRow1(r);
		anchor.setCol2(colIndex+1);
		anchor.setRow2(r+1);
		//Creates a picture
		XSSFPicture pict=(XSSFPicture)drawing.createPicture(anchor, pictureIdx);
		pict.setLineStyle(0);
		pict.setLineStyleColor(0, 0, 0);
		pict.setLineWidth(1);
		short heightUnits = 80*30;
		cell.getRow().setHeight(heightUnits);
		sheet.setColumnWidth(colIndex, 80*60);
	}

	private void setStyleproperties(CellStyle style) {
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setWrapText(true);
	}
	
	/**
	 * This method is used to get QR code for keyValue
	 * @param key
	 */
	public ImageView getQRCode(Object key){
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		int width = 100;
		int height = 100;

		BufferedImage bufferedImage = null;
		try {
			BitMatrix bitMatrix = qrCodeWriter.encode(key.toString(), BarcodeFormat.QR_CODE, width, height);
			bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			bufferedImage.createGraphics();

			Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, width, height);
			graphics.setColor(Color.BLACK);
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (bitMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}

		} catch (Exception ex) {
			Logger.getLogger().error("Error in creating QR Code");
		}

		ImageView qrView = new ImageView();
		qrView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
		return qrView;

	}
	
}
