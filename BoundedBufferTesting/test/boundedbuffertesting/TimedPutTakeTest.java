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
@Suite.SuiteClasses({boundedbuffertesting.PutTakeTest.class, boundedbuffertesting.SemaphoreBoundedBufferTest.class, boundedbuffertesting.BarrierTimer.class})
public class TimedPutTakeTest extends PutTakeTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        /*We can learn several things from running TimedPutTakeTest. One is the throughput of 
         * the producer-consumer handoff operation for various combinations of parameters; 
         * another is how the bounded buffer scales with different numbers of threads; a 
         * third is how we might select the bound size. Answering these questions requires
         * running the test for various combinations of parameters, so we'll need amain test
         * driver, shown in Listing 12.13*/
        int tpt = 10; // trials per thread
        for (int cap = 1; cap <= 1000; cap *= 10) {
            System.out.println("-->Capacity: " + cap);
            for (int pairs = 1; pairs <= 24; pairs *= 2) {
                TimedPutTakeTest t = new TimedPutTakeTest(cap, pairs, tpt);
                System.out.print("Pairs: " + pairs + "\t");
                t.test();
                System.out.print("\t");
                Thread.sleep(1000);
                t.test();
                System.out.println();
                Thread.sleep(1000);
            }
        }
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
         PutTakeTest.pool.shutdown();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        
    }
    
      private BarrierTimer timer = new BarrierTimer();

    public TimedPutTakeTest(int cap, int pairs, int trials) {
        super(cap, pairs, trials);
        barrier = new CyclicBarrier(nPairs * 2 + 1, timer);
    }

    @Test
    public void test() {
        try {
            timer.clear();
            for (int i = 0; i < nPairs; i++) {
                pool.execute(new PutTakeTest.Producer());
                pool.execute(new PutTakeTest.Consumer());
            }
            barrier.await();
            barrier.await();
            long time = timer.getTime();
            System.out.println("time = " + time + " pairs : " + nPairs + " " + nTrials);
            long nsPerItem =  time / (nPairs * (long) nTrials);//im nizsze tym lepiej
            //ile przez ten czas zrobil "pracy" nTrials - ilosc wysylek, i odbiorow nPairs
            System.out.print("Throughput: " + nsPerItem + " ns/item");//
            assertEquals(putSum.get(), takeSum.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
