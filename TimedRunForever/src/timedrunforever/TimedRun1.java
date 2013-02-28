package timedrunforever;

import java.util.concurrent.*;

/**
 * InterruptBorrowedThread
 * <p/>
 * Scheduling an interrupt on a borrowed thread
 *
 * shows an attempt at running an arbitrary Runnable for a given amount of time.
 * It runs the task in the calling thread and schedules a cancellation task to 
 * interrupt it after a given time interval. This addresses the problem of unchecked
 * exceptions thrown from the task, since they can then be caught by the caller of timedRun. 
 */
public class TimedRun1 {
    private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(1);

    public static void timedRun(Runnable r,
                                long timeout, TimeUnit unit) {
        final Thread taskThread = Thread.currentThread();//to jest wywolujacy(caller)
        cancelExec.schedule(new Runnable() {//scheduluje ze po zadanym timeout
                                        //ma wyslac interrupt do callera, ale caller
                                       //nie wie o tym ze dostanie interrupt,
                                       //wiec to moze zostac niezauwazone
            public void run() {
                System.out.println("interrupt" + Thread.currentThread().toString());
                System.out.println(taskThread.toString());
                taskThread.interrupt();//interrupt caller thread(main in this ex)
                                 //but this won't be catch by caller
            }
        }, timeout, unit);
        r.run();
    }
}
