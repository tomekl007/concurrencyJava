/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitorvehicletracker;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;



/**
 *
 * @author Tomek
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
     MutablePoint a = new MutablePoint(1,2);
     MutablePoint b = new MutablePoint(3,4);
     Map<String, MutablePoint> points = new HashMap<>();
     points.put("1", a);     
     points.put("2", b);
     MonitorVehicleTracker dvt = new MonitorVehicleTracker(points); 
     Map m = dvt.getLocations();
     
     //System.out.println(dvt.getLocations());
     System.out.println(m);

     //this change is not see in "m"
     dvt.setLocation("1", 10,10);
     //System.out.println(dvt.getLocations());
          System.out.println(m);
          
        MutablePoint p = (MutablePoint) m.get("1");
       p.x = 123;

     MutablePoint c = new MutablePoint(100,200);
    Map map = dvt.getLocations();
    //  map.put("3",c);
    
         System.out.println(dvt.getLocations());



               
      }
}
