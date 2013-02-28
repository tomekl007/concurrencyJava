package boundedbuffertesting;

import java.util.concurrent.atomic.*;

/**
 * XorShift
 *
 * @author Brian Goetz and Tim Peierls
 */

/*the xor-Shift function in Listing 12.4 (Marsaglia, 2003) is among 
 * the cheapest medium-quality random number functions. Starting it
 off with values based on hashCode and nanoTime makes the sums both 
 * unguessable and almost always different for each run.*/
public class XorShift {
    static final AtomicInteger seq = new AtomicInteger(8862213);
    int x = -1831433054;

    public XorShift(int seed) {
        x = seed;
    }

    public XorShift() {
        this((int) System.nanoTime() + seq.getAndAdd(129));
    }

    public int next() {
        x ^= x << 6; //^bitwise exlucive OR
        x ^= x >>> 21;
        x ^= (x << 7);
        return x;
    }
}
