/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package non.blockingcounter;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CasCounterTest {
    
    public CasCounterTest() {
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
    
    CyclicBarrier barrier = new CyclicBarrier(10);

    @Test
    public void testCas() throws InterruptedException, BrokenBarrierException{
        final CasCounter cc = new CasCounter();
        
        Runnable r = new Runnable() {

            @Override
            public void run() {
                System.out.println("in thread " + Thread.currentThread().toString() );
                for(int i = 0 ; i < 10; i++)
                  cc.increment();
                try {
                    barrier.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(CasCounterTest.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BrokenBarrierException ex) {
                    Logger.getLogger(CasCounterTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        
        for(int i = 0; i<10;i++){
            new Thread(r).start();
        }
        
        barrier.await();
        assertEquals(cc.getValue(),100);
        
    }
}