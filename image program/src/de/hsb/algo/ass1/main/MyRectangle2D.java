package de.hsb.algo.ass1.main;

import java.awt.geom.Rectangle2D;

public class MyRectangle2D extends Rectangle2D.Float{
	public MyRectangle2D(float x, float y, float width, float height){
		setRect(x, y, width, height);
	}
	
	public boolean isInside(float x, float y){
		return getBounds2D().contains(x, y);
	}
	
	public void addX(float x){
		this.x += x;
	}
	
	public void addY(float y){
		this.y += y;
	}
	
	public void reset(){
		this.x=0;
		this.y=0;
		this.width=0;
		this.height=0;
	}
	
	public void addWidth(float w){
		this.width += w;
	}
	
	public void addHeight(float h){
		this.height += h;
	}
}
