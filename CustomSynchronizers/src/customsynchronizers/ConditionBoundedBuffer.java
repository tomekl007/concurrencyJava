package customsynchronizers;

import java.util.concurrent.locks.*;

//import net.jcip.annotations.*;

/**
 * ConditionBoundedBuffer
 * <p/>
 * Bounded buffer using explicit condition variables
 *
 * Listing 14.11 shows yet another bounded buffer implementation, 
 * this time using two Conditions, notFull and notEmpty, to represent
 * explicitly the "not full" and "not empty" condition predicates. 
 * When take blocks because the buffer is empty, it waits on notEmpty
 * , and put unblocks any threads blocked in take by signaling on notEmpty
 */

//@ThreadSafe
public class ConditionBoundedBuffer <T> {
    protected final Lock lock = new ReentrantLock();
    // CONDITION PREDICATE: notFull (count < items.length)
    private final Condition notFull = lock.newCondition();
    // CONDITION PREDICATE: notEmpty (count > 0)
    private final Condition notEmpty = lock.newCondition();
    private static final int BUFFER_SIZE = 100;
  //  @GuardedBy("lock")
    private final T[] items = (T[]) new Object[BUFFER_SIZE];
    //@GuardedBy("lock") 
    private int tail, head, count;

    // BLOCKS-UNTIL: notFull
    public void put(T x) throws InterruptedException {
    
        lock.lock();
        try {
            while (count == items.length)
                notFull.await();
            items[tail] = x;
            if (++tail == items.length)
                tail = 0;
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    // BLOCKS-UNTIL: notEmpty
    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                notEmpty.await();
            T x = items[head];
            items[head] = null;
            if (++head == items.length)
                head = 0;
            --count;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }/*The behavior of ConditionBoundedBuffer is the same as BoundedBuffer,
     * but its use of condition queues is more readable - it is easier to
     * analyze a class that uses multiple Conditions than one that uses a
     * single intrinsic condition queue with multiple condition predicates.
     * By separating the two condition predicates into separate wait sets,
     * Condition makes it easier to meet the requirements for single notification.
     * Using the more efficient signal instead of signalAll reduces the number
     * of context switches and lock acquisitions triggered by each buffer operation.*/
}
