package savingstatewebcrawler;

import java.util.*;
import java.util.concurrent.*;

/**
 * TrackingExecutor
 * <p/>
 * ExecutorService that keeps track of cancelled tasks after shutdown
 *
 * TRackingExecutor in Listing 7.21 shows a technique for determining which tasks were in progress at shutdown time. 
 * By encapsulating an ExecutorService and instrumenting execute (and similarly submit, not shown) to 
 * remember which tasks were cancelled after shutdown, trackingExecutor can identify which tasks started
 * but did not complete normally. After the executor terminates, getCancelledTasks returns the list of
 * cancelled tasks. In order for this technique to work, the tasks must preserve the thread's interrupted
 * status when they return, which well behaved tasks will do anyway.
 */



/*TRackingExecutor has an unavoidable race condition that could make it yield false positives: 
 * tasks that are identified as cancelled but actually completed. This arises because
 * the thread pool could be shut down between when the last instruction of the task 
 * executes and when the pool records the task as complete. This is not a problem if tasks
 * are idempotent (if performing them twice has the same effect as performing them once), as
 * they typically are in a web crawler. Otherwise, the application retrieving the cancelled 
 * tasks must be aware of this risk and be prepared to deal with false positives.*/

public class TrackingExecutor extends AbstractExecutorService {
    private final ExecutorService exec;
    private final Set<Runnable> tasksCancelledAtShutdown =
            Collections.synchronizedSet(new HashSet<Runnable>());

    public TrackingExecutor(ExecutorService exec) {
   
               
        
        
        this.exec = exec;
    }

    public void shutdown() {
        exec.shutdown();
    }

    public List<Runnable> shutdownNow() {
        return exec.shutdownNow();
    }

    public boolean isShutdown() {
        return exec.isShutdown();
    }

    public boolean isTerminated() {
        return exec.isTerminated();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit)
            throws InterruptedException {
        return exec.awaitTermination(timeout, unit);
    }

    public List<Runnable> getCancelledTasks() {
        if (!exec.isTerminated())
            throw new IllegalStateException(/*...*/);
        return new ArrayList<Runnable>(tasksCancelledAtShutdown);
    }

    public void execute(final Runnable runnable) {
        exec.execute(new Runnable() {
            public void run() {
                try {
                    runnable.run();
                } finally {
                    //jesli executror isShutdown in thread zostal interrupt
                    //then add this task to taskCancelledAtShutdown
                    if (isShutdown()
                            && Thread.currentThread().isInterrupted()){
                        System.out.println("add runnable : "+ runnable.toString()  +" to taskCancelledAtShutdown"  );
                        tasksCancelledAtShutdown.add(runnable);
                    }
                }
            }
        });
    }
}
