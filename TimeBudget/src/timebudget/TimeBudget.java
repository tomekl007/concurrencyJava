/*
 uses the timed version of invokeAll to submit multiple tasks to an 
 * ExecutorService and retrieve the results. The invokeAll method takes
 * a collection of tasks and returns a collection of Futures. The two
 * collections have identical structures; invokeAll adds the Futures 
 * to the returned collection in the order imposed by the task collection's
 * iterator, thus allowing the caller to associate a Future with the Callable
 * it represents. The timed version of invokeAll will return when all the tasks 
 * have completed, the calling thread is interrupted, or the timeout expires. Any 
 * tasks that are not complete when the timeout expires are cancelled. On return from 
 * invokeAll, each task will have either completed normally or been cancelled; the
 * client code can call get or isCancelled to find out which.
 */
package timebudget;

/**
 *
 * @author Tomasz Lelek
 */
import java.util.*;
import java.util.concurrent.*;

/**
 * QuoteTask
 * <p/>
 * Requesting travel quotes under a time budget
 *
 * @author Brian Goetz and Tim Peierls
 */
public class TimeBudget {
    private static ExecutorService exec = Executors.newCachedThreadPool();

    public List<TravelQuote> getRankedTravelQuotes(TravelInfo travelInfo, Set<TravelCompany> companies,
                                                   Comparator<TravelQuote> ranking, long time, TimeUnit unit)
            throws InterruptedException {
        List<QuoteTask> tasks = new ArrayList<QuoteTask>();
        for (TravelCompany company : companies)
            tasks.add(new QuoteTask(company, travelInfo));

        List<Future<TravelQuote>> futures = exec.invokeAll(tasks, time, unit);

        List<TravelQuote> quotes =
                new ArrayList<TravelQuote>(tasks.size());
        Iterator<QuoteTask> taskIter = tasks.iterator();
      //  Thread.sleep(10000);
        for (Future<TravelQuote> f : futures) {
            QuoteTask task = taskIter.next();
            try {
                System.out.println("f.get");
                quotes.add(f.get());
                //Thread.sleep(30000);
            } catch (ExecutionException e) {
                System.out.println("ExecutionExcepion");
                quotes.add(task.getFailureQuote(e.getCause()));
            } catch (CancellationException e) {
                System.out.println("CancellactionEx");
                quotes.add(task.getTimeoutQuote(e));
            }
        }

       // Collections.sort(quotes, ranking);
        return quotes;
    }

}

class QuoteTask implements Callable<TravelQuote> {
    private final TravelCompany company;
    private final TravelInfo travelInfo;

    public QuoteTask(TravelCompany company, TravelInfo travelInfo) {
        this.company = company;
        this.travelInfo = travelInfo;
    }

    TravelQuote getFailureQuote(Throwable t) {
        return null;
    }

    TravelQuote getTimeoutQuote(CancellationException e) {
        return null;
    }

    public TravelQuote call() throws Exception {
        System.out.println("call");
        Thread.sleep(10000);
        return company.solicitQuote(travelInfo);
    }
}

interface TravelCompany {
    TravelQuote solicitQuote(TravelInfo travelInfo) throws Exception;
}

interface TravelQuote {
}

interface TravelInfo {
}