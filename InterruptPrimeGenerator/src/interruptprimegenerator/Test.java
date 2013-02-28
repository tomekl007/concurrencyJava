/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interruptprimegenerator;

import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException  {
        BlockingQueue<BigInteger> bq = new LinkedBlockingQueue<>() ;
       PrimeProducer pp = new PrimeProducer(bq);
       pp.start();
       // try {
        //    Thread.sleep(1000);
        //} catch (InterruptedException ex) {
         //   Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
       // }
       Thread.sleep(1000);
       pp.cancel();
      
       //pp.interrupt();
       
//       
//       BlockingQueue<BigInteger> bq2 = new LinkedBlockingQueue<>() ;
//       PrimeProducer pp2 = new PrimeProducer(bq2);
//       pp2.start();
//       pp2.cancel();
    }
}
