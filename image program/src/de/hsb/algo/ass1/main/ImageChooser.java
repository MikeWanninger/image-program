package de.hsb.algo.ass1.main;

import java.awt.Image;
import java.awt.MediaTracker;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageChooser extends JFrame {
	
	Image image;
	
	public Image bildAuswahl(){
		JFileChooser fileChooser = new JFileChooser(new File("./images/"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Pictures", "jpg", "gif", "png", "bmp");
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setFileFilter(filter);
		fileChooser.showOpenDialog(this);
		try {
			image = ImageIO.read(fileChooser.getSelectedFile());
		} catch (IOException e2) {
		}
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(image, 0);
		try {
			mt.waitForAll();
		} catch (InterruptedException e1) {
		}
		
		return image;
	}
	
		public String ordnerAuswahl(){
		JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showOpenDialog(null);
        File f = null;
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            f = fc.getSelectedFile();
            System.out.println(f.getPath());
        }
        return f.getPath();
	}
}
