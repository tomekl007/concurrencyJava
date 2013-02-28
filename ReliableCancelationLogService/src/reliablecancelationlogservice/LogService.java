package reliablecancelationlogservice;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.*;

//import net.jcip.annotations.*;

/**
 * LogService
 * <p/>
 * Adding reliable cancellation to LogWriter
 *
 * The way to provide reliable shutdown for LogWriter is to fix the race condition, 
 * which means making the submission of a new log message atomic. But we don't
 * want to hold a lock while trying to enqueue the message, since put could block. Instead,
 * we can atomically check for shutdown and conditionally increment a counter to "reserve
 * the right to submit a message, as shown in LogService in Listing 7.15. 
 */
public class LogService {
    private final BlockingQueue<String> queue;
    private final LoggerThread loggerThread;
    private final PrintWriter writer;
   // @GuardedBy("this")
    private boolean isShutdown;
   // @GuardedBy("this")
    private int reservations;

    
    public LogService(Writer writer) {
      
 
        System.out.println("constructor" + writer);
        this.queue = new LinkedBlockingQueue<String>();
        this.loggerThread = new LoggerThread();
        this.writer = new PrintWriter(writer);
    }

    public void start() {
        loggerThread.start();
    }

    public void stop() {
        synchronized (this) {
            isShutdown = true;
        }
        loggerThread.interrupt();
    }

    public void log(String msg) throws InterruptedException {
        synchronized (this) {
            if (isShutdown)
                throw new IllegalStateException(/*...*/);
            ++reservations;
        }
          System.out.println("put reservation = " +reservations);
        System.out.println("LogService put: " + msg  );
        queue.put(msg);
    }

    private class LoggerThread extends Thread {
        public void run() {
            try {
                while (true) {
                    try {
                        synchronized (LogService.this) {
                            if (isShutdown && reservations == 0)
                                break;
                        }
                        String msg = queue.take();
                        synchronized (LogService.this) {
                            --reservations;
                        }
                        System.out.println("take reservation = " +reservations);
                        System.out.println("LoggerThread take form queue : " + msg  );
                        writer.println(msg);
                    } catch (InterruptedException e) { /* retry */
                    }
                }
            } finally {
                writer.close();
            }
        }
    }
}

