/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extandingthreadpoolexecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         ExecutorService es = new TimingThreadPool();
        
        
        es.shutdown();
        es.execute(new Runnable() {

            @Override
            public void run() {
                System.out.println("some task nr " + Thread.currentThread().toString());
              
            }
        });
        
    }
}
