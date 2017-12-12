package de.hsb.algo.ass1.main;

import java.awt.Color;
import java.awt.Graphics;



public class Circle implements Shape{
	int x,y,r;
	Color w,e;
	boolean filled;
	public Circle(int nx, int ny, int nr, Color w, Color e, boolean filled){
		x = nx;
		y = ny;
		r = nr;
		this.w = w;
		this.e = e;
		this.filled = filled;
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
	public int getR(){
		return r;
	}
	public void setR(int nr){
		r = nr;
	}
	public void draw(Graphics g) {
		Bresenham.drawCircle(g, x, y, r, w, e, filled);
	}
}
