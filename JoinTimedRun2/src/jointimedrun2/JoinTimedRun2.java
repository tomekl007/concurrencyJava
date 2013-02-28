/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jointimedrun2;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomasz Lelek
 */
public class JoinTimedRun2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)  {
        try {
            TimedRun2.timedRun(new Runnable() {

                @Override
                public void run() {
                 
                         System.out.println("Hi from thread " + Thread.currentThread().toString());
                
                }
            },2,TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            System.out.println("InterruptException in " + Thread.currentThread().toString() );
            Logger.getLogger(JoinTimedRun2.class.getName()).log(Level.SEVERE, null, ex);
        }  catch(Error e){
            System.out.println("Error exception");
        }
    }
}
