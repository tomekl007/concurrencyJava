/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cachingmemorizer;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Factorizer
 * <p/>
 * Factorizing servlet that caches results using Memoizer
 *
 * @author Brian Goetz and Tim Peierls
 */
//@ThreadSafe
public class Factorizer {//extends GenericServlet implements Servlet {
    private final Computable<BigInteger, BigInteger[]> c =
            new Computable<BigInteger, BigInteger[]>() {
                public BigInteger[] compute(BigInteger arg) {
                    return factor(arg);
                }
            };
    private final Computable<BigInteger, BigInteger[]> cache
            = new Memorizer<BigInteger, BigInteger[]>(c);

    public void service(String req,
                        String resp) {
        try {
            BigInteger i = extractFromRequest(req);
            encodeIntoResponse(resp, cache.compute(i));
        } catch (InterruptedException e) {
            encodeError(resp, "factorization interrupted");
        }
    }

    void encodeIntoResponse(String resp, BigInteger[] factors) {
    }

    void encodeError(String resp, String errorString) {
    }

    BigInteger extractFromRequest(String req) {
        return new BigInteger("7");
    }

    BigInteger[] factor(BigInteger i) {
         System.out.println("factor " + i);
   try {
            // Doesn't really factor
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Factorizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigInteger[]{i};
    }
}