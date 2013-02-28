/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bettervector;

import java.util.Vector;

/**
 *
 * @author Tomasz Lelek
 */
 
//@ThreadSafe
public class BetterVector <E> extends Vector<E> {
    // When extending a serializable class, you should redefine serialVersionUID
    static final long serialVersionUID = -3963416950630760754L;

    public synchronized boolean putIfAbsent(E x) {
        boolean absent = !contains(x);
        if (absent)
            add(x);
        return absent;
    }
}