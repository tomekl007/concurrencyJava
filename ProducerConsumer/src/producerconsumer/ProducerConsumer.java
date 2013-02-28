/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package producerconsumer;
import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.*;

/**
 * ProducerConsumer
 * <p/>
 * Producer and consumer tasks in a desktop search application
 *
 * @author Brian Goetz and Tim Peierls
 */
public class ProducerConsumer {
    //producer
    static class FileCrawler implements Runnable {
        private final BlockingQueue<File> fileQueue;
        private final FileFilter fileFilter;
        private final File root;

        public FileCrawler(BlockingQueue<File> fileQueue,
                           final FileFilter fileFilter,
                           File root) {
            this.fileQueue = fileQueue;
            this.root = root;
            this.fileFilter = new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || fileFilter.accept(f);
                }
            };
        }

        private boolean alreadyIndexed(File f) {
            return false;
        }

        public void run() {
            try {
                crawl(root);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void crawl(File root) throws InterruptedException {
            File[] entries = root.listFiles(fileFilter);
            if (entries != null) {
                for (File entry : entries){
                    if (entry.isDirectory())
                        crawl(entry);
                    else if (!alreadyIndexed(entry)){
                        
                        fileQueue.put(entry);
                System.out.println("crawl.put : " + entry.toPath());
                    }
                }
            }
        }
    }

    //consumer
    static class Indexer implements Runnable {
        private final BlockingQueue<File> queue;

        public Indexer(BlockingQueue<File> queue) {
            this.queue = queue;
        }

        public void run() {
            try {
                
                while (true){
                    File path = queue.take();
                    System.out.println("Thread nr : "+ Thread.currentThread().getName() + "  path.toPath(): " + path.toPath());
                    indexFile(path);
              
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void indexFile(File file) {
            // Index the file...
        };
    }

    private static final int BOUND = 10;
    private static final int N_CONSUMERS = Runtime.getRuntime().availableProcessors();

    public static void startIndexing(File[] roots) {
        BlockingQueue<File> queue = new LinkedBlockingQueue<File>(BOUND);
        FileFilter filter = new FileFilter() {
            public boolean accept(File file) {
                return true;
            }
        };

        for (File root : roots){
            System.out.println("new FIle Crawler");
                   
            new Thread(new FileCrawler(queue, filter, root)).start();
        }
        

        for (int i = 0; i < N_CONSUMERS; i++){
             System.out.println("new Indexer");
            new Thread(new Indexer(queue)).start();
        }
    }
}