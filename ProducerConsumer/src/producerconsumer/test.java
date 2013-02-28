/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package producerconsumer;

import java.io.File;

/**
 *
 * @author Tomasz Lelek
 */
public class test {
    
    public static void main(String[] args) {
      File[] roots = new File[1];
      roots[0] = new File("L:/Example");
        
        ProducerConsumer.startIndexing(roots);
        
      
      
      }
    
}
