/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package boundedhashset;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {
         public static void main(String[] args) throws InterruptedException {
           BoundedHashSet<String> bounded = new BoundedHashSet<>(1);
           bounded.add("String1");
           bounded.remove("String1");
           bounded.add("String2");
           bounded.add("String3");
           
    
         }
   
    
    
}
