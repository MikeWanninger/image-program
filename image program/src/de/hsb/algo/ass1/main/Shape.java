package de.hsb.algo.ass1.main;

import java.awt.Graphics;

public interface Shape {
	public void draw(Graphics g);
	public int getX();
	public int getY();
	public void setX(int nx);
	public void setY(int ny);
}
