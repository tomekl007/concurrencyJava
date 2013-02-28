/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package boundedbuffertesting;

import java.lang.management.ThreadInfo;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tomasz Lelek
 */
public class SemaphoreBoundedBufferTest {
    
    
    public SemaphoreBoundedBufferTest() {
       
    }
    
    @BeforeClass
    public static void setUpClass() {
       
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isEmpty method, of class SemaphoreBoundedBuffer.
     */
  /*  @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        SemaphoreBoundedBuffer instance = null;
        boolean expResult = false;
        boolean result = instance.isEmpty();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isFull method, of class SemaphoreBoundedBuffer.
     */
  /*  @Test
    public void testIsFull() {
        System.out.println("isFull");
        SemaphoreBoundedBuffer instance = null;
        boolean expResult = false;
        boolean result = instance.isFull();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of put method, of class SemaphoreBoundedBuffer.
     */
  /*  @Test
    public void testPut() throws Exception {
        System.out.println("put");
        Object x = null;
        SemaphoreBoundedBuffer instance = null;
        instance.put(x);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of take method, of class SemaphoreBoundedBuffer.
     */
  /*  @Test
    public void testTake() throws Exception {
        System.out.println("take");
        SemaphoreBoundedBuffer instance = null;
        Object expResult = null;
        Object result = instance.take();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
    
    /*The most basic unit tests for BoundedBuffer are similar to what we'd
     * use in a sequential contextcreate a bounded buffer, call its methods,
     * and assert post-conditions and invariants. Some invariants that quickly 
     * come to mind are that a freshly created buffer should identify itself as
     * empty, and also as not full. A similar but slightly more complicated safety
     * test is to insert N elements into a buffer with capacity N (which should
     * succeed without blocking), and test that the buffer recognizes that it is
     * full (and not empty). JUnit test methods for these properties are shown in*/
    
    private static final long LOCKUP_DETECT_TIMEOUT = 1000;
    private static final int CAPACITY = 10000;
    private static final int THRESHOLD = 10000;
    
    @Test
    public void testIsEmptyWhenConstructed() {
        SemaphoreBoundedBuffer<Integer> bb = new SemaphoreBoundedBuffer<Integer>(10);
        assertTrue(bb.isEmpty());
        assertFalse(bb.isFull());
        
     
    }
   @Test
    public void testIsFullAfterPuts() throws InterruptedException {
        SemaphoreBoundedBuffer<Integer> bb = new SemaphoreBoundedBuffer<Integer>(10);
        for (int i = 0; i < 10; i++)
            bb.put(i);
        assertTrue(bb.isFull());
        assertFalse(bb.isEmpty());
    }//These simple test methods are entirely sequential. 
   //Including a set of sequential tests in your test suite is often helpful, since they can disclose
   //when a problem is not related to concurrency issues before you start looking for data races.

   
   /*Listing 12.3 shows an approach to testing blocking operations. It creates a "taker" thread that 
    * attempts to take an element from an empty buffer. If take succeeds, it registers failure. The 
    * test runner thread starts the taker thread, waits a long time, and then interrupts it. If the 
    * taker thread has correctly blocked in the take operation, it will throw InterruptedException, 
    * and the catch block for this exception treats this as success and lets the thread exit. The main
    * test runner thread then attempts to join with the taker thread and verifies that the join returned 
    * successfully by calling Thread.isAlive; if the taker thread responded to the interrupt, the join 
    * should complete quickly. */
   @Test
    public void testTakeBlocksWhenEmpty() {
        final SemaphoreBoundedBuffer<Integer> bb = new SemaphoreBoundedBuffer<Integer>(10);
        Thread taker = new Thread() {
            public void run() {
                try {
                    int unused = bb.take();
                    fail(); // if we get here, it's an error
                } catch (InterruptedException success) {
                  
                    System.out.println("interruption");//lapie i watek umiera
                }
            }
        };
        try {
        
            
            taker.start();
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            taker.interrupt();
            /*The timed join ensures that the test completes even if take gets stuck in some unexpected way.
             * This test method tests several properties of takenot only that it blocks but that, when 
             * interrupted, it throws InterruptedException. This is one of the few cases in which it is
             * appropriate to subclass Thread explicitly instead of using a Runnable in a pool: in
             * order to test proper termination with join. The same approach can be used to test
             * that the taker thread unblocks after an element is placed in the queue by the main thread.*/
           taker.join(LOCKUP_DETECT_TIMEOUT);
            assertFalse(taker.isAlive());//jesli dalej bedzie alive to false
        } catch (Exception unexpected) {
            fail();
        }
    }
 
    class Big {
        double[] data = new double[100000];
    }
    /*Undesirable memory retention can be easily tested with heap-inspection tools that 
     * measure application memory usage; a variety of commercial and open-source heap-profiling 
     * tools can do this. The testLeak method in Listing 12.7 contains placeholders 
     * for a heap-inspection tool to snapshot the heap, which forces a garbage collection[5] 
     * and then records information about the heap size and memory usage. */
   
           /*The testLeak method inserts several large objects into a bounded buffer and 
            * then removes them; memory usage at heap snapshot #2 should be approximately
            * the same as at heap snapshot #1. On the other hand, if doExtract forgot to 
            * null out the reference to the returned element (items[i]=null), the reported
            * memory usage at the two snapshots would definitely not be the same. 
            * (This is one of the few times where explicit nulling is necessary; 
            * most of the time, it is either not helpful or actually harmful [EJ Item 5].)*/
    /*@Test
   public void testLeak() throws InterruptedException {
        SemaphoreBoundedBuffer<Big> bb = new SemaphoreBoundedBuffer<Big>(CAPACITY);
        int heapSize1 = snapshotHeap();
        for (int i = 0; i < CAPACITY; i++)
            bb.put(new Big());
        for (int i = 0; i < CAPACITY; i++)
            bb.take();
        int heapSize2 = snapshotHeap();
        assertTrue(Math.abs(heapSize1 - heapSize2) < THRESHOLD);
    }

    private int snapshotHeap() {
        // Snapshot heap and return heap size 
        return 0;
    }
*/
 
    
    //-----------Test thread pool
     public final TestingThreadFactory threadFactory =  new TestingThreadFactory();
     
    /*If the core pool size is smaller than the maximum size, the thread pool should grow as 
     * demand for execution increases. Submitting long-running tasks to the pool makes the 
     * number of executing tasks stay constant for long enough to make a few assertions, 
     * such as testing that the pool is expanded as expected, as shown in Listing 12.9.*/
    @Test
    public void testPoolExpansion() throws InterruptedException {
        System.out.println("->>here");
        int MAX_SIZE = 10;
       
        ExecutorService exec = Executors.newFixedThreadPool(MAX_SIZE, threadFactory);
        
       
             
        

        for (int i = 0; i < 10 * MAX_SIZE; i++)
            exec.execute(new Runnable() {
                public void run() {
                    try {
                        System.out.println("in :  " +Thread.currentThread());
                        
                        Thread.sleep(Long.MAX_VALUE);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        for (int i = 0;
             i < 20 && threadFactory.numCreated.get() < MAX_SIZE;
             i++)
            Thread.sleep(100);//to ma wstrzymac az utrorza sie 10 thread
        
        System.out.println("num created : "  + threadFactory.numCreated.get());
        assertEquals(threadFactory.numCreated.get(), MAX_SIZE);
        exec.shutdownNow();
        
    }
}

/*We can instrument thread creation by using a custom thread factory. TestingThreadFactory 
 * in Listing 12.8 maintains a count of created threads; test cases can then verify the 
 * number of threads created during a test run. TestingThreadFactory could be extended to
 * return a custom Thread that also records when the thread terminates, so that test cases
 * can verify that threads are reaped in accordance with the execution policy*/
class TestingThreadFactory implements ThreadFactory {
    public final AtomicInteger numCreated = new AtomicInteger();
    private final ThreadFactory factory = Executors.defaultThreadFactory();

    public Thread newThread(Runnable r) {
        int nr = numCreated.incrementAndGet();
        System.out.println("new " + nr); 
        return factory.newThread(r);
    }
}
   
   
   
   

