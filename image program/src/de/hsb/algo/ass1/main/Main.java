package de.hsb.algo.ass1.main;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;



public class Main extends JFrame{
	Shuffle s;
	int counter = 0;

	public Main(String path) {
		super("Cool, shuffling ...");
		s = new Shuffle(this, path);
		setLayout(new BorderLayout());
		add(s, BorderLayout.CENTER);
		
		SlideShow slideshow = new SlideShow(s);
		
		add(new ControlPanel(slideshow), BorderLayout.WEST);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		pack();
		setVisible(true);
		Thread t = new Thread(slideshow);
		t.start();
	}

	public void update(Graphics g) {
		paint(g);
	}

	public static void main(String[] args) {
		new Main(new ImageChooser().ordnerAuswahl());
	}
}
