package savingstatewebcrawler;

import java.net.URL;
import java.util.*;
import java.util.concurrent.*;



import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * WebCrawler
 * <p/>
 * Using TrackingExecutorService to save unfinished tasks for later execution
 *
 *WebCrawler in Listing 7.22 shows an application of trackingExecutor.
 * The work of a web crawler is often unbounded, so if a crawler must be
 * shut down we might want to save its state so it can be restarted later.
 * CrawlTask provides a getPage method that identifies what page it is working on.
 * When the crawler is shut down, both the tasks that did not start and those that
 * were cancelled are scanned and their URLs recorded, so that page-crawling tasks 
 * for those URLs can be added to the queue when the crawler restarts
 */
public abstract class WebCrawler {
    private volatile TrackingExecutor exec;
    //@GuardedBy("this") 
    private final Set<URL> urlsToCrawl = new HashSet<URL>();

    private final ConcurrentMap<URL, Boolean> seen = new ConcurrentHashMap<URL, Boolean>();
    private static final long TIMEOUT = 500;
    private static final TimeUnit UNIT = MILLISECONDS;

    
    public WebCrawler(URL startUrl) {
        urlsToCrawl.add(startUrl);
    }
    
    //my
    public Set<URL> getUrlsToCrawl(){
        return this.urlsToCrawl;
    }

    public synchronized void start() {
        exec = new TrackingExecutor(Executors.newCachedThreadPool());
        for (URL url : urlsToCrawl) submitCrawlTask(url);
        urlsToCrawl.clear();
    }

    public synchronized void stop() throws InterruptedException {
        try {
            saveUncrawled(exec.shutdownNow());
            if (exec.awaitTermination(TIMEOUT, UNIT))
                saveUncrawled(exec.getCancelledTasks());
        } finally {
            exec = null;
        }
    }

    protected abstract List<URL> processPage(URL url);

    private void saveUncrawled(List<Runnable> uncrawled) {
        for (Runnable task : uncrawled)
            urlsToCrawl.add(((CrawlTask) task).getPage());
    }

    private void submitCrawlTask(URL u) {
        exec.execute(new CrawlTask(u));
    }

    private class CrawlTask implements Runnable {
        private final URL url;

        CrawlTask(URL url) {
            this.url = url;
        }

        private int count = 1;

        boolean alreadyCrawled() {
            return seen.putIfAbsent(url, true) != null;
        }

        void markUncrawled() {
            
            seen.remove(url);
            System.out.printf("marking %s uncrawled%n", url);
        }

        public void run() {
            for (URL link : processPage(url)) {
                System.out.println("run after processPage");
                if (Thread.currentThread().isInterrupted())
                    return;
                submitCrawlTask(link);
            }
        }

        public URL getPage() {
            return url;
        }
    }
}
