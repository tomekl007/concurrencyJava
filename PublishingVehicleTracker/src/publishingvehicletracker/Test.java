/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package publishingvehicletracker;

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
        
        SafePoint a = new SafePoint(1,2);
     SafePoint b = new SafePoint(3,4);
     Map<String, SafePoint> points = new HashMap<>();
     points.put("1", a);     
     points.put("2", b);
     PublishingVehicleTracker dvt = new PublishingVehicleTracker(points); 
     Map m = dvt.getLocations();
     

     
     //System.out.println(dvt.getLocations());
     System.out.println(m);

     //this change is not see in "m"
     dvt.setLocation("1", 10,10);
     //System.out.println(dvt.getLocations());
          System.out.println(m);
          
          /*he getLocation method returns an unmodifiable copy of the underlying Map.
           * Callers cannot add or remove vehicles, but could change the location 
           * of one of the vehicles by mutating the SafePoint values in the returned Map.
           * Again, the "live" nature of the Map may be a benefit or a drawback, depending
           * on the requirements.*/
       SafePoint s = (SafePoint) m.get("1");
       s.set(123, 1234);

     SafePoint c = new SafePoint(100,200);
    Map map = dvt.getLocations();
    //map.put("3",c);
    
         System.out.println(dvt.getLocations());



               
      }
}
