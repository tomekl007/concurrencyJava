package net.jcip.examples;

import java.util.concurrent.locks.*;

import net.jcip.annotations.*;

/**
 * SemaphoreOnLock
 * <p/>
 * Counting semaphore implemented using Lock
 * (Not really how java.util.concurrent.Semaphore is implemented)
 *
 * Given this commonality, you might think that Semaphore was implemented on top of ReentrantLock,
 * or perhaps ReentrantLock was implemented as a Semaphore with one permit. This would be entirely 
 * practical; it is a common exercise to prove that a counting semaphore can be implemented using a 
 * lock (as in SemaphoreOnLock in Listing 14.12) and that a lock can be implemented using a counting semaphore. 
 */
@ThreadSafe
public class SemaphoreOnLock {
    private final Lock lock = new ReentrantLock();
    // CONDITION PREDICATE: permitsAvailable (permits > 0)
    private final Condition permitsAvailable = lock.newCondition();
    @GuardedBy("lock") private int permits;

    SemaphoreOnLock(int initialPermits) {
        lock.lock();
        try {
            permits = initialPermits;
        } finally {
            lock.unlock();
        }
    }

    // BLOCKS-UNTIL: permitsAvailable
    public void acquire() throws InterruptedException {
        lock.lock();
        try {
            while (permits <= 0)
                permitsAvailable.await();
            --permits;
        } finally {
            lock.unlock();
        }
    }

    public void release() {
        lock.lock();
        try {
            ++permits;
            permitsAvailable.signal();
        } finally {
            lock.unlock();
        }
    }
}
