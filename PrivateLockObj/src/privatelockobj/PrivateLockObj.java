/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package privatelockobj;

/**
 *
 * @author Tomasz Lelek
 */
public class PrivateLockObj {

   private final Object myLock = new Object();
   // @GuardedBy("myLock")
   Widget widget;

    void someMethod() {
        synchronized (myLock) {
            // Access or modify the state of widget
        }
    }
}
