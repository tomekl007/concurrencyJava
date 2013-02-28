package customthreadfactory;

import java.util.concurrent.*;

/**
 * MyThreadFactory
 * <p/>
 * Custom thread factory
 *
 * MyThreadFactory in Listing 8.6 illustrates a custom thread factory. It instantiates a
 * new MyAppThread, passing a pool-specific name to the constructor so that threads from
 * each pool can be distinguished in thread dumps and error logs.
 * My-AppThread can also be used elsewhere in the application so
 * that all threads can take advantage of its debugging features
 */
public class MyThreadFactory implements ThreadFactory {
    private final String poolName;

    public MyThreadFactory(String poolName) {
        this.poolName = poolName;
    }

    public Thread newThread(Runnable runnable) {
        System.out.println("newThread");
        return new MyAppThread(runnable, poolName);
    }
}
