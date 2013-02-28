/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package futuregetwithtimeout;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RenderWithTimeBudget rwtb = new RenderWithTimeBudget();
        try {
            rwtb.renderPageWithAd();
        } catch (InterruptedException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
