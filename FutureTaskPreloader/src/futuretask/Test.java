/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package futuretask;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {
    
     public static void main(String[] args) throws InterruptedException {
         Preloader p = new Preloader();
         p.start();
         Thread.sleep(1000);
        try { 
             try {
                 //p.cancel();
                 p.get();
                
             } catch (DataLoadException ex) {
                 Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
             }
        } catch (InterruptedException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
}
