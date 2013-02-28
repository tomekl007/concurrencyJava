package net.jcip.examples;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;

import net.jcip.annotations.*;

/**
 * SocketUsingTask
 * <p/>
 * Encapsulating nonstandard cancellation in a task with newTaskFor
 *
 * 
 * The technique used in ReaderThread to encapsulate nonstandard cancellation can
 * be refined using the newTaskFor hook added to ThreadPoolExecutor in Java 6. When
 * a Callable is submitted to an ExecutorService, submit returns a Future that can
 * be used to cancel the task. The newTaskFor hook is a factory method that creates 
 * the Future representing the task. It returns a RunnableFuture, an interface that 
 * extends both Future and Runnable (and is implemented by FutureTask). Customizing 
 * the task Future allows you to override Future.cancel. Custom cancellation code can
 * perform logging or gather statistics on cancellation, and can also be used to cancel 
 * activities that are not responsive to interruption
 * ReaderThread encapsulates cancellation of socket-using threads by overriding interrupt;
 * the same can be done for tasks by overriding Future.cancel.
 * 
 */


/*SocketUsingTask implements CancellableTask and defines Future.cancel
 * to close the socket as well as call super.cancel. If a SocketUsingTask 
 * is cancelled through its Future, the socket is closed and the executing
 * thread is interrupted. This increases the task's responsiveness to cancellation:
 * not only can it safely call interruptible blocking methods while remaining responsive 
 * to cancellation, but it can also call blocking socket I/O methods. */
public abstract class SocketUsingTask <T> implements CancellableTask<T> {
    @GuardedBy("this") private Socket socket;

    protected synchronized void setSocket(Socket s) {
        socket = s;
    }

    public synchronized void cancel() {
        try {
            if (socket != null)
                socket.close();
        } catch (IOException ignored) {
        }
    }

    public RunnableFuture<T> newTask() {
        return new FutureTask<T>(this) {
            public boolean cancel(boolean mayInterruptIfRunning) {
                try {
                    SocketUsingTask.this.cancel();
                } finally {
                    return super.cancel(mayInterruptIfRunning);
                }
            }
        };
    }
}

//defines a CancellableTask interface that extends Callable and adds a cancel method and a 
//newTask factory method for constructing a RunnableFuture. CancellingExecutor extends 
//ThreadPoolExecutor, and overrides newTaskFor to let a CancellableTask create its own Future. 
interface CancellableTask <T> extends Callable<T> {
    void cancel();

    RunnableFuture<T> newTask();
}


@ThreadSafe
class CancellingExecutor extends ThreadPoolExecutor {
    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof CancellableTask)
            return ((CancellableTask<T>) callable).newTask();
        else
            return super.newTaskFor(callable);
    }
}
