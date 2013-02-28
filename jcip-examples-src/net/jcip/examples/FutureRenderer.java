package net.jcip.examples;

import java.util.*;
import java.util.concurrent.*;
import static net.jcip.examples.LaunderThrowable.launderThrowable;

/**
 * FutureRenderer
 * <p/>
 * Waiting for image download with \Future
 *
 * @author Brian Goetz and Tim Peierls
 */
public abstract class FutureRenderer {
    private final ExecutorService executor = Executors.newCachedThreadPool();

    void renderPage(CharSequence source) {
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        Callable<List<ImageData>> task =
                new Callable<List<ImageData>>() {
                    public List<ImageData> call() {
                        List<ImageData> result = new ArrayList<ImageData>();
                        for (ImageInfo imageInfo : imageInfos)
                            result.add(imageInfo.downloadImage());
                        return result;
                    }
                };

        /*we create a Callable to download all the images, and submit it to an ExecutorService.
         * This returns a Future describing the task's execution; when the mai
         * n task gets to the point where it needs the images, it waits for the result by
         * calling Future.get. If we're lucky, the results will already be ready by the
         * time we ask; otherwise, at least we got a head start on downloading the images.*/
        Future<List<ImageData>> future = executor.submit(task);//to uruchomi dodatkowy thread
        renderText(source);

        try {
            List<ImageData> imageData = future.get();
            for (ImageData data : imageData)
                renderImage(data);
            /*The state-dependent nature of get means that the caller need not be aware of the
             * state of the task, and the safe publication properties of task submission and 
             * result retrieval make this approach thread-safe. The exception handling code 
             * surrounding Future.get deals with two possible problems: that the task encountered 
             * an Exception, or the thread calling get was interrupted before the results were
             * available.*/
        } catch (InterruptedException e) {
            // Re-assert the thread's interrupted status
            Thread.currentThread().interrupt();
            // We don't need the result, so cancel the task too
            future.cancel(true);
        } catch (ExecutionException e) {
            throw launderThrowable(e.getCause());
        }
    }

    interface ImageData {
    }

    interface ImageInfo {
        ImageData downloadImage();
    }

    abstract void renderText(CharSequence s);

    abstract List<ImageInfo> scanForImageInfo(CharSequence s);

    abstract void renderImage(ImageData i);
}
