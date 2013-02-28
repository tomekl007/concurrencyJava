/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitorvehicletracker;

/**
 *
 * @author Tomasz Lelek
 */
//@NotThreadSafe
public class MutablePoint {
    public int x, y;

    public MutablePoint() {
        x = 0;
        y = 0;
    }

    public MutablePoint(MutablePoint p) {
        this.x = p.x;
        this.y = p.y;
    }
    
      public MutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
      
    public String toString(){
        return "\nx : " + x + " y :" + y;
    }
}