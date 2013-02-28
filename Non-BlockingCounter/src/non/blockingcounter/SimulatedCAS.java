package non.blockingcounter;



/**
 * SimulatedCAS
 * <p/>
 * Simulated CAS operation
 *
 * CAS has three operands - a memory location V on which to operate, the expected old value A, and the new value B. 
 * CAS atomically updates V to the new value B, but only if the value in V matches the expected old value A; 
 * otherwise it does nothing. In either case, it returns the value currently in V. 
 * (The variant called compare-and-set instead returns whether the operation succeeded.) 
 * CAS means "I think V should have the value A; if it does, put B there, otherwise don't change it but tell me I
 * was wrong." CAS is an optimistic technique - it proceeds with the update in the hope of success, 
 * and can detect failure if another thread has updated the variable since it was last examined.
 * 
 * SimulatedCAS in Listing 15.1 illustrates the semantics (but not the implementation or performance) of CAS
 */

//ThreadSafe
public class SimulatedCAS {
   // @GuardedBy("this") 
    private int value;
    
    public SimulatedCAS(){
    this.value = 0;
}

    public synchronized int get() {
        return value;
    }

    public synchronized int compareAndSwap(int expectedValue,
                                           int newValue) {
        int oldValue = value;
        if (oldValue == expectedValue)
            value = newValue;
        return oldValue;
    }

    public synchronized boolean compareAndSet(int expectedValue,
                                              int newValue) {
        return (expectedValue
                == compareAndSwap(expectedValue, newValue));
    }
}
