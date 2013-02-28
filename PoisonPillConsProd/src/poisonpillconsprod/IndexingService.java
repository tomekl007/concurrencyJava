package poisonpillconsprod;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.*;

/**
 * IndexingService
 * <p/>
 * Shutdown with poison pill
 *
 *Another way to convince a producer-consumer service to shut down is with a poison pill: 
 * a recognizable object placed on the queue that means "when you get this, stop." With a FIFO queue, 
 * poison pills ensure that consumers finish the work on their queue before shutting down, 
 * since any work submitted prior to submitting the poison pill will be retrieved before
 * the pill; producers should not submit any work after putting a poison pill on the queue. 
 * 
 * Poison pills work only when the number of producers and consumers is known.
 * The approach in IndexingService can be extended to multiple producers by having each producer
 * place a pill on the queue and having the consumer stop only when it receives Nproducers pills. 
 * It can be extended to multiple consumers by having each producer place Nconsumers pills on the queue,
 * though this can get unwieldy with large numbers of producers and consumers. Poison pills work 
 * reliably only with unbounded queues.
 */
public class IndexingService {
    private static final int CAPACITY = 1000;
    private static final File POISON = new File("");
    private final IndexerThread consumer = new IndexerThread();
     private final IndexerThread consumer2 = new IndexerThread();
   
    private final CrawlerThread producer = new CrawlerThread();
    private final BlockingQueue<File> queue;
    private final FileFilter fileFilter;
    private final File root;

    public IndexingService(File root, final FileFilter fileFilter) {
        this.root = root;
        this.queue = new LinkedBlockingQueue<File>(CAPACITY);
        this.fileFilter = new FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || fileFilter.accept(f);
            }
        };
    }

    private boolean alreadyIndexed(File f) {
        return false;
    }

    class CrawlerThread extends Thread {
        public void run() {
            try {
                crawl(root);
            } catch (InterruptedException e) { /* fall through */
            } finally {
                while (true) {
                    try {
                        System.out.println("crawler put POISON " + POISON.toString());
                        queue.put(POISON);        
                        queue.put(POISON);

                       break;
                    } catch (InterruptedException e1) { /* retry */
                    }
                }
            }
        }

        private void crawl(File root) throws InterruptedException {
            File[] entries = root.listFiles(fileFilter);
            if (entries != null) {
                for (File entry : entries) {
                    if (entry.isDirectory())
                        crawl(entry);
                    else if (!alreadyIndexed(entry))
                        queue.put(entry);
                }
            }
        }
    }

    class IndexerThread extends Thread {
        public void run() {
            try {
                while (true) {
                    File file = queue.take();
                    System.out.println("indexerThread nr : " + Thread.currentThread().toString() + " take "  + file.toString());
                    if (file == POISON)
                        break;
                    else
                        indexFile(file);
                }
            } catch (InterruptedException consumed) {
            }
        }

        public void indexFile(File file) {
            /*...*/
        };
    }

    public void start() {
        producer.start();
     //  producer.start();
        consumer.start();
        consumer2.start();
        
    }

    public void stop() {
        producer.interrupt();
    }

    public void awaitTermination() throws InterruptedException {
        consumer.join();
    }
}
