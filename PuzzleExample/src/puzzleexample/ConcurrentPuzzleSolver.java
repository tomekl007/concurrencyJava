package puzzleexample;

import java.util.*;
import java.util.concurrent.*;

/**
 * ConcurrentPuzzleSolver
 * <p/>
 * Concurrent version of puzzle solver
 *
 *ConcurrentPuzzleSolver in Listing 8.16 uses an inner SolverTask class
 * that extends Node and implements Runnable. Most of the work is done in run:
 * evaluating the set of possible next positions, pruning positions already 
 * searched, evaluating whether success has yet been achieved (by this task 
 * or by some other task), and submitting unsearched positions to an Executor
 */
public class ConcurrentPuzzleSolver <P, M> {
    private final Puzzle<P, M> puzzle;
    private final ExecutorService exec;
    /*To avoid infinite loops, the sequential version maintained a Set of previously 
     * searched positions; ConcurrentPuzzleSolver uses a ConcurrentHashMap for this purpose. 
     * This provides thread safety and avoids the race condition inherent in conditionally 
     * updating a shared collection by using putIfAbsent to atomically add a position only 
     * if it was not previously known. ConcurrentPuzzleSolver uses the internal work queue 
     * of the thread pool instead of the call stack to hold the state of the search.*/
    private final ConcurrentMap<P, Boolean> seen;
    protected final ValueLatch<PuzzleNode<P, M>> solution = new ValueLatch<PuzzleNode<P, M>>();

    public ConcurrentPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
        this.exec = initThreadPool();
        this.seen = new ConcurrentHashMap<P, Boolean>();
        if (exec instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) exec;
            tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        }
    }

    private ExecutorService initThreadPool() {
        return Executors.newCachedThreadPool();
    }

    public List<M> solve() throws InterruptedException {
        try {
            P p = puzzle.initialPosition();
            exec.execute(newTask(p, null, null));
            // block until solution found
            /*. The main thread needs to wait until a solution is found; getValue
             * in ValueLatch blocks until some thread has set the value. ValueLatch
             * provides a way to hold a value such that only the first call actually
             * sets the value, callers can test whether it has been set, and callers
             * can block waiting for it to be set. On the first call to setValue, the 
             * solution is updated and the CountDownLatch is decremented, releasing the
             * main solver thread from getValue.*/
            PuzzleNode<P, M> solnPuzzleNode = solution.getValue();//problem :
            //if all possible moves and positions have been evaluated and no solution has been found, solve waits forever in the call to getSolution
            
            return (solnPuzzleNode == null) ? null : solnPuzzleNode.asMoveList();
        } finally {
            //The first thread to find a solution also shuts down the Executor, to prevent new tasks from being accepted
            exec.shutdown();
            /*To avoid having to deal with RejectedExecutionException, the rejected execution
             * handler should be set to discard submitted tasks. Then, all unfinished tasks 
             * eventually run to completion and any subsequent attempts to execute new tasks
             * fail silently, allowing the executor to terminate. (If the tasks took longer
             * to run, we might want to interrupt them instead of letting them finish.)*/
        }
    }

    protected Runnable newTask(P p, M m, PuzzleNode<P, M> n) {
        return new SolverTask(p, m, n);
    }

    protected class SolverTask extends PuzzleNode<P, M> implements Runnable {
        SolverTask(P pos, M move, PuzzleNode<P, M> prev) {
            super(pos, move, prev);
        }

        public void run() {
            //Each task first consults the solution latch and stops if a solution has already been found
            if (solution.isSet()
                    || seen.putIfAbsent(pos, true) != null)
                return; // already solved or seen this position
            if (puzzle.isGoal(pos))
                solution.setValue(this);
            else
                for (M m : puzzle.legalMoves(pos))
                    exec.execute(newTask(puzzle.move(pos, m), m, this));
        }
    }
}
