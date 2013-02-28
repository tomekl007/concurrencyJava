package transferingmoneydeadlock;

import java.util.concurrent.atomic.*;

/**
 * DynamicOrderDeadlock
 * <p/>
 * Dynamic lock-ordering deadlock
 *
 * Sometimes it is not obvious that you have sufficient control over lock ordering to 
 * prevent deadlocks. Consider the harmless-looking code in Listing 10.2 that transfers
 * funds from one account to another. It acquires the locks on both Account objects before 
 * executing the transfer, ensuring that the balances are updated atomically and without 
 * violating invariants such as "an account cannot have a negative balance"
 */


public class DynamicOrderDeadlock {
    // Warning: deadlock-prone!
    public static void transferMoney(Account fromAccount,
                                     Account toAccount,
                                     DollarAmount amount)
            throws InsufficientFundsException {
        System.out.println("transfer money");
        synchronized (fromAccount) {
            synchronized (toAccount) {
                if (fromAccount.getBalance().compareTo(amount) < 0)
                    throw new InsufficientFundsException();
                else {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
                }
            }
        }
    }

    class DollarAmount implements Comparable<DollarAmount> {
        // Needs implementation

        int amount ;
        public DollarAmount(int amount) {
            this.amount = amount;
        }

        public DollarAmount add(DollarAmount d) {
            return new DollarAmount(amount + d.amount);
        }

        public DollarAmount subtract(DollarAmount d) {
             return new DollarAmount(amount - d.amount);

        }

        public int compareTo(DollarAmount dollarAmount) {
            return 0;
        }
    }

     class Account {
        private DollarAmount balance;
        private final int acctNo;
        private  final AtomicInteger sequence = new AtomicInteger();

        public Account() {
            
            acctNo = sequence.incrementAndGet();
            this.balance = new DollarAmount(299);
            System.out.println("new account" + balance);
        }

        void debit(DollarAmount d) {
           this.balance = balance.subtract(d);
        }

        void credit(DollarAmount d) {
            balance = balance.add(d);
        }
        
        public void setBalance(DollarAmount da ){
            this.balance = da;
            
        }

        DollarAmount getBalance() {
            return this.balance;
           // return new DollarAmount(299);
        }

        int getAcctNo() {
            return acctNo;
        }
    }

    static class InsufficientFundsException extends Exception {
    }
}
