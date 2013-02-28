/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package keeptrackofcancelledtask;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        TrackingExecutor te = new TrackingExecutor(exec);
        
        te.execute(new Runnable () {

            @Override
            public void run() {
                System.out.println("in my runnable" + Thread.currentThread().toString());
            }
        });
        
         List<Runnable> r = te.shutdownNow();//dont return this task because is not awaitng
         //execution but it was interrupt in middle of execution
        System.out.println("task awaiting execution : " + r.toString());
        
        //Thread.sleep(3000);
       
        
       // te.shutdownNow();
    }
    
    
}
