package outoftime;

import java.util.*;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * OutOfTime
 * <p/>
 * Class illustrating confusing Timer behavior
 *
 * Another problem with Timer is that it behaves poorly if a TimerTask throws an unchecked exception.
 * The Timer thread doesn't catch the exception, so an unchecked exception thrown from a TimerTask
 * terminates the timer thread. Timer also doesn't resurrect the thread in this situation;
 * instead, it erroneously assumes the entire Timer was cancelled. In this case,
 * TimerTasks that are already scheduled but not yet executed are never run, and new tasks cannot be scheduled. 
 * (This problem, called "thread leakage" 
 * 
 *OutOfTime in Listing 6.9 illustrates how a Timer can become confused in this manner and,
 * as confusion loves company, how the Timer shares its confusion with the next hapless caller that
 * tries to submit a TimerTask. You might expect the program to run for six seconds and exit, but
 * what actually happens is that it terminates after one second with an IllegalStateException whose
 * message text is "Timer already cancelled". ScheduledThreadPoolExecutor deals properly with
 * ill-behaved tasks; there is little reason to use Timer in Java 5.0 or later.
 */

public class OutOfTime {
    public static void main(String[] args) throws Exception {
        Timer timer = new Timer();
        timer.schedule(new ThrowTask(), 1);
        SECONDS.sleep(1);
        timer.schedule(new ThrowTask(), 1);
        SECONDS.sleep(5);
        
    }

    static class ThrowTask extends TimerTask {
        public void run() {
            throw new RuntimeException();
        }
    }
}
