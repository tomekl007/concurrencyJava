/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package novisibility;

import java.util.concurrent.TimeUnit;

/**
 * NoVisibility
 * <p/>
 * Sharing variables without synchronization
 *
 * @author Brian Goetz and Tim Peierls
 */

public class NoVisibility {
    //to avoid synchronize get and set
    private static boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {
        public void run() {
            while (!ready)
                Thread.yield(); //yield - thread jest gotow ustapic
            System.out.println(number);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ReaderThread().start();
         ready = true;
        // TimeUnit.SECONDS.sleep(1);
        number = 42;
       
        
    }
}
