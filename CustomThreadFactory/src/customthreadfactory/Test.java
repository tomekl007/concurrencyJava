/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package customthreadfactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
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
    public static void main(String[] args) {
       MyThreadFactory mtf =  new MyThreadFactory("pool1");
          ExecutorService exec = Executors.newCachedThreadPool(mtf);
          
          //modifing executor created with standard factories
          if(exec instanceof ThreadPoolExecutor)
              ((ThreadPoolExecutor) exec).setMaximumPoolSize(300);
          
          //Executors includes a factory method, unconfigurableExecutorService, which
          //takes an existing ExecutorService and wraps it with one exposing only the methods
                  //of ExecutorService so it cannot be further configured.
          ExecutorService execUN = Executors.unconfigurableExecutorService(exec);
          if(execUN instanceof ThreadPoolExecutor)
              ((ThreadPoolExecutor) execUN).setMaximumPoolSize(300);
          
          exec.execute(new Runnable() {

            @Override
            public void run() {
                System.out.println("some runnable in " + Thread.currentThread().toString());
                  try {
                      Thread.sleep(10000);
                  } catch (InterruptedException ex) {
                      Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                  }
            }
        });
          exec.execute(new Runnable() {

            @Override
            public void run() {
               
                System.out.println("some runnable in " + Thread.currentThread().toString());
                // throw new Error();
            }
        });
          
            
         
         
        
          
    }
}
