/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package statelessfactorizer;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author Tomek
 */
public class StatelessFactorizer implements Servlet {
    
   AtomicReference<Integer> i = new AtomicReference <>();
   
    public void service(ServletRequest req, ServletResponse resp) {
  
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        encodeIntoResponse(resp, factors);
    }
    
}