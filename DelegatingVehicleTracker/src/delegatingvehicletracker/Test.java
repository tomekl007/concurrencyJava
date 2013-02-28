/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delegatingvehicletracker;

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
        
     Point a = new Point(1,2);
     Point b = new Point(3,4);
     Map<String, Point> points = new HashMap<>();
     points.put("1", a);     
     points.put("2", b);
     DelegatingVehicleTracker dvt = new DelegatingVehicleTracker(points); 
     //in "m" change to underlaing data source are visible
     Map m = dvt.getLocations();
     //in "s" change to underlaing data source are not visible 
     Map s = dvt.getLocationsAsStatic();
          
     System.out.println(m);
     System.out.println(s);

     //System.out.println(dvt.getLocations());
     dvt.setLocation("1", 10,10);
     //System.out.println(dvt.getLocations());
     System.out.println(m);
     System.out.println(s);

     //widac zmiany w m, momo ze nie wywoluje ponownie dvt.getLocations();
     //bo getLocations zwraca shallow copy(adress copy), wiec jesli zmnienie
     //zmninie wartosc jednego z pkt. w underlaining data source(locations)
     //to sa w doczne w m
     
       Point p = (Point) m.get("1");
       p.x = 123;

     
     Point c = new Point(100,100);
    Map map = dvt.getLocations();
   // map.put("3",c);
    
         System.out.println(dvt.getLocations());



               
      }
}
