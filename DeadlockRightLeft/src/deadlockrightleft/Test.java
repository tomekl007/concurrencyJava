/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deadlockrightleft;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       final LeftRightDeadlock lrd = new LeftRightDeadlock();
       lrd.rightLeft();
       lrd.leftRight();
       
       Executor exec = Executors.newCachedThreadPool();
       for(int i = 0; i < 10; i++){
           exec.execute(new Runnable() {

               @Override
               public void run() {
                   lrd.rightLeft();
               }
           });
           exec.execute(new Runnable() {

               @Override
               public void run() {
                 lrd.leftRight();
               }
           });
       }
    }
}
