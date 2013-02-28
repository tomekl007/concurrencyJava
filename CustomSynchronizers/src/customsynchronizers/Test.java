/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package customsynchronizers;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        final BoundedBuffer<String> bb = new BoundedBuffer<>();
        new Thread (new Runnable() {

            @Override
            public void run() {
                try {
                    bb.put("some");
                } catch (InterruptedException ex) {
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
              
        
        bb.take();
        
    } 
}
