/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iteracjaseida;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomek
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        float array [] [] = { {5, -2 ,1,23}, {1, 10, -3,87}, {2, 3, 6,87},{1,2,3,4} };
        Matrix A = new Matrix(array);
        //vector as one dimension array(can be change)
        float vector [] = {5, 10, 12,6};
        
        System.out.println(A);
        
        
        
        int numberOfIteration = 2;
        
       float array2 [] [] = { {8, 1 ,1}, {1, 8, 1}, {1, 1, 8}}; 
       float vector2 [] = {4, 4, 4};
       Matrix B = new Matrix(array2);
       
       float array3 [] [] = { {-4,-1, 0 , 1}, {0, -2, -1, 0}, {0,1, 2, 0},{0,1,3,-8}}; 
       float vector3 [] = {4, 1, 4, -1};
       Matrix C = new Matrix(array3);
        
       
            IteracjaSeidla.startIteration(B, vector2, numberOfIteration);
//            
//            float a1 [][] = {{2,2,2}, {2,3,4}, {3,4,5}};
//            float a2 [][] = {{2,2,2}, {2,3,4}, {3,4,5}};
//            Matrix c = new Matrix(a1); 
//        Matrix d = new Matrix(a2);
//        
//        Matrix r = c.addMatrix(d);
//        System.out.println("r : "+ r );
        
    }
}
