package cancellationusingfuture;

import java.util.concurrent.*;


/**
 * TimedRun
 * <p/>
 * Cancelling a task using Future
 *
 *  shows a version of timedRun that submits the task to an ExecutorService and
 * retrieves the result with a timed Future.get. If get terminates with a TimeoutException,
 * the task is cancelled via its Future. (To simplify coding, this version calls Future.cancel 
 * unconditionally in a finally block, taking advantage of the fact that cancelling a completed
 * task has no effect.) If the underlying computation throws an exception prior to cancellation,
 * it is rethrown from timedRun, which is the most convenient way for the caller to deal with the
 * exception. Listing 7.10 also illustrates another good practice: cancelling tasks whose result
 * is no longer needed.
 */
public class TimedRun {
    private static final ExecutorService taskExec = Executors.newCachedThreadPool();

    public static void timedRun(Runnable r,
                                long timeout, TimeUnit unit)
            throws InterruptedException {
        Future<?> task = taskExec.submit(r);
        try {
            task.get(timeout, unit);
        } catch (TimeoutException e) {
            System.out.println("TimeoutException");
            // task will be cancelled below
        } catch (ExecutionException e) {
            // exception thrown in task; rethrow
            throw LaunderThrowable.launderThrowable(e.getCause());
        } finally {
            // Harmless if task already completed
            System.out.println("Atttempt to cancel");
            task.cancel(true); // interrupt if running
            //When Future.get throws InterruptedException or TimeoutException and
            //you know that the result is no longer needed by the program,
            //cancel the task with Future.cancel.
        }
    }
}
