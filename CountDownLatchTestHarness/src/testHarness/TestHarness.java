/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testHarness;

import java.util.concurrent.*;

/**
 * TestHarness
 * <p/>
 * Using CountDownLatch for starting and stopping threads in timing tests
 *
 *  TestHarness creates a number of threads that run a given task concurrently.
 * It uses two latches, a "starting gate" and an "ending gate". The starting gate
 * is initialized with a count of one; the ending gate is initialized with a count
 * equal to the number of worker threads. The first thing each worker thread does is
 * wait on the starting gate; this ensures that none of them starts working until they
 * all are ready to start. The last thing each does is count down on the ending gate;
 * this allows the master thread to wait efficiently until the last of the worker threads
 * has finished, so it can calculate the elapsed time. 
 */
public class TestHarness {
    public long timeTasks(int nThreads, final Runnable task)
            throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread() {
                public void run() {
                    System.out.println("thread nr in timeTasks : " + Thread.currentThread().toString());
                    try {
                        //czeka az startGate.countDown
                        startGate.await();
                        try {
                            task.run();
                        } finally {
                            //endGate - 1 -> dzieki temu dopiero po zakonczeniu wszystkich watków
                            //endGate.await "przepusci dalej"
                            endGate.countDown();
                        }
                    } catch (InterruptedException ignored) {
                    }
                }
            };
            t.start();
        }

        long start = System.nanoTime();
        //po tym jak wszystkie zainicjalizowae startGate.coutnDown (czyli -1)
        
        startGate.countDown();
        
        endGate.await();
        
        long end = System.nanoTime();
        return end - start;
    }
}