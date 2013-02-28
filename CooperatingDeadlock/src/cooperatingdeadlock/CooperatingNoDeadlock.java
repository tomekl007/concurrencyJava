package cooperatingdeadlock;

import java.awt.Point;
import java.util.*;

//import net.jcip.annotations.*;

/**
 * CooperatingNoDeadlock
 * <p/>
 * Using open calls to avoiding deadlock between cooperating objects
 *
 * Taxi and Dispatcher in Listing 10.5 can be easily refactored to use open calls and thus eliminate the deadlock risk.
 * This involves shrinking the synchronized blocks to guard only operations that involve shared state, as in Listing 10.6.
 * Very often, the cause of problems like those in Listing 10.5 is the use of synchronized methods instead of smaller
 * synchronized blocks for reasons of compact syntax or simplicity rather than because the entire method must be guarded
 * by a lock. (As a bonus, shrinking the synchronized block may also improve scalability as well; see Section 11.4.1 for
 * advice on sizing synchronized blocks.) 
 */
class CooperatingNoDeadlock {
  //  @ThreadSafe
    class Taxi {
    //    @GuardedBy("this")
        private Point location, destination;
        private final Dispatcher dispatcher;

        public Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public synchronized Point getLocation() {
            return location;
        }

        public synchronized void setLocation(Point location) {
            System.out.println("taxi.setLocation");
            boolean reachedDestination;
            synchronized (this) {
                this.location = location;
                reachedDestination = location.equals(destination);
            }
            if (reachedDestination){
                System.out.println("notifyAvailable(alien)");
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

   // @ThreadSafe
    class Dispatcher {
      //  @GuardedBy("this") 
        private final Set<Taxi> taxis;
       // @GuardedBy("this") 
        private final Set<Taxi> availableTaxis;

        public Dispatcher() {
            taxis = new HashSet<Taxi>();
            availableTaxis = new HashSet<Taxi>();
        }

        public synchronized void notifyAvailable(Taxi taxi) {
            System.out.println("availableTaxi.add " + taxi);
            availableTaxis.add(taxi);
        }
        
        public synchronized void addTaxi(Taxi t){
            taxis.add(t);
        }

        public Image getImage() {
            System.out.println("get image");
            Set<Taxi> copy;
            synchronized (this) {
                //i create copy
                copy = new HashSet<Taxi>(taxis);
            }
            Image image = new Image();
            for (Taxi t : copy)
                //so i invoke t.getLocation on local "copy"
                //therefore do not deadlock
                image.drawMarker(t.getLocation());
            return image;
        }
    }

    class Image {
        public void drawMarker(Point p) {
             System.out.println("draw marker p : " + p);
        }
    }

}