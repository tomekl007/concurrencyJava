
package cooperatingdeadlock;

import java.awt.Point;
import java.util.*;

//import net.jcip.annotations.*;

/**
 * CooperatingDeadlock
 * <p/>
 * Lock-ordering deadlock between cooperating objects
 *
 *While no method explicitly acquires two locks, callers of setLocation and getImage can acquire
 * two locks just the same. If a thread calls setLocation in response to an update from a GPS 
 * receiver, it first updates the taxi's location and then checks to see if it has reached its 
 * destination. If it has, it informs the dispatcher that it needs a new destination. Since both 
 * setLocation and notifyAvailable are synchronized, the thread calling setLocation acquires the 
 * Taxi lock and then the Dispatcher lock. Similarly, a thread calling getImage acquires the Dispatcher
 * lock and then each Taxi lock (one at at time). Just as in LeftRightDeadlock, two locks are acquired 
 * by two threads in different orders, risking deadlock.
 */
public class CooperatingDeadlock {
    // Warning: deadlock-prone!
    class Taxi {
       // @GuardedBy("this")
        private Point location, destination;
        private final Dispatcher dispatcher;

        public Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public synchronized Point getLocation() {
            return location;
        }

        //need lock on filed "location"
        public synchronized void setLocation(Point location) throws InterruptedException {
            System.out.println("taxi.setLocation");
            Thread.currentThread().sleep(1000);
            this.location = location;
            if (location.equals(destination)){
                System.out.println("notifyAvailable(alien)");
                //invokig the alien method
                dispatcher.notifyAvailable(this);
            }
        }

        public synchronized Point getDestination() {
            return destination;
        }

        public synchronized void setDestination(Point destination) {
            this.destination = destination;
        }
    }

    class Dispatcher {
        //@GuardedBy("this")
        private final Set<Taxi> taxis;
       // @GuardedBy("this") 
        private final Set<Taxi> availableTaxis;

        public Dispatcher() {
            taxis = new HashSet<Taxi>();
            availableTaxis = new HashSet<Taxi>();
        }
        
        public synchronized void addTaxi(Taxi t){
            taxis.add(t);
        }
        
        public synchronized void notifyAvailable(Taxi taxi) {
            availableTaxis.add(taxi);
        }

        public synchronized Image getImage() throws InterruptedException {
            System.out.println("get image");
            Thread.currentThread().sleep(1000);
            Image image = new Image();
            for (Taxi t : taxis)
                image.drawMarker(//invoking the alien method
                        t.getLocation());//acquire lock on field "location"
            return image;
        }
    }

    class Image {
        public void drawMarker(Point p) {
            System.out.println("draw marker p : " + p);
        }
    }
}
