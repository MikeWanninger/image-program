package de.hsb.algo.ass2.main;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Image;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

public class ImageColorReduction {
	Point3D[] xSort, ySort, zSort, set;
	boolean found = false;
	public Image imageColorReduction(Image image, double reductionFactor,
			Frame owner) throws Exception {
		int w = image.getWidth(owner);
		int h = image.getHeight(owner);

		int[] pixArray = new int[w * h];

		PixelGrabber pxGrab = new PixelGrabber(image, 0, 0, w, h, pixArray, 0, w);
		pxGrab.grabPixels();

		HashMap<Integer, ColorInfoObject> colorHashMap = new HashMap<Integer, ColorInfoObject>();

		
		for (int i = 0; i < pixArray.length; ++i) {
			ColorInfoObject obj = colorHashMap.get(pixArray[i]);
			if (obj == null) {
				colorHashMap.put(pixArray[i], obj = new ColorInfoObject(
						pixArray[i]));
			}
			obj.occurence.add(i);
		}

		Vector<ColorInfoObject> occuranceSort = new Vector<ColorInfoObject>(
				colorHashMap.values());

		Collections.sort(occuranceSort, new Comparator<ColorInfoObject>() {
			public int compare(ColorInfoObject o1, ColorInfoObject o2) {
				Integer links = o1.occurence.size();
				Integer rechts = o2.occurence.size();
				return links.compareTo(rechts);
			}
		});
		final int CUT = (int) (occuranceSort.size() * reductionFactor);

		set = new Point3D[occuranceSort.size() - CUT + 1];

		for (int i = 0; i < set.length; ++i) {
			set[i] = new Point3D(
					(new Color(occuranceSort.get(i + CUT - 1).color)).getRed(),
					(new Color(occuranceSort.get(i + CUT - 1).color).getGreen()),
					(new Color(occuranceSort.get(i + CUT - 1).color).getBlue()));
		}

		java.util.Arrays.sort(set, new Comparator<Point3D>() {
			public int compare(Point3D o1, Point3D o2) {
				return ((Integer) (o1.x)).compareTo(o2.x);
			}
		});
		xSort = set.clone();

		java.util.Arrays.sort(set, new Comparator<Point3D>() {
			public int compare(Point3D o1, Point3D o2) {
				return ((Integer) (o1.y)).compareTo(o2.y);
			}
		});
		ySort = set.clone();

		java.util.Arrays.sort(set, new Comparator<Point3D>() {
			public int compare(Point3D o1, Point3D o2) {
				return ((Integer) (o1.z)).compareTo(o2.z);
			}
		});
		zSort = set.clone();

		for (int i = 0; i <= CUT; ++i) {

			Point3D toApproximate = new Point3D(new Color(
					occuranceSort.get(i).color).getRed(), new Color(
					occuranceSort.get(i).color).getGreen(), new Color(
					occuranceSort.get(i).color).getBlue());

			Point3D res = approximate3D(toApproximate);

			occuranceSort.get(i).color = (new Color(res.x, res.y, res.z))
					.getRGB();
		}

		int[] reducedPix = new int[w * h];
		for (int i = 0; i < occuranceSort.size(); ++i) {
			for (int j = 0; j < occuranceSort.get(i).occurence.size(); ++j) {
				reducedPix[occuranceSort.get(i).occurence.get(j)] = occuranceSort
						.get(i).color;
			}
		}

		MemoryImageSource m = new MemoryImageSource(w, h, reducedPix, 0, w);
		final Image img = owner.createImage(m);
		img.flush();
		return img;
	}

	// --------------------------------------------------------------------------------
	private Point3D approximate3D(Point3D target) {
		// binsearch bezueglich der X-Projektion
		double xRange, yRange, zRange;
		int[] xSorted = new int[xSort.length];

		for (int i = 0; i < xSort.length; i++) {
			xSorted[i] = xSort[i].x;
		}
		int middleX = binsearch(xSorted, target.x, 0,
				xSorted.length - 1);

		if (!found) {
			if (target.x < xSorted[middleX]) {
				double distanceRight = xSort[middleX].distance(target);
				if (middleX > 0) {
					double distanceLeft = xSort[middleX - 1]
							.distance(target);
					if (distanceLeft <= distanceRight) {
						xRange = distanceLeft;
						middleX = middleX - 1;
					} else {
						xRange = distanceRight;
					}
				} else {
					xRange = distanceRight;
				}
			} else {
				double distanceLeft = xSort[middleX].distance(target);

				if (middleX < xSorted.length - 1) {
					double distanceRight = xSort[middleX + 1]
							.distance(target);

					if (distanceLeft <= distanceRight) {
						xRange = distanceLeft;
					} else {
						xRange = distanceRight;
						middleX = middleX + 1;
					}
				} else {
					xRange = distanceLeft;
				}
			}
		} else {
			xRange = xSort[middleX].distance(target);
		}

		// binsearch bezueglich der Y-Projektion
		int[] ySorted = new int[ySort.length];

		for (int i = 0; i < ySort.length; i++) {
			ySorted[i] = ySort[i].y;
		}

		int middleY = binsearch(ySorted, target.y, 0,
				ySorted.length - 1);

		if (!found) {
			if (target.y < ySorted[middleY]) {
				double distanceRight = ySort[middleY].distance(target);
				if (middleY > 0) {
					double distanceLeft = ySort[middleY - 1]
							.distance(target);
					if (distanceLeft <= distanceRight) {
						yRange = distanceLeft;
						middleY = middleY - 1;
					} else {
						yRange = distanceRight;
					}
				} else {
					yRange = distanceRight;
				}
			} else {
				double distanceLeft = ySort[middleY].distance(target);

				if (middleY < ySorted.length - 1) {
					double distanceRight = ySort[middleY + 1]
							.distance(target);

					if (distanceLeft <= distanceRight) {
						yRange = distanceLeft;
					} else {
						yRange = distanceRight;
						middleY = middleY + 1;
					}
				} else {
					yRange = distanceLeft;
				}
			}
		} else {
			yRange = ySort[middleY].distance(target);
		}

		// binsearch bezueglich der Z-Projektion
		int[] zSorted = new int[zSort.length];

		for (int i = 0; i < zSort.length; i++) {
			zSorted[i] = zSort[i].z;
		}
		int middleZ = binsearch(zSorted, target.z, 0,
				zSorted.length - 1);

		if (!found) {
			if (target.z < xSorted[middleZ]) {
				double distanceRight = zSort[middleZ].distance(target);
				if (middleZ > 0) {
					double distanceLeft = zSort[middleZ - 1]
							.distance(target);
					if (distanceLeft <= distanceRight) {
						zRange = distanceLeft;
						middleZ = middleZ - 1;
					} else {
						zRange = distanceRight;
					}
				} else {
					zRange = distanceRight;
				}
			} else {
				double distanceLeft = zSort[middleZ].distance(target);

				if (middleZ < xSorted.length - 1) {
					double distanceRight = zSort[middleZ + 1]
							.distance(target);

					if (distanceLeft <= distanceRight) {
						zRange = distanceLeft;
					} else {
						zRange = distanceRight;
						middleZ = middleZ + 1;
					}
				} else {
					zRange = distanceLeft;
				}
			}
		} else {
			zRange = zSort[middleZ].distance(target);
		}

		double startRange = 0;
		Point3D nearestPoint = new Point3D();

		if (xRange < yRange) {
			startRange = xRange;
			nearestPoint.x = xSort[middleX].x;
			nearestPoint.y = xSort[middleX].y;
			nearestPoint.z = xSort[middleX].z;
		} else {
			startRange = yRange;
			nearestPoint.x = ySort[middleY].x;
			nearestPoint.y = ySort[middleY].y;
			nearestPoint.z = ySort[middleY].z;
		}
		if (zRange < startRange) {
			startRange = zRange;
			nearestPoint.x = zSort[middleZ].x;
			nearestPoint.y = zSort[middleZ].y;
			nearestPoint.z = zSort[middleZ].z;
		}

		double shortestRange = startRange;

		double xDist = shortestRange;
		// wir laufen im x array nach rechts
		for (int i = 1; (middleX + i) < xSort.length; ++i) {
			if (xSort[middleX + i].x <= (target.x + xDist)) {
				double distance = xSort[middleX + i].distance(target);
				if (distance < xDist) {
					xDist = distance;
					nearestPoint = new Point3D(xSort[middleX + i].x,
							xSort[middleX + i].y, xSort[middleX + i].z);
				}
			} else {
				break;
			}

		}

		// wir laufen im x array nach links
		for (int i = 0; (middleX - i) >= 0; ++i) {
			if (xSort[middleX - i].x >= (target.x - shortestRange)) {

				double distance = xSort[middleX - i].distance(target);

				if (distance < shortestRange) {
					shortestRange = distance;
					nearestPoint = new Point3D(xSort[middleX - i].x,
							xSort[middleX - i].y, xSort[middleX - i].z);
				}
			} else {
				break;
			}
		}
		return nearestPoint;
	}


	public int binsearch(int[] arg, int target, int left, int right) {
		int iL = left;
		int iR = right;
		int MIDDLE = 0;
		while (iL <= iR) {
			MIDDLE = (iL + iR) / 2;
			final int RES = Integer.compare(arg[MIDDLE], target);
			if (RES == 0) {
				found = true;
				return MIDDLE;
			} else if (RES < 0)
				iL = MIDDLE + 1;
			else
				iR = MIDDLE - 1;
		}
		found = false;
		return MIDDLE;
	}
}
