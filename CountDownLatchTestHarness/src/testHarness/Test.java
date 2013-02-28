/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testHarness;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {
    
    public static void main(String[] args) throws InterruptedException {
     TestHarness th = new TestHarness();
     
     long time = th.timeTasks(4, new Runnable(){

            @Override
            public void run() {
             try {
                 System.out.println("thread nr : " + Thread.currentThread().toString());
                 Thread.sleep(4000);
             } catch (InterruptedException ex) {
                 Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
             }
            }

           
         
     });
     System.out.println("time : " + time);
        
      
      
      }
    
    
}
