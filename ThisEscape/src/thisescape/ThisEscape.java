/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thisescape;

import java.awt.Event;
import java.util.EventListener;

/**
 *
 * @author Tomek
 */


//don't do this : 
//public class ThisEscape {
//public ThisEscape(EventSource source) {
//source.registerListener(
//new EventListener() { //this reference escape outside.
//    //bo source dostaje obiekt ktory tworze - EventListener
//    //a inner obj. posiada hidden reference to enclosing instance(this)
//    //to moze spowodowac użycie nie w pelni skonstruowanego obiektu przez
//    //watek ktory "zajmuje sie" source
//    
//public void onEvent(Event e) {
//doSomething(e);
//}
//});
//}
//}

