package de.hsb.algo.ass1.main;

import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;

public class ImageView extends Frame{
	
	Image img;
	int w,h;
	public ImageView() {
		img = Toolkit.getDefaultToolkit().getImage(
				"../algolernen/src/uebung03/aufgabe01/1.jpg");
		setVisible(true);
	}
	
	public void setImage(Image img){
		this.img = img;
		w=img.getWidth(this);
		h=img.getWidth(this);

		getGraphics().drawImage(img, 0, 0, w,h, this);
	}

}
