package de.hsb.algo.ass1.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

public class Bresenham {
	public static void drawCircle(Graphics g, int x0, int y0, int r, Color CW,
			Color CE, boolean filled) {
		int y = 0;
		int x = r;
		int F = -r;
		int dy = 1;
		int dyx = -2 * r + 3;
		while (y <= x) {
			setPixel(g, x0, y0, x, y, CW, CE, r, filled);
			++y;
			dy += 2;
			dyx += 2;
			if (F > 0) {
				F += dyx;
				--x;
				dyx += 2;
			} else {
				F += dy;
			}
		}
	}

	private static void setPixel(Graphics g, int x0, int y0, int x, int y,
			Color CE, Color CW, int r, boolean filled) {

		Point linksOben = new Point(x0 - y, y0 - x);
		Point rechtsOben = new Point(x0 + y, y0 - x);
		Point linksUnten = new Point(x0 - y, y0 + x);
		Point rechtsUnten = new Point(x0 + y, y0 + x);
		Point mitteLinksOben = new Point(x0 - x, y0 - y);
		Point mitteRechtsOben = new Point(x0 + x, y0 - y);
		Point mitteLinksUnten = new Point(x0 - x, y0 + y);
		Point mitteRechtsUnten = new Point(x0 + x, y0 + y);

		int start =CE.getRGB();
		int end =CW.getRGB();
		
		if(filled){
			drawLine(g, mitteLinksOben, mitteRechtsOben, start, end);
			drawLine(g, mitteLinksUnten, mitteRechtsUnten, start, end);
			drawLine(g, linksUnten, rechtsUnten, start, end);
			drawLine(g, linksOben, rechtsOben, start, end);
		}else{
			drawLine(g, mitteLinksOben, mitteLinksOben, start, end);
			drawLine(g, mitteLinksUnten, mitteLinksUnten, start, end);
			drawLine(g, linksUnten, linksUnten, start, end);
			drawLine(g, linksOben, linksOben, start, end);
			drawLine(g, mitteRechtsOben, mitteRechtsOben, start, end);
			drawLine(g, mitteRechtsUnten, mitteRechtsUnten, start, end);
			drawLine(g, rechtsUnten, rechtsUnten, start, end);
			drawLine(g, rechtsOben, rechtsOben, start, end);
		}
	}

	public static void drawLine(Graphics g, Point start, Point end, int cStart,
			int cEnd) {
		final int dx = Math.abs(start.x - end.x);
		final int dy = Math.abs(start.y - end.y);
		final int sgnDx = start.x < end.x ? 1 : -1;
		final int sgnDy = start.y < end.y ? 1 : -1;
		int shortD, longD, incXshort, incXlong, incYshort, incYlong;
		if (dx > dy) {
			shortD = dy;
			longD = dx;
			incXlong = sgnDx;
			incXshort = 0;
			incYlong = 0;
			incYshort = sgnDy;
		} else {
			shortD = dx;
			longD = dy;
			incXlong = 0;
			incXshort = sgnDx;
			incYlong = sgnDy;
			incYshort = 0;
		}
		int d = longD / 2, x = start.x, y = start.y;

		double fullLength = Point2D.distance(start.x, start.y, end.x, end.y);

		for (int i = 0; i <= longD; ++i) {
			double part = Point2D.distance(start.x, start.y, x, y);
			double tempP = (part / fullLength) * 100;
			g.setColor(new Color(colorShuffle(cStart, cEnd, (int) tempP)));
			g.drawLine(x, y, x, y);
			x += incXlong;
			y += incYlong;
			d += shortD;
			if (d >= longD) {
				d -= longD;
				x += incXshort;
				y += incYshort;
			}
		}
	}


	private static int colorShuffle(int i1, int i2, int p) {
		int red = singleShuffle((i1 >> 16) & 255, (i2 >> 16) & 255, p);
		int green = singleShuffle((i1 >> 8) & 255, (i2 >> 8) & 255, p);
		int blue = singleShuffle((i1) & 255, (i2) & 255, p);
		return (255 << 24) | (red << 16) | (green << 8) | blue;
	}


	private static int singleShuffle(int i1_part, int i2_part, int p) {
		return i1_part + (i2_part - i1_part) * p / 100;
	}
}
