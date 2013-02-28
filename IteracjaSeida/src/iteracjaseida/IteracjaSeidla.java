/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iteracjaseida;



/**
 *
 * @author Tomek
 */
 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




/**
 *
 * @author Tomek
 */
public class IteracjaSeidla {
//extends Thread {
    
//    Matrix A;    
//    float B[];
//    int n ;
    
    
//    public void IteracjaSeidla(Matrix a, float[] b, int numberOfIteration ){
//        this.A = a;
//        this.B = b;
//        this.n = numberOfIteration;
//    }
    
//    @Override
//    public void run() {
//                   System.out.println("in run");
//
//    }
//    
   
    
    
    
    public static float[] startIteration(Matrix a, float[] b, int numberOfIteration) {//throws Exception{
        
       // Matrix A1 = a.appendVector(b);
       Matrix A1 = new Matrix(a.matrix.length , a.matrix.length + 1 );
      // System.out.println(A1);
       
       A1.appendVectorToMatrix(a, b);
        System.out.println(A1);
        
        int j = 0;
        for(int i = 0; i < a.matrix.length; i++){
            if(A1.matrix[i][i] == 0 ){
                j = i + 1;
                
                while(A1.matrix[j][i] == 0){
                    j += 1;
                }
                
                A1.swapRows(i,j);
                System.out.println(A1);

            }
        }
        
        int wym = a.matrix.length ;
        System.out.println("wym" + wym);
        Matrix D1 = new Matrix(wym, wym);
       // float [] C = new float[wym];
       Matrix C = new Matrix(wym,1);
        
        for(int i = 0; i < wym ; i++){
            for(j = 0; j < wym; j++){
                D1.matrix[i][j] = -(A1.matrix[i][j]/A1.matrix[i][i]);
            }
            D1.matrix[i][i] = 0;
            System.out.println(D1);
            
            C.matrix[i][0] = A1.matrix[i][wym]/A1.matrix[i][i];
            System.out.println(C.matrix[0][0] +"" +"" + C.matrix[1][0]+"" +""+ C.matrix[2][0]);
        }
        
        System.out.println(D1);
        //X = D1 * X + C
        
        //float [] X = new float[wym];
        //System.out.print("wym " + wym);
        Matrix X = new Matrix(wym ,1);
        Matrix Y = new Matrix(wym,1);// float [] Y = new float[wym];//add result of iteration
//        X.matrix[0][0] = 2;
//        X.matrix[1][0] = 2;
//        X.matrix[2][0] = 2;

        boolean flag = true;
        Matrix MConst = new Matrix(C.rows,C.columns);
        for(int i = 0;  i < numberOfIteration ; i++ ){
          
        //  final Matrix Const = new Matrix(C.matrix);  
          //  if(flag){
          //   Matrix MConst = new Matrix(C.rows,C.columns);
          //   Matrix.deepCopy(C, MConst);    
          //   flag = false;
         //   }
            
          if(flag){  
         System.out.println("in if");
         
         Matrix.deepCopy(C, MConst);    
          

          X = C;
          flag = false;
            }
//            Matrix resultOfNIteration = X.addMatrix(C);
//            
//            if(numberOfIteration > 0){
//          X = D1.multiplyByVect(resultOfNIteration);      
//            }
       // else{  
            System.out.println("X : "  + X + "C " + C);
         
            //for(j = 0; j < X.rows ; j++){
            for(j = 0; j < D1.rows; j++){
                //ciacham macierz D1 na vectory
                Matrix tempVect = new Matrix(1, D1.columns);
               
                //take rows and add to vector
                for(int k = 0 ; k < D1.columns; k++){
                   tempVect.matrix[0][k] = D1.matrix[j][k];
                }
                
                System.out.println("temp matrix nr " + j  + " " + tempVect );
                Matrix m = tempVect.multiplyByVect(X);
                //result is one number in m.matrix[0][0]
                System.out.println("result : " + m.matrix[0][0]);
                
                System.out.println("stala C[ "+ j +"] " + MConst.matrix[j][0]);
                float stala =  MConst.matrix[j][0];
                
                float r = (m.matrix[0][0] + stala);
                 System.out.println("->after : " +  r);
                 X.matrix[j][0] = r;
                //m.matrix[0][0] + C.matrix[j];
            }
            
            
          //  X = D1.multiplyByVect(X).addMatrix(C);
           // X.matrix[j][0] = X.matrix[j][0];
            
           // System.out.println("X after : "  + X + " C " + C);

        //  }            
            
            
          System.out.println("result : " + X);

            
        }
        
        
        
        
        
        
        
        
        
        
       
       
        return null;
    }
    
    
    
}
