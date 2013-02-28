/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package boundedbuffertesting;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tomasz Lelek
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({boundedbuffertesting.SemaphoreBoundedBufferTest.class})
public class PutTakeTest  {

    @BeforeClass
    public static void setUpClass() throws Exception {
        //disable 
        new PutTakeTest(10, 10, 100).test(); // sample parameters
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
         pool.shutdown();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        
    }
    
     protected static final ExecutorService pool = Executors.newCachedThreadPool();
     /*We presented a technique for mitigating this problem in Section 5.5.1, using a CountDownLatch 
      * as a starting gate and another as a finish gate. Another way to get the same effect is to
      * use a CyclicBarrier, initialized with the number of worker threads plus one, and have the worker 
      * threads and the test driver wait at the barrier at the beginning and end of their run. This ensures 
      * that all threads are up and running before any start working. PutTakeTest uses this technique to
      * coordinate starting and stopping the worker threads, creating more potential concurrent interleavings*/
    protected CyclicBarrier barrier;
    protected final SemaphoreBoundedBuffer<Integer> bb;
    protected final int nTrials, nPairs;
    protected final AtomicInteger putSum = new AtomicInteger(0);
    protected final AtomicInteger takeSum = new AtomicInteger(0);
//
//    public static void main(String[] args) throws Exception {
//        
//       
//    }

    public PutTakeTest(int capacity, int npairs, int ntrials) {
        this.bb = new SemaphoreBoundedBuffer<Integer>(capacity);
        this.nTrials = ntrials;
        this.nPairs = npairs;
        this.barrier = new CyclicBarrier(npairs * 2 + 1);
    }

    public void test() {
        try {
            for (int i = 0; i < nPairs; i++) {
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            barrier.await(); // wait for all threads to be ready - wszystkie nowo stroznone Thread
            //wywolaja barrier.await- oznacza to ze sa gotowe, CyclicBarrier sie resetuje
            
            barrier.await(); // wait for all threads to finish
            System.out.println("testing equals");         
/*The final trick employed by PutTakeTest is to use a deterministic termination criterion so that
 * no additional inter-thread coordination is needed to figure out when the test is finished.
 * The test method starts exactly as many producers as consumers and each of them puts or
 * takes the same number of elements, so the total number of items added and removed is the same. */
            assertEquals(putSum.get(), takeSum.get());
           
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }
/*PutTakeTest in Listings 12.5 and 12.6 starts N producer threads that generate
 * elements and enqueue them, and N consumer threads that dequeue them. Each thread
 * updates the checksum of the elements as they go in or out, using a per thread
 * checksum that is combined at the end of the test run so as to add no more
 * synchronization or contention than required to test the buffer.*/
    public class Producer implements Runnable {
        public void run() {
            try {
                System.out.println("producer " + Thread.currentThread().toString());
                int seed = (this.hashCode() ^ (int) System.nanoTime());//here make random seed
                System.out.println("seed : " +seed);
                int sum = 0;
                barrier.await();
                for (int i = nTrials; i > 0; --i) {
                    bb.put(seed);
                    sum += seed;
                    seed = xorShift(seed);//randomize
                }
                putSum.getAndAdd(sum);
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public class Consumer implements Runnable {
        public void run() {
       System.out.println("consumer " + Thread.currentThread().toString());

            try {
                barrier.await();
                int sum = 0;
                for (int i = nTrials; i > 0; --i) {
                    sum += bb.take();
                }
                takeSum.getAndAdd(sum);
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    /*Tests like PutTakeTest tend to be good at finding safety violations. 
     * For example, a common error in implementing semaphore-controlled buffers
     * is to forget that the code actually doing the insertion and extraction
     * requires mutual exclusion (using synchronized or ReentrantLock). A sample
     * run of PutTakeTest with a version of BoundedBuffer that omits making 
     * doInsert and doExtract synchronized fails fairly quickly. 
     * Running PutTakeTest with a few dozen threads iterating a few
     * million times on buffers of various capacity on various systems
     * increases our confidence about the lack of data corruption in put and take*/
    
    
    
    
    
}
