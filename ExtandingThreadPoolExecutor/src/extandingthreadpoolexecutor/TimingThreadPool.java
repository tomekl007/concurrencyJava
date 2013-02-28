package extandingthreadpoolexecutor;


import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.logging.*;

/**
 * TimingThreadPool
 * <p/>
 * Thread pool extended with logging and timing
 *
 * TimingThreadPool in Listing 8.9 shows a custom thread pool that uses before-Execute, afterExecute,
 * and terminated to add logging and statistics gathering. To measure a task's runtime, beforeExecute 
 * must record the start time and store it somewhere afterExecute can find it. Because execution hooks 
 * are called in the thread that executes the task, a value placed in a ThreadLocal by beforeExecute can
 * be retrieved by afterExecute. TimingThreadPool uses a pair of AtomicLongs to keep track of the total 
 * number of tasks processed and the total processing time, and uses the terminated hook to print a log 
 * message showing the average task time.
 */
public class TimingThreadPool extends ThreadPoolExecutor {

    public TimingThreadPool() {
        super(1, 1, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>() );
    }

    private final ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    private final Logger log = Logger.getLogger("TimingThreadPool");
    private final AtomicLong numTasks = new AtomicLong();
    private final AtomicLong totalTime = new AtomicLong();

    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        System.out.println("BeforeExec : " + String.format("Thread %s: start %s", t, r.toString()) );
        log.fine(String.format("Thread %s: start %s", t, r));
        startTime.set(System.nanoTime());
    }

    protected void afterExecute(Runnable r, Throwable t) {
        try {
            long endTime = System.nanoTime();
            long taskTime = endTime - startTime.get();
            numTasks.incrementAndGet();
            totalTime.addAndGet(taskTime);
            System.out.println("AfterExec : " + String.format("Thread %s: end %s, time=%dns",
                    t, r.toString(), taskTime) );
            log.fine(String.format("Thread %s: end %s, time=%dns",
                    t, r, taskTime));
        } finally {
            super.afterExecute(r, t);
        }
    }

    protected void terminated() {
        try {
             System.out.println("Terminated : " + String.format("Terminated")
                     );
            log.info(String.format("Terminated") );//,
                   // totalTime.get() / numTasks.get()));
        } finally {
            super.terminated();
        }
    }
}
