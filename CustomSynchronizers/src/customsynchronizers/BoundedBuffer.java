package customsynchronizers;

//import net.jcip.annotations.*;

/**
 * BoundedBuffer
 * <p/>
 * Bounded buffer using condition queues
 *
 BoundedBuffer in Listing 14.6 implements a bounded buffer using wait and notifyAll.
 * This is simpler than the sleeping version, and is both more efficient
 * (waking up less frequently if the buffer state does not change) 
 * and more responsive (waking up promptly when an interesting state change happens).
 * This is a big improvement, but note that the introduction of 
 * condition queues didn't change the semantics compared to the sleeping version.
 * It is simply an optimization in several dimensions: CPU efficiency,
 * context-switch overhead, and responsiveness.
 */
//@ThreadSafe
        public class BoundedBuffer <V> extends BaseBoundedBuffer<V> {
    // CONDITION PREDICATE: not-full (!isFull())
    // CONDITION PREDICATE: not-empty (!isEmpty())
    public BoundedBuffer() {
        this(100);
    }

    public BoundedBuffer(int size) {
        super(size);
    }

    // BLOCKS-UNTIL: not-full
    public synchronized void put(V v) throws InterruptedException {
        System.out.println("put");
        while (isFull())
            wait();/*Object.wait atomically releases the lock and asks the
             * OS to suspend the current thread, allowing other threads 
             * to acquire the lock and therefore modify the object state.
             * Upon waking, it reacquires the lock before returning. 
             * Intuitively, calling wait means "I want to go to sleep,
             * but wake me when something interesting happens", 
             * and calling the notification methods means "something interesting happened*/ 
        doPut(v);
        notifyAll();
    }

    // BLOCKS-UNTIL: not-empty
    public synchronized V take() throws InterruptedException {
        System.out.println("take");
        while (isEmpty())
            wait();
        V v = doTake();
        notifyAll();
        return v;
    }

    
    
    /*The notification done by put and take in BoundedBuffer is conservative:
     * a notification is performed every time an object is put into or removed
     * from the buffer. This could be optimized by observing that a thread can
     * be released from a wait only if the buffer goes from empty to not empty
     * or from full to not full, and notifying only if a put or take effected 
     * one of these state transitions. This is called conditional notification.
     * While conditional notification can improve performance, it is tricky to 
     * get right (and also complicates the implementation of subclasses) and so
     * should be used carefully. Listing 14.8 illustrates using conditional 
     * notification in BoundedBuffer.put. */
    // BLOCKS-UNTIL: not-full
    // Alternate form of put() using conditional notification
    public synchronized void alternatePut(V v) throws InterruptedException {
        while (isFull())
            wait();
        boolean wasEmpty = isEmpty();
        doPut(v);
        if (wasEmpty)
            notifyAll();
    }
}
