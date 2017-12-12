package de.hsb.algo.ass2.main;

public class Point3D{

	public int x,y,z;
	
	public Point3D(){
		x=0;
		y=0;
		z=0;
	}
	
	public Point3D(int x,int y,int z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public Point3D(int x, int y){
		this.x=x;
		this.y=y;
		z=0;
	}
	
	public double distance(Point3D point){
		return Math.abs(Math.sqrt(Math.pow(Math.abs(x-point.x), 2)+Math.pow(Math.abs(y-point.y), 2)+Math.pow(Math.abs(z-point.z), 2)));
	}
	
	@Override
	public String toString(){
		return "("+x+","+y+","+z+")";
	}
}
