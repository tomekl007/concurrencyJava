/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runtimeex;

import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 *
 * @author Tomasz Lelek
 */
public class RuntimeEx {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      //  Runtime.getRuntime().exit(1); 
        Runtime.getRuntime().addShutdownHook(new Thread() {
            
            public void run(){
              //  try {
                    System.out.println("in shutdown hook");
               // }catch(InterruptedException ignored){}
                
            }
        });
        FutureTask f;
        
     Map<String, String> env = System.getenv();
        System.out.println(" " +  env);
    }
}
