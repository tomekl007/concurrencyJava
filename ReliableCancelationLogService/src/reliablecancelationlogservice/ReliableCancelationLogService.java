/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reliablecancelationlogservice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomasz Lelek
 */
public class ReliableCancelationLogService {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException  {
        File f = new File("ex");
        Writer writer = null;
        try {
            writer = new PrintWriter(f);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReliableCancelationLogService.class.getName()).log(Level.SEVERE, null, ex);
        }
        LogService ls = new LogService(writer);
        
         ls.start();
        for(int i = 0 ; i < 100 ; i++){
            
            ls.log(String.valueOf(i));
           Thread.sleep(100);
            //if(i == 30)
               // ls.stop();
        }
        
       
        
    }
}
