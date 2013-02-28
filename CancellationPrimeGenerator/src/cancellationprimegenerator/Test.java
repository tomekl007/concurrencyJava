/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cancellationprimegenerator;

import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        PrimeGenerator pg = new PrimeGenerator();
//        pg.run();
//        pg.cancel();
        pg.aSecondOfPrimes();
       // List<BigInteger> lbi = pg.get();
       // System.out.println(lbi);
    }
}
