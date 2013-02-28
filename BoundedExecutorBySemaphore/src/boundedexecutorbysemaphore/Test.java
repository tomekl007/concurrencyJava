/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package boundedexecutorbysemaphore;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
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
        Executor exec = Executors.newCachedThreadPool();
        BoundedExecutor be = new BoundedExecutor(exec, 1);
        be.submitTask(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("some runnable");
            }
        });
        
        for(int i = 0 ;i < 10; i++){
        be.submitTask(new Runnable() {

            @Override
            public void run() {
                
                System.out.println("some runnable2");
            }
        });
        
        }
        
    }
}
