package com.honda.galc.client.dc.enumtype;

import javafx.scene.paint.Color;


public  enum ArcColor {
		TIMER 	  (Color.WHITESMOKE,Color.LAWNGREEN),
		ONTIME    (Color.HONEYDEW,Color.LAWNGREEN),
		DELAYED   (Color.LEMONCHIFFON,Color.GOLD),
		LATE      (Color.LIGHTSALMON,Color.RED);
		
		private final Color backgroundColor;
		private final Color foregroundColor;
		
		ArcColor(Color bg, Color fg) {
			this.backgroundColor = bg;
			this.foregroundColor = fg;
		}
		
		public Color bg() {
			return backgroundColor;
		}

		public Color fg() {
			return foregroundColor;
		}
};

