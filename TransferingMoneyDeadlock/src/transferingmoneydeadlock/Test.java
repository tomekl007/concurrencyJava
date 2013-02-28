/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transferingmoneydeadlock;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import transferingmoneydeadlock.DynamicOrderDeadlock.Account;
import transferingmoneydeadlock.DynamicOrderDeadlock.DollarAmount;
import transferingmoneydeadlock.DynamicOrderDeadlock.InsufficientFundsException;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InsufficientFundsException, InterruptedException  {
       
            /*How can TRansferMoney deadlock? It may appear as if all the threads
     * acquire their locks in the same order, but in fact the lock order 
     * depends on the order of arguments passed to transferMoney, and these
     * in turn might depend on external inputs. Deadlock can occur if two
     * threads call transferMoney at the same time, one transferring from X
     * to Y, and the other doing the opposite*/
            DynamicOrderDeadlock dod = new DynamicOrderDeadlock();
           final Account myAccount = dod.new Account();
          final Account yourAccount = dod.new Account();
          DollarAmount balance = dod.new DollarAmount(100);   
          myAccount.setBalance(balance);
          yourAccount.setBalance(balance);

          

           final DollarAmount da1 = dod.new DollarAmount(10);         
           final DollarAmount da2 = dod.new DollarAmount(20);
           Executor exec = Executors.newCachedThreadPool();

           for(int i = 0; i < 10;i++){
               exec.execute(new Runnable() {

               @Override
               public void run() {
                       try {
                           DynamicOrderDeadlock.transferMoney(myAccount, yourAccount, da1);
                       } catch (InsufficientFundsException ex) {
                           Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                       }
               }
           });
              exec.execute(new Runnable() {

               @Override
               public void run() {
                      try {
                          DynamicOrderDeadlock.transferMoney(yourAccount, myAccount, da2 );
                      } catch (InsufficientFundsException ex) {
                          Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                      }
               }
           });
              
               System.out.println("i = " + i);
            
           }
        
    }
}
