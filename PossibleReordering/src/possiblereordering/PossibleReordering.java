/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package possiblereordering;

/**
 *
 * @author Tomasz Lelek
 */
/**
 * PossibleReordering DO NOT DO THIS !
 * <p/>
 * Insufficiently synchronized program that can have surprising results
 *
 * PossibleReordering in Listing 16.1 illustrates how difficult it is to reason about the behavior 
 * of even the simplest concurrent programs unless they are correctly synchronized. It is
 * fairly easy to imagine how PossibleReordering could print (1, 0), or (0, 1), or (1, 1):
 * thread A could run to completion before B starts, B could run to completion before A starts, 
 * or their actions could be interleaved. But, strangely, PossibleReordering can also print (0, 0)! 
 * The actions in each thread have no dataflow dependence on each other, and accordingly can be executed
 * out of order. (Even if they are executed in order, the timing by which caches are flushed to main
 * memory can make it appear, from the perspective of B, that the assignments in A occurred in the
 * opposite order.)
 */
public class PossibleReordering {
    static int x = 0, y = 0;
    static int a = 0, b = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread one = new Thread(new Runnable() {
            public void run() {
                a = 1;
                x = b;
            }
        });
        Thread other = new Thread(new Runnable() {
            public void run() {
                b = 1;
                y = a;
            }
        });
        one.start();
        other.start();
        one.join();
        other.join();
        System.out.println("( " + x + "," + y + ")");
    }
}