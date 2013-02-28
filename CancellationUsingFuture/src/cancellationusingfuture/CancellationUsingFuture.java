/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cancellationusingfuture;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomasz Lelek
 */
public class CancellationUsingFuture {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            TimedRun.timedRun(new Runnable() {

                    @Override
                    public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        System.out.println("Sleep Interrupted, because timed run cancel this task");
                        Logger.getLogger(CancellationUsingFuture.class.getName()).log(Level.SEVERE, null, ex);
                    }
                             System.out.println("Hi from thread " + Thread.currentThread().toString());
                    
                    }
                },2,TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            System.out.println("InterruptException catch in main ");
            Logger.getLogger(CancellationUsingFuture.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
