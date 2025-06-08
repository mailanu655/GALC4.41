package com.honda.galc.client.teamleader.qics.twopartpair;

import java.awt.Color;
import java.awt.Graphics;


/** * *
* @version 1
* @author Gangadhararao Gadde
* @since Jan 15,2015
*/
public class DrawnButton extends javax.swing.JButton {

	private static final long serialVersionUID = 1L;

	private	int BUTTON_MODE				=	0;

	public	final static int UP_MODE	=	0;

	public	final static int DOWN_MODE	=	1;

	public	final static int LEFT_MODE	=	2;

	public	final static int RIGHT_MODE	=	3;

	private int fieldButtomMode			=	0;

	private int fieldSetThickness		=	2;

	public DrawnButton() {
		super();
		initialize();
	}

	public DrawnButton(int mode) {
		super();
		BUTTON_MODE	=	mode;
		initialize();
	}

	public DrawnButton(String text) {
		super(text);
	}

	public DrawnButton(String text, javax.swing.Icon icon) {
		super(text, icon);
	}

	public DrawnButton(javax.swing.Icon icon) {
		super(icon);
	}

	public int getButtomMode() {
		return fieldButtomMode;
	}

	public int getSetThickness() {
		return fieldSetThickness;
	}

	private void handleException(Exception exception) {
        System.out.println("--------- UNCAUGHT EXCEPTION --------- by "+ getClass().getName());
		exception.printStackTrace(System.out);
	}

	private void initialize() {
		try {
			setName("DrawnButton");
			setSetThickness(2);
			setText("");
			setSize(50, 50);
		} catch (Exception e) {
			handleException(e);
		}
		this.repaint();
	}


	public void paint(Graphics g) {
		super.paint(g);
		if(fieldSetThickness <= 0) {
			return;
		}
		if(isEnabled()) {
			g.setColor(Color.black);
		} else {
			g.setColor(Color.lightGray);
		}
		int X	=	0;
		int Y	=	0;
		if(BUTTON_MODE==UP_MODE||BUTTON_MODE==DOWN_MODE) {
			X	=	getWidth()	/ 4;
			Y	=	(getHeight() / 5) - (fieldSetThickness / 4);
		} else {
			X	=	getWidth()	/ 5;
			Y	=	(getHeight() / 4) - (fieldSetThickness / 4) ;
		}
		for(int i = 0;i < fieldSetThickness;i++) {
			switch (BUTTON_MODE) {
			case UP_MODE : {
				g.drawLine(X * 1,Y * 2 + i,X * 2,Y * 1 + i);
				g.drawLine(X * 2,Y * 1 + i,X * 3,Y * 2 + i);
				g.drawLine(X * 1,Y * 3 + i,X * 2,Y * 2 + i);
				g.drawLine(X * 2,Y * 2 + i,X * 3,Y * 3 + i);
				g.drawLine(X * 1,Y * 4 + i,X * 2,Y * 3 + i);
				g.drawLine(X * 2,Y * 3 + i,X * 3,Y * 4 + i);
				break;
			}
			case DOWN_MODE : {
				g.drawLine(X * 1,Y * 1 + i,X * 2,Y * 2 + i);
				g.drawLine(X * 2,Y * 2 + i,X * 3,Y * 1 + i);
				g.drawLine(X * 1,Y * 2 + i,X * 2,Y * 3 + i);
				g.drawLine(X * 2,Y * 3 + i,X * 3,Y * 2 + i);
				g.drawLine(X * 1,Y * 3 + i,X * 2,Y * 4 + i);
				g.drawLine(X * 2,Y * 4 + i,X * 3,Y * 3 + i);
				break;
			}
			case LEFT_MODE : {
				g.drawLine(X * 1,Y * 2 + i,X * 2,Y * 1 + i);
				g.drawLine(X * 1,Y * 2 + i,X * 2,Y * 3 + i);
				g.drawLine(X * 2,Y * 2 + i,X * 3,Y * 1 + i);
				g.drawLine(X * 2,Y * 2 + i,X * 3,Y * 3 + i);
				g.drawLine(X * 3,Y * 2 + i,X * 4,Y * 1 + i);
				g.drawLine(X * 3,Y * 2 + i,X * 4,Y * 3 + i);
				break;
			}
			case RIGHT_MODE : {
				g.drawLine(X * 1,Y * 1 + i,X * 2,Y * 2 + i);
				g.drawLine(X * 1,Y * 3 + i,X * 2,Y * 2 + i);
				g.drawLine(X * 2,Y * 1 + i,X * 3,Y * 2 + i);
				g.drawLine(X * 2,Y * 3 + i,X * 3,Y * 2 + i);
				g.drawLine(X * 3,Y * 1 + i,X * 4,Y * 2 + i);
				g.drawLine(X * 3,Y * 3 + i,X * 4,Y * 2 + i);
				break;
			}
			default : {
				break;
			}
			}
		}
	}

	public void setButtomMode(int buttomMode) {
		BUTTON_MODE	=	buttomMode;
		int oldValue = fieldButtomMode;
		fieldButtomMode = buttomMode;
		firePropertyChange("buttomMode", new Integer(oldValue), new Integer(buttomMode));
	}

	public void setSetThickness(int setThickness) {
		int oldValue = fieldSetThickness;
		fieldSetThickness = setThickness;
		firePropertyChange("setThickness", new Integer(oldValue), new Integer(setThickness));
	}

	public void setText(String param) {}
}
