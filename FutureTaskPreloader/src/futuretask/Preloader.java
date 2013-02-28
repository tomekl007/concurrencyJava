/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package futuretask;


import java.util.concurrent.*;

/**
 * Preloader
 *
 * Using FutureTask to preload data that is needed later
 *
 * @author 
 * FutureTask also acts like a latch. (FutureTask implements Future, which describes an abstract result-bearing computation [CPJ 4.3.3].) 
 * A computation represented by a FutureTask is implemented with a Callable, the result-bearing equivalent of Runnable, and can be in one
 * of three states: waiting to run, running, or completed. Completion subsumes all the ways a computation can complete, including normal 
 * completion, cancellation, and exception. Once a FutureTask enters the completed state, it stays in that state forever. The behavior of
 * Future.get depends on the state of the task. If it is completed, get returns the result immediately, and otherwise blocks until the 
 * task transitions to the completed state and then returns the result or throws an exception. FutureTask conveys the result from the 
 * thread executing the computation to the thread(s) retrieving the result; the specification of FutureTask guarantees that this transfer 
 * constitutes a safe publication of the result. 
 */



/*Preloader creates a FutureTask that describes the task of loading product information from a database and a 
 * thread in which the computation will be performed. It provides a start method to start the thread, since it 
 * is inadvisable to start a thread from a constructor or static initializer. When the program later needs the 
 * ProductInfo, it can call get, which returns the loaded data if it is ready, or waits for the load to complete
 * if not. 
 * 
*/
public class Preloader {
    ProductInfo loadProductInfo() throws DataLoadException {
        System.out.println("loadProductInfo");
        
        return null;
    }

    private final FutureTask<ProductInfo> future =
        new FutureTask<ProductInfo>(new Callable<ProductInfo>() {
            public ProductInfo call() throws DataLoadException, InterruptedException {
                Thread.sleep(10000);
                return loadProductInfo();
            }
        });
    private final Thread thread = new Thread(future);

    public void start() { thread.start(); }

    public ProductInfo get()
            throws DataLoadException, InterruptedException {
        try {
            System.out.println("get wait ");
            return future.get();
        } catch (ExecutionException e) {
            /** Tasks described by Callable can throw checked and unchecked exceptions, and any code can throw an Error.
 * Whatever the task code may throw, it is wrapped in an ExecutionException and rethrown from Future.get. This complicates
 * code that calls get, not only because it must deal with the possibility of ExecutionException (and the unchecked 
 * CancellationException), but also because the cause of the ExecutionException is returned as a THRowable, which is 
 * inconvenient to deal with. When get throws an ExecutionException in Preloader, the cause will fall into one of three 
 * categories: a checked exception thrown by the Callable, a RuntimeException, or an Error.*/
            Throwable cause = e.getCause();
            if (cause instanceof DataLoadException)
                throw (DataLoadException) cause;
            else
                throw LaunderThrowable.launderThrowable(cause);
        }
    }
    
    public void cancel(){
        
        future.cancel(true);
    }

    interface ProductInfo {
       
    }
}

class DataLoadException extends Exception { }