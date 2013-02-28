/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iteracjaseida;

import java.util.List;
//import java.lang.Exception;

/**
 *
 * @author Tomek
 */
public class Matrix {
    
    public float matrix [] [] ;
    
   public int rows;
    public int columns;
    
    //List< List<Float> > m;
    
    public Matrix( float matrix [] []){
        
        this.matrix = matrix;
        rows = matrix.length;
        columns = matrix.length;
       // m = new  
        
    }
    
    public Matrix(float matrix [] [],int rows, int columns){
        
        
       this.matrix = matrix;
        
        
        this.rows = rows;
        this.columns = columns;
    }
    
    public Matrix(int rows, int columns){
        
        
        matrix = new float[rows][columns];
        this.rows = rows;
        this.columns = columns;
    }

    
//    public Matrix appendVector(Matrix m, float [] vect){
//        
//        Matrix A1 = new Matrix(m.matrix);
//        
//        for(int i = 0; i < matrix.length ; i++){
//            System.out.println(i);
//            
//           for(int j = 0; j < matrix.length  ; j++){
//                           System.out.println(j);
//
//               
//           }   
//        }
//        
//        
//        return null;
//        
//        
//    }
    
     public void appendVectorToMatrix( Matrix src, float[] vect){
        
         int v = 0;
         for(int i = 0; i < rows ; i++){
             for (int j = 0; j < columns; j++){
                 
                 if(j == columns - 1){
                     
                     matrix[i][j] = vect[v];
                     v++;
                     break;
                 }
                 matrix[i][j] = src.matrix[i][j];
             }
         }
        
        
        
        
        
    }
     
     
     //Vect is a Matrix[n][1]
     public Matrix multiplyByVect(Matrix divisor){
         
         if(this.columns != divisor.rows || divisor.columns > 1){
             System.out.println("cant multiply D1 " + this.columns + "   "+ divisor.rows);
             return null;
         }
         
         
         Matrix result = new Matrix(divisor.rows,divisor.columns);
        System.out.println(result);

                 
         int iresult = 0;
         
         for(int i = 0; i < rows ; i++){
                for(int j = 0; j < divisor.rows ; j++){
                   // System.out.println(" j " + j);
                    System.out.println("mult : " + matrix[i][j] + " * " + divisor.matrix[j][0]);
                  result.matrix[i][0] += matrix[i][j] * divisor.matrix[j][0];
                }
              
             
         }
        System.out.println("result of multiply : " + result);
        

        return result;
         
         
     }
     
     public static void deepCopy(Matrix src, Matrix dest){
         
         for(int i = 0; i < src.rows; i++){
             for(int j = 0; j < src.columns; j++){
                 dest.matrix[i][j] = src.matrix[i][j];
             }
         }
         System.out.println("after deep copy = " + dest);
         
     }
     
     
     public Matrix multiplyByVectSeidel(Matrix divisor){
         
         if(this.columns != divisor.rows || divisor.columns > 1){
             System.out.println("cant multiply D1 " + this.columns + "   "+ divisor.rows);
             return null;
         }
         
         
         Matrix result = new Matrix(divisor.rows,divisor.columns);
        System.out.println(result);

                 
         System.out.println("DIVISOR : " + divisor);
         
         for(int i = 0; i < rows ; i++){
                
                for(int j = 0; j < divisor.rows ; j++){
                    
                  
                //    System.out.println(" j " + j);
                  result.matrix[i][0] += matrix[i][j] * divisor.matrix[j][0];
                  
                }
                if(i >= 1){
                System.out.println("-> switch[" + (i-1) + "] " + divisor.matrix[(i-1)][0] + " on "+
                        result.matrix[i-1][0]);
                divisor.matrix[i-1][0] = result.matrix[i-1][0] +(float)0.5;
                }
               
             
         }
        System.out.println("result of multiply : " + result);
        

        return result;
         
         
     }
     
     
     
     public Matrix addMatrix(Matrix comp){
        
         if(rows != comp.rows || columns != comp.columns){
         System.out.println("cant add, matrix'es are diffrent");    
         return null;
         }
         
         Matrix result = new Matrix(rows, columns);
         for(int i = 0; i < comp.rows; i++){
             for(int j = 0; j < comp.columns; j++){
                 result.matrix[i][j] = matrix[i][j] + comp.matrix[i][j];
             }
             
         }
         
         return result;
          
     }
     
     
     
    public void swapRows(int row1, int row2){// throws Exception{
        
        if(row1 > rows || row2 > rows ){
            row1 = 0;
            row2 = 0;
//            throw new Exception("from custom");
      }
        
        float temp;
        for(int i = 0; i < columns; i++){
        
           temp = matrix[row1][i];                        
           matrix[row1][i] = matrix[row2][i];
           matrix[row2][i] = temp;
            
        }
        
    }
            
            
    @Override
    public String toString(){
       String matrixTextForm = "";
        
     
        for(int i = 0; i < rows ; i++){
            System.out.println(i);
            matrixTextForm += "\n";
           for(int j = 0; j < columns  ; j++){
                           System.out.println(j);

               matrixTextForm += " " +  String.valueOf( matrix[i][j] );
           }   
        }
        
        return matrixTextForm;
    }
    
}
