package test;

import java.util.ArrayList;

import pa2.ImageProcessor;
import pa2.MatrixCuts;
import pa2.Picture;
import pa2.Tuple;

public class MatrixCutsTest {

	public static void main(String[] args) {
//		int cost[][] = { 	{5, 7, 1, 4, 5}, 
//                			{0, 0, 1, 1, 8}, 
//                			{2, 0, 49, 1, 0},
//                			{0, 1, 1, 1, 0},
//                			{50,51,0,26,1}};
		int cost[][] = { 	{5    ,7    ,9   , 4  ,  5}, 
    			{2    ,3    ,1    ,1    ,2}, 
    			{2,    0 ,   49   ,46  , 8},
    			{3   , 1   , 1   , 1   , 1},
    			{50  , 51 ,  25  , 26,   1}};
		for (int i = 0; i < cost.length; i++) {
			for (int j = 0; j < cost[0].length; j++) {
				System.out.print( "   " + cost[j][i]);
			}
			System.out.println();
		}
		
		System.out.println();
		ArrayList<Tuple> tupleList = MatrixCuts.widthCut(cost);
		System.out.println(tupleList.toString());
//		for (Tuple t : tupleList) {
//			System.out.println(t.toString());
//		}
//		
//		tupleList = MatrixCuts.stitchCut(cost);
//		for (Tuple t : tupleList) {
//			System.out.println(t.toString());
//		}
//		
		Picture p1 = new Picture("http://web.cs.iastate.edu/~pavan/311/F19/images/iastate1.jpg");
		Picture p2 = ImageProcessor.reduceWidth(5, "http://web.cs.iastate.edu/~pavan/311/F19/images/iastate1.jpg");
		p1.show();
		p2.show();
	}
}
