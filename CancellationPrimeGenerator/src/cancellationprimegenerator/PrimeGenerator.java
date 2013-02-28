/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cancellationprimegenerator;

import static java.util.concurrent.TimeUnit.SECONDS;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;



/**
 * PrimeGenerator
 * <p/>
 * Using a volatile field to hold cancellation state
 *
 *One such cooperative mechanism is setting a "cancellation requested" flag that the
 * task checks periodically; if it finds the flag set, the task terminates early. 
 * PrimeGenerator in Listing 7.1, which enumerates prime numbers until it 
 * is cancelled, illustrates this technique. The cancel method sets the cancelled flag,
 * and the main loop polls this flag before searching for the next prime number.
 * (For this to work reliably, cancelled must be volatile.) 
 */
//@ThreadSafe
public class PrimeGenerator implements Runnable {
    private static ExecutorService exec = Executors.newCachedThreadPool();

  //  @GuardedBy("this")
    private final List<BigInteger> primes
            = new ArrayList<>();
    private volatile boolean cancelled;

    public void run() {
        BigInteger p = BigInteger.ONE;
        while (!cancelled) {
            p = p.nextProbablePrime();
            synchronized (this) {
                System.out.println("found prime : " + p);
                primes.add(p);
               
            }
        }
    }

    public void cancel() {
        cancelled = true;
    }

    public synchronized List<BigInteger> get() {
        return new ArrayList<>(primes);
    }

    /*a sample use of this class that lets the prime generator run for one second before cancelling it.
     * The generator won't necessarily stop after exactly one second, since there may be some delay 
     * between the time that cancellation is requested and the time that the run loop next checks for
     * cancellation. The cancel method is called from a finally block to ensure that the prime generator
     * is cancelled even if the call to sleep is interrupted. If cancel were not called, the prime-seeking
     * thread would run forever, consuming CPU cycles and preventing the JVM from exitin*/
    static List<BigInteger> aSecondOfPrimes() throws InterruptedException {
        PrimeGenerator generator = new PrimeGenerator();
        exec.execute(generator);
        try {
            SECONDS.sleep(1);
        } finally {
            generator.cancel();
        }
        return generator.get();
    }
}