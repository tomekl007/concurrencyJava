package puzzleexample;

import java.util.concurrent.*;

//import net.jcip.annotations.*;

/**
 * ValueLatch
 * <p/>
 * Result-bearing latch used by ConcurrentPuzzleSolver
 *
 * ValueLatch in Listing 8.17 uses a CountDownLatch to provide the needed latching behavior,
 * and uses locking to ensure that the solution is set only once. 
 */
//@ThreadSafe
public class ValueLatch <T> {
  //  @GuardedBy("this")
    private T value = null;
    private final CountDownLatch done = new CountDownLatch(1);

    public boolean isSet() {
        return (done.getCount() == 0);
    }

    public synchronized void setValue(T newValue) {
        if (!isSet()) {
            value = newValue;
            done.countDown();
        }
    }

    public T getValue() throws InterruptedException {
        done.await();
        synchronized (this) {
            return value;
        }
    }
}
