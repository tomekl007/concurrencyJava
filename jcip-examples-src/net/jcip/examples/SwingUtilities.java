package net.jcip.examples;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;

/**
 * SwingUtilities
 * <p/>
 * Implementing SwingUtilities using an Executor
 *
 * he invokeLater and invokeAndWait methods function a lot like an Executor. In fact,
 * it is trivial to implement the threading-related methods from SwingUtilities using
 * a single-threaded Executor, as shown in Listing 9.1. This is not how SwingUtilitie
 * s is actually implemented, as Swing predates the Executor framework, but is probably
 * how it would be if Swing were being implemented today
 */

/*The Swing event thread can be thought of as a single-threaded Executor that processes
 * tasks from the event queue. As with thread pools, sometimes the worker thread dies 
 * and is replaced by a new one, but this should be transparent to tasks. Sequential, 
 * single-threaded execution is a sensible execution policy when tasks are short-lived,
 * scheduling predictability is not important, or it is imperative that tasks not execute
 * concurrently. */
public class SwingUtilities {
    private static final ExecutorService exec =
            Executors.newSingleThreadExecutor(new SwingThreadFactory());
    private static volatile Thread swingThread;

    private static class SwingThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            swingThread = new Thread(r);
            return swingThread;
        }
    }

    public static boolean isEventDispatchThread() {
       
        return Thread.currentThread() == swingThread;
    
    }
    
    

    public static void invokeLater(Runnable task) {
        exec.execute(task);
    }

    public static void invokeAndWait(Runnable task)
            throws InterruptedException, InvocationTargetException {
        Future f = exec.submit(task);
        try {
            f.get();
        } catch (ExecutionException e) {
            throw new InvocationTargetException(e);
        }
    }
}
