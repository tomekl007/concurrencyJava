/*
 shows a typical application of a timed Future.get. It generates a 
 * composite web page that contains the requested content plus an 
 * advertisement fetched from an ad server. It submits the ad-fetchin
 * g task to an executor, computes the rest of the page content, and 
 * then waits for the ad until its time budget runs out. If the get
 * times out, it cancels the ad-fetching task and uses a default advertisement instead
 */
package futuregetwithtimeout;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 *
 * @author Tomasz Lelek
 */
public class RenderWithTimeBudget {
    private static final Ad DEFAULT_AD = new Ad();
    private static final long TIME_BUDGET = 10;
    private static final ExecutorService exec = Executors.newCachedThreadPool();

    Page renderPageWithAd() throws InterruptedException {
        long endNanos = System.nanoTime() + TIME_BUDGET;
        Future<Ad> f = exec.submit(new FetchAdTask());
        // Render the page while waiting for the ad
        Page page = renderPageBody();
        Ad ad;
        try {
            // Only wait for the remaining time budget
            long timeLeft = endNanos - System.nanoTime();
            System.out.println("before get");
            ad = f.get(timeLeft, NANOSECONDS);
            System.out.println("after get");
        } catch (ExecutionException e) {
            ad = DEFAULT_AD;
        } catch (TimeoutException e) {
            System.out.println("TimeoutException");
            ad = DEFAULT_AD;
            f.cancel(true);//The true parameter to Future.cancel means that the task thread can be interrupted if the task is currently running
        }
        page.setAd(ad);
        return page;
    }

    Page renderPageBody() { return new Page(); }


    static class Ad {
    }

    static class Page {
        public void setAd(Ad ad) { }
    }

    static class FetchAdTask implements Callable<Ad> {
        public Ad call() throws InterruptedException {
            Thread.sleep(2000);
            return new Ad();
        }
    }

}