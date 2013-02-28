package deadlockinsinglethreadexecuor;

import java.util.concurrent.*;

/**
 * ThreadDeadlock
 * <p/>
 * Task that deadlocks in a single-threaded Executor
 *
 * ThreadDeadlock in Listing 8.1 illustrates thread starvation deadlock.
 * Render-PageTask submits two additional tasks to the Executor to fetch the
 * page header and footer, renders the page body, waits for the results of
 * the header and footer tasks, and then combines the header, body, and footer
 * into the finished page. With a single-threaded executor, ThreadDeadlock will
 * always deadlock. Similarly, tasks coordinating amongst themselves with a barrier
 * could also cause thread starvation deadlock if the pool is not big enough
 */
public class ThreadDeadlock {
    ExecutorService exec = Executors.newSingleThreadExecutor();

    public class LoadFileTask implements Callable<String> {
        private final String fileName;

        public LoadFileTask(String fileName) {
            
            this.fileName = fileName;
        }

        public String call() throws Exception {
            // Here's where we would actually read the file
            System.out.println("call");
            return "";
        }
    }

    public class RenderPageTask implements Callable<String> {
        public String call() throws Exception {
            Future<String> header, footer;
            header = exec.submit(new LoadFileTask("header.html"));
            footer = exec.submit(new LoadFileTask("footer.html"));
            String page = renderBody();
            // Will deadlock -- task waiting for result of subtask
           
            System.out.println("after");
             return header.get() + page + footer.get();
        }

        private String renderBody() {
            // Here's where we would actually render the page
            return "";
        }
    }
}
