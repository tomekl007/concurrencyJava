/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiddeniterator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Tomasz Lelek
 */


public class HiddenIterator {
    
    //SOlution : 
    //If HiddenIterator wrapped the HashSet with a synchronizedSet, encapsulating the synchronization, 
    //this sort of error would not occur. 
    //@GuardedBy("this") 
    private final Set<Integer> set = new HashSet<Integer>();

    public synchronized void add(Integer i) {
        set.add(i);
    }

    public synchronized void remove(Integer i) {
        set.remove(i);
    }

    public void addTenThings() {
        Random r = new Random();
        for (int i = 0; i < 10; i++)
            add(r.nextInt());
        /*The string concatenation gets turned by the compiler into a call to StringBuilder.append(Object), 
         * which in turn invokes the collection's toString method - and the implementation of toString in 
         * the standard collections iterates the collection and calls toString on each element to
         * produce a nicely formatted representation of the collection's contents. */
        System.out.println("DEBUG: added ten elements to " + set);
    }
}

/*Iteration is also indirectly invoked by the collection's hashCode and equals methods, 
 * which may be called if the collection is used as an element or key of another collection.
 * Similarly, the containsAll, removeAll, and retainAll methods, as well as the constructors 
 * that take collections as arguments, also iterate the collection. All of these indirect uses
 * of iteration can cause ConcurrentModificationException. */