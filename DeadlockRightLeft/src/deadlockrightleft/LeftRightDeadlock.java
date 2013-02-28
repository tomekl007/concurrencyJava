/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deadlockrightleft;

/**
 *
 * @author Tomasz Lelek
 */
/**
 * LeftRightDeadlock
 *
 * Simple lock-ordering deadlock
 *
 *LeftRightDeadlock in Listing 10.1 is at risk for deadlock. The leftRight
 * and rightLeft methods each acquire the left and right locks. If one
 * thread calls leftRight and another calls rightLeft, and their actions are
 * interleaved , they will deadlock
 */

/*Verifying consistent lock ordering requires a global analysis of your program's locking behavior. 
 * It is not sufficient to inspect code paths that acquire multiple locks individually; both leftRight
 * and rightLeft are "reasonable" ways to acquire the two locks, they are just not compatible. When
 * it comes to locking, the left hand needs to know what the right hand is doing*/
public class LeftRightDeadlock {
    private final Object left = new Object();
    private final Object right = new Object();

    
    //jesli to bedzie w tym samym momecie to bedzie deadlock
    public void leftRight() {
        synchronized (left) {
            synchronized (right) {
                doSomething();
            }
        }
    }

    public void rightLeft() {
        synchronized (right) {
            synchronized (left) {
                doSomethingElse();
            }
        }
    }

    void doSomething() {
        System.out.println("do sth");
    }

    void doSomethingElse() {
        System.out.println("do sth else");

    }
}