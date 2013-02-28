package executorwebserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * TaskExecutionWebServer
 * <p/>
 * Web server using a thread pool
 *
 * @author Brian Goetz and Tim Peierls
 */
public class TaskExecutionWebServer {
    private static final int NTHREADS = 10;
    private static final Executor exec
            = Executors.newFixedThreadPool(NTHREADS);// fixed-size thread pool creates threads as tasks 
    //are submitted, up to the maximum pool size, and then attempts to keep the pool size constant
    //(adding new threads if a thread dies due to an unexpected Exception)
    

    public static void main(String[] args) throws IOException {
       
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {
                public void run() {
                    handleRequest(connection);
                }
            };
            exec.execute(task);
        }
        
    }
    

    private static void handleRequest(Socket connection) {
        // request-handling logic here
    }
}
