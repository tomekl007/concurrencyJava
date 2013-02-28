package reclosablethreadgatecondwait;

//import net.jcip.annotations.*;

/**
 * ThreadGate
 * <p/>
 * Recloseable gate using wait and notifyAll
 *
 * It is easy to develop a recloseable ThreadGate class using condition waits, as shown in Listing 14.9.
 * ThreadGate lets the gate be opened and closed, providing an await method that blocks until the
 * gate is opened. The open method uses notifyAll because the semantics of this class fail the 
 * "one-in, one-out" test for single notification. 
 */
//@ThreadSafe
public class ThreadGate {
    // CONDITION-PREDICATE: opened-since(n) (isOpen || generation>n)
  //  @GuardedBy("this")
    private boolean isOpen;
    //@GuardedBy("this")
    private int generation;

    public synchronized void close() {
        isOpen = false;
    }

    /*Since ThreadGate only supports waiting for the gate to open, it performs notification only in open; 
     * to support both "wait for open" and "wait for close" operations, it would have to notify in both open
     * and close. This illustrates why state-dependent classes can be fragile to maintainthe addition of 
     * a new statedependent operation may require modifying many code paths that modify the object state 
     * so that the appropriate notifications can be performed. */
    public synchronized void open() {
        System.out.println("open");
        ++generation;
        isOpen = true;
        notifyAll();
    }

    
    /*The condition predicate used by await is more complicated than simply testing isOpen.
     * This is needed because if N threads are waiting at the gate at the time it is opened
     * , they should all be allowed to proceed. But, if the gate is opened and closed in 
     * rapid succession, all threads might not be released if await examines only isOpen: 
     * by the time all the threads receive the notification, reacquire the lock, and emerge 
     * from wait, the gate may have closed again. So ThreadGate uses a somewhat more complicated
     * condition predicate: every time the gate is closed, a "generation" counter is incremented,
     * and a thread may pass await if the gate is open now or if the gate has opened since this
     * thread arrived at the gate. */
    // BLOCKS-UNTIL: opened-since(generation on entry)
    public synchronized void await() throws InterruptedException {
        int arrivalGeneration = generation;// 0 = 0
        //pozniej w petli sprawdzam, i jesli open() to generation ++
        //wiec warunek while nie bedzie spelniony
        while (!isOpen && arrivalGeneration == generation)
            wait();
    }
}
