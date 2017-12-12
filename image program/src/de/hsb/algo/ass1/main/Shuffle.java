package de.hsb.algo.ass1.main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


class Shuffle extends Component {
	final int W = 800;
	final int H = 600;
	Image[] m_Images;
	Image m_Img, m_Img1, m_Img2;
	int[][] m_ImgPix;
	int[] m_Pix = new int[W * H];
	MemoryImageSource m_ImgSrc;
	int counter = 0;
	private int[] m_Img1Pix = new int[W*H];
	private int[] m_Img2Pix = new int[W*H];
	PixelGrabber[] pxGrabber;

	public Shuffle(Frame father, String imageFolderPath) {
		try {
			File folder = new File(imageFolderPath);
			String[] images = listFilesInFolder(folder);
			m_Images = new Image[images.length];
			m_ImgPix = new int[images.length][W * H];
			for (int i = 0; i < images.length; ++i) {
				System.out.println(images[i]);
				m_Images[i] = getToolkit().getImage(images[i])
						.getScaledInstance(W, H, Image.SCALE_SMOOTH);
			}
			
			MediaTracker mt = new MediaTracker(this);
			for (int i = 0; i < m_Images.length; ++i) {
				mt.addImage(m_Images[i], 0);
			}
			mt.waitForAll();
			pxGrabber = new PixelGrabber[m_Images.length];
			for(int i = 0; i<pxGrabber.length; ++i){
				pxGrabber[i] = new PixelGrabber(m_Images[i],0,0,W,H,m_ImgPix[i],0,W);
				pxGrabber[i].grabPixels();
			}
			
			m_ImgSrc = new MemoryImageSource(W, H, m_Pix, 0, W);
			m_ImgSrc.setAnimated(true);
			m_Img = createImage(m_ImgSrc);
		} catch (InterruptedException e) {
		}
	}

	public void paint(Graphics g) {
		g.drawImage(m_Img, 0, 0, getWidth(), getHeight(), this);
	}

	public Dimension getPreferredSize() {
		return getMinimumSize();
	}

	public Dimension getMinimumSize() {
		return new Dimension(W, H);
	}

	private int compColor(int x1, int x2, int p) {
		return x1 + (x2 - x1) * p / 100;
	}

	private int compPix(int pix1, int pix2, int p) {
		final int RED = compColor((pix1 >> 16) & 0xff, (pix2 >> 16) & 0xff, p);
		final int GREEN = compColor((pix1 >> 8) & 0xff, (pix2 >> 8) & 0xff, p);
		final int BLUE = compColor(pix1 & 0xff, pix2 & 0xff, p);
		return 0xff000000 | (RED << 16) | (GREEN << 8) | BLUE;
	}

	public void shuffle(int p, int counter) {
		for (int i = 0; i < W * H; ++i) {
			m_Pix[i] = compPix(m_ImgPix[counter%m_ImgPix.length][i],
					m_ImgPix[(counter+1)%m_ImgPix.length][i], p);
		}	
		m_ImgSrc.newPixels();
		repaint();
	}
	
	public Image getImage(){
		return m_Img;
	}

	public String[] listFilesInFolder(final File folder) {
		ArrayList<String> images = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			if (!fileEntry.isDirectory()) {
				images.add(fileEntry.getAbsolutePath());
			}
		}
		String[] tmp = new String[images.size()];
		for (int i = 0; i < tmp.length; ++i) {
			tmp[i] = images.get(i);
		}
		return tmp;
	}
}