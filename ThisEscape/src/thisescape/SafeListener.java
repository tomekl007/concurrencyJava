/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thisescape;

import java.awt.Event;
import java.util.EventListener;

/**
avoid escape of "this" reference by using private consturctor,
* and public factory method
 */
public class SafeListener {
private final EventListener listener;
private SafeListener() {
listener = new EventListener() {
public void onEvent(Event e) {
doSomething(e);
}
};
}
public static SafeListener newInstance(EventSource source) {
SafeListener safe = new SafeListener();//tworze bezpiecznie obj. this
          //bo w konstruktorze referencja this nigdzie not escape

source.registerListener(safe.listener);//i dopiero teraz daje jej escape
      //jak juz obiekt jest caly skonstruowany
return safe;
}
}