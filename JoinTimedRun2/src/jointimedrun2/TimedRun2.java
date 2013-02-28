package jointimedrun2;

import java.util.concurrent.*;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import jointimedrun2.LaunderThrowable;

/**
 * TimedRun2
 * <p/>
 * Interrupting a task in a dedicated thread
 *
 * addresses the exception-handling problem of aSecondOfPrimes 
 * and the problems with the previous attempt. The thread created to run the
 * task can have its own execution policy, and even if the task doesn't respond
 * to the interrupt, the timed run method can still return to its caller. After
 * starting the task thread, timedRun executes a timed join with the newly created
 * thread. After join returns, it checks if an exception was thrown from the task 
 * and if so, rethrows it in the thread calling timedRun. The saved Throwable is
 * shared between the two threads, and so is declared volatile to safely publish 
 * it from the task thread to the timedRun thread. This version addresses the 
 * problems in the previous examples, but because it relies on a timed join, it
 * shares a deficiency with join: we don't know if control was returned because 
 * the thread exited normally or because the join timed out.
 * 
 * [2] This is a flaw in the Thread API, because whether or not the join completes successfully has memory visibility consequences in the Java 
 * Memory Model, but join does not return a status indicating whether it was successful. 
 */
public class TimedRun2 {
    private static final ScheduledExecutorService cancelExec = newScheduledThreadPool(1);

    public static void timedRun(final Runnable r,
                                long timeout, TimeUnit unit)
            throws InterruptedException{
        class RethrowableTask implements Runnable {
            private volatile Throwable t;//volatile poniewaz w timedRun task, and caller task

            public void run() {
                try {
                    System.out.println("retrrTask " + Thread.currentThread().toString());
                    r.run();
                  //  throw new Error();//jesli z tego watku zostanie rzucony wyjatek, 
                } catch (Throwable t) {
                   this.t = t;//to zapamietuje go w this.t
                    
                }
            }

            void rethrow() {
                if (t != null)
                    throw LaunderThrowable.launderThrowable(t) ;
            }
        }

        RethrowableTask task = new RethrowableTask();//w tym jest task runnable ktory dostarczyl caller
        final Thread taskThread = new Thread(task);
        taskThread.start();//startuje task dosarczony przez callera
        
        cancelExec.schedule(new Runnable() {
            public void run() {
                System.out.println("interrupt " + Thread.currentThread().toString());
                taskThread.interrupt();
            }
        }, timeout, unit);//scheduler po odpowiednim czasie interrupt moj thread
        
        taskThread.join(unit.toMillis(timeout));//join throw InterruptException 
        System.out.println("before retrow"+ Thread.currentThread().toString());
        task.rethrow();//sprawdza czy by rzucony wyjatek z wewnatrz RetrowableTask.run i go rzuca dalej
    }
}
