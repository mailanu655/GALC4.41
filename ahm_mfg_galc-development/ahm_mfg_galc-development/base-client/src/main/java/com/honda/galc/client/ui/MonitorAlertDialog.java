/**
 * 
 */
package com.honda.galc.client.ui;

import java.awt.HeadlessException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.ui.event.Event;
import com.honda.galc.client.ui.event.EventType;
import com.honda.galc.common.logging.Logger;

/**
 * @author Subu Kathiresan
 * @date Jan 12, 2012
 */
public class MonitorAlertDialog extends JDialog implements Runnable {
	
	private static final long serialVersionUID = -6414948234294109441L;
	
	private String message;
	private String buttonText;
	private JFrame owner;
	private JOptionPane optionPane;
	
	public MonitorAlertDialog(JFrame owner, String title, String message, String buttonText) throws HeadlessException {
		super(owner, true);
		
		super.setTitle(title);
		this.owner = owner;
		this.message = message;
		this.buttonText = buttonText;
		
		intialize();
	}

	private void intialize() {
		AnnotationProcessor.process(this);

		Object stringArray[] = {buttonText};
		optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_OPTION, null, stringArray);
		optionPane.addPropertyChangeListener(
				new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent e) {
						String prop = e.getPropertyName();
						if ((e.getSource() == optionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
							Event event = new Event(getTitle(), this, EventType.WARNING_OVERRIDDEN);
							EventBus.publish(event);
							disposeDialog();
						}
					}
				});

		this.add(optionPane);
		this.pack();
		this.setLocationRelativeTo(owner);
	}

	@EventSubscriber(eventClass = Event.class)
	public void processMonitorAlertEvent(Event event){
		if (event.getEventType() == EventType.PLC_READY && event.getSource().equals(super.getTitle())) {
			Logger.getLogger().info("Received Event :", event.getSource().getClass() + ": " + EventType.PLC_READY);
			disposeDialog();
		}
	}

	private void disposeDialog() {
		optionPane.setEnabled(false);
		optionPane.setVisible(false);
		this.dispose();
	}
	
	public String getTitle() {
		return super.getTitle();
	}

	public void run() {
		this.setVisible(true);
	}
	
	public void start(){
		try {
			SwingUtilities.invokeAndWait(this);
		} catch (Exception e) {
			Logger.getLogger().warn(e, "Failed to start client start up progress monitor.");
		}		
	}
}
