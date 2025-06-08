package com.honda.galc.client.ui.component;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class FilteredLabeledComboBox extends LabeledComboBox{
	
	private static final long serialVersionUID = 1L;
	
	protected List<?> items = null;
	protected final JComboBox comboBox;
	protected final JTextField textField;
	private int lastSelectedIndex = -1;
	private boolean wasItemSelected;
		
	class ComboBoxItemListener implements ItemListener {		
		public void itemStateChanged(ItemEvent evt) {
		    final JComboBox comboBox = (JComboBox) evt.getSource();	  
		    wasItemSelected = false;
		    
		    if (evt.getStateChange() == ItemEvent.SELECTED) {
		    	wasItemSelected = true;
		    	comboBox.setEditable(false);
		    	Object selectedItem = comboBox.getSelectedItem();		    	
		    	if(selectedItem != null && !items.contains(selectedItem)){
		    		//Prevents set an invalid item when user press enter after type not valid text
		    		restoreModelItems();
		    		comboBox.setSelectedItem((lastSelectedIndex == -1) ? null : items.get(lastSelectedIndex));
		    	}else{
		    		//Store last selected item to use it if user enters an invalid text
		    		lastSelectedIndex = items.indexOf(comboBox.getSelectedItem());	    		
		    	}		    	
		    }
		}
	}	
	
	public FilteredLabeledComboBox(String labelText){
		this(labelText, true);
	}
	
	public FilteredLabeledComboBox(String labelText, boolean isHorizontal){
		super(labelText, isHorizontal);
		comboBox = getComponent();
		textField = (JTextField) comboBox.getEditor().getEditorComponent();
		addTextFieldListeners();
		addComboComponentListeners();
	}	
	
	public void addTextFieldListeners(){
		textField.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				//Set ComboBox Editable to false in order to show the correct display name instead a Object string in case element was selected
				comboBox.setEditable(false);
				//Reset selected item when user focus out and doesn't select any new value
				if(!wasItemSelected && lastSelectedIndex != -1){					
					comboBox.setSelectedItem(items.get(lastSelectedIndex));
				}
			}			
			@Override
			public void focusGained(FocusEvent e) {
				//Clear input text when user navigate to input with the keyboard
				restoreModelItems();
				textField.setText("");		
				comboBox.showPopup();
			}
		});
		textField.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){   
            	//Show popup of items when input doesn't lost focus but popup was closed by an user action
            	comboBox.showPopup();
            }
        });
		textField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
            	List<Integer> movementKeyCodes = Arrays.asList(new Integer[]{ KeyEvent.VK_UP, KeyEvent.VK_DOWN });
            	int keyCodePressed = event.getKeyCode();
            	if(keyCodePressed ==  KeyEvent.VK_ESCAPE && lastSelectedIndex != -1){
            		//When 'Esc' key was pressed, try to restore previous selected item
					comboBox.setSelectedItem(items.get(lastSelectedIndex));	
            	}else if(keyCodePressed ==  KeyEvent.VK_ESCAPE){
            		//When 'Esc' key was pressed and there is not any previous selected item, the combo box stops to be editable  
					comboBox.setEditable(false);
				}else if(!movementKeyCodes.contains(keyCodePressed)){
					//If key pressed is not one used to navigate up and down on the combo list
            		filterItems(textField.getText());
            		
				}
            }
        });
	}
	
	public String getTextFieldText() {
		return textField.getText();
	}
	
	public void addComboComponentListeners(){
		comboBox.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {}			
			@Override
			public void focusGained(FocusEvent e) {
				comboBox.setEditable(true);	
			}
		});
		comboBox.addPopupMenuListener(new PopupMenuListener(){
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {	
				if(lastSelectedIndex != -1 && comboBox.getSelectedIndex() == lastSelectedIndex){
					//Clear input when user clicks an already selected item from list
					SwingUtilities.invokeLater(new Runnable() {
	                    public void run() {
	                    	textField.setText("");	
	                    }
	                });
				}	    		
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {}			
		});
		comboBox.addItemListener(new ComboBoxItemListener());
	}
	
	protected ComboBoxModel getModel(){
		return (ComboBoxModel)comboBox.getModel();
	}
	
	public <T> void setModel(ComboBoxModel<T> model) {
		this.<T>setModel(model, -1);
	}
		
	@Override
	@SuppressWarnings("unchecked")
	public <T> void setModel(ComboBoxModel<T> model, int selectionIndex) {
		this.items = (model.objects != null ) ? new ArrayList((Collection<? extends T>)model.objects) : new ArrayList();
		super.setModel(model, selectionIndex);
	}
	
	public void restoreModelItems(){
		ComboBoxModel model = getModel();
		model.objects = new ArrayList(items);
		super.setModel(model, -1);
	}
	
	public void setSelectedIndex(int index){
		comboBox.setSelectedIndex(index);
	}
	
	public void filterItems(String enteredText) {
		if(items == null)return;
		
		ComboBoxModel model = getModel();
		
		if(enteredText.trim().equals("")){	
			model.objects = new ArrayList(items);
		}else{
			model.objects = new ArrayList();
			int listSize = items.size();
	        for (int i = 0; i < listSize; i++) {
	        	String itemDisplayName = model.getDisplayObject(items.get(i)).trim().toLowerCase();
	        	if(itemDisplayName.contains(enteredText.trim().toLowerCase())){
	        		model.objects.add(items.get(i));
	        	}   
	        }
		}
        
		//Hide popup in order to force UI to resize its height
		comboBox.hidePopup();
        super.setModel(model, -1);
        textField.setText(enteredText);
        comboBox.showPopup();
    }	
}
