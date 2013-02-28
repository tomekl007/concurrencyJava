/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timebudget;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {
    
     public static void main(String[] args) throws InterruptedException {
    TravelInfo ti = new TravelInfo() {};
    Set<TravelCompany> comp = new HashSet<>();
    comp.add(new TravelCompany() {

             @Override
             public TravelQuote solicitQuote(TravelInfo travelInfo) throws Exception {
               //  Thread.sleep(4000);
                return new TravelQuote() {};
             }
         });
    comp.add(new TravelCompany() {

             @Override
             public TravelQuote solicitQuote(TravelInfo travelInfo) throws Exception {
                return new TravelQuote() {};
             }
         });
    
            
    
    TimeBudget tb = new TimeBudget();
    tb.getRankedTravelQuotes(ti,comp,null,1,TimeUnit.SECONDS);
     }
}
