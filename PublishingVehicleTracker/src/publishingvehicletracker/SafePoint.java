
package publishingvehicletracker;



/**
 SafePoint in Listing 4.11 provides a getter that retrieves both the x and y values at once
 *by returning a two-element array. If we provided separate getters for x and y, then 
 *the values could change between the time one coordinate is retrieved and the other, 
 * resulting in a caller seeing an inconsistent value: an (x, y) location where the 
 * vehicle never was. Using SafePoint, we can construct a vehicle tracker that publishes
 * the underlying mutable state without undermining thread safety, as shown in the 
 * PublishingVehicleTracker class in Listing 4.12. 
 */
//@ThreadSafe
public class SafePoint {
    //@GuardedBy("this") 
    private int x, y;

    private SafePoint(int[] a) {
        this(a[0], a[1]);
    }

    public SafePoint(SafePoint p) {
        this(p.get());
    }

    public SafePoint(int x, int y) {
        this.set(x, y);
    }

    public synchronized int[] get() {
        return new int[]{x, y};
    }

    public synchronized void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
     public String toString(){
        return "\nx : " + x + " y :" + y;
    }
}
