package com.honda.galc.client.mvc;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.lang.reflect.ParameterizedType;

import javax.swing.JButton;
import javax.swing.JTextField;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.util.ReflectionUtils;


/**
 * 
 * <h3>AbstractView Class description</h3>
 * <p> AbstractView description </p>
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
 * @author Jeffray Huang<br>
 * Feb 24, 2014
 *
 *
 */
public abstract class AbstractView<M extends AbstractModel,C extends AbstractController<?,?>> extends ApplicationMainPanel implements IView<M,C>{

	private static final long serialVersionUID = 1L;

	protected C controller;
	
	protected M model;
	
	public AbstractView(MainWindow mainWindow){
		super(mainWindow);
		this.model = createModel();
		model.setApplicationContext(mainWindow.getApplicationContext());
		this.controller = createController();
		
		prepare();
	}
	
	public C getController() {
		return controller;
	}
	
	public M getModel() {
		return model;
	}
	
	protected C createController() {
		return (C)ReflectionUtils.createInstance(getControllerClass(), model, this);
	}
	
	protected M createModel() {
		return (M)ReflectionUtils.createInstance(getModelClass());
	}
	
	
	@SuppressWarnings("unchecked")
	private Class<C> getControllerClass() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
	    return (Class<C>) genericSuperclass.getActualTypeArguments()[1];
	}
	
	@SuppressWarnings("unchecked")
	private Class<M> getModelClass() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
	    return (Class<M>) genericSuperclass.getActualTypeArguments()[0];
	}
	
	protected LabeledUpperCaseTextField createLabeledTextField(String label,int labelWidth,Font font,int columnSize,Color backgroundColor,boolean enabled) {
		LabeledUpperCaseTextField labeledTextField =  new LabeledUpperCaseTextField(label);
		labeledTextField.setFont(font);
		labeledTextField.setLabelPreferredWidth(labelWidth);
		labeledTextField.getComponent().setMaximumLength(columnSize);
		labeledTextField.setInsets(0, 10, 0, 10);
		labeledTextField.getComponent().setFixedLength(false);
		labeledTextField.getComponent().setHorizontalAlignment(JTextField.RIGHT);
		labeledTextField.getComponent().setBackground(backgroundColor);
		labeledTextField.getComponent().setEnabled(enabled);
		labeledTextField.getComponent().setDisabledTextColor(Color.black);
		return labeledTextField;
	}
	
	protected LabeledComboBox createLabeledCombobox(String label,int labelWidth,Font font,boolean enabled) {
		LabeledComboBox labeledComboBox =  new LabeledComboBox(label);
		labeledComboBox.setFont(font);
		labeledComboBox.setLabelPreferredWidth(labelWidth);
		labeledComboBox.setInsets(0, 10, 0, 10);
		labeledComboBox.getComponent().setEnabled(enabled);
		return labeledComboBox;
	}
	
	protected JButton createButton(String label,Font font) {
		JButton button = new JButton(label);
		button.setName(label);
		button.setMargin(new Insets(0,0,0,0));
		button.setFont(font);
		return button;
	}
	
	public void setWaitCursor() {
		getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}
	
	public void restoreDefaultCursor() {
		getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	public void hide() {
		
	}

	public void prepare() {
		// Activate the controller to listen all components (this+children)
        getController().activate();
        
        reload();
	}
	
}
