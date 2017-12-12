package de.hsb.algo.ass1.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;



public class Line implements Shape{
	
	int x,y,x2,y2;
	Color w,e;
	
	public Line(int nx, int ny, int nx2, int ny2, Color w, Color e){
		x = nx;
		y = ny;
		x2 = nx2;
		y2 = ny2;
		this.w = w;
		this.e = e;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int nx) {
		x = nx;
	}

	public void setY(int ny) {
		y = ny;
	}
	
	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	public void setX2(int nx) {
		x2 = nx;
	}

	public void setY2(int ny) {
		y2 = ny;
	}

	public void draw(Graphics g) {
		Bresenham.drawLine(g, new Point(x,y), new Point(x2,y2), w.getRGB(), e.getRGB());
	}
}
