package com.honda.galc.client.dc.view;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dc.mvc.ProcessInstructionModel;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class OperationListWidget extends ProcessInstructionWidget<ProcessInstructionModel> {

	public OperationListWidget(ProcessInstructionModel model) {
		super(model);
	}

	public void initComponents() {
		String ProcessPointID = getModel().getProductModel().getProcessPoint().getProcessPointId();
		String ProductID = getModel().getProductModel().getProductId();
		
		//get parameter from property
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class);
		String width = property.getOperationListWidth();
		String height = property.getOperationListHeight();
		String fontsize = property.getOperationListFontSize();
		String backgroundcolor = property.getOperationListBackGroundColor();
		String textcolor = property.getOperationListTextColor();
		String rowoddcolor = property.getOperationListAlterOddRowColor();
		String rowevencolor = property.getOperationListAlterEvenRowColor();
		String column = property.getOperationListColumnList();
		
		WebView view = new WebView();
		view.setPrefSize(Double.parseDouble(width)+35, Double.parseDouble(height));

		WebEngine engine = view.getEngine();
		String hosturl = getModel().getProductModel().getApplicationContext().getArguments().getServerURL();
		String url ="http://"+ViewControlUtil.getHostURL(hosturl)+"/BaseWeb/OperationListWidget.html?width="+width+"&height="+height+"&fontsize="+fontsize+"&PPID="+ProcessPointID+"&ProductID="+ProductID;
		if (!StringUtils.isEmpty(column)) url=url+"&column="+column;
		if (!StringUtils.isEmpty(textcolor)) url=url+"&textcolor="+textcolor;
		if (!StringUtils.isEmpty(rowoddcolor)) url=url+"&rowoddcolor="+rowoddcolor;
		if (!StringUtils.isEmpty(rowevencolor)) url=url+"&rowevencolor="+rowevencolor;
		if (!StringUtils.isEmpty(backgroundcolor)) url=url+"&backgroundcolor="+backgroundcolor;
		
		engine.load(url);
		this.setRight(view);
	}

}