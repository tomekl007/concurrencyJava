/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timebudgettrylock;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
    public static void main(String[] args) {
        final TimedLocking tl = new TimedLocking();
        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    tl.trySendOnSharedLine("some msg", 100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    tl.trySendOnSharedLine("some msg", 100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        t1.start();
        //t1.interrupt();
        t2.start();
        
        //------------
         Thread t3 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    tl.trySendOnSharedLine("some texxt", 100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
         t3.start();
         t3.interrupt();
        
        
    }
}
