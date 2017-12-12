package de.hsb.algo.ass1.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;

public class Editor extends JFrame {

	public enum Functions {
		scheren, rotieren, skalieren
	}

	Point mousePosition, imagePosition = new Point(0, 0);
	Functions selectedFunction;
	MemoryImageSource m_ImgSrc;
	MemoryImageSource m_ImgSrcPrev;
	int[] m_pix, m_pix_ori, m_pix_newImg;
	Image imgResult;
	Image imgPrev;
	Matrix m;
	int w = 800, h = 600;
	JSlider sliderX;
	JSlider sliderY;
	JButton btn;
	TransMouseControl emc;
	MyRectangle2D editField = null;
	JFrame frame = this;
	JCheckBoxMenuItem otherImg;
	JCheckBoxMenuItem line;
	JCheckBoxMenuItem circle;
	JCheckBoxMenuItem filledcircle;
	Editor editor;
	Preview preview;
	Graphics2D g2d;
	Graphics g2dPrev;
	int lPosX;
	int lPosY;
	int lPosX2;
	int lPosY2;
	int cPosX;
	int cPosY;
	int cR;
	Color cWest = Color.RED;
	Color cEast = Color.BLUE;
	ArrayList<Shape> shapeList = new ArrayList<Shape>();
	
	public Editor(Image img) {
		// -----------------------------------------------------------------------------
		// FrameStuff
		// -----------------------------------------------------------------------------
		setSize(w, h);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		editor = this;
		Image image = copyImage(img);

		m_pix = new int[w * h];
		m_pix_ori = new int[w * h];
		PixelGrabber p = new PixelGrabber(image, 0, 0, w, h, m_pix_ori, 0, w);
		try {
			p.grabPixels();
		} catch (Exception e) {

		}
		for (int i = 0; i < m_pix.length; ++i)
			m_pix[i] = m_pix_ori[i];
		m_ImgSrc = new MemoryImageSource(w, h, m_pix, 0, w);
		m_ImgSrc.setAnimated(true);
		imgResult = createImage(m_ImgSrc);
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("menu");

		JRadioButtonMenuItem scheren = new JRadioButtonMenuItem("scheren");
		JRadioButtonMenuItem rotieren = new JRadioButtonMenuItem("rotieren");
		JRadioButtonMenuItem skalieren = new JRadioButtonMenuItem("skalieren");
		line = new JCheckBoxMenuItem("Linie");
		circle = new JCheckBoxMenuItem("Kreis unausgefuellt");
		filledcircle = new JCheckBoxMenuItem("Kreis gefuellt");
		otherImg = new JCheckBoxMenuItem("Bild auswuehlen");
		JCheckBoxMenuItem editRec = new JCheckBoxMenuItem("transform");

		final ButtonGroup group = new ButtonGroup();

		group.add(scheren);
		group.add(rotieren);
		group.add(skalieren);
		
		final ButtonGroup groupGeom = new ButtonGroup();
		groupGeom.add(line);
		groupGeom.add(circle);
		groupGeom.add(filledcircle);
		groupGeom.add(editRec);
		editRec.setSelected(true);

		rotieren.setSelected(true);
		selectedFunction = Functions.rotieren;

		menu.add(scheren);
		menu.add(rotieren);
		menu.add(skalieren);
		menu.add(otherImg);
		menu.add(line);
		menu.add(circle);
		menu.add(filledcircle);
		menu.add(editRec);
		
		menuBar.add(menu);
		
		JMenu colorWest = new JMenu("Farbe Westen");
		JMenu colorEast = new JMenu("Farbe Osten");
		
		JCheckBoxMenuItem wBlack = new JCheckBoxMenuItem("schwarz");
		wBlack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cWest = Color.BLACK;
			}
		});
		JCheckBoxMenuItem wWhite = new JCheckBoxMenuItem("wei�");
		wWhite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cWest = Color.WHITE;
			}
		});
		JCheckBoxMenuItem wBlue = new JCheckBoxMenuItem("blau");
		wBlue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cWest = Color.BLUE;
			}
		});
		JCheckBoxMenuItem wGreen = new JCheckBoxMenuItem("gr�n");
		wGreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cWest = Color.GREEN;
			}
		});
		JCheckBoxMenuItem wYellow = new JCheckBoxMenuItem("gelb");
		wYellow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cWest = Color.YELLOW;
			}
		});
		JCheckBoxMenuItem wRed = new JCheckBoxMenuItem("rot");
		wRed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cWest = Color.RED;
			}
		});
		
		
		
		JCheckBoxMenuItem eBlack = new JCheckBoxMenuItem("schwarz");
		eBlack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cEast = Color.BLACK;
			}
		});
		JCheckBoxMenuItem eWhite = new JCheckBoxMenuItem("wei�");
		eWhite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cEast = Color.WHITE;
			}
		});
		JCheckBoxMenuItem eBlue = new JCheckBoxMenuItem("blau");
		eBlue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cEast = Color.BLUE;
			}
		});
		JCheckBoxMenuItem eGreen = new JCheckBoxMenuItem("gr�n");
		eGreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cEast = Color.GREEN;
			}
		});
		JCheckBoxMenuItem eYellow = new JCheckBoxMenuItem("gelb");
		eYellow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cEast = Color.YELLOW;
			}
		});
		JCheckBoxMenuItem eRed = new JCheckBoxMenuItem("rot");
		eRed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cEast = Color.RED;
			}
		});
		
		final ButtonGroup cWestGroup = new ButtonGroup();
		cWestGroup.add(wBlack);
		cWestGroup.add(wWhite);
		cWestGroup.add(wBlue);
		cWestGroup.add(wGreen);
		cWestGroup.add(wYellow);
		cWestGroup.add(wRed);
		
		colorWest.add(wBlack);
		colorWest.add(wWhite);
		colorWest.add(wBlue);
		colorWest.add(wGreen);
		colorWest.add(wYellow);
		colorWest.add(wRed);
		
		final ButtonGroup cEastGroup = new ButtonGroup();
		cEastGroup.add(eBlack);
		cEastGroup.add(eWhite);
		cEastGroup.add(eBlue);
		cEastGroup.add(eGreen);
		cEastGroup.add(eYellow);
		cEastGroup.add(eRed);
		
		wRed.setSelected(true);
		eBlue.setSelected(true);
		
		colorEast.add(eBlack);
		colorEast.add(eWhite);
		colorEast.add(eBlue);
		colorEast.add(eGreen);
		colorEast.add(eYellow);
		colorEast.add(eRed);
		

		menuBar.add(colorWest);
		menuBar.add(colorEast);
		setJMenuBar(menuBar);

		sliderX = new JSlider(0, 361, 180);
		sliderX.setPaintLabels(true);
		sliderX.setPaintTicks(true);
		sliderX.setMinorTickSpacing(10);
		sliderX.setMajorTickSpacing(20);
		sliderX.setSnapToTicks(true);

		sliderY = new JSlider(0, 361, 180);
		sliderY.setPaintLabels(true);
		sliderY.setPaintTicks(true);
		sliderY.setMinorTickSpacing(10);
		sliderY.setMajorTickSpacing(20);
		sliderY.setSnapToTicks(true);

		if (selectedFunction == Functions.rotieren) {
			sliderY.setVisible(false);
		}

		btn = new JButton("rotate");
		add(new JPanel() {
			{
				setLayout(new BorderLayout());
				add(new JPanel() {
					{
						setLayout(new GridLayout(2, 1));
						add(sliderX);
						add(sliderY);
					}
				}, BorderLayout.CENTER);
				add(btn, BorderLayout.EAST);
			}
		}, BorderLayout.SOUTH);

		editField = new MyRectangle2D(0, 0, 0, 0);

		add(new EditPanel(new TransMouseControl(frame)), BorderLayout.CENTER);

		pack();
		setVisible(true);

		// -----------------------------------------------------------------------------
		// Listener
		// -----------------------------------------------------------------------------
		// addMouseListener(new MouseAdapter() {
		// @Override
		// public void mouseClicked(MouseEvent e) {
		//
		// }
		// });
		//
		// addMouseMotionListener(new MouseMotionAdapter() {
		// @Override
		// public void mouseMoved(MouseEvent e) {
		// if (Aufgabe01.this.getMousePosition().x < Aufgabe01.this
		// .getWidth()
		// && Aufgabe01.this.getMousePosition().y < Aufgabe01.this
		// .getHeight()) {
		// Aufgabe01.this.mousePosition = Aufgabe01.this
		// .getMousePosition();
		// }
		// }
		// });

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				dispose();
			}
		});

		scheren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Editor.this.selectedFunction = Functions.scheren;
				btn.setText("scheren");
				sliderX.setMinorTickSpacing(10);
				sliderX.setMajorTickSpacing(20);
				sliderX.setMaximum(100);

				sliderY.setMinorTickSpacing(10);
				sliderY.setMajorTickSpacing(20);
				sliderY.setMaximum(100);
				sliderY.setVisible(true);
			}
		});

		skalieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Editor.this.selectedFunction = Functions.skalieren;
				btn.setText("skalieren");
				sliderX.setMinorTickSpacing(1);
				sliderX.setMajorTickSpacing(2);
				sliderX.setMaximum(10);

				sliderY.setMinorTickSpacing(1);
				sliderY.setMajorTickSpacing(2);
				sliderY.setMaximum(10);
				sliderY.setVisible(true);
			}
		});

		rotieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Editor.this.selectedFunction = Functions.rotieren;
				btn.setText("rotieren");
				sliderX.setMinorTickSpacing(10);
				sliderX.setMajorTickSpacing(20);
				sliderX.setMaximum(360);

				sliderY.setMinorTickSpacing(10);
				sliderY.setMajorTickSpacing(20);
				sliderY.setMaximum(360);
				sliderY.setVisible(false);
			}
		});

		btn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				switch (selectedFunction) {
				case scheren:
					newMatrix(Matrix.translation(-(int) editField.x,
							-(int) editField.y));
					newMatrix(Matrix.multiplyMM(
							Matrix.shearX(sliderX.getValue() / 100.0),
							Matrix.shearY(sliderY.getValue() / 100.0)));
					newMatrix(Matrix.translation((int) editField.x,
							(int) editField.y));
					transformImage(m);

					break;
				case rotieren:
					newMatrix(Matrix.translation(-(int) editField.x,
							-(int) editField.y));
					newMatrix(Matrix.rotate(Math.toRadians(sliderX.getValue())));
					newMatrix(Matrix.translation((int) editField.x,
							(int) editField.y));
					imagePosition = new Point((int) editField.x,
							(int) editField.y);
					transformImage(m);

					break;
				case skalieren:
					newMatrix(Matrix.translation(-(int) editField.x,
							-(int) editField.y));
					newMatrix(Matrix.scale(2, 2));
					newMatrix(Matrix.translation((int) editField.x,
							(int) editField.y));
					imagePosition = new Point((int) editField.x,
							(int) editField.y);
					transformImage(m);
					break;
				}
			}
		});
		otherImg.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(otherImg.isSelected()){
					m_pix_newImg = new int[w*h];
					Image img = new ImageChooser().bildAuswahl().getScaledInstance(w, h, Image.SCALE_SMOOTH);
					try {
						MediaTracker mt = new MediaTracker(preview);
						mt.waitForAll();
						PixelGrabber pxGrab = new PixelGrabber(img, 0, 0, w, w, m_pix_newImg, 0, w);
						pxGrab.grabPixels();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					
					m_ImgSrcPrev = new MemoryImageSource(w, h, m_pix_newImg, 0, w);
					m_ImgSrcPrev.setAnimated(true);
					imgPrev = createImage(m_ImgSrcPrev);
					preview = new Preview();
				}else{
					preview.dispose();
				}
			}
		});
	}
	
	
	
	// -----------------------------------------------------------------------------
	// Methoden
	// -----------------------------------------------------------------------------
	void newMatrix(Matrix a) {
		if (m != null)
			m = Matrix.multiplyMM(m, a);
		else
			m = a;
	}

	public void transformImage(Matrix trans) {
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				Vector k = new Vector(i, j);
				Vector v = Matrix.multiplyMV(trans, k);
				// m_pix[w * j + i] = 0xffffffff;
				if (v.x() >= editField.x && v.y() >= editField.y
						&& v.x() < editField.x + editField.width
						&& v.y() < editField.y + editField.height) {
					m_pix[w * j + i] = m_pix_ori[w * v.y() + v.x()];
					if(otherImg.isSelected()){
						m_pix_newImg[w*j+i] = m_pix_ori[w*v.y()+v.x()];
					}
				}
			}
		}
		if(otherImg.isSelected()){
			m_ImgSrcPrev.newPixels();
			preview.repaint();
		}
		m_ImgSrc.newPixels();
		repaint();
	}

	public void drawEditField(Graphics2D g2) {
		if (editField != null) {
			g2.setColor(new Color(0, 0, 200, 200));
			g2.draw(editField);
			g2.setColor(new Color(0, 0, 200, 50));
			g2.fill(editField);
		}
	}

	public void translate(MyRectangle2D rec, int dx, int dy) {
		for (int x = 0, y = -1; x < m_pix.length; ++x) {
			if (x % w == 0) {
				y++;
			}
			if (x % w >= rec.x && y >= rec.y && x % w <= rec.x + rec.width
					&& y <= rec.y + rec.height) {
				m_pix[(x + dy * w + dx)%m_pix.length] = m_pix[x];
				if(otherImg.isSelected()){
					m_pix_newImg[(x+dy*w+dx)%m_pix.length] = m_pix[x];
				}
			}
		}
		if(otherImg.isSelected()){
			m_ImgSrcPrev.newPixels();
			preview.repaint();
		}
		m=null;
		m_ImgSrc.newPixels();
		frame.repaint();
	}
	
	public Image getPrevImage(){
		return imgPrev;
	}

	public Image copyImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		Image bimage = new BufferedImage(img.getWidth(null),
				img.getHeight(null), Image.SCALE_SMOOTH);

		// Draw the image on to the buffered image
		Graphics2D bGr = (Graphics2D) bimage.getGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}
	
	public BufferedImage toRenderedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null),
				img.getHeight(null), Image.SCALE_SMOOTH);

		// Draw the image on to the buffered image
		Graphics2D bGr = (Graphics2D) bimage.getGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

	// -----------------------------------------------------------------------------
	// Mouselistener
	// -----------------------------------------------------------------------------
	class TransMouseControl extends MouseAdapter {
		int x, y;
		boolean moveable = false;
		MyRectangle2D tmp = new MyRectangle2D(0, 0, 0, 0);
		JFrame f;

		public TransMouseControl(JFrame f) {
			this.f = f;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			x = e.getX();
			y = e.getY();
			if (!editField.isInside(x, y)) {
				editField.reset();
				moveable = false;
				m = null;
			}
			if (!moveable) {
				editField.x = x;
				editField.y = y;
				tmp.x = x;
				tmp.y = y;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (!moveable) {
				editField.width = e.getX() - x;
				editField.height = e.getY() - y;
				tmp.width = e.getX() - x;
				tmp.height = e.getY() - y;
			} else {
				if(!line.isSelected() && !circle.isSelected() && !filledcircle.isSelected()){
					translate(tmp, (int) (editField.getX()-tmp.getX()),
							(int) (editField.getY()-tmp.getY()));
				}
			}
			if(line.isSelected()){
				lPosX = (int)(editField.x);
				lPosY = (int)(editField.y);
				lPosX2 = (int)(editField.x+editField.width);
				lPosY2 = (int)(editField.y+editField.height);
				shapeList.add(new Line(lPosX, lPosY, lPosX2, lPosY2,cWest,cEast));
				
			}else if(circle.isSelected() || filledcircle.isSelected()){
				cPosX = (int)(editField.x + (editField.width*0.5));
				cPosY = (int)(editField.y + (editField.height*0.5));
				if(editField.width > editField.height)
					cR = (int)(editField.width/2);
				else
					cR = (int)(editField.height/2);
				if(filledcircle.isSelected())
					shapeList.add(new Circle(cPosX, cPosY, cR, cWest, cEast, true));
				else
					shapeList.add(new Circle(cPosX, cPosY, cR, cWest, cEast, false));
			}
			if(preview != null)
				preview.repaint();
			moveable = true;
			sliderX.setEnabled(true);
			f.repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (!moveable) {
				editField.width = e.getX() - x;
				editField.height = e.getY() - y;
				tmp.width = e.getX() - x;
				tmp.height = e.getY() - y;
				if(line.isSelected()){
					lPosX = (int)(editField.x);
					lPosY = (int)(editField.y);
					lPosX2 = (int)(editField.x+editField.width);
					lPosY2 = (int)(editField.y+editField.height);
					
				}else if(circle.isSelected() || filledcircle.isSelected()){
					cPosX = (int)(editField.x + (editField.width*0.5));
					cPosY = (int)(editField.y + (editField.height*0.5));
					if(editField.width > editField.height)
						cR = (int)(editField.width/2);
					else
						cR = (int)(editField.height/2);
				}
				if(preview != null)
					preview.repaint();
				f.repaint();
				
			} else {
				if(!line.isSelected() && !circle.isSelected()){
					int dx = e.getX() - x;
					int dy = e.getY() - y;
					if (editField.isInside(x, y)) {
						editField.addX(dx);
						editField.addY(dy);
						f.repaint();
					}
					x += dx;
					y += dy;
				}
			}
		}

	}

	class EditPanel extends JPanel {

		TransMouseControl emc;

		public EditPanel(TransMouseControl emc) {
			this.emc = emc;
			addMouseListener(emc);
			addMouseMotionListener(emc);
			setFocusable(true);
			setMinimumSize(new Dimension(w, h));
			setPreferredSize(getMinimumSize());
		}

		@Override
		public void paintComponent(Graphics g) {
			g2d = (Graphics2D) g;
			g2d.drawImage(imgResult, 0, 0, getWidth(), getHeight(), this);
			if(!line.isSelected() && !circle.isSelected() && !filledcircle.isSelected())
				drawEditField(g2d);
			else if(line.isSelected())
				Bresenham.drawLine(g2d, new Point(lPosX,lPosY), new Point(lPosX2,lPosY2), cWest.getRGB(), cEast.getRGB());
			else if(circle.isSelected())
				Bresenham.drawCircle(g2d, cPosX, cPosY, cR, cWest, cEast, false);
			else if(filledcircle.isSelected())
				Bresenham.drawCircle(g2d, cPosX, cPosY, cR, cWest, cEast, true);
			if(shapeList.size() > 0)
				for(int i=0;i<shapeList.size();++i){
					shapeList.get(i).draw(g2d);
				}
		}
	}
	
	class Preview extends JFrame{
		Preview prev = this;
		public Preview(){
			setLayout(new BorderLayout());
			add(new JComponent() {
				{
					setPreferredSize(new Dimension(w, h));
					setMinimumSize(getPreferredSize());
				}
				public void paintComponent(Graphics g){
					g2dPrev = (Graphics2D) g;
					g2dPrev.drawImage(imgPrev, 0, 0, getWidth(), getHeight(), null);
					if(line.isSelected()){
						Bresenham.drawLine(g2dPrev, new Point(lPosX,lPosY), new Point(lPosX2,lPosY2), cWest.getRGB(), cEast.getRGB());
					}
					else if(circle.isSelected()){
						if(filledcircle.isSelected())
							Bresenham.drawCircle(g2d, cPosX, cPosY, cR, cWest, cEast, true);
						else
							Bresenham.drawCircle(g2d, cPosX, cPosY, cR, cWest, cEast, false);
					}
					if(shapeList.size() > 0)
						for(int i=0;i<shapeList.size();++i){
							shapeList.get(i).draw(g2dPrev);
						}
				}
			});
			JMenuItem save = new JMenuItem("Bild speichern");
			JMenu menu = new JMenu("Option");
			menu.add(save);
			JMenuBar mnBar = new JMenuBar();
			mnBar.add(menu);
			setJMenuBar(mnBar);
			save.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser();
					File f = null;
					int receive = fc.showSaveDialog(prev);
					if(receive == JFileChooser.APPROVE_OPTION){
						f = fc.getSelectedFile();
					}
					BufferedImage img = toRenderedImage(imgPrev);
					try {
						ImageIO.write(img, "png", f);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			pack();
			setVisible(true);
		}
	}

}
