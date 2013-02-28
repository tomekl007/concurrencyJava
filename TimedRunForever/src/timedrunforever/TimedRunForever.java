/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timedrunforever;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomasz Lelek
 */
public class TimedRunForever {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         TimedRun1.timedRun(new Runnable() {

            @Override
            public void run() {
               //  try {
                  //   Thread.sleep(3000);
                while(!Thread.currentThread().isInterrupted() ){
                     System.out.println("Hi from thread " + Thread.currentThread().toString());
                }
                 //} catch (InterruptedException ex) {
                     //Thread.currentThread().interrupt();
                   //  Logger.getLogger(TimedRunForever.class.getName()).log(Level.SEVERE, null, ex);
                // }
                System.out.println("interrupted");
            }
        },2,TimeUnit.SECONDS);
        
    }
}
