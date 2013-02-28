/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrent3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

/**
 *
 * @author Tomek
 */


public class Concurrent3 {

    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Widget lw = new LoggingWidget();
        
        lw.doSomething();
        
    }
}
