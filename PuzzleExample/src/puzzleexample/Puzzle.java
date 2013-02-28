package puzzleexample;

import java.util.*;

/**
 * Puzzle
 * <p/>
 * Abstraction for puzzles like the 'sliding blocks puzzle'
 *
 * @author Brian Goetz and Tim PeierlsWe define a "puzzle" as a combination of an initial position, a goal position,
 * and a set of rules that determine valid moves. The rule set has two parts: computing the list of lega
 * l moves from a given position and computing the result of applying a move to a position. Puzzle in 
 * Listing 8.13 shows our puzzle abstraction; the type parameters P and M represent the classes for a
 * position and a move. From this interface, we can write a simple sequential solver that searches the 
 * puzzle space until a solution is found or the puzzle space is exhausted. */
public interface Puzzle <P, M> {
    P initialPosition();

    boolean isGoal(P position);

    Set<M> legalMoves(P position);

    P move(P position, M move);
}
