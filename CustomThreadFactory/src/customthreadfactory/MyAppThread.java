package customthreadfactory;

import java.util.concurrent.atomic.*;
import java.util.logging.*;

/**
 * MyAppThread
 * <p/>
 * Custom thread base class
 *
 * The interesting customization takes place in MyAppThread, shown in Listing 8.7,
 * which lets you provide a thread name, sets a custom UncaughtException-Handler 
 * that writes a message to a Logger, maintains statistics on how many threads
 * have been created and destroyed, and optionally writes a debug message to the
 * log when a thread is created or terminates. 
 */
public class MyAppThread extends Thread {
    public static final String DEFAULT_NAME = "MyAppThread";
    private static volatile boolean debugLifecycle = false;
    private static final AtomicInteger created = new AtomicInteger();
    private static final AtomicInteger alive = new AtomicInteger();
    private static final Logger log = Logger.getAnonymousLogger();

    public MyAppThread(Runnable r) {
        this(r, DEFAULT_NAME);
    }

    public MyAppThread(Runnable runnable, String name) {
        super(runnable, name + "-" + created.incrementAndGet());//do kontruktora Thread
        setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t,
                                          Throwable e) {
                log.log(Level.SEVERE,
                        "UNCAUGHT in thread " + t.getName(), e);
            }
        });
    }

    public void run() {
        // Copy debug flag to ensure consistent value throughout.
        boolean debug = debugLifecycle;
        //System.out.println(debug);
        if (debug) log.log(Level.FINE, "Created " + getName());
        try {
            alive.incrementAndGet();
            super.run();
        } finally {
            alive.decrementAndGet();
            if (debug) log.log(Level.FINE, "Exiting " + getName());
            printLogger();
        }
    }

    public static int getThreadsCreated() {
        return created.get();
    }

    public static int getThreadsAlive() {
        return alive.get();
    }

    public static boolean getDebug() {
        return debugLifecycle;
    }

    public static void setDebug(boolean b) {
        debugLifecycle = b;
    }
    
    //my method
    public void printLogger(){
        System.out.println(log);
    }
}
