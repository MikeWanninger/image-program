package de.hsb.algo.ass1.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;


public class Histogramm extends Component{
	
	BufferedImage img;
	int[][][] rgbValues;
	int[] commonValues;
	int imgPix = 0;
	int yOffset = 200;
	int width = 640, height = width/19*6;
	BufferedImage tmpImage = new BufferedImage(width, height*4, BufferedImage.TYPE_INT_ARGB);
	Graphics graphics;
	static Histogramm currentHistogramm;

	public Histogramm(Image img) {
		this.img = toBufferedImage(img);
		rgbValues = new int[this.img.getWidth()][this.img.getHeight()][3];
		commonValues = new int[256*3];
		imgPix = this.img.getWidth() * this.img.getHeight();
		for(int i=0;i<this.img.getWidth();++i){
			for(int j=0;j<this.img.getHeight(); ++j){
				int rgb = this.img.getRGB(i, j);
				Color col = new Color(rgb);
				rgbValues[i][j][0] = col.getRed();
				rgbValues[i][j][1] = col.getGreen();
				rgbValues[i][j][2] = col.getBlue();
				
				commonValues[rgbValues[i][j][0]]++;
				commonValues[rgbValues[i][j][1]+256]++;
				commonValues[rgbValues[i][j][2]+(256*2)]++;
			}
		}
	}
	
	public Dimension getPreferredSize() {
		return getMinimumSize();
	}

	public Dimension getMinimumSize() {
		return new Dimension(width, height*4);
	}
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    return bimage;
	}
	
	
	public static Histogramm getCurrentHistogramm() {
		return currentHistogramm;
	}

	public static void setCurrentHistogramm(Histogramm Histogramm) {
		currentHistogramm = Histogramm;
	}
}
