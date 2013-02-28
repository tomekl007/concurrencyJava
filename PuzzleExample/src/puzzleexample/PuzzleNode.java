package puzzleexample;

import java.util.*;

//import net.jcip.annotations.*;

/**
 * PuzzleNode
 * <p/>
 * Link node for the puzzle solving framework
 *
 * Node in Listing 8.14 represents a position that has been reached through some series of moves
 * , holding a reference to the move that created the position and the previous Node. Following
 * the links back from a Node lets us reconstruct the sequence of moves that led to the current position.

 */
//@Immutable
public class PuzzleNode <P, M> {
    final P pos;
    final M move;
    final PuzzleNode<P, M> prev;

    public PuzzleNode(P pos, M move, PuzzleNode<P, M> prev) {
        this.pos = pos;
        this.move = move;
        this.prev = prev;
    }

    List<M> asMoveList() {
        List<M> solution = new LinkedList<M>();
        for (PuzzleNode<P, M> n = this; n.move != null; n = n.prev)
            solution.add(0, n.move);
        return solution;
    }
}
