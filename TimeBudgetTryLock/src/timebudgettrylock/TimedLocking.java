package timebudgettrylock;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * TimedLocking
 * <p/>
 * Locking with a time budget
 *
 * We saw one way to ensure serialized access to a resource in Section 9.5: 
 * a single-threaded executor. Another approach is to use an exclusive lock 
 * to guard access to the resource. The code in Listing 13.4 tries to send a
 * message on a shared communications line guarded by a Lock, but fails gracefully
 * if it cannot do so within its time budget. The timed TRyLock makes it 
 * practical to incorporate exclusive locking into such a time-limited activity.
 */
public class TimedLocking {
    private Lock lock = new ReentrantLock();

    public boolean trySendOnSharedLine(String message,
                                       long timeout, TimeUnit unit)
            throws InterruptedException {
        long nanosToLock = unit.toNanos(timeout)
                - estimatedNanosToSend(message);
        if (!lock.tryLock(nanosToLock, NANOSECONDS)){//If the specified waiting time elapses then the value false is returned.
            
            System.out.println("time elapsed - tryLock return false");
            return false;
            
        }
        try {
            return sendOnSharedLine(message);
        } finally {
            lock.unlock();
        }
    }

//    private boolean sendOnSharedLine(String message) throws InterruptedException {
//        System.out.println("sendOnSharedLIne");
//        Thread.currentThread().sleep(1000);//for testing purpose
//        /* send something */
//        return true;
//    }

    long estimatedNanosToSend(String message) {
        return message.length();
    }
    
    
    /*The canonical structure of interruptible lock acquisition is slightly 
     * more complicated than normal lock acquisition, as two TRy blocks are 
     * needed. (If the interruptible lock acquisition can throw InterruptedException,
     * the standard try-finally locking idiom works.) Listing 13.5 uses lockInterruptibly 
     * to implement sendOnSharedLine from Listing 13.4 so that we can call it from 
     * a cancellable task. The timed TRyLock is also responsive to interruption and
     * so can be used when you need both timed and interruptible lock acquisition. */
    private Lock lock2 = new ReentrantLock();

    public boolean sendOnSharedLine(String message)
            throws InterruptedException {
        System.out.println("sendOnShared interruptibly");
        lock2.lockInterruptibly();//Acquires the lock unless the current thread is interrupted.
        //then InterruptedException is thrown and the current thread's interrupted status is cleared.
        try {
            return cancellableSendOnSharedLine(message);
        } finally {
            lock2.unlock();
        }
    }

    private boolean cancellableSendOnSharedLine(String message) throws InterruptedException {
        /* send something */
        return true;
    }
}
