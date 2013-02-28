/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package checkemail;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomasz Lelek
 */
public class CheckEmail {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CheckForMail cfe = new CheckForMail();
        Set<String> hosts = new HashSet<>();
        hosts.add("first");
        hosts.add("second");
        hosts.add("third");

        try {
            cfe.checkMail(hosts, 1, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(CheckEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
