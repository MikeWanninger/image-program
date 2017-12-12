package de.hsb.algo.ass1.main;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.hsb.algo.ass2.main.Approximation;

public class ControlPanel extends Panel {

	Button btnRun = new Button("stop");
	Button btnHisto = new Button("Approximation");
	Button btnEditor = new Button("Editor");
	SlideShow slide;

	public ControlPanel(SlideShow slideshow) {
		slide = slideshow;
		setLayout(new GridLayout(3, 1));
		add(btnRun);
		add(btnHisto);
		add(btnEditor);
		btnRun.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				boolean flag = !slide.getRunStatus();
				slide.start(flag);
				btnHisto.setEnabled(!slide.getRunStatus());
				if (flag) {
					btnRun.setLabel("stop");
				} else {
					btnRun.setLabel("run");
				}
			}

		});

		btnHisto.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					new Approximation();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});

		btnEditor.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new Editor(slide.getShuffle().getImage());
			}
		});

	}
}