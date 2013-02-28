package inducelockorderbyhashcode;

/**
 * InduceLockOrder
 *
 * Inducing a lock order to avoid deadlock
 *
 * One way to induce an ordering on objects is to use System.identityHashCode, 
 * which returns the value that would be returned by Object.hashCode. Listing 10.3 
 * shows a version of transferMoney that uses System.identityHashCode to induce a 
 * lock ordering. It involves a few extra lines of code, but eliminates the possibility
 * of deadlock. 
 * 
 * In the rare case that two objects have the same hash code, we must use 
 * an arbitrary means of ordering the lock acquisitions, and this reintroduces the possibility
 * of deadlock. To prevent inconsistent lock ordering in this case, a third "tie breaking" 
 * lock is used. By acquiring the tie-breaking lock before acquiring either Account lock,
 * we ensure that only one thread at a time performs the risky task of acquiring two locks
 * in an arbitrary order, eliminating the possibility of deadlock (so long as this mechanism 
 * is used consistently). If hash collisions were common, this technique might become a concurrency
 * bottleneck (just as having a single, program-wide lock would), but because hash collisions with
 * System.identityHashCode are vanishingly infrequent, this technique provides that last bit of safety 
 * at little cost.
 */
public class InduceLockOrder {
    private static final Object tieLock = new Object();

    public void transferMoney(final Account fromAcct,
                              final Account toAcct,
                              final DollarAmount amount)
            throws InsufficientFundsException {
        class Helper {
            public void transfer() throws InsufficientFundsException {
                if (fromAcct.getBalance().compareTo(amount) < 0)
                    throw new InsufficientFundsException();
                else {
                    fromAcct.debit(amount);
                    toAcct.credit(amount);
                }
            }
        }
        int fromHash = System.identityHashCode(fromAcct);
        int toHash = System.identityHashCode(toAcct);

         
        if (fromHash < toHash) {
            synchronized (fromAcct) {
                synchronized (toAcct) {
                    new Helper().transfer();
                }
            }
        } else if (fromHash > toHash) {
            synchronized (toAcct) {
                synchronized (fromAcct) {
                    new Helper().transfer();
                }
            }
        } else {
            synchronized (tieLock) {
                synchronized (fromAcct) {
                    synchronized (toAcct) {
                        new Helper().transfer();
                    }
                }
            }
        }
    }

    interface DollarAmount extends Comparable<DollarAmount> {
    }

    interface Account {
        void debit(DollarAmount d);

        void credit(DollarAmount d);

        DollarAmount getBalance();

        int getAcctNo();
    }

    class InsufficientFundsException extends Exception {
    }
}
