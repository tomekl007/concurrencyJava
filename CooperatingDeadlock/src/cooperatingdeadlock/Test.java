/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cooperatingdeadlock;

import cooperatingdeadlock.CooperatingDeadlock.Dispatcher;
import cooperatingdeadlock.CooperatingDeadlock.Taxi;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
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
    public static void main(String[] args) {
        //no deadlock
//        CooperatingNoDeadlock cnd = new CooperatingNoDeadlock();
//        final CooperatingNoDeadlock.Dispatcher dr = cnd.new Dispatcher();
//        final CooperatingNoDeadlock.Taxi taxi = cnd.new Taxi(dr);
//        CooperatingNoDeadlock.Taxi taxi2 = cnd.new Taxi(dr);
//        CooperatingNoDeadlock.Taxi taxi3 = cnd.new Taxi(dr);
////        Set<CooperatingNoDeadlock.Taxi> taxisFleet = new HashSet<>();
////        taxisFleet.add(taxi);        
////        taxisFleet.add(taxi2);
////        taxisFleet.add(taxi3);
//        taxi.setDestination(new Point(1,1));
//        dr.addTaxi(taxi);        
//        dr.addTaxi(taxi2);
//        dr.addTaxi(taxi3);
//
//        
//        
//         new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                for(int i = 0; i < 3; i++ ){
////                    try {
//                        taxi.setLocation(new Point(1,1));
////                    } catch (InterruptedException ex) {
////                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
////                    }
//                }
//            }
//        }).start();
//        
//         new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                for(int i = 0; i < 3; i++ ){
////                    try {                  
//                        dr.getImage();
////                    } catch (InterruptedException ex) {
////                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
////                    }
//                  
//                    
//                }
//            }
//        }).start();
        
        
        //deadlock
        CooperatingDeadlock cd = new CooperatingDeadlock();
        final Dispatcher d = cd.new Dispatcher();
        final Taxi t = cd.new Taxi(d);
        Taxi t2 = cd.new Taxi(d);
        Taxi t3 = cd.new Taxi(d);
        Set<Taxi> taxis = new HashSet<>();
        taxis.add(t);        
        taxis.add(t2);
        taxis.add(t3);
        t.setDestination(new Point(1,1));
        

        
        d.addTaxi(t);      
        d.addTaxi(t2);
        d.addTaxi(t3);
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                for(int i = 0; i < 3; i++ ){
                    try {
                     
                        t.setLocation(new Point(1,1));
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
                    
                }
            }
        }).start();
        
         new Thread(new Runnable() {

            @Override
            public void run() {
                for(int i = 0; i < 3; i++ ){
                    try {                  
                        d.getImage();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    }
                  
                    
                }
            }
        }).start();
        


        
        
        
    }
}
