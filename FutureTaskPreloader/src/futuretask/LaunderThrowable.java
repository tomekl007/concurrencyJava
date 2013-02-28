package futuretask;

/**
 * StaticUtilities
 *
 * @author Brian Goetz and Tim Peierls
 */
public class LaunderThrowable {

    /**
     * Coerce an unchecked Throwable to a RuntimeException
     * <p/>
     * If the Throwable is an Error, throw it; if it is a
     * RuntimeException return it, otherwise throw IllegalStateException
     */
    
    /*  We must handle each of these 
 * cases separately, but we will use the launderThrowable utility method in Listing 5.13 to encapsulate some of the messier
 * exception-handling logic. Before calling launderThrowable, Preloader tests for the known checked exceptions and rethrows
 * them. That leaves only unchecked exceptions, which Preloader handles by calling launderThrowable and throwing the result.
 * If the Throwable passed to launderThrowable is an Error, launderThrowable rethrows it directly; if it is not a RuntimeException
 * , it throws an IllegalStateException to indicate a logic error. That leaves only RuntimeException, which launderThrowable returns
 * to its caller, and which the caller generally rethrows. */
    public static RuntimeException launderThrowable(Throwable t) {
        if (t instanceof RuntimeException)
            return (RuntimeException) t;
        else if (t instanceof Error)
            throw (Error) t;
        else
            throw new IllegalStateException("Not unchecked", t);
    }
}
