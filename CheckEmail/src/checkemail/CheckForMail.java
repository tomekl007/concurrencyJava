package checkemail;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * CheckForMail
 * <p/>
 * Using a private \Executor whose lifetime is bounded by a method call
 *
 * If a method needs to process a batch of tasks and does not return until all the tasks are finished, 
 * it can simplify service lifecycle management by using a private Executor whose lifetime is bounded 
 * by that method. (The invokeAll and invokeAny methods can often be useful in such situations.)
 * 
 * The checkMail method in Listing 7.20 checks for new mail in parallel on a
 * number of hosts. It creates a private executor and submits a task for each
 * host: it then shuts down the executor and waits for termination, which occurs 
 * when all the mail-checking tasks have completed.[4] 
 * 
 * [4] The reason an AtomicBoolean
 * is used instead of a volatile boolean is that in order to access the hasNewMail flag 
 * from the inner Runnable, it would have to be final, which would preclude modifying
 */
public class CheckForMail {
    public boolean checkMail(Set<String> hosts, long timeout, TimeUnit unit)
            throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        final AtomicBoolean hasNewMail = new AtomicBoolean(false);
        try {
            for (final String host : hosts)
                exec.execute(new Runnable() {
                    public void run() {
                        System.out.println("run");
                        if (checkMail(host))
                            hasNewMail.set(true);
                    }
                });
        } finally {
            exec.shutdown();
           
            exec.awaitTermination(timeout, unit);//czeka timeout, i przechodzi dalej niewazne czy taski zakoczyly sie poprawnie czy nie
             
        }
        return hasNewMail.get();
    }

    private boolean checkMail(String host) {
        
        // Check for mail
        return false;
    }
}
