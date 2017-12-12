package de.hsb.algo.ass2.main;

import javafx.stage.FileChooser;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.io.FileFilter;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FileChooserUI;

public class Approximation extends JFrame{
	final int w = 800;
	final int h = 700;
	
	JComponent approxComp;
	JFrame f;
	Image img;
	
	public Approximation() throws Exception {
		f = this;
		setLayout(new BorderLayout());
		JPanel pane = new JPanel(new GridLayout(2,1));
		add(pane, BorderLayout.CENTER);
		setSize(w, h);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});


		// ---------------------------------------------------
		// musste den Teil schnell neu schreiben, weil eine Datei nicht im Projekt war. gibt definitv schönere Lösungen
		// final Image image = new Dateiauswahl().bildAuswahl();
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.showOpenDialog(null);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png"));
		final Image image = ImageIO.read(fileChooser.getSelectedFile());

		if(image == null) {
			System.exit(0);
		}
		// ----------------------------------------------------

		MediaTracker mt = new MediaTracker(this);
		mt.addImage(image, 0);
		mt.waitForAll();

		pane.add(new JComponent() {
			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
			}
		});
		
		setVisible(true);
		
		final ImageColorReduction icr = new ImageColorReduction();
		img = image;
		
		pane.add(approxComp = new JComponent() {
			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
			}
		});
		
		final JSlider slider = new JSlider(0, 100);
		slider.setMinorTickSpacing(10);
		slider.setMajorTickSpacing(20);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				try {
					System.out.println("start approximation");
					slider.setEnabled(false);
					img = icr.imageColorReduction(image, (1-((float)slider.getValue()/100)), f);
					slider.setEnabled(true);
					repaint();
					System.out.println("finished");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		add(slider,BorderLayout.SOUTH);
		
		repaint();
	}
}
