package nonstandardcancelbyoverrideinterrupt;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * ReaderThread
 * <p/>
 * Encapsulating nonstandard cancellation in a Thread by overriding interrupt
 *
 * shows a technique for encapsulating nonstandard cancellation. 
 * ReaderThread manages a single socket connection, reading synchronously
 * from the socket and passing any data received to processBuffer. To facilitate
 * terminating a user connection or shutting down the server, ReaderThread
 * overrides interrupt to both deliver a standard interrupt and close the underlying socket;
 * thus interrupting a ReaderThread makes it stop what it is doing whether it is blocked in
 * read or in an interruptible blocking method.
 */
public class ReaderThread extends Thread {
    private static final int BUFSZ = 512;
    private final Socket socket;
    private final InputStream in;

    public ReaderThread(Socket socket) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
    }

    public void interrupt() {
        try {
            socket.close();
        } catch (IOException ignored) {
        } finally {
            super.interrupt();
        }
    }

    public void run() {
        try {
            byte[] buf = new byte[BUFSZ];
            while (true) {
                int count = in.read(buf);
                if (count < 0)
                    break;
                else if (count > 0)
                    processBuffer(buf, count);
            }
        } catch (IOException e) { /* Allow thread to exit */
        }
    }

    public void processBuffer(byte[] buf, int count) {
    }
}
