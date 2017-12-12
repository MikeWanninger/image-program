package de.hsb.algo.ass2.main;

import java.util.Vector;

public class ColorInfoObject {
	public int color, sub_col;
	public Vector<Integer> occurence = new Vector<Integer>();

	public ColorInfoObject(int color) {
		this.color = color;
		this.sub_col = color;
	}
}
