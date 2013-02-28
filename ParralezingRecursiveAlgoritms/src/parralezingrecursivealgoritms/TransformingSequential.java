package parralezingrecursivealgoritms;

import java.util.*;
import java.util.concurrent.*;

/**
 * TransformingSequential
 * <p/>
 * Transforming sequential execution into parallel execution
 *
 * @author Brian Goetz and Tim Peierls
 */
public abstract class TransformingSequential {

    //f we have a loop whose iterations are independent and we don't need to wait for all 
    //of them to complete before proceeding, we can use an Executor to transform a sequential
    //loop into a parallel one, as shown in processSequentially and processInParallel in Listing 8.10.
    void processSequentially(List<Element> elements) {
        for (Element e : elements)
            process(e);
    }

    void processInParallel(Executor exec, List<Element> elements) {
        for (final Element e : elements)
            exec.execute(new Runnable() {
                public void run() {
                    
                    process(e);
                }
            });
    }/*A call to processInParallel returns more quickly than a call to processSequentially 
     * because it returns as soon as all the tasks are queued to the Executor, rather than 
     * waiting for them all to complete. If you want to submit a set of tasks and wait for 
     * them all to complete, you can use ExecutorService.invokeAll; to retrieve the results
     * as they become available, you can use a CompletionService*/
    
    

    public abstract void process(Element e);

/*Loop parallelization can also be applied to some recursive designs; there are often sequential
 * loops within the recursive algorithm that can be parallelized in the same manner as Listing 8.10.
 * The easier case is when each iteration does not require the results of the recursive iterations
 * it invokes. For example, sequentialRecursive in Listing 8.11 does a
 depth-first traversal of a tree, performing a calculation on each node and placing the result in a collection*/
    public <T> void sequentialRecursive(List<Node<T>> nodes,
                                        Collection<T> results) {
        for (Node<T> n : nodes) {
            results.add(n.compute());
            sequentialRecursive(n.getChildren(), results);
        }
    }
/*the transformed version, parallelRecursive, also does a depth-first traversal, but
 * instead of computing the result as each node is visited, it submits a task to compute the node result*/
    public <T> void parallelRecursive(final Executor exec,
                                      List<Node<T>> nodes,
                                      final Collection<T> results) {
        for (final Node<T> n : nodes) {
            exec.execute(new Runnable() {
                public void run() {
                    results.add(n.compute());
                }
            });
            parallelRecursive(exec, n.getChildren(), results);
        }
    }

    /*When parallelRecursive returns, each node in the tree has been visited (the traversal is still 
     * sequential: only the calls to compute are executed in parallel) and the computation for each 
     * node has been queued to the Executor. Callers of parallelRecursive can wait for all the
     * results by creating an Executor specific to the traversal and using shutdown and 
     * awaitTermination, as shown in Listing 8.12*/
    public <T> Collection<T> getParallelResults(List<Node<T>> nodes)
            throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Queue<T> resultQueue = new ConcurrentLinkedQueue<T>();
        parallelRecursive(exec, nodes, resultQueue);
        exec.shutdown();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        return resultQueue;
    }

    public interface Element {
    }

    interface Node <T> {
        
        T compute();

        List<Node<T>> getChildren();
    }
    
    public class NodeImpl implements Node<String>{
        
        public List<Node<String>> childrens;
        volatile int i;
        
        public NodeImpl(){
            i = 0;
            childrens = new LinkedList<Node<String>>();
        }

        @Override
        public String compute() {
            System.out.println("some computing in " + Thread.currentThread().toString());
            i++;
            return String.valueOf(i);
        }

        @Override
        public List<Node<String>> getChildren() {
            return this.childrens;
        }
        
    }
    
}

