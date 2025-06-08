package com.honda.galc.client.mvc;

import java.lang.reflect.ParameterizedType;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.ui.ApplicationMainPane;
import com.honda.galc.client.ui.MainWindow;
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
public abstract class AbstractView<M extends IModel,C extends AbstractController<?,?>> extends ApplicationMainPane implements IView<M,C>{

	private C controller;
	
	private M model;
	
	private ViewId viewId;
	
	public AbstractView(ViewId viewId,MainWindow mainWindow){
		super(mainWindow);
		this.viewId = viewId;
		this.model = createModel();
		this.controller = createController();
		initView();
	}
	
	public C getController() {
		return controller;
	}
	
	public M getModel() {
		return model;
	}

	public ViewId getViewId() {
		return viewId;
	}
	
	public String getViewLabel(){
		return viewId == null ? "" : viewId.getViewLabel();
	}

	public void hide() {
	}

	public void prepare() {
        // Activate the controller to listen all components (this+children)
        getController().activate();
	}
	
	protected C createController() {
		return (C)ReflectionUtils.createInstance(getControllerClass(), model, this);
	}
	
	protected M createModel() {
		return (M)ReflectionUtils.createInstance(getModelClass());
	}
		
	@SuppressWarnings("unchecked")
	protected Class<C> getControllerClass() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
	    return (Class<C>) genericSuperclass.getActualTypeArguments()[1];
	}
	
	@SuppressWarnings("unchecked")
	protected Class<M> getModelClass() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
	    return (Class<M>) genericSuperclass.getActualTypeArguments()[0];
	}
	
	public abstract void reload();

	public abstract void start();
	
	public abstract void initView();
	
	public void reset() {}
	
	public void resetFocus() {}
}
