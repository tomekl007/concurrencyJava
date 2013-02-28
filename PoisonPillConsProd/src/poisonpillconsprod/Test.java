/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package poisonpillconsprod;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        File root = new File("L:/Example");
        IndexingService is = new IndexingService(root,new FileFilter() {

            @Override
            public boolean accept(File pathname) {
              return true;
            }
        });
        
       is.start();
       
       //
      // is.stop();
      // Thread.sleep(1000);
       //is.awaitTermination();
      // 
       
    }
}
