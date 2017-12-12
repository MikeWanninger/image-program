package de.hsb.algo.ass1.main;

import java.awt.Image;

public class SlideShow implements Runnable{
	int counter = 0;
	Shuffle shuffle;
	boolean run = true;
	
	public SlideShow(Shuffle shuffle){
		this.shuffle = shuffle;
	}
	
	public void start(boolean flag){
		run = flag;
	}
	
	public boolean getRunStatus(){
		return run;
	}
	
	public Shuffle getShuffle(){
		return shuffle;
	}
	
	public Image getImage(){
		return shuffle.getImage();
	}
	
	
	public void run() {
		try {
			while (true) {
				if(run){
					for (int i = 0; i <= 100; i += 2) {
						shuffle.shuffle(i, counter);
						Thread.sleep(50);
					}				
					counter++;
				}
				Thread.sleep(20);
			}
		} catch (InterruptedException e) {
		}
	}

}
