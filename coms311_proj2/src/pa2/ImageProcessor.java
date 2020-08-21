package pa2;

import java.awt.Color;
import java.util.ArrayList;

/**
 * 
 * @author Steven Rein and Amith Kopparapu Venkata Boja
 *
 */
public class ImageProcessor {
	
	public static Picture reduceWidth(int x, String inputImage) {
		

		Picture finalPic = new Picture(inputImage);
		int[][] costs = new int[finalPic.height()][finalPic.width()];
		
		for (int k = 0; k < x; k++) {			
			for(int i=0;i<finalPic.height();i++) {
				for(int j=0;j<finalPic.width();j++) {
					if (j == 0) {
						costs[i][j] = computeImportance(finalPic.get(i, j), finalPic.get(i, j+1));
					} else if (j == finalPic.width() - 1) {
						costs[i][j] = computeImportance(finalPic.get(i, j), finalPic.get(i, j-1));
					} else {
						System.out.println("" + costs[i][j]+ " "+ i + " "+j);
						
						costs[i][j] = computeImportance(finalPic.get(i, j-1), finalPic.get(i, j+1));
					}
					//System.out.println("" + costs[i][j]);
				}
			}
			
			ArrayList<Tuple> pixelsToRemove = MatrixCuts.widthCut(costs);
			finalPic=createNewPicture(finalPic, pixelsToRemove);
			costs = new int[finalPic.height()][finalPic.width()];
		}			
		return finalPic;
	}
	
	private static int computeImportance(Color c1, Color c2) {
		int r1 = c1.getRed() - c2.getRed();
		int r2 = r1*r1;
		int g1 = c1.getGreen() - c2.getGreen();
		int g2 = g1*g1;
		int b1 = c1.getBlue() - c2.getBlue();
		int b2 = b1*b1;
		
		return r2+g2+b2;
	}
	
	private static Picture createNewPicture(Picture picture, ArrayList<Tuple> pixelsToRemove) {
		Picture result = new Picture(picture.width() - 1, picture.height());
		System.out.println("ArrayList size : "+pixelsToRemove.size());
		System.out.println("picture height: "+picture.height());
		System.out.println("picture width : "+picture.width());
		for (Tuple t : pixelsToRemove) {
			System.out.println(t.toString());
		}
		
		for (int i = 0; i < picture.width() -1; i++) {
			Tuple t = pixelsToRemove.get(i+1);
			boolean remove = false;
			for (int j = 0; j < picture.height(); j++) {
				if (t.getY() == i) {
					remove = true;
				}
				if (remove == false) {
					result.set(i, j, picture.get(i, j));
				} else {
					result.set(i, j, picture.get(i+1, j));
				}
			}
		}
		
		return result;
	}
 
	
}
