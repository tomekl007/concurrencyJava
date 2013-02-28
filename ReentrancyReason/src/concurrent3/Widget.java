/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrent3;

/**
 *
 * @author Tomek
 */
public class Widget {
public synchronized void doSomething() {
System.out.printf(Widget.class.getName());
}
}


class LoggingWidget extends Widget {
public synchronized void doSomething() {
System.out.println(toString() + ": calling doSomething");
super.doSomething();
}
}