package pa2;

import java.util.ArrayList;

/**
 * 
 * @author Steven Rein and Amith Kopparapu Venkata Boja
 *
 */
public class MatrixCuts {
	
	// May need to swap x and y on this implementation?? But not for the creation of tuples
	public static ArrayList<Tuple> widthCut(int[][] mat) {
		if (mat.length < 1 && mat[0].length < 1) {
			return new ArrayList<>();
		} else {
			int[][] costMat = new int[mat.length][mat[0].length];
			Tuple[][] previousTuple = new Tuple[mat.length][mat[0].length];	
			for (int y = 0; y < costMat.length; y++) {
				for (int x = 0; x < costMat[0].length; x++) {
					costMat[y][x]=Integer.MAX_VALUE;
				}
			}
			for(int l=0;l<mat[0].length;l++) {
				costMat[0][l]=mat[0][l];
				previousTuple[0][l]=null;
			}
			
			// Iterate through all cells, assigning the optimal value at that cell.
			for (int y = 0; y < mat.length-1; y++) { 
				for (int x = 0; x < mat[0].length; x++) {
					//int thisCost = costMat[y][x];
					int previousTupleCode = -1;
					
//					if (y == 0) {
//						costMat[y][x] = mat[y][x];
//						//previousTupleCode = -1;
//					} else {
						// Do left
						if (x != 0 && costMat[y][x] + mat[y+1][x-1] < costMat[y+1][x-1]) {
							costMat[y+1][x-1] = costMat[y][x] + mat[y+1][x-1];
							previousTupleCode = 0;
						}
						// Do middle
						if (costMat[y][x] + mat[y+1][x] < costMat[y+1][x]) {
							costMat[y+1][x] = costMat[y][x] + mat[y+1][x];
							previousTupleCode = 1;
						}
						// Do right
						if (x != mat[0].length - 1 && costMat[y][x] + mat[y+1][x+1] < costMat[y+1][x+1]) {
							costMat[y+1][x+1] = costMat[y][x] + mat[y+1][x+1] ;
							previousTupleCode = 2;
						}
//					}
					
					switch (previousTupleCode) {
					case 0:
						previousTuple[y+1][x-1] = new Tuple(x, y);
						break;
					case 1:
						previousTuple[y+1][x] = new Tuple(x, y);
						break;
					case 2:
						previousTuple[y+1][x+1] = new Tuple(x, y);
						break;
					case -1:
						previousTuple[y][x] = null;
 						break;
					}

					//costMat[y][x] = thisCost;
				}
				
			}
			
			// Determine which option in the top row is best. 
			// Since we need the one that ends in the left most of the bottom row, we need to check all possible x values.
			int y = costMat.length - 1;
			int x = 0;
			int topCost = Integer.MAX_VALUE;
			for (int i = 0; i < mat[0].length; i++) {
				if (costMat[y][i] < topCost) {
					topCost = costMat[y][i];
					x = i;
				}
			}
			int cost = costMat[y][x];
			System.out.println(cost);
			// Create List of Tuples to return as the result.
			ArrayList<Tuple> tempResult = new ArrayList<>();
			tempResult.add(new Tuple(y, x));
			while (previousTuple[y][x] != null) {
				Tuple t = previousTuple[y][x];
				tempResult.add(t);
				y = t.getY();
				x = t.getX();
			}
			//tempResult.add(new Tuple(y, x));
			
			ArrayList<Tuple> result = new ArrayList<>();
			result.add(new Tuple(cost, -1));
			for (int i = tempResult.size() - 1; i >= 0; i--) {
				result.add(tempResult.get(i));
			}

			return result;
		}
	}
	
	
	public static ArrayList<Tuple> stitchCut(int [][] mat) {
		if (mat.length < 1 && mat[0].length < 1) {
			return new ArrayList<>();
		} else {
			int[][] costMat = new int[mat.length][mat[0].length];
			Tuple[][] previousTuple = new Tuple[mat.length][mat[0].length];
 			
			int x = mat[0].length - 1;
			int y = mat.length - 1;			
			
			// Iterate through all cells, assigning the optimal value at that cell.
			// Y = 0
			for (x = mat[0].length - 1; x >= 0; x--) {
				costMat[y][x] = mat[y][x];
				previousTuple[y][x] = null;
			}
					
			// X = 0
			x = mat[0].length - 1;
			for (y = mat.length - 2; y >= 0; y--) {
				costMat[y][x] = costMat[y+1][x] + mat[y][x];
				previousTuple[y][x] = new Tuple(y+1, x);
			}
			
			for (y = mat.length - 2; y >= 0; y--) {
				for (x = mat[0].length - 2; x >= 0; x--) {
					int thisCost = Integer.MAX_VALUE;
					int previousTupleCode = -1;
					// Do x-1 check
					if (costMat[y][x+1] + mat[y][x] < thisCost) {
						thisCost = costMat[y][x+1] + mat[y][x];
						previousTupleCode = 0;
					}
					// Do y-1 check					
					if (costMat[y+1][x] + mat[y][x] < thisCost) {
						thisCost = costMat[y+1][x] + mat[y][x];
						previousTupleCode = 1;
					}
					// Do y-1,x-1 check
					if (costMat[y+1][x+1] + mat[y][x] < thisCost) {
						thisCost = costMat[y+1][x+1] + mat[y][x];
						previousTupleCode = 2;
					}
					
					switch (previousTupleCode) {
					case 0:
						previousTuple[y][x] = new Tuple(y, x+1);
						break;
					case 1:
						previousTuple[y][x] = new Tuple(y+1, x);
						break;
					case 2:
						previousTuple[y][x] = new Tuple(y+1, x+1);
						break;
					case -1:
						break;
					}

					costMat[y][x] = thisCost;
				}
				
			}
			
			// Determine which option in the top row is best.
			y = 0;
			int topCost = Integer.MAX_VALUE;
			for (int i = 0; i < mat[0].length; i++) {
				if (costMat[y][i] < topCost) {
					topCost = costMat[y][i];
					x = i;
				}
			}
			int cost = costMat[y][x];
			
			// Create List of Tuples to return the result
			ArrayList<Tuple> result = new ArrayList<>();
			result.add(new Tuple(cost, -1));
			result.add(new Tuple(x, y));
			
			while (previousTuple[y][x] != null) {
				Tuple t = previousTuple[y][x];
				result.add(t);
				y = t.getX();
				x = t.getY();
			}

			return result;
		}		
	}
}

