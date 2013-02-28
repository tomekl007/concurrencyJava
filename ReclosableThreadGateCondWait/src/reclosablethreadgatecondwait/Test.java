/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reclosablethreadgatecondwait;

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
    public static void main(String[] args) throws InterruptedException {
        final ThreadGate tg = new ThreadGate();
        tg.close();
        Thread thread1 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    tg.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("thread" + Thread.currentThread().toString());
                
            }
        });
        
        Thread thread2 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    tg.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("thread" + Thread.currentThread().toString());
                
            }
        });
        thread1.start();
        thread2.start();
        Thread.currentThread().sleep(1000);
        tg.open();

        
        
        
        
        
    }
}
