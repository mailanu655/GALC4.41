package com.honda.galc.client.utils;

import javax.swing.text.html.ImageView;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Background;

import com.honda.galc.client.simulation.SimulationProcessor;
import com.honda.galc.client.ui.component.ILoggedComponent;
import com.honda.galc.client.ui.component.LoggedPasswordField;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.StringUtil;

/**
 * 
 * @author Joseph Allen
 * @author Suriya Sena
 *
 */
public class GuiChangeListenerFactory {


	private static ChangeListener<Boolean> booleanChangeListener;
	private static ChangeListener<Background> backgroundChangeListener;
	private static ChangeListener<Effect> effectChangeListener;
	private static ChangeListener<ImageView> imageViewChangeListener;
	private static ChangeListener<Number> numberChangeListener;
	private static ChangeListener<Node> nodeChangeListener;
	private static ChangeListener<String> stringChangeListener;
	private static ChangeListener<String> autoCleanStringChangeListener;
	
	static {

		booleanChangeListener = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				ILoggedComponent component = (ILoggedComponent) ((ReadOnlyBooleanProperty) observable).getBean();
				if (newValue != null) {
					StringBuilder message = component.getMessage();
					message.append(" had property ");
					message.append(observable.getClass().getSimpleName());
					message.append(" changed to ");
					message.append(newValue.toString());
					Logger.getLogger().gui(message.toString());
				}
				if (observable.getClass().getSimpleName().equals("FocusedProperty")
						&& (component.getClass() == LoggedTextField.class
								|| component.getClass() == LoggedPasswordField.class)
						&& newValue) {
					SimulationProcessor.getInstance().setScene(component.getScene());
				}
			}
		};
		
		backgroundChangeListener = new ChangeListener<Background>(){
			@Override
			public void changed(ObservableValue<? extends Background> observable,Background oldValue, Background newValue) {

				if(newValue != null){
   				    ILoggedComponent component = (ILoggedComponent) ((ReadOnlyObjectProperty<? extends Background>) observable).getBean();
   				    
					StringBuilder message = component.getMessage();
					message.append(" had property ");
					String color = "";
					String image = "";
					if(newValue.getFills().size() > 0){
						color = newValue.getFills().get(0).getFill().toString();
					}
					if(newValue.getImages().size() > 0){
						image = newValue.getImages().get(0).getImage().toString();
					}
					message.append(newValue.getClass().getSimpleName());
					message.append(" changed to new color: ");
					message.append(color);
					message.append(" new image: ");
					message.append(image);
					Logger.getLogger().gui(message.toString());
				}
			}
		};
		

		effectChangeListener = new ChangeListener<Effect>(){
			@Override
			public void changed(ObservableValue<? extends Effect> observable,Effect oldValue, Effect newValue) {
				if(newValue != null){
   				    ILoggedComponent component = (ILoggedComponent) ((ReadOnlyObjectProperty<? extends Effect>) observable).getBean();
   				    
					StringBuilder message = component.getMessage();
					message.append(" had property ");
					message.append(newValue.getClass().getSimpleName());
					message.append(" changed to ");
					message.append(newValue.toString());
					Logger.getLogger().gui(message.toString());
				}
			}
		};
		
		imageViewChangeListener = new ChangeListener<ImageView>(){
			@Override
			public void changed(ObservableValue<? extends ImageView> observable, ImageView oldValue, ImageView newValue) {
				if(newValue != null){
  				    ILoggedComponent component = (ILoggedComponent) ((ReadOnlyObjectProperty<? extends ImageView>) observable).getBean();
  				    
					StringBuilder message = component.getMessage();
					message.append(" had property ");
					message.append(newValue.getClass().getSimpleName());
					message.append(" changed to ");
					message.append(newValue.getImage().toString());
					Logger.getLogger().gui(message.toString());
				}
			}
		};
		
		numberChangeListener = new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(newValue != null){
  				    ILoggedComponent component = (ILoggedComponent) ((ReadOnlyObjectProperty<? extends Number>) observable).getBean();
					StringBuilder message = component.getMessage();
					message.append(" had property ");
					message.append(newValue.getClass().getSimpleName());
					message.append(" changed to ");
					message.append(newValue.doubleValue());
					Logger.getLogger().gui(message.toString());
				}
			}
		};
		
		nodeChangeListener = new ChangeListener<Node>(){
			@Override
			public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
				
				if(newValue != null){
  				    ILoggedComponent component = (ILoggedComponent) ((ReadOnlyObjectProperty<? extends Node>) observable).getBean();
					StringBuilder message = component.getMessage();
					message.append(" had property ");
					message.append(newValue.getClass().getSimpleName());
					message.append(" changed to ");
					message.append(newValue.toString());
					Logger.getLogger().gui(message.toString());
				}
			}
		};
		
		stringChangeListener = new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue != null){
   				    ILoggedComponent component = (ILoggedComponent) ((ReadOnlyStringProperty) observable).getBean();
					StringBuilder message = component.getMessage();
					message.append(" had property ");
					message.append(newValue.getClass().getSimpleName());
					message.append(" changed to ");
					message.append(newValue);
					Logger.getLogger().gui(message.toString());
				}
			}
		};

		autoCleanStringChangeListener = new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue != null){
   				    ILoggedComponent component = (ILoggedComponent) ((ReadOnlyStringProperty) observable).getBean();
   				    String str = StringUtil.removeControlCharacters(newValue);
   				    if(!newValue.equals(str)) {
   	   				    component.setMessage(str);
   	   				    return;
   				    }
 					StringBuilder message = component.getMessage();
					message.append(" had property ");
					message.append(newValue.getClass().getSimpleName());
					message.append(" changed to ");
					message.append(newValue);
					Logger.getLogger().gui(message.toString());
				}
			}
		};
	}

	public static ChangeListener<Boolean> getBooleanListener(final ILoggedComponent component) {
		return booleanChangeListener;
	}

	public static ChangeListener<Background> getBackgroundListener(final ILoggedComponent component) {
		return backgroundChangeListener;
	}

	public static ChangeListener<Effect> getEffectListener(final ILoggedComponent component) {
		return effectChangeListener;
	}

	public static ChangeListener<ImageView> getImageViewListener(final ILoggedComponent component) {
		return imageViewChangeListener; 
	}

	public static ChangeListener<Number> getNumberListener(final ILoggedComponent component) {
		return numberChangeListener;
	}

	public static ChangeListener<Node> getNodeListener(final ILoggedComponent component) {
		return nodeChangeListener;
	}

	public static ChangeListener<String> getStringListener(final ILoggedComponent component) {
		return stringChangeListener;
	}

	public static ChangeListener<String> getAutoCleanStringListener(final ILoggedComponent component) {
		return autoCleanStringChangeListener;
	}
}
