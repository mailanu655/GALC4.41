package com.honda.galc.client.gts.view.action;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JColorChooser;
import javax.swing.JDialog;

public class ColorChooser {
	
	private static JColorChooser colorChooser;
	
	public static Color selectColor(Component c,Color initColor) {
		final JColorChooser colorSelector = getColorChooser(initColor);
		ColorTracker colorTracker = new ColorTracker(colorSelector);
        JDialog dialog = JColorChooser.createDialog(c, "Please Pick a Color", true, colorSelector, colorTracker, colorTracker);

	    dialog.setVisible(true);
	    return colorTracker.getColor();
	}
	
	private static JColorChooser getColorChooser(Color initialColor) {
		if(colorChooser == null) {
			colorChooser = new JColorChooser(initialColor != null?
                    initialColor : Color.white);
		}
		colorChooser.setColor(initialColor != null?initialColor : Color.white);
		return colorChooser;
	}
	

}

@SuppressWarnings("serial")
class ColorTracker implements ActionListener, Serializable {
    JColorChooser chooser;
    Color color;

    public ColorTracker(JColorChooser c) {
        chooser = c;
    }

    public void actionPerformed(ActionEvent e) {
    	if(e.getActionCommand().equals("OK")) {
    	  color = chooser.getColor();
    	}else color = null;
    }

    public Color getColor() {
        return color;
    }
}
