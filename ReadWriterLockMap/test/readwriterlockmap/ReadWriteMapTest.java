/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package readwriterlockmap;

import java.util.LinkedHashMap;
import java.util.Map;
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
import org.omg.PortableInterceptor.SUCCESSFUL;

/**
 *
 * @author Tomasz Lelek
 */
public class ReadWriteMapTest {
    
    public ReadWriteMapTest() {
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
     * Test of put method, of class ReadWriteMap.
     */
   Map<String,String> m = new LinkedHashMap<>();
   ReadWriteMap<String,String> rwmap = new ReadWriteMap<String,String>(m);
   CyclicBarrier barrier = new CyclicBarrier(1);
   
   @Test
   public void testWriteReadLock(){
       
       Thread writer1,writer2;
       Runnable r = new Runnable() {

           @Override
           public void run() {
              for(int i = 0 ; i < 10; i++){
              rwmap.put(String.valueOf(i), "some value");
              
       }
               try {
                   barrier.await();
               } catch (InterruptedException ex) {
                   Logger.getLogger(ReadWriteMapTest.class.getName()).log(Level.SEVERE, null, ex);
               } catch (BrokenBarrierException ex) {
                   Logger.getLogger(ReadWriteMapTest.class.getName()).log(Level.SEVERE, null, ex);
               }
      
           }
       };
       writer1 = new Thread(r);
       writer2 = new Thread(r);

       
       
         Thread reader1,reader2;
         Runnable run = new Runnable() {

           @Override
           public void run() {
              for(int i = 0 ; i < 10; i++){
              rwmap.get(String.valueOf(i));
       }
      
           }
       };
         reader1  = new Thread(run);
         reader2  = new Thread(run);

         
         
         writer1.start();
        try {
            barrier.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(ReadWriteMapTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BrokenBarrierException ex) {
            Logger.getLogger(ReadWriteMapTest.class.getName()).log(Level.SEVERE, null, ex);
        }
         reader1.start();
         reader2.start();
         assertTrue(reader1.isAlive() && reader2.isAlive());
        
         writer2.start();
         assertTrue(true);
         
       
       
   }

}
