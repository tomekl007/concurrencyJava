/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitorvehicletracker;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Tomasz Lelek
 * 
 * even though MutablePoint is not thread-safe, the tracker class is. Neither
 * the map nor any of the mutable points it contains is ever published. When 
 * we need to a return vehicle locations to callers, the appropriate values 
 * are copied using either the MutablePoint copy constructor or deepCopy, which
 * creates a new Map whose values are copies of the keys and values from the old Map
 */
//@ThreadSafe
 public class MonitorVehicleTracker {
   // @GuardedBy("this") 
     private final Map<String, MutablePoint> locations;

    public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
        this.locations = deepCopy(locations);
    }

    /*This implementation maintains thread safety in part by copying mutable data
     * before returning it to the client. 
     * This is usually not a performance issue, but could become one if 
     * the set of vehicles is very large*/
    public synchronized Map<String, MutablePoint> getLocations() {
        return deepCopy(locations);
    }

    
    /*Another consequence of copying the data on each call to getLocation 
     * is that the contents of the returned collection do not change even if
     * the underlying locations change*/
    //jesli zwroce clientowi w getLocations Map, a ja cos zmienie w MonitorVehicleTracker.locations
    //to dane ktore ma klient sa inne 
    public synchronized MutablePoint getLocation(String id) {
        MutablePoint loc = locations.get(id);
        return loc == null ? null : new MutablePoint(loc);
    }

    public synchronized void setLocation(String id, int x, int y) {
        MutablePoint loc = locations.get(id);
        if (loc == null)
            throw new IllegalArgumentException("No such ID: " + id);
        loc.x = x;
        loc.y = y;
    }

    private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> m) {
        Map<String, MutablePoint> result = new HashMap<String, MutablePoint>();

        for (String id : m.keySet())
            result.put(id, new MutablePoint(m.get(id)));

        
        //Note that deepCopy can't just wrap the Map with an unmodifiableMap,:
        ////Collections.unmodifiableMap(m);
        //because that protects only the collection from modification; it does not 
        //prevent callers from modifying the mutable objects stored in it. For the same reason,
        //populating the HashMap in deepCopy via a copy constructor wouldn't work either,
        //because only the references to the points would be copied, not the point objects themselves
        
        return Collections.unmodifiableMap(result);
    }
}