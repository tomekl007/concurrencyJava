/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lockstrippingmap;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        StripedMap sm = new StripedMap(32);
        Object o = new Object();
        sm.get(o);
        Object ob = new Object();        
        sm.get(ob);

    }
}
