/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package confinmenttrheadlocal;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConnectionDispenser
 * <p/>
 * Using ThreadLocal to ensure thread confinement
 *
 * @author Brian Goetz and Tim Peierls
 */
public class ConfinmentTrheadLocal {
    static String DB_URL = "jdbc:mysql://localhost/mydatabase";

    private ThreadLocal<Connection> connectionHolder
            = new ThreadLocal<Connection>() {
                public Connection initialValue() {
                    try {
                        return DriverManager.getConnection(DB_URL);
                    } catch (SQLException e) {
                        throw new RuntimeException("Unable to acquire Connection, e");
                    }
                };
            };

    public Connection getConnection() {
        /*When a thread calls ThreadLocal.get for the first time, initialValue is consulted to provide the initial value for that thread.
         * Conceptually, you can think of a ThreadLocal<T> as holding a Map<Thread,T> that stores 
         * the thread-specific values, though this is not how it is actually implemented. 
         * The thread-specific values are stored in the Thread object itself; when the thread
         * terminates, the thread-specific values can be garbage collected*/
        return connectionHolder.get();
      
    }
}
