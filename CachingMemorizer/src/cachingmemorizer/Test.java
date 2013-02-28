/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cachingmemorizer;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final Factorizer f = new Factorizer();
         Runnable r = new Runnable() {

            @Override
            public void run() {
                f.service("1", "1");
            }
        };
        
        r.run();
        //r.run();
        
        f.service("1", "1");
       
    }
}
