package non.blockingcounter;

/**
 * CasCounter
 * <p/>
 * Nonblocking counter using CAS
 *
 * The typical pattern for using CAS is first to read the value A from V, 
 * derive the new value B from A, and then use CAS to atomically change V 
 * from A to B so long as no other thread has changed V to another value in
 * the meantime. CAS addresses the problem of implementing atomic read-modify-write 
 * sequences without locking, because it can detect interference from other threads. 
 */
//@ThreadSafe

/*CasCounter in Listing 15.2 implements a thread-safe counter using CAS. The 
 * increment operation follows the canonical form - fetch the old value, 
 * transform it to the new value (adding one), and use CAS to set the new 
 * value. If the CAS fails, the operation is immediately retried. Retrying
 * repeatedly is usually a reasonable strategy, although in cases of extreme
 * contention it might be desirable to wait or back off before retrying to 
 * avoid livelock. CasCounter does not block, though it may have to retry 
 * several[4] times if other threads are updating the counter at the same time.
 * (In practice, if all you need is a counter or sequence generator, just use
 * AtomicInteger or AtomicLong, which provide atomic increment and other arithmetic methods.) */
public class CasCounter {
    private SimulatedCAS value = new SimulatedCAS();

    public int getValue() {
        return value.get();
    }

    public int increment() {
        int v;
        do {
            v = value.get();
        } while (v != value.compareAndSwap(v, v + 1));//jesli v to jest ta ktora wziolem to swap na v+1
        return v + 1;
    }
}
