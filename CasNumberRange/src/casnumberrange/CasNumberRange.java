/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package casnumberrange;

/**
 *
 * @author Tomasz Lelek
 */
import java.util.concurrent.atomic.*;

//import net.jcip.annotations.*;

/**
 * CasNumberRange
 * <p/>
 * Preserving multivariable invariants using CAS
 *
 *We can combine the technique from OneValueCache with atomic references to close the race condition by
 * atomically updating the reference to an immutable object holding the lower and upper bounds.
 * CasNumberRange in Listing 15.3 uses an AtomicReference to an IntPair to hold the state; by 
 * using compareAndSet it can update the upper or lower bound without the race conditions of NumberRange.
 */
//@ThreadSafe
        public class CasNumberRange {
    //@Immutable
            private static class IntPair {
        // INVARIANT: lower <= upper
        final int lower;
        final int upper;

        public IntPair(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }
    }

    private final AtomicReference<IntPair> values =
            new AtomicReference<IntPair>(new IntPair(0, 0));

    public int getLower() {
        return values.get().lower;
    }

    public int getUpper() {
        return values.get().upper;
    }

    public void setLower(int i) {
        while (true) {
            IntPair oldv = values.get();
            if (i > oldv.upper)
                throw new IllegalArgumentException("Can't set lower to " + i + " > upper");
            IntPair newv = new IntPair(i, oldv.upper);
            if (values.compareAndSet(oldv, newv))
                return;
        }
    }

    public void setUpper(int i) {
        while (true) {
            IntPair oldv = values.get();
            if (i < oldv.lower)
                throw new IllegalArgumentException("Can't set upper to " + i + " < lower");
            IntPair newv = new IntPair(oldv.lower, i);
            if (values.compareAndSet(oldv, newv))
                return;
        }
    }
}