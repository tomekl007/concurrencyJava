package latchonaqs;

import java.util.concurrent.locks.*;

//import net.jcip.annotations.*;

/**
 * OneShotLatch
 * <p/>
 * Binary latch using AbstractQueuedSynchronizer
 *
 * OneShotLatch in Listing 14.14 is a binary latch implemented using AQS. 
 * It has two public methods, await and signal, that correspond to acquisition
 * and release. Initially, the latch is closed; any thread calling await blocks 
 * until the latch is opened. Once the latch is opened by a call to signal, waiting
 * threads are released and threads that subsequently arrive at the
 * latch will be allowed to proceed
 */
//@ThreadSafe
public class OneShotLatch {
    private final Sync sync = new Sync();

    public void signal() {
        sync.releaseShared(0);
    }

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(0);
    }

    
    
    
    /*In OneShotLatch, the AQS state holds the latch state - closed (zero) or open (one). 
     * The await method calls acquireSharedInterruptibly in AQS, which in turn consults the 
     * TRyAcquireShared method in OneShotLatch. The tryAcquire-Shared implementation must return
     * a value indicating whether or not acquisition can proceed. If the latch has been previously
     * opened, tryAcquireShared returns success, allowing the thread to pass; otherwise it returns
     * a value indicating that the acquisition attempt failed. The acquireSharedInterruptibly
     * method interprets failure to mean that the thread should be placed on the queue of waiting threads.
     * Similarly, signal calls releaseShared, which causes tryReleaseShared to be consulted. 
     * The TRyReleaseShared implementation unconditionally sets the latch state to open and indicates
     * (through its return value) that the synchronizer is in a fully released state. This causes AQS to
     * let all waiting threads attempt to reacquire the synchronizer, and acquisition will now succeed 
     * because tryAcquireShared returns success.*/
    private class Sync extends AbstractQueuedSynchronizer {
        //callback from acquireSharedInterruptibly()
        protected int tryAcquireShared(int ignored) {
            // Succeed if latch is open (state == 1), else fail
            return (getState() == 1) ? 1 : -1;
        }

        //callback from releaseShared()
        protected boolean tryReleaseShared(int ignored) {
            setState(1); // Latch is now open
            return true; // Other threads may now be able to acquire

        }
    }
}/*PneShotLatch is a fully functional, usable, performant synchronizer, implemented in only twenty 
 * or so lines of code. Of course, it is missing some useful feature - ssuch as timed acquisition
 * or the ability to inspect the latch statebut these are easy to implement as well, since 
 * AQS provides timed versions of the acquisition methods and utility methods for common inspection operations.
 * 
 * OneShotLatch could have been implemented by extending AQS rather than delegating to it,
 * but this is undesirable for several reasons [EJ Item 14]. Doing so would undermine 
 * the simple (two-method) interface of OneShotLatch, and while the public methods of 
 * AQS won't allow callers to corrupt the latch state, callers could easily use them incorrectly.
 * None of the synchronizers in java.util.concurrent extends AQS directly - they all delegate 
 * to private inner subclasses of AQS instead*/
