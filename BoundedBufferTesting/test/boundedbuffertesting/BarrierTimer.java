package boundedbuffertesting;

/**
 * BarrierTimer
 * <p/>
 * Barrier-based timer
 *
 * The primary extension we have to make to PutTakeTest is to measure the time taken for a run.
 * Rather than attempting to measure the time for a single operation, we get a more accurate
 * measure by timing the entire run and dividing by the number of operations to get a 
 * per-operation time. We are already using a CyclicBarrier to start and stop the worker
 * threads, so we can extend this by using a barrier action that measures the start and 
 * end time, as shown in Listing 12.11.
 */
public class BarrierTimer implements Runnable {
    private boolean started;
    private long startTime, endTime;

    public synchronized void run() {
        long t = System.nanoTime();
        if (!started) {
            started = true;
            startTime = t;
        } else
            endTime = t;
    }

    public synchronized void clear() {
        started = false;
    }

    public synchronized long getTime() {
        return endTime - startTime;
    }
}
